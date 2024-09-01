import java.util.*;
public class Tokenizer{

	private Map<TokenType, ArrayList<String>> tokenMap = new HashMap<>();

	private String[] keywords = {"int", "float", "char", "void",
		    "if", "else", "switch", "case", "default",
		    "for", "while", "do", "goto", "continue", "break", "return", "typedef",
		    "struct", "auto", "union", "signed", "unsigned", "goto", "static"};
	
	
	private String[] operators = {
		    "+", "-", "*", "/", "%", "++", "--",    // Arithmetic
		    "==", "!=", ">", "<", ">=", "<=",       // Relational
		    "&&", "||", "!",                        // Logical
		    "&", "|", "^", "~", "<<", ">>",         // Bitwise
		    "=", "+=", "-=", "*=", "/=", "%=",      // Assignment
		    "&=", "|=", "^=", "<<=", ">>=",         // Assignment (cont.)
		    "?", ":",                               // Ternary
		    "sizeof", "&", "*", ".", "->",          // Others
		    ","};								    // Others (cont.)
	
	private String[] symbols = {"(", ")", "{", "}", "[","]", ";"};
	private String input;
	private int pos;
	private int length;
	private String subString;
	
	public Tokenizer(String input) {
		this.input = input;
		this.pos = 0;
		this.subString = "";
		this.length = this.input.length();
		for(TokenType type : TokenType.values()) {
			tokenMap.put(type, new ArrayList<String>());
		}
		System.out.println(tokenMap);
	}
	
	private boolean isOperator(String ch) {
		return Arrays.asList(operators).contains(ch);
	}
	
	private boolean isSymbol(char ch) {
		return Arrays.asList(symbols).contains(String.valueOf(ch));
	}
	
	private boolean isDigit(char ch) {
		return Character.isDigit(ch);
	}
	
	public void Tokenize() {
		while(pos < length) {
			this.ignoreWhiteSpace();
			if(this.pos == length) {
				break;
			}
			char currentChar = input.charAt(pos);
			System.out.println("current symbol " + currentChar);
			
			//preprocessing directives
			if(currentChar == '#') {
				String directive = "";
				while(input.charAt(pos) != '>' && pos < length) {
					directive += input.charAt(pos);
					System.out.println("yes");
					this.advance();
				}
				if(input.charAt(pos) == '>') {
					directive += input.charAt(pos);
				}
				tokenMap.get(TokenType.DIRECTIVE).add(directive);
				continue;
			}
			
			
			//check for symbols
			if(isSymbol(currentChar)) {
				System.out.println("its a symbol");
	            tokenMap.get(TokenType.SYMBOLS).add(String.valueOf(currentChar));
	            this.advance();
	            System.out.println(tokenMap);
	            continue;
	        }
			
			//check for double operators
			if(pos < length) {
				String twoCharacterOperator = "" + currentChar + peek();
				if(isOperator(twoCharacterOperator)) {
					tokenMap.get(TokenType.OPERATOR).add(twoCharacterOperator);
					this.advance();
					this.advance();
					continue;
				}
				System.out.println(tokenMap);
			}
			
			//check for single operators
			if(isOperator(String.valueOf(currentChar))){
				System.out.println("Its an operator");
				tokenMap.get(TokenType.OPERATOR).add(String.valueOf(currentChar));
				this.advance();
				System.out.println(tokenMap);
				continue;
			}
			
			//Handle numbers
			if(isDigit(currentChar)) {
				String number = "";
				while((pos < length) && (isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
					number += input.charAt(pos);
					this.advance();
				}
				tokenMap.get(TokenType.CONSTANT).add(number);
				continue;
			}
			
			//check for keywords and identifiers
			subString += input.charAt(pos);
			System.out.println(subString);
			char nextLetter = peek();
			if(peek() == ' ' || peek() == '\n') {
				
				this.ignoreWhiteSpace();
			}
			if(nextLetter == ' ' || isOperator(String.valueOf(nextLetter)) || isSymbol(nextLetter)) {
				System.out.println(pos);
				if(Arrays.asList(keywords).contains(subString)) {
					tokenMap.get(TokenType.KEYWORDS).add(subString);
					subString = "";
				}
				else {
					tokenMap.get(TokenType.IDENTIFIER).add(subString);
					subString = "";
				}
			}
			System.out.println("EMPTY!");
			System.out.println(tokenMap);
			
			this.advance();
		}
	}
	
	private void advance() {
		if(this.pos + 1 == length) {
			return;
		}
		this.pos++;
	}
	
	public void ignoreWhiteSpace() {
		while(input.charAt(pos) == ' ') {
//			System.out.println("whitespace!");
			this.advance();
		}
	}
	
	public char peek() {
		return input.charAt(pos+1);
	}
}
