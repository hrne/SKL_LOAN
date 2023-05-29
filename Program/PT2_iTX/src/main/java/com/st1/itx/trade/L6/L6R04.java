package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L6R04") // 尋找各類代碼檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R04 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R04 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCode = this.parse.stringToInteger(titaVo.getParam("FunCode"));
		String iDefCode = titaVo.getParam("DefCode");
		String iCode = titaVo.getParam("Code");

		CdCodeId tCdCodeId2 = new CdCodeId("CodeType", iDefCode);
		CdCode tCdCode2 = sCdCodeService.findById(tCdCodeId2, titaVo);

		if (tCdCode2 == null) {
			throw new LogicException(titaVo, "E0001", "代碼檔代碼:CodeType/" + iDefCode);
		}

		CdCodeId tCdCodeId = new CdCodeId(iDefCode, iCode);
		CdCode tCdCode = sCdCodeService.findById(tCdCodeId, titaVo);

		if (tCdCode == null) {
			if (iFunCode == 1) {
				tCdCode = new CdCode();
				tCdCode.setCdCodeId(tCdCodeId);
				tCdCode.setDefType(tCdCode2.getDefType());
				tCdCode.setItem("");
				tCdCode.setEnable("Y");
				moveTotaCdDef(tCdCode);
			} else {
				throw new LogicException(titaVo, "E0001", "代碼:" + iDefCode + "/" + iCode);
			}
		} else {
			if (iFunCode == 1) {
				throw new LogicException(titaVo, "E0002", "代碼:" + iDefCode + "/" + iCode);
			}
			tCdCode.setDefType(tCdCode.getDefType());
			moveTotaCdDef(tCdCode);
		}

		// 初始值Tota

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 各類代碼檔
	private void moveTotaCdDef(CdCode mCdCode) throws LogicException {
		this.totaVo.putParam("L6R04DefType", mCdCode.getDefType());
		this.totaVo.putParam("L6R04DefCode", mCdCode.getDefCode());
		this.totaVo.putParam("L6R04Code", mCdCode.getCode());
		this.totaVo.putParam("L6R04Item", mCdCode.getItem());
		this.totaVo.putParam("L6R04Enable", mCdCode.getEnable());
		this.totaVo.putParam("L6R04MinCodeLength", mCdCode.getMinCodeLength());
		this.totaVo.putParam("L6R04MaxCodeLength", mCdCode.getMaxCodeLength());
		this.totaVo.putParam("L6R04IsNumeric", mCdCode.getIsNumeric());
		
	}

}