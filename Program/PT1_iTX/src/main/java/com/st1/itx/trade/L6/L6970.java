package com.st1.itx.trade.L6;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JobDetail;
import com.st1.itx.db.service.JobDetailService;
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
	JobDetailService jobDetailService;

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

		int batchResultCode = parse.stringToInteger(titaVo.getParam("BatchResultCode"));
		int inputStartDate = parse.stringToInteger(titaVo.getParam("InputStartDate")) + 19110000;
		int inputEndDate = parse.stringToInteger(titaVo.getParam("InputEndDate")) + 19110000;

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		Slice<JobDetail> slJobDetail;

		// 2022-01-19 智偉新增:查Online的JobDetail
		TitaVo onlineTitaVo = (TitaVo) titaVo.clone();
		onlineTitaVo.setDataBaseOnLine();

		if (batchResultCode == 0) {
			// 查全部
			// 2023-07-19 Wei 增加 篩選"eodFlow"
			slJobDetail = jobDetailService.findExecDateIn("eodFlow", inputStartDate, inputEndDate, this.index,
					this.limit, onlineTitaVo); // 2022-01-19 智偉修改:查Online
		} else {
			// 只查成功或失敗
			// 2023-07-19 Wei 增加 篩選"eodFlow"
			slJobDetail = jobDetailService.findStatusExecDateIn("eodFlow", inputStartDate, inputEndDate,
					batchResultCode == 1 ? "S" : "F", this.index, this.limit, onlineTitaVo); // 2022-01-19 智偉修改:查Online
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slJobDetail != null && slJobDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		ArrayList<JobDetail> lJobDetail = slJobDetail == null ? null
				: new ArrayList<JobDetail>(slJobDetail.getContent());

		if (lJobDetail != null && !lJobDetail.isEmpty()) {

			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

			for (JobDetail tJobDetail : lJobDetail) {
				String jobCode = tJobDetail.getJobCode();
				String nestJobCode = tJobDetail.getNestJobCode();
				if (nestJobCode == null || nestJobCode.equals(jobCode)) {
					nestJobCode = "";
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OOExecDate", tJobDetail.getExecDate() - 19110000); // PK欄位的日期要自行轉為民國年
				occursList.putParam("OOJobCode", jobCode);
				occursList.putParam("OONestJobCode", nestJobCode);
				occursList.putParam("OOStepId", tJobDetail.getStepId());
				occursList.putParam("OOStatus", tJobDetail.getStatus());
				occursList.putParam("OOStepStartTime", format.format(tJobDetail.getStepStartTime()));

				// 2022-01-10 智偉修改: 批次執行中,StepEndTime可能為null
				occursList.putParam("OOStepEndTime",
						tJobDetail.getStepEndTime() == null ? "" : format.format(tJobDetail.getStepEndTime()));

				this.totaVo.addOccursList(occursList);
			}
		} else {
			// 查無資料
			throw new LogicException("E0001", "JobDetail");
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