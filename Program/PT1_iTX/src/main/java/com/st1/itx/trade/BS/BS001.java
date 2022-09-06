package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.AcMainCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 日始作業 <br>
 * 執行時機：日始作業，每日系統換日後自動執行 <br>
 * 1.應處理清單－每日維護清檔 <br>
 * 2.總帳檔換日過帳(含年初損益類結轉)<br>
 * 3.新增當日應處理明細<br>
 * 3.1.每月21日(遇假日順延)，火險保費未繳轉借支<br>
 * 3.2.每月第2營業日，到期明細表<br>
 * 3.2.1.L9710寬限到期明細表 每月第2營業日印後2個月<br>
 * 3.2.2.L9711 放款到期明細表及通知單 每月第2營業日印後4個月<br>
 * 3.2.3.工作月結束次日，業績工作月結算啟動通知<br>
 * 4.啟動背景作業<br>
 * 4.1 BS004 新增應處理明細－員工資料異動更新<br>
 * 4.2 BS005 新增應處理明細－預約撥款到期<br>
 * 4.3 BS006 新增應處理明細－支票兌現檢核<br>
 * 4.4 BS007 新增應處理明細－未齊件到期通知<br>
 * 4.5 BS901 新增應處理明細－未付火險費提存，月初日迴轉上月<br>
 * 4.6 BS902 新增應處理明細－聯貸費用攤提(月初日)<br>
 * 4.7 BS010 新增應處理明細－轉列催收<br>
 * 4.8 BS020 新增整批入帳明細－暫收抵繳期款<br>
 * 4.9 BS060 現金流量預估資料檔維護(月底前五個營業日)<br>
 * 4.10 BS600 放款戶帳冊別轉換(帳冊別帳務調整日期等於系統營業日時)<br>
 * 4.11 BS902 新增應處理明細－聯貸費用攤提(月初日)<br>
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS001")
@Scope("prototype")
public class BS001 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	public TxBizDateService txBizDateService;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public AcMainCom acMainCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS001 ......");

		txToDoCom.setTxBuffer(this.txBuffer);
		acMainCom.setTxBuffer(this.txBuffer);

		/*---------- Step 1. 應處理清單－每日維護清檔 ------------------*/

		txToDoCom.dailyHouseKeeping(titaVo);
		this.batchTransaction.commit();

		/*---------- Step 2. 總帳檔換日過帳(含年初損益類結轉) ----------*/

		// 抓總帳檔(批次日期)
		TxBizDate tTxBizDate = txBizDateService.findById("BATCH");

		this.info("BatchDate =" + tTxBizDate.getTbsDyf() + ", OnlineDate=" + this.txBuffer.getMgBizDate().getTbsDyf());
		Slice<AcMain> slAcMain = acMainService.acmainAcDateEq(tTxBizDate.getTbsDyf(), this.index, Integer.MAX_VALUE);
		List<AcMain> lAcMain = slAcMain == null ? null : slAcMain.getContent();

		// 過總帳檔(連線日期)
		if (lAcMain != null) {
			acMainCom.changeDate(tTxBizDate.getTbsDy(), this.txBuffer.getMgBizDate().getTbsDy(), lAcMain, titaVo);
		}

		// commitEnd
		this.batchTransaction.commit();

		/*---------- Step 3. 按日期新增應處理清單--------------------------------*/

		addTxToDo(titaVo);
		// commitEnd
		this.batchTransaction.commit();

		/*---------- Step 4.  啟動背景作業 -----------------------------------*/

		// 啟動背景作業－BS004 新增應處理明細－員工資料異動更新
		MySpring.newTask("BS004", this.txBuffer, titaVo);

		// 啟動背景作業－BS005 新增應處理明細－預約撥款到期
		MySpring.newTask("BS005", this.txBuffer, titaVo);

		// 啟動背景作業－BS006 新增應處理明細－支票兌現檢核
		MySpring.newTask("BS006", this.txBuffer, titaVo);

		// 啟動背景作業－BS007 新增應處理明細－未齊件到期通知
		MySpring.newTask("BS007", this.txBuffer, titaVo);

		// 啟動背景作業－新增應處理明細－未付火險費提存，月初日迴轉上月
		if (this.txBuffer.getMgBizDate().getTbsDy() / 100 != this.txBuffer.getMgBizDate().getLbsDy() / 100) {
			MySpring.newTask("BS901", this.txBuffer, titaVo);
		}
		// 啟動背景作業－新增應處理明細－聯貸費用攤提(月初日)
		if (this.txBuffer.getMgBizDate().getTbsDy() / 100 != this.txBuffer.getMgBizDate().getLbsDy() / 100) {
			MySpring.newTask("BS902", this.txBuffer, titaVo);
		}
		// 啟動背景作業－BS010 新增應處理明細－放款轉列催收、火險費轉列催收、法務費轉列催收
		MySpring.newTask("BS010", this.txBuffer, titaVo);

		// 啟動背景作業－ BS020 新增整批入帳明細－暫收抵繳期款
		MySpring.newTask("BS020", this.txBuffer, titaVo);

		// 啟動背景作業－ BS050 未齊件到期寄發Email通知
		MySpring.newTask("BS050", this.txBuffer, titaVo);

		// 啟動背景作業－ BS060 現金流量預估資料檔維護
		// bizDate 月底前五個營業日
		dateUtil.init();
		int bizDate = dateUtil.getbussDate(this.txBuffer.getMgBizDate().getMfbsDyf(), -5);
		this.info("月底前五個營業日=" + bizDate);
		if (bizDate == this.txBuffer.getMgBizDate().getTbsDyf()) {
			MySpring.newTask("BS060", this.txBuffer, titaVo);
		}

		// 啟動背景作業－ BS600 放款戶帳冊別轉換
		// 執行時機：日始作業，帳冊別帳務調整日期等於系統營業日時自動執行
		if (this.txBuffer.getSystemParas().getAcBookAdjDate() == this.txBuffer.getTxCom().getTbsdy()) {
			MySpring.newTask("BS600", this.txBuffer, titaVo);
		}

		// end
		return null;
	}

	// 新增應處理明細
	private void addTxToDo(TitaVo titaVo) throws LogicException {
		int processDate = 0;
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		TempVo tTempVo = new TempVo();
		/* 1.每月15日(遇假日順延)，火險出單明細表作業 */
		processDate = (this.txBuffer.getMgBizDate().getTbsDy() / 100) * 100 + 15;
		if (this.txBuffer.getMgBizDate().getLbsDy() < processDate && this.txBuffer.getMgBizDate().getTbsDy() >= processDate) {
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4602");
			tTempVo.clear();
			tTempVo.putParam("Note", "每月15日火險出單明細表作業");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
		/* 1.每月21日(遇假日順延)，火險保費未繳轉借支 */
		processDate = (this.txBuffer.getMgBizDate().getTbsDy() / 100) * 100 + 21;
		if (this.txBuffer.getMgBizDate().getLbsDy() < processDate && this.txBuffer.getMgBizDate().getTbsDy() >= processDate) {
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4604");
			tTempVo.clear();
			tTempVo.putParam("Note", "每月21日火險保費未繳轉借支");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

		/* 2.每月第2營業日，到期明細表 */
		// L9710寬限到期明細表 每月第2營業日印後2個月
		// L9711 放款到期明細表及通知單 每月第2營業日印後4個月
		dateUtil.init();
		dateUtil.setDate_1(this.txBuffer.getMgBizDate().getLbsDyf());
		TxBizDate bizDate = dateUtil.getForTxBizDate();
		if (bizDate.getTbsDy() / 100 != bizDate.getLbsDy() / 100) { // 上營業日為月初日(換月)
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L9710");
			tTempVo.clear();
			tTempVo.putParam("Note", "寬限到期明細表 每月第2營業日印後2個月");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過

			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L9711");
			tTempVo.clear();
			tTempVo.putParam("Note", "放款到期明細表及通知單 每月第2營業日印後4個月");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

		/* 3.工作月結束次日，業績工作月結算啟動通知 */
		CdWorkMonth tCdWorkMonth = sCdWorkMonthService.findDateFirst(this.txBuffer.getTxBizDate().getTbsDyf(), this.txBuffer.getTxBizDate().getLbsDyf(), titaVo);
		if (tCdWorkMonth == null) {
			throw new LogicException(titaVo, "E0001", "CdWorkMonth 放款業績工作月對照檔，業績日期=" + this.txBuffer.getTxBizDate().getLbsDyf()); // 查詢資料不存在
		}
		// 終止日期 >= 本營業日 && 終止日期 < 下營業日
		if (tCdWorkMonth.getEndDate() >= this.txBuffer.getTxBizDate().getLbsDy() && tCdWorkMonth.getEndDate() < this.txBuffer.getTxBizDate().getTbsDy()) {
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("PFCL00");
			tTempVo.clear();
			tTempVo.putParam("Note", parse.IntegerToString(tCdWorkMonth.getYear() - 1911, 3) + "-" + parse.IntegerToString(tCdWorkMonth.getMonth(), 2) + "工作月結束 " + tCdWorkMonth.getStartDate() + "~"
					+ tCdWorkMonth.getEndDate() + "，請啟動業績工作月結算作業");
			tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}

		/* 4.員工扣薪日程表的媒體日期 = 本日，產出員工扣薪檔 */
		// L45101產出15日薪員工扣薪檔
		// L45102產出非15日薪員工扣薪檔
		int todayf = this.getTxBuffer().getMgBizDate().getTbsDyf();
		Slice<EmpDeductSchedule> slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(todayf, todayf, index, limit, titaVo);

		if (slEmpDeductSchedule != null) {
			for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
				String itemCode = "";
				if ("4".equals(tEmpDeductSchedule.getAgType1()) || "5".equals(tEmpDeductSchedule.getAgType1())) {
					itemCode = "L45101";
				} else {
					itemCode = "L45102";
				}
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode(itemCode);
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
			}
		}
		/* 5. 員工扣薪日程表的入帳日期= 本日，員工扣薪入帳作業 */
		// EMEP00 員工扣薪入帳作業
		Slice<EmpDeductSchedule> sl2EmpDeductSchedule = empDeductScheduleService.entryDateRange(todayf, todayf, index, limit, titaVo);
		if (sl2EmpDeductSchedule != null) {
			tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("EMEP00");
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
	}

}