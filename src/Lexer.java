import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Lexer {
	public static void main(String[] args) throws FileNotFoundException {
		// Reader
		File file = new File("C:\\Users\\Ritvik Prakash\\eclipse-workspace\\Lexer\\src\\code.txt");
		Scanner myReader = new Scanner(file);
		String input = myReader.useDelimiter("\\A").next();
	    myReader.close();
	    
	    Tokenizer tokens = new Tokenizer(input);
	    tokens.Tokenize();
	    
	}

}
