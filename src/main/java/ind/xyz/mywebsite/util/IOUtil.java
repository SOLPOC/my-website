package ind.xyz.mywebsite.util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
    public static List<String>  getAllFilenames(String directoryPath) {
        List<String > filenames = new ArrayList<>();

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("输入的路径不是一个有效的目录！");
            return filenames;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filenames.add(file.getName());
            }
        }

        return filenames;
    }
}