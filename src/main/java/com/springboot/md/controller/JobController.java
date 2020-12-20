package com.springboot.md.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.springboot.md.config.AbstracController;
import com.springboot.md.dao.JavaJob51Mapper;
import com.springboot.md.pojo.JavaJob51;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 秒度111
 * @Date 2019/7/19 0019 22:09
 */
@RestController
@Slf4j
@RequestMapping("/JobController")
public class JobController extends AbstracController {

    @Resource
    private JavaJob51Mapper mapper;

    private static String url1 = "https://search.51job.com/list/000000,000000,0000,00,9,99,java,2,";
    private static String url2 = ".html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";

    private static Integer i = 492;

    @RequestMapping(value = "initJob")
    public void init() throws InterruptedException {
        //设置webdriver路径
        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(JobController.class.getClassLoader().getResource("chromedriver.exe")).getPath());
        //指定要爬取的地址
        String httpUrl = "127.0.0.1:11000";  // 代理IP示例
        // 设置代理IP
        ChromeOptions options = new ChromeOptions();


        options.addArguments("--headless", "--disable-gpu");

        //  options.addArguments("--proxy-server=http://" + httpUrl);
        // 开启开发者模式
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver webDriver = new ChromeDriver(options);
        // 窗口最大化
        //    webDriver.manage().window().maximize();
        // webDriver.get("https://search.51job.com/list/000000,000000,0000,00,9,08%252c09,java,2,1.html?lang=c&postchannel=0000&workyear=02%252c03&cotype=99&degreefrom=03%252c04%252c07&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=");
        webDriver.get(url1 + i++ + url2);
        System.out.println("第" + i + "页");
        //   login(webDriver);
        //   Thread.sleep(5000L);
        extractJob(webDriver);
    }

    public void login(WebDriver webDriver) {
        webDriver.findElement(By.className("uer")).findElement(By.tagName("a")).click();
        List<WebElement> txt = webDriver.findElements(By.className("txt"));
        txt.get(0).findElement(By.className("ef")).sendKeys("17621471014");
        txt.get(1).findElement(By.className("ef")).sendKeys("A5268413");
        webDriver.findElement(By.className("btnbox")).click();
    }


    private void extractJob(WebDriver webDriver) throws InterruptedException {
        WebElement j_joblist = webDriver.findElements(By.className("j_joblist")).get(0);
        List<WebElement> er = j_joblist.findElements(By.className("er"));
        List<WebElement> el = j_joblist.findElements(By.className("el"));
        List<JavaJob51> objects = CollUtil.newArrayList();
        for (int i = 0; i < er.size(); i++) {
            JavaJob51 bossJava = new JavaJob51();
            WebElement webElementER = er.get(i);
            demo1(bossJava, webElementER);
            demo3(bossJava, webElementER);
            WebElement webElementEI = el.get(i);
            demo2(bossJava, webElementEI);
            demo4(bossJava, webElementEI);
            demo5(bossJava, webElementEI);
            demo6(bossJava, webElementEI);
            demo8(bossJava, webElementEI);
            objects.add(bossJava);
        }
        mapper.addALL(objects);
        System.out.println("插入成功");
        webDriver.quit();
        init();

    }

    public void demo1(JavaJob51 bossJava, WebElement jobElement) throws InterruptedException {
        //公司性质  互联网
        List<WebElement> p = jobElement.findElements(By.tagName("P"));
//民营公司 | 10000人以上
        String text = p.get(0).getText().trim();
        if (StrUtil.isEmpty(text)) {
            Thread.sleep(5000L);
        }
        String[] split = text.split(" | ");
        String setListed = split[0];
        if (split.length == 3) {

            String num = split[2];
            bossJava.setNum(num);
        }

        bossJava.setListed(setListed);
        String s = p.get(1).getText().trim();
        bossJava.setType(s);
        //   log.info("公司性质======================》[{}]", type);

    }

    public void demo3(JavaJob51 bossJava, WebElement jobElement) {
        //公司名称
        String companyName = jobElement.findElement(By.tagName("a")).getText();
        log.info("公司名称======================》[{}]", companyName);
        bossJava.setCompanyName(companyName);
    }


    public void demo2(JavaJob51 bossJava, WebElement jobElement) {
        //发布时间
        String time = jobElement.findElement(By.className("time")).getText();
        //  log.info("招聘人======================》[{}]", substring1);
        bossJava.setTime(time);
    }


    public void demo4(JavaJob51 bossJava, WebElement jobElement) {
        // 职位名称

        String jobName = jobElement.findElement(By.className("t")).findElements(By.tagName("span")).get(0).getText();
        //  log.info("职位名称======================》[{}]", jobName);
        bossJava.setJobName(jobName);
    }

    public void demo5(JavaJob51 bossJava, WebElement jobElement) {
        //工资  8k-14k
        String money = jobElement.findElement(By.className("sal")).getText();
        //   log.info("薪资======================》[{}]", money);
        bossJava.setMoney(money);
    }

    public void demo6(JavaJob51 bossJava, WebElement jobElement) {
        //广州-天河区  |  3-4年经验  |  本科  |  招1人
        String are = jobElement.findElement(By.className("info")).findElements(By.tagName("span")).get(1).getText().trim();
        String[] split = are.split(" | ");
        if (split.length <= 3) return;
        String city = split[0];
        String[] split1 = city.split("-");
        bossJava.setCity(split1[0]);//城市
        if (split1.length > 1) {
            bossJava.setAre(split1[1]); //区域
        }

        String year = split[2];//年限
        bossJava.setYear(year);
        if (split.length == 5) {
            bossJava.setPerNum(split[4]);
        } else {
            bossJava.setEducation(split[4]);//学历
            bossJava.setPerNum(split[6]);//招聘人数
        }


        //    log.info("地区======================》[{}]", are);
    }


    public void demo8(JavaJob51 bossJava, WebElement jobElement) {
        //福利
        List<WebElement> span = jobElement.findElements(By.tagName("span"));
        if (span.size() == 5) {

            String welfare = span.get(4).findElements(By.tagName("i")).stream().map(WebElement::getText).collect(Collectors.joining(","));
            bossJava.setWelfare(welfare);
        }
        //    log.info("待遇秒速======================》[{}]", welfare);
    }

    /**
     * 选择区域
     *
     * @param webDriver
     * @param address
     */
    private void are(WebDriver webDriver, String address) {
        WebElement areElement = webDriver.findElement(By.xpath("//*[@id='filterCollapse']/div[1]/div[2]/li/div[2]/div/a[contains(text(),'" + address + "')]"));
        //*[@id="filterCollapse"]/div[1]/div[2]/li/div[2]/div/a[2]
        areElement.click();
    }

    /**
     * 选择要求
     *
     * @param webDriver
     * @param chosenTitle
     * @param containsTitle
     */
    private void option(WebDriver webDriver, String chosenTitle, String containsTitle) {
        WebElement containsElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + chosenTitle + "')]"));
        WebElement optionElement = containsElement.findElement(By.xpath("../a[contains(text(),'" + containsTitle + "')]"));
        optionElement.click();
    }

    /**
     * 选择工资
     *
     * @param webDriver
     * @param state
     * @param money
     * @param jobStyle
     */
    private void mon(WebDriver webDriver, String state, String money, String jobStyle) {
        WebElement element = webDriver.findElement(By.xpath(" //*[@id='order']/li/div[1]/a[contains(text(),'" + state + "')]"));
        element.click();
        WebElement moneyElement = webDriver.findElement(By.xpath("//div[@class='item salary selectUI']//div[@class='selectUI-text text']//span[contains(text(),'不限')]"));
        moneyElement.click();
        WebElement numElement = moneyElement.findElement(By.xpath("..//a[contains(text(),'" + money + "')]"));
        numElement.click();
        webDriver.findElement(By.xpath("//*[@id='order']/li/div[3]/div/span")).click();
        WebElement workElement = webDriver.findElement(By.xpath("//*[@id='order']/li/div[3]/div/ul/li[2]/a[contains(text(),'" + jobStyle + "')]"));
        workElement.click();
    }

}
