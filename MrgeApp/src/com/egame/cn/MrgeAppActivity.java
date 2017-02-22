package com.egame.cn;

import java.io.File;

import com.efun.core.tools.EfunLocalUtil;
import com.slzhu.hw.R;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MrgeAppActivity extends BaseActivity {
	
	private String mrgeNewFile = "";
	
	File newFile;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d("efun", "onCreate");
		
		String srcpath = this.getApplicationInfo().sourceDir;
		Log.d("efun", "apk srcpath:" + srcpath);
		File srcApk = new File(srcpath);
		
		File obbFile = getObbFile(this);
		
		mrgeNewFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + this.getPackageName() + 
		"." + EfunLocalUtil.getVersionCode(this) + ".mrge.apk";
		newFile = new File(mrgeNewFile);
		
		if (!srcApk.exists()) {
			Toast.makeText(getApplicationContext(),"读取不到原始apk", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!obbFile.exists()) {
			Toast.makeText(getApplicationContext(),"找不到obb patch", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		if (obbFile.exists() && srcApk.exists() && srcApk.length() < obbFile.length()) {
		
			new MrgePatchTask(this, srcApk, newFile, obbFile){
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					if ("0".equals(result)) {
						installApp(MrgeAppActivity.this, newFile.getAbsolutePath());
					}
				};
			}.asyncExcute();
		}
				
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("efun", "onResume");
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}