package com.cpigeon.book.module.foot.viewmodel;

import com.base.base.BaseViewModel;
import com.base.http.HttpErrorException;
import com.base.util.utility.LogUtil;
import com.cpigeon.book.model.PigeonPublicModel;

import io.reactivex.functions.Consumer;

/**
 * 不同模块 公用的接口 如： 羽色 ，眼砂  雌雄，血统
 * Created by Administrator on 2018/8/9.
 */
public class PigeonPublicViewModel extends BaseViewModel {


//    //获取鸽子性别
//    public void getTXGP_PigeonSexType_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_PigeonSexType_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//
//    //获取鸽子选择  血统
//    public void getTXGP_PigeonBloodType_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_PigeonBloodType_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//
//    //获取鸽子选择  眼砂
//    public void getTXGP_PigeonEyeType_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_PigeonEyeType_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//
//    //获取鸽子选择  羽色
//    public void getTXGP_PigeonPlumeType_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_PigeonPlumeType_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//    //足环的来源
//    public void getTXGP_FootRingSource_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_FootRingSource_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//
//    //获取  足环的类型  选择
//    public void getTXGP_FootRingType_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_FootRingType_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }
//
//
//    //获取  种鸽来源  选择
//    public void getTXGP_PigeonSource_SelectData() {
//        submitRequestThrowError(PigeonPublicModel.getTXGP_PigeonSource_Select(), r -> {
//            if (r.isOk()) {
//
//            } else throw new HttpErrorException(r);
//        });
//    }


    public String selectType = "1";

    public Consumer<String> setSelectType() {
        return s -> {
            LogUtil.print("输入：" + s);
            selectType = s;
        };
    }

    //获取  足环，种赛鸽的类型，状态，来源，羽色，血统，眼沙，性别
    public void getTXGP_Type_SelectData() {
        submitRequestThrowError(PigeonPublicModel.getTXGP_Type_Select(selectType), r -> {
            if (r.isOk()) {

            } else throw new HttpErrorException(r);
        });
    }

}
