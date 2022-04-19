package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdSupv;
import com.st1.itx.db.service.CdSupvService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimL6R09SupvReasonCode=X,4
 */
@Service("L6R09") // 尋找主管理由檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R09 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R09.class);

	/* DB服務注入 */
	@Autowired
	public CdSupvService sCdSupvService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R09 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimSupvReasonCode = titaVo.getParam("RimL6R09SupvReasonCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R09"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R09"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdSupv(new CdSupv());

		// 查詢主管理由檔
		CdSupv tCdSupv = sCdSupvService.findById(iRimSupvReasonCode, titaVo);

		/* 如有找到資料 */
		if (tCdSupv != null) {
			if (iRimTxCode.equals("L6606") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimSupvReasonCode); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdSupv(tCdSupv);
			}
		} else {
			if (iRimTxCode.equals("L6606") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "主管理由檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 主管理由檔
	private void moveTotaCdSupv(CdSupv mCdSupv) throws LogicException {
		this.totaVo.putParam("L6R09SupvReasonCode", mCdSupv.getSupvReasonCode());
		this.totaVo.putParam("L6R09SupvReasonItem", mCdSupv.getSupvReasonItem());
		this.totaVo.putParam("L6R09SupvReasonLevel", mCdSupv.getSupvReasonLevel());
		// this.totaVo.putParam("L6R09Enable", mCdSupv.getEnable());
	}

}