package com.sigmatrix.yhmh5.yhmh5.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * 
 *    
 * 项目名称：Framework   
 * 类名称：GsonUtils   
 * 类描述：Gson解析框架   
 * 创建人：耿卫斌  
 * 创建时间：2015-2-3 下午1:53:44   
 * 修改备注：    
 		showLodingDialog(mContext, "正在请求...");
		RequestParams params=new RequestParams(请求地址);
		params.addParameter("参数名称", "参数");//自适应添加参数（自动识别Get或Post）
		mCancelable=x.http().request(HttpMethod.POST, params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException cex) {
				// TODO Auto-generated method stub
				System.out.println("主动停止网络请求");
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				// TODO Auto-generated method stub
				//失败操作
				System.out.println("网络请求错误返回信息："+ex.getMessage());
				showNetWorkErrorDialog(mContext, new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dissMissDialog();
						//这里可以重新调用请求
					}
				});
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub
				dissMissDialog();
				System.out.println("请求完毕,不管成功失败");
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				//成功
				System.out.println("其他请求结果："+result);
				int code=GsonUtils.code(result, "errorCode");
				String message=GsonUtils.message(result, "message");
				if(code==200){
					//请求成功
					 
				}else{
					//请求失败
					showToast(mContext, message);
				}
			}
		});

		//外层为数组类型
		 	jsonArray = new JSONArray(json);
					List<实体类> mBean=new ArrayList<实体类>();
					for(int i=0;i<jsonArray.length();i++){
						TestBean json2bean = GsonUtils.json2bean(jsonArray.opt(i).toString(), 实体类.class);
						mBean.add(json2bean);
						}
 *
 */
public class GsonUtils {
	public static <T> T json2bean(String result, Class<T> clazz) {

		Gson gson = new Gson();

		T t = gson.fromJson(result, clazz);

		return t;
	}

	public static JsonObject str2JosnObj(String result) {

		return new JsonParser().parse(result).getAsJsonObject();
	}

	public static int code(String result, String key) {

		return str2JosnObj(result).get(key).getAsInt();
	}
	
	public static String message(String result, String key) {
		
		return str2JosnObj(result).get(key).getAsString();
	}
	
	public static Boolean success(String result,String key){
		return str2JosnObj(result).get(key).getAsBoolean();
	}
	
	public static Long getLong(String result,String key){
		return str2JosnObj(result).get(key).getAsLong();
	}
	
	//JsonArray转成集合
	public static <T, clazz> LinkedList<clazz> star2JsonArray(String str,Class<T> clazz){
		Type listType = (Type) new TypeToken<LinkedList<clazz>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<clazz> users = gson.fromJson(str, listType); 
		return users;
	}
}
