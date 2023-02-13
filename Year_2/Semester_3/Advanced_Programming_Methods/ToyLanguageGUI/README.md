
# Toy Language

The toy language is a simplified programming language designed for educational purposes with a focus on simplicity and ease of use. It includes basic programming concepts such as types, values, statements, expressions, program states, but it is also capable of running concurrent programs using different synchronization mechanisms such as locks, semaphores and count down latches (or barriers).

## Used

- Java OOP concepts
- Model View Controller Architecture
- JavaFX framework
- Concurrency and multithreading concepts

## Demo
![App Preview](https://github.com/razvanpetruta/UniversityProjects/blob/main/Year_2/Semester_3/Advanced_Programming_Methods/ToyLanguageGUI/Preview.gif)

## Types

- BoolType
- IntType
- RefType
- StringType
## Values

- BoolValue
- IntValue
- RefValue
- StringValue
## Statements

- AcquireStatement
    ```java
    acquire(varName)
    ```
- AssignStatement
    ```java
    assign(varName, expression)
    ```
- AwaitStatement
    ```java
    await(varName)
    ```
- CloseReadFile
    ```java
    closeReadFile(expression)
    ```
- CompoundStatement
    ```java
    (firstStatement, secondStatement)
    ```
- ConditionalAssignmentStatement
    ```java
    varName = expression1 ? expression2 : expression3
    ```
- CountDownStatement
    ```java
    countDown(varName)
    ```
- CreateSemaphoreStatement
    ```java
    createSemaphore(varName, expression)
    ```
- ForkStatement
    ```java
    fork(statement)
    ```
- ForStatement
    ```java
    for(varName = expression1; varName < expression2; varName = expression3) {
        statement
    }
    ```
- IfStatement
    ```java
    if(expression) {
        thenStatement
    } else {
        elseStatement
    }
    ```
- LockStatement
    ```java
    lock(varName)
    ```
- NewLatchStatement
    ```java
    newLatch(varName, expression)
    ```
- NewLockStatement
    ```java
    newLock(varName)
    ```
- NewStatement 
    ```java
    new(varName, expression)
    ```
- NopStatement
    ```java
    ```
- OpenReadFile
    ```java
    openReadFile(expression)
    ```
- PrintStatement
    ```java
    print(expression)
    ```
- ReadFile 
    ```java
    readFile(expression, varName)
    ```
- ReleaseStatement
    ```java
    release(varName)
    ```
- RepeatUntilStatement
    ```java
    Repeat {
        statement
    } until (expression)
    ```
- SleepStatement
    ```java
    sleep(integer)
    ```
- SwitchStatement
    ```java
    switch(mainExpression)
        (case (expression1): statement1)
        (case (expression2): statement2)
        (default: defaultStatement)
    ```
- UnlockStatement
    ```java
    unlock(varName)
    ```
- VariableDeclarationStatement
    ```java
    varName Type
    ```
- WaitStatement
    ```java
    wait(integer)
    ```
- WhileStatement
    ```java
    while(expression) {
        statement
    }
    ```
- WriteHeapStatement
    ```java
    writeHeap(varName, expression)
    ```

## Statements

- AcquireStatement
    ```java
    acquire(varName)
    ```
- AssignStatement
    ```java
    assign(varName, expression)
    ```
- AwaitStatement
    ```java
    await(varName)
    ```
- CloseReadFile
    ```java
    closeReadFile(expression)
    ```
- CompoundStatement
    ```java
    (firstStatement, secondStatement)
    ```
- ConditionalAssignmentStatement
    ```java
    varName = expression1 ? expression2 : expression3
    ```
- CountDownStatement
    ```java
    countDown(varName)
    ```
- CreateSemaphoreStatement
    ```java
    createSemaphore(varName, expression)
    ```
- ForkStatement
    ```java
    fork(statement)
    ```
- ForStatement
    ```java
    for(varName = expression1; varName < expression2; varName = expression3) {
        statement
    }
    ```
- IfStatement
    ```java
    if(expression) {
        thenStatement
    } else {
        elseStatement
    }
    ```
- LockStatement
    ```java
    lock(varName)
    ```
- NewLatchStatement
    ```java
    newLatch(varName, expression)
    ```
- NewLockStatement
    ```java
    newLock(varName)
    ```
- NewStatement 
    ```java
    new(varName, expression)
    ```
- NopStatement
    ```java
    ```
- OpenReadFile
    ```java
    openReadFile(expression)
    ```
- PrintStatement
    ```java
    print(expression)
    ```
- ReadFile 
    ```java
    readFile(expression, varName)
    ```
- ReleaseStatement
    ```java
    release(varName)
    ```
- RepeatUntilStatement
    ```java
    Repeat {
        statement
    } until (expression)
    ```
- SleepStatement
    ```java
    sleep(integer)
    ```
- SwitchStatement
    ```java
    switch(mainExpression)
        (case (expression1): statement1)
        (case (expression2): statement2)
        (default: defaultStatement)
    ```
- UnlockStatement
    ```java
    unlock(varName)
    ```
- VariableDeclarationStatement
    ```java
    varName Type
    ```
- WaitStatement
    ```java
    wait(integer)
    ```
- WhileStatement
    ```java
    while(expression) {
        statement
    }
    ```
- WriteHeapStatement
    ```java
    writeHeap(varName, expression)
    ```

## Expressions

- ArithmeticExpression
    ```java
    expression1 [+|-|*|/] expression2
    ```
- LogicExpression
    ```java
    expression1 [or|and] expression2
    ```
- MULExpression
    ```java
    (e1 * e2) - (e1 + e2)
    ```
- NotExpression
    ```java
    !expression
    ```
- ReadHeapExpression
    ```java
    readHeap(expression)
    ```
- RelationalExpression
    ```java
    expression1 [<|<=|==|!=|>|>=] expression2
    ```
- ValueExpression
    ```java
    value
    ```
- VariableExpression
    ```java
    value of varName
    ```
## ProgramState

- ExecutionStack: a stack of statements to execute the current ProgramState
- SymbolTable: a table which keeps the variable names and the associated values
- Output: a list which keeps all the messages printed by the toy ProgramState
- FileTable: manages the files opened in our toy language program
- HeapTable: manage the variables allocated on the heap
- ProgramIDs: the IDs of the currently running programs
- LockTable: a table which maps the lock variables with the program's ID that has the lock
- LatchTable: a table which maps the latch variable with the count number
- SemaphoreTable: a table witch maps the semaphore variable with the maximum number of threads that can acquire the semaphore and the list of thread IDs
## UML Diagram
[see diagram](https://github.com/razvanpetruta/UniversityProjects/blob/main/Year_2/Semester_3/Advanced_Programming_Methods/ToyLanguageGUI/UMLDiagram.png)