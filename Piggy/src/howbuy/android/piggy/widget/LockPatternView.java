/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package howbuy.android.piggy.widget;

import howbuy.android.util.Screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Displays and detects the user's unlock attempt, which is a drag of a finger
 * across 9 regions of the screen.
 * 
 * Is also capable of displaying a static pattern in "in progress", "wrong" or
 * "correct" states.
 */
public class LockPatternView extends View {
	public static final float Square_Cicle_Interval_Constant = 0.3f;
	public static final int type_Big = 0;
	public static final int type_Small = 1;
	public int type_Patten = type_Big;//
	public float Square_Cicle_border = 0.5f;

	private Bitmap mBitmapNormal;
	private Bitmap mBitmapTouch;
	private float mSquareWidth;
	private float mSquareHeight;
	private float mSquareInterval;
	private float mCicleRadius;
	private Paint mPaint = new Paint();
	private Paint mPathPaint = new Paint();
	private static final float DRAG_THRESHHOLD = 0.0f;

	private OnPatternListener mOnPatternListener;
	private ArrayList<Cell> mPattern = new ArrayList<Cell>(9);
	private boolean[][] mPatternDrawLookup = new boolean[3][3];
	private final Path mCurrentPath = new Path();
	private final Matrix mArrowMatrix = new Matrix();
	private final Rect mInvalidate = new Rect();
	private final Rect mTmpInvalidateRect = new Rect();

	private float mInProgressX = -1;
	private float mInProgressY = -1;
	private boolean mPatternInProgress;
	private Screen mScreen;

	public LockPatternView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPaint.setAntiAlias(true);
		mPaint.setColor(0xf9ffffff);
		mPaint.setStyle(Paint.Style.STROKE);

