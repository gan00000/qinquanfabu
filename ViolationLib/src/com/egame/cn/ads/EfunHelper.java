package com.egame.cn.ads;

import com.efun.core.tools.EfunResourceUtil;

import android.content.Context;

public class EfunHelper {

	public static boolean addInmobi(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_INMOBI");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addChartBoost(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_CHARTBOOST");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addVpon(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_VPON");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addKuADCPA(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_KUADCPA");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addAppma(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_APPMAADID");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addFA(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_FA");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addFacebook(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_FACEBOOK");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addAppflyer(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_APPFLYER");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addEfunPush(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "EFUN_PUSH");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static boolean addEfunAlarmPush(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "EFUN_ALARMPUSH");
		if ("true".equals(inmobi)) {
			return true;
		}
		return false;
	}

	public static boolean addEfunPlatform(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "Efun_Google_Platform");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addMM(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_MM");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean addAdWords(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "ADS_ADWORDS");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}

	public static boolean isPlatformLandscape(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "Efun_Platform_Landscape");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static boolean isNativeBilling(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "Efun_Native_Billing");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static boolean isCrashCatch(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "Efun_Crash_Catch");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static boolean isReqPermission_Ads(Context context) {
		String inmobi = EfunResourceUtil.findStringByName(context, "Efun_ReqPermission_Ads");
		if (inmobi.equals("true")) {
			return true;
		}
		return false;
	}
	

}
