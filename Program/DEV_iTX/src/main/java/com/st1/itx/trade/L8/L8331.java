package com.st1.itx.trade.L8;

import java.util.ArrayList;
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
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.domain.JcicZ444Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ444Service;
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

@Service("L8331")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8331 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8331.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ444Service sJcicZ444Service;
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
		this.info("active L8331 ");
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
		JcicZ444 tJcicZ444 = new JcicZ444();
		JcicZ444Id tJcicZ444Id = new JcicZ444Id();
		tJcicZ444Id.setCustId(CustId);// 債務人IDN
		tJcicZ444Id.setSubmitKey(SubmitKey);// 報送單位代號
		tJcicZ444Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));// 款項統一收復申請日
//		tJcicZ444Id.setBankId(titaVo.getParam("BankId").trim());// 異動債權金機構代號
		tJcicZ444.setJcicZ444Id(tJcicZ444Id);

		tJcicZ444.setTranKey(titaVo.getParam("TranKey").trim());// 交易代碼
		tJcicZ444.setCustRegAddr(titaVo.getParam("CustRegAddr").trim());// 債務人戶籍之郵遞區號及地址
		tJcicZ444.setCustComAddr(titaVo.getParam("CustComAddr").trim());// 債務人通訊地之郵遞區號及地址
		tJcicZ444.setCustRegTelNo(titaVo.getParam("CustRegTelNo").trim());// 債務人戶籍電話
		tJcicZ444.setCustComTelNo(titaVo.getParam("CustComTelNo").trim());// 債務人通訊電話
		tJcicZ444.setCustMobilNo(titaVo.getParam("CustMobilNo").trim());// 債務人行動電話

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ444.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ444.setOutJcicTxtDate(0);
		}
		JcicZ444 tJcicZ444VO=sJcicZ444Service.holdById(tJcicZ444Id, titaVo);
		JcicZ444 OrgJcicZ444 = null;
		if(tJcicZ444VO!=null) {
			OrgJcicZ444 = (JcicZ444) dataLog.clone(tJcicZ444VO);//資料異動前
		}

		this.info("tJcicZ444VO=["+tJcicZ444.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ444VO,tJcicZ444VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ444.getTranKey())) {
					
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
					sJcicZ444Service.delete(tJcicZ444VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
//			jcicCom.checkHadJcicZ446(tJcicZ444Id.getCustId(), tJcicZ444Id.getApplyDate(), tJcicZ444Id.getSubmitKey(), tJcicZ444Id.getBankId(), titaVo);
//			jcicCom.checkJcicZ440(tJcicZ444Id.getCustId(), tJcicZ444Id.getApplyDate(), tJcicZ444Id.getSubmitKey(), tJcicZ444Id.getBankId(), titaVo);
			if(tJcicZ444VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ444.setCreateDate(tJcicZ444VO.getCreateDate());
				tJcicZ444.setCreateEmpNo(tJcicZ444VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ444.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ444.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ444.setCreateDate(OrgJcicZ444.getCreateDate());
				tJcicZ444.setCreateEmpNo(OrgJcicZ444.getCreateEmpNo());
				try {
					tJcicZ444 = sJcicZ444Service.update2(tJcicZ444, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ444, tJcicZ444);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ444.setTranKey(TranKey);
				try {
					sJcicZ444Service.insert(tJcicZ444, titaVo);
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