package com.howbuy.frag.control;

import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ToggleButton;

import com.howbuy.lib.control.CircleMenu;
import com.howbuy.lib.control.Sector;
import com.howbuy.lib.control.CircleMenu.ICircleMenuEvent;
import com.howbuy.lib.frag.AbsFrag;
import com.howbuy.lib.utils.LogUtils;
import com.howbuy.lib.utils.ViewUtils;
import com.howbuy.libtest.R;
import com.renzh.lib.adp.CircleMenuAdp;  

 public class FragControlCircleMenu extends AbsFrag implements ICircleMenuEvent  {
		private CircleMenu mMenus = null;
	    private CircleMenuAdp mAdpter;
	@Override
	protected int getFragLayoutId() {
		return R.layout.frag_control_circlemenu;
	}

	@Override
	protected void initViewAdAction(View root, Bundle bundle) {
		mMenus = (CircleMenu)root.findViewById(R.id.lay_menu);
		mMenus.setAdapter(buildMenu(5));
		mMenus.setSoundRes(R.raw.tock);
		mMenus.setInterpolator(new DecelerateInterpolator());	
	}
	private CircleMenuAdp buildMenu(int n){
		mAdpter=new CircleMenuAdp( null);
		Sector sector=new Sector(0,0,-1);
		sector.setText("n1", "p1", "d1");
		sector.addFlags(Sector.FLAG_ENABLE_TEXT, -1);
		sector.setBmpRes(R.drawable.btn_01, R.drawable.btn_01b, 0);
		mAdpter.addItem(sector, true, false);
		sector=new Sector(1,0,-1);
		sector.setText("n2", "p2", "d2");
		//sector.setBmpRes(R.drawable.btn_02, R.drawable.btn_02b, 0);
		mAdpter.addItem(sector, true, false);
		sector=new Sector(2,0,-1);
		//sector.setText("n3", "p3", "d3");
		sector.setBmpRes(R.drawable.btn_03, R.drawable.btn_03b, 0);
		mAdpter.addItem(sector, true, false);
		sector=new Sector(3,0,-1);
		sector.setText("n4", "p4", "d4");
		//sector.setBmpRes(R.drawable.btn_04, R.drawable.btn_04b, 0);
		sector.addFlags(Sector.FLAG_FORCE_REPLACE_IFBMPNULL, 1);
		mAdpter.addItem(sector, true, false);
		sector=new Sector(4,0,-1);
		sector.setText("n5", "p5", "d5");
		sector.setBmpRes(R.drawable.btn_05, R.drawable.btn_05b, 0);
		mAdpter.addItem(sector, true, false);
		
		sector=new Sector(5,0,-1);
		sector.setText("n6", "p6", "d6");
		sector.setBmpRes(R.drawable.btn_06, R.drawable.btn_06b, 0);
		mAdpter.addItem(sector, true, false);
		mAdpter.notifyDataChanged(true);
		
		sector=new Sector(6,0,-1);
		sector.setText("n7", "p7", "d7");
		sector.setBmpRes(R.drawable.ic_launcher, R.drawable.ic_launcher, 0);
		sector.addFlags(Sector.FLAG_FORCE_REPLACE_IFBMPNULL, 1);
		sector.setColor(0xffaa00aa, 0xff880088, 0xff770077);
		mMenus.setCenterSector(sector);
		sector=new Sector(7,0,-1);
		sector.setColor(0x77000000, 0xaa000000, 0xaa333333);
		mMenus.setSelectSector(sector);
		mMenus.setCircleMenuEvent(this);
		return mAdpter;
	}

	@Override
	public boolean onXmlBtClick(View v) {
		if(v instanceof ToggleButton){
			boolean has=((ToggleButton) v).isChecked();
			int flag=0;
			switch(v.getId()){
			case R.id.bt_enable:{
				mMenus.setEnabled(has);
				break;
			}
			
			case R.id.bt_select_center:{
				 flag=CircleMenu.FLAG_CLICK_CENTER;
				break;
			}
			case R.id.bt_select_sector:{
				 flag=CircleMenu.FLAG_CLICK_SECTOR;
				break;
			}
			case R.id.bt_keep_select:{
				 flag=CircleMenu.FLAG_ENABLE_SELECT;
				break;
			}
			case R.id.bt_roate_backgroud:{
				 flag=CircleMenu.FLAG_ROATE_BACKGROUD;
				break;
			}
			case R.id.bt_roate_select:{
				 flag=CircleMenu.FLAG_ROATE_SECTOR;
				break;
			}
			case R.id.bt_sound:{
				 flag=CircleMenu.FLAG_ENABLE_SOUND;
				break;
			}
			case R.id.bt_roate_enable:{
				flag=CircleMenu.FLAG_ENABLE_ROATE;
				break;
			}
			}
			int newFlags= opFlag(mMenus.getFlag(), flag, has);
			if(newFlags!=mMenus.getFlag()){
				mMenus.setMenuAction(newFlags);
				return true;
			} 
		}
		return false;
	}
	private int opFlag(int value,int flag,boolean has){
		if(has){
			return ViewUtils.addFlag(value, flag);
		}else{
			return ViewUtils.subFlag(value, flag);
		}
	}
	
	@Override
	public void onMenuClick(CircleMenu view, Sector s, int i,boolean isOpend) {
		if(i==-2){
			if(isOpend){
				mMenus.toggleMenu(false, true);
			}else{
				mMenus.toggleMenu(true, true);
			}
		}else{
			pop("onMenuClick key="+s.getKey()+" index="+i,true);
		}
	}
	
	@Override
	public void onMenuPassIndicator(CircleMenu v, int pre, int cur) {
	}
	@Override
	public float onPrepareRoate(float roat, Sector indicatorSector, int index) {
		return roat;
	}
	
	
 }