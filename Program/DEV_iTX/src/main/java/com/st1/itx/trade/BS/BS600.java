package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.springjpa.cm.BS600ServiceImpl;
import com.st1.itx.main.ApControl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("BS600")
@Scope("prototype")
/**
 * 放款戶帳冊別轉換<br>
 * 
 * 執行時機：日始作業(BS001)，帳冊別帳務調整日期等於系統營業日時自動執行<br>
 * 
 * 1.L6709帳冊別目標金額維護交易時，更新系統參數設定檔的帳冊別帳務調整日期()<br>
 * 
 * 2.依帳冊別金額設定檔內的分配順序及放款目標金額，重置帳冊別(區隔帳冊)，原帳冊別排序優先
 * 2.1 排相同帳冊別、相同區隔帳冊
 * 2.1 排相同帳冊別、區隔帳冊=00A-傳統帳冊(參數檔)、有餘額
 * 2.3 排相同帳冊別、按分配順序排、有餘額
 *  
 * 3.BY 戶號啟動 L6801 放款戶帳冊別轉換<br>
 * 
 * 4.L6905日結明細查詢(經辦別:999999，整批批號:BS600)，可查詢帳務明細<br>
 *
 * 
 * @author LAI
 * @version 1.0.0
 */
public class BS600 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(BS600.class);

	@Autowired
	public Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	BS600ServiceImpl sBS600ServiceImpl;

	@Autowired
	CdAcBookService cdAcBookService;

	@Autowired
	TxTellerService txtellerService;

	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS600 run ... ");
		// 查帳冊別代碼檔
		Slice<CdAcBook> sCdAcBook = cdAcBookService.findAll(0, Integer.MAX_VALUE, titaVo);

		List<CdAcBook> lCdAcBook = sCdAcBook == null ? null : new ArrayList<CdAcBook>(sCdAcBook.getContent());

		if (lCdAcBook == null) {
			this.info("BS600 replaceAcBookCode CdAcBook無資料,結束.");
			return null;
		}
		// 按分配順序排序
		Collections.sort(lCdAcBook, new Comparator<CdAcBook>() {
			public int compare(CdAcBook c1, CdAcBook c2) {
				if (c1.getAssignSeq() != c2.getAssignSeq()) {
					return c1.getAssignSeq() - c2.getAssignSeq();
				}
				return 0;
			}
		});

		// 將放款實際金額歸零
		for (CdAcBook tCdAcBook : lCdAcBook) {
			this.info("BS600 replaceAcBookCode CdAcBook = " + tCdAcBook);
			tCdAcBook.setActualAmt(BigDecimal.ZERO);
		}

		// 依未銷戶號加總，按帳冊別、分配順序、戶號排序
		List<Map<String, String>> sqlResult = null;
		try {
			// 計算各戶號餘額
			sqlResult = sBS600ServiceImpl.execSql(titaVo);

		} catch (Exception e) {
			this.error("BS600 Error" + e.getMessage());
		}

		if (sqlResult == null) {
			this.info("BS600 replaceAcBookCode sBS600ServiceImpl 無資料,結束.");
			return null;
		}

		for (CdAcBook tCdAcBook : lCdAcBook) {
			// 將放款實際金額歸零
			tCdAcBook.setActualAmt(BigDecimal.ZERO);
			// Step 1. 排相同帳冊別、相同區隔帳冊
			for (Map<String, String> result : sqlResult) {
				if (result.get("F1").equals(tCdAcBook.getAcBookCode())
						&& result.get("F2").equals(tCdAcBook.getAcSubBookCode())) {
					BigDecimal bal = new BigDecimal(result.get("F3"));
					// 若此戶號尚未被指派到新帳冊別且放款目標金額 >= 放款實際金額，將戶號指派到此帳冊別
					if (result.get("newAcSubBookCode") == null
							&& tCdAcBook.getTargetAmt().compareTo(tCdAcBook.getActualAmt().add(bal)) >= 0) {
						this.info("BS600 replaceAcBookCode ======== Step 1 ============");
						setingAcSubBookCode(result, tCdAcBook, bal, titaVo);
					}
				}
			}
			// Step 2. 排相同帳冊別、區隔帳冊=00A-傳統帳冊(參數檔)、有餘額
			for (Map<String, String> result : sqlResult) {
				if (result.get("F1").equals(tCdAcBook.getAcBookCode())
						&& result.get("F2").equals(this.txBuffer.getSystemParas().getAcSubBookCode())) {
					BigDecimal bal = new BigDecimal(result.get("F3"));
					// 若此戶號尚未被指派到新帳冊別且放款目標金額 >= 放款實際金額，將戶號指派到此帳冊別
					if (result.get("newAcSubBookCode") == null && bal.compareTo(BigDecimal.ZERO) > 0
							&& tCdAcBook.getTargetAmt().compareTo(tCdAcBook.getActualAmt().add(bal)) >= 0) {
						this.info("BS600 replaceAcBookCode ======== Step 1 ============");
						setingAcSubBookCode(result, tCdAcBook, bal, titaVo);
					}
				}
			}

			// Step 3. 排相同帳冊別、按分配順序排、有餘額
			for (Map<String, String> result : sqlResult) {
				if (result.get("F1").equals(tCdAcBook.getAcBookCode())) {
					BigDecimal bal = new BigDecimal(result.get("F3"));
					// 若此戶號尚未被指派到新帳冊別且放款目標金額 >= 放款實際金額，將戶號指派到此帳冊別
					if (result.get("newAcSubBookCode") == null && bal.compareTo(BigDecimal.ZERO) > 0
							&& tCdAcBook.getTargetAmt().compareTo(tCdAcBook.getActualAmt().add(bal)) >= 0) {
						this.info("BS600 replaceAcBookCode ======== Step 1 ============");
						setingAcSubBookCode(result, tCdAcBook, bal, titaVo);
					}
				}
			}
		}

		this.info("BS600 ========== CdAcBook ==========");

		// 重算原區隔帳冊的放款實際金額(實際金額不正確時校正)，00A-傳統帳冊(參數檔)除外
		for (CdAcBook tCdAcBook : lCdAcBook) {
			BigDecimal actualAmt = BigDecimal.ZERO;
			for (Map<String, String> result : sqlResult) {
				if (result.get("F1").equals(tCdAcBook.getAcBookCode())
						&& result.get("F2").equals(tCdAcBook.getAcSubBookCode()))
					actualAmt = actualAmt.add(new BigDecimal(result.get("F3").trim()));
			}
			tCdAcBook.setActualAmt(actualAmt);
			this.info("BS600 reset CdAcBook " + tCdAcBook.toString());
		}

		try {
			cdAcBookService.updateAll(lCdAcBook, titaVo); // update All
		} catch (DBException e) {
			e.printStackTrace();
			throw new LogicException(titaVo, "E0613", "CdAcBook update " + e.getErrorMsg());
		}

		this.batchTransaction.commit();

		// Step 2. 無區隔帳冊，設定為00A-傳統帳冊(參數檔)
		for (Map<String, String> result : sqlResult) {
			if (result.get("newAcSubBookCode") == null) {
				result.put("newAcSubBookCode", this.txBuffer.getSystemParas().getAcSubBookCode());
			}
		//	if (!result.get("F2").equals(result.get("newAcSubBookCode"))) {
				this.info("BS600 convert custNo = " + result.get("F0") + " , oldAcSubBookCode = " + result.get("F2")
						+ " , newAcSubBookCode = " + result.get("newAcSubBookCode") + " ,Bal=" + result.get("F3"));
		//	}
		}

		// 紀錄帳冊別有異動的戶號執行L6801放款戶帳冊別轉換
		for (Map<String, String> result : sqlResult) {
			// 戶號
			int custNo = Integer.parseInt(result.get("F0").trim());
			// 帳冊別
			String acBookCode = result.get("F1");
			// 原區隔帳冊
			String oldAcSubBookCode = result.get("F2");
			// 新區隔帳冊
			String newAcSubBookCode = result.get("newAcSubBookCode");

			// 紀錄帳冊別有異動的戶號
			if (!oldAcSubBookCode.equals(newAcSubBookCode)) {
				/* 執行交易 L6801放款戶帳冊別轉換 */
//  old    new 
// "201"  "00A"   ("201","00A")
// "00A"  "201"   ("00A","201")
// "201"  "301"   ("201","00A") ("00A","301")
				if (!oldAcSubBookCode.equals(this.txBuffer.getSystemParas().getAcSubBookCode())) {
					excuteTxCode(custNo, acBookCode, oldAcSubBookCode,
							this.txBuffer.getSystemParas().getAcSubBookCode(), titaVo);
				}

				if (!newAcSubBookCode.equals(this.txBuffer.getSystemParas().getAcSubBookCode())) {
					excuteTxCode(custNo, acBookCode, this.txBuffer.getSystemParas().getAcSubBookCode(),
							newAcSubBookCode, titaVo);
				}
			}
		}

		return null;
	}

	private void setingAcSubBookCode(Map<String, String> result, CdAcBook tCdAcBook, BigDecimal bal, TitaVo titaVo)
			throws LogicException {
		this.info("BS600 replaceAcBookCode custNo = " + result.get("F0"));
		this.info("BS600 replaceAcBookCode oldAcSubBookCode = " + result.get("F2"));
		this.info("BS600 replaceAcBookCode newAcSubBookCode = " + tCdAcBook.getAcSubBookCode());
		this.info("BS600 replaceAcBookCode bal = " + bal);
		this.info("BS600 replaceAcBookCode -----------------------------------------");
		this.info("BS600 replaceAcBookCode TargetAmt = " + tCdAcBook.getTargetAmt());
		this.info("BS600 replaceAcBookCode ActualAmt = " + tCdAcBook.getActualAmt());
		this.info("BS600 replaceAcSubBookCode add.");

		// 將戶號指派到此帳冊別
		result.put("newAcSubBookCode", tCdAcBook.getAcSubBookCode());

		// 累加此帳冊別的放款實際金額
		tCdAcBook.setActualAmt(tCdAcBook.getActualAmt().add(bal));
		this.info("BS600 replaceAcBookCode ActualAmt = " + tCdAcBook.getActualAmt());
	}

	/* 執行交易 */
	private void excuteTxCode(int custNo, String acBookCode, String oldAcSubBookCode, String newAcSubBookCode,
			TitaVo titaVo) throws LogicException {
		// 組入帳交易電文
		cnt++;
		TitaVo txTitaVo = new TitaVo();
		txTitaVo = (TitaVo) titaVo.clone();

		// 交易代號：L6801 放款戶帳冊別轉換
		txTitaVo.putParam("TXCD", "L6801");
		txTitaVo.putParam("TXCODE", "L6801");

		// 櫃台機種類、經辦
		txTitaVo.putParam("TRMTYP", "AU"); // 自動
		txTitaVo.putParam("TLRNO", "999999"); // 自動
		txTitaVo.putParam("EMPONT", "999999");
		TxTeller tTxTeller = txtellerService.findById("999999", titaVo);
		txTitaVo.putParam("TXTNO", parse.IntegerToString(tTxTeller.getTxtNo() + 1, 8));

		// 整批批號
		txTitaVo.putParam("BATCHNO", "BS600");
		txTitaVo.putParam("BATCHSEQ", cnt);

		// 交易戶號
		txTitaVo.putParam("MRKEY", parse.IntegerToString(custNo, 7));

		// 交易金額
		txTitaVo.putParam("TXAMT", BigDecimal.ZERO);

		//
		txTitaVo.putParam("HCODE", "0"); // 正常交易
		txTitaVo.putParam("RELCD", "1"); // 一段式
		txTitaVo.putParam("ACTFG", "0"); // 登錄
		txTitaVo.putParam("SECNO", "09"); // 業務別 09-放款

		//
		txTitaVo.putParam("TimCustNo", custNo);
		txTitaVo.putParam("AcBookCode", acBookCode);
		txTitaVo.putParam("OldAcSubBookCode", oldAcSubBookCode);
		txTitaVo.putParam("NewAcSubBookCode", newAcSubBookCode);

		// 執行入帳交易
		this.info("BS600 excuteTx " + txTitaVo);
		ApControl apControl = (ApControl) MySpring.getBean("apControl");
		apControl.callTrade(txTitaVo);
		apControl = null;
	}

}