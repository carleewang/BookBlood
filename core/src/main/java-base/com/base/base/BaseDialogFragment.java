package com.base.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.base.base.blurdialogfragment.SupportBlurDialogFragment;
import com.base.http.R;
import com.base.util.Lists;
import com.base.util.Utils;
import com.base.util.system.ScreenTool;
import com.base.widget.LoadingView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Zhu TingYu on 2018/8/6.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    private List<BaseViewModel> viewModels = Lists.newArrayList();

    protected LoadingView progressView;
    ViewGroup mRootView;
    protected final CompositeDisposable composite = new CompositeDisposable();

    public BaseDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        Dialog dialog = new Dialog(getActivity(), getDialogStyle());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        if (getLayoutView() == null) {
            dialog.setContentView(getLayoutRes());
        } else {
            dialog.setContentView(getLayoutView());
        }
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        assert window != null;
        if(getDialogBackground() != 0){
            window.setBackgroundDrawableResource(getDialogBackground());
        }
        //window.setWindowAnimations(R.style.AnimBottomDialog);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.55f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        initLayout(window, lp);

        initView(dialog);

        initProgressLayout(dialog);


        return dialog;
    }
    @StyleRes
    protected int getDialogStyle(){
        return 0;
    }
    @DrawableRes
    protected int getDialogBackground(){
        return R.drawable.shape_bg_corner_3;
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected View getLayoutView() {
        return null;
    }

    protected abstract void initView(Dialog dialog);

    protected void initLayout(Window window, WindowManager.LayoutParams lp) {
        lp.gravity = Gravity.CENTER;
        lp.width = (ScreenTool.getScreenWidth() / 4) * 3;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    public void hide() {
        this.dismiss();
    }

    public BaseActivity getBaseActivity() {
        BaseActivity activity = null;
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
        }
        return activity;
    }

    protected void initViewModel(BaseViewModel viewModel) {
        this.viewModels.add(viewModel);
        viewModel.setBaseActivity(getBaseActivity());
        viewModel.getError().observe(this, restErrorInfo -> {
            if (restErrorInfo != null) {
                error(restErrorInfo.code, restErrorInfo.message);
            }
        });
    }

    protected void error(String message) {
        setProgressVisible(false);
        getBaseActivity().error(message);
    }

    protected void error(@StringRes int resId) {
        setProgressVisible(false);
        error(Utils.getString(resId));
    }

    public void error(int code, String error) {
        getBaseActivity().error(code, error);
    }

    private void initProgressLayout(Dialog dialog) {
        mRootView = (ViewGroup) dialog.getWindow().getDecorView();
        if (progressView == null) {
            progressView = new LoadingView(getContext());
        }
        setProgressVisible(false);
        mRootView.addView(progressView);
    }

    /**
     * 加载框
     *
     * @param isVisible 是否显示
     */

    public void setProgressVisible(boolean isVisible) {
        if (progressView != null) {
            progressView.bringToFront();
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    isVisible ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        composite.clear();
        super.onDismiss(dialog);
    }
}
