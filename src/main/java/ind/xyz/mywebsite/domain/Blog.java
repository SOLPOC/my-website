package ind.xyz.mywebsite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {
    private String id;
    private String author;
    private String title;
    private String content;
    private String type; // .txt or .md
    private String url;
    private String category;
    private String language;
    private String tag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
