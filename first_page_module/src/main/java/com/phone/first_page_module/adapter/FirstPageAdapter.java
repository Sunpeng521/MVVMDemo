package com.phone.first_page_module.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phone.common_library.callback.OnItemViewClickListener;
import com.phone.first_page_module.R;
import com.phone.common_library.bean.FirstPageResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirstPageAdapter
	extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	private static final String                                          TAG               = "FirstPageAdapter";
	private              Context                                         mContext;
	private              List<FirstPageResponse.ResultData.JuheNewsBean> mJuheNewsBeanList = new ArrayList<>();
	
	public FirstPageAdapter(Context context) {
		mContext = context;
	}
	
	public void clearData() {
		this.mJuheNewsBeanList.clear();
		notifyDataSetChanged();
	}
	
	public void addAllData(List<FirstPageResponse.ResultData.JuheNewsBean> dataBeanList) {
		this.mJuheNewsBeanList.addAll(dataBeanList);
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_first_page, parent, false);
		return new BodyHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof BodyHolder) {
			BodyHolder bodyHolder = (BodyHolder) holder;
			FirstPageResponse.ResultData.JuheNewsBean juheNewsBean = mJuheNewsBeanList.get(position);
			String title = juheNewsBean.getTitle();
			String author = juheNewsBean.getAuthor_name();
			String time = juheNewsBean.getDate();
			String imgSrc = juheNewsBean.getThumbnail_pic_s();
			String imgMid = juheNewsBean.getThumbnail_pic_s02();
			String imgRight = juheNewsBean.getThumbnail_pic_s03();
			
			bodyHolder.newsSummaryTitleTv.setText(title);
			bodyHolder.newsSummaryAuthor.setText(author);
			bodyHolder.newsSummaryTime.setText(time);
			Glide.with(mContext).load(imgSrc).into(bodyHolder.newsSummaryPhotoIvLeft);
			Glide.with(mContext).load(imgMid).into(bodyHolder.newsSummaryPhotoIvMiddle);
			Glide.with(mContext).load(imgRight).into(bodyHolder.newsSummaryPhotoIvRight);
			
			bodyHolder.llRoot.setOnClickListener(view -> {
				onItemViewClickListener.onItemClickListener(position, view);
			});
		}
	}
	
	@Override
	public int getItemCount() {
		return mJuheNewsBeanList.size();
	}
	
	private static class BodyHolder extends RecyclerView.ViewHolder {
		
		private LinearLayout llRoot;
		private TextView     newsSummaryTitleTv;
		private LinearLayout newsSummaryPhotoIvGroup;
		private ImageView    newsSummaryPhotoIvLeft;
		private ImageView    newsSummaryPhotoIvMiddle;
		private ImageView    newsSummaryPhotoIvRight;
		private TextView     newsSummaryAuthor;
		private TextView     newsSummaryTime;
		
		public BodyHolder(@NonNull View itemView) {
			super(itemView);
			
			llRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);
			newsSummaryTitleTv = (TextView) itemView.findViewById(R.id.news_summary_title_tv);
			newsSummaryPhotoIvGroup = (LinearLayout) itemView.findViewById(R.id.news_summary_photo_iv_group);
			newsSummaryPhotoIvLeft = (ImageView) itemView.findViewById(R.id.news_summary_photo_iv_left);
			newsSummaryPhotoIvMiddle = (ImageView) itemView.findViewById(R.id.news_summary_photo_iv_middle);
			newsSummaryPhotoIvRight = (ImageView) itemView.findViewById(R.id.news_summary_photo_iv_right);
			newsSummaryAuthor = (TextView) itemView.findViewById(R.id.news_summary_author);
			newsSummaryTime = (TextView) itemView.findViewById(R.id.news_summary_time);
		}
	}
	
	private OnItemViewClickListener onItemViewClickListener;
	
	public void setRcvOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener) {
		this.onItemViewClickListener = onItemViewClickListener;
	}
	
}
