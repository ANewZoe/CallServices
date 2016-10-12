package com.jieshoufuwu.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.assist.User_Smessage;
import com.jieshoufuwu.fragment.S_Fragment;
import com.jieshoufuwu.fragment.U_Fragment;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	private User_Smessage user;// ����Աʵ��
	public static S_Fragment sf;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ������
		setContentView(R.layout.mainactivity);
		initview();

	}

	private void initview() {
		// TODO Auto-generated method stub

		user = (User_Smessage) getIntent().getExtras().getSerializable("user");
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.u_frame, new U_Fragment(this, user));
		ft.replace(R.id.s_frame, new S_Fragment(this, fm, user));
		ft.commit();

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == 4) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("��ȷ��Ҫ�˳���");
			dialog.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							MainActivity.this.finish();
							System.exit(0);
						}
					});
			dialog.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			dialog.show();
		}
		
		return false;

	}
}