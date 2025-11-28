package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UadyVirtualTest { 
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private final StringBuffer verificationErrors = new StringBuffer();
    @SuppressWarnings("unused")
    JavascriptExecutor js;

    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize(); 
    }
// Test: testContrasenaIncorrecta
// Este test intenta iniciar sesión en el portal con un correo y una contraseña falsa.
// Se espera que aparezca un elemento con id 'passwordError' (assertTrue errorMsn.isDisplayed).
    @Test
    public void testContrasenaIncorrecta() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
        driver.get("https://es.uadyvirtual.uady.mx/login/index.php");
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("virtual.uady.mx"))).click();
      
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("i0116")));
        emailInput.clear();
        emailInput.sendKeys("a22211726@alumnos.uady.mx"); 
      
        wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("i0118")));
        passwordInput.clear();
        passwordInput.sendKeys("ContraseñaFalsa12345"); 
    
        wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click(); 
        
        WebElement errorMsn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordError")));
        assertTrue(errorMsn.isDisplayed(), "El mensaje de error de contraseña no apareció.");
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

    @SuppressWarnings("unused")
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

     @SuppressWarnings("unused")
    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

     @SuppressWarnings("unused")
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