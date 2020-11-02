import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Testing how to live update txt file during runtime
 */
public class FileTruncationTest {
    public static void main(String[] args) {
        PrintStream tmp = System.out;

        //TODO use notepad++ for automatic update
        try{
            PrintStream stream = new PrintStream("file_truncation_test.txt");
            System.setOut(stream);

            for (int i=0; i < 10; i++) {
                System.out.println(i);
                Thread.sleep(1000);
            }

        } catch (FileNotFoundException | InterruptedException e) {
            System.out.println(e);
        }
        finally {
            System.setOut(tmp);
        }

        }
    }
