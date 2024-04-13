
# Memory management model

One of the goals of Bluc is to have a readable, safe, *and* fast memory management model.

Pointers are fast, but can be dangerous.

The Rust borrow checker is fast, and safe, but the compiler speed suffers for this. Rust's borrowing rules are also not the easiest to work with.

What if there's a memory system which is fast, safe, *and* has limited impact on compiler performance?

## Returning a pointer from a function/method, the C way

Assume we have a function, `add`, that returns a pointer to an integer. Said integer is the result of adding two numbers together, and the result is stored on the heap:

```cpp
fn int* add(int a, int b)
{
    int* result = malloc(int);

    *result = a + b;
    
    return result;
}
```

> *In actuality, this is an impractical use of a pointer*

What's meant by the return statement? There are a few implications of ending the function:
1. We want to return some data.
2. We're done using `int *result`.
3. We (usually) want the calling function to be responsible for managing and freeing this pointer.

These implications come with complexities:
1. Was a pointer to this variable stored anywhere else?
2. How do we make sure that the calling function frees the pointer appropriately?
3. What if the user *didn't* actually want to lose ownership of *result? How can we implement a "Sanity check" to make sure this is what the user intended?

## The `transfer` keyword
Suppose that we have the same task of adding two integers together, and returning the result on an int allocated on the heap:

```cpp
fn int* add(int a, int b)
{
    var result_ptr = malloc(int);

    *result_ptr = a + b;
    
    #// This will explicitly transfer ownership of result to the calling function, and return the pointer
    transfer result_ptr;
}
```

> - *Here, we're using the type-inferencing form of the var keyword. It's still an int\*.*
>
> - *This is, again, a contrived example, and an impractical use of a pointer.*
>
> - Finally, we're using #// so that the C++ code formatter in markdown detects the line as a comment. In actuality, comment in Bluc only need to start with #

The `transfer` keyword indicates, explicitly, that we're giving up ownership of this pointer.

It also tells the compiler to perform all safety/sanity checks on this pointer. We won't be able to assign it to any fields, or otherwise assign a reference to it, except for the initial assignment and returning it. 

We also *must* return the pointer, or a struct which references the pointer.

## The optional `reinit` parameter of the `transfer` keyword
Suppose we're writing the Bluc compiler, and we're trying to get the result of parsing a file.

We want to pass the result of the parsing via a pointer, because it will be faster than copying-by-value the entire AST.
  
> ### Some background info:
>
> The Bluc Parser class is subdivided across several modularized classes. We have a statement parser, several statement "sub" parsers, an expression parser, and several expression "sub" parsers. Think of them as just separate namespaces of the parser.
>
> Each sub-parser requires a reference to the main parser, which stores state and AST information.
>
> ### Why we can't just use a namespace or a singleton
>
> At first glance, it seems like the Parser class might be an ideal candidate to use either a namespace or a singleton -- after all, how many parsers do we need for one compiler?
>
> However, upon further inspection, we don't exactly want a singleton -- what if we upgrade the parser to use multiple threads, and for thread-safety, we want one parser per thread? We also don't want a namespace, since different parsers may need to store different states.
>
> Conversely, we don't want to create a new parser for every single file. There would be a lot of overhead from the memory allocations and de-allocations.

As one potential solution, let's try our `transfer` syntax as described earlier:

```cpp
class Parser()
{
private:
    #// AstNode is a tree, with parent and child nodes.
    Ast_Node*            ast_root;
    Statement_Parser*    stmt_parser;

    #// Other fields/functions here

public:
    this()
    {
        #// assume ast_root and stmt_parser are properly initialized
    }

    fn void addChildNodeToRoot()
    {
        #//... implementation here ...
    }

    fn Ast_Node* parse()
    {
        for(each token in lexed_tokens)
        {
            this.stmt_parser.parse();
        }

        #// Compiler error!! :(
        transfer ast_root;
    }

    #// Other fields/functions here
}
```

When we try to `transfer` astRoot in the parse function, we get a compiler error. We can't transfer astRoot because the class still has a reference to it. This would also break data hiding/encapsulation. If we can just modify the private variable from another part of the program (via a pointer), it's no longer private.

What are we actually intending to do with the statement `transfer ast_root`?

1. We want the calling function to see the result of the function, which is stored in ast_root.
2. We want to return the result as quickly as possible (no copy-by-value)
3. We want the calling function to take ownership of the data.
4. We want the calling function to be able to freely modify the result, **without affecting the workings of this Parser instance**.

What if we added an extended form of our `transfer` keyword which allowed this functionality? It will give ownership of the `ast_root` pointer to the calling function, and re-initialize our `ast_root` in the Parser instance to a new memory location.

```cpp
class Parser()
{
private:
    #// AstNode is a tree, with parent and child nodes.
    Ast_Node*            ast_root;
    Statement_Parser*    stmt_parser;

    ...
    #// Other fields/functions here
    ...

public:
    this()
    {
        ...
        #// assume ast_root and stmt_parser are properly initialized
        ...
    }
    
    fn Ast_Node* parse()
    {
        for(each line in lexed_lines)
        {
            this.stmt_parser.parse();
        }

        transfer reinit ast_root;
    }

    ...
    #// Other fields/functions here
    ...
}
```

This is an acceptable syntax! We now have:
1. A way to return the result
2. We return the value as quickly as *reasonably* possible
3. We are giving ownership of the pointer to the calling function
4. We maintain the principles of data encapsulation/hiding -- nobody is able to modify the `ast_root` but this instance.

One small question -- *how*, exactly, are we supposed to re-initialize `ast_root`? We could, theoretically, be using *any* memory allocator.

### Reinit, the sequel

Let's add a way for the compiler to know *how* to reinitialize the pointer.

```cpp
class Parser()
{
private:
    # AstNode is a tree, with parent and child nodes.
    Ast_Node*            ast_root;
    Statement_Parser*    stmt_parser;

    ...
    #// Other fields/functions here
    ...

public:
    this()
    {
        ...
        #// assume ast_root and stmt_parser are properly initialized
        ...
    }

    #// Tell the compiler how to re-initialize our transferrable pointers.
    reinit
    {
        ast_root:
            value = malloc(Ast_Node);
            break;
    }

    fn Ast_Node* parse()
    {
        for(each line in lexed_lines)
        {
            this.stmt_parser.parse();
        }

        transfer reinit ast_root;
    }

    ...
    #// Other fields/functions here
    ...
}
```

This may look like a `switch` statement, and indeed it's syntactically similar to one. You can add several cases which fall-through to the same allocator. However, *which* re-initializer we need to use is determined at **compile time** -- there are **no jump tables** or actual switches.

First, the result is returned to the calling function -- and potentially stored in a variable.

Second, before the next statement is executed, the `reinit` for `ast_root` will run. The function can keep its reference to our pointer, and modify it as much as it wants -- that's no longer our memory.