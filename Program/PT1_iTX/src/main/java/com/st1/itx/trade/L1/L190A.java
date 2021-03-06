package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

/**
 * Tita EmployeeNo=X,6 AgCurInd=X,1 END=X,1
 */
@Service("L190A") // 員工資料檔查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */
public class L190A extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L190A.class);

	/* DB服務注入 */
	@Autowired
	public CdEmpService sCdEmpService;

	@Autowired
	public CdBcmService iCdBcmService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L190A ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 400; // 124 * 400 = 52000

		String iCenterCode = titaVo.getParam("CenterCode");
		String iEmployeeNo = titaVo.getParam("EmployeeNo");
		String iFullname = titaVo.getParam("EmployeeNoX");
		String iAgCurInd = titaVo.getParam("AgCurInd");
		Slice<CdEmp> slCdEmp = null;
		if (iAgCurInd.equals("")) {
			// 查全部
			if (!iCenterCode.equals("")) {
				slCdEmp = sCdEmpService.findCenterCode(iCenterCode, this.index, this.limit, titaVo);
			} else if (!iEmployeeNo.equals("")) {
				slCdEmp = sCdEmpService.findEmployeeNo(iEmployeeNo, iEmployeeNo, this.index, this.limit, titaVo);
			} else if (!iFullname.equals("")) {
				slCdEmp = sCdEmpService.findFullnameLike(iFullname + "%", this.index, this.limit, titaVo);
			} else {
				slCdEmp = sCdEmpService.EmployeeNoLike(iEmployeeNo.trim() + "%", this.index, this.limit, titaVo);
			}
		} else {
			if (!iCenterCode.equals("")) {
				slCdEmp = sCdEmpService.findCenterCodeAndAgCurInd(iCenterCode, iAgCurInd, this.index, this.limit, titaVo);
			} else if (!iEmployeeNo.equals("")) {
				slCdEmp = sCdEmpService.findEmployeeNoAndAgCurInd(iEmployeeNo, iEmployeeNo, iAgCurInd, this.index, this.limit, titaVo);
			} else if (!iFullname.equals("")) {
				slCdEmp = sCdEmpService.findFullnameLikeAndAgCurInd(iFullname + "%", iAgCurInd, this.index, this.limit, titaVo);
			} else {
				slCdEmp = sCdEmpService.EmployeeNoLikeAndAgCurInd(iEmployeeNo.trim() + "%", iAgCurInd, this.index, this.limit, titaVo);
			}
		}
		List<CdEmp> lCdEmp = slCdEmp == null ? null : slCdEmp.getContent();
		if (lCdEmp == null || lCdEmp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "員工資料檔"); // 查無資料
		}

		// 查詢員工資料檔檔
		this.info("slCdEmp====" + lCdEmp);

		// 如有找到資料
		for (CdEmp tCdEmp : slCdEmp) {
			OccursList occursList = new OccursList();
			Slice<CdBcm> xCdBcm = null;
			CdBcm sCdBcm = new CdBcm();
			String xCenterCode = "";
			xCenterCode = tCdEmp.getCenterCode();
			xCdBcm = iCdBcmService.findUnitCode(xCenterCode, xCenterCode, 0, Integer.MAX_VALUE, titaVo);
			if (xCdBcm != null) {
				sCdBcm = xCdBcm.getContent().get(0);
			}
			occursList.putParam("OOEmployeeNo", tCdEmp.getEmployeeNo());
			occursList.putParam("OOAgentId", tCdEmp.getAgentId());
			occursList.putParam("OOFullname", tCdEmp.getFullname());
			occursList.putParam("OOCenterCode", tCdEmp.getCenterCode());
			if (sCdBcm != null) {
				occursList.putParam("OOCenterCodeName", sCdBcm.getUnitItem());
			} else {
				occursList.putParam("OOCenterCodeName", "");
			}
			occursList.putParam("OOCommLineType", tCdEmp.getAgStatusCode());
			occursList.putParam("OOAgCurInd", tCdEmp.getAgCurInd());
			// 固定
			if (tCdEmp.getLastUpdate() == null || tCdEmp.getLastUpdate().toString().equals("")) {
				occursList.putParam("OODataDate", "");
			} else {
				String taU = tCdEmp.getLastUpdate().toString();
				String uaDate = StringUtils.leftPad(String.valueOf(Integer.valueOf(taU.substring(0, 10).replace("-", "")) - 19110000), 7, '0');
				uaDate = uaDate.substring(0, 3) + "/" + uaDate.substring(3, 5) + "/" + uaDate.substring(5);
				occursList.putParam("OODataDate", uaDate);
			}
			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occursList);
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdEmp.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter(); // 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}