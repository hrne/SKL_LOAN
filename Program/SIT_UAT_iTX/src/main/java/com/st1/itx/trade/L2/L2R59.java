package com.st1.itx.trade.L2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L2418ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L2R59")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R59 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L2R59.class);

	@Autowired
	public L2418ServiceImpl l2418ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R59 ");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設200筆 總長不可超過六萬 */
		this.limit = 200;

		String s = "";
		String outs = "";

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l2418ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (resultList != null) {
			for (Map<String, String> result : resultList) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += result.get("LandOfficeCode").trim() + ":" + result.get("Item").trim();
			}

			outs = s;
		}

		this.totaVo.putParam("HelpDesc", outs);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 中文以2位計算長度
	private static int clength(String s) {
		int clen = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen += 2;
			} else {
				clen += 1;
			}

//				this.info("XXR99 clength [" + c + "] = " + clen);
		}

		return clen;
	}
}