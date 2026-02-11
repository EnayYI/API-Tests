package Login.Locators;

import org.openqa.selenium.By;

public final class Locators {
    private Locators() {}

    public static final By login_field = By.id("username");
    public static final By password_field = By.id("password");
    public static final By login_button = By.id("submit-login");
    public static final By login_succes_text = By.id("flash");
    public static final By logout_button = By.xpath("//*[@id=\"core\"]/div/div/a");
    public static final By logout_succes_text = By.id("flash");
    public static final By wrong_login = By.id("flash");
    public static final By wrong_password = By.id("flash");



}