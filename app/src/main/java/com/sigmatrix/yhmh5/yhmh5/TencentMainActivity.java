package com.sigmatrix.yhmh5.yhmh5;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.jb.barcode.BarcodeManager;
import android.jb.barcode.BarcodeManager.Callback;
import android.jb.utils.Tools;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sigmatrix.yhmh5.yhmh5.Utils.AppUrl;
import com.sigmatrix.yhmh5.yhmh5.Utils.SharedPrefUtil;
import com.sigmatrix.yhmh5.yhmh5.Utils.StatusBarUtil;
import com.sigmatrix.yhmh5.yhmh5.Utils.ToastUtil;
import com.sigmatrix.yhmh5.yhmh5.app.MyApplication;
import com.sigmatrix.yhmh5.yhmh5.barcode.BeepManager;
import com.sigmatrix.yhmh5.yhmh5.dummy.SampleActivity;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class TencentMainActivity extends Activity implements View.OnClickListener/*,View.OnLayoutChangeListener */ {
    private Context mContext;
    private JavaScriptInterface JSInterface;
    private WebView webview;
//    private ProgressBar progressBar;
    private String number;//单号
    private String tag;//标识
    private RelativeLayout rl_view1, rl_view3, rl_view4, rl_view5, rl_view8;
    private ImageView iv_view;
    private RelativeLayout left_drawer;
    private DrawerLayout drawer_layout;
    private BeepManager beepManager;
    private BarcodeManager barcodeManager;
    private final int Handler_SHOW_RESULT = 1999;
    private final int Handler_Scan = 2000;
    private long nowTime = 0;
    private long lastTime = 0;

    private SoundPool soundPool;
    private int music;
    private byte[] codeBuffer;
    private String codeId;
    private String barcode;
    private String mDeviceId;
    private String deviceId;
    int tag1;

    public String URL = AppUrl.LOGIN;

    private String cookie = "";
    private String[] split;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            StatusBarUtil.setStatusBarColor(TencentMainActivity.this, R.color.red);
            StatusBarUtil.StatusBarLightMode(TencentMainActivity.this,StatusBarUtil.StatusBarLightMode(TencentMainActivity.this));
        }
        setContentView(R.layout.activity_tencentmain);
        AndroidBug5497Workaround.assistActivity(this);
        webview = (WebView) findViewById(R.id.web_view);
        iv_view = (ImageView) findViewById(R.id.iv_view);
//
        initBroadcastReciever();//手持打开
        initVoice2();//手持打开

        left_drawer = (RelativeLayout) findViewById(R.id.left_drawer);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                webview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                webview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        WebSettings settings = webview.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);//设置js
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportZoom(true);          //允许缩放
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDatabaseEnabled(true);

        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDomStorageEnabled(true);
        webview.requestFocus();
        webview.setScrollContainer(false);

        webview.addJavascriptInterface(new JavaScriptInterface(TencentMainActivity.this), "user");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {

                super.onPageStarted(webView, s, bitmap);
            }

            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                cookie =  cookieManager.getCookie(url);
                split =  cookie.split(";");
                if(cookie!=null&&split.length>1){
                    SharedPrefUtil.getInstance().putString(TencentMainActivity.this,"cookie",cookie);
                }
                String typeId = null;
                HashMap<String, String> map = new HashMap<>();
                if(cookie!=null) {
                    for (int x = 0; x < split.length; x++) {
                        String[] split1 = split[x].split("=");
                        map.put(split1[0], split1[1] + "");
                    }
                    typeId = map.get(" typeId");
                }
                if("1".equals(typeId)||"5".equals(typeId)||"6".equals(typeId)){
                    rl_view3.setVisibility(View.GONE);
                }else{
                    rl_view3.setVisibility(View.VISIBLE);
                }
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    cookieManager.flush();
                }else {
                    CookieSyncManager.getInstance().sync();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return super.shouldInterceptRequest(view, url);
            }
        });

//                cookie = SharedPrefUtil.getInstance().getString(TencentMainActivity.this,"cookie","");
//                split = cookie.split(";");
//                if (/*(URL.indexOf("login/login"))!=-1&&*/split.length>1) {
//                    CookieSyncManager.createInstance(TencentMainActivity.this);
//                    CookieManager cookieManager = CookieManager.getInstance();
//                    cookieManager.setAcceptCookie(true);
//                    cookieManager.removeAllCookie();
//                    for (int x = 0; x < split.length; x++) {
//                        String[] split1 = split[x].split("=");
//                        cookieManager.setCookie(URL,split1[0]+"="+split1[1]);
//                    }
////            cookieManager.setCookie(URL, cookie);
//                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//                        cookieManager.flush();
//                    }else {
//                        CookieSyncManager.getInstance().sync();
//                    }

