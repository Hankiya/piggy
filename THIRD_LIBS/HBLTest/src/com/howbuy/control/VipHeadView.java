package com.howbuy.control;

import java.io.File;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.howbuy.entity.VipInf;
import com.howbuy.lib.control.ElasticLayout.IScrollable;
import com.howbuy.lib.entity.AbsResult;
import com.howbuy.lib.entity.ClickType;
import com.howbuy.lib.interfaces.IReqNetFinished;
import com.howbuy.lib.interfaces.IStreamParse;
import com.howbuy.lib.net.AsyReqHelper;
import com.howbuy.lib.net.ReqNetOpt;
import com.howbuy.lib.net.ReqResult;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.PathUtils;
import com.howbuy.lib.utils.SysUtils;
import com.howbuy.libtest.R;

public class VipHeadView extends RelativeLayout implements View.OnClickListener,
		View.OnLongClickListener, IReqNetFinished, IStreamParse, IScrollable {

	// 修改后做标志FLAG_MODIFY此时没有FLAG_UPLOADING,上传过程中加上FLAG_UPLOADING,上传返回后.同时移除FLAG_MODIFY和FLAG_UPLOADING;
	// 根据上传结果设置FLAG_UPLOAD_SUCCESS
	private static String TAG = null;
	private static final int FLAG_MODIFY_NICKNAME = 1;
	private static final int FLAG_MODIFY_PHOTO = 2;
	private static final int FLAG_UPLOADING_NICKNAME = 4;
	private static final int FLAG_UPLOADING_PHOTO = 8;
	private static final int FLAG_UPLOAD_SUCCESS_NICKNAME = 16;
	private static final int FLAG_UPLOAD_SUCCESS_PHOTO = 32;
	private static final int FLAG_LOADING_VIP = 64;
	private static final int FLAG_LOAD_SUCCESS_VIP = 128;
	private static final int FLAG_LOAD_DB = 256;
	private static final int FLAG_OPT_AUTO_UPLOAD = 512;
	private static final int FLAG_OPT_KEEP_MODIFY = 1024;

	public static final int REQUEST_PICKPIC_GALLERY = 1000;
	public static final int REQUEST_PICKPIC_CAMERA = REQUEST_PICKPIC_GALLERY + 1;
	public static final int REQUEST_PICKPIC_CUT = REQUEST_PICKPIC_GALLERY + 2;
	private int mFlag = FLAG_UPLOAD_SUCCESS_NICKNAME | FLAG_UPLOAD_SUCCESS_PHOTO
			| FLAG_LOAD_SUCCESS_VIP;

	private Activity mAty;
	private VipInf mVipInf;
	private int mResDef = 0;
	private boolean mEnableCut = true;
	private int mCutX = 150, mCutY = 150;
	private String mPhotoPath;
	private Bitmap mBmpPhoto;
	private IVipHeadEvent mListen;

	private ImageView mIvPhoto = null;
	private View mLayLeft, mVHover;

	public void setVipHeadEvent(IVipHeadEvent l) {
		this.mListen = l;
	}

	public boolean hasFlag(int flag) {
		return flag == (mFlag & flag);
	}

	private void addFlag(int flag) {
		mFlag |= flag;
	}

	private void subFlag(int flag) {
		mFlag &= ~flag;
	}

	private void setFlag(int flag) {
		mFlag = flag;
	}

	public boolean hasFlag(int result, int flag) {
		return flag == (result & flag);
	}

	public boolean isKeepUploaded() {
		return hasFlag(FLAG_OPT_KEEP_MODIFY);
	}

	public void setKeepUpload(boolean isAuto) {
		if (isAuto) {
			addFlag(FLAG_OPT_KEEP_MODIFY);
		} else {
			subFlag(FLAG_OPT_KEEP_MODIFY);
		}
	}

	public boolean isAutoUploaded() {
		return hasFlag(FLAG_OPT_AUTO_UPLOAD);
	}

	public void setUploadeAuto(boolean isAuto) {
		if (isAuto) {
			addFlag(FLAG_OPT_AUTO_UPLOAD);
		} else {
			subFlag(FLAG_OPT_AUTO_UPLOAD);
		}
	}

	public void setPhotoDefRes(int resId) {
		mResDef = resId;
	}

	public VipHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TAG = this.getClass().getSimpleName();
	}

	public boolean loadDataFromDB() {
		return false;
	}

	public void writeDataToDB() {

	}

	public void setVipHeadPhoto(Bitmap bmp, String fullPath) {
		if (bmp != null) {
			mIvPhoto.setImageBitmap(bmp);
			addFlag(FLAG_MODIFY_PHOTO);
			if (mListen != null) {
				if (hasFlag(FLAG_OPT_AUTO_UPLOAD)) {
					mListen.onPreRequest(IVipHeadEvent.UPLOADED_PHOTO, bmp);
					addFlag(FLAG_UPLOADING_PHOTO);
					ReqNetOpt opt = new ReqNetOpt(0,"upload photo", FLAG_UPLOADING_PHOTO, "url");
					opt.setPareser(this);
					opt.addFlag(ReqNetOpt.FLAG_REQ_POST);
					AsyReqHelper.getInstance().doRequest(null, this, null);
				}
			}
		}
	}

	private void initUIControl() {
		mLayLeft = findViewById(R.id.vip_head_lay_left);
		mIvPhoto = (ImageView) mLayLeft.findViewById(R.id.vip_head_iv_photo);
		mVHover = mLayLeft.findViewById(R.id.vip_head_v_hover);
	}

	private void initUIAction() {
		mVHover.setOnClickListener(this);
		mVHover.setOnLongClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mVHover)) {
			if (mListen != null) {
				mListen.onVipHeadPhotoClick(ClickType.CLICK_ONCE);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v.equals(mVHover)) {
			if (mListen != null) {
				mListen.onVipHeadPhotoClick(ClickType.CLICK_LONE);
			}
			showPickImageWay();
		}
		return true;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	protected void onAttachedToWindow() {
		initUIControl();
		initUIAction();
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mBmpPhoto != null && !mBmpPhoto.isRecycled()) {
			mBmpPhoto.recycle();
			mBmpPhoto = null;
		}
		super.onDetachedFromWindow();
	}

	public interface IVipHeadEvent {
		public static int UPLOADED_PHOTO = 1;
		public static int UPLOADED_NICKNAME = 2;
		public static int LOADED_VIPINF = 3;

		public void onPreRequest(int requestType, Object obj);

		public void onVipHeadLoaded(AbsResult<VipInf> result, boolean fromNet);

		public void onVipHeadUploaded(int uploadedType, Object obj, Exception e);

		public void onVipHeadPhotoClick(ClickType clickType);

		public Activity onStartAtyForResult(Intent tent, int requestCode);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_PICKPIC_GALLERY: {
			if (Activity.RESULT_OK == resultCode) {
				Uri uri = data.getData();
				mPhotoPath = getPathFromUri(uri);
				if (mPhotoPath == null) {
					mPhotoPath = uri.getPath();
				}
				if (mEnableCut) {
					startPhotoZoom(uri);
				}
			}
		}
			break;
		case REQUEST_PICKPIC_CAMERA: {
			if (Activity.RESULT_OK == resultCode) {
				File temp = new File(mPhotoPath);
				if (mEnableCut) {
					startPhotoZoom(Uri.fromFile(temp));
				}
			}
		}
			break;
		case REQUEST_PICKPIC_CUT: {
			if (Activity.RESULT_OK == resultCode) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						setVipHeadPhoto((Bitmap) extras.getParcelable("data"), mPhotoPath);
					}
				}
			}
		}
			break;
		}

	}

	private void showPickImageWay() {
		Dialog dialog = new AlertDialog.Builder(getContext()).setTitle("选择照片来源")
				.setItems(new String[] { "相册", "拍照", "取消" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (which) {
						case 0:
							Intent tg = new Intent(Intent.ACTION_PICK, null);
							tg.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									"image/*");
							if (mListen != null) {
								mAty = mListen.onStartAtyForResult(tg, REQUEST_PICKPIC_GALLERY);
							}
							break;
						case 1:
							Intent tc = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							mPhotoPath = PathUtils.buildPath(PathUtils.PATH_SCREENHOT,
									"camera_temp.jpg");
							File temp = new File(mPhotoPath);
							temp.getParentFile().mkdirs();
							tc.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
							if (mListen != null) {
								mAty = mListen.onStartAtyForResult(tc, REQUEST_PICKPIC_CAMERA);
							}
							break;
						case 2:
							dialog.dismiss();
							break;
						default:
							break;
						}
					}
				}).create();
		dialog.show();
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", mCutX);
		intent.putExtra("aspectY", mCutY);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", mCutX);
		intent.putExtra("outputY", mCutY);
		intent.putExtra("return-data", true);
		if (mListen != null) {
			mListen.onStartAtyForResult(intent, REQUEST_PICKPIC_CUT);
		}
	}

	private String getPathFromUri(Uri uri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = mAty.managedQuery(uri, proj, // Which columns to return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			if (column_index != -1)
				return cursor.getString(column_index);
		}
		return null;
	}

	@Override
	public Object parseToObj(InputStream is, ReqNetOpt opt) throws Exception {

		return null;
	}

	@Override
	public void onRepNetFinished(ReqResult<ReqNetOpt> result) {
		int handType = result.mReqOpt.getHandleType();
		if (hasFlag(handType, FLAG_UPLOADING_PHOTO | FLAG_UPLOADING_NICKNAME)) {// 同时上传图像和昵称。
			subFlag(FLAG_UPLOADING_PHOTO | FLAG_UPLOADING_NICKNAME);
			if (result.isSuccess()) {
				addFlag(FLAG_UPLOAD_SUCCESS_PHOTO | FLAG_UPLOAD_SUCCESS_NICKNAME);
			} else {
				subFlag(FLAG_UPLOAD_SUCCESS_PHOTO | FLAG_UPLOAD_SUCCESS_NICKNAME);
			}
		} else {
			if (hasFlag(handType, FLAG_UPLOADING_PHOTO)) {// 上传图像。
				subFlag(FLAG_UPLOADING_PHOTO);
				if (result.isSuccess()) {
					addFlag(FLAG_UPLOAD_SUCCESS_PHOTO);
				} else {
					subFlag(FLAG_UPLOAD_SUCCESS_PHOTO);
				}
			} else if (hasFlag(handType, FLAG_UPLOADING_NICKNAME)) {// 上传昵称。
				subFlag(FLAG_UPLOADING_NICKNAME);
				if (result.isSuccess()) {
					addFlag(FLAG_UPLOAD_SUCCESS_NICKNAME);
				} else {
					subFlag(FLAG_UPLOAD_SUCCESS_NICKNAME);
				}
			} else if (hasFlag(handType, FLAG_LOADING_VIP)) {// 获取vip信息。
				subFlag(FLAG_LOADING_VIP);
				if (result.isSuccess()) {
					addFlag(FLAG_LOAD_SUCCESS_VIP);
				} else {
					subFlag(FLAG_LOAD_SUCCESS_VIP);
				}
			}
		}

	}

	@Override
	public boolean isScrollable(int scrollType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean hasScroll(int val, int type) {
		return type == (type & val);
	}
}
