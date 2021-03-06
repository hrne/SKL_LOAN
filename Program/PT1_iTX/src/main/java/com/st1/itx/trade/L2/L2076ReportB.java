package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2076ReportB")
@Scope("prototype")
public class L2076ReportB extends MakeReport {

	@Autowired
	public ClOtherRightsService sClOtherRightsService;
	@Autowired
	public CdCityService cdCityService;
	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public CdLandOfficeService cdLandOfficeService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public FacCloseService sFacCloseService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public ClFacService clFacService;
	@Autowired
	public LoanCom loanCom;

	@Autowired
	DateUtil dDateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// ReportDate : 報表日期(帳務日)
	// ReportCode : 報表編號
	// ReportItem : 報表名稱
	// Security : 報表機密等級(中文敍述)
	// PageSize : 紙張大小;
	// PageOrientation : 紙張方向
	// P:Portrait Orientation (直印) , L:Landscape Orientation(橫印)
	private int reportDate = 0;
	private String brno = "";
	private String reportCode = "L2076";
	private String reportItem = "用印申請書";
	private String defaultPdf = "";
	private TempVo t2TempVo = new TempVo();

	private String security = "";
	private String pageOrientation = "P";
	private int iCustNo = 0;
	private int iCloseNo = 0;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2076ReportB.printHeader");

		this.print(-4, 6, "", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(5);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-15, 25, " ");
	}

	public Boolean exec(FacClose tFacClose, List<TxTemp> txTemp, TitaVo titaVo) throws LogicException {

		this.info("L2076ReportB exec");

		exportPdf(tFacClose, txTemp, titaVo);

		return true;
	}

	private void exportPdf(FacClose tFacClose, List<TxTemp> txTemp, TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2631B", "用印申請書", "", "L2631B_用印申請書.pdf");

		this.setFont(1);

		this.setFontSize(12);

		this.setCharSpaces(0);
		String custId = "";
		CustMain tCustMain = sCustMainService.custNoFirst(tFacClose.getCustNo(), tFacClose.getCustNo(), titaVo);
		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}

		String wkSysDate = titaVo.getCalDy(); // 系統日國曆
		this.info("wkSysDate = " + wkSysDate);

		int wkYy = parse.stringToInteger(wkSysDate.substring(0, 3)); // 年
		int wkMm = parse.stringToInteger(wkSysDate.substring(3, 5)); // 月
		int wkDd = parse.stringToInteger(wkSysDate.substring(5, 7)); // 日
		String closeDate = parse.IntegerToString(tFacClose.getCloseDate(), 7); // 結清日期
		this.info("closeDate = " + closeDate);
		int wkCloseYy = parse.stringToInteger(closeDate.substring(0, 3)); // 年
		int wkCloseMm = parse.stringToInteger(closeDate.substring(3, 5)); // 月
		int wkCloseDd = parse.stringToInteger(closeDate.substring(5, 7)); // 日

		this.info("wkYymmdd = " + wkYy + "/" + wkMm + "/" + wkDd);
		this.print(-17, 86, "" + wkYy); // 年
		this.print(-20, 87, "" + wkMm); // 月
		this.print(-23, 87, "" + wkDd); // 日
		if (tFacClose.getFacmNo() > 0) {
			this.print(-31, 42,
					StringUtils.leftPad(String.valueOf(tFacClose.getCustNo()), 7, "0") + "-" + tFacClose.getFacmNo()); // 戶號額度
		} else {
			Slice<FacMain> slFacMain = facMainService.facmCustNoRange(tFacClose.getCustNo(), tFacClose.getCustNo(), 0,
					999, 0, Integer.MAX_VALUE, titaVo);
			List<FacMain> lFacMain = slFacMain == null ? null : slFacMain.getContent();
			String facms = "";
			String x = "";
			for (FacMain facMain : lFacMain) {
				facms += x + facMain.getFacmNo();
				x = ".";
			}

			this.print(-31, 42, StringUtils.leftPad(String.valueOf(tFacClose.getCustNo()), 7, "0") + "-" + facms); // 戶號額度
		}
		this.print(-33, 42, loanCom.getCustNameByNo(tFacClose.getCustNo())); // 戶名
		this.print(-36, 42, custId); // 統編
		int i = 0;
		for (TxTemp tTxTemp : txTemp) {
			i++;
			t2TempVo = new TempVo();
			t2TempVo = t2TempVo.getVo(tTxTemp.getText());
			int txCustNo = parse.stringToInteger(t2TempVo.getParam("CustNo"));
			int txFacmNo = parse.stringToInteger(t2TempVo.getParam("FacmNo"));
			int txClCode1 = parse.stringToInteger(t2TempVo.getParam("ClCode1"));
			int txClCode2 = parse.stringToInteger(t2TempVo.getParam("ClCode2"));
			int txClNo = parse.stringToInteger(t2TempVo.getParam("ClNo"));
			ClFac t2ClFac = new ClFac();
			t2ClFac = clFacService.clMainFirst(txClCode1, txClCode2, txClNo, "Y", titaVo);
			String amtChinese = "";
			// 設定金額
			BigDecimal wkOriSettingAmt = BigDecimal.ZERO;
			if (t2ClFac != null) {
				wkOriSettingAmt = t2ClFac.getOriSettingAmt();
				amtChinese = this.convertAmtToChinese(wkOriSettingAmt);
			}
			if (txTemp.size() >= 2) {
				switch (i) {
				case 1:
					this.print(-37, 42, amtChinese + " 元整"); // 設定金額
					break;
				case 2:
					this.print(-38, 42, amtChinese + " 元整"); // 設定金額
					break;
				case 3:
					this.print(-39, 42, amtChinese + " 元整"); // 設定金額
					break;
				case 4:
					this.print(-40, 42, amtChinese + " 元整"); // 設定金額
					break;
				}
			}else {
				this.print(-39, 42, amtChinese + " 元整"); // 設定金額
			}
		}
		this.print(-41, 42, wkCloseYy + "/" + wkCloseMm + "/" + wkCloseDd); // 結清日期

		long sno = this.close();

		this.toPdf(sno);
	}
}
