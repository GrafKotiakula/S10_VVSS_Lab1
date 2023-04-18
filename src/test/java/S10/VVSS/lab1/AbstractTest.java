package S10.VVSS.lab1;

import S10.VVSS.lab1.entities.listitem.ListItemService;
import S10.VVSS.lab1.entities.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = InMemApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = InMemApp.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AbstractTest {
    public static final String parametrizedTestPattern = "{index} - {0}";

    protected WebApplicationContext context;
    protected UserService userService;
    protected ListItemService listItemService;
    protected ObjectMapper mapper;
    protected MockMvc mockMvc;

    public AbstractTest(WebApplicationContext context) {
        this.context = context;
        userService = context.getBean(UserService.class);
        listItemService = context.getBean(ListItemService.class);
        mapper = context.getBean(ObjectMapper.class);
    }

    protected MockHttpServletRequestBuilder defaultRequestBuilder() {
        return get("/")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @BeforeAll
    public void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        resetDatabase(dataSource);
        buildMockMvc();
    }

    public void resetDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("database/test-db-clear.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("database/test-db-data.sql"));
        }
    }

    public void buildMockMvc() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(context.getBean(FilterChainProxy.class).getFilters("/**").toArray(Filter[]::new))
                .defaultRequest(defaultRequestBuilder())
                .alwaysDo(print());
        mockMvc = builder.build();
    }

    protected String buildRepeatableString(String str, int count){
        StringBuilder builder = new StringBuilder();
        while (count > 0) {
            --count;
            builder.append(str);
        }
        return builder.toString();
    }
}
