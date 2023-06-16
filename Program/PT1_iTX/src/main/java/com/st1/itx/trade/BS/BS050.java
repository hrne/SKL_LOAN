package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

@Service("BS050")
@Scope("prototype")
/**
 * 未齊案件到期時,需依下列清單,寄發EMAIL通知 額度建檔所列[房貸專員/企金人員] 有「L9110首次撥款審核資料表」交易權限使用者
 * 「L3100撥款」登錄經辦
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class BS050 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	private TxTellerService txTellerService;
	@Autowired
	private MailService mailService;
	@Autowired
	private LoanNotYetService loanNotYetService;
	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;
	@Autowired
	private LoanBorMainService loanBorMainService;
	@Autowired
	private FacMainService facMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS050 ");

		List<LoanNotYet> lLoanNotYet = new ArrayList<LoanNotYet>();
		Slice<LoanNotYet> slLoanNotYet = loanNotYetService.findAll(0, Integer.MAX_VALUE, titaVo);

		lLoanNotYet = slLoanNotYet == null ? null : slLoanNotYet.getContent();
		if (lLoanNotYet != null) {
			for (LoanNotYet t : lLoanNotYet) {
				String tEmpNo = "";
				String wkNotYetItem = "";

				String bodyText = "";
				List<String> lEmpNo = new ArrayList<String>();
				FacMain tFacMain = new FacMain();
				Slice<LoanBorMain> slLoanBorMain = null;
				List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();

//				篩出未銷的未齊件資料
				if (t.getCloseDate() != 0 || t.getYetDate() == 0) {
					continue;
				}
//				篩出未齊件已到期資料 
				if (t.getYetDate() > this.txBuffer.getMgBizDate().getLbsDy()) {
					continue;
				}

				/*
				 * 未齊件代碼說明2022.2.9 by 昱衡
				 */
				CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.findById(t.getNotYetCode(), titaVo);
				if (cdLoanNotYet != null) {
					wkNotYetItem = cdLoanNotYet.getNotYetItem();
				} else {
					wkNotYetItem = t.getNotYetCode();
				}

				tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
//				取額度建檔所列[房貸專員/企金人員]
				if (tFacMain != null && !"".equals(tFacMain.getBusinessOfficer())) {

					bodyText += " 案件編號 " + tFacMain.getCreditSysNo() + " 核准號碼 " + tFacMain.getApplNo() + " 初貸日 "
							+ tFacMain.getFirstDrawdownDate() + " 房貸專員 " + tFacMain.getBusinessOfficer();
					if (!"".equals(tFacMain.getBusinessOfficer())) {

						tEmpNo = tFacMain.getBusinessOfficer();

						lEmpNo.add(tEmpNo);
					}

				}
				slLoanBorMain = loanBorMainService.bormCustNoEq(t.getCustNo(), t.getFacmNo(), t.getFacmNo(), 0, 900, 0,
						Integer.MAX_VALUE, titaVo);
				lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
				if (lLoanBorMain != null && lLoanBorMain.size() > 0) {
//				取「L3100撥款」登錄經辦
					for (LoanBorMain t2 : lLoanBorMain) {
						tEmpNo = "";
						if (t2 != null && !"".equals(t2.getCreateEmpNo())) {
							if (!"".equals(t2.getLastUpdateEmpNo())) {
								tEmpNo = t2.getLastUpdateEmpNo();
							} else {
								tEmpNo = t2.getCreateEmpNo();
							}
							lEmpNo.add(tEmpNo);
						}
					}
				}

				String subject = "未齊件到期通知 借戶 " + FormatUtil.pad9("" + t.getCustNo(), 7) + "-"
						+ FormatUtil.pad9("" + t.getFacmNo(), 3) + " 說明 :  " + wkNotYetItem;

				bodyText += " 齊件到期日 " + t.getYetDate() + " 備註 " + t.getReMark();

				// 經辦代號找員工姓名
				for (String t3 : lEmpNo) {

					TxTeller tTxTeller = txTellerService.findById(t3, titaVo);
					if (tTxTeller != null && !"".equals(tTxTeller.getEmail().trim())) {
						mailService.setParams(tTxTeller.getEmail(), subject, bodyText);
						mailService.exec();
					}
				}

			}
		}
		return null;
	}
}