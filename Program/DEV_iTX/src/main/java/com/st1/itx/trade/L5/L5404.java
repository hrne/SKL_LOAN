package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfBsOfficer;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfBsOfficerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5404")
@Scope("prototype")
/**
 * 房貸專員績效津貼計算
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5404 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5404.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public InnFundAplService innFundAplService;

	@Autowired
	public PfBsOfficerService iPfBsOfficerService;

	@Autowired
	public CdEmpService iCdEmpService;

	@Autowired
	public PfBsDetailService iPfBsDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5404 ");
		this.totaVo.init(titaVo);
		int iYyyMm = Integer.valueOf(titaVo.getParam("YyyMm"));
		int iForDetailYyyMm = iYyyMm + 191100;
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;
		Slice<PfBsOfficer> iPfBsOfficer = iPfBsOfficerService.findByMonth(iYyyMm, this.index, this.limit, titaVo);
		CdEmp iCdEmp = new CdEmp();
		if (iPfBsOfficer == null) {
			throw new LogicException(titaVo, "E0001", "");
		} else {
			for (PfBsOfficer oPfBsOfficer : iPfBsOfficer) {
				iCdEmp = iCdEmpService.findById(oPfBsOfficer.getEmpNo(), titaVo);
				Slice<PfBsDetail> iPfBsDetail = iPfBsDetailService.findBsOfficerOneMonth(oPfBsOfficer.getEmpNo(), iForDetailYyyMm, this.index, this.limit, titaVo); // 用工作年月抓資料
				BigDecimal iPerfCnt = new BigDecimal(0);
				BigDecimal iTotalDrawdownAmt = new BigDecimal(0);
				if (iPfBsDetail != null) {
					for (PfBsDetail aPfBsDetail : iPfBsDetail) {
						this.info("專員" + oPfBsOfficer.getEmpNo() + "有資料:" + iPfBsDetail);
						iPerfCnt = iPerfCnt.add(aPfBsDetail.getPerfCnt());
						iTotalDrawdownAmt = iTotalDrawdownAmt.add(aPfBsDetail.getDrawdownAmt());
					}
//					BigDecimal iCount = iPerfCnt.divide(iTotalDrawdownAmt);
					OccursList occurList = new OccursList();
					this.info("專員責任:" + oPfBsOfficer.getGoalAmt());
					occurList.putParam("OOEmpNo", oPfBsOfficer.getEmpNo());
					occurList.putParam("OOFullName", iCdEmp.getFullname());
					occurList.putParam("OOAreaCode", oPfBsOfficer.getAreaCode());
					occurList.putParam("OODeptItem", oPfBsOfficer.getDepItem());
					occurList.putParam("OOGoalAmt", oPfBsOfficer.getGoalAmt()); // 責任額
					occurList.putParam("OOPerfCnt", iPerfCnt);
					occurList.putParam("OOTotalDrawdownAmt", iTotalDrawdownAmt); // 房貸撥款金額
					occurList.putParam("OOCount", 0);
					this.totaVo.addOccursList(occurList);
				}
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}