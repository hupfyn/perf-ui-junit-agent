package perf.ui.junit.agent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import perf.ui.junit.agent.annotations.PerfUI;

import java.util.Objects;

public class DummyTest {

    private static WebDriver driver;
    private static WebDriverWait waiter;

    @Rule
    public PerfUIMetricGrabber perfUIMetricGrabber = new PerfUIMetricGrabber();


    @BeforeClass
    public static void setUp() {
        System.getenv();
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().fullscreen();
        waiter = new WebDriverWait(driver, 10);

    }

    @PerfUI(name = "Demo_test")
    @Test
    public void AmazonSearchWithParameters_1() {
        driver.get("https://www.amazon.com/s?k=Fender+Jazz+Bass");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @PerfUI
    @Test
    public void AmazonSearchWithParameters_2() {
        driver.get("https://www.amazon.com/s?k=Fender+Jaguar");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }
    @PerfUI
    @Test
    public void AmazonSearchWithParameters_3() {
        driver.get("https://www.amazon.com/s?k=Fender+Stratocaster");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @After
    public void audit() {
        perfUIMetricGrabber.runAudit(driver);
    }

    @AfterClass
    public static void shutDown(){
        if (!Objects.isNull(driver)) {
            driver.quit();
        }
    }
}
