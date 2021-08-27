package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4965ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 * SearchFlag=9,1<br>
 * ClCode1=X,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * ApplNo=9,7<br>
 * NowInsuNo=X,16<br>
 * InsuCompany=X,2<br>
 * InsuTypeCode=X,2<br>
 * InsuEndDateFrom=9,7<br>
 * InsuEndDateTo=9,7<br>
 * END=X,1<br>
 */

@Service("L4965")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4965 extends TradeBuffer {

	@Autowired
	public L4965ServiceImpl l4965ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4965 ");
		this.totaVo.init(titaVo);

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		List<String[]> dataL4965 = l4965ServiceImpl.findData(this.index, this.limit, titaVo);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (dataL4965 != null && dataL4965.size() == this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		if (this.index == 0 && (dataL4965 == null || dataL4965.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "");
		}

		for (String[] data : dataL4965) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNo", data[0]);
			occursList.putParam("OOCustName", data[1]);
			occursList.putParam("OOFacmNo", data[2]);
			occursList.putParam("OOApplNo", data[3]);
			occursList.putParam("OOClCode1", data[4]);
			occursList.putParam("OOClCode2", data[5]);
			occursList.putParam("OOClNo", data[6]);
			occursList.putParam("OONowInsuNo", data[7]);
			occursList.putParam("OOEndoInsuNo", data[8]); 
			occursList.putParam("OOInsuCompany", data[9]); 
			occursList.putParam("OOFireInsuCovrg", data[10]);
			occursList.putParam("OOFireInsuPrem", data[11]);
			occursList.putParam("OOEthqInsuCovrg", data[12]);
			occursList.putParam("OOEthqInsuPrem", data[13]);
			occursList.putParam("OOInsuStartDate", data[14]);
			occursList.putParam("OOInsuEndDate", data[15]);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}