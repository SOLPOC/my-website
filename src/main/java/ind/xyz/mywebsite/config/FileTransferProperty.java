package ind.xyz.mywebsite.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(value = "app")
public class FileTransferProperty {
    /**
     * 上传路径
     */
    private String server = "";

    /**
     * 下载路径
     */
    private String directory = "";

    /**
     * 文件类型
     */
    private String[] fileTypeArray;

    /**
     * 文件大小
     */
    private int maxFileSize;

    private String blogDirectory="";

    private String resourceDirectory="";
}
