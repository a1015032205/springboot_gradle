package com.springboot.md.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * info
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Info implements Serializable {
    private Integer id;

    private String name;

    private Integer age;

    private static final long serialVersionUID = 1L;
}