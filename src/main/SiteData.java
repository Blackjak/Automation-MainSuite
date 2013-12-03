package main;

import java.util.ArrayList;

import org.browsermob.core.har.Har;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SiteData {
	
	//The url that was loaded.
	public String url;
	
	//The driver that was used.
	public WebDriver driver;
	
	//The source code of teh site loaded.
	public String sourceCode;
	
	//All the elements of the source code.
	public ArrayList<WebElement> elements;
	
	//The traffic of the site while it was loading.
	public Har traffic;

}
