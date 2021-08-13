package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita FormNo=X,10 FormName=x,80 END=X,1
 */

@Service("L6068") // 報表代號對照檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6068 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6068.class);

	/* DB服務注入 */
	@Autowired
	public CdReportService sCdReportService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6068 ");
		this.totaVo.init(titaVo);

		String iFormNo = titaVo.getParam("FormNo");
		String iFormName = titaVo.getParam("FormName");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 178 * 200 = 35,600

		// 查詢報表代號對照檔
		Slice<CdReport> slCdReport;
		if (iFormName.isEmpty()) {
			slCdReport = sCdReportService.formNoLike("%" + iFormNo.trim() + "%", this.index, this.limit, titaVo);
		} else {
			slCdReport = sCdReportService.formNameLike("%" + iFormName.trim() + "%", this.index, this.limit, titaVo);
		}
		List<CdReport> lCdReport = slCdReport == null ? null : slCdReport.getContent();

		if (lCdReport == null || lCdReport.size() == 0) {
			throw new LogicException(titaVo, "E0001", "報表代號對照檔"); // 查無資料
		}
		// 如有找到資料
		for (CdReport tCdReport : lCdReport) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOFormNo", tCdReport.getFormNo());
			occursList.putParam("OOFormName", tCdReport.getFormName());
			occursList.putParam("OOCycle", tCdReport.getCycle());
			occursList.putParam("OOSendCode", tCdReport.getSendCode());
			occursList.putParam("OOLetter", tCdReport.getLetter());
			occursList.putParam("OOMessage", tCdReport.getMessage());
			occursList.putParam("OOEmail", tCdReport.getEmail());
			occursList.putParam("OOUsageDesc", tCdReport.getUsageDesc());
			occursList.putParam("OOSignCode", tCdReport.getSignCode());
			occursList.putParam("OOEnable", tCdReport.getEnable());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdReport != null && slCdReport.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}