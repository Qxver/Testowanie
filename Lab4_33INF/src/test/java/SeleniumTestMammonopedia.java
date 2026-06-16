import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTestMammonopedia {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:/Users/grzeg/Desktop/Testowanie/Lab4_33INF/geckodriver.exe");
        driver = new FirefoxDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testKategoriaMemy() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.mammonopedia.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Klikamy w odnośnik tekstowy "Memy" na stronie głównej
        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Memy")));
        categoryLink.click();

        // Weryfikujemy czy nagłówek strony zmienił się na "Kategoria:Memy"
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        assertThat(heading.getText(), is("Kategoria:Memy"));
    }

    @Test
    public void testPrzejscieDoLogowania() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.mammonopedia.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Szukamy przycisku logowania za pomocą uniwersalnego selektora CSS dla MediaWiki
        WebElement zalogujLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("#pt-login, #pt-login-2, a[href*='Zaloguj']")
        ));
        zalogujLink.click();

        // Sprawdzamy czy poprawnie weszliśmy na podstronę logowania
        wait.until(ExpectedConditions.urlContains("Zaloguj"));
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        assertThat(heading.getText(), containsString("Zaloguj"));
    }

    @Test
    public void testKategoriaMammonolodzy() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.mammonopedia.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Klikamy w odnośnik tekstowy "Mammonolodzy" na stronie głównej
        WebElement mammonolodzyLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Mammonolodzy")));
        mammonolodzyLink.click();

        // Weryfikujemy czy nagłówek strony zmienił się na "Kategoria:Mammonolodzy"
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstHeading")));
        assertThat(heading.getText(), is("Kategoria:Mammonolodzy"));
    }
}