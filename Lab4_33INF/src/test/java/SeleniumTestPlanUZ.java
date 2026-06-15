import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumTestPlanUZ {
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
    public void planNauczycieli() {
        driver.get("http://www.plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.linkText("Plan nauczycieli")).click();
        driver.findElement(By.linkText("B")).click();
        driver.findElement(By.linkText("dr inż. Jacek Bieganowski")).click();
        System.out.println(driver.findElement(By.cssSelector(".main")).getText());
        assertThat(driver.findElement(By.cssSelector(".main")).getText(), containsString("Seminarium Instytutowe IMEI"));
    }

    @Test
    public void planGrupa33() {
        driver.get("https://plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.linkText("Plan grup")).click();
        driver.findElement(By.linkText("Informatyka")).click();
        driver.findElement(By.linkText("33INF-SSI-SP Informatyka / stacjonarne / pierwszego stopnia z tyt. inżyniera")).click();
        System.out.println(driver.findElement(By.cssSelector(".main")).getText());
        assertThat(driver.findElement(By.cssSelector(".main")).getText(), containsString("Testowanie i rozwój aplikacji"));
    }

    @Test
    public void trybJasny() {
        driver.get("https://plan.uz.zgora.pl/");
        driver.manage().window().setSize(new Dimension(834, 5233));
        driver.findElement(By.cssSelector("#theme-toggle > .bi")).click();
        driver.findElement(By.cssSelector(".bi-sun-fill")).click();
        System.out.println(driver.findElement(By.tagName("body")).getCssValue("background-color"));
        assertThat(driver.findElement(By.tagName("body")).getCssValue("background-color"), is("rgb(243, 248, 236)"));
    }
}