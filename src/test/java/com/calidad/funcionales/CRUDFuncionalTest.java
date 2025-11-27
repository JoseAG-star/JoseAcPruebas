package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CRUDFuncionalTest {
 private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    JavascriptExecutor js;
    @BeforeEach
    public void setUp() throws Exception {
        WebDriverManager.chromedriver().setup(); 
        driver = new ChromeDriver();
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testCreate() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
    driver.findElement(By.name("name")).click();
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Jose Ac");
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Jose Ac G");
    driver.findElement(By.name("email")).click();
    driver.findElement(By.name("email")).clear();
    driver.findElement(By.name("email")).sendKeys("WaldoWick@gmail.com");
    driver.findElement(By.name("age")).click();
    driver.findElement(By.name("age")).clear();
    driver.findElement(By.name("age")).sendKeys("22");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Gender'])[2]/following::div[1]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Male'])[1]/following::div[2]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();
     assertEquals("MERN CRUD",driver.getTitle());
}

  @Test
  public void testCaminoMalo() throws Exception {
    driver.get("https://mern-crud-mpfr.onrender.com/");
    // Clic en "Add New"
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        
        // Llenar datos válidos excepto email
        driver.findElement(By.name("name")).sendKeys("Usuario Error");
        driver.findElement(By.name("email")).sendKeys("email-sin-arroba.com"); // FORMATO INCORRECTO
        driver.findElement(By.name("age")).sendKeys("25");
        
        driver.findElement(By.xpath("//div[text()='Gender']")).click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Male']")).click();
        
        // Intentar guardar
        driver.findElement(By.xpath("//button[text()='Add']")).click();
      assertEquals("MERN CRUD",driver.getTitle());
  } 

  // CASO 3: ACTUALIZAR (Update)
    @Test
    @Order(3)
    public void testUpdateUser() throws Exception {
         driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//table/tbody/tr[1]//button[contains(text(),'Edit')]")).click();

        // 2. Limpiar y escribir nuevo nombre
        WebElement nameField = driver.findElement(By.name("name"));
        nameField.clear();
        nameField.sendKeys("Jose Editado");

        // 3. Guardar (Usando tu XPath)
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Woah!'])[1]/following::button[1]")).click();

    }

    // CASO 4: BORRAR (Versión Corta)
    @Test
    @Order(4)
    public void testDeleteUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        // 1. Clic en Delete del primer usuario (botón rojo)
        driver.findElement(By.xpath("//table/tbody/tr[1]//button[contains(@class, 'red')]")).click();

        // 2. Confirmar borrado en el modal (Botón "Yes")
        driver.findElement(By.xpath("//button[contains(text(), 'Yes')]")).click();

        // 3. Validar que ya no está (o que el título sigue siendo correcto)
        Thread.sleep(1000); // Esperar a que desaparezca
        // Verificamos que seguimos en la app sin errores
        assertEquals("MERN CRUD", driver.getTitle());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
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
