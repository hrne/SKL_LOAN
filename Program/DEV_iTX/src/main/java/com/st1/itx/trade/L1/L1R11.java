package com.st1.itx.trade.L1;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdReport;
import com.st1.itx.db.service.CdReportService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R11")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R11 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1R11.class);

	/* DB服務注入 */
	@Autowired
	public CdReportService sCdReportService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R11 ");
		this.totaVo.init(titaVo);

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 178 * 200 = 35,600

		Slice<CdReport> slCdReport1;
		Slice<CdReport> slCdReport2;
		ArrayList<CdReport> lCdReport1 = new ArrayList<CdReport>();
		ArrayList<CdReport> lCdReport2 = new ArrayList<CdReport>();
		slCdReport1 = sCdReportService.formNoLike("L9" + "%", 0, Integer.MAX_VALUE, titaVo);
		lCdReport1 = slCdReport1 == null ? null : new ArrayList<CdReport>(slCdReport1.getContent());
		slCdReport2 = sCdReportService.formNoLike("L4" + "%", 0, Integer.MAX_VALUE, titaVo);
		lCdReport2 = slCdReport2 == null ? null : new ArrayList<CdReport>(slCdReport2.getContent());
		if (lCdReport1 != null) {
			for (CdReport tmpCdReport1 : lCdReport1) {
				lCdReport2.add(tmpCdReport1);
			}
		}
	
		if (lCdReport2 == null || lCdReport2.size() == 0) {
			throw new LogicException(titaVo, "E0001", "報表代號對照檔"); // 查無資料
		}
		
		int amount = 1; //筆數
		for (CdReport iCdReport:lCdReport2) {
			if(iCdReport.getSendCode()==0) {
				//寄送記號為0跳過
				continue;
			}
			this.info("報表"+amount+"為"+iCdReport.getFormNo()+" : "+iCdReport.getFormName()+".");
			this.totaVo.putParam("L1r11FormNo" + amount, iCdReport.getFormNo());
			this.totaVo.putParam("L1r11FormName" + amount, iCdReport.getFormName());
			amount ++;
		}
		//補空白資料 總共回傳39筆
		while (amount <=40){
			this.totaVo.putParam("L1r11FormNo" + amount, " ");
			this.totaVo.putParam("L1r11FormName" + amount, " ");
			amount ++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}