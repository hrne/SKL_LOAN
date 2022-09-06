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

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;

/**
 * Tita<br>
 * DefNo=9,4<br>
 * DefType=9,2<br>
 * Code=X,10<br>
 * END=X,1<br>
 */

@Service("L6064") // 各類代碼檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6064 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdCodeService sCdCodeDefService;
	@Autowired
	public CdEmpService cdEmpService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6064 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iDefType = titaVo.getParam("DefType");
		String iDefCode = titaVo.getParam("DefCode").trim();
		String iDefItem = titaVo.getParam("DefItem").trim();
		String iCode = titaVo.getParam("Code");
		String iCodeItem = titaVo.getParam("CodeItem");
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 217 * 200 = 43400

		Slice<CdCode> slCdCode = null;
		if (iDefCode.length() > 0 || iDefType.equals("") || iDefItem.length() > 0) {
			if (iDefItem.length() > 0) {
				slCdCode = sCdCodeDefService.defItemEq("CodeType", "%" + iDefItem + "%", this.index, this.limit, titaVo);
			} else if (("").equals(iCode)) {
				slCdCode = sCdCodeDefService.defItemEq(iDefCode, "%" + iCodeItem + "%", index, limit, titaVo);
			} else {
				slCdCode = sCdCodeDefService.defCodeEq(iDefCode, "%" + iCode + "%", this.index, this.limit, titaVo);
			}
		} else {
			int iDefType9 = Integer.parseInt(iDefType);
			slCdCode = sCdCodeDefService.DefTypeEq("CodeType", iDefType9, iCode + "%", iCodeItem + "%", this.index, this.limit, titaVo);
		}
		List<CdCode> lCdCode = slCdCode == null ? null : slCdCode.getContent();

		if (lCdCode == null || lCdCode.size() == 0) {
			throw new LogicException(titaVo, "E0001", "各類代碼檔檔"); // 查無資料
		}
		// 如有找到資料
		for (CdCode tCdCode : lCdCode) {

//			if (iDefItem.length() > 0) {
//				Slice<CdCode> m1CdCode = sCdCodeDefService.defCodeEq(tCdCode.getCode(), "%" + iCode + "%", index, limit, titaVo);
//				List<CdCode> m2CdCode = m1CdCode == null ? null : m1CdCode.getContent();
//				if (m2CdCode != null) {
//					this.info("m2CdCode==" + m2CdCode);
//					for (CdCode m3CdCode : m2CdCode) {
//						OccursList occursList = new OccursList();
//						occursList.putParam("OODefCode", m3CdCode.getDefCode());
//						occursList.putParam("OOCode", m3CdCode.getCode());
//						occursList.putParam("OOItem", m3CdCode.getItem());
//						occursList.putParam("OOType", m3CdCode.getDefType());
//						occursList.putParam("OOEnable", m3CdCode.getEnable());
//						this.totaVo.addOccursList(occursList);
//					}
//				}

//			} else {
			OccursList occursList = new OccursList();
			occursList.putParam("OODefCode", tCdCode.getDefCode());
			occursList.putParam("OOCode", tCdCode.getCode());
			occursList.putParam("OOItem", tCdCode.getItem());
			occursList.putParam("OOType", tCdCode.getDefType());
			occursList.putParam("OOEnable", tCdCode.getEnable());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdCode.getLastUpdate()) + " " + parse.timeStampToStringTime(tCdCode.getLastUpdate()));
			occursList.putParam("OOLastEmp", tCdCode.getLastUpdateEmpNo() + " " + empName(titaVo, tCdCode.getLastUpdateEmpNo()));
			this.totaVo.addOccursList(occursList);
		}

		/* 將每筆資料放入Tota的OcList */

//		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdCode != null && slCdCode.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String empName(TitaVo titaVo, String empNo) throws LogicException {
		String rs = empNo;

		CdEmp cdEmp = cdEmpService.findById(empNo, titaVo);
		if (cdEmp != null) {
			rs = cdEmp.getFullname();
		}
		return rs;
	}
}