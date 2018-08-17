package com.cpigeon.book.model;

import com.base.http.ApiResponse;
import com.cpigeon.book.R;
import com.cpigeon.book.http.RequestData;
import com.cpigeon.book.model.entity.DetailsSingleFootEntity;
import com.cpigeon.book.model.entity.FootAdminListEntity;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2018/8/7.
 */

public class FootAdminModel {


    //hl 添加足环（单个）
    public static Observable<ApiResponse<Object>> getTXGP_FootRing_Add(String foot, String money, String footType, String footState, String footSource, String remark) {
        return RequestData.<ApiResponse<Object>>build()
                .setToJsonType(new TypeToken<ApiResponse<Object>>() {
                }.getType())
                .url(R.string.foot_add_single)
                .addBody("numstr", foot)//足环号码
                .addBody("money", money)//足环金额
                .addBody("typeid", footType)// 足环类型
                .addBody("stateid", footState)// 足环状态
                .addBody("source", footSource)// 足环来源
                .addBody("reamrk", remark)// 备注
                .request();
    }

    //hl 修改足环（单个）
    public static Observable<ApiResponse<Object>> getTXGP_FootRing_Edit(String footId, String foot, String money, String footType, String footState, String footSource, String remark) {
        return RequestData.<ApiResponse<Object>>build()
                .setToJsonType(new TypeToken<ApiResponse<Object>>() {
                }.getType())
                .url(R.string.foot_edit_single)
                .addBody("footid", footId)//足环id
                .addBody("numstr", foot)//足环号码
                .addBody("money", money)//足环金额
                .addBody("typeid", footType)// 足环类型
                .addBody("state", footState)// 足环状态
                .addBody("source", footSource)// 足环来源
                .addBody("reamrk", remark)// 备注
                .request();
    }

    //hl 删除足环（单个）
    public static Observable<ApiResponse<Object>> getTXGP_FootRing_Delete(String delFootId) {
        return RequestData.<ApiResponse<Object>>build()
                .setToJsonType(new TypeToken<ApiResponse<Object>>() {
                }.getType())
                .url(R.string.foot_del_single)
                .addBody("footid", delFootId)//足环id
                .request();
    }


    //hl 获取单个足环详细
    public static Observable<ApiResponse<DetailsSingleFootEntity>> getTXGP_FootRing_Select(String footId) {
        return RequestData.<ApiResponse<DetailsSingleFootEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<DetailsSingleFootEntity>>() {
                }.getType())
                .url(R.string.foot_details_single)
                .addBody("footid", footId)
                .request();
    }


    //hl 添加足环号段
    public static Observable<ApiResponse<Object>> getTXGP_FootRing_AddSection(String startFoot, String endFoot, String typeId, String stateId, String source
            , String money, String usenum, String remark) {
        return RequestData.<ApiResponse<Object>>build()
                .setToJsonType(new TypeToken<ApiResponse<Object>>() {
                }.getType())
                .url(R.string.foot_add_segment)
                .addBody("fromNum", startFoot)//开始足环号
                .addBody("endNum", endFoot)//终止足环号
                .addBody("typeid", typeId)//类型
                .addBody("state", stateId)//状态
                .addBody("source", source)//来源
                .addBody("money", money)//足环金额
                .addBody("usenum", usenum)//挂环次数
                .addBody("remark", remark)//备注
                .request();
    }


    //hl 得到各种类型的足环个数
    public static Observable<ApiResponse<List<FootAdminListEntity>>> getTXGP_FootRing_SelectKeyAll(int pi, int ps, String year, String typeid, String stateid, String key) {
        return RequestData.<ApiResponse<List<FootAdminListEntity>>>build()
                .setToJsonType(new TypeToken<ApiResponse<List<FootAdminListEntity>>>() {
                }.getType())
                .url(R.string.foot_list_all)
                .addBody("pi", String.valueOf(pi))
                .addBody("ps", String.valueOf(ps))
                .addBody("year", year)
                .addBody("typeid", typeid)
                .addBody("stateid", stateid)
                .addBody("key", key)
                .request();
    }
}
