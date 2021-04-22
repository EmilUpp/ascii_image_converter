import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Converts grayness to bible
 *
 * Lowers the sharpness, increases the holiness
 */
public class ConvertToBible {
    public static char[] bibleCharArray = "".toCharArray();
    static int currentIndex = 0;

    /**
     * Converts a grayness level to corresponding grayness from the bible by using " ", "." aswell as
     * lower and upper case characters from the bible
     *
     * Calls loadBible first time it needs to access it
     * @param grayness int 0-4 corresponding to grayness intervals
     * @return char corresponding to grayness
     * @throws FileNotFoundException raised if the bible file is missing
     */
    public static char graynessToASCIIBible(int grayness) throws FileNotFoundException {
        if (bibleCharArray.length == 0){
            loadBible();
        }

        String letter = " ";
        switch (grayness) {
            case 0 -> letter = " ";
            case 1 -> letter = ".";
            case 2 -> letter = nextBibleLetter().toLowerCase();
            case 3 -> letter = nextBibleLetter().toUpperCase();
        }

        return letter.charAt(0);
    }

    /**
     * Pulls the next character from the bible file
     * If the bible is gone through completely it wraps around
     * @return String containing the char
     */
    private static String nextBibleLetter() {
        String bibleLetter = String.valueOf(bibleCharArray[currentIndex]);

        currentIndex++;

        if (currentIndex >= bibleCharArray.length) {
            currentIndex = 0;
        }

        return bibleLetter;

    }

    /**
     * Loads the bible from the text file, strips it to extract the words
     * Converts it to a char array and updates bibleCharArray
     * @throws FileNotFoundException raised if the bible file is missing
     */
    public static void loadBible() throws FileNotFoundException {
        File bibleFile = new File("bibleFile.txt");

        Scanner fileReader = new Scanner(bibleFile);

        StringBuilder bibleString = new StringBuilder();

        while (fileReader.hasNextLine()){
            bibleString.append(fileReader.nextLine().strip());
        }

        fileReader.close();

        bibleCharArray = bibleString.toString().toCharArray();
    }
}
