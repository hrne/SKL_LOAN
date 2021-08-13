package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L5951ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 */

@Service("L5952")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5952 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5952.class);
	@Autowired
	public PfBsDetailService sPfBsDetailService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	public PfBsOfficerService sPfBsOfficerService;
	@Autowired
	public CdAoDeptService sCdAoDeptService;

	@Autowired
	public L5051ServiceImpl l5051ServiceImpl;

	@Autowired
	public L5951ServiceImpl l5951ServiceImpl;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

//	@Autowired
//	public CdEmpService sCdEmpService;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5952 ");
		this.totaVo.init(titaVo);
		String FunctionCd = titaVo.getParam("FunctionCd").trim();// 查詢方式
		String WorkMonthFm = titaVo.getParam("WorkMonthFm").trim();// 工作月(起)YYYYmm
		String WorkMonthTo = titaVo.getParam("WorkMonthTo").trim();// 工作月(訖)YYYYmm
		String CustNo = titaVo.getParam("CustNo").trim();// 戶號
		String FacmNo = titaVo.getParam("FacmNo").trim();// 額度編號
		String SumByFacm = titaVo.getParam("SumByFacm").trim();// 額度加總 Y N

		if (("1").equals(FunctionCd)) {
			if (WorkMonthFm != null && WorkMonthTo != null) {
				if (Integer.parseInt(WorkMonthFm) > Integer.parseInt(WorkMonthTo)) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "工作月起訖有誤");
				}
			}
		}

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;// 查全部

		String sqlL5952 = "";
		sqlL5952 += "SELECT  ";
		// sqlL5952+="BSOF.\"DeptCode\" AS \"部室別\" ";
		sqlL5952 += "BSOF.\"DepItem\" AS \"部室別名稱\" ";
		// sqlL5952+=",BSOF.\"DistCode\" AS \"區部代號\" ";
		// sqlL5952+=",BSOF.\"DistItem\" AS \"區部代號名稱\" ";
		// sqlL5952+=",BSOF.\"AreaCode\" AS \"區域中心\" ";
		// sqlL5952+=",CB.\"UnitItem\" AS \"區域中心名稱\" ";
		sqlL5952 += ",BSOF.\"Fullname\" AS \"房貸專員名稱\" ";
//		if(("Y").equals(SumByFacm)) {
//			sqlL5952+=",SumBsDetail.\"SumPerfCnt\" AS \"房貸專員件數\" ";
//			sqlL5952+=",SumBsDetail.\"SumPerfAmt\" AS \"房貸專員業績金額\" ";
//		}else {
		sqlL5952 += ",BSD.\"PerfCnt\" AS \"房貸專員件數\" ";
		sqlL5952 += ",BSD.\"PerfAmt\" AS \"房貸專員業績金額\" ";
