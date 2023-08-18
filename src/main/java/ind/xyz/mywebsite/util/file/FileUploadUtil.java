package ind.xyz.mywebsite.util.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Component
public class FileUploadUtil {
    public static boolean uploadToServer(MultipartFile multipartFile  , String uploadPath, String uploadFileName) {
        try {
            File file = new File(uploadPath);
            file.mkdirs();
            InputStream inputStream=multipartFile.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("/usr/resource/success.txt");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

