import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTestGov {
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
    public void CzyPodpisGrafikiJestPoprawny() {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://www.gov.pl/");
        driver.manage().window().setSize(new Dimension(1500, 1000));
        driver.findElement(By.id("cb2-accept")).click();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("query")));
        input.sendKeys("Donald Tusk", Keys.ENTER);
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Donald Tusk"))).click();

        WebElement img = driver.findElement(By.cssSelector(".main-photo img"));
        assertThat(img.getAttribute("alt"), is("Donald Tusk"));
    }

    @Test
    public void OdwolanieMandatDrogowy() {
        driver.get("https://www.gov.pl/");
        driver.manage().window().setSize(new Dimension(1327, 964));
        driver.findElement(By.id("cb2-accept")).click();
        driver.findElement(By.id("govpl-i-services_for_citizens")).click();
        driver.findElement(By.cssSelector("#kierowcy-i-pojazdy > button")).click();
        driver.findElement(By.linkText("Odwołaj się od mandatu drogowego")).click();
        System.out.println(driver.findElement(By.className("service-card-metric")).getText());
        assertThat(driver.findElement(By.className("service-card-metric")).getText(), containsString("Ministerstwo Spraw Wewnętrznych i Administracji"));
    }

    @Test
    public void SadzeniakiZiemniaka() {
        driver.get("https://www.gov.pl/");
        driver.manage().window().setSize(new Dimension(1920, 1036));
        driver.findElement(By.id("cb2-accept")).click();
        driver.findElement(By.id("farmer-tab")).click();
        driver.findElement(By.cssSelector("#services-farmer li:nth-child(4) span")).click();
        driver.findElement(By.cssSelector("#uprawa-roslin > button")).click();
        driver.findElement(By.linkText("Złóż wniosek o ponowną ocenę weryfikacyjną sadzeniaków ziemniaka")).click();
        System.out.println(driver.findElement(By.className("service-card-metric")).getText());
        assertThat(driver.findElement(By.className("service-card-metric")).getText(), containsString("Państwowa Inspekcja Ochrony Roślin i Nasiennictwa"));

    }
}