		mPathPaint.setStrokeWidth(25f);
		mPathPaint.setAntiAlias(true);
		mPathPaint.setColor(0x90ffffff);
		mPathPaint.setStyle(Paint.Style.STROKE);
		mPathPaint.setStrokeJoin(Paint.Join.ROUND);
		mPathPaint.setStrokeCap(Paint.Cap.ROUND);
		mScreen=new Screen(context);

	}

	private Bitmap createBitMapNormal() {
		float currWidth = mSquareWidth - mSquareInterval * 2f;
		Bitmap bitmap = Bitmap.createBitmap((int) currWidth, (int) currWidth, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setStyle(Paint.Style.STROKE);
		p.setColor(0xf9ffffff);
		p.setStrokeWidth(Square_Cicle_border);
		p.setShadowLayer(3, 0, 0, 0xf0ffffff);

		canvas.drawCircle(currWidth / 2f, currWidth / 2f, currWidth / 2f - Square_Cicle_border, p);
		return bitmap;
	}

	private Bitmap createBitMapTouch() {
		float currWidth = mSquareWidth - mSquareInterval * 2f;
		// GradientDrawable
		Bitmap bitmap = Bitmap.createBitmap((int) currWidth, (int) currWidth, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		// 背景
		Paint p1 = new Paint();
		p1.setAntiAlias(true);
		p1.setStyle(type_Patten == type_Big?Paint.Style.STROKE:Paint.Style.FILL);
		p1.setColor(0xf9ffffff);
		p1.setStrokeWidth(Square_Cicle_border);
		p1.setShadowLayer(3, 0, 0, 0xf0ffffff);

		canvas.drawCircle(currWidth / 2f, currWidth / 2f, currWidth / 2f - Square_Cicle_border, p1);

		if (type_Patten == type_Big) {
			// 外层圆圈
			Paint p2 = new Paint();
			p2.setColor(0xFFFD6A27);
			p2.setAntiAlias(true);
			p2.setStyle(Paint.Style.FILL);

			// 内外圈
			Paint p3 = new Paint();
			p3.setColor(Color.WHITE);
			p3.setAntiAlias(true);
			p3.setShadowLayer(15, 0, 0, 0xffffffff);
			p3.setStyle(Paint.Style.FILL);


//			Matrix matrix = canvas.getMatrix();
//			matrix.setScale(0.4f, 0.4f);// 缩放0.8倍
			canvas.drawCircle(currWidth / 2f, currWidth / 2f, (currWidth / 2f - Square_Cicle_border)  *0.8f, p2);
//			matrix.setScale(0.15f, 0.15f);// 缩放0.15倍
			canvas.drawCircle(currWidth / 2f, currWidth / 2f, (currWidth / 2f - Square_Cicle_border) * 0.3f, p3);
		}

		return bitmap;
	}

	public LockPatternView(Context context) {
		super(context);
	}

	private int resolveMeasured(int measureSpec) {
		int result = 0;
		int specSize = MeasureSpec.getSize(measureSpec);
		int needSize = (type_Patten == type_Big ? getScWidth() : getScWidth() / 8);
		switch (MeasureSpec.getMode(measureSpec)) {
		case MeasureSpec.UNSPECIFIED:
			result = Math.min(needSize, specSize);
			break;
		case MeasureSpec.AT_MOST:// wrapContent
			result = specSize;
			break;
		case MeasureSpec.EXACTLY:// match parent
			result = Math.min(needSize, specSize);
			break;
		default:
			result = specSize;
		}
		if (mScreen.getWidth()<321) {
			result=result-10;
		}
		return result;
	}

	private void drawCicle(Canvas canvas, float leftX, float topY, boolean isTouch) {
		Bitmap currBitMap = isTouch ? mBitmapTouch : mBitmapNormal;
		mArrowMatrix.setTranslate(leftX, topY);
		mArrowMatrix.postTranslate(mSquareInterval, mSquareInterval);
		canvas.drawBitmap(currBitMap, mArrowMatrix, mPaint);
		// canvas.drawRect(new RectF(leftX, topY, leftX + mSquareWidth, topY +
		// mSquareWidth), mPaint);
	}

	private PointF squareTopLeftToCiclePo(Cell cell) {
		float x = getPaddingLeft() + cell.row * mSquareWidth + mSquareInterval + mCicleRadius;
		float y = getPaddingRight() + cell.column * mSquareHeight + mSquareInterval + mCicleRadius;
		return new PointF(x, y);
	}

	/**
	 * 确定这个点所在九宫格（圆）的地方
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Cell detectCell(float x, float y) {
		int xb = (int) Math.floor((double) ((x - getPaddingLeft()) / mSquareWidth));// 取整x
		int yb = (int) Math.floor((double) ((y - getPaddingLeft()) / mSquareHeight));// 取整y

		if (xb < 0 || yb < 0 || xb > 2 || yb > 2) {
			return null;
		}
		// 圆心
		PointF ciclePointF = squareTopLeftToCiclePo(new Cell(xb, yb));

		// 平方
		float x2 = (float) Math.pow(Math.abs(x - ciclePointF.x), 2);
		float y2 = (float) Math.pow(Math.abs(y - ciclePointF.y), 2);

		// 开根号
		float d = (float) Math.sqrt(x2 + y2);

		if (d > mCicleRadius) {
			return null;
		} else {
			return new Cell(xb, yb);
		}
	}

	private void clearPatternDrawLookup() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				mPatternDrawLookup[i][j] = false;
			}
		}
	}

	private void addCellToPattern(Cell newCell) {
		if (newCell == null) {
			return;
		}
		boolean isAdded = mPatternDrawLookup[newCell.getRow()][newCell.getColumn()];
		if (isAdded == false) {
			if (mPattern.size() > 0) {
				Cell preCell = mPattern.get(mPattern.size() - 1);
				boolean direction;
				boolean directionX;
				ArrayList<Cell> middleCells = new ArrayList<LockPatternView.Cell>();
				if (preCell != null) {
					if (newCell.row == preCell.row) {
						int columnNum = newCell.column - preCell.column;
						direction = columnNum > 0;
						columnNum = Math.abs(columnNum) - 1;
						if (columnNum > 0) {
							for (int i = 1; i < columnNum + 1; i++) {
								Cell middle;
								if (direction) {// 正反向滑动
									middle = new Cell(newCell.row, preCell.column + i);
								} else {
									middle = new Cell(newCell.row, preCell.column - i);
								}
								mPatternDrawLookup[middle.getRow()][middle.getColumn()] = true;
							}
						}
					} else if (newCell.column == preCell.column) {
						int rowNum = newCell.row - preCell.row;
						direction = rowNum > 0;
						rowNum = Math.abs(rowNum) - 1;
						if (rowNum > 0) {
							for (int i = 1; i < rowNum + 1; i++) {
								Cell middle;
								if (direction) {
									middle = new Cell(preCell.row + i, newCell.column);
								} else {
									middle = new Cell(preCell.row - i, newCell.column);
								}
								middleCells.add(middle);
								mPatternDrawLookup[middle.getRow()][middle.getColumn()] = true;
							}
						}
					} else {
						PointF p1 = squareTopLeftToCiclePo(preCell);
						PointF p2 = squareTopLeftToCiclePo(newCell);
						float x = Math.abs(p1.x - p2.x);
						float y = Math.abs(p1.y - p2.y);
						// 两个点之间的距离
						float distanceCurr = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
						// 一个square对角线的长度
						float distanceSquare = (float) Math.sqrt(Math.pow(mSquareWidth, 2) + Math.pow(mSquareHeight, 2));
						int distanceNum = (int) Math.floor(distanceCurr / (distanceSquare - 0.01f)) - 1;
						// 1.舍去小数向下取整：Math.floor(3.5) = 3;
						// 2.四舍五入取整 Math.rint(3.5) = 4;
						// 3.进位取整即向上取整：Math.ceil(3.1) = 4;
						directionX = (newCell.row - preCell.row) > 0;
						direction = (newCell.column - preCell.column) > 0;
						for (int i = 1; i < distanceNum + 1; i++) {
							Cell middle;
							int r = directionX ? preCell.row + i : preCell.row - i;
							int c = direction ? preCell.column + i : preCell.column - i;
							middle = new Cell(r, c);
							if (!mPatternDrawLookup[middle.getRow()][middle.getColumn()]) {
								middleCells.add(middle);
								mPatternDrawLookup[middle.getRow()][middle.getColumn()] = true;
							}
						}
					}

					// 不止3X3的话这里要变
					if (middleCells.size() != 0) {
						mPattern.addAll(middleCells);
					}
				}
			}

			mPatternDrawLookup[newCell.getRow()][newCell.getColumn()] = true;
			mPattern.add(newCell);
		}
	}

	private void handActionMove(MotionEvent event) {
		final int historySize = event.getHistorySize();
		mTmpInvalidateRect.setEmpty();
		boolean invalidateNow = false;
		for (int i = 0; i < historySize + 1; i++) {
			final float x = i < historySize ? event.getHistoricalX(i) : event.getX();
			final float y = i < historySize ? event.getHistoricalY(i) : event.getY();
			Cell hitCell = detectCell(event.getX(), event.getY());
			addCellToPattern(hitCell);
			final int patternSize = mPattern.size();
			if (hitCell != null && patternSize == 1) {
				mPatternInProgress = true;
				// notifyPatternStarted();
			}
			// note current x and y for rubber banding of in progress patterns
			final float dx = Math.abs(x - mInProgressX);
			final float dy = Math.abs(y - mInProgressY);
			if (dx > DRAG_THRESHHOLD || dy > DRAG_THRESHHOLD) {
				invalidateNow = true;
			}

			if (mPatternInProgress && patternSize > 0) {
				final ArrayList<Cell> pattern = mPattern;
				final Cell lastCell = pattern.get(patternSize - 1);
				PointF p = squareTopLeftToCiclePo(lastCell);
				float lastCellCenterX = p.x;
				float lastCellCenterY = p.y;

				// Adjust for drawn segment from last cell to (x,y). Radius
				// accounts for line width.
				float left = Math.min(lastCellCenterX, x) - mCicleRadius;
				float right = Math.max(lastCellCenterX, x) + mCicleRadius;
				float top = Math.min(lastCellCenterY, y) - mCicleRadius;
				float bottom = Math.max(lastCellCenterY, y) + mCicleRadius;

				// Invalidate between the pattern's new cell and the pattern's
				// previous cell
				if (hitCell != null) {
					final float width = mSquareWidth * 0.5f;
					final float height = mSquareHeight * 0.5f;
					PointF p2 = squareTopLeftToCiclePo(lastCell);
					final float hitCellCenterX = p2.x;
					final float hitCellCenterY = p2.y;

					left = Math.min(hitCellCenterX - width, left);
					right = Math.max(hitCellCenterX + width, right);
					top = Math.min(hitCellCenterY - height, top);
					bottom = Math.max(hitCellCenterY + height, bottom);
				}

				// Invalidate between the pattern's last cell and the previous
				// location
				mTmpInvalidateRect.union(Math.round(left), Math.round(top), Math.round(right), Math.round(bottom));
			}
		}
		mInProgressX = event.getX();
		mInProgressY = event.getY();

		// To save updates, we only invalidate if the user moved beyond a
		// certain amount.
		if (invalidateNow) {
			mInvalidate.union(mTmpInvalidateRect);
			invalidate(mInvalidate);
			mInvalidate.set(mTmpInvalidateRect);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// handActionDown(event);
			handActionMove(event);
			break;
		case MotionEvent.ACTION_UP:
			if (type_Patten == type_Big) {
				if (null != mOnPatternListener) {
					mOnPatternListener.onPatternDetected(mPattern);
				}
				clearPatternDrawLookup();
				mPattern.clear();
				mCurrentPath.reset();
				mPatternInProgress = false;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (type_Patten == type_Small) {
				return false;
			}
			handActionMove(event);
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (type_Patten == type_Big) {
			Square_Cicle_border = 4f;
		} else {
			Square_Cicle_border = 1f;
		}
		final int width = w - getPaddingLeft() - getPaddingRight();
		mSquareWidth = width / 3.0f;

		final int height = h - getPaddingTop() - getPaddingBottom();
		mSquareHeight = height / 3.0f;
		mSquareInterval = mSquareWidth / 2f * Square_Cicle_Interval_Constant;// 0.5倍

		mCicleRadius = (mSquareWidth - mSquareInterval * 2f) / 2f;

		if (w != 0) {
			mBitmapNormal = createBitMapNormal();
			mBitmapTouch = createBitMapTouch();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int viewWidth = resolveMeasured(widthMeasureSpec);
		int viewHeight = resolveMeasured(heightMeasureSpec);
		int wH = Math.min(viewHeight, viewWidth);
		setMeasuredDimension(wH, wH);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mBitmapNormal == null) {
			return;
		}

		float leftX = 0, topY = 0;

		// drawCicle
		for (int i = 0; i < mPatternDrawLookup.length; i++) {
			leftX = getPaddingLeft() + i * mSquareWidth;
			for (int j = 0; j < mPatternDrawLookup[i].length; j++) {
				topY = getPaddingLeft() + j * mSquareHeight;
				if (mPatternDrawLookup[i][j] == true) {
					drawCicle(canvas, leftX, topY, true);
				} else {
					drawCicle(canvas, leftX, topY, false);
				}
			}
		}

		if (type_Patten == type_Small) {
			return;
		}

		// drawPath
		if (mPatternInProgress) {
			final Path currPath = mCurrentPath;
			currPath.rewind();
			for (int i = 0; i < mPattern.size(); i++) {
				Cell c = mPattern.get(i);
				if (c != null) {
					PointF p = squareTopLeftToCiclePo(c);
					if (i == 0) {
						currPath.moveTo(p.x, p.y);
					} else {
						currPath.lineTo(p.x, p.y);
					}
				}
			}
			currPath.lineTo(mInProgressX, mInProgressY);
			canvas.drawPath(currPath, mPathPaint);
		}
	}

	public static interface OnPatternListener {
		void onPatternDetected(List<Cell> pattern);
	}

	public static class Cell {
		int row;
		int column;
		static Cell[][] sCells = new Cell[3][3];
		static {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					sCells[i][j] = new Cell(i, j);
				}
			}
		}

		private Cell(int row, int column) {
			checkRange(row, column);
			this.row = row;
			this.column = column;
		}

		public int getRow() {
			return row;
		}

		public int getColumn() {
			return column;
		}

		public static synchronized Cell of(int row, int column) {
			checkRange(row, column);
			return sCells[row][column];
		}

		private static void checkRange(int row, int column) {
			if (row < 0 || row > 2) {
				throw new IllegalArgumentException("row must be in range 0-2");
			}
			if (column < 0 || column > 2) {
				throw new IllegalArgumentException("column must be in range 0-2");
			}
		}

		public String toString() {
			return "(row=" + row + ",clmn=" + column + ")";
		}
	}

	public enum DisplayMode {
		Correct, Animate, Wrong
	}

	public int getScWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}

	public int getScHeight() {
		return getResources().getDisplayMetrics().heightPixels;
	}

	public int dip2px(float dipValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public int getType_Patten() {
		return type_Patten;
	}

	public void setType_Patten(int type_Patten) {
		this.type_Patten = type_Patten;
		requestLayout();
		invalidate();
	}

	public ArrayList<Cell> getmPattern() {
		return mPattern;
	}

	public void setPattern(ArrayList<Cell> mPattern) {
		if (mPattern.size() == 0 || mPattern.size() > 9) {
			return;
		}
		this.mPattern.addAll(mPattern);
		for (int i = 0; i < mPattern.size(); i++) {
			Cell c = mPattern.get(i);
			mPatternDrawLookup[c.row][c.column] = true;
		}
		invalidate();
	}

	public OnPatternListener getmOnPatternListener() {
		return mOnPatternListener;
	}

	public void setmOnPatternListener(OnPatternListener mOnPatternListener) {
		this.mOnPatternListener = mOnPatternListener;
	}

	public void clearPattern() {
		clearPatternDrawLookup();
		mPattern.clear();
		invalidate();
	}

}