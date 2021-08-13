package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.domain.JcicZ056Id;

/*DB服務*/
import com.st1.itx.db.service.JcicZ056Service;

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
* Approve=X,1<br>
* OutstandAmt=9,9<br>
* ClaimStatus1=X,1<br>
* SaveDate=9,7<br>
* ClaimStatus2=X,1<br>
* SaveEndDate=9,7<br>
* SubAmt=9,9<br>
* AdminName=X,20<br>
* OutJcicTxtDate=9,7<br>
*/

@Service("L8317")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8317 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8317.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ056Service sJcicZ056Service;
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
		this.info("Run L8317");
		this.info("active L8317 ");
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
		
		//int Today=dateUtil.getNowIntegerForBC();
		/* DB資料容器WD */
		//JcicMAaster
		JcicZ056 tJcicZ056 = new JcicZ056();
		JcicZ056Id tJcicZ056Id = new JcicZ056Id();
		tJcicZ056Id.setCustId(CustId);//債務人IDN
		tJcicZ056Id.setSubmitKey(SubmitKey);//報送單位代號
		tJcicZ056Id.setCaseStatus(CaseStatus);//案件狀態
		tJcicZ056Id.setClaimDate(Integer.parseInt(jcicCom.RocTurnDc(ClaimDate,0)));//裁定日期
		tJcicZ056Id.setCourtCode(CourtCode);//承審法院代碼

		this.info("L8317 tJcicZ056Id=["+tJcicZ056Id.toString()+"]");
		tJcicZ056.setJcicZ056Id(tJcicZ056Id);
		
		tJcicZ056.setTranKey(TranKey);//交易代號
		
		tJcicZ056.setYear(Integer.parseInt(jcicCom.RocTurnDc(Year,2)));//年度別
		tJcicZ056.setCourtDiv(titaVo.getParam("CourtDiv").trim());//法院承審股別
		tJcicZ056.setCourtCaseNo(titaVo.getParam("CourtCaseNo").trim());//法院案號
		tJcicZ056.setApprove(titaVo.getParam("Approve").trim());//法院裁定免責確定
		tJcicZ056.setOutstandAmt(parse.stringToInteger(titaVo.getParam("OutstandAmt").trim()));//原始債權金額
		tJcicZ056.setClaimStatus1(titaVo.getParam("ClaimStatus1").trim());//法院裁定保全處分
		tJcicZ056.setSaveDate(parse.stringToInteger(titaVo.getParam("SaveDate").trim()));//保全處分起始日
		tJcicZ056.setClaimStatus2(titaVo.getParam("ClaimStatus2").trim());//法院裁定撤銷保全處分
		tJcicZ056.setSaveEndDate(parse.stringToInteger(titaVo.getParam("SaveEndDate").trim()));//保全處分撤銷日
		tJcicZ056.setSubAmt(parse.stringToInteger(titaVo.getParam("SubAmt").trim()));//清算損失金額
		tJcicZ056.setAdminName(titaVo.getParam("AdminName").trim());//管理人姓名

		//OutJcicTxtDate 可以刪除不可異動
		if(jcicCom.JcicOutDateCanUpdByUser(titaVo)==true) {
			tJcicZ056.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
		}else {
			tJcicZ056.setOutJcicTxtDate(0);
		}
		JcicZ056 tJcicZ056VO=sJcicZ056Service.holdById(tJcicZ056Id, titaVo);
		JcicZ056 OrgJcicZ056 = null;
		if(tJcicZ056VO!=null) {
			OrgJcicZ056 = (JcicZ056) dataLog.clone(tJcicZ056VO);//資料異動前
		}

		this.info("tJcicZ056VO=["+tJcicZ056.toString()+"]");

		if((jcicCom.getDeleteFunctionCode()).equals(FunctionCd)) {
			boolean DeleteTF=jcicCom.DeleteLogic(titaVo,tJcicZ056VO,tJcicZ056VO.getOutJcicTxtDate());
			if(DeleteTF) {
				//刷主管卡後始可刪除
				// 交易需主管核可
				if(("A").equals(OrgJcicZ056.getTranKey())) {
					
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
					sJcicZ056Service.delete(tJcicZ056VO, titaVo);
				} catch (DBException e) {
					//E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "");
				}
			}
		}else {
			CheckCaseStatus(tJcicZ056Id.getCustId(), tJcicZ056Id.getSubmitKey(), tJcicZ056Id.getCaseStatus(), tJcicZ056Id.getClaimDate(), tJcicZ056Id.getCourtCode(), titaVo);
			if(tJcicZ056VO!=null ) {
				if (TranKey.equals("A")) {
					throw new LogicException(titaVo, "E0002", "");
				}
				//UPDATE
				//KeyValue
				tJcicZ056.setCreateDate(tJcicZ056VO.getCreateDate());
				tJcicZ056.setCreateEmpNo(tJcicZ056VO.getCreateEmpNo());
				if(OutJcicTxtDate!=null && OutJcicTxtDate.length()!=0) {
					if(Integer.parseInt(OutJcicTxtDate)==0) {
						tJcicZ056.setOutJcicTxtDate(0);
					}
				}else {
					tJcicZ056.setOutJcicTxtDate(Integer.parseInt(jcicCom.RocTurnDc(OutJcicTxtDate,0)));
				}

				tJcicZ056.setCreateDate(OrgJcicZ056.getCreateDate());
				tJcicZ056.setCreateEmpNo(OrgJcicZ056.getCreateEmpNo());
				try {
					tJcicZ056 = sJcicZ056Service.update2(tJcicZ056, titaVo);//資料異動後-1
					dataLog.setEnv(titaVo, OrgJcicZ056, tJcicZ056);//資料異動後-2
					dataLog.exec();//資料異動後-3
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "");
				}
			}else {
				//INSERT
				TranKey="A";
				tJcicZ056.setTranKey(TranKey);
				try {
					sJcicZ056Service.insert(tJcicZ056, titaVo);
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
		//未曾報送「清算程序開始(A)」前，不能報送，「清算程序終止(結)(B)」。
		//報送「清算撤銷免責確定(D)」之前，需曾報送「清算程序開始(A)」或「清算程序開始同時終止(C)」，且12欄原填報為Y。

		//A:清算程序開始
		//B:清算程序終止(結)
		//C:清算程序開始同時終止
		//D:清算撤消免責確定
		//E:清算調查程序
		//G:清算撤回
		//H:清算復權
		ClaimDate=Integer.parseInt(jcicCom.RocTurnDc(String.valueOf(ClaimDate),0));
		if(CaseStatus!=null && CaseStatus.length()!=0) {
			Vector<String> vStatus=new Vector<String>();
			Vector<String> vApprove=new Vector<String>();
			this.info("L8317 CheckCaseStatus SubmitKey=["+SubmitKey+"] CustId=["+CustId+"] ClaimDate=["+ClaimDate+"] CourtCode=["+CourtCode+"]");
			Slice<JcicZ056> slJcicZ056=sJcicZ056Service.CheckCaseStatus(SubmitKey, CustId, ClaimDate, CourtCode, 0, Integer.MAX_VALUE, titaVo);
			List<JcicZ056> lJcicZ056=slJcicZ056 == null ? null : slJcicZ056.getContent();
			if(lJcicZ056!=null && lJcicZ056.size()!=0) {
				int lJcicZ056S=lJcicZ056.size();
				for(int i=0;i<lJcicZ056S;i++) {
					String ThisStatus=lJcicZ056.get(i).getCaseStatus();
					if(!vStatus.contains(ThisStatus)) {
						vStatus.add(ThisStatus);
						this.info("L8316 vStatus ThisStatus=["+ThisStatus+"]");
					}else {
						//有重複的資料
						this.info("L8316 CheckCaseStatus SubmitKey=["+SubmitKey+"],CustId=["+CustId+"] 資料重複請查驗");
					}
					
					if(ThisStatus.equals("A") || ThisStatus.equals("C")) {
						String ThisApprove=lJcicZ056.get(i).getApprove();
						vApprove.add(ThisApprove);
					}
				}
			}
			
			//未曾報送「清算程序開始(A)」前，不能報送，「清算程序終止(結)(B)」。
			//報送「清算撤銷免責確定(D)」之前，需曾報送「清算程序開始(A)」或「清算程序開始同時終止(C)」，且12欄[法院裁定免責]原填報為Y。
			
			//案件狀態未曾報送A或E狀態，不能報送其他狀態，請重新選擇
			//案件狀態未曾報送A或C狀態，且法院裁定免責確定不為Y，不能報送D狀態，請重新選擇

			//E8006 案件狀態未曾報送A(清算程序開始)或E(清算調查程序)狀態，不能報送其他狀態，請重新選擇
//			if(!CaseStatus.equals("A") && !CaseStatus.equals("E")) {
//				if(!vStatus.contains("A") && !vStatus.contains("E")) {
//					throw new LogicException(titaVo, "E8006","");
//				}
//			}

			if(!CaseStatus.equals("A") && !CaseStatus.equals("C")) {
				if(!vStatus.contains("A") && !vStatus.contains("C")) {
					//案件狀態未曾報送A(清算程序開始)或C(清算程序開始同時終止)狀態，不能報送其他狀態，請重新選擇
					throw new LogicException(titaVo, "","案件狀態未曾報送A(清算程序開始)或C(清算程序開始同時終止)狀態，不能報送其他狀態，請重新選擇");
				}
			}
			
			//報送「清算撤銷免責確定(D)」之前，需曾報送「清算程序開始(A)」或「清算程序開始同時終止(C)」，且12欄原填報為Y。
			
			//E8007 案件狀態未曾報送A(清算程序開始)或C(清算程序開始同時終止)狀態，且法院裁定免責確定不為Y，不能報送D(清算撤消免責確定)狀態，請重新選擇
			if(CaseStatus.equals("D")) {
				if(!vStatus.contains("A") && !vStatus.contains("C")) {
					throw new LogicException(titaVo, "E8007","");
				}else {
					
				}
				if(!vApprove.contains("Y")) {
					throw new LogicException(titaVo, "E8007","");
				}
			}
		}
		
		
		return;
	}
}