package com.cpigeon.book.module.breeding;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.util.IntentBuilder;
import com.base.util.Lists;
import com.base.util.Utils;
import com.base.util.picker.PickerUtil;
import com.base.util.utility.ToastUtils;
import com.base.widget.BottomSheetAdapter;
import com.cpigeon.book.R;
import com.cpigeon.book.base.BaseBookFragment;
import com.cpigeon.book.base.BaseInputDialog;
import com.cpigeon.book.module.breeding.viewmodel.PairingNestAddViewModel;
import com.cpigeon.book.util.TextViewUtil;
import com.cpigeon.book.widget.LineInputView;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加窝次
 * Created by Administrator on 2018/9/10.
 */

public class PairingNestAddFragment extends BaseBookFragment {


    @BindView(R.id.ll_nest_num)
    LineInputView llNestNum;
    @BindView(R.id.ll_foot_father)
    LineInputView llFootFather;
    @BindView(R.id.ll_foot_mother)
    LineInputView llFootMother;
    @BindView(R.id.ll_pairing_time)
    LineInputView llPairingTime;
    @BindView(R.id.ll_lay_eggs)
    LineInputView llLayEggs;
    @BindView(R.id.ll_lay_eggs_time)
    LineInputView llLayEggsTime;
    @BindView(R.id.ll_fertilized_egg)
    LineInputView llFertilizedEgg;
    @BindView(R.id.ll_fertilized_egg_no)
    LineInputView llFertilizedEggNo;
    @BindView(R.id.ll_hatches_info)
    LineInputView llHatchesInfo;
    @BindView(R.id.ll_hatches_time)
    LineInputView llHatchesTime;
    @BindView(R.id.ll_offspring_info)
    LineInputView llOffspringInfo;
    @BindView(R.id.ll_hatches_num)
    LineInputView llHatchesNum;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;

    private PairingNestAddViewModel mPairingNestAddViewModel;

