package com.games.mmo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import com.alibaba.fastjson.JSONObject;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.JsonUtil;

public class TestVoice{

    private static final String testFileName = "D:\\FFOutput\\234~3.wav";
    private static final String apiKey = "mw3sabsgR096zm0rSyiuoVXs";
    private static final String secretKey = "34439fea473d88815bb2249258e13f19";
    private static final String cuid = "testRole";
	
    private static final String serverURL = "http://vop.baidu.com/server_api";
    private static String token = "";


    public static void main(String[] args) throws Exception {
          getToken();
//          method1();
          //method2 速度最快
//          method2();


    }

    public static HttpURLConnection getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" + 
            "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = ((OthBean)JsonUtil.toBean(printResponse(conn), OthBean.class)).access_token;//(printResponse(conn)).getString("access_token");
        return conn;
    }

    public static String method1(Integer len,String text) throws Exception {
//    	long startTime=System.currentTimeMillis();
//        File pcmFile = new File(testFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();

        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "pcm");
        params.put("rate", 8000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", len);
//        params.put("speech", DatatypeConverter.printBase64Binary(loadFile(pcmFile)));
        params.put("speech", text);
        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        String msg=printResponse(conn);
        ResultBean resultBean=JsonUtil.toBean(msg, ResultBean.class);
        return resultBean.result[0];
//      System.out.println(System.currentTimeMillis()-startTime);
    }

    private static void method2(String text) throws Exception {
    	long startTime=System.currentTimeMillis();
        File pcmFile = new File(testFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL
                + "?cuid=" + cuid + "&token=" + token).openConnection();

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "audio/wav; rate=8000");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(loadFile(pcmFile));
        wr.flush();
        wr.close();

        printResponse(conn);
        System.out.println(System.currentTimeMillis()-startTime);
    }

    private static String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            // request error
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append("");
        }
        rd.close();
//        System.out.println(response.toString());
        return response.toString();
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
    
    public static class OthBean {
    	public String access_token;
    	public String session_key;
    	public String scope;
    	public String refresh_token;
    	public String session_secret;
    	public String expires_in;
    }
    
    public static class ResultBean {
    	public String corpus_no;
    	public String err_msg;
    	public String err_no;
    	public String[] result;
    	public String sn;

    }


}