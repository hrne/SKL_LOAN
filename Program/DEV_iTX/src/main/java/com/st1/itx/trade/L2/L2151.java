package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2151 駁回額度登錄
 * a.若申請案件資料為駁時,只需輸入准駁日期
 */
/*
 * Tita
 * FuncCode=X,1
 * ApplNo=9,7
 * ApproveDate=9,7
 */
/**
 * L2151 駁回額度登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2151")
@Scope("prototype")
public class L2151 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2151.class);

	/* DB服務注入 */
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public TxTempService txTempService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DateUtil dDateUtil;

	// input work
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iApplNo;
	private int iApproveDate;

	// work area
	private int wkApplNo;
	private FacCaseAppl tFacCaseAppl;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private TempVo tTempVo = new TempVo();
	private List<TxTemp> lTxTemp;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2151 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
		this.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		this.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		this.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		this.info("   EntdyI         = " + titaVo.getEntDyI());
		this.info("   Kinbr          = " + titaVo.getKinbr());
		this.info("   TlrNo          = " + titaVo.getTlrNo());
		this.info("   Tno            = " + titaVo.getTxtNo());
		this.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin         = " + titaVo.getOrgKin());
		this.info("   OrgTlr         = " + titaVo.getOrgTlr());
		this.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		iApproveDate = this.parse.stringToInteger(titaVo.getParam("ApproveDate"));

		// 檢查輸入資料
		if (!(iFuncCode == 5 || iFuncCode == 7)) {
			throw new LogicException(titaVo, "E2004", "L2151"); // 功能選擇錯誤
		}

		// 更新案件申請檔 駁回
		if (iFuncCode == 7) {
			if (titaVo.isHcodeNormal()) {
				FacCaseApplNormalRoutine();
			} else {
				FacCaseApplEraseRoutine();
			}
		}

		this.totaVo.putParam("OProcessCode", 2); // 駁回

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void FacCaseApplNormalRoutine() throws LogicException {
		this.info("FacCaseApplNormalRoutine ...");

		// 駁回額度登錄需主管核可
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0011", "");
		}
		tFacCaseAppl = facCaseApplService.holdById(iApplNo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E2006", "案件申請檔"); // 修改資料不存在
		}
		if (tFacCaseAppl.getProcessCode().equals("1")) {
			throw new LogicException(titaVo, "E2064", ""); // 申請案件已核准
		}
		if (tFacCaseAppl.getProcessCode().equals("2")) {
			throw new LogicException(titaVo, "E2065", ""); // 申請案件已駁回
		}
		// 新增交易暫存檔(放款資料)
		AddTxTempApplRoutine();

		// 更新案件申請檔
		tFacCaseAppl.setProcessCode("2"); // 駁回
		tFacCaseAppl.setApproveDate(iApproveDate);
		try {
			facCaseApplService.update(tFacCaseAppl);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E2010", "案件申請檔"); // 更新資料時，發生錯誤
		}

		titaVo.putParam("ProcessCodeX", "駁");
	}

	private void FacCaseApplEraseRoutine() throws LogicException {
		this.info("FacCaseApplEraseRoutine ...");

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, Integer.MAX_VALUE);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();

		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = " + titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkApplNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			tTempVo = tTempVo.getVo(tx.getText());
			this.info("   wkApplNo = " + wkApplNo);
			// 還原案件申請檔
			RestoredFacCaseApplRoutine();
		}
	}

	// 新增交易暫存檔(放款資料)
	private void AddTxTempApplRoutine() throws LogicException {
		this.info("AddTxTempApplRoutine ... ");

		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, iApplNo, 0, 0, 0, titaVo);
		tTempVo.clear();
		tTempVo.putParam("ProcessCode", tFacCaseAppl.getProcessCode());
		tTempVo.putParam("ApproveDate", tFacCaseAppl.getApproveDate());
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}

	// 還原案件申請檔
	private void RestoredFacCaseApplRoutine() throws LogicException {
		this.info("RestoredFacCaseApplRoutine ...");

		tFacCaseAppl = facCaseApplService.holdById(wkApplNo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0006", "案件申請檔 申請號碼 = " + wkApplNo); // 鎖定資料時，發生錯誤
		}
		tFacCaseAppl.setProcessCode(tTempVo.getParam("ProcessCode")); // 核准
		tFacCaseAppl.setApproveDate(this.parse.stringToInteger(tTempVo.getParam("ApproveDate")));
		try {
			facCaseApplService.update(tFacCaseAppl);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "案件申請檔"); // 更新資料時，發生錯誤
		}
	}
}