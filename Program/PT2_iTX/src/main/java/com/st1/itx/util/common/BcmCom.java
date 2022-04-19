package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.PfDeparment;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.PfDeparmentService;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * 修改單位主管資料時，同步至CdBcm及PfDeparment
 * 
 * @author st1
 *
 */
@Component("BcmCom")
@Scope("prototype")
public class BcmCom extends TradeBuffer {

	// 一、L5024>L5405 修改"員工代號"欄位(單位主管)時，同時修改PfDeparment、CdBcm的單位主管代號
	// 修改單位主管代號時，同時修改區部代號、部室代號與正在修改的單位代號相同的CdBcm區部經理、部室經理
	// 使用範例: checkDiff(oldCdBcm, newCdBcm, titaVo);

	// 二、L6085>L6755 只能修改單位經理，區部經理及部室經理改為顯示
	// 需要修改區部經理或部室經理時，要用該區部、部室代號修改該筆單位代號資料
	// 修改單位主管代號時，同時修改區部代號、部室代號與正在修改的單位代號相同的CdBcm區部經理、部室經理
	// 使用範例: checkDiff(oldPfDeparment, newPfDeparment, titaVo);

	@Autowired
	private PfDeparmentService sPfDeparmentService;
	@Autowired
	private CdBcmService sCdBcmService;

	/**
	 * 檢查有無修改單位主管
	 * 
	 * @param oldCdBcm 原CdBcm
	 * @param newCdBcm 新CdBcm
	 * @param titaVo   titaVo
	 * @throws DBException DBException
	 */
	public void checkDiff(CdBcm oldCdBcm, CdBcm newCdBcm, TitaVo titaVo) throws DBException {
		if (oldCdBcm == null) {
			return;
		}
		if (newCdBcm == null) {
			return;
		}
		String newUnitCode = newCdBcm.getUnitCode();
		if (oldCdBcm.getUnitCode().equals(newUnitCode)) {
			this.info("新舊單位代號相同");

			String newUnitManager = newCdBcm.getUnitManager();

			if (!oldCdBcm.getUnitManager().equals(newUnitManager)) {
				this.info("新舊單位主管代號不同");
				updateDiff(newUnitCode, newUnitManager, titaVo);
			}

		}
	}

	/**
	 * 檢查有無修改單位主管
	 * 
	 * @param oldPfDeparment 原PfDeparment
	 * @param newPfDeparment 舊PfDeparment
	 * @param titaVo         titaVo
	 * @throws DBException DBException
	 */
	public void checkDiff(PfDeparment oldPfDeparment, PfDeparment newPfDeparment, TitaVo titaVo) throws DBException {
		if (oldPfDeparment == null) {
			return;
		}
		if (newPfDeparment == null) {
			return;
		}
		String newUnitCode = newPfDeparment.getUnitCode();
		if (oldPfDeparment.getUnitCode().equals(newUnitCode)) {
			this.info("新舊單位代號相同");

			String newUnitManager = newPfDeparment.getEmpNo();

			if (!oldPfDeparment.getEmpNo().equals(newUnitManager)) {
				this.info("新舊單位主管代號不同");
				updateDiff(newUnitCode, newUnitManager, titaVo);
			}

		}
	}

