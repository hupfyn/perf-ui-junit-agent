package perf.ui.junit.agent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import perf.ui.junit.agent.annotations.PerfUI;

import java.util.Objects;

@RunWith(PerfUITestRunner.class)
public class DummyTest {

    private WebDriver driver;
    private WebDriverWait waiter;

    public WebDriver getWebDriver() {
        return driver;
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(new ChromeOptions().addArguments("--remote-debugging-port=9222"));
        driver.manage().window().maximize();
        waiter = new WebDriverWait(driver, 10);
    }

    @PerfUI
    @Test
    public void AmazonSearchWithParameters_1() {
        driver.get("https://www.amazon.com/s?k=Fender+Jazz+Bass");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @Test
    public void AmazonSearchWithParameters_2() {
        driver.get("https://www.amazon.com/s?k=Fender+Jazz+Bass");
        waiter.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
        waiter.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(text(),'results')]")));
    }

    @After
    public void shutDown() {
        if (!Objects.isNull(driver)) {
            driver.quit();
        }
    }

}
