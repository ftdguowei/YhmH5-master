package com.sigmatrix.yhmh5.yhmh5.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.sigmatrix.yhmh5.yhmh5.app.MyApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;


/**
 * Created by dawen on 2017/3/21.
 */

public class AndroidUtils {
    public static String getMacAddress() throws SocketException {
        String address = null;
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        Log.d("TEST_BUG", " interfaceName = " + interfaces );
        while (interfaces.hasMoreElements()) {
            NetworkInterface netWork = interfaces.nextElement();
            // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
            byte[] by = netWork.getHardwareAddress();
            if (by == null || by.length == 0) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : by) {
                builder.append(String.format("%02X:", b));
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            String mac = builder.toString();
            Log.d("TEST_BUG", "interfaceName="+netWork.getName()+", mac="+mac);
            // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
            if (netWork.getName().equals("wlan0")) {
                Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", mac="+mac);
                address = mac;
            }
        }
        if(address==null||address.isEmpty()){
            address =getMacAddress2();
        }
        if(address==null||address.isEmpty()){
            address =getLocalIpAddress();
        }
        return address;

    }

    public static String getMacAddress2(){
        String mac_s= "";
        try {
            byte[] mac;
            NetworkInterface ne =
                    NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac_s;
    }
    public static  String byte2hex(byte[] b)
    {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if(stmp.length() == 1){
                hs = hs.append("0").append(stmp+":");
            }else {
                hs = hs.append(stmp+":");
            }
        }
        stmp = hs.toString();
        stmp = stmp.substring(0,stmp.length()-1);
        return stmp;
    }

    public static String getDvID() {
        WifiManager wm = (WifiManager) MyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return m_szWLANMAC;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e(LOG_TAG, ex.toString());
        }
        return "";
    }


    public static String getVersionName() throws Exception  {
        PackageManager packageManager = MyApplication.getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(MyApplication.getContext().getPackageName(),0);
        String versionName = packInfo.versionName;
        return versionName;
    }

    public static int getAndroidVersion(){
        return android.os.Build.VERSION.SDK_INT;
    }

    public static String getSisTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
