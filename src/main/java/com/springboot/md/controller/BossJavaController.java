package com.springboot.md.controller;

import cn.hutool.core.collection.CollUtil;
import com.springboot.md.config.AbstracController;
import com.springboot.md.dao.BossJavaJobMapper;
import com.springboot.md.pojo.BossJavaJob;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author 秒度111
 * @Date 2019/7/19 0019 22:09
 */
@RestController
@RequestMapping("/BossJavaController")
@Slf4j
public class BossJavaController extends AbstracController {

    private static final List<String> list1 = CollUtil.newArrayList("不限", "高中", "大专", "本科", "硕士", "博士");
    private static final List<String> list2 = CollUtil.newArrayList("不限", "未融资", "天使轮", "A轮", "B轮", "C轮", "D轮及以上", "已上市", "不需要融资");
    private static List<Map<String, Objects>> relust = CollUtil.newArrayList();

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Resource
    private BossJavaJobMapper mapper;

    @RequestMapping(value = "initBoos")
    public void init() {
        //设置webdriver路径
        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(BossJavaController.class.getClassLoader().getResource("chromedriver.exe")).getPath());
        //指定要爬取的地址
        String httpUrl = "127.0.0.1:11000";  // 代理IP示例
        // 设置代理IP
        ChromeOptions options = new ChromeOptions();
        //  options.addArguments("--proxy-server=http://" + httpUrl);
        // 开启开发者模式
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        WebDriver webDriver = new ChromeDriver(options);
        // 窗口最大化
        //    webDriver.manage().window().maximize();
        webDriver.get("https://www.zhipin.com/c100010000-p100101/?page=1&ka=page-1");
        //通过Xpath选择元素
//        //选择地区
//        are(webDriver, "武汉");
//        //选择基本信息
//        option(webDriver, "工作经验", "3年及以下");
//        option(webDriver, "学历要求", "本科");
//        option(webDriver, "融资阶段", "不限");
//        option(webDriver, "公司规模", "不限");
//        option(webDriver, "行业领域", "不限");
//        //选择排序
//        mon(webDriver, "最新", "5k-10k", "全职");
        //解析页面中的元素
        extractJob(webDriver);
    }


    public void demo1(BossJavaJob bossJava, WebElement jobElement) {
        //公司性质  互联网
        String type = jobElement.findElement(By.className("company-text")).findElement(By.className("false-link")).getText();
        //   log.info("公司性质======================》[{}]", type);
        bossJava.setType(type);
    }

    public void demo2(BossJavaJob bossJava, WebElement jobElement) {
        //招聘人
        String name = jobElement.findElement(By.className("name")).getText();
        String substring1 = name.substring(0, 3);
        //  log.info("招聘人======================》[{}]", substring1);
        bossJava.setName(substring1);
    }

    public void demo3(BossJavaJob bossJava, WebElement jobElement) {

        //公司名称
        String companyName = jobElement.findElement(By.className("company-text")).findElement(By.className("name")).getText();
        log.info("公司名称======================》[{}]", companyName);
        bossJava.setCompanyName(companyName);
    }

    public void demo4(BossJavaJob bossJava, WebElement jobElement) {
        // 职位名称
        String jobName = jobElement.findElement(By.className("job-name")).getText();
        //  log.info("职位名称======================》[{}]", jobName);
        bossJava.setJobName(jobName);
    }

    public void demo5(BossJavaJob bossJava, WebElement jobElement) {
        //工资  8k-14k
        String money = jobElement.findElement(By.className("red")).getText();
        //   log.info("薪资======================》[{}]", money);
        bossJava.setMoney(money);
    }

    public void demo6(BossJavaJob bossJava, WebElement jobElement) {
        //地区 [漕河泾]
        String are = jobElement.findElement(By.className("job-area")).getText();
        String[] split = are.split("·");
        bossJava.setCity(split[0]);//城市
        bossJava.setAre(split[1]); //区域
        if (split.length > 2) {
            bossJava.setStreet(split[2]); //街道
        }
        //    log.info("地区======================》[{}]", are);
    }

    public void demo7(BossJavaJob bossJava, WebElement jobElement) {
        String education = ((RemoteWebElement) jobElement).findElementByTagName("p").getText();
        for (String s : list1) {
            if (education.endsWith(s)) {
                //年限
                String year = education.substring(0, education.length() - 2);
                //学历
                String s1 = education.split(year)[1];
                bossJava.setYear(year);
                bossJava.setEducation(s1);
                //    log.info("年限要求======================》[{}]", year);
                //      log.info("学历要求======================》[{}]", s1);
                break;
            }
        }
    }

    public void demo8(BossJavaJob bossJava, WebElement jobElement) {

        //福利
        String welfare = jobElement.findElement(By.className("info-desc")).getText();
        //    log.info("待遇秒速======================》[{}]", welfare);
        bossJava.setWelfare(welfare);
    }

    public void demo9(BossJavaJob bossJava, WebElement jobElement) {

        //  技术栈   Java
        List<WebElement> list = jobElement.findElements(By.className("tag-item"));
        String collect = list.stream().map(WebElement::getText).collect(Collectors.joining(","));
        bossJava.setTechnology(collect);
        //    log.info("技术栈======================》[{}]", collect);

    }

    public void demo10(BossJavaJob bossJava, WebElement jobElement) {
        //互联网不需要融资500-999人
        String p = jobElement.findElement(By.className("company-text")).findElement(By.tagName("p")).getText();
        for (String s : list2) {
            if (p.contains(s)) {
                String[] split1 = p.split(s);
                //人数
                String num = split1[1];
                bossJava.setNum(num);
                bossJava.setListed(s);
                //       log.info("人数=====================》[{}]", num);
                //       log.info("上市情况=====================》[{}]", s);
            }
        }

    }

    private void extractJob(WebDriver webDriver) {
        List<WebElement> elements = webDriver.findElements(By.className("job-primary"));
        List<BossJavaJob> collect = elements.stream().map(jobElement -> {
            BossJavaJob bossJava = new BossJavaJob();
//            executor.execute(() -> demo1(bossJava, jobElement));
//            executor.execute(() -> demo2(bossJava, jobElement));
//            executor.execute(() -> demo3(bossJava, jobElement));
//            executor.execute(() -> demo4(bossJava, jobElement));
//            executor.execute(() -> demo5(bossJava, jobElement));
//            executor.execute(() -> demo6(bossJava, jobElement));
//            executor.execute(() -> demo7(bossJava, jobElement));
//            executor.execute(() -> demo8(bossJava, jobElement));
//            executor.execute(() -> demo9(bossJava, jobElement));
//            executor.execute(() -> demo10(bossJava, jobElement));
            demo1(bossJava, jobElement);
            demo2(bossJava, jobElement);
            demo3(bossJava, jobElement);
            demo4(bossJava, jobElement);
            demo5(bossJava, jobElement);
            demo6(bossJava, jobElement);
            demo7(bossJava, jobElement);
            demo8(bossJava, jobElement);
            demo9(bossJava, jobElement);
            demo10(bossJava, jobElement);
            return bossJava;
        }).collect(Collectors.toList());
        mapper.add(collect);
        //解析下一页
        try {
            WebElement pagerNext = webDriver.findElement(By.className("page"));
            //下一页不可点  next disabled
            if (pagerNext.getText().contains("...")) {
                List<WebElement> elements1 = webDriver.findElement(By.className("page")).findElements(By.tagName("a"));
                elements1.get(elements1.size() - 1).click();
                //  pagerNext.click();
                System.out.println("--------------------解析下一页-----------------------");
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                extractJob(webDriver);
            }
        } catch (Exception e) {

        }

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
