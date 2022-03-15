package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimSecNo=9,2
 */
@Service("L6R27") // 尋找會計業務關帳控制檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R27 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R27.class);

	/* DB服務注入 */
	@Autowired
	public AcCloseService AcCloseService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R27 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iRimSecNo = titaVo.getParam("RimSecNo");
		int clsFg = 0;

		// 查詢會計業務關帳控制檔
		AcCloseId tAcCloseId = new AcCloseId();
		tAcCloseId.setAcDate((this.txBuffer.getTxCom().getTbsdy()));
		tAcCloseId.setBranchNo(titaVo.getAcbrNo());
		tAcCloseId.setSecNo(iRimSecNo); // 業務類別: 01-撥款匯款 02-支票繳款 03-債協 09-放款
		AcClose tAcClose = AcCloseService.findById(tAcCloseId, titaVo);

		if (tAcClose == null) {
			throw new LogicException(titaVo, "E0001", "會計業務關帳控制檔"); // 查無資料
		}

		/* 將資料放入Tota */
		this.totaVo.putParam("L6R27SecNo", tAcClose.getSecNo());
		this.totaVo.putParam("L6R27ClsNo", tAcClose.getClsNo());
		this.totaVo.putParam("L6R27SlipNo", tAcClose.getSlipNo());
		this.totaVo.putParam("L6R27CoreSeqNo", tAcClose.getCoreSeqNo());

		// 傳票批號 , 02:支票繳款時固定為11
		if ("02".equals(iRimSecNo)) {
			this.totaVo.putParam("L6R27BatNo", 11);
		} else {
			this.totaVo.putParam("L6R27BatNo", tAcClose.getBatNo());
		}

		clsFg = tAcClose.getClsFg();
		this.info("L6R27 clsFg : " + clsFg);
		switch (clsFg) {
		case 0: // 0-開帳
			this.totaVo.putParam("L6R27ClsFg", 1);
			break;
		case 1: // 1-關帳
			this.totaVo.putParam("L6R27ClsFg", 2);
			break;
		case 2: // 2-關帳取消
			this.totaVo.putParam("L6R27ClsFg", 1);
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}