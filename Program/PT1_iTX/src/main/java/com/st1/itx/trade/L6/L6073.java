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
import com.st1.itx.db.domain.CdInsurer;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CdInsurerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * InsurerType=X,1<br>
 * InsurerCode=X,2<br>
 * END=X,1<br>
 */

@Service("L6073") // 保險公司資料檔查詢
@Scope("prototype")
/**
 * 
 * 
 * @author Yoko
 * @version 1.0.0
 */

public class L6073 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdInsurerService sCdInsurerService;
	
	@Autowired
	private CdEmpService cdEmpService;
	
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6073 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iInsurerType = titaVo.getParam("InsurerType");
		String iInsurerCode = titaVo.getParam("InsurerCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 123 * 200 = 24,600

		// 查詢保險公司資料檔
		Slice<CdInsurer> slCdInsurer;
		if (iInsurerType.isEmpty() || iInsurerType.equals("0")) {
			if (!iInsurerCode.isEmpty()) {
				this.info("iInsurerCode Not Empty");
				slCdInsurer = sCdInsurerService.insurerTypeRange("1", "2", iInsurerCode, iInsurerCode, this.index, this.limit, titaVo);
			} else {
				slCdInsurer = sCdInsurerService.findAll(this.index, this.limit, titaVo);
			}

		} else {
			if (iInsurerCode.isEmpty()) {
				slCdInsurer = sCdInsurerService.insurerTypeRange(iInsurerType, iInsurerType, "00", "ZZ", this.index, this.limit, titaVo);
			} else {
				slCdInsurer = sCdInsurerService.insurerTypeRange(iInsurerType, iInsurerType, iInsurerCode, iInsurerCode, this.index, this.limit, titaVo);
			}
		}
		List<CdInsurer> lCdInsurer = slCdInsurer == null ? null : slCdInsurer.getContent();

		if (lCdInsurer == null || lCdInsurer.size() == 0) {
			throw new LogicException(titaVo, "E0001", "保險公司資料檔"); // 查無資料
		}
		// 如有找到資料
		for (CdInsurer tCdInsurer : lCdInsurer) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOInsurerType", tCdInsurer.getInsurerType());
			occursList.putParam("OOInsurerCode", tCdInsurer.getInsurerCode());
			occursList.putParam("OOInsurerItem", tCdInsurer.getInsurerItem());
			occursList.putParam("OOInsurerShort", tCdInsurer.getInsurerShort());
			occursList.putParam("OOTelArea", tCdInsurer.getTelArea());
			occursList.putParam("OOTelNo", tCdInsurer.getTelNo());
			occursList.putParam("OOTelExt", tCdInsurer.getTelExt());
			occursList.putParam("OOInsurerId", tCdInsurer.getInsurerId());
			occursList.putParam("OOLastUpdate", parse.timeStampToStringDate(tCdInsurer.getLastUpdate()) + " " + parse.timeStampToStringTime(tCdInsurer.getLastUpdate())); // 最後修改日期
			occursList.putParam("OOLastEmp", tCdInsurer.getLastUpdateEmpNo() + " " + empName(titaVo, tCdInsurer.getLastUpdateEmpNo())); // 最後修改人員
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdInsurer != null && slCdInsurer.hasNext()) {
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