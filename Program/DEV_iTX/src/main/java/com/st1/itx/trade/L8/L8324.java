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
import com.st1.itx.db.domain.JcicZ572;
import com.st1.itx.db.domain.JcicZ572Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ572Service;

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

@Service("L8324")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8324 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8324.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ572Service sJcicZ572Service;
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
		this.info("active L8324 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String BankId=titaVo.getParam("BankId").trim();//受理款項統一收付之債權金融機構代號
		String PayDate=titaVo.getParam("PayDate").trim();//本分配表首繳日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ572 tJcicZ572 = new JcicZ572();
		JcicZ572Id tJcicZ572Id = new JcicZ572Id();
		tJcicZ572Id.setCustId(CustId);//債務人IDN
		tJcicZ572Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ572Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ572Id.setBankId(BankId);
		tJcicZ572Id.setPayDate(parse.stringToInteger(jcicCom.RocTurnDc(PayDate,0))); //本分配表首繳日
		tJcicZ572.setJcicZ572Id(tJcicZ572Id);
		
		tJcicZ572.setTranKey(TranKey);//交易代碼
		tJcicZ572.setStartDate(parse.stringToInteger(titaVo.getParam("StartDate").trim()));//生效日期
		tJcicZ572.setAllotAmt(parse.stringToInteger(titaVo.getParam("AllotAmt").trim()));//參與分配債權金額
		tJcicZ572.setOwnPercentage(parse.stringToBigDecimal(titaVo.getParam("OwnPercentage").trim()));//債權比例

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ572.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ572.setOutJcicTxtDate(0);
		}
		JcicZ572 tJcicZ572VO=sJcicZ572Service.holdById(tJcicZ572Id, titaVo);
		JcicZ572 OrgJcicZ572 = null;
		if(tJcicZ572VO!=null) {
			OrgJcicZ572 = (JcicZ572) dataLog.clone(tJcicZ572VO);//資料異動前
		}

		this.info("tJcicZ572VO=["+tJcicZ572.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ572VO,tJcicZ572VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ572.getTranKey())) {
					
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
					sJcicZ572Service.delete(tJcicZ572VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ572VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ572.setCreateDate(tJcicZ572VO.getCreateDate());
				tJcicZ572.setCreateEmpNo(tJcicZ572VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ572.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ572.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ572.setCreateDate(OrgJcicZ572.getCreateDate());
				tJcicZ572.setCreateEmpNo(OrgJcicZ572.getCreateEmpNo());
				try {
					tJcicZ572 = sJcicZ572Service.update2(tJcicZ572, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ572, tJcicZ572);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ572.setTranKey(TranKey);
				try {
					sJcicZ572Service.insert(tJcicZ572, titaVo);
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