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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.domain.JcicZ045Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ045Service;

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
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* MaxMainCode=X,10<br>
* AgreeCode=X,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8306")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8306 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8306.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ045Service sJcicZ045Service;
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
		this.info("L8306");
		this.info("active L8306 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		String MaxMainCode=titaVo.getParam("MaxMainCode").trim();//最大債權金融機構代號
		
		String AgreeCode=titaVo.getParam("AgreeCode").trim();//帳號

		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ045 tJcicZ045 = new JcicZ045();
		JcicZ045Id tJcicZ045Id = new JcicZ045Id();

		tJcicZ045Id.setCustId(CustId);//債務人IDN
		tJcicZ045Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ045Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ045Id.setMaxMainCode(MaxMainCode);//最大債權金融機構代號
		
		tJcicZ045.setJcicZ045Id(tJcicZ045Id);

		tJcicZ045.setTranKey(TranKey);
		tJcicZ045.setAgreeCode(AgreeCode);
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ045.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ045.setOutJcicTxtDate(0);
		}
		JcicZ045 tJcicZ045VO=sJcicZ045Service.holdById(tJcicZ045Id, titaVo);
		JcicZ045 OrgJcicZ045 = null;
		if(tJcicZ045VO!=null) {
			OrgJcicZ045 = (JcicZ045) dataLog.clone(tJcicZ045VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ045VO,tJcicZ045VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ045.getTranKey())) {
					
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
					sJcicZ045Service.delete(tJcicZ045VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ045.getJcicZ045Id().getCustId(),tJcicZ045.getJcicZ045Id().getRcDate(),tJcicZ045.getJcicZ045Id().getSubmitKey(), titaVo);//資料檢核
			jcicCom.checkJcicZ048(tJcicZ045.getJcicZ045Id().getCustId(),tJcicZ045.getJcicZ045Id().getRcDate(),tJcicZ045.getJcicZ045Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ045VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ045.setCreateDate(tJcicZ045VO.getCreateDate());
				tJcicZ045.setCreateEmpNo(tJcicZ045VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ045.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ045.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ045.setCreateDate(OrgJcicZ045.getCreateDate());
				tJcicZ045.setCreateEmpNo(OrgJcicZ045.getCreateEmpNo());
				try {
					tJcicZ045 = sJcicZ045Service.update2(tJcicZ045, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ045, tJcicZ045);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ045.setTranKey(TranKey);
				try {
					sJcicZ045Service.insert(tJcicZ045, titaVo);
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