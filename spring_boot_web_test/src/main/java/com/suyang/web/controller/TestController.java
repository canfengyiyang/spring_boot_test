package com.suyang.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String helloWorld() {

        return "hello wolrd";
    }

    @GetMapping("/hello2")
    public String helloWorld2(@RequestParam("name") String name,
                              @RequestParam("age") Integer age) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "OK");
        jsonObject.put("name", name);
        jsonObject.put("age", age);

        System.out.println(jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }


    @PostMapping("/hello3")
    public String helloWorld3() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "OK");
        jsonObject.put("name", "name");
        jsonObject.put("age", "age");

        System.out.println(jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }

    @PostMapping("/hello4")
    public String helloWorld4(@RequestParam("name") String name,
                              @RequestParam("age") Integer age) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "OK");
        jsonObject.put("name", name);
        jsonObject.put("age", age);

        System.out.println(jsonObject.toJSONString());

        return jsonObject.toJSONString();
    }
}
