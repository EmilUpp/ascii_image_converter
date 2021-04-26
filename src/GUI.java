import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Class for handling the GUI showing the converted live feed from the camera to ASCII chars
 *
 * GUI functionality:
 * Change in resolution of the picture displayed
 * The current FPS of the live feed
 * Start/Stopping the display
 * Customizable grayscale
 * Tool for bible study
 */
public class GUI {
    private JButton drawButton;
    private JPanel mainPanel;
    private JTextArea imageTextArea;
    private JLabel fpsLabel;
    private JSlider resolutionSlider;
    private JButton stopButton;
    private JButton chooseGrayscaleButton;

    Timer timer;

    int scale = 8;
    int fontSize = (int) (scale * (5/3f));

    long frameTime = 0;
    java.util.List<Double> frameRates = new ArrayList<>();

    WebcamHandler webcamHandler = new WebcamHandler();

    public GUI() {
        imageTextArea.setFont(new Font("Lucida Console", Font.PLAIN, fontSize));

        imageTextArea.setBackground(Color.BLACK);
        imageTextArea.setForeground(Color.WHITE);

        drawImage();

        drawButton.addActionListener(e -> {
            timer = new Timer(0, e1 -> drawImage());
            timer.start();
        });

        stopButton.addActionListener(e -> {
            if (timer != null && timer.isRunning()){
                timer.stop();
            }
        });
        chooseGrayscaleButton.addActionListener(e -> {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            grayScaleOption(new String(ConvertToASCII.grayscale));

            if (timer != null) {
                timer.start();
            }
        });
    }

    /**
     * Reads in the current image from the webcamera
     * Converts The image to a ASCII representation using the given grayscale
     * Writes the image to a textarea
     * Updates the framerate
     */
    public void drawImage() {
        frameTime = System.nanoTime();

        setResolution();

        BufferedImage image = webcamHandler.takeScreenshot();

        // Hopefully corrects for different webcam resolutions
        double webcamFactor = (image.getWidth() / 320f);
        image = ImageHandler.resizeImage(image, (scale/2.3f) * webcamFactor);

        resizeTextArea(image);

        String imageToDraw = ConvertToASCII.convertImageToAsciiString(image, ConvertToASCII.grayscale);

        imageTextArea.setText(imageToDraw);

        updateFrameRate(frameTime);
    }

    /**
     * Resizes the textarea to fit the text
     * @param readImage BufferedImage the image that's going to be written
     */
    private void resizeTextArea(BufferedImage readImage) {
        int width = (readImage.getWidth() * (scale));
        int height = (readImage.getHeight() * (scale));

        imageTextArea.setPreferredSize(new Dimension(width, height));
    }

    /**
     * Sets the resolution of the picture by adjusting the scale and fontsize
     * 5/3 is the ratio for the font used; Lucida Console
     */
    public void setResolution(){
        scale = resolutionSlider.getValue();
        fontSize = (int) (scale * (5/3f));
        imageTextArea.setFont(new Font("Lucida Console", Font.PLAIN, fontSize));
    }

    /**
     * Calculates the FPS by taking the average of the last 30 frames and write to the label
     * @param frameTime long elapsed time of current frame
     */
    public void updateFrameRate(long frameTime) {
        double fps = 1 / ConvertToASCII.roundToNDecimal((System.nanoTime() - frameTime) / 1000000000.0, 4);
        frameRates.add(fps);

        if (frameRates.size() > 30) {
            frameRates = frameRates.subList(frameRates.size() - 30, frameRates.size());
        }

        double sum = 0;

        for (Double frameRate : frameRates) {
            sum += frameRate;
        }

        double average = sum/frameRates.size();

        fpsLabel.setText(String.format("%.1f", average));
    }

    /**
     * Handles the option menu for changing and setting the grayscale used in converting the image
     * @param currentGrayscale String the current grayscale used
     */
    public void grayScaleOption(String currentGrayscale) {
        Object[] options = {"Confirm", "Presets", "Cancel"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Choose a grayscale"));
        JTextField textField = new JTextField(35);
        textField.setText(currentGrayscale);
        panel.add(textField);

        int result = JOptionPane.showOptionDialog(null, panel, "Grayscale",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        String nextGrayscale = textField.getText();

        if (nextGrayscale.length() == 0){
            nextGrayscale = " ";
        }

        if (result == JOptionPane.YES_OPTION) {
            ConvertToASCII.grayscale = nextGrayscale.toCharArray();
        }
        else if (result == 1){
            choosePreset();
        }
    }

    /**
     * Handles preset menu
     * Short sets grayscale to 10 characters
     * Long  sets grayscale to 70 characters
     * God mode loads in the bible
     */
    private void choosePreset() {
        Object[] options = {"Short", "Long", "God Mode", "Cancel"};

        JPanel panel = new JPanel();
        panel.add(new JLabel("Choose a preset"));

        int result = JOptionPane.showOptionDialog(null, panel, "Grayscale presets",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, null);

        String nextGrayscale = switch (result) {
            case 0 -> new String(ConvertToASCII.shortScale);
            case 1 -> new String(ConvertToASCII.longScale);
            case 2 -> "Amen";
            default -> new String(ConvertToASCII.grayscale);
        };

        grayScaleOption(nextGrayscale);

    }

    public static void main(String[] args) {
        //Skapa ditt fönster
        String namn = "Gui Test";
        JFrame frame = new JFrame(namn);
        //Tala om att du vill kunna stänga ditt förnster med krysset i högra hörnet
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Ange storleken på ditt fönster och att det ska vara fast
        frame.setSize(800, 600);
        frame.setResizable(false);
        //Positionera ditt fönster i mitten av skärmen
        frame.setLocationRelativeTo(null);

        //Skapa en instans av din den här klassen som hanterar din panel
        GUI myForm = new GUI();
        //Lägg in din panel i programfönstret
        frame.setContentPane(myForm.mainPanel);
        //Visa programfönstret på skärmen
        frame.setVisible(true);

        WebcamHandler.closeCameraOnExit();
    }
}
