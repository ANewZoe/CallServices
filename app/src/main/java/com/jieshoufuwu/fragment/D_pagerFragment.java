package com.jieshoufuwu.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.adapter.D_listAdapter;
import com.jieshoufuwu.assist.Call_Smessage;
import com.jieshoufuwu.assist.User_Smessage;
import com.jiwshouFuwu.http.PublicHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@SuppressLint({ "HandlerLeak", "InflateParams" })
public class D_pagerFragment extends Fragment {
	private Context context;
	private PublicHttp stp;
	private D_listAdapter d_listAdapter;
	public List<Call_Smessage> service_List;
	private User_Smessage user;

	public D_pagerFragment(){}
	@SuppressLint("ValidFragment")
	public D_pagerFragment(Context context, User_Smessage user) {
		super();
		this.context = context;
		this.user = user;
		new ArrayList<Call_Smessage>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View dView = LayoutInflater.from(context).inflate(R.layout.s_fragment_listview, null);
		GridView s_listview = (GridView) dView.findViewById(R.id.s_listview);
		d_listAdapter = new D_listAdapter(context, user, this);
		s_listview.setAdapter(d_listAdapter);
		service_List = new ArrayList<Call_Smessage>();
		return dView;
	}


	public void upD_data() {
		stp = new PublicHttp(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case PublicHttp.VISITSUCCESS:
						try {
							JSONObject jn = new JSONObject(stp.getHttpEntityString());
							service_List.clear();// 清除数据
							if ("1".equals(jn.getString("state"))) {
								JSONArray jna = jn.getJSONArray("data");
								for (int i = 0; i < jna.length(); i++) {
									Call_Smessage st = new Call_Smessage();
									JSONObject jsnob = jna.getJSONObject(i);
									st.setId(jsnob.getString("id"));// 服务列表id
									st.setSid(jsnob.getString("service_id"));// 服务类型
									st.setSta(jsnob.getString("sta"));
									st.setStime(jsnob.getString("service_time"));// 呼叫服务时间
									st.setTid(jsnob.getString("tid"));// 桌台号
									st.setTname(jsnob.getString("tname"));//桌台名
									st.setIcon_url(jsnob.getString("icon_url"));//服务图片
									st.setUid(jsnob.getString("uid"));// 服务员id
									st.setGrab_time(jsnob.getString("grap_time"));//接单时间
									service_List.add(st);

								}
							}

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							// D_ServiceHttp.httpTime = 0;
						}
						d_listAdapter.setService_List(service_List);//刷新
						break;
					case PublicHttp.VISITDEFEATED:
						break;

					default:
						break;
				}
			}
		}, "");
		new Thread(stp).start();
	}
}
