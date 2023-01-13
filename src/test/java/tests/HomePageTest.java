package tests;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import constants.AppConstants;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.microsoft.playwright.TimeoutError;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import testUtil.TestUtil;
/*import listeners.Retry;*/
import org.testng.annotations.Ignore;

public class HomePageTest extends BaseTest {
    private Page page;
    @Ignore
    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    public void loginPageNavigationTest() {
        String actLoginPageTitle = homePage.getLoginPageTitle();
        System.out.println("page act title: " + actLoginPageTitle);
        Assert.assertEquals(actLoginPageTitle, AppConstants.LOGIN_PAGE_TITLE);
    }

//login--------------------------------------------------------------------------------------
   /* @Test(priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    public void appLoginTest() {
        Assert.assertTrue(homePage.doLogin(prop.getProperty("username").trim(), prop.getProperty("password").trim()),"Failed to login because 'E-mail or password incorrect'");
    }*/

   @Ignore
    @Test(priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    public void appLoginTest() {
        Assert.assertTrue(homePage.doLogin(prop.getProperty("username").trim(),
                prop.getProperty("password").trim()));
        String s = homePage.getSiteLogoVision();
        homePage.emptyTheCart();
        if (s.equals("ok"))
            System.out.println("ok");
        else if (s.equals("wrong_IDs")) {
            Assert.fail("Informations de connexion Incorrects");
        } else if (s.equals("no_logo_seen")) {
            Assert.fail("Impossible d'acceder à la page Home");
        }
    }


    //searchProduct--------------------------------------------------------------------------------
             //provideLocalStrucutre data
    @DataProvider
    public Object[][] getProductData() {
        return new Object[][]{
                {"T-shirt"},
                {"zenity"},
                {"balenciaga"},
                {"I-phone"}
        };
    }
            //provideExterbal Excel data

    @DataProvider(name = "getProductDataExcel")
    public Object[][] getProductDataExcel() {
        Object productData[][] = TestUtil.getTestData(AppConstants.ARTICLES_SHEET_NAME);
        return productData; }

    /*@Ignore*/
    @Test(dataProvider = "getProductData", priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    public void searchArticleTest(String productName) {
        homePage.doSearch(productName);
        Locator p = homePage.page.locator(homePage.searchResult)
                .filter(new Locator.FilterOptions().setHasText(productName));

        int count = p.count();
        if (count == 0)
            count++;
        for (int i = 0; i < count; ++i) {
            String s = homePage.getResultSearch(i, productName);

            if (s.equals("ok"))
                System.out.println("The product is present");
            else if (s.equals("No_product_found")) {
                Assert.fail("Product does not exist in the database");
            } else if (s.equals("not_ok")) {
                Assert.fail("No match between the result and the element sought");
            }

        }
    }


//-----------------------------------------------------------------------------------------------------------
//addProduct--------------------------------------------------------------------------------
//provideLocalStrucutre data
    @DataProvider
    public Object[][] getProductDataForAdd() {
        return new Object[][]{
                {"I-phone PRO 256 NOIR", 4},
                {"T-shirt en coton biologique", 2},
                {"Yucca Elephantipes", 8}
        };
    }
    //provideExterbal Excel data
    @DataProvider(name = "getProductDataForAddExcel")
    public Object[][] getProductDataForAddExcel() {
        Object addproductData[][] = TestUtil.getTestData(AppConstants.ADDARTICLES_SHEET_NAME);
        return addproductData;
    }



//        @Ignore
        @Test(priority = 5, dataProvider = "getProductDataForAdd")
        public void addArticleToCartTest (String productName,int X){
            homePage.page.fill("id=style_input_navbar_search__Scaxy", "");
//        homePage.emptyTheCart();
            Boolean b = homePage.ClickOnAnArticle(productName);
            Assert.assertTrue(b, "Article inexistant");
            homePage.ClickOnAddToCart(X);
            Assert.assertTrue(homePage.VerifyArticleInCart(productName), "Item missing from cart");
            homePage.page.click("text=LES PRODUITS");
//
        }



        /*delete--------------------------------------------------------------------------------------------*/


    /*@Ignore*/
    @Test(priority = 6,dataProvider = "getProductDataForAdd")
    public void removeFromCartTest(String productName, int X) throws InterruptedException {
//
        homePage.ClickOnCartIcon();
        for (int i=0;i<X;i++)
        {homePage.DeleteFromCart(productName);
        }
        Assert.assertFalse(homePage.VerifyArticleDeletion(productName),"Item still in cart");
//
    }

        //Registration-----------------------------------------------------------------------------
        @DataProvider(name = "getRegistrationTestData")
        public Object[][] getRegistrationTestData() {
            Object usersData[][] = TestUtil.getTestData(AppConstants.CONTACTS_SHEET_NAME);
            return usersData;
        }

       /* @Ignore*/
//    @Test(dataProvider = "getProductDataForre", priority = 1)
    @Test(dataProvider = "getRegistrationTestData", priority = 1)
    public void RegistrateNewUserTest(String email, String password, String passwordconf) {
        homePage.page.navigate("https://ztrain-web.vercel.app/auth/login");
        try{
            homePage.page.waitForURL("https://ztrain-web.vercel.app/auth/login",
                    new Page.WaitForURLOptions().setTimeout(10000));}
        catch (TimeoutError ignored){}
        homePage.clickRegisterButton(email, password, passwordconf);
        String i = homePage.getSiteLogoVision();
        switch (i) {
            case "ok":
                System.out.println("ok");
                break;
            case "no_logo_seen":
                Assert.fail("Unable to access home page");
                break;
            case "short_Pswd":
                Assert.fail("Password too short");
            case "same_Pswds":
                Assert.fail("Passwords do not match");
            case "used_IDs":
                Assert.fail("User already exists");
            case "invalidIDs":
                Assert.fail("The email address does not have a valid format");
                break;
        }
    }


        @DataProvider
    public Object[][] getProductDataForre() {
        return new Object[][]{
                {"dhhj@gmail.com", "1226678888","1226678888"},
        };
    }



//logout --------------------------------------------------------------------------------------------

        @Test(priority = 7)
        public void appLogoutTest () {
            homePage = homePage.navigateToLoginPage();
        }


//--------------------------------------------------------------------------------------------
//
//    @Test(priority = 4)
//    public void homePageTitleTest() {
//        String actualTitle = loginPage.getHomePageTitle();
//        Assert.assertEquals(actualTitle, AppConstants.HOME_PAGE_TITLE);
//    }
//
//    @Test(priority = 5)
//    public void homePageURLTest() {
//        String actualURL = loginPage.getHomePageURL();
//        Assert.assertEquals(actualURL, prop.getProperty("url"));
//    }

//--------------------------------------------------------------------------------------------

        //   @Ignore
//   @Test(priority = 2)
//    public void forgotPwdLinkExistTest() {
//        Assert.assertTrue(loginPage.isForgotPwdLinkExist());
//    }
//

//--------------------------------------------------------------------------------------------


        //HomePage----------------------------------------------------------------------------------------------------
//    @Test(priority = 4)
//    public void homePageTitleTest() {
//        String actualTitle = homePage.getHomePageTitle();
//        Assert.assertEquals(actualTitle, AppConstants.HOME_PAGE_TITLE);
//    }
////
//    @Test(priority = 5)
//    public void homePageURLTest() {
//        String actualURL = homePage.getHomePageURL();
//        Assert.assertEquals(actualURL, prop.getProperty("url"));
//    }
//-------------------------------------------------------------------------------------------------------------

    }
