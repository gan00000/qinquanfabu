package com.egame.cn;

import java.io.File;

import com.brave.shine.sea.R;
import com.efun.core.db.EfunDatabase;
import com.efun.core.tools.ApkUtil;
import com.efun.core.tools.EfunLocalUtil;
import com.efun.download.EfunDownLoader;
import com.efun.download.listener.EfunDownLoadListener;
import com.efun.service.FileService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownLoadCopyAppActivity extends BaseActivity {


	protected static final String ALREADY_DOWNLOAD = "ALREADY_DOWNLOAD";

	protected static final String ALREADY_DOWNLOAD_VALUE = "10";

	static EfunDownLoader downLoader;

	ProgressBar bar;
	
	int fileSize_m = 0;
	int downLoadedSize_m = 0;

	private String obbNewFile = "";

	private String remoteApkPath;
	
	private TextView download_text_view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.copydownload);
		Log.d("efun", "onCreate");
		bar = (ProgressBar) this.findViewById(R.id.progressBar);
		remoteApkPath = ResHelper.getInstallAppDownloadUrl(this);
		download_text_view = (TextView) findViewById(R.id.progressBar_text);
		download_text_view.setTextColor(Color.RED);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("efun", "onResume");
	

		if (!TextUtils.isEmpty(ResHelper.getInstallAppPackageName(this))) {// 包名不一样的情形

			if (ApkUtil.isInstallApp(this, ResHelper.getInstallAppPackageName(this))) {
				startApp(this);// 包名不一样才会启动
			} else {
				//downApp(this);
				askInstallGame();
			}

		} else {// 游戏包名跟本包名一致

			if (!TextUtils.isEmpty(remoteApkPath)) {
				//downApp(this);
				askInstallGame();
			}
		}

		
		
		if (bar != null) {
			bar.setMax(fileSize_m);
			bar.setProgress(downLoadedSize_m);
			
		}
		

		
	}

	private void copyObbFile(Activity activity) {
		
		obbNewFile  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getPackageName() + "/edown/" +
		
		"fn." + EfunLocalUtil.getVersionCode(activity) + "." + activity.getPackageName() + ".apk";
		
		String obbPath = getObbFilePath(activity);
		
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

	protected void askInstallGame() {
		
		String g = EfunDatabase.getSimpleString(this, EfunDatabase.EFUN_FILE, ALREADY_DOWNLOAD);
		if (ALREADY_DOWNLOAD_VALUE.equals(g)) {
			
			downApp(DownLoadCopyAppActivity.this);
			
		}else{
			
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage("download and install game");
			b.setNegativeButton("close", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			b.setPositiveButton("download", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					downApp(DownLoadCopyAppActivity.this);
					EfunDatabase.saveSimpleInfo(DownLoadCopyAppActivity.this, EfunDatabase.EFUN_FILE, ALREADY_DOWNLOAD, ALREADY_DOWNLOAD_VALUE);
				}
			});
			
			b.create().show();
		}
		
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
				
				float i =(float) downLoadedSize / (float)fileSize * 100;
				float percent =  (float)(Math.round(i * 100)) / 100;//Math.round为四舍五入算法
				download_text_view.setText("downloading  " + String.valueOf(percent) + "%");
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
	}
}