package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.AcMainId;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*----------------------- AcMainCom 總帳入帳處理 ------------------*/
//    --- Input ---
//     1.功能
//        0.upd    入帳更新
//        1.core   在核心入帳(新建銷帳檔)
//        2.chgDy  系統換日過帳(含年初損益類結轉)  
//    
//     2.訂正記號 
//        AcHcode        DECIMAL(1)        
//*     0-正常 
//*     1-訂正未放行(刪除明細)
//*     2-訂正或沖正帳務(要入總帳) 
//*     3-訂正帳務(不入總帳)
//    
//     3.會計帳務明細 ArrayList
//    
//      
//    --- Output ---
//     None

/**
 * 總帳入帳處理<BR>
 * 1. run ：入帳更新總帳檔 call by AcEntetCom<BR>
 * 1.1 由會計明細累加至總帳檔帳冊別金額，更新餘額 、借貸金額欄<BR>
 * 1.2 帳冊別差額，寫入應收調撥款科目<BR>
 * 2. core ： 在核心入帳，更新餘額欄 call by AcReceivableCom<BR>
 * 2.1 業務銷帳科目如以維護方式寫入則由核心出帳，ex.暫付火險費借方<BR>
 * 2.2 更新餘額 、借貸金額寫入核心欄<BR>
 * 3. changeDate：系統換日過帳(含年初損益類結轉) call by BS001(日始作業) <BR>
 * 3.1 將餘額過至次日<BR>
 * 3.2 年初時將損益類科目(4,5,6)，餘額歸零<BR>
 * 
 * @author st1
 *
 */
