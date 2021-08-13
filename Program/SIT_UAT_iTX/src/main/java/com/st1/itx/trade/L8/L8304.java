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
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.domain.JcicZ043Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ043Service;

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
* MaxMainCode=X,10<br>
* Account=X,50<br>
* CollateralType=X,2<br>
* OriginLoanAmt=9,12<br>
* CreditBalance=9,12<br>
* PerPeriordAmt=9,10<br>
* LastPayAmt=9,10<br>
* LastPayDate=9,7<br>
* OutstandAmt=9,10<br>
* RepayPerMonDay=9,2<br>
* ContractStartYM=9,6<br>
* ContractEndYM=9,6<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8304")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8304 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8304.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ043Service sJcicZ043Service;
	
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
		this.info("L8304");
		this.info("active L8304 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		String MaxMainCode=titaVo.getParam("MaxMainCode").trim();//最大債權金融機構代號
		String Account=titaVo.getParam("Account").trim();//帳號
		String CollateralType=titaVo.getParam("CollateralType").trim();//擔保品類別
		String OriginLoanAmt=titaVo.getParam("OriginLoanAmt").trim();//原借款金額
		String CreditBalance=titaVo.getParam("CreditBalance").trim();//授信餘額
		String PerPeriordAmt=titaVo.getParam("PerPeriordAmt").trim();//每期應付金額
		String LastPayAmt=titaVo.getParam("LastPayAmt").trim();//最近一起繳款金額
		String LastPayDate=titaVo.getParam("LastPayDate").trim();//最後繳息日
		String OutstandAmt=titaVo.getParam("OutstandAmt").trim();//已到期尚未償還金額
		String RepayPerMonDay=titaVo.getParam("RepayPerMonDay").trim();//每月應還款日
		String ContractStartYM=titaVo.getParam("ContractStartYM").trim();//契約起始年月
		String ContractEndYM=titaVo.getParam("ContractEndYM").trim();//契約截止年月

		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ043 tJcicZ043 = new JcicZ043();
		JcicZ043Id tJcicZ043Id = new JcicZ043Id();

		tJcicZ043Id.setCustId(CustId);//債務人IDN
		tJcicZ043Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ043Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ043Id.setMaxMainCode(MaxMainCode);//最大債權金融機構代號
		tJcicZ043Id.setAccount(Account);
		tJcicZ043.setJcicZ043Id(tJcicZ043Id);
		
		tJcicZ043.setTranKey(TranKey);
		tJcicZ043.setCollateralType(CollateralType);//擔保品類別
		tJcicZ043.setOriginLoanAmt(parse.stringToBigDecimal(OriginLoanAmt));//原借款金額
		tJcicZ043.setCreditBalance(parse.stringToBigDecimal(CreditBalance));//授信餘額
		tJcicZ043.setPerPeriordAmt(parse.stringToBigDecimal(PerPeriordAmt));//每期應付金額
		tJcicZ043.setLastPayAmt(parse.stringToBigDecimal(LastPayAmt));//最近一起繳款金額
		tJcicZ043.setLastPayDate(Integer.parseInt(jcicCom.RocTurnDc(LastPayDate,0)));//最後繳息日
		tJcicZ043.setOutstandAmt(parse.stringToBigDecimal(OutstandAmt));//已到期尚未償還金額
		tJcicZ043.setRepayPerMonDay(parse.stringToInteger(RepayPerMonDay));//每月應還款日
		tJcicZ043.setContractStartYM(parse.stringToInteger(jcicCom.RocTurnDc(ContractStartYM, 1)));//契約起始年月
		tJcicZ043.setContractEndYM(parse.stringToInteger(jcicCom.RocTurnDc(ContractEndYM, 1)));//契約截止年月
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ043.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ043.setOutJcicTxtDate(0);
		}
		JcicZ043 tJcicZ043VO=sJcicZ043Service.holdById(tJcicZ043Id, titaVo);
		JcicZ043 OrgJcicZ043 = null;
		if(tJcicZ043VO!=null) {
			OrgJcicZ043 = (JcicZ043) dataLog.clone(tJcicZ043VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ043VO,tJcicZ043VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ043.getTranKey())) {
					
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
					sJcicZ043Service.delete(tJcicZ043VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ043.getJcicZ043Id().getCustId(),tJcicZ043.getJcicZ043Id().getRcDate(),tJcicZ043.getJcicZ043Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ043VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ043.setCreateDate(tJcicZ043VO.getCreateDate());
				tJcicZ043.setCreateEmpNo(tJcicZ043VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ043.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ043.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ043.setCreateDate(OrgJcicZ043.getCreateDate());
				tJcicZ043.setCreateEmpNo(OrgJcicZ043.getCreateEmpNo());
				try {
					tJcicZ043 = sJcicZ043Service.update2(tJcicZ043, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ043, tJcicZ043);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ043.setTranKey(TranKey);
				try {
					sJcicZ043Service.insert(tJcicZ043, titaVo);
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