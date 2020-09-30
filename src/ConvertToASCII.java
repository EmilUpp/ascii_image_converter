import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Converts an image to ascii characters in a txt file
 */
public class ConvertToASCII {
    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        BufferedImage grayImage = Grayscale.convertToGrayScale("G:\\Min enhet\\Programmering\\ascii_image_converter\\give_you_up.jpg", 1);
        System.out.println((System.nanoTime() - startTime)/1000000000.0 + " seconds to grayscale");
        int width = grayImage.getWidth();
        int height = grayImage.getHeight();

        // How many different grayness levels there are
        int roughness = ".:-=+*%#@".toCharArray().length - 1;

        PrintStream tmp = System.out;

        try{
            PrintStream stream = new PrintStream("ASCII_image_test.txt");
            System.setOut(stream);

            for (int y = 0; y < height; y+=4) {
                StringBuilder charRow = new StringBuilder();
                for (int x = 0; x < width; x++){
                    int p = grayImage.getRGB(x, y);

                    int b = p&0xff;

                    int grayness = rgbToGrayness(b, roughness);
                    charRow.append(graynessToASCII(grayness));
                    //System.out.print(grayness + " ");
                }
                System.out.println(charRow);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        finally {
            System.setOut(tmp);
        }

        System.out.println((System.nanoTime() - startTime)/1000000000.0 + " seconds total");
    }

    public static int rgbToGrayness(int rgb, int roughness) {
        return 1 + rgb / (255/roughness);
    }

    public static double roundToNDecimal(double value, int n) {
        return (double)Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
    }

    public static char graynessToASCII(int grayness) {
        //char[] asciiList = new char[] {' ','.',':','-','=','+','*','%','@','#'};
        char[] asciiList = ".:-=+*%#@".toCharArray();
        if (grayness > asciiList.length) {
            grayness = asciiList.length;
        }
        return asciiList[(asciiList.length) - grayness];
    }

    public static char[] stringToCharList(String string) {
        return string.toCharArray();
    }
}
