package com.st1.itx.trade.LC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxAttachment;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxAttachmentService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("LC105")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class LC105 extends TradeBuffer {
	@Autowired
	public TxAttachmentService txAttachmentService;

	@Autowired
	public CdEmpService cdEmpService;
	
	@Autowired
	public DataLog iDataLog;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC105 ");
		this.totaVo.init(titaVo);

		String iTranNo = titaVo.getParam("TranNo");
		String iMrKey = titaVo.getParam("MrKey");
		String sFileNo = titaVo.getParam("FileNo");
		Long iFileNo = 0L;
		if(!"".equals(sFileNo)) {
			iFileNo = Long.parseLong(sFileNo);			
		}else {
			iFileNo = 0L;
		}
		this.info("iFileNo   = " + iFileNo);
		
		Slice<TxAttachment> slTxAttachment = txAttachmentService.findByTran(iTranNo, iMrKey, 0, Integer.MAX_VALUE,
				titaVo);
		List<TxAttachment> lTxAttachment = slTxAttachment == null ? null : slTxAttachment.getContent();

		String iTranKey_Tmp = titaVo.getParam("TranKey_Tmp").trim();

		if (lTxAttachment == null || lTxAttachment.size() == 0) {
			throw new LogicException(titaVo, "E0001", "附件資料"); // 查無資料
		}
		TxAttachment uTxAttachment = new TxAttachment();
		if ("4".equals(iTranKey_Tmp)) {
			uTxAttachment = txAttachmentService.holdById(iFileNo, titaVo);
			if (uTxAttachment == null) {
				throw new LogicException("E0004", "");
			}
			try {
				txAttachmentService.delete(uTxAttachment, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "附件檔資料");
			}
			iDataLog.setEnv(titaVo, uTxAttachment, uTxAttachment);
			iDataLog.exec("LC105刪除");

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}