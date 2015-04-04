package howbuy.android.piggy.ui;

import howbuy.android.piggy.R;
import howbuy.android.util.Cons;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyTokenId extends Activity {
	private EditText tokenId;
	private Button submitBtn;
	private TextView tokenTv;
	private SharedPreferences mSf;
	private String tokenString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.modify_token_xml);
		 tokenId = (EditText) findViewById(R.id.tokenId);
		 submitBtn = (Button) findViewById(R.id.submit_btn);
		 tokenTv = (TextView) findViewById(R.id.tokenTv);
		 
		mSf = getSharedPreferences(Cons.SFbaseUser, Context.MODE_PRIVATE);
			
		 tokenString =  mSf.getString(Cons.SFfirstUUid, null);
		 if (!TextUtils.isEmpty(tokenString)) {
			 tokenTv.setText(tokenString);
		 }else {
			 tokenTv.setText("token为空！");
		 }
		 
		 submitBtn.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 if (tokenId.getText() != null) {
					 mSf.edit().putString(Cons.SFfirstUUid, tokenId.getText().toString()).commit();
					 
					 tokenString =  mSf.getString(Cons.SFfirstUUid, null);
					 if (!TextUtils.isEmpty(tokenString)) {
						 tokenTv.setText(tokenString);
						 Toast.makeText(getApplicationContext(), "更改成功！", Toast.LENGTH_SHORT).show();
						 
					 }else {
						 tokenTv.setText("token为空！");
						 Toast.makeText(getApplicationContext(), "更改失败！", Toast.LENGTH_SHORT).show();
					 }
				 }			
			 }
		 });
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	

}
