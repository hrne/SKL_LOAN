package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.springjpa.cm.L597AServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
* AcDate=9,7<br>
* IsMainFin=9,1<br>
* SearchOption=X,2<br>
* SearchDetail=X,1<br>
* Export=X,1<br>
* IsBtn=X,1<br>
*/

@Service("L597A")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L597A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L597A.class);
	/* DB服務注入 */
	@Autowired
	public NegCom NegCom;
	
	@Autowired
	public NegTransService sNegTransService;
	
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	public L597AServiceImpl l597AServiceImpl;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L597A ");
		this.totaVo.init(titaVo);
		int AcDate=Integer.parseInt(titaVo.getParam("AcDate").trim()); //
		int SearchOption=Integer.parseInt(titaVo.getParam("SearchOption").trim()); //
		int SearchDetail=Integer.parseInt(titaVo.getParam("SearchDetail").trim()); //
		int Export=Integer.parseInt(titaVo.getParam("Export").trim()); //
		int IsBtn=Integer.parseInt(titaVo.getParam("IsBtn").trim()); //
		String TransTxKind = titaVo.getParam("TransTxKind").trim(); //
		
		int IsMainFin = 0;
		if("Y".equals(titaVo.getParam("IsMainFin").trim())) {
			IsMainFin = 1;
		} 
		
		Boolean selectfg = false;
		Boolean timesfg = false;
		if(!"".equals(TransTxKind)) {
			selectfg = true;
		}
		
		
		/*設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值*/
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		//this.limit=Integer.MAX_VALUE;//查全部
		this.limit=50;//查全部
		
		String sql="";
		List<String[]> Data=null;
		try {
			sql = l597AServiceImpl.FindL597A(titaVo, AcDate, IsMainFin, SearchOption, SearchDetail, Export, IsBtn,"");
		} catch (Exception e) {
			//E5003 組建SQL語法發生問題
			logger.info("L5051 ErrorForSql="+e);
			throw new LogicException(titaVo, "E5003","");
		}
		try {
			Data=l597AServiceImpl.FindL597A(l597AServiceImpl.FindData(this.index, this.limit, sql, titaVo, AcDate, IsMainFin, SearchOption, SearchDetail, Export, IsBtn),"L597A");
		} catch (Exception e) {
			//E5004 讀取DB時發生問題
			logger.info("L5051 ErrorForDB="+e);
			throw new LogicException(titaVo, "E5004","");
		}
		
		if(Data!=null && Data.size()!=0) {
			int Seq=0;
			for(String [] lData:Data) {
				for(int i=0;i<lData.length;i++) {
					String strValue=lData[i];
					if(strValue!=null) {
						
					}else {
						strValue="";
					}
					//occursList1.putParam(OccursName[i],strValue);
				}
				
				String TxKind = lData[6];
				String NewtransTxKind = "";
				
				String Status = "";
				String NewStatus = "";
				String CustLoanKind = "";
				String ApplDate = "";
				String MainDueAmt = "";
				String TotalPeriod = "";
				String IntRate = "";
				String RepaidPeriod = "";
				String NewRepaidPeriod = "";
				String MainFinCode = "";
				String OrgPrincipalBal = "";
				String NewPrincipalBal = "";
				String OrgAccuTempAmt = "";
				String NewAccuTempAmt = "";
				String OrgAccuOverAmt = "";
				String NewAccuOverAmt = "";
				String OrgAccuDueAmt = "";
				String NewAccuDueAmt = "";
				String OrgAccuSklShareAmt = "";
				String NewAccuSklShareAmt = "";
				String OrgNextPayDate = "";
				String NewNextPayDate = "";
				String OrgRepayPrincipal = "";
				String NewRepayPrincipal = "";
				String OrgRepayInterest = "";
				String NewRepayInterest = "";
				String TwoStepCode = "";
				String ChgCondDate = "";
				String PayIntDate = "";
				String NewPayIntDate = "";
				String OrgStatusDate = "";
				String NewStatusDate = "";
				String TransAcDate = "";
				String TransTitaTlrNo = "";
				String TransTitaTxtNo = "";
				String TransCustNo = "";
				String TransCaseSeq = "";
				String TransEntryDate = "";
				String TransTxStatus = "";
				String NewTransTxStatus = "";
				String TransTxAmt = "";
				String NewTransTxAmt = "";
				String TransPrincipalBal = "";
				String NewTransPrincipalBal = "";
				String TransReturnAmt = "";
				String NewTransReturnAmt = "";
				String TransSklShareAmt = "";
				String NewTransSklShareAmt = "";
				String TransApprAmt = "";
				String NewTransApprAmt = "";
				String TransExportDate = "";
				String TransExportAcDate = "";
				String TransTempRepayAmt = "";
				String NewTransTempRepayAmt = "";
				String TransOverRepayAmt = "";
				String NewTransOverRepayAmt = "";
				String TransPrincipalAmt = "";
				String NewTransPrincipalAmt = "";
				String TransInterestAmt = "";
				String NewTransInterestAmt = "";
				String TransOverAmt = "";
				String NewTransOverAmt = "";
				String TransIntStartDate = "";
				String NewTransIntStartDate = "";
				String TransIntEndDate = "";
				String NewTransIntEndDate = "";
				String TransRepayPeriod = "";
				String NewTransRepayPeriod = "";
				String TransShouldPayPeriod = "";
				String NewTransShouldPayPeriod = "";
				String TransDueAmt = "";
				String NewTransDueAmt = "";
				String TransRepayDate = "";
				String NewTransRepayDate = "";
				String TransOrgAccuOverAmt = "";
				String NewTransOrgAccuOverAmt = "";
				String TransAccuOverAmt = "";
				String NewTransAccuOverAmt = "";

				
					
				if(SearchOption == 2 || SearchOption == 3 || SearchOption == 12) {
				  NegTransId NegTransIdVO = new NegTransId();
				  NegTransIdVO.setAcDate(parse.stringToInteger(DcToRoc(lData[8])));
				  NegTransIdVO.setTitaTlrNo(lData[21]);
				  NegTransIdVO.setTitaTxtNo(parse.stringToInteger(lData[22]));
				
				  NegTrans NegTransVO = new NegTrans();
				  NegTransVO = sNegTransService.findById(NegTransIdVO);
				  if (NegTransVO != null) {
				    Map<String, String> Map = NegCom.trialNegtrans(NegTransVO, "0", TxKind, titaVo);
				    NewtransTxKind = Map.get("NewtransTxKind");//試算交易別   
				     this.info("Map = " + Map);
				     Status = Map.get("Status");
					 NewStatus = Map.get("NewStatus");
					 CustLoanKind = Map.get("CustLoanKind");
					 ApplDate = Map.get("ApplDate");
					 MainDueAmt = Map.get("MainDueAmt");
					 TotalPeriod = Map.get("TotalPeriod");
					 IntRate = Map.get("IntRate");
					 RepaidPeriod = Map.get("RepaidPeriod");
					 NewRepaidPeriod = Map.get("NewRepaidPeriod");
					 MainFinCode = Map.get("MainFinCode");
					 
					 OrgPrincipalBal = Map.get("OrgPrincipalBal");
					 NewPrincipalBal = Map.get("NewPrincipalBal");
					 OrgAccuTempAmt = Map.get("OrgAccuTempAmt");
					 NewAccuTempAmt = Map.get("NewAccuTempAmt");
					 OrgAccuOverAmt = Map.get("OrgAccuOverAmt");
					 NewAccuOverAmt = Map.get("NewAccuOverAmt");
					 OrgAccuDueAmt = Map.get("OrgAccuDueAmt");
					 NewAccuDueAmt = Map.get("NewAccuDueAmt");
					 OrgAccuSklShareAmt = Map.get("OrgAccuSklShareAmt");
					 NewAccuSklShareAmt = Map.get("NewAccuSklShareAmt");
					 OrgNextPayDate = Map.get("OrgNextPayDate");
					 NewNextPayDate = Map.get("NewNextPayDate");
					 OrgRepayPrincipal = Map.get("OrgRepayPrincipal");
					 NewRepayPrincipal = Map.get("NewRepayPrincipal");
					 OrgRepayInterest = Map.get("OrgRepayInterest");
					 NewRepayInterest = Map.get("NewRepayInterest");
					 
					 TwoStepCode = Map.get("TwoStepCode");
					 ChgCondDate = Map.get("ChgCondDate");
					 PayIntDate = Map.get("PayIntDate");
					 NewPayIntDate = Map.get("NewPayIntDate");
					 OrgStatusDate = Map.get("OrgStatusDate");
					 NewStatusDate = Map.get("NewStatusDate");
					 TransAcDate = Map.get("TransAcDate");
					 TransTitaTlrNo = Map.get("TransTitaTlrNo");
					 TransTitaTxtNo = Map.get("TransTitaTxtNo");
					 
					 TransCustNo = Map.get("TransCustNo");
					 TransCaseSeq = Map.get("TransCaseSeq");
					 TransEntryDate = Map.get("transEntryDate");
					 TransTxStatus = Map.get("TransTxStatus");
					 NewTransTxStatus = Map.get("NewtransTxStatus");
					 TransTxAmt = Map.get("transTxAmt");
					 
					 NewTransTxAmt = Map.get("NewtransTxAmt");
					 TransPrincipalBal = Map.get("TransPrincipalBal");
					 NewTransPrincipalBal = Map.get("NewTransPrincipalBal");
					 
					 TransReturnAmt = Map.get("TransReturnAmt");
					 NewTransReturnAmt = Map.get("NewTransReturnAmt");
					 TransSklShareAmt = Map.get("TransSklShareAmt");
					 NewTransSklShareAmt = Map.get("NewTransSklShareAmt");
					 TransApprAmt = Map.get("TransApprAmt");
					 NewTransApprAmt = Map.get("NewTransApprAmt");
					 TransExportDate = Map.get("TransExportDate");
					 TransExportAcDate = Map.get("TransExportAcDate");
					 TransTempRepayAmt = Map.get("TransTempRepayAmt");
					 NewTransTempRepayAmt = Map.get("NewTransTempRepayAmt");
					 TransOverRepayAmt = Map.get("TransOverRepayAmt");
					 NewTransOverRepayAmt = Map.get("NewTransOverRepayAmt");
					 TransPrincipalAmt = Map.get("TransPrincipalAmt");
					 NewTransPrincipalAmt = Map.get("NewTransPrincipalAmt");
					 
					 TransInterestAmt = Map.get("TransInterestAmt");
					 NewTransInterestAmt = Map.get("NewTransInterestAmt");
					 TransOverAmt = Map.get("TransOverAmt");
					 NewTransOverAmt = Map.get("NewTransOverAmt");
					 TransIntStartDate = Map.get("TransIntStartDate");
					 NewTransIntStartDate = Map.get("NewTransIntStartDate");
					 TransIntEndDate = Map.get("TransIntEndDate");
					 NewTransIntEndDate = Map.get("NewTransIntEndDate");
					 
					 TransRepayPeriod = Map.get("TransRepayPeriod");
					 NewTransRepayPeriod = Map.get("NewTransRepayPeriod");
					 TransShouldPayPeriod = Map.get("TransShouldPayPeriod");
					 NewTransShouldPayPeriod = Map.get("NewTransShouldPayPeriod");
					 TransDueAmt = Map.get("TransDueAmt");
					 NewTransDueAmt = Map.get("NewTransDueAmt");
					 TransRepayDate = Map.get("TransRepayDate");
					 NewTransRepayDate = Map.get("NewTransRepayDate");
					 TransOrgAccuOverAmt = Map.get("TransOrgAccuOverAmt");
					 NewTransOrgAccuOverAmt = Map.get("NewTransOrgAccuOverAmt");
					 TransAccuOverAmt = Map.get("TransAccuOverAmt");
					 NewTransAccuOverAmt = Map.get("NewTransAccuOverAmt");			    
				  }
				} // if
				
				OccursList occursList1 = new OccursList();
				if(selectfg) { // 試算交易別有值
					
					if(NewtransTxKind.equals(TransTxKind)) {
						Seq++;
						occursList1.putParam("OOSeq",Seq);//
						occursList1.putParam("OOCustId",lData[2]);//身分證號
						occursList1.putParam("OOCaseSeq",lData[3]);//案件序號
						occursList1.putParam("OOCustNo",lData[4]);//戶號
						occursList1.putParam("OOCustName",lData[5]);//戶名
						occursList1.putParam("OOTxSts",TxKind);//交易別
						occursList1.putParam("OORemark",lData[7]);//備註
						occursList1.putParam("OOAcctDate",DcToRoc(lData[8]));//會計日
						occursList1.putParam("OOEntryDate",DcToRoc(lData[9]));//入帳日
						occursList1.putParam("OORepayDate",DcToRoc(lData[10]));//入帳還款日
						occursList1.putParam("OOTmpAmt",lData[11]);//暫收金額
						occursList1.putParam("OOOverPayAmt",lData[12]);//溢繳款
						occursList1.putParam("OOPayPeriod",lData[13]);//繳期數
						occursList1.putParam("OOPayAmt",lData[14]);//還款金額
						occursList1.putParam("OORevivPeriod",lData[15]);//應還期數
						occursList1.putParam("OORevivAmt",lData[16]);//應還金額
						occursList1.putParam("OOAcumTmpAmt",lData[17]);//累溢短收
						occursList1.putParam("OOSklShareAmt",lData[18]);//新壽攤分
						occursList1.putParam("OOApprAmt",lData[19]);//撥付金額
						occursList1.putParam("OOReturnAmt",lData[20]);//退還金額
						occursList1.putParam("OOTitaTlrNo",lData[21]);//經辦
						occursList1.putParam("OOTitaTxtNo",lData[22]);//交易序號
						
						occursList1.putParam("OONewtransTxKind", NewtransTxKind );//試算交易別
						
						occursList1.putParam("OOStatus",Status);
						occursList1.putParam("OONewStatus",NewStatus);
						occursList1.putParam("OOCustLoanKind",CustLoanKind);
						occursList1.putParam("OOApplDate",ApplDate);
						occursList1.putParam("OOMainDueAmt",MainDueAmt);
						occursList1.putParam("OOTotalPeriod",TotalPeriod);
						occursList1.putParam("OOIntRate",IntRate);
						occursList1.putParam("OORepaidPeriod",RepaidPeriod);
						occursList1.putParam("OONewRepaidPeriod",NewRepaidPeriod);
						occursList1.putParam("OOMainFinCode",MainFinCode);
						occursList1.putParam("OOOrgPrincipalBal",OrgPrincipalBal);
						occursList1.putParam("OONewPrincipalBal",NewPrincipalBal);
						occursList1.putParam("OOOrgAccuTempAmt",OrgAccuTempAmt);
						occursList1.putParam("OONewAccuTempAmt",NewAccuTempAmt);
						occursList1.putParam("OOOrgAccuOverAmt",OrgAccuOverAmt);
						occursList1.putParam("OONewAccuOverAmt",NewAccuOverAmt);
						occursList1.putParam("OOOrgAccuDueAmt",OrgAccuDueAmt);
						occursList1.putParam("OONewAccuDueAmt",NewAccuDueAmt);
						occursList1.putParam("OOOrgAccuSklShareAmt",OrgAccuSklShareAmt);
						occursList1.putParam("OONewAccuSklShareAmt",NewAccuSklShareAmt);
						occursList1.putParam("OOOrgNextPayDate",OrgNextPayDate);
						
						occursList1.putParam("OONewNextPayDate",NewNextPayDate);
						occursList1.putParam("OOOrgRepayPrincipal",OrgRepayPrincipal);
						occursList1.putParam("OONewRepayPrincipal",NewRepayPrincipal);
						occursList1.putParam("OOOrgRepayInterest",OrgRepayInterest);
						occursList1.putParam("OONewRepayInterest",NewRepayInterest);
						occursList1.putParam("OOTwoStepCode",TwoStepCode);
						occursList1.putParam("OOChgCondDate",ChgCondDate);
						occursList1.putParam("OOPayIntDate",PayIntDate);
						occursList1.putParam("OONewPayIntDate",NewPayIntDate);
						occursList1.putParam("OOOrgStatusDate",OrgStatusDate);
						
						occursList1.putParam("OONewStatusDate",NewStatusDate);
						occursList1.putParam("OOTransAcDate",TransAcDate);
						occursList1.putParam("OOTransTitaTlrNo",TransTitaTlrNo);
						occursList1.putParam("OOTransTitaTxtNo",TransTitaTxtNo);
						occursList1.putParam("OOTransCustNo",TransCustNo);
						occursList1.putParam("OOTransCaseSeq",TransCaseSeq);
						occursList1.putParam("OOTransEntryDate",TransEntryDate);
						occursList1.putParam("OOTransTxStatus",TransTxStatus);
						occursList1.putParam("OONewTransTxStatus",NewTransTxStatus);
						occursList1.putParam("OOTransTxAmt",TransTxAmt);
						
						occursList1.putParam("OONewTransTxAmt",NewTransTxAmt);
						occursList1.putParam("OOTransPrincipalBal",TransPrincipalBal);
						occursList1.putParam("OONewTransPrincipalBal",NewTransPrincipalBal);
						occursList1.putParam("OOTransReturnAmt",TransReturnAmt);
						occursList1.putParam("OONewTransReturnAmt",NewTransReturnAmt);
						occursList1.putParam("OOTransSklShareAmt",TransSklShareAmt);
						occursList1.putParam("OONewTransSklShareAmt",NewTransSklShareAmt);
						occursList1.putParam("OOTransApprAmt",TransApprAmt);
						occursList1.putParam("OONewTransApprAmt",NewTransApprAmt);
						
						
						occursList1.putParam("OOTransExportDate",TransExportDate);
						occursList1.putParam("OOTransExportAcDate",TransExportAcDate);
						occursList1.putParam("OOTransTempRepayAmt",TransTempRepayAmt);
						occursList1.putParam("OONewTransTempRepayAmt",NewTransTempRepayAmt);
						occursList1.putParam("OOTransOverRepayAmt",TransOverRepayAmt);
						
						occursList1.putParam("OONewTransOverRepayAmt",NewTransOverRepayAmt);
						occursList1.putParam("OOTransPrincipalAmt",TransPrincipalAmt);
						occursList1.putParam("OONewTransPrincipalAmt",NewTransPrincipalAmt);
						occursList1.putParam("OOTransInterestAmt",TransInterestAmt);
						occursList1.putParam("OONewTransInterestAmt",NewTransInterestAmt);
						
						occursList1.putParam("OOTransOverAmt",TransOverAmt);
						occursList1.putParam("OONewTransOverAmt",NewTransOverAmt);
						occursList1.putParam("OOTransIntStartDate",TransIntStartDate);
						occursList1.putParam("OONewTransIntStartDate",NewTransIntStartDate);
						occursList1.putParam("OOTransIntEndDate",TransIntEndDate);
						occursList1.putParam("OONewTransIntEndDate",NewTransIntEndDate);
						
						occursList1.putParam("OOTransRepayPeriod",TransRepayPeriod);
						occursList1.putParam("OONewTransRepayPeriod",NewTransRepayPeriod);
						occursList1.putParam("OOTransShouldPayPeriod",TransShouldPayPeriod);
						occursList1.putParam("OONewTransShouldPayPeriod",NewTransShouldPayPeriod);
						occursList1.putParam("OOTransDueAmt",TransDueAmt);
						occursList1.putParam("OONewTransDueAmt",NewTransDueAmt);
						
						
						occursList1.putParam("OOTransRepayDate",TransRepayDate);
						occursList1.putParam("OONewTransRepayDate",NewTransRepayDate);
						occursList1.putParam("OOTransOrgAccuOverAmt",TransOrgAccuOverAmt);
						occursList1.putParam("OONewTransOrgAccuOverAmt",NewTransOrgAccuOverAmt);
						occursList1.putParam("OOTransAccuOverAmt",TransAccuOverAmt);
						occursList1.putParam("OONewTransAccuOverAmt",NewTransAccuOverAmt);
						
						timesfg = true;
						this.totaVo.addOccursList(occursList1);
					} else {
						continue;
					}
					
				} else {
					
					Seq++;
					occursList1.putParam("OOSeq",Seq);//
					occursList1.putParam("OOCustId",lData[2]);//身分證號
					occursList1.putParam("OOCaseSeq",lData[3]);//案件序號
					occursList1.putParam("OOCustNo",lData[4]);//戶號
					occursList1.putParam("OOCustName",lData[5]);//戶名
					occursList1.putParam("OOTxSts",TxKind);//交易別
					occursList1.putParam("OORemark",lData[7]);//備註
					occursList1.putParam("OOAcctDate",DcToRoc(lData[8]));//會計日
					occursList1.putParam("OOEntryDate",DcToRoc(lData[9]));//入帳日
					occursList1.putParam("OORepayDate",DcToRoc(lData[10]));//入帳還款日
					occursList1.putParam("OOTmpAmt",lData[11]);//暫收金額
					occursList1.putParam("OOOverPayAmt",lData[12]);//溢繳款
					occursList1.putParam("OOPayPeriod",lData[13]);//繳期數
					occursList1.putParam("OOPayAmt",lData[14]);//還款金額
					occursList1.putParam("OORevivPeriod",lData[15]);//應還期數
					occursList1.putParam("OORevivAmt",lData[16]);//應還金額
					occursList1.putParam("OOAcumTmpAmt",lData[17]);//累溢短收
					occursList1.putParam("OOSklShareAmt",lData[18]);//新壽攤分
					occursList1.putParam("OOApprAmt",lData[19]);//撥付金額
					occursList1.putParam("OOReturnAmt",lData[20]);//退還金額
					occursList1.putParam("OOTitaTlrNo",lData[21]);//經辦
					occursList1.putParam("OOTitaTxtNo",lData[22]);//交易序號
					
					occursList1.putParam("OONewtransTxKind",NewtransTxKind);//試算交易別
					
					occursList1.putParam("OOStatus",Status);
					occursList1.putParam("OONewStatus",NewStatus);
					occursList1.putParam("OOCustLoanKind",CustLoanKind);
					occursList1.putParam("OOApplDate",ApplDate);
					occursList1.putParam("OOMainDueAmt",MainDueAmt);
					occursList1.putParam("OOTotalPeriod",TotalPeriod);
					occursList1.putParam("OOIntRate",IntRate);
					occursList1.putParam("OORepaidPeriod",RepaidPeriod);
					occursList1.putParam("OONewRepaidPeriod",NewRepaidPeriod);
					occursList1.putParam("OOMainFinCode",MainFinCode);
					occursList1.putParam("OOOrgPrincipalBal",OrgPrincipalBal);
					occursList1.putParam("OONewPrincipalBal",NewPrincipalBal);
					occursList1.putParam("OOOrgAccuTempAmt",OrgAccuTempAmt);
					occursList1.putParam("OONewAccuTempAmt",NewAccuTempAmt);
					occursList1.putParam("OOOrgAccuOverAmt",OrgAccuOverAmt);
					occursList1.putParam("OONewAccuOverAmt",NewAccuOverAmt);
					occursList1.putParam("OOOrgAccuDueAmt",OrgAccuDueAmt);
					occursList1.putParam("OONewAccuDueAmt",NewAccuDueAmt);
					occursList1.putParam("OOOrgAccuSklShareAmt",OrgAccuSklShareAmt);
					occursList1.putParam("OONewAccuSklShareAmt",NewAccuSklShareAmt);
					occursList1.putParam("OOOrgNextPayDate",OrgNextPayDate);
					
					occursList1.putParam("OONewNextPayDate",NewNextPayDate);
					occursList1.putParam("OOOrgRepayPrincipal",OrgRepayPrincipal);
					occursList1.putParam("OONewRepayPrincipal",NewRepayPrincipal);
					occursList1.putParam("OOOrgRepayInterest",OrgRepayInterest);
					occursList1.putParam("OONewRepayInterest",NewRepayInterest);
					occursList1.putParam("OOTwoStepCode",TwoStepCode);
					occursList1.putParam("OOChgCondDate",ChgCondDate);
					occursList1.putParam("OOPayIntDate",PayIntDate);
					occursList1.putParam("OONewPayIntDate",NewPayIntDate);
					occursList1.putParam("OOOrgStatusDate",OrgStatusDate);
					
					occursList1.putParam("OONewStatusDate",NewStatusDate);
					occursList1.putParam("OOTransAcDate",TransAcDate);
					occursList1.putParam("OOTransTitaTlrNo",TransTitaTlrNo);
					occursList1.putParam("OOTransTitaTxtNo",TransTitaTxtNo);
					occursList1.putParam("OOTransCustNo",TransCustNo);
					occursList1.putParam("OOTransCaseSeq",TransCaseSeq);
					occursList1.putParam("OOTransEntryDate",TransEntryDate);
					occursList1.putParam("OOTransTxStatus",TransTxStatus);
					occursList1.putParam("OONewTransTxStatus",NewTransTxStatus);
					occursList1.putParam("OOTransTxAmt",TransTxAmt);
					
					occursList1.putParam("OONewTransTxAmt",NewTransTxAmt);
					occursList1.putParam("OOTransPrincipalBal",TransPrincipalBal);
					occursList1.putParam("OONewTransPrincipalBal",NewTransPrincipalBal);
					occursList1.putParam("OOTransReturnAmt",TransReturnAmt);
					occursList1.putParam("OONewTransReturnAmt",NewTransReturnAmt);
					occursList1.putParam("OOTransSklShareAmt",TransSklShareAmt);
					occursList1.putParam("OONewTransSklShareAmt",NewTransSklShareAmt);
					occursList1.putParam("OOTransApprAmt",TransApprAmt);
					occursList1.putParam("OONewTransApprAmt",NewTransApprAmt);
					
					
					occursList1.putParam("OOTransExportDate",TransExportDate);
					occursList1.putParam("OOTransExportAcDate",TransExportAcDate);
					occursList1.putParam("OOTransTempRepayAmt",TransTempRepayAmt);
					occursList1.putParam("OONewTransTempRepayAmt",NewTransTempRepayAmt);
					occursList1.putParam("OOTransOverRepayAmt",TransOverRepayAmt);
					
					occursList1.putParam("OONewTransOverRepayAmt",NewTransOverRepayAmt);
					occursList1.putParam("OOTransPrincipalAmt",TransPrincipalAmt);
					occursList1.putParam("OONewTransPrincipalAmt",NewTransPrincipalAmt);
					occursList1.putParam("OOTransInterestAmt",TransInterestAmt);
					occursList1.putParam("OONewTransInterestAmt",NewTransInterestAmt);
					
					occursList1.putParam("OOTransOverAmt",TransOverAmt);
					occursList1.putParam("OONewTransOverAmt",NewTransOverAmt);
					occursList1.putParam("OOTransIntStartDate",TransIntStartDate);
					occursList1.putParam("OONewTransIntStartDate",NewTransIntStartDate);
					occursList1.putParam("OOTransIntEndDate",TransIntEndDate);
					occursList1.putParam("OONewTransIntEndDate",NewTransIntEndDate);
					
					occursList1.putParam("OOTransRepayPeriod",TransRepayPeriod);
					occursList1.putParam("OONewTransRepayPeriod",NewTransRepayPeriod);
					occursList1.putParam("OOTransShouldPayPeriod",TransShouldPayPeriod);
					occursList1.putParam("OONewTransShouldPayPeriod",NewTransShouldPayPeriod);
					occursList1.putParam("OOTransDueAmt",TransDueAmt);
					occursList1.putParam("OONewTransDueAmt",NewTransDueAmt);
					
					
					occursList1.putParam("OOTransRepayDate",TransRepayDate);
					occursList1.putParam("OONewTransRepayDate",NewTransRepayDate);
					occursList1.putParam("OOTransOrgAccuOverAmt",TransOrgAccuOverAmt);
					occursList1.putParam("OONewTransOrgAccuOverAmt",NewTransOrgAccuOverAmt);
					occursList1.putParam("OOTransAccuOverAmt",TransAccuOverAmt);
					occursList1.putParam("OONewTransAccuOverAmt",NewTransAccuOverAmt);
					this.totaVo.addOccursList(occursList1);
				}
				
				
				
			}
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			if(Data !=null && Data.size() >=this.limit) {
				titaVo.setReturnIndex(this.setIndexNext());
				//this.totaVo.setMsgEndToAuto();// 自動折返
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
			
			if(selectfg && !timesfg) {
				throw new LogicException(titaVo, "E2003","");
			}
		}else {
			//E2003 查無資料
			throw new LogicException(titaVo, "E2003","");
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	public String DcToRoc(String Dc) {
		String Roc="0";
		if(Dc!=null && Dc.length()!=0) {
			int DcL=Dc.length();
			if(DcL==8) {
				Roc=String.valueOf(Integer.parseInt(Dc)-19110000);
			}
		}
		return Roc;
	}	
}