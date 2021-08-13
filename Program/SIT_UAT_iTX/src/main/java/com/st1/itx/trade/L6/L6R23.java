package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimAcNoCode=X,8
 * RimAcSubCode=X,5
 * RimAcDtlCode=X,2
 */
@Service("L6R23") // 尋找會計科子細目設定檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R23 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L6R23.class);

	/* DB服務注入 */
	@Autowired
	public CdAcCodeService sCdAcCodeService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R23 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimAcNoCode = titaVo.getParam("RimAcNoCode");
		String iRimAcSubCode = titaVo.getParam("RimAcSubCode");
		String iRimAcDtlCode = titaVo.getParam("RimAcDtlCode");

		this.info(" L6R23 titaVo : " + titaVo.getParam("RimAcNoCode") + "-" + titaVo.getParam("RimAcSubCode") + "-" + titaVo.getParam("RimAcDtlCode") + "~~~~~");

		if (iRimAcNoCode.isEmpty()) {
			iRimAcNoCode = "        ";
		}
		if (iRimAcSubCode.isEmpty()) {
			iRimAcSubCode = "     ";
		}
		if (iRimAcDtlCode.isEmpty()) {
			iRimAcDtlCode = "  ";
		}

		this.info(" L6R23 iRim   : " + iRimAcNoCode + "-" + iRimAcSubCode + "-" + iRimAcDtlCode + ".");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R23"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R23"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdAcCode(new CdAcCode());

		// 查詢會計科子細目設定檔
		// testing
		// CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iRimAcNoCode,"
		// "," "));
		// CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iRimAcNoCode,"
		// "," "));
		// System.out.println(tCdAcCode);
		CdAcCode tCdAcCode = sCdAcCodeService.findById(new CdAcCodeId(iRimAcNoCode, iRimAcSubCode, iRimAcDtlCode), titaVo);
		// testing-end

		/* 如有找到資料 */
		if (tCdAcCode != null) {
			if (iRimTxCode.equals("L6601") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", iRimAcNoCode + "-" + iRimAcSubCode + "-" + iRimAcDtlCode); // 新增資料已存在
			} else if (iRimTxCode.equals("L6201") && iRimFuncCode == 5 && (tCdAcCode.getAcctFlag() != 0)) {
				throw new LogicException(titaVo, "E6006", iRimAcNoCode + "-" + iRimAcSubCode + "-" + iRimAcDtlCode); // 資負明細科目不可經由[其他傳票輸入]交易入帳
			} else if (iRimTxCode.equals("L6201") && iRimFuncCode == 5 && (tCdAcCode.getClassCode() != 0)) {
				throw new LogicException(titaVo, "E6007", iRimAcNoCode + "-" + iRimAcSubCode + "-" + iRimAcDtlCode); // 非可入帳科目
			} else if (iRimTxCode.equals("L6908") && iRimFuncCode == 5 && (tCdAcCode.getReceivableFlag() == 0) && (!(tCdAcCode.getAcctFlag() == 1))) {
				throw new LogicException(titaVo, "E6009", iRimAcNoCode + "-" + iRimAcSubCode + "-" + iRimAcDtlCode); // 銷帳科目記號不符
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdAcCode(tCdAcCode);
			}
		} else {
			if ((iRimTxCode.equals("L6601") && iRimFuncCode == 1) || (iRimTxCode.equals("L6061") && iRimFuncCode == 5)) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "會計科子細目設定檔"); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 會計科子細目設定檔
	private void moveTotaCdAcCode(CdAcCode mCdAcCode) throws LogicException {
		this.totaVo.putParam("L6R23AcNoCode", mCdAcCode.getAcNoCode());
		this.totaVo.putParam("L6R23AcSubCode", mCdAcCode.getAcSubCode());
		this.totaVo.putParam("L6R23AcDtlCode", mCdAcCode.getAcDtlCode());
		this.totaVo.putParam("L6R23AcNoItem", mCdAcCode.getAcNoItem());
		this.totaVo.putParam("L6R23AcctCode", mCdAcCode.getAcctCode());
		this.totaVo.putParam("L6R23AcctItem", mCdAcCode.getAcctItem());
		this.totaVo.putParam("L6R23ClassCode", mCdAcCode.getClassCode());
		this.totaVo.putParam("L6R23AcBookFlag", mCdAcCode.getAcBookFlag());
		this.totaVo.putParam("L6R23DbCr", mCdAcCode.getDbCr());
		this.totaVo.putParam("L6R23AcctFlag", mCdAcCode.getAcctFlag());
		this.totaVo.putParam("L6R23ReceivableFlag", mCdAcCode.getReceivableFlag());
		this.totaVo.putParam("L6R23ClsChkFlag", mCdAcCode.getClsChkFlag());
		this.totaVo.putParam("L6R23InuseFlag", mCdAcCode.getInuseFlag());
	}

}