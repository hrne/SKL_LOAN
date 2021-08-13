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
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.domain.JcicZ052Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ052Service;
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
* BankCode1=X,10<br>
* DataCode1=X,2<br>
* BankCode2=X,10<br>
* DataCode2=X,2<br>
* BankCode3=X,10<br>
* DataCode3=X,2<br>
* BankCode4=X,10<br>
* DataCode4=X,2<br>
* BankCode5=X,10<br>
* DataCode5=X,2<br>
* ChangePayDate=9,7<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8313")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8313 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8313.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ052Service sJcicZ052Service;
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
		this.info("Run L8313");
		this.info("active L8313 ");
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
		JcicZ052 tJcicZ052 = new JcicZ052();
		JcicZ052Id tJcicZ052Id = new JcicZ052Id();
		tJcicZ052Id.setCustId(CustId);//債務人IDN
		tJcicZ052Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ052Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		
		tJcicZ052.setJcicZ052Id(tJcicZ052Id);
		
		tJcicZ052.setTranKey(TranKey);//交易代號
		tJcicZ052.setBankCode1(titaVo.getParam("BankCode1").trim());//同意報送債權機構代號1
		tJcicZ052.setDataCode1(titaVo.getParam("DataCode1").trim());//同意報送檔案格式資料別1
		tJcicZ052.setBankCode2(titaVo.getParam("BankCode2").trim());//同意報送債權機構代號2
		tJcicZ052.setDataCode2(titaVo.getParam("DataCode2").trim());//同意報送檔案格式資料別2
		tJcicZ052.setBankCode3(titaVo.getParam("BankCode3").trim());//同意報送債權機構代號3
		tJcicZ052.setDataCode3(titaVo.getParam("DataCode3").trim());//同意報送檔案格式資料別3
		tJcicZ052.setBankCode4(titaVo.getParam("BankCode4").trim());//同意報送債權機構代號4
		tJcicZ052.setDataCode4(titaVo.getParam("DataCode4").trim());//同意報送檔案格式資料別4
		tJcicZ052.setBankCode5(titaVo.getParam("BankCode5").trim());//同意報送債權機構代號5
		tJcicZ052.setDataCode5(titaVo.getParam("DataCode5").trim());//同意報送檔案格式資料別5
		tJcicZ052.setChangePayDate(parse.stringToInteger(titaVo.getParam("ChangePayDate").trim()));//申請變更還款條件日

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ052.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ052.setOutJcicTxtDate(0);
		}
		JcicZ052 tJcicZ052VO=sJcicZ052Service.holdById(tJcicZ052Id, titaVo);
		JcicZ052 OrgJcicZ052 = null;
		if(tJcicZ052VO!=null) {
			OrgJcicZ052 = (JcicZ052) dataLog.clone(tJcicZ052VO);//資料異動前
		}

		this.info("tJcicZ052VO=["+tJcicZ052.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ052VO,tJcicZ052VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ052.getTranKey())) {
					
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
					sJcicZ052Service.delete(tJcicZ052VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ052Id.getCustId(), tJcicZ052Id.getRcDate(), tJcicZ052Id.getSubmitKey(), titaVo);
			if(tJcicZ052VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ052.setCreateDate(tJcicZ052VO.getCreateDate());
				tJcicZ052.setCreateEmpNo(tJcicZ052VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ052.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ052.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ052.setCreateDate(OrgJcicZ052.getCreateDate());
				tJcicZ052.setCreateEmpNo(OrgJcicZ052.getCreateEmpNo());
				try {
					tJcicZ052 = sJcicZ052Service.update2(tJcicZ052, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ052, tJcicZ052);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ052.setTranKey(TranKey);
				try {
					sJcicZ052Service.insert(tJcicZ052, titaVo);
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