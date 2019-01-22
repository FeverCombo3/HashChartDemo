package com.yjq.hashchart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

/**
 * 算力曲线外部ViewGroup
 * Created By YJQ
 */
public class HashChartViewGroup extends LinearLayout {

    private TextView mTvShareUnit;
    private RadioGroup mTimeRadioGroup;
    private RadioButton mRb1h,mRb1day;
    private HashChartView hashChartView;

    private String mDimension;
    private OnDimensionChangeListener mListener;

    public HashChartViewGroup(Context context) {
        super(context);
        init(context);
    }

    public HashChartViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HashChartViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setOrientation(VERTICAL);

        View top = LayoutInflater.from(context).inflate(R.layout.layout_share_view_top,null);

        mTvShareUnit = top.findViewById(R.id.hash_rate_unit);
        mRb1h = top.findViewById(R.id.hash_rate_rb_1h);
        mRb1day = top.findViewById(R.id.hash_rate_rb_1d);
        mTimeRadioGroup = top.findViewById(R.id.hash_rate_rg);

        addView(top);

        hashChartView = new HashChartView(context);
        hashChartView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        addView(hashChartView);

        initTab();
    }

    private void initTab() {
        mTimeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hash_rate_rb_1h) {
                    mRb1h.setTextColor(UIUtils.getColor(R.color.white));
                    mRb1day.setTextColor(UIUtils.getColor(R.color.grey_5));
                    mDimension = "1h";
                } else {
                    mRb1h.setTextColor(UIUtils.getColor(R.color.grey_5));
                    mRb1day.setTextColor(UIUtils.getColor(R.color.white));
                    mDimension = "1d";
                }
                if(mListener != null){
                    mListener.onDimensionChanged(mDimension);
                }
            }
        });
    }

    public interface OnDimensionChangeListener{
        void onDimensionChanged(String dimension);
    }

    public void setOnDimensionChangeListener(OnDimensionChangeListener listener){
        this.mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //hashChartView = findViewById(R.id.hcv);
    }

    public void setUnitAndData(final String coinType, final List<LinePointManager.LinePoint> data){
        if(data == null || data.size() == 0) return;
        mTvShareUnit.setText("算力"+ "(" + CoinUtils.rateUnit(coinType, data.get(0).unit) + ")");
        //hashChartView.setData(mDimension,coinType,data);

        //直接在onCreate中setData显示不出来，得加个post
        hashChartView.post(new Runnable() {
            @Override
            public void run() {
                hashChartView.setData(mDimension,coinType,data);
            }
        });
    }
}
