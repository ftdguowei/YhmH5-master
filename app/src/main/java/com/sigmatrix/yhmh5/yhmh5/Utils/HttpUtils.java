package com.sigmatrix.yhmh5.yhmh5.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class HttpUtils {
	static String strJson = "";//提交Json数据时用于判断
	//https证书
	public static SSLContext s_sSLContext = null;
	public interface OnResponseListener {
		void onFinished();
		void onCancelled();
		void onError();
		void onSuccess(String result);
		void onFailure(String result);
	}

	/**
	 * 提交Json数据
	 * @param mContext
	 * @param url
	 * @param json
	 * @param listener
	 */
	public static void httpPostJsonArr(final Context mContext,String url,String json,final OnResponseListener listener) {
		strJson = json;
		Log.d("请求json数组内容", strJson);
		httpRequestByPost(mContext,url,new HashMap<String, String>(),listener);
		strJson = "";
	}
	
	/**
	 * 提交普通数据
	 * @param mContext
	 * @param url
	 * @param paramsData
	 * @param listener
	 */
	public static void httpRequestByPost(final Context mContext,String url,HashMap<String, String> paramsData,final OnResponseListener listener) {
		/* 判断https证书是否成功验证 */
		/*
		SSLContext sslContext = getSSLContext(mContext);
		if(null == sslContext){
			if (BuildConfig.DEBUG) ToastUtil.toast(mContext, "https证书验证：没有Https证书");
			return ;
		}
		*/
//		BaseActivity.showLodingDialog(mContext);
		RequestParams params = new RequestParams(url);
		//添加请求头
		params.setConnectTimeout(10000);//设置请求超时时间
		params.addHeader("Content-Type", "Application/json");
		params.addHeader("Client-Type", "Android");

		Iterator<Entry<String, String>> iter = paramsData.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Entry entry = (Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			params.addParameter(key, val);// 自适应添加参数（自动识别Get或Post）
		}

		String postdata = params.toJSONString();
		Log.d("请求","请求url："+ url);
		Log.d("请求","请求参数："+ postdata);
		params.setBodyContent(params.toJSONString());//将参数转换为JSON串视服务器规则来注释/开放
//		params.setSslSocketFactory(s_sSLContext.getSocketFactory());//如果使用Https视服务器规则来注释/开放
		if (!TextUtils.isEmpty(strJson)) {
			//提交Json数据
			params.setBodyContent(strJson);
			params.setAsJsonContent(true);
		}
		x.http().request(HttpMethod.POST, params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException cex) {
				if(listener!=null){
					listener.onCancelled();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.e("请求错误", ex.toString());
				if(listener!=null){
					listener.onError();
				}
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onFinished();
				}
			}

			@Override
			public void onSuccess(String result) {
				Log.e("返回", "接口请求成功："+result.toString());
				String code = GsonUtils.message(result, "resultState");
				String message = GsonUtils.message(result, "resultMsg");
				if("0000".equals(code)){
					if(listener!=null){
						listener.onSuccess(result);
					}
				}else{
					ToastUtil.toast(mContext, message);
					if(listener!=null){
						listener.onFailure(result);
					}
				}
			}

		});
	}

	/**
	 * 提交普通数据
	 * @param mContext
	 * @param url
	 * @param paramsData
	 * @param listener
	 */
	public static void httpRequestByPostBitmap(final Context mContext, String url,HashMap<String,String>paramsData, HashMap<String, Bitmap> bitmapData, final OnResponseListener listener) {
		RequestParams params = new RequestParams(url);
		//添加请求头
		params.setConnectTimeout(10000);//设置请求超时时间
		params.addHeader("Content-Type", "multipart/form-data");
		params.addHeader("Client-Type", "Android");


		Iterator<Entry<String, String>> iter = paramsData.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Entry entry = (Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			params.addParameter(key, val);// 自适应添加参数（自动识别Get或Post）
		}

		Iterator<Entry<String, Bitmap>> iter1 = bitmapData.entrySet().iterator();
		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			Entry entry = (Entry) iter1.next();
			String key = (String) entry.getKey();
			Bitmap val = (Bitmap) entry.getValue();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			val.compress(Bitmap.CompressFormat.PNG, 100, baos);
			InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			params.addParameter(key, isBm);// 自适应添加参数（自动识别Get或Post）
		}
		String postdata = params.toJSONString();
		Log.d("请求","请求url："+ url);
		Log.d("请求","请求参数："+ postdata);
		params.setBodyContent(params.toJSONString());//将参数转换为JSON串视服务器规则来注释/开放
//		params.setSslSocketFactory(s_sSLContext.getSocketFactory());//如果使用Https视服务器规则来注释/开放
		if (!TextUtils.isEmpty(strJson)) {
			//提交Json数据
			params.setBodyContent(strJson);
			params.setAsJsonContent(true);
		}
		x.http().request(HttpMethod.POST, params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException cex) {
				if(listener!=null){
					listener.onCancelled();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.e("请求错误", ex.toString());
				if(listener!=null){
					listener.onError();
				}
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.onFinished();
				}
			}

			@Override
			public void onSuccess(String result) {
				Log.e("返回", "接口请求成功："+result.toString());
				String code = GsonUtils.message(result, "resultState");
				String message = GsonUtils.message(result, "resultMsg");
				if("0000".equals(code)){
					if(listener!=null){
						listener.onSuccess(result);
					}
				}else{
					ToastUtil.toast(mContext, message);
					if(listener!=null){
						listener.onFailure(result);
					}
				}
			}

		});
	}

	/**
	 * 获取Https的证书
	 * @param context Activity（fragment）的上下文
	 * @return SSL的上下文对象
	 */
	public static SSLContext getSSLContext(Context context) {
		if (null != s_sSLContext) {
			return s_sSLContext;
		}
		//以下代码来自百度 参见http://www.tuicool.com/articles/vmUZf2
		CertificateFactory certificateFactory = null;
		InputStream inputStream = null;
		KeyStore keystore = null;
		String tmfAlgorithm = null;
		TrustManagerFactory trustManagerFactory = null;
		try {
			certificateFactory = CertificateFactory.getInstance("X.509");
			inputStream = context.getAssets().open("leichi.crt");//这里导入SSL证书文件
			//        inputStream = context.getAssets().open("51p2b_server_bs.pem");//这里导入SSL证书文件
			Certificate ca = certificateFactory.generateCertificate(inputStream);
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(null, null);
			keystore.setCertificateEntry("ca", ca);
			tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
			trustManagerFactory.init(keystore);
			// Create an SSLContext that uses our TrustManager
			s_sSLContext = SSLContext.getInstance("TLS");
			s_sSLContext.init(null, trustManagerFactory.getTrustManagers(), null);
			return s_sSLContext;
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
