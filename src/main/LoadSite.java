package main;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.SiteData;

public class LoadSite extends MainSuite {
	
//Load a site on a webdriver that is already opened, the boolean fullLoad defines if the wait should be longer in order to wait the site to load the 100%.

//This method only loads and url on a webdriver.	
public static void getUrl(String URL, WebDriver driver, boolean fullLoad) throws InterruptedException, AWTException {
		
	if (fullLoad) {
		
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		
	} else {
	
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	
	}
	
	boolean load = false;
	
	for (int i = 1; i<5; i++){
			
		if (!load) {
		
			try {
				
				System.out.println("Loading: "+URL);
				
				driver.get(URL);
				
				handleAuth(driver);
				
				if (fullLoad) {
					
					waitForLoad(driver);
					
				}
	
				} catch (org.openqa.selenium.TimeoutException e) {
					
				}
			
			if (fullLoad) {
				
				if (((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")) {
					
					load = true;
					
				}
				
			} else {
		
				if (((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")||((JavascriptExecutor)driver).executeScript("return document.readyState").equals("interactive")) {
					
					load = true;
					
				}
				
			}
			
		}
			
	}
		
}
	
//This method returns all the data of the site in a SiteData object.
public static SiteData getUrlData(String URL, WebDriver driver, boolean fullLoad) throws InterruptedException, AWTException {
	
	if (fullLoad) {
		
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		
	} else {
	
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
		
		driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
	
	}
	
	SiteData data = new SiteData();
	
	boolean load = false;
	
	for (int i = 1; i<5; i++){
		
		if (!load) {
		
			try {
				
				server.newHar(URL);
				
				System.out.println("Loading: "+URL);
				
				driver.get(URL);
				
				handleAuth(driver);
				
				if (fullLoad) {
					
					waitForLoad(driver);
					
				}
	
				} catch (org.openqa.selenium.TimeoutException e) {
					
				}
			
			if (fullLoad) {
				
				if (((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")) {
					
					load = true;
					
				}
				
			} else {
		
				if (((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete")||((JavascriptExecutor)driver).executeScript("return document.readyState").equals("interactive")) {
					
					load = true;
					
				}
				
			}
			
		}
		
	}
		
	data.url = URL;
	
	data.driver = driver;
	
	data.traffic = server.getHar();
	
	data.sourceCode = driver.getPageSource();
	
	data.elements = (ArrayList<WebElement>) driver.findElements(By.xpath("//*"));
	
	return data;
	
}
	
//Wait for the DOM complete state of the document.
public static void waitForLoad(WebDriver driver) {
    
	ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            
		public Boolean apply(WebDriver driver) {
                
			return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
            
		}
        
	};
    
	WebDriverWait wait = new WebDriverWait(driver, 30, 30000);
    
	wait.until(pageLoadCondition);

}

//This method is used to handle auth windows.
public static void handleAuth(WebDriver driver) throws InterruptedException, AWTException{
	
	boolean alert = true;
	
	while (alert){
		
		try { 
			
		driver.switchTo().alert().accept();
		
		Thread.sleep(1000);
		
		Robot robot = new Robot();
		
		robot.keyPress(KeyEvent.VK_ENTER);
		
		Thread.sleep(1000);
		
		} catch (org.openqa.selenium.NoAlertPresentException e){
			
			alert = false;
			
			}
			
	}

}

}
