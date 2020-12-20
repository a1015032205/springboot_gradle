package com.springboot.md.config;

import cn.hutool.core.date.DateUtil;
import com.springboot.md.aop.WebLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author 秒度
 * @Email fangxin.md@Gmail.com
 * @Date 2020/9/30 下午1:01
 * @Description 抽象控制器
 */
@Slf4j
@Component
//@RequestLog
@WebLog
public class AbstracController implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    protected ResponseEntity<ModelMap> setSuccessModelMap() {
        return setSuccessModelMap(new ModelMap(), null);
    }


    protected ResponseEntity<ModelMap> setSuccessModelMap(Object data) {
        return setSuccessModelMap(new ModelMap(), data);
    }


    protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap) {
        return setSuccessModelMap(modelMap, null);
    }


    protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap, Object data) {
        return setSuccessModelMap(modelMap, HttpStatus.OK, data);
    }


    protected ResponseEntity<ModelMap> setSuccessModelMap(ModelMap modelMap, HttpStatus httpStatus, Object data) {
        return setModelMap(modelMap, String.valueOf(httpStatus.value()), httpStatus.getReasonPhrase(), data);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Field[] fields = getClass().getDeclaredFields();

        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object o = field.get(this);
                Class<?> type = field.getType();
                if (o == null && type.getSimpleName().toLowerCase().contains("service")) {
                    o = applicationContext.getBean(type);
                    field.set(this, o);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected ResponseEntity<ModelMap> setModelMap(ModelMap modelMap, String code, String msg, Object data) {
        if (!modelMap.isEmpty()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.putAll(modelMap);
            modelMap.clear();
            map.keySet().stream()
                    .filter(key -> !key.startsWith("org.springframework.validation.BindingResult") && !key.equalsIgnoreCase("void"))
                    .forEach(key -> modelMap.put(key, map.get(key)));
        }

        if (data != null) {
            if (data instanceof Collection) {
                modelMap.put("row", data);
                modelMap.put("total", ((Collection) data).size());
            } else {
                modelMap.put("data", data);
            }
        }

        modelMap.put("code", code);
        modelMap.put("message", msg);
        modelMap.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return ResponseEntity.ok(modelMap);
    }
}
