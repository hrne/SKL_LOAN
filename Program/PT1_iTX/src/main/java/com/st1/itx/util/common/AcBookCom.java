package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.CdAcBookId;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.util.parse.Parse;

/**
 * 帳冊別處理<BR>
 * 1.run ：更新會計分錄的區隔帳冊 call by AcEntetCom<BR>
 * 1.1 新戶號則按分配順序(目標金額、實際金額)抓取可用之區隔帳冊<BR>
 * 1.2 累計實際金額(含L6801放款戶帳冊別轉換)<BR>
 * 2.getAcSubBookCode ： 抓取某戶號的帳冊別、區隔帳冊 call by LXXXX<BR>
 * 
 * @author st1
 *
 */
@Component("AcBookCom")
@Scope("prototype")
public class AcBookCom extends TradeBuffer {
	@Autowired
	public CdAcBookService cdAcBookService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	Parse parse;

	List<CdAcBook> lCdAcBook;

	/*-----------  更新會計分錄的區隔帳冊  -------------- */
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcBookCom ... ");
		String acSubBookCode = null;
		int acBookFlag = 0;
		int CustNo = 0;
		int acHCode = this.txBuffer.getTxCom().getBookAcHcode(); // 帳務訂正記號
//		帳務訂正記號  AcHCode   0.正常     1.訂正     2.3.沖正              
		BigDecimal LoanAmt = BigDecimal.ZERO;

		// 帳冊別轉換使用會計分錄上已設定好的區隔帳冊
		// L6801放款戶帳冊別轉換(啟動 by BS600放款戶帳冊別轉換(run by BS001日始作業))
		if ("L6801".equals(titaVo.getTxcd())) {
			for (AcDetail ac : this.txBuffer.getAcDetailList()) {
				// 抓非00A:傳統帳冊的那筆
				if (!ac.getAcSubBookCode().equals(this.txBuffer.getSystemParas().getAcSubBookCode())) {
					acBookFlag = 1;
					acSubBookCode = ac.getAcSubBookCode();
					if (ac.getAcctFlag() == 1) {
						if (ac.getDbCr().equals("D"))
							LoanAmt = LoanAmt.add(ac.getTxAmt());
						else
							LoanAmt = LoanAmt.subtract(ac.getTxAmt());
					}
				}
			}

		}
		// 一般交易須取得區隔帳冊
		else {
			for (AcDetail ac : this.txBuffer.getAcDetailList()) {
				// 帳冊別記號 AcBookFlag = 1-細分，區隔帳冊By戶號設定(AcBookCom)
				if (ac.getAcBookFlag() == 1) {
					acBookFlag = 1;
					acSubBookCode = ac.getAcSubBookCode();
					CustNo = ac.getCustNo();
				}
				// 業務科目記號 AcctFlag = 1- 資負明細科目(放款、催收)，累計撥還款金額
				if (ac.getAcctFlag() == 1) {
					if (ac.getDbCr().equals("D"))
						LoanAmt = LoanAmt.add(ac.getTxAmt());
					else
						LoanAmt = LoanAmt.subtract(ac.getTxAmt());
				}
			}

			// 正常交易且帳冊別記號=1，需取得區隔帳冊
			// 1).找該戶第一筆放款的區隔帳冊
			// 2).該戶為初貸則依帳冊別設定金額，按分配順序抓取可用之區隔帳冊
			// 3).超過分配金額，設定為00A:傳統帳冊(參數檔)
			if (acHCode == 0 && acBookFlag == 1) {
				AcReceivable tAcReceivable = new AcReceivable();
				// find first AcReceivable(custNo, acctFlag, facmNo)
				tAcReceivable = acReceivableService.acrvFacmNoFirst(CustNo, 1, 0, titaVo);
				if (tAcReceivable != null)
					acSubBookCode = tAcReceivable.getAcSubBookCode();
				else {
					if (LoanAmt.compareTo(BigDecimal.ZERO) > 0) {
						// find CdAcBook order by AssignSeq(分配順序)
						Slice<CdAcBook> slCdAcBook = cdAcBookService.acBookAssignSeqGeq(this.txBuffer.getAcDetailList().get(0).getAcBookCode(), 1, this.index, this.limit, titaVo);
						lCdAcBook = slCdAcBook == null ? null : slCdAcBook.getContent();

						/* TargetAmt 放款目標金額 >= ActualAmt放款實際金額 + LoanAmt 撥還款累計 */
						if (lCdAcBook != null) {
							for (CdAcBook cd : lCdAcBook) {
								if (cd.getTargetAmt().compareTo(LoanAmt.add(cd.getActualAmt())) >= 0) {
									acSubBookCode = cd.getAcSubBookCode();
									break;
								}
							}
						}
					}
				}
				// 區隔帳冊預設00A:傳統帳冊
				if (acSubBookCode == null || acSubBookCode.length() == 0) {
					acSubBookCode = this.txBuffer.getSystemParas().getAcSubBookCode();
				}

			}

			// 更新需細分帳冊別分錄的區隔帳冊欄
			for (AcDetail ac : this.txBuffer.getAcDetailList()) {
				if (ac.getAcBookFlag() == 1) { // 1-細分，區隔帳冊By戶號設定(AcBookCom)
					ac.setAcSubBookCode(acSubBookCode);
				}
			}
		}

		// 訂正為負
		if (acHCode == 1) {
			LoanAmt = BigDecimal.ZERO.subtract(LoanAmt);
		}
		// 區隔帳冊非預設之00A:傳統帳冊，則以累計撥還款金額更新該帳冊別設定檔之放款實際金額欄
		if (LoanAmt.compareTo(BigDecimal.ZERO) != 0 && !acSubBookCode.equals(this.txBuffer.getSystemParas().getAcSubBookCode())) {
			CdAcBook tCdAcBook = new CdAcBook();
			tCdAcBook = cdAcBookService.holdById(new CdAcBookId(this.txBuffer.getAcDetailList().get(0).getAcBookCode(), acSubBookCode), titaVo);
			if (tCdAcBook == null)
				throw new LogicException(titaVo, "E6003", "帳冊別金額設定檔Notfound" + acSubBookCode); // 會計入帳檢核錯誤
			else {
				tCdAcBook.setActualAmt(LoanAmt.add(tCdAcBook.getActualAmt()));
				try {
					cdAcBookService.update(tCdAcBook, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E6003", "帳冊別金額設定檔更新時，發生錯誤"); // 會計入帳檢核錯誤
				}
			}
			this.info("AcBookCom ... AcHCode=" + acHCode + ",AcSubBookCode=" + acSubBookCode + "LoanAmt=" + LoanAmt + ", " + tCdAcBook.getActualAmt());
		}
		return null;
	}

	/**
	 * 抓取某戶號的帳冊別、區隔帳冊
	 * 
	 * @param custNo 戶號
	 * @param titaVo TitaVo
	 * @return String array（帳冊別, 區隔帳冊）
	 */
	public String[] getAcBookCode(int custNo, TitaVo titaVo) {
		String[] acBook = new String[] { null, null };
		AcReceivable tAcReceivable = new AcReceivable();
		tAcReceivable = acReceivableService.acrvFacmNoFirst(custNo, 1, 0, titaVo);
		if (tAcReceivable != null) {
			acBook[0] = tAcReceivable.getAcBookCode();
			acBook[1] = tAcReceivable.getAcSubBookCode();
		}
		return acBook;
	}
}