package com.checkinn.admin;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.checkinn.R;

public class SpinnerAdapter<T> extends ArrayAdapter<T> {


	public SpinnerAdapter(Context context, int resource, List<T> objects) {
		super(context, resource, objects);
		mContext = context;
	}


	private Context mContext;


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mContext, R.layout.list_item_spinner, null);
		
		TextView tv =(TextView)view.findViewById(R.id.tv);
		tv.setText(getItem(position).toString());
		return view;
	}
}
