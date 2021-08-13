package com.st1.itx.trade.L8;

import java.util.ArrayList;

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
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ048Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
* TranKey=X,3<br>
* SubmitKey=X,10<br>
* CustId=X,10<br>
* RcDate=9,7<br>
* CustRegAddr=X,150<br>
* CustComAddr=X,150<br>
* CustRegTelNo=X,16<br>
* CustComTelNo=X,16<br>
* CustMobilNo=X,16<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8309")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8309 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8309.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	
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
		this.info("Run L8309");
		this.info("active L8309 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ048 tJcicZ048 = new JcicZ048();
		JcicZ048Id tJcicZ048Id = new JcicZ048Id();
		tJcicZ048Id.setCustId(CustId);//債務人IDN
		tJcicZ048Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ048Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		
		tJcicZ048.setJcicZ048Id(tJcicZ048Id);

		tJcicZ048.setTranKey(TranKey);
		this.info("L8309 CustRegAddr=["+titaVo.getParam("CustRegAddr").trim()+"]");
		tJcicZ048.setCustRegAddr(titaVo.getParam("CustRegAddr").trim());//債務人戶籍之郵遞區號及地址
		this.info("L8309 CustRegAddr Get=["+tJcicZ048.getCustRegAddr()+"]");
		
		this.info("L8309 CustComAddr=["+titaVo.getParam("CustComAddr").trim()+"]");
		tJcicZ048.setCustComAddr(titaVo.getParam("CustComAddr").trim());//債務人通訊地之郵遞區號及地址
		this.info("L8309 CustComAddr Get=["+tJcicZ048.getCustComAddr()+"]");
		tJcicZ048.setCustRegTelNo(titaVo.getParam("CustRegTelNo").trim());//債務人戶籍電話
		tJcicZ048.setCustComTelNo(titaVo.getParam("CustComTelNo").trim());//債務人通訊電話
		tJcicZ048.setCustMobilNo(titaVo.getParam("CustMobilNo").trim());//債務人行動電話
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ048.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ048.setOutJcicTxtDate(0);
		}
		JcicZ048 tJcicZ048VO=sJcicZ048Service.holdById(tJcicZ048Id, titaVo);
		JcicZ048 OrgJcicZ048 = null;
		if(tJcicZ048VO!=null) {
			OrgJcicZ048 = (JcicZ048) dataLog.clone(tJcicZ048VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ048VO,tJcicZ048VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ048.getTranKey())) {
					
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
					sJcicZ048Service.delete(tJcicZ048VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			//JcicZ044 JcicZ044Vo=jcicCom.checkJcicZ044(tJcicZ048.getJcicZ048Id().getCustId(),tJcicZ048.getJcicZ048Id().getRcDate(),tJcicZ048.getJcicZ048Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ048VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ048.setCreateDate(tJcicZ048VO.getCreateDate());
				tJcicZ048.setCreateEmpNo(tJcicZ048VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ048.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ048.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ048.setCreateDate(OrgJcicZ048.getCreateDate());
				tJcicZ048.setCreateEmpNo(OrgJcicZ048.getCreateEmpNo());
				try {
					tJcicZ048 = sJcicZ048Service.update2(tJcicZ048, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ048, tJcicZ048);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ048.setTranKey(TranKey);
				try {
					sJcicZ048Service.insert(tJcicZ048, titaVo);
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