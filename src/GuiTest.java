import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class GuiTest {
    private JButton drawButton;
    private JPanel mainPanel;
    private JTextArea imageTextArea;
    private JButton changeResolutionButton;
    private JLabel fpsLabel;
    private JSlider resolutionSlider;
    private JButton stopButton;

    Timer timer;

    int currentImage = 0;
    String[] imagePathList;

    int scale = 8;
    int fontSize = (int) (scale * (5/3f));

    long frameTime = 0;
    java.util.List<Double> frameRates = new ArrayList<>();

    WebcamHandler webcamHandler = new WebcamHandler();

    public GuiTest() {

        imageTextArea.setFont(new Font("Lucida Console", Font.PLAIN, fontSize));

        imageTextArea.setBackground(Color.BLACK);
        imageTextArea.setForeground(Color.WHITE);

        imagePathList = LoadFromFolder.findJPGInFolder("G:\\Min enhet\\Programmering\\ascii_image_converter\\image_lists\\pokemon_image_list");


        drawButton.addActionListener(e -> {
            timer = new Timer(0, e1 -> {
                try {
                    drawImage();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            });
            timer.start();
        });
        /*
        changeResolutionButton.addActionListener(e -> {
            timer.stop();
            scale = (int) JOptionPane.showInputDialog(mainPanel, "Choose resolution", "Resolution", JOptionPane.PLAIN_MESSAGE, null, new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 4);
            fontSize = (int) (scale * (5/3f));
            imageTextArea.setFont(new Font("Lucida Console", Font.PLAIN, fontSize));
            timer.start();
        });

         */
        stopButton.addActionListener(e -> {
            if (timer.isRunning()){
                timer.stop();
            }
        });
    }

    public void drawImage() throws IOException, InterruptedException {
        frameTime = System.nanoTime();

        setResolution();

        String filepath = imagePathList[currentImage];

        BufferedImage image = webcamHandler.takeScreenshot();

        image = ImageHandler.resizeImage(image, scale/2f);

        resizeTextArea(image);

        //BufferedImage image = ImageHandler.loadImage(filepath, scale);

        long startTime = System.nanoTime();
        String imageToDraw = ConvertToASCII.printToASCII(image);
        double convertTime = ConvertToASCII.roundToNDecimal((System.nanoTime() - startTime)/1000000000.0, 7);
        // Thread.sleep((long) Math.max(0, 40 - convertTime * 1000));

        // String imageToDraw = ConvertToASCII.printToASCII("G:\\Min enhet\\Programmering\\ascii_image_converter\\images\\gunther_img.jpg", 5);

        imageTextArea.setText(imageToDraw);
        currentImage++;

        if (currentImage >= imagePathList.length) {
            currentImage = 0;
        }

        //fpsLabel.setText(String.format("%.2f", 1 / ConvertToASCII.roundToNDecimal((System.nanoTime() - frameTime) / 1000000000.0, 2)));
        updateFrameRate(frameTime);
    }

    private void resizeTextArea(String filepath) throws IOException {
        BufferedImage readImage = ImageHandler.loadImage(filepath, scale);
        int width = (readImage.getWidth() * (scale));
        int height = (readImage.getHeight() * (scale));

        imageTextArea.setPreferredSize(new Dimension(width, height));
    }

    private void resizeTextArea(BufferedImage readImage) throws IOException {
        int width = (readImage.getWidth() * (scale));
        int height = (readImage.getHeight() * (scale));

        imageTextArea.setPreferredSize(new Dimension(width, height));
    }

    public void setResolution(){
        scale = resolutionSlider.getValue();
        fontSize = (int) (scale * (5/3f));
        imageTextArea.setFont(new Font("Lucida Console", Font.PLAIN, fontSize));
    }

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
        GuiTest myForm = new GuiTest();
        //Lägg in din panel i programfönstret
        frame.setContentPane(myForm.mainPanel);
        //Visa programfönstret på skärmen
        frame.setVisible(true);
    }
}
