package howbuy.android.piggy.help;

import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.MD5Utils;
import howbuy.android.util.file.FileManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;


/**
 * 缓存帮助类
 * @author Administrator
 *
 */
public class CacheHelp {
	public static final long cachetime_mint = 60*10000l;
	public static final long cachetime_60_mint= 60*10000l;
	public static final long cachetime_half_day= 6*60*10000l;
	public static final long cachetime_one_day= 24*60*10000l;
	public static final long cachetime_one_month= 30*24*60*10000l;
	public static final long cachetime_nocache = 0l;
	
	public static final String CacheFolder = "datacache";
	
	public static String getCachePath() {
		String basePath= ApplicationParams.getInstance().getCacheDir().getAbsolutePath();
		File f=new File(basePath+File.separator+CacheFolder);
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath();
	}
	
	public static File hasCache(String url) {
		String md5UrlFileName=MD5Utils.toMD5(url);
		String cachePath=getCachePath();
		File f=new File(cachePath, md5UrlFileName);
		if (f.exists()&&f.length()>0) {
			return f;
		}
		return null;
	}
	
	public static InputStream hasCacheInTime(String url,long cacheTime) {
		File in=hasCache(url);
		if (in!=null) {
			long fCreateTime=in.lastModified();
			long currTime=System.currentTimeMillis();
			long jian=currTime-fCreateTime;
			if (jian<cacheTime&&jian>0) {
				try {
					Log.d("CacheHelp", "hasCacheInTime");
					return new FileInputStream(in);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				return null;
			}
		}
		return null;
	}
	
	public static InputStream saveCacheFile(String url,InputStream in){
		String md5UrlFileName=MD5Utils.toMD5(url);
		String cachePath=getCachePath();
		if (in!=null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			byte[] buffer = new byte[1024];  
			int len;  
			try {
				while ((len = in.read(buffer)) > -1 ) {  
				    baos.write(buffer, 0, len);  
				}
				baos.flush();                
				
				InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());  
				InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());  
				
				File f=new File(cachePath, md5UrlFileName);
				String abPath=f.getAbsolutePath();
				FileManager.saveFile(abPath,stream1);
				return stream2;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		return null;
	}
}
