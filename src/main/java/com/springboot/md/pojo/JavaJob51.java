package com.springboot.md.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaJob51   {
    @JSONField(serialize = false  )
    private Integer id;

    private String companyName;

    private String jobName;

    private String money;

    private String city;

    private String are;

    private String perNum;

    private String year;

    private String education;

    private String welfare;

    private String time;

    private String num;

    private String listed;

    private String type;

    private String name;

    private String cUser;

    private Date cTime;

    private String uUser;

    private Date uTime;

    private String delTag;

}