package com.sigmatrix.yhmh5.yhmh5.dummy.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.sigmatrix.yhmh5.yhmh5.R;
import com.sigmatrix.yhmh5.yhmh5.dummy.SoftwareWebView;

public class ExceptionDialog extends TransparentDialog implements TransparentDialog.OnButtonClickListener {

  private String title;
  private String message;
  private OnClickListener click;
  private SoftwareWebView wvMessage;
  private View view;

  public ExceptionDialog(Context context, OnClickListener click, Exception e) {
    this(context, click, e.getClass().getSimpleName(), e.getMessage());
  }

  public ExceptionDialog(Context context, OnClickListener click, String title, String message) {
    super(context);
    this.click = click;
    this.title = title;
    this.message = message;
    LayoutInflater inflater =
      (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    view = inflater.inflate(R.layout.exception_dialog, null);
    wvMessage = (SoftwareWebView)view.findViewById(R.id.exception_dialog_WebViewMessage);
    wvMessage.setBackgroundColor(0);
    setView(view);

    setStrings();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  private void setStrings() {
    setTitle(title);
    setButton(BUTTON_POSITIVE, "OK", this);
    wvMessage.loadData("<font color=\"white\">" + message +
        "</font>", "text/html", "utf8");
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_CAMERA) {
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_BACK) {
      getButton(BUTTON_POSITIVE).performClick();
    }
    return super.onKeyDown(keyCode, event);
  }

  public void onPositiveButtonClicked() {
    dismiss();
    if (click != null) { 
      click.onClick(this, BUTTON_POSITIVE);
    }
  }

  public void onNegativeButtonClicked() {
  }

  public void onNeutralButtonClicked() {
  }

}

