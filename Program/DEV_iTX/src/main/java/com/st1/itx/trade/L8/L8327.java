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
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.domain.JcicZ575Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ575Service;

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

@Service("L8327")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8327 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8327.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ575Service sJcicZ575Service;
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
		this.info("active L8327 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String BankId=titaVo.getParam("BankId").trim();//異動債權金融機構代號
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String ModifyType=titaVo.getParam("ModifyType").trim();//債權異動類別
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		
		//JcicMAaster
		JcicZ575 tJcicZ575 = new JcicZ575();
		JcicZ575Id tJcicZ575Id = new JcicZ575Id();
		tJcicZ575Id.setCustId(CustId);//債務人IDN
		tJcicZ575Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ575Id.setApplyDate(parse.stringToInteger(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ575Id.setBankId(BankId);//異動債權金機構代號
		tJcicZ575.setJcicZ575Id(tJcicZ575Id);

		tJcicZ575.setTranKey(TranKey);//交易代碼
		tJcicZ575.setModifyType(ModifyType);//債權異動類別

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ575.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ575.setOutJcicTxtDate(0);
		}
		JcicZ575 tJcicZ575VO=sJcicZ575Service.holdById(tJcicZ575Id, titaVo);
		JcicZ575 OrgJcicZ575 = null;
		if(tJcicZ575VO!=null) {
			OrgJcicZ575 = (JcicZ575) dataLog.clone(tJcicZ575VO);//資料異動前
		}

		this.info("tJcicZ575VO=["+tJcicZ575.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ575VO,tJcicZ575VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ575.getTranKey())) {
					
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
					sJcicZ575Service.delete(tJcicZ575VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ575VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ575.setCreateDate(tJcicZ575VO.getCreateDate());
				tJcicZ575.setCreateEmpNo(tJcicZ575VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ575.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ575.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ575.setCreateDate(OrgJcicZ575.getCreateDate());
				tJcicZ575.setCreateEmpNo(OrgJcicZ575.getCreateEmpNo());
				try {
					tJcicZ575 = sJcicZ575Service.update2(tJcicZ575, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ575, tJcicZ575);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ575.setTranKey(TranKey);
				try {
					sJcicZ575Service.insert(tJcicZ575, titaVo);
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