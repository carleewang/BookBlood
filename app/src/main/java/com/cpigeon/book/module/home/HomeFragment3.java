package com.cpigeon.book.module.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.base.util.dialog.DialogUtils;
import com.base.util.system.AppManager;
import com.cpigeon.book.R;
import com.cpigeon.book.base.BaseBookFragment;
import com.cpigeon.book.model.UserModel;
import com.cpigeon.book.module.foot.BreedPigeonFragment;
import com.cpigeon.book.module.foot.FootAdminHomeFragment;
import com.cpigeon.book.module.foot.FootAdminListFragment;
import com.cpigeon.book.module.foot.StatisticalFragment;
import com.cpigeon.book.module.login.LoginActivity;
import com.cpigeon.book.module.login.viewmodel.LoginViewModel;
import com.cpigeon.book.service.SingleLoginService;

/**
 * Created by Zhu TingYu on 2018/7/10.
 */

public class HomeFragment3 extends BaseBookFragment {

    Button acBtn;
    Button ac_btns1;
    Button ac_btns2;
    Button ac_btns3;
    Button ac_btns4;
    Button ac_btns5;

    private LoginViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new LoginViewModel();
        initViewModel(mViewModel);


        acBtn = findViewById(R.id.ac_btns);
        ac_btns1 = findViewById(R.id.ac_btns1);
        ac_btns2 = findViewById(R.id.ac_btns2);
        ac_btns3 = findViewById(R.id.ac_btns3);
        ac_btns4 = findViewById(R.id.ac_btns4);
        ac_btns5 = findViewById(R.id.ac_btns5);

        acBtn.setOnClickListener(v -> {
            FootAdminHomeFragment.start(getActivity());
        });

        //hl  退出登录
        mViewModel.outMsg.observe(this, s -> {
            SingleLoginService.stopService();
            UserModel.getInstance().cleanUserInfo();
            getBaseActivity().errorDialog = DialogUtils.createSuccessDialog(getActivity(), s, false, dialog -> {
                dialog.dismiss();
                //结束所有页面，跳转到登录页
                AppManager.getAppManager().killAllToLoginActivity(LoginActivity.class);
            });
        });

        ac_btns1.setOnClickListener(v -> {
            BreedPigeonFragment.start(getActivity());
        });

        ac_btns2.setOnClickListener(v -> {
            mViewModel.outLogin();
        });

        ac_btns3.setOnClickListener(v -> {
            //用户信息
            InfoDetailsFragment.start(getActivity());
        });

        ac_btns4.setOnClickListener(v -> {
            //获取统计数据
            StatisticalFragment.start(getActivity());
        });

        ac_btns5.setOnClickListener(v -> {
            //足环管理列表
            FootAdminListFragment.start(getActivity());
        });
    }
}
