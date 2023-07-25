package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L6973ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * L6973 資料庫批次錯誤查詢
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L6973")
@Scope("prototype")
public class L6973 extends TradeBuffer {

	String tranCode = "L6973";

	@Autowired
	L6973ServiceImpl l6973ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + tranCode);
		this.totaVo.init(titaVo);

		/*
		 * *** 折返控制相關 *** 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		// *** 折返控制相關 ***
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100; // 252 * 100 = 25200

		List<Map<String, String>> resultList;

		String jobTxSeq = titaVo.getParam("JobTxSeq");

		this.info("jobTxSeq=" + jobTxSeq);

		if (jobTxSeq != null && !jobTxSeq.isEmpty()) {
			try {
				// *** 折返控制相關 ***
				resultList = l6973ServiceImpl.doQueryByJobTxSeq(jobTxSeq, this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.error("L6973ServiceImpl doQueryByJobTxSeq " + e.getMessage());
				throw new LogicException("E0013", "L6973");
			}
		} else {
			try {
				// *** 折返控制相關 ***
				resultList = l6973ServiceImpl.doQuery(this.index, this.limit, titaVo);
			} catch (Exception e) {
				this.error("L6973ServiceImpl doQuery " + e.getMessage());
				throw new LogicException("E0013", "L6973");
			}
		}

		if (resultList != null && resultList.size() > 0) {
			for (Map<String, String> result : resultList) {
				// new occurs
				OccursList occurslist = new OccursList();

				occurslist.putParam("LogUkey", result.get("LogUkey"));
				occurslist.putParam("LogDate", result.get("LogDate"));
				occurslist.putParam("LogTime", result.get("LogTime"));
				occurslist.putParam("UspName", result.get("UspName"));

				String errorMessage = result.get("ErrorMessage");
				// 前端設定長度為100
				if (errorMessage.length() > 100) {
					errorMessage = errorMessage.substring(0, 100);
				}
				occurslist.putParam("ErrorMessage", errorMessage);

				String errorBackTrace = result.get("ErrorBackTrace");
				// 前端設定長度為100
				if (errorBackTrace.length() > 100) {
					errorBackTrace = errorBackTrace.substring(0, 100);
				}
				occurslist.putParam("ErrorBackTrace", errorBackTrace);
				occurslist.putParam("ExecEmpNo", result.get("ExecEmpNo"));

				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occurslist);
			}

			// *** 折返控制相關 ***
			if (l6973ServiceImpl.hasNext()) {
				titaVo.setReturnIndex(this.setIndexNext());
				/* 手動折返 */
				this.totaVo.setMsgEndToEnter();
			}
		} else {
			throw new LogicException("E2003", "資料庫批次錯誤紀錄檔"); // 查無資料
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}