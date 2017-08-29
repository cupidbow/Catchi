package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.envisability.catchi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 05.06.2017.
 */
public class ReportActivity extends Activity {
    @BindView(R.id.etReason)
    EditText etReason;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity,ReportActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ivBack)
    public void onBack()
    {
        finish();
    }

    @OnClick(R.id.tvSend)
    public void onSend()
    {
        finish();
    }
}
