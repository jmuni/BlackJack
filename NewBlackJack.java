import java.util.Scanner;
import java.util.*;
import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;
import java.text.*;
//import java.util.ArrayUtils;

public class NewBlackJack {

	public static void main(String[] args) {

		System.out.println("How many decks are you playing with?");
		Scanner inputNumDecks = new Scanner(System.in);
		// Scanner inputChoice = new Scanner(System.in);
		// Scanner inputTemp;

		// sets a deckLimit variable to 52 which is the total number of cards
		// per deck
		int deckLimit = 52;
		int numDecks = inputNumDecks.nextInt();
		// inputNumDecks.close();
		System.out.println("# Decks = " + numDecks);
		int numCards = numDecks * deckLimit;

		// sets up a fixed sized array based on numDecks
		Character[] deck = setUpDeck(numDecks);
	
		

		// checking generated deck based on user input for number of decks		
		System.out.println("The Deck");
		showDeck(deck);
		// shuffles the array of cards
//		shuffleDeck(deck);

		List<Character> charList = Arrays.asList(deck);
		Collections.shuffle(charList);
		Character[] shuffledDeck = (Character[]) charList.toArray();
		
//test for dealer blackjack			
//		Character[] shuffledDeck = {'J','A','T','Q'};
		// checking the shuffled deck. wont be shown
		System.out.println("The Shuffled Deck");
		showDeck(shuffledDeck);

		// initializes the player balance. Minimum balance is set to 10.
		// when the player falls below the minimum balance, the program exits
		System.out.println();
		System.out.println("How much money will you start with?");
		Scanner inputBalance = new Scanner(System.in);
		int playerBalance = inputBalance.nextInt();
		int minimumBet = 10;

		int n = 0;
		// int playerTotal = 0;
		// int dealerTotal = 0;
		// boolean to see if player can double down or split
		boolean canDoubleDown = false;
		boolean canSplit = false;
		int numSplit = 0;
		
		boolean playerBlackJack = false;
		boolean dealerBlackJack = false;
		boolean offerInsurance = false;

		// we want an loop until the player says end or until the deck runs out
		while (n < numCards) {

			checkBalance(playerBalance);

			int betAmount = 0;

			System.out.println("How much will you bet?");
			Scanner inputBet = new Scanner(System.in);
			betAmount = inputBet.nextInt();

			while (playerBalance < betAmount) {
				System.out.println(
						"You dont have enough money. Please enter a number under your balance of " + playerBalance);
				betAmount = inputBet.nextInt();
			}

			while (betAmount < minimumBet) {
				System.out.println("The minimum bet is 10. Please enter a number that is 10 or higher");
				betAmount = inputBet.nextInt();
			}

			// resets arrays for player and dealer in setUpHands method
			Character[] playerHand = setUpHand();
			Character[] dealerHand = setUpHand();
			Character[] split1 = setUpHand();
			Character[] split2 = setUpHand();
			Character[] split3 = setUpHand();

			// player get first card, dealer gets second, player gets third, and
			// dealer gets fourth
			playerHand = addCard(shuffledDeck, playerHand, n);
			n++;
			dealerHand = addCard(shuffledDeck, dealerHand, n);
			n++;
			playerHand = addCard(shuffledDeck, playerHand, n);
			n++;
			dealerHand = addCard(shuffledDeck, dealerHand, n);
			n++;

			// shows player hand and dealers 4th card. Dealer other card shows
			// up as ? because in actual BJ we dont see it.
			showPlayer(playerHand);
			System.out.println("Your hand : " + handTotal(playerHand));
			hideDealer(dealerHand);
			
			playerBlackJack = false;
			dealerBlackJack = false;
			offerInsurance = false;
			
			playerBlackJack = checkBlackJack(playerHand);
			dealerBlackJack = checkBlackJack(dealerHand);
			offerInsurance = checkInsurance(dealerHand);
			

			if (playerBlackJack && offerInsurance) {
				System.out.println("You have Black Jack. The Dealer has Ace showing. Would you like even money? (y/n)");
				System.out.println("If yes, you win your bet. If no, and dealer has BlackJack you push. If no and dealer does NOT have BlackJack you win 1.5x your bet.");
				//do something, gotta fix this part
			} else if (playerBlackJack && !offerInsurance) {
				System.out.println("You got BlackJack!");
				playerBalance = playerBalance + (betAmount * 3 / 2);
			} else if (!playerBlackJack && dealerBlackJack) {
				showDealer(dealerHand);
				System.out.println("Dealer got BlackJack!");
				playerBalance = playerBalance - betAmount;
			}

			else {
				// bustChance(n, numCards, playerHand, deck);

				canDoubleDown = false;
				canSplit = false;

				System.out.println("Will you hit(h) / stand(s) ?");
			

			if (handTotal(playerHand) == 9 || handTotal(playerHand) == 10 || handTotal(playerHand) == 11) {
				if (playerBalance >= (betAmount * 2)) {
					canDoubleDown = true;
					System.out.println("You can double down (d)");
				}
			}
			if (playerHand[0] == playerHand[1]) {

				canSplit = true;
				System.out.println("You can split (q)");
			}

			Scanner inputChoice = new Scanner(System.in);
			String choice = inputChoice.nextLine();
			// inputChoice.close();
			// so apparantly closing scanners messes this stuff up.

			while (choice.equals("h")) {
				playerHand = addCard(shuffledDeck, playerHand, n);
				n++;
				showPlayer(playerHand);
				System.out.println("Your hand : " + handTotal(playerHand));
				hideDealer(dealerHand);
				if (handTotal(playerHand) > 21) {
					showDealer(dealerHand);
					System.out.println("You bust!");
					playerBalance -= betAmount;
					break;
				}
				// bustChance(n, numCards, playerHand, deck);
				System.out.println("Will you hit(h) or stand(s)?");
				choice = inputChoice.nextLine();
				// sc.close();

				// if (hitchoice.equals("stand")) {
				// break;
				// }
			}

			if (choice.equals("s")) {
				showDealer(dealerHand);
				System.out.println("Dealer : " + handTotal(dealerHand));
				while (handTotal(dealerHand) < 17) {
					dealerHand = addCard(shuffledDeck, dealerHand, n);
					n++;
					showDealer(dealerHand);
					System.out.println("Dealer's hand : " + handTotal(dealerHand));
					if (handTotal(dealerHand) > 21) {
						System.out.println("Dealer busts!");
						playerBalance += betAmount;
					}
				}
			}

			if (choice.equals("d") && canDoubleDown == true) {

				betAmount *= 2;
				playerHand = addCard(shuffledDeck, playerHand, n);
				n++;
				showPlayer(playerHand);
				System.out.println("Your hand : " + handTotal(playerHand));
				hideDealer(dealerHand);
				if (handTotal(playerHand) > 21) {
					System.out.println("You bust!");
					playerBalance -= betAmount;
					break;
				}
				showDealer(dealerHand);
				System.out.println("Dealer : " + handTotal(dealerHand));
				while (handTotal(dealerHand) < 17) {
					dealerHand = addCard(shuffledDeck, dealerHand, n);
					n++;
					showDealer(dealerHand);
					System.out.println("Dealer's hand : " + handTotal(dealerHand));
					if (handTotal(dealerHand) > 21) {
						System.out.println("Dealer busts!");
						playerBalance += betAmount;
					}
				}
			}
/*
			if (choice.equals("q") && canSplit) {
				split1[0] = playerHand[0];
				split2[0] = playerHand[1];
				numSplit = 2;


					System.out.println("Will you hit(h) or stand(s)?");
				choice = inputChoice.nextLine();

				// error check
				if (!choice.equals("h") && !choice.equals("s")) {
					System.out.println("Invalid Input for Splitting! Exiting...");
					System.exit(0);
				}

			}
*/
			// check for proper input
			if (choice.equals("h")) {
				
			} else if (choice.equals("s")) {
				
			} else if (choice.equals("d") && canDoubleDown) {
				
			} else if (choice.equals("q") && canSplit) {
				
			} else {
				System.out.println("Invalid Input! Exiting...");
				System.exit(0);
			}
		

			if (handTotal(playerHand) <= 21 && handTotal(dealerHand) <= 21) {
				if (handTotal(playerHand) > handTotal(dealerHand)) {
					System.out.println("Your " + handTotal(playerHand) + " beats the Dealer's " + handTotal(dealerHand));
					playerBalance += betAmount;
				} else if (handTotal(playerHand) < handTotal(dealerHand)) {
					System.out.println("The Dealer's " + handTotal(dealerHand) + " beats your " + handTotal(playerHand));
					playerBalance -= betAmount;
				} else if (handTotal(playerHand) == handTotal(dealerHand)) {
					System.out.println("Your " + handTotal(playerHand) + " pushes the Dealer's " + handTotal(dealerHand));
				}
			} // end bust loop

		}// end blackjack check
		System.out.println();
		System.out.println("///////////////////////////////////////////////////////////////////////");
		System.out.println("Starting new hand!");
		System.out.println("Your balance now is : " + playerBalance);
		System.out.println("There are " + (numCards - n) + " cards left in the deck");
		// System.out.println("Ready for the next hand? Type r");
		// Scanner inputReady = new Scanner(System.in);
		// char ready = inputReady.next().charAt(0);
		// inputReady.close();
		// close hit/stand scanner for next hand
		// inputChoice.close();
		
		}//end checkBlackJack
		/*
		 * if(ready == ('r')) { continue; } else {
		 * System.out.println("Not ready, exiting."); System.exit(0); }
		 */
	}// end while loop for deck

