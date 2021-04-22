import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Converts an image to ascii characters in a txt file
 * @author Emil Sitell
 */
public class ConvertToASCII {
    public final static char[] longScale = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray();
    public final static char[] shortScale = ".:-=+*%#@".toCharArray();

    public static char[] grayscale = longScale;

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] imagePathList = LoadFromFolder.findJPGInFolder("G:\\Min enhet\\Programmering\\ascii_image_converter\\image_lists\\give_you_up_image_list");
        final int scale = 3;
        char[] grayscale = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ".toCharArray();

        for (String imagePath : imagePathList) {
            long startTime = System.nanoTime();
            printToASCII(ImageHandler.loadImage(imagePath, scale), grayscale);
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
    public static String printToASCII(BufferedImage readImage, char[] grayscale) {
        //long startTime = System.nanoTime();
        // BufferedImage readImage = ImageHandler.loadImage(filepath, scale);
        //double grayScaleTime = (System.nanoTime() - startTime)/1000000000.0;
        //System.out.println(grayScaleTime + " seconds to load");

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
            // Uncomment to write to given file
            //PrintStream stream = new PrintStream("ASCII_image_test.txt");
            System.setOut(tmp);

            // String for storing all ASCII chars
            //StringBuilder charArray = new StringBuilder();
            for (double y = 0; y < height; y+=roundToNDecimal(verticalSamplingScale, 1)) {
                for (int x = 0; x < width; x++){
                    int pixelValueBinary = readImage.getRGB(width - (x+1), (int) y);

                    // Reads the RGB values
                    int r = (pixelValueBinary>>16)&0xff;
                    int g = (pixelValueBinary>>8)&0xff;
                    int b = pixelValueBinary&0xff;

                    int avg = (r+g+b)/3;

                    int grayness = rgbToGrayness(avg, roughness);

                    if (!(new String(grayscale).equalsIgnoreCase("Pray"))){
                        charArray.append(graynessToASCII(grayness, grayscale, false));
                    }
                    else{
                        charArray.append(ConvertToBible.graynessToASCIIBible(4 - grayness));
                    }
                }
                // Splits the rows of chars
                charArray.append("\n");
            }
            // To make sure prior image is out of picture
            /*
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                               "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                               "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.println(charArray);

             */
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.setOut(tmp);
        }

        return charArray.toString();

/*        double convertTime = roundToNDecimal((System.nanoTime() - startTime)/1000000000.0 - grayScaleTime, 7);
        System.out.println(convertTime + " seconds to convert");

        System.out.println();
        System.out.println("Original resolution: " + width * SCALE + "x" + height * SCALE);
        System.out.println("ASCII resolution:  " + width + "x"  + (height / verticalSamplingScale));

        System.out.println();
        System.out.println(grayScaleTime + convertTime + " seconds in total");*/

    }

    public static int rgbToGrayness(int rgb, int roughness) {
        return 1 + rgb / (roughness);
    }

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
    public static void clearConsole(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error while clearing console");
        }
    }
}
