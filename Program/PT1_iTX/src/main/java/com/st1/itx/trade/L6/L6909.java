package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L6909ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.parse.Parse;

@Service("L6909") // 暫收款查詢(依戶號)
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6909 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	Parse parse;
	@Autowired
	SortMapListCom sortMapListCom;

	@Autowired
	public L6909ServiceImpl l6909ServiceImpl;

	// 額度小計
	private HashMap<Integer, BigDecimal> facmYdBal = new HashMap<>();
	private HashMap<Integer, BigDecimal> facmTdBal = new HashMap<>();
	private HashMap<Integer, Integer> facmEntryDate = new HashMap<>();
	private HashMap<Integer, Integer> facmAcDate = new HashMap<>();
	private int acdate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6909 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 316 * 100 = 31,600

		// 會計日為零則以入帳日找入帳日，會計日不可小於暫收餘額檔會計日
		List<Map<String, String>> acDateList = null;
		try {
			acDateList = l6909ServiceImpl.queryAcDate(titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (acDateList != null) {
			for (Map<String, String> t : acDateList) {
				titaVo.putParam("AcDateS", parse.stringToInteger(t.get("AcDateS")) - 19110000);
				titaVo.putParam("AcDateE", parse.stringToInteger(t.get("AcDateE")) - 19110000);
			}
		}
		// 讀取暫收餘額檔餘額
		List<Map<String, String>> tavList = null;
		try {
			tavList = l6909ServiceImpl.queryDailyTav(titaVo);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}

		if (tavList != null) {
			for (Map<String, String> t : tavList) {
				this.info("YdBal CustNo=" + titaVo.get("CustNo") + ", FacmNo=" + t.get("FacmNo") + ", YdBal="
						+ t.get("YdBal"));
				int facmNo = parse.stringToInteger(t.get("FacmNo"));
				facmYdBal.put(facmNo, parse.stringToBigDecimal(t.get("YdBal")));
				facmTdBal.put(facmNo, parse.stringToBigDecimal(t.get("YdBal")));
				facmAcDate.put(facmNo, parse.stringToInteger(t.get("AcDate")) - 19110000);
			}
		}

		List<Map<String, String>> L6909List = null;
		List<Map<String, String>> oList = new ArrayList<Map<String, String>>();

		try {
			L6909List = l6909ServiceImpl.FindAll(titaVo, this.index, this.limit);
		} catch (Exception e) {
			throw new LogicException(titaVo, "E0001", "SQL ERROR"); // 讀取DB時發生問題
		}
		if (L6909List != null) {
			String newTxNo = "";
			int seq = 0;
			for (Map<String, String> t : L6909List) {
				Map<String, String> da = new HashMap<>();
				acdate = parse.stringToInteger(t.get("AcDate")) - 19110000;
				int facmNo = parse.stringToInteger(t.get("FacmNo"));
				int entrydate = 0;
				String titaHCode = t.get("TitaHCode");
				BigDecimal tavDb = BigDecimal.ZERO;
				BigDecimal tavCr = BigDecimal.ZERO;
				String txNo = "0000" + t.get("TitaTlrNo")
						+ parse.IntegerToString(parse.stringToInteger(t.get("TitaTxtNo")), 8);
				String AcFg = "";
				// 畫面按鈕控制 第一筆為主要次筆為副
				if (txNo.equals(newTxNo)) {
					AcFg = "";
				} else {
					AcFg = "Y";
					newTxNo = txNo;
				}

				String OODesc = t.get("TranItem"); // 交易別
				if (parse.stringToInteger(t.get("EntryDate")) != 0) {
					entrydate = parse.stringToInteger(t.get("EntryDate")) - 19110000;
				}
				tavDb = parse.stringToBigDecimal(t.get("DbAmt"));
				tavCr = parse.stringToBigDecimal(t.get("CrAmt"));

				if (facmTdBal.containsKey(facmNo)) {
					facmTdBal.put(facmNo, facmTdBal.get(facmNo).add(tavCr.subtract(tavDb)));
				} else {
					facmTdBal.put(facmNo, tavCr.subtract(tavDb));
				}
				if (facmEntryDate.containsKey(facmNo)) {
					facmEntryDate.put(facmNo, entrydate);
				}
				if (facmAcDate.containsKey(facmNo)) {
					facmAcDate.put(facmNo, acdate);
				}

				seq++;

				da.put("OOSeq", "" + seq);
				da.put("OOEntryDate", "" + entrydate);// 入帳日期
				da.put("OOAcDate", "" + acdate);// 會計日期
				da.put("OOTAVFacmNo", parse.IntegerToString(facmNo, 3));// 暫收款額度
				da.put("OODesc", OODesc); // 交易別
				da.put("OOTAVDb", "" + tavDb);// 暫收借
				da.put("OOTAVCr", "" + tavCr);// 暫收貸
				da.put("OOTAVBal", "" + facmTdBal.get(facmNo));// 暫收餘額
				da.put("OOAcFg", "" + AcFg);// 畫面控制
				da.put("OOTxNo", "" + txNo);// 畫面控制
				da.put("OOHCode", titaHCode);// 訂正別
				oList.add(da);

			}
			BigDecimal custBal = BigDecimal.ZERO;

//			昨日額度
			if (oList.size() > 0) {
				for (Integer facmNo : facmYdBal.keySet()) {
					if (facmYdBal.get(facmNo).compareTo(BigDecimal.ZERO) != 0) {
						OccursList occursList = new OccursList();
						occursList.putParam("OOEntryDate", "");// 入帳日期
						occursList.putParam("OOAcDate", "");// 會計日期
						occursList.putParam("OOTAVFacmNo", parse.IntegerToString(facmNo, 3));// 暫收款額度
						occursList.putParam("OODesc", "額度"); // 交易別
						occursList.putParam("OOTAVDb", "");// 暫收借
						occursList.putParam("OOTAVCr", "");// 暫收貸
						occursList.putParam("OOTAVBal", facmYdBal.get(facmNo));// 暫收餘額
						occursList.putParam("OOAcFg", "N");// 畫面控制
						occursList.putParam("OOTxNo", "");// 畫面控制
						occursList.putParam("OOHCode", "");// 訂正別
						custBal = custBal.add(facmYdBal.get(facmNo));
						this.totaVo.addOccursList(occursList);
					}
				}

//			昨日全戶
				if (parse.stringToInteger(titaVo.get("FacmNo")) == 0) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOEntryDate", "");// 入帳日期
					occursList.putParam("OOAcDate", "");// 會計日期
					occursList.putParam("OOTAVFacmNo", "");// 暫收款額度
					occursList.putParam("OODesc", "全戶"); // 交易別
					occursList.putParam("OOTAVDb", "");// 暫收借
					occursList.putParam("OOTAVCr", "");// 暫收貸
					occursList.putParam("OOTAVBal", custBal);// 暫收餘額
					occursList.putParam("OOAcFg", "N");// 畫面控制
					occursList.putParam("OOTxNo", "");// 畫面控制
					occursList.putParam("OOHCode", "");// 訂正別
					this.totaVo.addOccursList(occursList);
				}
			}
			for (Map<String, String> da : oList) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOEntryDate", da.get("OOEntryDate"));// 入帳日期
				occursList.putParam("OOAcDate", da.get("OOAcDate"));// 會計日期
				occursList.putParam("OOTAVFacmNo", da.get("OOTAVFacmNo"));// 暫收款額度
				occursList.putParam("OODesc", da.get("OODesc")); // 交易別
				occursList.putParam("OOTAVDb", da.get("OOTAVDb"));// 暫收借
				occursList.putParam("OOTAVCr", da.get("OOTAVCr"));// 暫收貸
				occursList.putParam("OOTAVBal", da.get("OOTAVBal"));// 暫收餘額
				occursList.putParam("OOAcFg", da.get("OOAcFg"));// 畫面控制
				occursList.putParam("OOTxNo", da.get("OOTxNo"));// 畫面控制
				occursList.putParam("OOHCode", da.get("OOHCode"));// 訂正別
				this.totaVo.addOccursList(occursList);
			}

//			額度

			custBal = BigDecimal.ZERO;
			for (Integer facmNo : facmTdBal.keySet()) {
				if (facmTdBal.get(facmNo).compareTo(BigDecimal.ZERO) != 0) {
					OccursList occursList = new OccursList();
					occursList.putParam("OOEntryDate", facmEntryDate.get(facmNo));// 入帳日期
					occursList.putParam("OOAcDate", facmAcDate.get(facmNo));// 會計日期
					occursList.putParam("OOTAVFacmNo", parse.IntegerToString(facmNo, 3));// 暫收款額度
					occursList.putParam("OODesc", "額度"); // 交易別
					occursList.putParam("OOTAVDb", "");// 暫收借
					occursList.putParam("OOTAVCr", "");// 暫收貸
					occursList.putParam("OOTAVBal", facmTdBal.get(facmNo));// 暫收餘額
					occursList.putParam("OOAcFg", "N");// 畫面控制
					occursList.putParam("OOTxNo", "");// 畫面控制
					occursList.putParam("OOHCode", "");// 訂正別
					custBal = custBal.add(facmTdBal.get(facmNo));
					this.totaVo.addOccursList(occursList);
				}
			}

//			全戶
			if (parse.stringToInteger(titaVo.get("FacmNo")) == 0) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOEntryDate", "");// 入帳日期
				occursList.putParam("OOAcDate", "");// 會計日期
				occursList.putParam("OOTAVFacmNo", "");// 暫收款額度
				occursList.putParam("OODesc", "全戶"); // 交易別
				occursList.putParam("OOTAVDb", "");// 暫收借
				occursList.putParam("OOTAVCr", "");// 暫收貸
				occursList.putParam("OOTAVBal", custBal);// 暫收餘額
				occursList.putParam("OOAcFg", "N");// 畫面控制
				occursList.putParam("OOTxNo", "");// 畫面控制
				occursList.putParam("OOHCode", "");// 訂正別
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}