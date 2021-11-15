package com.yang.hcd.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);


    public static String doGetTestOne(String url, String params) {
        try {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotBlank(params)) {
                JSONObject paramsJson = JSONObject.parseObject(params);
                for (Map.Entry<String, Object> entry : paramsJson.entrySet()) {
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "utf-8"));
                }
                params = sb.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url + "?" + params);

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient(false, false, null);

        return execute(httpClient, httpGet);
    }

    public static String execute(CloseableHttpClient httpClient, HttpUriRequest request) {
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(request);
            LOGGER.info("响应状态为: {}", response.getStatusLine());

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                LOGGER.info("响应内容长度为: {}", responseEntity.getContentLength());

                String result = EntityUtils.toString(responseEntity);
                LOGGER.info("响应内容为: {}", result);
                return result;
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * GET---有参测试 (方式二:将参数放入键值对类中,再放入URI中,从而通过URI得到HttpGet实例)
     *
     */
    public static String doGetTestWayTwo(URI uri) {
        // 创建Get请求
        HttpGet httpGet = new HttpGet(uri);

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient(false, false, null);
        return execute(httpClient, httpGet);
    }

    /**
     * POST---无参测试
     *
     */
    public static String doPostTestOne(String uri) {
        // 创建Post请求
        HttpPost httpPost = new HttpPost(uri);

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient(false, false, null);
        return execute(httpClient, httpPost);
    }

    /**
     * POST---有参测试(普通参数)
     */
    public static String doPostTestFour(String uri, String params) {

        // 创建Post请求
        HttpPost httpPost = new HttpPost(uri + "?" + params);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();

        httpPost.setConfig(requestConfig);


        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = getHttpClient(false, false, null);
        return execute(httpClient, httpPost);
    }

    /**
     * POST---有参测试(对象参数)
     *
     * @date 2018年7月13日 下午4:18:50
     */
    public static void doPostTestTwo() {

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:12345/doPostControllerTwo");
        User user = new User();
        user.setName("潘晓婷");
        user.setAge(18);
        user.setGender("女");
        user.setMotto("姿势要优雅~");
        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)
        String jsonString = JSON.toJSONString(user);

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * POST---有参测试(普通参数 + 对象参数)
     *
     * @date 2018年7月13日 下午4:18:50
     */
    public void doPostTestThree() {

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        // 参数
        URI uri = null;
        try {
            // 将参数放入键值对类NameValuePair中,再放入集合中
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("flag", "4"));
            params.add(new BasicNameValuePair("meaning", "这是什么鬼？"));
            // 设置uri信息,并将参数集合放入uri;
            // 注:这里也支持一个键值对一个键值对地往里面放setParameter(String key, String value)
            uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(12345)
                    .setPath("/doPostControllerThree").setParameters(params).build();
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }

        HttpPost httpPost = new HttpPost(uri);
        // HttpPost httpPost = new
        // HttpPost("http://localhost:12345/doPostControllerThree1");

        // 创建user参数
        User user = new User();
        user.setName("潘晓婷");
        user.setAge(18);
        user.setGender("女");
        user.setMotto("姿势要优雅~");

        // 将user对象转换为json字符串，并放入entity中
        StringEntity entity = new StringEntity(JSON.toJSONString(user), "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        RequestConfig config = RequestConfig.custom().
                setConnectionRequestTimeout()

        httpPost.setConfig(config);

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据是否是https请求，是否需要证书校验，获取HttpClient客户端
     *
     * @param isHttps      是否是HTTPS请求
     * @param needVerifyCa 是否需要证书校验，如果是http请求，选择false
     * @param caName       证书路径名，如果需要证书需要填写证书路径 比如：xxx/xxx.crt
     * @return HttpClient实例
     */
    private static CloseableHttpClient getHttpClient(boolean isHttps, boolean needVerifyCa, String caName) {
        CloseableHttpClient httpClient;
        if (isHttps) {
            SSLConnectionSocketFactory sslSocketFactory;
            try {
                if (!needVerifyCa) {
                    //不作证书校验
                    sslSocketFactory = getSocketFactory(needVerifyCa, null, null);
                } else {
                    //需要证书检验
                    // 证书
                    InputStream ca = this.getClass().getClassLoader().getResourceAsStream(caName);
                    // 证书的别名，即:key。 注:cAalias只需要保证唯一即可，不过推荐使用生成keystore时使用的别名。
                    String cAalias = System.currentTimeMillis() + "" + new SecureRandom().nextInt(1000);
                    sslSocketFactory = getSocketFactory(true, ca, cAalias);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
            return httpClient;
        }
        httpClient = HttpClientBuilder.create().build();
        return httpClient;
    }

    /**
     * HTTPS辅助方法, 为HTTPS请求 创建SSLSocketFactory实例、TrustManager实例
     *
     * @param needVerifyCa  是否需要检验CA证书(即:是否需要检验服务器的身份)
     * @param caInputStream CA证书。(若不需要检验证书，那么此处传null即可)
     * @param cAalias       别名。(若不需要检验证书，那么此处传null即可)
     *                      注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @return SSLConnectionSocketFactory实例
     * @throws NoSuchAlgorithmException 异常信息
     * @throws CertificateException     异常信息
     * @throws KeyStoreException        异常信息
     * @throws IOException              异常信息
     * @throws KeyManagementException   异常信息
     */
    private static SSLConnectionSocketFactory getSocketFactory(boolean needVerifyCa, InputStream caInputStream, String cAalias)
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException,
            IOException, KeyManagementException {
        X509TrustManager x509TrustManager;
        // https请求，需要校验证书
        if (needVerifyCa) {
            KeyStore keyStore = getKeyStore(caInputStream, cAalias);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            x509TrustManager = (X509TrustManager) trustManagers[0];
            // 这里传TLS或SSL其实都可以的
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            return new SSLConnectionSocketFactory(sslContext);
        }
        // https请求，不作证书校验
        x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                // 不验证
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
        return new SSLConnectionSocketFactory(sslContext);
    }

    /**
     * 获取(密钥及证书)仓库
     * 注:该仓库用于存放 密钥以及证书
     *
     * @param caInputStream CA证书(此证书应由要访问的服务端提供)
     * @param cAalias       别名
     *                      注意:别名应该是唯一的， 别名不要和其他的别名一样，否者会覆盖之前的相同别名的证书信息。别名即key-value中的key。
     * @return 密钥、证书 仓库
     * @throws KeyStoreException        异常信息
     * @throws CertificateException     异常信息
     * @throws IOException              异常信息
     * @throws NoSuchAlgorithmException 异常信息
     * @date 2019/6/11 18:48
     */
    private static KeyStore getKeyStore(InputStream caInputStream, String cAalias)
            throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        // 证书工厂
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 秘钥仓库
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        keyStore.setCertificateEntry(cAalias, certificateFactory.generateCertificate(caInputStream));
        return keyStore;
    }


    /**
     * 发送文件
     * <p>
     * multipart/form-data传递文件(及相关信息)
     * <p>
     * 注:如果想要灵活方便的传输文件的话，
     * 除了引入org.apache.httpcomponents基本的httpclient依赖外
     * 再额外引入org.apache.httpcomponents的httpmime依赖。
     * 追注:即便不引入httpmime依赖，也是能传输文件的，不过功能不够强大。
     */
    public static void test4() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:12345/file");
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // 第一个文件
            String filesKey = "files";
            File file1 = new File("C:\\Users\\JustryDeng\\Desktop\\back.jpg");
            multipartEntityBuilder.addBinaryBody(filesKey, file1);
            // 第二个文件(多个文件的话，使用同一个key就行，后端用数组或集合进行接收即可)
            File file2 = new File("C:\\Users\\JustryDeng\\Desktop\\头像.jpg");
            // 防止服务端收到的文件名乱码。 我们这里可以先将文件名URLEncode，然后服务端拿到文件名时在URLDecode。就能避免乱码问题。
            // 文件名其实是放在请求头的Content-Disposition里面进行传输的，如其值为form-data; name="files"; filename="头像.jpg"
            multipartEntityBuilder.addBinaryBody(filesKey, file2, ContentType.DEFAULT_BINARY, URLEncoder.encode(file2.getName(), "utf-8"));
            // 其它参数(注:自定义contentType，设置UTF-8是为了防止服务端拿到的参数出现乱码)
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            multipartEntityBuilder.addTextBody("name", "邓沙利文", contentType);
            multipartEntityBuilder.addTextBody("age", "25", contentType);

            HttpEntity httpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(httpEntity);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("HTTPS响应内容为:" + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送流
     */

    public static void test5() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:12345/is?name=邓沙利文");
        CloseableHttpResponse response = null;
        try {
            InputStream is = new ByteArrayInputStream("流啊流~".getBytes());
            InputStreamEntity ise = new InputStreamEntity(is);
            httpPost.setEntity(ise);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("HTTPS响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("HTTPS响应内容长度为:" + responseEntity.getContentLength());
                // 主动设置编码，来防止响应乱码
                String responseStr = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                System.out.println("HTTPS响应内容为:" + responseStr);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
