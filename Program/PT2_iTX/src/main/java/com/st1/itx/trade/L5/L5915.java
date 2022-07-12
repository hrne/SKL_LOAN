package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L5915ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5915")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5915 extends TradeBuffer {
	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	private L5915ServiceImpl l5915ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5915 ");
		this.totaVo.init(titaVo);

		List<Map<String, String>> dList = null;

		try {
			dList = l5915ServiceImpl.FindData(titaVo);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5915 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		if (dList == null || dList.size() == 0) {
			throw new LogicException(titaVo, "E0001", "");
		}

		String empNo = "";
		int tCnt = 0;
		BigDecimal tBonus = new BigDecimal("0");

		for (Map<String, String> d : dList) {
			if (!empNo.isEmpty() && !empNo.equals(d.get("Coorgnizer"))) {

				putTotal(tCnt, tBonus);

				// reset
				tCnt = 0;
				tBonus = new BigDecimal("0");
			}
			OccursList occursList = new OccursList();

			occursList.putParam("OEmpNo", d.get("Coorgnizer"));
			occursList.putParam("OEmpName", d.get("Fullname"));
			occursList.putParam("OCustNo", String.format("%07d", parse.stringToInteger(d.get("CustNo").toString())));
			occursList.putParam("OFacmNo", String.format("%03d", parse.stringToInteger(d.get("FacmNo"))));
			occursList.putParam("OBormNo", String.format("%03d", parse.stringToInteger(d.get("BormNo"))));
			occursList.putParam("OAmt", d.get("DrawdownAmt"));

			BigDecimal bonus = new BigDecimal(d.get("CoorgnizerBonus"));

			int cnt = 0;
			if (bonus.compareTo(BigDecimal.ZERO) != 0) {
				cnt = 1;
			}
			occursList.putParam("OCnt", cnt);

			tCnt += cnt;
			tBonus = tBonus.add(bonus);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);

		}

		putTotal(tCnt, tBonus);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void putTotal(int cnt, BigDecimal bonus) {
		OccursList occursList = new OccursList();

		occursList.putParam("OEmpNo", "");
		occursList.putParam("OEmpName", "小計");
		occursList.putParam("OCustNo", "");
		occursList.putParam("OFacmNo", "");
		occursList.putParam("OBormNo", "");

		occursList.putParam("OAmt", bonus);
		occursList.putParam("OCnt", cnt);

		this.totaVo.addOccursList(occursList);
	}
}