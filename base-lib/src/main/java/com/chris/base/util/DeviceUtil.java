package com.chris.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 通过IMEI和MAC经过MD5加密，得到的基本算唯一的Android设备唯一表示码
 * 用于激活量的统计功能
 * Created by Christain on 2015/12/15.
 */
public class DeviceUtil {

    private static String uuid;             //设备ID
    private static String imsi;             //运营商
    private static String strNetworkType;   //网络类型
    private static String versionName;      //版本名称
    private static String phoneModel;       //手机型号
    private static String phoneMfr;         //手机厂商

    /**
     * 设备唯一标识
     */
    public static String getUUID(Context context) {
        if (!TextUtils.isEmpty(uuid)) {
            return uuid;
        }
        //获取imei号
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = TelephonyMgr.getDeviceId();
        //获取mac地址
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String mac = wm.getConnectionInfo().getMacAddress();

        //重新组合的deviceId
        String deviceId = imei + mac;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return UUID.randomUUID().toString();
        }
        m.update(deviceId.getBytes(), 0, deviceId.length());
        byte p_md5Data[] = m.digest();
        String uniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            if (b <= 0xF)
                uniqueID += "0";
            uniqueID += Integer.toHexString(b);
        }
        uniqueID = uniqueID.toUpperCase();
        if (uniqueID != null) {
            uuid = uniqueID;
            return uniqueID;
        }
        return UUID.randomUUID().toString();
    }

    /**
     * 获取用户当前使用网络类型
     */
    public static String getNetType(Context context) {
        if (!TextUtils.isEmpty(strNetworkType)) {
            return strNetworkType;
        }
        ConnectivityManager cmg = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmg.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI网络";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                strNetworkType = "2G/3G/4G";
            }else {
                strNetworkType = "未知网络";
            }
        }else{
            strNetworkType = "离线网络";
        }
        return strNetworkType;
    }

    /**
     * 获取用户当前使用的运营商类型
     */
    public static String getIMSI(Context context) {
        if (!TextUtils.isEmpty(imsi)) {
            return imsi;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi= telephonyManager.getSubscriberId();
            imsi = imsi.substring(imsi.length()-2,imsi.length());
        }catch (Exception e ) {
            imsi = null;
        }
        return ((imsi == null)?"":imsi);
    }

    /**
     * 版本名字
     */
    public static String getVersionName(Context context) {
        if (!TextUtils.isEmpty(versionName)) {
            return versionName;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionName = pInfo.versionName;
            return (versionName != null) ? versionName : "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 手机型号
     */
    public static String getPhoneModel() {
        if (!TextUtils.isEmpty(phoneModel)) {
            return phoneModel;
        }
        phoneModel = Build.MODEL;
        return (phoneModel != null) ? phoneModel : "";
    }

    /**
     * 手机厂商
     */
    public static String getPhoneManufacturer() {
        if (!TextUtils.isEmpty(phoneMfr)) {
            return phoneMfr;
        }
        phoneMfr = Build.MANUFACTURER;
        return (phoneMfr != null) ? phoneMfr : "杂牌";
    }
}
