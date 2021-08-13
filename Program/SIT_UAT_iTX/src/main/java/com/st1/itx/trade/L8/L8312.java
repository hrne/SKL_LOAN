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
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.domain.JcicZ051Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ051Service;

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
* DelayCode=X,1<br>
* DelayYM=X,6<br>
* DelayDesc=X,80<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8312")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8312 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8312.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ051Service sJcicZ051Service;
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
		this.info("Run L8312");
		this.info("active L8312 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		
		String DelayYM=titaVo.getParam("DelayYM").trim();//延期繳款年月
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ051 tJcicZ051 = new JcicZ051();
		JcicZ051Id tJcicZ051Id = new JcicZ051Id();
		tJcicZ051Id.setCustId(CustId);//債務人IDN
		tJcicZ051Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ051Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ051Id.setDelayYM(Integer.parseInt(jcicCom.RocTurnDc(DelayYM,1)));;//繳款年月
		
		tJcicZ051.setJcicZ051Id(tJcicZ051Id);

		tJcicZ051.setTranKey(TranKey);
		tJcicZ051.setDelayCode(titaVo.getParam("DelayCode").trim());//延期繳款原因
		tJcicZ051.setDelayDesc(titaVo.getParam("DelayDesc").trim());//延期繳款案情說明

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ051.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ051.setOutJcicTxtDate(0);
		}
		JcicZ051 tJcicZ051VO=sJcicZ051Service.holdById(tJcicZ051Id, titaVo);
		JcicZ051 OrgJcicZ051 = null;
		if(tJcicZ051VO!=null) {
			OrgJcicZ051 = (JcicZ051) dataLog.clone(tJcicZ051VO);//資料異動前
		}

		this.info("tJcicZ051VO=["+tJcicZ051.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ051VO,tJcicZ051VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ051.getTranKey())) {
					
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
					sJcicZ051Service.delete(tJcicZ051VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ051(tJcicZ051,titaVo);
			if(tJcicZ051VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				
				//UPDATE
				//KeyValue
				tJcicZ051.setCreateDate(tJcicZ051VO.getCreateDate());
				tJcicZ051.setCreateEmpNo(tJcicZ051VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ051.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ051.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ051.setCreateDate(OrgJcicZ051.getCreateDate());
				tJcicZ051.setCreateEmpNo(OrgJcicZ051.getCreateEmpNo());
				try {
					tJcicZ051 = sJcicZ051Service.update2(tJcicZ051, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ051, tJcicZ051);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ051.setTranKey(TranKey);
				try {
					sJcicZ051Service.insert(tJcicZ051, titaVo);
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