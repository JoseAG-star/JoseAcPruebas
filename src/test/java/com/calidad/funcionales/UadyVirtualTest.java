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

public class UadyVirtualTest { // Nombre de clase actualizado
    private WebDriver driver;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
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

    @Test
    public void testContrasenaIncorrecta() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 

        // 1. Ir a la página
        driver.get("https://es.uadyvirtual.uady.mx/login/index.php");

        // 2. Click en botón de login Microsoft
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("virtual.uady.mx"))).click();

        // 3. Ingresar Correo (Debe ser un correo VÁLIDO para llegar a la pantalla de password)
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("i0116")));
        emailInput.clear();
        emailInput.sendKeys("a22211726@alumnos.uady.mx"); 
      
        wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click();

        // 4. Ingresar Contraseña INCORRECTA
        WebElement passwordInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("i0118")));
        passwordInput.clear();
        // ENVIAMOS UNA CONTRASEÑA FALSA INTENCIONALMENTE
        passwordInput.sendKeys("ContraseñaFalsa12345"); 
        
        // Intentar iniciar sesión
        wait.until(ExpectedConditions.elementToBeClickable(By.id("idSIButton9"))).click(); 

        // 5. VALIDACIÓN DEL ERROR
        // En lugar de esperar entrar al menú, esperamos que aparezca el mensaje de error de Microsoft.
        // El ID del mensaje de error de password en Microsoft suele ser "passwordError".
        
        WebElement errorMsn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("passwordError")));
        
        // Verificamos que el mensaje de error esté visible
        assertTrue(errorMsn.isDisplayed(), "El mensaje de error de contraseña no apareció.");
        
        // Opcional: Verificar el texto del error
        // assertTrue(errorMsn.getText().contains("contraseña"), "El texto del error no es el esperado.");
        
        System.out.println("Prueba Exitosa: Se detectó el mensaje de error correctamente.");
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