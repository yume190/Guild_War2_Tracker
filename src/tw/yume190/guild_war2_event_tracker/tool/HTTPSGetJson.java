package tw.yume190.guild_war2_event_tracker.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;

public class HTTPSGetJson {
	
	private String url;
	//private ProgressDialog mProgressDialog;
	
	public HTTPSGetJson(String u){
		url = u;
		//this.mProgressDialog = null;
	}
	
	public HTTPSGetJson(String u,ProgressDialog mProgressDialog){
		url = u;
		//this.mProgressDialog = mProgressDialog;
	}
	
//	public void ProcessResponse(String resp) throws IllegalStateException, 
//	    IOException, JSONException, NoSuchAlgorithmException {
//	
//		JSONObject responseObject = new JSONObject(resp);
//		/* Do your process here */
//	}
	
	public JSONObject ProcessResponse() throws IllegalStateException, 
	    IOException, JSONException, NoSuchAlgorithmException {
	
		JSONObject responseObject = new JSONObject(SearchRequest(""));
		/* Do your process here */
		return responseObject;
	}
	
	public JSONObject ProcessResponseObject() throws IllegalStateException, 
	    IOException, JSONException, NoSuchAlgorithmException {
	
		JSONObject responseObject = new JSONObject(SearchRequest(""));
		/* Do your process here */
		return responseObject;
	}
	
	public JSONArray ProcessResponseArray() throws IllegalStateException, 
	    IOException, JSONException, NoSuchAlgorithmException {
	
		JSONArray responseArray = new JSONArray(SearchRequest(""));
		/* Do your process here */
		return responseArray;
	}

		
	public String SearchRequest(String searchString)throws MalformedURLException, IOException {
		
		String newFeed = url + URLEncoder.encode(searchString, "UTF-8");
		StringBuilder response = new StringBuilder();
		
		URL url = new URL(newFeed);
		trustEveryone();
		
		HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
		if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		    //BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
			BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 1024);
//			int fileSize = httpconn.getContentLength();
		    String strLine = null;
//		    long total = 0;
		    while ((strLine = input.readLine()) != null) {
		        response.append(strLine);
//		        total += strLine.getBytes().length;
//		    	if(mProgressDialog != null && fileSize > 0)
//		    		mProgressDialog.setProgress((int)(total * 100 / fileSize));   
		    }
		    input.close();
		}
		return response.toString();
	}   
	
	
	/**
	* http://stackoverflow.com/questions/1217141/self-signed-ssl-acceptance-android
	*/
	private void trustEveryone() {
		try {
		    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
		    	public boolean verify(String hostname, SSLSession session) {
		    		return true;
		    }});
		    SSLContext context = SSLContext.getInstance("TLS");
		    context.init(null, new X509TrustManager[]{new X509TrustManager(){
	            public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
	            public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {}
	            public X509Certificate[] getAcceptedIssuers() {
	                    return new X509Certificate[0];
	            }
		    }}, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		    
		} catch (Exception e) { // should never happen
		        e.printStackTrace();
		}
	}

}
