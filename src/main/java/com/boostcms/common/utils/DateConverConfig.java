package com.boostcms.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * 
 * 
 * @author boostcms
 * @email boostcms@163.com
 * @date 2020-01-30 21:11:22
 */


@Configuration
public class DateConverConfig {
    @Bean
    public Converter<String, Date> stringDateConvert() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = sdf.parse((String) source);
                } catch (Exception e) {
                    SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = sdfday.parse((String) source);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
                return date;
            }
        };
    }
    
    
    @Bean
    public Converter<String, String> stringStringConvert() {
        return new Converter<String, String>() {
            @Override
            public String convert(String source) {
               if(source == null) {
            	   return null;
               }
                return StringUtils.normalizeSpace(source) ;
            }
        };
    }

}
