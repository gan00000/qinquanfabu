package com.egame.cn.ads;

import com.efun.ads.callback.S2SListener;
import com.efun.core.tools.EfunLogUtil;
import com.egame.cn.ads.AdvertUtil.AdsAppflyer;
import com.egame.cn.ads.AdvertUtil.AdsFacebook;

import android.content.Context;
import android.content.Intent;

public class S2SAdvert implements S2SListener {

	@Override
	public void s2sRunEvery(Context context,Intent intent, int arg1) {
		
		//写入每次都运行的广告代码
			if (EfunHelper.addAppflyer(context)) {
				AdsAppflyer.appFlyer(context);
			}
			
//			if (EfunHelper.addChartBoost(context)) {
//				AdsChartboost.init(context);
//			}	
			
			if (EfunHelper.addAdWords(context)) {
				AdvertUtil.adsAdWords(context);
			}
			
			if (EfunHelper.addFacebook(context)) {
				AdsFacebook.facebook(context);
			}

		
	}

	@Override
	public void s2sonDestroy(Context context) {
		//写入需要销毁对象的广告代码
//			if (EfunHelper.addChartBoost(context)) {
//				AdsChartboost.destory();
//			}
//			if (EfunHelper.addInmobi(context)) {
//				AdsInmobi.inmobiDestroy(context);
//			}
		
	}

	@Override
	public void s2sonStopServic(Context context,Intent intent) {
		//写入停止服务前调用的广告代码
//			if (EfunHelper.addChartBoost(context)) {
//				AdsChartboost.stopService();
//			}
		
	}

	@Override
	public void s2sResultRunOnlyOne(Context context) {
		// 写入只运行一次的广告代码

		EfunLogUtil.logI("start ads main");

	}

	@Override
	public void onlyOnceForADS(Context context, Intent arg1, int arg2) {
		// TODO Auto-generated method stub
	}
	
}
