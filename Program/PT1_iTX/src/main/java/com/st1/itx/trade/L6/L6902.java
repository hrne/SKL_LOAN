package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=X,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDateSt=9,7 AcDateEd=9,7 END=X,1
 */

@Service("L6902") // 會計總帳查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6902 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcMainService sAcMainService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	AcDetailService sAcDetailService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6902 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCode = titaVo.getParam("AcNoCode");
		String iAcSubCode = titaVo.getParam("AcSubCode");
		String iAcDtlCode = titaVo.getParam("AcDtlCode");
		int iAcDateSt = this.parse.stringToInteger(titaVo.getParam("AcDateSt"));
		int iFAcDateSt = iAcDateSt + 19110000;
		int iAcDateEd = this.parse.stringToInteger(titaVo.getParam("AcDateEd"));
		int iFAcDateEd = iAcDateEd + 19110000;
		String dbcr = "";
		int classcode = 0;

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500; // 60 * 500 = 30,000

		// 查詢會計科子細目設定檔
		if (!(iAcNoCode.isEmpty())) {
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode), titaVo);
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
			} else {
				dbcr = tCdAcCode.getDbCr();
				classcode = tCdAcCode.getClassCode(); // 1:下編子細目
			}
		}

		// 查詢會計總帳檔
		Slice<AcMain> slAcMain;
		if (classcode == 1) {
			slAcMain = sAcMainService.acmainAcBookCodeRange2(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iFAcDateSt, iFAcDateEd, this.index, this.limit, titaVo);
		} else {
			slAcMain = sAcMainService.acmainAcBookCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iAcDtlCode, iFAcDateSt, iFAcDateEd, this.index, this.limit,
					titaVo);
		}

		List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();

		if (lAcMain == null || lAcMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "會計總帳檔"); // 查無資料
		}

		// 如有找到資料
		if (classcode == 0) {
			for (AcMain tAcMain : lAcMain) {

				OccursList occursList = new OccursList();
				occursList.putParam("OOAcDate", tAcMain.getAcDate());
				occursList.putParam("OOAcBookCode", tAcMain.getAcBookCode());
				occursList.putParam("OOAcSubBookCode", tAcMain.getAcSubBookCode());
				occursList.putParam("OODbAmt", tAcMain.getDbAmt());
				occursList.putParam("OOCrAmt", tAcMain.getCrAmt());
				occursList.putParam("OODbCr", dbcr);
				occursList.putParam("OOTdBal", tAcMain.getTdBal());
				Timestamp lastUpdateTime = tAcMain.getLastUpdate();
				occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(lastUpdateTime) + " "
						+ parse.timeStampToStringTime(lastUpdateTime));
				String lastUpdateEmpNo = tAcMain.getLastUpdateEmpNo();
				occursList.putParam("OOLastEmp", findEmpNoAndName(lastUpdateEmpNo, titaVo));

				// 檢查是否有明細，以判斷是否需顯示按鈕

				Slice<AcDetail> slAcDetail = sAcDetailService.acdtlAcDateRange(tAcMain.getAcBookCode(),
						tAcMain.getAcSubBookCode() + "%", tAcMain.getBranchNo(), tAcMain.getCurrencyCode(),
						tAcMain.getAcNoCode(), tAcMain.getAcSubCode(), tAcMain.getAcDtlCode(), tAcMain.getAcDate() + 19110000, tAcMain.getAcDate() + 19110000,
						0, 1, titaVo);
				List<AcDetail> lAcDetail = slAcDetail == null ? null : slAcDetail.getContent();

				occursList.putParam("OOHasDetail", lAcDetail != null && !lAcDetail.isEmpty() ? "Y" : "N");
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		} else {
			BigDecimal dbAmt = new BigDecimal(0);
			BigDecimal crAmt = new BigDecimal(0);
			BigDecimal tdbal = new BigDecimal(0);
			int count = 0;
			int acdate = 0;
			String acbookcode = "";
			String acsubbookcode = "";
			Timestamp lastUpdateTime = null;
			String lastUpdateEmpNo = "";

			for (AcMain tAcMain : lAcMain) {

				if (count == 0) { // 第一筆小計算
					count++;
					this.info("第一筆");
					dbAmt = dbAmt.add(tAcMain.getDbAmt());
					crAmt = crAmt.add(tAcMain.getCrAmt());
					tdbal = tdbal.add(tAcMain.getTdBal());
					this.info("dbAmt=" + dbAmt + ",crAmt=" + crAmt + ",tdbal=" + tdbal);
					acdate = tAcMain.getAcDate();
					acsubbookcode = tAcMain.getAcSubBookCode();
					lastUpdateTime = tAcMain.getLastUpdate();
					lastUpdateEmpNo = tAcMain.getLastUpdateEmpNo();
					continue;
				}

				// 其他資料相同小計
				if (acdate == tAcMain.getAcDate() && acsubbookcode.equals(tAcMain.getAcSubBookCode())) {
					this.info("其餘筆");
					dbAmt = dbAmt.add(tAcMain.getDbAmt());
					crAmt = crAmt.add(tAcMain.getCrAmt());
					tdbal = tdbal.add(tAcMain.getTdBal());
					acdate = tAcMain.getAcDate();
					acbookcode = tAcMain.getAcBookCode();
					acsubbookcode = tAcMain.getAcSubBookCode();
					lastUpdateTime = tAcMain.getLastUpdate();
					lastUpdateEmpNo = tAcMain.getLastUpdateEmpNo();
					continue;
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OOAcDate", acdate);
				occursList.putParam("OOAcBookCode", acbookcode);
				occursList.putParam("OOAcSubBookCode", acsubbookcode);
				occursList.putParam("OODbAmt", dbAmt);
				occursList.putParam("OOCrAmt", crAmt);
				occursList.putParam("OODbCr", dbcr);
				occursList.putParam("OOTdBal", tdbal);
				occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(lastUpdateTime) + " "
						+ parse.timeStampToStringTime(lastUpdateTime));
				occursList.putParam("OOLastEmp", findEmpNoAndName(lastUpdateEmpNo, titaVo));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);

				// 準備下一筆
				dbAmt = new BigDecimal(0);
				crAmt = new BigDecimal(0);
				tdbal = new BigDecimal(0);
				acdate = tAcMain.getAcDate();
				acbookcode = tAcMain.getAcBookCode();
				acsubbookcode = tAcMain.getAcSubBookCode();
				lastUpdateTime = tAcMain.getLastUpdate();
				lastUpdateEmpNo = tAcMain.getLastUpdateEmpNo();
				dbAmt = dbAmt.add(tAcMain.getDbAmt());
				crAmt = crAmt.add(tAcMain.getCrAmt());
				tdbal = tdbal.add(tAcMain.getTdBal());

			}
			// 最後一筆
			OccursList occursList = new OccursList();
			occursList.putParam("OOAcDate", acdate);
			occursList.putParam("OOAcBookCode", acbookcode);
			occursList.putParam("OOAcSubBookCode", acsubbookcode);
			occursList.putParam("OODbAmt", dbAmt);
			occursList.putParam("OOCrAmt", crAmt);
			occursList.putParam("OODbCr", dbcr);
			occursList.putParam("OOTdBal", tdbal);
			occursList.putParam("OOLastUpdate",
					parse.timeStampToStringDate(lastUpdateTime) + " " + parse.timeStampToStringTime(lastUpdateTime));
			occursList.putParam("OOLastEmp", findEmpNoAndName(lastUpdateEmpNo, titaVo));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);

		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slAcMain != null && slAcMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String findEmpNoAndName(String empNo, TitaVo titaVo) {

		String empName = empNo;

		CdEmp cdEmp = sCdEmpService.findById(empNo, titaVo);
		if (cdEmp != null)
			empName = cdEmp.getFullname();

		return empNo + empName;
	}
}