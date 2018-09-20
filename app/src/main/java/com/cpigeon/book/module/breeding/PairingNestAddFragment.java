package com.cpigeon.book.module.breeding;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.util.IntentBuilder;
import com.base.util.Lists;
import com.base.util.Utils;
import com.base.util.map.LocationLiveData;
import com.base.util.map.WeatherLiveData;
import com.base.util.picker.PickerUtil;
import com.base.util.utility.LogUtil;
import com.base.util.utility.TimeUtil;
import com.base.util.utility.ToastUtils;
import com.base.widget.BottomSheetAdapter;
import com.base.widget.recyclerview.XRecyclerView;
import com.cpigeon.book.R;
import com.cpigeon.book.base.BaseBookFragment;
import com.cpigeon.book.base.BaseInputDialog;
import com.cpigeon.book.model.entity.PairingInfoEntity;
import com.cpigeon.book.model.entity.PairingNestInfoEntity;
import com.cpigeon.book.model.entity.PigeonEntity;
import com.cpigeon.book.module.breeding.adapter.OffspringInfoAdapter;
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

    @BindView(R.id.rv_offspring_info)
    RecyclerView rv_offspring_info;

    private PairingNestAddViewModel mPairingNestAddViewModel;
    private OffspringInfoAdapter mOffspringInfoAdapter;

    public static void start(Activity activity, PairingInfoEntity mPairingInfoEntity, PigeonEntity mBreedPigeonEntity, int maxNest) {
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_DATA, mPairingInfoEntity)
                .putExtra(IntentBuilder.KEY_DATA_2, mBreedPigeonEntity)
                .putExtra(IntentBuilder.KEY_DATA_3, maxNest)
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

        mPairingNestAddViewModel.mPairingInfoEntity = (PairingInfoEntity) getBaseActivity().getIntent().getSerializableExtra(IntentBuilder.KEY_DATA);
        mPairingNestAddViewModel.mBreedPigeonEntity = (PigeonEntity) getBaseActivity().getIntent().getSerializableExtra(IntentBuilder.KEY_DATA_2);

        int nestNum = getBaseActivity().getIntent().getIntExtra(IntentBuilder.KEY_DATA_3, 0);

        //窝次
        llNestNum.setContent(String.valueOf(++nestNum));
        //父足环号码
        llFootFather.setContent(mPairingNestAddViewModel.mPairingInfoEntity.getMenFootRingNum());
        //母足环号码
        llFootMother.setContent(mPairingNestAddViewModel.mPairingInfoEntity.getWoFootRingNum());

        llPairingTime.setContent(TimeUtil.format(new Date().getTime(), TimeUtil.FORMAT_YYYYMMDD));
        mPairingNestAddViewModel.pairingTime = TimeUtil.format(new Date().getTime(), TimeUtil.FORMAT_YYYYMMDD);
        mPairingNestAddViewModel.isCanCommit();

        mOffspringInfoAdapter = new OffspringInfoAdapter();
        rv_offspring_info.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        rv_offspring_info.setAdapter(mOffspringInfoAdapter);
        mOffspringInfoAdapter.setOnItemClickListener((adapter, view1, position) -> {
            adapter.remove(position);
            mPairingNestAddViewModel.setIdStr(mOffspringInfoAdapter);
        });


    }


    @Override
    protected void initObserve() {
        super.initObserve();
        mPairingNestAddViewModel.isCanCommit.observe(this, aBoolean -> {
            TextViewUtil.setEnabled(tvNextStep, aBoolean);
        });

        LocationLiveData.get(true).observe(this, aMapLocation -> {
            Log.d("dingwei", "initObserve: 城市--》" + aMapLocation.getCity());
            LogUtil.print(aMapLocation);
            WeatherLiveData.get(aMapLocation.getCity()).observe(this, localWeatherLive -> {
                Log.d("dingwei", "initObserve: 天气" + localWeatherLive.getWeather());
                mPairingNestAddViewModel.weather = localWeatherLive.getWeather();//天气
                mPairingNestAddViewModel.temper = localWeatherLive.getTemperature();//气温
                mPairingNestAddViewModel.hum = localWeatherLive.getHumidity();//湿度
                mPairingNestAddViewModel.dir = localWeatherLive.getWindDirection();//风向
            });
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
                OffspringChooseFragment.start(getBaseActivity(), PairingNestAddFragment.requestCode);
                mPairingNestAddViewModel.isCanCommit();
                break;
            case R.id.tv_next_step:
                //立即添加
                mPairingNestAddViewModel.getTXGP_PigeonBreedNest_Add();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PairingNestAddFragment.requestCode) {
            //选择子代后返回
            try {
                PigeonEntity mBreedPigeonEntity = (PigeonEntity) data.getSerializableExtra(IntentBuilder.KEY_DATA);
                Log.d("hehheheheh", "onActivityResult: " + mBreedPigeonEntity.getFootRingNum());

                PairingNestInfoEntity.PigeonListBean mOffspringInfo = new PairingNestInfoEntity.PigeonListBean.Builder()
                        .FootRingID(mBreedPigeonEntity.getFootRingID())
                        .FootRingNum(mBreedPigeonEntity.getFootRingNum())
                        .PigeonID(mBreedPigeonEntity.getPigeonID())
                        .PigeonPlumeName(mBreedPigeonEntity.getPigeonPlumeName())
                        .build();
                mOffspringInfoAdapter.addData(mOffspringInfo);
                mOffspringInfoAdapter.notifyDataSetChanged();

                mPairingNestAddViewModel.setIdStr(mOffspringInfoAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final int requestCode = 0x0000201;


}
