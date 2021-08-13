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
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ050Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

@Service("L8311")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8311 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8311.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ050Service sJcicZ050Service;
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
		this.info("Run L8311");
		this.info("active L8311 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		
		String PayDate=titaVo.getParam("PayDate").trim();//繳款日期
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ050 tJcicZ050 = new JcicZ050();
		JcicZ050Id tJcicZ050Id = new JcicZ050Id();
		tJcicZ050Id.setCustId(CustId);//債務人IDN
		tJcicZ050Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ050Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ050Id.setPayDate(Integer.parseInt(jcicCom.RocTurnDc(PayDate,0)));//繳款日期
		
		tJcicZ050.setJcicZ050Id(tJcicZ050Id);

		tJcicZ050.setTranKey(TranKey);
		tJcicZ050.setPayAmt(parse.stringToInteger(titaVo.getParam("PayAmt").trim()));//本次繳款金額
		tJcicZ050.setSumRepayActualAmt(parse.stringToInteger(titaVo.getParam("SumRepayActualAmt").trim()));//累計實際還款金額
		tJcicZ050.setSumRepayShouldAmt(parse.stringToInteger(titaVo.getParam("SumRepayShouldAmt").trim()));//累計應還款金額
		tJcicZ050.setStatus(titaVo.getParam("Status").trim());//債權結案註記
		
		String SecondRepayYM=titaVo.getParam("SecondRepayYM").trim();
		this.info("SecondRepayYM=["+SecondRepayYM+"]");
		this.info("SecondRepayYM JcicCom=["+Integer.parseInt(jcicCom.RocTurnDc(SecondRepayYM,1))+"]");
		tJcicZ050.setSecondRepayYM(Integer.parseInt(jcicCom.RocTurnDc(SecondRepayYM,1)));//進入第二階梯還款年月
		this.info("SecondRepayYM tJcicZ050=["+tJcicZ050.getSecondRepayYM()+"]");
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ050.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ050.setOutJcicTxtDate(0);
		}
		JcicZ050 tJcicZ050VO=sJcicZ050Service.holdById(tJcicZ050Id, titaVo);
		JcicZ050 OrgJcicZ050 = null;
		if(tJcicZ050VO!=null) {
			OrgJcicZ050 = (JcicZ050) dataLog.clone(tJcicZ050VO);//資料異動前
		}
		
		this.info("tJcicZ050VO=["+tJcicZ050.toString()+"]");
		
		if("Y".equals(tJcicZ050.getStatus())) {
			
		}else {
			jcicCom.checkHadJcicZ046(tJcicZ050Id.getCustId(), tJcicZ050Id.getRcDate(), tJcicZ050Id.getSubmitKey(), titaVo);
		}
		
		
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ050VO,tJcicZ050VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ050.getTranKey())) {
					
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
					sJcicZ050Service.delete(tJcicZ050VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.CheckSumJcicZ050PayAmt(tJcicZ050,titaVo);
			//JcicZ044 JcicZ044Vo=jcicCom.checkJcicZ044(tJcicZ050.getJcicZ050Id().getCustId(),tJcicZ050.getJcicZ050Id().getRcDate(),tJcicZ050.getJcicZ050Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ050VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ050.setCreateDate(tJcicZ050VO.getCreateDate());
				tJcicZ050.setCreateEmpNo(tJcicZ050VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ050.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ050.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ050.setCreateDate(OrgJcicZ050.getCreateDate());
				tJcicZ050.setCreateEmpNo(OrgJcicZ050.getCreateEmpNo());
				try {
					tJcicZ050 = sJcicZ050Service.update2(tJcicZ050, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ050, tJcicZ050);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ050.setTranKey(TranKey);
				try {
					sJcicZ050Service.insert(tJcicZ050, titaVo);
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