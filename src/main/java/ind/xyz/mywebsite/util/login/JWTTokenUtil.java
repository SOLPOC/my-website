package ind.xyz.mywebsite.util.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class JWTTokenUtil {

    private Environment environment;

    @Autowired
    private JWTTokenUtil(Environment environment) {
        this.environment = environment;
    }


    private static Environment env;

    //PostConstruct注解不可以加参数
    @PostConstruct
    public void init() {
        env = this.environment;
        TOKEN_SECRET=env.getProperty("token-secret");
    }

    private static final long EXPIRE_TIME= 60*60*1000*24; // One hour
    private static String TOKEN_SECRET="";  //密钥盐

    /**
     * Create a token
     * @return
     */
    public static String sign(String username,String id){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("JWT")
                    .withClaim("username", username)
                    .withClaim("id",id)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    public static String sign(){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("JWT")
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * Verify sign
     * @param token
     * @return
     */
    public static boolean verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("JWT").build();
            DecodedJWT jwt = verifier.verify(token);
//            System.out.println("认证通过：");
//            System.out.println("issuer: " + jwt.getIssuer());
//            System.out.println("username: " + jwt.getClaim("username").asString());
//            System.out.println("userId: " + jwt.getClaim("userId").asString());
//            System.out.println("id"+jwt.getClaim("id").asString());
//            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public static String getId(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("JWT").build();
        DecodedJWT jwt = verifier.verify(token);
        String id = jwt.getClaim("id").asString();
        return id;
    }
}

