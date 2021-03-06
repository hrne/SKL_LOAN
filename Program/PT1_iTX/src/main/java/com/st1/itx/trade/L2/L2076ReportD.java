package com.st1.itx.trade.L2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.util.common.CustNoticeCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2076ReportD")
@Scope("prototype")
public class L2076ReportD extends MakeReport {

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
	public LoanCom loanCom;
	@Autowired
	public CustNoticeCom custNoticeCom;

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

	private String security = "";
	private String pageOrientation = "P";
	private int iCustNo = 0;
	private int iCloseNo = 0;


	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2076ReportD.printHeader");
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

	public Boolean exec(FacClose tFacClose, String Addres, TitaVo titaVo) throws LogicException {

		this.info("L2076ReportB exec");

		exportPdf(tFacClose, Addres, titaVo);

		return true;
	}

	private void exportPdf(FacClose tFacClose, String Addres, TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2631D", "雙掛號信封", "", "L2631D_雙掛號信封.pdf");

		this.setFont(1);

		this.setFontSize(16);

		this.setCharSpaces(0);
		String custId = "";
		CustMain tCustMain = sCustMainService.custNoFirst(tFacClose.getCustNo(), tFacClose.getCustNo(), titaVo);
		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}
		String custName = loanCom.getCustNameByNo(tFacClose.getCustNo());
		if (custName.length() > 10) {
			custName = loanCom.getCustNameByNo(tFacClose.getCustNo()).substring(0, 10);
		}
		String telNo = "";
		if (tFacClose.getTelNo1().isEmpty()) {

			if (tFacClose.getTelNo2().isEmpty()) {
				telNo = tFacClose.getTelNo3();
			} else {
				telNo = tFacClose.getTelNo2();
			}
		} else {
			telNo = tFacClose.getTelNo1();
		}

		this.print(-33, 33, Addres); // 戶籍地址

		this.print(-35, 33, "#" + StringUtils.leftPad(String.valueOf(tFacClose.getCustNo()), 7, "0") + "  " + custName); // 戶號戶名
		this.setFontSize(14);
		this.print(-41, 52, telNo); // 電話
		this.setFontSize(16);
		long sno = this.close();

		this.toPdf(sno);
	}
}
