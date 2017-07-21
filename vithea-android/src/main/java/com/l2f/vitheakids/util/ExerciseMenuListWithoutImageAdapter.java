package com.l2f.vitheakids.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.l2f.vitheakids.R;

public class ExerciseMenuListWithoutImageAdapter extends BaseAdapter {

	private ArrayList<String> list = new ArrayList<String>();
	private static LayoutInflater inflater;
	private Context mContext;

	public ExerciseMenuListWithoutImageAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public ExerciseMenuListWithoutImageAdapter(Context context, List<String> values) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		list = (ArrayList<String>) values;
	}
	
	//
	// View holder
	// 
	static class ViewHolder {
		TextView exerciseTypeName;
	}
	
	
	public void add(String listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<String> getList() {
		return list;
	}
	
	public void removeAllViews() {
		list.clear();
		this.notifyDataSetChanged();
	}

	//
	// Methods from Base Adapter
	// 
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		String curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			
			newView = inflater.inflate(R.layout.list_item_without_image, parent, false);
			holder.exerciseTypeName = (TextView) newView.findViewById(R.id.list_item_title);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}
		
		holder.exerciseTypeName.setText(curr);
		
		return newView;
	}
}
