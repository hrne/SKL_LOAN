package com.st1.itx.trade.L2;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.ReltMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 */

@Service("L2035")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2035 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2035 ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 38 * 500 = 19000

		// 取tita戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 取tita案件編號
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));
		
		ReltMain iReltMain = new ReltMain();
		Slice<ReltMain> sReltMain = null;
		OccursList occursList = new OccursList();
		ArrayList<Integer> iCaseNoList = new ArrayList<>();
		if (iCustNo == 0) {
			iReltMain = sReltMainService.caseNoFirst(iCaseNo, titaVo);
			if (iReltMain == null) {
				throw new LogicException(titaVo, "E2003", "無關係人檔資料"); // 查無資料
			}
			occursList.putParam("OOCaseNo", iReltMain.getCaseNo());
			this.totaVo.addOccursList(occursList);
		}else {
			sReltMain = sReltMainService.custNoEq(iCustNo,this.index,this.limit, titaVo);
			if (sReltMain == null) {
				throw new LogicException(titaVo, "E2003", "該戶號" + iCustNo + "無關係人檔資料"); // 查無資料
			}
			for (ReltMain ssReltMain:sReltMain) {
				occursList = new OccursList();
				if (iCaseNoList.contains(ssReltMain.getCaseNo())) {
					continue;
				}else {
					iCaseNoList.add(ssReltMain.getCaseNo());
					occursList.putParam("OOCaseNo", ssReltMain.getCaseNo());
					this.totaVo.addOccursList(occursList);
				}
				
			}
		}
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}