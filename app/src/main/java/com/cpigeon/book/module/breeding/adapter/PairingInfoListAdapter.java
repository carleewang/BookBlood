package com.cpigeon.book.module.breeding.adapter;

import com.base.base.BaseViewHolder;
import com.base.base.adpter.BaseQuickAdapter;
import com.base.util.Lists;
import com.cpigeon.book.R;
import com.cpigeon.book.model.entity.BreedingInfoEntity;

/**
 * Created by Administrator on 2018/9/10.
 */

public class PairingInfoListAdapter  extends BaseQuickAdapter<BreedingInfoEntity, BaseViewHolder> {


    public PairingInfoListAdapter() {
        super(R.layout.item_pairing_info, Lists.newArrayList(new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build(),
                new BreedingInfoEntity.Builder().build()
                ));
    }

    @Override
    protected void convert(BaseViewHolder helper, BreedingInfoEntity item) {

    }
}