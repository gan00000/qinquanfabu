package com.egame.cn.ads;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AppsFlyerLib;
import com.efun.core.res.EfunResConfiguration;
import com.efun.core.tools.EfunLogUtil;
import com.efun.core.tools.EfunResourceUtil;
import com.facebook.efun.EfunFacebookProxy;
import com.google.ads.conversiontracking.AdWordsConversionReporter;

public class AdvertUtil {

	private static final String ADSTAG = "efunads";

	/**
	 * facebook 使用方法：在S2SAdvert中s2sRunOnlyOne下调用facebook
	 */
	public static class AdsFacebook {
		public static void facebook(Context context) {
			Log.i(ADSTAG, "facebook广告启动");
			EfunFacebookProxy.activateApp(context, EfunResConfiguration.getApplicationId(context));
			EfunLogUtil.logI(ADSTAG, "AdsFacebook + appid:" + EfunResConfiguration.getApplicationId(context));
		}
	}


	/**
	 * Appflyer 使用方法：在S2SAdvert中s2sRunOnlyOne下调用appFlyer
	 */
	public static class AdsAppflyer {
		public static void appFlyer(Context context) {
			Log.i(ADSTAG,"Ads appFlyer");
			EfunLogUtil.logI(ADSTAG, "Ads appFlyer + ADS_APPFLYER_DEVKEY:"
					+ EfunResourceUtil.findStringByName(context, "ADS_APPFLYER_DEVKEY"));
			AppsFlyerLib.setAppsFlyerKey(EfunResourceUtil.findStringByName(context, "ADS_APPFLYER_DEVKEY"));
			AppsFlyerLib.setCustomerUserId("My-Unique-ID");
			AppsFlyerLib.setCurrencyCode("USD");
			// AppsFlyerLib.setAppId(id);
			AppsFlyerLib.sendTracking(context.getApplicationContext());
			// AppsFlyerLib.sendTracking(context);
		}
	}


	/**
	 * <p>
	 * Description: adwords激活
	 * </p>
	 * 
	 * @param context
	 * @date 2015年6月12日
	 */
	public static void adsAdWords(Context context) {
		Log.i(ADSTAG,"adsAdWords启动");
		String conversionId = EfunResourceUtil.findStringByName(context, "ADS_ADWORDS_CONVERSIONID");
		String label = EfunResourceUtil.findStringByName(context, "ADS_ADWORDS_LABEL");
		String value = EfunResourceUtil.findStringByName(context, "ADS_ADWORDS_VALUE");
		String isRepeatableString = EfunResourceUtil.findStringByName(context, "ADS_ADWORDS_ISREPEATABLE");
		boolean isRepeatable = false;
		if (!TextUtils.isEmpty(isRepeatableString) && isRepeatableString.equals("true")) {
			isRepeatable = true;
		}
		EfunLogUtil.logI(ADSTAG, "adsAdWords  + ADS_ADWORDS_CONVERSIONID:" + conversionId + "  ADS_ADWORDS_LABEL:"
				+ label + "  ADS_ADWORDS_VALUE:"+value+"  ADS_ADWORDS_ISREPEATABLE:"+isRepeatableString);
		AdWordsConversionReporter.reportWithConversionId(context.getApplicationContext(), conversionId, label, value,
				isRepeatable);
	}


}
