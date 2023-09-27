package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.springjpa.cm.L6064ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.data.DataLog;

/**
 * Tita FuncCode=9,1 BusinessType=X,2 JcicEmpName=x,8 JcicEmpTel=x,16 END=X,1
 */

@Service("L8502")
@Scope("prototype")
/**
 *
 *
 * @author St1
 * @version 1.0.0
 */
public class L8502 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SystemParasService sSystemParasService;
	@Autowired
	public CdCodeService sCdCodeService;
	@Autowired
	public L6064ServiceImpl l6064ServiceImpls;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8502 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFunCode = this.parse.stringToInteger(titaVo.getParam("FunCode"));
		String iDefCode = "";
		String iAmlHMLX = titaVo.getParam("AmlHMLX");
		if (titaVo.getParam("AmlHMLFg").equals("H")) {
			iDefCode = "H";
		} else if (titaVo.getParam("AmlHMLFg").equals("M")) {
			iDefCode = "M";
		} else {
			iDefCode = "L";
		}
		if(iFunCode!=1) {
			iDefCode = iDefCode+iAmlHMLX;
		}
			
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 217 * 200 = 43400

		Slice<CdCode> L6064DateList = null;

		L6064DateList = sCdCodeService.defCodeEq2("HMLCode", 8, "%" + iDefCode + "%", this.index, this.limit, titaVo);

		String io = "";
		int i = 0;
		if (iFunCode == 1 && L6064DateList != null) {
			for (CdCode t : L6064DateList) {
				i++;
				if (L6064DateList.getSize() == i) {
					String xCode = t.getCode().substring(1, 3);
					String iCode = t.getCode().substring(0, 1);
					io = iCode + parse.IntegerToString(parse.stringToInteger(xCode) + 1, 2);
				}
			}
		}else if (iFunCode == 1 && L6064DateList == null) {
			iDefCode = iDefCode + "01";
		}
