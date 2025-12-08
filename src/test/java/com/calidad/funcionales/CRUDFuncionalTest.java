package com.calidad.funcionales;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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

    // Variable para usar el mismo nombre
    @SuppressWarnings("FieldMayBeFinal")
    private String nombreOriginal = "Jose Ac G"; 

   @BeforeEach
public void setUp() throws Exception {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    options.addArguments("--start-maximized");
    // Agrega estas opciones para que sea más estable en CircleCI (headless mode)
    options.addArguments("--headless=new"); 
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--no-sandbox");

    driver = new ChromeDriver(options);
    
    // CAMBIO IMPORTANTE: Aumentar de 10 a 60 segundos
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    
    js = (JavascriptExecutor) driver;
}

    // TEST 1: CREATE
    @Test
    @Order(1)
    public void testCreate() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        // Esperamos a que el botón de agregar sea clickeable
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='root']/div/div[2]/button")));
        addBtn.click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys(nombreOriginal);
        driver.findElement(By.name("email")).sendKeys("WaldoWick@gmail.com");
        driver.findElement(By.name("age")).sendKeys("22");
        
        driver.findElement(By.xpath("//div[text()='Gender']")).click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Male']")).click();
        
        driver.findElement(By.xpath("//button[text()='Add']")).click();
        
        Thread.sleep(1500); // Espera para asegurar que se guardó en la tabla
        
        // Assert: Validamos que el nombre aparezca en el código de la página
        boolean usuarioExiste = driver.getPageSource().contains(nombreOriginal);
        assertTrue(usuarioExiste, "El usuario creado debería aparecer en la tabla.");
    }

    // TEST 2: CAMINO MALO
    @Test
    @Order(2)
    public void testCaminoMalo() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@id='root']/div/div[2]/button"))).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name"))).sendKeys("Usuario Error");
        driver.findElement(By.name("email")).sendKeys("email-sin-arroba.com"); 
        driver.findElement(By.name("age")).sendKeys("25");
        
        driver.findElement(By.xpath("//div[text()='Gender']")).click();
        driver.findElement(By.xpath("//div[@role='option']//span[text()='Male']")).click();
        
        WebElement addBtnModal = driver.findElement(By.xpath("//button[text()='Add']"));
        addBtnModal.click();

        // Assert: El botón debe seguir visible porque NO se debió cerrar el modal por el error
        assertTrue(addBtnModal.isDisplayed(), "El modal no debería cerrarse con datos inválidos.");
    }

    // TEST 3: UPDATE
    @Test
    @Order(3)
    public void testUpdateUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");
        
        // ESTRATEGIA: Vamos a la primera fila (tr[1]) que suele ser el último agregado
        // Usamos espera explícita para asegurarnos que la tabla cargó
        WebElement editBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//tbody/tr[1]//button[contains(text(),'Edit')]")
        ));
        clickJS(editBtn);
        
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        
        // LIMPIEZA ROBUSTA PARA REACT:
        // A veces clear() falla, así que seleccionamos todo (Ctrl+A) y borramos
        nameField.click();
        nameField.sendKeys(Keys.CONTROL + "a");
        nameField.sendKeys(Keys.DELETE);
        
        Thread.sleep(500); // Pequeña pausa visual
        String nuevoNombre = "Jose Editado";
        nameField.sendKeys(nuevoNombre);
        
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Save']")
        ));
        clickJS(saveBtn);
        
        Thread.sleep(1500); // Espera para que la tabla se actualice
        
        // Assert: Buscamos el nuevo nombre en la página
        boolean nombreActualizado = driver.getPageSource().contains(nuevoNombre);
        assertTrue(nombreActualizado, "El nombre en la tabla debería decir 'Jose Editado'.");
    }

    // TEST 4: DELETE
    @Test
    @Order(4)
    public void testDeleteUser() throws Exception {
        driver.get("https://mern-crud-mpfr.onrender.com/");

        // Borramos el de la primera fila (que debería ser "Jose Editado")
        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//tbody/tr[1]//button[contains(text(),'Delete')]")
        ));
        clickJS(deleteBtn);

        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'modal')]//button[text()='Yes' or text()='Delete']")
        ));
        
        Thread.sleep(500); 
        clickJS(confirmBtn);
        
        // Esperamos a que el botón de confirmación desaparezca (el modal se cierra)
        wait.until(ExpectedConditions.invisibilityOf(confirmBtn));
        
        Thread.sleep(1000); // Esperamos refresco de tabla

        // Assert: Verificamos que "Jose Editado" YA NO esté en la primera fila
        // Ojo: Si la tabla tiene muchos datos, mejor verificar que no esté en toda la pagina
        boolean usuarioSigueAhi = driver.getPageSource().contains("Jose Editado");
        assertFalse(usuarioSigueAhi, "El usuario 'Jose Editado' no debería aparecer tras borrarlo.");
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