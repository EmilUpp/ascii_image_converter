import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;


/**
 * Class for handling the webcam in order to capture live image
 */
public class WebcamHandler {
    public static Webcam webcam;

    public BufferedImage liveImage;

    public WebcamHandler() {
        try{
            webcam = Webcam.getDefault();
        }
        catch (Exception e){
            webcam = null;
            System.out.println("Error with webcam");
        }

        liveImage = null;
    }


    public static void main(String[] args) throws IOException {

        WebcamHandler handler = new WebcamHandler();

        handler.takeScreenshot();
        handler.saveLatestImage();
    }

    /**
     * Takes a screenshot by reading the current image
     * @return BufferedImage the image live
     */
    BufferedImage takeScreenshot() {
        if (webcam == null){
            return new BufferedImage(256, 256,
                    BufferedImage.TYPE_INT_RGB);
        }

        if (!webcam.isOpen()) {
            webcam.open();
        }

        liveImage = webcam.getImage();

        return liveImage;
    }

    /**
     * Saves an image as a png
     * @throws IOException if anythings doesn't work out
     */
    void saveLatestImage() throws IOException {
        ImageIO.write(liveImage, "PNG", new File("test.png"));
    }

    /**
     * Makes sure the camera is closed on exit
     */
    public static void closeCameraOnExit() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> webcam.close()));
    }
}
