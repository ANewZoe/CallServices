package com.jieshoufuwu.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.activity.MainActivity;
import com.jieshoufuwu.activity.SetpswdActivity;
import com.jieshoufuwu.assist.Assist;
import com.jieshoufuwu.assist.MyApplication;
import com.jieshoufuwu.assist.UpdateInfo;
import com.jieshoufuwu.assist.User_Smessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "NewApi", "InflateParams", "HandlerLeak" })
public class U_Fragment extends Fragment implements OnClickListener {
	private Context context;
	private ImageView user_image;
	private Button setting_btn;
	private User_Smessage user;

	private TextView s_evaluation;
	private TextView s_count;
	private PopupWindow pw;

	private String m_appNameStr;
	private Handler m_mainHandler;
	private ProgressDialog m_progressDlg;
	private static final int N=0;
	private static final int G=1;
	Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
				case N:
					notNewVersionDlgShow();
					break;
				case G:
					doNewVersionUpdate();
					break;
				default:
					break;
			}
		}
	};

	public U_Fragment(){}
	@SuppressLint("ValidFragment")
	public U_Fragment(Context context, User_Smessage user) {
		super();
		this.context = context;
		this.user = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View uView = LayoutInflater.from(context).inflate(R.layout.u_layout,null);
		((TextView) uView.findViewById(R.id.user_id)).setText("ID："+ user.getId());
		((TextView) uView.findViewById(R.id.user_name)).setText("昵称："+ user.getNickname());
		user_image = (ImageView) uView.findViewById(R.id.user_image);

		m_mainHandler=new Handler();
		m_progressDlg=new ProgressDialog(context);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		m_progressDlg.setIndeterminate(false);
		m_appNameStr=context.getString(R.string.app_name)+".apk";

		ImageLoader.getInstance().displayImage(user.getFace(), user_image,MyApplication.options);

		setting_btn = (Button) uView.findViewById(R.id.setting_btn);
		setting_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pw.isShowing()) {
					pw.dismiss();
				} else {
					pw.showAsDropDown(v, -(int) Assist.WIDTH / 20, 0);
				}
			}
		});
		createPopwindow();
		return uView;
	}

	public class CheckNewestVersionAsyncTask implements Runnable {

		InputStream iStream;
		String verName = Assist.getVerName(context);

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				String path = "";
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int responseCode = conn.getResponseCode();
				if (responseCode == 200) {
					iStream = conn.getInputStream();// 从服务器获得输入流
				}
				UpdateInfo info = UpdataInfoParser.getUpdataInfo(iStream);
				if (info.getVersion().equals(verName)) {
					handler.sendEmptyMessage(N);
				} else {
					handler.sendEmptyMessage(G);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		}
	}
	public static class UpdataInfoParser{
		public static UpdateInfo getUpdataInfo(InputStream is) throws Exception {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int type = parser.getEventType();
			UpdateInfo info = new UpdateInfo();
			while (type != XmlPullParser.END_DOCUMENT) {
				switch (type) {
					case XmlPullParser.START_TAG:
						if ("version".equals(parser.getName())) {
							info.setVersion(parser.nextText());
						} else if ("description".equals(parser.getName())) {
							info.setDescription(parser.nextText());
						}
						break;
				}
				type = parser.next();
			}
			return info;
		}
	}

	private void doNewVersionUpdate(){
		String verName=Assist.getVerName(context);
		final String updatesoft_address="http://"+Assist.serviceIP+"/eat_bendi/Public/Android/JieShouFuWu.apk";//软件更新包地址
		String str="当前版本:"+verName+",检测到最新版本，请及时更新！";
		Dialog dialog=new AlertDialog.Builder(context).setTitle("软件更新").setMessage(str)
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						m_progressDlg.setTitle("正在下载");
						m_progressDlg.setMessage("请稍后...");
						downFile(updatesoft_address);
					}
				})
				.setNegativeButton("稍后更新",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						dialog.dismiss();
					}
				}).create();

		dialog.show();
	}

	private void notNewVersionDlgShow(){
		String verName=Assist.getVerName(context);
		String str="当前版本:"+verName+",已是最新版本，无需更新！";
		Dialog dialog=new AlertDialog.Builder(context).setTitle("软件更新").setMessage(str)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();

		dialog.show();
	}

	private void downFile(final String url){
		m_progressDlg.show();
		new Thread(){
			public void run(){
				HttpClient httpClient=new DefaultHttpClient();
				HttpGet httpGet=new HttpGet(url);
				HttpResponse response;
				try {
					response=httpClient.execute(httpGet);
					HttpEntity entity=response.getEntity();
					long length=entity.getContentLength();
					m_progressDlg.setMax((int)length);//设置进度条的最大值,获取文件的大小
					InputStream is=entity.getContent();
					FileOutputStream fileOutputStream=null;
					if (null!=is) {
						File file=new File(Environment.getExternalStorageDirectory(),m_appNameStr);
						fileOutputStream=new FileOutputStream(file);
						byte[] buf=new byte[1024];
						int ch=-1;
						int count=0;
						while ((ch=is.read(buf))!=-1) {
							fileOutputStream.write(buf,0,ch);
							count+=ch;
							if (length>0) {
								m_progressDlg.setProgress(count);//设置当前进度,获取当前下载量
							}
						}
					}
					fileOutputStream.flush();//缓冲
					if (null!=fileOutputStream) {
						fileOutputStream.close();//关闭文件输出流
					}
					down();
				} catch (ClientProtocolException e) {
					// TODO: handle exception
					e.printStackTrace();
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void down(){
		m_mainHandler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				m_progressDlg.cancel();
				install();
			}
		});
	}

	private void install(){
		Intent intent =new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),m_appNameStr)), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.setpswd_btn:
				Intent intent = new Intent((MainActivity) context,SetpswdActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("s_user", user);
				intent.putExtras(bundle);
				startActivity(intent);
				if (pw.isShowing()) {
					pw.dismiss();
				}
				break;
			case R.id.update_btn:
				CheckNewestVersionAsyncTask cv=new CheckNewestVersionAsyncTask();
				new Thread(cv).start();
				if (pw.isShowing()) {
					pw.dismiss();
				}
				break;
			case R.id.exit_btn:
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("温馨提示");
				dialog.setMessage("您确定要退出程序吗？");
				dialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
							}
						});
				dialog.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								System.exit(0);
							}
						});
				dialog.show();
				break;
			default:
				break;
		}
	}



	private void createPopwindow() {
		// TODO Auto-generated method stub
		pw = new PopupWindow(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		View uview = LayoutInflater.from(context).inflate(R.layout.set_popwindow, null);
		pw.setContentView(uview);
		uview.findViewById(R.id.setpswd_btn).setOnClickListener(this);
		uview.findViewById(R.id.update_btn).setOnClickListener(this);
		uview.findViewById(R.id.exit_btn).setOnClickListener(this);

	}

}
