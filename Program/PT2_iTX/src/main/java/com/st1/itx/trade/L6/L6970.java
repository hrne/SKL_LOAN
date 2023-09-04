package com.st1.itx.trade.L6;

import java.io.PrintWriter;
import java.io.StringWriter;
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

		// 2022-01-19 智偉新增:查Online的JobDetail
		TitaVo onlineTitaVo = (TitaVo) titaVo.clone();
		onlineTitaVo.setDataBaseOnLine();

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		try {
			resultList = l6970ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l6970ServiceImpl error: " + e.getMessage());
			throw new LogicException("E0013", "L6970");
		}

		if (titaVo.getReturnIndex() == 0 && (resultList == null || resultList.isEmpty())) {
			throw new LogicException(titaVo, "E0001", "批次工作明細檔");
		}

		moveTota(resultList);

		if (l6970ServiceImpl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();
		}
	}

	private void moveTota(List<Map<String, String>> resultList) throws LogicException {
		for (Map<String, String> result : resultList) {
			String jobCode = result.get("JobCode");
			String nestJobCode = result.get("NestJobCode");
			String stepStartTime = result.get("StepStartTime");
			String status = result.get("Status");
			String execDate = result.get("ExecDate");
			String stepId = result.get("StepId");
			String stepEndTime = result.get("StepEndTime");
			String haveUspErrorLog = result.get("HaveUspErrorLog");
			String txSeq = result.get("TxSeq");
			if (nestJobCode == null || nestJobCode.equals(jobCode)) {
				nestJobCode = "";
			}
			String jobDetailStatus = status;
			OccursList occursList = new OccursList();
			occursList.putParam("OOExecDate", parse.stringToInteger(execDate) - 19110000); // PK欄位的日期要自行轉為民國年
			occursList.putParam("OOJobCode", jobCode);
			occursList.putParam("OONestJobCode", nestJobCode);
			occursList.putParam("OOStepId", stepId);
			occursList.putParam("OOStatus", jobDetailStatus);
			occursList.putParam("OOStepStartTime", stepStartTime);
			occursList.putParam("OOStepEndTime", stepEndTime);
			occursList.putParam("OOHaveUspErrorLog", haveUspErrorLog);
			occursList.putParam("OOJobTxSeq", txSeq);

			this.totaVo.addOccursList(occursList);
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