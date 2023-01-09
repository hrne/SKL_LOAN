package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.springjpa.cm.L6903ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=X,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDateSt=9,7 AcDateEd=9,7 END=X,1
 */

@Service("L6903") // 會計帳務明細查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6903 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public AcMainService sAcMainService;
	@Autowired
	public TxTranCodeService sTxTranCodeService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public CdEmpService cdEmpService;;
	@Autowired
	public L6903ServiceImpl l6903ServiceImpl;

	@Autowired
	Parse parse;
	private String debits[] = { "1", "5", "6", "9" }; // 資產、支出為借方科目，負債、收入為貸方科目
	private List<String> debitsList = Arrays.asList(debits);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6903 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iAcBookCode = titaVo.getParam("AcBookCode");
		String iAcSubBookCode = titaVo.getParam("AcSubBookCode");
		String iBranchNo = titaVo.getParam("BranchNo");
		String iCurrencyCode = titaVo.getParam("CurrencyCode");
		String iAcNoCode = titaVo.getParam("AcNoCode").trim();
		String iAcSubCode = titaVo.getParam("AcSubCode").trim();
		String iAcDtlCode = titaVo.getParam("AcDtlCode").trim();
		int iAcDateSt = this.parse.stringToInteger(titaVo.getParam("AcDateSt"));
		int iFAcDateSt = iAcDateSt + 19110000;
		int iAcDateEd = this.parse.stringToInteger(titaVo.getParam("AcDateEd"));
		int iFAcDateEd = iAcDateEd + 19110000;
		BigDecimal wkBal = new BigDecimal(0);
		BigDecimal wkDb = new BigDecimal(0);
		BigDecimal wkCr = new BigDecimal(0);
		String iTranItem = "";
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
		this.limit = 90;

		// 查詢會計科子細目設定檔
		if (!(iAcNoCode.isEmpty())) {
			CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iAcNoCode, iAcSubCode, iAcDtlCode), titaVo);
			if (tCdAcCode == null) {
				throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
			}
			classcode = tCdAcCode.getClassCode(); // 1:下編子細目
		} else {
			classcode = 2;
		}

		// 查詢會計總帳檔
		Slice<AcMain> slAcMain;
		if (classcode == 1) {
			slAcMain = sAcMainService.acmainAcBookCodeRange2(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iFAcDateSt, iFAcDateEd, 0, Integer.MAX_VALUE, titaVo);
		} else if (classcode == 2) {
			slAcMain = sAcMainService.acmainAcBookCodeRange3(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iFAcDateSt, iFAcDateEd, 0, Integer.MAX_VALUE, titaVo);
		} else {

			slAcMain = sAcMainService.acmainAcBookCodeRange(iAcBookCode, iAcSubBookCode.trim() + "%", iBranchNo,
					iCurrencyCode, iAcNoCode, iAcSubCode, iAcDtlCode, iFAcDateSt, iFAcDateEd, 0, Integer.MAX_VALUE,
					titaVo);
		}

		if (slAcMain != null) {
			for (AcMain tAcMain : slAcMain.getContent()) {
				wkBal = wkBal.add(tAcMain.getYdBal());
				wkDb = wkDb.add(tAcMain.getDbAmt());
				wkCr = wkCr.add(tAcMain.getCrAmt());
			}
		}

		List<Map<String, String>> dList = null;

		try {
			dList = l6903ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		if (this.index == 0 && (dList == null || dList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔");
		}

		for (Map<String, String> d : dList) {
			// 不含未入帳,例如:未放行之交易

			OccursList occursList = new OccursList();

			occursList.putParam("OOAcNoCode", d.get("AcNoCode"));
			occursList.putParam("OOAcSubCode", d.get("AcSubCode"));
			occursList.putParam("OOAcDtlCode", d.get("AcDtlCode"));
			occursList.putParam("OOCustNo", d.get("CustNo"));
			occursList.putParam("OOFacmNo", d.get("FacmNo"));
			occursList.putParam("OOBormNo", d.get("BormNo"));

			if ("D".equals(d.get("DbCr"))) {
				occursList.putParam("OODbAmt", d.get("TxAmt"));
				occursList.putParam("OOCrAmt", 0);
			} else {
				occursList.putParam("OODbAmt", 0);
				occursList.putParam("OOCrAmt", d.get("TxAmt"));
			}
			String Odate = parse.stringToStringDateTime(d.get("CreateDate"));
			this.info("CreateDate   = " + Odate);
			String Date = FormatUtil.left(Odate, 9);
			occursList.putParam("OOLastDate", Date);
			String Time = FormatUtil.right(Odate, 8);
			occursList.putParam("OOLastTime", Time);
			occursList.putParam("OOSlipBatNo", d.get("SlipBatNo"));
			occursList.putParam("OOSlipSumNo", d.get("SlipSumNo"));
			occursList.putParam("OOEntAc", d.get("EntAc"));
			occursList.putParam("OOMediaSlipNo", d.get("MediaSlipNo"));
			occursList.putParam("OORvNo", d.get("RvNo"));
			occursList.putParam("OOSlipNote", d.get("SlipNote"));
			occursList.putParam("OOTitaTxCd", d.get("TitaTxCd"));
			occursList.putParam("OOTranItem", d.get("TranItem"));
			occursList.putParam("OOTitaTlrNo", d.get("TitaTlrNo"));
			occursList.putParam("OOTlrItem", d.get("TlrName"));
			occursList.putParam("OOTitaSupNo", d.get("TitaSupNo"));
			occursList.putParam("OOSupItem", d.get("SupName"));
			occursList.putParam("OOTitaTxtNo", FormatUtil.pad9(d.get("TitaTxtNo"), 8));
			occursList.putParam("OOAcNoItem", d.get("AcNoItem"));
			occursList.putParam("OOSlipNo", d.get("SlipNo"));
			String DateTime = parse.stringToStringDateTime(d.get("LastUpdate"));
			occursList.putParam("OOLastUpdate", DateTime);
			occursList.putParam("OOLastEmp", d.get("TitaTlrNo"));
			occursList.putParam("OOLastEmpName", empName(titaVo, d.get("TitaTlrNo")));

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		this.totaVo.putParam("ODbAmt", wkDb);
		this.totaVo.putParam("OCrAmt", wkCr);

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (dList != null && dList.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			 this.totaVo.setMsgEndToEnter();// 手動折返
//			this.totaVo.setMsgEndToAuto();// 自動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 查詢交易控制檔
	private String inqTxTranCode(String uTranNo, String uTranItem, TitaVo titaVo) throws LogicException {

		TxTranCode tTxTranCode = new TxTranCode();

		tTxTranCode = sTxTranCodeService.findById(uTranNo, titaVo);

		if (tTxTranCode == null) {
			uTranItem = "";
		} else {
			uTranItem = tTxTranCode.getTranItem();
		}

		return uTranItem;

	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}