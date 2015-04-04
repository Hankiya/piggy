package howbuy.android.piggy.help;

import howbuy.android.piggy.application.ApplicationParams;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;

public class FileLineKeyValueHelp {
	public static final String KeyValueFile = "KeyValueFile";
	private static FileLineKeyValueHelp mHelp;
	private static Context mContext;

	private FileLineKeyValueHelp() {
	}

	public static FileLineKeyValueHelp getInstance(Context context) {
		if (mHelp == null) {
			mHelp = new FileLineKeyValueHelp();
		}
		if (context!=null) {
			mContext=context;
		}
		return mHelp;
	}

	private File getFile() {
		File f = new File(getContext().getFilesDir() + File.separator + KeyValueFile);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return f;
	}

	public void writeLine(String key, String value) {
		try {
			FileWriter fw = new FileWriter(getFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(key + "=" + value+"\r\n");
			bw.flush();
			fw.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String checkLine(String key) {
		try {
			FileReader reader = new FileReader(getFile());
			BufferedReader br = new BufferedReader(reader);
			String str = null;

			while ((str = br.readLine()) != null) {
				System.out.println(str);
				String iKey=null;
				String value=null;
				if (!TextUtils.isEmpty(str)&&str.contains("=")) {
					int index=str.indexOf("=");
					iKey=str.substring(0, index);
					if (!TextUtils.isEmpty(iKey)&&iKey.equals(key)) {
						value=str.substring(index, str.length());
						if (!TextUtils.isEmpty(value)) {
							return value;
						}
					}
				}
			}
			br.close();
			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Context getContext() {
		if (mContext==null) {
			mContext=ApplicationParams.getInstance();
		}
		return mContext;
	}
	

}
