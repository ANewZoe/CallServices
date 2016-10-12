package com.jieshoufuwu.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabWidget;

import com.example.jieshoufuwu.R;
import com.jieshoufuwu.adapter.ViewPagerAdapter;
import com.jieshoufuwu.assist.User_Smessage;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint({ "InflateParams", "HandlerLeak", "NewApi" })
public class S_Fragment extends Fragment {
	private Context context;
	private ViewPager s_ViewPager;
	private TabWidget s_TabWidget;
	private FragmentManager fm;
	private User_Smessage user;// 服务员实例
	private List<Button> tv_list;
	private List<Drawable> tabbg_list;
	private List<Fragment> fragment_list;
	private final int TABSUM=3;

	public S_Fragment(){}
	@SuppressLint("ValidFragment")
	public S_Fragment(Context context, FragmentManager fm, User_Smessage user) {
		super();
		this.context = context;
		this.fm = fm;
		tv_list = new ArrayList<Button>();
		tabbg_list=new ArrayList<Drawable>();
		this.user=user;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View sView = LayoutInflater.from(context).inflate(R.layout.s_layout,null);
		s_TabWidget = (TabWidget) sView.findViewById(R.id.s_TabWidget);

		s_TabWidget.setStripEnabled(true);//设置下划线
		s_TabWidget.setShowDividers(0);//分割线
		s_TabWidget.setRightStripDrawable(getResources().getDrawable(R.drawable.sline_unselected));
		s_TabWidget.setLeftStripDrawable(getResources().getDrawable(R.drawable.sline_unselected));

		s_ViewPager = (ViewPager) sView.findViewById(R.id.s_ViewPager);
		tabbg_list.add(container.getResources().getDrawable(R.drawable.n_service));
		tabbg_list.add(container.getResources().getDrawable(R.drawable.d_service));
		tabbg_list.add(container.getResources().getDrawable(R.drawable.h_service));

		for (int i = 0; i < TABSUM; i++) {
			tv_list.add(new Button(context));
			tv_list.get(i).setBackgroundDrawable(tabbg_list.get(i));

			tv_list.get(i).setGravity(Gravity.CENTER);
			tv_list.get(i).setWidth(LayoutParams.WRAP_CONTENT);
			tv_list.get(i).setHeight(LayoutParams.WRAP_CONTENT);
			tv_list.get(i).setGravity(Gravity.CENTER);

			s_TabWidget.addView(tv_list.get(i));
			final int k = i;
			s_TabWidget.getChildAt(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					s_ViewPager.setCurrentItem(k);
				}
			});
		}

		fragment_list = new ArrayList<Fragment>();
		fragment_list.add(new N_pagerFragment(context,user));
		fragment_list.add(new D_pagerFragment(context,user));
		fragment_list.add(new H_pagerFragment(context));

		s_ViewPager.setAdapter(new ViewPagerAdapter(fragment_list, fm));
		s_ViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				s_TabWidget.setCurrentTab(arg0);

				if (arg0 == 1) {
					((D_pagerFragment) fragment_list.get(arg0)).upD_data();
				} else if (arg0 == 2) {
					((H_pagerFragment) fragment_list.get(arg0)).upH_data();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
		s_ViewPager.setCurrentItem(0);
		s_TabWidget.setCurrentTab(0);

		return sView;
	}

}
