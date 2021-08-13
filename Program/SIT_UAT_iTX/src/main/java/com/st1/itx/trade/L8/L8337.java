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
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.domain.JcicZ454Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ454Service;

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

@Service("L8337")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8337 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8337.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ454Service sJcicZ454Service;
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
		this.info("active L8337 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey = titaVo.getParam("TranKey").trim(); // 交易代碼
		String CustId = titaVo.getParam("CustId").trim();// 債務人IDN
		String SubmitKey = titaVo.getParam("SubmitKey").trim();// 報送單位代號
		String ApplyDate = titaVo.getParam("ApplyDate").trim();// 款項統一收復申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		// int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		// JcicMAaster
		JcicZ454 tJcicZ454 = new JcicZ454();
		JcicZ454Id tJcicZ454Id = new JcicZ454Id();
		tJcicZ454Id.setCustId(CustId);// 債務人IDN
		tJcicZ454Id.setSubmitKey(SubmitKey);// 報送單位代號
		tJcicZ454Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));// 款項統一收復申請日
		tJcicZ454Id.setBankId(titaVo.getParam("BankId").trim());// 異動債權金機構代號
		tJcicZ454Id.setMaxMainCode(titaVo.getParam("MaxMainCode").trim());
		tJcicZ454.setJcicZ454Id(tJcicZ454Id);

		tJcicZ454.setTranKey(titaVo.getParam("TranKey").trim());// 交易代碼
		tJcicZ454.setPayOffResult(titaVo.getParam("PayOffResult").trim());// 單獨全數受清償原因
		tJcicZ454.setPayOffDate(parse.stringToInteger(titaVo.getParam("PayOffDate").trim()));// 單獨全數受清償日期

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ454.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ454.setOutJcicTxtDate(0);
		}
		JcicZ454 tJcicZ454VO=sJcicZ454Service.holdById(tJcicZ454Id, titaVo);
		JcicZ454 OrgJcicZ454 = null;
		if(tJcicZ454VO!=null) {
			OrgJcicZ454 = (JcicZ454) dataLog.clone(tJcicZ454VO);//資料異動前
		}

		this.info("tJcicZ454VO=["+tJcicZ454.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ454VO,tJcicZ454VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ454.getTranKey())) {
					
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
					sJcicZ454Service.delete(tJcicZ454VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkHadJcicZ446(tJcicZ454Id.getCustId(), tJcicZ454Id.getApplyDate(), tJcicZ454Id.getSubmitKey(), tJcicZ454Id.getBankId(), titaVo);
			jcicCom.checkJcicZ448(tJcicZ454Id.getCustId(), tJcicZ454Id.getApplyDate(), tJcicZ454Id.getSubmitKey(), tJcicZ454Id.getBankId(),tJcicZ454Id.getMaxMainCode(), titaVo);
			if(tJcicZ454VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ454.setCreateDate(tJcicZ454VO.getCreateDate());
				tJcicZ454.setCreateEmpNo(tJcicZ454VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ454.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ454.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ454.setCreateDate(OrgJcicZ454.getCreateDate());
				tJcicZ454.setCreateEmpNo(OrgJcicZ454.getCreateEmpNo());
				try {
					tJcicZ454 = sJcicZ454Service.update2(tJcicZ454, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ454, tJcicZ454);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ454.setTranKey(TranKey);
				try {
					sJcicZ454Service.insert(tJcicZ454, titaVo);
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