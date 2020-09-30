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
        BufferedImage readImage = ImageHandler.loadImage("G:\\Min enhet\\Programmering\\ascii_image_converter\\gunther_img.jpg", 1);
        double grayScaleTime = (System.nanoTime() - startTime)/1000000000.0;
        System.out.println(grayScaleTime + " seconds to grayscale");

        int width = readImage.getWidth();
        int height = readImage.getHeight();

        // How many different grayness levels there are
        int roughness = ".:-=+*%#@".toCharArray().length - 1;

        PrintStream tmp = System.out;

        // Reads the image
        try{
            PrintStream stream = new PrintStream("ASCII_image_test.txt");
            System.setOut(stream);

            // String for storing all ASCII chars
            StringBuilder charArray = new StringBuilder();
            for (int y = 0; y < height; y+=2) {
                for (int x = 0; x < width; x++){
                    int pixelValueBinary = readImage.getRGB(x, y);

                    int a = (pixelValueBinary>>24)&0xff;
                    int r = (pixelValueBinary>>16)&0xff;
                    int g = (pixelValueBinary>>8)&0xff;
                    int b = pixelValueBinary&0xff;

                    int avg = (r+g+b)/3;

                    int grayness = rgbToGrayness(avg, roughness);
                    charArray.append(graynessToASCII(grayness));
                }
                // Splits the rows of chars
                charArray.append("\n");
            }
            System.out.println(charArray);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        finally {
            System.setOut(tmp);
        }
        double convertTime = roundToNDecimal((System.nanoTime() - startTime)/1000000000.0 - grayScaleTime, 7);
        System.out.println(convertTime + " seconds to convert");
        System.out.println();
        System.out.println(grayScaleTime + convertTime + " seconds in total");
    }

    public static int rgbToGrayness(int rgb, int roughness) {
        return 1 + rgb / (255/roughness);
    }

    public static double roundToNDecimal(double value, int n) {
        return (double)Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
    }

    public static char graynessToASCII(int grayness) {
        char[] asciiList = ".:-=+*%#@".toCharArray();
        if (grayness > asciiList.length) {
            grayness = asciiList.length;
        }
        return asciiList[(asciiList.length) - grayness];
    }
}
