package ind.xyz.mywebsite.util;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Scanner;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ConvertUtil {
    public static String[] objectArray2StringArray(Object[] objectArray) {
        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; i++) {
            stringArray[i] = objectArray[i].toString();
        }
        return stringArray;
    }

    public static String inputStream2String(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    public static String convertImageToBase64(String imagePath) {
        String base64Image = "";
        try {
            File imageFile = new File(imagePath);
// 读取图像文件
            BufferedImage image = ImageIO.read(imageFile);

// 创建一个字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

// 将图像写入字节数组输出流，格式为PNG
            ImageIO.write(image, "png", baos);

// 执行Base64编码
            byte[] imageBytes = baos.toByteArray();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);

// 关闭字节数组输出流
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Image;
    }

    public static String getImgStrToBase64(String imgStr) {
        InputStream inputStream = null;
        byte[] buffer = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();){
            //判断网络链接图片文件/本地目录图片文件
            if (imgStr.startsWith("http://") || imgStr.startsWith("https://")) {
                // 创建URL
                URL url = new URL(imgStr);
                // 创建链接
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                inputStream = conn.getInputStream();
                // 将内容读取内存中
                buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                buffer = outputStream.toByteArray();
            } else {
                inputStream = new FileInputStream(imgStr);
                int count = 0;
                while (count == 0) {
                    count = inputStream.available();
                }
                buffer = new byte[count];
                inputStream.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 对字节数组Base64编码
        Encoder encode = Base64.getEncoder();
        return encode.encodeToString(buffer);
    }
}
