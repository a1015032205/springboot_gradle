package com.springboot.md.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BossJavaJob implements Serializable {
    private Integer id;

    private String time;

    private String companyName;

    private String jobName;

    private String money;

    private String city;

    private String are;

    private String street;

    private String year;

    private String education;

    private String welfare;

    private String technology;

    private String num;

    private String perNum;

    private String listed;

    private String type;

    private String name;

    private String cUser;

    private Date cTime;

    private String uUser;

    private Date uTime;

    private String delTag;

    private static final long serialVersionUID = 1L;


}