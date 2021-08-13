package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.MlaundryParas;
import com.st1.itx.db.service.MlaundryParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimBusinessType=X,2
 */
@Service("L8R49") // 尋找疑似洗錢樣態條件設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L8R49 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R49.class);

	/* DB服務注入 */
	@Autowired
	public MlaundryParasService sMlaundryParasService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R49 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimBusinessType = titaVo.getParam("RimBusinessType");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L8R49"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L8R49"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaMlaundryParas(new MlaundryParas());

		// 查詢疑似洗錢樣態條件設定檔
		MlaundryParas tMlaundryParas = sMlaundryParasService.findById(iRimBusinessType, titaVo);

		/* 如有找到資料 */
		if (tMlaundryParas != null) {
			if (iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", titaVo.getParam("RimBusinessType")); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaMlaundryParas(tMlaundryParas);
			}
		} else {
			if (iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				if(!("L8201").equals(iRimTxCode)) {
					throw new LogicException(titaVo, "E0001", "疑似洗錢樣態條件設定檔"); // 查無資料
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 疑似洗錢樣態條件設定檔
	private void moveTotaMlaundryParas(MlaundryParas mMlaundryParas) throws LogicException {

		this.totaVo.putParam("L8R49Factor1TotLimit", mMlaundryParas.getFactor1TotLimit());
		this.totaVo.putParam("L8R49Factor2Count", mMlaundryParas.getFactor2Count());
		this.totaVo.putParam("L8R49Factor2AmtStart", mMlaundryParas.getFactor2AmtStart());
		this.totaVo.putParam("L8R49Factor2AmtEnd", mMlaundryParas.getFactor2AmtEnd());
		this.totaVo.putParam("L8R49Factor3TotLimit", mMlaundryParas.getFactor3TotLimit());
		this.totaVo.putParam("L8R49FactorDays", mMlaundryParas.getFactorDays());
	}

}