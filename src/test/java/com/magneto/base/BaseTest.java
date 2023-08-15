package com.magneto.base;

import com.magneto.DriverFactory.initDriver;
import com.magneto.pages.HomePage;
import com.magneto.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.util.Properties;

public class BaseTest {


    WebDriver driver;
    initDriver initDriver;
    protected Properties prop;
    protected LoginPage loginPage;
    protected HomePage homePage;

    @BeforeTest
    public void setUp(){
        initDriver = new initDriver();
        prop =initDriver.initializeProperties();
        driver = initDriver.initializeDriver(prop);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }
}
