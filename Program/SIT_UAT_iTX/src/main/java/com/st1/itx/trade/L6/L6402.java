package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6402")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6402 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6402.class);

	/* DB服務注入 */
	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6402 ");

		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iTranNo = titaVo.get("TranNo").trim();

		TxTranCode tTxTranCode = txTranCodeService.holdById(iTranNo);

		if (tTxTranCode == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "交易代碼:" + iTranNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "交易代碼:" + iTranNo);
			}

			tTxTranCode = new TxTranCode();
			tTxTranCode = MoveToDb(iTranNo, tTxTranCode, titaVo);
			tTxTranCode.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tTxTranCode.setCreateEmpNo(titaVo.getTlrNo());
			try {
				txTranCodeService.insert(tTxTranCode);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "交易代碼:" + iTranNo);
			}
			try {
				if ("2".equals(iFunCode)) {
					tTxTranCode = MoveToDb(iTranNo, tTxTranCode, titaVo);
					tTxTranCode.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tTxTranCode.setLastUpdateEmpNo(titaVo.getTlrNo());
					txTranCodeService.update(tTxTranCode);
				} else if ("4".equals(iFunCode)) {
					txTranCodeService.delete(tTxTranCode);
				}
			} catch (DBException e) {
				if ("2".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0007", e.getErrorMsg());
				} else if ("4".equals(iFunCode)) {
					throw new LogicException(titaVo, "E0008", e.getErrorMsg());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private TxTranCode MoveToDb(String TranNo, TxTranCode tTxTranCode, TitaVo titaVo) throws LogicException {

		tTxTranCode.setTranNo(TranNo);
		tTxTranCode.setTranItem(titaVo.get("TranItem").trim());
		tTxTranCode.setDesc(titaVo.get("Desc").trim());
		tTxTranCode.setTypeFg(Integer.valueOf(titaVo.get("TypeFg")));
		tTxTranCode.setCancelFg(Integer.valueOf(titaVo.get("CancelFg")));
		tTxTranCode.setModifyFg(Integer.valueOf(titaVo.get("ModifyFg")));
		tTxTranCode.setMenuNo(titaVo.get("MenuNo").trim());
		tTxTranCode.setSubMenuNo(titaVo.get("SubMenuNo").trim());
		tTxTranCode.setStatus(Integer.valueOf(titaVo.get("Status")));
		tTxTranCode.setMenuFg(Integer.valueOf(titaVo.get("MenuFg")));
		tTxTranCode.setSubmitFg(Integer.valueOf(titaVo.get("SubmitFg")));

		return tTxTranCode;

	}
}