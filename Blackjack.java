/**
 * Project 3:  Practicing the use of Java Lists by implementing a
 * command line Blackjack game.
 * @author Nick Langan CSC 1052-001
 * Date Created:  April 17, 2019
 * Date Modified:  April 24, 28-30, 2019
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Blackjack {public static void main(String[] args) {
	Scanner scan = new Scanner(System.in);

	//Deck of cards for the player and dealer are created
	CardDeck playerDeck = new CardDeck();
	CardDeck dealerDeck = new CardDeck();

	//ArrayList objects for the player and dealer hands are created
	ArrayList<Card> playerHand = new ArrayList<>();
	ArrayList<Card> dealerHand = new ArrayList<>();

	//The two card decks are shuffled
	playerDeck.shuffle();
	dealerDeck.shuffle();

	//Here, the first 2 cards for the player and dealer are drawn from the deck
	Card c = playerDeck.nextCard();
	Card e = playerDeck.nextCard();
	Card d = dealerDeck.nextCard();
	Card f = dealerDeck.nextCard();

	//The 2 cards each for the player and dealer are added to their respective hands
	playerHand.add(c);
	playerHand.add(e);
	dealerHand.add(d);
	dealerHand.add(f);

	//Program greets the player with a welcome sequence
	System.out.println("Welcome to Villanova Blackjack!");
	System.out.println();
	System.out.println("Gambling problem?  Call 1-800-GAMBLER");
	System.out.println();
	System.out.println("GAME SUMMARY:");
	System.out.println("The goal of the game is to get the sum of cards in your hand higher than the dealer’s without\r\n" + 
			"going over 21. Each card is worth the rank value, where face cards are worth 10 and aces are\r\n" + 
			"worth either 1 or 11.");
	
	System.out.println();
	System.out.println("Press ENTER to continue!");
	scan.nextLine();

	//The initial two cards in playerHand are printed to the player, along with their total value using the countHand method to calculate.
	System.out.println("In your deck, the initial two cards you have drawn are: ");
	System.out.println(printHand(playerHand));
	System.out.println("For a total value of: " + countHand(playerHand));

	System.out.println();

	//The first card in dealerHand is printed to the player, along with its value.  The hole card is not revealed.
	System.out.println("The dealer's first card in their deck is " + d.toString() + ", a value of: " + d.getRank().getValue());	

	System.out.println();

	//The player is given their first opportunity to hit or stay.  Their input response determines the next sequence.  
	System.out.println("Would you like to hit or stay?  Type HIT or STAY below and press enter.  Type QUIT below and press enter to exit.");
	String response = scan.nextLine();
	response = response.toLowerCase();

	//Boolean object is created to keep track of whether or not the player busts over a value of 21.
	boolean bust = false;

	//While loop is generated, based on a hit response from the player and that the player's value is at or below 21 (boolean bust value).  
	while (response.charAt(0) == 'h' && bust == false)
	{

		//A new card is drawn from the playerDeck and added to their hand
		Card g = playerDeck.nextCard();
		playerHand.add(g);

		//The newly added card is revealed to the player
		System.out.println("You have added " + g.toString());

		System.out.println();

		//The status of the player's entire hand is revealed by using the printHand method
		System.out.println("Your hand currently contains: ");
		System.out.println(printHand(playerHand));

		//The value of the player's entire hand is revealed by using the countHand method
		System.out.println("For a total value of: " + countHand(playerHand));
		System.out.println();

		//If the value of the player's entire hand exceeds 21, the bust boolean value changes to TRUE, and they will ultimately lose the game.
		if (countHand(playerHand) > 21) {
			bust = true;
		}
		//If the player's hand value remains at or below 21, they proceed to be asked whether they wish to hit or stay again.  
		else {
			System.out.println("Would you like to hit or stay?  Type HIT or STAY below and press enter.  Type QUIT below and press enter to exit.");
			response = scan.nextLine();
			response = response.toLowerCase();
		}

	}

	//The system.exit command is used if the player chooses to quit the program.
	if (response.charAt(0) == 'q' ) {
		System.out.println("Thank you for playing!");
		System.exit(0); 
	}

	//Boolean objects are created to keep track of whether or not the player ultimately beats the dealer, and if there is a tie between the player and dealer
	boolean playerWinner = true;
	boolean tie = false; 

	//If the player chooses to 'stay', while the dealer's hand value remains below 17, a new card from their deck is added to the dealer's hand
	if (response.charAt(0) == 's'){
		{while (countHand(dealerHand) < 17) {
			Card h = dealerDeck.nextCard();
			dealerHand.add(h);
		}}

		//If the dealer's hand value exceeds 21, the playerWinner boolean changes to true, as they automatically win the game
		if (countHand(dealerHand) > 21)
			playerWinner = true;
		//If neither participant has busted but the dealer's hand value exceeds the player's hand value, the playerWinner boolean is changed to false and the dealer will win
		else if (countHand(dealerHand) > countHand(playerHand))
			playerWinner = false;
		//If neither participant has busted but the player's hand value exceeds the dealer's hand value, the playerWinner boolean is changed to true and the player will win
		else if (countHand(dealerHand) < countHand(playerHand)) 
			playerWinner = true;
		//If neither participant has busted and the player and dealer hand values are the same, the tie boolean is changed to true
		else {
			tie = true;
			playerWinner = false;
		}
	}

	//If the player has busted, an appropriate message prints and the dealer is declared the winner
	if (bust == true)
	{System.out.println("You busted!  The dealer wins.");
	playerWinner = false;
	}

	System.out.println();

	//If the playerWinner boolean is set to true based on the previous if/else statements, the player is declared the winner and receives proper accolades.  
	//The ultimate hand values of the player and dealer are also printed.
	if (playerWinner == true)
	{System.out.println("You win!  Congratulations!");
	System.out.println("Your hand had a value of " + countHand(playerHand) + " and the dealer hand had a value of " + countHand(dealerHand) + ".");
	}

	//If the playerWinner boolean is set to false based on the previous if/else statements, there is no tie, AND the outcome is not a result of the player busting,
	//the player is notified of their loss and the dealer is declared the winner.
	if (playerWinner == false && bust == false && tie == false)
	{System.out.println("You Lost.  I'm so, so sorry.");
	System.out.println("Your hand had a value of " + countHand(playerHand) + " and the dealer hand had a value of " + countHand(dealerHand) + ".");
	}

	//Sometimes in life, there is no winner.  If the tie boolean is declared true, the outcome of a push is declared.  
	if (playerWinner == false && tie == true)
	{System.out.println("There was a push!");
	System.out.println("Your hand had a value of " + countHand(playerHand) + " and the dealer hand had a value of " + countHand(dealerHand) + ".");
	}

}

/**
 * printHand:  Accepts an ArrayList of type card.  Uses iterator object to iterate through the Arraylist hand and print the hand objects in a comma delimited list.  
 * @param hand
 * @return handShake 
 */
