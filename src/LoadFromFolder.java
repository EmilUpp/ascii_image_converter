import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

/**
 * Loads a video from a filepath and constructs a list of images
 */
public class LoadFromFolder {
    public static void main(String[] args) {
        String[] files = findJPGInFolder("G:/Min enhet/Programmering/ascii_image_converter/give_you_up_image_list");
         for (String file: files) {
             System.out.println(file);
         }
    }

    public static String[] findJPGInFolder(String folderPath) {
        // This filter will only include files ending with .jpg
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File f, String name) {
                return name.endsWith(".jpg");
            }
        };

        File folder = new File(folderPath);
        File[] fileList = folder.listFiles(filter);
        //String[] filePathList = folder.list(filter);
        assert fileList != null;
        String[] filePathList = new String[fileList.length];

        for (int i = 0; i < filePathList.length; i++) {
            filePathList[i] = fileList[i].toString();
        }

        return filePathList;
    }
}
