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
import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * Tita UnitCode=X,6 DeptCode=X,6 DistCode=X,6 END=X,1
 */

@Service("L6086") // 單位代號查詢
@Scope("prototype")
/**
 *
 *
 * @author Yoko
 * @version 1.0.0
 */

public class L6086 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L6086.class);

	/* DB服務注入 */
	@Autowired
	public CdBcmService sCdBcmService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6086 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iUnitCode = titaVo.getParam("UnitCode");
		String iDeptCode = titaVo.getParam("DeptCode");
		String iDistCode = titaVo.getParam("DistCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 200; // 138 * 200 = 27,600

		// 查詢分公司資料檔
		Slice<CdBcm> slCdBcm;
		if (!(iDistCode.isEmpty() || iDistCode.equals("000000"))) {
			slCdBcm = sCdBcmService.findDistCode(iDistCode, iDistCode, this.index, this.limit, titaVo);
		} else if (!(iDeptCode.isEmpty() || iDeptCode.equals("000000"))) {
			slCdBcm = sCdBcmService.findDeptCode(iDeptCode, iDeptCode, this.index, this.limit, titaVo);
		} else if (!(iUnitCode.isEmpty() || iUnitCode.equals("000000"))) {
			slCdBcm = sCdBcmService.findUnitCode(iUnitCode, iUnitCode, this.index, this.limit, titaVo);
		} else {
			slCdBcm = sCdBcmService.findAll(this.index, this.limit, titaVo);
		}
		List<CdBcm> lCdBcm = slCdBcm == null ? null : slCdBcm.getContent();

		if (lCdBcm == null || lCdBcm.size() == 0) {
			throw new LogicException(titaVo, "E0001", "分公司資料檔"); // 查無資料
		}
		// 如有找到資料
		for (CdBcm tCdBcm : lCdBcm) {
			if (tCdBcm.getDeptCode() != null && !(tCdBcm.getDeptCode().isEmpty())) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOUnitCode", tCdBcm.getUnitCode());
				occursList.putParam("OOUnitItem", tCdBcm.getUnitItem());
				occursList.putParam("OODeptCode", tCdBcm.getDeptCode());
				occursList.putParam("OODeptItem", tCdBcm.getDeptItem());
				occursList.putParam("OODistCode", tCdBcm.getDistCode());
				occursList.putParam("OODistItem", tCdBcm.getDistItem());
				/* 將每筆資料放入Tota的OcList */
				this.totaVo.addOccursList(occursList);
			}
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slCdBcm != null && slCdBcm.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}