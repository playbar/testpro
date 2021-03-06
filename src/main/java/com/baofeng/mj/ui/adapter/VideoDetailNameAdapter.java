package com.baofeng.mj.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.baofeng.mj.R;
import com.baofeng.mj.bean.VideoDetailBean;

import java.util.List;

/**
 * 视频详情页面中的选集，当选集小于等于8集时，显示title
 * Created by muyu on 2016/5/22.
 */
public class VideoDetailNameAdapter extends BaseAdapter {
	private List<VideoDetailBean.AlbumsBean.VideosBean> mjList;
	private Context context;
	private int selectedIndexSeq;

	public VideoDetailNameAdapter(Context context, List<VideoDetailBean.AlbumsBean.VideosBean>  mjList) {
		this.context = context;
		this.mjList = mjList;
	}

	public void setSelectedIndexSeq(int position) {
		this.selectedIndexSeq = position;
		notifyDataSetChanged();
	}



	@Override
	public int getCount() {
		if(mjList == null){
			return 0;
		}else if(mjList.size() > 10){
			return 10;
		}
		return mjList.size();
	}

	@Override
	public Object getItem(int position) {
		return mjList == null ? null : mjList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return (mjList == null || mjList.isEmpty()) ? 0 : mjList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		
		final MyGridViewItemViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			view = layoutInflater.inflate(R.layout.video_detail_name_item, null);
			viewHolder = new MyGridViewItemViewHolder(); 
			viewHolder.ItemText = (RadioButton) view.findViewById(R.id.detail_name_item);
			view.setTag(viewHolder);

		} else {
			view = convertView;
			viewHolder = (MyGridViewItemViewHolder) view.getTag();
		}

		VideoDetailBean.AlbumsBean.VideosBean videosBean = mjList.get(position);
		if(videosBean.getSeq()==(selectedIndexSeq)){
//		if(selectedIndex == position) {
			viewHolder.ItemText.setChecked(true);
			videosBean.setSeletedStatus(true);
		}else{
			viewHolder.ItemText.setChecked(false);
			videosBean.setSeletedStatus(false);
		}

		//update view holder
		updateViewHolder(viewHolder, (VideoDetailBean.AlbumsBean.VideosBean) getItem(position));
		return view;
	}

	private void updateViewHolder(final MyGridViewItemViewHolder viewHolder, VideoDetailBean.AlbumsBean.VideosBean data) {

		viewHolder.ItemText.setText(data.getTitle());
	}

	public void update(List<VideoDetailBean.AlbumsBean.VideosBean> data) {
		this.mjList = data;
		notifyDataSetChanged();
	}

	public void add(List<VideoDetailBean.AlbumsBean.VideosBean> data) {
		if (this.mjList == null) {
			this.mjList = data;
		} else {
			mjList.clear();
			this.mjList.addAll(data);
		}
		notifyDataSetChanged();
	}

	public void update() {
		notifyDataSetChanged();
	}

	public boolean hasData() {
		return mjList != null;
	}
	
	class MyGridViewItemViewHolder {
		private RadioButton ItemText;
	}

}
