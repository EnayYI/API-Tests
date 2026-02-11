package Login.Logic;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Operations {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public Operations(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Ввести текст: {text}")
    public void type(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        el.sendKeys(text);
    }

    @Step("Нажать на элемент")
    public void click(By locator) {
        try {
            // 1) ждём что элемент можно нажать
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            // 2) пытаемся кликнуть обычным способом
            el.click();
        } catch (ElementClickInterceptedException e) {
            // если что-то перекрыло элемент:
            WebElement el = driver.findElement(locator);

            // 3) скроллим к элементу
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", el
            );

            // 4) кликаем через JS (как будто браузер сам нажал)
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();", el
            );
        }
    }

    @Step("Проверить, что элемент отображается на странице")
    public void shouldBeVisible(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Assertions.assertTrue(el.isDisplayed(), "Элемент не отображается: " + locator);
    }
}