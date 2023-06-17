package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.db.service.springjpa.cm.L6907ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6907")
@Scope("prototype")
/**
 *
 * @author YuJiaXing
 * @author Mata 2022/5/13
 * @version 1.0.0
 */
public class L6907 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public LoanFacTmpService loanFacTmpService;

	@Autowired
	public L6907ServiceImpl l6907ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String acctCode;
	CdAcCode tCdAcCode = new CdAcCode();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6907 ");
		this.totaVo.init(titaVo);

		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		String iTmpTxCode = titaVo.getParam("TmpTxCode");
		int iTmpFacmNo = parse.stringToInteger(titaVo.getParam("TmpFacmNo"));
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 45 * 200 = 9000

		List<Map<String, String>> L6907List = null;

		try {
			L6907List = l6907ServiceImpl.FindAll(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E0001", "SQL ERROR");
		}
		if (L6907List == null || L6907List.size() == 0) {
			// 查無資料 拋錯
			throw new LogicException(titaVo, "E0001", "會計銷帳檔");
		}

		boolean istmpfacmno = false;
		Slice<LoanFacTmp> slLoanFacTmp = null;
		// 檢查由L3200連動時的額度是否為指定額度
		if ("L3200".equals(iTmpTxCode)) {
			slLoanFacTmp = loanFacTmpService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
			if (slLoanFacTmp != null) {
				if (iTmpFacmNo != 0) {
					for (LoanFacTmp t : slLoanFacTmp.getContent()) {
						if (iTmpFacmNo == t.getFacmNo()) {
							istmpfacmno = true;
						}
					}
				}
			}
		}
		for (Map<String, String> tAcReceivable : L6907List) {
			OccursList occursList = new OccursList();

			// 由L3200連動時處理
			if ("L3200".equals(iTmpTxCode)) {
				// 無指定額度全部為累溢收，全部不顯示 ==> 20221215 無設定指定額度則全部都可用
				int checkSkip = 0;
				if (slLoanFacTmp == null) {
					checkSkip = 1;
				} else if (istmpfacmno) {
					// 指定額度只有該額度為溢收款，其他皆顯示 ==> 20221215 指定額度可使用自已及非指定額度
					if (iTmpFacmNo == parse.stringToInteger(tAcReceivable.get("FacmNo"))) {
						checkSkip = 1;
					} else {
						boolean isDataFacmNoTmp = false;
						for (LoanFacTmp t : slLoanFacTmp.getContent()) {
							if (parse.stringToInteger(tAcReceivable.get("FacmNo")) == t.getFacmNo()) {
								isDataFacmNoTmp = true;
								break;
							}
						}
						if (!isDataFacmNoTmp) {
							checkSkip = 1;
						}
					}
				} else {
					// 非指定額度該全部非指定額度為溢收款，指定額度顯示 ==> 20221215 非指定額度可使用非指定額度
					boolean isDataFacmNoTmp = false;
					for (LoanFacTmp t : slLoanFacTmp.getContent()) {
						if (parse.stringToInteger(tAcReceivable.get("FacmNo")) == t.getFacmNo()) {
							isDataFacmNoTmp = true;
							break;
						}
					}
					if (!isDataFacmNoTmp) {
						checkSkip = 1;
					}
				}
				if (checkSkip == 0) {
					continue;
				}

			}
			// new occurs
			// 科子項目
			occursList.putParam("OOAcNoCode", tAcReceivable.get("AcNoCode"));
			occursList.putParam("OOAcSubCode", tAcReceivable.get("AcSubCode"));
			occursList.putParam("OOAcDtlCode", tAcReceivable.get("AcDtlCode"));
			occursList.putParam("OOAcBookCode", tAcReceivable.get("AcBookCode"));

			// Y-顯示[明細]按鈕
			String l6908Flag = "Y";
			// 未收費用未變動不顯示按鈕
			if (parse.stringToInteger(tAcReceivable.get("ReceivableFlag")) >= 3
					&& tAcReceivable.get("RvAmt").compareTo(tAcReceivable.get("RvBal")) == 0) {
				l6908Flag = "";
			}
			if ("0".equals(tAcReceivable.get("TitaTxtNo"))) {
				l6908Flag = "";
			}

			// 交易序號 = 0不顯示按鈕
			occursList.putParam("L6908Flag", l6908Flag);
			// 戶號 OOCustNoX
			occursList.putParam("OOCustNoX", tAcReceivable.get("CustNo") + '-' + tAcReceivable.get("FacmNo"));
			occursList.putParam("OOCustNo", tAcReceivable.get("CustNo"));
			occursList.putParam("OOFacmNo", tAcReceivable.get("FacmNo"));

			if (parse.stringToInteger(tAcReceivable.get("ReceivableFlag")) >= 3
					&& tAcReceivable.get("RvAmt").compareTo(tAcReceivable.get("RvBal")) == 0) {
				occursList.putParam("OOFacmNo", tAcReceivable.get("FacmNo"));
			}
			// 業務科目 OOAcctCodeX
			occursList.putParam("OOAcctCode", tAcReceivable.get("AcctCode"));
			occursList.putParam("OOAcctItem", tAcReceivable.get("AcctItem"));
			// 銷帳編號 OORvNo
			occursList.putParam("OORvNo", tAcReceivable.get("RvNo"));
			// 起帳日期 OOOpenAcDate
			occursList.putParam("OOOpenAcDate", tAcReceivable.get("OpenAcDate"));
			// 起帳金額 #OORvAmt
			occursList.putParam("OORvAmt", tAcReceivable.get("RvAmt"));
			// 最後交易日 #OOLastTxDate
			occursList.putParam("OOLastTxDate", tAcReceivable.get("LastTxDate"));
			// 未銷餘額 OORvBal
			occursList.putParam("OORvBal", tAcReceivable.get("RvBal"));
			// 業務科目合計 OO_SUM1
			occursList.putParam("OO_SUM", tAcReceivable.get("SumRvBal"));
			// 區隔帳冊 OOAcSubBookCode
			occursList.putParam("OOAcSubBookCode", tAcReceivable.get("AcSubBookCode"));
			// 最後修改日期 OOLastUpdate
			occursList.putParam("OOLastUpdate", parse.stringToStringDateTime(tAcReceivable.get("LastUpdate")));
			// 最後修改人員 OOLastEmp
			occursList.putParam("OOLastEmp", tAcReceivable.get("LastUpdateEmpNo"));

			occursList.putParam("OOGetFeeTxt", "");
			if (parse.stringToInteger(tAcReceivable.get("ReceivableFlag")) >= 3
					&& parse.stringToBigDecimal(tAcReceivable.get("RvBal")).compareTo(BigDecimal.ZERO) > 0) {
				occursList.putParam("OOGetFeeTxt", "未收");
			}

			this.totaVo.addOccursList(occursList);

		}
		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (L6907List != null && L6907List.size() >= this.limit) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}