	// showProb

	// }//end main

	// creates the deck based on user input for number of decks
	static Character[] setUpDeck(int numDecks) {

		int numCards = 52 * numDecks;
		Character[] deck = new Character[numCards];

		for (int i = 0; i < numCards; i++) {

			int temp = i;
			if (i >= 52)
				temp = i % 52;

			if (temp <= 3)
				deck[i] = 'A';
			if (temp > 3 && temp < 8)
				deck[i] = '2';
			if (temp > 7 && temp < 12)
				deck[i] = '3';
			if (temp > 11 && temp < 16)
				deck[i] = '4';
			if (temp > 15 && temp < 20)
				deck[i] = '5';
			if (temp > 19 && temp < 24)
				deck[i] = '6';
			if (temp > 23 && temp < 28)
				deck[i] = '7';
			if (temp > 27 && temp < 32)
				deck[i] = '8';
			if (temp > 31 && temp < 36)
				deck[i] = '9';
			if (temp > 35 && temp < 40)
				deck[i] = 'T';
			if (temp > 39 && temp < 44)
				deck[i] = 'J';
			if (temp > 43 && temp < 48)
				deck[i] = 'Q';
			if (temp > 47 && temp < 52)
				deck[i] = 'K';
		}
		return deck;
	}// end setUpDeck

