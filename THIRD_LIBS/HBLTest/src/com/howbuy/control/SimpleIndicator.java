package com.howbuy.control;

/**
 renzh:bool_show_num="true"
 renzh:float_item_gap="5dp"
 renzh:float_item_wid_normal="20dp"
 renzh:float_item_wid_selected="30dp"
 renzh:int_color_item_normal="#7fff"
 renzh:int_color_item_selected="#ffff"
 renzh:int_color_num_normal="#7ff0"
 renzh:int_color_num_selected="#ff00"
 renzh:int_item_count="5"
 renzh:int_selected_item="2"
 */
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

//ADD-S by RenZheng  2012-8-24 for indicator  to all apps'showing page.
public class SimpleIndicator extends View {

	private ArrayList<RectF> rects_ = new ArrayList<RectF>();

	private int current_ = -1;
	private int count_ = 0;
	private float item_gap_ = 2f;
	private float item_size_normal_ = 15f;
	private float item_size_selected_ = 20f;
	private int item_color_normal_ = 0;
	private int item_color_selected_ = 0;
	private int item_color_num_normal_ = 0;
	private int item_color_num_selected_ = 0;
	private boolean show_num_ = true;

	public int getCurrent() {
		return current_;
	}

	public int getCount() {
		return count_;
	}

	public SimpleIndicator(Context context) {
		this(context, null, 0);
	}

	public SimpleIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SimpleIndicator(Context context, AttributeSet atr, int defStyle) {
		super(context, atr, defStyle);
		/*
		 * if (atr != null) { // float scale =
		 * context.getResources().getDisplayMetrics().density; // atr.getat
		 * TypedArray a = context.obtainStyledAttributes(atr,
		 * R.styleable.RIndicator_style); item_gap_ = a.getDimension(
		 * R.styleable.RIndicator_style_float_item_gap, 10f);
		 * 
		 * item_size_normal_ = a.getDimension(
		 * R.styleable.RIndicator_style_float_item_wid_normal, 25f);
		 * item_size_selected_ = a.getDimension(
		 * R.styleable.RIndicator_style_float_item_wid_selected, 35f); int cur =
		 * a.getInt(R.styleable.RIndicator_style_int_selected_item, -1); int
		 * count = a .getInt(R.styleable.RIndicator_style_int_item_count, 0);
		 * item_color_normal_ = a.getColor(
		 * R.styleable.RIndicator_style_int_color_item_normal, 0X77FFFFFF);
		 * item_color_selected_ = a.getColor(
		 * R.styleable.RIndicator_style_int_color_item_selected, 0XFFFFFFFF);
		 * item_color_num_normal_ = a.getColor(
		 * R.styleable.RIndicator_style_int_color_num_normal, 0X77000000);
		 * item_color_num_selected_ = a.getColor(
		 * R.styleable.RIndicator_style_int_color_num_selected, 0XFF000000);
		 * show_num_ = a.getBoolean( R.styleable.RIndicator_style_bool_show_num,
		 * true); a.recycle(); if (count > 0) { setCurGrid(cur, count); } }
		 */
	}

	/**
	 */
	public boolean setCurGrid(int cur, int num) {
		if (count_ != num) {
			int dnum = num - count_;
			count_ = num;
			addOrMoveList(dnum);
			if (cur == current_ || (rects_.size() != count_)) {
				whenDataChanged();
				return true;
			}
			requestLayout();
		}
		return setCurGrid(cur);

	}

	public boolean setCurGrid(int cur) {
		if (getCount() > 0) {
			if (current_ != cur) {
				current_ = cur;
				whenDataChanged();
				invalidate();
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int padW = getPaddingLeft() + getPaddingRight();
		int padH = getPaddingBottom() + getPaddingTop();
		if (count_ > 0) {
			int wid = (int) ((item_gap_ + item_size_normal_) * count_)
					+ (int) (item_size_selected_ - item_size_normal_);
			setMeasuredDimension(wid + padW, (int) Math.max(item_size_normal_, item_size_selected_)
					+ padH);
		} else {
			setMeasuredDimension(padW, padH);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final Paint p = new Paint();
		p.setAntiAlias(true);
		p.setTextAlign(Align.CENTER);
		p.setStyle(Paint.Style.FILL_AND_STROKE);
		for (int i = 0; i < count_; i++) {
			RectF rect = rects_.get(i);
			if (current_ == i) {
				p.setColor(item_color_selected_);
				canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, p);
				if (show_num_) {
					float textSize = rect.width() * 0.8f;
					p.setTextSize(textSize);
					p.setColor(item_color_num_selected_);
					FontMetrics fm = p.getFontMetrics();
					canvas.drawText("" + (i + 1), rect.centerX(), rect.centerY() + textSize / 2
							+ (fm.top - fm.ascent), p);
				}
			} else {
				p.setColor(item_color_normal_);
				canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, p);
				if (show_num_) {
					float textSize = rect.width() * 0.8f;
					p.setTextSize(textSize);
					p.setColor(item_color_num_normal_);
					FontMetrics fm = p.getFontMetrics();
					canvas.drawText("" + (i + 1), rect.centerX(), rect.centerY() + textSize / 2
							+ (fm.top - fm.ascent), p);
				}
			}
		}
		super.onDraw(canvas);
	}

	private void whenDataChanged() {
		float bx = item_gap_ / 2 + getPaddingLeft(), by = getPaddingTop()
				+ (item_size_selected_ - item_size_normal_) / 2;
		for (int i = 0; i < count_; i++) {
			RectF rect = rects_.get(i);
			if (current_ == i) {
				rect.top = by - (item_size_selected_ - item_size_normal_) / 2;
				rect.left = bx;
				rect.right = bx + item_size_selected_;
				rect.bottom = rect.top + item_size_selected_;
			} else {
				rect.set(bx, by, bx + item_size_normal_, by + item_size_normal_);
			}
			bx = rect.right + item_gap_;

		}

	}

	private void addOrMoveList(int dnum) {

		if (dnum > 0) {
			while (dnum-- > 0) {
				rects_.add(new RectF());
			}
		} else {
			dnum = Math.min(rects_.size(), -dnum);
			while (dnum-- > 0) {
				rects_.remove(0);
			}
		}
	}

	@Override
	public String toString() {
		return "RIndicator [rects_=" + rects_ + ", current_=" + current_ + ", count_=" + count_
				+ ", item_gap_=" + item_gap_ + ", item_size_normal_=" + item_size_normal_
				+ ", item_size_selected_=" + item_size_selected_ + ", item_color_normal_="
				+ item_color_normal_ + ", item_color_selected_=" + item_color_selected_
				+ ", item_color_num_normal_=" + item_color_num_normal_
				+ ", item_color_num_selected_=" + item_color_num_selected_ + ", show_num_="
				+ show_num_ + "]";
	}

}
