package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdGuarantor;
import com.st1.itx.db.service.CdGuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimGuaRelCode=X,2
 */
@Service("L6R08") // 尋找保證人關係代碼檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R08 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6R08.class);

	/* DB服務注入 */
	@Autowired
	public CdGuarantorService sCdGuarantorService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R08 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimGuaRelCode = titaVo.getParam("RimGuaRelCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R08"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R08"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdGuarantor(new CdGuarantor());

		// 查詢保證人關係代碼檔
		CdGuarantor tCdGuarantor = sCdGuarantorService.findById(iRimGuaRelCode, titaVo);

		/* 如有找到資料 */
		if (tCdGuarantor != null) {
			if (iRimTxCode.equals("L6607") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimGuaRelCode")); // 新增資料已存在
			} else {
				/* 將資料放入Tota */
				moveTotaCdGuarantor(tCdGuarantor);
			}
		} else {
			if (iRimTxCode.equals("L6607") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "保證人關係代碼檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 保證人關係代碼檔
	private void moveTotaCdGuarantor(CdGuarantor mCdGuarantor) throws LogicException {
		this.totaVo.putParam("L6R08GuaRelCode", mCdGuarantor.getGuaRelCode());
		this.totaVo.putParam("L6R08GuaRelItem", mCdGuarantor.getGuaRelItem());
		this.totaVo.putParam("L6R08GuaRelJcic", mCdGuarantor.getGuaRelJcic());
	}

}