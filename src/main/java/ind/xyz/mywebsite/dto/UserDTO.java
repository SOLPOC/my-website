package ind.xyz.mywebsite.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {
//    private String id;
    private String username;
//    private String nickname;
    private String token;
    private Timestamp loginTime;
}
