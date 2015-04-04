package com.howbuy.curve;

public class FunCharType implements ICharType {

	@Override
	public int getColor(int defColor) {
		return 0xff4c77aa;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public String getTag() {
		return "fund_tag";
	}

	@Override
	public String getName() {
		return "fund_name";
	}

}
