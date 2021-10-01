package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;
import com.st1.itx.db.domain.NegFinShareLog;
import com.st1.itx.db.domain.NegFinShareLogId;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegFinShareService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.NegFinShareLogService;

/**
 * Tita<br>
 * FunctionCode=X,2<br>
 * CustId=X,10<br>
 * DeferYMStart=9,5<br>
 * DeferYMEnd=9,5<br>
 * CaseKindCode=X,1<br>
 * CustLoanKind=X,1<br>
 * CustNo=X,7<br>
 * ApplDate=9,7<br>
 * DueAmt=9,14.2<br>
 * TotalPeriod=9,3<br>
 * IntRate=9,2.4<br>
 * FirstDueDate=9,7<br>
 * LastDueDate=9,7<br>
 * IsMainFin=X,1<br>
 * TotalContrAmt=9,14.2<br>
 * MainFinCode=X,3<br>
 * MainFinCodeName=X,16<br>
 * NegFinShareFinCode1=X,3<br>
 * NegFinShareContractAmt1=9,14.2<br>
 * NegFinShareAmtRatio1=9,2.4<br>
 * NegFinShareDueAmt1=9,14.2<br>
 * NegFinShareCancelDate1=9,7<br>
 * NegFinShareCancelAmt1=9,14.2<br>
 * NegFinShareFinCode2=X,3<br>
 * NegFinShareContractAmt2=9,14.2<br>
 * NegFinShareAmtRatio2=9,2.4<br>
 * NegFinShareDueAmt2=9,14.2<br>
 * NegFinShareCancelDate2=9,7<br>
 * NegFinShareCancelAmt2=9,14.2<br>
 * NegFinShareFinCode3=X,3<br>
 * NegFinShareContractAmt3=9,14.2<br>
 * NegFinShareAmtRatio3=9,2.4<br>
 * NegFinShareDueAmt3=9,14.2<br>
 * NegFinShareCancelDate3=9,7<br>
 * NegFinShareCancelAmt3=9,14.2<br>
 * NegFinShareFinCode4=X,3<br>
 * NegFinShareContractAmt4=9,14.2<br>
 * NegFinShareAmtRatio4=9,2.4<br>
 * NegFinShareDueAmt4=9,14.2<br>
 * NegFinShareCancelDate4=9,7<br>
 * NegFinShareCancelAmt4=9,14.2<br>
 * NegFinShareFinCode5=X,3<br>
 * NegFinShareContractAmt5=9,14.2<br>
 * NegFinShareAmtRatio5=9,2.4<br>
 * NegFinShareDueAmt5=9,14.2<br>
 * NegFinShareCancelDate5=9,7<br>
 * NegFinShareCancelAmt5=9,14.2<br>
 */

