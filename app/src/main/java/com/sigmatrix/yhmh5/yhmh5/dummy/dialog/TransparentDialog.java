package com.sigmatrix.yhmh5.yhmh5.dummy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sigmatrix.yhmh5.yhmh5.R;


public class TransparentDialog extends Dialog implements View.OnClickListener {

  public interface OnButtonClickListener {
    void onPositiveButtonClicked();
    void onNegativeButtonClicked();
    void onNeutralButtonClicked();
  }

  private OnButtonClickListener mClickListener;
  
  private View mView;
  private Button mButtonPositive;
  private CharSequence mButtonPositiveText;
  private Button mButtonNegative;
  private CharSequence mButtonNegativeText;
  private Button mButtonNeutral;
  private CharSequence mButtonNeutralText;
  private TextView mTitle;
  private CharSequence mTitleText;
  private ImageView mIcon;
  
  public TransparentDialog(Context context) {
    // transparency style has been removed
    // => TransparentDialog is not transparent anymore
    super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
    init(context);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private void init(Context context) {
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mView = inflater.inflate(R.layout.transparent_dialog, null);
    mTitle = (TextView)mView.findViewById(R.id.transparent_dialog_Title);
    mIcon = (ImageView)mView.findViewById(R.id.transparent_dialog_Icon);

    setContentView(mView);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (mIcon != null && mIcon.getDrawable() == null) {
      mIcon.setVisibility(View.GONE);
    }
  }

  public void setView(View view) {
    if (view != null) {
      FrameLayout custom = (FrameLayout) mView.findViewById(R.id.transparent_dialog_custom);
      custom.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    } else {
      mView.findViewById(R.id.transparent_dialog_customPanel).setVisibility(View.GONE);
    }
  }

  public void setButton(int whichButton, CharSequence text, OnButtonClickListener listener) {
    mClickListener = listener;
    switch (whichButton) {
    case DialogInterface.BUTTON_POSITIVE:
      mButtonPositiveText = text;
      if (mButtonPositive != null) {
        mButtonPositive.setText(mButtonPositiveText);
      }
      break;
    case DialogInterface.BUTTON_NEGATIVE:
      mButtonNegativeText = text;
      if (mButtonNegative != null) {
        mButtonNegative.setText(mButtonNegativeText);
      }
      break;
    case DialogInterface.BUTTON_NEUTRAL:
      mButtonNeutralText = text;
      if (mButtonNeutral != null) {
        mButtonNeutral.setText(mButtonNeutralText);
      }
      break;
    default:
      throw new IllegalArgumentException("Button does not exist");
    }
    setupButtons();
  }

  public Button getButton(int whichButton) {
    switch (whichButton) {
    case DialogInterface.BUTTON_POSITIVE:
      return mButtonPositive;
    case DialogInterface.BUTTON_NEGATIVE:
      return mButtonNegative;
    case DialogInterface.BUTTON_NEUTRAL:
      return mButtonNeutral;
    default:
      return null;
    }
  }
  
  @Override
  public void setTitle(CharSequence title) {
    mTitleText = title;
    if (mTitle != null) {
      mTitle.setText(mTitleText);
    }
  }
  
  public void setIcon(int id) {
    if (mIcon != null) {
      mIcon.setImageResource(id);
    }
  }

  private boolean setupButtons() {
    int BIT_BUTTON_POSITIVE = 1;
    int BIT_BUTTON_NEGATIVE = 2;
    int BIT_BUTTON_NEUTRAL = 4;
    int whichButtons = 0;
    mButtonPositive = (Button) mView.findViewById(R.id.transparent_dialog_button1);
    mButtonPositive.setOnClickListener(this);

    if (TextUtils.isEmpty(mButtonPositiveText)) {
      mButtonPositive.setVisibility(View.GONE);
    } else {
      mButtonPositive.setText(mButtonPositiveText);
      mButtonPositive.setVisibility(View.VISIBLE);
      whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
    }

    mButtonNegative = (Button) mView.findViewById(R.id.transparent_dialog_button2);
    mButtonNegative.setOnClickListener(this);

    if (TextUtils.isEmpty(mButtonNegativeText)) {
      mButtonNegative.setVisibility(View.GONE);
    } else {
      mButtonNegative.setText(mButtonNegativeText);
      mButtonNegative.setVisibility(View.VISIBLE);

      whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
    }

    mButtonNeutral = (Button) mView.findViewById(R.id.transparent_dialog_button3);
    mButtonNeutral.setOnClickListener(this);

    if (TextUtils.isEmpty(mButtonNeutralText)) {
      mButtonNeutral.setVisibility(View.GONE);
    } else {
      mButtonNeutral.setText(mButtonNeutralText);
      mButtonNeutral.setVisibility(View.VISIBLE);

      whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
    }

    /*
     * If we only have 1 button it should be centered on the layout and expand
     * to fill 50% of the available space.
     */
    if (whichButtons == BIT_BUTTON_POSITIVE) {
      centerButton();
    } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
      centerButton();
    } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
      centerButton();
    } else {
      alignButtons();
    }
    return whichButtons != 0;
  }

  private void centerButton() {
    LinearLayout.LayoutParams params;
    View leftSpacer = mView.findViewById(R.id.transparent_dialog_leftSpacer);
    if (leftSpacer != null) {
      params = (LinearLayout.LayoutParams)leftSpacer.getLayoutParams();
      params.weight = 0.5f;
      leftSpacer.setVisibility(View.VISIBLE);
      leftSpacer.setLayoutParams(params);
    }
    View rightSpacer = mView.findViewById(R.id.transparent_dialog_rightSpacer);
    if (rightSpacer != null) {
      params = (LinearLayout.LayoutParams)rightSpacer.getLayoutParams();
      params.weight = 0.5f;
      rightSpacer.setVisibility(View.VISIBLE);
      rightSpacer.setLayoutParams(params);
    }
  }

  private void alignButtons() {
    LinearLayout.LayoutParams params;
    View leftSpacer = mView.findViewById(R.id.transparent_dialog_leftSpacer);
    if (leftSpacer != null) {
      params = (LinearLayout.LayoutParams)leftSpacer.getLayoutParams();
      params.weight = 0;
      leftSpacer.setVisibility(View.VISIBLE);
      leftSpacer.setLayoutParams(params);
    }
    View rightSpacer = mView.findViewById(R.id.transparent_dialog_rightSpacer);
    if (rightSpacer != null) {
      params = (LinearLayout.LayoutParams)rightSpacer.getLayoutParams();
      params.weight = 0;
      rightSpacer.setVisibility(View.VISIBLE);
      rightSpacer.setLayoutParams(params);
    }
  }

  public void onClick(View v) {
    if (mClickListener != null) {
      if (v == mButtonPositive) {
        mClickListener.onPositiveButtonClicked();
      } else if (v == mButtonNegative) {
        mClickListener.onNegativeButtonClicked();
      } else if (v == mButtonNeutral) {
        mClickListener.onNeutralButtonClicked();
      }
    }
  }

}
