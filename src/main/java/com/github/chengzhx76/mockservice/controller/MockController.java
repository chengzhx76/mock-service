package com.github.chengzhx76.mockservice.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.github.chengzhx76.mockservice.util.IpAdrressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:
 * @author: hp
 * @date: 2019/6/28
 */
@RestController
public class MockController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public String hello() {
        return "Welcome to reactive world ~";
    }

    @GetMapping(value = "/**")
    public Map<String, Object> get(HttpServletRequest request) throws IOException {

        String ip = IpAdrressUtil.getIpAdrress(request);
        String path = request.getServletPath();
        String params = request.getQueryString();

        Map<String, String> mapHeaders = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            mapHeaders.put(key, request.getHeader(key));
        }

        String data = String.format("GET->请求\nIP[%s]\n路径[%s]\n头[%s]\n参数[%s]", ip, path,
                MapUtil.join(mapHeaders, "\n", "->"), params);

        logger.info(data);

        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("path", path);
        map.put("header", mapHeaders);
        map.put("params", params);

        return map;
    }

    @PostMapping(value = "/**")
    public Map<String, Object> post(HttpServletRequest request) throws IOException {

        String ip = IpAdrressUtil.getIpAdrress(request);
        String path = request.getServletPath();
        String params = request.getQueryString();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String body = IoUtil.read(reader);

        Map<String, String> mapHeaders = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            mapHeaders.put(key, request.getHeader(key));
        }

        String data = String.format("GET->请求\nIP[%s]\n路径[%s]\n头[%s]\n参数[%s]\nBody参数[%s]", ip, path,
                MapUtil.join(mapHeaders, "\n", "->"), params, body);

        logger.info(data);

        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("path", path);
        map.put("header", mapHeaders);
        map.put("params", params);
        map.put("body", JSONUtil.parseObj(body));

        return map;
    }

}