//		else {
//			iDefCode = io;
//		}
		
		String iCode = iDefCode;
		int iDefType = 8;

		int iChkFg = 0;

		// 更新各類代碼檔
		CdCodeId tCdCodeId = new CdCodeId("HMLCode", iCode);

		CdCode tCdCode = sCdCodeService.holdById(tCdCodeId);

		switch (iFunCode) {
		case 1: // 新增
			if (tCdCode != null) {
				throw new LogicException(titaVo, "E0002", "代碼:" + iDefCode + "/" + iCode);
			}
			tCdCode = new CdCode();
			tCdCode.setCdCodeId(tCdCodeId);
			tCdCode = moveCdDef(tCdCode, iDefType, titaVo);
			this.info("tCdCodeDefcode  = " + tCdCode.getCode());
			String str = titaVo.getParam("AmlHMLFg").trim();
			boolean isNumeric = str.matches("[+-]?\\d*(\\.\\d+)?");

			String iIsNumeric = tCdCode.getIsNumeric();

			if ("N".equals(iIsNumeric)) {
				if (!isNumeric) {
					throw new LogicException(titaVo, "E0005", "新增資料有誤，該代碼檔代號限輸入數字");
				}
			}

			tCdCode.setIsNumeric("N");

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
			if (iDefCode.equals("CodeType")) {
				this.info("Synchronize CodeType");
				iChkFg = updDefType(iCode, iDefType, iChkFg, titaVo);
			}

			CdCode tCdCode2 = (CdCode) dataLog.clone(tCdCode);
			try {
				tCdCode = moveCdDef(tCdCode, iDefType, titaVo);
				tCdCode.setIsNumeric("N");
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
			String ixDefCode = iDefCode;
			String ixt = "%";
			if (ixDefCode.equals("CodeType")) {
				Slice<CdCode> icCdCode = sCdCodeService.defCodeEq("AmlHMLFg", ixt, 0, Integer.MAX_VALUE, titaVo);
				List<CdCode> isCdCode = icCdCode == null ? null : icCdCode.getContent();
				if (isCdCode != null) {
					throw new LogicException(titaVo, "E0008", "請先刪除代碼檔代號" + "AmlHMLFg" + "資料"); // 刪除資料不存在
				}
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

		Slice<CdCode> icCdCode = sCdCodeService.defCodeEq("CodeType", "HMLCode", 0, Integer.MAX_VALUE, titaVo);
		List<CdCode> isCdCode = icCdCode == null ? null : icCdCode.getContent();

		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE; // 217 * 200 = 43400

		Slice<CdCode> L6064DateList = null;
		String iDefCode = "";
		L6064DateList = sCdCodeService.defCodeEq2("HMLCode", 8, "%" + iDefCode + "%", this.index, this.limit, titaVo);

		String io = "";
		for (CdCode t : L6064DateList) {
			String xCode = t.getCode().substring(1, 3);
			String iCode = t.getCode().substring(0, 1);
			io = iCode + parse.IntegerToString(parse.stringToInteger(xCode) + 1, 2);
		}
		this.info("io2   = " + io);
		if (L6064DateList == null) {
			iDefCode = iDefCode + "01";
		} else {
			iDefCode = io;
		}

		tCdCode.setDefType(mDefType);
		tCdCode.setCode(iDefCode);
		tCdCode.setDefCode("HMLCode");
		tCdCode.setItem(titaVo.getParam("AmlEmp").trim()); // 2021-08-04 智偉修改:增加trim method,不留下空白
		tCdCode.setEnable("N");
		tCdCode.setMinCodeLength(1);
		tCdCode.setMaxCodeLength(3);

		for (CdCode iCdCode : isCdCode) {

			String ix = iCdCode.getIsNumeric();
			this.info("ix    = " + ix);
			if (ix == null) {
				tCdCode.setIsNumeric("N");
			} else {
				tCdCode.setIsNumeric(ix);
			}
		}

		CdCode cdCode = new CdCode();
		if (("CodeType").equals("HMLCode")) {
			Slice<CdCode> cCdCode = sCdCodeService.defCodeEq("AmlHMLFg", "%", 0, Integer.MAX_VALUE, titaVo);
			List<CdCode> sCdCode = cCdCode == null ? null : cCdCode.getContent();
			this.info("titaVoget   = " + "N");
			if (sCdCode != null) {
				for (CdCode iCdCode : sCdCode) {

					cdCode = new CdCode();
					this.info(
							"iCdCode==" + iCdCode.getDefCode() + ",code==" + iCdCode.getCode() + ",IsNumeric==" + "N");
					cdCode = sCdCodeService.holdById(new CdCodeId(iCdCode.getDefCode(), iCdCode.getCode()), titaVo);
					cdCode.setMinCodeLength(1);
					cdCode.setMaxCodeLength(3);
					cdCode.setIsNumeric("N");
					try {
						sCdCodeService.update2(cdCode, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}
			}

		}

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

		this.info("uDefType==" + uDefType + ",lCdCode Type==" + lCdCode.get(0).getDefType());
		if (uDefType == lCdCode.get(0).getDefType()) {
			uChkFg = 0;
			return uChkFg;
		}

		// 如有找到資料
		for (CdCode tCdCode : lCdCode) {
			CdCode uCdCode = new CdCode();
			uCdCode = sCdCodeService.holdById(new CdCodeId(tCdCode.getDefCode(), tCdCode.getCode()));
			this.info("IsNumeric   = " + "N");
			if (uCdCode != null) {
				try {
					uCdCode.setDefType(uDefType);
					uCdCode.setIsNumeric("N");
					sCdCodeService.update(uCdCode);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007",
							tCdCode.getDefCode() + "-" + tCdCode.getCode() + ":" + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
			} else {
				throw new LogicException(titaVo, "E0003", tCdCode.getDefCode() + "-" + tCdCode.getCode()); // 修改資料不存在
			}
		}
		return uChkFg;
	}
}
