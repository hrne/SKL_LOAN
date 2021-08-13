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
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.domain.JcicZ061Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ061Service;

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
* MaxMainCode=X,10<br>
* ExpBalanceAmt=9,9<br>
* CashBalanceAmt=9,9<br>
* CreditBalanceAmt=9,9<br>
* MaxMainNote=X,1<br>
* IsGuarantor=X,1<br>
* IsChangePayment=X,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8319")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8319 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8319.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ061Service sJcicZ061Service;
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
		this.info("Run L8319");
		this.info("active L8319 ");
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
		JcicZ061 tJcicZ061 = new JcicZ061();
		JcicZ061Id tJcicZ061Id = new JcicZ061Id();
		tJcicZ061Id.setCustId(CustId);//債務人IDN
		tJcicZ061Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ061Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ061Id.setChangePayDate(Integer.parseInt(jcicCom.RocTurnDc(ChangePayDate,0)));//申請變更還款條件日
		tJcicZ061.setJcicZ061Id(tJcicZ061Id);
		
		tJcicZ061.setTranKey(TranKey);//交易代號
		tJcicZ061.setMaxMainCode(titaVo.getParam("MaxMainCode").trim());//最大債權金融機構代號
		tJcicZ061.setExpBalanceAmt(parse.stringToInteger(titaVo.getParam("ExpBalanceAmt").trim()));//信用貸款協商剩餘債權餘額
		tJcicZ061.setCashBalanceAmt(parse.stringToInteger(titaVo.getParam("CashBalanceAmt").trim()));//現金卡協商剩餘債權餘額
		tJcicZ061.setCreditBalanceAmt(parse.stringToInteger(titaVo.getParam("CreditBalanceAmt").trim()));//信用卡協商剩餘債權餘額
		tJcicZ061.setMaxMainNote(titaVo.getParam("MaxMainNote").trim());//最大債權金融機構報送註記
		tJcicZ061.setIsGuarantor(titaVo.getParam("IsGuarantor").trim());//是否有保證人
		tJcicZ061.setIsChangePayment(titaVo.getParam("IsChangePayment").trim());//是否同意債務人申請變更還款條件方案

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ061.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ061.setOutJcicTxtDate(0);
		}
		JcicZ061 tJcicZ061VO=sJcicZ061Service.holdById(tJcicZ061Id, titaVo);
		JcicZ061 OrgJcicZ061 = null;
		if(tJcicZ061VO!=null) {
			OrgJcicZ061 = (JcicZ061) dataLog.clone(tJcicZ061VO);//資料異動前
		}

		this.info("tJcicZ061VO=["+tJcicZ061.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ061VO,tJcicZ061VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ061.getTranKey())) {
					
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
					sJcicZ061Service.delete(tJcicZ061VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ061VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ061.setCreateDate(tJcicZ061VO.getCreateDate());
				tJcicZ061.setCreateEmpNo(tJcicZ061VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ061.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ061.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ061.setCreateDate(OrgJcicZ061.getCreateDate());
				tJcicZ061.setCreateEmpNo(OrgJcicZ061.getCreateEmpNo());
				try {
					tJcicZ061 = sJcicZ061Service.update2(tJcicZ061, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ061, tJcicZ061);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ061.setTranKey(TranKey);
				try {
					sJcicZ061Service.insert(tJcicZ061, titaVo);
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