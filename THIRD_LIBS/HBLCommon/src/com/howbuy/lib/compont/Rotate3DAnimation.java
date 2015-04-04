package com.howbuy.lib.compont;



import android.annotation.SuppressLint;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;
/**
 * @author rexy  840094530@qq.com 
 * @date 2014-1-14 上午10:41:18
 */
public class Rotate3DAnimation extends Animation {
	private float mFromXDegrees, mToXDegrees;
	private float mFromYDegrees, mToYDegrees;
	private float mFromZDegrees, mToZDegrees;
	private float mCenterX = 0.5f;
	private float mCenterY = 0.5f;
	private int mCenterXType = Animation.RELATIVE_TO_SELF;
	private int mCenterYType = Animation.RELATIVE_TO_SELF;
	private Camera mCamera;

	/**
	 * @param fromX X轴旋转起点角度.
	 * @param toX   X轴旋转终点角度. 
	 */
	public void setRoateX(float fromX, float toX) {
		mFromXDegrees = fromX;
		mToXDegrees = toX; 
	}

	/** 
	 * @param fromY Y轴旋转起点角度.
	 * @param toY   Y轴旋转终点角度. 
	 */
	public void setRoateY(float fromY, float toY) {
		mFromYDegrees = fromY;
		mToYDegrees = toY;
	}

	/**
	 * @param fromZ Z轴旋转起点角度.
	 * @param toZ   Z轴旋转终点角度.
	 */
	public void setRoateZ(float fromZ, float toZ) {
		mFromZDegrees = fromZ;
		mToZDegrees = toZ;
	}

	/**
	 * @param @param mCenXType   one of Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_PARENT,Animation.ABSOLUTE;
	 * @param @param cx  如果是相对类型,参数在0和1之间.
	 * @param @param mCenYType   one of Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_PARENT,Animation.ABSOLUTE;
	 * @param @param cy 如果是相对类型,参数在0和1之间.
	 * @throws
	 */
	public void setRoateCenter(int mCenXType, float cx, int mCenYType, float cy) {
		mCenterX = cx;
		mCenterY = cy;
		mCenterXType = mCenXType;
		mCenterYType = mCenYType;  
	}

	/** 
	 * @param fromZDegree Z轴旋转起点角度.
	 * @param toZDegree   Z轴旋转终点角度.
	 */
	public Rotate3DAnimation(float fromXDegree, float toXDegree,float fromYDegree, float toYDegree) {
	  this(fromXDegree, toXDegree, fromYDegree, toYDegree, 0, 0);
	}
	/**
	 * @param fromXDegree X轴旋转起点角度.
	 * @param toXDegree   X轴旋转终点角度.
	 * @param fromYDegree Y轴旋转起点角度.
	 * @param toYDegree   Y轴旋转终点角度. 
	 */
	public Rotate3DAnimation(float fromZDegree, float toZDegree) {
		this(0, 0, 0, 0, fromZDegree, toZDegree);
	}
	/**
	 * @param fromXDegree X轴旋转起点角度.
	 * @param toXDegree   X轴旋转终点角度.
	 * @param fromYDegree Y轴旋转起点角度.
	 * @param toYDegree   Y轴旋转终点角度.
	 * @param fromZDegree Z轴旋转起点角度.
	 * @param toZDegree   Z轴旋转终点角度.
	 */
	public Rotate3DAnimation(float fromXDegree, float toXDegree,float fromYDegree, float toYDegree,float fromZDegree, float toZDegree) {
		mFromXDegrees = fromXDegree;
		mToXDegrees = toXDegree;
		mFromYDegrees=fromYDegree;
		mToYDegrees=toYDegree;
		mFromZDegrees=fromZDegree;
		mToZDegrees=toZDegree;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
		mCenterX = resolveSize(mCenterXType, mCenterX, width, parentWidth);
		mCenterY = resolveSize(mCenterYType, mCenterY, height, parentHeight);
	}

	// 生成Transformation
	@SuppressLint("NewApi")
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float degX = mFromXDegrees + (mToXDegrees - mFromXDegrees)
				* interpolatedTime;
		float degY = mFromYDegrees + (mToYDegrees - mFromYDegrees)
				* interpolatedTime;
		float degZ = mFromZDegrees + (mToZDegrees - mFromZDegrees)
				* interpolatedTime;
		final Matrix matrix = t.getMatrix();
		mCamera.save();
		if (android.os.Build.VERSION.SDK_INT < 12) {
			mCamera.rotateX(degX);
			mCamera.rotateY(degY);
			mCamera.rotateZ(degZ);
		} else {
			mCamera.rotate(degX, degY, degZ);
		}
		mCamera.getMatrix(matrix);
		mCamera.restore();
		matrix.preTranslate(-mCenterX, -mCenterY);// 将操作标平移到(0,0).
		matrix.postTranslate(mCenterX, mCenterY);// 操作完成后平移回原中心.
	}
}