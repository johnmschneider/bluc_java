
### 1. There should be one obvious "way" to do things

- That "way" should be <u>**readable, safe, and fast**</u> -- in that order.
- If there is a legitimate reason to ignore this rule, it can be ignored. One must think hard about the pros/cons of violating this rule.

### 2. The language should be simple enough that you can learn it, and keep it all in your head.

### 3. The programmer is human, and humans make mistakes.

- Favor safety over speed and flexibility.
- Double-check code correctness, by default. This includes things such as array bounds-checking.

### 4. The programmer is human, and humans sometimes know better than the compiler

- Never remove the ability to run "unsafe" code -- code that can't be verified as safe by the compiler.
- Never perform "unsafe" operations by default -- the programmer must explicitly request to do so.
- The programmer is free to optimize as much as they need to for their critical sections of code.

### 5. It should be easy to write pretty-clean, pretty-fast, pretty-safe code.

- Favor reasonable and sane defaults for the language.
- Implement any language constructs or compiler magic that are necessary to achieve this goal.
- It may not be possible or even desirable  to achieve 100% cleanliness, safety, or speed for every use case. "Pretty good" is still good enough.


### 5. The language should not easily devolve into "symbol soup"


### 6. The compiler should be fast

- This may mean the language has slightly more verbose syntax.
- Syntaxes such as "fn" to specify we're starting a function, and "var" to start a variable declaration are reasonable tradeoffs.
- This should <u>**never**</u> degrade the language into "symbol soup". 