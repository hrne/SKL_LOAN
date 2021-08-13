package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;


/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.domain.JcicZ055Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ055Service;

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
* CaseStatus=X,1<br>
* ClaimDate=9,7<br>
* CourtCode=X,3<br>
* Year=9,3<br>
* CourtDiv=X,8<br>
* CourtCaseNo=X,80<br>
* PayDate=9,7<br>
* PayEndDate=9,7<br>
* Period=9,3<br>
* Rate=9,2.2<br>
* OutstandAmt=9,9<br>
* SubAmt=9,9<br>
* ClaimStatus1=X,1<br>
* SaveDate=9,7<br>
* ClaimStatus2=X,1<br>
* SaveEndDate=9,7<br>
* OutJcictxtDate=9,7<br>
* IsImplement=X,1<br>
* InspectName=X,20<br>
*/

@Service("L8316")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8316 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8316.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ055Service sJcicZ055Service;
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
		this.info("active L8316 ");
		this.totaVo.init(titaVo);
		String FunctionCd=titaVo.getParam("FunctionCd").trim(); //功能代碼:01:新增,02:修改,04:刪除,05:查詢
		String TranKey=titaVo.getParam("TranKey").trim(); //交易代碼
		String CustId=titaVo.getParam("CustId").trim();//債務人IDN
		String SubmitKey=titaVo.getParam("SubmitKey").trim();//報送單位代號
		
		String CaseStatus=titaVo.getParam("CaseStatus").trim();//案件狀態
		String ClaimDate=titaVo.getParam("ClaimDate").trim();//裁定日期
		String CourtCode=titaVo.getParam("CourtCode").trim();//承審法院代碼
		String Year =titaVo.getParam("Year").trim();
		String OutJcicTxtDate=titaVo.getParam("OutJcicTxtDate").trim();//轉出Jcic文字檔日期
		/*檢核 CaseStatus 案件狀態*/
		

		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ055 tJcicZ055 = new JcicZ055();
		JcicZ055Id tJcicZ055Id = new JcicZ055Id();
		tJcicZ055Id.setCustId(CustId);//債務人IDN
		tJcicZ055Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ055Id.setCaseStatus(CaseStatus);//案件狀態
		tJcicZ055Id.setClaimDate(Integer.parseInt(jcicCom.RocTurnDc(ClaimDate,0)));//裁定日期
	
		tJcicZ055Id.setCourtCode(CourtCode);//承審法院代碼
		
		tJcicZ055.setJcicZ055Id(tJcicZ055Id);
		
		tJcicZ055.setTranKey(TranKey);//交易代號
		
		tJcicZ055.setYear(Integer.parseInt(jcicCom.RocTurnDc(Year,2)));//年度別
		tJcicZ055.setCourtDiv(titaVo.getParam("CourtDiv").trim());//法院承審股別
		tJcicZ055.setCourtCaseNo(titaVo.getParam("CourtCaseNo").trim());//法院案號
		tJcicZ055.setPayDate(parse.stringToInteger(titaVo.getParam("PayDate").trim()));//更生方案首期應繳款日
		tJcicZ055.setPayEndDate(parse.stringToInteger(titaVo.getParam("PayEndDate").trim()));//更生方案末期應繳款日
		tJcicZ055.setPeriod(parse.stringToInteger(titaVo.getParam("Period").trim()));//更生條件(期數)
		tJcicZ055.setRate(parse.stringToBigDecimal(titaVo.getParam("Rate").trim()));//更生條件(利率)
		tJcicZ055.setOutstandAmt(parse.stringToInteger(titaVo.getParam("OutstandAmt").trim()));//原始債權金額
		tJcicZ055.setSubAmt(parse.stringToInteger(titaVo.getParam("SubAmt").trim()));//更生損失金額
		tJcicZ055.setClaimStatus1(titaVo.getParam("ClaimStatus1").trim());//法院裁定保全處分
		tJcicZ055.setSaveDate(parse.stringToInteger(titaVo.getParam("SaveDate").trim()));//保全處分起始日
		tJcicZ055.setClaimStatus2(titaVo.getParam("ClaimStatus2").trim());//法院裁定撤銷保全處分
		tJcicZ055.setSaveEndDate(parse.stringToInteger(titaVo.getParam("SaveEndDate").trim()));//保全處分撤銷日
		tJcicZ055.setIsImplement(titaVo.getParam("IsImplement").trim());//是否依更生條件履行
		tJcicZ055.setInspectName(titaVo.getParam("InspectName").trim());//監督人姓名

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ055.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ055.setOutJcicTxtDate(0);
		}
		JcicZ055 tJcicZ055VO=sJcicZ055Service.holdById(tJcicZ055Id, titaVo);
		JcicZ055 OrgJcicZ055 = null;
		if(tJcicZ055VO!=null) {
			OrgJcicZ055 = (JcicZ055) dataLog.clone(tJcicZ055VO);//資料異動前
		}

		this.info("tJcicZ055VO=["+tJcicZ055.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ055VO,tJcicZ055VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ055.getTranKey())) {
					
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
					sJcicZ055Service.delete(tJcicZ055VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			CheckCaseStatus(tJcicZ055Id.getCustId(), tJcicZ055Id.getSubmitKey(), tJcicZ055Id.getCaseStatus(), tJcicZ055Id.getClaimDate(), tJcicZ055Id.getCourtCode(), titaVo);
			if(tJcicZ055VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ055.setCreateDate(tJcicZ055VO.getCreateDate());
				tJcicZ055.setCreateEmpNo(tJcicZ055VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ055.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ055.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ055.setCreateDate(OrgJcicZ055.getCreateDate());
				tJcicZ055.setCreateEmpNo(OrgJcicZ055.getCreateEmpNo());
				try {
					tJcicZ055 = sJcicZ055Service.update2(tJcicZ055, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ055, tJcicZ055);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ055.setTranKey(TranKey);
				try {
					sJcicZ055Service.insert(tJcicZ055, titaVo);
				} catch (DBException e) {
					//E0005	新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "");
				}
				
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
	public void CheckCaseStatus(String CustId,String SubmitKey,String CaseStatus,int ClaimDate,String CourtCode,TitaVo titaVo) throws LogicException {
		/*檢核 CaseStatus 案件狀態*/
		//未曾報送過「更生程序開始(1)」前，不能報送「更生方案確認可確定(3)」。
		//未曾報送過「更生方案確認可確定(3)」，不能報送「更生方案履行完畢(4)」或「更生裁定免責確定(5)」。
		//1:更生程序開始
		//2:更生撤回
		//3:更生方案認可確定
		//4:更生方案履行完畢
		//5:更生裁定免責確定
		//6:更生調查程序
		if(CaseStatus!=null && CaseStatus.length()!=0) {
			/*
			 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
			 */
			this.index = titaVo.getReturnIndex();

			/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
			this.limit = 500;
			
			int intCaseStatus=parse.stringToInteger(CaseStatus);
			Vector<String> vStatus=new Vector<String>();

			Slice<JcicZ055> slJcicZ055=sJcicZ055Service.CheckCaseStatus(SubmitKey, CustId, ClaimDate, CourtCode, 0, Integer.MAX_VALUE, titaVo);
			List<JcicZ055> lJcicZ055=slJcicZ055 == null ? null : slJcicZ055.getContent();
			if(lJcicZ055!=null && lJcicZ055.size()!=0) {
				int lJcicZ055S=lJcicZ055.size();
				for(int i=0;i<lJcicZ055S;i++) {
					//判斷是否有過1,6程序
					String ThisStatus=lJcicZ055.get(i).getCaseStatus();
					if(!vStatus.contains(ThisStatus)) {
						vStatus.add(ThisStatus);
					}else {
						//有重複的資料
						//this.info("L8316 CheckCaseStatus SubmitKey=["+SubmitKey+"],CustId=["+CustId+"] 資料重複請查驗");
					}
				}
			}
			
			//案件狀態未曾報送1或6狀態，不能報送其他狀態，請重新選擇
			//案件狀態未曾報送3狀態更生方案認可確定前，不能報送4更生方案履行完畢或5更生裁定免責確定
//			int vStatusS=vStatus.size();
			if(intCaseStatus!=1 && intCaseStatus!=6) {
				if(!vStatus.contains("1") && !vStatus.contains("6")) {
					//案件狀態未曾報送1或6狀態，不能報送其他狀態，請重新選擇
					throw new LogicException(titaVo, "E8004","");
				}
			}
			if(intCaseStatus==4 || intCaseStatus==5) {
				if(!vStatus.contains("3")) {
					//案件狀態未曾報送3狀態更生方案認可確定前，不能報送4更生方案履行完畢或5更生裁定免責確定
					throw new LogicException(titaVo, "E8005","");
				}
			}
		}
		return;
	}
}