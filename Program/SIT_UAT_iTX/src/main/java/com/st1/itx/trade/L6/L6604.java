package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Slice;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 DefNo=X,4 DefType=X,2 DefCode=X,20 DefItem=X,80 Code=X,10
 * Item=X,100 Enable=X,1 END=X,1
 */

@Service("L6604")
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6604 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6604 ");
		this.info("active L6604 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCode = this.parse.stringToInteger(titaVo.getParam("FunCode"));
		String iDefCode = titaVo.getParam("DefCode");
		String iCode = titaVo.getParam("Code");
		int iDefType = this.parse.stringToInteger(titaVo.getParam("DefType"));
		int iChkFg = 0;

		// 檢查輸入資料
		if (!(iFunCode >= 1 && iFunCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6604"); // 功能選擇錯誤
		}

		// 維護代碼時,代碼檔必須先建立
		CdCodeId tCdCodeIdT = new CdCodeId("CodeType", iDefCode);
		CdCode tCdCodeT = sCdCodeService.findById(tCdCodeIdT);

		if (tCdCodeT == null) {
			throw new LogicException(titaVo, "E0001", "代碼檔代碼:CodeType/" + iDefCode);
		}
		this.info("L6604 tCdCodeT.getDefType() : " + tCdCodeT.getDefType());

		// 代碼的[業務類別]同代碼檔
		if (!(iDefCode.equals("CodeType"))) {
			iDefType = tCdCodeT.getDefType();
		}

		// 更新各類代碼檔
//    CdCode tCdCode = new CdCode();
		CdCodeId tCdCodeId = new CdCodeId(iDefCode, iCode);

		CdCode tCdCode = sCdCodeService.holdById(tCdCodeId);

		switch (iFunCode) {
		case 1: // 新增
			if (tCdCode != null) {
				throw new LogicException(titaVo, "E0002", "代碼:" + iDefCode + "/" + iCode);
			}
			tCdCode = new CdCode();
			tCdCode.setCdCodeId(tCdCodeId);
			tCdCode = moveCdDef(tCdCode, iDefType, titaVo);
			try {
				sCdCodeService.insert(tCdCode, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", iDefCode + "-" + iCode + ":" + e.getErrorMsg()); // 新增資料已存在
				} else {
					throw new LogicException(titaVo, "E0005", iDefCode + "-" + iCode + ":" + e.getErrorMsg()); // 新增資料時，發生錯誤
				}
			}
			break;

		case 2: // 修改
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0003", iDefCode + "-" + iCode); // 修改資料不存在
			}

			// 代碼檔修改[業務類別]時,其下代碼一併修改
			if (iDefCode.equals("CodeType") && (!(iDefType == tCdCode.getDefType()))) {
				iChkFg = updDefType(iCode, iDefType, iChkFg, titaVo);
			}

			CdCode tCdCode2 = (CdCode) dataLog.clone(tCdCode);
			try {
				tCdCode = moveCdDef(tCdCode, iDefType, titaVo);
				tCdCode = sCdCodeService.update2(tCdCode, titaVo); ////
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", iDefCode + "-" + iCode + ":" + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode2, tCdCode); ////
			dataLog.exec("修改各類代碼檔"); ////
			break;

		case 4: // 刪除
			if (tCdCode == null) {
				throw new LogicException(titaVo, "E0004", iDefCode + "-" + iCode); // 刪除資料不存在
			}
			try {
				sCdCodeService.delete(tCdCode);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", iDefCode + "-" + iCode + ":" + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
			dataLog.setEnv(titaVo, tCdCode, tCdCode); ////
			dataLog.exec("刪除各類代碼檔"); ////
			break;
			
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private CdCode moveCdDef(CdCode tCdCode, int mDefType, TitaVo titaVo) throws LogicException {

		tCdCode.setDefType(mDefType);
		tCdCode.setDefCode(titaVo.getParam("DefCode"));
		tCdCode.setCode(titaVo.getParam("Code"));
		tCdCode.setItem(titaVo.getParam("Item").trim()); // 2021-08-04 智偉修改:增加trim method,不留下空白
		tCdCode.setEnable(titaVo.getParam("Enable"));

		return tCdCode;
	}

	// 代碼檔修改[業務類別]時,其下代碼一併修改
	private int updDefType(String uCode, int uDefType, int uChkFg, TitaVo titaVo) throws LogicException {

		Slice<CdCode> slCdCode;
		slCdCode = sCdCodeService.defCodeEq(uCode, "%", this.index, Integer.MAX_VALUE);
		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();

		if (lCdCode == null || lCdCode.size() == 0) {
			this.info("L6604 updDefType notfound : " + uChkFg);
			uChkFg = 0;
			return uChkFg;
		}

		// 如有找到資料
		for (CdCode tCdCode : lCdCode) {

			CdCode uCdCode = new CdCode();
			uCdCode = sCdCodeService.holdById(new CdCodeId(tCdCode.getDefCode(), tCdCode.getCode()));

			if (uCdCode != null) {
				try {
					uCdCode.setDefType(uDefType);
					sCdCodeService.update(uCdCode);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", tCdCode.getDefCode() + "-" + tCdCode.getCode() + ":" + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0003", tCdCode.getDefCode() + "-" + tCdCode.getCode()); // 修改資料不存在
			}

		}

		return uChkFg;

	}

}