package com.calidad.funcionales;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.fail;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver; 
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException; 
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;

public class EdgeBusquedaTest { 
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js; // Este objeto nos permite hacer el scroll

    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.manage().window().maximize();
        
        // Aquí inicializamos el ejecutor de JavaScript
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testBusqueda() throws Exception {
        driver.get("https://www.bing.com/");

        // Búsqueda en Bing
        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.clear();
        searchBox.sendKeys("wolverine");
        searchBox.submit();
        driver.findElement(By.id("b_results"));
        js.executeScript("window.scrollBy(0, 400)");
        Thread.sleep(2000); 

        driver.get("https://es.wikipedia.org/wiki/Wolverine");
        
        assertEquals("Wolverine - Wikipedia, la enciclopedia libre", driver.getTitle());
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (driver != null) {
            driver.quit();
        }
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}