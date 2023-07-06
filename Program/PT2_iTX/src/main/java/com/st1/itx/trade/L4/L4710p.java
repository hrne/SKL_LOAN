package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SmsCom;

@Service("L4710p")
@Scope("prototype")
/**
 * 2023-07-06 新增 by ST1 Wei from SKL 琦欣<BR>
 * 用API傳送的時候要背景作業
 * 
 * @author ST1 Wei
 * @version 1.0.0
 */
public class L4710p extends TradeBuffer {

	@Autowired
	private TxToDoDetailService txToDoDetailService;

	@Autowired
	private SystemParasService systemParasService;

	@Autowired
	private SmsCom smsCom;

	private String smsFtpFlag = "Y";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4710p ");
		this.totaVo.init(titaVo);

		sendMsgByAPI(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void sendMsgByAPI(TitaVo titaVo) throws LogicException {

		txBuffer.init(titaVo);

		this.info("txBuffer.getTxCom().getTbsdy() : " + txBuffer.getTxCom().getTbsdy());
		this.info("txBuffer.getTxCom().getNbsdy() : " + txBuffer.getTxCom().getNbsdy());

		SystemParas systemParas = systemParasService.findById("LN", titaVo);

		if (systemParas != null) {
			smsFtpFlag = systemParas.getSmsFtpFlag();
		}
		this.info("L4710p smsFtpFlag = " + smsFtpFlag);

		switch (smsFtpFlag) {
		case "T":
			// 測試模式: 用測試資料測試對方API承受力
			this.info("L4710p test mode");
			// 2023-07-05 Wei from SKL 琦欣 說資訊長要他測API能撐最多幾筆
			String mobile = "0900000000";
			String msg = "測試訊息";
			this.info("L4710p sendSms test mobile = " + mobile);
			this.info("L4710p sendSms test msg = " + msg);
			for (int i = 1; i <= 10000; i++) {
				this.info("L4710p sendSms 測試第" + i + "次");
				smsCom.sendSms(titaVo, mobile, msg);
			}
			this.info("L4710p sendSms 測試完成");
			break;
		case "A":
			// API模式: 把簡訊資料一筆一筆送出去
			this.info("L4710p API mode");
			queryDataAndSend(titaVo);
			break;
		default:
			// 不可接受其他記號
			this.info("L4710p return.");
			return;
		}

	}

	private void queryDataAndSend(TitaVo titaVo) throws LogicException {
		Slice<TxToDoDetail> sTxToDoDetail = null;

		sTxToDoDetail = txToDoDetailService.detailStatusRange("TEXT00", 0, 0, 0, Integer.MAX_VALUE);

		if (sTxToDoDetail == null || sTxToDoDetail.isEmpty()) {
			this.info("簡訊媒體檔,本日無資料");
			return;
		}

		for (TxToDoDetail tTxToDoDetail : sTxToDoDetail) {
			// 2023-07-04 Wei 改回用FTP from SKL 琦欣
			// 這裡改用參數修改 如果他們又想改用API 就把SystemParas.SmsFtpFlag改為A
			if (smsFtpFlag.equals("A")) {
				// 2023-05-29 Wei 改用API傳送 from SKL 琦欣
				String proccessNote = tTxToDoDetail.getProcessNote();
				String[] data = proccessNote.split(",");

				if (data.length >= 4) {
					String mobile = data[2];
					String msg = data[3];
					this.info("L4710 sendSms api mobile = " + mobile);
					this.info("L4710 sendSms api msg = " + msg);
					smsCom.sendSms(titaVo, mobile, msg);
				}
			}
		}
	}
}