package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimUnitCode=X,6
 */
@Service("L6R13") // 尋找分公司資料檔資料
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L6R13 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6R13 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimUnitCode = titaVo.getParam("RimUnitCode");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "L6R13"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "L6R13"); // 功能選擇錯誤
		}

		// 初始值Tota
		moveTotaCdBcm(new CdBcm(), titaVo);

		// 查詢分公司資料檔
		CdBcm tCdBcm = sCdBcmService.findById(iRimUnitCode, titaVo);

		/* 如有找到資料 */
		if (tCdBcm != null) {
			if (iRimTxCode.equals("L6755") && iRimFuncCode == 1) {
				throw new LogicException(titaVo, "E0002", "單位代號:" + iRimUnitCode); // 新增資料已存在
			} else {
				/* 將每筆資料放入Tota */
				moveTotaCdBcm(tCdBcm, titaVo);
			}
		} else {
			if (iRimTxCode.equals("L6755") && iRimFuncCode == 1) {
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "單位代號:" + iRimUnitCode); // 查無資料
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 分公司資料檔
	private void moveTotaCdBcm(CdBcm mCdBcm, TitaVo titaVo) throws LogicException {

		String unitManagerNm = "";
		String deptManagerNm = "";
		String distManagerNm = "";

		// 查詢員工資料檔
		if (!(mCdBcm.getUnitManager().isEmpty())) {
			unitManagerNm = findCdEmp(mCdBcm.getUnitManager(), unitManagerNm, titaVo);
		}
		if (!(mCdBcm.getDeptManager().isEmpty())) {
			deptManagerNm = findCdEmp(mCdBcm.getDeptManager(), deptManagerNm, titaVo);
		}
		if (!(mCdBcm.getDistManager().isEmpty())) {
			distManagerNm = findCdEmp(mCdBcm.getDistManager(), distManagerNm, titaVo);
		}

		this.totaVo.putParam("L6R13UnitCode", mCdBcm.getUnitCode());
		this.totaVo.putParam("L6R13UnitItem", mCdBcm.getUnitItem());
		this.totaVo.putParam("L6R13DeptCode", mCdBcm.getDeptCode());
		this.totaVo.putParam("L6R13DeptItem", mCdBcm.getDeptItem());
		this.totaVo.putParam("L6R13DistCode", mCdBcm.getDistCode());
		this.totaVo.putParam("L6R13DistItem", mCdBcm.getDistItem());
		this.totaVo.putParam("L6R13UnitManager", mCdBcm.getUnitManager());
		this.totaVo.putParam("L6R13DeptManager", mCdBcm.getDeptManager());
		this.totaVo.putParam("L6R13DistManager", mCdBcm.getDistManager());
		this.totaVo.putParam("L6R13UnitManagerNm", unitManagerNm);
		this.totaVo.putParam("L6R13DeptManagerNm", deptManagerNm);
		this.totaVo.putParam("L6R13DistManagerNm", distManagerNm);
		this.totaVo.putParam("L6R13Enable", mCdBcm.getEnable());

	}

	// 查詢員工資料檔
	private String findCdEmp(String bcmManagerNm, String empManagerNm, TitaVo titaVo) throws LogicException {

		CdEmp tCdEmp = sCdEmpService.findById(bcmManagerNm, titaVo);
		if (tCdEmp == null) {
			empManagerNm = "";
		} else if (!(tCdEmp.getAgCurInd().equals("Y") || tCdEmp.getAgCurInd().equals("y"))) {
			empManagerNm = "";
		} else {
			empManagerNm = tCdEmp.getFullname();
		}

		this.info("L6R13 CdEmp ManagerNm : " + bcmManagerNm + empManagerNm);
		return empManagerNm;
	}

}