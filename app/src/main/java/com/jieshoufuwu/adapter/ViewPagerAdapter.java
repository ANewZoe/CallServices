package com.jieshoufuwu.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	private List<Fragment> fragment_list;

	public ViewPagerAdapter(List<Fragment> fragment_list, FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragment_list = fragment_list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragment_list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragment_list.size();
	}

}
