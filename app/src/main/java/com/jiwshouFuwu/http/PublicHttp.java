package com.jiwshouFuwu.http;

import android.os.Handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class PublicHttp implements Runnable {
	public static final int VISITSUCCESS = 0;
	public static final int VISITDEFEATED = 1;
	private String s; 
	private String uri;
	private Handler handle;
	private HttpEntity entity;

	public PublicHttp(Handler handle, String uri) {
		super();
		this.uri = uri;
		this.handle = handle;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);
		try {
			
			HttpResponse request = client.execute(httpget);
			if (request.getStatusLine().getStatusCode() == 200) {
				entity = request.getEntity();
				s= new String(EntityUtils.toByteArray( entity));
				handle.sendEmptyMessage(VISITSUCCESS);
			} else {
				handle.sendEmptyMessage(VISITDEFEATED);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			handle.sendEmptyMessage(VISITDEFEATED);
			e.printStackTrace();
		} finally {
			if (null != client) {
				client.getConnectionManager().shutdown();
			}
		}
	}

	public HttpEntity getHttpEntity() {

		return entity;
	}
	public String getHttpEntityString(){
		return s;
	}
}
