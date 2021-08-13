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
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.domain.JcicZ063Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ063Service;

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
* ChangePayDate=9,7<br>
* ClosedDate=9,7<br>
* ClosedResult=9,1<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8321")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8321 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8321.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ063Service sJcicZ063Service;
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
		this.info("Run L8321");
		this.info("active L8321 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String RcDate=titaVo.getParam("RcDate").trim();//協商申請日
		String ChangePayDate=titaVo.getParam("ChangePayDate").trim();//申請變更還款條件日
		String ClosedDate=titaVo.getParam("ClosedDate").trim();//變更還款條件結案日期
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ063 tJcicZ063 = new JcicZ063();
		JcicZ063Id tJcicZ063Id = new JcicZ063Id();
		tJcicZ063Id.setCustId(CustId);//債務人IDN
		tJcicZ063Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ063Id.setRcDate(Integer.parseInt(jcicCom.RocTurnDc(RcDate,0)));//協商申請日
		tJcicZ063Id.setChangePayDate(Integer.parseInt(jcicCom.RocTurnDc(ChangePayDate,0)));//申請變更還款條件日
//		tJcicZ063Id.setClosedDate(Integer.parseInt(jcicCom.RocTurnDc(ClosedDate,0)));//變更還款條件結案日期
		tJcicZ063.setJcicZ063Id(tJcicZ063Id);
		
		tJcicZ063.setTranKey(TranKey);//交易代號
		tJcicZ063.setClosedResult(titaVo.getParam("ClosedResult").trim());//結案原因

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ063.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ063.setOutJcicTxtDate(0);
		}
		JcicZ063 tJcicZ063VO=sJcicZ063Service.holdById(tJcicZ063Id, titaVo);
		JcicZ063 OrgJcicZ063 = null;
		if(tJcicZ063VO!=null) {
			OrgJcicZ063 = (JcicZ063) dataLog.clone(tJcicZ063VO);//資料異動前
		}

		this.info("tJcicZ063VO=["+tJcicZ063.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ063VO,tJcicZ063VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ063.getTranKey())) {
					
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
					sJcicZ063Service.delete(tJcicZ063VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ063VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ063.setCreateDate(tJcicZ063VO.getCreateDate());
				tJcicZ063.setCreateEmpNo(tJcicZ063VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ063.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ063.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ063.setCreateDate(OrgJcicZ063.getCreateDate());
				tJcicZ063.setCreateEmpNo(OrgJcicZ063.getCreateEmpNo());
				try {
					tJcicZ063 = sJcicZ063Service.update2(tJcicZ063, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ063, tJcicZ063);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ063.setTranKey(TranKey);
				try {
					sJcicZ063Service.insert(tJcicZ063, titaVo);
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