package com.egame.cn;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.efun.core.tools.ApkUtil;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.download.EfunDownLoader;
import com.efun.download.listener.EfunDownLoadListener;
import com.efun.service.FileService;
import com.egame.cn.view.TextProgressBar;
import com.ploo.nma.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class RevertAppActivity extends Activity {

	private static final String PACKAGENAME = "com.sea.semda.sm";

	private static final String className = "org.cocos2dx.lua.AppActivity";

	private static final String remoteApkPath = "http://mdzzen.download.vsplay.com/apk/ServantBattle.apk";

	static EfunDownLoader downLoader;

	TextProgressBar bar;
	
	int fileSize_m = 0;
	int downLoadedSize_m = 0;
	
	AppInstallReceiver ar;
	
	public String obbPath = "";

	private String copyobbdstfile = "";
	
	String renameApk = "";
	
	//Button download_game_btn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revert_layout);
		Log.d("efun", "onCreate");
		bar = (TextProgressBar) this.findViewById(R.id.progressBar);
	/*	download_game_btn = (Button) this.findViewById(R.id.download_game);
		
		download_game_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				copyObbFileOrDownloadApp(MainActivity.this);
			//	Toast.makeText(getApplicationContext(),EfunLocalUtil.getVersionCode(MainActivity.this), Toast.LENGTH_SHORT).show();
			}
		});
				*/
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("efun", "onResume");
		
		if (!ApkUtil.isInstallApp(RevertAppActivity.this, PACKAGENAME)) {
			Log.d("efun", "not app");
		//	download_game_btn.setVisibility(View.VISIBLE);
			if (revertGameApk(this) && !TextUtils.isEmpty(renameApk)) {
				installApp(this, renameApk);
			}else{
				bar.setVisibility(View.VISIBLE);
				//copyObbFileOrDownloadApp(MainActivity.this);
				downApp(RevertAppActivity.this);
			}
		}else {
			Log.d("efun", "has app");
			startApp(RevertAppActivity.this);
		}
		
		if (bar != null) {
			bar.setMax(fileSize_m);
			bar.setProgress(downLoadedSize_m);
		}

		
	}
	
	

	private boolean revertGameApk(Activity activity){
		obbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + activity.getPackageName() + 
				"/main." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".obb";
		
		try {
			renameApk = obbPath + ".apk";
			
			File obbFile = new File(obbPath);
			File apkFile = new File(renameApk);
			if (apkFile.exists()) {
				return true;
			}
			if (obbFile.exists()) {
				obbFile.renameTo(apkFile);

				RandomAccessFile r = new RandomAccessFile(apkFile, "rw");
				r.seek(0); //指针设置在a后.
				r.write(new byte[] { 80 });
				r.close();
				return true;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}


	private void copyObbFileOrDownloadApp(Activity activity) {
		obbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + activity.getPackageName() + 
				"/main." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".obb";
		
		copyobbdstfile  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getPackageName() + "/edown/" +
		
		"fn." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".apk";
		
		Log.d("efun", "obbPath:" + obbPath);
		Log.d("efun", "copyobbdstfile:" + copyobbdstfile);
		File src = new File(obbPath);
		
		if (src == null || !src.exists()) {
			downApp(RevertAppActivity.this);
			return;
		}
	
		new CopyFileTask(obbPath, copyobbdstfile){
			
			protected void onPostExecute(String result) {
				
				if ("1".equals(result)) {//复制成功
					bar.setMax(100);
					bar.setProgress(100);
					//ApkUtil.installApk(MainActivity.this, obbNewFile);
					installApp(RevertAppActivity.this, copyobbdstfile);
				}else{
					downApp(RevertAppActivity.this);
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
				
				installApp(RevertAppActivity.this, saveDir + File.separator + apk_file_name);
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
			//	Toast.makeText(getApplicationContext(), "download failed", Toast.LENGTH_SHORT).show();
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