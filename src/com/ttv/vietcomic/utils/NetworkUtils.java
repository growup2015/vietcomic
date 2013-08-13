package com.ttv.vietcomic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {
	
	public static boolean canConnect(Activity context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo wifi = connec .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		android.net.NetworkInfo mobile = connec .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isConnected()) {
			return true;
		} else if (!mobile.isConnected()) {
			return false;
		} else if (mobile.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static boolean canConnectMobile(Activity context) {
		ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		android.net.NetworkInfo mobile = connec .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (!mobile.isConnected()) {
			return false;
		} else if (mobile.isConnected()) {
			return true;
		}
		return false;
	}
	
	public static String doPost(String url, ArrayList<String> paramName, ArrayList<String> paramValue) throws Exception {
		try {
			int timeoutConnection = 15000;
			int timeoutSocket = 20000;
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(url);
			
			// Set content to post
			if (paramName != null && !paramName.isEmpty()) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
				int index = 0;
				for (String name : paramName) {
					nameValuePairs.add(new BasicNameValuePair(name, paramValue.get(index++)));
				}
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			// Set header
			// ttvclip&OAPassword=ttv@clip!1
			// osType = 2 -> Android
			// httppost.addHeader("appHeader", Constant.OWNER_HEADER);
				
			// Get response
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity ht = response.getEntity();
			
			BufferedHttpEntity buf = new BufferedHttpEntity(ht);

			InputStream is = buf.getContent();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));

			StringBuilder data = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			     data.append(line).append("\n");
			}
			String sData = data.toString();
			if (sData.endsWith("\n")) sData = sData.substring(0, sData.length() - 1);
			
			try {
				if (is != null) is.close();
			} catch (Exception e2) {
			}
			
			return sData;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String doGet(String url) throws Exception {
		HttpURLConnection httpConn = null;
		try {
			InputStream is = null;
            URLConnection conn = new URL(url).openConnection();
            httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            int responseCode = httpConn.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                 is = httpConn.getInputStream();
            }
			BufferedReader r = new BufferedReader(new InputStreamReader(is));

			StringBuilder data = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			     data.append(line).append("\n");
			}
			String sData = data.toString();
			if (sData.endsWith("\n")) sData = sData.substring(0, sData.length() - 1);
			return sData;
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (httpConn != null) httpConn.disconnect();
			} catch (Exception e2) {
			}
		}
	}
	
	public static String mobistoreGetMSISDN(String url) throws Exception {
		InputStream is = null;
		HttpURLConnection sc = null;
		try {
			String newAddress = url;
			sc = (HttpURLConnection)(new URL( newAddress ).openConnection());
			sc.setInstanceFollowRedirects( false );
			sc.setRequestMethod("GET");
			sc.connect();
			
			String mobistoreCookie="";
			String vivasCookie="";
			String vivasCookie2="";
			String iwebCookie="";
			String vnpCookie="";
			String vmsCookie="";
			
			boolean readLocationFromHeader = true;
			int status = sc.getResponseCode();
			int count=0;
			while (status ==  HttpURLConnection.HTTP_MOVED_TEMP){

            	System.out.println("#######################################################");
            	System.out.println("\r\nNow \""+newAddress+"\"");
				String tmpCookie = sc.getHeaderField("Set-Cookie");
				if (tmpCookie!=null && tmpCookie.length()>0){
	            	System.out.println("-------------------------------------------------------------");
					System.out.println(tmpCookie);
	            	System.out.println("-------------------------------------------------------------");
					int j = tmpCookie.indexOf("; Path=");
					if (j!=-1)
						tmpCookie = tmpCookie.substring(0,j);
					System.out.println("Cookie "+tmpCookie);
					if (newAddress.startsWith("http://mobistore.com.vn"))
						mobistoreCookie = tmpCookie;
					else if (newAddress.startsWith("http://m.vmobile.vn/wap"))
						vivasCookie = tmpCookie;					
					else if (newAddress.startsWith("http://m.vmobile.vn/publicAPI"))
						vivasCookie2 = tmpCookie;					
					else if (newAddress.startsWith("http://wap.mobifone.com.vn"))
						vmsCookie = tmpCookie;
					else if (newAddress.startsWith("http://wap.vinaphone.com.vn"))
						vnpCookie = tmpCookie;						
					else if (newAddress.startsWith("http://iweb.vmobile.vn"))
						iwebCookie = tmpCookie;						
				}
				
				if (readLocationFromHeader) newAddress = sc.getHeaderField("Location");
				if (newAddress == null) break;
				System.out.println("\r\nRedirect to \""+newAddress+"\"");
				
				System.out.println("Wait "+count);
	            
	            if (sc != null) sc.disconnect();
	            sc = (HttpURLConnection)(new URL(newAddress).openConnection());
	            sc.setInstanceFollowRedirects( false );
            	sc.setRequestMethod("GET");
            	
            	System.out.println("**************************************************************");
            	System.out.println("mobistoreCookie: "+mobistoreCookie);
            	System.out.println("vivasCookie: "+vivasCookie);
            	System.out.println("vivasCookie2: "+vivasCookie2);
            	System.out.println("iwebCookie: "+iwebCookie);
            	System.out.println("vmsCookie: "+vmsCookie);
            	System.out.println("vnpCookie: "+vnpCookie);
            	System.out.println("**************************************************************");
            	            	
            	sc.setRequestProperty("Accept","*/*");
            	sc.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            	
            	if (newAddress.startsWith("http://mobistore.vn") && mobistoreCookie!=null && mobistoreCookie.length()>0){
					sc.setRequestProperty("Cookie",mobistoreCookie);
				} else if (newAddress.startsWith("http://m.vmobile.vn/wap") && vivasCookie!=null && vivasCookie.length()>0) 
					sc.setRequestProperty("Cookie",vivasCookie);
				else if (newAddress.startsWith("http://m.vmobile.vn/publicAPI") && vivasCookie2!=null && vivasCookie2.length()>0) 
					sc.setRequestProperty("Cookie",vivasCookie2);
				else if (newAddress.startsWith("http://wap.mobifone.com.vn") && vmsCookie!=null && vmsCookie.length()>0)
					sc.setRequestProperty("Cookie",vmsCookie);
				else if (newAddress.startsWith("http://wap.vinaphone.com.vn") && vnpCookie!=null && vnpCookie.length()>0)
					sc.setRequestProperty("Cookie",vnpCookie);
				else if (newAddress.startsWith("http://iweb.vmobile.vn") && iwebCookie!=null && iwebCookie.length()>0)
					sc.setRequestProperty("Cookie",iwebCookie);
            	
            	status = sc.getResponseCode();

            	System.out.println("HTTP RESPONSE:"+status);

	            if (count++==50) {
	            	System.out.println("Qua nhieu redirect, stop!");
	            	break;
	            }
            }			

			if (status !=  HttpURLConnection.HTTP_OK 
					&& status !=  HttpURLConnection.HTTP_INTERNAL_ERROR){
	            sc.disconnect();
	            return null;
            }
			
            is = sc.getInputStream();
            String result="";
            int c = 0;
            while (true) {
            	c = is.read();
	            if (c == -1) {
	                break;
	            }
                result = result + ((char)c);
            }
            System.out.println("||||||||||||||\n"+result+"\n||||||||||||||||||");
            
            if (!result.startsWith("84")){
            	System.out.println("Vui long truy cap qua 3G/GPRS");
            } else {
            	System.out.println("Detect thanh cong");
            }
	        return result;
	        
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {
	            if (is != null) is.close();
	            if (sc != null) sc.disconnect();
	        } catch (IOException ioe) {}
		}
	}
	
	private static HttpURLConnection createConnectionHasRedirect(String url) {
		HttpURLConnection sc = null;
		try {
			sc = (HttpURLConnection)(new URL(url).openConnection());
            sc.setInstanceFollowRedirects( false );
        	sc.setRequestMethod("GET");
        	sc.setReadTimeout(10000);
        	sc.setRequestProperty("Accept","*/*");
        	sc.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		} catch (Exception e) {
		}
		return sc;
	}
	
	public static String getMSISDN(String url) throws Exception {
		
		HttpURLConnection sc = null;
		System.out.println("\r\nStart \""+url+"\"");
		try {
			String newAddress = url;
			String mobistoreCookie="";
			String vivasCookie="";
			String vivasCookie2="";
			String iwebCookie="";
			String vnpCookie="";
			String vmsCookie="";
			
			sc = createConnectionHasRedirect(newAddress);
			
			boolean readLocationFromHeader = true;
			int status = sc.getResponseCode();
			int count = 0;
			while (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER){
            	System.out.println("#######################################################");
            	System.out.println("\r\nNow \""+newAddress+"\"");

				String tmpCookie = sc.getHeaderField("Set-Cookie");
				if (tmpCookie != null && tmpCookie.length() > 0){
	            	System.out.println("-------------------------------------------------------------");
					System.out.println(tmpCookie);
	            	System.out.println("-------------------------------------------------------------");
					int j = tmpCookie.indexOf("; Path=");
					if (j!=-1)
						tmpCookie = tmpCookie.substring(0,j);
					System.out.println("Cookie "+tmpCookie);
					
//					if (newAddress.startsWith("http://util.vn"))
//						mobistoreCookie = tmpCookie;
//					else 
					if (newAddress.startsWith("http://m.vmobile.vn/wap"))
						vivasCookie = tmpCookie;					
					else if (newAddress.startsWith("http://m.vmobile.vn/publicAPI"))
						vivasCookie2 = tmpCookie;					
					else if (newAddress.startsWith("http://wap.mobifone.com.vn"))
						vmsCookie = tmpCookie;
					else if (newAddress.startsWith("http://wap.vinaphone.com.vn"))
						vnpCookie = tmpCookie;						
					else if (newAddress.startsWith("http://iweb.vmobile.vn"))
						iwebCookie = tmpCookie;
					else
						mobistoreCookie = tmpCookie;
				}
				
				if (readLocationFromHeader) newAddress = sc.getHeaderField("Location");
				
				System.out.println("\r\nRedirect to \""+newAddress+"\"");
				if(newAddress != null) {
					int idx1 = newAddress.indexOf("&m=84");
					int idx2 = newAddress.indexOf("&msisdn=84");
					
					if (idx1 > 0) {
						String msisdn = "0"+ newAddress.substring(idx1 + 5);
						int idx3 = msisdn.indexOf("&");
						if (idx3 > 0) msisdn = msisdn.substring(0, idx3);
						System.out.println(" == Detected " + msisdn);
						return msisdn;
					} else if (idx2 > 0) {
						String msisdn = "0"+ newAddress.substring(idx2 + 10);
						int idx3 = msisdn.indexOf("&");
						if (idx3 > 0) msisdn = msisdn.substring(0, idx3);
						System.out.println(" == Detected " + msisdn);
						return msisdn;
					}
				}

				System.out.println("Wait "+count);
	            
	            if (sc != null) sc.disconnect();
	            sc = createConnectionHasRedirect(newAddress);
            	
            	System.out.println("**************************************************************");
            	System.out.println("mobistoreCookie: "+mobistoreCookie);
            	System.out.println("vivasCookie: "+vivasCookie);
            	System.out.println("vivasCookie2: "+vivasCookie2);
            	System.out.println("iwebCookie: "+iwebCookie);
            	System.out.println("vmsCookie: "+vmsCookie);
            	System.out.println("vnpCookie: "+vnpCookie);
            	System.out.println("**************************************************************");
            	            	
            	if (newAddress.startsWith("http://m.vmobile.vn/wap") && vivasCookie!=null && vivasCookie.length()>0) 
					sc.setRequestProperty("Cookie",vivasCookie);
				else if (newAddress.startsWith("http://m.vmobile.vn/publicAPI") && vivasCookie2!=null && vivasCookie2.length()>0) 
					sc.setRequestProperty("Cookie",vivasCookie2);
				else if (newAddress.startsWith("http://wap.mobifone.com.vn") && vmsCookie!=null && vmsCookie.length()>0)
					sc.setRequestProperty("Cookie",vmsCookie);
				else if (newAddress.startsWith("http://wap.vinaphone.com.vn") && vnpCookie!=null && vnpCookie.length()>0)
					sc.setRequestProperty("Cookie",vnpCookie);
				else if (newAddress.startsWith("http://iweb.vmobile.vn") && iwebCookie!=null && iwebCookie.length()>0)
					sc.setRequestProperty("Cookie",iwebCookie);
				else 
					sc.setRequestProperty("Cookie",mobistoreCookie);
            	
            	status = sc.getResponseCode();

            	System.out.println("HTTP RESPONSE:"+status);

	            if (count++==50) {
	            	System.out.println("Qua nhieu redirect, stop!");
	            	break;
	            }
	            
            }
			
			String result = "";
			if (status !=  HttpURLConnection.HTTP_OK 
					&& status !=  HttpURLConnection.HTTP_INTERNAL_ERROR){
	            sc.disconnect();
	            return null;
            }
			
			BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			String inputLine;
			StringBuffer html = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine);
			}
			in.close();
			result = html.toString();
            
            System.out.println("||||||||||||||\n"+result+"\n||||||||||||||||||");
            
            if (!result.startsWith("84")){
            	System.out.println("Vui long truy cap qua 3G/GPRS");
            } else {
            	System.out.println("Detect thanh cong");
            }
            
            if (result.startsWith("84")) result = "0" + result.substring(2);
	        return result;
	        
		} catch (Exception e) {
			throw e;
			
		} finally {
			try {
	            if (sc != null) sc.disconnect();
	        } catch (Exception ioe) {}
		}
	}
}
