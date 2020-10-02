import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Converts an image to ascii characters in a txt file
 * @author Emil Sitell
 */
public class ConvertToASCII {
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i=0; i < 10; i++) {
            toASCII();
            Thread.sleep(1);
        }
    }

    public static void toASCII() throws IOException {
        long startTime = System.nanoTime();
        final int SCALE = 4;
        BufferedImage readImage = ImageHandler.loadImage("G:\\Min enhet\\Programmering\\ascii_image_converter\\gunther_img.jpg", SCALE);
        double grayScaleTime = (System.nanoTime() - startTime)/1000000000.0;
        System.out.println(grayScaleTime + " seconds to load");

        int width = readImage.getWidth();
        int height = readImage.getHeight();
        double verticalSamplingScale = 5/3.0;

        // How many different grayness levels there are
        char[] asciiList = ".:-=+*%#@".toCharArray();
        int roughness = 255 / (asciiList.length - 1);

        PrintStream tmp = System.out;

        // Reads the image
        try{
            //PrintStream stream = new PrintStream("ASCII_image_test.txt");
            System.setOut(tmp);

            // String for storing all ASCII chars
            StringBuilder charArray = new StringBuilder();
            for (double y = 0; y < height; y+=roundToNDecimal(verticalSamplingScale, 1)) {
                for (int x = 0; x < width; x++){
                    int pixelValueBinary = readImage.getRGB(x, (int) y);

                    // Reads the RGB values
                    int a = (pixelValueBinary>>24)&0xff;
                    int r = (pixelValueBinary>>16)&0xff;
                    int g = (pixelValueBinary>>8)&0xff;
                    int b = pixelValueBinary&0xff;

                    int avg = (r+g+b)/3;

                    int grayness = rgbToGrayness(avg, roughness);
                    charArray.append(graynessToASCII(grayness, asciiList));
                }
                // Splits the rows of chars
                charArray.append("\n");
            }
            System.out.println(charArray);
        } finally {
            System.setOut(tmp);
        }

        double convertTime = roundToNDecimal((System.nanoTime() - startTime)/1000000000.0 - grayScaleTime, 7);
        System.out.println(convertTime + " seconds to convert");

        System.out.println();
        System.out.println("Original resolution: " + width * SCALE + "x" + height * SCALE);
        System.out.println("ASCII resolution:  " + width + "x"  + (height / verticalSamplingScale));

        System.out.println();
        System.out.println(grayScaleTime + convertTime + " seconds in total");
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
    public static char graynessToASCII(int grayness, char[] asciiList) {
        if (grayness > asciiList.length) {
            grayness = asciiList.length;
        }
        return asciiList[(asciiList.length) - grayness];
    }
}
