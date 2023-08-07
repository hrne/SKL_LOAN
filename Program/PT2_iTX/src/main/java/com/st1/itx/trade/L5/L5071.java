package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
/*DB服務*/
import com.st1.itx.db.service.CustMainService;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.springjpa.cm.L5071ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* CustId=X,10<br>
* CaseKindCode=9,1<br>
* CustLoanKind=9,1<br>
* Status=9,1<br>
*/

/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
@Service("L5071")
@Scope("prototype")
public class L5071 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	L5071ServiceImpl l5071ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public NegCom sNegCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5071 ");
		this.totaVo.init(titaVo);

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 150;// 查全部

		int iSelectType = Integer.valueOf(titaVo.getParam("SelectType"));

		List<Map<String, String>> listL5071 = null;
		try {
			if (iSelectType == 1) {// staus只找0,2,3
				listL5071 = l5071ServiceImpl.findType1(this.index, this.limit, titaVo);
			} else {
				listL5071 = l5071ServiceImpl.findAll(this.index, this.limit, titaVo);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("L5071ServiceImpl.findAll error = " + errors.toString());
		}
		if (listL5071 != null && l5071ServiceImpl.hasNext()) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}
		String CustName = "";
		if (listL5071 == null || listL5071.size() == 0) {
			throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
		} else {
			CustMain tCustMain = new CustMain();
			for (Map<String, String> t5071 : listL5071) {

				OccursList occursList = new OccursList();
				if (t5071.get("F15").length() != 0) {// 保證人或保貸戶此欄才有值
					occursList.putParam("OOCustId", t5071.get("F15"));
					CustName = t5071.get("F16");
				} else {
					occursList.putParam("OOCustId", t5071.get("CustId"));
					tCustMain = sCustMainService.custIdFirst(t5071.get("CustId"), titaVo);
					if (tCustMain != null) {
						CustName = StringCut.replaceLineUp(tCustMain.getCustName());
					}
				}
				occursList.putParam("OOCustName", CustName);
				occursList.putParam("OOCaseKindCode", t5071.get("F1"));
				occursList.putParam("OOCustLoanKind", t5071.get("F2"));
				occursList.putParam("OOStatus", t5071.get("F3"));
				occursList.putParam("OOCustNo", t5071.get("F4"));
				occursList.putParam("OOCaseSeq", t5071.get("F5"));
				occursList.putParam("OOApplDate", parse.stringToInteger(t5071.get("F6")) - 19110000);
				occursList.putParam("OODueAmt", t5071.get("F7"));
				occursList.putParam("OOTotalPeriod", t5071.get("F8"));
				occursList.putParam("OOIntRate", t5071.get("F9"));
				occursList.putParam("OOFirstDueDate", parse.stringToInteger(t5071.get("F10")) - 19110000);
				if (parse.stringToInteger(t5071.get("F11")) == 0) {
					occursList.putParam("OOLastDueDate", 0);
				} else {
					occursList.putParam("OOLastDueDate", parse.stringToInteger(t5071.get("F11")) - 19110000);
				}

				occursList.putParam("OOIsMainFin", t5071.get("F12"));
				occursList.putParam("OOTotalContrAmt", t5071.get("F14"));
				occursList.putParam("OOMainFinCode", t5071.get("F13"));
				String MainFinCodeName = sNegCom.FindNegFinAcc(t5071.get("F13"), titaVo)[0];
				occursList.putParam("OOMainFinCodeName", MainFinCodeName);
				occursList.putParam("OOCaseCount", t5071.get("F17"));
				occursList.putParam("OOTransCount", t5071.get("F18"));
				occursList.putParam("OOAppr01Count", t5071.get("F19"));
				this.totaVo.addOccursList(occursList);
			} // for

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}