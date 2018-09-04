package com.cpigeon.book.module.breed;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.base.base.adpter.BaseQuickAdapter;
import com.base.util.db.AppDatabase;
import com.base.util.db.DbEntity;
import com.cpigeon.book.base.BaseSearchActivity;
import com.cpigeon.book.model.UserModel;
import com.cpigeon.book.model.entity.BreedPigeonEntity;
import com.cpigeon.book.module.breed.adpter.BreedPigeonListAdapter;
import com.cpigeon.book.module.breed.viewmodel.BreedPigeonListModel;
import com.cpigeon.book.util.RecyclerViewUtils;
import com.cpigeon.book.widget.SearchTextView;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/8/28.
 */

public class SearchBreedPigeonActivity extends BaseSearchActivity {
    BreedPigeonListAdapter mAdapter;

    private BreedPigeonListModel mBreedPigeonListModel;

    @Override
    protected List<DbEntity> getHistory() {
        return AppDatabase.getInstance(getBaseContext()).DbEntityDao()
                .getDataByUserAndType(UserModel.getInstance().getUserId(), AppDatabase.TYPE_SEARCH_BREED_PIGEON);
    }

    @Override
    protected BaseQuickAdapter getResultAdapter() {
        mAdapter = new BreedPigeonListAdapter();
        return mAdapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBreedPigeonListModel = new BreedPigeonListModel();
        initViewModel(mBreedPigeonListModel);


        mSearchTextView.setOnSearchTextClickListener(new SearchTextView.OnSearchTextClickListener() {
            @Override
            public void search(String key) {

                setProgressVisible(true);
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mBreedPigeonListModel.searchStr = key;
                mBreedPigeonListModel.getPigeonList();

//                mAdapter.setNewData(Lists.newTestArrayList());
            }

            @Override
            public void cancel() {
                finish();
            }
        });


        mBreedPigeonListModel.isSearch = true;
        mBreedPigeonListModel.typeid = "8";

        mRecyclerView.setRefreshListener(() -> {
            mAdapter.getData().clear();
            mBreedPigeonListModel.pi = 1;
            mBreedPigeonListModel.getPigeonList();
        });

        mAdapter.setOnLoadMoreListener(() -> {
            mBreedPigeonListModel.pi++;
            mBreedPigeonListModel.getPigeonList();
        }, mRecyclerView.getRecyclerView());


        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            BreedPigeonEntity mBreedPigeonEntity = mAdapter.getData().get(position);
            BreedPigeonDetailsFragment.start(getBaseActivity(), mBreedPigeonEntity);
        });

        initObserve();
    }


    protected void initObserve() {

        mBreedPigeonListModel.mPigeonListData.observe(this, datas -> {
            setProgressVisible(false);
            RecyclerViewUtils.setLoadMoreCallBack(mRecyclerView, mAdapter, datas);
        });

        mBreedPigeonListModel.listEmptyMessage.observe(this, s -> {
            mAdapter.setEmptyText(s);
        });

    }
}
