package com.egame.cn;

import java.io.File;

import com.efun.core.tools.ApkUtil;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.download.EfunDownLoader;
import com.efun.download.listener.EfunDownLoadListener;
import com.efun.service.FileService;
import com.gaend.lda.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownLoadCopyAppActivity extends Activity {

	private static final String PACKAGENAME = "com.sea.semda.sm";

	private static final String className = "org.cocos2dx.lua.AppActivity";

	private static final String remoteApkPath = "http://mdzzen.download.vsplay.com/apk/ServantBattle.apk";

	static EfunDownLoader downLoader;

	ProgressBar bar;
	
	int fileSize_m = 0;
	int downLoadedSize_m = 0;
	
	AppInstallReceiver ar;
	
	public String obbPath = "";

	private String obbNewFile = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.copydownload);
		Log.d("efun", "onCreate");
		bar = (ProgressBar) this.findViewById(R.id.progressBar);
		
		
	//	startApp(MainActivity.this);
				
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("efun", "onResume");
		
		if (!ApkUtil.isInstallApp(DownLoadCopyAppActivity.this, PACKAGENAME)) {
			Log.d("efun", "not app");
			copyObbFile(this);
		}else {
			Log.d("efun", "has app");
			startApp(DownLoadCopyAppActivity.this);
		}
		
		if (bar != null) {
			bar.setMax(fileSize_m);
			bar.setProgress(downLoadedSize_m);
		}

		
	}

	private void copyObbFile(Activity activity) {
		obbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + activity.getPackageName() + 
				"/main." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".obb";
		
		obbNewFile  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getPackageName() + "/edown/" +
		
		"fn." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".apk";
		
		Log.d("efun", "obbPath:" + obbPath);
		Log.d("efun", "obbNewFile:" + obbNewFile);
		File src = new File(obbPath);
	
		new CopyFileTask(obbPath, obbNewFile){
			
			protected void onPostExecute(String result) {
				
				if ("1".equals(result)) {//复制成功
					bar.setMax(100);
					bar.setProgress(100);
					//ApkUtil.installApk(MainActivity.this, obbNewFile);
					installApp(DownLoadCopyAppActivity.this, obbNewFile);
				}else{
					downApp(DownLoadCopyAppActivity.this);
				}
				
			};
			
			protected void onProgressUpdate(Integer[] values) {
				if (bar != null) {
					bar.setMax(values[0]);
					bar.setProgress(values[1]);
				}
			};
			
		}.asyncExcute();

	}


	private void startApp(Activity activity) {
		
		try {
			Intent i = new Intent("android.intent.action.MAIN");
			i.setPackage(PACKAGENAME);
			i.setClassName(PACKAGENAME, className);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			activity.startActivity(i);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "open game error", Toast.LENGTH_SHORT).show();
		}
	}



	protected void downApp(Context context) {
		if (downLoader == null) {
			downLoader = new EfunDownLoader();
		}
		initDownLoad(context);
		downLoader.startDownLoad(context);
	}
	
	protected void installApp(Context context, String apkpath){
//		saveDir + File.separator + apk_file_name
		try {
			ar = new AppInstallReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addDataScheme("package");
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			filter.addAction(Intent.ACTION_PACKAGE_ADDED);
			filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
			registerReceiver(ar, filter );
			ApkUtil.installApk(context, apkpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initDownLoad(final Context context){
		final String saveDir = Environment.getExternalStorageDirectory().getPath() + "/edown";
		final String apk_file_name = FileService.getFileName(remoteApkPath);
		
		// 设置下载的路径
		downLoader.setDownUrl(remoteApkPath);
		// 设置下载后保全的路径
		downLoader.setSaveDirectoryPath(saveDir);
		downLoader.setDownLoadListener(new EfunDownLoadListener() {
			
			@Override
			public void update(int fileSize, int downLoadedSize) {
				Log.i("down", "fileSize:" + fileSize + " downLoadedSize:" + downLoadedSize);
				fileSize_m = fileSize;
				downLoadedSize_m = downLoadedSize;
				if (bar != null) {
					bar.setMax(fileSize_m);
					bar.setProgress(downLoadedSize_m);
				}
			}
			
			@Override
			public void finish() {
				Log.i("efun", "finish download");
				
				if (bar != null) {
					bar.setMax(100);
					bar.setProgress(100);
				}
				
				installApp(DownLoadCopyAppActivity.this, saveDir + File.separator + apk_file_name);
			}

			@Override
			public void reachFile(int fileSize) {
				Log.i("efun", "fileSize:" + fileSize);
				if (bar != null) {
					
					bar.setMax(fileSize_m);
				}
			}


			@Override
			public void unFinish() {
				Log.i("efun", "unfinish download");
				//Toast.makeText(getApplicationContext(), "download failed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void lackStorageSpace() {
				
			}

			@Override
			public void remoteFileNotFind() {
				
			}
			
			
		});
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.d("efun", "onDestroy");
		if (downLoader != null) {
			downLoader.pauseDownLoad(this);
		}
		if (ar != null) {
			this.unregisterReceiver(ar);
		}
	}
}