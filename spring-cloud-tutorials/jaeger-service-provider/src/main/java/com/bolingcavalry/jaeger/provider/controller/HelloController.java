package com.bolingcavalry.jaeger.provider.controller;

import com.bolingcavalry.common.Constants;
import com.bolingcavalry.jaeger.provider.util.RedisUtils;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author will
 * @email zq2599@gmail.com
 * @date 2021/9/12 7:26 上午
 * @description 功能介绍
 */
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private Tracer tracer;

    @Autowired
    private RedisUtils redisUtils;

    private String dateStr(){
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }


    private void mockBiz() {
        log.info("hello");
    }

    /**
     * 返回字符串类型
     * @return
     */
    @GetMapping("/hello")
    public String hello() {
        // 生成当前时间
        String timeStr = dateStr();

        Span span = tracer.buildSpan("mockBiz")
                    .withTag("time-str", timeStr)
                    .start();

        span.log("normal span log");

        mockBiz();

        span.finish();


        // 写入redis
        redisUtils.set("Hello",  timeStr);
        // 返回
        return Constants.HELLO_PREFIX + ", " + timeStr;
    }

}
