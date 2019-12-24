package com.sigmatrix.yhmh5.yhmh5.Utils;/**/

/**
 * Created by dawen on 2016/12/28.
 */

public class AppUrl {
    /**********************************域名********	**************************/
//	public static String URL="http://tdyh-numberonewap-dev.tdyh.cn";//开发服务器
    	public static String URL="http://tdyh-numberonewap-qa.tdyh.cn";//测试服务器
//    public static String URL="http://tdyh-NumberOneWAP.tdyh.cn";//正式

    /**********************************登录账号**********************************/
    public static String LOGIN=URL+"/NumberOneWAP/login/login";

    /**********************************我的信息**********************************/
    public static String MYINFO=URL+"/NumberOneWAP/login/myInfo";

    /**********************************库存**********************************/
    public static String STOCK=URL+"/NumberOneWAP/main/stock";

    /**********************************关于**********************************/
    public static String ABOUT=URL+"/NumberOneWAP/main/about";

    /**********************************登出**********************************/
    public static String LOGOUT=URL+"/NumberOneWAP/login/logout";

    /**********************************修改密码 **********************************/
    public static String UPDATEPWD=URL+"/NumberOneWAP/login/updatePwd";

    /**********************************获取验证码 **********************************/
    public static String GET_VERIFICATIONCODE=URL+"/auth/getVerificationCode";
}
