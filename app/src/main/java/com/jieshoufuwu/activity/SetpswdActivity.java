package com.jieshoufuwu.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.assist.MyApplication;
import com.jieshoufuwu.assist.User_Smessage;
import com.jiwshouFuwu.http.PublicHttp;

import org.json.JSONObject;

/**
 * 修改密码部分
 * @author root
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "NewApi", "HandlerLeak" })
public class SetpswdActivity extends Activity implements OnClickListener{

	private Button back_btn;//返回按钮
	private Button acknow_btn;//确认按钮
	private EditText former_pswd;//旧密码
	private EditText new_pswd;//新密码
	private EditText new_pswdqr;//确认新密码
	private PublicHttp ph;
	private User_Smessage user;



	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.reset_pswd);

		former_pswd=(EditText) findViewById(R.id.former_pswd);
		new_pswd=(EditText) findViewById(R.id.new_pswd);
		new_pswdqr=(EditText) findViewById(R.id.new_pswdqr);
		back_btn=(Button) findViewById(R.id.back_btn);
		acknow_btn=(Button) findViewById(R.id.acknow_btn);

		//获取从EntryActivity中使用intent传过来的值
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		user = (User_Smessage) bundle.getSerializable("s_user");

		//返回按钮
		back_btn.setOnClickListener(this);

		//提交按钮
		acknow_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v){
		switch (v.getId()) {
			//返回
			case R.id.back_btn:
				finish();
				break;
			//确认
			case R.id.acknow_btn:
				ph=new PublicHttp(new Handler(){

					public void handleMessage(Message msg){

						switch (msg.what) {
							//访问成功
							case PublicHttp.VISITSUCCESS:
								try {
									JSONObject jb=new JSONObject(ph.getHttpEntityString());
									//修改密码
									if ("1".equals(jb.getString("state"))) {

										Toast.makeText(MyApplication.getAppContext(), "修改成功！", Toast.LENGTH_SHORT).show();
										finish();

									}else if ("2".equals(jb.getString("state"))) {
										Toast.makeText(MyApplication.getAppContext(), "旧密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
									}else {
										Toast.makeText(MyApplication.getAppContext(), "参数为空，请输入密码！", Toast.LENGTH_SHORT).show();
									}

								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								break;
							//访问失败
							case PublicHttp.VISITDEFEATED:
								Toast.makeText(MyApplication.getAppContext(), "网络访问失败，请稍后重试！", Toast.LENGTH_SHORT).show();
								break;

							default:
								break;
						}
					}
				}, "");
				new Thread(ph).start();

				break;

			default:
				break;
		}
	}


}
