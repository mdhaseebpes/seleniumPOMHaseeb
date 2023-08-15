package com.magneto.DriverFactory;

import com.magneto.constants.FileLocations;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class initDriver {

    private static Logger logger = LogManager.getLogger(initDriver.class);
    public WebDriver driver;
    public Properties prop;

    OptionsManager optionsManager;
    public static ThreadLocal<WebDriver> tlDriver =  new ThreadLocal<>();

    /**
     *
     * @param prop
     * @return
     */
    public WebDriver initializeDriver(Properties prop){
        optionsManager = new OptionsManager(prop);
        String browserName = prop.getProperty("browser").toLowerCase().trim();

        if(browserName.equalsIgnoreCase("chrome")){
            //driver = new ChromeDriver(optionsManager.ChromeOptions());
            tlDriver.set(new ChromeDriver(optionsManager.ChromeOptions()));
        } else if (browserName.trim().equalsIgnoreCase("firefox")) {
            //driver = new FirefoxDriver(optionsManager.FirefoxOptions());
            tlDriver.set(new FirefoxDriver(optionsManager.FirefoxOptions()));
        }else if (browserName.equalsIgnoreCase("safari")) {
            //  driver = new SafariDriver();
            tlDriver.set(new SafariDriver());
        }else {
            System.out.println("please pass the right browser ..." + browserName);
        }

        getDriver().manage().deleteAllCookies();
        getDriver().manage().window().fullscreen();
        getDriver().get(prop.getProperty("url"));
        return getDriver();
    }


    /**
     * @return
     */
    public Properties initializeProperties() {
        prop = new Properties();
        FileInputStream fileInputStream = null;
        String envName = System.getProperty("env");

        try {
            if (envName == null) {
                logger.info("Running in qa environment as no environment is passed...... ");
                fileInputStream = new FileInputStream(FileLocations.QA_CONFIG_PATH);
            }
            switch (Objects.requireNonNull(envName).toLowerCase().trim()) {
                case "qa":
                    logger.info("Running in "+envName+" environment...... ");
                    fileInputStream = new FileInputStream(FileLocations.QA_CONFIG_PATH);
                    break;
                case "dev":
                    logger.info("Running in "+envName+" environment...... ");
                    fileInputStream = new FileInputStream(FileLocations.DEV_CONFIG_PATH);
                    break;
                case "stage":
                    logger.info("Running in "+envName+" environment...... ");
                    fileInputStream = new FileInputStream(FileLocations.STAGE_CONFIG_PATH);
                    break;
                case "prod":
                    logger.info("Running in "+envName+" environment...... ");
                    fileInputStream = new FileInputStream(FileLocations.CONFIG_PATH);
                    break;
                default:
                    logger.info("Pass correct environment ");

            }
            prop.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    /**
     *
     * @return
     */
    public static WebDriver getDriver() {

        return tlDriver.get();
    }



    /**
     *
     */
    public static String getScreenShot(){
        File fileSrc = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + File.separator + "build" + File.separator + "screenshot"+ File.separator + System.currentTimeMillis() + ".png";
        File destination = new File(path);
        try {
            FileUtils.copyFile(fileSrc,destination);
        } catch (IOException e) {
          e.printStackTrace();
        }
        return path;
    }
}
