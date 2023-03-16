package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBranchGroup;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdBranchGroupService;
import com.st1.itx.db.service.CdBranchService;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.db.service.springjpa.cm.L9739ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9741ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SortMapListCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L9R99")
@Scope("prototype")
/**
 * L9R99 隨機報表檢查
 * 
 * @author
 * @version 1.0.0
 */
public class L9R99 extends TradeBuffer {

	@Autowired
	L9739ServiceImpl l9739ServiceImpl;

	@Autowired
	L9741ServiceImpl l9741ServiceImpl;

	/* DB服務注入 */
	@Autowired
	private CdReportService sCdReportService;

	@Autowired
	private CdBranchGroupService sCdBranchGroupService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	private Parse parse;

	private int no = 0;

	String txcd = "L9R99";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);

		this.totaVo.init(titaVo);

		String tranCode = titaVo.getParam("tranNo");

		this.info("tranCode=" + tranCode);

		switch (tranCode) {
		case "L9739":
			try {
				l9739DataProcessing(titaVo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case "L9741":
			l9741DataProcessing(titaVo);
			break;

		case "L9801":
		case "L9802":
		case "L9803":
		case "L9804":
		case "L9805":
		case "L9806":
			l98XXDataProcessing(titaVo, tranCode);

			break;

		default:
			break;
		}

		// 帳務日(西元)
//		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
//		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上月底日(西元)
//		int mfbsdsy = this.txBuffer.getTxCom().getLmndyf();

//		int iYearMonth = 0;
//
//		// 帳務日==月底日
//		if (tbsdy == mfbsdy) {
//			iYearMonth = mfbsdy / 100;
//		}
//		// 帳務日< 月底日 取上的月底
//		if (tbsdy < mfbsdy) {
//			iYearMonth = mfbsdsy / 100;
//		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void l9739DataProcessing(TitaVo titaVo) throws JSONException {

		List<Map<String, String>> listL9739 = null;

		try {

			listL9739 = l9739ServiceImpl.findStandard(titaVo, titaVo.getEntDyI() + 19110000);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		this.info("listL9739Title =" + listL9739.size());

		// 政府補貼利率前期
		BigDecimal lastRate = BigDecimal.ZERO;
		// 政府補貼利率當期
		BigDecimal thisRate = BigDecimal.ZERO;
		// 前期-當期
		BigDecimal diffRate = BigDecimal.ZERO;
		// 郵局利率
		BigDecimal basePostRate = BigDecimal.ZERO;
		// 商品加碼
		BigDecimal prodIncrRate = BigDecimal.ZERO;
		// 使用利率
		BigDecimal rate = BigDecimal.ZERO;

		JSONObject thisJS;
		JSONObject lastJS;

		for (int i = 0; i < listL9739.size(); i++) {
			int t = i + 1;

			this.totaVo.putParam("OOProdNo" + t, listL9739.get(i).get("ProdNo"));
			this.totaVo.putParam("OOProdName" + t, listL9739.get(i).get("ProdName"));
			this.totaVo.putParam("OOEffectDate" + t, Integer.valueOf(listL9739.get(i).get("EffectDate")) - 19110000);

			int seq = Integer.valueOf(listL9739.get(i).get("Seq"));

			switch (seq) {
			// ID、IE同一組利率
			case 4:
			case 5:
				seq = 4;
				break;
			// IF、IG同一組利率
			case 6:
			case 7:
				seq = 5;
				break;
			// IH、II同一組利率
			case 8:
			case 9:
				seq = 6;
				break;
			default:
				break;
			}

			this.info("seq =" + seq);

			prodIncrRate = new BigDecimal(listL9739.get(i).get("ProdIncr").toString());
			basePostRate = new BigDecimal(listL9739.get(i).get("BaseRate").toString());

			this.info("prodIncrRate =" + prodIncrRate);
			this.info("basePostRate =" + basePostRate);

			if (!"0".equals(listL9739.get(i).get("JsonFields").toString())) {
				this.info("listL9739.get(i).get(\"JsonFields\").toString() ="
						+ listL9739.get(i).get("JsonFields").toString());
				thisJS = new JSONObject(listL9739.get(i).get("JsonFields").toString());
				thisRate = new BigDecimal(thisJS.get("SubsidyRate" + seq).toString());
			}

			if (!"0".equals(listL9739.get(i).get("lJsonFields").toString())) {

				this.info("listL9739.get(i).get(\"lJsonFields\").toString() ="
						+ listL9739.get(i).get("lJsonFields").toString());
				lastJS = new JSONObject(listL9739.get(i).get("lJsonFields").toString());
				lastRate = new BigDecimal(lastJS.get("SubsidyRate" + seq).toString());
			}

			this.info("thisRate =" + thisRate);
			this.info("lastRate =" + lastRate);

			diffRate = lastRate.subtract(thisRate);

			this.info("diffRate =" + diffRate);

			rate = basePostRate.add(prodIncrRate).add(diffRate).setScale(3, RoundingMode.HALF_DOWN);

			this.totaVo.putParam("OOFitRate" + t, rate);

		}
	}

	private void l9741DataProcessing(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listL9741 = null;

		try {

			listL9741 = l9741ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		this.info("listL9741 =" + listL9741.size());

		int intInsuYearMonth = parse.stringToInteger(titaVo.getParam("InsuYearMonth"));
		int intCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int SearchOption = parse.stringToInteger(titaVo.getParam("SearchOption"));// 1:正常未繳;2:借支;3:催收未繳;9:全部

		this.info("intInsuYearMonth=" + intInsuYearMonth);
		this.info("intCustNo=" + intCustNo);
		this.info("SearchOption=" + SearchOption);

		if (listL9741.size() == 0) {
			this.totaVo.putParam("OOInsuYearMonth", intInsuYearMonth);
			this.totaVo.putParam("OOCustNo", "0000000");
			this.totaVo.putParam("OOSearchOption", SearchOption);
			this.addList(this.totaVo);

			throw new LogicException(titaVo, "E0001", "查無資料");
		} else {
			this.totaVo.putParam("OOInsuYearMonth", intInsuYearMonth);
			this.totaVo.putParam("OOCustNo", intCustNo);
			this.totaVo.putParam("OOSearchOption", SearchOption);
		}

	}

	private void l98XXDataProcessing(TitaVo titaVo, String tranCode) throws LogicException {

		Slice<CdBranchGroup> sCdBranchGroup;
		sCdBranchGroup = sCdBranchGroupService.findByBranchNo("0000", this.index, Integer.MAX_VALUE, titaVo);
		List<CdBranchGroup> lCdBranchGroup = sCdBranchGroup == null ? null : sCdBranchGroup.getContent();

		// 查無資料
		if (lCdBranchGroup == null || lCdBranchGroup.size() == 0) {
			throw new LogicException(titaVo, "E0001", "報表代號對照檔");
		}

		for (int i = 1; i <= lCdBranchGroup.size(); i++) {
			this.totaVo.putParam("OOGroupNo" + i, i);
		}
		this.totaVo.putParam("OOGroupNoAllSize", lCdBranchGroup.size());

		String ld = "LD%";
		String lh = "LH%";
		String lm = "LM%";
		String lp = "LP%";
		String lq = "LQ%";
		String lw = "LW%";
		String ly = "LY%";
		/*
		 * 放款管理課：1 放款服務課：2 放款推展課：3 放款審查課：4 投資資訊規劃課：5 專案管理課：6 軟體測試課：7
		 */
		String g1 = "1";
		String g2 = "2";
		String g3 = "3";

		if ("L9801".equals(tranCode)) {
			findReport(titaVo, g2, 1);
			fullColumn(this.no);
		}
		if ("L9802".equals(tranCode)) {
			findReport(titaVo, g3, 3);
			fullColumn(this.no);
		}
		if ("L9803".equals(tranCode)) {
			// 1 放款管理課
			findReport(titaVo, g1, 2);
			fullColumn(this.no);

			// 2放款服務課
			findReport(titaVo, g2, 2);
			fullColumn(this.no);

			// 3放款審查課
			findReport(titaVo, g3, 2);
			fullColumn(this.no);

		}
		if ("L9804".equals(tranCode)) {
			findReport(titaVo, g3, 4);
			findReport(titaVo, g2, 4);
			fullColumn(this.no);
		}
		if ("L9805".equals(tranCode)) {
			findReport(titaVo, g1, 5);
			fullColumn(this.no);
		}
		if ("L9806".equals(tranCode)) {
			findReport(titaVo, g1, 6);
			fullColumn(this.no);
		}

		this.totaVo.putParam("OOTotalRptSize", this.no);

	}

	/**
	 * 每課組別的報表目前上限設定為50份
	 */
	private void fullColumn(int nowNo) {

		int maxNo = 50;
		if (nowNo > maxNo) {
			nowNo = nowNo % maxNo;
		} else if (nowNo == maxNo) {
			return;
		}
		for (int i = nowNo + 1; i <= maxNo; i++) {
			this.no++;
			this.totaVo.putParam("OOTradeCode" + this.no, " ");
			this.totaVo.putParam("OOTradeName" + this.no, " ");
			this.totaVo.putParam("OOTradeSub" + this.no, 0);
			this.totaVo.putParam("OOGroupNo" + this.no, 0);
		}

	}
	/**
	 * 查詢報表清單
	 *  @param titaVo
	 *  @param groupNo 課組別
	 *  @param cycle 報表週期
	 **/
	private void findReport(TitaVo titaVo, String groupNo, int cycle) throws LogicException {
		int sTradeSub = 0;
		Slice<CdReport> slCdReport;
		// 1 放款服務課

		slCdReport = sCdReportService.findRptCycleGrp(cycle, groupNo, this.index, Integer.MAX_VALUE, titaVo);
		List<CdReport> lCdReport = slCdReport == null ? null : slCdReport.getContent();

		// 查無資料
		if (lCdReport == null || lCdReport.size() == 0) {
			throw new LogicException(titaVo, "E0001", "報表代號對照檔");
		}

		// 如有找到資料
		for (CdReport tCdReport : lCdReport) {

			this.no++;

			this.totaVo.putParam("OOTradeCode" + this.no, tCdReport.getFormNo());
			this.totaVo.putParam("OOTradeName" + this.no, tCdReport.getFormName());
			sTradeSub = tCdReport.getFormNo().length() > 5 ? Integer.valueOf(tCdReport.getFormNo().substring(6, 7)) : 0;
			this.totaVo.putParam("OOTradeSub" + this.no, sTradeSub);
			this.totaVo.putParam("OOGroupNo" + this.no, tCdReport.getGroupNo());

		}
	}

}