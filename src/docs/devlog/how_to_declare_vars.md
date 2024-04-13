
## How should we design variable declarations?

There are several qualities that we want Bluc variable declarations to have.

1. **The compiler should be able to quickly and efficiently parse variable declarations.**

2. **The way to declare variables should be consistent across different scopes.** For example, classes should declare variables with the same syntax as functions and lambdas.

3. **The syntax should be easy to read and understand.** As a language, Bluc is for the programmer's benefit, not the computer. The computer would be equally as happy executing machine code.

For compiler speed, the syntax should be as clear as possible. Ambiguity is our enemy. If a statement could potentially mean multiple things, we'll have to parse even more tokens to make out what it really is. Extra tokens to reduce ambiguity may be worthwhile.

Conversely, the required amount of tokens should be kept to a minimum. We may be parsing projects with millions of lines of code, and it will add up.

The following is an example of a consideration we made, but ultimately steered away from.

Let's start with a language that has a syntax similar (though not identical) to C++. Say we only have to type one token -- `var` -- to know a line is a variable declaration.

```rust
fn void example()
{
    var i32 my_int = 22;
    var Long_Type_Name to_be_used_later;
}
```

This is alright, but `var` followed by another token indicating a var -- the type -- seems a bit monotonous. As a stylistic choice, let's try moving the type declarations to the right, like so:

```rust
fn void example()
{
    var my_int i32 = 22;
    var to_be_used_later Long_Type_Name;
}
```

There's now some space for your brain to distinguish "var" from the type.

However, this doesn't look quite right. Let's add a colon after the variable name, to separate the type from the name. As a sacrifice to satisfy our "minimum tokens" rule, let's remove the requirement of the `var` keyword:

```rust
fn void example()
{
    my_int : i32 = 22;
    to_be_used_later : Long_Type_Name;
}
```

This looks like a nice, modern syntax that all of the cool languages are using. Continuing down this path, let's remove the requirement of the `fn` keyword.

```rust
void example()
{
    my_int : i32 = 22;
    to_be_used_later : Long_Type_Name;
}
```

What happens if we add class fields into the mix?

```cpp
class Example()
{
private:
    my_field : i32;

public:
    void example()
    {
        my_int : i32 = 22;
        to_be_used_later : Long_Type_Name;
    }
}
```

It's possible to have this syntax, and it looks alright. However, when parsing a class, the compiler now has an ambiguity.

When we're parsing an item inside of the class block, are we on a field name, or the return type of a function? To be certain, we'll have to look to the next token and check if it matches `:`.

We seem to have reached a minima to the amount of tokens needed to declare a variable. But it's just a local minima. There's another solution I'd like to propose:

## In defense of C++-style syntax for variable declaration. Sort of.
Let's instead strip the `var` keyword away from our c++-like syntax, such that our syntax perfectly mirrors c++:

```cpp
void example()
{
    i32 my_int = 22;
    LongTypeName to_be_used_later;
}
```

This is now one token less than even the `name : type` syntax. What happens if we add the function into a class with a field?

```cpp
class Example()
{
private:
    i32 my_field;

public:
    void example()
    {
        i32 my_int = 22;
        Long_Type_Name to_be_used_later;
    }
}
```

We've run into another ambiguity. Now the parser isn't sure if we're on a field or a function until it reaches the `()` (or lack thereof).

It's possible to use this syntax, as C++ does. But what if, instead of changing the variable syntax, we change the function syntax -- *slightly*?

```cpp
class Example()
{
private:
    i32 my_field;

public:
    fn void example()
    {
        i32 my_int = 22;
        Long_Type_Name to_be_used_later;
    }
}
```

> *Our language is now sufficiently different that we can't effectively use the Rust OR C++ markdown code highlighters. We've compromised with the C++ highlighter, as it highlights more keywords.*

We now have one extra token we have to parse per function. However, we've achieved far more that we've lost.

We now have:
- **A unified syntax for variable declarations in classes and functions (and other scopes)**
- **An unambiguous syntax. We know what we're parsing from the first token.**
- **Compiler speed due to an unambiguous syntax**
- **Easy-to-read, and easy-to-understand code.** The programmer doesn't have to jump through hoops to declare a variable.

Finally, I would wager that we'll be declaring far more variables, overall, than functions. Supposing this is true, we will therefore achieve the maximum benefit with the syntax mentioned above.