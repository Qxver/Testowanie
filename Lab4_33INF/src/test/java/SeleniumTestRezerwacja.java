import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

public class SeleniumTestRezerwacja {
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
        driver.quit();
    }

    @Test
    public void testRegulaminLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://rezerwacja.zielona-gora.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Zapisujemy ID pierwotnej karty/okna
        String pierwotneOkno = driver.getWindowHandle();

        // Zamknięcie popupu informacyjnego
        wait.until(ExpectedConditions.elementToBeClickable(By.className("close")));
        driver.findElement(By.className("close")).click();

        // Przewijanie na dół strony do stopki
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // Kliknięcie w Regulamin (otwiera nową kartę)
        WebElement regulaminLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Regulamin")));
        regulaminLink.click();

        // Czekamy aż otworzy się druga karta i przełączamy się na nią
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(pierwotneOkno)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Weryfikacja adresu URL w nowo otwartej karcie
        wait.until(ExpectedConditions.urlContains("bip.zielonagora.pl"));
        assertThat(driver.getCurrentUrl(), containsString("Instrukcja_dostepu"));
    }

    @Test
    public void testPolitykaPrywatnosciLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://rezerwacja.zielona-gora.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Zapisujemy ID pierwotnej karty/okna
        String pierwotneOkno = driver.getWindowHandle();

        // Zamknięcie popupu informacyjnego
        wait.until(ExpectedConditions.elementToBeClickable(By.className("close")));
        driver.findElement(By.className("close")).click();

        // Przewijanie na dół strony
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // Kliknięcie w Politykę prywatności (otwiera nową kartę)
        WebElement politykaLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Polityka prywatności")));
        politykaLink.click();

        // Czekamy na drugą kartę i przełączamy kontekst testu
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(pierwotneOkno)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        // Weryfikacja adresu URL w nowej karcie
        wait.until(ExpectedConditions.urlContains("Ochrona_Danych_Osobowych"));
        assertThat(driver.getCurrentUrl(), containsString("Ochrona_Danych_Osobowych"));
    }

    @Test
    public void testPrzejscieDoUslugi() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://rezerwacja.zielona-gora.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));

        // Zamknięcie popupu
        wait.until(ExpectedConditions.elementToBeClickable(By.className("close")));
        driver.findElement(By.className("close")).click();

        String homepageUrl = driver.getCurrentUrl();

        // Kliknięcie w kafelek za pomocą XPath szukającego tekstu wewnątrz elementu (zamiast partialLinkText)
        WebElement serviceCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'department-card')]//div[contains(text(), 'Wydział Komunikacji')]")
        ));
        serviceCard.click();

        // Zweryfikuj, że URL się zmienił (przeszliśmy do wyboru spraw)
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe(homepageUrl)));
        assertNotEquals(homepageUrl, driver.getCurrentUrl());
    }
}