package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

/**
 * L4602Report
 * 
 * @author Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L4602Report extends MakeReport {

	@Autowired
	CdCodeService sCdCodeService;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L4602";
	String txName = "火險出單明細表";

	HashMap<String, String> repayCodeMap;

	HashMap<Integer, String> noticeFlagMap;

	public boolean exec(List<Map<String, Object>> listL4602, TitaVo titaVo) throws LogicException {

		if (listL4602.isEmpty()) {
			return false;
		}

		// 取得共用代碼檔
		getCdCodeMaps(titaVo);

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txcd, txName, txcd + "_" + txName);

		// 輸出表頭
		printL4602Header();

		// 輸出明細
		printDetail(listL4602);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

		return true;
	}

	private void getCdCodeMaps(TitaVo titaVo) {

		Slice<CdCode> sliceCdCodeRepayCode = sCdCodeService.getCodeListWithFlag("RepayCode", "Y", 0, 0, Integer.MAX_VALUE, titaVo);

		if (sliceCdCodeRepayCode != null) {

			List<CdCode> listCdCodeRepayCode = new ArrayList<>(sliceCdCodeRepayCode.getContent());

			if (listCdCodeRepayCode.size() > 0) {

				repayCodeMap = new HashMap<>();

				for (CdCode repayCode : listCdCodeRepayCode) {
					String code = repayCode.getCode();
					String item = repayCode.getItem();
					repayCodeMap.put(code, item);
				}
			}
		}

		noticeFlagMap = new HashMap<>();

		noticeFlagMap.put(1, "書信");
		noticeFlagMap.put(2, "簡訊");
		noticeFlagMap.put(3, "Email");
		noticeFlagMap.put(4, "不通知");
	}

	private void printDetail(List<Map<String, Object>> listL4602) throws LogicException {

		int rowCursor = 2;

		for (Map<String, Object> m : listL4602) {

			// 擔保品代號
			String clNo = "" + m.get("OOClCode1") + "-" + FormatUtil.pad9("" + m.get("OOClCode2"), 2) + "-" + FormatUtil.pad9("" + m.get("OOClNo"), 7);
			makeExcel.setValue(rowCursor, 1, clNo);

			// 原保險單號碼
			String prevInsuNo = "" + m.get("OOPrevInsuNo");
			makeExcel.setValue(rowCursor, 2, prevInsuNo);

			// 戶號
			String custNo = "" + FormatUtil.pad9("" + m.get("OOCustNo"), 7) + "-" + FormatUtil.pad9("" + m.get("OOFacmNo"), 3);
			makeExcel.setValue(rowCursor, 3, custNo);

			// 繳款方式
			String repayCode = "" + m.get("OORepayCodeX");

			String repayCodeItem = repayCodeMap.get(FormatUtil.pad9(repayCode, 2));

			if (repayCodeItem != null && !repayCodeItem.isEmpty()) {
				repayCode += repayCodeItem;
			}
			makeExcel.setValue(rowCursor, 4, repayCode);

			// 戶名
			String custName = "" + m.get("OOCustName");
			makeExcel.setValue(rowCursor, 5, custName);

			// 保險起日
			String newInsuStartDate = "" + m.get("OONewInsuStartDate");
			makeExcel.setValue(rowCursor, 6, showRocDate(newInsuStartDate, 1));

			// 保險迄日
			String newInsuEndDate = "" + m.get("OONewInsuEndDate");
			makeExcel.setValue(rowCursor, 7, showRocDate(newInsuEndDate, 1));

			// 火險保額
			String fireAmt = "" + m.get("OOFireAmt");
			makeExcel.setValue(rowCursor, 8, getBigDecimal(fireAmt), "#,##0");

			// 火險保費
			String fireFee = "" + m.get("OOFireFee");
			makeExcel.setValue(rowCursor, 9, getBigDecimal(fireFee), "#,##0");

			// 地震險保額
			String ethqAmt = "" + m.get("OOEthqAmt");
			makeExcel.setValue(rowCursor, 10, getBigDecimal(ethqAmt), "#,##0");

			// 地震險保費
			String ethqFee = "" + m.get("OOEthqFee");
			makeExcel.setValue(rowCursor, 11, getBigDecimal(ethqFee), "#,##0");

			// 總保費
			String totlFee = "" + m.get("OOTotlFee");
			makeExcel.setValue(rowCursor, 12, getBigDecimal(totlFee), "#,##0");

			// 通知方式
			String noticeWay = "" + m.get("OONoticeWay");
			String noticeWayItem = noticeFlagMap.get(Integer.parseInt(noticeWay));

			if (noticeWayItem != null && !noticeWayItem.isEmpty()) {
				noticeWay += noticeWayItem;
			}
			makeExcel.setValue(rowCursor, 13, noticeWay);

			rowCursor++;
		}
	}

	private void printL4602Header() throws LogicException {

		makeExcel.setValue(1, 1, "擔保品代號");
		makeExcel.setWidth(1, 16); // 欄寬

		makeExcel.setValue(1, 2, "原保險單號碼");
		makeExcel.setWidth(2, 17); // 欄寬

		makeExcel.setValue(1, 3, "戶號");
		makeExcel.setWidth(3, 15); // 欄寬

		makeExcel.setValue(1, 4, "繳款方式");
		makeExcel.setWidth(4, 12); // 欄寬

		makeExcel.setValue(1, 5, "戶名");
		makeExcel.setWidth(5, 30); // 欄寬

		makeExcel.setValue(1, 6, "保險起日");
		makeExcel.setWidth(6, 13); // 欄寬

		makeExcel.setValue(1, 7, "保險迄日");
		makeExcel.setWidth(7, 13); // 欄寬

		makeExcel.setValue(1, 8, "火險保額");
		makeExcel.setWidth(8, 13); // 欄寬

		makeExcel.setValue(1, 9, "火險保費");
		makeExcel.setWidth(9, 13); // 欄寬

		makeExcel.setValue(1, 10, "地震險保額");
		makeExcel.setWidth(10, 13); // 欄寬

		makeExcel.setValue(1, 11, "地震險保費");
		makeExcel.setWidth(11, 13); // 欄寬

		makeExcel.setValue(1, 12, "總保費");
		makeExcel.setWidth(12, 13); // 欄寬

		makeExcel.setValue(1, 13, "通知方式");
		makeExcel.setWidth(13, 13); // 欄寬
	}
}
