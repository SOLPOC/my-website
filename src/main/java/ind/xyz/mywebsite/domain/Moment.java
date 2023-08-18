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
public class Moment {
    private String id;
    private String content;
    private String emotion;
    private String location;
    private String timezone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}