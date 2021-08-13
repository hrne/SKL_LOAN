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
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.domain.JcicZ053Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ053Service;

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
* AgreeSend=X,1<br>
* AgreeSendData1=X,2<br>
* AgreeSendData2=X,2<br>
* ChangePayDate=9,7<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8314")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8314 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8314.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ053Service sJcicZ053Service;
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
		this.info("Run L8314");
		this.info("active L8314 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String MaxMainCode=titaVo.getParam("MaxMainCode").trim();//最大債權金融機構代號
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ053 tJcicZ053 = new JcicZ053();
		JcicZ053Id tJcicZ053Id = new JcicZ053Id();
		tJcicZ053Id.setCustId(CustId);//債務人IDN
		tJcicZ053Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ053Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ053Id.setMaxMainCode(MaxMainCode);//最大債權金融機構代號
		tJcicZ053.setJcicZ053Id(tJcicZ053Id);
		
		tJcicZ053.setTranKey(TranKey);//交易代號
		tJcicZ053.setAgreeSend(titaVo.getParam("AgreeSend").trim());//是否同意報送例外處理檔案格式
		tJcicZ053.setAgreeSendData1(titaVo.getParam("AgreeSendData1").trim());//同意補報送檔案格式資料別1
		tJcicZ053.setAgreeSendData2(titaVo.getParam("AgreeSendData2").trim());//同意補報送檔案格式資料別2
		tJcicZ053.setChangePayDate(parse.stringToInteger(titaVo.getParam("ChangePayDate").trim()));//申請變更還款條件日

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ053.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ053.setOutJcicTxtDate(0);
		}
		JcicZ053 tJcicZ053VO=sJcicZ053Service.holdById(tJcicZ053Id, titaVo);
		JcicZ053 OrgJcicZ053 = null;
		if(tJcicZ053VO!=null) {
			OrgJcicZ053 = (JcicZ053) dataLog.clone(tJcicZ053VO);//資料異動前
		}

		this.info("tJcicZ053VO=["+tJcicZ053.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ053VO,tJcicZ053VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ053.getTranKey())) {
					
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
					sJcicZ053Service.delete(tJcicZ053VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ052(tJcicZ053Id.getCustId(), tJcicZ053Id.getRcDate(), tJcicZ053Id.getSubmitKey(), titaVo);
			if(tJcicZ053VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ053.setCreateDate(tJcicZ053VO.getCreateDate());
				tJcicZ053.setCreateEmpNo(tJcicZ053VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ053.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ053.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ053.setCreateDate(OrgJcicZ053.getCreateDate());
				tJcicZ053.setCreateEmpNo(OrgJcicZ053.getCreateEmpNo());
				try {
					tJcicZ053 = sJcicZ053Service.update2(tJcicZ053, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ053, tJcicZ053);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ053.setTranKey(TranKey);
				try {
					sJcicZ053Service.insert(tJcicZ053, titaVo);
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