package com.checkinn.dashboard;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.checkinn.R;
import com.checkinn.objects.CafeteriaSchedule;

public class CafeteriaAdapter extends BaseAdapter {

	
	private ArrayList<CafeteriaSchedule> arrayListSchedule;
	private Context mContext;

	public CafeteriaAdapter(Context context ,ArrayList<CafeteriaSchedule> arr ) {
		mContext =context;
		arrayListSchedule= arr;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mContext, R.layout.list_item_schedule, null);
		TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
		TextView tvSubtitle1 = (TextView)view.findViewById(R.id.tvSubtitle1);
		tvTitle.setText(arrayListSchedule.get(position).cafe_name);
		NumberFormat f = new DecimalFormat("00");
		
		tvSubtitle1.setText("start time : "+arrayListSchedule.get(position).start_time +"  end time : "+arrayListSchedule.get(position).end_time);
		return view;
	}
	@Override
	public int getCount() {
		return arrayListSchedule.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

}
