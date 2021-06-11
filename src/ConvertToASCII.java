import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Class for converting images to an ASCII string representation
 *
 * The image is first converted to grayscale
 * The grayness is then split inte regions depending on how rough the grayscale is
 * Each interval is then replaces by corresponding character defined in the grayscale
 * The chars is written into the string representation of the image
 *
 * 2 predefnied grayscales that uses 70 and 10 characters respectivly is included
 */
public class ConvertToASCII {
    public final static char[] longScale = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray();
    public final static char[] shortScale = ".:-=+*%#@".toCharArray();

    public static char[] grayscale = longScale;

    public static void main(String[] args) throws InterruptedException {
        String[] imagePathList = LoadFromFolder.findFileByEndingInFolder("G:\\Min enhet\\Programmering\\ascii_image_converter\\image_lists\\give_you_up_image_list", "jpg");
        final int scale = 3;
        char[] grayscale = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray();

        for (String imagePath : imagePathList) {
            long startTime = System.nanoTime();
            convertImageToAsciiString(ImageHandler.loadImage(imagePath, scale), grayscale);
            double convertTime = roundToNDecimal((System.nanoTime() - startTime)/1000000000.0, 7);
            Thread.sleep((long) Math.max(0, 200 - convertTime * 1000));
        }
    }
    /**
     * Converts an image to ASCII characters and prints it to console
     * Uses an array of ASCII character to represent the grayscale
     * Sample ratio of source image is set for font Lucida Console
     *
     * @param readImage BufferedImage with path to image
     * @return String
     */
    public static String convertImageToAsciiString(BufferedImage readImage, char[] grayscale) {
        int width = readImage.getWidth();
        int height = readImage.getHeight();

        // 5/3 - ratio for lucida console
        final double verticalSamplingScale = 5/3.0;

        // How many different grayness levels there are
        int roughness = 255 / (grayscale.length);

        PrintStream tmp = System.out;
        StringBuilder charArray = new StringBuilder();

        // Reads the image
        try{
            System.setOut(tmp);

            // String for storing all ASCII chars
            for (double y = 0; y < height; y+=roundToNDecimal(verticalSamplingScale, 1)) {
                for (int x = 0; x < width; x++){
                    int pixelValueBinary = readImage.getRGB(width - (x+1), (int) y);

                    // Reads the RGB values
                    int r = (pixelValueBinary>>16)&0xff;
                    int g = (pixelValueBinary>>8)&0xff;
                    int b = pixelValueBinary&0xff;

                    int avg = (r+g+b)/3;

                    int grayness = rgbToGrayness(avg, roughness);

                    if (!(new String(grayscale).equalsIgnoreCase("Amen"))){
                        charArray.append(graynessToASCII(grayness, grayscale, false));
                    }
                    else{
                        charArray.append(ConvertToBible.graynessToASCIIBible(4 - grayness));
                    }
                }
                // Splits the rows of chars
                charArray.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.setOut(tmp);
        }

        return charArray.toString();
    }

    /**
     * Converts rgb to grayness level depending on given roughness
     * @param rgb int average rgb value in one pixel 0-255
     * @param roughness int how many gray level there is
     * @return int representing the graynesslevel
     */
    public static int rgbToGrayness(int rgb, int roughness) {
        return 1 + rgb / (roughness);
    }

    /**
     * Rounds a number to n decimal places
     * @param value double value to round
     * @param n int how many digits
     * @return double representing the rounded value
     */
    public static double roundToNDecimal(double value, int n) {
        return (double)Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
    }

    /**
     * Converts a int representing grayness to corresponding ASCII char
     * @param grayness grayness scaled to amount of available ASCII chars
     * @return char
     */
    public static char graynessToASCII(int grayness, char[] asciiList, boolean reversed) {
        if (grayness > asciiList.length) {
            grayness = asciiList.length;
        }
        if (reversed) {
            return asciiList[grayness - 1];
        } else {
            return asciiList[asciiList.length - grayness];
        }
    }

    /**
     * Loads a file, reads it and returns the content as a string
     * @param filePath String the filepath to desired file
     * @return String the string representation of the file
     * @throws FileNotFoundException if the file is missing
     */
    public static String loadFromFile(String filePath) throws FileNotFoundException {
        File bibleFile = new File(filePath);

        Scanner fileReader = new Scanner(bibleFile);

        StringBuilder fileString = new StringBuilder();

        while (fileReader.hasNextLine()){
            fileString.append(fileReader.nextLine()).append("\n");
        }

        fileReader.close();

        return fileString.toString();
    }
}
