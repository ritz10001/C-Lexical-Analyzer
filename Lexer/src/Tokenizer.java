import java.util.*;
import java.util.Map.Entry;
public class Tokenizer{

	private ArrayList<Token> tokens = new ArrayList<Token>();

	private String[] keywords = {
		    "auto", "double", "int", "struct",
		    "break", "else", "long", "switch",
		    "case", "enum", "register", "typedef",
		    "char", "extern", "return", "union",
		    "const", "float", "short", "unsigned",
		    "continue", "for", "signed", "void",
		    "default", "goto", "sizeof", "volatile",
		    "do", "if", "static", "while"
		};
	
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
	private int pos;
	private int length;
	private String subString;
	private String[] lines;
	private int lineNumber = 0;
	
	public Tokenizer(String input) {
		this.pos = 0;
		this.subString = "";
		this.lines = input.split("\n");
		for(int i = 0; i < this.lines.length; i++) {
			lines[i] = lines[i].trim();
		}
		this.length = this.lines[lineNumber].trim().length();
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
		for(String line : this.lines) {
			length = this.lines[lineNumber].trim().length();
			pos = 0;
			while(pos < length - 1) {
				this.ignoreWhiteSpace();
				if(pos >= length) {
					break;
				}
				char currentChar = line.charAt(pos);
				
				//preprocessing directives
				if(currentChar == '#') {
					String directive = new String("");
					boolean inc = true;
					while(line.charAt(pos) != '>' && pos < length) {
						if(line.charAt(pos) == '<') {
							if(!directive.substring(1).trim().equals("include")) {
								inc = false;
								break;
							}
						}
						directive += line.charAt(pos);
						this.advance();
					}
					if(line.charAt(pos) == '>') {
			            directive += line.charAt(pos);
			        }
					if(inc == false) {
						while((line.charAt(pos) != '>' && line.charAt(pos) != '\n') && pos < length) {
							this.advance();
						}
						continue;
					}
					tokens.add(new Token(TokenType.DIRECTIVE, directive));
					
					this.advance();
					continue;
				}			
				
				//check for symbols
				if(isSymbol(currentChar)) {
		            tokens.add(new Token(TokenType.SYMBOLS, String.valueOf(currentChar)));
		            this.advance();
		            continue;
		        }
				
				//check for double operators
				if(pos < length) {
					String twoCharacterOperator = "" + currentChar + peek();
					if(isOperator(twoCharacterOperator)) {
						tokens.add(new Token(TokenType.OPERATOR, twoCharacterOperator));
						this.advance();
						this.advance();
						continue;
					}
				}
				
				//check for single operators
				if(isOperator(String.valueOf(currentChar))){
					tokens.add(new Token(TokenType.OPERATOR, String.valueOf(currentChar)));
					this.advance();
					continue;
				}
				
				//Handle numbers
				if(isDigit(currentChar)) {
					String number = "";
					while((pos < length) && (isDigit(line.charAt(pos)) || line.charAt(pos) == '.')) {
						number += line.charAt(pos);
						this.advance();
					}
					tokens.add(new Token(TokenType.CONSTANT, number));
					continue;
				}
				
				//Handle letters/characters
				if (currentChar == '\'' && pos + 2 < length && line.charAt(pos + 2) == '\'') {
				    String character = line.substring(pos, pos + 3);
				    tokens.add(new Token(TokenType.CONSTANT, character));
				    this.advance();
				    this.advance();
				    this.advance();
				    continue;
				}
				
				//Handle Strings
				if(currentChar == '\"') {
					String string = "";
					string += currentChar;
					this.advance();
					while(pos < length && line.charAt(pos) != '\"') {
						string += line.charAt(pos);
						this.advance();
					}
				    if (pos < length) {
				        string += line.charAt(pos); 
				        this.advance();
				    }
				    tokens.add(new Token(TokenType.CONSTANT, string));
					continue;
				}
				
				subString += line.charAt(pos);

				if(pos < length - 1 && (peek() == ' ' || isOperator(String.valueOf(peek())) || isSymbol(peek()) || peek() == '\n')) {
					if(Arrays.asList(keywords).contains(subString)) {
						tokens.add(new Token(TokenType.KEYWORDS, subString));
					}
					else {
						tokens.add(new Token(TokenType.IDENTIFIER, subString));
					}
					subString = "";
				}
				this.advance();
			}
			lineNumber++;
			tokens.add(new Token(TokenType.EOL, "End of Line!"));
		}
		tokens.add(new Token(TokenType.EOF, "End of File!"));
		printTokens();
		
	}
	
	private void advance() {
		if(this.pos + 1 == length) {
			return;
		}
		this.pos++;
	}
	
	public void ignoreWhiteSpace() {
		while(lines[lineNumber].trim().charAt(pos) == ' ') {
			this.advance();
		}
	}
	
	public char peek() {
		if(pos + 1 < length) {
			return lines[lineNumber].trim().charAt(pos + 1);
		}
		return '\0';
	}
	
	public String getTokenTypeName(TokenType type) {
		switch(type) {
			case TokenType.IDENTIFIER:
				return "IDENTIFIER";
			case TokenType.OPERATOR:
				return "OPERATOR";
			case TokenType.CONSTANT:
				return "CONSTANT";
			case TokenType.KEYWORDS:
				return "KEYWORD";
			case TokenType.SYMBOLS:
				return "SYMBOL";
			case TokenType.EOL:
				return "EOL";
			case TokenType.EOF:
				return "EOF";
			case TokenType.DIRECTIVE:
				return "DIRECTIVE";
			default:
				return "UNDEFINED";
		}
	}
	
	public void printTokens() {
		System.out.println("Tokens generated by the Lexical Analyzer\n");
		for(Token token : tokens) {
			System.out.println("Type: " + getTokenTypeName(token.getTokenType()) + ", Value: " + token.getToken());
		}
	}
}