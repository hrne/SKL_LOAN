package com.st1.itx.trade.L5;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.service.TxAttachmentService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R49")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R49 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public TxAttachmentService iTxAttachmentService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R49 ");
		this.totaVo.init(titaVo);

		Slice<TxAttachment> iTxAttachment = null;
		iTxAttachment = iTxAttachmentService.findOnlyTran("L5102", 0, Integer.MAX_VALUE, titaVo);

		if (iTxAttachment == null) {
			totaVo.putParam("L5R49FileNo", 1);
		} else {
			totaVo.putParam("L5R49FileNo", iTxAttachment.getSize() + 1);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}