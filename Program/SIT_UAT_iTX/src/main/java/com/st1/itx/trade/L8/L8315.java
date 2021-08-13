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
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.domain.JcicZ054Id;

//import com.st1.itx.db.domain.JcicZ047;
//import com.st1.itx.db.domain.JcicZ047Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ054Service;

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
* PayOffResult=X,1<br>
* PayOffDate=9,7<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8315")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8315 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8315.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ054Service sJcicZ054Service;
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
		this.info("Run L8315");
		this.info("active L8315 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//防呆
//		JcicZ047Id tJcicZ047Id = new JcicZ047Id();
//		tJcicZ047Id.setCustId(CustId);//債務人IDN
//		tJcicZ047Id.setSubmitKey(SubmitKey);//報送單位代號
//		tJcicZ047Id.setRcDate(parse.stringToInteger(RcDate));//協商申請日
//		JcicZ047 tJcicZ047VO=sJcicZ047Service.findById(tJcicZ047Id);
//		if (tJcicZ047VO == null) {
//			//金融機構無擔保債務協議資料檔案(表47)查無此協商債務人資料，請先至表47新增資料
//			throw new LogicException(titaVo, "E8002","");
//		}
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ054 tJcicZ054 = new JcicZ054();
		JcicZ054Id tJcicZ054Id = new JcicZ054Id();
		tJcicZ054Id.setCustId(CustId);//債務人IDN
		tJcicZ054Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ054Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ054Id.setMaxMainCode(titaVo.getParam("MaxMainCode").trim());//最大債權金融機構代號
		
		tJcicZ054.setJcicZ054Id(tJcicZ054Id);
		
		tJcicZ054.setTranKey(TranKey);//交易代號
		tJcicZ054.setPayOffResult(titaVo.getParam("PayOffResult").trim());//單獨全數受清償原因
		tJcicZ054.setPayOffDate(parse.stringToInteger(titaVo.getParam("PayOffDate").trim()));//單獨全數受清償日期
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ054.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ054.setOutJcicTxtDate(0);
		}
		JcicZ054 tJcicZ054VO=sJcicZ054Service.holdById(tJcicZ054Id, titaVo);
		JcicZ054 OrgJcicZ054 = null;
		if(tJcicZ054VO!=null) {
			OrgJcicZ054 = (JcicZ054) dataLog.clone(tJcicZ054VO);//資料異動前
		}

		this.info("tJcicZ054VO=["+tJcicZ054.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ054VO,tJcicZ054VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ054.getTranKey())) {
					
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
					sJcicZ054Service.delete(tJcicZ054VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ047(tJcicZ054Id.getCustId(), tJcicZ054Id.getRcDate(), tJcicZ054Id.getSubmitKey(), titaVo);
			if(tJcicZ054VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ054.setCreateDate(tJcicZ054VO.getCreateDate());
				tJcicZ054.setCreateEmpNo(tJcicZ054VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ054.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ054.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ054.setCreateDate(OrgJcicZ054.getCreateDate());
				tJcicZ054.setCreateEmpNo(OrgJcicZ054.getCreateEmpNo());
				try {
					tJcicZ054 = sJcicZ054Service.update2(tJcicZ054, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ054, tJcicZ054);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ054.setTranKey(TranKey);
				try {
					sJcicZ054Service.insert(tJcicZ054, titaVo);
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