//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
        webview.loadUrl(URL);

        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
               if(url.indexOf("pgyer")!=-1) {
                   Uri uri = Uri.parse(url);
                   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                   startActivity(intent);
               }
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
//                    iv_view.setVisibility(View.GONE);
                    Message message = new Message();
                    message.what =  1;
                    mHandler2.sendMessageDelayed(message,3000);
                    try {
                        webview.loadUrl("javascript:getVersion('" + getVersionName() + "')");
//                        deviceId = SDK.getDeviceIdentifier(TencentMainActivity.this);
                        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                        deviceId = TelephonyMgr.getDeviceId();
//                        mDeviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
                        mDeviceId = deviceId;
                        webview.loadUrl("javascript:setAuthInfo('" + "Android" +"','"+ deviceId+"','"+ mDeviceId+"')");
                    } catch (Exception e) {
//                        progressBar.setVisibility(View.VISIBLE);
//                        progressBar.setProgress(newProgress);//设置加载进度
                    }
                }
            }
        });
        setView();
    }

    private void setView() {
        rl_view1 = (RelativeLayout) findViewById(R.id.rl_view1);
        rl_view1.setOnClickListener(this);//立即设置

        rl_view3 = (RelativeLayout) findViewById(R.id.rl_view3);
        rl_view3.setOnClickListener(this);//我的库存

        rl_view4 = (RelativeLayout) findViewById(R.id.rl_view4);
        rl_view4.setOnClickListener(this);//修改密码

        rl_view5 = (RelativeLayout) findViewById(R.id.rl_view5);
        rl_view5.setOnClickListener(this);//关于我们

        rl_view8 = (RelativeLayout) findViewById(R.id.rl_view8);
        rl_view8.setOnClickListener(this);//退出登录
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_view1:
                webview.loadUrl(AppUrl.MYINFO);
                setNoClick();
                break;
            case R.id.rl_view3:
                webview.loadUrl(AppUrl.STOCK);
                setNoClick();
                break;
            case R.id.rl_view4:
                webview.loadUrl(AppUrl.UPDATEPWD);
                setNoClick();
                break;
            case R.id.rl_view5:
                webview.loadUrl(AppUrl.ABOUT);
                setNoClick();
                break;
            case R.id.rl_view8:
                webview.loadUrl(AppUrl.LOGOUT);
                setNoClick();
                break;
        }
    }

    private void setNoClick() {
        drawer_layout.closeDrawer(left_drawer);
    }


    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void startScanActivity(int tag) {
            checkCameraPermission(tag);

//            barcodeManager.Barcode_Open(MainActivity.this, dataReceived);

        }
        @JavascriptInterface
        public void startScanActivity() {
            checkCameraPermission(2);

//            barcodeManager.Barcode_Open(MainActivity.this, dataReceived);

        }

        @JavascriptInterface
        public void getMenu() {
            Message message = new Message();
            message.what =  2;
            mHandler2.sendMessage(message);
        }

        @JavascriptInterface
        public void getAuthInfo() {
            Message message = new Message();
            message.what =  3;
            mHandler2.sendMessage(message);

        }
    }

    private void checkCameraPermission(int tag) {
        tag1 = tag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
//            Bundle bundle = new Bundle();
//            bundle.putString("number",number);
//            bundle.putString("tag",tag);
//            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//            intent.putExtras(bundle);
//            startActivityForResult(intent, 1);
            if(tag==1){
                CookieManager cookieManager = CookieManager.getInstance();
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    cookieManager.flush();
                    Intent intent = new Intent(TencentMainActivity.this, SampleActivity.class);
                    startActivityForResult(intent, 1);
                }else {
                    Message message = new Message();
                    message.what = 4;
                    mHandler2.sendMessageDelayed(message,3000);
                    CookieSyncManager.getInstance().sync();
                }
            }
            if(tag==2){
                Intent intent = new Intent(TencentMainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(tag1==1){
                    Intent intent = new Intent(TencentMainActivity.this, SampleActivity.class);
                    startActivityForResult(intent, 1);
                }
                if(tag1==2){
                    Intent intent = new Intent(TencentMainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        } else {
            ToastUtil.toast(TencentMainActivity.this,"授权被拒绝");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            String code = data.getStringExtra("code");
            Log.e("码", code);
            webview.loadUrl("javascript:setLocation('" + code + "')");
        }
    }

    public static String getVersionName() throws Exception {
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(), 0);
        String versionName = packInfo.versionName;
        return versionName;
    }

    private Handler mHandler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    iv_view.setVisibility(View.GONE);
                    break;
                case 2:
                    drawer_layout.openDrawer(left_drawer);
                    break;
                case 3:
//                    if(deviceId==null||"".equals(deviceId)) {
//                        deviceId = SDK.getDeviceIdentifier(TencentMainActivity.this);
//                    }

                    if(mDeviceId==null||"".equals(mDeviceId)) {
                        mDeviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
                    }
//                    webview.loadUrl("javascript:setAuthInfo('" + "Android" +"','"+ deviceId+"','"+ mDeviceId+"')");
                    webview.loadUrl("javascript:setAuthInfo('" + "Android" +"','"+ mDeviceId+"')");
                    break;

                case 4:
                    Intent intent = new Intent(TencentMainActivity.this, SampleActivity.class);
                    startActivityForResult(intent, 1);
                 break;
            }

        }
    };


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
////        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
////
////            Toast.makeText(MainActivity.this, "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
////        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
////
////            Toast.makeText(MainActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
////        }
//    }

    @Override
    protected void onResume() {
        //捷宝
        sendBroadcast(new Intent("ReLoadCom"));
        if (barcodeManager == null) {
            barcodeManager = BarcodeManager.getInstance();
        }
        barcodeManager.Barcode_Start();
        super.onResume();
    }




    private void initBroadcastReciever() {
        //捷宝
        /**
         * 监听橙色按钮按键广播
         */
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jb.action.F4key");
        registerReceiver(f4Receiver, intentFilter);
        beepManager = new BeepManager(this, true, false);
        if (barcodeManager == null) {
            barcodeManager = BarcodeManager.getInstance();
        }
        barcodeManager.Barcode_Open(TencentMainActivity.this, dataReceived);
    }

    /**
     * 捕获扫描物理按键广播
     */
    private BroadcastReceiver f4Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Bundle bundle = intent.getExtras();
            if (intent.hasExtra("F4key")) {
                if (intent.getStringExtra("F4key").equals("down")) {
                    Log.e("trig", "按下黄色按钮");
                    if (null != barcodeManager) {
                        nowTime = System.currentTimeMillis();
                        if (nowTime - lastTime > 200) {
                            barcodeManager.Barcode_Stop();
                            lastTime = nowTime;
                            if (null != barcodeManager) {
                                barcodeManager.Barcode_Start();
                            }
                        }
                    }
                } else if (intent.getStringExtra("F4key").equals("up")) {
                    Log.e("trig", "弹起黄色按钮");
                }
            }
        }
    };

    /**
     * 播放声音初始化
     */
    public void initVoice2() {
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = soundPool.load(MyApplication.getContext(), R.raw.ddd, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
    }

    Callback dataReceived = new Callback() {
        @Override
        public void Barcode_Read(byte[] buffer, String codeId, int errorCode) {
            System.out.println("扫描回调接口：dataReceived+......" + codeId
                    + "errorCode" + errorCode);
            if (null != buffer) {
                codeBuffer = buffer;
                TencentMainActivity.this.codeId = codeId;
                Message msg = new Message();
                msg.what = Handler_SHOW_RESULT;
                mHandler.sendMessage(msg);
                barcodeManager.Barcode_Stop();
            }
        }
    };

    @Override
    protected void onRestart() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.jb.action.F4key");
//        registerReceiver(f4Receiver, intentFilter);
//        barcodeManager.Barcode_Open(this, dataReceived);
//        sendBroadcast(new Intent("ReLoadCom"));
        super.onRestart();
    }


    @Override
    public void onPause() {
        // 若有启动有扫描服务则开启
//        sendBroadcast(new Intent("ReleaseCom"));
        super.onPause();
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Handler_SHOW_RESULT:
                    if (null != codeBuffer) {
                        String codeType = Tools.returnType(codeBuffer);
                        String val = null;
                        if (codeType.equals("default")) {
                            val = new String(codeBuffer);
                        } else {
                            try {
                                val = new String(codeBuffer, codeType);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("码详情：" + val);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            val = val.substring(1, val.length());
//                        }
                        String message = val;
                        barcode = message;
                        barcode = barcode.trim();
                        Log.e("扫码内容", message);
                        beepManager.play();
//                        if (!isRun) {
//                            isRun = true;
                            initCode(barcode);
//                        } else {
//                            play_voice();
//                            Config.showTip(MainActivity.this, "请等待处理结果");
//                            return;
//                        }
                    }
                    break;

                case Handler_Scan:
                    nowTime = System.currentTimeMillis();
                    barcodeManager.Barcode_Stop();
                    // 按键时间不低于200ms
                    if (nowTime - lastTime > 200) {
                        System.out.println("scan(0)");
                        if (null != barcodeManager) {
                            barcodeManager.Barcode_Start();
                        }
                        lastTime = nowTime;
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void initCode(String barcode) {
        webview.loadUrl("javascript:setLocation('" + barcode + "')");
    }

    /**
     * 关闭后记得取消注册接收器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(f4Receiver);
    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


}
