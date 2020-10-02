/*import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;

*//**
 * Loads a video from a filepath and constructs a list of images
 *//*
public class LoadVideo {
    public static void main(String[] args) throws Exception {
        //System.setProperty("C:\\Program Files\\Java\\jdk-14.0.2\\bin", "G/Min enhet/Programmering/Libraries/opencv/build/java/x64/opencv_java440.dll");

        Mat frame = new Mat();
        //0; default video device id
        VideoCapture camera = new VideoCapture(0);
        JFrame jframe = new JFrame("Title");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel vidpanel = new JLabel();
        jframe.setContentPane(vidpanel);
        jframe.setVisible(true);

        while (true) {
            if (camera.read(frame)) {
                ImageIcon image = new ImageIcon(Mat2BufferedImage.toBufferedImage(frame));
                vidpanel.setIcon(image);
                vidpanel.repaint();

            }
        }
    }
}*/
