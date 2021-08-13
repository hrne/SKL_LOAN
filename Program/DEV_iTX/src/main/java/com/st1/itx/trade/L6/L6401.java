package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6401")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L6401 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6401.class);

	/* DB服務注入 */
	@Autowired
	public TxTellerService txTellerService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6401 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iTlrNo = titaVo.get("TlrNo").trim();

		TxTeller tTxTeller = txTellerService.holdById(iTlrNo);

		if (tTxTeller == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "使用者:" + iTlrNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "使用者:" + iTlrNo);
			}

			tTxTeller = new TxTeller();
			tTxTeller = MoveToDb(iTlrNo, tTxTeller, titaVo);
			tTxTeller.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tTxTeller.setCreateEmpNo(titaVo.getTlrNo());
			try {
				txTellerService.insert(tTxTeller);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", e.getErrorMsg()); // 新增資料已存在
			}
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "使用者:" + iTlrNo);
			}
			try {
				if ("2".equals(iFunCode)) {
					tTxTeller = MoveToDb(iTlrNo, tTxTeller, titaVo);
					tTxTeller.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tTxTeller.setLastUpdateEmpNo(titaVo.getTlrNo());
					txTellerService.update(tTxTeller);
				} else if ("4".equals(iFunCode)) {
					txTellerService.delete(tTxTeller);
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

	private TxTeller MoveToDb(String iTlrNo, TxTeller tTxTeller, TitaVo titaVo) throws LogicException {

		tTxTeller.setTlrNo(iTlrNo);
		tTxTeller.setTlrItem(titaVo.getParam("TlrItem"));
		tTxTeller.setBrNo(titaVo.getParam("BrNo"));
		tTxTeller.setAdFg(Integer.valueOf(titaVo.get("AdFg")));
		tTxTeller.setLevelFg(Integer.valueOf(titaVo.get("LevelFg")));
		tTxTeller.setStatus(Integer.valueOf(titaVo.get("Status")));
		tTxTeller.setGroupNo(titaVo.getParam("GroupNo"));
		
//		tTxTeller.setEntdy(Integer.valueOf(titaVo.get("Entdy")));
//		tTxTeller.setTxtNo(Integer.valueOf(titaVo.get("TxtNo")));
		tTxTeller.setDesc(titaVo.getParam("Desc"));
		tTxTeller.setAmlHighFg(titaVo.getParam("AmlHighFg"));
		
		tTxTeller.setAuthNo(titaVo.getParam("AuthNo1"));
		tTxTeller.setAuthNo1(titaVo.getParam("AuthNo1"));
		tTxTeller.setAuthNo2(titaVo.getParam("AuthNo2"));
		tTxTeller.setAuthNo3(titaVo.getParam("AuthNo3"));
		tTxTeller.setAuthNo4(titaVo.getParam("AuthNo4"));
		tTxTeller.setAuthNo5(titaVo.getParam("AuthNo5"));
		tTxTeller.setAuthNo6(titaVo.getParam("AuthNo6"));
		tTxTeller.setAuthNo7(titaVo.getParam("AuthNo7"));
		tTxTeller.setAuthNo8(titaVo.getParam("AuthNo8"));
		tTxTeller.setAuthNo9(titaVo.getParam("AuthNo9"));
		tTxTeller.setAuthNo10(titaVo.getParam("AuthNo10"));

		return tTxTeller;

	}

}