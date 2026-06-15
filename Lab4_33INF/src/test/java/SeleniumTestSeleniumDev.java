import static org.testng.AssertJUnit.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTestSeleniumDev {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "/home/user/Dokumenty/Testowanie/Lab4_33INF/geckodriver");
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void GithubRedirect() {
        driver.get("https://www.selenium.dev/selenium-ide/");
        driver.manage().window().setSize(new Dimension(1498, 1047));
        driver.findElement(By.linkText("Docs")).click();
        driver.findElement(By.linkText("Frequently Asked Questions")).click();
        driver.findElement(By.linkText("https://github.com/SeleniumHQ/selenium-ide/releases")).click();
        assertEquals("https://github.com/SeleniumHQ/selenium-ide/releases", driver.getCurrentUrl());
    }

    @Test
    public void FirefoxDownloadRedirect() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://www.selenium.dev/selenium-ide/");
        String firstWindow = driver.getWindowHandle();
        driver.manage().window().setSize(new Dimension(1498, 1047));
        driver.findElement(By.cssSelector(".section:nth-child(2) .pluginWrapper:nth-child(2) > .button")).click();
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        driver.switchTo().window(driver.getWindowHandle());

        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(firstWindow)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        wait.until(ExpectedConditions.urlToBe("https://addons.mozilla.org/en-GB/firefox/addon/selenium-ide/"));
        assertEquals("https://addons.mozilla.org/en-GB/firefox/addon/selenium-ide/", driver.getCurrentUrl());
    }

    @Test
    public void IconsPresent() {
        driver.get("https://www.selenium.dev/selenium-ide/");
        driver.manage().window().setSize(new Dimension(1498, 1047));
        List<WebElement> images = driver.findElements(
                By.cssSelector(".blockImage img")
        );

        assertEquals("computer", images.get(0).getAttribute("alt"));
        assertEquals("bullseye", images.get(1).getAttribute("alt"));
        assertEquals("lightning bolt", images.get(2).getAttribute("alt"));
    }
}