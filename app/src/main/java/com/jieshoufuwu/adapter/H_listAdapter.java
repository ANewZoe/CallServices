package com.jieshoufuwu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.assist.Assist;
import com.jieshoufuwu.assist.Call_Smessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("InflateParams")
public class H_listAdapter extends BaseAdapter{
	private Context context;
	private List<Call_Smessage> service_List;
	public H_listAdapter(Context context) {
		super();
		this.context = context;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		serviceView sV;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.s_fragment_hlistview_item, null);
			sV = new serviceView();
			sV.h_num = (TextView) convertView.findViewById(R.id.h_num);
			sV.h_type_btn = (ImageView) convertView.findViewById(R.id.h_type_btn);
			sV.h_time = (TextView) convertView.findViewById(R.id.h_time);
			convertView.setTag(sV);
		}

		sV = (serviceView) convertView.getTag();
		sV.h_num.setText(service_List.get(position).getTname());
		ImageLoader.getInstance().displayImage(service_List.get(position).getIcon_url(), sV.h_type_btn);
		if (!"null".equals(service_List.get(position).getGrab_time())&&!"null".equals(service_List.get(position).getOver_time())) {
			int[] grag_time=Assist.getlTimt(Long.valueOf(service_List.get(position).getGrab_time())*1000L, Long.valueOf(service_List.get(position).getOver_time())*1000L);
			sV.h_time.setText(grag_time[0]*24+grag_time[1]+":"+grag_time[2]+":"+grag_time[3]);
		}else {
			sV.h_time.setText("00:00:00");
		}
		return convertView;
	}

	public static class serviceView {
		private TextView h_num = null;
		private ImageView h_type_btn = null;
		private TextView h_time = null;
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


}
