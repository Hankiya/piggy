package com.howbuy.control;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.AttributeSet;

import com.howbuy.lib.compont.BezierParam;
import com.howbuy.lib.control.AbsView;
import com.howbuy.lib.interfaces.ICharData;
import com.howbuy.libtest.R;

/**
 * @author rexy 840094530@qq.com
 * @date 2014-3-26 下午4:27:22
 */
public class SimpleChartView extends AbsView {
	private String time_format_ = "yy-MM-dd";
	private ShapeDrawable curve_shape_ = null;
	private int mCharDataType = 0;
	private int coord_col_ = 5;
	private int coord_row_ = 5;
	private int curve_shape_color_start_ = 0xaaffff00;
	private int curve_shape_color_end_ = 0xaaffffff;
	private int curve_color_ = 0xffffff00;
	private int coord_color_ = 0xff000000;
	private int grid_color_ = 0xff000000;
	private int coord_text_color_ = 0xf00ff00;
	private float coord_arrow_size_ = 20f;
	private float coord_text_size_ = 20;
	private float coord_min_weight_ = 0.2f;
	private float coord_max_weight_ = 0.2f;
	private float text_height_ = 0f, text_offset_ = 0f;
	private boolean enable_grid_ = true;
	private boolean enable_grid_dash_ = true;
	private int len_ = 0, len_pre_ = 0;
	private final ArrayList<ICharData> data_ = new ArrayList<ICharData>();
	private float min_, max_, min_base_, max_base_;
	private float scaleY_, space_, dw_;
	public static float MIN_KP_WID = 2f;
	public static float MAX_KP_WID = 60f;
	protected BezierParam mBezier;
	protected final Path mPath = new Path();

