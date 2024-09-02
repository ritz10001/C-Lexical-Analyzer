
public class Token {
	private TokenType tokenType;
	private String token;
	
	public Token(TokenType tokenType, String token) {
		this.tokenType = tokenType;
		this.token = token;
	}
	
	public TokenType getTokenType() {
		return tokenType;
	}
	
	public String getToken() {
		return token;
	}
}
