package com.st1.itx.trade.scheduled;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Transactional(value = "transactionManager")
public class ScheduledL4710 {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledL4710.class);

	@Autowired
	public TxBuffer txBuffer;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	TxToDoDetailService txToDoDetailService;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public TxToDoCom txToDoCom;

//	ScheduledL4710

	/*
	 * 秒（0~59） 分鐘（0~59） 小時（0~23） 天（月）（0~31，但是你需要考慮你月的天數） 月（0~11） 星期（1~7 1=SUN 或
	 * SUN，MON，TUE，WED，THU，FRI，SAT）
	 */
	@Scheduled(cron = "0 30 14 * * ?")
//	@Scheduled(cron = "0/29 * * * * ?")
	public void tt() {
		try {
			this.ex();
		} catch (LogicException e) {
			logger.info(e.getMessage());
		}
	}

	public void ex() throws LogicException {
		logger.info("每日 14:30 啟動...");
//		logger.info("每30 秒啟動...");

		TitaVo titaVo = new TitaVo();

//		預設tita Lable欄位為空值 & DB指定為online
		titaVo.init();

//		TxToDoCom need
		titaVo.putParam(ContentName.actfg, "0");
		titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		txBuffer.init(titaVo);

		dateUtil.init();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		int index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		int limit = 500;

		Slice<TxToDoDetail> sTxToDoDetail = null;

//		以日曆日抓取假日檔判斷是否假日，非假日才執行
		dateUtil.setDate_1(dateUtil.getNowIntegerForBC());
		dateUtil.setDate_2(dateUtil.getNowIntegerForBC());

		logger.info("dateUtil.getNowIntegerForBC()" + dateUtil.getNowIntegerForBC());
		logger.info("dateUtil.isHoliDay()" + dateUtil.isHoliDay());

		if (!dateUtil.isHoliDay()) {
			List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

			sTxToDoDetail = txToDoDetailService.detailStatusRange("TEXT00", 0, 0, index, limit);

			lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

			logger.info("txBuffer.getTxCom().getTbsdy() : " + txBuffer.getTxCom().getTbsdy());
			logger.info("txBuffer.getTxCom().getNbsdy() : " + txBuffer.getTxCom().getNbsdy());

//			temp path = D:\\tmp\\LNM56OP.txt
			makeFile.open(titaVo, txBuffer.getTxCom().getTbsdy(), "0000", "L4710", "L4710" + "-簡訊媒體檔", "LNM56OP.txt", 2);

			if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
				for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//					1.產出
					makeFile.put(tTxToDoDetail.getProcessNote());

					logger.info("tTxToDoDetail : " + tTxToDoDetail.toString());

//					2.回寫狀態
					TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
					tTxToDoDetailId.setCustNo(tTxToDoDetail.getCustNo());
					tTxToDoDetailId.setFacmNo(tTxToDoDetail.getFacmNo());
					tTxToDoDetailId.setBormNo(tTxToDoDetail.getBormNo());
					tTxToDoDetailId.setDtlValue(tTxToDoDetail.getDtlValue());
					tTxToDoDetailId.setItemCode(tTxToDoDetail.getItemCode());

					txToDoCom.setTxBuffer(txBuffer);
					txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
				}
				long sno = makeFile.close();

				logger.info("sno : " + sno);

				makeFile.toFile(sno);
			}
		}
		// 執行交易
//		try {
//			MySpring.newTask("L4710", this.txBuffer, titaVo);
//		} catch (LogicException e) {
//			throw new LogicException(titaVo, "XXXXX", e.getMessage());
//		}
	}
}
