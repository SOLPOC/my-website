package ind.xyz.mywebsite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Resource {
    private String id;
    private String name;
    private String size;
    private int state; // -1 private 1 public 0 encryption
    public String encryptionKey;
    public String verification;
    private String remark;
    private String url;
    private String type;
    private LocalDateTime uploadTime;
}
