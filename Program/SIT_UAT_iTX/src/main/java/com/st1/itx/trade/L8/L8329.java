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
import com.st1.itx.db.domain.JcicZ442;
import com.st1.itx.db.domain.JcicZ442Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ442Service;

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

@Service("L8329")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8329 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8329.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ442Service sJcicZ442Service;
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
		this.info("active L8329 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ442 tJcicZ442 = new JcicZ442();
		JcicZ442Id tJcicZ442Id = new JcicZ442Id();
		tJcicZ442Id.setCustId(CustId);//債務人IDN
		tJcicZ442Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ442Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ442Id.setBankId(titaVo.getParam("BankId").trim());//異動債權金機構代號
		tJcicZ442Id.setMaxMainCode(titaVo.getParam("MaxMainCode").trim());//最大債權金融機構代號
		tJcicZ442.setJcicZ442Id(tJcicZ442Id);

		tJcicZ442.setTranKey(TranKey);//交易代碼
		tJcicZ442.setIsMaxMain(titaVo.getParam("IsMaxMain").trim());//是否為最大債權金融機構報送
		tJcicZ442.setIsClaims(titaVo.getParam("IsClaims").trim());//是否為本金融機構債務人
		tJcicZ442.setGuarLoanCnt(Integer.parseInt(titaVo.getParam("GuarLoanCnt").trim()));//本金融機構有擔保債權筆數
		tJcicZ442.setCivil323ExpAmt(Integer.valueOf(titaVo.getParam("Civil323ExpAmt")));//依民法第323條計算之信用放款本息餘額
		tJcicZ442.setCivil323CashAmt(Integer.valueOf(titaVo.getParam("Civil323CashAmt")));//依民法第323條計算之現金卡放款本息餘額
		tJcicZ442.setCivil323CreditAmt(Integer.valueOf(titaVo.getParam("Civil323CreditAmt")));//依民法第323條計算之信用卡本息餘額
		tJcicZ442.setCivil323GuarAmt(Integer.valueOf(titaVo.getParam("Civil323GuarAmt")));//依民法第323條計算之保證債權本息餘額
		tJcicZ442.setReceExpPrin(Integer.valueOf(titaVo.getParam("ReceExpPrin")));//信用放款本金
		tJcicZ442.setReceExpInte(Integer.valueOf(titaVo.getParam("ReceExpInte")));//信用放款利息
		tJcicZ442.setReceExpPena(Integer.valueOf(titaVo.getParam("ReceExpPena")));//信用放款違約金
		tJcicZ442.setReceExpOther(Integer.valueOf(titaVo.getParam("ReceExpOther")));//信用放款其他費用
		tJcicZ442.setCashCardPrin(Integer.valueOf(titaVo.getParam("CashCardPrin")));//現金卡放款本金
		tJcicZ442.setCashCardInte(Integer.valueOf(titaVo.getParam("CashCardInte")));//現金卡放款利息
		tJcicZ442.setCashCardPena(Integer.valueOf(titaVo.getParam("CashCardPena")));//現金卡放款違約金
		tJcicZ442.setCashCardOther(Integer.valueOf(titaVo.getParam("CashCardOther")));//現金卡放款其他費用
		tJcicZ442.setCreditCardPrin(Integer.valueOf(titaVo.getParam("CreditCardPrin")));//信用卡放款本金
		tJcicZ442.setCreditCardInte(Integer.valueOf(titaVo.getParam("CreditCardInte")));//信用卡放款利息
		tJcicZ442.setCreditCardPena(Integer.valueOf(titaVo.getParam("CreditCardPena")));//信用卡放款違約金
		tJcicZ442.setCreditCardOther(Integer.valueOf(titaVo.getParam("CreditCardOther")));//信用卡放款其他費用
		tJcicZ442.setGuarObliPrin(Integer.valueOf(titaVo.getParam("GuarObliPrin")));//保證債權放款本金
		tJcicZ442.setGuarObliInte(Integer.valueOf(titaVo.getParam("GuarObliInte")));//保證債權放款利息
		tJcicZ442.setGuarObliPena(Integer.valueOf(titaVo.getParam("GuarObliPena")));//保證債權放款違約金
		tJcicZ442.setGuarObliOther(Integer.valueOf(titaVo.getParam("GuarObliOther")));//保證債權放款其他費用
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ442.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ442.setOutJcicTxtDate(0);
		}
		JcicZ442 tJcicZ442VO=sJcicZ442Service.holdById(tJcicZ442Id, titaVo);
		JcicZ442 OrgJcicZ442 = null;
		if(tJcicZ442VO!=null) {
			OrgJcicZ442 = (JcicZ442) dataLog.clone(tJcicZ442VO);//資料異動前
		}

		this.info("tJcicZ442VO=["+tJcicZ442.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ442VO,tJcicZ442VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ442.getTranKey())) {
					
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
					sJcicZ442Service.delete(tJcicZ442VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ440(tJcicZ442Id.getCustId(), tJcicZ442Id.getApplyDate(), tJcicZ442Id.getSubmitKey(), tJcicZ442Id.getBankId(), titaVo);
			if(tJcicZ442VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ442.setCreateDate(tJcicZ442VO.getCreateDate());
				tJcicZ442.setCreateEmpNo(tJcicZ442VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ442.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ442.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ442.setCreateDate(OrgJcicZ442.getCreateDate());
				tJcicZ442.setCreateEmpNo(OrgJcicZ442.getCreateEmpNo());
				try {
					tJcicZ442 = sJcicZ442Service.update2(tJcicZ442, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ442, tJcicZ442);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ442.setTranKey(TranKey);
				try {
					sJcicZ442Service.insert(tJcicZ442, titaVo);
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