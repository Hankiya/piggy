package howbuy.android.piggy.sound;

import howbuy.android.piggy.application.ApplicationParams;
import howbuy.android.util.Cons;
import android.content.Context;
import android.media.MediaPlayer;

public class SoundUtil {
	private final Context mContext;
	private MediaPlayer mCurrentMediaPlayer;

	public SoundUtil(Context context) {
		mContext = context;
	}

	public MediaPlayer getCurrentMediaPlayer() {
		return mCurrentMediaPlayer;
	}

	public void releaseSound() {
		if (mCurrentMediaPlayer != null) {
			mCurrentMediaPlayer.release();
		}
	}

	public void playSound(int resId) {
		boolean soundflag = ApplicationParams.getInstance().getsF().getBoolean(Cons.SFSoundFlag, true);
		if (!soundflag) {
			return;
		}

		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.stop();
			mCurrentMediaPlayer.release();
		}
		mCurrentMediaPlayer = MediaPlayer.create(mContext, resId);
		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.start();
		}
	}

}
