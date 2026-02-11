package Login.Tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import static Login.Locators.Locators.*;
import Login.Logic.Operations;
import org.openqa.selenium.chrome.ChromeOptions;

public class LoginTest {

    private WebDriver driver;
    private Operations action;


    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        action = new Operations(driver);
        driver.manage().window().maximize();
        driver.get("https://practice.expandtesting.com/login");

    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    //@Test
    void login() {
        action.type(login_field, "practice");
        action.type(password_field, "SuperSecretPassword!");
        action.click(login_button);
        action.shouldBeVisible(login_succes_text);
    }

    //@Test
    void logout(){
        action.type(login_field, "practice");
        action.type(password_field, "SuperSecretPassword!");
        action.click(login_button);
        action.click(logout_button);
        action.shouldBeVisible(logout_succes_text);
    }

    //@Test
    void wrong_login(){
        action.type(login_field, "practiceccccccc");
        action.type(password_field, "SuperSecretPassword!");
        action.click(login_button);
        action.shouldBeVisible(wrong_login);
    }

    //@Test
    void wrong_pass(){
        action.type(login_field, "practice");
        action.type(password_field, "SuperSecretPasswordsdfsd");
        action.click(login_button);
        action.shouldBeVisible(wrong_password);
    }

}
