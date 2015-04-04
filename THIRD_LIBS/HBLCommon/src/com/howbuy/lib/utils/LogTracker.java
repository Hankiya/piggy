package com.howbuy.lib.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

import android.os.Environment;
/**
 * class for write log to local.
 * 
 * @author rexy 840094530@qq.com
 * @date 2013-11-14 下午2:56:30
 */
final class LogTracker {
	private static LogTracker mLoger = null;
	File mFileRoot, mFile;
	private int mNumInBuffer = 0;
	private int mNumFlushes = 0;
	private StringBuilder mBuilder = new StringBuilder();
	private boolean mTracking = false;

	/**
	 * @param   dirPath  null allowed for PathUtils.PATH_CACHE_LOG
	 * @throws
	 */
	public static LogTracker getInstance(String dirPath) {
		if (mLoger == null) {
			synchronized (LogTracker.class) {
				if (mLoger == null) {
					mLoger = new LogTracker(dirPath);
				}
			}
		}else{
			if(dirPath!=null&&mLoger.logPathEqual(dirPath)){
				mLoger.setLogDir(dirPath); 
			}
		}
		return mLoger;
	}

	private LogTracker(String dirPath) {
		setLogDir(dirPath);
	}
	
	private boolean logPathEqual(String dir){
		return mFileRoot!=null&&mFileRoot.getAbsolutePath().equals(dir);
	}
	private void setLogDir(String dirPath){
		File pre=mFileRoot;
		mFileRoot =  (dirPath == null) ? null : new File(dirPath);
		if(mFileRoot==null){
			mFileRoot=PathUtils.getRootDir(PathUtils.PATH_SD_THIS, true);
			if(mFileRoot!=null){
				mFileRoot=new File(PathUtils.PATH_LOG);
			}else{
				if(PathUtils.initPathConfig(false, true)){
					mFileRoot=new File(PathUtils.PATH_LOG);
				}else{
					mFileRoot=Environment.getExternalStorageDirectory();
				}
			}
		}
	    mFileRoot=mFileRoot==null?pre:mFileRoot;
	}

	/**
	 * prepare write log into file. if logFileName is null will write to latest
	 * old file ,if the logFile Name is not equal the recently old filename will
	 * create a new file to write the log. if recent log file and the
	 * logFileName are both null,there would be create a default log file.
	 * 
	 * @param @param logFileName a file that to write the log message.
	 */
	public void startTracking(String logFileName) {
		if (!mTracking) {
			if (mFileRoot == null) {
				setLogDir(null);
			} else {
				if (mFile == null && logFileName == null) {
					logFileName = "default_log.txt";
				}
			}
			boolean isNewFile = false;
			if (mFile == null) {
				mFile = new File(mFileRoot, logFileName);
				isNewFile = true;
			} else {
				if (logFileName != null || !mFile.getName().equals(logFileName)) {
					mFile = new File(mFileRoot, logFileName);
					isNewFile = true;
				}
			}
			if (isNewFile) {
				mFile.getParentFile().mkdirs();
				stopTracking();
				mBuilder.delete(0, mBuilder.length());
				mNumInBuffer = 0;
				mNumFlushes = 0;
			}
			mBuilder.append("<start_app_log ").append(Calendar.getInstance().getTime().toLocaleString()).append(">\r\n\n");
			mNumFlushes = 0;
			mTracking = true;
		} else {
			if (logFileName != null && !mFile.getName().equals(logFileName)) {
				mTracking = false;
				startTracking(logFileName);
			}
		}
	}

	/**
	 * write log message to the current log file if it is ready to accept new
	 * content.
	 * 
	 * @param @param log to write
	 * @throws
	 */
	public void appendLog(String log) {
		if (mTracking){
			mBuilder. append(log).append("\n\n");
			mNumInBuffer++;
			if (mNumInBuffer > 10) {
				flush();
				mNumInBuffer = 0;
			}
		} 
	}

	private void flush() {
		if (!mTracking) {
			return;
		}
		try {
			boolean append = true;
			if (mNumFlushes == 0) {
				if (FileUtils.getFileSize(mFile) > 1024 << 10) {
					append = false;
				}
			}
			FileWriter writer = new FileWriter(mFile, append);
			writer.write(mBuilder.toString());
			mBuilder.delete(0, mBuilder.length());
			writer.flush();
			writer.close();
			mNumFlushes++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// close the log file and reset.
	public void stopTracking() {
		if (mTracking) {
			mBuilder.append("</end_app_log>\r\n\n");
			flush();
			mTracking = false;
		}
	}
}