	// method to check deck contents. useful because it can be used to show the
	// generated deck and then the shuffled deck.
	static void showDeck(Character deck[]) {

		for (int i = 0; i < deck.length; i++) {
			if (i % 26 == 0 && i != 0) {
				System.out.println();
			}
			if(deck[i] == 'T') 
				System.out.print("10 ");
			else
				System.out.print(deck[i] + " ");
		}
		System.out.println();
	}

	// method to show whats in the dealer hand, after the player stands
	static void showDealer(Character hand[]) {

		System.out.println();

		System.out.println("Dealer Hand");

		for (int i = 0; i < hand.length; i++) {
			if (hand[i] != '0') {
				if(hand[i] == 'T') 
					System.out.print("10 ");
				else
					System.out.print(hand[i] + " ");
			}
		}
		System.out.println();
	}

	// method to show whats in the players hand
	static void showPlayer(Character hand[]) {

		System.out.println();

		System.out.println("Player Hand");

		for (int i = 0; i < hand.length; i++) {
			if (hand[i] != '0') {
				if(hand[i] == 'T') 
					System.out.print("10 ");
				else
					System.out.print(hand[i] + " ");
			}
		}
		System.out.println();
	}

	// shows the face up card to the player. The other card is hidden however,
	// like in real blackjack
	static void hideDealer(Character hand[]) {

		System.out.println();

		System.out.println("Dealer Hand");
		
		if(hand[0] == 'T') 
			System.out.print("10 ");
		else
			System.out.print(hand[0] + " ");
		System.out.print("? ");
		// for (int i = 1; i < methodHand.length; i++) {
		// System.out.print(methodHand[i] + " ");
		// }
		System.out.println();
	}

