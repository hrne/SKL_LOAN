package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5411ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5411")
@Scope("prototype")
/**
 * 撥款件貸款成數統計資料產生
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5411 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5411.class);

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5411ServiceImpl iL5411ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5411 ");
		this.totaVo.init(titaVo);
		String iDateFm = titaVo.getParam("DateFm");
		String iDateTo = titaVo.getParam("DateTo");
		int xDateFm = Integer.valueOf(iDateFm) + 19110000;
		int xDateTo = Integer.valueOf(iDateTo) + 19110000;
		/*
		 * 需求欄位 案件編號 FacMain.ApplNo 核准額度 FacMain.LineAmt 借款人戶號 FacMain.CustNo 額度比數
		 * 首次撥款日** 要找首次撥款日落在查詢範圍內的資料 FacMain.FirstDrawdownDate 評估淨值 (猜測是同一額度相加) 成數
		 * (有固定算法) 成數區間 (excel有) 地區別名稱 地區區間 地區別 押租金 房貸專員 原編+姓名 FacMain.BusinessOfficer
		 * 核決主管 原編+姓名 FacMain.Supervisor
		 */
		List<Map<String, String>> iL5411SqlReturn = new ArrayList<Map<String, String>>();
		try {
			iL5411SqlReturn = iL5411ServiceImpl.FindData(xDateFm, xDateTo, titaVo);
		} catch (Exception e) {
			// E5004 讀取DB語法發生問題
			this.info("L5411 ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}