public static String printHand(ArrayList <Card> hand) {

	String handShake = "";

	Iterator<Card> it = hand.iterator();

	if (it.hasNext()) {
		handShake = it.next().toString();
		while (it.hasNext())
			handShake += ", "+ it.next().toString();

	}

	return handShake;
}

/**
 * countHand:  Accepts an ArrayList of type Card.  Uses two enhanced for loops to produce sum of card values in hand and count of ace ranks in hand.  
 * If the aceCount exceeds 0, while the total sum is greater than 21, the amount of 10 is subtracted from the sum.  This is to account for the value of the ace
 * being either 1 (sum less than 21) or 11 (sum exceeds 21).  The method is tested to account for a scenario where more than once ace exists in a give hand.  
 * After assessing the aceCount, the sum is returned as an integer.  
 * @param hand
 * @return sum
 */
public static int countHand(ArrayList <Card> hand) {

	int sum = 0;
	int aceCount=0;
	for (Card a: hand)
	{
		sum += a.rank.getValue();

	}

	for(Card c: hand)
	{if (c.rank.getValue() == 11)
		aceCount++;
	}

	if (aceCount > 0) {
		while(sum > 21 && aceCount > 0) {
			sum -= 10;
			aceCount--;

		}
	}

	return sum;
}
}
