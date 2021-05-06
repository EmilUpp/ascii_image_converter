import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;


/**
 * Example of how to take single picture.
 *
 * @author Bartosz Firyn (SarXos)
 */
public class WebcamHandler {
    public static Webcam webcam;

    public BufferedImage liveImage;

    public WebcamHandler() {
        webcam = Webcam.getDefault();
        liveImage = null;
    }


    public static void main(String[] args) throws IOException {

        WebcamHandler handler = new WebcamHandler();

        handler.takeScreenshot();
        handler.saveLatestImage();
    }

    BufferedImage takeScreenshot() {
        if (!webcam.isOpen()) {
            webcam.open();
        }

        liveImage = webcam.getImage();

        return liveImage;
    }

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