	/**
	 * 修改單位主管代號時，同時修改區部代號、部室代號與正在修改的單位代號相同的CdBcm區部經理、部室經理
	 * 
	 * @param unitCode         單位代號
	 * @param unitManagerEmpNo 單位主管(新)
	 * @param titaVo           titaVo
	 * @throws DBException DBException
	 */
	private void updateDiff(String unitCode, String unitManagerEmpNo, TitaVo titaVo) throws DBException {

		String empNo = titaVo.getTlrNo();

		Slice<PfDeparment> sPfDeparment = sPfDeparmentService.findByUnitCode(unitCode, 0, Integer.MAX_VALUE, titaVo);

		if (sPfDeparment != null) {

			List<PfDeparment> lPfDeparment = new ArrayList<>(sPfDeparment.getContent());

			for (PfDeparment tPfDeparment : lPfDeparment) {
				String pfEmpNo = tPfDeparment.getEmpNo();

				// 若PfDeparment.EmpNo與傳入參數unitManagerEmpNo不同，則更新
				if (pfEmpNo != null && !pfEmpNo.equals(unitManagerEmpNo)) {

					// 修改PfDepartment單位主管
					tPfDeparment.setEmpNo(unitManagerEmpNo);
					tPfDeparment.setLastUpdateEmpNo(empNo);

					sPfDeparmentService.update(tPfDeparment, titaVo);
				}
			}
		}

		Slice<CdBcm> sCdBcm = sCdBcmService.findUnitCode1(unitCode, 0, Integer.MAX_VALUE, titaVo);

		if (sCdBcm != null) {

			List<CdBcm> lCdBcm = new ArrayList<>(sCdBcm.getContent());

			boolean isDiff = false;

			for (CdBcm tCdBcm : lCdBcm) {
				String cdBcmUnitManager = tCdBcm.getUnitManager();

				// 若CdBcm.UnitManager與傳入參數unitManagerEmpNo不同，則更新
				if (cdBcmUnitManager != null && !cdBcmUnitManager.equals(unitManagerEmpNo)) {

					// 修改CdBcm單位經理
					tCdBcm.setUnitManager(unitManagerEmpNo);
					tCdBcm.setLastUpdateEmpNo(empNo);

					sCdBcmService.update(tCdBcm, titaVo);

					isDiff = true;
				}
			}

			if (isDiff) {
				// 修改區部代號、部室代號與正在修改的單位代號相同的CdBcm區部經理、部室經理
				findAndUpdatesCdBcm(unitCode, unitManagerEmpNo, titaVo);
			}
		}
	}

	private void findAndUpdatesCdBcm(String unitCode, String unitManagerEmpNo, TitaVo titaVo) throws DBException {

		String empNo = titaVo.getTlrNo();

		// 檢查區部代號 = unitCode的資料
		Slice<CdBcm> sCdBcm = sCdBcmService.findDistCode1(unitCode, 0, Integer.MAX_VALUE, titaVo);

		if (sCdBcm != null) {

			List<CdBcm> lCdBcm = new ArrayList<>(sCdBcm.getContent());

			for (CdBcm tCdBcm : lCdBcm) {
				String cdBcmDistManager = tCdBcm.getDistManager();

				// 若CdBcm.DistManager與傳入參數unitManagerEmpNo不同，則更新
				if (cdBcmDistManager != null && !cdBcmDistManager.equals(unitManagerEmpNo)) {

					// 修改CdBcm區部經理
					tCdBcm.setDistManager(unitManagerEmpNo);
					tCdBcm.setLastUpdateEmpNo(empNo);

					sCdBcmService.update(tCdBcm, titaVo);
				}
			}
		}

		// 檢查部室代號 = unitCode的資料
		sCdBcm = sCdBcmService.findDeptCode1(unitCode, 0, Integer.MAX_VALUE, titaVo);

		if (sCdBcm != null) {

			List<CdBcm> lCdBcm = new ArrayList<>(sCdBcm.getContent());

			for (CdBcm tCdBcm : lCdBcm) {
				String cdBcmDeptManager = tCdBcm.getDistManager();

				// 若CdBcm.DeptManager與傳入參數unitManagerEmpNo不同，則更新
				if (cdBcmDeptManager != null && !cdBcmDeptManager.equals(unitManagerEmpNo)) {

					// 修改CdBcm部室經理
					tCdBcm.setDeptManager(unitManagerEmpNo);
					tCdBcm.setLastUpdateEmpNo(empNo);

					sCdBcmService.update(tCdBcm, titaVo);
				}
			}
		}
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo arg0) throws LogicException {
		return null;
	}
}
