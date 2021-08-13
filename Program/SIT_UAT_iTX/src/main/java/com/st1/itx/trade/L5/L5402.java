package com.st1.itx.trade.L5;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.domain.PfBsOfficerId;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.data.DataLog;

@Service("L5402")
@Scope("prototype")
/**
  * 年度業績目標更新
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5402 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5402.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;
	
	@Autowired
	public PfBsOfficerService iPfBsOfficerService;
	
	@Autowired
	public CdBcmService iCdBcmService;
	
	@Autowired
	public CdEmpService iCdEmpService;
	
	@Autowired
	public DataLog dataLog;
	
	@Autowired
	MakeExcel makeExcel;
	
	@Value("${iTXInFolder}")
	private String inFolder = "";
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5402 ");
		this.totaVo.init(titaVo);
		
		String iYear = titaVo.getParam("UploadYear");	
		String iFileName = inFolder+ dateUtil.getNowStringBc() + File.separatorChar + 
						   titaVo.getTlrNo() + File.separatorChar +titaVo.getParam("FILENA").trim();
		int iUploadFg = Integer.valueOf(titaVo.getParam("UploadFg"));
		
		if(iFileName.equals("")) {
			throw new LogicException(titaVo, "E0014", "上傳檔案為空，請選擇 .xls之Excel檔案");
		}else {
			if(!iFileName.contains(".xls")) {
				throw new LogicException(titaVo, "E0014", "上傳檔案類型錯誤，請選擇 .xls之Excel檔案");
			}
		}
		
		makeExcel.openExcel(iFileName, 1);
		
		int checkValue = 5;
		String checkName = "";
		
		int officerCodeYE = 5;
		String iOfficerCode = "";
		String iDeptCode = "";
		String iLastDeptCode = "";
		String iDistCode = "";
		String iDistName = "";
		String iAreaCode = "";
		String iAreaItem = "";
		
		//判斷100行內"部專合計"資料是否存在
		while(true) {
			if (checkValue >= 100) {
				throw new LogicException(titaVo, "E0014", "固定位置無部專合計");
			}
			try {
				checkName = (String) makeExcel.getValue(checkValue, 1);
			}catch (Exception e) {
				throw new LogicException(titaVo, "E0014", "固定位置資料型態錯誤，請重新上傳");
			}
			
			if (checkName.equals("部專合計")) {
				break;
			}else {
			checkValue ++ ;
			}
		}
		//吃檔主執行區
		while(true) {
			//有效資料範圍結束，officerCodeYE遍歷與checkValue值相同時跳出
			if (officerCodeYE == checkValue) {
				break;
			}

			//第一筆資料檢核
			try {
				iOfficerCode = (String) makeExcel.getValue(officerCodeYE, 6);
			}catch (Exception e) {
				throw new LogicException(titaVo, "E0014", "員工編號資料型態錯誤，請重新上傳");
			}
			if (officerCodeYE == 5 && iOfficerCode.equals("")) {
				throw new LogicException(titaVo, "E0014", "固定位置無員工編號	");
			}
			try {
				iDeptCode = (String) makeExcel.getValue(officerCodeYE, 2);
			}catch (Exception e) {
				throw new LogicException(titaVo, "E0014", "部室代號資料型態錯誤，請重新上傳");
			}		
			if (officerCodeYE == 5 && iDeptCode.equals("")) {
				throw new LogicException(titaVo, "E0014", "固定位置無部室代號	");
			}
			try {
				iDistCode = (String) makeExcel.getValue(officerCodeYE, 4);
			}catch (Exception e) {
				throw new LogicException(titaVo, "E0014", "區部代號資料型態錯誤，請重新上傳");
			}	
			try {
				iDistName = (String) makeExcel.getValue(officerCodeYE, 3);
			}catch (Exception e) {
				throw new LogicException(titaVo, "E0014", "區部名稱資料型態錯誤，請重新上傳");
			}	
		
			if (officerCodeYE == 5 && iDistCode.equals("") && iDistName.equals("")) {
				throw new LogicException(titaVo, "E0014", "固定位置無區部代號或區部名稱");
			}
			
			if (iOfficerCode.equals("") || !iOfficerCode.equals("")&& iDistName.equals("部專")) {
				officerCodeYE ++ ;
				continue;
			}
						
			//第一筆與之後資料處理		
			int iWorkMonth = 7;
			switch(iUploadFg) {
			//1 :新增 2:修改
			case 1:
				PfBsOfficer iPfBsOfficer = new PfBsOfficer();
				PfBsOfficerId iPfBsOfficeId = new PfBsOfficerId();
				CdBcm iCdBcm = new CdBcm();
				CdEmp iCdEmp = new CdEmp();
				while (true) {
					if (iWorkMonth == 20) {
						break;
					}
					int seWorkMonth = Integer.valueOf(iYear+StringUtils.leftPad(String.valueOf(iWorkMonth-6),2,"0"))+191100;
					iPfBsOfficeId.setEmpNo(iOfficerCode);
					iPfBsOfficeId.setWorkMonth(seWorkMonth);
					iPfBsOfficer = iPfBsOfficerService.findById(iPfBsOfficeId, titaVo);
					if (iPfBsOfficer!= null) {
						throw new LogicException(titaVo, "E0005", "員工編號:"+iOfficerCode+"，工作月"+String.valueOf(seWorkMonth)+"已有資料");
					}else {
						iPfBsOfficer = new PfBsOfficer();
					}
					
					//部室代號處理，若iDeptCode為空則抓iLastDeptCode
					if (iDeptCode.equals("")) {
						iPfBsOfficer.setDeptCode(iLastDeptCode);
						iCdBcm = iCdBcmService.deptCodeFirst(iLastDeptCode, titaVo);
					}else {
						iLastDeptCode = iDeptCode;
						iPfBsOfficer.setDeptCode(iDeptCode);
						iCdBcm = iCdBcmService.deptCodeFirst(iDeptCode, titaVo);
					}
					if (iCdBcm == null) {
//						iPfBsOfficer.setDepItem("");
						throw new LogicException(titaVo, "E0001", "查無部室代號("+iDeptCode+")");
					}else {
						iPfBsOfficer.setDepItem(iCdBcm.getDeptItem());		
						//處理areacode
						switch(iCdBcm.getDeptCode()) {
						case "A0F000":
							iAreaCode = "10HC00";
							iAreaItem = "北部區域中心";
							break;
						case "A0M000":
							iAreaCode = "10HL00";
							iAreaItem = "南部區域中心";
							break;
						case "A0B000":
							iAreaCode = "10HC00";
							iAreaItem = "北部區域中心";
							break;
						case "A0E000":
							iAreaCode = "10HJ00";
							iAreaItem = "中部區域中心";
							break;
						default:
							iAreaCode = "";
							iAreaItem = "";
							break;
						}
					}
					//區域中心塞值
					iPfBsOfficer.setAreaCode(iAreaCode);
					iPfBsOfficer.setAreaItem(iAreaItem);
					
					//區部代號處理，代號為空白放區部名稱否則抓CdBcm資料
					iPfBsOfficer.setDistCode(iDistCode);
					if (iDistCode.equals("")) {
						if (iDistName.equals("")) {
							//區部代號與區部中文皆為空白時塞房貸部專
							iPfBsOfficer.setDistItem("房貸部專");
						}else {
							iPfBsOfficer.setDistItem(iDistName);
						}
					}else {
						iCdBcm = iCdBcmService.distCodeFirst(iDistCode, titaVo);
						if (iCdBcm == null) {
//							iPfBsOfficer.setDistItem("");
							throw new LogicException(titaVo, "E0001", "查無區部代號("+iDistCode+")");
						}else {
							iPfBsOfficer.setDistItem(iCdBcm.getDistItem());
						}
					}


					//房貸專員姓名處理
					iCdEmp = iCdEmpService.findById(iOfficerCode, titaVo);
					if (iCdEmp == null) {
//						iPfBsOfficer.setFullname("");
						throw new LogicException(titaVo, "E0001", "查無員工代號("+iOfficerCode+")");
					}else {
						iPfBsOfficer.setFullname(iCdEmp.getFullname());
					}
					double dGoalAmt = 0;
					//目標金額處理
					try {
						dGoalAmt =(double)makeExcel.getValue(officerCodeYE, iWorkMonth)*10000;
					}catch (Exception e) {
						throw new LogicException(titaVo, "E0014", "目標金額欄位資料型態錯誤，請重新上傳");
					}
					
					int iGoalAmt = Math.round((float)dGoalAmt);
					iPfBsOfficer.setGoalAmt(new BigDecimal(iGoalAmt));
					
					iPfBsOfficer.setPfBsOfficerId(iPfBsOfficeId);
					try {
						iPfBsOfficerService.insert(iPfBsOfficer,titaVo);
					}catch (DBException e) {
						throw new LogicException(titaVo, "E0005", e.getErrorMsg()); //新增錯誤訊息
					}
					
					iWorkMonth ++;
				}
				break;
			case 2:
				PfBsOfficer uPfBsOfficer = new PfBsOfficer();
				PfBsOfficer hPfBsOfficer = new PfBsOfficer();
				PfBsOfficerId uPfBsOfficeId = new PfBsOfficerId();
				CdBcm uCdBcm = new CdBcm();
				CdEmp uCdEmp = new CdEmp();
				while (true) {
					if (iWorkMonth == 20) {
						break;
					}
					int seWorkMonth = Integer.valueOf(iYear+StringUtils.leftPad(String.valueOf(iWorkMonth-6),2,"0"))+191100;
					uPfBsOfficeId.setEmpNo(iOfficerCode);
					uPfBsOfficeId.setWorkMonth(seWorkMonth);
					uPfBsOfficer = iPfBsOfficerService.findById(uPfBsOfficeId, titaVo);
					
					if (uPfBsOfficer == null) {
						//查無資料執行新增
						uPfBsOfficer = new PfBsOfficer();
						//部室代號處理，若iDeptCode為空則抓iLastDeptCode
						if (iDeptCode.equals("")) {
							uPfBsOfficer.setDeptCode(iLastDeptCode);
							uCdBcm = iCdBcmService.deptCodeFirst(iLastDeptCode, titaVo);
						}else {
							iLastDeptCode = iDeptCode;
							uPfBsOfficer.setDeptCode(iDeptCode);
							uCdBcm = iCdBcmService.deptCodeFirst(iDeptCode, titaVo);
						}
						if (uCdBcm == null) {
//							uPfBsOfficer.setDepItem("");
							throw new LogicException(titaVo, "E0001", "查無部室代號("+iDeptCode+")");
						}else {
							uPfBsOfficer.setDepItem(uCdBcm.getDeptItem());		
							//處理areacode
							switch(uCdBcm.getDeptCode()) {
							case "A0F000":
								iAreaCode = "10HC00";
								iAreaItem = "北部區域中心";
								break;
							case "A0M000":
								iAreaCode = "10HL00";
								iAreaItem = "南部區域中心";
								break;
							case "A0B000":
								iAreaCode = "10HC00";
								iAreaItem = "北部區域中心";
								break;
							case "A0E000":
								iAreaCode = "10HJ00";
								iAreaItem = "中部區域中心";
								break;
							default:
								iAreaCode = "";
								iAreaItem = "";
								break;
							}
						}
						//區域中心塞值
						uPfBsOfficer.setAreaCode(iAreaCode);
						uPfBsOfficer.setAreaItem(iAreaItem);
						
						//區部代號處理，代號為空白放區部名稱否則抓CdBcm資料
						uPfBsOfficer.setDistCode(iDistCode);
						if (iDistCode.equals("")) {
							uPfBsOfficer.setDistItem(iDistName);
						}else {
							uCdBcm = iCdBcmService.distCodeFirst(iDistCode, titaVo);
							if (uCdBcm == null) {
//								uPfBsOfficer.setDistItem("");
								throw new LogicException(titaVo, "E0001", "查無區部代號("+iDistCode+")");
							}else {
								uPfBsOfficer.setDistItem(uCdBcm.getDistItem());
							}
						}
						//區部代號與區部中文皆為空白時塞房貸部專
						if (iDistCode.equals("")&&iDistName.equals("")) {
							uPfBsOfficer.setDistItem("房貸部專");
						}

						//房貸專員姓名處理
						uCdEmp = iCdEmpService.findById(iOfficerCode, titaVo);
						if (uCdEmp == null) {
//							uPfBsOfficer.setFullname("");
							throw new LogicException(titaVo, "E0001", "查無員工代號("+iOfficerCode+")");
						}else {
							uPfBsOfficer.setFullname(uCdEmp.getFullname());
						}
						
						double dGoalAmt = 0;
						//目標金額處理
						try {
							dGoalAmt =(double)makeExcel.getValue(officerCodeYE, iWorkMonth)*10000;
						}catch (Exception e) {
							throw new LogicException(titaVo, "E0014", "目標金額欄位資料型態錯誤，請重新上傳");
						}
						int iGoalAmt = Math.round((float)dGoalAmt);
						uPfBsOfficer.setGoalAmt(new BigDecimal(iGoalAmt));
						
						uPfBsOfficer.setPfBsOfficerId(uPfBsOfficeId);

						try {
							iPfBsOfficerService.insert(uPfBsOfficer,titaVo);
						}catch (DBException e) {
							throw new LogicException(titaVo, "E0005", e.getErrorMsg()); //新增錯誤訊息
						}
			
						iWorkMonth ++;
					}else {
						
						//資料重複執行更新
						// 變更前
						hPfBsOfficer = iPfBsOfficerService.holdById(uPfBsOfficeId, titaVo);
						//部室代號處理，若iDeptCode為空則抓iLastDeptCode
						if (iDeptCode.equals("")) {
							hPfBsOfficer.setDeptCode(iLastDeptCode);
							uCdBcm = iCdBcmService.deptCodeFirst(iLastDeptCode, titaVo);
						}else {
							iLastDeptCode = iDeptCode;
							hPfBsOfficer.setDeptCode(iDeptCode);
							uCdBcm = iCdBcmService.deptCodeFirst(iDeptCode, titaVo);
						}
						if (uCdBcm == null) {
							hPfBsOfficer.setDepItem("");
							throw new LogicException(titaVo, "E0001", "查無部室代號("+iDeptCode+")");
						}else {
							hPfBsOfficer.setDepItem(uCdBcm.getDeptItem());		
							//處理areacode
							switch(uCdBcm.getDeptCode()) {
							case "A0F000":
								iAreaCode = "10HC00";
								iAreaItem = "北部區域中心";
								break;
							case "A0M000":
								iAreaCode = "10HL00";
								iAreaItem = "南部區域中心";
								break;
							case "A0B000":
								iAreaCode = "10HC00";
								iAreaItem = "北部區域中心";
								break;
							case "A0E000":
								iAreaCode = "10HJ00";
								iAreaItem = "中部區域中心";
								break;
							default:
								iAreaCode = "";
								iAreaItem = "";
								break;
							}
						}
						//區域中心塞值
						hPfBsOfficer.setAreaCode(iAreaCode);
						hPfBsOfficer.setAreaItem(iAreaItem);
						
						//區部代號處理，代號為空白放區部名稱否則抓CdBcm資料
						hPfBsOfficer.setDistCode(iDistCode);
						if (iDistCode.equals("")) {
							hPfBsOfficer.setDistItem(iDistName);
						}else {
							uCdBcm = iCdBcmService.distCodeFirst(iDistCode, titaVo);
							if (uCdBcm == null) {
//								hPfBsOfficer.setDistItem("");
								throw new LogicException(titaVo, "E0001", "查無區部代號("+iDistCode+")");
							}else {
								hPfBsOfficer.setDistItem(uCdBcm.getDistItem());
							}
						}
						//區部代號與區部中文皆為空白時塞房貸部專
						if (iDistCode.equals("")&&iDistName.equals("")) {
							hPfBsOfficer.setDistItem("房貸部專");
						}

						//房貸專員姓名處理
						uCdEmp = iCdEmpService.findById(iOfficerCode, titaVo);
						if (uCdEmp == null) {
//							hPfBsOfficer.setFullname("");
							throw new LogicException(titaVo, "E0001", "查無員工代號("+iOfficerCode+")");
						}else {
							hPfBsOfficer.setFullname(uCdEmp.getFullname());
						}
						
						double dGoalAmt = 0;
						//目標金額處理
						try {
							dGoalAmt =(double)makeExcel.getValue(officerCodeYE, iWorkMonth)*10000;
						}catch (Exception e) {
							throw new LogicException(titaVo, "E0014", "目標金額欄位資料型態錯誤，請重新上傳");
						}
						int iGoalAmt = Math.round((float)dGoalAmt);
						hPfBsOfficer.setGoalAmt(new BigDecimal(iGoalAmt));
						try {
							iPfBsOfficerService.update(hPfBsOfficer,titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料更新錯誤
						}

						iWorkMonth ++;
					}			
				}
				break;
			}
			
			officerCodeYE ++ ;
		}
		
		
		System.out.print(titaVo.getParam("FILENA").trim()+"資料新增成功");
		
		totaVo.putParam("OSuccessFlag",1);
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	

}