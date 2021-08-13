package com.st1.itx.trade.L1;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CustCross;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CustCrossService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L1R06")
@Scope("prototype")
public class L1R06 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L1R06.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService iCustMainService;

	@Autowired
	public CustCrossService iCustCrossService;

	@Autowired
	public CdCodeService iCdCodeService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("L1R06 Started");
		this.totaVo.init(titaVo);

		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		// tita取值
		// 統編
		String NewCust = titaVo.get("RimNewCust");
		String iCustId = titaVo.get("RimCustId").trim();
		int iCustNo = Integer.valueOf(titaVo.get("RimCustNo"));
		CustMain iCustMain = new CustMain();
		Slice<CdCode> iCdCode = null;
		CustCross iCustCross = new CustCross();
		int i = 1;

		//for L1101/L1102 use
		if (NewCust != null && "Y".equals(NewCust.trim())) {
			
		} else {
			if (!iCustId.equals("")) {
				iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
			} else if (iCustNo != 0) {
				iCustMain = iCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			} else {
				throw new LogicException(titaVo, "E0001", "統一編號與戶號須擇一輸入"); // 查無資料
			}

			if (iCustMain == null) {
				throw new LogicException(titaVo, "E0001", "查無此客戶"); // 查無資料
			}
		}
		// 抓子公司名稱
		iCdCode = iCdCodeService.getCodeList(1, "SubCompanyCode", this.index, this.limit, titaVo);
		if (iCdCode == null) {
			throw new LogicException(titaVo, "E0001", "共用代碼檔查無子公司選項"); // 查無資料
		}

		// 回傳戶名
		totaVo.putParam("L1r06CustName", iCustMain.getCustName());
		// 回傳子公司代號與名稱，共20筆
		for (CdCode xCdCode : iCdCode) {
			this.info("TTTT--=" + xCdCode.getCode());
			iCustCross = new CustCross(); // init
			iCustCross = iCustCrossService.subCompanyCodeFirst(iCustMain.getCustUKey(), xCdCode.getCode(), titaVo);
			// 被設定為停用時跳過
			if (xCdCode.getEnable().equals("Y")) {
				if (iCustCross == null) {
					totaVo.putParam("L1r06SubCompanyCode" + i, xCdCode.getCode());
					totaVo.putParam("L1r06SubCompanyItem" + i, xCdCode.getItem());
					// 查無資料時預設不同意
					totaVo.putParam("L1r06CrossUse" + i, "N");
				} else {
					totaVo.putParam("L1r06SubCompanyCode" + i, xCdCode.getCode());
					totaVo.putParam("L1r06SubCompanyItem" + i, xCdCode.getItem());
					totaVo.putParam("L1r06CrossUse" + i, iCustCross.getCrossUse());
				}
				i++;
			}
		}
		// 補滿剩下筆數
		while (true) {
			totaVo.putParam("L1r06SubCompanyCode" + i, "");
			totaVo.putParam("L1r06SubCompanyItem" + i, "");
			totaVo.putParam("L1r06CrossUse" + i, "");
			if (i == 20) {
				break;
			}
			i++;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}