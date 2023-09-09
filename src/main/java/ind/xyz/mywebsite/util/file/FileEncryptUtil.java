package ind.xyz.mywebsite.util.file;



import cn.hutool.core.util.ByteUtil;
import ind.xyz.mywebsite.config.FileTransferProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.UUID;
@Component
public class FileEncryptUtil {

    @Autowired
    private FileTransferProperty ftp;

    private static FileTransferProperty fileTransferProperty;

    @PostConstruct
    public void init() {
        fileTransferProperty=ftp;
    }

    public static final String algorithm = "AES";
    // AES/CBC/NOPaddin
    // AES 默认模式
    // 使用CBC模式, 在初始化Cipher对象时, 需要增加参数, 初始化向量IV : IvParameterSpec iv = new
    // IvParameterSpec(key.getBytes());
    // NOPadding: 使用NOPadding模式时, 原文长度必须是8byte的整数倍
//    public static final String transformation = "AES/CBC/NOPadding";
    public static final String transformation = "AES/CBC/PKCS5Padding";
    public static String key="";


    /***
     * 加密
     * @param original 需要加密的参数（注意必须是16位）
     * @return
     * @throws Exception
     */
    public static String encryptByAES(String original) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        key= fileTransferProperty.getAesKey();
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(加密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 加密
        byte[] bytes = cipher.doFinal(original.getBytes());

        return Base64Util.encryptBASE64(bytes);
    }

    public static String encryptByAES(byte[] bs) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        key= fileTransferProperty.getAesKey();
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(加密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 加密
        byte[] bytes = cipher.doFinal(bs);

        return Base64Util.encryptBASE64(bytes);
    }

    public static String encryptByAES(String original, String key) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        key= fileTransferProperty.getAesKey();
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(加密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        // cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 加密
        byte[] bytes = cipher.doFinal(original.getBytes());

        return Base64Util.encryptBASE64(bytes);
    }

    /**
     * All files will be encrypted by same key
     * @param inputStream
     * @return
     * @throws Exception
     */
/*    public static InputStream encryptByAES(InputStream inputStream) throws Exception {
        byte buffer[] = new byte[1024];
        int length = -1;
        StringBuffer sb = new StringBuffer();
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            sb.append(encryptByAES(buffer.toString()));
        }
        String filename=fileTransferProperty.getTempDirectory()+"/"+ UUID.randomUUID().toString();
        OutputStream os = new FileOutputStream(filename);
        try {
            os.write(sb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file=new File(filename);
        InputStream inputStream1=new FileInputStream(file);
        return inputStream1;
    }*/

    public static StringBuffer encryptByAES(InputStream inputStream) throws Exception {
        byte buffer[] = new byte[1024];
        int length = -1;
        StringBuffer sb = new StringBuffer();
        while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            System.out.println(length);
/*            if (length<1024) { // Last chunk of data which can not be encrypted
                // Set a mark to declare the last
                for(int j=length;j<1024;j++){
                    buffer[j]=32; // DEL
                }
                System.out.println(buffer.length);
            }*/
            sb.append(new String(buffer));
        }
       return new StringBuffer( encryptByAES(sb.toString()));
    }

    public static  StringBuffer decryptByAES(InputStream inputStream) throws Exception {
/*        byte buffer[] = new byte[1024];
        int length = -1;
        StringBuffer sb = new StringBuffer();*/
      /*  while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
            System.out.println(length);
//            if (buffer[1023]==127) { // Last chunk of data which can not be encrypted
//                for(int i=0;i<length;i++) {
//                    sb.append(new String(new byte[]{buffer[i]}));
//                }
//            }
            sb.append(new String(buffer));
        }*/
        try {

            // 使用InputStreamReader将InputStream转换为Reader
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // 使用BufferedReader包装InputStreamReader，以便一次读取一行
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            // 逐行读取文件内容
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // 关闭资源
            bufferedReader.close();

            // 获取读取的字符串
            String fileContent = stringBuilder.toString();

            System.out.println(fileContent);
            String s = decryptByAES(fileContent);
            s=s.trim();
            return new StringBuffer(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     * @param encrypted 需要解密的参数
     * @return
     * @throws Exception
     */
    public static String decryptByAES(String encrypted) throws Exception {
        // 获取Cipher
        Cipher cipher = Cipher.getInstance(transformation);
        // 生成密钥
        key=fileTransferProperty.getAesKey();
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
        // 指定模式(解密)和密钥
        // 创建初始化向量
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
        // cipher.init(Cipher.DECRYPT_MODE, keySpec);
        // 解密
        byte[] bytes = cipher.doFinal(Base64Util.decryBASE64(encrypted));

        return new String(bytes);
    }
}
