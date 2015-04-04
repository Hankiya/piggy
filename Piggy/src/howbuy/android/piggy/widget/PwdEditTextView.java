package howbuy.android.piggy.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class PwdEditTextView extends EditText {
	public PwdEditTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PwdEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// TODO Auto-generated constructor stub
	}

	public PwdEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Editable getText() {
		// TODO Auto-generated method stub
		return super.getText();
	}

}
