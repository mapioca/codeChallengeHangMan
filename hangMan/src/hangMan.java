import java.io.*;
import java.util.*;

public class hangMan {
    static boolean keepPlaying;
    static boolean gameOver;

    static int correctGuesses;
    static int incorrectGuesses;
    static int numLettersGuessed = 0;
    static int numLettersToGuess = 0;

    static StringBuilder randomWord;

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, StringBuilder> dictionaryWords = new HashMap<>();
        String DICTIONARY_NAME = "words.txt";

        System.out.println("Hello user! Welcome to Hangman");

        //  Generate a list of words based on text file
        dictionaryWords = getDictionaryWords(DICTIONARY_NAME);

        //  Play until user wants to stop
        while(!isGameOver()) {
            setupGame(dictionaryWords); //  game Setup
            playGame(getRandomWord());  //  play game using a random word
            willPlayAgain();            //  asks user to play again
        }
    }

    /**
     * Asks user if he/she wants to play again
     */
    private static void willPlayAgain() {
        Scanner myScanner = new Scanner(System.in);

        boolean gotRightInput = false;
        while(!gotRightInput) {
            System.out.println("Would you like to play again?(y/n)");

            char playAgain = myScanner.next().charAt(0);
            if(playAgain == 'n' || playAgain == 'N') {
                setGameOver(true);
                gotRightInput = true;
            } else if(playAgain == 'y' || playAgain =='Y') {
                setGameOver(false);
                gotRightInput = true;
            } else {
                gotRightInput = false;
            }
        }
    }

    private static void setupGame(Map<Integer, StringBuilder> dictionaryWords) {
        setRandomWord(getRandomWord(dictionaryWords));
        setNumLettersToGuess(getRandomWord().length());
        setNumLettersGuessed(0);
        setGameOver(false);
        setCorrectGuesses(0);
        setIncorrectGuesses(0);
    }

    /**
     * Starts a new game
     * @param randomWord word to guess
     */
    private static void playGame(StringBuilder randomWord) {
        Set<Character> lettersGuessed = new HashSet<>();
        StringBuilder partiallyConstructedWord;

        //  Sets all letters to a blank space ('-')
        partiallyConstructedWord = hideWord(randomWord);

        System.out.println("The word to be guessed has " + randomWord.length() + " letter(s). Good luck!");

        // Game core
        keepPlaying = true;
        while(keepPlaying) {
            playGameHelper(partiallyConstructedWord, lettersGuessed);
        }

    }

    /**
     * Plays the actual game.
     * Asks for user input and checks if a letter was guessed.
     * Updates partially constructed word and displays it to the user
     * @param partiallyConstructedWord partial word that has been guessed
     * @param lettersGuessed list of letters already guessed by user
     */
    private static void playGameHelper(StringBuilder partiallyConstructedWord,
                                       Set<Character> lettersGuessed) {
        Scanner myScanner = new Scanner(System.in);

        //  Print partially constructed word
        printPlayWord(getRandomWord(), partiallyConstructedWord, lettersGuessed);

        //  Get input from user
        boolean passed = false;
        while(!passed) {
            System.out.print("Enter guess: ");
            char guess = myScanner.next().charAt(0);

            if(lettersGuessed.contains(guess)) {
                passed = false;
                System.out.print(guess + " has been already guessed. ");

            } else {
                passed = true;
                lettersGuessed.add(guess);

                //Check if letter exists in word
                if(isLetterInWord(guess)) {
                    increaseCorrectGuesses();
                } else {
                    increaseIncorrectGuesses();
                    System.out.println("'" + guess + "'" + " was not found in the word");
                }

                updateConstructedWord(randomWord, partiallyConstructedWord, guess);
                System.out.println("\nGuesses made: [Correct=" + getCorrectGuesses() +
                        ", Incorrect=" + getIncorrectGuesses() + "]");

                if((getNumLettersToGuess() - getNumLettersGuessed()) == 0) {
                    setKeepPlaying(false);
                    System.out.println("Congrats! You guessed the word! The word was: '" + getRandomWord() + "'");
                    System.out.println("It took " + (getCorrectGuesses() + getIncorrectGuesses()) + " guesses\n");

                }
            }
        }
    }


    /**
     * Checks if letter is indeed in the word
     * @param guess user letter guess
     * @return whether or not the letter was found in the word
     */
    private static boolean isLetterInWord(char guess) {
        boolean letterFound = false;

        for(int i = 0; i < getRandomWord().length(); i++) {
            if(getRandomWord().charAt(i) == guess) {
                letterFound = true;
                break;
            }
        }
        return letterFound;
    }

    private static void updateConstructedWord(StringBuilder randomWord, StringBuilder partiallyConstructedWord, char guess) {

        for(int i = 0; i < randomWord.length(); i++) {
            if(randomWord.charAt(i) == guess) {
                partiallyConstructedWord.setCharAt(i, guess);
                numLettersGuessed++;
            }
        }
    }

    /**
     * Prints the partially constructed word
     * @param randomWord word to be guessed
     * @param partiallyConstructedWord word getting constructed
     * @param lettersGuessed list of letters that have been guessed
     */
    private static void printPlayWord(StringBuilder randomWord, StringBuilder partiallyConstructedWord, Set<Character> lettersGuessed) {
        System.out.print("Letters guessed:");
        printLettersGuessed(lettersGuessed);
        System.out.println(partiallyConstructedWord);
    }

    /**
     * Print letters that has been guessed
     * @param lettersGuessed list of guessed letters
     */
    private static void printLettersGuessed(Set<Character> lettersGuessed) {
        for(Character letter : lettersGuessed) {
            System.out.print(" " + letter);
        }
        System.out.println();
    }


    /**
     * Substitutes the letters in the word for blank spaces ('-')
     * @param randomWord word to be guessed
     * @return word with only blanks
     */
    private static StringBuilder hideWord(StringBuilder randomWord) {
        StringBuilder hiddenWord = new StringBuilder();
        char theChar = '-';
        for(int i = 0; i < randomWord.length(); i++) {
            hiddenWord.append(theChar);
        }
        return hiddenWord;
    }



    /**
     * Gets a random word from the list
     * @param dictionaryWords the list containing possible words to be guessed
     * @return a random word to be guessed
     */
    private static StringBuilder getRandomWord(Map<Integer, StringBuilder> dictionaryWords) {
        Random random = new Random();
        int randomIndex = random.nextInt(dictionaryWords.size() - 1);
        return dictionaryWords.get(randomIndex);
    }



    /**
     * Gets the words from a text file and
     * puts them in a set
     * @param name the name of the file
     * @return a set with words to guess
     */
    public static Map<Integer, StringBuilder> getDictionaryWords(String name) throws FileNotFoundException {
        Map<Integer, StringBuilder> dictionaryWords = new HashMap<>();

        File file = new File(name);
        Scanner scanner = new Scanner(file);

        int index = 0;
        while(scanner.hasNext()) {
            StringBuilder word = new StringBuilder(scanner.next());
            dictionaryWords.put(index, word);
            index++;
        }
        return dictionaryWords;
    }




    /** Getters and Setters **/

    public static StringBuilder getRandomWord() {
        return randomWord;
    }

    public static void setRandomWord(StringBuilder randomWord) {
        hangMan.randomWord = randomWord;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setKeepPlaying(boolean keepPlaying) {
        hangMan.keepPlaying = keepPlaying;
    }

    public static void setGameOver(boolean gameOver) {
        if(gameOver) {
            System.out.println("Thank you for playing!");
        }
        hangMan.gameOver = gameOver;
    }

    private static void increaseIncorrectGuesses() {
        incorrectGuesses++;
    }

    private static void increaseCorrectGuesses() {
        correctGuesses++;
    }

    public static int getNumLettersGuessed() {
        return numLettersGuessed;
    }

    public static void setNumLettersGuessed(int numLettersGuessed) {
        hangMan.numLettersGuessed = numLettersGuessed;
    }

    public static int getNumLettersToGuess() {
        return numLettersToGuess;
    }

    public static void setNumLettersToGuess(int numLettersToGuess) {
        hangMan.numLettersToGuess = numLettersToGuess;
    }

    public static boolean isKeepPlaying() {
        return keepPlaying;
    }

    public static int getCorrectGuesses() {
        return correctGuesses;
    }

    public static void setCorrectGuesses(int correctGuesses) {
        hangMan.correctGuesses = correctGuesses;
    }

    public static int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public static void setIncorrectGuesses(int incorrectGuesses) {
        hangMan.incorrectGuesses = incorrectGuesses;
    }
}
