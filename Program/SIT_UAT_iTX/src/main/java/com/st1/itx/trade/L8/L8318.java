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
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.domain.JcicZ060Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ060Service;

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
* YM=X,6<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8318")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8318 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8318.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ060Service sJcicZ060Service;
	
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
		this.info("Run L8318");
		this.info("active L8318 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String ChangePayDate=titaVo.getParam("ChangePayDate").trim();//申請變更還款條件日
		
		String YM=titaVo.getParam("YM").trim();
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ060 tJcicZ060 = new JcicZ060();
		JcicZ060Id tJcicZ060Id = new JcicZ060Id();
		tJcicZ060Id.setCustId(CustId);//債務人IDN
		tJcicZ060Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ060Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ060Id.setChangePayDate(Integer.parseInt(jcicCom.RocTurnDc(ChangePayDate,0)));//申請變更還款條件日
		tJcicZ060.setJcicZ060Id(tJcicZ060Id);
		
		tJcicZ060.setTranKey(TranKey);//交易代號
		

		tJcicZ060.setYM(Integer.parseInt(jcicCom.RocTurnDc(YM,1)));//已清分足月期付金年月

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ060.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ060.setOutJcicTxtDate(0);
		}
		JcicZ060 tJcicZ060VO=sJcicZ060Service.holdById(tJcicZ060Id, titaVo);
		JcicZ060 OrgJcicZ060 = null;
		if(tJcicZ060VO!=null) {
			OrgJcicZ060 = (JcicZ060) dataLog.clone(tJcicZ060VO);//資料異動前
		}

		this.info("tJcicZ060VO=["+tJcicZ060.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ060VO,tJcicZ060VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ060.getTranKey())) {
					
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
					sJcicZ060Service.delete(tJcicZ060VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			//jcicCom.checkJcicZ063(tJcicZ060Id.getCustId(), tJcicZ060Id.getRcDate(), tJcicZ060Id.getSubmitKey(), tJcicZ060Id.getChangePayDate(), titaVo);
			
			//若無JCICZ047資剔退 或 若已有JCICZ046剔退
			jcicCom.checkJcicZ047(tJcicZ060Id.getCustId(), tJcicZ060Id.getRcDate(), tJcicZ060Id.getSubmitKey(), titaVo);
			jcicCom.checkHadJcicZ046(tJcicZ060Id.getCustId(), tJcicZ060Id.getRcDate(), tJcicZ060Id.getSubmitKey(), titaVo);
			if(tJcicZ060VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ060.setCreateDate(tJcicZ060VO.getCreateDate());
				tJcicZ060.setCreateEmpNo(tJcicZ060VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ060.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ060.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ060.setCreateDate(OrgJcicZ060.getCreateDate());
				tJcicZ060.setCreateEmpNo(OrgJcicZ060.getCreateEmpNo());
				try {
					tJcicZ060 = sJcicZ060Service.update2(tJcicZ060, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ060, tJcicZ060);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ060.setTranKey(TranKey);
				try {
					sJcicZ060Service.insert(tJcicZ060, titaVo);
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