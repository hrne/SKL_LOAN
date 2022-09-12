package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.cm.L4943ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4943")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4943 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	L4943ServiceImpl l4943ServiceImpl;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	CdCodeService cdCodeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4943 ");
		this.totaVo.init(titaVo);

		BigDecimal totUnpaidAmt = BigDecimal.ZERO;
		BigDecimal totTempAmt = BigDecimal.ZERO;
		BigDecimal totRepayAmt = BigDecimal.ZERO;

		int functionCode = parse.stringToInteger(titaVo.getParam("FunctionCode").trim());

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100;

//		1.戶號 = 戶號+入帳日
//		2.上限金額 = 入帳日+限額
//		3.下限金額 = 入帳日+限額
//		4.檢核不正常(Aml) = 入帳日+銀行別
//		9.整批 = 銀行別+還款別+入帳日

		List<Map<String, String>> resulAlltList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> resulParttList = new ArrayList<Map<String, String>>();

		if (functionCode != 7) {
			try {
				// *** 折返控制相關 ***
				resulAlltList = l4943ServiceImpl.findAll(1, titaVo);
			} catch (Exception e) {
				this.error("l4943ServiceImpl findByCondition " + e.getMessage());
				throw new LogicException("E0013", e.getMessage());
			}
		}

		if (resulAlltList != null && resulAlltList.size() > 0) {
			totUnpaidAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("UnpaidAmt"));
			totTempAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("TempAmt"));
			totRepayAmt = parse.stringToBigDecimal(resulAlltList.get(0).get("RepayAmt"));
		}

		if (functionCode == 6) {
			totaVo.putParam("OTotalUnpaidAmt", totUnpaidAmt);
			totaVo.putParam("OTotalTempAmt", totTempAmt);
			totaVo.putParam("OTotalRepayAmt", totRepayAmt);
		} else {

			if (functionCode == 7) {
				// 7:已到期未至應繳日
				try {
					resulParttList = l4943ServiceImpl.doQuery7(this.index, this.limit, titaVo);
				} catch (Exception e) {
					this.error("l4943ServiceImpl findByCondition " + e.getMessage());
					throw new LogicException("E0013", e.getMessage());
				}
			} else {
				try {
					// *** 折返控制相關 ***
					resulParttList = l4943ServiceImpl.findAll(0, this.index, this.limit, titaVo);
				} catch (Exception e) {
					this.error("l4943ServiceImpl findByCondition " + e.getMessage());
					throw new LogicException("E0013", e.getMessage());
				}
			}

			if (resulParttList != null && resulParttList.size() > 0) {

				for (Map<String, String> result : resulParttList) {
					int entryDate = parse.stringToInteger(result.get("EntryDate"));
					int prevIntDate = parse.stringToInteger(result.get("PrevIntDate"));
					int payIntDate = parse.stringToInteger(result.get("PayIntDate"));
					int acDate = parse.stringToInteger(result.get("AcDate"));

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
					occursList.putParam("OOCustNo", result.get("CustNo"));
					occursList.putParam("OOFacmNo", result.get("FacmNo"));
					if (functionCode == 7 && entryDate == 0) {
						int maturityDate = parse.stringToInteger(result.get("MaturityDate"));
						int nextPayIntDate = parse.stringToInteger(result.get("NextPayIntDate"));
						BigDecimal loanBal = result.get("LoanBal") == null ? BigDecimal.ZERO
								: new BigDecimal(result.get("LoanBal"));
						if (maturityDate > 19110000) {
							maturityDate = maturityDate - 19110000;
						}
						if (nextPayIntDate > 19110000) {
							nextPayIntDate = nextPayIntDate - 19110000;
						}
						occursList.putParam("OOPrevIntDate", maturityDate);
						occursList.putParam("OOPayIntDate", nextPayIntDate);
						occursList.putParam("OOUnpaidAmt", loanBal);
						totUnpaidAmt = totUnpaidAmt.add(loanBal);
					} else {
						occursList.putParam("OOPrevIntDate", prevIntDate);
						occursList.putParam("OOPayIntDate", payIntDate);
						occursList.putParam("OOUnpaidAmt", result.get("UnpaidAmt"));
					}
					occursList.putParam("OORepayType", result.get("RepayType"));
					occursList.putParam("OOTempAmt", result.get("TempAmt"));
					occursList.putParam("OORepayAmt", result.get("RepayAmt"));
					occursList.putParam("OOMediaCode", result.get("MediaCode"));
					occursList.putParam("OOAcDate", acDate);
					if (functionCode == 10) { // 入帳後溢短收
						occursList.putParam("OOTempAmt", result.get("TxTempAmt"));
						occursList.putParam("OOOverShort", result.get("OverShort"));
					}
					String procNote = "";
					TempVo tempVo = new TempVo();
					tempVo = tempVo.getVo(result.get("JsonFields"));
					String returnCode = result.get("ReturnCode");
					String mediaKind = result.get("MediaKind");
					String amlRsp = result.get("AmlRsp");
					if (functionCode == 7) {
						int maturityDate = parse.stringToInteger(result.get("MaturityDate"));
						procNote = "已到期未至應繳日, 到期日:" + maturityDate;
					} else if (acDate > 0) {
						if (tempVo.get("ProcStsCode") != null && tempVo.get("ProcStsCode").length() > 0) {
							procNote = procStsCodeX(tempVo.get("ProcStsCode"), titaVo);
						}
					} else if (returnCode == null || returnCode.trim().isEmpty()) {

//					順序為帳號授權、AML、扣帳金額為零、扣款金額(by 額度)超過帳號設定限額(限額為零不檢查)
						if (tempVo.get("Auth") != null && tempVo.get("Auth").length() > 0) {
							procNote = "帳號授權檢核:" + authX(tempVo.get("Auth"), titaVo) + "。";
						} else if ("1".equals(amlRsp) || "2".equals(amlRsp)) {
							procNote = "Aml檢核:" + amlX(amlRsp, titaVo) + "。";
						} else if (tempVo.get("Deduct") != null && tempVo.get("Deduct").length() > 0) {
							procNote = "扣款檢核：" + tempVo.get("Deduct") + "。";
						}
					} else {
						if ("00".equals(returnCode)) {
							procNote = "扣款成功";
						} else {
							procNote = "扣款失敗：" + procCodeX(
									"3".equals(mediaKind) ? "003" + returnCode : "002" + returnCode, titaVo);
						}
					}
					occursList.putParam("OOAmlRsp", " ");
					occursList.putParam("OReturnCode", returnCode);
					occursList.putParam("OOProcNote", procNote);
//					 將每筆資料放入Tota的OcList 
					this.totaVo.addOccursList(occursList);
				}
				totaVo.putParam("OTotalUnpaidAmt", totUnpaidAmt);
				totaVo.putParam("OTotalTempAmt", totTempAmt);
				totaVo.putParam("OTotalRepayAmt", totRepayAmt);

				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (resulParttList.size() == this.limit && hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

			} else {
				throw new LogicException(titaVo, "E0001", "查無資料");
			}
		} // else

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String procStsCodeX(String procStsCode, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "ProcStsCode", procStsCode, titaVo);

		if (cdCode != null) {
			result = cdCode.getItem();
		}

		return result;
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

	private String procCodeX(String procCode, TitaVo titaVo) {
		String result = "";

		CdCode cdCode = cdCodeService.getItemFirst(4, "ProcCode", procCode, titaVo);

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