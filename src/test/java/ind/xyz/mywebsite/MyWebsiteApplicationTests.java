package ind.xyz.mywebsite;

import ind.xyz.mywebsite.domain.Moment;
import ind.xyz.mywebsite.mapper.MomentMapper;
import ind.xyz.mywebsite.util.file.*;
import ind.xyz.mywebsite.util.md.MdUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.List;

@SpringBootTest
class MyWebsiteApplicationTests {

    @Test
    void contextLoads() {
            String downloadUrl = "http://13.113.66.74:80/xyz/resource/bgVideo.mp4";
            String filePath = "C:\\Users\\wyf\\desktop";
            String fileName = "download.mp4";
            FileUtil.downloadFromServer(downloadUrl, filePath, fileName);
    }

//    @Before
//    void getSqlSessionFactory() throws IOException {
//            // Init SqlSessionFactory
//            String resource = "mybatis-config.xml";
//            InputStream inputStream = Resources.getResourceAsStream(resource);
//            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        }


    private SqlSessionFactory sqlSessionFactory;
    @Test
    void test1() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        MomentService momentService=new MomentServiceImpl();
//        System.out.println(momentService.getAll());
        SqlSession session = sqlSessionFactory.openSession();
        MomentMapper mapper = session.getMapper(MomentMapper.class);
        List<Moment> moments = mapper.getAll();
        session.close();
        System.out.println(moments);
    }
    @Test
    void test2(){
        String html = MdUtil.MdToHtmlForApiDoc("# title");
        System.out.println(html);
    }
    @Test
    void test3(){
        FileUtil.uploadToServer(null,null,"md.md");
    }
//    @Test
//    void test4(){
//        FileUploadUtil.uploadToServer(new File("C:\\Users\\wyf\\desktop\\txt.txt"),null,null);
//    }

    @Test
    void test5() throws Exception{
        String url = "http://13.113.66.74:80/usr/resource";
        String charset = "UTF-8";
        String param = "value";
        File textFile = new File("C:\\Users\\wyf\\desktop\\txt.txt");
        File binaryFile = new File("C:\\Users\\wyf\\desktop\\txt.txt");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();

            // Send text file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
            writer.append(CRLF).flush();
            Files.copy(textFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(binaryFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        }

// Request is lazily fired whenever you need to obtain information about response.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); // Should be 200
    }

    @Test
    void test6() throws Exception {
        File file=new File("C:\\Users\\wyf\\desktop\\test.jpg");
        String read = FileReadUtil.read(file);
//        System.out.println(read);
        System.out.println(read.length());
        String string = FileAESUtil.encrypt(read);
        System.out.println(string.length());
        String decryption = FileAESUtil.decrypt(string);
//        System.out.println(decryption.length());
        System.out.println(decryption.equals(read));

    }
    @Test
    void test7() throws Exception {
    }

}
