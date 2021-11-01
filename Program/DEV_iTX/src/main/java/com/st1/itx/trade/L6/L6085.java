package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita InqFg=X,1 UnitCode=X,6 DeptCode=X,6 DistCode=X,6 UnitManager=X,6
 * DeptManager=X,6 DistManager=X,6 END=X,1
 */

@Service("L6085") // 分公司資料檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6085 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6085 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iInqFg = this.parse.stringToInteger(titaVo.getParam("InqFg"));
		String iUnitCode = titaVo.getParam("UnitCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iDistCode = titaVo.getParam("DistCode");
		String iUnitManager = titaVo.getParam("UnitManager");
		String iDeptManager = titaVo.getParam("DeptManager");
		String iDistManager = titaVo.getParam("DistManager");
		String unitManagerNm = "";
		String deptManagerNm = "";
		String distManagerNm = "";

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 216 * 200 = 43,200

		// 查詢分公司資料檔
		Slice<CdBcm> slCdBcm;
		if (iInqFg == 1) {
			slCdBcm = sCdBcmService.findUnitCode1(iUnitCode+"%", this.index, this.limit, titaVo);
		} else if (iInqFg == 2) {
			slCdBcm = sCdBcmService.findDistCode1(iDistCode+"%", this.index, this.limit, titaVo);
		} else if (iInqFg == 3) {
			slCdBcm = sCdBcmService.findDeptCode1(iDeptCode+"%", this.index, this.limit, titaVo);
		} else if (iInqFg == 4) {
			slCdBcm = sCdBcmService.findUnitManager(iUnitManager+"%", this.index, this.limit, titaVo);
		} else if (iInqFg == 5) {
			slCdBcm = sCdBcmService.findDistManager(iDistManager+"%", this.index, this.limit, titaVo);
		} else if (iInqFg == 6) {
			slCdBcm = sCdBcmService.findDeptManager(iDeptManager+"%", this.index, this.limit, titaVo);
		} else {
			slCdBcm = sCdBcmService.findAll(this.index, this.limit, titaVo);
		}

		List<CdBcm> lCdBcm = slCdBcm == null ? null : slCdBcm.getContent();

		if (lCdBcm == null || lCdBcm.size() == 0) {
			throw new LogicException(titaVo, "E0001", "分公司資料檔"); // 查無資料
		}
		// 如有找到資料
		for (CdBcm tCdBcm : lCdBcm) {

			// 主管辦公室等特殊單位不顯示
			if (tCdBcm.getDeptCode().isEmpty()) {
				continue;
			}

			OccursList occursList = new OccursList();
			occursList.putParam("OOUnitCode", tCdBcm.getUnitCode());
			occursList.putParam("OOUnitItem", tCdBcm.getUnitItem());
			occursList.putParam("OODeptCode", tCdBcm.getDeptCode());
			occursList.putParam("OODeptItem", tCdBcm.getDeptItem());
			occursList.putParam("OODistCode", tCdBcm.getDistCode());
			occursList.putParam("OODistItem", tCdBcm.getDistItem());
			occursList.putParam("OOUnitManager", tCdBcm.getUnitManager());
			occursList.putParam("OODeptManager", tCdBcm.getDeptManager());
			occursList.putParam("OODistManager", tCdBcm.getDistManager());
			occursList.putParam("OOEnable", tCdBcm.getEnable());
			unitManagerNm = "";
			deptManagerNm = "";
			distManagerNm = "";

			// 查詢員工資料檔
			if (!(tCdBcm.getUnitManager().isEmpty())) {
				unitManagerNm = findCdEmp(tCdBcm.getUnitManager(), unitManagerNm, titaVo);
			}
			if (!(tCdBcm.getDeptManager().isEmpty())) {
				deptManagerNm = findCdEmp(tCdBcm.getDeptManager(), deptManagerNm, titaVo);
			}
			if (!(tCdBcm.getDistManager().isEmpty())) {
				distManagerNm = findCdEmp(tCdBcm.getDistManager(), distManagerNm, titaVo);
			}

			occursList.putParam("OOUnitManagerNm", unitManagerNm);
			occursList.putParam("OODeptManagerNm", deptManagerNm);
			occursList.putParam("OODistManagerNm", distManagerNm);

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		if (this.totaVo.getOccursList().size() == 0) {
			throw new LogicException(titaVo, "E0001", "分公司資料檔"); // 查無資料
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBcm != null && slCdBcm.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
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

		this.info("L6085 CdEmp ManagerNm : " + bcmManagerNm + empManagerNm);
		return empManagerNm;
	}

}