	public SimpleChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.SimpleChartView, 0, 0);
			coord_col_ = a.getInt(R.styleable.SimpleChartView_coord_col, 5);
			coord_row_ = a.getInt(R.styleable.SimpleChartView_coord_row, 5);
			curve_color_ = a.getColor(R.styleable.SimpleChartView_curve_color,
					0xffffff00);
			coord_color_ = a.getColor(R.styleable.SimpleChartView_coord_color,
					0xff000000);
			grid_color_ = a.getColor(R.styleable.SimpleChartView_coord_color,
					0xff333333);
			coord_text_color_ = a.getColor(
					R.styleable.SimpleChartView_coord_text_color, 0xf00ff00);

			coord_text_size_ = a.getDimension(
					R.styleable.SimpleChartView_coord_text_size, 18);
			coord_arrow_size_ = a.getDimension(
					R.styleable.SimpleChartView_coord_arrow_size, 20);
			coord_min_weight_ = a.getFloat(
					R.styleable.SimpleChartView_coord_min_weight, 0.2f);
			coord_min_weight_ = Math.min(Math.max(coord_min_weight_, 0), 0.5f);

			coord_max_weight_ = a.getFloat(
					R.styleable.SimpleChartView_coord_max_weight, 0.2f);
			coord_max_weight_ = Math.min(Math.max(coord_max_weight_, 0), 0.5f);
			if (coord_min_weight_ + coord_max_weight_ > 0.5f) {
				float scale = 0.5f / (coord_min_weight_ + coord_max_weight_);
				coord_min_weight_ *= scale;
				coord_max_weight_ *= scale;
			}
			enable_grid_ = a.getBoolean(
					R.styleable.SimpleChartView_enable_grid, true);
			enable_grid_ = a.getBoolean(
					R.styleable.SimpleChartView_enable_grid, true);
			enable_grid_dash_ = a.getBoolean(
					R.styleable.SimpleChartView_enable_grid_dash, true);
			curve_shape_color_start_ = a.getColor(
					R.styleable.SimpleChartView_curve_shape_color_start,
					0xaaff0000);
			curve_shape_color_end_ = a.getColor(
					R.styleable.SimpleChartView_curve_shape_color_end,
					0xaaffffff);
			a.recycle();
		}
		mBezier = new BezierParam(mPath, 0.8f);
		mPaint.setStrokeWidth(0.75f);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(coord_text_size_);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	/**
	 * when a data object have different value as y coordinate set datatype can
	 * switch to different ,that will affect by ICharData.getValue(int type)
	 * 
	 * @param @param newDataType new type that will used by
	 *        ICharData.getValue(int type);
	 * @param @param applyed true will be affect right now .
	 * @return void
	 */
	public void setDataType(int newDataType, boolean applyed) {
		if (newDataType >= 0 && newDataType != mCharDataType) {
			mCharDataType = newDataType;
			notifyDataChanged(true, true);
		}
	}

	/**
	 * set both shape color start and end.
	 * 
	 * @param @param start_color
	 * @param @param end_color
	 */
	public void setShapeColor(int start_color, int end_color) {
		curve_shape_color_start_ = start_color;
		curve_shape_color_end_ = end_color;
		curve_shape_ = null;
		invalidate();
	}

	/**
	 * set either start or end color of the shape.
	 */
	public void setShadeColor(int color, boolean start) {
		if (start) {
			curve_shape_color_start_ = color;
		} else {
			curve_shape_color_end_ = color;
		}
		curve_shape_ = null;
		invalidate();
	}

	/**
	 * get either start or end color of the shape.
	 */
	public int getShadeColor(boolean start) {
		return start ? curve_shape_color_start_ : curve_shape_color_end_;
	}

	/**
	 * get both start and end color of the shape.
	 */
	public int[] getShapeColor() {
		return new int[] { curve_shape_color_start_, curve_shape_color_end_ };
	}

	/** set the curve color of the char */
	public void setCurveColor(int color) {
		curve_color_ = color;
		invalidate();
	}

	/** get the curve color of the char */
	public int getCurveColor() {
		return curve_color_;
	}

	/** set the coordinate color of the char */
	public void setCoordColor(int color) {
		coord_color_ = color;
		invalidate();
	}

	/** get the coordinate color of the char */
	public int getCoordColor() {
		return coord_color_;
	}

	/** set the coordinate text x and y color of the char */
	public void setCoordTextColor(int color) {
		coord_text_color_ = color;
		invalidate();
	}

	/** get the coordinate text color of the char */
	public int getCoordTextColor() {
		return coord_text_color_;
	}

	/** set the coordinate text size of the char */
	public void setCoordTextSize(float size) {
		if (size > 0) {
			coord_text_size_ = size;
			requestLayout();

		}
	}

	public float getCoordTextSize() {
		return coord_text_size_;
	}

	public void setCoordRow(int row) {
		if (row > 0) {
			coord_row_ = row;
			dw_ = (mRecFrame.height()) / coord_row_;
			invalidate();
		}
	}

	public int getCoordRow() {
		return coord_row_;
	}

	public void setCoordCol(int col) {
		if (col > 0) {
			coord_col_ = col;
			invalidate();
		}
	}

	public int getCoordCol() {
		return coord_col_;
	}

	public void setArrowSize(float size) {
		if (size > 0) {
			coord_arrow_size_ = size;
		} else {
			size = 10;
		}
		requestLayout();
	}

	public float getArrowSize() {
		return coord_arrow_size_;
	}

	/**
	 * set the white space bottom and top weight of the char.
	 * 
	 * @param @param minWeight max is 0.5;
	 * @param @param maxWeight max is 0.5; both weight plus result should less
	 *        than 0.5;
	 */
	public void setMinAndMaxWeight(float minWeight, float maxWeight) {
		minWeight = Math.min(Math.max(0, minWeight), 0.5f);
		maxWeight = Math.min(Math.max(0, maxWeight), 0.5f);
		if (minWeight + maxWeight > 0.5f) {
			float scale = 0.5f / (minWeight + maxWeight);
			minWeight *= scale;
			maxWeight *= scale;
		}
		coord_min_weight_ = minWeight;
		coord_max_weight_ = maxWeight;
		computeScale(true);
		invalidate();
	}

	public float getMinWeight() {
		return coord_min_weight_;
	}

	public float getMaxWeight() {
		return coord_max_weight_;
	}

	public void setGridColor(int color) {
		grid_color_ = color;
		invalidate();
	}

	public int getGridColor() {
		return grid_color_;
	}

	public void setEnableGrid(boolean enable) {
		enable_grid_ = enable;
		invalidate();

	}

	public boolean isEnableGrid() {
		return enable_grid_;
	}

	public void setEnableGridDash(boolean enable) {
		enable_grid_dash_ = enable;
		invalidate();
	}

	public boolean isEnableGridDash() {
		return enable_grid_dash_;
	}

	public int getSize() {
		return len_;
	}

	public ICharData getData(int i) {
		if (i >= 0 && i < len_) {
			return data_.get(i);
		}
		return null;
	}

	public void setData(ArrayList<? extends ICharData> data) {
		data_.clear();
		if (data != null) {
			data_.addAll(data);
		}
		len_ = data_.size();
		if (len_ != len_pre_) {
			len_pre_ = len_;
			if (len_ > 0) {
				space_ = (mRecFrame.width() - 2) / (len_ - 1);
			}
			curve_shape_ = null;
		}
		notifyDataChanged(true, true);
	}

	public ArrayList<ICharData> getData() {
		return data_;
	}

	public void addData(ArrayList<? extends ICharData> data, boolean insert_end) {
		if (data != null) {
			if (insert_end) {
				data_.addAll(data);
			} else {
				data_.addAll(0, data);
			}
			len_ = data_.size();
			notifyDataChanged(true, true);
		}

	}

	public void addData(ICharData data, boolean insert_end) {
		if (data != null) {
			if (insert_end) {
				data_.add(data);
			} else {
				data_.add(0, data);
			}
			len_ = data_.size();
		}
	}

	/**
	 * @Description: adjust min and max value.
	 * @param @param val new data to compared.
	 */
	protected void min_max(float val) {
		min_base_ = min_ = Math.min(min_, val);
		max_base_ = max_ = Math.max(max_, val);
	}

	/**
	 * @Description: init min and max.
	 */
	protected void reset_min_max() {
		min_base_ = min_ = Integer.MAX_VALUE;
		max_base_ = max_ = Integer.MIN_VALUE;
	}

	private int computeMinAndMax() {
		reset_min_max();
		for (int i = 0; i < len_; i++) {
			min_max(data_.get(i).getValue(mCharDataType));
		}
		return checkErrMinAdMax();
	}

	/**
	 * @Description: check min and max value . escape for especial data.
	 * @return 0:min and max is different and normal. 1:min=max all data is the
	 *         same . 2 min and max are not init.
	 */

	private int checkErrMinAdMax() {
		int code = 0;
		if (min_ == max_) {
			code |= 1;
		}
		if (min_ == Integer.MAX_VALUE || max_ == Integer.MIN_VALUE) {
			code |= 2;
		}
		return code;
	}

	protected float transY2Screen(float val) {
		return (float) (mRecFrame.top - scaleY_ * (val - max_base_));
	}

	public static String timeFormat(Long time, String format) {
		String date = new java.text.SimpleDateFormat((format == null || format
				.trim().equals("")) ? "yyyy-MM-dd  HH:mm" : format)
				.format(new java.util.Date(time));// HH:mm:ss
		return date;
	}

	public static String valueFormat(float val, int decimal) {
		try {
			if (val == 0) {
				return " 0 ";
			}
			if (decimal > 0) {
				String format = "%1$.#f".replace("#", decimal + "");
				String r = String.format(format, val);
				int len = r.length();
				while (r.charAt(--len) == '0')
					;
				if (r.charAt(len) == '.') {
					return r.substring(0, len);
				} else {
					return r.substring(0, ++len);
				}
			}
		} catch (Exception e) {
		}
		return val + "";
	}

	@Override
	public boolean notifyDataChanged(boolean needInvalidate, boolean fromUser) {
		if (len_ > 0) {
			int error = computeMinAndMax();
			if (error == 1) {
				min_ *= 0.8f;
				max_ *= 1.2f;
			}
			computeScale(true);
			if (needInvalidate) {
				invalidate();
			}
		}
		return true;
	}

	/**
	 * @Title: computeScaleY
	 * @Description: TODO
	 */
	protected void computeScale(boolean checkYmin) {
		if (coord_min_weight_ != 0 || coord_max_weight_ != 0) {
			float lenY = (max_ - min_)
					/ (1 - coord_max_weight_ - coord_min_weight_);
			min_base_ = min_ - lenY * coord_min_weight_;
			max_base_ = max_ + lenY * coord_max_weight_;
			if (checkYmin && min_base_ < 0) {
				max_base_ = max_ + lenY * coord_max_weight_ * (min_) / lenY
						/ coord_min_weight_;
				min_base_ = 0;
				coord_min_weight_ = (min_) / max_base_;
				coord_max_weight_ = (max_base_ - max_) / max_base_;
			}
			curve_shape_ = null;
		}
		scaleY_ = mRecFrame.height() / (max_base_ - min_base_);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (!mRecFrame.isEmpty()) {
			if (enable_grid_) {
				drawGrid(canvas, mPaint);
			} else {
				drawGridColAdXText(canvas, mPaint, null, false);
			}
			drawCoord(canvas, mPaint);
			if (len_ > 0) {
				if (len_ == 1) {
					canvas.drawCircle(mRecFrame.right - 1, transY2Screen(data_
							.get(0).getValue(mCharDataType)), 2, mPaint);
				} else {
					drawCurveQuant(canvas, mPaint);
				}
			}
			drawYText(canvas, mPaint);
		}
	}

	private void drawGrid(Canvas canvas, Paint paint) {
		paint.setColor(grid_color_);
		DashPathEffect pathEffect = null;
		if (enable_grid_dash_) {
			pathEffect = new DashPathEffect(new float[] { 10, 15, 10, 15 },
					0.5f);
			paint.setPathEffect(pathEffect);
		}
		drawGridRow(canvas, paint);
		drawGridColAdXText(canvas, paint, pathEffect, true);
		if (enable_grid_dash_) {
			paint.setPathEffect(null);
		}
	}

	private void drawGridRow(Canvas canvas, Paint paint) {
		float y = mRecFrame.top;
		while (y <= mRecFrame.bottom) {
			canvas.drawLine(mRecFrame.left, y, mRecFrame.right, y, paint);
			y += dw_;
		}
	}

	private void drawGridColAdXText(Canvas canvas, Paint paint,
			DashPathEffect pathEffect, boolean enableGrid) {
		if (!enableGrid) {
			paint.setColor(grid_color_);
			paint.setStrokeWidth(0.75f);
		}
		paint.setTextAlign(Align.CENTER);
		float xOffset = 0;
		float dc = (mRecFrame.width()) / coord_col_;
		float x = mRecFrame.left;
		int i = 0;
		while (x <= mRecFrame.right) {
			if (enableGrid) {
				paint.setColor(grid_color_);
				canvas.drawLine(x, mRecFrame.top, x, mRecFrame.bottom, paint);
			}
			paint.setColor(coord_text_color_);
			paint.setPathEffect(null);
			int id = Math.round((mRecFrame.right - x) / (space_));
			id = Math.max(Math.min(len_ - 1, id), 0);

			String xStr = id < len_ ? timeFormat(data_.get(id).getTime(),
					time_format_) : "--";
			xOffset = 0;
			if (i == 0) {
				xOffset = Math.max(0, paint.measureText(xStr) / 2 - x);
			}
			if (i++ == coord_col_) {
				xOffset = Math.min(0, getWidth() - x - paint.measureText(xStr)
						/ 2);
			}
			canvas.drawText(xStr, x + xOffset, mRecFrame.bottom + text_height_
					+ text_offset_, paint);
			paint.setPathEffect(pathEffect);
			x += dc;
		}
	}

	private void drawCoord(Canvas canvas, Paint paint) {
		paint.setColor(coord_color_);
		paint.setStrokeWidth(2f);
		float top = mRecFrame.top - coord_arrow_size_;
		float right = mRecFrame.right + coord_arrow_size_;
		canvas.drawLine(mRecFrame.left, mRecFrame.bottom, mRecFrame.left, top,
				paint);
		canvas.drawLine(mRecFrame.left, mRecFrame.bottom, right,
				mRecFrame.bottom, paint);

		float arowLen = 6;
		float arowspace = arowLen / 2;

		canvas.drawLine(mRecFrame.left, top, mRecFrame.left - arowspace, top
				+ arowLen, paint);
		canvas.drawLine(mRecFrame.left, top, mRecFrame.left + arowspace, top
				+ arowLen, paint);

		canvas.drawLine(right, mRecFrame.bottom, right - arowLen,
				mRecFrame.bottom - arowspace, paint);
		canvas.drawLine(right, mRecFrame.bottom, right - arowLen,
				mRecFrame.bottom + arowspace, paint);

	}

	private void drawCurveQuant(Canvas canvas, Paint paint) {
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2f);
		paint.setColor(curve_color_);
		float x = mRecFrame.right - 1;
		float val0 = transY2Screen(data_.get(0).getValue(mCharDataType));
		mBezier.setFirstPoint(x, val0, x - space_, transY2Screen(data_.get(1)
				.getValue(mCharDataType)));
		x = x - space_ - space_;
		for (int i = 2; i < len_; i++, x -= space_) {
			mBezier.getNextPath(x,
					transY2Screen(data_.get(i).getValue(mCharDataType)), false);
		}
		mBezier.getLastPath(false);
		canvas.drawPath(mPath, paint);
		mPath.lineTo(mBezier.getCurPoint().x, mRecFrame.bottom-mPaint.getStrokeWidth());
		mPath.lineTo(mRecFrame.right, mRecFrame.bottom-mPaint.getStrokeWidth());
		if (curve_shape_ == null) {
			curve_shape_ = new ShapeDrawable(new PathShape(mPath,
					mRecFrame.width(), mRecFrame.height()));
			LinearGradient linear_shap = new LinearGradient(0, mRecFrame.top,
					0, mRecFrame.bottom, curve_shape_color_start_,
					curve_shape_color_end_, TileMode.CLAMP);
			((ShapeDrawable) curve_shape_).getPaint().setShader(linear_shap);
		}
		curve_shape_.setBounds(0, 0, (int) mRecFrame.width(),
				(int) mRecFrame.height());
		curve_shape_.draw(canvas);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setPathEffect(null);
	}

	private void drawYText(Canvas canvas, Paint paint) {
		paint.setTextAlign(Align.LEFT);
		paint.setStrokeWidth(0.75f);
		paint.setColor(coord_text_color_);
		float txtLeft = mRecFrame.left + coord_text_size_ / 3, y = mRecFrame.top
				- coord_text_size_ / 3;
		float dval = (float) ((max_base_ - min_base_) / (coord_row_)), val = (float) max_base_;
		while (y <= mRecFrame.bottom) {
			canvas.drawText(valueFormat(val, 2), txtLeft, y, paint);
			y += dw_;
			val -= dval;
		}
	}

	public float[] computeCountAndKPwid(float avaiableWid, float scale,
			boolean apply) {
		int count = 0;
		float wid = 0;
		if (scale <= 0) {
			wid = space_ = MAX_KP_WID / 6;
			count = (int) (avaiableWid / wid);
		} else {
			wid = space_ * scale;
			wid = Math.max(Math.min(wid, MAX_KP_WID), MIN_KP_WID);
			count = (int) (avaiableWid / wid);
		}
		if (apply) {
			setRequireCountAndKPwid(count, wid);
		}
		return new float[] { count, wid };
	}

	public void setRequireCountAndKPwid(int requireCount, float wid) {
		space_ = wid;
	}

	@Override
	protected void onFrameSizeChanged(boolean fromUser) {
		Paint p = new Paint();
		p.setTextSize(coord_text_size_ * 1.2f);
		FontMetrics fm = p.getFontMetrics();
		text_height_ = fm.descent - fm.ascent;
		text_offset_ = (fm.ascent + fm.descent) / 2;
		space_ = (mRecFrame.width() - 2) / (len_ - 1);
		dw_ = (mRecFrame.height()) / coord_row_;
		curve_shape_ = null;
	}
}