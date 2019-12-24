/**
 * 
 * @author heqing1
 * @version 2012-10-22
 */
package com.sigmatrix.yhmh5.yhmh5.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {
	public static final String DEFAULT_SHARED_PREF_NAME = "DefaultSharedPrefName";
	private static SharedPrefUtil instance;

	private SharedPrefUtil() {

	}

	public static synchronized SharedPrefUtil getInstance() {
		if (instance == null) {
			instance = new SharedPrefUtil();
		}
		return instance;
	}

	/**
	 * 从指定fileName的sharedPref中读取key对应的字符串值
	 * 
	 * @param fileName
	 * @param key
	 * @param defaultValue
	 * @return string
	 */
	public String getString(Context context, String fileName, String key,
			String defaultValue) {
		SharedPreferences shareddata = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return shareddata.getString(key, defaultValue);
	}

	/**
	 * key-value键值对写入fileName指定的sharedPref中
	 * 
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putString(Context context, String fileName, String key,
			String value) {
		SharedPreferences.Editor dataEditor = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE).edit();
		dataEditor.putString(key, value);
		return dataEditor.commit();
	}

	/**
	 * 从指定fileName的sharedPref中读取key对应的int值
	 * 
	 * @param fileName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(Context context, String fileName, String key,
			int defaultValue) {
		SharedPreferences sharedata = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return sharedata.getInt(key, defaultValue);
	}

	/**
	 * key-value键值对写入fileName指定的sharedPref中
	 * 
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putInt(Context context, String fileName, String key,
			int value) {
		SharedPreferences.Editor dataEditor = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE).edit();
		dataEditor.putInt(key, value);
		return dataEditor.commit();
	}

	/**
	 * 从指定fileName的sharedPref中读取key对应的boolean值
	 * 
	 * @param fileName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(Context context, String fileName, String key,
			boolean defaultValue) {
		SharedPreferences sharedata = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return sharedata.getBoolean(key, defaultValue);
	}

	/**
	 * key-value键值对写入fileName指定的sharedPref中
	 * 
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putBoolean(Context context, String fileName, String key,
			Boolean value) {
		SharedPreferences.Editor dataEditor = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE).edit();
		dataEditor.putBoolean(key, value);
		return dataEditor.commit();
	}

	/**
	 * 从指定fileName的sharedPref中读取key对应的long值
	 * 
	 * @param fileName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getLong(Context context, String fileName, String key,
			long defaultValue) {
		SharedPreferences sharedata = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
		return sharedata.getLong(key, defaultValue);
	}

	/**
	 * key-value键值对写入fileName指定的sharedPref中
	 * 
	 * @param fileName
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putLong(Context context, String fileName, String key,
			long value) {
		SharedPreferences.Editor dataEditor = context.getSharedPreferences(
				fileName, Context.MODE_PRIVATE).edit();
		dataEditor.putLong(key, value);
		return dataEditor.commit();
	}

	/**
	 * 从默认的sharedPref中读取key对应的string值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(Context context, String key, String defaultValue) {
		return getString(context, DEFAULT_SHARED_PREF_NAME, key, defaultValue);
	}

	/**
	 * key-value键值对写入fileName指定的sharedPref中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putString(Context context, String key, String value) {
		return putString(context, DEFAULT_SHARED_PREF_NAME, key, value);
	}

	/**
	 * 从默认的sharedPref中读取key对应的int值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(Context context, String key, int defaultValue) {
		return getInt(context, DEFAULT_SHARED_PREF_NAME, key, defaultValue);
	}

	/**
	 * key-value键值对写入默认sharedPref中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putInt(Context context, String key, int value) {
		return putInt(context, DEFAULT_SHARED_PREF_NAME, key, value);
	}

	/**
	 * 从默认的sharedPref中读取key对应的boolean值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(Context context, String key, boolean defaultValue) {
		return getBoolean(context, DEFAULT_SHARED_PREF_NAME, key, defaultValue);
	}

	/**
	 * key-value键值对写入默认sharedPref中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putBoolean(Context context, String key, Boolean value) {
		return putBoolean(context, DEFAULT_SHARED_PREF_NAME, key, value);
	}

	/**
	 * 从默认的sharedPref中读取key对应的long值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getLong(Context context, String key, long defaultValue) {
		return getLong(context, DEFAULT_SHARED_PREF_NAME, key, defaultValue);
	}

	/**
	 * key-value键值对写入默认sharedPref中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putLong(Context context, String key, long value) {
		return putLong(context, DEFAULT_SHARED_PREF_NAME, key, value);
	}

}
