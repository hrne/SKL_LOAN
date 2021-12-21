package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.domain.NegApprId;
/*DB服務*/
import com.st1.itx.db.service.NegApprService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunctionCode=9,1<br>
 * YYY=9,3<br>
 * MM=9,2<br>
 * ExportDate1=9,7<br>
 * ApprAcDate1=9,7<br>
 * BringUpDate1=9,7<br>
 * ExportDate2=9,7<br>
 * ApprAcDate2=9,7<br>
 * BringUpDate2=9,7<br>
 * ExportDate3=9,7<br>
 * ApprAcDate3=9,7<br>
 * BringUpDate3=9,7<br>
 * ExportDate4=9,7<br>
 * ApprAcDate4=9,7<br>
 * BringUpDate4=9,7<br>
 */

@Service("L5704")
@Scope("prototype")
/**
 * 
 * 
 * @author CHIH-CHENG
 * @version 1.0.0
 */
public class L5704 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegApprService sNegApprService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5704");
		this.info("active L5704 ");
		this.totaVo.init(titaVo);

		String FunctionCode = titaVo.getParam("FunctionCode").trim(); // 功能代號 0:查詢 1:維護
		String YYY = titaVo.getParam("YYY").trim(); // 民國年
		String MM = titaVo.getParam("MM").trim(); // 月份

		int iYearMonth = Integer.valueOf(YYY + MM) + 191100;
		int mExportDate = 0;
		int mBringUpDate = 0;
		int mApprAcDate = 0;

		// 新增 每個條件都建一筆
		if (FunctionCode.equals("1") || FunctionCode.equals("3")) {

			moveNegAppr(FunctionCode, iYearMonth, titaVo);
			// 修改-先刪全部再建
		} else if (FunctionCode.equals("2")) {

			Slice<NegAppr> sNegAppr;
			sNegAppr = sNegApprService.yyyyMmEq(iYearMonth, this.index, this.limit, titaVo);
			List<NegAppr> lNegAppr = sNegAppr == null ? null : sNegAppr.getContent();

			if (lNegAppr == null || lNegAppr.size() == 0) {
				throw new LogicException(titaVo, "E0001", "撥付日期設定檔"); // 查無資料
			}

			for (NegAppr tNegAppr : lNegAppr) {

				NegAppr dNegAppr = new NegAppr();
				dNegAppr = sNegApprService.holdById(new NegApprId(iYearMonth, tNegAppr.getKindCode()));

				if (dNegAppr == null) {
					throw new LogicException(titaVo, "E0006", "撥付日期設定檔"); // 鎖定資料不存在
				}

				mExportDate = parse.stringToInteger(titaVo.getParam("ExportDate" + dNegAppr.getKindCode())); // 製檔日
				mBringUpDate = parse.stringToInteger(titaVo.getParam("BringUpDate" + dNegAppr.getKindCode())); // 提兌日
				mApprAcDate = parse.stringToInteger(titaVo.getParam("ApprAcDate" + dNegAppr.getKindCode())); // 傳票日
				if (dNegAppr.getExportMark() == 1) {
					if (dNegAppr.getExportDate() != mExportDate || dNegAppr.getBringUpDate() != mBringUpDate || dNegAppr.getApprAcDate() != mApprAcDate) {
						throw new LogicException(titaVo, "E0015", "已製檔不可修改，類別＝" + dNegAppr.getKindCode());
					}
					continue; // 已製檔不刪除
				}

				try {
					this.info("FunctionCode=2 delete");
					sNegApprService.delete(dNegAppr);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}

			}
			// 每個條件都建一筆
			moveNegAppr(FunctionCode, iYearMonth, titaVo);

		} else if (FunctionCode.equals("4")) {
			this.info("FunctionCode=4 Start");
			Slice<NegAppr> sNegAppr;
			sNegAppr = sNegApprService.yyyyMmEq(iYearMonth, this.index, this.limit, titaVo);
			List<NegAppr> lNegAppr = sNegAppr == null ? null : sNegAppr.getContent();

			if (lNegAppr == null || lNegAppr.size() == 0) {
				throw new LogicException(titaVo, "E0001", "撥付日期設定檔"); // 查無資料
			}

			for (NegAppr tNegAppr : lNegAppr) {
				this.info("FunctionCode=4 HoldBy");
				NegAppr dNegAppr = new NegAppr();
				dNegAppr = sNegApprService.holdById(new NegApprId(iYearMonth, tNegAppr.getKindCode()));

				if (dNegAppr == null) {
					throw new LogicException(titaVo, "E0004", "撥付日期設定檔"); // 刪除資料不存在
				}
				if (dNegAppr.getExportMark() != dNegAppr.getApprAcMark() || dNegAppr.getApprAcMark() != dNegAppr.getBringUpMark() || dNegAppr.getBringUpMark() != dNegAppr.getExportMark()) {
					throw new LogicException(titaVo, "E0015", "製檔日傳票日提兌日記號不一致，類別＝" + dNegAppr.getKindCode());
				}

				try {
					sNegApprService.delete(dNegAppr);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
				}

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveNegAppr(String mFuncCode, int mWorkMonth, TitaVo titaVo) throws LogicException {
		this.info("moveNegAppr start");
		String mExportDate;
		String mApprAcDate;
		String mBringUpDate;

		for (int i = 1; i <= 4; i++) {
			mExportDate = titaVo.getParam("ExportDate" + i);
			mApprAcDate = titaVo.getParam("ApprAcDate" + i);
			mBringUpDate = titaVo.getParam("BringUpDate" + i);
			this.info("mExportDate==" + mExportDate + ",mApprAcDate==" + mApprAcDate + ",mBringUpDate==" + mBringUpDate);

			NegAppr tNegAppr = new NegAppr();
			NegApprId tNegApprId = new NegApprId();
			if (Integer.valueOf(mExportDate) == 0 && Integer.valueOf(mApprAcDate) == 0 && Integer.valueOf(mBringUpDate) == 0) {
				continue;
			}

			tNegApprId.setYyyyMm(mWorkMonth);
			tNegApprId.setKindCode(i);
			if (("2").equals(mFuncCode)) {
				NegAppr sNegAppr = new NegAppr();
				sNegAppr = sNegApprService.findById(tNegApprId, titaVo);
				if (sNegAppr != null) { // 已存在不新增
					continue;
				}
			}

			tNegAppr.setNegApprId(tNegApprId);
			tNegAppr.setExportDate(Integer.valueOf(mExportDate));
			tNegAppr.setApprAcDate(Integer.valueOf(mApprAcDate));
			tNegAppr.setBringUpDate(Integer.valueOf(mBringUpDate));

			try {
				sNegApprService.insert(tNegAppr);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}

		}
	}
}