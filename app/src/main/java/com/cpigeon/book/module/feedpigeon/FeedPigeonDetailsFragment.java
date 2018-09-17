package com.cpigeon.book.module.feedpigeon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.util.IntentBuilder;
import com.base.util.Lists;
import com.base.util.PictureSelectUtil;
import com.base.util.glide.GlideUtil;
import com.base.widget.recyclerview.XRecyclerView;
import com.bumptech.glide.Glide;
import com.cpigeon.book.R;
import com.cpigeon.book.base.BaseBookFragment;
import com.cpigeon.book.model.UserModel;
import com.cpigeon.book.model.entity.ImgTypeEntity;
import com.cpigeon.book.model.entity.PigeonEntity;
import com.cpigeon.book.module.breeding.PairingInfoListFragment;
import com.cpigeon.book.module.feedpigeon.adapter.FeedPigeonDetailsAdapter;
import com.cpigeon.book.module.feedpigeon.viewmodel.FeedPigeonListViewModel;
import com.cpigeon.book.module.photo.ImgUploadFragment;
import com.cpigeon.book.util.PigeonPublicUtil;
import com.cpigeon.book.util.RecyclerViewUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 养鸽记录详情
 * Created by Zhu TingYu on 2018/9/7.
 */

public class FeedPigeonDetailsFragment extends BaseBookFragment {

    XRecyclerView mRecyclerView;
    FeedPigeonDetailsAdapter mAdapter;

    private FeedPigeonListViewModel mFeedPigeonListViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mFeedPigeonListViewModel = new FeedPigeonListViewModel();
        initViewModels(mFeedPigeonListViewModel);
    }

    public static void start(Activity activity, PigeonEntity mBreedPigeonEntity) {
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, mBreedPigeonEntity)
                .startParentActivity(activity, FeedPigeonDetailsFragment.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xrecyclerview_no_padding_layout, container, false);
    }

    @Override
    protected void initObserve() {
        super.initObserve();

        mFeedPigeonListViewModel.mFeedPigeonListData.observe(this, datas -> {
            setProgressVisible(false);
            RecyclerViewUtils.setLoadMoreCallBack(mRecyclerView, mAdapter, datas);
        });

        mFeedPigeonListViewModel.listEmptyMessage.observe(this, s -> {
            mAdapter.setEmptyText(s);
        });

        //养鸽记录统计
        mFeedPigeonListViewModel.mFeedPigeonStatistical.observe(this, datas -> {

            Glide.with(getBaseActivity())
                    .load(mFeedPigeonListViewModel.mPigeonEntity.getCoverPhotoUrl())
                    .placeholder(R.drawable.ic_img_default)
                    .into(mCircleImageView);

            mTvFootNumber.setText(mFeedPigeonListViewModel.mPigeonEntity.getFootRingNum());

            PigeonPublicUtil.setPigeonSexImg(mFeedPigeonListViewModel.mPigeonEntity.getPigeonSexName(), mImgSex);

            mTvStatus.setText(mFeedPigeonListViewModel.mPigeonEntity.getStateName());

            String remark = "注射疫苗" + datas.getVaccineCount() + "次," +
                    "保健" + datas.getHealthCount() + "次," +
                    "用药" + datas.getDrugCount() + "次," +
                    "病情" + datas.getDiseaseCount() + "次," +
                    "随拍" + datas.getPhotoCount() + "张";

            mTvRemark.setText(remark);

        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.getMenu().clear();
        toolbar.getMenu().add("")
                .setIcon(R.mipmap.ic_feed_record_photo)
                .setOnMenuItemClickListener(item -> {
                    PictureSelectUtil.openCamera(getBaseActivity());

                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        toolbar.getMenu().add("")
                .setIcon(R.mipmap.ic_feed_record_add)
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder(getBaseActivity(), FeedPigeonRecordActivity.class)
                            .putExtra(IntentBuilder.KEY_DATA, mFeedPigeonListViewModel.mPigeonEntity)
                            .startActivity();
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        setTitle(R.string.text_feed_pigeon_record);


        mFeedPigeonListViewModel.mPigeonEntity = (PigeonEntity) getBaseActivity().getIntent().getSerializableExtra(IntentBuilder.KEY_DATA);

        mRecyclerView = findViewById(R.id.list);
        mAdapter = new FeedPigeonDetailsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(Lists.newArrayList());
        mAdapter.addHeaderView(initView());

        mRecyclerView.setRefreshListener(() -> {
            RecyclerViewUtils.setLoadMoreCallBack(mRecyclerView, mAdapter, Lists.newArrayList());
        });

        setProgressVisible(true);
        //获取养鸽记录 列表
        mFeedPigeonListViewModel.getTXGP_Pigeon_SelectRecordData();
        //获取养鸽记录统计
        mFeedPigeonListViewModel.getTXGP_Pigeon_SelectIDCountData();
    }


    CircleImageView mCircleImageView;
    TextView mTvFootNumber;
    ImageView mImgSex;
    TextView mTvStatus;
    TextView mTvRemark;

    private View initView() {
        View view = LayoutInflater.from(getBaseActivity()).inflate(R.layout.include_feed_pigeon_details_head, null);

        mCircleImageView = view.findViewById(R.id.circleImageView);
        mTvFootNumber = view.findViewById(R.id.tvFootNumber);
        mImgSex = view.findViewById(R.id.imgSex);
        mTvStatus = view.findViewById(R.id.tvStatus);
        mTvRemark = view.findViewById(R.id.tvRemark);

        GlideUtil.setGlideImageView(getBaseActivity(), UserModel.getInstance().getUserData().touxiangurl, mCircleImageView);
        mTvFootNumber.setText("2018-12-224645");


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        if (requestCode == 1) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_TYPE, new ImgTypeEntity.Builder().imgPath(selectList.get(0).getCompressPath())
                    .imgType(ImgTypeEntity.TYPE_NEF)
                    .build())
                    .startParentActivity(getBaseActivity(), ImgUploadFragment.class, ImgUploadFragment.CODE_SELECT_COUNTY);

        }
    }
}
