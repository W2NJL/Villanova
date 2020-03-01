import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * TextAnalysis.java provides 4 methods to be used with performing textual analysis, including:
 * - countLetter - Counting the top 10 occurrences of each a-z character in a text
 * - wordCount - Counting the top 10 occurrences of each word in a text
 * - wordStopList - Counting the top 10 occurrences of each word in a text, EXCLUDING all words contained in the designated stop-list.txt
 * - longWords - Counting the top 10 longest words found in a text, providing the words and their length
 * @author Nick Langan CSC 2053-002
 * Date Created:  September 27, 2019
 * Date Modified:  September, 28-30, 2019
 */
public class TextAnalysis {

	//Instance variables created
	protected HashMap<String, Integer> letterMap = new HashMap<>();
	protected List<String> wordList = new LinkedList<>();
	protected Set<String> keys; 	

	/**TextAnalysis constructor class.  Accepts a list of type String.  
	 * @param wordListed
	 */
	TextAnalysis(List <String> wordListed){

		this.wordList = wordListed; 
	}

	/**readFile:  Accepts a Scanner object (read from FileReader), iterates through file and adds words using regular expressions to List WordList.  
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

	/**
	 * clearHash:  Erases the contents of the HashMap letterMap instance variable.  Used in accordance with each of the four public methods so the program starts anew with a fresh HashMap.  
	 */
	private void clearHash() {
		letterMap = new HashMap<>();
	}

	/**
	 * keySet:  Creates a set of keys for the HashMap letterMap.  Used in accordance with each of the four public methods so the program can ultimately return a set of keys and values to the user.  
	 */
	private void keySet() {
		keys = letterMap.keySet();
	}

	/**
	 * sortByValue:  Takes the elements of the letterMap HashMap and inserts them into a LinkedList.
	 * Use the Collections.sort function to organize the LinkedList in order using Comparator to compare two Map Entry objects from the original HashMap.
	 * Places the data from the Linked List back into a HashMap but only includes the top 10 results in greatest to least order.
	 * Reassigns the temporary HashMap back into the letterMap instance variable.  
	 * Used with each of the four public methods.  
	 * Inspiration for this method was found at:  https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
	 */
	private void sortByValue() 
	{ 
		// Create a list from elements of HashMap 
		List<Map.Entry<String, Integer> > lists = 
				new LinkedList<Map.Entry<String, Integer> >(letterMap.entrySet()); 

		// Sort the list 
		Collections.sort(lists, new Comparator<Map.Entry<String, Integer> >() { 
			public int compare(Map.Entry<String, Integer> o1,  
					Map.Entry<String, Integer> o2) 
			{ 
				return (o2.getValue()).compareTo(o1.getValue()); 
			} 
		}); 

		// put data from sorted list to temporary HashMap  
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 

		for (Map.Entry<String, Integer> list : lists) { 
			if(temp.size()<10)
				temp.put(list.getKey(), list.getValue()); 
		} 

		//Assign temp HashMap to letterMap instance variable 
		letterMap = temp; 
	} 

	/**
	 * countLetter:  Iterates through the contents of the List wordList that has been imported.  Loops through each character of the words stored in a wordList entry.  Adds the count for each character
	 * as a value and the character as a key into the letterMap HashMap.  Returns a String output of the contents of the keys and values.  
	 */
	private String countLetter() {

		//Clears the letterMap HashMap instance variable
		clearHash();

		String current;
		Iterator<String> it = wordList.iterator();

		while (it.hasNext()) {
			String curWord = it.next();

			for(int i = 0; i<curWord.length(); i++) {
				current = Character.toString(curWord.charAt(i));		
				if(letterMap.containsKey(current)) {
					int num = letterMap.get(current);
					num++;
					letterMap.put(current, num);
				} else {
					letterMap.put(current, 1);
				}
			}
		}

		//The HashMap is sorted into a top 10, greatest to least
		sortByValue(); 

		String sortedHash = "";

		//Set of keys created
		keySet();	

		//Keys and values are returned to the user
		for(String key: keys) {
			sortedHash += "Letter: " + key + " with a frequency of: " + letterMap.get(key) + "\n";
		}
		return sortedHash; 

	}

