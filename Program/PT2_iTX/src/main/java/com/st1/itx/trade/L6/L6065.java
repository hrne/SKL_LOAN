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
import com.st1.itx.db.domain.CdOverdue;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita OverdueSign=X,1 OverdueCode=X,4 END=X,1
 */

@Service("L6065") // 逾期新增減少原因檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6065 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdOverdueService sCdOverdueService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6065 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iOverdueSign = titaVo.getParam("OverdueSign");
		String iOverdueCode = titaVo.getParam("OverdueCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 105 * 200 = 21000

		// 查詢逾期新增減少原因檔
		Slice<CdOverdue> slCdOverdue;
		if (iOverdueSign.isEmpty() || iOverdueSign.equals("0")) {
			slCdOverdue = sCdOverdueService.findAll(this.index, this.limit, titaVo);
		} else {
			if (iOverdueCode.isEmpty() || iOverdueCode.equals("0000")) {
				slCdOverdue = sCdOverdueService.overdueCodeRange(iOverdueSign, iOverdueSign, "0000", "ZZZZ", this.index, this.limit, titaVo);
			} else {
				slCdOverdue = sCdOverdueService.overdueCodeRange(iOverdueSign, iOverdueSign, iOverdueCode, iOverdueCode, this.index, this.limit, titaVo);
			}
		}
		List<CdOverdue> lCdOverdue = slCdOverdue == null ? null : slCdOverdue.getContent();

		if (lCdOverdue == null || lCdOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "逾期新增減少原因檔檔"); // 查無資料
		}
		// 如有找到資料
		String dOverdueCode = "";
		String dOverdueItem = "";
		for (CdOverdue tCdOverdue : lCdOverdue) {
			OccursList occursList = new OccursList();
			// 後三碼000的
			dOverdueCode = tCdOverdue.getOverdueCode().substring(1, 4);
			if (Integer.parseInt(dOverdueCode) == 0) {
				dOverdueItem = tCdOverdue.getOverdueItem();
			}
			occursList.putParam("OOOverdue", dOverdueItem);
			occursList.putParam("OOOverdueSign", tCdOverdue.getOverdueSign());
			occursList.putParam("OOOverdueCode", tCdOverdue.getOverdueCode());
			occursList.putParam("OOOverdueItem", tCdOverdue.getOverdueItem());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdOverdue.getLastUpdate()) + " " + parse.timeStampToStringTime(tCdOverdue.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdOverdue.getLastUpdateEmpNo() + " " + empName(titaVo, tCdOverdue.getLastUpdateEmpNo()));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdOverdue != null && slCdOverdue.hasNext()) {
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