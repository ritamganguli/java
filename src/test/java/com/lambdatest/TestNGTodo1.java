package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestNGTodo1 {

    private RemoteWebDriver driver;
    private String Status = "failed";

    @BeforeMethod
    public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        String hub = "@hub.lambdatest.com/wd/hub";

        Object browserOptions = null;
        // Chrome
        if (m.getName().contains("Chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.setPlatformName("Windows 10");
            options.setBrowserVersion("latest");
            HashMap<String, Object> ltOptions = new HashMap<String, Object>();
//            ltOptions.put("dedicatedProxy",true);
            ltOptions.put("project", "Untitled");
            ltOptions.put("geoLocation", "IN");
            options.setCapability("LT:Options", ltOptions);
            browserOptions = options;
        }
        // Edge
        else if (m.getName().contains("Edge")) {
            EdgeOptions options = new EdgeOptions();
            options.setPlatformName("Windows 10");
            options.setBrowserVersion("latest");
            HashMap<String, Object> ltOptions = new HashMap<String, Object>();
//            ltOptions.put("dedicatedProxy",true);
            ltOptions.put("geoLocation","IN");
            ltOptions.put("project", "Untitled");
            options.setCapability("LT:Options", ltOptions);
            browserOptions = options;
        }
        // Firefox
        else if (m.getName().contains("Firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPlatformName("Windows 10");
            options.setBrowserVersion("latest");
            HashMap<String, Object> ltOptions = new HashMap<String, Object>();
//            ltOptions.put("dedicatedProxy",true);
            ltOptions.put("geoLocation", "IN");
            ltOptions.put("project", "Untitled");
            options.setCapability("LT:Options", ltOptions);
            browserOptions = options;
        }

        driver = new RemoteWebDriver(new URL("https://" + "shubhamr" + ":" + "dl8Y8as59i1YyGZZUeLF897aCFvIDmaKkUU1e6RgBmlgMLIIhh" + hub), (Capabilities) browserOptions);
    }

    @Test
    public void testChrome() throws InterruptedException {
        String spanText;
        System.out.println("Loading Url");

        driver.get("https://www.merckgroup.com/en/careers/job-search.html?");

        System.out.println("Clicking on accept all cookies");
        driver.findElement(By.xpath("//button[@aria-label='accept cookies']")).click();
        Thread.sleep(10000);
        driver.findElement(By.xpath("//div[@class='se-list-job-item-title']/a")).click();
        Thread.sleep(10000);
        Status="passed";
    }

    @Test
    public void testEdge() throws InterruptedException {
        System.out.println("Loading Url");

        driver.get("https://www.merckgroup.com/en/careers/job-search.html?");

        System.out.println("Clicking on accept all cookies");
        driver.findElement(By.xpath("//button[@aria-label='accept cookies']")).click();

        Thread.sleep(10000);
        driver.findElement(By.xpath("//div[@class='se-list-job-item-title']/a")).click();
        Thread.sleep(10000);
        Status="passed";
    }

    @Test
    public void testFirefox() throws InterruptedException {
        System.out.println("Loading Url");

        driver.get("https://www.merckgroup.com/en/careers/job-search.html?");

        System.out.println("Clicking on accept all cookies");
        driver.findElement(By.xpath("//button[@aria-label='accept cookies']")).click();

        Thread.sleep(10000);
        driver.findElement(By.xpath("//div[@class='se-list-job-item-title']/a")).click();
        Thread.sleep(10000);
        Status="passed";
    }

    @AfterMethod
    public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }
}
