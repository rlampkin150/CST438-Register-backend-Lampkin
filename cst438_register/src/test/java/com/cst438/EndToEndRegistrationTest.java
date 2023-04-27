package com.cst438;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

public class EndToEndRegistrationTest {
	public static final String FIREFOX_DRIVER_FILE_LOCATION = "C:\\Users\\leero\\Downloads\\geckodriver-v0.33.0-win-aarch64";

	public static final String URL = "http://localhost:3000"; 
	public static final String TEST_STUDENT_EMAIL = "rlampkin@csumb.edu";
	public static final String TEST_STUDENT_NAME = "test"; 
	public static final int SLEEP_DURATION = 500;

	@Autowired
	StudentRepository studentRepository;

	@Test
    public void addStudentTest() throws Exception {
        System.setProperty("webdriver.firefox.driver", FIREFOX_DRIVER_FILE_LOCATION);
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        try {
        	driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			driver.findElement(By.id("addStudent")).click();
			Thread.sleep(SLEEP_DURATION);
			WebElement studentName = driver.findElement(By.id("studentName"));
			studentName.sendKeys(TEST_STUDENT_NAME);
			WebElement studentEmail = driver.findElement(By.id("studentEmail"));
			studentEmail.sendKeys(TEST_STUDENT_EMAIL);
			driver.findElement(By.id("add")).click();
			Thread.sleep(SLEEP_DURATION);
			Student check = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			assertNotNull(check,"ERROR: Student Not Found in Database");
        } 
        catch (Exception ex) {
            throw ex;
        } 
        finally {
        	Student s = studentRepository.findByEmail(TEST_STUDENT_EMAIL);
			if (s != null)
				studentRepository.delete(s);

			driver.quit();
        }
	}
}
