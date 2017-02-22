package com.egame.cn;

import com.efun.core.tools.EfunResourceUtil;

import android.content.Context;

public class ResHelper {

	public static String getInstallAppPackageName(Context context){
		return EfunResourceUtil.findStringByName(context, "install_package_name");
	}
	
	public static String getInstallAppPackageMainAcitivityName(Context context){
		return EfunResourceUtil.findStringByName(context, "install_package_main_activity");
	}
	
	public static String getInstallAppDownloadUrl(Context context){
		return EfunResourceUtil.findStringByName(context, "install_apk_download_url");
	}
	
}
