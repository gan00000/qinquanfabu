package com.egame.cn;

import java.io.File;
import java.io.IOException;

import com.droidupdate.jni.PatchUtil;
import com.efun.core.task.EfunRequestAsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

public class MrgePatchTask extends EfunRequestAsyncTask {

	private File oldapk;
	private File newFile;
	private File patchFile;
	Activity activity;
	int ret = 1;
	
	ProgressDialog progressDialog;
	
	Handler h;

	public MrgePatchTask(Activity activity,File oldapk, File newFile, File patchFile) {
		super();
		this.oldapk = oldapk;
		this.newFile = newFile;
		this.patchFile = patchFile;
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle("please wait ... ");
		progressDialog.setMessage("0%");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		h = new Handler(){
			public void handleMessage(android.os.Message msg) {
				float finish = msg.arg1;
				float size = msg.arg2;
				String percentStr = "0%";
				if (size > 0) {
					//float percent = finish / size  * 100;
					//Log.d("MrgePatchTask", "finish:" + finish + ",size:" + size + ",percent:" + percent);
					float percent =  (float)(Math.round(finish / size  * 100 * 100)) / 100;//Math.round为四舍五入算法
					Log.d("MrgePatchTask", "percent:" + percent);
					percentStr = String.valueOf(percent) + "%";
				}
				
				progressDialog.setMessage(percentStr);
			};
		};
		
		PatchUtil.handler = h;
	}


	@Override
	protected String doInBackground(String... params) {
		try {
			int result = applyPatch(oldapk, newFile, patchFile);
			if (result == 0) {
				return "0";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "1";
	}

	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.setMessage("100%");
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	
	private int applyPatch(File oldapk, File newFile, File patchFile) throws IOException {

		if (patchFile != null && patchFile.exists()) {
			ret = PatchUtil.applyPatch(oldapk.getAbsolutePath(), newFile.getAbsolutePath(), patchFile.getAbsolutePath());
			//0合并成功
		}
		
		return ret;
	}
	
}