//		}

		sqlL5952 += ",BSD.\"BsOfficer\" AS \"房貸專員\" ";
		sqlL5952 += ",CM.\"CustName\" AS \"戶名\" ";
		sqlL5952 += ",BSD.\"CustNo\" AS \"戶號\" ";
		sqlL5952 += ",BSD.\"FacmNo\" AS \"額度\" ";
		sqlL5952 += ",BSD.\"BormNo\" AS \"撥款序號\" ";
		sqlL5952 += ",BSD.\"DrawdownDate\" AS \"撥款日\" ";
		sqlL5952 += ",BSD.\"ProdCode\" AS \"商品代碼\" ";
		sqlL5952 += ",BSD.\"PieceCode\" AS \"計件代碼\" ";
		sqlL5952 += ",BSD.\"DrawdownAmt\" AS \"撥款金額\" ";
		sqlL5952 += ",BSD.\"WorkMonth\" AS \"工作月\" ";
		// sqlL5952+=",BSOF.\"GoalAmt\" AS \"目標金額\" ";
		// sqlL5952+=",ITD.\"DeptCode\" AS \"介紹人區部代號\" ";
		sqlL5952 += ",ICBDept.\"UnitItem\" AS \"介紹人區部名稱\" ";
		// sqlL5952+=",ITD.\"DistCode\" AS \"介紹人部室別\" ";
		sqlL5952 += ",ICBDist.\"UnitItem\" AS \"介紹人部室別名稱\" ";
		// sqlL5952+=",ITD.\"UnitCode\" AS \"介紹人區域中心\" ";
		sqlL5952 += ",ICBUnit.\"UnitItem\" AS \"介紹人區域中心名稱\" ";
		sqlL5952 += ",IEMP.\"Fullname\" AS \"介紹人姓名\" ";
		sqlL5952 += ",ITD.\"Introducer\" AS \"介紹人員編\" ";
		// sqlL5952+=",ITD.\"UnitManager\" AS \"介紹人區經理\" ";
		// sqlL5952+=",IEMPM.\"Fullname\" AS \"介紹人區經理名稱\" ";
		sqlL5952 += "FROM \"PfBsDetail\" BSD ";
		sqlL5952 += "LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\"= BSD.\"CustNo\" ";
		sqlL5952 += "LEFT JOIN \"PfBsOfficer\" BSOF ";
		sqlL5952 += "ON BSOF.\"WorkMonth\"=BSD.\"WorkMonth\" AND BSOF.\"EmpNo\"=BSD.\"BsOfficer\" ";
		// sqlL5952+="LEFT JOIN \"CdBcm\" CB ON CB.\"UnitCode\"=BSOF.\"AreaCode\" ";
		sqlL5952 += "LEFT JOIN \"PfItDetail\" ITD ";
		sqlL5952 += "ON ITD.\"PerfDate\"=BSD.\"PerfDate\" AND ITD.\"CustNo\"=BSD.\"CustNo\" AND ITD.\"FacmNo\"=BSD.\"FacmNo\" AND ITD.\"BormNo\"=BSD.\"BormNo\" ";
		sqlL5952 += "AND ITD.\"CntingCode\" IN ('Y','N') ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBUnit ON ICBUnit.\"UnitCode\"=ITD.\"UnitCode\" ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBDist ON ICBDist.\"UnitCode\"=ITD.\"DistCode\" ";
		sqlL5952 += "LEFT JOIN \"CdBcm\" ICBDept ON ICBDept.\"UnitCode\"=ITD.\"DeptCode\" ";
		sqlL5952 += "LEFT JOIN \"CdEmp\" IEMP ON IEMP.\"EmployeeNo\"=ITD.\"Introducer\" ";
		// sqlL5952+="LEFT JOIN \"CdEmp\" IEMPM ON
		// IEMPM.\"EmployeeNo\"=ITD.\"UnitManager\" ";

//		if(("Y").equals(SumByFacm)) {
//			String sqlSumL5952="SELECT \"BsOfficer\" AS \"BsOfficer\",SUM(NVL(\"PerfCnt\",0)) AS \"SumPerfCnt\",SUM(NVL(\"PerfAmt\",0)) AS \"SumPerfAmt\" ";
//			sqlSumL5952+= "FROM \"PfBsDetail\" ";
//			sqlSumL5952+= "WHERE \"BsOfficer\" IS NOT NULL ";
//			if(("1").equals(FunctionCd)) {
//				sqlSumL5952+="AND \"WorkMonth\" BETWEEN :WorkMonthFm AND :WorkMonthTo ";
//			}else if(("2").equals(FunctionCd)) {
//				sqlSumL5952+="AND \"CustNo\" = :CustNo ";
//				if(FacmNo!=null && FacmNo.length()!=0 && Integer.parseInt(FacmNo)!=0) {
//					sqlSumL5952+="AND \"FacmNo\" = :FacmNo ";
//				}
//			}
//			sqlSumL5952+= "group by \"BsOfficer\" ORDER BY \"BsOfficer\" ";
//			sqlL5952+="LEFT JOIN ("+sqlSumL5952+") SumBsDetail ON SumBsDetail.\"BsOfficer\"=BSD.\"BsOfficer\" ";
//		}

		Map<String, String> queryKey = new HashMap<String, String>();
//		if(("1").equals(FunctionCd)) {
		// 工作月
		int IntWorkMonthFm = l5051ServiceImpl.ChangeYM(WorkMonthFm);
		int IntWorkMonthTo = l5051ServiceImpl.ChangeYM(WorkMonthTo);
		queryKey.put("WorkMonthFm", String.valueOf(IntWorkMonthFm));
		queryKey.put("WorkMonthTo", String.valueOf(IntWorkMonthTo));
		// 工作月
		sqlL5952 += "WHERE BSD.\"WorkMonth\" BETWEEN :WorkMonthFm AND :WorkMonthTo ";

