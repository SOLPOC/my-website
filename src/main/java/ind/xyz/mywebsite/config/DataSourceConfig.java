package ind.xyz.mywebsite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.driver-class-name}")
    public static String driverClassName;
    @Value("${spring.datasource.url}")
    public static String url;
    @Value("${spring.datasource.username}")
    public static  String username;
    @Value("${spring.datasource.password}")
    public static String password;
    @Bean
    public static DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
