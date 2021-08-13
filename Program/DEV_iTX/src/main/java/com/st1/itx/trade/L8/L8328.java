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
import com.st1.itx.db.domain.JcicZ440;
import com.st1.itx.db.domain.JcicZ440Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ440Service;

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

@Service("L8328")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8328 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8328.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ440Service sJcicZ440Service;
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
		this.info("active L8328 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String iAgreeDate = titaVo.getParam("AgreeDate").trim();
		String iStartDate = titaVo.getParam("StartDate").trim();
		String iRemindDate = titaVo.getParam("RemindDate").trim();
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ440 tJcicZ440 = new JcicZ440();
		JcicZ440Id tJcicZ440Id = new JcicZ440Id();
		tJcicZ440Id.setCustId(CustId);//債務人IDN
		tJcicZ440Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ440Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
//		tJcicZ440Id.setBankId(titaVo.getParam("BankId").trim());//受理調解機構代號
		tJcicZ440.setJcicZ440Id(tJcicZ440Id);

		tJcicZ440.setTranKey(titaVo.getParam("TranKey").trim());//交易代碼
		tJcicZ440.setAgreeDate(parse.stringToInteger(iAgreeDate));//同意書取得日期
		tJcicZ440.setStartDate(parse.stringToInteger(iStartDate));//首次調解日
		tJcicZ440.setRemindDate(parse.stringToInteger(iRemindDate));//債權計算基準日
		tJcicZ440.setApplyType(titaVo.getParam("ApplyType").trim());//受理方式
		tJcicZ440.setReportYn(titaVo.getParam("ReportYn").trim());//協辦行是否需自行回報債權
		tJcicZ440.setNotBankId1(titaVo.getParam("NotBankId1").trim());//未揭露債權機構代號1
		tJcicZ440.setNotBankId2(titaVo.getParam("NotBankId2").trim());//未揭露債權機構代號2
		tJcicZ440.setNotBankId3(titaVo.getParam("NotBankId3").trim());//未揭露債權機構代號3
		tJcicZ440.setNotBankId4(titaVo.getParam("NotBankId4").trim());//未揭露債權機構代號4
		tJcicZ440.setNotBankId5(titaVo.getParam("NotBankId5").trim());//未揭露債權機構代號5
		tJcicZ440.setNotBankId6(titaVo.getParam("NotBankId6").trim());//未揭露債權機構代號6
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ440.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ440.setOutJcicTxtDate(0);
		}
		JcicZ440 tJcicZ440VO=sJcicZ440Service.holdById(tJcicZ440Id, titaVo);
		JcicZ440 OrgJcicZ440 = null;
		if(tJcicZ440VO!=null) {
			OrgJcicZ440 = (JcicZ440) dataLog.clone(tJcicZ440VO);//資料異動前
		}

		this.info("tJcicZ440VO=["+tJcicZ440.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ440VO,tJcicZ440VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ440.getTranKey())) {
					
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
					sJcicZ440Service.delete(tJcicZ440VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ440VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ440.setCreateDate(tJcicZ440VO.getCreateDate());
				tJcicZ440.setCreateEmpNo(tJcicZ440VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ440.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ440.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ440.setCreateDate(OrgJcicZ440.getCreateDate());
				tJcicZ440.setCreateEmpNo(OrgJcicZ440.getCreateEmpNo());
				try {
					tJcicZ440 = sJcicZ440Service.update2(tJcicZ440, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ440, tJcicZ440);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ440.setTranKey(TranKey);
				try {
					sJcicZ440Service.insert(tJcicZ440, titaVo);
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