package com.st1.itx.trade.L5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.*;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.ClBuildingOwnerService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.YearlyHouseLoanIntService;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Component("L5813Batch")
@Scope("prototype")

public class L5813File extends MakeFile {

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
	
	public void exec(TitaVo titaVo) throws LogicException {
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		
		int tYear = Integer.parseInt(titaVo.getParam("Year"))+1;
		
		int date = Integer.valueOf(titaVo.getEntDy());
		String brno = titaVo.getBrno();
		String fileCode = "L5813";
		String fileItem = "國稅局申報媒體檔";
		String fileName = "每年房屋擔保借款繳息媒體檔"+tYear+"年度";

		this.open(titaVo, date, brno, fileCode, fileItem, fileName);

		doFile(titaVo);
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "L5813國稅局申報媒體檔已完成", titaVo);
	}



	public void doFile(TitaVo titaVo) throws LogicException {
		
		String iYear = titaVo.getParam("Year");
		String iYearMonth = String.valueOf(Integer.parseInt(iYear)+1911);
		this.info("iYearMonth=="+iYearMonth);
		int St = Integer.parseInt(iYearMonth+"01");
		int End = Integer.parseInt(iYearMonth+"12");
		
		Slice<YearlyHouseLoanInt> tYearlyHouseLoanInt = sYearlyHouseLoanIntService.findbyYear(St, End, this.index, this.limit, titaVo);
		List<YearlyHouseLoanInt> sYearlyHouseLoanInt = tYearlyHouseLoanInt == null ? null:tYearlyHouseLoanInt.getContent();
		
		
		if(sYearlyHouseLoanInt==null) {
			throw new LogicException(titaVo, "E0001", "每年房屋擔保借款繳息工作檔");
		} else {
			
			for(YearlyHouseLoanInt iYearlyHouseLoanInt : sYearlyHouseLoanInt) {
				
				String bdLocation = "";// 建物地址
				String bdOwner = ""; // 所有權人姓名
				String bdCustId = ""; // 所有權人身分證
				int bdContractDate = 0;// 房屋所有權取得日 ???
				String iCustName = "";
				String iCustId = "";
				String iUsageCode = "";
				String iYearMonthSt = "";// 繳息年月起
				int iCustNo = iYearlyHouseLoanInt.getCustNo();
				int iFacmNo = iYearlyHouseLoanInt.getFacmNo();
				BigDecimal iLoanAmt = iYearlyHouseLoanInt.getLoanAmt();// 最初初貸金額
				int iFirstDrawdownDate = iYearlyHouseLoanInt.getFirstDrawdownDate();// 貸款起日
				int iMaturityDate = iYearlyHouseLoanInt.getMaturityDate(); // 貸款迄日
				BigDecimal iLoanBal = iYearlyHouseLoanInt.getLoanBal();// 放款餘額
				

				tCustMain = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
				
				if(tCustMain==null) {
					throw new LogicException(titaVo, "E0001", "客戶資料主檔,戶號="+iCustNo);
				} else {
					iCustName = tCustMain.getCustName();
					if(iCustName.length()>6) {
						iCustName = iCustName.substring(0, 6);
					}
					iCustId = tCustMain.getCustId();
				}
				
				//戶號找擔保品
				tClFac = clFacService.mainClNoFirst(iCustNo, iFacmNo, "Y", titaVo);
				
				if(tClFac != null) {
					int oClCode1 = tClFac.getClCode1();
					int oClCode2 = tClFac.getClCode2();
					int oClNo = tClFac.getClNo();
					tClBuilding = sClBuildingService.findById(new ClBuildingId(oClCode1, oClCode2, oClNo), titaVo);
					if(tClBuilding!=null) {
						bdLocation = tClBuilding.getBdLocation();
						bdContractDate = tClBuilding.getContractDate();
					}
					
					//所有權人戶名、統編
					tClBuildingOwner = sClBuildingOwnerService.clNoEq(oClCode1, oClCode2, oClNo, this.index, this.limit, titaVo);
					List<ClBuildingOwner> sClBuildingOwner = tClBuildingOwner == null ? null:tClBuildingOwner.getContent();
					if(sClBuildingOwner != null) {
						tCustMain = sCustMainService.findById(sClBuildingOwner.get(0).getOwnerCustUKey(), titaVo);
						bdOwner = tCustMain.getCustName();
						bdCustId = tCustMain.getCustId();
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
				tCdCode = sCdCodeService.findById(new CdCodeId("UsageCode",cUsageCode),titaVo);
				if(tCdCode!=null) {
					iUsageCode = tCdCode.getItem();// 用途別
				}
				
				
			String strField = "";
			strField += iYear; // 年度 3碼
			strField += "458"; // 金融機構固定458 3碼
			strField += "    ";// 分行代號 空白 4碼
			strField += makeFile.fillStringR(bdLocation, 120); // 房屋座落地址 120碼
			strField += makeFile.fillStringR(bdOwner, 40); // 房屋所有權人姓名 40碼
			strField += makeFile.fillStringR(bdCustId, 10); // 房屋所有權人身分證統一編號 10碼
			strField += makeFile.fillStringL(String.valueOf(bdContractDate), 7, '0'); // 房屋所有權取得日 7碼 YYYMMDD
			strField += makeFile.fillStringR(iCustName, 12); // 借款人姓名 12碼
			strField += makeFile.fillStringR(iCustId, 10); // 借款人身分證統一編號 10碼
			strField += makeFile.fillStringR(String.format("%07d", iCustNo)+"-"+String.format("%03d", iFacmNo), 50); // 貸款帳號 50 碼
			strField += makeFile.fillStringL(String.valueOf(iLoanAmt), 10, '0'); // 最初貸款金額 10碼 右靠前埔0
			strField += String.valueOf(iFirstDrawdownDate); // 貸款起日 7碼
			strField += String.valueOf(iMaturityDate); // 貸款迄日 7碼
			strField += makeFile.fillStringL(String.valueOf(iLoanBal), 10, '0'); // 截至本年度未償還本金餘額 10碼
			strField += makeFile.fillStringL(iYearMonthSt, 5, '0'); // 繳息年月起 5碼
			strField += makeFile.fillStringL(String.valueOf(iYYYMM), 5, '0'); // 繳息年月迄 5碼
			strField += makeFile.fillStringL(String.valueOf(iYearlyHouseLoanInt.getYearlyInt()), 10, '0');; // 年度實際繳息金額 10碼 右靠前埔0
			strField += makeFile.fillStringR(iUsageCode, 12); // 用途別 12碼
			this.put(strField);
			}	
		}
		
		
		
	}




}
