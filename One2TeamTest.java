import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class One2TeamTest {
	WebDriver driver;
	
	@Test
	public void testTest() throws InterruptedException {
		String serverPath = "c:/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", serverPath);

		driver = new ChromeDriver();


		String startUrl = "http://chewie.one2team.com";
		String p1_Login="candidat";
		String p1_Password="Candidat1*";
		String p1_Domain="telco";
		String p3_cardName = "MyNameIs_" + (LocalDateTime.now()).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"));
		String p4_uploadPath = "c:/aaa.txt";


	  
	  
	  //1- open url
	  driver.get(startUrl);
	  //TEST
	  assertEquals("The page'"+startUrl+"' didn't load", "One2team - Connexion",  driver.getTitle());
	  
	  
	  //2- connection
	  	//2.1 set ups
		//2.2 fill
	  input("//*[@id='userName']", p1_Login);
	  input("//*[@id='passWord']", p1_Password);
	  input("//*[@id='domainName']", p1_Domain);  
	
	  	//2.3 submit
	  click("//*[@id='flogin']/input");
	  waitComplete();
	  
	  //TEST
	  assertEquals("The title of the page is '"+driver.getTitle()+"' expected 'chewie.one2team.com/telco' didn't load, maybe connexion problem, please verfy the crediantials in the test code.", "chewie.one2team.com/telco",  driver.getTitle());
	  assertTrue("Current page is not expected", exists("//*[@id='ogp' and @class='tab-border-over']"));
	  assertFalse("Current page is not expected", exists("//*[@id='slideboard' and @class='tab-border-over']"));
	  
	  //3-1 navigate
	  click("//*[@id='img_slideboard']");
	  waitComplete();
	  
	  //TEST
	  assertEquals("Expected page did not load, maybe connexion problem, please verfy the crediantials in the test code.", "chewie.one2team.com/telco",  driver.getTitle());
	  assertTrue("Current page is not expected", exists("//*[@id='slideboard' and @class='tab-border-over']"));
	  assertFalse("Current page is not expected", exists("//*[@id='ogp' and @class='tab-border-over']"));
	  
	
      //3-2 select	  
	  driver.switchTo().frame("slideboard");
      click("//*[@id='s2id_autogen1']/a");
	  click("//*[@id='select2-results-2']/li");
	  waitComplete();
	
	  //3-3 create card
	  hoverClick("//*[@id='columns-container']/div/div[1]/div[1]/div[2]/div[1]/span/span");	
	  input("//*[@id='cardName']", p3_cardName);
	  click("/html/body/div[1]/div/div/card-creation-popin-content-dumb/div[2]/div/button-with-status/div/div");
	  waitComplete();
	
	  //TEST
	 
	  String col1card1Xpath = "(//*[@class='containCol__col__card'])[1]/div[1]/div[1]/div[1]/div/div[1]";
	  String col2card1Xpath = "(//*[@class='containCol__col__card'])[2]/div[1]/div[1]/div[1]/div/div[1]";
	  String col2Xpath = "//*[@id='columns-container']/div/div[2]/div[2]";
	  
	  String col1card1text = driver.findElement(By.xpath(col1card1Xpath)).getText();
	  String col2card1text = driver.findElement(By.xpath(col2card1Xpath)).getText();
	  assertEquals("The first card of first colonne is titled"+col1card1text, 
			  p3_cardName, 
			  col1card1text
			  );
	  assertNotEquals("The first card of second colonne is titled"+col2card1text,  
			  p3_cardName, 
			  col2card1text
			  );
	  
	  
	  //4- drag & drop
	  dragAndDrop("(//*[@class='containCol__col__card'])[1]/div/div",col2Xpath);
	  waitComplete();
	  
	  //TEST
	  col1card1text = driver.findElement(By.xpath(col1card1Xpath)).getText();
	  col2card1text = driver.findElement(By.xpath(col2card1Xpath)).getText();
	  assertNotEquals("After Drop: The first card of first colonne is titled"+col1card1text, 
			  p3_cardName, 
			  col1card1text
			  );
	  assertEquals("After Drop: The first card of second colonne is titled"+col2card1text,  
			  p3_cardName, 
			  col2card1text
			  );

	  //5- upload
	  //5-1navigate to
	  click("(//*[@class='containCol__col__card'])[2]/div[1]/div[1]");
	  waitComplete();
	  click("//*[@id='card-popin-back-content-current']/div[1]/div[2]/card-modal-collapse-column-dumb/div/div/card-modal-block-dumb[2]/div/div[1]/h4");
	
	  upload("//*[@id='add-file-document_18']", p4_uploadPath);

	  driver.close();
	}
	

	private void upload(String xpath, String uploadPath) {
		WebElement element = driver.findElement(By.xpath(xpath));		
		element.sendKeys(uploadPath);		
		driver.findElement(By.name("send")).click();
	}


	private void input(String xpath, String inputText){
		WebElement element = driver.findElement(By.xpath(xpath));
		element.clear();
		element.sendKeys(inputText);
		//.setAttribute("value", inputText);
	}

	private void dragAndDrop(String xpathElement, String xpathDest){
		WebElement element = driver.findElement(By.xpath(xpathElement));
		WebElement elementDest = driver.findElement(By.xpath(xpathDest));
		Actions builder = new Actions(driver);
		Action dragAndDrop = builder
				.clickAndHold(element)
				.moveToElement(elementDest)
				.release(elementDest)
				.build();
		dragAndDrop.perform();
	}

	private void click(String xpath){
		WebElement element = driver.findElement(By.xpath(xpath));
		element.click();
	}
	
	private void hoverClick(String xpath) throws InterruptedException{
		WebElement element = driver.findElement(By.xpath(xpath));
		Actions action = new Actions(driver);	
		action.moveToElement(element).build().perform();
        TimeUnit.MILLISECONDS.sleep(500);
        element.click();
	}
	
	private Boolean exists(String xpath){
		Boolean res = driver.findElements(By.xpath(xpath)).size() > 0;
		return res;
	}
	
	private void waitComplete() throws InterruptedException{
		WebDriverWait wait = new WebDriverWait(driver,60);
         wait.until((ExpectedCondition<Boolean>) wd ->
		 			((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));    
         TimeUnit.SECONDS.sleep(5);
	}
}
