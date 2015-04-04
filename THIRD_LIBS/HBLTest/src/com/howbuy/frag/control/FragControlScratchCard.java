 package com.howbuy.frag.control;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.howbuy.lib.control.ScratchCard;
import com.howbuy.lib.control.ScratchCard.IScratchListener;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.libtest.R;


public class FragControlScratchCard extends AbsFrag implements   IScratchListener{
    ScratchCard mCard=null;
    TextView mTvCard=null;
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_scratchcard;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mCard=(ScratchCard) root.findViewById(R.id.scratchcard);
		mTvCard=(TextView) root.findViewById(R.id.tv_card);
		mCard.setScratchListener(this);
	}
	@Override
	public void onScratchChanged(ScratchCard card, float rate) {
	  if(rate>0.7f){
		  mCard.removeCardInAnim(500);
		  mCard.setEnabled(false);
	  }else{
		  mTvCard.setText("rect:"+mCard.getRecordRect().size()+" area:"+rate); 
	  }
	}
	public void onScratchRemoved(){
		mCard.resetCard(true, false);
	}

	@Override
	public boolean onXmlBtClick(View v) {
	  if(v.getId()==R.id.bt_reset_card){
		  mCard.resetCard(true, false);
		  return true;
	  }
	  return false;
	}
	
}