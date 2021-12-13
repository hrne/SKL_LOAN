package com.st1.itx.trade.L5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L5813Batch")
@Scope("prototype")

public class L5813Batch extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;
	
	@Autowired
	public YearlyHouseLoanIntService sYearlyHouseLoanIntService;
	
	@Autowired
	public ClFacService clFacService;
	
	@Autowired
	public ClBuildingService sClBuildingService;
	
	@Autowired
	public ClBuildingOwnerService sClBuildingOwnerService;
	
	@Autowired
	public CdCodeService sCdCodeService;
	
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	public MakeFile makeFile;
	@Autowired
	public WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	private CustMain tCustMain = new CustMain();
	private CdCode tCdCode = new CdCode();
	private ClFac tClFac = new ClFac();
	private ClBuilding tClBuilding = null;
	private Slice<ClBuildingOwner> tClBuildingOwner = null;
	private ClBuildingOwner cClBuildingOwner = null;
	private Boolean checkFlag = true;
	private String sendMsg = " ";
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.totaVo.init(titaVo);
		
		
		int tYear = Integer.parseInt(titaVo.getParam("Year"))+1;
		String fileCode = "L5813";
		String fileItem = "國稅局申報媒體檔";
		String fileName = "每年房屋擔保借款繳息媒體檔"+tYear+"年度.CSV";
		
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileCode + fileItem, fileName, 2);
		
		try {
			doFile(titaVo);
		} catch (LogicException e) {
			checkFlag = false;
			sendMsg = e.getErrorMsg();
		}

		if (checkFlag) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L5813國稅局申報媒體檔已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), sendMsg, titaVo);
		}
		

		
		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);
		
		this.addList(this.totaVo);
		return this.sendList();
		
	}



	public void doFile(TitaVo titaVo) throws LogicException {
		
		
		
		String iYear = titaVo.getParam("Year");
		String iYearMonth = String.valueOf(Integer.parseInt(iYear)+1911);
		this.info("iYearMonth=="+iYearMonth);
		int St = Integer.parseInt(iYearMonth+"01");
		int End = Integer.parseInt(iYearMonth+"12");
		
		Slice<YearlyHouseLoanInt> tYearlyHouseLoanInt = sYearlyHouseLoanIntService.findbyYear(St, End, titaVo.getReturnIndex(), 500, titaVo);
		List<YearlyHouseLoanInt> sYearlyHouseLoanInt = tYearlyHouseLoanInt == null ? null:tYearlyHouseLoanInt.getContent();
		
		
		if(sYearlyHouseLoanInt==null) {
			throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔無資料");
		} else {
			
			for(YearlyHouseLoanInt iYearlyHouseLoanInt : sYearlyHouseLoanInt) {
				//只找尋購置不動產 ***不確定***
				if(!(("2").equals(iYearlyHouseLoanInt.getUsageCode()) || ("02").equals(iYearlyHouseLoanInt.getUsageCode())) ) {
					continue;
				}
				String bdLocation = "";// 建物地址
				String bdOwner = ""; // 所有權人姓名
				String bdCustId = ""; // 所有權人身分證
				int houseBuyDate = iYearlyHouseLoanInt.getHouseBuyDate();//房屋取的日期
				String iCustName = "";
				String iCustId = "";
				String iUkey= "";
				String iYearMonthSt = "";// 繳息年月起
				int iCustNo = iYearlyHouseLoanInt.getCustNo();
				int iFacmNo = iYearlyHouseLoanInt.getFacmNo();
				BigDecimal iLoanAmt = iYearlyHouseLoanInt.getLoanAmt();// 最初初貸金額
				int iFirstDrawdownDate = iYearlyHouseLoanInt.getFirstDrawdownDate();// 貸款起日
				int iMaturityDate = iYearlyHouseLoanInt.getMaturityDate(); // 貸款迄日
				BigDecimal iLoanBal = iYearlyHouseLoanInt.getLoanBal();// 放款餘額
				
				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(iYearlyHouseLoanInt.getJsonFields());
				bdLocation = tTempVo.get("F21");

				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				
				if(tCustMain==null) {
					throw new LogicException(titaVo, "E0001", "客戶資料主檔,戶號="+iCustNo);
				} else {
					iCustName = tCustMain.getCustName();
					if(iCustName.length()>6) {
						iCustName = iCustName.substring(0, 6);
					}
					iCustId = tCustMain.getCustId();
					iUkey = tCustMain.getCustUKey();
				}
				
				//戶號找擔保品
				tClFac = clFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);
				
				if(tClFac != null) {
					int oClCode1 = tClFac.getClCode1();
					int oClCode2 = tClFac.getClCode2();
					int oClNo = tClFac.getClNo();
//					tClBuilding = sClBuildingService.findById(new ClBuildingId(oClCode1, oClCode2, oClNo), titaVo);
//					if(tClBuilding!=null) {
//						bdLocation = tClBuilding.getBdLocation();//	
//					}
					
					//所有權人戶名、統編 先找本人
					cClBuildingOwner = sClBuildingOwnerService.findById(new ClBuildingOwnerId(oClCode1,oClCode2,oClNo,iUkey), titaVo);
					
					if(cClBuildingOwner != null) {
						bdOwner = tCustMain.getCustName();
						bdCustId = tCustMain.getCustId();
					}else {
						
						tClBuildingOwner = sClBuildingOwnerService.clNoEq(oClCode1, oClCode2, oClNo, this.index, this.limit, titaVo);
						List<ClBuildingOwner> sClBuildingOwner = tClBuildingOwner == null ? null:tClBuildingOwner.getContent();
						
						if(sClBuildingOwner != null) {
							tCustMain = sCustMainService.findById(sClBuildingOwner.get(0).getOwnerCustUKey(), titaVo);
							bdOwner = tCustMain.getCustName();
							bdCustId = tCustMain.getCustId();
						}
					}
					
					
				}
				
				
				
				int iYYYMM = iYearlyHouseLoanInt.getYearMonth()-191100; 
				if(iYearlyHouseLoanInt.getFirstDrawdownDate() < iYYYMM * 100) {// 繳息年月起
					iYearMonthSt = String.valueOf(iYYYMM).substring(0, 3)+"01";
				} else {
					iYearMonthSt = String.valueOf(iYearlyHouseLoanInt.getFirstDrawdownDate()/100);
				}
				
				
				String cUsageCode = iYearlyHouseLoanInt.getUsageCode();
				if(!cUsageCode.isEmpty() && cUsageCode.length()<2) {
					cUsageCode = "0"+cUsageCode;
				}
//				tCdCode = sCdCodeService.findById(new CdCodeId("UsageCode",cUsageCode),titaVo);
//				if(tCdCode!=null) {
//					iUsageCode = tCdCode.getItem();// 用途別
//				}
				
				
			String strField = "";
			strField += iYear; // 年度 3碼
			strField += ",";
			strField += "458"; // 金融機構固定458 3碼
			strField += ",";
			strField += "    ";// 分行代號 空白 4碼
			strField += ",";
			strField += bdLocation; // 房屋座落地址 120碼
			strField += ",";
			strField += bdOwner; // 房屋所有權人姓名 40碼
			strField += ",";
			strField += bdCustId; // 房屋所有權人身分證統一編號 10碼
			strField += ",";
			strField += String.valueOf(houseBuyDate); // 房屋所有權取得日 7碼 YYYMMDD
			strField += ",";
			strField += iCustName; // 借款人姓名 12碼
			strField += ",";
			strField += iCustId; // 借款人身分證統一編號 10碼
			strField += ",";
			strField += String.format("%07d", iCustNo)+"-"+String.format("%03d", iFacmNo); // 貸款帳號 50 碼
			strField += ",";
			strField += String.valueOf(iLoanAmt); // 最初貸款金額 10碼 右靠前埔0
			strField += ",";
			strField += String.valueOf(iFirstDrawdownDate); // 貸款起日 7碼
			strField += ",";
			strField += String.valueOf(iMaturityDate); // 貸款迄日 7碼
			strField += ",";
			strField += String.valueOf(iLoanBal); // 截至本年度未償還本金餘額 10碼
			strField += ",";
			strField += iYearMonthSt; // 繳息年月起 5碼 *不確定*
			strField += ",";
			strField += String.valueOf(iYYYMM); // 繳息年月迄 5碼 *不確定*
			strField += ",";
			strField += String.valueOf(iYearlyHouseLoanInt.getYearlyInt()); // 年度實際繳息金額 10碼 右靠前埔0
//			strField += ",";
//			strField += iUsageCode; // 用途別 12碼
			makeFile.put(strField);
			}	
		}
		
		
		
	}




}