@Service("L5701")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5701 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegFinShareService sNegFinShareService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public NegFinShareLogService sNegFinShareLogService;

	@Autowired
	public DataLog iDataLog;	

	@Autowired
	NegCom negCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	private TitaVo titaVo;
	private int NegFinShareL = 30;// Detail有幾個

	private String NegFinShareFinCode[] = new String[NegFinShareL];// 債權機構
	private String NegFinShareContractAmt[] = new String[NegFinShareL];// 簽約金額
	private String NegFinShareAmtRatio[] = new String[NegFinShareL];// 債權比例%
	private String NegFinShareDueAmt[] = new String[NegFinShareL];// 期款
	private String NegFinShareCancelDate[] = new String[NegFinShareL];// 註銷日期
	private String NegFinShareCancelAmt[] = new String[NegFinShareL];// 註銷本金
	private boolean Code09 = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5701 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		this.info("L5701 Run");
		String FunctionCode = titaVo.getParam("FunctionCode").trim(); // 功能代碼

		String CustId = titaVo.getParam("CustId").trim(); // 身份證號
		String CustNo = titaVo.getParam("CustNo").trim(); // 戶號-MainKey
		String CaseSeq = titaVo.getParam("CaseSeq").trim(); // 案件序號-MainKey
		String ChgCondDate = titaVo.getParam("ChgCondDate").trim(); // 申請變更還款條件日
		String Status = titaVo.getParam("Status").trim(); // 債權戶況

		NegMainId NegMainId = new NegMainId();

		int IntCaseSeq = 1;
		if (CaseSeq != null && CaseSeq.length() != 0) {
			IntCaseSeq = parse.stringToInteger(CaseSeq);
		} else {
			if (("01").equals(FunctionCode)) {

			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
		}
		int intCustNo = parse.stringToInteger(CustNo);

		// 戶號不可為0
		if (intCustNo == 0) {
			// 取個CustNo
			intCustNo = negCom.getNewCustNo(CustId, titaVo);
			CustNo = String.valueOf(intCustNo);
			if (intCustNo != 0) {

			} else {
				throw new LogicException(titaVo, "E5005", "");
			}
		}
		totaVo.putParam("CustNo", CustNo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		checkstatus(intCustNo , FunctionCode , IntCaseSeq , Status, titaVo);	//戶況檢核

		NegMainId.setCustNo(intCustNo);
		NegMainId.setCaseSeq(IntCaseSeq);

		NegMain InputNegMain = InputNegMain(NegMainId);// 畫面上的資料
		NegMain NegMainVO = sNegMainService.findById(NegMainId);
		if (NegMainVO != null) {//修改內容使用InputNegMain時須補畫面上與程式內沒有更新的欄位
			if (("02").equals(FunctionCode) || ("10").equals(FunctionCode)) {
				InputNegMain.setAccuTempAmt(NegMainVO.getAccuTempAmt());
				InputNegMain.setAccuOverAmt(NegMainVO.getAccuOverAmt());
				InputNegMain.setAccuDueAmt(NegMainVO.getAccuDueAmt());
				InputNegMain.setAccuSklShareAmt(NegMainVO.getAccuSklShareAmt());
				InputNegMain.setRepaidPeriod(NegMainVO.getRepaidPeriod());
				InputNegMain.setNextPayDate(NegMainVO.getNextPayDate());
				InputNegMain.setPayIntDate(NegMainVO.getPayIntDate());
				InputNegMain.setRepayPrincipal(NegMainVO.getRepayPrincipal());
				InputNegMain.setRepayInterest(NegMainVO.getRepayInterest());
				InputNegMain.setStatusDate(NegMainVO.getStatusDate());

				InputNegMain.setThisAcDate(NegMainVO.getThisAcDate());
				InputNegMain.setThisTitaTlrNo(NegMainVO.getThisTitaTlrNo());
				InputNegMain.setThisTitaTxtNo(NegMainVO.getThisTitaTxtNo());
				InputNegMain.setLastAcDate(NegMainVO.getLastAcDate());
				InputNegMain.setLastTitaTlrNo(NegMainVO.getLastTitaTlrNo());
				InputNegMain.setLastTitaTxtNo(NegMainVO.getLastTitaTxtNo());
				InputNegMain.setCreateDate(NegMainVO.getCreateDate());
				InputNegMain.setCreateEmpNo(NegMainVO.getCreateEmpNo());
				InputNegMain.setLastUpdate(NegMainVO.getLastUpdate());
				InputNegMain.setLastUpdateEmpNo(NegMainVO.getLastUpdateEmpNo());
			}
		}
		
		switch (FunctionCode) {
		case "01":
			// 新增
			// 找出最大的CaseSeq
			// NegMain TempNegMainVO=new NegMain();
			IntCaseSeq = MaxIntCaseSeq(CustNo);
			NegMainId.setCaseSeq(IntCaseSeq);
			UpdInsertNegMain(NegMainId, InputNegMain, FunctionCode);
			InsertShareLog(intCustNo, IntCaseSeq, FunctionCode);// 新增ShareLog
			break;
		case "02":
			// 修改
			UpdInsertNegMain(NegMainId, InputNegMain, FunctionCode);
			break;
		case "03":
			// 毀諾
			// InputNegMain.setStatus("2");
			if (NegMainVO != null) {
				int Today = (parse.stringToInteger(titaVo.getCalDy()) + 19110000) / 100 ;// 日曆日-轉西元年月
				if ( NegMainVO.getDeferYMStart() != 0) {		//毀諾日期檢查
					if ( Today >= NegMainVO.getDeferYMStart() && Today <= NegMainVO.getDeferYMEnd() ) {		
						throw new LogicException(titaVo, "E0015", "該案件尚在延期繳款期間，不可設定毀諾");
					}
				}
				NegMainVO.setStatus("2");
				UpdInsertNegMain(NegMainId, NegMainVO, FunctionCode);
			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
			break;
		case "04":
			// 刪除
			// 未還過款就可以刪除
			// 已還過款就不可刪除
			Slice<NegTrans> slNegTrans = sNegTransService.custNoEq(intCustNo, this.index, this.limit, titaVo);
			List<NegTrans> lNegTrans = slNegTrans == null ? null : slNegTrans.getContent();

			int Delete = 0;
			if (lNegTrans != null && lNegTrans.size() != 0) {
				for (NegTrans NegTransVO : lNegTrans) {
					if (NegTransVO.getCaseSeq() == IntCaseSeq) {
						Delete = 1;
						break;
					}
				}
				// 不可刪除

			}
			if (Delete == 0) {
				// 可刪除
				try {
					sNegMainService.holdById(NegMainId);
					sNegMainService.delete(NegMainVO);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
				DelNegFinShare(intCustNo, IntCaseSeq);
				DelNegFinShareLog(intCustNo, IntCaseSeq);
			} else {
				throw new LogicException(titaVo, "E0015", "債務協商交易檔已有交易資料不可刪除");
			}

			break;
		case "05":
			// 查詢
			break;
		case "06":
			// 註銷
			//計算總本金餘額
			if (NegMainVO != null) {
				UpdInsertNegMain(NegMainId, NegMainVO, FunctionCode);
			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
			break;
		case "07":
			// 設定毀諾
			if (NegMainVO != null) {
				NegMainVO.setStatus("2");
				UpdInsertNegMain(NegMainId, NegMainVO, FunctionCode);
			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
			break;
		case "08":
			// 取消毀諾
			// InputNegMain.setStatus("0");
			if (NegMainVO != null) {
				NegMainVO.setStatus("0");
				UpdInsertNegMain(NegMainId, NegMainVO, FunctionCode);
			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
			break;
		case "09":
			// 變更還款條件
			// 舊資料改為(已變更+申請變更還款條件日)
			// 新資料CaseSeq+1
			int intChgCondDate = parse.stringToInteger(ChgCondDate);
			if (NegMainVO != null) {
				NegMainVO.setStatus("1");
				NegMainVO.setChgCondDate(intChgCondDate); // 申請變更還款條件日
				UpdInsertNegMain(NegMainId, NegMainVO, FunctionCode);
			} else {
				throw new LogicException(titaVo, "E0006", "案件序號- 不可為空值");
			}
			IntCaseSeq = MaxIntCaseSeq(CustNo);
			NegMainId.setCaseSeq(IntCaseSeq);
			UpdInsertNegMain(NegMainId, InputNegMain, FunctionCode);
			break;
		case "10":
			// 喘息期
			// 喘息期最多六個月
			//if (NegMainVO.getDeferYMStart() > 0) {
			//	int deferYMStart = InputNegMain.getDeferYMStart() * 100 + 1;
			//	int deferYMEnd = InputNegMain.getDeferYMEnd() * 100 + 1;
			//	int DeferYM = negCom.DiffMonth(1 , deferYMStart , deferYMEnd ) + 1 ;// 月份差異
			//	if (DeferYM > 6) {
			//		throw new LogicException(titaVo, "E0015", "喘息期超過六個月");
			//	}
			//}
			UpdInsertNegMain(NegMainId, InputNegMain, FunctionCode);
			break;
		case "11":
			// 二階段新增
			// NegMain.setTwoStepCode("Y");
			// 直接寫入新的NEGMAIN
			// 檢查舊的是否已經結案
			// 若未結案 新的NEGMAIN-Status為未生效
			// 已結案則是正常
			if ( ("N").equals(NegMainVO.getTwoStepCode()) ) {
				throw new LogicException(titaVo, "E0015", "二階段註記為Ｎ，不可執行二階段新增");
			}
			if ( ("1").equals(NegMainVO.getCaseKindCode()) ) {
				throw new LogicException(titaVo, "E0015", "案件種類為債協，不可執行二階段新增");
			}
			
			IntCaseSeq = MaxIntCaseSeq(CustNo);
			NegMainId.setCaseSeq(IntCaseSeq);
			if (NegMainVO.getStatus().equals("3")) {
				// 已結案
				InputNegMain.setStatus("0");
			} else {
				// 未生效
				InputNegMain.setStatus("4");
			}
			UpdInsertNegMain(NegMainId, InputNegMain, FunctionCode);
			break;
		default:

		}
		if (("02").equals(FunctionCode) || ("06").equals(FunctionCode) || ("09").equals(FunctionCode) || ("11").equals(FunctionCode)) {
			UpdateShareLog(intCustNo, IntCaseSeq, FunctionCode);// 修改ShareLog
		}

		if (!("04").equals(FunctionCode)) {
			DelNegFinShare(intCustNo, IntCaseSeq);
			UpdNegFinShare(intCustNo, IntCaseSeq);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	public int MaxIntCaseSeq(String CustNo) throws LogicException {
		// 取號CustNo
		int IntCaseSeq = 0;
		Slice<NegMain> slNegMain = sNegMainService.custNoEq(parse.stringToInteger(CustNo), this.index, this.limit, titaVo);
		List<NegMain> lNegMain = slNegMain == null ? null : slNegMain.getContent();

		if (lNegMain != null && lNegMain.size() != 0) {
			for (NegMain lNegMainVO : lNegMain) {
				int ThisCaseSeq = lNegMainVO.getCaseSeq();
				if (ThisCaseSeq > IntCaseSeq) {
					IntCaseSeq = ThisCaseSeq;
				}
			}
		}
		return IntCaseSeq + 1;
	}

	public void checkstatus(int custno ,String functioncode,int caseseq,String status, TitaVo titaVo) throws LogicException {
		// 一個戶號只可有一個戶況為0正常
		Slice<NegMain> slNegMain = sNegMainService.custNoEq(custno, this.index, this.limit, titaVo);
		List<NegMain> lNegMain = slNegMain == null ? null : slNegMain.getContent();

		if (lNegMain == null) {
			return;
		} 

		NegMain tNegMain = new NegMain();
		for (int i = 0; i < lNegMain.size(); i++) {
			tNegMain = lNegMain.get(i);
			if ( !("0").equals(tNegMain.getStatus()) ) {	//戶況非0正常,則不需檢查
				continue;
			}	
			if (("01").equals(functioncode) || ("08").equals(functioncode)) { // 新增,取消毀諾
				throw new LogicException(titaVo, "E0015", "尚有正常繳款的債權資料");
			}
			if (("02").equals(functioncode) && ("0").equals(status)) { // 修改
				if (tNegMain.getCaseSeq() != caseseq) {
					throw new LogicException(titaVo, "E0015", "尚有正常繳款的債權資料");
				}
			}
			
		}
	}
	
	
	public NegMain InputNegMain(NegMainId NegMainIdVO) throws LogicException {
		int DeferYMStart = parse.stringToInteger(titaVo.getParam("DeferYMStart").trim()); // 延期繳款年月(起)
		int DeferYMEnd = parse.stringToInteger(titaVo.getParam("DeferYMEnd").trim()); // 延期繳款年月(訖)
		String CaseKindCode = titaVo.getParam("CaseKindCode").trim(); // 案件種類
		String Status = titaVo.getParam("Status").trim(); // 戶況
		String CustLoanKind = titaVo.getParam("CustLoanKind").trim(); // 債權戶別
		String ApplDate = titaVo.getParam("ApplDate").trim(); // 協商申請日
		String DueAmt = titaVo.getParam("DueAmt").trim(); // 月付金
		String TotalPeriod = titaVo.getParam("TotalPeriod").trim(); // 期數
		String IntRate = titaVo.getParam("IntRate").trim(); // 計息條件
		String FirstDueDate = titaVo.getParam("FirstDueDate").trim(); // 首次應繳日
		String LastDueDate = titaVo.getParam("LastDueDate").trim(); // 還款結束日
		String IsMainFin = titaVo.getParam("IsMainFin").trim(); // 最大債權 Y N
		String TotalContrAmt = titaVo.getParam("TotalContrAmt").trim(); // (新壽)簽約總金額
		String MainFinCode = titaVo.getParam("MainFinCode").trim(); // 最大債權機構
		String TwoStepCode = titaVo.getParam("TwoStepCode").trim(); // 二階段註記
		String ChgCondDate = titaVo.getParam("ChgCondDate").trim(); // 申請變更還款條件日
		String PayerCustNo = titaVo.getParam("PayerCustNo").trim(); // 付款人戶號
		String CourCode = titaVo.getParam("CourCode").trim(); // 受理調解機構代號

		NegMain InputNegMain = new NegMain();

		InputNegMain.setNegMainId(NegMainIdVO);
		if (DeferYMStart == 0) {
			InputNegMain.setDeferYMStart(0);// 延期繳款年月(parse.stringToInteger(起)
			InputNegMain.setDeferYMEnd(0);// 延期繳款年月(parse.stringToInteger(訖)
		} else {
			InputNegMain.setDeferYMStart(DeferYMStart + 191100);// 延期繳款年月(parse.stringToInteger(起)
			InputNegMain.setDeferYMEnd(DeferYMEnd + 191100);// 延期繳款年月(parse.stringToInteger(訖)
		}


		InputNegMain.setCaseKindCode(CaseKindCode);// 案件種類
		InputNegMain.setStatus(Status);// 戶況
		InputNegMain.setCustLoanKind(CustLoanKind);// 債權戶別
		InputNegMain.setCustNo(NegMainIdVO.getCustNo());// 戶號-MainKey
		InputNegMain.setCaseSeq(NegMainIdVO.getCaseSeq());// 案件序號-MainKey
		InputNegMain.setApplDate(parse.stringToInteger(ApplDate));// 協商申請日
		InputNegMain.setDueAmt(parse.stringToBigDecimal(DueAmt));// 月付金
		InputNegMain.setTotalPeriod(parse.stringToInteger(TotalPeriod));// 期數
		InputNegMain.setIntRate(parse.stringToBigDecimal(IntRate));// 計息條件
		InputNegMain.setFirstDueDate(parse.stringToInteger(FirstDueDate));// 首次應繳日
		InputNegMain.setLastDueDate(parse.stringToInteger(LastDueDate));// 還款結束日
		InputNegMain.setIsMainFin(IsMainFin);// 最大債權 Y N
		InputNegMain.setTotalContrAmt(parse.stringToBigDecimal(TotalContrAmt));// (parse.stringToInteger(新壽)簽約總金額
		InputNegMain.setMainFinCode(MainFinCode);// 最大債權機構
		InputNegMain.setTwoStepCode(TwoStepCode);// 二階段註記
		InputNegMain.setPayerCustNo(parse.stringToInteger(PayerCustNo));// 付款人戶號
		InputNegMain.setChgCondDate(parse.stringToInteger(ChgCondDate));// 申請變更還款條件日
		InputNegMain.setCourCode(CourCode);// 受理調解機構代號

		return InputNegMain;
	}

	public void UpdInsertNegMain(NegMainId NegMainIdVO, NegMain InputNegMain, String FunctionCode) throws LogicException {
		if (NegMainIdVO.getCaseSeq() == 0) {
			throw new LogicException(titaVo, "", "發生未預期的錯誤");
		}
		InputNegMain.setNegMainId(NegMainIdVO);
		NegMain NegMainVO = sNegMainService.findById(NegMainIdVO);
		
		BigDecimal cPrincipalBal = BigDecimal.ZERO;
		
		if(!Code09 && ("09").equals(FunctionCode)) { //變更還款條件第一次只更新戶況,所以總本金餘額為原來的
			Code09 =true;
			
		} else {
			if(("01").equals(FunctionCode) || ("09").equals(FunctionCode) || ("11").equals(FunctionCode)) {
				cPrincipalBal = InputNegMain.getTotalContrAmt();
			} else {
				cPrincipalBal = NegMainVO.getPrincipalBal();
			}
			
			
			for (int i = 0; i < NegFinShareL; i++) {
				int Row = i + 1;
				NegFinShareCancelAmt[i] = titaVo.getParam("NegFinShareCancelAmt" + Row + "").trim(); // 註銷本金
				
				if(NegFinShareCancelAmt[i]!=null && NegFinShareCancelAmt[i].length()>0) {
					cPrincipalBal = cPrincipalBal.subtract(parse.stringToBigDecimal(NegFinShareCancelAmt[i]));
				}
			}
			InputNegMain.setPrincipalBal(cPrincipalBal);// 總本金餘額=簽約總金額-註銷金額
		}
		if (InputNegMain.getNextPayDate() == 0) {
			// 下次應繳日
			InputNegMain.setNextPayDate(InputNegMain.getFirstDueDate());
		}

		
		if (NegMainVO != null) {
			// UPDATE
			NegMain sNegMsain = sNegMainService.holdById(NegMainVO.getNegMainId());

			if (InputNegMain.getDeferYMStart() > 0) {// 設定喘息期一併異動下次繳款日與還款結束日
				if (sNegMsain.getDeferYMStart() != InputNegMain.getDeferYMStart()
						|| sNegMsain.getDeferYMEnd() != InputNegMain.getDeferYMEnd()) {
					int daystart = InputNegMain.getDeferYMStart() * 100 - 19110000 + 01;
					int dayend = InputNegMain.getDeferYMEnd() * 100 - 19110000 + 01;
					int period = negCom.DiffMonth(1, daystart, dayend) + 1;
					int odaystart = sNegMsain.getDeferYMStart() ;
					int odayend = sNegMsain.getDeferYMEnd();
					int operiod = 0;
					if (odaystart > 0) {
						odaystart = odaystart * 100 - 19110000 + 01;
						odayend = odayend * 100 - 19110000 + 01;
						operiod = negCom.DiffMonth(1, odaystart, odayend) + 1;
					}
					if (daystart <= sNegMsain.getPayIntDate() ) {
						throw new LogicException(titaVo, "E0015", "延期繳款年月區間設定有誤,繳款迄日="+sNegMsain.getPayIntDate());
					}

					if (daystart <= odayend) {// 已設定-修改區間
						if (sNegMsain.getNextPayDate() > odayend) {// 已異動下次應繳日+還款結束日,只可異動-延期繳款[迄]
							if (daystart != odaystart) {
								throw new LogicException(titaVo, "E0015", "下次繳款日與還款結束日已異動,只可修改延期繳款[迄]");
							} else {
								InputNegMain.setNextPayDate(
										negCom.getRepayDate(sNegMsain.getNextPayDate(), period - operiod, titaVo));
								InputNegMain.setLastDueDate(
										negCom.getRepayDate(sNegMsain.getLastDueDate(), period - operiod, titaVo));
							}
						} else {// 未異動下次應繳日+還款結束日=>下次繳款日在喘息期區間內才異動
							if (InputNegMain.getNextPayDate() >= daystart && InputNegMain.getNextPayDate() <= dayend) {
								InputNegMain.setNextPayDate(
										negCom.getRepayDate(InputNegMain.getNextPayDate(), period, titaVo));
								InputNegMain.setLastDueDate(
										negCom.getRepayDate(InputNegMain.getLastDueDate(), period, titaVo));
							}
						}
					} else {// 新增設定區間- 下次繳款日在喘息期區間內才異動
						if (InputNegMain.getNextPayDate() >= daystart && InputNegMain.getNextPayDate() <= dayend) {
							InputNegMain
									.setNextPayDate(negCom.getRepayDate(InputNegMain.getNextPayDate(), period, titaVo));
							InputNegMain
									.setLastDueDate(negCom.getRepayDate(InputNegMain.getLastDueDate(), period, titaVo));
						}
					}
				}
			}
			
			NegMain beforeNegMain = (NegMain) iDataLog.clone(sNegMsain);
			try {
				sNegMsain = sNegMainService.update(InputNegMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", e.getErrorMsg());
			}
			iDataLog.setEnv(titaVo, beforeNegMain, sNegMsain);
			iDataLog.exec();
		} else {
			// INSERT
			if(("01").equals(FunctionCode)) {//新增時繳息迄日=協商申請日
				InputNegMain.setPayIntDate(InputNegMain.getApplDate());
			}
			if(("09").equals(FunctionCode)) {//變更還款時繳息迄日=申請變更還款條件日
				InputNegMain.setPayIntDate(InputNegMain.getChgCondDate());
				InputNegMain.setChgCondDate(0);
			}
			if(("11").equals(FunctionCode)) {//二階段新增時繳息迄日=首次繳款日前一個月
				InputNegMain.setPayIntDate(negCom.getRepayDate(InputNegMain.getFirstDueDate(), -1, titaVo));
			}
			
			try {
				this.info("InputNegMain==" + InputNegMain);
				sNegMainService.insert(InputNegMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}
		}
	}

	public void DelNegFinShare(int intCustNo, int IntCaseSeq) throws LogicException {
		// 全部清掉 NegFinShare
		Slice<NegFinShare> slNegFinShare = sNegFinShareService.findFinCodeAll(intCustNo, IntCaseSeq, this.index, this.limit, titaVo);
		List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();

		if (lNegFinShare != null && lNegFinShare.size() != 0) {
			try {
				sNegFinShareService.deleteAll(lNegFinShare);
			} catch (DBException e) {
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}

	public void DelNegFinShareLog(int intCustNo, int IntCaseSeq) throws LogicException {
		// 全部清掉 NegFinShareLog
		Slice<NegFinShareLog> slNegFinShareLog = sNegFinShareLogService.findFinCodeAll(intCustNo, IntCaseSeq, this.index, this.limit, titaVo);
		List<NegFinShareLog> lNegFinShareLog = slNegFinShareLog == null ? null : slNegFinShareLog.getContent();

		if (lNegFinShareLog != null && lNegFinShareLog.size() != 0) {
			try {
				sNegFinShareLogService.deleteAll(lNegFinShareLog);
			} catch (DBException e) {
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
		}
	}

	public void UpdNegFinShare(int intCustNo, int IntCaseSeq) throws LogicException {
		// NegFinShare
		for (int i = 0; i < NegFinShareL; i++) {
			int Row = i + 1;
			NegFinShareFinCode[i] = titaVo.getParam("NegFinShareFinCode" + Row + "").trim(); // 債權機構
			NegFinShareContractAmt[i] = titaVo.getParam("NegFinShareContractAmt" + Row + "").trim(); // 簽約金額
			NegFinShareAmtRatio[i] = titaVo.getParam("NegFinShareAmtRatio" + Row + "").trim(); // 債權比例%
			NegFinShareDueAmt[i] = titaVo.getParam("NegFinShareDueAmt" + Row + "").trim(); // 期款
			NegFinShareCancelDate[i] = titaVo.getParam("NegFinShareCancelDate" + Row + "").trim(); // 註銷日期
			NegFinShareCancelAmt[i] = titaVo.getParam("NegFinShareCancelAmt" + Row + "").trim(); // 註銷本金

			if (NegFinShareFinCode[i] != null && NegFinShareFinCode[i].length() != 0) {
				NegFinShareId NegFinShareId = new NegFinShareId();
				NegFinShare NegFinShare = new NegFinShare();
				// 註銷日期有值則跳過
				if (Integer.parseInt(NegFinShareCancelDate[i]) != 0) {
					this.info("NegFinShareCancelDate" + i + " not null");
					continue;
				}
				NegFinShareId.setCaseSeq(IntCaseSeq);
				NegFinShareId.setCustNo(intCustNo);
				NegFinShareId.setFinCode(NegFinShareFinCode[i]);

				NegFinShare.setNegFinShareId(NegFinShareId);
				NegFinShare.setContractAmt(parse.stringToBigDecimal(NegFinShareContractAmt[i]));
				NegFinShare.setAmtRatio(parse.stringToBigDecimal(NegFinShareAmtRatio[i]));
				NegFinShare.setDueAmt(parse.stringToBigDecimal(NegFinShareDueAmt[i]));
				NegFinShare.setCancelDate(parse.stringToInteger(NegFinShareCancelDate[i]));
				NegFinShare.setCancelAmt(parse.stringToBigDecimal(NegFinShareCancelAmt[i]));

				NegFinShare NegFinShareVO = sNegFinShareService.findById(NegFinShareId);
				if (NegFinShareVO != null) {
					// UPDATE
					try {
						sNegFinShareService.holdById(NegFinShareVO.getNegFinShareId());
						NegFinShare.setCreateDate(NegFinShareVO.getCreateDate());
						NegFinShare.setCreateEmpNo(NegFinShareVO.getCreateEmpNo());
						sNegFinShareService.update(NegFinShare);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				} else {
					// throw new LogicException(titaVo, "XXXXX", "查無資料");
					// INSERT
					try {
						sNegFinShareService.insert(NegFinShare);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());
					}
				}
			} else {
				break;
			}

		}
	}

	public void InsertShareLog(int mCustNo, int mCaseSeq, String tFunctionCode) throws LogicException {

		for (int i = 0; i < NegFinShareL; i++) {

			int Row = i + 1;
			NegFinShareFinCode[i] = titaVo.getParam("NegFinShareFinCode" + Row + "").trim(); // 債權機構
			NegFinShareContractAmt[i] = titaVo.getParam("NegFinShareContractAmt" + Row + "").trim(); // 簽約金額
			NegFinShareAmtRatio[i] = titaVo.getParam("NegFinShareAmtRatio" + Row + "").trim(); // 債權比例%
			NegFinShareDueAmt[i] = titaVo.getParam("NegFinShareDueAmt" + Row + "").trim(); // 期款
			NegFinShareCancelDate[i] = titaVo.getParam("NegFinShareCancelDate" + Row + "").trim(); // 註銷日期
			NegFinShareCancelAmt[i] = titaVo.getParam("NegFinShareCancelAmt" + Row + "").trim(); // 註銷本金

			if (NegFinShareFinCode[i] != null && NegFinShareFinCode[i].length() != 0) {
				NegFinShareLogId NegFinShareLogId = new NegFinShareLogId();
				NegFinShareLog NegFinShareLog = new NegFinShareLog();
				// 註銷日期有值就跳過
//				if (Integer.parseInt(NegFinShareCancelDate[i]) != 0) {
//					this.info("NegFinShareCancelDate" + i + " not null");
//					continue;
//				}

				NegFinShareLogId.setCustNo(mCustNo);
				NegFinShareLogId.setCaseSeq(mCaseSeq);
				NegFinShareLogId.setSeq(1);
				NegFinShareLogId.setFinCode(NegFinShareFinCode[i]);

				NegFinShareLog.setNegFinShareLogId(NegFinShareLogId);
				NegFinShareLog.setContractAmt(parse.stringToBigDecimal(NegFinShareContractAmt[i]));
				NegFinShareLog.setAmtRatio(parse.stringToBigDecimal(NegFinShareAmtRatio[i]));
				NegFinShareLog.setDueAmt(parse.stringToBigDecimal(NegFinShareDueAmt[i]));
				NegFinShareLog.setCancelDate(parse.stringToInteger(NegFinShareCancelDate[i]));
				NegFinShareLog.setCancelAmt(parse.stringToBigDecimal(NegFinShareCancelAmt[i]));

				try {
					sNegFinShareLogService.insert(NegFinShareLog);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg());

				}
			} else {
				break;
			}

		}
	}

	public void UpdateShareLog(int mCustNo, int mCaseSeq, String tFunctionCode) throws LogicException {

		ArrayList<String> InArraylist;
		InArraylist = new ArrayList<String>();
		// 輸入資料
		for (int i = 0; i < NegFinShareL; i++) {
			int Row = i + 1;
			NegFinShareFinCode[i] = titaVo.getParam("NegFinShareFinCode" + Row + "").trim(); // 債權機構
			NegFinShareContractAmt[i] = titaVo.getParam("NegFinShareContractAmt" + Row + "").trim(); // 簽約金額
			NegFinShareCancelDate[i] = titaVo.getParam("NegFinShareCancelDate" + Row + "").trim(); // 註銷日期
			NegFinShareCancelAmt[i] = titaVo.getParam("NegFinShareCancelAmt"+Row+"").trim();//註銷金額
			if (NegFinShareFinCode[i].length() != 0) {
				InArraylist.add(NegFinShareFinCode[i]);
				InArraylist.add(NegFinShareContractAmt[i]);
				InArraylist.add(NegFinShareCancelDate[i]);
				InArraylist.add(NegFinShareCancelAmt[i]);
			}
		}

		// 修改時抓最大歷程序號
		int newSeq = 0;
		int oldSeq = 1;
		Boolean bt = false;
		Slice<NegFinShareLog> sNegFinShareLog;

		sNegFinShareLog = sNegFinShareLogService.findFinCodeAll(mCustNo, mCaseSeq, this.index, this.limit, titaVo);
		List<NegFinShareLog> lNegFinShareLog = sNegFinShareLog == null ? null : sNegFinShareLog.getContent();
		if (lNegFinShareLog != null) {
			for (NegFinShareLog tNegFinShareLog : lNegFinShareLog) {
				newSeq = tNegFinShareLog.getSeq();
				if (newSeq > oldSeq) {
					oldSeq = newSeq;
				}
				this.info("newSeq==" + newSeq + ",oldSeq==" + oldSeq);
			}
		}

		// 抓NegFinShareLog內最新歷程序號資料資料
		Slice<NegFinShareLog> nNegFinShareLog;
		nNegFinShareLog = sNegFinShareLogService.findNewSeq(mCustNo, mCaseSeq, oldSeq, this.index, this.limit, titaVo);
		List<NegFinShareLog> bNegFinShareLog = nNegFinShareLog == null ? null : nNegFinShareLog.getContent();

		ArrayList<String> NewArraylist;
		NewArraylist = new ArrayList<String>();
		// DB最大歷程序號資料
		if (bNegFinShareLog != null) {
			for (NegFinShareLog newNegFinShareLog : bNegFinShareLog) {

				NewArraylist.add(newNegFinShareLog.getFinCode());
				NewArraylist.add(newNegFinShareLog.getContractAmt().toString());
				if (newNegFinShareLog.getCancelDate() != 0) {
					NewArraylist.add(String.valueOf(newNegFinShareLog.getCancelDate()));
				} else {
					NewArraylist.add("0000000");
				}
				NewArraylist.add(newNegFinShareLog.getCancelAmt().toString());
			}
			// 自動排序
			Collections.sort(InArraylist);
			Collections.sort(NewArraylist);

			this.info("InArraylist after==" + InArraylist);
			this.info("NewArraylist after==" + NewArraylist);
			this.info("InArraylist size==" + InArraylist.size() + ",NewArraylist size==" + NewArraylist.size());
			// 資料長度不同直接新增
			if (InArraylist.size() == NewArraylist.size()) {
				// 比對資料是否相同
				if (InArraylist.equals(NewArraylist)) {
					this.info("bt in");
					// 資料皆相同
					bt = true;
				}
			}

		}

		// 資料不同則新增
		if (!bt) {
			for (int i = 0; i < NegFinShareL; i++) {

				int Row = i + 1;
				NegFinShareFinCode[i] = titaVo.getParam("NegFinShareFinCode" + Row + "").trim(); // 債權機構
				NegFinShareContractAmt[i] = titaVo.getParam("NegFinShareContractAmt" + Row + "").trim(); // 簽約金額
				NegFinShareAmtRatio[i] = titaVo.getParam("NegFinShareAmtRatio" + Row + "").trim(); // 債權比例%
				NegFinShareDueAmt[i] = titaVo.getParam("NegFinShareDueAmt" + Row + "").trim(); // 期款
				NegFinShareCancelDate[i] = titaVo.getParam("NegFinShareCancelDate" + Row + "").trim(); // 註銷日期
				NegFinShareCancelAmt[i] = titaVo.getParam("NegFinShareCancelAmt" + Row + "").trim(); // 註銷本金

				if (NegFinShareFinCode[i] != null && NegFinShareFinCode[i].length() != 0) {
					NegFinShareLogId NegFinShareLogId = new NegFinShareLogId();
					NegFinShareLog NegFinShareLog = new NegFinShareLog();
					this.info("NegFinShareCancelDate" + i + "==" + NegFinShareCancelDate[i]);
					// 註銷日期有值則跳過
//					if (Integer.parseInt(NegFinShareCancelDate[i]) != 0) {
//						this.info("NegFinShareCancelDate" + i + " not null");
//						continue;
//					}
					NegFinShareLogId.setCustNo(mCustNo);
					NegFinShareLogId.setCaseSeq(mCaseSeq);
					if(("09").equals(tFunctionCode) ||("11").equals(tFunctionCode)) {
						NegFinShareLogId.setSeq(oldSeq);
					} else {
						NegFinShareLogId.setSeq(oldSeq+1);
					}
					
					NegFinShareLogId.setFinCode(NegFinShareFinCode[i]);
					NegFinShareLog.setNegFinShareLogId(NegFinShareLogId);
					NegFinShareLog.setContractAmt(parse.stringToBigDecimal(NegFinShareContractAmt[i]));
					NegFinShareLog.setAmtRatio(parse.stringToBigDecimal(NegFinShareAmtRatio[i]));
					NegFinShareLog.setDueAmt(parse.stringToBigDecimal(NegFinShareDueAmt[i]));
					NegFinShareLog.setCancelDate(parse.stringToInteger(NegFinShareCancelDate[i]));
					NegFinShareLog.setCancelAmt(parse.stringToBigDecimal(NegFinShareCancelAmt[i]));

					try {
						sNegFinShareLogService.insert(NegFinShareLog,titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg());

					}
				} else {
					break;
				}

			}

		}
	}
}