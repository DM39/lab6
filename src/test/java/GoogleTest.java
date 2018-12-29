import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class GoogleTest
{
    private static ChromeDriverExtended driver;

    @BeforeClass
    public static void setup() throws Exception
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriverExtended();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void test1()throws Exception
    {
        String target = "Окна Zigmar";
        String search_text = "окна";

        searchGoogle(driver ,search_text);

        int page =0;
        boolean findTarget;

        do{
            findTarget =  findKey(driver , target);
            page++;
        }while( (!findTarget) & nextPage(driver));

        if (findTarget)
            screenshot(driver , "test1-page"+page);


        Assert.assertTrue(findTarget & page != 1 );
    }

    @Test
    public void test2()throws Exception
    {
        String target = "Уголь - ООО\"Киев Щебень\"";
        String search_text = "уголь";

        searchGoogle(driver ,search_text);

        boolean findTarget =  findKey(driver , target);
        if (findTarget)
            screenshot(driver , "test2");

        Assert.assertTrue(findTarget);
    }

    @Test
    public void test3()throws Exception
    {
        String target = "Kaspersky.ua | Kaspersky Antivirus 2017 | Официальный сайт\u200E";
        String search_text = "водопад";

        searchGoogle(driver ,search_text);

        int page =0;
        boolean findTarget;

        do {
            page++;
            findTarget =  findKey(driver , target);
            screenshot(driver , "test3-page"+page);
        }while( (!findTarget) & nextPage(driver));

        Assert.assertFalse(findTarget );
    }

    public static void searchGoogle(ChromeDriverExtended driver, String search_text)
    {
        driver.get("https://www.google.com/");
        WebElement searchField = driver.findElement(By.cssSelector("#tsf > div:nth-child(2) > div > div.RNNXgb > div > div.a4bIc > input"));
        searchField.sendKeys(search_text);
        WebElement searchButton = driver.findElement(By.cssSelector("#tsf > div:nth-child(2) > div > div.FPdoLc.VlcLAe > center > input[type=\"submit\"]:nth-child(1)"));
        searchButton.click();
    }

    public static boolean findKey(ChromeDriverExtended driver, String key)
    {
        List <WebElement> heads = driver.findElements(new By.ByClassName("rc"));
        for (WebElement head: heads)
            if (head.getText().contains(key))
                return true;
        return false;
    }

    public static boolean nextPage(ChromeDriverExtended driver )
    {
        try
        {
            WebElement next_button = driver.findElement(By.cssSelector("#pnnext"));
            next_button.click();
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }

    public static void screenshot(ChromeDriverExtended driver, String name) throws Exception
    {
        File screenshot = driver.getFullScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("Screenshots/"+name+".png"));
    }

    @AfterClass
    public static void close() {
        driver.quit();
    }
}
