package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L4943ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustNo=9,7<br>
 * EntryDateFm=9,7<br>
 * EntryDateTo=9,7<br>
 * PostLimitAmt=9,14.2<br>
 * SingleLimet=9,14.2<br>
 * END=X,1<br>
 */

@Service("L4943")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4943 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4943.class);

	/* DB服務注入 */
	@Autowired
	public L4943ServiceImpl l4943ServiceImpl;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	private CdCodeService cdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4943 ");
		this.totaVo.init(titaVo);

		BigDecimal totUnpaidAmt = BigDecimal.ZERO;
		BigDecimal totTempAmt = BigDecimal.ZERO;
		BigDecimal totRepayAmt = BigDecimal.ZERO;

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 150;

//		1.戶號 = 戶號+入帳日
//		2.上限金額 = 入帳日+限額
//		3.下限金額 = 入帳日+限額
//		4.檢核不正常(Aml) = 入帳日+銀行別
//		9.整批 = 銀行別+還款別+入帳日

		List<Map<String, String>> resulAlltList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> resulParttList = new ArrayList<Map<String, String>>();

		try {
			// *** 折返控制相關 ***
			resulAlltList = l4943ServiceImpl.findAll(1, titaVo);
		} catch (Exception e) {
			this.error("l4943ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}
		if (resulAlltList != null && resulAlltList.size() > 0) {
//			先將應收金額、暫收金額、還款金額加總
			totUnpaidAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("F0"));
			totTempAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("F1"));
			totRepayAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("F2"));
		}

		try {
			// *** 折返控制相關 ***
			resulParttList = l4943ServiceImpl.findAll(0, this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.error("l4943ServiceImpl findByCondition " + e.getMessage());
			throw new LogicException("E0013", e.getMessage());
		}

		if (resulParttList != null && resulParttList.size() > 0) {

			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if (resulParttList.size() == this.limit && hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}

			for (Map<String, String> result : resulParttList) {
				int entryDate = parse.stringToInteger(result.get("F0"));
				int prevIntDate = parse.stringToInteger(result.get("F4"));
				int payIntDate = parse.stringToInteger(result.get("F5"));
				int acDate = parse.stringToInteger(result.get("F11"));

				if (entryDate > 19110000) {
					entryDate = entryDate - 19110000;
				}
				if (prevIntDate > 19110000) {
					prevIntDate = prevIntDate - 19110000;
				}
				if (payIntDate > 19110000) {
					payIntDate = payIntDate - 19110000;
				}
				if (acDate > 19110000) {
					acDate = acDate - 19110000;
				}

				OccursList occursList = new OccursList();

				occursList.putParam("OOEntryDate", entryDate);
				occursList.putParam("OOCustNo", result.get("F1"));
				occursList.putParam("OOFacmNo", result.get("F2"));
				occursList.putParam("OOBormNo", result.get("F3"));
				occursList.putParam("OOPrevIntDate", prevIntDate);
				occursList.putParam("OOPayIntDate", payIntDate);
				occursList.putParam("OORepayType", result.get("F6"));
				occursList.putParam("OOUnpaidAmt", result.get("F7"));
				occursList.putParam("OOTempAmt", result.get("F8"));
				occursList.putParam("OORepayAmt", result.get("F9"));
				occursList.putParam("OOMediaCode", result.get("F10"));
				occursList.putParam("OOAcDate", acDate);

				TempVo tempVo = new TempVo();
				tempVo = tempVo.getVo(result.get("F12"));

				String procNote = "";

				if (tempVo.get("Aml") != null && tempVo.get("Aml").length() > 0) {
					procNote = "Aml檢核訊息：" + amlX(tempVo.get("Aml"), titaVo) + "。";
				}

				if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
					procNote = procNote + "帳號授權檢核：" + authX(tempVo.get("Auth"), titaVo) + "。";
				}

				if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
					procNote = procNote + "扣款檢核：" + tempVo.get("Deduct") + "。";
				}

				occursList.putParam("OOAmlRsp", " ");

				occursList.putParam("OOProcNote", procNote);
//					 將每筆資料放入Tota的OcList 
				this.totaVo.addOccursList(occursList);
			}

			totaVo.putParam("OTotalUnpaidAmt", totUnpaidAmt);
			totaVo.putParam("OTotalTempAmt", totTempAmt);
			totaVo.putParam("OTotalRepayAmt", totRepayAmt);

		} else {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

//		this.totaVo.putParam("OTotCnt", singleCnt);
//		this.totaVo.putParam("OTotAmt", totAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String authX(String auth, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "AuthStatusCode", auth, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private String amlX(String aml, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "AmlCheckItem", aml, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
	}

	private Boolean hasNext() {
		Boolean result = true;

		int times = this.index + 1;
		int cnt = l4943ServiceImpl.getSize();
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