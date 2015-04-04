package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.piggy.jni.DesUtil;
import howbuy.android.piggy.ui.base.AbstractBaseHaveLockActivity;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AbstractBaseHaveLockActivity {
	String aa = "qp38RUkLdEV/1RA3YnvfXExwO2Sm6jnLi+/tSHGbkTgshydKtmaBSjxyosd5rEnb4LPXVf2v5XKwTNjL0uwzrMM/MpS/bjoLNaqZxq5OQDu4fGRSXVEi7U6nYf2Btft1Yjpr4oCnB4izSa/5TLXzFgkngtf03snY40uYgboBPzY=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(MainActivity.this, LockSetActivity.class);
				startActivity(in);
				byte[] key = Base64.decode(aa.getBytes(), Base64.DEFAULT);

				DesUtil des = new DesUtil();
				String s = new String("眼里转");
				try {
					byte[] resEn = des.encryptDes(s.getBytes("utf-8"), key,true);
					byte[] resDe = des.dencryptDes(resEn, key,true);
					String destString = new String(resDe);
					Log.d("rsakey", "main" + destString);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(MainActivity.this, ProPertyActivity.class);
				startActivity(in);
			}
		});
		findViewById(R.id.button3).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(MainActivity.this, LoginActivity.class);
				startActivity(in);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void initUi(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.aty_main);
	}

	static {
		System.loadLibrary("desrsa");
	}

}
