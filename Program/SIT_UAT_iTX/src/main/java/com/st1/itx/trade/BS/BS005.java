package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.parse.Parse;

@Service("BS005")
@Scope("prototype")
/**
 * 新增應處理明細－預約撥款到期 <br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行<br>
 * 1.挑出預約撥款到期資料(撥款日期 <= 本日)，寫入應處理清單<br>
 * 2.若該筆預約撥款到期不執行，可執行L3120-預約撥款刪除，該筆應處理明細會同步刪除<br>
 * @author YuJiaXing
 * @version 1.0.0
 */
public class BS005 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS005.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS005 ");
		txToDoCom.setTxBuffer(this.getTxBuffer());

		// 預約撥款到期
		rvTxRoutine(titaVo);

		this.batchTransaction.commit();

		// 撥款收息作業
		rvLnRoutine(titaVo);

		this.batchTransaction.commit();
		return null;
	}

	// 預約撥款到期
	private void rvTxRoutine(TitaVo titaVo) throws LogicException {
		int TbsDyf = this.getTxBuffer().getMgBizDate().getTbsDyf();

		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

		List<Integer> lBormStatus = new ArrayList<Integer>();
		lBormStatus.add(99); // 99:預約撥款
        // 預約撥款日期 <= 本日
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findStatusRange(lBormStatus, 0, TbsDyf, this.index, Integer.MAX_VALUE); // Stauts=ArrayList,撥款日期DrawdownDate>=,<=今天
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		this.info("TbsDyf  = " + TbsDyf);
		this.info("lLoanBorMain  = " + lLoanBorMain);
		// data size > 0 -> 新增應處理明細
		if (lLoanBorMain != null) {
			TxToDoDetail tTxToDoDetail;
			for (LoanBorMain tLoanBorMain : lLoanBorMain) {
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("RVTX00"); // 預約撥款到期
				tTxToDoDetail.setCustNo(tLoanBorMain.getCustNo());
				tTxToDoDetail.setFacmNo(tLoanBorMain.getFacmNo());
				tTxToDoDetail.setBormNo(tLoanBorMain.getBormNo());
				txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
			}
		}
	}

	// 撥款收息作業
	private void rvLnRoutine(TitaVo titaVo) throws LogicException {
		int TbsDyf = this.getTxBuffer().getMgBizDate().getTbsDyf();
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

		// 攤還方式= 5.按月撥款收息(逆向貸款)，戶況：正常戶，0 <=下次應繳日<=本日，1 <=撥款序號 <= 1
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.AmortizedCodeEq("5", 0, 0, TbsDyf, 1, 1, this.index, Integer.MAX_VALUE); //
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		this.info("TbsDyf  = " + TbsDyf);
		this.info("lLoanBorMain  = " + lLoanBorMain);
		// data size > 0 -> 新增應處理明細
		if (lLoanBorMain != null) {
			TxToDoDetail tTxToDoDetail;
			for (LoanBorMain tLoanBorMain : lLoanBorMain) {
				// 下次繳息日 < 到期日
				if (tLoanBorMain.getNextPayIntDate() < tLoanBorMain.getMaturityDate()) {
					tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setItemCode("RVLN00"); // 撥款收息作業
					tTxToDoDetail.setCustNo(tLoanBorMain.getCustNo());
					tTxToDoDetail.setFacmNo(tLoanBorMain.getFacmNo());
					txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
	}

}