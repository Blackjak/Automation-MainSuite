package tests;

import java.awt.AWTException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.browsermob.core.har.HarEntry;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import main.LoadSite;
import main.SiteData;

public class Example extends main.MainSuite{
	
///////////////////////////////////////////// TESTNG Methods /////////////////////////////////////////////
	
	@BeforeClass
	@Parameters("webDriver")
	public void before(String webDriver) throws UnknownHostException{
		
		openDriver(webDriver);
		
	}
	
	@Test
	@Parameters("webDriver")
	public void test(String webDriver) throws InterruptedException, IOException, AWTException{
		
		for (Sheet sheet:sheets){
			
			System.out.println("ExampleLog-"+sheet.getSheetName()+"-"+webDriver+"-Log.html");
			
			PrintWriter writer = createDoc("ExampleLog-"+sheet.getSheetName()+"-"+webDriver+"-Log.html");
			
			writer.println("<html>");	
			
			writer.println("<body>");
			
			writer.println("<h1>"+ sheet.getSheetName()+" on "+webDriver+"</h1>");
			
			writer.println("<div class=\"centerTable\">");
			
			writer.println("<table style=\"border-collapse: collapse;\">");
		
			int i = 0;
			
			while (!sheet.getRow(i).getCell(0).getStringCellValue().equals("END")) {
				
				System.out.println("Row: "+i );
				
				SiteData data = LoadSite.getUrlData(sheet.getRow(i).getCell(0).getStringCellValue(), Driver, true);
				
				//if the title of the site is youtube it logs a green row on the html log, if not the row is red.
				
				if (data.sourceCode.indexOf("<title>Google</title>") == -1) {
					
					System.out.println("The title of "+sheet.getRow(i).getCell(0).getStringCellValue()+" is not Google");
					
					writer.println("<tr style=\"border:2px solid black;\"><th style=\"border:2px solid black;background-color: red \">"+sheet.getRow(i).getCell(0).getStringCellValue()+" Google is not the title</th></tr> ");
					
				} else {
					
					System.out.println("The title of "+sheet.getRow(i).getCell(0).getStringCellValue()+" is Google");
					
					writer.println("<tr style=\"border:2px solid black;\"><th style=\"border:2px solid black;background-color: green \">"+sheet.getRow(i).getCell(0).getStringCellValue()+" Google is the title</th></tr> ");
					
				}
				
				//Print the traffic of the site
				
				List<HarEntry> entryList = data.traffic.getLog().getEntries();
				
				for (HarEntry harEntry : entryList) {
					
					System.out.println("Trafic:");
					
					System.out.println(harEntry.getRequest().getUrl());
					System.out.println("------------------------------------------------------------------------------------------------");
					
				}
				
				i ++;
			
			}
			
			writer.println("</table>");
			
			writer.println("</div>");
			
			writer.println("</body>");
			
			writer.println("</html>");
			
			closeDoc(writer);

		}
		
	}
	
	@AfterClass
	@Parameters("webDriver")
	public void after(String webDriver){
		
		closeDriver(webDriver);
		
	}

}
