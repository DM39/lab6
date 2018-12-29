import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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

public class RozetkaTest
{
    private static ChromeDriverExtended driver;

    @BeforeClass
    public static void setup() throws Exception
    {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriverExtended();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    }

    @Test
    public void test()throws Exception
    {
        driver.get("https://rozetka.com.ua/bicycles/c83884/sort=expensive/");

        WebElement priceField = driver.findElement(By.xpath("//*[@id=\"price[max]\"]"));
        int maxPrice = getPrices(driver).get(0)+1;
        priceField.sendKeys(Integer.toString(maxPrice));

        WebElement okButton = driver.findElement(By.xpath("//*[@id=\"sort_price\"]/div[1]/span/span"));
        okButton.click();
        driver.get("https://rozetka.com.ua/bicycles/c83884/price=0-"+maxPrice+"/");

        List<Integer> prices = getPrices(driver);
        for(int price : getPrices(driver))
            Assert.assertTrue(prices.toString(), price <= maxPrice);
    }

    public static List<Integer> getPrices(ChromeDriverExtended driver)
    {
        List<Integer> prices = new ArrayList<Integer>();
        for (WebElement price: driver.findElements(new By.ByClassName("g-price-uah")))
            prices.add(Integer.parseInt(price.getText().replaceAll("[^0-9]*", "")));
        return prices;
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
