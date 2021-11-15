package com.yang.hcd.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HttpClientUtilsTest {

    @Test
    public void doGetTestOne() {
        String url = "http://localhost:8080/test/hello2";
        JSONObject params = new JSONObject();
        params.put("name", "张三");
        params.put("age", 28);
        HttpClientUtils.doGetTestOne(url, params.toJSONString());
    }

    @Test
    public void doGetTestWayTwo() {

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("name", "张三"));
        nvps.add(new BasicNameValuePair("age", "18"));
        URI uri = null;
        try {
            uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(8080).setPath("test/hello2").setParameters(nvps).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpClientUtils.doGetTestWayTwo(uri);
    }

    @Test
    public void doPostTestOne() {

        String uri = "http://localhost:8080/test/hello3";
        HttpClientUtils.doPostTestOne(uri);
    }

}