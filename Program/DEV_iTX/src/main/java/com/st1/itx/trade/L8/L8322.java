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
import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ570Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
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

@Service("L8322")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8322 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8322.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ570Service sJcicZ570Service;
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
		this.info("active L8322 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		String ApplyDate=titaVo.getParam("ApplyDate").trim();//款項統一收復申請日
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		
//		int Today=dateUtil.getNowIntegerForBC(); //取得今天日期
//		int todayDd = Integer.valueOf(String.valueOf(Today).substring(6));
//		if (todayDd>=11 && todayDd<=15) {
//			
//		}else {
//			throw new LogicException(titaVo, "E8103", "");
//		}
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ570 tJcicZ570 = new JcicZ570();
		JcicZ570Id tJcicZ570Id = new JcicZ570Id();
		tJcicZ570Id.setCustId(CustId);//債務人IDN
		tJcicZ570Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ570Id.setApplyDate(Integer.parseInt(jcicCom.RocTurnDc(ApplyDate,0)));//款項統一收復申請日
		tJcicZ570.setJcicZ570Id(tJcicZ570Id);
		this.info("L8322 applydate="+tJcicZ570Id.getApplyDate());
		this.info("L8322 CustId="+tJcicZ570Id.getCustId());
		this.info("L8322 SubmitKey="+tJcicZ570Id.getSubmitKey());
		
		tJcicZ570.setTranKey(TranKey);//交易代碼
		tJcicZ570.setAdjudicateDate(parse.stringToInteger(titaVo.getParam("AdjudicateDate").trim()));//更生方案認可裁定日
		tJcicZ570.setBankCount(parse.stringToInteger(titaVo.getParam("BankCount").trim()));//更生債權金融機構家數
		this.info("L8322 BANKCOUNT="+titaVo.getParam("BankCount"));
		tJcicZ570.setBank1(titaVo.getParam("Bank1").trim());//債權金融機構代號1
		tJcicZ570.setBank2(titaVo.getParam("Bank2").trim());//債權金融機構代號2
		tJcicZ570.setBank3(titaVo.getParam("Bank3").trim());//債權金融機構代號3
		tJcicZ570.setBank4(titaVo.getParam("Bank4").trim());//債權金融機構代號4
		tJcicZ570.setBank5(titaVo.getParam("Bank5").trim());//債權金融機構代號5
		tJcicZ570.setBank6(titaVo.getParam("Bank6").trim());//債權金融機構代號6
		tJcicZ570.setBank7(titaVo.getParam("Bank7").trim());//債權金融機構代號7
		tJcicZ570.setBank8(titaVo.getParam("Bank8").trim());//債權金融機構代號8
		tJcicZ570.setBank9(titaVo.getParam("Bank9").trim());//債權金融機構代號9
		tJcicZ570.setBank10(titaVo.getParam("Bank10").trim());//債權金融機構代號10
		tJcicZ570.setBank11(titaVo.getParam("Bank11").trim());//債權金融機構代號11
		tJcicZ570.setBank12(titaVo.getParam("Bank12").trim());//債權金融機構代號12
		tJcicZ570.setBank13(titaVo.getParam("Bank13").trim());//債權金融機構代號13
		tJcicZ570.setBank14(titaVo.getParam("Bank14").trim());//債權金融機構代號14
		tJcicZ570.setBank15(titaVo.getParam("Bank15").trim());//債權金融機構代號15
		tJcicZ570.setBank16(titaVo.getParam("Bank16").trim());//債權金融機構代號16
		tJcicZ570.setBank17(titaVo.getParam("Bank17").trim());//債權金融機構代號17
		tJcicZ570.setBank18(titaVo.getParam("Bank18").trim());//債權金融機構代號18
		tJcicZ570.setBank19(titaVo.getParam("Bank19").trim());//債權金融機構代號19
		tJcicZ570.setBank20(titaVo.getParam("Bank20").trim());//債權金融機構代號20
		tJcicZ570.setBank21(titaVo.getParam("Bank21").trim());//債權金融機構代號21
		tJcicZ570.setBank22(titaVo.getParam("Bank22").trim());//債權金融機構代號22
		tJcicZ570.setBank23(titaVo.getParam("Bank23").trim());//債權金融機構代號23
		tJcicZ570.setBank24(titaVo.getParam("Bank24").trim());//債權金融機構代號24
		tJcicZ570.setBank25(titaVo.getParam("Bank25").trim());//債權金融機構代號25
		tJcicZ570.setBank26(titaVo.getParam("Bank26").trim());//債權金融機構代號26
		tJcicZ570.setBank27(titaVo.getParam("Bank27").trim());//債權金融機構代號27
		tJcicZ570.setBank28(titaVo.getParam("Bank28").trim());//債權金融機構代號28
		tJcicZ570.setBank29(titaVo.getParam("Bank29").trim());//債權金融機構代號29
		tJcicZ570.setBank30(titaVo.getParam("Bank30").trim());//債權金融機構代號30
		
		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ570.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ570.setOutJcicTxtDate(0);
		}
		JcicZ570 tJcicZ570VO=sJcicZ570Service.holdById(tJcicZ570Id, titaVo);
		JcicZ570 OrgJcicZ570 = null;
		if(tJcicZ570VO!=null) {
			OrgJcicZ570 = (JcicZ570) dataLog.clone(tJcicZ570VO);//資料異動前
		}

		this.info("tJcicZ570VO=["+tJcicZ570.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ570VO,tJcicZ570VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ570.getTranKey())) {
					
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
					sJcicZ570Service.delete(tJcicZ570VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			if(tJcicZ570VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ570.setCreateDate(tJcicZ570VO.getCreateDate());
				tJcicZ570.setCreateEmpNo(tJcicZ570VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ570.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ570.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ570.setCreateDate(OrgJcicZ570.getCreateDate());
				tJcicZ570.setCreateEmpNo(OrgJcicZ570.getCreateEmpNo());
				try {
					tJcicZ570 = sJcicZ570Service.update2(tJcicZ570, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ570, tJcicZ570);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ570.setTranKey(TranKey);
				try {
					sJcicZ570Service.insert(tJcicZ570, titaVo);
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