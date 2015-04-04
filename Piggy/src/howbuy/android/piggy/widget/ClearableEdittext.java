package howbuy.android.piggy.widget;

import howbuy.android.piggy.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

public class ClearableEdittext extends EditText implements OnFocusChangeListener, TextWatcher, OnTouchListener {
	public static final String NAME = "ClearableEdittext";
	public static final int TypeNorm = 2;
	public static final int TypePas = 1;

	public int mClearType = TypeNorm;
	public boolean mCurrPasVisableFlag;

	private Drawable mCancelRightDrawble;
	private Drawable mPasGoneDrawble;
	private Drawable mPasVisableDrawble;
	private boolean isFocsAble;
	private boolean enable = true;
	private boolean isAlwaysShowPasFlag = true;
	private MyFocusChangeListen mFocusListen;
	private MyTextChangeListen mTextChangeListen;
	private MySelelctChanged mySelelctChanged;
	private int mXmlInputType = InputType.TYPE_CLASS_TEXT;

	public ClearableEdittext(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		initView(context);
	}

	public ClearableEdittext(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		// String iType=
		// attrs.getAttributeValue("http://schemas.android.com/apk/res/android",
		// "inputType");
		mXmlInputType = getInputType();
	}

	public ClearableEdittext(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context contexts) {
		if (enable) {
			this.setOnFocusChangeListener(this);
			this.addTextChangedListener(this);
			this.setOnTouchListener(this);
			this.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int resourse = getResources().getDimensionPixelSize(R.dimen.cancelpaddingleft);
					setPadding(getPaddingLeft(), getPaddingTop(), resourse, getPaddingBottom());
				}
			});
			mCancelRightDrawble = getResources().getDrawable(R.drawable.cancel);
			mPasGoneDrawble = getResources().getDrawable(R.drawable.ic_xs);
			mPasVisableDrawble = getResources().getDrawable(R.drawable.ic_xs_one);
		}
	}

	public void setVisableBtn(boolean isVisable) {
		Drawable d = null;
		switch (mClearType) {
		case TypeNorm:
			if (isVisable) {
				d = mCancelRightDrawble;
			}
			break;
		case TypePas:
			d = mCurrPasVisableFlag ? mPasVisableDrawble : mPasGoneDrawble;
			if (isVisable==false&&isAlwaysShowPasFlag==false) {
				d=null;
			}
			break;
		default:
			break;
		}
		setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], d, getCompoundDrawables()[3]);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub
		if (mTextChangeListen != null) {
			mTextChangeListen.beforeTextChanged(s, start, count, after);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (mTextChangeListen != null) {
			mTextChangeListen.afterTextChanged(s);
		}

		if (!isFocsAble) {
			return;
		}

		if (TextUtils.isEmpty(s.toString())) {
			setVisableBtn(false);
		} else {
			setVisableBtn(true);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (mFocusListen != null) {
			mFocusListen.onFocusChange(hasFocus);
		}
		isFocsAble = hasFocus;
		if (hasFocus == true) {
			boolean isEmpty = TextUtils.isEmpty(getEditableText().toString());
			if (!isEmpty) {
				setVisableBtn(true);
			} else {
				setVisableBtn(false);
			}
		} else {
			setVisableBtn(false);
		}
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		if (mTextChangeListen != null) {
			mTextChangeListen.onTextChanged(text, start, lengthBefore, lengthAfter);
		}
	}

	@Override
	public Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		return super.onSaveInstanceState();
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(state);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (isClearnEnable() && getCompoundDrawables()[2] != null) {
			boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - getIntrinsicWidthByType());
			if (tappedX) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					switch (mClearType) {
					case TypeNorm:
						getEditableText().clear();
						break;
					case TypePas:
						int currType;
						if (mCurrPasVisableFlag) {// 密文
							mCurrPasVisableFlag = false;
							currType = getPasVisableInputType();
						} else {
							mCurrPasVisableFlag = true;
							currType = getPasGoneInputType();
						}
						setInputType(currType);
						setVisableBtn(true);
						setSelection(getText().length());
					default:
						break;
					}
				}
				return true;
			}
		}
		return super.onTouchEvent(event);
	}

	private int getIntrinsicWidthByType() {
		switch (mClearType) {
		case TypeNorm:
			return mCancelRightDrawble.getIntrinsicWidth();
		case TypePas:
			if (mCurrPasVisableFlag) {
				return mPasVisableDrawble.getIntrinsicWidth();
			} else {
				return mPasGoneDrawble.getIntrinsicWidth();
			}
		default:
			break;
		}
		return mCancelRightDrawble.getIntrinsicWidth();
	}

	private int getPasVisableInputType() {
		int currType;
		if (InputType.TYPE_CLASS_NUMBER==(InputType.TYPE_CLASS_NUMBER & mXmlInputType)) {//数字键盘
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				currType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD;
			} else {
				currType = InputType.TYPE_CLASS_NUMBER;
				setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
		} else {//非数字键盘
			currType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
			
		}
		return currType;
	}
	
	private int getPasGoneInputType() {
		if(InputType.TYPE_CLASS_NUMBER==(InputType.TYPE_CLASS_NUMBER & mXmlInputType))	{
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				setTransformationMethod(null);
			}
			return InputType.TYPE_CLASS_NUMBER;
		}else {
			return InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
		}
	}
	

	public boolean isClearnEnable() {
		return enable;
	}

	public void setClearnEnable(boolean enable) {
		this.enable = enable;
		if (false == enable) {
			initView(getContext());
			invalidate();
		}
	}

	public MyFocusChangeListen getmListen() {
		return mFocusListen;
	}

	public void setmListen(MyFocusChangeListen mListen) {
		this.mFocusListen = mListen;
	}

	public MyTextChangeListen getmTextChangeListen() {
		return mTextChangeListen;
	}

	public void setmTextChangeListen(MyTextChangeListen mTextChangeListen) {
		this.mTextChangeListen = mTextChangeListen;
	}

	public int getClearType() {
		return mClearType;
	}

	public void setClearType(int mClearType) {
		this.mClearType = mClearType;
		Log.d(NAME, "2iType=" + mClearType);
		mXmlInputType = getInputType();
		if (mClearType == TypePas) {
			int currType = getPasVisableInputType();
			setInputType(currType);
			setAlwaysShowPasFlag(true);
			setVisableBtn(true);
		}
	}
	

	public MySelelctChanged getMySelelctChanged() {
		return mySelelctChanged;
	}

	public void setMySelelctChanged(MySelelctChanged mySelelctChanged) {
		this.mySelelctChanged = mySelelctChanged;
	}

	public boolean isAlwaysShowPasFlag() {
		return isAlwaysShowPasFlag;
	}

	public void setAlwaysShowPasFlag(boolean isAlwaysShowPasFlag) {
		this.isAlwaysShowPasFlag = isAlwaysShowPasFlag;
	}

	public interface MyFocusChangeListen {
		public void onFocusChange(boolean hasFocus);
	}

	public interface MyTextChangeListen {
		public void beforeTextChanged(CharSequence s, int start, int count, int after);

		public void onTextChanged(CharSequence s, int start, int before, int count);

		public void afterTextChanged(Editable s);
	}
	
	public interface MySelelctChanged{
		public void onSelectionChanged(int selStart, int selEnd);
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		// TODO Auto-generated method stub
		super.onSelectionChanged(selStart, selEnd);
		if (mySelelctChanged!=null) {
			mySelelctChanged.onSelectionChanged(selStart, selEnd);
		}
	}
}
