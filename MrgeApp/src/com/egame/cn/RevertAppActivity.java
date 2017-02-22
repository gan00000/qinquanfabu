package com.egame.cn;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.efun.core.task.EfunRequestAsyncTask;
import com.efun.core.tools.ApkUtil;
import com.egame.cn.view.TextProgressBar;
import com.slzhu.hw.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class RevertAppActivity extends BaseActivity {

	TextProgressBar bar;
	
	public String obbPath = "";

	
	String renameApk = "";

	private String remoteApkPath;
	
	private Handler handle;
	
	ProgressDialog pb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revert_layout);
		Log.d("efun", "onCreate");

		obbPath = getObbFilePath(this);
		renameApk = obbPath + ".apk";
		remoteApkPath = ResHelper.getInstallAppDownloadUrl(this);
		handle = new Handler();
		pb = new ProgressDialog(this);
		pb.setMessage("Loading");
		pb.setIndeterminate(true);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("efun", "onResume");
		if (handle != null) {
			pb.show();
			handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if (pb != null) {
						pb.dismiss();
					}
					doMM();
				}
			}, 1 * 1000);
		}else{
			doMM();
		}
		
	}
	
	protected void doMM() {
		new EfunRequestAsyncTask() {

			@Override
			protected String doInBackground(String... params) {
				doTask();
				return null;
			}
		}.asyncExcute();

	}

	protected void doTask() {
		if (!TextUtils.isEmpty(ResHelper.getInstallAppPackageName(this))) {//包名不一样的情形
			
			if (ApkUtil.isInstallApp(RevertAppActivity.this, ResHelper.getInstallAppPackageName(this))) {
				startApp(RevertAppActivity.this);//包名不一样才会启动
			
			}else{
				
				if (revertGameApk(this) && !TextUtils.isEmpty(renameApk)) {
					installApp(this, renameApk);
				}
			}
			
		}else{//游戏包名跟本包名一致
			
			if (revertGameApk(this) && !TextUtils.isEmpty(renameApk)) {
				installApp(this, renameApk);
			}
		}
		
	}
	
	

	private boolean revertGameApk(Activity activity){
		try {
			File obbFile = new File(obbPath);
			File apkFile = new File(renameApk);
			if (apkFile.exists()) {
				return true;
			}
			if (obbFile.exists()) {
				obbFile.renameTo(apkFile);

				RandomAccessFile r = new RandomAccessFile(apkFile, "rw");
				r.seek(0); //指针设置在a后.
				//r.write(new byte[] { 80 });
				r.write(new byte[]{80,75,03});
				r.close();
				return true;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.d("efun", "onDestroy");
		
		if (pb != null && pb.isShowing()) {
			pb.dismiss();
			pb = null;
		}
	}

}