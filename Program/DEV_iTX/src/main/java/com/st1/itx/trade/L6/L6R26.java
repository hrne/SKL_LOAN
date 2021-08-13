package com.st1.itx.trade.L6;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.CdAcBook;
import com.st1.itx.db.domain.CdAcBookId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.CdAcBookService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimAcBookCode=X,3
 */
@Service("L6R26") // 尋找帳冊別金額設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R26 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R26.class);

	/* DB服務注入 */
	@Autowired
	public CdAcBookService CdAcBookService;
	@Autowired
	public AcCloseService AcCloseService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R26 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimAcBookCode = titaVo.getParam("RimAcBookCode");
		String iRimAcSubBookCode = titaVo.getParam("RimAcSubBookCode");
		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R26"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 4)) {
			throw new LogicException(titaVo, "E0010", "L6R26"); // 功能選擇錯誤
		}
		if (iRimAcBookCode.isEmpty()) {
			throw new LogicException(titaVo, "E6011", "帳冊別"); // 查詢資料不可為空白
		}

		// 查詢會計業務關帳控制檔
		AcCloseId acCloseId = new AcCloseId();
		acCloseId.setAcDate((this.txBuffer.getTxCom().getTbsdy()));
		acCloseId.setBranchNo(titaVo.getAcbrNo());
		acCloseId.setSecNo("09"); // 業務類別: 09-放款
		AcClose tAcClose = AcCloseService.findById(acCloseId, titaVo);

		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "會計業務關帳控制檔"); // 查無資料
		}
		if (!(tAcClose.getClsFg() == 1)) {
			throw new LogicException(titaVo, "E6004", "帳冊別異動必須關帳後執行"); // 非關帳狀態
		}

		// 查詢帳冊別金額設定檔
		CdAcBook tCdAcBook = CdAcBookService.findById(new CdAcBookId(iRimAcBookCode,iRimAcSubBookCode), titaVo);

		/* 如有找到資料 */
		if (tCdAcBook != null) {
			if (iRimTxCode.equals("L6709") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimAcBookCode")); // 新增資料已存在
			} else if ((tCdAcBook.getActualAmt().compareTo(BigDecimal.ZERO) > 0) && iRimFuncCode == 4) {
				throw new LogicException(titaVo, "E6018", "放款實際金額有值時不可刪除"); // 刪除資料錯誤
			} else {
				/* 將資料放入Tota */
				this.totaVo.putParam("L6R26AcBookCode", tCdAcBook.getAcBookCode());
				this.totaVo.putParam("L6R26CurrencyCode", tCdAcBook.getCurrencyCode());
				this.totaVo.putParam("L6R26TargetAmt", tCdAcBook.getTargetAmt());
				this.totaVo.putParam("L6R26AssignSeq", tCdAcBook.getAssignSeq());
				this.totaVo.putParam("L6R26AcctSource", tCdAcBook.getAcctSource());
			}
		} else {
			if (iRimTxCode.equals("L6709") && iRimFuncCode == 1) {
				/* 初值放入Tota */
				this.totaVo.putParam("L6R26AcBookCode", iRimAcBookCode);
				this.totaVo.putParam("L6R26CurrencyCode", "");
				this.totaVo.putParam("L6R26TargetAmt", 0);
				this.totaVo.putParam("L6R26AssignSeq", 0);
				this.totaVo.putParam("L6R26AcctSource", "");
			} else {
				throw new LogicException(titaVo, "E0001", "帳冊別金額設定檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}