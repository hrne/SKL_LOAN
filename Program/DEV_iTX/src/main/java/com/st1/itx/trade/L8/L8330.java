package com.st1.itx.trade.L8;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.domain.JcicZ443Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ443Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8330")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8330 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8330.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ443Service sJcicZ443Service;
	@Autowired
	public JcicCom jcicCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8330 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String Account=titaVo.getParam("Account").trim();//帳號
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期

		/* DB資料容器WD */
		//JcicMAaster
		JcicZ443 tJcicZ443 = new JcicZ443();
		JcicZ443Id tJcicZ443Id = new JcicZ443Id();
		tJcicZ443Id.setCustId(CustId);//債務人IDN
		tJcicZ443Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ443Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
//		tJcicZ443Id.setBankId(titaVo.getParam("BankId").trim());//異動債權金機構代號
		tJcicZ443Id.setMaxMainCode(titaVo.getParam("MaxMainCode").trim());//最大債權金融機構代號
		tJcicZ443Id.setAccount(Account);//帳號
		tJcicZ443.setJcicZ443Id(tJcicZ443Id);

		tJcicZ443.setTranKey(titaVo.getParam("TranKey").trim());//交易代碼
		tJcicZ443.setIsMaxMain(titaVo.getParam("IsMaxMain").trim());//是否為最大債權
		tJcicZ443.setGuarantyType(titaVo.getParam("GuarantyType").trim());//擔保品類別
		tJcicZ443.setLoanAmt(parse.stringToBigDecimal(titaVo.getParam("LoanAmt").trim()));//原借款金額
		tJcicZ443.setCreditAmt(parse.stringToBigDecimal(titaVo.getParam("CreditAmt").trim()));//授信餘額
		tJcicZ443.setPrincipal(parse.stringToBigDecimal(titaVo.getParam("Principal").trim()));//本金
		tJcicZ443.setInterest(parse.stringToBigDecimal(titaVo.getParam("Interest").trim()));//利息
		tJcicZ443.setPenalty(parse.stringToBigDecimal(titaVo.getParam("Penalty").trim()));//違約金
		tJcicZ443.setOther(parse.stringToBigDecimal(titaVo.getParam("Other").trim()));//其他費用
		tJcicZ443.setTerminalPayAmt(parse.stringToBigDecimal(titaVo.getParam("TerminalPayAmt").trim()));//每期應付金額
		tJcicZ443.setLatestPayAmt(parse.stringToBigDecimal(titaVo.getParam("LatestPayAmt").trim()));//最近一期繳款金額
		tJcicZ443.setFinalPayDay(parse.stringToInteger(titaVo.getParam("FinalPayDay").trim()));//最後繳息日
		tJcicZ443.setNotyetacQuit(parse.stringToBigDecimal(titaVo.getParam("NotyetacQuit").trim()));//已到期尚未清償金額
		tJcicZ443.setMothPayDay(parse.stringToInteger(titaVo.getParam("MothPayDay").trim()));//每月應還款日
		
		tJcicZ443.setBeginDate(parse.stringToInteger(jcicCom.RocTurnDc(titaVo.getParam("BeginDate").trim(),1)));//契約起始年月
		tJcicZ443.setEndDate(parse.stringToInteger(jcicCom.RocTurnDc(titaVo.getParam("EndDate").trim(),1)));//契約截止年月
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ443.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ443.setOutJcicTxtDate(0);
		}
		JcicZ443 tJcicZ443VO=sJcicZ443Service.holdById(tJcicZ443Id, titaVo);
		JcicZ443 OrgJcicZ443 = null;
		if(tJcicZ443VO!=null) {
			OrgJcicZ443 = (JcicZ443) dataLog.clone(tJcicZ443VO);//資料異動前
		}

		this.info("tJcicZ443VO=["+tJcicZ443.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ443VO,tJcicZ443VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ443.getTranKey())) {
					
				}else {
					//刷主管卡後始可刪除
					// 交易需主管核可
					if (!titaVo.getHsupCode().equals("1")) {
						//titaVo.getSupCode();
						sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
					}
				}
				//刪除
				try {
					sJcicZ443Service.delete(tJcicZ443VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
//			jcicCom.checkJcicZ440(tJcicZ443Id.getCustId(), tJcicZ443Id.getApplyDate(), tJcicZ443Id.getSubmitKey(), tJcicZ443Id.getBankId(), titaVo);
			if(tJcicZ443VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ443.setCreateDate(tJcicZ443VO.getCreateDate());
				tJcicZ443.setCreateEmpNo(tJcicZ443VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ443.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ443.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ443.setCreateDate(OrgJcicZ443.getCreateDate());
				tJcicZ443.setCreateEmpNo(OrgJcicZ443.getCreateEmpNo());
				try {
					tJcicZ443 = sJcicZ443Service.update2(tJcicZ443, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ443, tJcicZ443);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ443.setTranKey(TranKey);
				try {
					sJcicZ443Service.insert(tJcicZ443, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		
//		int Today=dateUtil.getNowIntegerForBC();
//		JcicZ443 tJcicZ443VO=sJcicZ443Service.findById(tJcicZ443Id,titaVo);
//		
//		int tFinalDay = Integer.parseInt(titaVo.getParam("FinalPayDay"))+19110000;
//		this.info("finalday=="+tFinalDay);
//		this.info("date=="+Today);
//		if (tFinalDay<Today) {
//			if(tJcicZ443VO!=null ) {
//				if (TranKey.equals("A")) {
//					throw new LogicException(titaVo, "E0002", "");
//				}
//				sJcicZ443Service.holdById(tJcicZ443VO.getJcicZ443Id(), titaVo);
//				JcicZ443 OrgJcicZ443 = (JcicZ443) dataLog.clone(tJcicZ443VO);//資料異動前
//				if(("Z").equals(TranKey)) {
//					if(("A").equals(OrgJcicZ443.getTranKey())) {
//						
//					}else {
//						//刷主管卡後始可刪除
//						// 交易需主管核可
//						if (!titaVo.getHsupCode().equals("1")) {
//							//titaVo.getSupCode();
//							sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
//						}
//					}
//					//刪除
//					try {
//						sJcicZ443Service.delete(tJcicZ443VO, titaVo);
//					} catch (DBException e) {
//						//E0008 刪除資料時，發生錯誤
//						throw new LogicException(titaVo, "E0008", "");
//					}
//				}else {
//					//Update
//					//OutJcicTxtDate 可以刪除不可異動
//					String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
//					if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
//						if(Integer.parseInt(OutJcicTxtDate)==0) {
//							tJcicZ443.setOutJcicTxtDate(0);
//						}
//					}else {
//						tJcicZ443.setOutJcicTxtDate(0);
//					}
//					tJcicZ443.setCreateDate(OrgJcicZ443.getCreateDate());
//					tJcicZ443.setCreateEmpNo(OrgJcicZ443.getCreateEmpNo());
//					try {
//						//tJcicZ443 = sJcicZ443Service.update(tJcicZ443,titaVo);//資料異動後-1
//						tJcicZ443 = sJcicZ443Service.update2(tJcicZ443,titaVo);//資料異動後-1
//						dataLog.setEnv(titaVo, OrgJcicZ443, tJcicZ443);//資料異動後-2
//						dataLog.exec();//資料異動後-3
//					} catch (DBException e) {
//						throw new LogicException(titaVo, "E0007", "");
//					}
//				}
//			}else {
//				//throw new LogicException(titaVo, "XXXXX", "查無資料");
//				//INSERT
//				try {
//					sJcicZ443Service.insert(tJcicZ443,titaVo);
//				} catch (DBException e) {
//					throw new LogicException(titaVo, "E0005", "");
//				}
//			}
//		}else {
//			throw new LogicException(titaVo, "E8106", "");
//		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}