import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * Project 1:  Textual Analysis of books, used in associaton with TextAnalysis.java.
 * @author Nick Langan CSC 2053-002
 * Date Created:  September 27, 2019
 * Date Modified:  September, 28-30, 2019
 */

public class Project1 {

	/**
	 * readFile:  Accepts a Scanner object (in this case, read from FileReader), iterates through file and adds words using regular expressions to List WordList.
	 * @param wordList
	 * @param wordsIn
	 */
	private static void readFile(List<String> wordList, 
			Scanner wordsIn) {

		while (wordsIn.hasNext())  { 
			wordsIn.useDelimiter("[^a-zA-Z']");
			String word = wordsIn.next();
			word=word.toLowerCase();
			//replace all leading apostrophes
			word = word.replaceAll("'+", "");
			//replace all trailing apostrophes
			word = word.replaceAll("'+$", "");
			if(word.equals(" ") || word.equals(""))
				continue;
			else
				wordList.add(word);
		}
	}


	public static void main(String[] args) throws FileNotFoundException {

		//Initial list to store word contents from file is created
		List<String> wordList = new LinkedList<>();

		// Set up file reading
		String fn;		
		fn = "tom-sawyer.txt"; //hard-coded to Tom Sawyer
		Scanner wordsIn = new Scanner(new FileReader(fn));
		//Read in file
		readFile(wordList, wordsIn);

		// Process file
		wordsIn = new Scanner(new FileReader(fn));
		wordsIn.close();
		Scanner scan = new Scanner(System.in);

		//Text Analysis eBook object is created, to perform text analysis operations
		TextAnalysis eBook = new TextAnalysis(wordList); 

		//Welcome message printed to user
		System.out.println("Welcome to the eBook text analyzer!  This program will provide a count for the top 10 letters, the top 10 words, the top 10 words compared"
				+ " to a stop list, and the top 10 longest words found in the eBook in question.");
		System.out.println();
		System.out.println("Press ENTER to continue!");
		scan.nextLine();
		System.out.println();

		//Letter Frequency: The top 10 most frequent letters in the text are provided using the getLetterCount method
		System.out.println("The top 10 most utilized letters in " + fn + " sorted greatest to least are:");
		System.out.println(eBook.getLetterCount());

		System.out.println();
		System.out.println("Press ENTER to continue!");
		scan.nextLine();

		//Word Frequency: The top 10 most frequent words in the text are provided using the getWordCount method
		System.out.println("The top 10 most utilized words in " + fn + " sorted greatest to least are:");
		System.out.println(eBook.getWordCount()); 

		System.out.println();
		System.out.println("Press ENTER to continue!");
		scan.nextLine();

		//Word Frequency with Stop List:  The top 10 most frequent words, with stop list words excluded, are provided using the getWordStopList method
		System.out.println("The top 10 most utilized words, with stop list words excluded, in " + fn + " sorted greatest to least are:");
		System.out.println(eBook.getWordStopList()); 

		System.out.println();
		System.out.println("Press ENTER to continue!");
		scan.nextLine();

		//Wild Card: The top 10 longest words, with length of word, are provided using the getLongWords method
		System.out.println("The top 10 longest words in " + fn + " sorted greatest to least are:");
		System.out.println(eBook.getLongWords()); 

		System.out.println();
		System.out.println("Thank you for using my program!");

		scan.close();

	}


}
