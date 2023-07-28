package com.st1.itx.trade.L6;

import java.text.SimpleDateFormat;
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
import com.st1.itx.db.service.springjpa.cm.L6970ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * 夜間批次控制檔查詢
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L6970")
@Scope("prototype")
public class L6970 extends TradeBuffer {

	@Autowired
	L6970ServiceImpl l6970ServiceImpl;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6970 ");
		this.totaVo.init(titaVo);

		// 如果OOJobCode有值, 表示是重新啟動JOB
		// 如果沒有值, 表示是查詢
		if (titaVo.containsKey("OOJobCode")) {
			doExecution(titaVo, titaVo.getParam("OOJobCode"));
		} else {
			doInquiry(titaVo);
		}

		this.info("L6970 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void doInquiry(TitaVo titaVo) throws LogicException {
		this.info("L6970 doInquiry ... ");

		// int batchResultCode =
		// parse.stringToInteger(titaVo.getParam("BatchResultCode"));
		// int inputStartDate = parse.stringToInteger(titaVo.getParam("InputStartDate"))
		// + 19110000;
		// int inputEndDate = parse.stringToInteger(titaVo.getParam("InputEndDate")) +
		// 19110000;

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;

		// 2022-01-19 智偉新增:查Online的JobDetail
		TitaVo onlineTitaVo = (TitaVo) titaVo.clone();
		onlineTitaVo.setDataBaseOnLine();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l6970ServiceImpl.findAll(this.index, this.limit, titaVo);
		} catch (Exception e) {
			this.info("Error ... " + e.getMessage());
		}

		if (this.index == 0 && (resultList == null || resultList.size() == 0)) {
			throw new LogicException(titaVo, "E0001", "批次工作明細檔");
		}

		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

		for (Map<String, String> result : resultList) {
			String jobCode = result.get("JobCode");
			String nestJobCode = result.get("NestJobCode");
			if (nestJobCode == null || nestJobCode.equals(jobCode)) {
				nestJobCode = "";
			}
			String time = format.format(parse.StringToSqlDateO(result.get("StepStartTime"), result.get("StepStartTime")));
			String jobDetailStatus = result.get("Status");
			OccursList occursList = new OccursList();
			occursList.putParam("OOExecDate", parse.stringToInteger(result.get("ExecDate")) - 19110000); // PK欄位的日期要自行轉為民國年
			occursList.putParam("OOJobCode", jobCode);
			occursList.putParam("OONestJobCode", nestJobCode);
			occursList.putParam("OOStepId", result.get("StepId"));
			occursList.putParam("OOStatus", jobDetailStatus);
			occursList.putParam("OOStepStartTime", time);
			if (result.get("StepEndTime") != null) {
				time = format.format(parse.StringToSqlDateO(result.get("StepEndTime"), result.get("StepEndTime")));
				occursList.putParam("OOStepEndTime",time);
			}
			occursList.putParam("OOHaveUspErrorLog", result.get("HaveUspErrorLog"));
			occursList.putParam("OOJobTxSeq", result.get("TxSeq"));

			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (l6970ServiceImpl.hasNext()) {// resultList != null && resultList.size() >= this.limit
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
//			this.totaVo.setMsgEndToAuto();// 自動折返
		}

	}

	private void doExecution(TitaVo titaVo, String jobName) throws LogicException {
		this.info("L6970 doExecution ... jobName = " + (jobName == null ? "" : jobName));

		if (jobName == null || jobName.isEmpty()) {
			this.error("L6970 doExecution() got empty jobName");
			throw new LogicException("EC009", "欲發動的批次程式名稱為空白");
		} else {
			titaVo.setBatchJobId(jobName);
		}
	}
}