//		}else if(("2").equals(FunctionCd)) {
//			//戶號
//			queryKey.put("CustNo", CustNo);
//			sqlL5952+="AND BSD.\"CustNo\" = :CustNo ";
//			if(FacmNo!=null && FacmNo.length()!=0 && Integer.parseInt(FacmNo)!=0) {
//				queryKey.put("FacmNo", FacmNo);
//				sqlL5952+="AND BSD.\"FacmNo\" = :FacmNo ";
//			}
//		}else {
//			//E5009	資料檢核錯誤
//			throw new LogicException(titaVo, "E5009","[查詢方式("+FunctionCd+")]不存在");
//		}
		sqlL5952 += "ORDER BY BSD.\"BsOfficer\",BSD.\"CustNo\",BSD.\"FacmNo\",BSD.\"BormNo\" ";

		sqlL5952 += sqlRow;

		this.info("L5952 sqlL5952 sql=[" + sqlL5952 + "]");
		List<String[]> dataL5952 = l5951ServiceImpl.FindData(this.index, this.limit, sqlL5952, queryKey, titaVo);
		if (dataL5952 != null && dataL5952.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		String bsOfficer = "";
		String custNo = "";
		String facmNo = "";
		double perfCnt = 0;
		int perfAmt = 0;
		int drawdownAmt = 0;
		int subCnt = 0;

		if (dataL5952 != null && dataL5952.size() != 0) {
			for (String[] lData : dataL5952) {
				String OOBsDeptX = lData[0];// 部室名
				String OOBsOfficerName = lData[1];// 房貸專員名稱
				String OOPerfCnt = lData[2];// 件數
				if (OOPerfCnt == null || "".equals(OOPerfCnt)) {
					OOPerfCnt = "0";
				}
				String OOPerfAmt = lData[3];// 業績金額
				if (OOPerfAmt == null || "".equals(OOPerfAmt)) {
					OOPerfAmt = "0";
				}
				String OOBsOfficer = lData[4];// 房貸專員
				String OOCustNm = lData[5];// 戶名
				String OOCustNo = lData[6];// 戶號
				String OOFacmNo = lData[7];// 額度
				String OOBormNo = lData[8];// 撥款序號
				String OODrawdownDate = lData[9];// 撥款日
				String OOProdCode = lData[10];// 商品代碼
				String OOPieceCode = lData[11];// 計件代碼
				String OODrawdownAmt = lData[12];// 撥款金額
				if (OODrawdownAmt == null || "".equals(OODrawdownAmt)) {
					OODrawdownAmt = "0";
				}
				String OOWorkMonth = lData[13];// 工作月
				String OOIntDept = lData[14];// 區部名稱
				String OODistDept = lData[15];// 部室別名稱
				String OOUnitDept = lData[16];// 區域中心名稱
				String OOIntroducerName = lData[17];// 姓名
				String OOIntroducer = lData[18];// 員編

				// 顯示合計
				if (("Y").equals(SumByFacm)) {
					if ((!"".equals(bsOfficer) && !bsOfficer.equals(OOBsOfficer)) || (!"".equals(custNo) && !custNo.equals(OOCustNo)) || (!"".equals(facmNo) && !facmNo.equals(OOFacmNo))) {
						if (subCnt > 1) {
							subTotal(bsOfficer, custNo, facmNo, perfCnt, perfAmt, drawdownAmt);
						}

						perfCnt = 0;
						perfAmt = 0;
						drawdownAmt = 0;
						subCnt = 0;
					}
				}
				OccursList occursList1 = new OccursList();
				occursList1.putParam("OOBsDeptX", OOBsDeptX);// 部室名
				occursList1.putParam("OOBsOfficerName", OOBsOfficerName);// 房貸專員名稱
				occursList1.putParam("OOPerfCnt", numIsNull(OOPerfCnt));// 件數
				occursList1.putParam("OOPerfAmt", numIsNull(OOPerfAmt));// 業績金額
				occursList1.putParam("OOBsOfficer", OOBsOfficer);// 房貸專員
				occursList1.putParam("OOCustNm", OOCustNm);// 戶名
				occursList1.putParam("OOCustNo", OOCustNo);// 戶號
				occursList1.putParam("OOFacmNo", OOFacmNo);// 額度
				occursList1.putParam("OOBormNo", OOBormNo);// 撥款序號
				occursList1.putParam("OODrawdownDate", toRoc(OODrawdownDate));// 撥款日
				occursList1.putParam("OOProdCode", OOProdCode);// 商品代碼
				occursList1.putParam("OOPieceCode", OOPieceCode);// 計件代碼
				occursList1.putParam("OODrawdownAmt", numIsNull(OODrawdownAmt));// 撥款金額
				occursList1.putParam("OOWorkMonth", toRoc(OOWorkMonth));// 工作月
				occursList1.putParam("OOIntDept", OOIntDept);// 區部名稱
				occursList1.putParam("OODistDept", OODistDept);// 部室別名稱
				occursList1.putParam("OOUnitDept", OOUnitDept);// 區域中心名稱
				occursList1.putParam("OOIntroducerName", OOIntroducerName);// 姓名
				occursList1.putParam("OOIntroducer", OOIntroducer);// 員編
				this.totaVo.addOccursList(occursList1);

				subCnt++;
				bsOfficer = OOBsOfficer;
				custNo = OOCustNo;
				facmNo = OOFacmNo;
				perfCnt += Double.valueOf(OOPerfCnt);
				perfAmt += Integer.valueOf(OOPerfAmt);
				drawdownAmt += Integer.valueOf(OODrawdownAmt);
			}
			if (!"".equals(bsOfficer) && ("Y").equals(SumByFacm) && subCnt > 1) {
				subTotal(bsOfficer, custNo, facmNo, perfCnt, perfAmt, drawdownAmt);
			}
		} else {
			if (this.index != 0) {
				// 代表有多筆查詢,然後筆數剛好可以被整除

			} else {
				// E2003 查無資料
				throw new LogicException(titaVo, "E2003", "");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void subTotal(String officer, String custNo, String facmNo, double perfCnt, int perfAmt, int drawdownAmt) {
		this.info("L5952.subTotal=" + officer + "-" + custNo + "-" + facmNo);

		OccursList occursList1 = new OccursList();
		occursList1.putParam("OOBsDeptX", "");// 部室名
		occursList1.putParam("OOBsOfficerName", "");// 房貸專員名稱
		occursList1.putParam("OOPerfCnt", perfCnt);// 件數
		occursList1.putParam("OOPerfAmt", perfAmt);// 業績金額
		occursList1.putParam("OOBsOfficer", officer);// 房貸專員
		occursList1.putParam("OOCustNm", String.format("%07d-%03d", Integer.valueOf(custNo),Integer.valueOf(facmNo)) + "合計");// 戶名
		occursList1.putParam("OOCustNo", 0);// 戶號
		occursList1.putParam("OOFacmNo", 0);// 額度
		occursList1.putParam("OOBormNo", 0);// 撥款序號
		occursList1.putParam("OODrawdownDate", 0);// 撥款日
		occursList1.putParam("OOProdCode", "");// 商品代碼
		occursList1.putParam("OOPieceCode", "");// 計件代碼
		occursList1.putParam("OODrawdownAmt", drawdownAmt);// 撥款金額
		occursList1.putParam("OOWorkMonth", "");// 工作月
		occursList1.putParam("OOIntDept", "");// 區部名稱
		occursList1.putParam("OODistDept", "");// 部室別名稱
		occursList1.putParam("OOUnitDept", "");// 區域中心名稱
		occursList1.putParam("OOIntroducerName", "");// 姓名
		occursList1.putParam("OOIntroducer", "");// 員編

		this.totaVo.addOccursList(occursList1);
	}

	public Integer toRoc(String iDate) {
		int date = 0;
		if ("".equals(iDate) || iDate == null) {
			date = 0;

		} else {
			if (iDate.length() >= 7) {
				date = Integer.valueOf(iDate);
				if (date > 19110000) {
					date -= 19110000;
				}
			} else if (iDate.length() >= 5) {
				// 年月
				date = Integer.valueOf(iDate);
				if (date > 191100) {
					date -= 191100;
				}
			}
		}
		return date;
	}

	public String numIsNull(String num) {
		if (num != null && num.length() != 0) {

		} else {
			num = "0";
		}
		return num;
	}
}