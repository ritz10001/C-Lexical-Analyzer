import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Lexer {
//https://github.com/rgwohlbold/interpreter-ice/blob/master/src/lexer/Lexer.java
	public static void main(String[] args) throws FileNotFoundException {
		// Reader
		File file = new File("C:\\Users\\Ritvik Prakash\\eclipse-workspace\\Lexer\\src\\extra.txt");
		Scanner myReader = new Scanner(file);
		String input = myReader.useDelimiter("\\A").next();
		System.out.println(input);
	    myReader.close();
	    
	    Tokenizer tokens = new Tokenizer(input);
	    tokens.Tokenize();
	    
	}

}
