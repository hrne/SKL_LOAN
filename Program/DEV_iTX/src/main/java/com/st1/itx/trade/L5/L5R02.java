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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.NegAppr;
/*DB服務*/
import com.st1.itx.db.service.NegApprService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R02")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5R02 extends TradeBuffer {
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
		this.info("Run L5R02");
		this.info("active L5R02 ");
		this.totaVo.init(titaVo);
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = 0;
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = Integer.MAX_VALUE;// 查全部

		String iYYY = titaVo.getParam("RimYYY").trim();// 民國年
		String iMM = titaVo.getParam("RimMM").trim();// 月
		int iFunctionCode = Integer.parseInt(titaVo.getParam("RimFunctionCode").trim());

		int YearMonth = Integer.parseInt(iYYY + iMM) + 191100;

		Slice<NegAppr> slNegAppr;

		slNegAppr = sNegApprService.YyyyMmEq(YearMonth, this.index, this.limit, titaVo);

		if (iFunctionCode == 1) {
			if (slNegAppr != null) {
				throw new LogicException(titaVo, "E0002", iYYY + "/" + iMM);
			}
		}

		List<NegAppr> lNegAppr = slNegAppr == null ? null : slNegAppr.getContent();

		for (int i = 1; i <= 4; i++) {
			totaVo.putParam("L5r02ExportDate" + i, 0);// 製檔日
			totaVo.putParam("L5r02ApprAcDate" + i, 0);// 傳票日
			totaVo.putParam("L5r02BringUpDate" + i, 0);// 提兌日
			totaVo.putParam("L5r02ExportMark" + i, 0);// 製檔日記號
			totaVo.putParam("L5r02ApprAcMark" + i, 0);// 傳票日記號
			totaVo.putParam("L5r02BringUpMark" + i, 0);// 提兌日記號
		}

		if (lNegAppr != null) {
			for (int i = 1; i <= 4; i++) {
				for (NegAppr NegApprVO : lNegAppr) {
					if (NegApprVO.getKindCode() == i) {
						totaVo.putParam("L5r02ExportDate" + i, NegApprVO.getExportDate());// 製檔日
						totaVo.putParam("L5r02ApprAcDate" + i, NegApprVO.getApprAcDate());// 傳票日
						totaVo.putParam("L5r02BringUpDate" + i, NegApprVO.getBringUpDate());// 提兌日
						totaVo.putParam("L5r02ExportMark" + i, NegApprVO.getExportMark());// 製檔日記號
						totaVo.putParam("L5r02ApprAcMark" + i, NegApprVO.getApprAcMark());// 傳票日記號
						totaVo.putParam("L5r02BringUpMark" + i, NegApprVO.getBringUpMark());// 提兌日記號
					}
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}