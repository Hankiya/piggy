package howbuy.android.piggy.api.dto;

import java.util.ArrayList;

/**
 * 支行列表
 * 
 * @ClassName: SupportBankBranchListDto.java
 * @Description:
 * @author yescpu yes.cpu@gmail.com
 * @date 2013-10-12上午10:02:01
 */
public class SupportBankBranchListDto extends TopHeaderDto {
	ArrayList<SupportBankBranchDto> supportBankBranchDtos;

	public ArrayList<SupportBankBranchDto> getSupportBankBranchDtos() {
		return supportBankBranchDtos;
	}

	public void setSupportBankBranchDtos(ArrayList<SupportBankBranchDto> supportBankBranchDtos) {
		this.supportBankBranchDtos = supportBankBranchDtos;
	}

}
