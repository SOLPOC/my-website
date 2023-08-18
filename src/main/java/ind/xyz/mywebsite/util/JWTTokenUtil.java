package ind.xyz.mywebsite.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTokenUtil {

    private static final long EXPIRE_TIME = 60 * 60 * 1000; // One hour
    private static final String TOKEN_SECRET = "Spanish";  //密钥盐

    /**
     * Create a token
     *
     * @return
     */
    public static String sign(String username, String id) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("JWT")
                    .withClaim("username", username)
                    .withClaim("id", id)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * Verify sign
     *
     * @param token
     * @return
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("JWT").build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getId(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("JWT").build();
        DecodedJWT jwt = verifier.verify(token);
        String id = jwt.getClaim("id").asString();
        return id;
    }
}

