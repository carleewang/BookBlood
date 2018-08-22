package com.cpigeon.book.base;

import android.os.Bundle;
import android.view.View;

import com.base.BaseFragment;
import com.base.util.dialog.DialogUtils;
import com.base.util.system.AppManager;
import com.base.util.utility.StringUtil;
import com.cpigeon.book.model.UserModel;
import com.cpigeon.book.module.login.LoginActivity;
import com.cpigeon.book.service.SingleLoginService;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Zhu TingYu on 2018/7/26.
 */

public class BaseBookFragment extends BaseFragment {

    Unbinder unbinder;

    @Override
    public void error(int code, String error) {

        if (code == 90102) {

            if (!StringUtil.isStringValid(error)) {
                return;
            }

            //保证界面只有一个错误提示
            if (getBaseActivity().errorDialog == null || !getBaseActivity().errorDialog.isShowing()) {
                getBaseActivity().errorDialog = DialogUtils.createHintDialog(getActivity(), error, SweetAlertDialog.ERROR_TYPE, false, dialog -> {
                    SingleLoginService.stopService();
                    UserModel.getInstance().cleanUserInfo();
                    dialog.dismiss();
                    AppManager.getAppManager().killAllActivity();
                    LoginActivity.start(getBaseActivity());
                });
            }

        } else {
            super.error(code, error);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
