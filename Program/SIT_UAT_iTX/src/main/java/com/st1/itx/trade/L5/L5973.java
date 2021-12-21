package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.NegAppr01;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegAppr01Service;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* DB容器 */
import com.st1.itx.db.domain.CustMain;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

/**
 * Tita<br>
 * YYY=9,3<br>
 * MM=9,2<br>
 * DD=9,2<br>
 * CustId=X,10<br>
 */

@Service("L5973")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5973 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Autowired
	public NegCom sNegCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5973");
		this.info("active L5973 ");
		this.totaVo.init(titaVo);

		String YYY = titaVo.getParam("YYY").trim(); // 年
		String MM = titaVo.getParam("MM").trim(); // 月
		String DD = titaVo.getParam("DD").trim(); // 日(可寫可不寫)
		int iDD = parse.stringToInteger(DD);
		String CustId = titaVo.getParam("CustId").trim(); // 身分證號

		int iCustNo = 0;
		if (CustId != null && CustId.length() != 0) {
			CustMain CustMainVO = sCustMainService.custIdFirst(CustId);
			if (CustMainVO != null) {
				iCustNo = CustMainVO.getCustNo();
			}
		}
		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;// 查全部 Integer.MAX_VALUE

		Slice<NegAppr01> slNegAppr01 = null;

		String ExportDate = YYY + MM + DD;// 製檔日期
		int intExportDate = 0;

		int DateFrom = 0;
		int DateTo = 0;

		if (ExportDate != null && ExportDate.length() != 0 && parse.stringToInteger(ExportDate) > 0) {
			intExportDate = parse.stringToInteger(ExportDate) + 19110000;
			if (DD != null && DD.length() != 0 && iDD > 0) {
				if (iCustNo != 0) {
					slNegAppr01 = sNegAppr01Service.custNoExportDateEq(iCustNo, intExportDate, this.index, this.limit, titaVo);
				} else {
					slNegAppr01 = sNegAppr01Service.exportEq(intExportDate, this.index, this.limit, titaVo);
				}
			} else {
				DateFrom = intExportDate + 01;
				DateTo = intExportDate + 31;
				if (iCustNo != 0) {
					slNegAppr01 = sNegAppr01Service.custExporBetween(iCustNo, DateFrom, DateTo, this.index, this.limit, titaVo);
				} else {
					slNegAppr01 = sNegAppr01Service.exportDateBetween(DateFrom, DateTo, this.index, this.limit, titaVo);
				}
			}
		} else {
			if (iCustNo != 0) {
				slNegAppr01 = sNegAppr01Service.custNoEq(iCustNo, this.index, this.limit, titaVo);
			} else {
				slNegAppr01 = sNegAppr01Service.findAll(this.index, this.limit, titaVo);
			}
		}
		List<NegAppr01> lNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();

		if (lNegAppr01 != null && lNegAppr01.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		if (lNegAppr01 != null) {
			CustMain CustMainVO = new CustMain();
			for (NegAppr01 NegAppr01VO : lNegAppr01) {
				OccursList occursList = new OccursList();

				int ThisCustNo = NegAppr01VO.getCustNo();

				CustMainVO = sCustMainService.custNoFirst(ThisCustNo, ThisCustNo, titaVo);

				occursList.putParam("OORank", "0");//
				occursList.putParam("OOExportDate", NegAppr01VO.getExportDate());
				occursList.putParam("OOCaseKindCode", NegAppr01VO.getCaseKindCode());
				occursList.putParam("OOCustId", CustMainVO.getCustId());
				occursList.putParam("OOCustNo", NegAppr01VO.getCustNo());
				occursList.putParam("OOCustName", CustMainVO.getCustName());
				occursList.putParam("OOFinCodeName", NegAppr01VO.getFinCode());
				occursList.putParam("OOFinCodeNameNM", sNegCom.FindNegFinAcc(NegAppr01VO.getFinCode(), titaVo)[0]);// 債權機構名稱
				occursList.putParam("OOApprAmt", NegAppr01VO.getApprAmt());
				occursList.putParam("OOAccuApprAmt", NegAppr01VO.getAccuApprAmt());
				occursList.putParam("OOAmtRatio", NegAppr01VO.getAmtRatio());
				this.totaVo.addOccursList(occursList);
			}

			/*
			 * #OORank每筆的第一筆給1 其餘給0 #OOExportDate #OOCaseKindCode #OOCustId #OOCustNo
			 * #OOCustName #OOFinCodeName #OOApprAmt #OOAccuApprAmt #OOAmtRatio
			 */
		} else {
			if (this.index != 0) {
				// 代表有多筆查詢,然後筆數剛好可以被整除

			} else {
				// 查詢資料不存在
				throw new LogicException(titaVo, "E0001", "最大債權撥付資料檔");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}