package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.ReltMain;
import com.st1.itx.db.service.ReltCompanyService;
import com.st1.itx.db.service.ReltFamilyService;
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
	// private static final Logger logger = LoggerFactory.getLogger(L2035.class);

	/* DB服務注入 */
	@Autowired
	public ReltMainService sReltMainService;

	/* DB服務注入 */
	@Autowired
	public ReltFamilyService sReltFamilyService;

	/* DB服務注入 */
	@Autowired
	public ReltCompanyService sReltCompanyService;

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
		// new array list
		List<ReltMain> lReltMain = new ArrayList<ReltMain>();
		
		Slice<ReltMain> slReltMain = null;

		ReltMain tReltMain = new ReltMain();

		if (iCustNo != 0) {

			slReltMain = sReltMainService.CustNoEq(iCustNo, index, limit, titaVo);

			if (slReltMain == null) {
				throw new LogicException(titaVo, "E2003", "該戶號" + iCustNo + "無關係人檔資料"); // 查無資料
			}
			
			for(ReltMain tMain: slReltMain) {
			  lReltMain.add(tMain);
			}

		} else {

			tReltMain = sReltMainService.CaseNoFirst(iCaseNo, titaVo);

			if (tReltMain == null) {
				throw new LogicException(titaVo, "E2003", "無關係人檔資料"); // 查無資料
			}
			
			lReltMain.add(tReltMain);
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
//		if (slReltMain != null && slReltMain.hasNext()) {
//			titaVo.setReturnIndex(this.setIndexNext());
//			/* 手動折返 */
//			this.totaVo.setMsgEndToEnter();
//		}
		for (ReltMain tmpReltMain : lReltMain) {
			this.info("tGuarantor L2035" + tReltMain);
			// new occurs
			OccursList occursList = new OccursList();
			
			occursList.putParam("OOCaseNo", tmpReltMain.getCaseNo());
			occursList.putParam("OOCustNo", tmpReltMain.getCustNo());
			occursList.putParam("OOReltId", tmpReltMain.getReltId());
			occursList.putParam("OORelName", tmpReltMain.getReltName());
			occursList.putParam("OOPosInd", tmpReltMain.getReltCode());
			occursList.putParam("OORemarkType", tmpReltMain.getRemarkType());
			occursList.putParam("OORemark", tmpReltMain.getReltmark());
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}