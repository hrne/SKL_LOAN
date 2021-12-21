package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.service.springjpa.cm.L2921ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CaseNo=9,7<br>
 * CustNo=9,7<br>
 * CustId=X,10<br>
 * ApplNo=9,7<br>
 * TELLER=X,6<br>
 * FacmNo=9,3<br>
 * CloseCode=9,1<br>
 * END=X,1<br>
 */

@Service("L2921")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2921 extends TradeBuffer {

	/* DB服務注入 */
//	@Autowired
//	public TxTellerService txTellerService;

	@Autowired
	public L2921ServiceImpl sL2921ServiceImpl;

	@Autowired
	public CdEmpService sCdEmpService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public LoanNotYetService sLoanNotYetService;

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2921 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 103 * 500 = 51500

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultList = sL2921ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("L2038ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", "L2038");

		}

		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				// new occurs
				OccursList occurslist = new OccursList();

				int FirstDrawdownDate = 0, YetDate = 0, CloseDate = 0;
				if (parse.stringToInteger(result.get("F6")) > 0) {
					FirstDrawdownDate = parse.stringToInteger(result.get("F6")) - 19110000;
				}
				if (parse.stringToInteger(result.get("F11")) > 0) {
					YetDate = parse.stringToInteger(result.get("F11")) - 19110000;
				}
				if (parse.stringToInteger(result.get("F12")) > 0) {
					CloseDate = parse.stringToInteger(result.get("F12")) - 19110000;
				}

				occurslist.putParam("OOCaseNo", result.get("F0")); // 案號
				occurslist.putParam("OOCustNo", result.get("F1")); // 戶號
				occurslist.putParam("OOCustName", result.get("F2")); // 戶名
				occurslist.putParam("OOCustId", result.get("F3")); // 統編
				occurslist.putParam("OOApplNo", result.get("F4")); // 核准號碼
				occurslist.putParam("OOFacmNo", result.get("F5")); // 額度號碼
				occurslist.putParam("OOFirstDrawdownDate", FirstDrawdownDate); // 首撥日期
				occurslist.putParam("OOEmpNo", result.get("F7")); // 經辦(房貸專員)
				occurslist.putParam("OOEmpName", result.get("F8"));
				occurslist.putParam("OONotYetCode", result.get("F9")); // 未齊件代碼
				occurslist.putParam("OONotYetItemX", result.get("F10")); // 未齊件說明
				occurslist.putParam("OOYetDate", YetDate); // 齊件日期
				occurslist.putParam("OOCloseDate", CloseDate); // 銷號日期
				occurslist.putParam("OOReMark", result.get("F13")); // 備註

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			} // for

			if (resultList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			throw new LogicException(titaVo, "E2003", "查無未齊件資料"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();

	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = sL2921ServiceImpl.getSize();
		int size = times * this.limit;

		this.info("index ..." + this.index);
		this.info("times ..." + times);
		this.info("cnt ..." + cnt);
		this.info("size ..." + size);

		if (size == cnt) {
			result = false;
		}
		this.info("result ..." + result);

		return result;
	}

}