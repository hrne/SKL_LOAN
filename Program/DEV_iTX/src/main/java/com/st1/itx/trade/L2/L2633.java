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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CLOSEDATE=9,7<br>
 * SEQ=9,4<br>
 * OPT=9,1<br>
 * END=X,1<br>
 */

@Service("L2633")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2633 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2633.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public FacCloseService sFacCloseService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2633 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// tita
		// 入帳日期
		int iCloseDate = parse.stringToInteger(titaVo.getParam("CloseDate")) + 19110000;
		// 起始清償序號
		int iCloseNoStartAt = parse.stringToInteger(titaVo.getParam("CloseNoStartAt"));
		// 固定max清償序號
		int iCloseNoEndAt = 999;
		// 顯示選項
		int iDisplayType = parse.stringToInteger(titaVo.getParam("DisplayType"));
		int itemSt = 0;
		int itemEd = 1;
		// 1查車貸
		if (iDisplayType == 1) {
			itemSt = 1;
		}
		// new ArrayList
		List<FacClose> lFacClose = new ArrayList<FacClose>();

		Slice<FacClose> slFacClose = sFacCloseService.findCloseNo(iCloseDate, iCloseNoStartAt, iCloseNoEndAt, itemSt,
				itemEd, this.index, this.limit);
		lFacClose = slFacClose == null ? null : slFacClose.getContent();
		if (lFacClose == null) {
			throw new LogicException("E2003", "清償作業檔"); // 查無資料
		}
		for (FacClose tFacClose : lFacClose) {

			// new occurs
			OccursList occursList = new OccursList();
			// new table
			CustMain tCustMain = new CustMain();
			int custno = tFacClose.getCustNo();
			tCustMain = sCustMainService.custNoFirst(custno, custno);
			if (tCustMain == null) {
				tCustMain = new CustMain();
			}
			occursList.putParam("OOCustNo", tFacClose.getCustNo());
			occursList.putParam("OOFacmNo", tFacClose.getFacmNo());
			occursList.putParam("OOCustName", tCustMain.getCustName());
			occursList.putParam("OOCloseReasonCode", tFacClose.getCloseReasonCode());
			occursList.putParam("OOCloseAmt", tFacClose.getCloseAmt());
			occursList.putParam("OOCollectWayCode", tFacClose.getCollectWayCode());
			occursList.putParam("OORmk", tFacClose.getRmk());
			occursList.putParam("OOTelNo1", tFacClose.getTelNo1());
			occursList.putParam("OODocNo", tFacClose.getDocNo());
			occursList.putParam("OOAgreeNo", tFacClose.getAgreeNo());
			occursList.putParam("OOClsNo", tFacClose.getClsNo());

			this.info("occursList L2633" + occursList);
			this.totaVo.addOccursList(occursList);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}