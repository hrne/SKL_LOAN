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

	@Autowired
	LoanCom loanCom;

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
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode")); // 1.撥款 2.退款
		List<BankRemit> lBankRemit = new ArrayList<BankRemit>();
		List<BankRemit> unReleaselBankRemit = new ArrayList<BankRemit>();
		Slice<BankRemit> slBankRemit = bankRemitService.findL4901B(acDate, batchNo, 00, 99, 0, 0, 0, Integer.MAX_VALUE,
				titaVo);
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

		if (unReleaseCnt > 0) {
			this.info("unReleaselBankRemit =" + unReleaselBankRemit);
			totaB.putParam("MSGID", "L412B");

			// tota 未放行清單
			for (BankRemit t : unReleaselBankRemit) {
				setTota(t, titaVo);
			}

			this.addList(totaB);
		}

		if (lBankRemit.size() == 0) {
			throw new LogicException(titaVo, "E0001", "查無資料");
		}

		// 執行交易
		titaVo.setBatchNo(batchNo);
		this.info("batchNo = " + batchNo);
		this.info("titaVo = " + titaVo.toString());
		MySpring.newTask("L4102Batch", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");

		this.addList(this.totaVo);
		return this.sendList();
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