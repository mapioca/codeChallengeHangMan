import java.io.*;
import java.util.*;

public class hangMan {

    public static void main(String[] args) throws FileNotFoundException {
        Map<Integer, StringBuilder> dictionaryWords = new HashMap<>();
        String DICTIONARY_NAME = "words.txt";
        StringBuilder randomWord = new StringBuilder();
        boolean keepPlaying = true;

        System.out.println("Hello user! Welcome to Hangman");
        dictionaryWords = getDictionaryWords(DICTIONARY_NAME);

        //  Play until user wants to stop
        while(keepPlaying) {
            randomWord = getRandomWord(dictionaryWords);
            playGame(randomWord);
        }



    }

    private static void playGame(StringBuilder randomWord) {
        System.out.println("The word to be guessed has " + randomWord.length() + " letter(s). Good luck!");

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
}
