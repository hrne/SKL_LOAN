package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.service.springjpa.cm.L5052ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.L5052Vo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCd=9,1<br>
 * PerfDateFm=9,7<br>
 * PerfDateTo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 */

@Service("L5052")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5052 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L5052ServiceImpl l5052ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5052 ");
		this.totaVo.init(titaVo);

		String SumByFacm = titaVo.getParam("SumByFacm").trim();

//		String L5052Sql = "";
//		List<Map<String,String>> L5052VoList = null;
//		try {
//			L5052Sql = l5052ServiceImpl.FindL5052(FunctionCd, PerfDateFm, PerfDateTo, CustNo, FacmNo);
//		} catch (Exception e) {
//			// E5003 組建SQL語法發生問題
//			this.info("L5051 ErrorForSql=" + e);
//			throw new LogicException(titaVo, "E5003", "");
//		}
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部
		if ("Y".equals(SumByFacm)) {
			this.limit = Integer.MAX_VALUE;// 查全部
		} else {
			this.limit = 40;// 每次40筆
		}
		List<Map<String, String>> L5052List = null;

		try {
			L5052List = l5052ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		if (L5052List == null || L5052List.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}

		this.info("L5052List.size() =" + L5052List.size());

		String BsOfficer = "";
		String CustNo = "";
		String FacmNo = "";
		String WorkMonth = "";
		BigDecimal PerfCnt = new BigDecimal("0");
		BigDecimal PerfAmt = new BigDecimal("0");
		BigDecimal DrawdownAmt = new BigDecimal("0");
		int cnt = 0;
		Boolean first = true;

		Map<String, String> dd = new HashMap<String, String>();

		for (Map<String, String> d : L5052List) {
			Long adjLogNo = Long.valueOf(d.get("AdjLogNo").toString());
			Long adjWorkMonth = Long.valueOf(d.get("AdjWorkMonth").toString());

			BigDecimal cntPerfCnt = BigDecimal.ZERO;
			BigDecimal cntPerfAmt = BigDecimal.ZERO;

			if (adjLogNo > 0 && adjWorkMonth > 0) {
				cntPerfCnt = new BigDecimal(d.get("AdjPerfCnt"));
				cntPerfAmt = new BigDecimal(d.get("AdjPerfAmt"));
			} else {
				cntPerfCnt = new BigDecimal(d.get("PerfCnt"));
				cntPerfAmt = new BigDecimal(d.get("PerfAmt"));
			}

			if ("Y".equals(SumByFacm)) {
				if (first || !BsOfficer.equals(d.get("BsOfficer").trim()) || !CustNo.equals(d.get("CustNo").trim()) || !FacmNo.equals(d.get("FacmNo").trim())) {
					if (!first) {
						putTota(dd, WorkMonth, PerfCnt, PerfAmt, DrawdownAmt, 1, SumByFacm);
					}

					WorkMonth = d.get("WorkMonth").trim();
					PerfCnt = new BigDecimal("0");
					PerfAmt = new BigDecimal("0");
					DrawdownAmt = new BigDecimal("0");
					cnt = 0;
					first = false;
				}

//				PerfCnt = PerfCnt.add(new BigDecimal(d.get("PerfCnt")));
//				PerfAmt = PerfAmt.add(new BigDecimal(d.get("PerfAmt")));
				PerfCnt = PerfCnt.add(cntPerfCnt);
				PerfAmt = PerfAmt.add(cntPerfAmt);

				DrawdownAmt = DrawdownAmt.add(new BigDecimal(d.get("DrawdownAmt")));

				cnt++;

				BsOfficer = d.get("BsOfficer").trim();
				CustNo = d.get("CustNo").trim();
				FacmNo = d.get("FacmNo").trim();
				if (!WorkMonth.equals(d.get("WorkMonth").trim())) {
					WorkMonth = "";
				}
			} else {
				putTota(d, d.get("WorkMonth"), cntPerfCnt, cntPerfAmt, new BigDecimal(d.get("DrawdownAmt")), 0, SumByFacm);
			}

			dd.clear();
			dd.putAll(d);

		}

		if ("Y".equals(SumByFacm)) {
			putTota(dd, WorkMonth, PerfCnt, PerfAmt, DrawdownAmt, 1, SumByFacm);
		}

		if (L5052List != null && L5052List.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			if ("Y".equals(SumByFacm)) {
				this.totaVo.setMsgEndToAuto();// 自動折返
			} else {
				this.totaVo.setMsgEndToEnter();// 手動折返
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void putTota(Map<String, String> d, String WorkMonth, BigDecimal PerfCnt, BigDecimal PerfAmt, BigDecimal DrawdownAmt, int canModify, String SumByFacm) {
		OccursList occursList = new OccursList();

		occursList.putParam("OOLogNo", d.get("LogNo"));
		occursList.putParam("OOBsDeptName", d.get("BsDeptName"));// 部室中文(部室中文(房貸專員))
		occursList.putParam("OOOfficerName", d.get("OfficerName"));// 員工姓名(房貸專員)
		occursList.putParam("OOBsOfficer", d.get("BsOfficer"));// 員工代號(房貸專員)

		int AdjFg = 0;
		int LogFg = 0;
		if ("N".equals(SumByFacm)) {
			Long AdjLogNo = Long.valueOf(d.get("AdjLogNo").toString());

			if (AdjLogNo > 0) {
				LogFg = 1;
				int AdjWorkMonth = Integer.valueOf(d.get("AdjWorkMonth").toString());
				if (AdjWorkMonth > 0) {
					AdjFg = 1;
				} else {
					AdjFg = 0;
				}
			}

		}

		occursList.putParam("OOAdjFg", AdjFg);
		occursList.putParam("OOPerfCnt", PerfCnt);// 計件件數
		occursList.putParam("OOPerfAmt", PerfAmt);// 業績金額

		occursList.putParam("OOCustName", d.get("CustName"));// 戶名
		occursList.putParam("OOCustNo", d.get("CustNo"));// 戶號
		occursList.putParam("OOFacmNo", d.get("FacmNo"));// 額度編號
		occursList.putParam("OOBormNo", d.get("BormNo"));// 撥款序號

		occursList.putParam("OOPerfDate", d.get("PerfDate"));// 撥款日期

		occursList.putParam("OOProdCode", d.get("ProdCode"));// 商品代碼
		occursList.putParam("OOPieceCode", d.get("PieceCode"));// 計件代碼
		occursList.putParam("OODrawdownAmt", DrawdownAmt);// 撥款金額
		occursList.putParam("OOWorkMonth", WorkMonth);// 工作月

//			occursList.putParam("OODeptCode", d.get("DeptCode"));// 部室代號
//			occursList.putParam("OODistCode", d.get("DistCode"));// 區部代號
//			occursList.putParam("OOUnitCode", d.get("UnitCode"));// 單位代號
		occursList.putParam("OOItDeptName", d.get("ItDeptName"));// 部室中文
		occursList.putParam("OOItDistName", d.get("ItDistName"));// 區部中文
		occursList.putParam("OOItUnitName", d.get("ItUnitName"));// 單位中文
		occursList.putParam("OOIntroducer", d.get("Introducer"));// 員工代號(介紹人)
		occursList.putParam("OOIntroducerName", d.get("IntroducerName"));// 員工姓名(介紹人)

		occursList.putParam("OOCanModify", 1);
		occursList.putParam("OORepayType", d.get("RepayType"));
		occursList.putParam("OOLog", LogFg);

		occursList.putParam("OOLastUpdate", parse.stringToStringDateTime(d.get("LastUpdate")));
		occursList.putParam("OOLastEmp", d.get("LastUpdateEmpNo") + " " + d.get("LastUpdateEmpName"));

		this.totaVo.addOccursList(occursList);

	}

}