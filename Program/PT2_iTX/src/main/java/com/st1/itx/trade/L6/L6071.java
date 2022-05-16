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
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita BankCode=X,3 BranchCode=X,4 BankItem=x,50 END=X,1
 */

@Service("L6071") // 行庫資料查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6071 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBankService sCdBankService;
	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6071 ");
		this.totaVo.init(titaVo);

		String iBankCode = titaVo.getParam("BankCode");
		String iBranchCode = titaVo.getParam("BranchCode");
		String iBankItem = titaVo.getParam("BankItem");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 207 * 200 = 41400

		this.info("Index : " + this.index);
		// 查詢行庫資料檔
		Slice<CdBank> slCdBank;
		if (iBankItem.isEmpty()) {
			slCdBank = sCdBankService.branchCodeLike(iBankCode.trim() + "%", iBranchCode.trim() + "%", this.index, this.limit, titaVo);
		} else {
			slCdBank = sCdBankService.bankItemLike("%" + iBankItem.trim() + "%", this.index, this.limit, titaVo);
		}
		List<CdBank> lCdBank = slCdBank == null ? null : slCdBank.getContent();

		if (lCdBank == null || lCdBank.size() == 0) {
			throw new LogicException(titaVo, "E0001", "行庫代號:" + iBankCode + "-" + iBranchCode); // 查無資料
		}
		// 如有找到資料
		for (CdBank tCdBank : lCdBank) {

			this.info(tCdBank.toString());
			// 0:查詢銀行名稱,不含分行名稱 ; 1:分行名稱
			// if (iBankFg.equals("0") && tCdBank.getBankCode().length() > 3) {
			// this.info("continue");
			// continue;
			// }

			OccursList occursList = new OccursList();
			occursList.putParam("OOBankCode", tCdBank.getBankCode());
			occursList.putParam("OOBranchCode", tCdBank.getBranchCode());
			occursList.putParam("OOBankItem", tCdBank.getBankItem());
			occursList.putParam("OOBranchItem", tCdBank.getBranchItem());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdBank.getLastUpdate())+ " " +parse.timeStampToStringTime(tCdBank.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdBank.getLastUpdateEmpNo() + " " + empName(titaVo, tCdBank.getLastUpdateEmpNo()));
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBank != null && slCdBank.hasNext()) {
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