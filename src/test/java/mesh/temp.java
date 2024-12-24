package mesh;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.profesorfalken.jpowershell.PowerShell;

class temp {
	private WebDriver driver;
	private Map<String, Object> vars;
	JavascriptExecutor js;

	@Before
	public void setUp() {
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
	}

	@After
	public void tearDown() {
		driver.quit();
	}
	
	@Test
	public void powerShell() {
	    //PowerShell.openSession()
       // .executeCommandAndChain("Get-Process", (res -> System.out.println("List Processes:" + res.getCommandOutput())))
       // .executeCommandAndChain("Get-WmiObject Win32_BIOS", (res -> System.out.println("BIOS information:" + res.getCommandOutput())))
       // .close()

		
		String cmd = "selenium-side-runner -c \"browserName=chrome\"  --output-directory=results  C:\\Users\\patri\\git\\budget\\asb.side\r\n";
		
		PowerShell.openSession().executeCommand(cmd);
		
	}

	@Test
	public void export() {
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
		System.out.println("temp1...");
		vars.put("VarPwd", "gath3r3r");
		vars.put("VarFromDay", "10");
		vars.put("VarFromYear", "2024");
		vars.put("VarFromMonth", "6");
		System.out.println("temp2...");
		System.out.println("2...");
		driver.get("https://online.asb.co.nz/auth/authn?goto=https://online.asb.co.nz/fnc");
		driver.manage().window().setSize(new Dimension(1032, 850));
		driver.findElement(By.id("dUsername")).sendKeys("patfada");
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).sendKeys(vars.get("VarPwd").toString());
		driver.findElement(By.cssSelector(".logon-button__label")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#account-123140013110200 .icon-details-card-layout_details__1mgdG")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.cssSelector("td:nth-child(4) .scw_table td:nth-child(2)")).click();
		driver.findElement(By.id("Request_FromDate_inputDate")).click();
		driver.findElement(By.cssSelector(".StatementSelectorControlsWrap")).click();
		driver.findElement(By.id("Request_FromDate_inputDate")).sendKeys(vars.get("VarFromDay").toString());
		driver.findElement(By.id("Request_FromDate_inputMonth")).sendKeys(vars.get("VarFromMonth").toString());
		driver.findElement(By.id("Request_FromDate_inputYear")).sendKeys(vars.get("VarFromYear").toString());
		driver.findElement(By.id("ExportFormatDropdown_input")).click();
		driver.findElement(By.cssSelector("#ExportFormatDropdown_input_4 > .leftDisplay")).click();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector("#main-menu-button > .label")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(2);
		{
			WebElement element = driver.findElement(By.cssSelector(".label:nth-child(2)"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element).perform();
		}
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		driver.findElement(By.linkText("View accounts")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		driver.findElement(By.cssSelector(".style_smalloutlined__35ZOQ")).click();
		driver.findElement(By.cssSelector("#account-XXXXXXXXXXXX8128 .overline")).click();
		driver.switchTo().defaultContent();
		driver.findElement(By.linkText("Search & Export")).click();
		driver.findElement(By.id("Request_FromDate_inputDate")).click();
		driver.findElement(By.id("Request_FromDate_inputDate")).click();
		{
			WebElement element = driver.findElement(By.id("Request_FromDate_inputDate"));
			Actions builder = new Actions(driver);
			builder.doubleClick(element).perform();
		}
		driver.findElement(By.id("Request_FromDate_inputDate")).sendKeys(vars.get("VarFromDay").toString());
		driver.findElement(By.id("Request_FromDate_inputMonth")).sendKeys(vars.get("VarFromMonth").toString());
		driver.findElement(By.id("Request_FromDate_inputYear")).sendKeys(vars.get("VarFromYear").toString());
		driver.findElement(By.cssSelector(".statementSelectorWrap")).click();
		driver.findElement(By.id("ExportFormatDropdown_input")).click();
		driver.findElement(By.cssSelector("#ExportFormatDropdown_input_4 > .leftDisplay")).click();

	}
	

}
