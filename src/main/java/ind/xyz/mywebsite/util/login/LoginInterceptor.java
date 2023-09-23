package ind.xyz.mywebsite.util.login;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // Allow options request
//        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
//            return true;
//        }
//        String token = request.getHeader("Authorization");
//
//        if(JWTTokenUtil.verify(token)){
//            return true;
//        }
//        // 失败我们跳转回登录页面
//        request.setAttribute("msg","LOGIN ERROR");
//        request.getRemoteHost();
//        response.setStatus(401);
////        request.getRequestDispatcher("/login").forward(request,response);
//        return false;
//    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if(UserHolder.getUser()==null){ // Do not log
//            response.setStatus(401);
//            return false;
//        }
//        return true;
//    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check login status by token
        String token =request.getHeader("authorization");
        System.out.println(token);
        if(!JWTTokenUtil.verify(token)){ // Invalid token
            return false;
        }
        if(StrUtil.isBlank(token)){ // Not login
            return false;
        }

                // Query user in the redis
/*      Map<Object,Object> userMap=stringRedisTemplate.opsForHash().entries(RedisConstants.LOGIN_USER_KEY+token);
        System.out.println(userMap);
        if(userMap.isEmpty()){ // Fake user token
            return false;
        }
        UserDTO userDTO= BeanUtil.fillBeanWithMap(userMap,new UserDTO(),true);
        System.out.println("LI-userDTO"+userDTO);
        // Save UserDTO to thread local
        UserHolder.saveUser(userDTO);

        // Query in mysql
        String sql="select * from t_authorization where user='admin'";



    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
