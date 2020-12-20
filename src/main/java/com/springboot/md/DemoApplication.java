package com.springboot.md;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootApplication
@Slf4j
@MapperScan("com.springboot.md.dao")
@EnableScheduling
@EnableAsync
public class DemoApplication extends SpringApplication {

    public DemoApplication() {

    }

    public DemoApplication(Class... primarySources) {
        super(primarySources);
    }

    public static void main(String[] args) throws IOException {

//        String filePath = "E:\\java\\WorkSpace\\" + "application.yml";
//        InputStream ism = new FileInputStream(filePath);
//        Properties properties = new Properties();
//        properties.load(ism);
        DemoApplication demoApplication = new DemoApplication(DemoApplication.class);
        // demoApplication.setDefaultProperties(properties);
        demoApplication.run(args);
    }


    @Override
    protected void refresh(ConfigurableApplicationContext applicationContext) {
        log.info("================  应用预检完成，准备启动 =================");
        Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
        super.refresh(applicationContext);
    }

    @Override
    protected void afterRefresh(ConfigurableApplicationContext context, ApplicationArguments args) {
        log.info("================  容器刷新成功，即将启动 =================");
    }
}

