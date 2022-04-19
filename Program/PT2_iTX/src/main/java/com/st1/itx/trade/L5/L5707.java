package com.st1.itx.trade.L5;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

//import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.OccursList;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;

/*DB服務*/

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegReportCom;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* DB容器 */

/*DB服務*/

@Service("L5707")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
//最大債權撥付產檔
public class L5707 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegReportCom NegReportCom;
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegFinShareService sNegFinShareService;
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	@Autowired
	public NegCom NegCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5707 ");
		this.info("L5707 Run");
		this.totaVo.init(titaVo);
		String BringUpDate = titaVo.getParam("BringUpDate").trim();// 提兌日

		if (BringUpDate != null && BringUpDate.length() != 0) {
			long sno = 0L;
			if (titaVo.isHcodeNormal()) {
				// 正向
				StringBuffer sbData = NegReportCom.BatchTx01(BringUpDate, titaVo);
				sno = NegReportCom.CreateTxt(sbData, "BATCHTX01", titaVo);
				this.info("L5707 sno=[" + sno + "]");
			} else {
				// 訂正
				NegReportCom.BatchTx01(BringUpDate, titaVo);
				// InsUpdNegApprO1<-在這裡面處理
			}
			totaVo.put("TxtSnoF", "" + sno);
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[提兌日]為空值");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}