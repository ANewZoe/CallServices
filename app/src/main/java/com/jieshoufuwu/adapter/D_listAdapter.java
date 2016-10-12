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
import com.jieshoufuwu.fragment.D_pagerFragment;
import com.jiwshouFuwu.http.PublicHttp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@SuppressLint({ "InflateParams", "HandlerLeak" })
public class D_listAdapter extends BaseAdapter {
	private Context context;
	private List<Call_Smessage> service_List;
	private PublicHttp publicHttp;
	private User_Smessage user;
	private D_pagerFragment d_pagerFragment;

	//计时
	Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch (msg.what) {
				case 0:
					notifyDataSetChanged();
					break;

				default:
					break;
			}
		}
	};

	public D_listAdapter(Context context, User_Smessage user,D_pagerFragment d_pagerFragment) {
		super();
		this.context = context;
		this.user = user;
		this.service_List = new ArrayList<Call_Smessage>();
		this.d_pagerFragment = d_pagerFragment;
		//计时，每1s刷新一次
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(1000);
						handler.sendEmptyMessage(0);//刷新
					} catch (InterruptedException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}
		}).start();

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
			convertView = LayoutInflater.from(context).inflate(R.layout.s_fragment_dlistview_item, null);
			sV = new serviceView();
			sV.d_num = (TextView) convertView.findViewById(R.id.d_num);
			sV.d_type_btn = (ImageView) convertView.findViewById(R.id.d_type_btn);
			sV.d_time = (TextView) convertView.findViewById(R.id.d_time);

			convertView.setTag(sV);
		}
		sV = (serviceView) convertView.getTag();
		sV.d_num.setText(service_List.get(position).getTname());
		ImageLoader.getInstance().displayImage(service_List.get(position).getIcon_url(), sV.d_type_btn);
		int[] time = Assist.getlTimt(Long.valueOf(service_List.get(position).getGrab_time())*1000L,System.currentTimeMillis());
		sV.d_time.setText(time[1]+":"+time[2] + ":" + time[3]);


		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(context);
				dialog.setTitle("温馨提示");
				dialog.setMessage("您正在为"+service_List.get(position).getTname()+"号桌服务，服务完成请点确定！");
				dialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
							}
						});
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								DService(v, position);
							}
						});

				dialog.show();
			}

		});

		return convertView;


	}

	public static class serviceView {
		private TextView d_num = null;
		private ImageView d_type_btn = null;
		private TextView d_time = null;
	}

	public void setService_List(List<Call_Smessage> service_List) {
		if (null != service_List) {
			this.service_List.clear();
			for (int i = 0; i < service_List.size(); i++) {
				this.service_List.add(service_List.get(i));
			}
			notifyDataSetChanged();

		}

	}

	// 待服务
	@SuppressLint("HandlerLeak")
	private void DService(final View v, int posint) {

		publicHttp = new PublicHttp(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case PublicHttp.VISITSUCCESS:
						try {
							String string = new String(EntityUtils.toByteArray(publicHttp.getHttpEntity()));
							JSONObject job = new JSONObject(string);
							if ("1".equals(job.getString("state"))) {
								Toast.makeText(context, "服务完成", Toast.LENGTH_SHORT).show();
							} else if ("2".equals(job.getString("state"))) {
								Toast.makeText(context, "服务完成失败！",Toast.LENGTH_SHORT).show();
							}
							d_pagerFragment.upD_data();
						} catch (Exception e) {
							// TODO: handle exception
							d_pagerFragment.upD_data();
						}
						break;
					case PublicHttp.VISITDEFEATED:
						Toast.makeText(context, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
			}
		}, "");
		new Thread(publicHttp).start();
	}

}
