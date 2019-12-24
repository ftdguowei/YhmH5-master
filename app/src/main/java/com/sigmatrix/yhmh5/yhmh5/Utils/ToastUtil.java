package com.sigmatrix.yhmh5.yhmh5.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sigmatrix.yhmh5.yhmh5.R;

/**
 * 
 *    
 * 项目名称：Framework   
 * 类名称：ToastUtil   
 * 类描述：自定义Toast   
 * 创建人：耿卫斌  
 * 创建时间：2015-1-16 下午2:42:13   
 * 修改备注：    
 *
 */
public class ToastUtil {
	private static  Toast toast=null;
	/**自定义Toast
	 * 短时间显示Toast 作用:不重复弹出Toast,如果当前有toast正在显示，则先取消
	 * 
	 * @param info
	 *            显示的内容
	 */
	@SuppressLint("InflateParams") 
	public static void toast(Context context,String info) {
		if (toast != null) {
			toast.cancel();
			toast=null;
		}
		View view = LayoutInflater.from(context).inflate(R.layout.toast_msg,
				null);
		TextView message = (TextView) view.findViewById(R.id.message_ToastUtil);
		message.setText(info);
		view.setBackgroundResource(R.drawable.toastutil_custome);
		toast = new Toast(context);
		toast.setView(view);
		toast.show();
	}
	/**
	 * 系统默认Toast
	 * @param context
	 * @param info
	 */
	public static void SystemToast(Context context,String info) {
		if (toast != null) {
			toast.cancel();
			toast=null;
		}
		toast = new Toast(context);
		toast= Toast.makeText(context, info, Toast.LENGTH_SHORT);
		toast.show();
	}
}
