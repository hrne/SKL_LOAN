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
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.domain.JcicZ041Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ041Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
* FUNCD=X,1<br>
* DEBTOR_IDNO=X,10<br>
* DEPARTMIND=X,10<br>
* NEGO_APPLDATE=9,7<br>
* INTED_DATE=9,7<br>
* ACCEPTIND=9,1<br>
* RE_FINANCIAL_IND=X,3<br>
* UNDIS_CLAIMSORG_IND1=X,3<br>
* UNDIS_CLAIMSORG_IND2=X,3<br>
* UNDIS_CLAIMSORG_IND3=X,3<br>
* OUT_JcicTXT_DATE=9,7<br>
* END=X,1<br>
*/

@Service("L8302")
@Scope("prototype")
public class L8302 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8302.class);
	
	/* DB服務注入 */
	@Autowired
	public JcicZ041Service sJcicZ041Service;
	
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
		this.info("active L8302 ");
		
		this.totaVo.init(titaVo);//前端資料
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		
		String ScDate=titaVo.getParam("ScDate").trim();//停催日期
		String NegoStartDate=titaVo.getParam("NegoStartDate").trim();//協商開始日
		String NonFinClaimAmt=titaVo.getParam("NonFinClaimAmt").trim();//非金融機構債權金額
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ041 tJcicZ041 = new JcicZ041();
		JcicZ041Id tJcicZ041Id = new JcicZ041Id();

		tJcicZ041Id.setCustId(CustId);//債務人IDN
		tJcicZ041Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ041Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日

		tJcicZ041.setJcicZ041Id(tJcicZ041Id);
		
		tJcicZ041.setCustId(tJcicZ041Id.getCustId());//債務人IDN
		tJcicZ041.setSubmitKey(tJcicZ041Id.getSubmitKey());//報送單位代號
		tJcicZ041.setRcDate(tJcicZ041Id.getRcDate());//協商申請日
		
		tJcicZ041.setTranKey(TranKey);
		tJcicZ041.setScDate(Integer.parseInt(jcicCom.RocTurnDc(ScDate,0)));
		tJcicZ041.setNegoStartDate(Integer.parseInt(jcicCom.RocTurnDc(NegoStartDate,0)));
		tJcicZ041.setNonFinClaimAmt(parse.stringToInteger(NonFinClaimAmt));
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ041.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ041.setOutJcicTxtDate(0);
		}
		JcicZ041 tJcicZ041VO=sJcicZ041Service.holdById(tJcicZ041Id, titaVo);
		JcicZ041 OrgJcicZ041 = null;
		if(tJcicZ041VO!=null) {
			OrgJcicZ041 = (JcicZ041) dataLog.clone(tJcicZ041VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ041VO,tJcicZ041VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ041.getTranKey())) {
					
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
					sJcicZ041Service.delete(tJcicZ041VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			jcicCom.checkJcicZ040(tJcicZ041.getJcicZ041Id().getCustId(),tJcicZ041.getJcicZ041Id().getRcDate(),tJcicZ041.getJcicZ041Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ041VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				
				
				tJcicZ041.setCreateDate(tJcicZ041VO.getCreateDate());
				tJcicZ041.setCreateEmpNo(tJcicZ041VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ041.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ041.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ041.setCreateDate(OrgJcicZ041.getCreateDate());
				tJcicZ041.setCreateEmpNo(OrgJcicZ041.getCreateEmpNo());
				try {
					tJcicZ041 = sJcicZ041Service.update2(tJcicZ041, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ041, tJcicZ041);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ041.setTranKey(TranKey);
				try {
					sJcicZ041Service.insert(tJcicZ041, titaVo);
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