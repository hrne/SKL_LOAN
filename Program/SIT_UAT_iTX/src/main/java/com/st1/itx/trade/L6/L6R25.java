package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.service.springjpa.cm.L6R25ServiceImpl;
import com.st1.itx.util.common.data.L6R25Vo;
import com.st1.itx.db.domain.TxAuthGroup;
import com.st1.itx.db.service.TxAuthGroupService;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L6R25")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0 rim for L6403
 */
public class L6R25 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R25.class);

	/* DB服務注入 */
//	@Autowired
//	public TxTranCodeService sTxTranCodeService;

	@Autowired
	public L6R25ServiceImpl L6R25ServiceImpl;

	@Autowired
	public TxAuthGroupService sTxAuthGroupService;

	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	private class apClass {
		//
		int cnt;
		int len;
		String text;

		void init() {
			this.cnt = 0;
			this.len = 0;
			this.text = "";
		}

		//
		void put(int type, String sMenu, String txcd, String desc, int authfg) {
			// type(類別):0.子選單 2.查詢交易 3.維護交易
			// sMenu(子選單)
			// txcd(子選單代號/交易代號)
			// Desc(子選單說明/交易明稱)
			// AuthFg(權限記號):0.無權限 1.有權限

			logger.info("L6R25 put = " + type + "/" + txcd + "/" + authfg);
			this.cnt++;
			int clen = clength(desc);
			String ts = String.format("%01d%-2s%-5s%03d%s%01d", type, sMenu, txcd, clen, desc, authfg);
			len += (12 + clen);
			text += ts;
		}

		String get() {
			len += 3;
			return String.format("%04d%03d%s", this.len, this.cnt, this.text);
		}

	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R25 ");
		this.totaVo.init(titaVo);

		String iFunCode = titaVo.get("FunCode").trim();
		String iAuthNo = titaVo.get("AuthNo").trim();

		TxAuthGroup tTxAuthGroup = sTxAuthGroupService.findById(iAuthNo, titaVo);

		if (tTxAuthGroup == null) {
			if ("2".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0003", "權限群組:" + iAuthNo);
			} else if ("4".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0004", "權限群組:" + iAuthNo);
			}

			MoveAuthGroup(new TxAuthGroup());
		} else {
			if ("1".equals(iFunCode)) {
				throw new LogicException(titaVo, "E0002", "權限群組:" + iAuthNo);
			}

			MoveAuthGroup(tTxAuthGroup);
		}

		String ApL = "";
		for (int i = 1; i < 10; i++) {
			ApL += apProc(iAuthNo, "L" + i, titaVo);
		}

		this.info("L6R25 ApL = " + ApL);
		this.totaVo.putParam("ApL", ApL);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveAuthGroup(TxAuthGroup tTxAuthGroup) {
		this.totaVo.putParam("AuthItem", tTxAuthGroup.getAuthItem());
		this.totaVo.putParam("Desc", tTxAuthGroup.getDesc());
		this.totaVo.putParam("Status", tTxAuthGroup.getStatus());
		this.totaVo.putParam("BranchNo", tTxAuthGroup.getBranchNo());
		this.totaVo.putParam("LevelFg", tTxAuthGroup.getLevelFg());
	}

	private String apProc(String authNo, String apType, TitaVo titaVo) {
		apClass apData = new apClass();

		apData.init();

		List<L6R25Vo> l6r25List = null;
		try {
			l6r25List = L6R25ServiceImpl.findAll(authNo, apType);
			this.info("l6r25List size = " + l6r25List.size());
			String subMenu = "";
			for (L6R25Vo tL6R25Vo : l6r25List) {
				if (!subMenu.equals(tL6R25Vo.getSubMenuNo())) {
					subMenu = tL6R25Vo.getSubMenuNo();
					apData.put(0, subMenu, subMenu, tL6R25Vo.getSubMenuItem().trim(), 0);
				}
				apData.put(tL6R25Vo.getTypeFg(), tL6R25Vo.getSubMenuNo(), tL6R25Vo.getTranNo(), tL6R25Vo.getTranItem().trim(), tL6R25Vo.getAuthFg());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.info("L6R25ServiceImpl.findAll error ! ");
		}

//		this.totaVo.putParam("Ap"+apType, apData.get());
		return apData.get();

	}

	// 中文以2位計算長度
	private static int clength(String s) {
		int clen = 0;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			if (c.matches("[\\u0391-\\uFFE5]+")) {
				clen += 2;
			} else {
				clen += 1;
			}

//			this.info("XXR99 clength [" + c + "] = " + clen);
		}

		return clen;
	}

}