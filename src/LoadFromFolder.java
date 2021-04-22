import java.io.File;
import java.io.FilenameFilter;

/**
 * Extracts files from file directory by file ending
 */
public class LoadFromFolder {
    public static void main(String[] args) {
        String[] files = findFileByEndingInFolder("G:/Min enhet/Programmering/ascii_image_converter/give_you_up_image_list", "jpg");
         for (String file: files) {
             System.out.println(file);
         }
    }

    /**
     * Finds and returns all files in a directory witth specified ending
     * @param folderPath String the directory to search
     * @param ending String ending to return
     * @return String[] list of all filepaths
     */
    public static String[] findFileByEndingInFolder(String folderPath, String ending) {
        // This filter will only include files ending with .jpg
        FilenameFilter filter = (f, name) -> name.endsWith("." + ending);

        File folder = new File(folderPath);
        File[] fileList = folder.listFiles(filter);

        assert fileList != null;
        String[] filePathList = new String[fileList.length];

        for (int i = 0; i < filePathList.length; i++) {
            filePathList[i] = fileList[i].toString();
        }

        return filePathList;
    }
}
