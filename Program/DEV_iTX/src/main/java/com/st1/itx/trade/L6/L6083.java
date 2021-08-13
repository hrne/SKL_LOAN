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
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CdBcm;
import com.st1.itx.db.domain.CdAoDept;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.CdAoDeptService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita EmployeeNo=X,6 END=X,1
 */

@Service("L6083") // 放款專員所屬業務部室對照檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6083 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6083.class);

	/* DB服務注入 */
	@Autowired
	public CdAoDeptService sCdAoDeptService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6083 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iEmployeeNo = titaVo.getParam("EmployeeNo");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 92 * 200 = 18,400

		// 查詢放款專員所屬業務部室對照檔
		Slice<CdAoDept> slCdAoDept;
		if (iEmployeeNo.isEmpty() || iEmployeeNo.equals("000000")) {
			slCdAoDept = sCdAoDeptService.findAll(this.index, this.limit, titaVo);
		} else {
			slCdAoDept = sCdAoDeptService.findEmployeeNo(iEmployeeNo, iEmployeeNo, this.index, this.limit, titaVo);
		}
		List<CdAoDept> lCdAoDept = slCdAoDept == null ? null : slCdAoDept.getContent();

		if (lCdAoDept == null || lCdAoDept.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款專員所屬業務部室對照檔"); // 查無資料
		}
		// 如有找到資料
		for (CdAoDept tCdAoDept : lCdAoDept) {

			OccursList occursList = new OccursList();
			occursList.putParam("OOEmployeeNo", tCdAoDept.getEmployeeNo());
			occursList.putParam("OODeptCode", tCdAoDept.getDeptCode());

			// 查詢員工資料檔
			CdEmp tCdEmp = sCdEmpService.findById(tCdAoDept.getEmployeeNo(), titaVo);
			if (tCdEmp == null) {
				occursList.putParam("OOFullname", "");
			} else if (!(tCdEmp.getAgCurInd().equals("Y") || tCdEmp.getAgCurInd().equals("y"))) {
				occursList.putParam("OOFullname", "");
			} else {
				occursList.putParam("OOFullname", tCdEmp.getFullname());
			}

			// 查詢分公司資料檔
			CdBcm tCdBcm = sCdBcmService.deptCodeFirst(tCdAoDept.getDeptCode(), titaVo);
			if (tCdBcm == null) {
				occursList.putParam("OODeptItem", "");
			} else {
				occursList.putParam("OODeptItem", tCdBcm.getDeptItem());
			}

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdAoDept != null && slCdAoDept.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}