	/**
	 * getLetterCount:  The public method that calls the encapsulated countLetter method.  
	 */
	public String getLetterCount() {
		return countLetter();
	}

	/**
	 * wordCount:  Iterates through the contents of the List wordList that has been imported.  Adds the count for each word
	 * as a value and the word itself as a key into the letterMap HashMap.  Returns a String output of the contents of the keys and values.  
	 */
	private String wordCount() {

		//Clears the letterMap HashMap instance variable
		clearHash();

		Iterator<String> it = wordList.iterator();

		while (it.hasNext()) {
			String curWord = it.next();

			if(letterMap.containsKey(curWord)) {
				int num = letterMap.get(curWord);
				num++;
				letterMap.put(curWord, num);
			} else {
				letterMap.put(curWord, 1);
			}
		}

		//The HashMap is sorted into a top 10, greatest to least
		sortByValue();

		String sortedHash = "";

		//Set of keys created
		keySet();	

		//Keys and values are returned to the user
		for(String key: keys) {
			sortedHash += "Word: " + key + " with a frequency of: " + letterMap.get(key) + "\n";
		}
		return sortedHash; 
	}

	/**
	 * getWordCount:  The public method that calls the encapsulated wordCount method.  
	 */
	public String getWordCount() {
		return wordCount();
	}

	/**
	 * wordCount:  Imports the file stop-list.txt using FileReader (file not found exception is handled through try/catch).  Inserts contents into stopList List object.
	 * Iterates through the contents of the List wordList that has been imported.  The word is compared to the stopList and if it does 
	 * not appear in the stopList, the method adds the count for each word as a value and the word itself as a key into the letterMap HashMap.  
	 * Returns a String output of the contents of the keys and values.  
	 */
	private String wordStopList() {

		//Clears the letterMap HashMap instance variable
		clearHash();

		//A new LinkedList is created to hold the contents of the stopList
		List<String> stopList = new LinkedList<>();

		Iterator<String> it = wordList.iterator();

		//stopList is imported from text file
		String fname = "stop-list.txt";       // input file of text
		FileReader fin = null;
		//can throw exception
		//must handle
		try {
			fin = new FileReader(fname);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner info = new Scanner(fin);

		readFile(stopList, info);

		while (it.hasNext()) {
			String curWord = it.next();

			if(!stopList.contains(curWord)) {
				if(letterMap.containsKey(curWord)) {
					int num = letterMap.get(curWord);
					num++;
					letterMap.put(curWord, num);
				} else {
					letterMap.put(curWord, 1);
				}
			}
		}

		//The HashMap is sorted into a top 10, greatest to least
		sortByValue();

		String sortedHash = "";

		//Set of keys created
		keySet();	

		//Keys and values are returned to the user
		for(String key: keys) {
			sortedHash += "Word: " + key + " with a frequency of: " + letterMap.get(key) + "\n";
		}
		return sortedHash;
	}

	/**
	 * getWordStopList:  The public method that calls the encapsulated wordStopList method.  
	 */
	public String getWordStopList() {
		return wordStopList();
	}

	/**
	 * longWords: Iterates through the contents of the List wordList that has been imported.  If the word is not already inserted in the 
	 * letterMap HashMap (to prevent multiple occurrences), the word is inserted into the HashMap along with the word's length in characters.  
	 * Returns a String output of the contents of the keys and values.  
	 */
	private String longWords() {

		//Clears the letterMap HashMap instance variable
		clearHash();

		Iterator<String> it = wordList.iterator();

		while (it.hasNext()) {
			String curWord = it.next();

			if(letterMap.containsKey(curWord)) {
				continue; 
			} else {
				letterMap.put(curWord, curWord.length());

			}
		}

		//The HashMap is sorted into a top 10, greatest to least
		sortByValue();

		String sortedHash = "";

		//Set of keys created
		keySet();		

		//Keys and values are returned to the user
		for(String key: keys) {
			sortedHash += "Word: " + key + " with a length of: " + letterMap.get(key) + " characters\n";
		}
		return sortedHash;

	}

	/**
	 * getLongWords:  The public method that calls the encapsulated longWords method.
	 */
	public String getLongWords() {
		return longWords();
	}
}
