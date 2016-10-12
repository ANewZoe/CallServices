package com.jieshoufuwu.fragment;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jieshoufuwu.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.jieshoufuwu.adapter.N_listAdapter;
import com.jieshoufuwu.assist.Assist;
import com.jieshoufuwu.assist.Call_Smessage;
import com.jieshoufuwu.assist.MsynListener;
import com.jieshoufuwu.assist.User_Smessage;
import com.jiwshouFuwu.http.PublicHttp;
import com.jiwshouFuwu.http.ServiceTypeHttp;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@SuppressLint({ "HandlerLeak", "InflateParams", "Wakelock" })
public class N_pagerFragment extends Fragment {
	private Context context;
	private ServiceTypeHttp stp;
	private PublicHttp publi;
	private N_listAdapter n_listadapter;
	private MsynListener mSynListener;
	private SpeechSynthesizer mTts;
	private User_Smessage user;
	public static String table_id, on_call, service_id;
	public static long call_cout = 0;
	private WakeLock mWakeLock;
	private List<Call_Smessage> service_list;
	Vibrator vib;


	public N_pagerFragment(){}
	@SuppressLint("ValidFragment")
	public N_pagerFragment(Context context, User_Smessage user) {
		super();
		this.context = context;
		this.service_list = new ArrayList<Call_Smessage>();
		this.user = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View dView = LayoutInflater.from(context).inflate(R.layout.s_fragment_listview, null);
		GridView s_listview = (GridView) dView.findViewById(R.id.s_listview);
		n_listadapter = new N_listAdapter(context, user);
		s_listview.setAdapter(n_listadapter);
		mSynListener = new MsynListener();
		SpeechUtility.createUtility(context,SpeechConstant.APPID + "=572b20f8");
		mTts = SpeechSynthesizer.createSynthesizer(context, null);
		mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
		mTts.setParameter(SpeechConstant.SPEED, "20");
		mTts.setParameter(SpeechConstant.VOLUME, "80");
		vib = ((Vibrator) (context).getSystemService(Service.VIBRATOR_SERVICE));

		stp = new ServiceTypeHttp(new Handler() {
			@SuppressWarnings({})
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				switch (msg.what) {
					// 访问成功
					case ServiceTypeHttp.VISITSUCCESS:
						List<Call_Smessage> service_List = new ArrayList<Call_Smessage>();
						try {
							JSONObject jn = new JSONObject(stp.getHttpEntiyString());
							service_List.clear();// 清除数据
							if (!"0".equals(jn.getString("state"))) {
								JSONArray jna = jn.getJSONArray("data");
								for (int i = 0; i < jna.length(); i++) {
									JSONObject jOb = jna.getJSONObject(i);
									Call_Smessage st = new Call_Smessage();
									st.setId(jOb.getString("id"));// id 服务列表ID
									st.setSid(jOb.getString("service_id"));// 服务类型
									st.setTname(jOb.getString("tname"));
									st.setUid(jOb.getString("uid"));
									st.setStime(jOb.getString("service_time"));// 呼叫服务时间
									st.setIcon_url(jOb.getString("icon_url"));// 图片
									st.setTid(jOb.getString("tid"));// 桌台号
									st.setGrab_time(jOb.getString("grap_time"));//接单时间
									service_List.add(st);
								}
							}

							if (service_List.size() > 0 ) {
								// 解锁并唤醒屏幕,激活屏幕
								// 点亮屏幕
								mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP| PowerManager.SCREEN_DIM_WAKE_LOCK,"My Tag");
								mWakeLock.acquire();
								KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
								KeyguardLock kl = km.newKeyguardLock(Context.KEYGUARD_SERVICE);
								kl.disableKeyguard();
								if (MsynListener.music == 0) {
									//语音合成
									mTts.startSpeaking(service_List.get(0).getTname()+ "号桌,"+"呼叫"+ Assist.sType.get(service_List.get(0).getSid())+ "服务", mSynListener);
									vib.vibrate(1000);// 设置震动的时间
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ServiceTypeHttp.httpTime = 0;
						}
						n_listadapter.setService_List(service_List);//刷新
						break;
					case ServiceTypeHttp.VISITDEFEATED:
						ServiceTypeHttp.httpTime = 0;
						break;
					default:
						break;
				}
			}

		});
		new Thread(stp).start();

		return dView;

	}

	@SuppressWarnings("unused")
	private void NItemService(int posint) {
		publi = new PublicHttp(new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
					case PublicHttp.VISITSUCCESS:
						try {

							String string = new String(EntityUtils.toByteArray(publi.getHttpEntity()));
							JSONObject jb = new JSONObject(string);
							if ("1".equals(jb.getString("state"))) {// 抢单成功
								Toast.makeText(context, "抢单成功", Toast.LENGTH_SHORT).show();

							} else {// 抢单失败
								Toast.makeText(context, "抢单失败", Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case PublicHttp.VISITDEFEATED:
						Toast.makeText(context, "获取网络失败，请稍后重试！", Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}
			}
		}, "");

		new Thread(publi).start();
	}

}
