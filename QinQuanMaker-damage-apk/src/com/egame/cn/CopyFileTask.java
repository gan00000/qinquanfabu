package com.egame.cn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.efun.core.task.EfunRequestAsyncTask;

import android.util.Log;

public class CopyFileTask extends EfunRequestAsyncTask {
	
	private static final String TAG = "CopyFileTask";
	private String oldFilePath;
	private String newFilePath;
	
	private boolean copySuccess = false;

	public CopyFileTask(String srcFile, String dstFile) {
		this.oldFilePath = srcFile;
		this.newFilePath = dstFile;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		copySuccess = copyFile(oldFilePath, newFilePath);
		if (copySuccess) {
			return "1";
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
	
	
	public boolean copyFile(String oldFilePath,String newFilePath) {

		try {
			//如果原文件不存在
			File oldFile = new File(oldFilePath);
			
			if (oldFile != null && oldFile.exists()) {
				Log.d(TAG, "oldFile.exists true");
				long oldFileL = oldFile.length();
				//获得原文件流
				FileInputStream inputStream = new FileInputStream(oldFile);
				byte[] data = new byte[2048 * 2];
				//输出流
				File newFile = new File(newFilePath);
				if (newFile != null && newFile.exists()) {
					newFile.delete();
				}
				
				File newTmpFile = new File(newFilePath + ".tmp");
				if (!newTmpFile.getParentFile().exists()) {
					newTmpFile.getParentFile().mkdirs();
				}
				
				FileOutputStream outputStream = new FileOutputStream(newTmpFile);
				int offset = 0;
				int copyCounts = 0;
				//开始处理流
				while ((offset = inputStream.read(data, 0, data.length)) != -1) {
					outputStream.write(data,0,offset);
					copyCounts += offset;
					offset = 0;
					Log.d(TAG, "copy ... ");
					publishProgress((int)oldFileL,copyCounts);
				}
				
				inputStream.close();
				outputStream.close();
				newTmpFile.renameTo(newFile);
				return true;
			} 
			Log.d(TAG, "oldFile.exists false");
		} catch (IOException e) {
			Log.d(TAG, "e:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
		
	}

}
