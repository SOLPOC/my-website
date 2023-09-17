package ind.xyz.mywebsite.util.file;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileReadUtil {

    public static String read(InputStream inputStream) throws IOException {
        @Cleanup
//        使用File
//        File file = new File(filePath);
//        FileInputStream fileInputStream = new FileInputStream(file);

//        FileInputStream（字节流） 实现了InputStream接口，用来读取文件中的字节流，参数是文件或者文件路径+文件名称

//        将 fileInputStream（字节流） 流作为参数，转为InputStreamReader（字符流）
        InputStreamReader reader = new InputStreamReader(inputStream);

//      将 字符流（参数）转为字符串流，带缓冲的流读取，默认缓冲区8k
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        StringBuilder sb=new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        inputStream.close();
        bufferedReader.close();
        return sb.toString();
    }

    public static String read(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        byte[] imageData = IOUtils.toByteArray(inputStream);
        String imageDataString = new String(imageData, StandardCharsets.UTF_8);
        return imageDataString;
    }

    public static String read(File file) throws IOException {
        @Cleanup
//        使用File
//        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

//        FileInputStream（字节流） 实现了InputStream接口，用来读取文件中的字节流，参数是文件或者文件路径+文件名称
//        FileInputStream fileInputStream = new FileInputStream(filePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = inputStreamReader.read()) != -1) {
            byteArrayOutputStream.write(i);
        }
//        System.out.println(byteArrayOutputStream.toString());
        fileInputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }

    public static String read(String path) throws IOException {
        File file=new File(path);
        @Cleanup
//        使用File
//        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

//        FileInputStream（字节流） 实现了InputStream接口，用来读取文件中的字节流，参数是文件或者文件路径+文件名称
//        FileInputStream fileInputStream = new FileInputStream(filePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = inputStreamReader.read()) != -1) {
            byteArrayOutputStream.write(i);
        }
//        System.out.println(byteArrayOutputStream.toString());
        fileInputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toString();
    }
}
