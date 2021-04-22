import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Converts grayness to bible
 *
 * Lowers the sharpness, increases the holiness
 */
public class ConvertToBible {
    public static char[] bibleCharArray = "".toCharArray();
    static int currentIndex = 0;

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

    private static String nextBibleLetter() {
        String bibleLetter = String.valueOf(bibleCharArray[currentIndex]);

        currentIndex++;

        if (currentIndex >= bibleCharArray.length) {
            currentIndex = 0;
        }

        return bibleLetter;

    }

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
