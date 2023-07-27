package com.st1.itx.trade.L6;

import java.math.BigDecimal;
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
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L6904ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita AcBookCode=X,3 BranchNo=9,4 CurrencyCode=X,3 AcNoCode=X,8 AcSubCode=X,5
 * AcDtlCode=X,2 AcDate=9,7 InqType=9,1 END=X,1
 */

@Service("L6904") // 日結彙計查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6904 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcDetailService sAcDetailService;
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	public CdEmpService iCdEmpService;
	@Autowired
	Parse parse;
	@Autowired
	private L6904ServiceImpl l6904ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6904 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iInqType = this.parse.stringToInteger(titaVo.getParam("InqType")); // 彙計方式

		this.index = titaVo.getReturnIndex();

		this.limit = Integer.MAX_VALUE;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		// this.limit = 200; // 157 * 200 = 31,400

		// 查詢會計帳務明細檔
		List<Map<String, String>> dList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dListHead = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dListOc = new ArrayList<Map<String, String>>();
		try {
			dList = l6904ServiceImpl.FindData(titaVo, index, limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		int totalDCnt = 0; // 貸方總筆數
		int totalCCnt = 0; // 借方總筆數
		BigDecimal totalDAmt = BigDecimal.ZERO; // 貸方總金額
		BigDecimal totalCAmt = BigDecimal.ZERO; // 借方總金額
		// 總筆數資料
		// 彙總傳票號碼,彙總傳票筆數算一筆

		if (this.index == 0 && (dList == null || dList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		for (Map<String, String> d : dList) {
			if (d.get("AcNoCode").isEmpty()) {
				dListHead.add(d);
			} else {
				dListOc.add(d);
			}
		}
		if (this.index == 0 && (dListOc == null || dListOc.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "會計帳務明細檔"); // 查無資料
		}
		//共同輸出區
		for (Map<String, String> d : dListHead) {
			if (iInqType != 7) {
				totalDCnt = parse.stringToInteger(d.get("SumDCnt"));
				totalCCnt = parse.stringToInteger(d.get("SumCCnt"));
			}
			totalDAmt = parse.stringToBigDecimal(d.get("SumDAmt"));
			totalCAmt = parse.stringToBigDecimal(d.get("SumCAmt"));
		}
		for (Map<String, String> d : dListOc) {
			OccursList occursList = new OccursList();

			if (iInqType == 7) {
				if ("0".equals(d.get("DataInq"))) {
					totalDCnt = totalDCnt + parse.stringToInteger(d.get("SumDCnt"));
					totalCCnt = totalCCnt + parse.stringToInteger(d.get("SumCCnt"));
				} else {
					if (parse.stringToInteger(d.get("SumDCnt")) > 0) {
						totalDCnt = totalDCnt + 1;
					}
					if (parse.stringToInteger(d.get("SumCCnt")) > 0) {
						totalCCnt = totalCCnt + 1;
					}
				}
			}
			occursList.putParam("OOAcSubBookCode", d.get("AcNoCode"));
			occursList.putParam("OOAcNoCode", d.get("AcNoCode"));// AcNoCode
			occursList.putParam("OOAcSubCode", d.get("AcSubCode"));// AcSubCode
			occursList.putParam("OOAcDtlCode", d.get("AcDtlCode"));// AcDtlCode
			// 彙總傳票號碼,彙總傳票筆數算一筆
//			if (iInqType == 7 && parse.stringToInteger(d.get("SumDCnt")) > 0 && !"0".equals(d.get("DataInq"))) {
//				occursList.putParam("OODbCnt", "1/" + d.get("SumDCnt"));
//			} else {
//				occursList.putParam("OODbCnt", d.get("SumDCnt"));
//			}
			occursList.putParam("OODbCnt", d.get("SumDCnt"));
			occursList.putParam("OODbAmt", d.get("SumDAmt"));
			// 彙總傳票號碼,彙總傳票筆數算一筆
//			if (iInqType == 7 && parse.stringToInteger(d.get("SumCCnt")) > 0 && !"0".equals(d.get("DataInq"))) {
//				occursList.putParam("OOCrCnt", "1/" + d.get("SumCCnt"));
//			} else {
//				occursList.putParam("OOCrCnt", d.get("SumCCnt"));
//			}
			occursList.putParam("OOCrCnt", d.get("SumCCnt"));
			occursList.putParam("OOCrAmt", d.get("SumCAmt"));
			if (iInqType == 5) {
				occursList.putParam("OOInqData", "0".equals(d.get("DataInq")) ? ""
						: parse.IntegerToString(parse.stringToInteger(d.get("DataInq")), 2));
			} else {
				occursList.putParam("OOInqData", "0".equals(d.get("DataInq")) ? "" : d.get("DataInq"));
			}
			occursList.putParam("OOInqDataX", d.get("DataInqX"));
			occursList.putParam("OOAcNoItem", d.get("AcNoItem"));// AcNoItem

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}
		this.totaVo.putParam("OTotalDCnt", totalDCnt);
		this.totaVo.putParam("OTotalCCnt", totalCCnt);
		this.totaVo.putParam("OTotalDAmt", totalDAmt);
		this.totaVo.putParam("OTotalCAmt", totalCAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}
}