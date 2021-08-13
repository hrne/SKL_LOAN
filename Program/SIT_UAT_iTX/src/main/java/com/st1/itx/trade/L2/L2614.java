package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.ForeclosureFee;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.ForeclosureFeeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2614")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2614 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2614.class);

	/* DB服務注入 */
	@Autowired
	public ForeclosureFeeService foreclosureFeeService;
	/* DB服務注入 */
	@Autowired
	public AcDetailService acDetailService;
	/* DB服務注入 */
	@Autowired
	public CdAcCodeService cdAcCodeService;
	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2614 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		// tita
		// 催收日起日
		int iOverdueDate = parse.stringToInteger(titaVo.getParam("OverdueDate")) + 19110000;

		// new ArrayList
		List<ForeclosureFee> lForeclosureFee = new ArrayList<ForeclosureFee>();
		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

		// new table
		CustMain tCustMain = new CustMain();
		CdAcCode tmpCdAcCode = new CdAcCode();

		Slice<ForeclosureFee> slForeclosureFee = foreclosureFeeService.overdueDateBetween(iOverdueDate, iOverdueDate,
				this.index, this.limit);
		lForeclosureFee = slForeclosureFee == null ? null : slForeclosureFee.getContent();

		if (lForeclosureFee == null) {
			// 法拍費用檔無資料
			throw new LogicException(titaVo, "E2003", "法拍費用檔"); // 查無資料
		}

		this.info("lForeclosureFee = " + lForeclosureFee);
		for (ForeclosureFee tForeclosureFee : lForeclosureFee) {
			// new ArrayList
			lAcDetail = new ArrayList<AcDetail>();

			String rvNo = parse.IntegerToString(tForeclosureFee.getRecordNo(), 7);
			Slice<AcDetail> slAcDetail = acDetailService.findL2613(iOverdueDate, rvNo, "L618C", this.index, this.limit);
			lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

			this.info("lAcDetail = " + lAcDetail);
			for (AcDetail tmpAcDetail : lAcDetail) {

				// new occurs
				OccursList occursList = new OccursList();

				// new table
				tmpCdAcCode = new CdAcCode();

				// 借貸金額
				BigDecimal damt = BigDecimal.ZERO;
				BigDecimal camt = BigDecimal.ZERO;

				// 科子細目代號
				String acNoCode = tmpAcDetail.getAcNoCode();
				String acSubCode = tmpAcDetail.getAcSubCode();
				String acDtlCode = tmpAcDetail.getAcDtlCode();
				// 戶號
				int custno = tmpAcDetail.getCustNo();

				// 取戶名
				tCustMain = custMainService.custNoFirst(custno, custno);

				// 取科子目名稱
				tmpCdAcCode = cdAcCodeService.findById(new CdAcCodeId(acNoCode, acSubCode, acDtlCode));

				// 借貸別D為借方金額 C為貸方金額
				if (tmpAcDetail.getDbCr().equals("D")) {
					damt = tmpAcDetail.getTxAmt();
				} else {
					camt = tmpAcDetail.getTxAmt();
				}

				occursList.putParam("OOTxtNo", tmpAcDetail.getTitaTxtNo()); // 交易序號
				occursList.putParam("OOSlipNo", tmpAcDetail.getSlipNo()); // 傳票號碼
				occursList.putParam("OOacNoCode", acNoCode); // 科目代號
				occursList.putParam("OOacSubCode", acSubCode); // 子目代號
				occursList.putParam("OOacDtlCode", acDtlCode); // 細目代號
				occursList.putParam("OOAcNoItem", tmpCdAcCode.getAcNoItem()); // 科子目名稱
				occursList.putParam("OODTxAmt", damt); // 借方金額
				occursList.putParam("OOCTxAmt", camt); // 貸方金額
				occursList.putParam("OOCustNo", custno); // 戶號
				occursList.putParam("OOCustName", tCustMain.getCustName()); // 戶名

				this.info("occursList L2614" + occursList);
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}

		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}