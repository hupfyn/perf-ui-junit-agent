package perf.ui.junit.agent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import perf.ui.junit.agent.annotations.PerfUI;

import java.util.Objects;

public class DummyTest {

    private WebDriver driver;
    private WebDriverWait waiter;

    @Rule
    public PerfUIMetricGrabber perfUIMetricGrabber = new PerfUIMetricGrabber();

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
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
    public void shutDown() {
        perfUIMetricGrabber.runAudit(driver);
        if (!Objects.isNull(driver)) {
            driver.quit();
        }
    }

}
