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
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.domain.JcicZ049Id;
/*DB服務*/
import com.st1.itx.db.service.JcicZ049Service;

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
* SubmitKey=9,3<br>
* CustId=X,10<br>
* RcDate=9,7<br>
* ClaimStatus=X,1<br>
* ApplyDate=9,7<br>
* CourtCode=X,3<br>
* Year=9,4<br>
* CourtDiv=X,4<br>
* CourtCaseNo=X,20<br>
* Approve=X,1<br>
* ClaimDate=9,7<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8310")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8310 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8310.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ049Service sJcicZ049Service;
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
		this.info("Run L8310");
		this.info("active L8310 ");
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
		JcicZ049 tJcicZ049 = new JcicZ049();
		JcicZ049Id tJcicZ049Id = new JcicZ049Id();
		String year=titaVo.getParam("Year").trim();
		
		//前端為民國年
		if(year!=null && year.length()!=0) {
			int IntYear=Integer.parseInt(year);
			if(IntYear!=0) {
				year=String.valueOf(Integer.parseInt(year)+1911);
			}else {
				year="0";
			}
		}else {
			year="0";
		}
		tJcicZ049Id.setCustId(CustId);//債務人IDN
		tJcicZ049Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ049Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		
		tJcicZ049.setJcicZ049Id(tJcicZ049Id);

		tJcicZ049.setTranKey(TranKey);
		tJcicZ049.setClaimStatus(Integer.parseInt(titaVo.getParam("ClaimStatus").trim()));//案件進度
		tJcicZ049.setApplyDate(parse.stringToInteger(titaVo.getParam("ApplyDate").trim()));//遞狀日
		tJcicZ049.setCourtCode(titaVo.getParam("CourtCode").trim());//承審法院代碼
		tJcicZ049.setYear(Integer.parseInt(year));//年度別
		tJcicZ049.setCourtDiv(titaVo.getParam("CourtDiv").trim());//法院承審股別
		tJcicZ049.setCourtCaseNo(titaVo.getParam("CourtCaseNo").trim());//法院案號
		tJcicZ049.setApprove(titaVo.getParam("Approve").trim());//法院認可與否
		tJcicZ049.setClaimDate(parse.stringToInteger(titaVo.getParam("ClaimDate").trim()));//法院裁定日期

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ049.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ049.setOutJcicTxtDate(0);
		}
		JcicZ049 tJcicZ049VO=sJcicZ049Service.holdById(tJcicZ049Id, titaVo);
		JcicZ049 OrgJcicZ049 = null;
		if(tJcicZ049VO!=null) {
			OrgJcicZ049 = (JcicZ049) dataLog.clone(tJcicZ049VO);//資料異動前
		}
		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ049VO,tJcicZ049VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ049.getTranKey())) {
					
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
					sJcicZ049Service.delete(tJcicZ049VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			//JcicZ044 JcicZ044Vo=jcicCom.checkJcicZ044(tJcicZ049.getJcicZ049Id().getCustId(),tJcicZ049.getJcicZ049Id().getRcDate(),tJcicZ049.getJcicZ049Id().getSubmitKey(), titaVo);//資料檢核
			if(tJcicZ049VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				
				//UPDATE
				//KeyValue
				tJcicZ049.setCreateDate(tJcicZ049VO.getCreateDate());
				tJcicZ049.setCreateEmpNo(tJcicZ049VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ049.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ049.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ049.setCreateDate(OrgJcicZ049.getCreateDate());
				tJcicZ049.setCreateEmpNo(OrgJcicZ049.getCreateEmpNo());
				try {
					tJcicZ049 = sJcicZ049Service.update2(tJcicZ049, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ049, tJcicZ049);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ049.setTranKey(TranKey);
				try {
					sJcicZ049Service.insert(tJcicZ049, titaVo);
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