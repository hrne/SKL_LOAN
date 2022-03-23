package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * OOAcDate=9,7<br>
 * OOENTRYDATE=9,7<br>
 * OOCustNoFacmNo=X,11<br>
 * OOFee=X,21<br>
 * OOReceiveDate=9,7<br>
 * OOCloseDate=9,7<br>
 * OOOverdueDate=9,7<br>
 */

@Service("L2613")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2613 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService foreclosureFeeService;

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	/* DB服務注入 */
	@Autowired
	public AcDetailService acDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2613 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// tita
		// 催收日起日
		int iOverdueDateStart = parse.stringToInteger(titaVo.getParam("OverdueDateStart")) + 19110000;
		// 催收日迄日
		int iOverdueDateEnd = parse.stringToInteger(titaVo.getParam("OverdueDateEnd")) + 19110000;

		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

		// new table
		CustMain tCustMain = new CustMain();

		Slice<ForeclosureFee> slForeclosureFee = foreclosureFeeService.overdueDateBetween(iOverdueDateStart,
				iOverdueDateEnd, this.index, this.limit);
		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();
		if (lForeclosureFee == null) {
			// 法拍費用檔無資料
			throw new LogicException(titaVo, "E0001", "法拍費用檔"); // 查無資料
		}
		List<LinkedHashMap<String, String>> chkOccursList = null;
		for (ForeclosureFee tForeclosureFee : lForeclosureFee) {
			// new occurs
			OccursList occursList = new OccursList();
			tCustMain = new CustMain();
			lAcDetail = new ArrayList<AcDetail>();
			tCustMain = custMainService.custNoFirst(tForeclosureFee.getCustNo(), tForeclosureFee.getCustNo());
			int custno = tForeclosureFee.getCustNo();
			int acDate = tForeclosureFee.getOverdueDate() + 19110000;
			String rvNo = parse.IntegerToString(tForeclosureFee.getRecordNo(), 7);
			this.info("acDate = " + acDate);

			int dSlipNo = 0;
			int cSlipNo = 0;

			Slice<AcDetail> slAcDetail = acDetailService.findL2613("F24", custno, rvNo, this.index, this.limit);
			lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();
			if (lAcDetail == null) {
				lAcDetail = new ArrayList<AcDetail>();
			}
			for (AcDetail tmpAcDetail : lAcDetail) {
				if (tmpAcDetail.getDbCr().equals("D")) {
					dSlipNo = tmpAcDetail.getSlipNo();
				} else {
					cSlipNo = tmpAcDetail.getSlipNo();
				}

			}

			occursList.putParam("OOOverdueDate", tForeclosureFee.getOverdueDate()); // 會計日期
			occursList.putParam("OOCustNo", tForeclosureFee.getCustNo()); // 戶號
			occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名
			occursList.putParam("OOReceiveDate", tForeclosureFee.getReceiveDate()); // 收件日期
			occursList.putParam("OODocDate", tForeclosureFee.getDocDate()); // 單據日期
			occursList.putParam("OOFeeCode", tForeclosureFee.getFeeCode()); // 科目
			occursList.putParam("OOFeeAmt", tForeclosureFee.getFee()); // 法拍費用
			occursList.putParam("OORecordNo", tForeclosureFee.getRecordNo()); // 記錄號碼
			occursList.putParam("OODSlipNo", dSlipNo); // d借傳票號碼SlipNo
			occursList.putParam("OOCSlipNo", cSlipNo); // c貸傳票號碼SlipNo
			this.info("occursList L2613" + occursList);
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);

			chkOccursList = this.totaVo.getOccursList();

		} // for

		if (lForeclosureFee.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		if (chkOccursList == null && titaVo.getReturnIndex() == 0) {
			throw new LogicException(titaVo, "E0001", "法拍費用檔"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}