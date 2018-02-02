package com.l2f.vitheakids.util;

import java.util.ArrayList;
import java.util.List;

import com.l2f.vitheakids.R;
import com.l2f.vitheakids.model.ExerciseTypeRecord;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseMenuListAdapter extends BaseAdapter {
	
	private ArrayList<ExerciseTypeRecord> list = new ArrayList<ExerciseTypeRecord>();
	private static LayoutInflater inflater;
	private Context mContext;
	
	public ExerciseMenuListAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public ExerciseMenuListAdapter(Context context, List<ExerciseTypeRecord> values) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		list = (ArrayList<ExerciseTypeRecord>) values;
	}
	
	//
	// View holder
	// 
	static class ViewHolder {

		ImageView exerciseTypeImage;
		TextView exerciseTypeName;

	}
	
	public void add(ExerciseTypeRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}

	public ArrayList<ExerciseTypeRecord> getList() {
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

		ExerciseTypeRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			
			newView = inflater.inflate(R.layout.list_item, parent, false);
			holder.exerciseTypeImage = (ImageView) newView.findViewById(R.id.list_item_image);
			holder.exerciseTypeName = (TextView) newView.findViewById(R.id.list_item_title);
			newView.setTag(holder);

		} else {
			holder = (ViewHolder) newView.getTag();
		}
		
		holder.exerciseTypeImage.setImageResource(curr.getExerciseTypeImage());
		holder.exerciseTypeName.setText(curr.getExerciseTypeName());
		
		return newView;
		
		
	}
}
