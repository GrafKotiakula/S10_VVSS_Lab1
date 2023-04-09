package S10.VVSS.lab1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
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

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebMvc
public class InMemApp extends SpringBootServletInitializer {
    private final static Logger logger = LoggerFactory.getLogger(InMemApp.class);
    private final static String currentProfile = "inmem";
    private final static String dataFilePath = "database/test-db-data.sql";

    public static void main(String[] args) throws SQLException, ScriptException {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, currentProfile);
        ApplicationContext context = SpringApplication.run(App.class, args);

        DataSource dataSource = context.getBean(DataSource.class);
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource(dataFilePath));
        } catch (SQLException | ScriptException ex) {
            logger.error(ex.getMessage());
            SpringApplication.exit(context, () -> 1);
            throw ex;
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, currentProfile);
        super.onStartup(servletContext);
    }

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
