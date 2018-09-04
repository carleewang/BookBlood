package com.cpigeon.book.module.menu.service;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.util.IntentBuilder;
import com.base.util.Lists;
import com.base.widget.recyclerview.XRecyclerView;
import com.cpigeon.book.R;
import com.cpigeon.book.base.BaseBookFragment;
import com.cpigeon.book.module.menu.service.adpter.RenewAdapter;
import com.cpigeon.book.veiwholder.ServiceViewHolder;

/**
 * hl  续费
 * Created by Administrator on 2018/8/20.
 */
public class RenewalFragment extends BaseBookFragment {

    XRecyclerView mRecyclerView;
    RenewAdapter mAdapter;


    public static void start(Activity activity) {
        IntentBuilder.Builder()
                .startParentActivity(activity, RenewalFragment.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xrecyclerview_no_padding_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.text_open_service);
        setToolbarRight(R.string.text_open_report, item -> {
            OpenServiceReportFragment.start(getBaseActivity());
            return false;
        });
        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.addItemDecorationLine();
        mAdapter = new RenewAdapter();
        mAdapter.addHeaderView(initHeadView());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(Lists.newTestArrayList());

    }

    private View initHeadView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.include_renew_head, null);
        ConstraintLayout item = view.findViewById(R.id.clOpenService);
        ServiceViewHolder viewHolder = new ServiceViewHolder(item);

        SpannableStringBuilder span = new SpannableStringBuilder("享用所有功能，不受限制，只要:");
        SpannableString odlMoney = new SpannableString("188");
        odlMoney.setSpan(new StrikethroughSpan(), 0, odlMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString newMoney = new SpannableString("188");
        newMoney.setSpan(new ForegroundColorSpan(Color.RED), 0, newMoney.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.append(newMoney);

        span.append("不要");
        span.append(odlMoney);

        viewHolder.bindData(ServiceViewHolder.TYPE_RENEW, span, v -> {
            PayOpenServiceDialog.show("", getBaseActivity().getSupportFragmentManager());
        });
        return view;
    }
}
