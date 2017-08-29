package com.envisability.catchi.dialog_fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.envisability.catchi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Тарас on 05.06.2017.
 */
public class OptionsDialogFragment extends DialogFragment {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llOptions)
    LinearLayout llOptions;

    private static OptionsDialogFragment _dialog;
    private OnOptionDialogClick listener;
    String[] optionsText;

    public static OptionsDialogFragment build()
    {
        if(_dialog!=null)
            _dialog.dismiss();
        _dialog = new OptionsDialogFragment();
        return _dialog;
    }


    public OptionsDialogFragment setOptions(String[] options)
    {
        this.optionsText = options;
        return this;
    }

    public OptionsDialogFragment setListener(OnOptionDialogClick listener)
    {
        this.listener = listener;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_fragment_options, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0,0,0,0)));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        ButterKnife.bind(this,v);

        prepareUI(inflater);

        return v;
    }

    public OptionsDialogFragment show(Activity activity)
    {
        show(activity.getFragmentManager(),"optionsDialog");
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public void prepareUI(LayoutInflater inflater)
    {
        if(optionsText!=null)
        {
            for(int i = 0; i < optionsText.length; i++)
            {
                TextView tv = (TextView)inflater.inflate(R.layout.item_dialog_option,null);
                tv.setText(optionsText[i]);
                llOptions.addView(tv);
                final int index = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener!=null)
                            listener.onOption(OptionsDialogFragment.this, index,optionsText[index]);

                    }
                });
            }
        }
    }

    public interface OnOptionDialogClick
    {
        void onOption(OptionsDialogFragment dialogFragment, int i, String data);
    }
}
