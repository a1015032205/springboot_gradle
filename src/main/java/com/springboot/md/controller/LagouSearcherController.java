package com.springboot.md.controller;

import com.springboot.md.config.AbstracController;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author 秒度111
 * @Date 2019/7/19 0019 22:09
 */
@RestController
@Slf4j
@RequestMapping(value = "/LagouSearcherController")
public class LagouSearcherController extends AbstracController {


    @RequestMapping(value = "init")
    public void init() {
        //设置webdriver路径
        System.setProperty("webdriver.chrome.driver", Objects.requireNonNull(LagouSearcherController.class.getClassLoader().getResource("chromedriver.exe")).getPath());
        //创建webDriver
        WebDriver webDriver = new ChromeDriver();
        //指定要爬取的地址
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


    /**
     * 爬取数据
     *
     * @param webDriver
     * @return
     */
    private void extractJob(WebDriver webDriver) {
        List<WebElement> elements = webDriver.findElements(By.xpath("job-primary"));
        for (WebElement element : elements) {
            String name = element.findElement(By.className("job-name")).getText();
            System.out.println(name);
        }
        List<WebElement> jobElements = webDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            Map<String, Object> map = new HashMap<>();
            //公司名称  上海羿楫信息
            String companyName = jobElement.findElement(By.className("company_name")).getText();
            map.put("companyName", companyName);
            //工资  8k-14k
            String money = jobElement.findElement(By.className("money")).getText();
            map.put("money", money);
            //区域 [漕河泾]
            String add = jobElement.findElement(By.className("add")).getText();
            int length = add.length();
            //发布时间
            String ftime = jobElement.findElement(By.className("format-time")).getText();
            map.put("ftime", ftime);
            //福利
            String welfare = jobElement.findElement(By.className("li_b_r")).getText();
            final int length1 = welfare.length();
            final String substring = welfare.substring(1, length1 - 1);
            map.put("welfare", substring);
            //漕河泾
            String region = add.substring(1, length - 1);
            map.put("region", region);
            //经验学历 9k-12k 经验1-3年 / 本科
            String expAndStudy = jobElement.findElement(By.className("li_b_l")).getText();
            String[] split = expAndStudy.split(" ");
            //工作经验
            String workExp = split[1];
            map.put("workExp", workExp);
            //学历
            String school = split[3];
            map.put("school", school);
            //企业性质，融资，人数  移动互联网 / 未融资 / 15-50人
            String num = jobElement.findElement(By.className("industry")).getText();
            final String[] nums = num.split(" / ");
            //企业性质
            final String nature = nums[0];
            map.put("nature", nature);
            //融资
            final String financing = nums[1];
            map.put("financing", financing);
            //人数
            final String people = nums[2];
            map.put("people", people);
            //网站ID 5710719
//            String id = jobElement.findElement(By.className("target_position")).getAttribute("value");
//            map.put("webId", id);
          /*  //HR图片
            String hr_portrait = jobElement.findElement(By.className("hr_portrait")).getAttribute("value");
            //HR名字
            String hr_name = jobElement.findElement(By.className("hr_name")).getAttribute("value");
            //HR职位
            String hr_position = jobElement.findElement(By.className("hr_position")).getAttribute("value");
            //HR的ID
            String target_hr = jobElement.findElement(By.className("target_hr")).getAttribute("value");*/
            System.out.println(map);
        }
        //解析下一页
        try {
            WebElement pagerNext = webDriver.findElement(By.className("pager_next"));
            //下一页不可点
            if (!pagerNext.getAttribute("class").contains("pager_next pager_next_disabled")) {
                pagerNext.click();
                System.out.println("--------------------解析下一页-----------------------");
                try {
                    Thread.sleep(1000L);
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
