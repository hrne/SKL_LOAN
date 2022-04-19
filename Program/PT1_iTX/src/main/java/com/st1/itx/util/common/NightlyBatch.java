package com.st1.itx.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.batch.scheduled.ScheduledBase;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.MySpring;

@Component("nightlyBatch")
@Scope("singleton")
public class NightlyBatch extends ScheduledBase {

	private void batchBegin(TitaVo titaVo, String datecode, String date) throws LogicException {
		this.changeBatchDate(titaVo, datecode, date);
	}

	/**
	 * 夜間批次<BR>
	 * 執行時機:每天晚上九點執行<BR>
	 * 檢核1:當日為營業日且當日不為月底日才繼續<BR>
	 * 檢核2:會計業務關帳控制檔的會計日期為當日且業務類別為09放款時,關帳狀態為1關帳才繼續
	 */
	@Scheduled(cron = "0 0 21 * * ?")
	private void exec() {
		this.mustInfo("Active nightlyBatch.exec  ");
		TitaVo titaVo = new TitaVo();
		// 檢核1:當日為營業日且當日不為月底日才繼續
		try {
			titaVo.init();
			titaVo.putParam(ContentName.kinbr, "0000");
			titaVo.putParam(ContentName.tlrno, "BAT001");
			titaVo.putParam(ContentName.empnot, "BAT001");

			TxBuffer txBuffer = MySpring.getBean("txBuffer", TxBuffer.class);
			txBuffer.init(titaVo);

			int tbsDyf = txBuffer.getTxBizDate().getTbsDyf();
			int nowDy = this.dateUtil.getNowIntegerForBC();
			int mfbsDy = txBuffer.getTxBizDate().getMfbsDyf();

			dateUtil.setDate_2(nowDy);
			boolean isBsDay = !dateUtil.isHoliDay();

			this.mustInfo("nightlyBatch tbsDyf = " + tbsDyf);
			this.mustInfo("nightlyBatch mfbsDy = " + mfbsDy);
			this.mustInfo("nightlyBatch nowDy = " + nowDy);
			this.mustInfo("nightlyBatch nowDy isBsDay = " + isBsDay);

			String autoBatchFg = "";

			SystemParasService sSystemParasService = MySpring.getBean("systemParasService", SystemParasService.class);
			SystemParas tSystemParas = sSystemParasService.findById("LN");
			if (tSystemParas != null) {
				autoBatchFg = tSystemParas.getAutoBatchFg();
			}

			if (autoBatchFg == null || autoBatchFg.isEmpty() || !autoBatchFg.equals("Y")) {
				this.mustInfo("nightlyBatch 自動批次記號不為Y，不執行自動批次");
				this.mustInfo("nightlyBatch finished.");
				return;
			}
			if (!isBsDay) {
				this.mustInfo("nightlyBatch 當日不為營業日，不執行自動批次");
				this.mustInfo("nightlyBatch finished.");
				return;
			}
			if (nowDy == mfbsDy) {
				this.mustInfo("nightlyBatch 當日為月底營業日，不執行自動批次");
				this.mustInfo("nightlyBatch finished.");
				return;
			}

			// 當日等於系統會計日期(一般營業日日批)
			if (nowDy == tbsDyf) {
				this.mustInfo("nightlyBatch 當日與系統會計日期相同,檢察官帳檔");
				AcCloseId acCloseId = new AcCloseId();
				acCloseId.setAcDate(tbsDyf);
				acCloseId.setBranchNo("0000");
				acCloseId.setSecNo("09");

				AcCloseService acCloseService = MySpring.getBean("acCloseService", AcCloseService.class);
				AcClose tAcClose = acCloseService.findById(acCloseId);

				// 檢核2:會計業務關帳控制檔的會計日期為當日且業務類別為09放款時,關帳狀態為1關帳才繼續
				if (!Objects.isNull(tAcClose) && tAcClose.getClsFg() == 1) {

					this.mustInfo("nightlyBatch 更新日期檔批次日期");

					this.batchBegin(titaVo, "BATCH", tbsDyf + ""); // 更新日期檔批次日期

					this.mustInfo("nightlyBatch 啟動批次");

					this.callJob("eodFlow", titaVo); // 啟動批次
				} else {
					this.mustInfo("nightlyBatch 會計業務關帳控制檔的會計日期為當日且業務類別為09放款時,關帳狀態不為1關帳,不啟動批次");
				}
			} else {
				this.mustInfo("nightlyBatch 當日與系統會計日期不同,不啟動批次");
			}
			txBuffer = null;
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("nightlyBatch error :" + errors.toString());
		} finally {
			titaVo.clear();
			titaVo = null;
			this.mustInfo("nightlyBatch finished.");
		}

	}
}
