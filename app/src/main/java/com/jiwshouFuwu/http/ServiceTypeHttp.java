package com.jiwshouFuwu.http;

import android.os.Handler;

import com.jieshoufuwu.assist.Call_Smessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ServiceTypeHttp implements Runnable {

	private HttpEntity httpentity = null;
	public static final int VISITSUCCESS = 0;
	public static final int VISITDEFEATED = 1;
	private HttpGet httpGet = null;
	private HttpResponse response = null;
	private String string;
	private Handler handler;
	public static int httpTime = 0;
	private Call_Smessage cals;

	public ServiceTypeHttp(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (ServiceTypeHttp.httpTime == 0) {
				ServiceTypeHttp.httpTime = 1;
				HttpClient uphttpClient = new DefaultHttpClient();
				httpGet = new HttpGet("");
				response = null;
				try {
					response = uphttpClient.execute(httpGet);
					if (response.getStatusLine().getStatusCode() == 200) {
						httpentity = response.getEntity();
						string=new String(EntityUtils.toByteArray(httpentity));
						handler.sendEmptyMessage(VISITSUCCESS);
					} else {
						handler.sendEmptyMessage(VISITDEFEATED);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					handler.sendEmptyMessage(VISITDEFEATED);
				} finally {
					if (uphttpClient != null) {
						uphttpClient.getConnectionManager().shutdown();
					}
				}
			}
			try {
				Thread.sleep(2000);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public HttpEntity gethttpentity() {
		return this.httpentity;
	}

	public String getHttpEntiyString() {
		return string;
	}
}
