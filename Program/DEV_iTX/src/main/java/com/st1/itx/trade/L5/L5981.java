package com.st1.itx.trade.L5;

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
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.NegFinShareLog;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.NegFinShareLogService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.parse.Parse;

@Service("L5981") // 國稅局申報檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author ZhiCheng
 * @version 1.0.0
 */

public class L5981 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public NegFinShareLogService sNegFinShareLogService;
	@Autowired
	Parse parse;
	@Autowired
	public NegCom sNegCom;
	@Autowired
	public CdBankService sCdBankService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5981 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iCaseSq = this.parse.stringToInteger(titaVo.getParam("CaseSq"));

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 45 * 200 = 9000

		Slice<NegFinShareLog> sNegFinShareLog;
		if (iCaseSq==0) {
			sNegFinShareLog = sNegFinShareLogService.CustNoEq(iCustNo, this.index, this.limit, titaVo);
		} else {
			sNegFinShareLog = sNegFinShareLogService.FindAllFinCode(iCustNo, iCaseSq, this.index, this.limit, titaVo);
		}
		List<NegFinShareLog> lNegFinShareLog = sNegFinShareLog == null ? null : sNegFinShareLog.getContent();

		if (lNegFinShareLog == null || lNegFinShareLog.size() == 0) {
			throw new LogicException(titaVo, "E0001", "債務協商債權分攤檔歷程檔"); // 查無資料
		}
		// 如有找到資料
		String FinCode = "";
		CdBank tCdBank = new CdBank();
		CdBankId tCdBankId = new CdBankId();
		for (NegFinShareLog tNegFinShareLog : lNegFinShareLog) {
			OccursList occursList = new OccursList();

			occursList.putParam("OOCreateDate", parse.timeStampToString(tNegFinShareLog.getCreateDate()));// 建檔日期
			occursList.putParam("OOCustNo", tNegFinShareLog.getCustNo());// 戶號
			occursList.putParam("OOCaseSeq", tNegFinShareLog.getCaseSeq());// 案件序號
			occursList.putParam("OOSeq", tNegFinShareLog.getSeq());// 歷程序號
			FinCode = tNegFinShareLog.getFinCode();
			occursList.putParam("OOFinCode", FinCode);// 債權機構

			tCdBankId.setBankCode(FinCode);
			tCdBankId.setBranchCode("    ");
			
			String tBankItem = sNegCom.FindNegFinAcc(FinCode, titaVo)[0];
			occursList.putParam("OOFinCodeName", tBankItem);// 債權機構名稱
			

			this.info("OOFinCodeName==" + occursList.get("OOFinCodeName"));

			occursList.putParam("OOContractAmt", tNegFinShareLog.getContractAmt());// 簽約金額
			occursList.putParam("OOAmtRatio", tNegFinShareLog.getAmtRatio());// 債權比例
			occursList.putParam("OODueAmt", tNegFinShareLog.getDueAmt());// 期款
			occursList.putParam("OOCancelDate", tNegFinShareLog.getCancelDate());// 註銷日期
			occursList.putParam("OOCancelAmt", tNegFinShareLog.getCancelAmt());// 註銷本金

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (sNegFinShareLog != null && sNegFinShareLog.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}