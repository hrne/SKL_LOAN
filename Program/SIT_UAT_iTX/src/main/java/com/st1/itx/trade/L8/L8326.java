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


import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.domain.JcicZ574Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ574Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* TranKey=X,1<br>
* CustId=X,10<br>
* SubmitKey=X,10<br>
* RcDate=9,7<br>
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8326")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8326 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8326.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ574Service sJcicZ574Service;
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
		this.info("active L8326 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		this.info("L8326custid=="+titaVo.getParam("CustId"));
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
//		String PayDate=titaVo.getParam("PayDate").trim();
		
		//int Today=dateUtil.getNowIntegerForBC();
		
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ574 tJcicZ574 = new JcicZ574();
		JcicZ574Id tJcicZ574Id = new JcicZ574Id();
		tJcicZ574Id.setCustId(CustId);//債務人IDN
		tJcicZ574Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ574Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ574.setJcicZ574Id(tJcicZ574Id);

		tJcicZ574.setTranKey(TranKey);//交易代碼
		tJcicZ574.setCloseDate(parse.stringToInteger(titaVo.getParam("CloseDate").trim()));//結案日期
		tJcicZ574.setCloseMark(titaVo.getParam("CloseMark").trim());//結案原因
		tJcicZ574.setPhoneNo(titaVo.getParam("PhoneNo").trim());//通訊電話

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ574.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ574.setOutJcicTxtDate(0);
		}
		JcicZ574 tJcicZ574VO=sJcicZ574Service.holdById(tJcicZ574Id, titaVo);
		JcicZ574 OrgJcicZ574 = null;
		if(tJcicZ574VO!=null) {
			OrgJcicZ574 = (JcicZ574) dataLog.clone(tJcicZ574VO);//資料異動前
		}

		this.info("tJcicZ574VO=["+tJcicZ574.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ574VO,tJcicZ574VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ574.getTranKey())) {
					
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
					sJcicZ574Service.delete(tJcicZ574VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ574VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ574.setCreateDate(tJcicZ574VO.getCreateDate());
				tJcicZ574.setCreateEmpNo(tJcicZ574VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ574.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ574.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ574.setCreateDate(OrgJcicZ574.getCreateDate());
				tJcicZ574.setCreateEmpNo(OrgJcicZ574.getCreateEmpNo());
				try {
					tJcicZ574 = sJcicZ574Service.update2(tJcicZ574, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ574, tJcicZ574);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ574.setTranKey(TranKey);
				try {
					sJcicZ574Service.insert(tJcicZ574, titaVo);
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