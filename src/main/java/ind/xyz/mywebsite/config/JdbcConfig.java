package ind.xyz.mywebsite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
@Configuration
//@ConfigurationProperties(prefix = "spring.datasource")
public class JdbcConfig {
    @Autowired
    private  static DataSource dataSource;

    @Bean
    public static JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }


}
