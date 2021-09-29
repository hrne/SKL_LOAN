package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.db.service.springjpa.cm.BS004ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BS004Vo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 新增應處理明細－員工應處理 <br>
 * 執行時機：日始作業，系統換日後(BS001執行後)自動執行<br>
 * 1. 新增應處理明細－EMRT00 員工利率調整<br>
 * 1.1 依員工利率產品第一碼為'E' ，及任用狀況碼(業務線別)判斷<br>
 * 1.1.1 身份變更， 任用狀況碼 <> 1-在職, 4-留職停薪<br>
 * 1.1.2 退休屆滿5年，任用狀況碼 = 5-退休離職 & QUIT_DATE 離職/停約日 < 本日減5年<br>
 * 2. 新增應處理明細－EMCU00 員工客戶別調整 <br>
 * 2.1 業務線別 <>1-在職, 4-留職停薪 && 客戶別 = 01-員工,09-新二階員工<br>
 * 3. 新增應處理明細－L4510 員工扣薪媒體製作<br>
 * 3.1 員工扣薪日程表的媒體日期 = 本日<br>
 * 4. 新增應處理明細－EMEP00 員工扣薪入帳作業<br>
 * 4.1 員工扣薪日程表的入帳日期= 本日<br>
 * 
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS004")
@Scope("prototype")
public class BS004 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public BS004ServiceImpl bS004ServiceImpl;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	/* 員工資料檔上傳後更新 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS004 ......");
		txToDoCom.setTxBuffer(this.txBuffer);

		// 退休屆滿5年(本日減5年)
		dateUtil.init();
		dateUtil.setDate_1(this.getTxBuffer().getMgBizDate().getTbsDyf());
		dateUtil.setYears(-5);
		int quitDate = dateUtil.getCalenderDay();
		this.info(this.getTxBuffer().getMgBizDate().getTbsDyf() + " quitDate= " + quitDate);

		// 新增應處理明細 EMRT00 員工利率調整
		emploan(quitDate, titaVo);

		this.batchTransaction.commit();

		// 新增應處理明細－EMCU00 員工客戶別調整
		empCustTyp(titaVo);

		this.batchTransaction.commit();

		// 新增應處理明細－L4510 員工扣薪媒體製作
		empMediaNotice(titaVo);

		// 新增應處理明細－EMEP00 員工扣薪入帳作業
		empEntryNotice(titaVo);

		this.batchTransaction.commit();

		return null;
	}

	/* 新增應處理明細 EMRT00 員工利率調整 */
	private void emploan(int quitDate, TitaVo titaVo) throws LogicException {
		List<BS004Vo> bS004VoList = null;
		try {
			// 員工利率產品比對不符清單
			bS004VoList = bS004ServiceImpl.compareProdNo(quitDate); // 退休屆滿5年
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("bS004List=" + bS004VoList.toString());
		// data size > 0 -> 新增應處理明細
		if (bS004VoList.size() > 0) {
			TxToDoDetail tTxToDoDetail;
			for (BS004Vo bs : bS004VoList) {
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("EMRT00"); // EMRT00 員工利率調整
				tTxToDoDetail.setCustNo(bs.getCustNo());
				tTxToDoDetail.setFacmNo(bs.getFacmNo());
				tTxToDoDetail.setBormNo(0);
				tTxToDoDetail.setDtlValue(" ");
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
			}
		}
	}

	/* 新增應處理明細－EMCU00 員工客戶別調整 */
	private void empCustTyp(TitaVo titaVo) throws LogicException {
		List<BS004Vo> bS004VoList = null;
		try {
			// 客戶檔客戶別與員工檔比對不符清單
			bS004VoList = bS004ServiceImpl.compareCustTyp(); // 身份變更
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("bS004List=" + bS004VoList.toString());

		this.info("bS004List=" + bS004VoList.toString());
		// data size > 0 -> 新增應處理明細
		if (bS004VoList.size() > 0) {
			TxToDoDetail tTxToDoDetail;
			for (BS004Vo bs : bS004VoList) {
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("EMCU00"); // EMCU00 員工客戶別調整
				tTxToDoDetail.setCustNo(bs.getCustNo());
				tTxToDoDetail.setFacmNo(0);
				tTxToDoDetail.setBormNo(0);
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
			}
		}
	}

//	新增應處理明細－L4510 員工扣薪媒體製作
	private void empMediaNotice(TitaVo titaVo) throws LogicException {
//		 員工扣薪日程表的媒體日期 = 本日
		int today = dateUtil.getNowIntegerRoc();

		Slice<EmpDeductSchedule> sEmpDeductSchedule = empDeductScheduleService.mediaDateRange(today, today, index, limit, titaVo);

		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();

		lEmpDeductSchedule = sEmpDeductSchedule == null ? null : sEmpDeductSchedule.getContent();

		if (lEmpDeductSchedule != null && lEmpDeductSchedule.size() != 0) {
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("L4510 ");
			tTxToDoDetail.setCustNo(0);
			tTxToDoDetail.setFacmNo(0);
			tTxToDoDetail.setBormNo(0);
			tTxToDoDetail.setDtlValue("");
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
	}

//	新增應處理明細－EMEP00 員工扣薪入帳作業
	private void empEntryNotice(TitaVo titaVo) throws LogicException {
//		 員工扣薪日程表的入帳日期= 本日
		int today = dateUtil.getNowIntegerRoc();

		Slice<EmpDeductSchedule> sEmpDeductSchedule = empDeductScheduleService.entryDateRange(today, today, index, limit, titaVo);

		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();

		lEmpDeductSchedule = sEmpDeductSchedule == null ? null : sEmpDeductSchedule.getContent();

		if (lEmpDeductSchedule != null && lEmpDeductSchedule.size() != 0) {
			TxToDoDetail tTxToDoDetail = new TxToDoDetail();
			tTxToDoDetail.setItemCode("EMEP00");
			tTxToDoDetail.setCustNo(0);
			tTxToDoDetail.setFacmNo(0);
			tTxToDoDetail.setBormNo(0);
			tTxToDoDetail.setDtlValue("");
			txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
		}
	}
}