@Component("acMainCom")
@Scope("prototype")
public class AcMainCom extends TradeBuffer {
	@Autowired
	public AcMainService acMainService;
	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	Parse parse;
	private CdAcCode tCdAcCode = new CdAcCode();
	private AcMain tAcMain = new AcMain();
	private AcMainId tAcMainId = new AcMainId();
	private String debits[] = { "1", "5", "6", "9" }; // 資產、支出為借方科目，負債、收入為貸方科目
	private List<String> debitsList = Arrays.asList(debits);
	private String profLoss[] = { "4", "5", "6" }; // 損益類科目
	private List<String> profLossList = Arrays.asList(profLoss);
	private String dbCr;
	private BigDecimal txAmt;
	private BigDecimal acBookDiff;
	private String acSubBookCode;
	private String acctCode;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 入帳更新
	 * 
	 * @param AcHcode 帳務訂正記號 // 帳務訂正記號 0-正常 1-訂正(刪除帳務) 2-出沖正帳務(入帳) 3-出沖正帳務(不入帳)
	 * @param acList  List of AcDetail
	 * @param titaVo  TitaVo
	 * @throws LogicException ...
	 */
	public void upd(int AcHcode, List<AcDetail> acList, TitaVo titaVo) throws LogicException {
		this.info("AcMainCom.... : AcHCode=" + AcHcode + ",acListsize=" + acList.size());
		acBookDiff = BigDecimal.ZERO;
		acSubBookCode = "";
		acctCode = "";
		HashMap<String, BigDecimal> map = new HashMap<>();
		for (AcDetail ac : acList) {
			// 借貸別
			dbCr = ac.getDbCr();
			if (AcHcode == 3) {
				if (ac.getDbCr().equals("D")) {
					dbCr = "C";
				} else {
					dbCr = "D";
				}
			} else {
				dbCr = ac.getDbCr();
			}
			// 交易金額
			if (AcHcode == 1 || AcHcode == 3) {
				txAmt = BigDecimal.ZERO.subtract(ac.getTxAmt()); // 當日訂正為負
			} else {
				txAmt = ac.getTxAmt(); // 正常及隔日訂正為正
			}
			// 業務科目代號
			acctCode = ac.getAcctCode();
			tAcMainId.setAcBookCode(ac.getAcBookCode());
			tAcMainId.setAcSubBookCode(ac.getAcSubBookCode());
			tAcMainId.setBranchNo(ac.getBranchNo());
			tAcMainId.setCurrencyCode(ac.getCurrencyCode());
			tAcMainId.setAcNoCode(ac.getAcNoCode());
			tAcMainId.setAcSubCode(ac.getAcSubCode());
			tAcMainId.setAcDtlCode(ac.getAcDtlCode());
			tAcMainId.setAcDate(ac.getAcDate());
			procUpdate(0, titaVo);
			if (dbCr.equals("D"))
				acBookDiff = ac.getTxAmt();
			else
				acBookDiff = BigDecimal.ZERO.subtract(ac.getTxAmt());
			// 累加區隔帳冊金額 +借 -貸
			acSubBookCode = ac.getAcSubBookCode();
			if (map.containsKey(acSubBookCode)) {
				map.put(acSubBookCode, map.get(acSubBookCode).add(acBookDiff));
			} else {
				map.put(acSubBookCode, acBookDiff);
			}

		}
		for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
			acSubBookCode = entry.getKey();
			acBookDiff = entry.getValue();
			// 帳冊別中介-應收調撥款
			if (acBookDiff.compareTo(BigDecimal.ZERO) != 0) {
				tCdAcCode = cdAcCodeService.acCodeAcctFirst("ACB", titaVo);
				if (tCdAcCode == null) {
					throw new LogicException("E6003", "AcDetailCom 會計科子細目設定檔應收調撥款科目(ACB)notfound");
				} else {
					acctCode = "ACB"; // 應收調撥款
					tAcMainId.setAcBookCode(acList.get(0).getAcBookCode());
					tAcMainId.setAcSubBookCode(acSubBookCode);
					tAcMainId.setBranchNo(acList.get(0).getBranchNo());
					tAcMainId.setCurrencyCode(acList.get(0).getCurrencyCode());
					tAcMainId.setAcNoCode(tCdAcCode.getAcNoCode());
					tAcMainId.setAcSubCode(tCdAcCode.getAcSubCode());
					tAcMainId.setAcDtlCode(tCdAcCode.getAcDtlCode());
					tAcMainId.setAcDtlCode("  ");
					tAcMainId.setAcDate(acList.get(0).getAcDate());
					if (acBookDiff.compareTo(BigDecimal.ZERO) > 0) {
						dbCr = "C";
					} else {
						dbCr = "D";
						acBookDiff = BigDecimal.ZERO.subtract(acBookDiff);
					}
					if (AcHcode == 1 || AcHcode == 3) {
						txAmt = BigDecimal.ZERO.subtract(acBookDiff); // 訂正為負
					} else {
						txAmt = acBookDiff; // 正常及隔日訂正為正
					}
					procUpdate(0, titaVo);
				}
			}
		}
		// END
		return;

	}

	/**
	 * 在核心入帳，更新餘額欄
	 * 
	 * @param AcHcode 帳務訂正記號 0.正常 1.訂正 2.3 沖正
	 * @param ac      AcDetail
	 * @param titaVo  TitaVo
	 * @throws LogicException LogicException
	 */
	public void core(int AcHcode, AcDetail ac, TitaVo titaVo) throws LogicException {
		acctCode = ac.getAcctCode();
		// 借貸別
		if (AcHcode == 3) {
			if (ac.getDbCr().equals("D")) {
				dbCr = "C";
			} else {
				dbCr = "D";
			}
		} else {
			dbCr = ac.getDbCr();
		}
		// 交易金額
		if (AcHcode == 1 || AcHcode == 3) {
			txAmt = BigDecimal.ZERO.subtract(ac.getTxAmt()); // 當日訂正為負
		} else {
			txAmt = ac.getTxAmt(); // 正常及隔日訂正為正
		}
		// 1.<000:全帳冊>記到子目
		tAcMainId.setAcBookCode("000");
		tAcMainId.setAcSubBookCode(ac.getAcSubBookCode());
		tAcMainId.setBranchNo(ac.getBranchNo());
		tAcMainId.setCurrencyCode(ac.getCurrencyCode());
		tAcMainId.setAcNoCode(ac.getAcNoCode());
		tAcMainId.setAcSubCode(ac.getAcSubCode());
		tAcMainId.setAcDtlCode(ac.getAcDtlCode());
		tAcMainId.setAcDate(ac.getAcDate());
		procUpdate(1, titaVo);

		return;
	}

	/**
	 * 系統換日過帳(含年初損益類結轉)
	 * 
	 * @param LbizDate 上營業日
	 * @param acDate   會計日期
	 * @param acList   List of AcMain
	 * @param titaVo   TitaVo
	 * @throws LogicException LogicException
	 */
	public void changeDate(int LbizDate, int acDate, List<AcMain> acList, TitaVo titaVo) throws LogicException {
		for (AcMain ac : acList) {
			acctCode = ac.getAcctCode();
			txAmt = ac.getTdBal(); // 昨日餘額
			// 跨年、損益類
			if (acDate / 10000 != LbizDate / 10000)
				if (profLossList.contains(tAcMainId.getAcNoCode().substring(0, 1)))
					txAmt = BigDecimal.ZERO;
			tAcMainId.setAcBookCode(ac.getAcBookCode());
			tAcMainId.setAcSubBookCode(ac.getAcSubBookCode());
			tAcMainId.setBranchNo(ac.getBranchNo());
			tAcMainId.setCurrencyCode(ac.getCurrencyCode());
			tAcMainId.setAcNoCode(ac.getAcNoCode());
			tAcMainId.setAcSubCode(ac.getAcSubCode());
			tAcMainId.setAcDtlCode(ac.getAcDtlCode());
			tAcMainId.setAcDate(acDate);
			procUpdate(2, titaVo);
		}
		return;
	}

	/* 更新總帳檔 */
	private void procUpdate(int Funcd, TitaVo titaVo) throws LogicException {
// 0-入帳更新, 1-在核心入帳，更新餘額欄, 2-系統換日過帳(含年初損益類結轉)  
		tAcMain = acMainService.holdById(tAcMainId, titaVo); // holdById
		if (tAcMain == null) {
			newAcMain(); // 新總帳檔
			computeAmts(Funcd); // 計算金額
			this.info("procUpdate.... : id= " + tAcMainId + "acctCode=" + acctCode + " " + tAcMain.getAcctCode());
			try {
				acMainService.insert(tAcMain, titaVo); // insert AcMain
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "tAcMain insert " + tAcMainId + e.getErrorMsg());
			}
		} else {
			computeAmts(Funcd); // 計算金額
			try {
				acMainService.update(tAcMain, titaVo); // update AcMain
			} catch (DBException e) {
				throw new LogicException(titaVo, "E6003", "tAcMain update " + tAcMainId + e.getErrorMsg());
			}
		}
	}

	private void newAcMain() {

		tAcMain = new AcMain();
		tAcMain.setAcMainId(tAcMainId);
	}

	private void computeAmts(int Funcd) {
		this.info("computeAmts Funcd=" + Funcd + ", DbCr=" + dbCr + ", txAmt=" + txAmt + tAcMain.toString());
		// 0.入帳更新
		if (Funcd == 0) {
			if (dbCr.equals("D")) {
				tAcMain.setDbAmt(tAcMain.getDbAmt().add(txAmt)); // 借方金額
				if (txAmt.compareTo(BigDecimal.ZERO) > 0)
					tAcMain.setDbCnt(tAcMain.getDbCnt() + 1);
				else
					tAcMain.setDbCnt(tAcMain.getDbCnt() - 1);
			} else {
				tAcMain.setCrAmt(tAcMain.getCrAmt().add(txAmt)); // 貸方金額
				if (txAmt.compareTo(BigDecimal.ZERO) > 0)
					tAcMain.setCrCnt(tAcMain.getCrCnt() + 1);
				else
					tAcMain.setCrCnt(tAcMain.getCrCnt() - 1);
			}
		}

		// 1.在核心入帳，更新餘額欄
		if (Funcd == 1) {
			if (dbCr.equals("D")) {
				tAcMain.setCoreDbAmt(tAcMain.getCoreDbAmt().add(txAmt)); // 借方金額
				if (txAmt.compareTo(BigDecimal.ZERO) > 0)
					tAcMain.setCoreDbCnt(tAcMain.getCoreDbCnt() + 1);
				else
					tAcMain.setCoreDbCnt(tAcMain.getCoreDbCnt() - 1);
			} else {
				tAcMain.setCoreCrAmt(tAcMain.getCoreCrAmt().add(txAmt)); // 貸方金額
				if (txAmt.compareTo(BigDecimal.ZERO) > 0)
					tAcMain.setCoreCrCnt(tAcMain.getCoreCrCnt() + 1);
				else
					tAcMain.setCoreCrCnt(tAcMain.getCoreCrCnt() - 1);
			}
		}
		// 2.系統換日過帳(含年初損益類結轉)
		if (Funcd == 2) {
			tAcMain.setYdBal(txAmt);
		}

		// all
// 借方科目{ "1", "5","6","9" }資產
// 借方科目本日餘額 = 昨日餘額 + 借方金額 - 貸方金額
// 貸方科目本日餘額 = 昨日餘額 + 貸方金額  - 借方金額
		if (debitsList.contains(tAcMainId.getAcNoCode().substring(0, 1)))
			tAcMain.setTdBal(tAcMain.getYdBal().add(tAcMain.getDbAmt()).subtract(tAcMain.getCrAmt())
					.add(tAcMain.getCoreDbAmt()).subtract(tAcMain.getCoreCrAmt()));
		else
			tAcMain.setTdBal(tAcMain.getYdBal().add(tAcMain.getCrAmt()).subtract(tAcMain.getDbAmt())
					.add(tAcMain.getCoreCrAmt()).subtract(tAcMain.getCoreDbAmt()));

		// acctCode
		tAcMain.setAcctCode(acctCode);

		// MonthEndYm // 平常日-> 0, 月底日資料 -> yyyymm,ex.202005
		if (tAcMainId.getAcDate() == txBuffer.getTxCom().getMfbsdy())
			tAcMain.setMonthEndYm(txBuffer.getTxCom().getMfbsdyf() / 100);
		else
			tAcMain.setMonthEndYm(0);

		this.info("computeAmts " + tAcMain.toString());
	}
}
