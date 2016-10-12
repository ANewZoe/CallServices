package com.jieshoufuwu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.assist.Assist;
import com.jieshoufuwu.assist.Shortcut;
import com.jieshoufuwu.assist.User_Smessage;
import com.jiwshouFuwu.http.PublicHttp;

import org.json.JSONObject;

@SuppressLint("HandlerLeak")
public class EntryActivity extends Activity {
	private TextView cue_text;
	private Button seturl_btn;
	private AutoCompleteTextView login_account;
	private EditText login_password;
	private CheckBox login_checkbox_rememberMe;
	private ImageView login_usericon;
	private ImageView login_pswdicon;
	private PublicHttp ph;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.entryactivity);
		getSharedPreferences();
		initview();
		// 完成sp的初始化
		sp = getSharedPreferences("userInfo", MODE_PRIVATE);
		// 获取sp里面存储的数据
		String savedAccount = sp.getString("login_account", "");
		String savedPswd = sp.getString("login_password", "");
		login_account.setText(savedAccount);
		login_password.setText(savedPswd);
		// 创建快捷方式
		// 判断是否已经存在快捷方式
		if (!isExistShortcut()) {
			// 创建快捷方式
			Shortcut.createShortCut(EntryActivity.this, R.drawable.ic_launcher,R.string.app_name);
		}
	}

	private void initview() {
		cue_text = (TextView) findViewById(R.id.cue_text);// 提示信息
		login_account = (AutoCompleteTextView) findViewById(R.id.login_account);// 账号
		login_password = (EditText) findViewById(R.id.login_password);// 密码
		login_checkbox_rememberMe = (CheckBox) findViewById(R.id.login_checkbox_rememberMe);// 是都保存登录信息
		login_usericon = (ImageView) findViewById(R.id.login_usericon);
		login_pswdicon = (ImageView) findViewById(R.id.login_pswdicon);

		// 设置ip地址
		findViewById(R.id.seturl_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ipurlDialog();
			}
		});

		// TODO Auto-generated method stub
		findViewById(R.id.login_btn_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ph = new PublicHttp(new Handler() {
							@Override
							public void handleMessage(Message msg) {
								// TODO Auto-generated method stub
								switch (msg.what) {
									// 访问成功
									case PublicHttp.VISITSUCCESS:
										try {
											JSONObject jb = new JSONObject(ph.getHttpEntityString());
											if ("1".equals(jb.getString("state"))) {
												JSONObject jbject = jb.getJSONObject("data");
												User_Smessage user = new User_Smessage();
												user.setId(jbject.getString("id"));
												user.setNickname(jbject.getString("nickname"));
												user.setFace(jbject.getString("face"));
												Intent intent = new Intent(EntryActivity.this,MainActivity.class);
												Bundle bundle = new Bundle();
												bundle.putSerializable("user", user);
												intent.putExtras(bundle);
												startActivity(intent);
												EntryActivity.this.finish();
											} else {
												cue_text.setText(jb.getString("msg"));
											}
											// 检查用户是否勾选了记住密码选项
											if (login_checkbox_rememberMe.isChecked()) {
												// 说明勾选框被选中，把用户名和密码记录下来
												// 获取到一个参数文件的编辑器
												Editor editor = sp.edit();
												editor.putString("login_account",login_account.getText().toString().trim());
												editor.putString("login_password",login_password.getText().toString().trim());
												// 把数据保存到sp里面
												editor.commit();
											}
										} catch (Exception e) {
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}
										break;
									// 访问失败
									case PublicHttp.VISITDEFEATED:
										cue_text.setText("服务器连接失败，请检查网络后重试！");
										break;
									default:
										break;
								}
							}

						},
								"");
						new Thread(ph).start();
					}
				});
	}

	// 设置IP地址
	protected void ipurlDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder ipurldialog = new AlertDialog.Builder(this);
		final EditText urledit = new EditText(this);
		final SharedPreferences sp = this.getSharedPreferences("csip",
				Activity.MODE_PRIVATE);
		final SharedPreferences.Editor spe = sp.edit();
		urledit.setHint("当前ip地址：" + sp.getString("ip", Assist.serviceIP));
		ipurldialog.setView(urledit);
		ipurldialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		ipurldialog.setPositiveButton("保存",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						spe.putString("ip", urledit.getText().toString().trim());
						spe.commit();
						Assist.serviceIP = sp.getString("ip", null);
					}
				});
		ipurldialog.show();
	}

	// 判断是否已经存在快捷方式
	public boolean isExistShortcut() {
		boolean isInstallShortcut = false;
		final ContentResolver cr = EntryActivity.this.getContentResolver();
		final String AUTHORITY = "com.android.launcher2.settings";
		final Uri content_uri = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor cursor = cr.query(content_uri, new String[] { "title",
						"iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		if (null != cursor && cursor.getCount() > 0) {
			isInstallShortcut = true;
		}
		return isInstallShortcut;
	}

	// 将ip地址以xml格式保存到本地
	public void getSharedPreferences() {
		SharedPreferences sharedPreferences = getSharedPreferences("csip",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (null == sharedPreferences.getString("ip", null)) {
			editor.putString("ip", "");//参数二为服务器IP地址
			editor.commit();
		}
		Assist.serviceIP = sharedPreferences.getString("ip", null);
	}

}