package com.st1.itx.trade.L8;

import java.util.ArrayList;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ062Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* CompletePeriod=9,3<br>
* Period=9,3<br>
* Rate=9,2.2<br>
* ExpBalanceAmt=9,9<br>
* CashBalanceAmt=9,9<br>
* CreditBalanceAmt=9,9<br>
* ChaRepayAmt=9,9<br>
* ChaRepayAgreeDate=9,7<br>
* ChaRepayViewDate=9,7<br>
* ChaRepayEndDate=9,7<br>
* ChaRepayFirstDate=9,7<br>
* PayAccount=X,20<br>
* PostAddr=X,100<br>
* MonthPayAmt=9,9<br>
* GradeType=X,1<br>
* Period2=9,3<br>
* Rate2=9,2.2<br>
* MonthPayAmt2=9,9<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8320")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8320 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8320.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ062Service sJcicZ062Service;
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
		this.info("Run L8320");
		this.info("active L8320 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String ChangePayDate=titaVo.getParam("ChangePayDate").trim();//申請變更還款條件日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ062 tJcicZ062 = new JcicZ062();
		JcicZ062Id tJcicZ062Id = new JcicZ062Id();
		tJcicZ062Id.setCustId(CustId);//債務人IDN
		tJcicZ062Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ062Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ062Id.setChangePayDate(Integer.parseInt(jcicCom.RocTurnDc(ChangePayDate,0)));//申請變更還款條件日
		tJcicZ062.setJcicZ062Id(tJcicZ062Id);
		
		tJcicZ062.setTranKey(TranKey);//交易代號
//		tJcicZ062.setTranKey(titaVo.getParam("TranKey").trim());//交易代碼
//		tJcicZ062.setCustId(titaVo.getParam("CustId").trim());//債務人IDN
//		tJcicZ062.setSubmitKey(titaVo.getParam("SubmitKey").trim());//報送單位代號
//		tJcicZ062.setRcDate(parse.stringToInteger(titaVo.getParam("RcDate").trim()));//協商申請日
//		tJcicZ062.setChangePayDate(parse.stringToInteger(titaVo.getParam("ChangePayDate").trim()));//申請變更還款條件日
		tJcicZ062.setCompletePeriod(parse.stringToInteger(titaVo.getParam("CompletePeriod").trim()));//變更還款條件已履約期數
		tJcicZ062.setPeriod(parse.stringToInteger(titaVo.getParam("Period").trim()));//(第一階梯)期數
		tJcicZ062.setRate(parse.stringToBigDecimal(titaVo.getParam("Rate").trim()));//(第一階梯)利率
		tJcicZ062.setExpBalanceAmt(parse.stringToInteger(titaVo.getParam("ExpBalanceAmt").trim()));//信用貸款協商剩餘債務簽約餘額
		tJcicZ062.setCashBalanceAmt(parse.stringToInteger(titaVo.getParam("CashBalanceAmt").trim()));//現金卡協商剩餘債務簽約餘額
		tJcicZ062.setCreditBalanceAmt(parse.stringToInteger(titaVo.getParam("CreditBalanceAmt").trim()));//信用卡協商剩餘債務簽約餘額
		tJcicZ062.setChaRepayAmt(parse.stringToBigDecimal(titaVo.getParam("ChaRepayAmt").trim()));//變更還款條件簽約總債務金額
		tJcicZ062.setChaRepayAgreeDate(parse.stringToInteger(titaVo.getParam("ChaRepayAgreeDate").trim()));//變更還款條件協議完成日
		tJcicZ062.setChaRepayViewDate(parse.stringToInteger(titaVo.getParam("ChaRepayViewDate").trim()));//變更還款條件面談日期
		tJcicZ062.setChaRepayEndDate(parse.stringToInteger(titaVo.getParam("ChaRepayEndDate").trim()));//變更還款條件簽約完成日期
		tJcicZ062.setChaRepayFirstDate(parse.stringToInteger(titaVo.getParam("ChaRepayFirstDate").trim()));//變更還款條件首期應繳款日
		tJcicZ062.setPayAccount(titaVo.getParam("PayAccount").trim());//繳款帳號
		tJcicZ062.setPostAddr(titaVo.getParam("PostAddr").trim());//最大債權金融機構聲請狀送達地址
		tJcicZ062.setMonthPayAmt(parse.stringToInteger(titaVo.getParam("MonthPayAmt").trim()));//月付金
		tJcicZ062.setGradeType(titaVo.getParam("GradeType").trim());//屬階梯式還款註記
		tJcicZ062.setPeriod2(parse.stringToInteger(titaVo.getParam("Period2").trim()));//第二階梯期數
		tJcicZ062.setRate2(parse.stringToBigDecimal(titaVo.getParam("Rate2").trim()));//第二階梯利率
		tJcicZ062.setMonthPayAmt2(parse.stringToInteger(titaVo.getParam("MonthPayAmt2").trim()));//第二階段月付金
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ062.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ062.setOutJcicTxtDate(0);
		}
		JcicZ062 tJcicZ062VO=sJcicZ062Service.holdById(tJcicZ062Id, titaVo);
		JcicZ062 OrgJcicZ062 = null;
		if(tJcicZ062VO!=null) {
			OrgJcicZ062 = (JcicZ062) dataLog.clone(tJcicZ062VO);//資料異動前
		}

		this.info("tJcicZ062VO=["+tJcicZ062.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ062VO,tJcicZ062VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ062.getTranKey())) {
					
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
					sJcicZ062Service.delete(tJcicZ062VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ062VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ062.setCreateDate(tJcicZ062VO.getCreateDate());
				tJcicZ062.setCreateEmpNo(tJcicZ062VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ062.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ062.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ062.setCreateDate(OrgJcicZ062.getCreateDate());
				tJcicZ062.setCreateEmpNo(OrgJcicZ062.getCreateEmpNo());
				try {
					tJcicZ062 = sJcicZ062Service.update2(tJcicZ062, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ062, tJcicZ062);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ062.setTranKey(TranKey);
				try {
					sJcicZ062Service.insert(tJcicZ062, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}