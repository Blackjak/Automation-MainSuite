package main;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.browsermob.proxy.ProxyServer;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainSuite {
	
//Webdivers Browsers
	
	public static WebDriver Driver;
	
//SeleniumServer
	
	public static ProxyServer server;
	
//BrowserMob Proxy
	
	public static Proxy proxy;
	
//Browsers configurations
	
	public static DesiredCapabilities ffCap = DesiredCapabilities.firefox();
	
	public static DesiredCapabilities safariCap = DesiredCapabilities.safari();
	
	public static DesiredCapabilities chromeCap = DesiredCapabilities.chrome();
	
	public static DesiredCapabilities ieCap = DesiredCapabilities.internetExplorer();
	
//Directory of the xls where the sites are
	
	public String XLSDirectory = "";
	
	public static ArrayList<Sheet> sheets = new ArrayList<Sheet>();
	
//Method to set proxy
	
	public static void setProxy(String proxyDirecc) throws UnknownHostException{
		
		proxy.setHttpProxy(proxyDirecc);
	 	   
    	proxy.setFtpProxy(proxyDirecc);
    	
    	ffCap.setCapability(CapabilityType.PROXY, proxy);     
	    
        chromeCap.setCapability(CapabilityType.PROXY, proxy);
        
        ieCap.setCapability(CapabilityType.PROXY, proxy);
        
        safariCap.setCapability(CapabilityType.PROXY, proxy);
    	
    	System.out.println("Proxy changes to: "+proxyDirecc);
    	
	}
	
	public void startServer(String proxyDirecc) throws Exception{
		
		Map<String, String> options = new HashMap<String, String>(); 
        
		options.put("proxyType", "MANUAL");
		
		server = new ProxyServer(4444);
		
		server.start();
		
		server.setOptions(options); 
		
		server.setCaptureHeaders(true);
       
		server.setCaptureContent(true);   
		
		proxy = server.seleniumProxy();
			
		if (!proxyDirecc.equals("NOPROXY")){
			
			setProxy(proxyDirecc);
			
		}
		
		ffCap.setCapability(CapabilityType.PROXY, proxy);     
	    
        chromeCap.setCapability(CapabilityType.PROXY, proxy);
        
        ieCap.setCapability(CapabilityType.PROXY, proxy);
        
        safariCap.setCapability(CapabilityType.PROXY, proxy);
        
        ieCap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
       
		ieCap.setCapability("ie.ensureCleanSession", true);
	    
	    ieCap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
	    
	    ieCap.setCapability("ignoreProtectedModeSettings", true);
	    
	    System.out.println("*-*-*-  SERVER INICIATED  -*-*-*");
		
	}
	
// Methods to open browsers
	
	public static WebDriver openDriver(String webDriver){
		
		switch (webDriver){
		
			case "firefox":
				
				Driver = new FirefoxDriver(ffCap);
				
				Driver.manage().deleteAllCookies();
				
				System.out.println("*-*-*-  FOIREFOX DRIVER INICIATED  -*-*-*");
				
				break;
			
			case "explorer":
							
				ieCap.setCapability("ignoreProtectedModeSettings", true);

				Driver = new InternetExplorerDriver(ieCap);
				
				Driver.manage().deleteAllCookies();
				
				System.out.println("*-*-*-  IE DRIVER INICIATED  -*-*-*");			
							
				break;
			
			case "chrome":
				
				Driver = new ChromeDriver(chromeCap);
				
				Driver.manage().deleteAllCookies();
				
				System.out.println("*-*-*-  CHROME DRIVER INICIATED  -*-*-*");
				
				break;
			
			case "safari":
				
				Driver = new SafariDriver(safariCap);
				
				Driver.manage().deleteAllCookies();
				
				System.out.println("*-*-*-  SAFARI DRIVER INICIATED  -*-*-*");
				
				break;
		
		}
		
		return Driver;
		
	}

//Method to close browsers
	
public static void closeDriver(String webDriver){
		
		switch (webDriver){
		
			case "firefox":
				
				Driver.quit();
				
				System.out.println("*-*-*-  FF DRIVER CLOSED  -*-*-*");
				
				break;
			
			case "explorer":
							
				Driver.quit();
				
				System.out.println("*-*-*-  IE DRIVER CLOSED  -*-*-*");			
							
				break;
			
			case "chrome":
				
				Driver.quit();
				
				System.out.println("*-*-*-  CHROME DRIVER CLOSED  -*-*-*");
				
				break;
			
			case "safari":
				
				Driver.quit();
				
				System.out.println("*-*-*-  SAFARI DRIVER CLOSED  -*-*-*");
				
				break;
		
		}
		
	}
	
//Method to close the server connection
	
	public void closeServer() throws Exception{
        
		server.stop();
        
		System.out.println("*-*-*-  SERVER CLOSED  -*-*-*");
	
	}
	
//Methods to open and close documents
	
	public PrintWriter createDoc(String name) throws IOException{
		
		System.out.println("Creating document "+name);
		
		FileWriter logFile = new FileWriter("test-logs/"+name);

		PrintWriter writer = new PrintWriter(logFile);
		
		return writer;
		
	}
	
	public void closeDoc(PrintWriter writer) throws IOException{
		
		writer.close();
		
	}

//TestNG methods
	
	@BeforeSuite
	@Parameters({"proxyDirecc","sheetsToWork","projectDirectory","xlsDirectory"})
	public void beforeSuite(String proxyDirecc,String sheetsToWork,String projectDirectory,String xls) throws Exception{
		
		XLSDirectory = xls;
		
		System.setProperty("webdriver.chrome.driver",projectDirectory + "\\chromedriver.exe");
		
		System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, projectDirectory + "\\IEDriverServer.exe"); 
		
		startServer(proxyDirecc);
		
		Workbook workbook = WorkbookFactory.create(new FileInputStream(XLSDirectory));
		
		String[] sheetNames = sheetsToWork.split(";");
		
		System.out.println("Sheets to test:");
		
		for (String sheetname:sheetNames){
			
			sheets.add(workbook.getSheet(sheetname));
			
			System.out.println(sheetname);
		
		}
		
	}
	
	@AfterSuite
	public void afterSuite() throws Exception{
		
		closeServer();		
		
	}

}

