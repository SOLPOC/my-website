package ind.xyz.mywebsite.util.file;
import ind.xyz.mywebsite.config.FileTransferProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
public class FileUtil {
    private static String path="D:/test";


    /**
         * 上传文件
         *
         * @param inputStream      文件
         * @param fileDirectory     服务器上要存储文件的路径
         * @param filename 服务器上要存储的文件的名称
         * @return
         */

    public static boolean saveToServer(InputStream inputStream, String fileDirectory, String filename) {
        InputStream ins = inputStream;
        FileOutputStream outs = null;
        try {
            File file = new File(fileDirectory);
            // 文件目录不存在则递归创建目录
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            //构建文件输出流
            outs = new FileOutputStream("D:/test/"+fileDirectory + File.separator+filename);
            int len;
            byte[] bytes = new byte[1024];
            //读取一个bytes的文件内容
            while ((len = ins.read(bytes)) != -1) {
                outs.write(bytes, 0, len);
            }
            outs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean saveToServer(StringBuffer stringBuffer, String fileDirectory, String filename) {
        FileOutputStream outs = null;
        try {
            File file = new File(fileDirectory);
            // 文件目录不存在则递归创建目录
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            //构建文件输出流
            outs = new FileOutputStream("D:/test/"+fileDirectory + File.separator+filename);
            outs.write(stringBuffer.toString().getBytes());
            outs.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean BatchToServer(MultipartFile[] multipartFiles, String fileDirectory, String[] filenames){
        boolean flag=true;
        for(int i=0;i<multipartFiles.length;i++){
            flag=flag&&uploadToServer(multipartFiles[i],fileDirectory,filenames[i]);
        }
        return flag;
    }

        public static boolean uploadToServer(MultipartFile multipartFile, String fileDirectory, String filename) {
            // 构建文件对象
            File file = new File(path+"/"+fileDirectory);
            // 文件目录不存在则递归创建目录
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            InputStream ins = null;
            FileOutputStream outs = null;
                try {
                        //获取文件输入流
                        ins = multipartFile.getInputStream();
                        //构建文件输出流
                        outs = new FileOutputStream("D:/test/" + fileDirectory + File.separator + filename);
                        int len;
                        byte[] bytes = new byte[1024];
                        //读取一个bytes的文件内容
                        while ((len = ins.read(bytes)) != -1) {
                            outs.write(bytes, 0, len);
                        }
                        outs.close();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outs != null) {
                            outs.close();
                        }
                        if (ins != null) {
                            ins.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            return false;
        }

        /**
         * 新文件上传
         *
         * @param multiFile      文件
         * @param fileDirectory     服务器上要存储文件的路径
         * @param filename 服务器上要存储的文件的名称
         * @return
         */
        public static boolean newUploadToServer(MultipartFile multiFile, String fileDirectory, String filename) {
            //构建文件对象
            File file = new File(fileDirectory);
            //文件目录不存在则递归创建目录
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            try {
                //获取文件输入流
                InputStream inputStream = multiFile.getInputStream();
                //构建文件输出流
                FileOutputStream outputStream = new FileOutputStream(fileDirectory + filename);
                int copy = FileCopyUtils.copy(inputStream, outputStream);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

    /**
     * 下载文件到服务器
     *
     * @param url      要下载的文件的地址
     * @param fileDirectory     服务器上存储的文件路径
     * @param filename 服务器上存储的文件名称
     * @return
     */
    public static boolean downloadFromServer(String url, String fileDirectory, String filename) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        boolean flag = false;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.connect();
            bis = new BufferedInputStream(connection.getInputStream());
            File file = new File(fileDirectory);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            String filePathName = fileDirectory + File.separator + filename;
            byte[] buf = new byte[1024];
            int size;
            fos = new FileOutputStream(filePathName);
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


    public static BufferedInputStream getBufferedInputStreamFromServer(String url) {
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        boolean flag = false;
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.connect();
            bis = new BufferedInputStream(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bis;
    }
}

