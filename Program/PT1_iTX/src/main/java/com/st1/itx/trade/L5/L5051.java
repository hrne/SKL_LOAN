package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L6932ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCd=9,1<br>
 * PerfDateFm=9,7<br>
 * PerfDateTo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 * END=X,1<br>
 */

@Service("L5051")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5051 extends TradeBuffer {
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	private L5051ServiceImpl l5051ServiceImpl;

	@Autowired
	L6932ServiceImpl l6932ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5051 ");
		this.totaVo.init(titaVo);

		String SumByFacm = titaVo.getParam("SumByFacm").trim();

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部

		if ("Y".equals(SumByFacm)) {
			this.limit = Integer.MAX_VALUE;// 查全部
		} else {
			this.limit = 40;// 每次40筆
		}
		List<Map<String, String>> L5051List = null;

		try {
			L5051List = l5051ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		if (L5051List == null || L5051List.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}

		this.info("L5051List.size() =" + L5051List.size());

		Map<String, String> dd = new HashMap<String, String>();

		String Introducer = "";
		String CustNo = "";
		String FacmNo = "";
		String WorkMonth = "";
		BigDecimal DrawdownAmt = new BigDecimal("0");
		BigDecimal PerfEqAmt = new BigDecimal("0");
		BigDecimal PerfReward = new BigDecimal("0");
		BigDecimal PerfAmt = new BigDecimal("0");
		int cnt = 0;
		Boolean first = true;

		for (Map<String, String> d : L5051List) {

			if ("Y".equals(SumByFacm)) {
				if (first || !Introducer.equals(d.get("Introducer").trim()) || !CustNo.equals(d.get("CustNo").trim())
						|| !FacmNo.equals(d.get("FacmNo").trim())) {
					if (!first) {
						putTota(dd, WorkMonth, DrawdownAmt, PerfEqAmt, PerfReward, PerfAmt, cnt, 1, SumByFacm, titaVo);
					}

					WorkMonth = d.get("WorkMonth").trim();
					DrawdownAmt = new BigDecimal("0");
					PerfEqAmt = new BigDecimal("0");
					PerfReward = new BigDecimal("0");
					PerfAmt = new BigDecimal("0");
					cnt = 0;
					first = false;
				}

				DrawdownAmt = DrawdownAmt.add(new BigDecimal(d.get("DrawdownAmt")));

				if ("0".equals(d.get("AdjRange")) || "9".equals(d.get("AdjRange"))) {
					PerfEqAmt = PerfEqAmt.add(new BigDecimal(d.get("PerfEqAmt")));
					PerfReward = PerfReward.add(new BigDecimal(d.get("PerfReward")));
					PerfAmt = PerfAmt.add(new BigDecimal(d.get("PerfAmt")));
				} else {
					PerfEqAmt = PerfEqAmt.add(new BigDecimal(d.get("AdjPerfEqAmt")));
					PerfReward = PerfReward.add(new BigDecimal(d.get("AdjPerfReward")));
					PerfAmt = PerfAmt.add(new BigDecimal(d.get("AdjPerfAmt")));
				}

				cnt++;

				Introducer = d.get("Introducer").trim();
				CustNo = d.get("CustNo").trim();
				FacmNo = d.get("FacmNo").trim();
				if (!WorkMonth.equals(d.get("WorkMonth").trim())) {
					WorkMonth = "";
				}
			} else {
				if ("0".equals(d.get("AdjRange")) || "9".equals(d.get("AdjRange"))) {
					PerfEqAmt = new BigDecimal(d.get("PerfEqAmt"));
					PerfReward = new BigDecimal(d.get("PerfReward"));
					PerfAmt = new BigDecimal(d.get("PerfAmt"));
				} else {
					PerfEqAmt = new BigDecimal(d.get("AdjPerfEqAmt"));
					PerfReward = new BigDecimal(d.get("AdjPerfReward"));
					PerfAmt = new BigDecimal(d.get("AdjPerfAmt"));
				}
				int canmodify = 0;
				if (d.get("MediaFg") == null || "".equals(d.get("MediaFg"))) {
					canmodify = 1;
				}
				putTota(d, d.get("WorkMonth"), new BigDecimal(d.get("DrawdownAmt")), PerfEqAmt, PerfReward, PerfAmt, 1,
						canmodify, SumByFacm, titaVo);
			}

			dd.clear();
			dd.putAll(d);
		}

		if ("Y".equals(SumByFacm)) {
			putTota(dd, WorkMonth, DrawdownAmt, PerfEqAmt, PerfReward, PerfAmt, cnt, 0, SumByFacm, titaVo);
		}

//		this.info("dd =" + dd.get("LogNo"));

		if (L5051List != null && L5051List.size() >= this.limit) {
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

	private void putTota(Map<String, String> d, String WorkMonth, BigDecimal DrawdownAmt, BigDecimal PerfEqAmt,
			BigDecimal PerfReward, BigDecimal PerfAmt, int cnt, int canModify, String SumByFacm, TitaVo titaVo)
			throws LogicException {
		OccursList occursList = new OccursList();

		occursList.putParam("OOLogNo", d.get("LogNo"));
		occursList.putParam("OOBsDeptName", d.get("BsDeptName"));// 部室中文(部室中文(房貸專員))
		occursList.putParam("OOOfficerName", d.get("OfficerName"));// 員工姓名(房貸專員)
		occursList.putParam("OOBsOfficer", d.get("BsOfficer"));// 員工代號(房貸專員)
		occursList.putParam("OOCustName", d.get("CustName"));// 戶名
		occursList.putParam("OOCustNo", d.get("CustNo"));// 戶號
		occursList.putParam("OOFacmNo", d.get("FacmNo"));// 額度編號
		occursList.putParam("OOBormNo", d.get("BormNo"));// 撥款序號
		occursList.putParam("OOPerfDate", d.get("DrawdownDate"));// 撥款日期
		occursList.putParam("OOProdCode", d.get("ProdCode"));// 商品代碼
		occursList.putParam("OOPieceCode", d.get("PieceCode"));// 計件代碼
		occursList.putParam("OODrawdownAmt", DrawdownAmt);// 撥款金額
		occursList.putParam("OODeptCode", d.get("DeptCode"));// 部室代號
		occursList.putParam("OODistCode", d.get("DistCode"));// 區部代號
		occursList.putParam("OOUnitCode", d.get("UnitCode"));// 單位代號
		occursList.putParam("OOItDeptName", d.get("ItDeptName"));// 部室中文
		occursList.putParam("OOItDistName", d.get("ItDistName"));// 區部中文
		occursList.putParam("OOItUnitName", d.get("ItUnitName"));// 單位中文
		occursList.putParam("OOIntroducer", d.get("Introducer"));// 員工代號(介紹人)
		occursList.putParam("OOIntroducerName", d.get("IntroducerName"));// 員工姓名(介紹人)
		occursList.putParam("OOUnitManagerName", d.get("UnitManagerName"));// 員工姓名(處經理姓名(介紹人))
		occursList.putParam("OODistManagerName", d.get("DistManagerName"));// 員工姓名(區經理姓名(介紹人))

		occursList.putParam("OOPerfEqAmt", PerfEqAmt);// 三階換算業績
		occursList.putParam("OOPerfReward", PerfReward);// 三階業務報酬
		occursList.putParam("OOPerfAmt", PerfAmt);// 三階業績金額
		occursList.putParam("OOCntingCode", d.get("CntingCode"));// 是否計件

//		if ("Y".equals(SumByFacm) && ("1".equals(d.get("AdjRange")) || "2".equals(d.get("AdjRange")))) {
//			occursList.putParam("OOPerfEqAmt", new BigDecimal(d.get("AdjPerfEqAmt")));// 三階換算業績
//			occursList.putParam("OOPerfReward", new BigDecimal(d.get("AdjPerfReward")));// 三階業務報酬
//			occursList.putParam("OOPerfAmt", new BigDecimal(d.get("AdjPerfAmt")));// 三階業績金額
//			occursList.putParam("OOCntingCode", d.get("adjCntingCode"));// 是否計件
//		} else {
//			occursList.putParam("OOPerfEqAmt", PerfEqAmt);// 三階換算業績
//			occursList.putParam("OOPerfReward", PerfReward);// 三階業務報酬
//			occursList.putParam("OOPerfAmt", PerfAmt);// 三階業績金額
//			occursList.putParam("OOCntingCode", d.get("CntingCode"));// 是否計件
//		}
		occursList.putParam("OOAdjRange", d.get("AdjRange"));
		occursList.putParam("OOCanModify", canModify);
		occursList.putParam("OORepayType", d.get("RepayType"));
		occursList.putParam("OOWorkMonth", WorkMonth);

//		occursList.putParam("OOLog", d.get("LOGCNT"));

		occursList.putParam("OOMediaDate", parse.stringToStringDate(d.get("MediaDate")));
		occursList.putParam("OOLastUpdate", parse.stringToStringDateTime(d.get("LastUpdate")));
		occursList.putParam("OOLastEmp", d.get("LastUpdateEmpNo") + " " + d.get("LastUpdateEmpName"));

		// L6932 用的參數
		titaVo.putParam("ST_DT", "0010101");
		titaVo.putParam("ED_DT", "9991231");
		titaVo.putParam("SX_DT", "0");
		titaVo.putParam("EX_DT", "0");
		titaVo.putParam("TRN_CODE", "L5501");
		titaVo.putParam("TRN_CODE2", "");
		titaVo.putParam("CUST_NO", d.get("CustNo"));
		titaVo.putParam("FACM_NO", d.get("FacmNo"));
		titaVo.putParam("BORM_SEQ", "000");
		titaVo.putParam("TxtNo", "");
		titaVo.putParam("MrKey", "");

		List<Map<String, String>> l6932Vo;

		try {
			l6932Vo = l6932ServiceImpl.FindData(titaVo, this.index, 1);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L6932ServiceImpl.findAll error = " + errors.toString());
			throw new LogicException("E0013", "L6932ServiceImpl");
		}

		occursList.putParam("OOHasHistory", l6932Vo != null && !l6932Vo.isEmpty() ? "Y" : "N");

		this.totaVo.addOccursList(occursList);
	}
}