package com.games.mmo.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import com.storm.lib.util.ExceptionUtil;

public class HttpURLUtil {
	private int connectTimeOut = 30 * 1000;
	private int timeOut = 30 * 1000;
	
	public String httpPost(String urlPath,String params) {
		String result=null;
		try{
            String queryString = params;
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//    		conn.setRequestProperty("content-type", "text/json");
//    		conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
//          conn.setRequestProperty( "User-Agent", userAgent );
            conn.setReadTimeout(timeOut); //读取超时 单位毫秒
            conn.setConnectTimeout(connectTimeOut);  //连接超时 单位毫秒
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
//            String str = String.format(Locale.CHINA, queryString);
            writer.write(queryString);
            writer.flush();
            tryClose(writer);
            tryClose(os);
            conn.connect();
            InputStream is = conn.getInputStream();
            result = stream2String(is);

    	} catch(Exception e) {
    		ExceptionUtil.processException(e,true);
    	}

    	return result;
    }
	
	
	/**
	 * 获取流中的字符串
	 * @param is
	 * @return
	 */
	private String stream2String( InputStream is ) {
		BufferedReader br = null;
		try{
			br = new BufferedReader( new java.io.InputStreamReader( is ));	
			String line = "";
			StringBuilder sb = new StringBuilder();
			while( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}
			return sb.toString();
		} catch( Exception e ) {
			ExceptionUtil.processException(e,true);
		} finally {
			tryClose( br );
		}
		return "";
	}
	
	
	/**
	 * 关闭输出流
	 * @param os
	 */
	private void tryClose( OutputStream os ) {
		try{
			if( null != os ) {
				os.close();
				os = null;
			}
		} catch( Exception e ) {
			ExceptionUtil.processException(e,true);
		}
	}
	
	/**
	 * 关闭writer
	 * @param writer
	 */
	private void tryClose( java.io.Writer writer ) {
		try{
			if( null != writer ) {
				writer.close();
				writer = null;
			}
		} catch( Exception e ) {
			ExceptionUtil.processException(e,true);
		}
	}
	
	/**
	 * 关闭Reader
	 * @param reader
	 */
	private void tryClose( java.io.Reader reader ) {
		try{
			if( null != reader ) {
				reader.close();
				reader = null;
			}
		} catch( Exception e ) {
			ExceptionUtil.processException(e,true);
		}
	}
}
