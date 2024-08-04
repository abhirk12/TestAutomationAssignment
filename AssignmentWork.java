package AutomationAssignment;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AssignmentWork {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.setProperty("webdriver.chrome.driver","D:\\chromedriver-win32 (1)\\chromedriver-win32\\chromedriver.exe");
    	ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
	    WebDriver driver =new ChromeDriver(options); 
	    
	    try { 
	    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    
	 // Step 1: Navigate to the FitPeo Homepage
        driver.get("https://www.fitpeo.com/");
        driver.manage().window().maximize();
        
        // Step 2: Navigate to the Revenue Calculator Page
        WebElement revenueCalculatorLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Revenue Calculator")));
        revenueCalculatorLink.click();
        
        // Step 3: Scroll Down to the Slider section
        WebElement slider = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='range']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", slider);

        // Step 4: Adjust the Slider to set its value to 820
        setSliderValue(driver, slider, 820);

        // Step 5: Update the Text Field to 560
        WebElement textField = driver.findElement(By.xpath("//input[@type='number']"));
        textField.clear();
        textField.sendKeys("560");
        textField.sendKeys(Keys.RETURN);
        wait.until(ExpectedConditions.attributeToBe(slider, "value", "560"));
        
        String currentValue = slider.getAttribute("value");
        System.out.println("Current slider value: " + currentValue);
        

        // Step 6: Validate Slider Value
        if (!currentValue.equals("560")) {
            throw new AssertionError("Expected slider value to be 560 but got " + currentValue);
        }
        
        // Step 7: Select CPT Codes
        String[] cptCodes = {"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"};
        for (String code : cptCodes) {
            WebElement checkbox = driver.findElement(By.xpath("//label[contains(text(), '" + code + "')]/preceding-sibling::input"));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        // Step 8: Validate Total Recurring Reimbursement
        WebElement totalReimbursement = driver.findElement(By.xpath("//h2[contains(text(), 'Total Recurring Reimbursement')]"));
        if (!totalReimbursement.getText().contains("110700")) {
            throw new AssertionError("Expected Total Recurring Reimbursement to be $110700 but got " + totalReimbursement.getText());
        }

        System.out.println("All tasks completed successfully!");
	    

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }


	private static void setSliderValue(WebDriver driver, WebElement slider, int value) {
        Actions actions = new Actions(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Ensure slider is visible
        js.executeScript("arguments[0].scrollIntoView(true);", slider);
        // Calculate the move offset
        int sliderWidth = slider.getSize().width;
        int sliderMax = Integer.parseInt(slider.getAttribute("max"));
        int sliderMin = Integer.parseInt(slider.getAttribute("min"));
        int currentVal = Integer.parseInt(slider.getAttribute("value"));
        int offsetX = (int) (((value - sliderMin) * sliderWidth / (sliderMax - sliderMin)) - (currentVal * sliderWidth / (sliderMax - sliderMin)));

        System.out.println("Slider Width: " + sliderWidth);
        System.out.println("Current Value: " + currentVal);
        System.out.println("Target Value: " + value);
        System.out.println("Offset X: " + offsetX);

        // Move the slider
        actions.clickAndHold(slider)
                .moveByOffset(offsetX, 0)
                .release()
                .perform();

     // Wait until the slider's value attribute is updated
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Increased wait time
        wait.until(ExpectedConditions.attributeToBe(slider, "value", String.valueOf(value)));

        // Check final slider value
        String finalValue = slider.getAttribute("value");
        System.out.println("Final slider value: " + finalValue);

        if (!finalValue.equals(String.valueOf(value))) {
            throw new AssertionError("Expected slider value to be " + value + " but got " + finalValue);
        
    }

	}

}
