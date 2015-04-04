package howbuy.android.util;

import howbuy.android.piggy.R;
import howbuy.android.piggy.application.ApplicationParams;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

public class SpannableUtil {
	/**
	 * @deprecated 气泡1
	 * @param mContext
	 * @param content
	 * @return
	 */
	public static SpannableString unTradeHint(Context mContext, String content) {
		SpannableString sp = new SpannableString(content);
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_page)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * @deprecated 气泡2
	 * @param mContext
	 * @param content
	 * @return
	 */
	public static SpannableString unTradeContent(Context mContext, String content) {
		SpannableString sp = new SpannableString(content);
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_page_gray)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * 
	 * 
	 * @param mContext
	 * @param content
	 * @return
	 */
	public static SpannableString exceptBeyoundHint(Context mContext, String content) {
		SpannableString sp = new SpannableString(content);
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(R.color.windowscolor), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	/**
	 * 
	 * 
	 * @param mContext
	 * @param content
	 * @return
	 */
	public static SpannableString exceptBeyoundNumber(Context mContext, String content) {
		SpannableString sp = new SpannableString(content);
		sp.setSpan(new AbsoluteSizeSpan(14, true), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.actioncolor)), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}



	public static SpannableString all(String content, int size, int color, boolean isXia) {
		int length = content.length();
		SpannableString sp = new SpannableString(content);
		if (size!=-1) {
			sp.setSpan(new AbsoluteSizeSpan(size, true), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (color != -1) {
			sp.setSpan(new ForegroundColorSpan(ApplicationParams.getInstance().getResources().getColor(color)), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		if (isXia) {
			sp.setSpan(new UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return sp;
	}

}
