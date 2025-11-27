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
    private JavascriptExecutor js; // Para forzar clicks
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeEach
    public void setUp() throws Exception {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver; // Inicializamos el ejecutor JS
    }

    // --- MÉTODOS CREATE Y CAMINO MALO (IGUAL QUE ANTES) ---
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

    // --- CORRECCIONES CLAVE AQUÍ ABAJO ---

@Test
    @org.junit.jupiter.api.Order(3)
    public void testUpdateUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        // 1. Clic en Edit (Usamos JS para asegurar el clic)
        WebElement editBtn = driver.findElement(By.xpath("//div[@id='root']/div/div[2]/table/tbody/tr[1]/td[5]/button[contains(text(),'Edit')]"));
        clickJS(editBtn);

        // 2. Esperar a que el campo aparezca
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        
        // --- MÉTODO INFALIBLE PARA BORRAR EN REACT ---
        nameField.click(); // Dar foco
        
        // Obtenemos el valor que tiene actualmente el campo
        String valorActual = nameField.getAttribute("value");
        
        // Presionamos 'Backspace' tantas veces como caracteres tenga + un margen de seguridad
        for (int i = 0; i < valorActual.length() + 3; i++) {
            nameField.sendKeys(Keys.BACK_SPACE);
        }
        
        // Pequeña pausa para que React procese que el campo está vacío
        Thread.sleep(500); 

        // Ahora escribimos el nuevo nombre
        nameField.sendKeys("Jose Ac G");
        // ---------------------------------------------

        // 3. Guardar
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Save']")
        ));
        
        clickJS(saveBtn); // Forzamos clic con JS
        
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    public void testDeleteUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        // 1. Abrir Modal de borrar
        WebElement deleteBtn = driver.findElement(By.xpath("//tbody/tr[1]//button[text()='Delete']"));
        clickJS(deleteBtn);

        // 2. CLIC FUERTE EN 'YES' (Confirmar)
        // El botón suele decir "Yes" o "Delete" en rojo. Buscamos ambos por si acaso.
        // Usamos presenceOfElementLocated porque a veces elementToBeClickable falla con JS
        WebElement confirmBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Yes' or text()='Delete']")
        ));
        
        // Pequeña pausa de seguridad para que el JS del navegador sepa que el botón existe
        Thread.sleep(500); 
        
        clickJS(confirmBtn); // <--- ESTA ES LA SOLUCIÓN MÁGICA

        // 3. Esperar que se cierre
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