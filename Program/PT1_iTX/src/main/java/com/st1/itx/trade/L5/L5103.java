package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5103")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L5103 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnDocRecordService innDocRecordService;
	@Autowired
	public FacCloseService facCloseService;

	@Autowired
	public TxTellerService txTellerService;
	@Autowired
	public CdCodeService sCdCodeDefService;

	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	private MailService mailService;
	@Autowired
	public CustMainService custMainService;
	
	private InnDocRecord tInnDocRecord = new InnDocRecord();
	private InnDocRecordId tInnDocRecordId = new InnDocRecordId();
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5103 ");
		this.totaVo.init(titaVo);

		String cfBrNo = titaVo.getParam("L5103ConfirmBrNo");
		String cfGroupNo = titaVo.getParam("L5103ConfirmGroupNo");

		int rdate = parse.stringToInteger(titaVo.getParam("ReturnDate"));
		String rEmpNo = titaVo.getParam("ReturnEmpNo");
		String applCode = titaVo.getParam("ApplCode");
		String usageCode = titaVo.getParam("UsageCode");

		TxCom txCom = this.txBuffer.getTxCom();
		txCom.setConfirmBrNo(cfBrNo);
		this.info("cfGroupNo==" + cfGroupNo);
		txCom.setConfirmGroupNo(cfGroupNo);
		this.txBuffer.setTxCom(txCom);
		boolean emailfg = false;

//		1.登 2.放 3.審 4.審放
		if (titaVo.getActFgI() <= 1) {
			String sApplEmpNo = titaVo.getParam("ApplEmpNo");
			String sKeeperEmpNo = titaVo.getParam("KeeperEmpNo");

			TxTeller t1txTeller = txTellerService.findById(sApplEmpNo, titaVo);
			TxTeller t2txTeller = txTellerService.findById(sKeeperEmpNo, titaVo);
			// 若管理人員編非空白,則檢查管理人員編需存在且非停用CdCode.InnDocKeeper
			if (!"".equals(sKeeperEmpNo)) {
				CdCode tCdCode = sCdCodeDefService.getItemFirst(5, "InnDocKeeper", sKeeperEmpNo, titaVo);
				if (tCdCode == null) {
					throw new LogicException(titaVo, "E0015", "該檔案借閱管理人不存在，需請放款部服務課變更");
				}
				if ("N".equals(tCdCode.getEnable())) {
					throw new LogicException(titaVo, "E0015", "該檔案借閱管理人已停用，需請放款部服務課變更");
				}
			}

//			若保管與借閱同單位改為2段式交易
			if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
				if (t1txTeller != null && t2txTeller != null) {
					if (t1txTeller.getGroupNo().equals(t2txTeller.getGroupNo())) {
						titaVo.put("RELCD", "2");
					} else {
						titaVo.put("RELCD", "4");
					}
				}
			}
			// 登錄
			if (titaVo.isHcodeNormal()) {
				if ("1".equals(applCode)) {
					// 申請
					insertNormal(titaVo);
				} else {
					// 歸還
					returnNormal(titaVo);
					// 清償歸檔需更新清償作業檔的銷號欄
				}
			}
			// 修正
			if (titaVo.isHcodeModify()) {
				if ("1".equals(applCode)) {
					// 申請
					updateModify(titaVo);
				}
			}

			// 訂正
			if (titaVo.isHcodeErase()) {
				if ("1".equals(applCode)) {
					// 申請
					updateErase(titaVo);
				} else {
					// 歸還
					returnErase(titaVo);

				}
			}
		} else {
			// 2.放 3.審 4.審放
			tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
			tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
			tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));
			tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId, titaVo);
			if (tInnDocRecord == null) {
				throw new LogicException(titaVo, "E0003", "InnDocRecord not found ");
			}
			if (titaVo.isHcodeNormal()) {
				// 正常
				tInnDocRecord.setTitaActFg("" + titaVo.getActFgI());
				switch (titaVo.getActFgI()) {
				case 2: // 放行
					if (rdate > 0) {
						rdate = rdate + 19110000;
						tInnDocRecord.setApplCode("2");
						tInnDocRecord.setReturnDate(rdate);
						tInnDocRecord.setReturnEmpNo(rEmpNo);
					}
					emailfg = true;
					break;
				case 3: // 審閱
					if (rdate > 0) {
						rdate = rdate + 19110000;
						tInnDocRecord.setApplCode("2");
						tInnDocRecord.setReturnDate(rdate);
						tInnDocRecord.setReturnEmpNo(rEmpNo);
					}
					break;
				case 4: // 審閱放行
					tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
					emailfg = true;
					break;
				}
			} else {
				// 訂正
				switch (titaVo.getActFgI()) {
				case 3: // 審閱
					tInnDocRecord.setTitaActFg("2");
					if (tInnDocRecord.getReturnDate() > 0) {
						tInnDocRecord.setApplCode("1");
						tInnDocRecord.setReturnDate(0);
						tInnDocRecord.setReturnEmpNo("");
					}
					break;
				}
			}
			try {
				innDocRecordService.update(tInnDocRecord, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "InnDocRecord " + e.getErrorMsg());
			}
			// email通知
			if (emailfg) {
				processEmail(tInnDocRecord, titaVo); 
			}
		}
