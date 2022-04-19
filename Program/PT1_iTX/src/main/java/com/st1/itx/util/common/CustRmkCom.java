package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustRmk;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustRmkService;
//import com.st1.itx.db.service.TxCtrlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 顧客控管警訊通知訊息 <BR>
 * 當顧客控管警訊資料檔CustRmk內有該戶號資料時送出通知訊息<BR>
 * 通知訊息維持3分鐘<BR>
 * 
 * @author st1
 * @version 1.0.0
 */
@Component("CustRmkCom")
@Scope("prototype")
public class CustRmkCom extends TradeBuffer {

//	private TitaVo titaVo;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public CustRmkService custRmkService;
	
	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	public ArrayList<TotaVo> getCustRmk(TitaVo titaVo, int iCustNo) throws LogicException {
		this.info("CustRmkCom.getCustRmk = " + iCustNo);

		// 查詢顧客控管警訊檔

		Slice<CustRmk> slCustRmk = custRmkService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		List<CustRmk> lCustRmk = slCustRmk == null ? null : slCustRmk.getContent();
		if (lCustRmk != null && lCustRmk.size() > 0) {
			String s = "{red-s}{b-s}顧客控管警訊：{b-e}{red-e}<br><br>";
			for (CustRmk custRmk : lCustRmk) {
				CdEmp cdEmp = cdEmpService.findById(custRmk.getLastUpdateEmpNo(), titaVo);
				String emp = custRmk.getLastUpdateEmpNo();
				if (cdEmp != null) {
					emp += " " + cdEmp.getFullname();
				}
				s += custRmk.getRmkDesc() + " ("+ emp + " " + parse.timeStampToString(custRmk.getLastUpdate()) + ")<br>";
			}

			this.totaVo.init(titaVo);

			this.totaVo.setHtmlContent(s);

			this.addList(totaVo);

//			webClient.sendPost(dateUtil.getNowStringBc(), parse.IntegerToString(dateUtil.getNowIntegerTime() / 100 + 3, 4), titaVo.getTlrNo(), "Y", "L2072", parse.IntegerToString(iCustNo, 7),
//					"該戶有顧客控管警訊", titaVo);
//			this.info("time=" + parse.IntegerToString(dateUtil.getNowIntegerTime() / 100 + 3, 4));

		}

		return this.sendList();
	}
}
