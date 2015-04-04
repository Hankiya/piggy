package howbuy.android.piggy.widget;

import howbuy.android.piggy.R;
import howbuy.android.piggy.api.dto.ProductInfo;
import howbuy.android.piggy.parameter.GlobalParams;
import howbuy.android.piggy.parameter.PiggyParams;
import howbuy.android.util.Arith;
import howbuy.android.util.Screen;
import howbuy.android.util.StringUtil;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class CicleView extends View {
	public static final String NAME = "PieView";
	private static final float IntervalHowbuyBankAng = 3f;
	private static final int ButtomTextPaddingTopDp = 10;
	private Screen mScreen;
	private float howbuyAng;
	private float howbuyAngTemp;
	private float bankAng;

	private float howbuyQrnh;
	private float bankQrnh;
	private String howbuyQrnhSt;
	private String bankQrnhSt;

	private float mCanvansHeight;
	private float mCanvansWidth;

	private RectF mPieRectF;

	private Paint mPieHowbuyP;
	private Paint mPieBankP;
	private Paint mPieIntervalP;
	private Paint mCicleCenterP;

	private Paint mButtomText1P;
	private Paint mButtomText2P;

	private Paint mCentText1P;
	private Paint mCentText2P;
	private Paint mCentText3P;
	private float inerRecenr;
	private float inerText1y;
	private float inerText2y;
	private float inerText3y;
	private float buttomText1y;
	private float buttomText2y;
	private String drawTextPiggy;
	private String drawtextqrnh;
	private String drawtextbank;

	public float getHowbuyQrnh() {
		return howbuyQrnh;
	}

	public void setHowbuyQrnh(float howbuyQrnh) {
		this.howbuyQrnh = howbuyQrnh;
	}

	public float getBankQrnh() {
		return bankQrnh;
	}

	public void setParams(float bankQrnh, float howbuyQrnh, boolean isInit) {
		setBankQrnh(bankQrnh);
		setHowbuyQrnh(howbuyQrnh);
		howbuyQrnhSt   =   String.valueOf(((float)(Math.round(getHowbuyQrnh()*1000))/1000));
//		howbuyQrnhSt=StringUtil.formatBaiFen(String.valueOf(getHowbuyQrnh()));
		bankQrnhSt=StringUtil.formatBaiFen(String.valueOf(getBankQrnh()));
		if (isInit) {
			reInit();
		}
	}

	public void setBankQrnh(float bankQrnh) {

		this.bankQrnh = bankQrnh;
	}

	public CicleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		intParams();
	}

	private void intParams() {
		mScreen = new Screen(getContext());
		Paint dPaint = new Paint();
		dPaint.setAntiAlias(true);

		mPieHowbuyP = new Paint(dPaint);
		mPieHowbuyP.setStyle(Paint.Style.FILL);
		mPieHowbuyP.setColor(Color.BLACK);

		mPieBankP = new Paint(dPaint);
		mPieBankP.setStyle(Paint.Style.FILL);
		mPieBankP.setColor(getResources().getColor(R.color.pie_bottom_arc));

		mPieIntervalP = new Paint(dPaint);
		mPieIntervalP.setStyle(Paint.Style.FILL);
		mPieIntervalP.setColor(Color.DKGRAY);

		mCicleCenterP = new Paint(dPaint);
		mCicleCenterP.setStyle(Paint.Style.FILL);
		mCicleCenterP.setColor(getResources().getColor(R.color.windowscolor));

		Paint pText = new Paint(dPaint);
		pText.setTextAlign(Align.CENTER);

		mCentText1P = new Paint(pText);
		mCentText1P.setTextSize(getResources().getDimensionPixelOffset(R.dimen.piecentertext1));
		mCentText1P.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
		mCentText1P.setColor(getResources().getColor(R.color.pie_centertext_1));
		mCentText2P = new Paint(pText);
		mCentText2P.setTextSize(getResources().getDimensionPixelOffset(R.dimen.piecentertext2));
		mCentText2P.setColor(getResources().getColor(R.color.pie_centertext_1));
		mCentText3P = new Paint(pText);
		mCentText3P.setTextSize(getResources().getDimensionPixelOffset(R.dimen.piecentertext3));
		mCentText3P.setColor(getResources().getColor(R.color.pie_centertext_2));
		mButtomText1P = new Paint(pText);
		mButtomText1P.setTextSize(getResources().getDimensionPixelOffset(R.dimen.piebuttomtext1));
		mButtomText1P.setColor(getResources().getColor(R.color.pie_buttomtext_1));
		mButtomText2P = new Paint(pText);
		mButtomText2P.setTextSize(getResources().getDimensionPixelOffset(R.dimen.piebuttomtext2));
		mButtomText2P.setColor(getResources().getColor(R.color.pie_buttomtext_2));

		if (GlobalParams.getGlobalParams().isDebugFlag()) {
			setHowbuyQrnh(5.33f);
			setBankQrnh(0.35f);
		}

		setValue();
		drawTextPiggy = getContext().getString(R.string.drawtextpiggy);
		drawtextbank = getContext().getString(R.string.drawtextbank);
		drawtextqrnh = getContext().getString(R.string.drawtextqrnh);
	}

	private void setValue() {
		float bankQrnh = 0.00f, howbuyQrnh = 0.00f;
		ProductInfo pd=PiggyParams.getInstance().getProductInfo();
		String bankQrnhString = "" ,howbuyQrnhQrnhString = "";
		if (pd!=null) {
			 bankQrnhString = pd.getBankInterestRates();
			howbuyQrnhQrnhString = pd.getQrsy();
		}
		if (!TextUtils.isEmpty(bankQrnhString)) {
			BigDecimal b;
			b = new BigDecimal(bankQrnhString);
			b.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			bankQrnh = b.floatValue();
		} else {
			bankQrnh = 0.35f;
		}
		if (!TextUtils.isEmpty(howbuyQrnhQrnhString)) {
			BigDecimal b;
			b = new BigDecimal(howbuyQrnhQrnhString);
			b.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			howbuyQrnh = b.floatValue();
		} else {
			howbuyQrnh = 0.00f;
		}
		setParams(bankQrnh, howbuyQrnh, false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		Log.i(NAME, "widthMeasureSpec---" + widthMeasureSpec);
//		Log.i(NAME, "heightMeasureSpec---" + heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mCanvansHeight = h;
		mCanvansWidth = w;
		mPieRectF = calculatePieRectf();
		float every = Arith.divFloat(Arith.subFloat(360f, Arith.mulFloat(IntervalHowbuyBankAng, 2f)), Arith.addFloat(howbuyQrnh, bankQrnh));

		howbuyAng = Arith.mulFloat(every, howbuyQrnh);
		bankAng = Arith.mulFloat(every, bankQrnh);

		LinearGradient lGradient = new LinearGradient(0, getPaddingTop(), 0, getPaddingTop() + mPieRectF.height(), getResources().getColor(R.color.draw_lock_bg_top),
				getResources().getColor(R.color.draw_lock_bg_buttom), TileMode.CLAMP);
		mPieHowbuyP.setShader(lGradient);

		inerRecenr = mPieRectF.width() / 2f * 0.8f;
		inerText1y = mPieRectF.centerY() + mScreen.dip2px(ButtomTextPaddingTopDp);
		inerText2y = inerText1y - mCentText1P.measureText("1") - mCentText2P.measureText("1") / 2;
		inerText3y = inerText1y + mCentText3P.measureText("1") + mCentText2P.measureText("1") / 2 + mScreen.dip2px(ButtomTextPaddingTopDp);
		buttomText1y = getPaddingTop() + mPieRectF.height() + mScreen.dip2px(ButtomTextPaddingTopDp) + mButtomText1P.measureText("1");
		buttomText2y = buttomText1y + mScreen.dip2px(ButtomTextPaddingTopDp) + mButtomText1P.measureText("1");
	}

	public void reInit() {
		inerRecenr = mPieRectF.width() / 2f * 0.8f;
		mPieRectF = calculatePieRectf();
		float every = Arith.divFloat(Arith.subFloat(360f, Arith.mulFloat(IntervalHowbuyBankAng, 2f)), Arith.addFloat(howbuyQrnh, bankQrnh));
		howbuyAng = Arith.mulFloat(every, howbuyQrnh);
		bankAng = Arith.mulFloat(every, bankQrnh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// Log.i(NAME, "width" + mPieRectF.width());
		// Log.i(NAME, "height" + mPieRectF.height());
		// canvas.drawRect(mPieRectF, new Paint());
		if (getBankQrnh() <= 0f) {
			return;
		}

		float start1 = (howbuyAng - 180f) / 2f;
		canvas.drawArc(mPieRectF, start1 + IntervalHowbuyBankAng, bankAng, true, mPieBankP);

		if (howbuyAngTemp < howbuyAng) {
			howbuyAngTemp += 5f;
			if (howbuyAngTemp > howbuyAng) {
				howbuyAngTemp = howbuyAng;
			}
			invalidate();
		}
		if (howbuyAngTemp != 0) {
			canvas.drawArc(mPieRectF, start1 + IntervalHowbuyBankAng + bankAng + IntervalHowbuyBankAng, howbuyAngTemp, true, mPieHowbuyP);
		}

		canvas.drawCircle(mPieRectF.centerX(), mPieRectF.centerY(), inerRecenr, mCicleCenterP);

		canvas.drawText(drawTextPiggy, mPieRectF.centerX(), inerText1y, mCentText2P);
		canvas.drawText(String.valueOf(howbuyQrnhSt) + "%", mPieRectF.centerX(), inerText2y, mCentText1P);
		canvas.drawText(drawtextqrnh, mPieRectF.centerX(), inerText3y, mCentText3P);

		canvas.drawText(String.valueOf(bankQrnhSt) + "%", mPieRectF.centerX(), buttomText1y, mButtomText1P);
		canvas.drawText(drawtextbank, mPieRectF.centerX(), buttomText2y, mButtomText2P);

	}

	private RectF calculatePieRectf() {
		RectF resRectF;
		int h1, h2, sumH1h2;
		Rect bounds = new Rect();
		mButtomText1P.getTextBounds("1", 0, 1, bounds);
		h1 = bounds.height();
		bounds.setEmpty();
		mButtomText2P.getTextBounds("1", 0, 1, bounds);
		h2 = bounds.height();
		sumH1h2 = h1 + h2;

		float overHeight = mCanvansHeight - sumH1h2 - getPaddingTop() - getPaddingBottom() - mScreen.dip2px(ButtomTextPaddingTopDp) * 3;
		float overWidth = mCanvansWidth - getPaddingLeft() - getPaddingRight();
		float pieSqrual = Math.min(overHeight, overWidth);

		float left, right, top, bottom;
		if (pieSqrual == overWidth) {//
			left = getPaddingLeft();
			right = getPaddingLeft() + pieSqrual;
			top = getPaddingTop();
			bottom = pieSqrual + getPaddingTop();
		} else {
			top = getPaddingTop();
			bottom = pieSqrual + getPaddingTop();
			left = (mCanvansWidth - pieSqrual) / 2;
			right = pieSqrual + left;
		}
		resRectF = new RectF(left, top, right, bottom);
		return resRectF;
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = mScreen.getWidth();

			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

}