//		 歸還清償正本時最後步驟更新清償作業檔的銷號欄
		if ("2".equals(applCode) && "01".equals(usageCode)) {
			if ((titaVo.isHcodeNormal() && titaVo.getActFgI() == parse.stringToInteger(titaVo.getRelCode()))
					|| (titaVo.isHcodeErase() && titaVo.isActfgEntry())) {
				FacClose mFacClose = facCloseService.findFacmNoMaxCloseNoFirst(
						parse.stringToInteger(titaVo.getParam("CustNo")),
						parse.stringToInteger(titaVo.getParam("FacmNo")), titaVo);
				if (mFacClose != null) {
					if (titaVo.isHcodeNormal() && mFacClose.getClsNo().isEmpty()) {
						int wkYy = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(0, 3)); // 年
						int wkMm = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(3, 5)); // 月
						int wkDd = parse.stringToInteger(titaVo.getParam("ReturnDate").substring(5, 7)); // 日
						String clsDate = wkYy + "/" + wkMm + "/" + wkDd;
						titaVo.putParam("ClsNo", clsDate);
					}
					if (titaVo.get("ClsNo") != null) {
						FacClose tFacClose = facCloseService.holdById(mFacClose, titaVo);
						if (titaVo.isHcodeNormal()) {
							tFacClose.setClsNo(titaVo.get("ClsNo"));
						} else {
							tFacClose.setClsNo("");
						}
						try {
							facCloseService.update(tFacClose, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007",
									"L5103 isHcodeNormal 2 update " + e.getErrorMsg());
						}
					}
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();

	}

	// 申請登錄
	public void insertNormal(TitaVo titaVo) throws LogicException {

		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		InnDocRecord nInnDocRecord = innDocRecordService.findById(tInnDocRecordId, titaVo);
		if (nInnDocRecord != null) {
			throw new LogicException(titaVo, "E0002", "待放行"); // 新增資料已存在
		}
		tInnDocRecord.setInnDocRecordId(tInnDocRecordId);
		setTitaToDb(titaVo);
		tInnDocRecord.setJsonFields(tTempVo.getJsonString());

		try {
			innDocRecordService.insert(tInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "L5103 InnDocRecord insert " + e.getErrorMsg());
		}
	}

	// 歸還登錄
	public void returnNormal(TitaVo titaVo) throws LogicException {

		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		InnDocRecord nInnDocRecord = innDocRecordService.findById(tInnDocRecordId, titaVo);
		if (tInnDocRecord == null) {
			throw new LogicException(titaVo, "E0003", "");// 修改資料不存在
		}

		if (("1").equals(nInnDocRecord.getTitaActFg())) {
			throw new LogicException(titaVo, "E0015", "待放行"); // 檢查錯誤
		}

		nInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
		nInnDocRecord.setApplCode(titaVo.getParam("ApplCode"));
		nInnDocRecord.setApplEmpNo(titaVo.getParam("ApplEmpNo"));
		nInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
		nInnDocRecord.setUsageCode(titaVo.getParam("UsageCode"));
		nInnDocRecord.setCopyCode(titaVo.getParam("CopyCode"));
		nInnDocRecord.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
		nInnDocRecord.setReturnDate(parse.stringToInteger(titaVo.getParam("ReturnDate")));
		nInnDocRecord.setReturnEmpNo(titaVo.getParam("ReturnEmpNo"));
		nInnDocRecord.setRemark(titaVo.getParam("Remark"));
		nInnDocRecord.setApplObj(titaVo.getParam("ApplObj"));
		nInnDocRecord.setFacmNoMemo(titaVo.getParam("FacmNoMemo"));

		try {
			innDocRecordService.update(nInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "L5103 InnDocRecord insert " + e.getErrorMsg());
		}

	}

	// 申請修正
	public void updateModify(TitaVo titaVo) throws LogicException {
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId, titaVo);
		if (tInnDocRecord == null) {
			throw new LogicException(titaVo, "E0003", "");// 修改資料不存在
		}
		tInnDocRecord.setInnDocRecordId(tInnDocRecordId);
		setTitaToDb(titaVo);
		tInnDocRecord.setJsonFields(tTempVo.getJsonString());
		try {
			innDocRecordService.update(tInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "L5103 updateModify " + e.getErrorMsg());
		}
	}

	// Tita->DB
	public void setTitaToDb(TitaVo titaVo) throws LogicException {
		tInnDocRecord.setTitaActFg(titaVo.getActFgI() + "");
		tInnDocRecord.setApplCode(titaVo.getParam("ApplCode"));
		tInnDocRecord.setApplEmpNo(titaVo.getParam("ApplEmpNo"));
		tInnDocRecord.setKeeperEmpNo(titaVo.getParam("KeeperEmpNo"));
		tInnDocRecord.setUsageCode(titaVo.getParam("UsageCode"));
		tInnDocRecord.setCopyCode(titaVo.getParam("CopyCode"));
		tInnDocRecord.setApplDate(parse.stringToInteger(titaVo.getParam("ApplDate")));
		tInnDocRecord.setReturnDate(parse.stringToInteger(titaVo.getParam("ReturnDate")));
		tInnDocRecord.setReturnEmpNo(titaVo.getParam("ReturnEmpNo"));
		tInnDocRecord.setRemark(titaVo.getParam("Remark"));
		tInnDocRecord.setApplObj(titaVo.getParam("ApplObj"));
		tInnDocRecord.setFacmNoMemo(titaVo.getParam("FacmNoMemo"));
		tInnDocRecord.setTitaEntDy(titaVo.getEntDyI());
		tInnDocRecord.setTitaTlrNo(titaVo.getTlrNo());
		tInnDocRecord.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));
		tTempVo.putParam("RELCD", titaVo.get("RELCD"));

		for (int i = 1; i <= 25; i++) {
			if (Integer.parseInt(titaVo.getParam("OPTA" + i)) != 0) {
				tTempVo.putParam("OPTA" + i, titaVo.getParam("OPTA" + i));
				tTempVo.putParam("AMTA" + i, titaVo.getParam("AMTA" + i));

			}
		}

		for (int i = 1; i <= 25; i++) {
			if (Integer.parseInt(titaVo.getParam("OPTB" + i)) != 0) {
				tTempVo.putParam("OPTB" + i, titaVo.getParam("OPTB" + i));
				tTempVo.putParam("AMTB" + i, titaVo.getParam("AMTB" + i));
			}
		}

		for (int i = 1; i <= 25; i++) {
			if (Integer.parseInt(titaVo.getParam("OPTC" + i)) != 0) {
				tTempVo.putParam("OPTC" + i, titaVo.getParam("OPTC" + i));
				tTempVo.putParam("AMTC" + i, titaVo.getParam("AMTC" + i));
			}
		}
	}

	// 申請訂正
	public void updateErase(TitaVo titaVo) throws LogicException {
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId, titaVo);
		if (tInnDocRecord == null) {
			throw new LogicException(titaVo, "E0004", "");// 刪除資料不存在
		}

		if (("2").equals(tInnDocRecord.getApplCode())) {// 申請登錄時才可刪除
			throw new LogicException(titaVo, "E0015", "已歸還");// 檢查錯誤
		}
		try {
			innDocRecordService.delete(tInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0004", "L5103 deleteErase " + e.getErrorMsg());
		}

	}

	// 歸還訂正
	public void returnErase(TitaVo titaVo) throws LogicException {
		tInnDocRecordId.setCustNo(parse.stringToInteger(titaVo.getParam("CustNo")));
		tInnDocRecordId.setFacmNo(parse.stringToInteger(titaVo.getParam("FacmNo")));
		tInnDocRecordId.setApplSeq(titaVo.getParam("ApplSeq"));

		tInnDocRecord = innDocRecordService.holdById(tInnDocRecordId, titaVo);
		if (tInnDocRecord == null) {
			throw new LogicException(titaVo, "E0004", "");// 刪除資料不存在
		}
		tInnDocRecord.setInnDocRecordId(tInnDocRecordId);
		tInnDocRecord.setTitaActFg("4");
		tInnDocRecord.setApplCode("1");
		tInnDocRecord.setReturnDate(0);
		tInnDocRecord.setReturnEmpNo("");
		try {
			innDocRecordService.update(tInnDocRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "L5103 updateModify " + e.getErrorMsg());
		}
	}
	
	/*---------- 主管放行時email通知 管理人/借閱人 ----------*/
	public void processEmail(InnDocRecord tInnDocRecord, TitaVo titaVo) throws LogicException {
		this.info("processEmail");
		String subject1 = "新貸中系統-文件調閱通知 " ;
		String subject2 = "新貸中系統-文件調閱完成通知 " ;

		String bodyText = "";
		String applemail = "";
		String keeperemail = "";
		String applname = "";
		String custname = "";
		String copyCode = "";
		String applObj= "";

		if(!"".equals(tInnDocRecord.getApplEmpNo().trim()) && "1".equals(tInnDocRecord.getApplCode())) {//借閱人-借閱時
			CdEmp tCdEmp = cdEmpService.findById(tInnDocRecord.getApplEmpNo(), titaVo);
			if (tCdEmp != null ) {
				applname = tCdEmp.getFullname();
				if(!"".equals(tCdEmp.getEmail().trim())) {
					applemail = tCdEmp.getEmail();
				}
			}
		}
		if(!"".equals(tInnDocRecord.getKeeperEmpNo().trim()) && "1".equals(tInnDocRecord.getApplCode())) {//管理人-借閱時
			CdEmp tCdEmp = cdEmpService.findById(tInnDocRecord.getKeeperEmpNo(), titaVo);
			if (tCdEmp != null && !"".equals(tCdEmp.getEmail().trim())) {
				keeperemail = tCdEmp.getEmail();
			}
		}

		// 若為借閱人主管放行,則通知管理人
		if (titaVo.getActFgI() == 2 && !"".equals(keeperemail.trim())) {
			this.info("tCdEmp.getEmail()=" + keeperemail.trim());
			if (tInnDocRecord.getCustNo() > 0) {// 戶名
				CustMain tCustMain = custMainService.custNoFirst(tInnDocRecord.getCustNo(), tInnDocRecord.getCustNo(),
						titaVo);
				if (tCustMain != null) {
					custname = tCustMain.getCustName();
				}
			}
			if (!"".equals(tInnDocRecord.getCopyCode())) {// 正本/影本 項目中文
				CdCode tCdCode = sCdCodeDefService.getItemFirst(5, "CopyCode", tInnDocRecord.getCopyCode(), titaVo);
				if (tCdCode != null) {
					copyCode = tCdCode.getItem();
				}
			}
			if (!"".equals(tInnDocRecord.getApplObj())) {// 文件 項目中文
				CdCode tCdCode = sCdCodeDefService.getItemFirst(5, "ApplObj", tInnDocRecord.getApplObj(), titaVo);
				if (tCdCode != null) {
					applObj = tCdCode.getItem();
				}
			}
			
			String createDate = DbDateToRocDate(tInnDocRecord.getCreateDate().toString());
			String createTime = new SimpleDateFormat("HH:mm:ss").format(tInnDocRecord.getCreateDate());
			
			bodyText += "【新貸中系統】文件調閱，敬請協助後續處理。<br>";
			bodyText += "申請時間：" + createDate + " " + createTime.substring(0, 5) + "<br>";
			bodyText += "戶號：" + tInnDocRecord.getCustNo()+"<br>";
			bodyText += "借戶：" + custname+"<br>";
			bodyText += "正本/影本：" + copyCode+"<br>";
			bodyText += "文件：" + applObj+"<br>";
			bodyText += "申請人：" + applname+"<br>";
			this.info("subject1=" + subject1 + " ,bodyText=" + bodyText);
			mailService.setParams(keeperemail, subject1, bodyText);
			mailService.exec();
		}
		// 若為管理人主管放行,則通知借閱人
		if (titaVo.getActFgI() == 4 && !"".equals(applemail.trim())) {
			this.info("tCdEmp.getEmail()=" + applemail.trim());
			bodyText += "【新貸中系統】調閱文件已備妥，請盡速領取。";
			this.info("subject2=" + subject2 + " ,bodyText=" + bodyText);
			mailService.setParams(applemail, subject2, bodyText);
			mailService.exec();
		}
	}
	private String DbDateToRocDate(String DbDate) {
		this.info("LC010 DbDateToRocDate : " + DbDate);

		String SysDateY = DbDate.substring(0, 4);
		int RocDate9 = Integer.valueOf(SysDateY) - 1911;
		String RocDate = String.valueOf(RocDate9) + "/" + DbDate.substring(5, 7) + "/" + DbDate.substring(8, 10);

		return RocDate;
	}


}