	// reset the hand to an array of 0's to signify an empty hand
	static Character[] setUpHand() {

		Character[] hand = { '0', '0', '0', '0', '0', '0', '0', '0', '0'};

		return hand;

	}

	// the next card in the deck is added to the player or dealers hand.
	static Character[] addCard(Character[] deck, Character[] hand, int card) {

		int i = 0;
		while (i < 9) {
			if (hand[i] == '0') {
				hand[i] = deck[card];
				break;
			} else {
				i++;
			}
		}
		return hand;
	}

	// takes a hand (int[] parameter) and totals it. Takes into account that 1
	// can act as 11 or 1. And acts as 1 if the total is over 21.
	static int handTotal(Character[] card) {

		int total = 0;
		
		
		for (int i = 0; i < card.length; i++) {
			if(card[i] == '0') {
				break;
			} else if(card[i] == 'T' || card[i] == 'J' || card[i] == 'Q' || card[i] == 'K') {
				total += 10;
			} else if(card[i] == 'A')  {
				total += 1;
			} else {
				total += card[i] - '0';
			}
		}

		
		for (int i = 0; i < card.length; i++) {
			if (card[i] == 'A' && total <= 11) 
				total += 10;		
		}

		return total;
	}

	// takes a hand (int[] parameter) and checks if it has an Ace(1) and a 10
	static boolean checkBlackJack(Character[] hand) {

		boolean hasBlackJack = false;
		if (hand[2] == '0' && handTotal(hand) == 21) {
			hasBlackJack = true;
		}
		return hasBlackJack;
	}
	
	static boolean checkInsurance(Character[] hand) {

		boolean offerInsurance = false;
		if (hand[2] == '0' && hand[0] == 'A') {
			offerInsurance = true;
		}
		return offerInsurance;
	}

	// error check to see if player actually has enough to keep playing
	static void checkBalance(int methodPlayerBalance) {

		int minBet = 10;

		if (methodPlayerBalance < minBet) {
			System.out.println("Your balance of " + methodPlayerBalance + " is too low. Exiting...");
			System.exit(0);
		}
	}

	// returns a percentage of busting, calculated by counting all the cards
	// equal or lesser than needed amount
	// 1st parameter takes int n as a parameter to start counted from that point
	// of the deck. 2nd input is the total number of cards in the deck
	// 3rd parameter takes the hand input to check
	static void bustChance(int methodN, int totalCards, Character[] hand, Character[] deck) {

		double bustPercent;
		double tensPercent;
		double acesPercent;
		int temp = 0;
		int tens = 0;
		int aces = 0;
		int cardsLeft = totalCards - methodN;

		for (int i = methodN; i < totalCards; i++) {
			if (deck[i] <= (21 - handTotal(hand))) {
				temp++;
			}
			if (deck[i] == 10) {
				tens++;
			}
			if (deck[i] == 1) {
				aces++;
			}
		}

		System.out.println();
		// System.out.println("methodN : " + methodN );
		// System.out.println("totalCards : " + totalCards);

		bustPercent = ((double) cardsLeft - (double) temp) / (double) cardsLeft * 100;
		tensPercent = (double) tens / (double) cardsLeft * 100;
		acesPercent = (double) aces / (double) cardsLeft * 100;
		System.out.println("# Valid Cards : " + temp);
		System.out.println("# Cards Left : " + cardsLeft);
		System.out.println("Aces Chance : " + acesPercent + "%");
		System.out.println("10s Chance : " + tensPercent + "%");
		System.out.println("Bust Chance : " + bustPercent + "%");
		for (int j = 0; j < 9; j++) {
			if (hand[j] == 1) {
				System.out.println(
						"If you only have 1 ace technically you wont bust. In that case take bust chance as soft Ace chance");
				break;
			}
		}

		// syntax to make percent show with 2 decimal places
		// DecimalFormat actualPercent = new DecimalFormat("#.##");
		// System.out.println("Bust Chance : " +
		// actualPercent.format(bustPercent) + "%");
		// return bustPercent;

	}

}

