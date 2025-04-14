import java.util.*;
import java.util.regex.*;


// Main class
public class Main {
    public static void main(String[] args) {
        // Sample source code input (could be read from a file with a unique .myLang extension)
        String sourceCode = "decimal pi = 3.14159;\n" +
                "char symbol = 'x';\n" +
                "int counter = 42;\n" +
                "boolean isactive = false;\n" +
                "// Decrement counter\n" +
                "counter = counter - 1;\n" +
                "result = counter ^ 2;\n" +
                "/* This is a multi-line\n" +
                "   comment for testing */";

        // 1. Lexical Analysis
        LexicalAnalyzer lexer = new LexicalAnalyzer();
        List<Token> tokens = lexer.tokenize(sourceCode);
        System.out.println("Total tokens: " + tokens.size());
        for (Token token : tokens) {
            System.out.println(token);
        }

        // 2. Symbol Table: Add identifiers found (dummy example: assuming type "int" for identifiers)
        SymbolTable symTable = new SymbolTable();
        for (Token token : tokens) {
            if (token.getType() == TokenType.IDENTIFIER || token.getType() == TokenType.CHAR_LITERAL) {
                // For this demonstration, we assign a dummy type ("int") and "global" scope.
                symTable.addSymbol(token.getValue(), "int", "global");
            }
        }
        symTable.printTable();

        // 3. Regular Expression, NFA, and DFA demonstration
        // For example, consider a dummy regex that could represent a valid identifier pattern.
        String regexPattern = "[a-z]+";
        Regex regex = new Regex(regexPattern);
        NFA nfa = new NFA(regex);
        System.out.println("\nNFA created with total states: " + nfa.getTotalStates());
        DFA dfa = nfa.toDFA();
        dfa.printTransitionTable();

        // 4. Error Handler demonstration (if an error were detected during analysis)
        // Here we simply simulate an error reporting.
        ErrorHandler.reportError("Unexpected token encountered", 3);
    }
}

// Token and Lexical Analyzer

// Token types for our simple language
enum TokenType {
    KEYWORD, IDENTIFIER, NUMBER, OPERATOR, COMMENT, STRING, CHAR_LITERAL, UNKNOWN
}

// Token class to hold information about individual tokens
class Token {
    private TokenType type;
    private String value;
    private int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "Line " + line + ": [" + type + "] " + value;
    }
}

// A simple lexical analyzer that tokenizes the source code.
class LexicalAnalyzer {
    // Updated token patterns to include char literals (e.g., 'x').
    private final String tokenPatterns =
            "(\\bint\\b|\\bboolean\\b|\\bchar\\b|\\bdecimal\\b)"   // Keywords
                    + "|('[a-z]')"                                          // Character literal (e.g., 'x')
                    + "|([a-z]+)"                                           // Identifiers (only lowercase letters)
                    + "|([0-9]+(\\.[0-9]{1,5})?)"                            // Numbers (integer or decimal up to 5 decimal places)
                    + "|([+\\-*/%\\^=;()])";                                 // Operators and punctuation

    public List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();

        // Remove multi-line comments first
        source = source.replaceAll("/\\*.*?\\*/", "");
        // Remove single-line comments
        source = source.replaceAll("//.*", "");

        Scanner scanner = new Scanner(source);
        int lineNumber = 1;
        Pattern pattern = Pattern.compile(tokenPatterns);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String tokenValue = matcher.group().trim();
                if (!tokenValue.isEmpty()) {
                    TokenType type = determineTokenType(tokenValue);
                    tokens.add(new Token(type, tokenValue, lineNumber));
                }
            }
            lineNumber++;
        }
        scanner.close();
        return tokens;
    }

    // Determine the token type based on its value.
    private TokenType determineTokenType(String token) {
        // Keywords
        if (token.equals("int") || token.equals("boolean") ||
                token.equals("char") || token.equals("decimal")) {
            return TokenType.KEYWORD;
        }
        // Character literal: matches a single lowercase letter inside single quotes.
        if (token.matches("'[a-z]'")) {
            return TokenType.CHAR_LITERAL;
        }
        // Identifier: only lowercase letters.
        if (token.matches("[a-z]+")) {
            return TokenType.IDENTIFIER;
        }
        // Number: integer or decimal (up to five decimals)
        if (token.matches("[0-9]+(\\.[0-9]{1,5})?")) {
            return TokenType.NUMBER;
        }
        // Operator or punctuation.
        if (token.matches("[+\\-*/%\\^=;()]")) {
            return TokenType.OPERATOR;
        }
        return TokenType.UNKNOWN;
    }
}

// Symbol Table

// A simple symbol class to represent entries in the symbol table.
class Symbol {
    private String name;
    private String type;
    private String scope; // "global" or "local"

    public Symbol(String name, String type, String scope) {
        this.name = name;
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Type: " + type +
                ", Scope: " + scope ;
    }
}

// A simple symbol table implemented with a HashMap.
class SymbolTable {
    private Map<String, Symbol> table;

    public SymbolTable() {
        table = new HashMap<>();
    }

    // Adds a symbol if it does not already exist.
    public void addSymbol(String name, String type, String scope) {
        if (!table.containsKey(name)) {
            table.put(name, new Symbol(name, type, scope));
        }
    }

    public Symbol getSymbol(String name) {
        return table.get(name);
    }

    public void printTable() {
        System.out.println("\nSymbol Table:");
        for (Symbol sym : table.values()) {
            System.out.println(sym);
        }
    }
}

// Error Handler

class ErrorHandler {
    public static void reportError(String message, int line) {
        System.err.println("Error on line " + line + ": " + message);
    }
}

// Regular Expression, NFA, and DFA

// A simple wrapper class for a regular expression.
class Regex {
    private String pattern;

    public Regex(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

// A dummy implementation of an NFA built from a regular expression.
class NFA {
    private List<String> states;
    private Map<String, Map<Character, String>> transitions;

    public NFA(Regex regex) {
        states = new ArrayList<>();
        transitions = new HashMap<>();

        // Dummy construction: create one state per character (plus one final state)
        for (int i = 0; i <= regex.getPattern().length(); i++) {
            String state = "q" + i;
            states.add(state);
            transitions.put(state, new HashMap<>());
        }
        // Create dummy transitions: for each character in the pattern, add a transition.
        for (int i = 0; i < regex.getPattern().length(); i++) {
            char symbol = regex.getPattern().charAt(i);
            transitions.get("q" + i).put(symbol, "q" + (i + 1));
        }
    }

    // Returns the total number of states.
    public int getTotalStates() {
        return states.size();
    }

    // Dummy conversion from NFA to DFA.
    public DFA toDFA() {
        // For this demonstration, we simply reuse the NFA's states and transitions.
        return new DFA(states, transitions);
    }
}

// A dummy DFA class that holds states and a transition table.
class DFA {
    private List<String> states;
    private Map<String, Map<Character, String>> transitions;

    public DFA(List<String> states, Map<String, Map<Character, String>> transitions) {
        this.states = states;
        this.transitions = transitions;
    }

    // Displays a dummy transition state table.
    public void printTransitionTable() {
        System.out.println("\nDFA Transition Table:");
        for (String state : states) {
            System.out.print(state + " -> ");
            Map<Character, String> trans = transitions.get(state);
            if (trans.isEmpty()) {
                System.out.print("No transitions");
            } else {
                for (Map.Entry<Character, String> entry : trans.entrySet()) {
                    System.out.print("[" + entry.getKey() + " : " + entry.getValue() + "] ");
                }
            }
            System.out.println();
        }
    }
}
