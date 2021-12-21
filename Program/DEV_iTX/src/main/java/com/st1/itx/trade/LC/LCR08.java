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
import com.st1.itx.db.domain.TxAttachType;
import com.st1.itx.db.service.TxAttachTypeService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LCR08")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class LCR08 extends TradeBuffer {
	@Autowired
	public TxAttachTypeService txAttachTypeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR08 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.getParam("FunCode");
		String iTranNo = titaVo.getParam("TranNo").trim();
		String iTypeItem = titaVo.getParam("TypeItem").trim();

		if ("".equals(iTypeItem)) {
			throw new LogicException(titaVo, "E0005", "常用類別不可空白");
		}
		TxAttachType txAttachType = txAttachTypeService.findByTypeItemFirst(iTranNo, iTypeItem, titaVo);

		if ("1".equals(iFunCode)) {
			if (txAttachType != null) {
				throw new LogicException(titaVo, "E0002", "附件類別");
			}

			txAttachType = new TxAttachType();

			txAttachType.setTranNo(iTranNo);
			txAttachType.setTypeItem(iTypeItem);

			try {
				txAttachTypeService.insert(txAttachType, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg());
			}

			// this.totaVo.setWarnMsg("已成功加入常用附件類別");
		} else {
			if (txAttachType == null) {
				throw new LogicException(titaVo, "E0004", "移除附件類別:" + iTranNo + "/" + iTypeItem);
			}
			try {
				txAttachTypeService.delete(txAttachType, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg());
			}
			// this.totaVo.setWarnMsg("已成功從常用附件類別移除");
		}

		totaVo.putParam("TypeItemHelp", getTxAttachType(iTranNo));

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String getTxAttachType(String tranNo) {
		String s = "";

		this.info("XXR99 getTxAttachType TranNo= " + tranNo);

		Slice<TxAttachType> slTxAttachType = txAttachTypeService.findByTranNo(tranNo, 0, Integer.MAX_VALUE);
		List<TxAttachType> lTxAttachType = slTxAttachType == null ? null : slTxAttachType.getContent();

		if (lTxAttachType != null && lTxAttachType.size() > 0) {
			for (TxAttachType txAttachType : lTxAttachType) {
				if (!"".equals(s)) {
					s += ";";
				}
				s += txAttachType.getTypeItem() + ":";
			}
		}
		return s;
	}
}