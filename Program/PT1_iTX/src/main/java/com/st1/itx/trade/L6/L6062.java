package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdIndustry;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdIndustryService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * IndustryCode=X,6<br>
 * END=X,1<br>
 */

@Service("L6062") // 行業別代號資料檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6062 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	private CdIndustryService sCdIndustryService;
	@Autowired
	private CdEmpService cdEmpService;
	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6062 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iIndustryCode = titaVo.getParam("IndustryCode").trim();
		String iIndustryItem = titaVo.get("IndustryItem").trim();

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 107 * 200 = 21400

		// 查詢行業別代號資料檔
		Slice<CdIndustry> slCdIndustry;
		if (iIndustryCode.isEmpty() && iIndustryItem.isEmpty()) {
			slCdIndustry = sCdIndustryService.findAll(this.index, this.limit, titaVo);
		} else if (!iIndustryCode.isEmpty()) {
			slCdIndustry = sCdIndustryService.findIndustryCode(iIndustryCode + "%", this.index, this.limit, titaVo);
		} else {
			slCdIndustry = sCdIndustryService.findIndustryItem("%" + iIndustryItem + "%", this.index, this.limit,
					titaVo);
		}
		List<CdIndustry> lCdIndustry = slCdIndustry == null ? null : slCdIndustry.getContent();

		if (lCdIndustry == null || lCdIndustry.isEmpty()) {
			throw new LogicException(titaVo, "E0001", "行業別代號資料檔"); // 查無資料
		}
		// 如有找到資料
		for (CdIndustry tCdIndustry : lCdIndustry) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOIndustryCode", tCdIndustry.getIndustryCode());
			occursList.putParam("OOIndustryItem", tCdIndustry.getIndustryItem());
			occursList.putParam("OOMainType", tCdIndustry.getMainType());
			occursList.putParam("OOIndustryRating", tCdIndustry.getIndustryRating());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdIndustry.getLastUpdate()) + " "
					+ parse.timeStampToStringTime(tCdIndustry.getLastUpdate()));
			occursList.putParam("OOLastEmp",
					tCdIndustry.getLastUpdateEmpNo() + " " + empName(titaVo, tCdIndustry.getLastUpdateEmpNo()));
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdIndustry != null && slCdIndustry.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}