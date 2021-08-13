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
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ573Service;

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

@Service("L8325")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8325 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8325.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ573Service sJcicZ573Service;
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
		this.info("active L8325 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String PayDate=titaVo.getParam("PayDate").trim();
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ573 tJcicZ573 = new JcicZ573();
		JcicZ573Id tJcicZ573Id = new JcicZ573Id();
		tJcicZ573Id.setCustId(CustId);//債務人IDN
		tJcicZ573Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ573Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ573Id.setPayDate(parse.stringToInteger(jcicCom.RocTurnDc(PayDate,0))); //本分配表首繳日
		tJcicZ573.setJcicZ573Id(tJcicZ573Id);

		tJcicZ573.setTranKey(TranKey);//交易代碼
		tJcicZ573.setPayAmt(parse.stringToInteger(titaVo.getParam("PayAmt").trim()));//本日繳款金額
		tJcicZ573.setTotalPayAmt(parse.stringToInteger(titaVo.getParam("TotalPayAmt").trim()));//累計繳款金額
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ573.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ573.setOutJcicTxtDate(0);
		}
		JcicZ573 tJcicZ573VO=sJcicZ573Service.holdById(tJcicZ573Id, titaVo);
		JcicZ573 OrgJcicZ573 = null;
		if(tJcicZ573VO!=null) {
			OrgJcicZ573 = (JcicZ573) dataLog.clone(tJcicZ573VO);//資料異動前
		}

		this.info("tJcicZ573VO=["+tJcicZ573.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ573VO,tJcicZ573VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ573.getTranKey())) {
					
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
					sJcicZ573Service.delete(tJcicZ573VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ573VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ573.setCreateDate(tJcicZ573VO.getCreateDate());
				tJcicZ573.setCreateEmpNo(tJcicZ573VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ573.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ573.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ573.setCreateDate(OrgJcicZ573.getCreateDate());
				tJcicZ573.setCreateEmpNo(OrgJcicZ573.getCreateEmpNo());
				try {
					tJcicZ573 = sJcicZ573Service.update2(tJcicZ573, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ573, tJcicZ573);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ573.setTranKey(TranKey);
				try {
					sJcicZ573Service.insert(tJcicZ573, titaVo);
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