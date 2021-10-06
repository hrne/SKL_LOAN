package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcDetailService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BankRemitFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.RemitForm;

/**
 * Tita<br>
 * ACCTDATE=9,7<br>
 * BATNO=X,6<br>
 * END=X,1<br>
 */

@Service("L4102")
@Scope("prototype")
/**
 * 撥款匯款作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4102 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public TotaVo totaA;

	@Autowired
	public TotaVo totaB;

	@Autowired
	public BankRemitService bankRemitService;

	@Autowired
	public BankRemitFileVo bankRemitFileVo;

	@Autowired
	public AcDetailService acDetailService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FacMainService facMainService;

	@Autowired
	public CdBcmService cdBcmService;

	@Autowired
	public AcCloseService acCloseService;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	RemitForm remitForm;

	@Autowired
	CdBankService cdBankService;

	@Autowired
	CdEmpService cdEmpService;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	int acDate = 0;
	String batchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4102 ");
		this.totaVo.init(titaVo);

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		batchNo = titaVo.getParam("BatchNo");
		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(acDate, batchNo, 00, 99, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
		if (slBankRemit == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		int unReleaseCnt = 0;
		for (BankRemit t : slBankRemit.getContent()) {
			if (t.getActFg() == 1) {
				unReleaseCnt++;
			} else {
				lBankRemit.add(t);
			}
		}
		if ("Y".equals(titaVo.get("ReleaseCheck"))) {
			if (unReleaseCnt > 0) {
				throw new LogicException(titaVo, "E0015", "未放行筆數：" + unReleaseCnt + " , 已放行筆數：" + lBankRemit.size()); // 檢查錯誤
			}

		}

		if (lBankRemit.size() == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		// 更新批號
//		batchNo = this.updBatchNo(batchNo, titaVo);
//		this.info("L4102 batchNo = " + batchNo);
//		this.info("L4102 lBankRemit = " + lBankRemit);
//		for (BankRemit tBankRemit : lBankRemit) {
//			this.info("L4102 tBankRemit = " + tBankRemit);
//			tBankRemit.setBatchNo(batchNo);
//			this.info("L4102 setBatchNo = " + tBankRemit.getBatchNo());
//		}

//		this.info("L4102 lBankRemit -2 = " + lBankRemit);
//		try {
//			bankRemitService.updateAll(lBankRemit, titaVo);
//		} catch (DBException e) {
//			throw new LogicException(titaVo, "E0007", "BankRemit " + e.getErrorMsg()); // 更新資料時，發生錯誤
//		}

//		// 更新批號
//		List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
//		for (BankRemit tBankRemit : lBankRemit) {
//			Slice<AcDetail> slAcDetail = acDetailService.acdtlRelTxseqEq(acDate,
//					titaVo.getKinbr() + tBankRemit.getTitaTlrNo() + tBankRemit.getTitaTxtNo(), acDate, 0,
//					Integer.MAX_VALUE, titaVo);
//			if (slAcDetail != null) {
//				for (AcDetail tAcDetail : slAcDetail.getContent()) {
//					tAcDetail.setTitaBatchNo(batchNo);
//					lAcDetail.add(tAcDetail);
//				}
//			}
//		}
//		if (lAcDetail.size() > 0) {
//			try {
//				acDetailService.updateAll(lAcDetail, titaVo);
//			} catch (DBException e) {
//				throw new LogicException(titaVo, "E0007", "AcDetail " + e.getErrorMsg()); // 更新資料時，發生錯誤
//			}
//		}

		// 執行交易
		titaVo.setBatchNo(batchNo);
		this.info("batchNo = " + batchNo);
		this.info("titaVo = " + titaVo.toString());
		MySpring.newTask("L4102Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getBatchNo(TitaVo titaVo) throws LogicException {
		String batchNo = "";
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無帳務資料"); // 查詢資料不存在
		}
		batchNo = "LN" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";
		return batchNo;
	}

	// 更新批號
	private String updBatchNo(String batchNo, TitaVo titaVo) throws LogicException {
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("01"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.holdById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無撥款帳務資料"); // 查詢資料不存在
		}
		batchNo = batchNo.trim() + parse.IntegerToString(tAcClose.getBatNo(), 2);
		tAcClose.setBatNo(tAcClose.getBatNo() + 1);
		try {
			acCloseService.update(tAcClose, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		return batchNo;
	}

}