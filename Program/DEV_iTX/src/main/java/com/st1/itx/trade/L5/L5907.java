package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.SpecInnReCheck;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.SpecInnReCheckService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5907")
@Scope("prototype")
/**
 * 指定覆審名單查詢
 *
 * @author Fegie
 * @version 1.0.0
 */

public class L5907 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public SpecInnReCheckService iSpecInnReCheckService;
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5907 ");
		this.totaVo.init(titaVo);

		this.index = titaVo.getReturnIndex();
		this.limit = 100;

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("FacmNo"));

		Slice<SpecInnReCheck> iSpecInnReCheck = null;
		CustMain iCustMain = new CustMain();
		if (iFacmNo == 0) {
			iSpecInnReCheck = iSpecInnReCheckService.findCustNo(iCustNo, this.index, this.limit, titaVo);
		} else {
			iSpecInnReCheck = iSpecInnReCheckService.findCustFacmNo(iCustNo, iFacmNo, this.index, this.limit, titaVo);
		}

		if (iSpecInnReCheck == null) {
			throw new LogicException(titaVo, "E0001", "指定覆審名單"); // 查無資料
		}

		for (SpecInnReCheck rSpecInnReCheck : iSpecInnReCheck) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNo", rSpecInnReCheck.getCustNo());
			occursList.putParam("OOFacmNo", rSpecInnReCheck.getFacmNo());
			iCustMain = iCustMainService.custNoFirst(rSpecInnReCheck.getCustNo(), rSpecInnReCheck.getCustNo(), titaVo);
			if (iCustMain == null) {
				occursList.putParam("OOCustName", "");
			} else {
				occursList.putParam("OOCustName", iCustMain.getCustName());
			}
			occursList.putParam("OORemark", rSpecInnReCheck.getRemark());
			occursList.putParam("OOCycle", rSpecInnReCheck.getCycle());

			if (rSpecInnReCheck.getReChkYearMonth() == 0) {
				occursList.putParam("OOReChkYearMonth", 0);
			} else {
				occursList.putParam("OOReChkYearMonth", rSpecInnReCheck.getReChkYearMonth() - 191100);
			}

			this.totaVo.addOccursList(occursList);
		}
		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (iSpecInnReCheck != null && iSpecInnReCheck.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}