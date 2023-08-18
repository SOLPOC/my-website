package ind.xyz.mywebsite.service.impl;

import com.alibaba.fastjson.JSONObject;
import ind.xyz.mywebsite.config.FileTransferProperty;
import ind.xyz.mywebsite.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
@Service
public class FileServiceImpl {

    @Autowired
    private FileTransferProperty fileTransferProperty;

    public boolean download(HttpServletRequest request, HttpServletResponse response,String directory, String filename,String browserReceivedFilename) {
        String url = fileTransferProperty.getServer()+"/"+directory+"/"+filename;
        BufferedInputStream bins = null;
        BufferedOutputStream bouts = null;
        try {
            //同一个窗口下载多次，清除空白流
            response.reset();
            File file = new File(url);
            if (!file.exists()) {
                System.out.println("要下载的文件不存在");
                return false;
            }
            bins = new BufferedInputStream(new FileInputStream(url));
            bouts = new BufferedOutputStream(response.getOutputStream());
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            // 如果是火狐浏览器
            if (userAgent.contains("firefox")) {
                filename = new String(filename.getBytes(), "ISO8859-1");
            } else {
                filename = URLEncoder.encode(filename, "UTF-8");
            }
            //设置发送到客户端的响应的内容类型
            response.setContentType("application/download");
            //指定客户端下载的文件的名称
            if(browserReceivedFilename!=null) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" +browserReceivedFilename+ "\"");
            }else {
                response.setHeader("Content-disposition", "attachment;filename=" + filename);
            }
            int len;
            byte[] bytes = new byte[1024];
            while ((len = bins.read(bytes)) != -1) {
                bouts.write(bytes, 0, len);
            }
            //刷新流
            bouts.flush();
            System.out.println("下载完成");
            return true;
        } catch (IOException e) {
            System.out.println("下载文件异常:{}"+ e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bouts != null) {
                    bouts.close();
                }
                if (bins != null) {
                    bins.close();
                }
                return true;
            } catch (IOException e) {
                System.out.println("关闭流异常"+ e);
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Originally string returned
      * @param request
     * @param response
     * @param multiFile
     * @return
     */
    public String upload(HttpServletRequest request,HttpServletResponse response,String directory, MultipartFile multiFile) {
        JSONObject json = new JSONObject();
        try {
            Pair<Boolean, String> pair = checkFile(multiFile);
            if (!pair.getLeft()) {
                json.put("msg", pair.getRight());
                return json.toJSONString();
            }
            boolean b = FileUtil.uploadToServer(multiFile, directory, multiFile.getOriginalFilename());
            json.put("msg", b ? "上传成功" : "上传失败");
            return json.toJSONString();
        } catch (Exception e) {
            System.out.println("系统异常e:"+ e);
            json.put("msg", "上传失败");
            return json.toJSONString();
        }
    }

        public void uploadAll(HttpServletRequest request,HttpServletResponse response,String[] directories, MultipartFile[] multiFiles) {
            for (int i=0;i<multiFiles.length;i++) {
                upload(request,response,directories[i],multiFiles[i]);
            }

        }

    public Pair<Boolean, String> checkFile(MultipartFile multiFile) {
        if (multiFile.isEmpty()) {
            return Pair.of(false, "文件为空");
        }
        //获取
        String filename = multiFile.getOriginalFilename();
        String contentType = multiFile.getContentType();
        if (StringUtils.isBlank(filename)) {
            return Pair.of(false, "文件名为空");
        }
        long size = multiFile.getSize();//字节
        System.out.println("收到请求");
        //获取文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //判断配置的文件列表里是否支持该文件类型
        if (!ArrayUtils.contains(fileTransferProperty.getFileTypeArray(), suffix)) {
            return Pair.of(false, "不支持该类型文件上传");
        }
        double fileSize = size / 1024.0;//单位kb
        if (fileSize > fileTransferProperty.getMaxFileSize()) {
            return Pair.of(false, "文件大小超过限制");
        }
        return Pair.of(true, "验证通过");
    }

}
