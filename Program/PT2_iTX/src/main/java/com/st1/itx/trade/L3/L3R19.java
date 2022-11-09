package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R19")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L3R19 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public AcDetailService acDetailService;
	@Autowired
	public BaTxCom baTxCom;
	@Autowired
	public LoanCom loanCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R19");
		this.totaVo.init(titaVo);

		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 會計日期
		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate"));
		// 經辦
		String iTellerNo = titaVo.getParam("RimTellerNo");
		// 交易序號
		String iTxtNo = titaVo.getParam("RimTxtNo");

		OccursList occursList = new OccursList();
		Slice<LoanBorTx> sloanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, iAcDate + 19110000, "0000", iTellerNo,
				iTxtNo, 0, Integer.MAX_VALUE, titaVo);

		// 查無資料error
		if (sloanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "撥款內容檔"); // 查詢資料不存在
		}
		for (LoanBorTx ln : sloanBorTx.getContent()) {
			if (!ln.getTitaHCode().equals("0")) {
				throw new LogicException(titaVo, "E0015", "訂正別非正常狀態"); // 檢查錯誤
			}
			if (!("L3210".equals(ln.getTitaTxCd()) || "L3220".equals(ln.getTitaTxCd())
					|| "L3230".equals(ln.getTitaTxCd()))) {
				continue;
			}
			Slice<AcDetail> slAcDetail = acDetailService.findTxtNoEq(ln.getAcDate() + 19110000, ln.getTitaKinBr(),
					ln.getTitaTlrNo(), parse.stringToInteger(ln.getTitaTxtNo()), 0, Integer.MAX_VALUE, titaVo);
			if (slAcDetail == null) {
				this.info("slAcDetail = " + slAcDetail);
				continue;
			}
			BigDecimal txAmt = BigDecimal.ZERO;
			if (ln.getTxAmt().compareTo(BigDecimal.ZERO) > 0) {
				txAmt = ln.getTxAmt();
			} else if (ln.getTxAmt().compareTo(BigDecimal.ZERO) < 0) {
				txAmt = BigDecimal.ZERO.subtract(ln.getTxAmt());
			} else {
				txAmt = ln.getTempAmt().subtract(ln.getOverflow());
				if (txAmt.compareTo(BigDecimal.ZERO) < 0) {
					txAmt = BigDecimal.ZERO.subtract(txAmt);
				}
			}
			this.totaVo.putParam("L3r19EntryDate", ln.getEntryDate());
			this.totaVo.putParam("L3r19AcDate", ln.getAcDate());
			this.totaVo.putParam("L3r19Desc", loanCom.getTxDescCodeX(ln, titaVo));
			this.totaVo.putParam("L3r19FacmNo", ln.getFacmNo());
			this.totaVo.putParam("L3r19AcctCode", "");
			this.totaVo.putParam("L3r19RvNo", "");
			this.totaVo.putParam("L3r19TxAmt", txAmt);
			this.totaVo.putParam("L3r19TellerNo", ln.getTitaTlrNo());
			this.totaVo.putParam("L3r19TxtNo", ln.getTitaTxtNo());
			for (AcDetail ac : slAcDetail.getContent()) {
				// F10 帳管費
				// F29 契變手續費

				this.info("ac.getDbCr() = " + ac.getDbCr());
				this.info("ac.getAcctCode() = " + ac.getAcctCode());
				if ("C".equals(ac.getDbCr())) {
					if ("F10".equals(ac.getAcctCode()) || "F27".equals(ac.getAcctCode())
							|| "TMI".equals(ac.getAcctCode()) || "F07".equals(ac.getAcctCode())
							|| "F08".equals(ac.getAcctCode()) || "F09".equals(ac.getAcctCode())
							|| "F10".equals(ac.getAcctCode()) || "F12".equals(ac.getAcctCode())
							|| "F13".equals(ac.getAcctCode()) || "F14".equals(ac.getAcctCode())
							|| "F15".equals(ac.getAcctCode()) || "F16".equals(ac.getAcctCode())
							|| "F18".equals(ac.getAcctCode()) || "F21".equals(ac.getAcctCode())
							|| "F24".equals(ac.getAcctCode()) || "F25".equals(ac.getAcctCode())
							|| "F27".equals(ac.getAcctCode()) || "F29".equals(ac.getAcctCode())) {
						this.totaVo.putParam("L3r19AcctCode", ac.getAcctCode());
						this.totaVo.putParam("L3r19RvNo", ac.getRvNo());
						// 將每筆資料放入Tota的OcList
					}
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}