package ind.xyz.mywebsite.util.file;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Component
public class FileDownloadUtil {
    public static boolean downloadFromServer(String server, String fileDirectory, String fileName) {
        try {
            String filePathName=fileDirectory+File.separator+fileName;
            FileOutputStream fileOutputStream = new FileOutputStream(filePathName);
            File file=new File(fileDirectory+File.separator+fileName);
            InputStream inputStream=new FileInputStream(file);
            BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
            byte[] buf = new byte[1024];
            int size;
            while ((size = bufferedInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, size);
            }
            bufferedInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
