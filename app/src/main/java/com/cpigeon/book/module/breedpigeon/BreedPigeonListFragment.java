package com.cpigeon.book.module.breedpigeon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.base.util.Lists;
import com.base.util.RxUtils;
import com.base.util.Utils;
import com.base.util.utility.StringUtil;
import com.base.widget.guideview.Component;
import com.base.widget.guideview.GuideManager;
import com.cpigeon.book.R;
import com.cpigeon.book.base.SearchFragmentParentActivity;
import com.cpigeon.book.event.PigeonAddEvent;
import com.cpigeon.book.model.entity.PigeonEntity;
import com.cpigeon.book.model.entity.PigeonSexCountEntity;
import com.cpigeon.book.module.basepigeon.BaseFootListFragment;
import com.cpigeon.book.module.basepigeon.BaseSearchPigeonActivity;
import com.cpigeon.book.module.basepigeon.StateListAdapter;
import com.cpigeon.book.module.breedpigeon.adpter.BreedPigeonListAdapter;
import com.cpigeon.book.module.breedpigeon.adpter.LinearLayoutListener;
import com.cpigeon.book.module.foot.PigeonDeleteComponent;
import com.cpigeon.book.module.homingpigeon.MyHomingPigeonFragment;
import com.cpigeon.book.module.homingpigeon.OnDeleteListener;
import com.cpigeon.book.util.SharedPreferencesTool;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 种鸽列表
 * Created by Zhu TingYu on 2018/8/28.
 */

public class BreedPigeonListFragment extends BaseFootListFragment {

    ImageView mImgAdd;

    public static void start(Activity activity) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseFootListFragment.STATEID, PigeonEntity.IN_THE_SHED);
        bundle.putString(BaseFootListFragment.TYPEID, PigeonEntity.ID_BREED_PIGEON);
        SearchFragmentParentActivity.
                start(activity, BreedPigeonListFragment.class, true, bundle);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.xreclyview_with_add_circle_btn;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.text_my_breed_pigeon);
    }

    @Override
    protected void initData() {
        super.initData();

        setStartSearchActvity(BaseSearchPigeonActivity.class);

        mImgAdd = findViewById(R.id.imgAdd);
        mImgAdd.setOnClickListener(v -> {
            //种鸽录入
            InputBreedInBookFragment.start(getBaseActivity());
        });

        mAdapter = new BreedPigeonListAdapter(new OnDeleteListener() {
            @Override
            public void delete(String PigeonId) {
                mBreedPigeonListModel.id = PigeonId;
                mBreedPigeonListModel.deletePigeon();
            }
        }, new LinearLayoutListener() {
            @Override
            public void click(int position) {
                try {
                    PigeonEntity mBreedPigeonEntity = mAdapter.getData().get(position);
                    BreedPigeonDetailsFragment.start(getBaseActivity(),
                            mBreedPigeonEntity.getPigeonID(),
                            mBreedPigeonEntity.getFootRingID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBreedPigeonListModel.getPigeonCount();

    }



    @Override
    protected void initObserve() {
        super.initObserve();
        mBreedPigeonListModel.mLivePigeonSexCount.observe(this, pigeonSexCountEntity -> {
            if(mAdapter.getHeaderLayoutCount() == 0){
                mAdapter.addHeaderView(initHeadView(pigeonSexCountEntity));
            }

            String isShow = SharedPreferencesTool.Get(getBaseActivity(),
                    SharedPreferencesTool.SP_GUIDE_DELETE_PIGEON, "", SharedPreferencesTool.SP_FILE_GUIDE);


            if (!StringUtil.isStringValid(isShow)) {
                composite.add(RxUtils.delayed(100, aLong -> {
                    GuideManager.get()
                            .setGuideComponent(new PigeonDeleteComponent())
                            .setTagView(mAdapter.getViewByPosition(mRecyclerView.getRecyclerView()
                                    ,1, R.id.llay))
                            .setGuideLocation(Component.ANCHOR_BOTTOM)
                            .setViewLocation(Component.FIT_CENTER)
                            .show(getBaseActivity());
                    SharedPreferencesTool.Save(getBaseActivity(), SharedPreferencesTool.SP_GUIDE_DELETE_PIGEON
                            , SharedPreferencesTool.SP_GUIDE_DELETE_PIGEON, SharedPreferencesTool.SP_FILE_GUIDE);
                }));
            }
        });

    }

    @Override
    protected void afterSetListData() {
        mBreedPigeonListModel.getPigeonCount();
    }

    private View initHeadView(PigeonSexCountEntity countEntity) {
        View view = LayoutInflater.from(getBaseActivity()).inflate(R.layout.include_stat_list_head, null);

        RecyclerView recyclerView = view.findViewById(R.id.statList);
        CardView cv_all_pigeon = view.findViewById(R.id.cv_all_pigeon);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        StateListAdapter stateListAdapter = new StateListAdapter(Lists.newArrayList(getResources()
                .getStringArray(R.array.array_breed_pigeon_type)));
        recyclerView.setAdapter(stateListAdapter);
        stateListAdapter.setUnitString(Utils.getString(R.string.text_pigeon_unit));
        stateListAdapter.setMaxCount(countEntity.ZCount);
        stateListAdapter.setNewData(countEntity.getBreedPigeonStat());
        cv_all_pigeon.setOnClickListener(v -> {
            MyHomingPigeonFragment.start(getBaseActivity());
        });

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnEvent(PigeonAddEvent event) {
        dataRefresh();
    }

    private void dataRefresh() {
        mBreedPigeonListModel.getPigeonCount();
        mAdapter.cleanList();
        mBreedPigeonListModel.pi = 1;
        mBreedPigeonListModel.getPigeonList();
    }

}