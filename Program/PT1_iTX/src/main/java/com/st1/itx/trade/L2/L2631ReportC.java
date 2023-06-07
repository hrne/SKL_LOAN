package com.st1.itx.trade.L2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.ClBuilding;
import com.st1.itx.db.domain.ClBuildingId;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.ClBuildingService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component("L2631ReportC")
@Scope("prototype")
public class L2631ReportC extends MakeReport {

	@Autowired
	CdEmpService cdEmpService;
	@Autowired
	CustMainService custMainService;
	@Autowired
	FacMainService facMainService;
	@Autowired
	ClFacService clFacService;
	@Autowired
	ClBuildingService clBuildingService;
	@Autowired
	ClLandService clLandService;
	@Autowired
	CdCodeService cdCodeService;

	@Autowired
	LoanCom loanCom;

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
	private String reportCode = "L2631-C";
	private String reportItem = "清償件檔案文件借出單";
	private String defaultPdf = "";
	private TempVo t2TempVo = new TempVo();

	private String security = "機密";
	private String pageSize = "A4";
	private String pageOrientation = "P";

//	// 製表日期
//	private String NowDate;
//	// 製表時間
//	private String NowTime;

	// 自訂表頭
	@Override
	public void printHeader() {

		this.info("L2631ReportC.printHeader");

		this.print(1, 8, "清償件檔案文件借出單");
		this.print(0, 76, "機密等級：" + this.security, "R");
		this.print(-4, 6, "", "L");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(4);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(80);

	}

	// 自訂表尾
	@Override
	public void printFooter() {
		this.print(-15, 25, " ");

//		this.print(-15, 25, "放款部部章：　　　　　　　　　　　　　　　　　　　經辦：" + this.titaVo.getTlrNo());
	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L2631ReportC exec");

		int iCustNo = parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(reportCode).setRptItem(reportItem).setSecurity(security).setRptSize(pageSize)
				.setPageOrientation(pageOrientation).build();
		this.open(titaVo, reportVo);
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), reportCode, reportItem, "", pageSize, pageOrientation);
		this.setFont(1);

		this.setFontSize(12);

		this.setCharSpaces(0);

		CdEmp iCdEmp = cdEmpService.findById(titaVo.getTlrNo(), titaVo);
		String tlrNoX = titaVo.getTlrNo();
		if (iCdEmp != null) {
			tlrNoX = iCdEmp.getFullname();
		}
		CustMain tCustMain = custMainService.custNoFirst(iCustNo, iCustNo, titaVo);
		String custId = "";
		if (tCustMain != null) {
			custId = tCustMain.getCustId();
		}

		int wkApplNo = 0;
		FacMain tFacMain = new FacMain();
		// 額度為0時抓最後一個額度出來
		if (iFacmNo == 0) {
			tFacMain = facMainService.findLastFacmNoFirst(iCustNo, titaVo);
		} else {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		}
		if (tFacMain != null) {
			wkApplNo = tFacMain.getApplNo();
		}
		Slice<ClFac> slClFac = clFacService.approveNoEq(wkApplNo, 0, Integer.MAX_VALUE, titaVo);
		int clCode1 = 0;
		int clCode2 = 0;
		int clNo = 0;
		if (slClFac != null) {
			for (ClFac t : slClFac.getContent()) {
				if ("Y".equals(t.getMainFlag())) {
					clCode1 = t.getClCode1();
					clCode2 = t.getClCode2();
					clNo = t.getClNo();
				}
			}
		}
		// 抓取門牌號碼
		String bdLocation = "";
		if (clCode1 == 1) {
			ClBuildingId clBuildingId = new ClBuildingId();
			clBuildingId.setClCode1(clCode1);
			clBuildingId.setClCode2(clCode2);
			clBuildingId.setClNo(clNo);
			ClBuilding tClBuilding = new ClBuilding();
			tClBuilding = clBuildingService.findById(clBuildingId, titaVo);
			if (tClBuilding != null) {
				bdLocation = tClBuilding.getBdLocation() + "，建號" + tClBuilding.getBdNo1() + "-"
						+ tClBuilding.getBdNo2();
			}
		} else if (clCode1 == 2) {
			ClLandId clLandId = new ClLandId();
			clLandId.setClCode1(clCode1);
			clLandId.setClCode2(clCode2);
			clLandId.setClNo(clNo);
			clLandId.setLandSeq(0);
			ClLand tClLand = new ClLand();
			tClLand = clLandService.findById(clLandId, titaVo);
			if (tClLand != null) {
				bdLocation = tClLand.getLandLocation();
			}
		}
		String collectWayCode = "";
		CdCode tCdCode = cdCodeService.findById(new CdCodeId("CollectWayCode", titaVo.getParam("CollectWayCode")),
				titaVo);
		if (tCdCode != null) {
			collectWayCode = tCdCode.getItem();
		}

		this.print(1, 8, "茲有借款戶：　" + FormatUtil.padX(loanCom.getCustNameByNo(iCustNo), 20)); // 戶名
		this.print(0, 40, "戶號：　" + parse.IntegerToString(iCustNo, 7) + "-" + parse.IntegerToString(iFacmNo, 3)); // 戶號
		this.print(1, 1, "");
		this.print(1, 8, "押品：　" + bdLocation); // 結清日期
		this.print(1, 1, "");
		this.print(1, 8, "因全部清償，擬借出下列文件辦理相關手續，待客戶簽收後再以領回條歸檔。");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 8, "１.　他項權利證明書正本　一份。");
		this.print(1, 1, "");
		this.print(1, 8, "２.　抵押權設定契約書正本　一份。");
		this.print(1, 1, "");
		this.print(1, 8, "３.　其他約定事項書正本　一份。");
		this.print(1, 1, "");
		this.print(1, 8, "４.　火險單正本　一份。");
		this.print(1, 1, "");
		this.print(1, 8, "５.　其他。　　說明：");
		this.print(1, 1, "");
		this.print(1, 8, "６.　備註：");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 25, "此致　　　檔案室");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 1, "");
		this.print(1, 60, "借戶經辦：　" + tlrNoX);
		this.print(1, 1, "");
		this.print(1, 60, "借出日期：　" + this.showRocDate(titaVo.getEntDyI(), 1));
		this.print(1, 1, "");
		this.print(1, 60, "檔案管理員：　");
		this.print(1, 1, "");
		this.print(1, 8, "﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍");
		this.print(1, 1, "");
		this.print(1, 8, "房貸清償證明：");
		this.print(0, 38, collectWayCode);
		this.print(1, 1, "");
		this.print(1, 38, "戶號：　" + parse.IntegerToString(iCustNo, 7));
		this.print(1, 38, "戶名：　" + FormatUtil.padX(loanCom.getCustNameByNo(iCustNo), 20));
		this.print(1, 38, "聯絡電話　１....　" + titaVo.getParam("TelNo1"));
		this.print(1, 38, "聯絡電話　２....　" + titaVo.getParam("TelNo2"));
		this.print(1, 1, "");
		this.print(1, 38, "放款服務課　　寄");

//		for (int i = 1; i <= 400; i++) {
//			if ((i % 10) == 0) {
//				this.print(-2, i, "" + (i / 10));
//			}
//			this.print(-1, i, "" + (i % 10));
//			this.print(-i, 1, "" + (i % 10));
//		}

		this.info("C 結束");
		this.close();

	}

}
