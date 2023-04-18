package S10.VVSS.lab1.test.web;

import S10.VVSS.lab1.InMemApp;
import S10.VVSS.lab1.entities.user.UserService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest(classes = InMemApp.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = InMemApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebAuthenticationTest {
    private final ServerProperties serverProperties;
    private final UserService userService;
    private final WebDriver driver;
    private final String username = "user";
    private final String password = "Ab12345";
    private final long waitTime = 1000;

    @Autowired
    public WebAuthenticationTest(WebApplicationContext context) {
        serverProperties = context.getBean(ServerProperties.class);
        userService = context.getBean(UserService.class);
        driver = MockMvcHtmlUnitDriverBuilder
                .webAppContextSetup(context)
                .build();
    }

    @BeforeAll
    public void resetDatabase(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("database/test-db-clear.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("database/test-db-data.sql"));
        }
    }

    @AfterAll
    public void afterAll() {
        driver.close();
    }

    private void basePageCheck() {
        Assertions.assertEquals("S10_VVSS_TDL", driver.getTitle());

        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/main")));
        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/main/header")));
        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/main/div[@class='content']")));

        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/footer")));
        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/footer/div[@class='footer-text']")));
        Assertions.assertNotNull(driver.findElement(By.xpath("/html/body/footer/a[@class='btn-secondary']")));
    }

    @Test
    @Order(0)
    @DisplayName("open main page")
    public void openMainPage() {
        driver.get( String.format("http://localhost:%d/", serverProperties.getPort()) );

        basePageCheck();

        Assertions.assertTrue(driver.findElement(By.id("auth-popup")).isDisplayed());
        Assertions.assertTrue(driver.findElement(By.id("header-username")).getText().isEmpty());
        Assertions.assertEquals(0, driver.findElements(By.className("list-item")).size());
    }

    @Test
    @Order(1)
    @DisplayName("log in")
    public void login() throws InterruptedException {
        WebElement authUsernameInput = driver.findElement(By.xpath("//label[@id='username']/input"));
        Assertions.assertEquals("text", authUsernameInput.getAttribute("type"));
        authUsernameInput.sendKeys(username);

        WebElement authPasswordInput = driver.findElement(By.xpath("//label[@id='password']/input"));
        Assertions.assertEquals("password", authPasswordInput.getAttribute("type"));
        authPasswordInput.sendKeys(password);

        driver.findElement(By.id("auth-submit")).click();

        Thread.sleep(waitTime);

        Assertions.assertFalse(driver.findElement(By.id("auth-popup")).isDisplayed());
        Assertions.assertEquals(username, driver.findElement(By.id("header-username")).getText());
        Assertions.assertEquals(userService.findByUsername(username).map(u -> u.getList().size()).orElse(null),
                driver.findElements(By.className("list-item")).size() );
    }

    @Test
    @Order(2)
    @DisplayName("go to about page")
    public void openAboutPage() {
        driver.findElement(By.xpath("/html/body/footer/a[@class='btn-secondary']")).click();

        basePageCheck();
        Assertions.assertTrue(driver.getCurrentUrl().matches("http://[^/]+/about"));
        Assertions.assertTrue(driver.findElement(By.id("header-btn")).getAttribute("href").matches("http://[^/]+/"));
        Assertions.assertTrue(driver.findElement(By.xpath("//footer/a[@class='btn-secondary']"))
                .getAttribute("href").matches("http://[^/]+/"));
    }

    @Test
    @Order(3)
    @DisplayName("return to main page")
    public void returnToMainPage() throws InterruptedException{
        driver.findElement(By.xpath("/html/body/footer/a[@class='btn-secondary']")).click();

        Thread.sleep(waitTime);

        basePageCheck();
        Assertions.assertFalse(driver.findElement(By.id("auth-popup")).isDisplayed());
    }

    @Test
    @Order(4)
    @DisplayName("log out")
    public void logout() {
        driver.findElement(By.id("header-btn")).click();

        Assertions.assertTrue(driver.findElement(By.id("header-username")).getText().isEmpty());
        Assertions.assertEquals(0, driver.findElements(By.className("list-item")).size());
        Assertions.assertTrue(driver.findElement(By.id("auth-popup")).isDisplayed());
    }

}