    public static void start(Activity activity) {
        IntentBuilder.Builder()
                .startParentActivity(activity, PairingNestAddFragment.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPairingNestAddViewModel = new PairingNestAddViewModel();
        initViewModels(mPairingNestAddViewModel);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pairing_nest_add, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("添加窝次");


        mPairingNestAddViewModel.isCanCommit();

    }


    @Override
    protected void initObserve() {
        super.initObserve();
        mPairingNestAddViewModel.isCanCommit.observe(this, aBoolean -> {
            TextViewUtil.setEnabled(tvNextStep, aBoolean);
        });
    }


    private BaseInputDialog mInputDialog;

    @OnClick({R.id.ll_nest_num, R.id.ll_foot_father, R.id.ll_foot_mother, R.id.ll_pairing_time, R.id.ll_lay_eggs, R.id.ll_lay_eggs_time, R.id.ll_fertilized_egg, R.id.ll_fertilized_egg_no, R.id.ll_hatches_info, R.id.ll_hatches_time, R.id.ll_offspring_info, R.id.ll_hatches_num, R.id.tv_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_nest_num:
                //窝次
                break;
            case R.id.ll_foot_father:
                //父足环号码
                break;
            case R.id.ll_foot_mother:
                //母足环号码
                break;
            case R.id.ll_pairing_time:
                //配对时间
                PickerUtil.showTimePicker(getActivity(), new Date().getTime(), (view1, year, monthOfYear, dayOfMonth) -> {
                    llPairingTime.setContent(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    mPairingNestAddViewModel.pairingTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    mPairingNestAddViewModel.isCanCommit();
                });
                break;
            case R.id.ll_lay_eggs:
                //产蛋信息
                String[] chooseWays = getResources().getStringArray(R.array.array_lay_eggs);
                BottomSheetAdapter.createBottomSheet(getBaseActivity(), Lists.newArrayList(chooseWays), p -> {
                    String way = chooseWays[p];
                    if (way.equals(Utils.getString(R.string.string_lay_eggs_yes))) {
                        //已产蛋
                        llLayEggs.setContent(way);
                        llLayEggsTime.setVisibility(View.VISIBLE);
                        llFertilizedEgg.setVisibility(View.VISIBLE);
                        llFertilizedEggNo.setVisibility(View.VISIBLE);

                    } else if (way.equals(Utils.getString(R.string.string_lay_eggs_no))) {
                        //未产蛋
                        llLayEggs.setContent(way);
                        llLayEggsTime.setVisibility(View.GONE);
                        llFertilizedEgg.setVisibility(View.GONE);
                        llFertilizedEggNo.setVisibility(View.GONE);
                    }
                    mPairingNestAddViewModel.layEggs = way;
                    mPairingNestAddViewModel.isCanCommit();
                });

                break;
            case R.id.ll_lay_eggs_time:
                //产蛋时间
                PickerUtil.showTimePicker(getActivity(), new Date().getTime(), (view1, year, monthOfYear, dayOfMonth) -> {
                    llLayEggsTime.setContent(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    mPairingNestAddViewModel.layEggsTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                });
                break;
            case R.id.ll_fertilized_egg:
                //产蛋 受精蛋
                mInputDialog = BaseInputDialog.show(getBaseActivity().getSupportFragmentManager()
                        , R.string.tv_fertilized_egg, InputType.TYPE_CLASS_NUMBER, content -> {
                            mInputDialog.hide();

                            llFertilizedEgg.setContent(content + "个");
                            mPairingNestAddViewModel.fertilizedEgg = content;

                        }, null);

                break;
            case R.id.ll_fertilized_egg_no:
                //产蛋 无精蛋
                mInputDialog = BaseInputDialog.show(getBaseActivity().getSupportFragmentManager()
                        , R.string.tv_fertilized_egg_no, InputType.TYPE_CLASS_NUMBER, content -> {
                            mInputDialog.hide();
                            llFertilizedEggNo.setContent(content + "个");
                            mPairingNestAddViewModel.fertilizedEggNo = content;
                        }, null);

                break;
            case R.id.ll_hatches_info:
                //出壳信息
                String[] chooseWays2 = getResources().getStringArray(R.array.array_hatches_info);
                BottomSheetAdapter.createBottomSheet(getBaseActivity(), Lists.newArrayList(chooseWays2), p -> {
                    String way = chooseWays2[p];
                    if (way.equals(Utils.getString(R.string.string_hatches_info_yes))) {
                        //已出壳
                        llHatchesInfo.setContent(way);
                        llHatchesTime.setVisibility(View.VISIBLE);
                        llHatchesNum.setVisibility(View.VISIBLE);
                    } else if (way.equals(Utils.getString(R.string.string_hatches_info_no))) {
                        //未出壳
                        llHatchesInfo.setContent(way);
                        llHatchesTime.setVisibility(View.GONE);
                        llHatchesNum.setVisibility(View.GONE);
                    }

                    mPairingNestAddViewModel.hatchesInfo = way;
                    mPairingNestAddViewModel.isCanCommit();
                });

                break;
            case R.id.ll_hatches_time:
                //出壳时间
                PickerUtil.showTimePicker(getActivity(), new Date().getTime(), (view1, year, monthOfYear, dayOfMonth) -> {
                    llHatchesTime.setContent(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    mPairingNestAddViewModel.hatchesTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                });
                break;
            case R.id.ll_hatches_num:
                //出壳个数
                mInputDialog = BaseInputDialog.show(getBaseActivity().getSupportFragmentManager()
                        , R.string.tv_hatches_num, InputType.TYPE_CLASS_NUMBER, content -> {
                            mInputDialog.hide();
                            llHatchesNum.setContent(content + "个");
                            mPairingNestAddViewModel.hatchesNum = content;
                        }, null);
                break;
            case R.id.ll_offspring_info:
                //子代信息
                llOffspringInfo.setContent("已挂环");
                mPairingNestAddViewModel.offspringInfo = "已挂环";

                mPairingNestAddViewModel.isCanCommit();
                break;
            case R.id.tv_next_step:
                //立即添加
                ToastUtils.showLong(getBaseActivity(), "点击立即添加");
                break;
        }
    }
}