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


/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.domain.JcicZ042Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ042Service;

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
* RcDate=9,7<br>
* MaxMainCode=X,3<br>
* IsClaims=X,1<br>
* GuarLoanCnt=9,2<br>
* ExpLoanAmt=9,9<br>
* Civil323ExpAmt=9,9<br>
* ReceExpAmt=9,9<br>
* CashCardAmt=9,9<br>
* Civil323CashAmt=9,9<br>
* ReceCashAmt=9,9<br>
* CreditCardAmt=9,9<br>
* Civil323CreditAmt=9,9<br>
* ReceCreditAmt=9,9<br>
* ReceExpPrin=9,9<br>
* ReceExpInte=9,9<br>
* ReceExpPena=9,9<br>
* ReceExpOther=9,9<br>
* CashCardPrin=9,9<br>
* CashCardInte=9,9<br>
* CashCardPena=9,9<br>
* CashCardOther=9,9<br>
* CreditCardPrin=9,9<br>
* CreditCardInte=9,9<br>
* CreditCardPena=9,9<br>
* CreditCardOther=9,9<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8303")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8303 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8303.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ042Service sJcicZ042Service;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public JcicCom jcicCom;
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public DataLog dataLog;
	@Autowired
	SendRsp sendRsp;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L8303");
		this.info("active L8303 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		
		String MaxMainCode=titaVo.getParam("MaxMainCode").trim();//最大債權金融機構代號
		String IsClaims=titaVo.getParam("IsClaims").trim();//是否為本金融機構債務人
		String GuarLoanCnt=titaVo.getParam("GuarLoanCnt").trim();//本金融機構有擔保債權筆數
		String ExpLoanAmt=titaVo.getParam("ExpLoanAmt").trim();//信用貸款對內本息餘額
		String Civil323ExpAmt=titaVo.getParam("Civil323ExpAmt").trim();//依民法第323條計算之信用貸款本息餘額
		String ReceExpAmt=titaVo.getParam("ReceExpAmt").trim();//信用貸款最近一期繳款金額
		String CashCardAmt=titaVo.getParam("CashCardAmt").trim();//現金卡放款對內本息餘額
		String Civil323CashAmt=titaVo.getParam("Civil323CashAmt").trim();//依民法第323條計算之現金卡放款本息餘額
		String ReceCashAmt=titaVo.getParam("ReceCashAmt").trim();//現金卡最近一期繳款金額
		String CreditCardAmt=titaVo.getParam("CreditCardAmt").trim();//信用卡對內本息餘額
		String Civil323CreditAmt=titaVo.getParam("Civil323CreditAmt").trim();//依民法第323條計算之信用卡本息餘額
		String ReceCreditAmt=titaVo.getParam("ReceCreditAmt").trim();//信用卡最近一期繳款金額
		String ReceExpPrin=titaVo.getParam("ReceExpPrin").trim();//信用貸款本金
		String ReceExpInte=titaVo.getParam("ReceExpInte").trim();//信用貸款利息
		String ReceExpPena=titaVo.getParam("ReceExpPena").trim();//信用貸款違約金
		String ReceExpOther=titaVo.getParam("ReceExpOther").trim();//信用貸款其他費用
		String CashCardPrin=titaVo.getParam("CashCardPrin").trim();//現金卡本金
		String CashCardInte=titaVo.getParam("CashCardInte").trim();//信金卡利息
		String CashCardPena=titaVo.getParam("CashCardPena").trim();//信金卡違約金
		String CashCardOther=titaVo.getParam("CashCardOther").trim();//現金卡其他費用
		String CreditCardPrin=titaVo.getParam("CreditCardPrin").trim();//信用卡本金
		String CreditCardInte=titaVo.getParam("CreditCardInte").trim();//信用卡利息
		String CreditCardPena=titaVo.getParam("CreditCardPena").trim();//信用卡違約金
		String CreditCardOther=titaVo.getParam("CreditCardOther").trim();//信用卡其他費用
		
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ042 tJcicZ042 = new JcicZ042();
		JcicZ042Id tJcicZ042Id = new JcicZ042Id();

		tJcicZ042Id.setCustId(CustId);//債務人IDN
		tJcicZ042Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ042Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日

		tJcicZ042.setJcicZ042Id(tJcicZ042Id);
		
		tJcicZ042.setTranKey(TranKey);
		tJcicZ042.setMaxMainCode(MaxMainCode);//最大債權金融機構代號
		tJcicZ042.setIsClaims(IsClaims);//是否為本金融機構債務人
		
		if(IsClaims.equals("N")) {
			GuarLoanCnt="0";//本金融機構有擔保債權筆數
			ExpLoanAmt="0";//信用貸款對內本息餘額
			Civil323ExpAmt="0";//依民法第323條計算之信用貸款本息餘額
			ReceExpAmt="0";//信用貸款最近一期繳款金額
			CashCardAmt="0";//現金卡放款對內本息餘額
			Civil323CashAmt="0";//依民法第323條計算之現金卡放款本息餘額
			ReceCashAmt="0";//現金卡最近一期繳款金額
			CreditCardAmt="0";//信用卡對內本息餘額
			Civil323CreditAmt="0";//依民法第323條計算之信用卡本息餘額
			ReceCreditAmt="0";//信用卡最近一期繳款金額
			ReceExpPrin="0";//信用貸款本金
			ReceExpInte="0";//信用貸款利息
			ReceExpPena="0";//信用貸款違約金
			ReceExpOther="0";//信用貸款其他費用
			CashCardPrin="0";//現金卡本金
			CashCardInte="0";//信金卡利息
			CashCardPena="0";//信金卡違約金
			CashCardOther="0";//現金卡其他費用
			CreditCardPrin="0";//信用卡本金
			CreditCardInte="0";//信用卡利息
			CreditCardPena="0";//信用卡違約金
			CreditCardOther="0";//信用卡其他費用
		}
		tJcicZ042.setGuarLoanCnt(parse.stringToInteger(GuarLoanCnt));//本金融機構有擔保債權筆數
		tJcicZ042.setExpLoanAmt(parse.stringToInteger(ExpLoanAmt));//信用貸款對內本息餘額
		tJcicZ042.setCivil323ExpAmt(parse.stringToInteger(Civil323ExpAmt));//依民法第323條計算之信用貸款本息餘額
		tJcicZ042.setReceExpAmt(parse.stringToInteger(ReceExpAmt));//信用貸款最近一期繳款金額
		tJcicZ042.setCashCardAmt(parse.stringToInteger(CashCardAmt));//現金卡放款對內本息餘額
		tJcicZ042.setCivil323CashAmt(parse.stringToInteger(Civil323CashAmt));//依民法第323條計算之現金卡放款本息餘額
		tJcicZ042.setReceCashAmt(parse.stringToInteger(ReceCashAmt));//現金卡最近一期繳款金額
		tJcicZ042.setCreditCardAmt(parse.stringToInteger(CreditCardAmt));//信用卡對內本息餘額
		tJcicZ042.setCivil323CreditAmt(parse.stringToInteger(Civil323CreditAmt));//依民法第323條計算之信用卡本息餘額
		tJcicZ042.setReceCreditAmt(parse.stringToInteger(ReceCreditAmt));//信用卡最近一期繳款金額
		tJcicZ042.setReceExpPrin(parse.stringToInteger(ReceExpPrin));//信用貸款本金
		tJcicZ042.setReceExpInte(parse.stringToInteger(ReceExpInte));//信用貸款利息
		tJcicZ042.setReceExpPena(parse.stringToInteger(ReceExpPena));//信用貸款違約金
		tJcicZ042.setReceExpOther(parse.stringToInteger(ReceExpOther));//信用貸款其他費用
		tJcicZ042.setCashCardPrin(parse.stringToInteger(CashCardPrin));//現金卡本金
		tJcicZ042.setCashCardInte(parse.stringToInteger(CashCardInte));//信金卡利息
		tJcicZ042.setCashCardPena(parse.stringToInteger(CashCardPena));//信金卡違約金
		tJcicZ042.setCashCardOther(parse.stringToInteger(CashCardOther));//現金卡其他費用
		tJcicZ042.setCreditCardPrin(parse.stringToInteger(CreditCardPrin));//信用卡本金
		tJcicZ042.setCreditCardInte(parse.stringToInteger(CreditCardInte));//信用卡利息
		tJcicZ042.setCreditCardPena(parse.stringToInteger(CreditCardPena));//信用卡違約金
		tJcicZ042.setCreditCardOther(parse.stringToInteger(CreditCardOther));//信用卡其他費用
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ042.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ042.setOutJcicTxtDate(0);
		}
		JcicZ042 tJcicZ042VO=sJcicZ042Service.holdById(tJcicZ042Id, titaVo);
		JcicZ042 OrgJcicZ042 = null;
		if(tJcicZ042VO!=null) {
			OrgJcicZ042 = (JcicZ042) dataLog.clone(tJcicZ042VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ042VO,tJcicZ042VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ042.getTranKey())) {
					
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
					sJcicZ042Service.delete(tJcicZ042VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ042.getJcicZ042Id().getCustId(),tJcicZ042.getJcicZ042Id().getRcDate(),tJcicZ042.getJcicZ042Id().getSubmitKey(), titaVo);//資料檢核
			CheckCountJcicZ043(tJcicZ042,GuarLoanCnt,titaVo);//比數檢核
			//GuarLoanCnt
			if(("Y").equals(IsClaims)) {
				jcicCom.checkJcicZ045(tJcicZ042.getJcicZ042Id().getCustId(),tJcicZ042.getJcicZ042Id().getRcDate(),tJcicZ042.getJcicZ042Id().getSubmitKey(),tJcicZ042.getMaxMainCode(), titaVo);
			}
			
			if(tJcicZ042VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ042.setCreateDate(tJcicZ042VO.getCreateDate());
				tJcicZ042.setCreateEmpNo(tJcicZ042VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ042.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ042.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ042.setCreateDate(OrgJcicZ042.getCreateDate());
				tJcicZ042.setCreateEmpNo(OrgJcicZ042.getCreateEmpNo());
				try {
					tJcicZ042 = sJcicZ042Service.update2(tJcicZ042, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ042, tJcicZ042);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ042.setTranKey(TranKey);
				try {
					sJcicZ042Service.insert(tJcicZ042, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	public void CheckCountJcicZ043(JcicZ042 tJcicZ042,String GuarLoanCnt,TitaVo titaVo) throws LogicException {
		int intGuarLoanCnt=0;
		if(GuarLoanCnt!=null && GuarLoanCnt.length()!=0) {
			intGuarLoanCnt=Integer.parseInt(GuarLoanCnt);
		}
		int CountJcicZ043=jcicCom.countJcicZ043(tJcicZ042.getJcicZ042Id().getCustId(),tJcicZ042.getJcicZ042Id().getRcDate(),tJcicZ042.getJcicZ042Id().getSubmitKey(),tJcicZ042.getMaxMainCode(), titaVo);
		if(CountJcicZ043!=intGuarLoanCnt) {
			//E5009	資料檢核錯誤
			throw new LogicException(titaVo, "E5009","資料筆數("+intGuarLoanCnt+") 不等於 JcicZ043筆數("+CountJcicZ043+")");
		}
	}
}