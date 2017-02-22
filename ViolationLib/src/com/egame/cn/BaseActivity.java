package com.egame.cn;

import java.io.File;

import com.efun.ads.call.EfunAdsPlatform;
import com.efun.core.tools.ApkUtil;
import com.efun.core.tools.EfunLocalUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	protected AppInstallReceiver ar;
	
	protected String obbFilePath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ar = new AppInstallReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addDataScheme("package");
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
		registerReceiver(ar, filter);
		
		EfunAdsPlatform.initEfunAdsS2S(this);
		
	}
	
	
	protected String getObbFilePath(Context context) {
		obbFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + this.getPackageName() + 
				"/main." + EfunLocalUtil.getVersionCode(this) + "." + this.getPackageName() + ".obb";
		return obbFilePath;
	}
	
	protected File getObbFile(Context context) {
		String path = getObbFilePath(this);
		if (!TextUtils.isEmpty(path)) {
			return new File(path);
		}
		return null;
	}
	
	
	protected void installApp(Context context, String apkpath){
		try {
			ApkUtil.installApk(context, apkpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void startApp(Activity activity){
		String packageName = ResHelper.getInstallAppPackageName(activity);
		String activityName = ResHelper.getInstallAppPackageMainAcitivityName(activity);
		if (TextUtils.isEmpty(packageName) || TextUtils.isEmpty(activityName)) {
			return;
		}
		startApp(activity, packageName, activityName);
	}
	
	
	protected void startApp(Activity activity,String packageName, String className) {
		
		try {
			Intent i = new Intent("android.intent.action.MAIN");
			i.setPackage(packageName);
			i.setClassName(packageName, className);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "open game error", Toast.LENGTH_SHORT).show();
		}
	}
	

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (ar != null) {
			this.unregisterReceiver(ar);
		}
		Log.d("efun", "onDestroy");
	}

}
