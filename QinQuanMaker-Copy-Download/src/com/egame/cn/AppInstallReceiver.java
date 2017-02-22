package com.egame.cn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppInstallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null || intent.getAction() == null) {
			return;
		}
	//	PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {//安装
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("efun", "安装成功");
         
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {//卸载
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("efun", "卸载成功");
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {//更新替换
            String packageName = intent.getData().getSchemeSpecificPart();
            Log.d("efun", "替换成功");
        }

	}

}
