package com.st1.itx.util.common;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;

import com.st1.itx.db.domain.CdSupv;
import com.st1.itx.db.service.CdSupvService;

@Component("sendRsp")
@Scope("prototype")

public class SendRsp extends CommBuffer {

	/* DB服務注入 */
	@Autowired
	public CdSupvService cdSupvService;

	/**
	 * 
	 * @param txBuffer txBuffer
	 * @param titaVo   titaVo
	 * @param no       理由代碼(可查詢L6066或9999表自行描述理由)
	 * @param msg      理由說明(理由代碼為9999時，不可空白)
	 * @throws LogicException LogicException
	 */
	public void addvReason(TxBuffer txBuffer, TitaVo titaVo, String no, String msg) throws LogicException {
		putReason(txBuffer, titaVo, no, msg);
	}

	/**
	 * 
	 * @param txBuffer txBuffer
	 * @param titaVo   titaVo
	 * @param no       理由代碼(可查詢L6066或9999表自行描述理由)
	 * @param msg      理由說明(理由代碼為9999時，不可空白)
	 * @throws LogicException LogicException
	 */
	public void addReason(TxBuffer txBuffer, TitaVo titaVo, String no, String msg) throws LogicException {
		putReason(txBuffer, titaVo, no, msg);
	}

	/**
	 * 
	 * @param txBuffer txBuffer
	 * @param titaVo   titaVo
	 * @param no       理由代碼(可查詢L6066或9999表自行描述理由)
	 * @throws LogicException LogicException
	 */
	public void addReason(TxBuffer txBuffer, TitaVo titaVo, String no) throws LogicException {
		putReason(txBuffer, titaVo, no, "");
	}

	private void putReason(TxBuffer txBuffer, TitaVo titaVo, String no, String msg) throws LogicException {
		this.info("SendRsp putReason : " + no + "/" + msg);

		this.txBuffer = txBuffer;

		if (no == null || "".equals(no)) {
			throw new LogicException(titaVo, "EC004", "主管援權理由代碼不可空白");
		}

		if (!"9999".equals(no)) {
			CdSupv cdSupv = cdSupvService.findById(no);
			if (cdSupv == null) {
				throw new LogicException(titaVo, "EC001", "主管授權理由檔(CdSupv):" + no);
			}
			if (msg == null || "".equals(msg)) {
				msg = cdSupv.getSupvReasonItem();
			} else {
				msg = cdSupv.getSupvReasonItem() + "，" + msg;
			}
		} else if (msg == null || "".equals(msg)) {
			throw new LogicException(titaVo, "EC004", "主管援權理由代碼9999，理由說明不可空白");
		}

		String nmsg = msg.replace(";", "；");

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("no", no);
		map.put("msg", nmsg);

		this.txBuffer.getRspList().add(map);

	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
