package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.BatxDetailService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.springjpa.cm.L4920ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * AcDate=9,7<br>
 * BatchNo=X,6<br>
 * StatusCode=9,1<br>
 * RepayCode=9,2<br>
 * ProcStsCode=X,1<br>
 * ReconCode=X,3<br>
 * FileName=X,20<br>
 * CustNo=9,7<br>
 * END=X,1<br>
 */

@Service("L4920")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4920 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BatxDetailService batxDetailService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public L4920ServiceImpl l4920ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4920 ");
		this.totaVo.init(titaVo);

		BigDecimal oSumPayAmt = new BigDecimal("0");
		BigDecimal oSumAccAmt = new BigDecimal("0");
		BigDecimal oSumDisAmt = new BigDecimal("0");
		List<String> enterList = new ArrayList<String>();
		enterList.add("5");
		enterList.add("6");
		enterList.add("7");

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

		List<Map<String, String>> resultAllList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> resultPartList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resultAllList = l4920ServiceImpl.findAll(1,  this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4920ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultAllList != null && resultAllList.size() > 0) {
			oSumPayAmt = parse.stringToBigDecimal(resultAllList.get(0).get("F0"));
			oSumAccAmt = parse.stringToBigDecimal(resultAllList.get(0).get("F1"));
			oSumDisAmt = oSumPayAmt.subtract(oSumAccAmt);
		}

		try {
			// *** 折返控制相關 ***
			resultPartList = l4920ServiceImpl.findAll(0, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4920ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resultPartList != null && resultPartList.size() > 0) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resultPartList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resultPartList) {
				OccursList occursList = new OccursList();
				this.info("result=" + result);
				int entryDate = 0;

				if (parse.stringToInteger(result.get("EntryDate")) > 19110000) {
					entryDate = parse.stringToInteger(result.get("EntryDate")) - 19110000;
				}

				occursList.putParam("OODetailSeq", result.get("DetailSeq"));
				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOCustNo", result.get("CustNo"));
				occursList.putParam("OOFacmNo", result.get("FacmNo"));
				occursList.putParam("OORepayType", result.get("RepayType"));
				occursList.putParam("OORepayCode", result.get("RepayCode"));
				occursList.putParam("OOReconCode", result.get("ReconCode"));
				occursList.putParam("OOReconCodeX", result.get("ReconCodeX"));
				occursList.putParam("OORepayAmt", result.get("RepayAmt"));
				occursList.putParam("OOAcctAmt", result.get("AcctAmt"));
				occursList.putParam("OODisacctAmt", result.get("DisacctAmt"));
				occursList.putParam("OOProcStsCode", result.get("ProcStsCode"));
				occursList.putParam("OOProcCode", result.get("ProcCode"));

				String procNote = "";
				String txcd = "";
				String fileSeq = result.get("DetailSeq");
				int custNo = parse.stringToInteger(result.get("CustNo"));

				if (result.get("ProcNote") != null) {

					TempVo tempVo = new TempVo();
					tempVo = tempVo.getVo(result.get("ProcNote"));

					if (tempVo.get("ReturnMsg") != null && tempVo.get("ReturnMsg").length() > 0) {
						procNote += "回應訊息:" + tempVo.get("ReturnMsg") + " ";
					}
					if (tempVo.get("EraseCnt") != null) {
						procNote += "訂正" + tempVo.get("EraseCnt") + "次 ";
					}

					if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
						procNote += "檢核訊息:" + tempVo.get("CheckMsg") + " ";
					}

					if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
						procNote += "錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
					}

					if ("9".equals(result.get("RepayType"))) {
						if (tempVo.get("TempReasonCodeX") != null && tempVo.get("TempReasonCodeX").length() > 0) {
							procNote = procNote + "暫收原因:" + tempVo.get("TempReasonCodeX") + " ";
						}
					}
					if ("1".equals(result.get("RepayType"))) {
						if (tempVo.get("PrePaidTerms") != null
								&& parse.stringToInteger(tempVo.get("PrePaidTerms")) > 0) {
							procNote = procNote + "預繳期數:" + tempVo.get("PrePaidTerms") + "期 ";
						}
						if (tempVo.get("PayIntDate") != null && tempVo.get("PayIntDate").length() > 0) {
							procNote = procNote + "應繳日:" + tempVo.get("PayIntDate") + " ";
						}
					}
					if (tempVo.get("FileSeq") != null && tempVo.get("FileSeq").length() > 0) {
						fileSeq = tempVo.get("FileSeq");
					}
					if (tempVo.get("Txcd") != null && tempVo.get("Txcd").length() > 0) {
						txcd = tempVo.get("Txcd");
					}
				}

				occursList.putParam("OOProcNote", procNote);

				occursList.putParam("OOTxSn", titaVo.getKinbr() + result.get("TitaTlrNo") + result.get("TitaTxtNo"));
				occursList.putParam("OOFileName", result.get("FileName"));
				occursList.putParam("OOFileSeq", fileSeq);
				occursList.putParam("OOTxCd", txcd);

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
			this.info("L4920 oSumPayAmt = " + oSumPayAmt.toString());
			this.totaVo.putParam("OSumPayAmt", oSumPayAmt);
			this.totaVo.putParam("OSumAccAmt", oSumAccAmt);
			this.totaVo.putParam("OSumDisAmt", oSumDisAmt);
		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		List<LinkedHashMap<String, String>> chkOccursList = this.totaVo.getOccursList();

		if (chkOccursList == null || chkOccursList.size() == 0) {
			throw new LogicException("E0001", "查無資料"); // 查無資料
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
//			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4920ServiceImpl.getSize();
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