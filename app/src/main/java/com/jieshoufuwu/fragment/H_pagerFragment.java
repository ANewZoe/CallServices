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
import com.jieshoufuwu.adapter.H_listAdapter;
import com.jieshoufuwu.assist.Call_Smessage;
import com.jiwshouFuwu.http.PublicHttp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@SuppressLint({ "InflateParams", "HandlerLeak" })
public class H_pagerFragment extends Fragment {
	private Context context;
	private  PublicHttp hstp;
	private  H_listAdapter h_listadapter;
	public  List<Call_Smessage> service_List;

	public H_pagerFragment(){}
	@SuppressLint("ValidFragment")
	public H_pagerFragment(Context context) {
		this.context = context;
		service_List = new ArrayList<Call_Smessage>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View dView = LayoutInflater.from(context).inflate(R.layout.s_fragment_listview, null);
		GridView s_listview = (GridView) dView.findViewById(R.id.s_listview);
		h_listadapter = new H_listAdapter(context);
		s_listview.setAdapter(h_listadapter);
		upH_data();
		return dView;
	}

	public void upH_data() {
		hstp = new PublicHttp(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
					case PublicHttp.VISITSUCCESS:
						try {

							JSONObject jn = new JSONObject(hstp.getHttpEntityString());
							service_List.clear();// 清除数据
							if ("1".equals(jn.getString("state"))) {
								JSONArray jna = jn.getJSONArray("data");
								for (int i = 0; i < jna.length(); i++) {
									JSONObject jOb = jna.getJSONObject(i);
									Call_Smessage st = new Call_Smessage();
									st.setId(jOb.getString("id"));// id
									st.setSid(jOb.getString("service_id"));// 服务类型
									st.setUid(jOb.getString("uid"));
									st.setStime(jOb.getString("service_time"));// 呼叫服务时间
									st.setTid(jOb.getString("tid"));// 桌台号
									st.setIcon_url(jOb.getString("icon_url"));//图片
									st.setTname(jOb.getString("tname"));//桌台名
									st.setGrab_time(jOb.getString("grap_time"));//接单时间
									st.setOver_time(jOb.getString("over_time"));//完成时间
									service_List.add(st);

								}
							}
							h_listadapter.setService_List(service_List);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();

						}
						break;
					case PublicHttp.VISITDEFEATED:

						break;

					default:
						break;
				}

			}
		}, "");

		new Thread(hstp).start();
	}
}
