package com.st1.itx.trade.L8;

import java.util.ArrayList;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TbJcicMu01;
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L8351")
@Scope("prototype")
/**
 * 聯徵產品檔案匯出作業
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8351 extends TradeBuffer {
	/* DB服務注入 */

	/* 日期工具 */
	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private TbJcicMu01Service iTbJcicMu01Service;

	@Autowired
	private WebClient webClient;

	@Autowired
	private L8351File iL8351File;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8351 ");
		this.totaVo.init(titaVo);

		String iSubmitType = titaVo.getParam("SubmitType");
		String iSubmitKey = titaVo.getParam("SubmitKey");
		String iTxtDate = titaVo.getParam("TxtDate");
		long fileNo = 0;
		if (iSubmitType.equals("1")) {
			fileNo = iL8351File.exec(titaVo);
			String checkMsg = "MU1人員名冊報送檔案已產出。";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					checkMsg, titaVo);
			totaVo.put("ExcelSnoM", "" + fileNo);
		} else {
			Slice<TbJcicMu01> xTbJcicMu01 = null;
			xTbJcicMu01 = iTbJcicMu01Service.findAll(0, Integer.MAX_VALUE, titaVo);
			if (xTbJcicMu01 != null) {
				int aTxDate = Integer.valueOf(iTxtDate);

				for (TbJcicMu01 xxTbJcicMu01 : xTbJcicMu01) {
					this.info("TESTTT=" + xxTbJcicMu01.getOutJcictxtDate());
					if (xxTbJcicMu01.getOutJcictxtDate() == aTxDate) {
						xxTbJcicMu01.setOutJcictxtDate(0);
						try {
							iTbJcicMu01Service.update(xxTbJcicMu01);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 資料修改錯誤
						}
					}
				}
			}
			totaVo.put("ExcelSnoM", "" + fileNo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}