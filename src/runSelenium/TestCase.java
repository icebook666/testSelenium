package runSelenium;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.webdriven.commands.Click;

public class TestCase {
	
	static Properties properties = new Properties();

	public static void main(String[] args) throws Exception {

		String myExeIE_path = "";
		String myExeChrome_path = "";
		WebDriver driver = null;
		TestCase myCase = new TestCase();
		
		try {
			
			if (args.length > 0) {
				File parameterFile = new File(args[0]);
				//properties.load(new FileInputStream(parameterFile));
				properties.load(new InputStreamReader(new FileInputStream(parameterFile), "UTF-8"));
				
				myExeIE_path = properties.getProperty("driver_IE_path");
				myExeChrome_path = properties.getProperty("driver_Chrome_path");
				
				int num1 = 0;

			    while( true ){
			    	
			    	System.out.println("=================Using IEDriverServer=================");
			        System.out.println("(1)瀏覽google首頁");
			        System.out.println("(2)瀏覽ALPHA Portal擷取螢幕畫面");
			        System.out.println("==================Using Chromedriver==================");
			        System.out.println("(3)瀏覽google圖片搜尋關鍵字，自動下載2次下拉捲軸出現的小圖片");
			        System.out.println("(4)瀏覽google圖片搜尋關鍵字，自動下載大圖50張");
			        System.out.println("(9)結束程式");
			        System.out.println("======================================================");
			        
			        System.out.print("請輸入欲執行程式的數字：");
			        Scanner scanner = new Scanner(System.in);
			        num1 = scanner.nextInt();
			        
			        switch (num1) {
					case 1:
						//(1)瀏覽google首頁
						System.out.println("執行(1)瀏覽google首頁");
						myCase.navigateGoogle(myExeIE_path, driver);
						break;
					case 2:
						//(2)瀏覽ALPHA Portal擷取螢幕畫面
						System.out.println("執行(2)瀏覽ALPHA Portal擷取螢幕畫面");
						myCase.navigatePortal(myExeIE_path, driver);
						break;
					case 3:
						//(3)瀏覽google圖片搜尋關鍵字，自動下載2次下拉捲軸出現的小圖片
						System.out.println("執行(3)瀏覽google圖片搜尋關鍵字，自動下載2次下拉捲軸出現的小圖片");
						myCase.navigateGooglePic(myExeChrome_path, driver);
						break;
					case 4:
						//(4)瀏覽google圖片搜尋關鍵字，自動下載大圖50張
						System.out.println("執行(4)瀏覽google圖片搜尋關鍵字，自動下載大圖50張");
						myCase.navigateGoogleBigPic(myExeChrome_path, driver);
						break;
					case 9:
						System.out.println("執行(9)結束程式");
						System.exit(0);
						break;
					default:
						System.out.println("無效的數字，請重新選擇!");
						break;
					}
			    }
				
			}
			else
			{
				System.out.println("Please input config.properties parameter!");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 執行(1)瀏覽google首頁
	 * @param myExe_path 指定WebDriver執行檔位置
	 * @param driver
	 */
	public void navigateGoogle(String myExe_path, WebDriver driver) {
		try {
			System.setProperty("webdriver.ie.driver", myExe_path);

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);

			System.out.println("==========Start==========");

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// And now use this to visit Site
			System.out.println("go to google.com.tw");
			driver.navigate().to("www.google.com.tw");

			//等待直到Name為q的欄位被載入，10秒逾時
			(new WebDriverWait(driver, 10))
					.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							return d.findElement(By.name("q"));
						}
					});

		    driver.findElement(By.name("q")).clear();
		    driver.findElement(By.name("q")).sendKeys(properties.getProperty("search_text"));
		    driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
		    
		    Thread.sleep(3000);
		    
		    //印出前2頁的所有搜尋結果Title和Link url
		    for (int i = 0; i < 2; i++) {
			    //抓取DOM elements, (.r a) 為Google搜尋結果的link
			    List<WebElement> searchReultATagList = driver.findElements(By.cssSelector(".r a"));
			    for (WebElement searchReultATag : searchReultATagList) {
				    System.out.println(searchReultATag.getText() + " : ");
				    System.out.println(searchReultATag.getAttribute("href"));
				    System.out.println("=======================");
			    }
			    //抓取DOM element, #pnnext 為Google搜尋下一頁按鈕
			    WebElement nextPageBtn = driver.findElement(By.id("pnnext"));
			    nextPageBtn.click();
		    }

			System.out.println("==========Finish==========");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (driver != null)
				// Close the browser
				driver.quit();
		}
	}
	
	/**
	 * 執行(3)瀏覽google圖片搜尋關鍵字，自動下載2次下拉捲軸出現的小圖片
	 * @param myExe_path 指定WebDriver執行檔位置
	 * @param driver
	 */
	public void navigateGooglePic(String myExe_path, WebDriver driver) {
		try {
			//System.setProperty("webdriver.ie.driver", myExe_path);
			//DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			//ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			//driver = new InternetExplorerDriver(ieCapabilities);
			
			String downloadFilepath = properties.getProperty("save_path");
	        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	        //chromePrefs.put("profile.default_content_settings.popups", 0);
	        chromePrefs.put("download.default_directory", downloadFilepath);
	        ChromeOptions options = new ChromeOptions();
	        HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
	        options.setExperimentalOption("prefs",chromePrefs);
	        //options.addArguments("--test-type");
	        //設置為headless模式
	        //options.addArguments("--headless");
	        //設置瀏覽器視窗打開大小
	        //options.addArguments("--window-size=1920,1080");
			
			System.setProperty("webdriver.chrome.driver", myExe_path);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			
			//capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	        
			capabilities.setCapability("chrome.switches",Arrays.asList("--start-maximized"));
			driver = new ChromeDriver(capabilities);

			System.out.println("==========Start==========");

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// And now use this to visit Site
			System.out.println("go to google picture site");
			driver.navigate().to("https://www.google.com.tw/imghp?hl=zh-TW&tab=wi");
			
			//等待直到Name為q的欄位被載入，10秒逾時
			(new WebDriverWait(driver, 10))
					.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							return d.findElement(By.name("q"));
						}
					});

		    driver.findElement(By.name("q")).clear();
		    driver.findElement(By.name("q")).sendKeys(properties.getProperty("search_pictext"));
		    driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
		    Thread.sleep(2000);
		    
		    //記錄不重復src
			HashSet<String> srcSet = new HashSet<String>();
			int num_pic = 1;
			String parentHandle = driver.getWindowHandle();
			
			//捲動捲軸向下2次，保存出現的所有圖片
			for (int i = 0; i < 2; i++)
			{
				//List<WebElement> elements = driver.findElements(By.cssSelector("img[alt]"));
				List<WebElement> elements = driver.findElements(By.cssSelector(".rg_ic.rg_i"));
				
		        for(WebElement element : elements){
		            String src = element.getAttribute("src");
		            if (src != null)
		            {
		            	if (src.indexOf("data:image") > -1 || src.indexOf("http") > -1)
		            	{
		            		//不重覆篩選
		            		if (!srcSet.contains(src))
		            		{
		            			//System.out.println(src);
			            		System.out.print("保存第"+num_pic+"張圖片...");
			            		try {
			            			if (src.indexOf("data:image") > -1)
			            			{
			            				ImageBase64Utils.saveBase64ImageStringToImage(downloadFilepath, properties.getProperty("search_pictext")+"_"+String.valueOf(num_pic), src);
			            				System.out.println("OK!");
			            			}
			            			else
			            			{
			            				//ImageBase64Utils.saveURLImage(properties.getProperty("save_path"), properties.getProperty("search_pictext")+"_"+String.valueOf(num_pic), src);

			            				//透過JS直接下載圖片
			            	            //System.out.println("save picture now~");
			            	            JavascriptExecutor js1 = (JavascriptExecutor) driver;
			            		        js1.executeScript("window.open('"+src+"')");
			            		        Thread.sleep(100);
			            		        
			            		        for (String winHandle : driver.getWindowHandles()) {
			            			    	driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
			            			    }
			            			    //System.out.println("Newly open Window Title is: " + driver.getTitle());
			            		        
			            			    JavascriptExecutor js2 = (JavascriptExecutor) driver;
			            			    String exe_JS = "var link = document.createElement('a'); ";
			            			    exe_JS += "link.href = '" + src + "'; ";
			            			    exe_JS += "link.download='"+properties.getProperty("search_pictext")+"_"+String.valueOf(num_pic)+".jpg"+"'; ";
			            			    exe_JS += "link.click();";
			            			    js2.executeScript(exe_JS);
			            			    //等待圖片下載完成的時間(視情況加長)
			            	            Thread.sleep(100);
			            	            
			            	            String subHandle = driver.getWindowHandle();
			            	            
			            	            if (!subHandle.equals(parentHandle))
			            	            {
			            	            	driver.close(); // close newly opened window when done with it
			            	            	System.out.println("OK!");
			            	            }
			            	            else
			            	            {
			            	            	System.out.println("Error!");
			            	            }

			            			    driver.switchTo().window(parentHandle); // switch back to the original window
			            	            //System.out.println("save ok");
			            	            
			            			}	
			            			num_pic++;
			            			
								} catch (Exception e) {
									System.out.println(e.getMessage());
									//System.out.println("Error!");
								} finally {
									srcSet.add(src);
								}
		            		}
		            	}
		            }
		        }
		        
		        //執行JS向下捲動
		        JavascriptExecutor js = (JavascriptExecutor) driver;
				// This  will scroll down the page by  1000 pixel vertical
		        js.executeScript("window.scrollBy(0,1000)");
		        Thread.sleep(3000);
		        
		        //Find element by link text and store in variable "Element"        		
		        //WebElement Element = driver.findElement(By.linkText("Linux"));
		        //This will scroll the page till the element is found
		        //js.executeScript("arguments[0].scrollIntoView();", Element);
		        //This will scroll the web page till end.		
		        //js.executeScript("window.scrollTo(0, document.body.scrollHeight)");    
		        
			}	
			
			System.out.println("==========Finish==========");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (driver != null)
				// Close the browser
				driver.quit();
		}
	}
	
	/**
	 * 執行(4)瀏覽google圖片搜尋關鍵字，自動下載大圖50張
	 * @param myExe_path 指定WebDriver執行檔位置
	 * @param driver
	 */
	public void navigateGoogleBigPic(String myExe_path, WebDriver driver) {
		try {
			
			String downloadFilepath = properties.getProperty("save_path") + "/big_image";
	        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	        //chromePrefs.put("profile.default_content_settings.popups", 0);
	        chromePrefs.put("download.default_directory", downloadFilepath);
	        ChromeOptions options = new ChromeOptions();
	        HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
	        options.setExperimentalOption("prefs",chromePrefs);
			
			System.setProperty("webdriver.chrome.driver", myExe_path);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
	        
			capabilities.setCapability("chrome.switches",Arrays.asList("--start-maximized"));
			driver = new ChromeDriver(capabilities);

			System.out.println("==========Start==========");

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			// And now use this to visit Site
			System.out.println("go to google picture site");
			driver.navigate().to("https://www.google.com.tw/imghp?hl=zh-TW&tab=wi");
			
			//等待直到Name為q的欄位被載入，10秒逾時
			(new WebDriverWait(driver, 10))
					.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							return d.findElement(By.name("q"));
						}
					});

		    driver.findElement(By.name("q")).clear();
		    driver.findElement(By.name("q")).sendKeys(properties.getProperty("search_pictext"));
		    driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
		    Thread.sleep(2000);
		    
		    //記錄不重復src
			HashSet<String> srcSet = new HashSet<String>();
			int num_pic = 1;
			String parentHandle = driver.getWindowHandle();
			
			List<WebElement> elements = driver.findElements(By.cssSelector(".rg_ic.rg_i"));
			
			Actions ob = new Actions(driver);
			
			//保存前50張圖片
	        for(WebElement element : elements){
	        	
	        	if (num_pic > 50)
	        		break;
	        	
	        	String src1 = element.getAttribute("src");
	        	
	        	if (src1 != null)
		        	if (src1.indexOf("data:image") > -1 || src1.indexOf("http") > -1)
		        	{
		        		ob.click(element).build().perform();
			        	Thread.sleep(1000);
			        	//篩選有irc_t i30052 class的<div> > 有irc_mic class的<div> > a > 含有irc_m開頭class的<img>且存在alt與data-iml屬性
			        	List<WebElement> big_pictures = driver.findElements(By.cssSelector("div[class='irc_t i30052'] div[class='irc_mic'] div[class^='irc_m'] a img[class='irc_mi'][alt][data-iml]"));
			        	
			        	for (WebElement big_picture : big_pictures) {
			        		
			        		String src2 = big_picture.getAttribute("src");
			        		
			        		if (src2 != null)
				        		//不重覆篩選
			            		if (!srcSet.contains(src2))
			            		{
			            			//System.out.println(src2);
				            		System.out.print("保存第"+num_pic+"張圖片...");
				            		try {
				            			if (src2.indexOf("data:image") > -1)
				            			{
				            				ImageBase64Utils.saveBase64ImageStringToImage(downloadFilepath, properties.getProperty("search_pictext")+"_"+String.valueOf(num_pic), src2);
				            				System.out.println("OK!");
				            			}
				            			else if (src2.indexOf("http") > -1)
				            			{
				            				//透過JS直接下載圖片
				            	            //System.out.println("save picture now~");
				            	            JavascriptExecutor js1 = (JavascriptExecutor) driver;
				            		        js1.executeScript("window.open('"+src2+"')");
				            		        Thread.sleep(300);
				            		        
				            		        for (String winHandle : driver.getWindowHandles()) {
				            			    	driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
				            			    }
				            			    //System.out.println("Newly open Window Title is: " + driver.getTitle());
				            		        
				            			    JavascriptExecutor js2 = (JavascriptExecutor) driver;
				            			    String exe_JS = "var link = document.createElement('a'); ";
				            			    exe_JS += "link.href = '" + src2 + "'; ";
				            			    exe_JS += "link.download='"+properties.getProperty("search_pictext")+"_"+String.valueOf(num_pic)+".jpg"+"'; ";
				            			    exe_JS += "link.click();";
				            			    js2.executeScript(exe_JS);
				            			    //等待圖片下載完成的時間(視情況加長)
				            	            Thread.sleep(1200);
				            	            
				            	            String subHandle = driver.getWindowHandle();
				            	            
				            	            if (!subHandle.equals(parentHandle))
				            	            {
				            	            	driver.close(); // close newly opened window when done with it
				            	            	System.out.println("OK!");
				            	            }
				            	            else
				            	            {
				            	            	System.out.println("Error!");
				            	            }
				            			    driver.switchTo().window(parentHandle); // switch back to the original window
				            	            //System.out.println("save ok");
				            			}
				            			num_pic++;
				            			
				            		} catch (Exception e) {
										System.out.println(e.getMessage());
									} finally {
										srcSet.add(src2);
									}
				            		
			            		}
						}
			        	
		        	}
	        }
	        
			System.out.println("==========Finish==========");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (driver != null)
				// Close the browser
				driver.quit();
		}
	}
	
	/**
	 * 執行(2)瀏覽ALPHA Portal擷取螢幕畫面
	 * @param myExe_path 指定WebDriver執行檔位置
	 * @param driver
	 */
	public void navigatePortal(String myExe_path, WebDriver driver) {
		try {
			
			String screenshotFilepath = properties.getProperty("save_path");
			System.setProperty("webdriver.ie.driver", myExe_path);

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);
			System.out.println("==========Start==========");

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			// And now use this to visit Site
			System.out.println("go to ALPHA Portal");
			driver.navigate().to("https://portal.alphanetworks.com/");
			
			//等待直到submit的按鈕被載入，10秒逾時
			(new WebDriverWait(driver, 10))
					.until(new ExpectedCondition<WebElement>() {
						public WebElement apply(WebDriver d) {
							return d.findElement(By.cssSelector("input[type='submit'][value='Login']"));
						}
					});

		    driver.findElement(By.name("userId")).sendKeys("TestID");
		    driver.findElement(By.name("userPasswords")).sendKeys("TestPassword");
		    driver.findElement(By.name("checkcode")).sendKeys("1234");
		    
		    System.out.println("螢幕快照1...");
		    //螢幕快照
		    File scrFile1 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile1, new File(screenshotFilepath+"/screen_01.png"));
			
		    driver.findElement(By.cssSelector("input[type='submit'][value='Login']")).click();
		    Thread.sleep(2000);
		    
		    System.out.println("螢幕快照2...");
		    //螢幕快照
		    File scrFile2 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile2, new File(screenshotFilepath+"/screen_02.png"));
		    
			Thread.sleep(2000);
	        
			System.out.println("==========Finish==========");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (driver != null)
				// Close the browser
				driver.quit();
		}
	}
	
	public void Test1(String myExe_path, WebDriver driver) {
		try {
			System.setProperty("webdriver.ie.driver", myExe_path);

			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(ieCapabilities);

			System.out.println("==========Start==========");

			driver.manage().window().maximize();
			//driver.navigate().to("www.google.com.tw");
			
			//Select select = new Select(driver.findElement(By.id("username")));
			//WebElement element = driver.findElement(By.id("username"));
			
			//Thread.sleep(3000); //等待3秒
			//driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			
			//WebDriverWait wait = new WebDriverWait(driver, 60);//初始化等待60s
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("xx")));//等待xx元素出現
			
			//自定義等待事件，等待直到Name為q的欄位被載入，10秒逾時
			/*
			(new WebDriverWait(driver, 10))
			.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver d) {
					return d.findElement(By.name("q"));
				}
			});
			*/			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (driver != null)
				// Close the browser
				driver.quit();
		}
	}

}
