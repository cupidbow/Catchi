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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.envisability.catchi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 03.06.2017.
 */
public class MainDialogFragment extends DialogFragment {

    @BindView(R.id.tvBtn1)
    TextView tvBtn1;
    @BindView(R.id.tvBtn2)
    TextView tvBtn2;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.tvOption1)
    TextView tvOption1;
    @BindView(R.id.tvOption2)
    TextView tvOption2;
    @BindView(R.id.rlAlert)
    RelativeLayout rlAlert;
    @BindView(R.id.rlOptions)
    RelativeLayout rlOptions;
    @BindView(R.id.llParent)
    LinearLayout llParent;

    private static MainDialogFragment _dialog;

    public static final int FLAG_TWO_BTNS = 1;
    public static final int FLAG_CANCELABLE = 2;
    public static final int FLAG_NOT_DISSMISS_AFTER_ACTION = 4;
    public static final int FLAG_WITH_OPTIONS = 8;
    private int flags = 0;

    private OnDialogClickListener dialogClickListener;
    private OnDialogOptionListener dialogOptionListener;

    private String text, btn1Text, btn2Text, option1Text, option2Text;

    public static MainDialogFragment build()
    {
        if(_dialog!=null)
            try {
                _dialog.dismiss();
            }
            catch (Exception e)
            {

            }
        _dialog = new MainDialogFragment();
        return _dialog;
    }

    public MainDialogFragment setFlags(int flags)
    {
        this.flags = flags;
        return this;
    }

    public MainDialogFragment setBody(String body)
    {
        this.text = body;
        return this;
    }

    public MainDialogFragment setButtons(String cancelText, String actionText)
    {
        this.btn1Text = cancelText;
        this.btn2Text = actionText;
        return this;
    }

    public MainDialogFragment setOptions(String option1Text, String option2Text)
    {
        this.option1Text = option1Text;
        this.option2Text = option2Text;
        return this;
    }

    public MainDialogFragment setDialogClickListener(OnDialogClickListener dialogClickListener)
    {
        this.dialogClickListener = dialogClickListener;
        return this;
    }

    public MainDialogFragment setDialogOptionListener(OnDialogOptionListener dialogOptionListener)
    {
        this.dialogOptionListener = dialogOptionListener;
        return this;
    }

    public MainDialogFragment setButton(String actionText)
    {
        this.btn2Text = actionText;
        return this;
    }

    public MainDialogFragment show(Activity activity)
    {
        show(activity.getFragmentManager(),"mainDialog");
        return this;
    }

    private void prepareUI()
    {
        if((flags & FLAG_TWO_BTNS )==0)
        {
            tvBtn1.setVisibility(View.GONE);
        }

        if((flags & FLAG_CANCELABLE)==0)
        {
            setCancelable(false);
        }
        else
        {
            setCancelable(true);
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        if((flags & FLAG_WITH_OPTIONS)==0)
        {
            rlOptions.setVisibility(View.GONE);
        }
        else
        {
            rlAlert.setVisibility(View.GONE);
        }

        if(text!=null)
            tvText.setText(text);
        if(btn1Text!=null)
            tvBtn1.setText(btn1Text);
        if(btn2Text!=null)
            tvBtn2.setText(btn2Text);
        if(option1Text!=null)
            tvOption1.setText(option1Text);
        if(option2Text!=null)
            tvOption2.setText(option2Text);
    }


    @OnClick(R.id.tvBtn1)
    public void onCancel()
    {
        if(dialogClickListener !=null)
            dialogClickListener.onCancelClick();
        if((flags & FLAG_NOT_DISSMISS_AFTER_ACTION )==0)
        {
            dismiss();
        }
    }

    @OnClick(R.id.tvBtn2)
    public void onAction()
    {
        if(dialogClickListener !=null)
            dialogClickListener.onActionClick();
        if((flags & FLAG_NOT_DISSMISS_AFTER_ACTION )==0)
        {
            dismiss();
        }
    }

    @OnClick(R.id.tvOption1)
    public void onOption1()
    {
        if(dialogOptionListener!=null)
            dialogOptionListener.onOption1();
        dismiss();
    }

    @OnClick(R.id.tvOption2)
    public void onOption2()
    {
        if(dialogOptionListener!=null)
            dialogOptionListener.onOption2();
        dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_fragment_main, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0,0,0,0)));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        ButterKnife.bind(this,v);

        prepareUI();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }




    public interface OnDialogClickListener
    {
        void onActionClick();
        void onCancelClick();
    }

    public interface OnDialogOptionListener
    {
        void onOption1();
        void onOption2();
    }

}
