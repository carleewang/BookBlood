package com.base.base.adpter;


import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.application.BaseApplication;
import com.base.base.BaseActivity;
import com.base.http.R;
import com.base.util.system.ScreenTool;
import com.base.util.utility.StringUtil;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
/**
 * Created by Zhu TingYu on 2018/1/11.
 */

public abstract class BaseQuickAdapter<T, K extends BaseViewHolder> extends com.chad.library.adapter.base.BaseQuickAdapter<T, K> {

    String emptyText;

    public BaseQuickAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public BaseQuickAdapter(List<T> data) {
        super(data);
    }

    public int mCurrentOperatePosition = -1;

    public void notifyOperatePosition(){
        if(mCurrentOperatePosition != -1){
            notifyItemChanged(mCurrentOperatePosition);
        }
    }

    @Override
    public void setNewData(List<T> data) {

        if (data == null || data.isEmpty()) {
            if (!getEmptyViewText().isEmpty()) {
                this.setEmptyView();
            }
        }else {
            if(getEmptyView() != null){
                getEmptyView().setVisibility(View.GONE);
            }
        }
        super.setNewData(data);
    }

    public void setEmptyView() {
        if (!getData().isEmpty()) {
            return;
        }
        if (getEmptyViewImage() == 0) {
            setSelfEmptyView(getEmptyViewText());
        } else {
            setSelfEmptyView(StringUtil.isStringValid(getEmptyViewText()) ?
                    getEmptyViewText() : emptyText, getEmptyViewImage());
        }
    }

    public void setEmptyView(String emptyMsg) {
        this.emptyText = emptyMsg;
        if (!getData().isEmpty()) {
            return;
        }
        if (getEmptyViewImage() == 0) {
            setSelfEmptyView(emptyMsg);
        } else {
            setSelfEmptyView(StringUtil.isStringValid(getEmptyViewText()) ?
                    getEmptyViewText() : emptyText, getEmptyViewImage());
        }
    }


    public void setLoadMore(boolean isEnd) {
        if (isEnd) {
            this.loadMoreEnd();
        } else this.loadMoreComplete();
    }

    public void setSelfEmptyView(String message, @DrawableRes int resId) {
        View view = View.inflate(BaseApplication.getAppContext(), R.layout.list_empty_layout, null);
        TextView textView = view.findViewById(R.id.empty);
        textView.setTextColor(BaseApplication.getAppContext().getResources().getColor(R.color.colorPrimary));
        textView.setText(message);
        ImageView imageView = view.findViewById(R.id.icon);
        imageView.setImageResource(resId);
        setEmptyView(view);
    }

    public void setSelfEmptyView(String message) {
        View view = View.inflate(BaseApplication.getAppContext(), R.layout.list_empty_layout, null);
        TextView textView = view.findViewById(R.id.title);
        textView.setTextColor(BaseApplication.getAppContext().getResources().getColor(R.color.colorPrimary));
        textView.setText(message);
        setEmptyView(view);
    }

    protected String getEmptyViewText() {
        return StringUtil.isStringValid(emptyText) ? emptyText : StringUtil.emptyString();
    }


    protected @DrawableRes
    int getEmptyViewImage() {
        return 0;
    }

    protected int getColor(@ColorRes int resId) {
        return mContext.getResources().getColor(resId);
    }

    protected float getDimension(@DimenRes int resId) {
        return mContext.getResources().getDimension(resId);
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) mContext;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public void cleanList() {
        removeAllHeaderView();
        getData().clear();
        notifyDataSetChanged();
    }

    public void addTopAndBottomMargin(com.base.base.BaseViewHolder holder, float margin) {

        int marginT = ScreenTool.dip2px(margin);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (getData().size() == 1) {
            layoutParams.setMargins(0, marginT, 0, 0);
        } else {
            if (holder.getAdapterPosition() - getHeaderLayoutCount() == 0) {
                layoutParams.setMargins(0, marginT, 0, 0);
            } else if (holder.getAdapterPosition() - getHeaderLayoutCount() == getData().size() - 1) {
                layoutParams.setMargins(0, 0, 0, marginT);
            } else {
                layoutParams.setMargins(0, 0, 0, 0);
            }
        }

        holder.itemView.setLayoutParams(layoutParams);
    }


    @NonNull
    @Override
    public List<T> getData() {
        return super.getData();
    }

    public void  setParams(TextView tv,int Resource)
    {
        tv.setPadding(5,2,5,2);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv.getLayoutParams();
        params.height=48;
        params.setMargins(0,0,10,0);
        tv.setBackgroundResource(Resource);
        tv.setLayoutParams(params);
    }
    public void  defultParams(TextView tv,int Resource)
    {
        tv.setPadding(0,0,0,0);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tv.getLayoutParams();
        params.height=60;
        params.setMargins(0,0,0,0);
        tv.setBackgroundResource(Resource);
        tv.setLayoutParams(params);
    }
}

