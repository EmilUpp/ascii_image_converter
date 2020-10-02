import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Converts a image to grayscale
 */
public class ImageHandler {
    public static void main(String[] args) throws IOException {
        BufferedImage grayImage = convertToGrayScale("C:\\Users\\Emil Sitell\\IdeaProjects\\ascii_images\\gunther_img.jpg", 4);
    }

    public static BufferedImage convertToGrayScale(String filePath, int scale) throws IOException {
        BufferedImage img = null;
        File f = null;

        // read image
        try {
            f = new File(filePath);
            img = ImageIO.read(f);
        }
        catch (IOException e){
            System.out.println(e);
        }

        long startTime = System.nanoTime();
        img = resizeImage(img, (img.getWidth()/(scale)), (int) (img.getHeight()/(scale)));
        double grayScaleTime = (System.nanoTime() - startTime)/1000000000.0;
        System.out.println(grayScaleTime + " seconds to resize");

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

    public static String createGrayPath(String origin) {
        List<String> splitOrigin = Arrays.asList(origin.split("\\\\"));
        String imgName = splitOrigin.get(splitOrigin.size() - 1);
        splitOrigin = splitOrigin.subList(0, splitOrigin.size() - 1);

        return String.join("\\", splitOrigin) + "\\" + imgName.replace(".jpg", "") + "_grayscale.jpg";
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
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
     * @throws IOException when file is now found
     */
    public static BufferedImage loadImage(String filePath, int scale) throws IOException {
        BufferedImage img = null;
        File f = null;

        // read image
        try {
            f = new File(filePath);
            img = ImageIO.read(f);
        }
        catch (IOException e){
            System.out.println(e);
        }

        if (scale > 1) {
            img = resizeImage(img, (img.getWidth()/(scale)),(img.getHeight()/(scale)));
        }

        return img;
    }
}
