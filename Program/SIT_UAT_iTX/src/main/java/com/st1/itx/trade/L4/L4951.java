package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * MediaDate=9,7<br>
 */

@Service("L4951")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4951 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4951.class);
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public CustMainService custMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4951 ");
		this.totaVo.init(titaVo);

//		1.查詢 媒體日期

		int mediaDate = parse.stringToInteger(titaVo.getParam("MediaDate")) + 19110000;
		int mediaType = parse.stringToInteger(titaVo.getParam("MediaType"));

		List<EmpDeductMedia> lEmpDeductMedia = new ArrayList<EmpDeductMedia>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<EmpDeductMedia> sEmpDeductMedia = null;

		if (mediaType == 1) {
			sEmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate, "4", this.index, this.limit);

		} else if (mediaType == 2) {
			sEmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate, "5", this.index, this.limit);
		}

		lEmpDeductMedia = sEmpDeductMedia == null ? null : sEmpDeductMedia.getContent();

		if (lEmpDeductMedia != null && lEmpDeductMedia.size() != 0) {
			for (EmpDeductMedia tEmpDeductMedia : lEmpDeductMedia) {
				OccursList occursList = new OccursList();

				CustMain tCustMain = new CustMain();

				tCustMain = custMainService.custNoFirst(tEmpDeductMedia.getCustNo(), tEmpDeductMedia.getCustNo());

				occursList.putParam("OOCustNo", tEmpDeductMedia.getCustNo());
				occursList.putParam("OORepayCode", tEmpDeductMedia.getRepayCode());
				occursList.putParam("OOPerfMonth", tEmpDeductMedia.getPerfMonth() - 191100);
				occursList.putParam("OOPerfRepayCode", tEmpDeductMedia.getPerfRepayCode());
				occursList.putParam("OOTellerNo", tCustMain.getEmpNo());
				occursList.putParam("OOTellerId", tEmpDeductMedia.getCustId());
				occursList.putParam("OOTellerName", tCustMain.getCustName());
				occursList.putParam("OORepayAmt", tEmpDeductMedia.getRepayAmt());
				occursList.putParam("OOMediaDate", tEmpDeductMedia.getEmpDeductMediaId().getMediaDate());
				occursList.putParam("OOMediaKind", tEmpDeductMedia.getEmpDeductMediaId().getMediaKind());
				occursList.putParam("OOMediaSeq", tEmpDeductMedia.getEmpDeductMediaId().getMediaSeq());

				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException("E0001", "L4951 查無資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}