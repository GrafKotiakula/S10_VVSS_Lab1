package S10.VVSS.lab1;

import S10.VVSS.lab1.config.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is used to run with in memory database
 * It must be used for tests only
 * */

@SpringBootApplication(exclude = App.class)
@EnableAutoConfiguration
@EnableWebMvc
public class InMemApp extends SpringBootServletInitializer {
    private final static Logger logger = LoggerFactory.getLogger(InMemApp.class);
    private final static String currentProfile = "inmem";
    private final static String dataFilePath = "database/test-db-data.sql";

    public static void main(String[] args) throws SQLException, ScriptException {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, currentProfile);
        ApplicationContext context = SpringApplication.run(InMemApp.class, args);

        logger.info("Datasource url: {}", context.getEnvironment().getProperty("spring.datasource.url"));
        logger.info("Password encoder: {}", context.getBean(PasswordEncoder.class).getClass().getSimpleName());

        DataSource dataSource = context.getBean(DataSource.class);
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource(dataFilePath));
            logger.info("Database loaded successfully");
        } catch (SQLException | ScriptException ex) {
            logger.error("Database load error: {}", ex.getMessage());
            SpringApplication.exit(context, () -> 1);
            throw ex;
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, currentProfile);
        super.onStartup(servletContext);
    }

    @Bean
    @Primary
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
