# 🧠 Language Analyzer in Java

This Java-based project demonstrates the fundamental components of a compiler's front-end. It performs **lexical analysis**, manages a **symbol table**, simulates **regex-based finite automata (NFA/DFA)**, and includes basic **error handling**.

---

## 📌 Features

### 1. Lexical Analyzer
Parses input source code into a list of **tokens**:

- **Keywords:** `int`, `boolean`, `char`, `decimal`
- **Identifiers:** Lowercase letter sequences (e.g., `counter`)
- **Literals:** 
  - Numbers: integers and floating-points up to 5 decimals
  - Characters: `'x'`
- **Operators & Symbols:** `+`, `-`, `*`, `/`, `%`, `^`, `=`, `;`, `()`
- **Comments:**
  - Single-line: `//`
  - Multi-line: `/* ... */`

### 2. Symbol Table
Stores identifiers using a `HashMap` with dummy type inference (`int` for all):

| Field     | Description        |
|-----------|--------------------|
| `name`    | Identifier name    |
| `type`    | Data type (e.g. int) |
| `scope`   | Current scope (`global`) |

### 3. Regex to NFA/DFA
Simulates regular expression parsing and automata construction.

- Example regex: `[a-z]+`
- NFA constructed per character
- Transition table printed for DFA

### 4. Error Handler
Basic utility to report token/grammar issues with line numbers.

```java
ErrorHandler.reportError("Unexpected token encountered", 3);
```

---

## 💡 Sample Input

```java
decimal pi = 3.14159;
char symbol = 'x';
int counter = 42;
boolean isactive = false;
// Decrement counter
counter = counter - 1;
result = counter ^ 2;
/* This is a multi-line
   comment for testing */
```

---

## 📤 Output Samples

### ✅ Token Stream
```
Line 1: [KEYWORD] decimal
Line 1: [IDENTIFIER] pi
Line 1: [OPERATOR] =
Line 1: [NUMBER] 3.14159
...
```

### 📘 Symbol Table
```
Name: pi, Type: int, Scope: global
Name: symbol, Type: int, Scope: global
...
```

### ⚙ DFA Transition Table
```
q0 -> [a : q1]
q1 -> [z : q2]
...
```

---

## 🧪 Run Instructions

### 🖥 Requirements
- Java 8 or later

### 🔧 Compile & Run

```bash
javac Main.java
java Main
```

---

## 🚀 Future Improvements

- Real DFA state construction from NFA
- Syntax parsing & AST generation
- Semantic analysis and type-checking
- Function declarations and local scopes

---

## 📜 License
This project is released under the [MIT License](LICENSE).

---

## 👨‍💻 Author
*Your Name Here* - [GitHub Profile](https://github.com/your-username)
