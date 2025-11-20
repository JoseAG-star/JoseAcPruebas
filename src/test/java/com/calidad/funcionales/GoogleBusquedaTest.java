package com.calidad.funcionales;

import java.util.regex.Pattern;
// Eliminamos la importación de NoSuchElementException de java.util
import java.util.concurrent.TimeUnit;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Imports de JUnit necesarios para assertEquals y fail
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.fail;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration; // Importación para Duration

// IMPORTS DE SELENIUM CRÍTICAS
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException; 
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Alert;

public class GoogleBusquedaTest {
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
  public void testBusqueda() throws Exception {
    driver.get("https://www.google.com/search?q=wolverine&oq=wolverine&gs_lcrp=EgZjaHJvbWUqBwgAEAAYjwIyBwgAEAAYjwIyCggBEC4YsQMYgAQyCggCEC4YsQMYgAQyCggDEC4YsQMYgAQyDwgEECMYJxiABBi0BBiKBTIMCAUQIxgnGIAEGIoFMgcIBhAAGIAEMgcIBxAuGIAEMgcICBAAGIAEMhAICRAuGIMBGLEDGIAEGIoF0gEIMjM5M2owajeoAgCwAgA&sourceid=chrome&ie=UTF-8");
    driver.findElement(By.id("APjFqb")).click();
    driver.get("https://www.google.com/search?q=wolverine&sca_esv=a828f4b37f770271&sxsrf=AE3TifOEE3egJmcKI9wBBN2DQWXDezxphA%3A1762821309832&ei=vYQSacbBMtqyqtsPnJW9wQE&ved=0ahUKEwiGlOaP7eiQAxVamWoFHZxKLxgQ4dUDCBE&uact=5&oq=wolverine&gs_lp=Egxnd3Mtd2l6LXNlcnAiCXdvbHZlcmluZTINECMYtAQY8AUYsAMYJzIHECMYsAMYJzIHECMYsAMYJzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzIKEAAYRxjWBBiwAzINEAAYgAQYigUYQxiwAzINEAAYgAQYigUYQxiwAzIOEAAY5AIY1gQYsAPYAQEyDhAAGOQCGNYEGLAD2AEBMg4QABjkAhjWBBiwA9gBATIQEC4YRxjWBBjIAxiwA9gBATITEC4YgAQYigUYQxjIAxiwA9gBATITEC4YQxiABBiKBRjIAxiwA9gBATITEC4YQxiABBiKBRjIAxiwA9gBAUiYDVAeWIoHcAJ4AZABAJgBmQKgAZkCqgEDMi0xuAEDyAEA-AEBmAIDoAK6AsICChAjGIAEGIoFGCfCAhAQIxi0BBjwBRiABBiKBRgnwgINEC4YgAQYigUYQxixA8ICDRAuGBQYhwIYsQMYgATCAgUQLhiABMICChAAGIAEGIoFGEPCAggQLhixAxiABMICBRAAGIAEwgIcEC4YgAQYigUYQxixAxiXBRjcBBjeBBjgBNgBAZgDAIgGAZAGE7oGBggBEAEYCZIHBTIuMC4xoAecELIHAzItMbgHpALCBwUyLTEuMsgHHA&sclient=gws-wiz-serp");
    driver.findElement(By.id("_L4USabWdGYakqtsP1cPm0A8_102")).click();
    driver.get("https://es.wikipedia.org/wiki/Wolverine");
    assertEquals("Wolverine - Wikipedia, la enciclopedia libre", driver.getTitle());
  }

  @AfterEach
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

  private void pause(long mils){
    try {
      Thread.sleep(mils);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}
