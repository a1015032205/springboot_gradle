package com.springboot.md.controller;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 下午3:12
 * @Description
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.springboot.md.config.AbstracController;
import com.springboot.md.dao.JavaJob51Mapper;
import com.springboot.md.pojo.JavaJob51;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/FileController")
public class FileController extends AbstracController implements InitializingBean {

    @Resource
    private JavaJob51Mapper mapper;

    public FileController() {
        System.out.println("DemoController");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct");
    }

    @PostMapping("/upload")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            String originalFilename = file.getOriginalFilename();
            String name = new String(originalFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            File file1 = FileUtil.file("/usr/local/files/" + name);
            FileUtil.writeBytes(file.getBytes(), file1);
        } catch (Exception e) {
            return setSuccessModelMap(ExceptionUtil.stacktraceToString(e));
        }
        return setSuccessModelMap("写入成功");
    }

    @RequestMapping("/down")
    public Object down(HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=all.zip");
        ServletOutputStream outputStream = response.getOutputStream();
        String name;
        ArrayList<Object> objects = CollUtil.newArrayList();
        String s;
        try {
            List<File> list = FileUtil.loopFiles("/usr/local/files/");
            int i = 0;
            List<File> renameList = new
                    ArrayList<>();
            for (File file : list) {
                HashMap<String, Object> map = new HashMap<>();
                String name1 = file.getName();
                String STR = new String(name1.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                File file1 = FileUtil.writeBytes(FileUtil.readBytes(file), "/usr/local/files/copy/" + "需求" + ++i + ".DOC");
                //    File rename = FileUtil.copy(file, FileUtil.file("/usr/local/files/copy/" + STR), false);
                renameList.add(file1);
                map.put("name1", name1);
                map.put("STR", STR);
                String name2 = file1.getName();
                map.put("file1", name2);
                String STR1 = new String(name2.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                map.put("file2", STR1);
                objects.add(map);
            }
            File zip = ZipUtil.zip(new File("/usr/local/files/all.zip"), false, renameList.toArray(new File[]{}));
            outputStream.write(FileUtil.readBytes(zip));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            return setSuccessModelMap(ExceptionUtil.stacktraceToString(e));
        }
        objects.forEach(System.out::println);
        return setSuccessModelMap(objects);
    }

    @PostMapping("/demo")
    @ResponseBody
    public Object demo(@RequestParam("name") String name,
                       @RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        System.out.println(name);
        List<File> jpg = FileUtil.loopFiles("E:\\DOTA2", pathname -> pathname.getName().toLowerCase().endsWith("jpg"));
        return null;
    }

    @GetMapping("/downXls")
    public void downXls(HttpServletResponse response) throws Exception {
        ExcelWriter writer = ExcelUtil.getWriter(true);
        List<JavaJob51> limit = mapper.getLimit();
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("companyName", "公司名称");
        writer.addHeaderAlias("jobName", "工作");
        writer.addHeaderAlias("money", "薪资");
        writer.addHeaderAlias("city", "城市");
        writer.write(limit, true);

        writer.autoSizeColumnAll();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=test.xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        IoUtil.close(out);
    }

}
