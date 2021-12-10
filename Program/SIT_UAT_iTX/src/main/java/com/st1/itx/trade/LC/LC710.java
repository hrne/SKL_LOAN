package com.st1.itx.trade.LC;

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

@Service("LC710")
@Scope("prototype")
/**
 * 夜間批次控制檔查詢
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LC710 extends TradeBuffer {

	@Autowired
	JobDetailService jobDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC710 ");
		this.totaVo.init(titaVo);

		int choice = Integer.parseInt(titaVo.getParam("Choice"));
		int inputStartDate = Integer.parseInt(titaVo.getParam("InputStartDate")) + 19110000;
		int inputEndDate = Integer.parseInt(titaVo.getParam("InputEndDate")) + 19110000;

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 100;

		Slice<JobDetail> slJobDetail;

		if (choice == 0) {
			// 查全部
			slJobDetail = jobDetailService.findExecDateIn(inputStartDate, inputEndDate, this.index, this.limit, titaVo);
		} else {
			// 只查成功或失敗
			slJobDetail = jobDetailService.findStatusExecDateIn(inputStartDate, inputEndDate, choice == 1 ? "S" : "F",
					this.index, this.limit, titaVo);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slJobDetail != null && slJobDetail.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToAuto();
		}

		ArrayList<JobDetail> lJobDetail = slJobDetail == null ? null
				: new ArrayList<JobDetail>(slJobDetail.getContent());

		if (lJobDetail != null && !lJobDetail.isEmpty()) {

			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

			for (JobDetail tJobDetail : lJobDetail) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOExecDate", tJobDetail.getExecDate() - 19110000); // PK欄位的日期要自行轉為民國年
				occursList.putParam("OOJobCode", tJobDetail.getJobCode());
				occursList.putParam("OOStepId", tJobDetail.getStepId());
				occursList.putParam("OOStatus", tJobDetail.getStatus());
				occursList.putParam("OOStepStartTime", format.format(tJobDetail.getStepStartTime()));
				occursList.putParam("OOStepEndTime", format.format(tJobDetail.getStepEndTime()));

				this.totaVo.addOccursList(occursList);
			}
		}

		this.info("LC710 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

}