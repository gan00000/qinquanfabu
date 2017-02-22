package com.egame.cn;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.brave.shine.sea.R;
import com.efun.core.task.EfunRequestAsyncTask;
import com.efun.core.tools.ApkUtil;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.download.EfunDownLoader;
import com.efun.download.listener.EfunDownLoadListener;
import com.efun.service.FileService;
import com.egame.cn.view.TextProgressBar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class RevertAppActivity extends BaseActivity {


	static EfunDownLoader downLoader;

	TextProgressBar bar;
	
	int fileSize_m = 0;
	int downLoadedSize_m = 0;
	
	public String obbPath = "";

	private String copyobbdstfile = "";
	
	String renameApk = "";

	private String remoteApkPath;
	
	private Handler handle;
	
	ProgressDialog pb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.revert_layout);
		Log.d("efun", "onCreate");
		bar = (TextProgressBar) this.findViewById(R.id.progressBar);
		

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
				}else{
					
				//	downApp(RevertAppActivity.this);
				}
			}
			
		}else{//游戏包名跟本包名一致
			
			if (revertGameApk(this) && !TextUtils.isEmpty(renameApk)) {
				installApp(this, renameApk);
			}else{
				
			//	downApp(RevertAppActivity.this);
			}
		}
		
		/*if (bar != null) {
			bar.setMax(fileSize_m);
			bar.setProgress(downLoadedSize_m);
		}*/
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


	protected void downApp(Context context) {
		if (TextUtils.isEmpty(remoteApkPath)) {
			return;
		}
		if (bar != null) {
			bar.setVisibility(View.VISIBLE);
		}
		if (downLoader == null) {
			downLoader = new EfunDownLoader();
		}
		initDownLoad(context);
		downLoader.startDownLoad(context);
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
		
		if (pb != null && pb.isShowing()) {
			pb.dismiss();
			pb = null;
		}
	}
}