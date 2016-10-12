package com.jieshoufuwu.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.assist.Assist;
import com.jieshoufuwu.assist.Call_Smessage;
import com.jieshoufuwu.assist.User_Smessage;
import com.jiwshouFuwu.http.PublicHttp;
import com.jiwshouFuwu.http.ServiceTypeHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class N_listAdapter extends BaseAdapter {
	private Context context;
	private List<Call_Smessage> service_List;
	private PublicHttp publicHttp;
	private User_Smessage user;

	public N_listAdapter(Context context, User_Smessage user) {
		super();
		this.context = context;
		this.user = user;
		this.service_List = new ArrayList<Call_Smessage>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return service_List.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return service_List.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		serviceView sV;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.s_fragment_nlistview_item, null);
			sV = new serviceView();
			sV.n_num = (TextView) convertView.findViewById(R.id.n_num);
			sV.n_type_btn = (ImageView) convertView.findViewById(R.id.n_type_btn);
			sV.n_time = (TextView) convertView.findViewById(R.id.n_time);

			convertView.setTag(sV);
		}

		sV = (serviceView) convertView.getTag();
		sV.n_num.setText(service_List.get(position).getTname());
		ImageLoader.getInstance().displayImage(service_List.get(position).getIcon_url(), sV.n_type_btn);
		final int[] time = Assist.getlTimt(Long.valueOf(service_List.get(position).getStime()) * 1000L,System.currentTimeMillis());
		sV.n_time.setText((time[0] * 24 + time[1]) + ":" + time[2]);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("服务详情");
				dialog.setMessage(time[2] + "分钟前，"+ service_List.get(position).getTname() + "号桌呼叫"
						+ Assist.sType.get(service_List.get(position).getSid())+ "服务，您确定要服务吗？");
				dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,int which) {
						// TODO Auto-generated method stub
					}
				});
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {
								// TODO Auto-generated method stub
								NService(position);
							}
						});
				dialog.show();
			}
		});
		return convertView;
	}

	public static class serviceView {
		private TextView n_num = null;
		private ImageView n_type_btn = null;
		private TextView n_time = null;
	}

	public void setService_List(List<Call_Smessage> service_List) {
		if (null != service_List) {
			this.service_List.clear();
			for (int i = 0; i < service_List.size(); i++) {
				this.service_List.add(service_List.get(i));
			}
			notifyDataSetChanged();// 调用此方法刷新
			ServiceTypeHttp.httpTime = 0;
		}

	}

	// 抢单,在抢单前要先获得后台的抢单接口
	@SuppressLint("HandlerLeak")
	private void NService(int posint) {
		publicHttp = new PublicHttp(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case PublicHttp.VISITSUCCESS:
						try {
							String string = new String(EntityUtils.toByteArray(publicHttp.getHttpEntity()));
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

		new Thread(publicHttp).start();
	}

}
