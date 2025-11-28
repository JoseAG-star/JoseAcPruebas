package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CRUDFuncionalTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js; 
    private final StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // Test: testCreate
// Este test automatiza el llenado de un formulario para crear un usuario (Jose Ac G) en la web.
// Se espera que el título de la página se mantenga como "MERN CRUD" tras la operación.
    @Test
    @org.junit.jupiter.api.Order(1)
    public void testCreate() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("Jose Ac G");
        driver.findElement(By.name("email")).sendKeys("WaldoWick@gmail.com");
        driver.findElement(By.name("age")).sendKeys("22");
        
        driver.findElement(By.xpath("//div[text()='Gender']")).click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Male']")).click();
        
        driver.findElement(By.xpath("//button[text()='Add']")).click();
        assertEquals("MERN CRUD", driver.getTitle());
    }
// Test: testCaminoMalo
// Este test intenta crear un usuario con datos inválidos (email sin arroba) para verificar estabilidad.
// Se espera que el título de la página siga siendo "MERN CRUD" (el test verifica que no crashee, aunque no valida el error explícitamente en el código).
    @Test
    @org.junit.jupiter.api.Order(2)
    public void testCaminoMalo() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        driver.findElement(By.xpath("//div[@id='root']/div/div[2]/button")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("Usuario Error");
        driver.findElement(By.name("email")).sendKeys("email-sin-arroba.com");
        driver.findElement(By.name("age")).sendKeys("25");
        
        driver.findElement(By.xpath("//div[text()='Gender']")).click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Male']")).click();
        
        driver.findElement(By.xpath("//button[text()='Add']")).click();
        assertEquals("MERN CRUD", driver.getTitle());
    }
// Test: testUpdateUser
// Este test localiza un botón de editar, borra el nombre existente y escribe uno nuevo.
// Se espera que el botón "Save" sea clickeable y se ejecute la acción.
@Test
    @org.junit.jupiter.api.Order(3)
    public void testUpdateUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        WebElement editBtn = driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr[1]/td[5]/button[contains(text(),'Edit')]"));
        clickJS(editBtn);
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        
        nameField.click();  
        String valorActual = nameField.getAttribute("value");
        
        for (int i = 0; i < valorActual.length() + 3; i++) {
            nameField.sendKeys(Keys.BACK_SPACE);
        }
        
        Thread.sleep(500); 

        nameField.sendKeys("Jose Ac G");
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Save']")
        ));
        clickJS(saveBtn);
        
    }
// Test: testDeleteUser
// Este test hace clic en eliminar un usuario y confirma la acción en el modal emergente.
// Se espera que el botón de confirmación desaparezca (invisibilityOf) indicando que el modal se cerró.
    @Test
    @org.junit.jupiter.api.Order(4)
    public void testDeleteUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        WebElement deleteBtn = driver.findElement(By.xpath("//tbody/tr[1]//button[text()='Delete']"));
        clickJS(deleteBtn);

        WebElement confirmBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Yes' or text()='Delete']")
        ));
        
        Thread.sleep(500); 
        clickJS(confirmBtn);
        wait.until(ExpectedConditions.invisibilityOf(confirmBtn));
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
    
    public void clickJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }
}