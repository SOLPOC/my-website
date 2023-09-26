package ind.xyz.mywebsite.util.login;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import ind.xyz.mywebsite.config.JdbcConfig;
import ind.xyz.mywebsite.dto.UserDTO;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${pass}")
    String pass;
//    @Autowired
//    DataSource dataSource;
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @Autowired
//    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

/*    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Allow options request
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");

        if(JWTTokenUtil.verify(token)){
            return true;
        }
        // 失败我们跳转回登录页面
        request.setAttribute("msg","LOGIN ERROR");
        request.getRemoteHost();
        response.setStatus(401);
//        request.getRequestDispatcher("/login").forward(request,response);
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(UserHolder.getUser()==null){ // Do not log
            response.setStatus(401);
            return false;
        }
        return true;
    }*/

    /**
     * Use JWT, data in mysql to check
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check login status by token
        String token =request.getHeader("authorization");
        System.out.println(token);
        if(!JWTTokenUtil.verify(token)){ // Invalid token
            return false;
        }
/*        if(StrUtil.isBlank(token)){ // Not login
            return false;
        }*/

        // Query in mysql
//        String sql="select * from t_authorization where user='admin'";

//                jdbcTemplate.query(sql, new ResultSetExtractor<UserDTO>() {
//            @Override
//            public UserDTO extractData(ResultSet rs) throws SQLException, DataAccessException {
//                UserDTO userDTO = new UserDTO();
//                userDTO.setUsername(rs.getString("user"));
//                userDTO.setLoginTime(rs.getTimestamp("login_time"));
//                userDTO.setToken(rs.getString("token"));
//                return userDTO;
//            }
//        });

/*
        LocalDateTime expiration=userDto.getLoginTime().toLocalDateTime().plusMinutes(60*24);

        if(expiration.isBefore(LocalDateTime.now())){
            return false;
        }

        if(!userDto.getToken().equals(token)) { // Invalid token
            return false;
        }

        UserHolder.saveUser(userDto);
*/

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
