package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
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
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.data.BankRemitFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.report.RemitForm;

/**
 * Tita<br>
 * ACCTDATE=9,7<br>
 * BATNO=X,6<br>
 * END=X,1<br>
 */

@Service("L4101")
@Scope("prototype")
/**
 * 撥款匯款作業
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4101 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FileCom fileCom;

	@Autowired
	public TotaVo totaA; // wang

	@Autowired
	public TotaVo totaB; // 未放行清單
	@Autowired
	public TotaVo totaWarnMsg; // 未放行清單

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

	@Autowired
	LoanCom loanCom;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	int acDate = 0;
	String batchNo = "";
	String newBatchNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4101 ");
		this.totaVo.init(titaVo);

		totaWarnMsg.putParam("MSGID", "L410W");
		totaB.putParam("MSGID", "L410B");

		acDate = parse.stringToInteger(titaVo.getParam("AcDate")) + 19110000;
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode")); // 1.撥款 2.退款
		batchNo = FormatUtil.padX(this.getBatchNo(iItemCode, titaVo), 6);

		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		List<BankRemit> unReleaselBankRemit = new ArrayList<BankRemit>();
		Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(acDate, batchNo, 00, 99, 0, 0, 0, Integer.MAX_VALUE, titaVo);
		if (slBankRemit == null) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		int unReleaseCnt = 0;
		for (BankRemit t : slBankRemit.getContent()) {
			// 作業項目為1.撥款時把退款篩選掉
			if (iItemCode == 1) {
				if (t.getDrawdownCode() == 4 || t.getDrawdownCode() == 5 || t.getDrawdownCode() == 11) {
					continue;
				}
			}

			// 作業項目為2.退款時把撥款篩選掉
			if (iItemCode == 2) {
				if (t.getDrawdownCode() == 1 || t.getDrawdownCode() == 2) {
					continue;
				}
			}

			if (t.getActFg() == 1) {
				unReleaseCnt++;
				unReleaselBankRemit.add(t);
			} else {
				lBankRemit.add(t);
			}
		}
		if (lBankRemit.size() == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		if (unReleaseCnt > 0) {

			// tota 未放行清單
			for (BankRemit t : unReleaselBankRemit) {
				setTota(t, titaVo);
			}

			this.addList(totaB);
		}

//		[是否檢核未放行]為Y時 , 若有未放行資料，則提示訊息 不出媒體檔 顯示未放行清單
		if ("Y".equals(titaVo.get("ReleaseCheck"))) {
			if (unReleaseCnt > 0) {
				this.totaA.setWarnMsg("有未放行資料不產生媒體檔");
				this.addList(totaA);
			} else {

				// 執行交易
				// 更新批號
				newBatchNo = this.updBatchNo(batchNo, titaVo);
				this.info("batchNo = " + batchNo);
				this.info("titaVo = " + titaVo.toString());
				MySpring.newTask("L4101Batch", this.txBuffer, titaVo);

				this.totaWarnMsg.setWarnMsg("背景作業中,待處理完畢訊息通知");
			}
		} else {

			// 執行交易
			// 更新批號
			newBatchNo = this.updBatchNo(batchNo, titaVo);
			this.info("batchNo = " + batchNo);
			this.info("titaVo = " + titaVo.toString());
			MySpring.newTask("L4101Batch", this.txBuffer, titaVo);

			this.totaWarnMsg.setWarnMsg("背景作業中,待處理完畢訊息通知");
		}
		this.addList(this.totaWarnMsg);
		totaVo.put("OBatchNo", newBatchNo);
		this.info("totaB = " + totaB.toString());
		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getBatchNo(int iItemCode, TitaVo titaVo) throws LogicException {
		String batchNo = "";
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("09"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無帳務資料"); // 查詢資料不存在
		}
		if (iItemCode == 1) {

			batchNo = "LN" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";
		} else {

			batchNo = "RT" + parse.IntegerToString(tAcClose.getClsNo() + 1, 2) + "  ";
		}
		return batchNo;
	}

	// 更新批號
	private String updBatchNo(String batchNo, TitaVo titaVo) throws LogicException {
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo("01"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款
		AcClose tAcClose = acCloseService.findById(tAcCloseId, titaVo);
		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "無撥款帳務資料"); // 查詢資料不存在
		}
		batchNo = batchNo.trim() + parse.IntegerToString(tAcClose.getBatNo(), 2);

		return batchNo;
	}

	// tota 未放行清單
	private void setTota(BankRemit t, TitaVo titaVo) throws LogicException {

		OccursList occursList = new OccursList();

//		戶號 戶名 經辦 交易時間

		occursList.putParam("OOCustNo", t.getCustNo()); // 戶號
		occursList.putParam("OOFacmNo", t.getFacmNo()); // 額度
		occursList.putParam("OOBormNo", t.getBormNo()); // 撥款

		occursList.putParam("OOCustName", loanCom.getCustNameByNo(t.getCustNo())); // 戶名

		// 查詢員工資料檔
		if (!"".equals(t.getTitaTlrNo())) {
			occursList.putParam("OOTlrNo", t.getTitaTlrNo());// 經辦
			occursList.putParam("OOTlrNoX", loanCom.getEmpFullnameByEmpNo(t.getTitaTlrNo()));
		} else {
			occursList.putParam("OOTlrNo", "");
			occursList.putParam("OOTlrNoX", "");
		}
		if (t.getLastUpdate() != null && t.getLastUpdate().toString().length() >= 19) {
			occursList.putParam("OOLastUpdateTime", dbDateToRocTime(t.getLastUpdate().toString()));// 交易時間
		} else {
			occursList.putParam("OOLastUpdateTime", 0);// 交易時間
		}

		totaB.addOccursList(occursList);
	}

	private String dbDateToRocTime(String dbDate) {

		return dbDate.substring(11, 19);
	}

}