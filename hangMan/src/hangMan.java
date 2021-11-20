import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class hangMan {
    static String DICTIONARY_NAME;

    public static void main(String[] args) throws FileNotFoundException {
        Set<StringBuilder> dictionaryWords = new HashSet<>();

        DICTIONARY_NAME = "words.txt";
        System.out.println("Hello user! Welcome to Hangman");
        dictionaryWords = getDictionaryWords(DICTIONARY_NAME);
    }

    /**
     * Gets the words from a text file and
     * puts them in a set
     * @param name the name of the file
     * @return a set with words to guess
     */
    public static Set<StringBuilder> getDictionaryWords(String name) throws FileNotFoundException {
        Set<StringBuilder> dictionaryWords = new HashSet<>();

        File file = new File(name);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()) {
            StringBuilder word = new StringBuilder(scanner.next());
            dictionaryWords.add(word);
        }
        return dictionaryWords;
    }
}
