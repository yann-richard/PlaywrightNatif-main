package base;

import Factory.PlaywrightFactory;
import Pages.HomePage;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;



public class BaseTest {
    PlaywrightFactory pf;
    Page page;
    protected Properties prop;

    protected HomePage homePage;
    Playwright playwright;
    Browser browser;

    public String UserName;
    public String Password;


    @Parameters({ "browser","username","password" })
    @BeforeTest
    public void setup(String browserName,String username, String passWord) {
        pf = new PlaywrightFactory();
        UserName = username;
        Password = passWord;
        prop = pf.init_prop();

        if (browserName != null) {
            prop.setProperty("browser", browserName);

        }

        page = pf.initBrowser(prop);
        homePage = new HomePage(page);


        File index = new File("C:/Users/Pawk71/Desktop/projetPlaywright/PlaywrightNatif/allure-results");
        if (index.exists()) {
            String[] entries = index.list();
            for (String s : entries) {
                if (!s.equals("environment.xml")){
                    File currentFile = new File(index.getPath(),s);
                    currentFile.delete();}
            }
//            index.delete();
        }

        File index2 = new File("C:/Users/Pawk71/Desktop/projetPlaywright/PlaywrightNatif/screenshot");
        if (index2.exists()) {
            String[]entries = index2.list();
            for(String s: entries){
                File currentFile = new File(index2.getPath(),s);
                currentFile.delete();
            }
            index2.delete();
        }

        File index3 = new File("C:/Users/Pawk71/Desktop/projetPlaywright/PlaywrightNatif/images");
        if (index3.exists()) {
            String[]entries = index3.list();
            for(String s: entries){
                File currentFile = new File(index3.getPath(),s);
                currentFile.delete();
            }
            index3.delete();
        }
        File index4 = new File("C:/Users/Pawk71/Desktop/projetPlaywright/PlaywrightNatif/Traces");
        if (index4.exists()) {
            String[]entries = index4.list();
            for(String s: entries){
                File currentFile = new File(index4.getPath(),s);
                currentFile.delete();
            }
            index4.delete();
        }

         File index5 = new File("C:/Users/Pawk71/.jenkins/workspace/jenkins_playwright/allure-results");
        if (index5.exists()) {
            String[] entries = index5.list();
            for (String s : entries) {
                if (!s.equals("environment.xml")){
                    File currentFile = new File(index5.getPath(),s);
                    currentFile.delete();}
            }
//            index5.delete();
        }
        File index7 = new File("C:/Users/Pawk71/.jenkins/workspace/jenkins_playwright/target/videos");
        if (index7.exists()) {
            String[]entries = index7.list();
            for(String s: entries){
                File currentFile = new File(index7.getPath(),s);
                currentFile.delete();
            }
            index7.delete();
        }
        File index8 = new File("C:/Users/Pawk71/Desktop/projetPlaywright/PlaywrightNatif/target/videos");
        if (index8.exists()) {
            String[] entries = index8.list();
            for (String s : entries) {
                File currentFile = new File(index8.getPath(), s);
                currentFile.delete();
            }
            index8.delete();
        }




    }

    @AfterTest
    public void tearDown() {

//            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("FailedTest.png")).setFullPage(true));

//            page.context().browser().close();

        pf.getBrowserContext().tracing().stop(new Tracing.StopOptions().setPath(Paths.get("Traces/trace.zip")));
        byte[] byteArr = new byte[0];
        try {
            Path content = Paths.get("Traces/trace.zip");
//            i++;
            Allure.addAttachment("trace: ", Files.newInputStream(content) );
            // file to byte[], Path
//            byteArr = Files.readAllBytes(content);
//            Allure.addAttachment("Trace", "archive/zip", new ByteArrayInputStream(byteArr), "zip");
        } catch (IOException e) {
            e.printStackTrace();
        }

        page.context().browser().close();


        byte[] byteArr2 = new byte[0];
        try {
            Path path = page.video().path();
            // file to byte[], Path
            byteArr2 = Files.readAllBytes(path);
            Allure.addAttachment("Video", "video/mp4", new ByteArrayInputStream(byteArr2), "mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}
