package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
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
			resultAllList = l4920ServiceImpl.findAll(1, titaVo);
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

				int entryDate = 0;

				if (parse.stringToInteger(result.get("F1")) > 19110000) {
					entryDate = parse.stringToInteger(result.get("F1")) - 19110000;
				}

				occursList.putParam("OODetailSeq", result.get("F0"));
				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOCustNo", result.get("F2"));
				occursList.putParam("OOFacmNo", result.get("F3"));
				occursList.putParam("OORepayType", result.get("F4"));
				occursList.putParam("OORepayCode", result.get("F5"));
				occursList.putParam("OOReconCode", result.get("F6"));
				occursList.putParam("OOReconCodeX", result.get("F7"));
				occursList.putParam("OORepayAmt", result.get("F8"));
				occursList.putParam("OOAcctAmt", result.get("F9"));

				if (enterList.contains(result.get("F10"))) {
					occursList.putParam("OODisacctAmt", parse.stringToBigDecimal(result.get("F8"))
							.subtract(parse.stringToBigDecimal(result.get("F9"))));
					occursList.putParam("OODisacctAmt", BigDecimal.ZERO);
				} else {
					occursList.putParam("OODisacctAmt", BigDecimal.ZERO);
				}
				occursList.putParam("OOProcStsCode", result.get("F10"));
				occursList.putParam("OOProcCode", result.get("F11"));

				String procNote = "";
				if (result.get("F12") != null) {

					TempVo tempVo = new TempVo();
					tempVo = tempVo.getVo(result.get("F12"));

					if (tempVo.get("CheckMsg") != null && tempVo.get("CheckMsg").length() > 0) {
						procNote = "檢核訊息:" + tempVo.get("CheckMsg") + " ";
					}

					if (tempVo.get("ErrorMsg") != null && tempVo.get("ErrorMsg").length() > 0) {
						procNote = procNote + "錯誤訊息:" + tempVo.get("ErrorMsg") + " ";
					}

					if (tempVo.get("Note") != null && tempVo.get("Note").length() > 0) {
						procNote = procNote + "摘要:" + tempVo.get("Note");
					}

//					當吃檔進去時不會寫入還款類別，檢核後才會寫入。
//					若該筆無還款類別且為數字型態，顯示虛擬帳號
					if (tempVo.get("VirtualAcctNo") != null && parse.stringToInteger(result.get("F4")) == 0
							&& isNumeric(tempVo.get("VirtualAcctNo"))) {
						procNote = procNote + "虛擬帳號:" + tempVo.get("VirtualAcctNo");
					}
				}

				occursList.putParam("OOProcNote", procNote);

				occursList.putParam("OOTxSn", titaVo.getKinbr() + result.get("F13") + result.get("F14"));

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