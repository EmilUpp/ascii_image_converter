import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class for handling images
 *
 * Main functions:
 * convertGrayScale: Converts and image to grayscale
 * loadImage: Loads an image from path
 * mirrorVertically: Mirrors an image around a vertical centre line
 */
public class ImageHandler {
    /**
     * Converts an image to a grayscale representation
     * @param filePath String path of the image
     * @param scale int if the image should be rescaled
     * @return BufferedImage representing the grayscaled image
     */
    public static BufferedImage convertToGrayScale(String filePath, int scale) {
        BufferedImage img = null;
        File f;

        // read image
        try {
            f = new File(filePath);
            img = ImageIO.read(f);
        }
        catch (IOException ignored){
        }

        assert img != null;
        img = resizeImage(img, scale);

        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                int p = img.getRGB(x, y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                int avg = (r+g+b)/3;

                p = (a<<24) | (avg<<16) | (avg<<8) | avg;

                img.setRGB(x, y, p);
            }
        }

        // write to img
        try {
            f = new File(createGrayPath(filePath));
            ImageIO.write(img, "jpg", f);
            return ImageIO.read(f);
        }
        catch (IOException e){
            System.out.println();
        }

        return img;
    }

    /**
     * Creates a new path by adding grayscale
     * @param origin String with the old filepath
     * @return String the new filepath
     */
    public static String createGrayPath(String origin) {
        List<String> splitOrigin = Arrays.asList(origin.split("\\\\"));
        String imgName = splitOrigin.get(splitOrigin.size() - 1);
        splitOrigin = splitOrigin.subList(0, splitOrigin.size() - 1);

        return String.join("\\", splitOrigin) + "\\" + imgName.replace(".jpg", "") + "_grayscale.jpg";
    }

    /**
     * Resizes an image bu specified scale
     * Scale 1 is same as original, scale 2 is halving width and height and so on
     * @param originalImage BufferedImage the original image that is to be scaled
     * @param scale double the desired scale level
     * @return BufferedImage the resized image
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, double scale) {
        int targetWidth = (int) (originalImage.getWidth()/(scale));
        int targetHeight = (int) (originalImage.getHeight()/(scale));

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Loads image from filepath
     * @param filePath path to image
     * @param scale how much the image should be scaled down
     * @return BufferedImage with loaded image
     */
    public static BufferedImage loadImage(String filePath, int scale) {
        BufferedImage img = null;
        File f;

        // read image
        try {
            f = new File(filePath);
            img = ImageIO.read(f);
        }
        catch (IOException e){
            System.out.printf("File \"%s\" not found%n", filePath);
        }

        if (scale > 1 && img != null) {
            img = resizeImage(img, scale);
        }

        return img;
    }

    /**
     * Mirrors an image vertically around the centre line
     * @param originalImage BufferedImage the image to mirror
     * @return BufferedImage the mirrored image
     */
    public static BufferedImage mirrorVertically(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 2; x++) {
                int firstPixel = originalImage.getRGB(x, y);
                int tempMirrorPixel = originalImage.getRGB(width - (x+1), y);

                originalImage.setRGB(width - (x+1), y, firstPixel);
                originalImage.setRGB(x, y, tempMirrorPixel);
            }
        }

        return originalImage;
    }
}
