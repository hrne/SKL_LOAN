package com.st1.itx.trade.L2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.service.CdCityService;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdLandOfficeService;
import com.st1.itx.db.service.ClOtherRightsService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2076ReportC")
@Scope("prototype")
public class L2076ReportC extends MakeReport {

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
	public CdEmpService cdEmpService;

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
	private String reportItem = "簽收回條";
	private String defaultPdf = "";

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

	public Boolean exec(FacClose tFacClose, String selectTotal, TitaVo titaVo) throws LogicException {

		this.info("L2076ReportB exec");

		exportPdf(tFacClose, selectTotal, titaVo);

		return true;
	}

	private void exportPdf(FacClose tFacClose, String selectTotal, TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L2631C", "簽收回條", "", "L2631C_簽收回條.pdf");

		this.setFont(1);

		this.setFontSize(12);

		this.setCharSpaces(0);
		String custId = "";
		CustMain tCustMain = sCustMainService.custNoFirst(tFacClose.getCustNo(), tFacClose.getCustNo(), titaVo);
		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}
		// 查詢員工資料檔
		this.info("titaVo.getTlrNo() = " + titaVo.getTlrNo());
		CdEmp tCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
		String tlrName = "";
		if (tCdEmp != null) {
			tlrName = tCdEmp.getFullname();
		}
		String closeDate = parse.IntegerToString(tFacClose.getCloseDate(), 7); // 結清日期
		this.info("closeDate = " + closeDate);
		int wkCloseYy = parse.stringToInteger(closeDate.substring(0, 3)); // 年
		int wkCloseMm = parse.stringToInteger(closeDate.substring(3, 5)); // 月
		int wkCloseDd = parse.stringToInteger(closeDate.substring(5, 7)); // 日
		this.print(-4, 13, loanCom.getCustNameByNo(tFacClose.getCustNo())); // 戶名
		this.print(-4, 39, StringUtils.leftPad(String.valueOf(tFacClose.getCustNo()), 7, "0") + "-" + StringUtils.leftPad(String.valueOf(tFacClose.getFacmNo()), 3, "0")); // 戶號額度
		this.print(-4, 74, wkCloseYy + "/" + wkCloseMm + "/" + wkCloseDd); // 結清日期
		this.print(-12, 66, loanCom.getCustNameByNo(tFacClose.getCustNo())); // 戶名
		this.print(-10, 32, selectTotal); // 份數
		this.print(-13, 65, custId); // 統編
		this.print(-49, 74, tlrName); // 經辦

		long sno = this.close();

		this.toPdf(sno);
	}
}
