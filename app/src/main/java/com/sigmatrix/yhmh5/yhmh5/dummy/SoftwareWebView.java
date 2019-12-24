package com.sigmatrix.yhmh5.yhmh5.dummy;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class SoftwareWebView extends WebView {

  public SoftwareWebView(Context context, AttributeSet attrs, int defStyle,
      boolean privateBrowsing) {
    super(context, attrs, defStyle, privateBrowsing);
    if (Build.VERSION.SDK_INT > 10) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  public SoftwareWebView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    if (Build.VERSION.SDK_INT > 10) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  public SoftwareWebView(Context context) {
    super(context);
    if (Build.VERSION.SDK_INT > 10) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  public SoftwareWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (Build.VERSION.SDK_INT > 10) {
      setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

}
