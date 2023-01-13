package listeners;

import Factory.PlaywrightFactory;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.*;
import org.testng.annotations.BeforeSuite;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;


public class AllureReportListener implements ITestListener {

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }


    public void saveScreen(Page page) {
        Allure.addAttachment("screenshot",
                new ByteArrayInputStream((page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("images/example.png"))))));
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test end time"+ new Date().toString()));
    }




    // Text attachments for Allure
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        return message;
    }

    // HTML attachments for Allure
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in onStart method " + iTestContext.getName());
        iTestContext.setAttribute("Page", PlaywrightFactory.getPage());
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test start time "+ new Date().toString()));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
        Allure.addAttachment("Information about Test Time:: ", String.valueOf("Test end time "+ new Date().toString()));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("I am in onTestStart method " + getTestMethodName(iTestResult) + " start");
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test start time "+ new Date().toString()));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in onTestSuccess method " + getTestMethodName(iTestResult) + " succeed");
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test end time "+ new Date().toString()));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in onTestFailure method " + getTestMethodName(iTestResult) + " failed");
        Object testClass = iTestResult.getInstance();
        Page page = PlaywrightFactory.getPage();
        // Allure ScreenShotRobot and SaveTestLog
        if (page instanceof Page) {
            System.out.println("Screenshot captured for test case:" + getTestMethodName(iTestResult));
            saveScreen(page);
        }
        // Save a log on allure.
        saveTextLog(getTestMethodName(iTestResult) + " failed and screenshot taken!");
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test end time "+ new Date().toString()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method " + getTestMethodName(iTestResult) + " skipped");
        Allure.addAttachment("Information about Test Time: ", String.valueOf("Test end time "+ new Date().toString()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
        Allure.addAttachment("Information about Test Time:: ", String.valueOf("Test end time "+ new Date().toString()));
    }

}
