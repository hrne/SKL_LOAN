package com.st1.itx.trade.L6;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L698A")
@Scope("prototype")
/**
 * 應處理明細查詢
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L698A extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	/* DB服務注入 */
	@Autowired
	public TxToDoDetailService txToDoDetailService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private int selectCode = 0;
	private int cnt = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L698A ");
		this.totaVo.init(titaVo);
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 60;

		selectCode = parse.stringToInteger(titaVo.getParam("SelectCode"));

		String itemCode  = titaVo.getParam("ItemCode");
		String iDtlValue = titaVo.getParam("DtlValue");

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();
		Slice<TxToDoDetail> slTxToDoDetail = null;
//		! 1:昨日留存  0-上一營業日
//		! 2:本日新增  本營業日-本營業日
//		! 3:全部     0-99991231
//		! 4:本日處理   0-99991231
//		! 5:本日刪除   0-99991231
//		! 6:保留     0-99991231
//		! 7:未處理 0-99991231
//		! 9:未處理 (按鈕處理) 0-99991231
//   0.未處理
//   1.已保留
//   2.已處理
//   3.已刪除

		// 上一營業日
		int lbsDy = this.txBuffer.getTxCom().getLbsdy() + 19110000;
		// 本營業日
		int tbsDy = this.txBuffer.getTxCom().getTbsdy() + 19110000;
		if (titaVo.getParam("DtlValue").equals("")) {
			switch (selectCode) {
			case 1:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, lbsDy, this.index, this.limit,
						titaVo);
				break;
			case 2:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, tbsDy, tbsDy, this.index, this.limit,
						titaVo);
				break;
			case 3:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 3, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 4:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 2, 2, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 5:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 3, 3, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 6:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 1, 1, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 7:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			case 9:
				slTxToDoDetail = txToDoDetailService.DataDateRange(itemCode, 0, 0, 0, 99991231, this.index, this.limit,
						titaVo);
				break;
			default:
				break;
			}
			lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

			Boolean showCustNo = false;
			if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {

				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

				for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//				if (selectCodeIsNotQualify(tTxToDoDetail)) {
//					continue;
//				}
					int custno = tTxToDoDetail.getCustNo();
					String custName = "";
					String custId = "";

					if (custno != 0) {
						CustMain tCustMain = custMainService.custNoFirst(custno, custno);
						if (tCustMain != null) {
							custName = tCustMain.getCustName();
							custId = tCustMain.getCustId();
						}
						if (!showCustNo) {
							showCustNo = true;
						}

					}

					OccursList occursList = new OccursList();

					int createDate = tTxToDoDetail.getDataDate();
					if (tTxToDoDetail.getCreateDate() != null) {
						Timestamp ts = tTxToDoDetail.getCreateDate();
						DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
						String sCreateDate = sdfdate.format(ts);
						createDate = parse.stringToInteger(sCreateDate) - 19110000;
					}
					occursList.putParam("OOAcDate", createDate);
					occursList.putParam("OOStatus", tTxToDoDetail.getStatus());
					occursList.putParam("OOCustNo", custno);
					occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
					occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
					occursList.putParam("OOCustName", custName);
					occursList.putParam("OOProcessNote", tTxToDoDetail.getProcessNote());
					occursList.putParam("OOTxSn", tTxToDoDetail.getTitaEntdy() + titaVo.getKinbr()
							+ tTxToDoDetail.getTitaTlrNo() + tTxToDoDetail.getTitaTxtNo()); // 登放序號
					occursList.putParam("OOExcuteTxcd", tTxToDoDetail.getExcuteTxcd());
					occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
					occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
					occursList.putParam("OOCustId", custId);
					cnt++;
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);

				}

			}
			if (cnt == 0) {
				throw new LogicException(titaVo, "E0001", ""); // 查詢資料不存在
			}
			if (showCustNo) {
				this.totaVo.putParam("OShowCustNoFg", "Y");
			}
		} else {
			slTxToDoDetail = txToDoDetailService.DtlValueLike(itemCode, "%"+iDtlValue+"%", this.index, this.limit, titaVo);
			lTxToDoDetail = slTxToDoDetail == null ? null : slTxToDoDetail.getContent();

			Boolean showCustNo = false;
			if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {

				/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
				if (slTxToDoDetail != null && slTxToDoDetail.hasNext()) {
					titaVo.setReturnIndex(this.setIndexNext());
					/* 手動折返 */
					this.totaVo.setMsgEndToEnter();
				}

				for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//				if (selectCodeIsNotQualify(tTxToDoDetail)) {
//					continue;
//				}
					int custno = tTxToDoDetail.getCustNo();
					String custName = "";
					String custId = "";

					if (custno != 0) {
						CustMain tCustMain = custMainService.custNoFirst(custno, custno);
						if (tCustMain != null) {
							custName = tCustMain.getCustName();
							custId = tCustMain.getCustId();
						}
						if (!showCustNo) {
							showCustNo = true;
						}

					}

					OccursList occursList = new OccursList();

					int createDate = tTxToDoDetail.getDataDate();
					if (tTxToDoDetail.getCreateDate() != null) {
						Timestamp ts = tTxToDoDetail.getCreateDate();
						DateFormat sdfdate = new SimpleDateFormat("yyyyMMdd");
						String sCreateDate = sdfdate.format(ts);
						createDate = parse.stringToInteger(sCreateDate) - 19110000;
					}
					occursList.putParam("OOAcDate", createDate);
					occursList.putParam("OOStatus", tTxToDoDetail.getStatus());
					occursList.putParam("OOCustNo", custno);
					occursList.putParam("OOFacmNo", tTxToDoDetail.getFacmNo());
					occursList.putParam("OOBormNo", tTxToDoDetail.getBormNo());
					occursList.putParam("OOCustName", custName);
					occursList.putParam("OOProcessNote", tTxToDoDetail.getProcessNote());
					occursList.putParam("OOTxSn", tTxToDoDetail.getTitaEntdy() + titaVo.getKinbr()
							+ tTxToDoDetail.getTitaTlrNo() + tTxToDoDetail.getTitaTxtNo()); // 登放序號
					occursList.putParam("OOExcuteTxcd", tTxToDoDetail.getExcuteTxcd());
					occursList.putParam("OOItemCode", tTxToDoDetail.getItemCode());
					occursList.putParam("OODtlValue", tTxToDoDetail.getDtlValue());
					occursList.putParam("OOCustId", custId);
					cnt++;
					/* 將每筆資料放入Tota的OcList */
					this.totaVo.addOccursList(occursList);

				}

			}
			if (cnt == 0) {
				throw new LogicException(titaVo, "E0001", ""); // 查詢資料不存在
			}
			if (showCustNo) {
				this.totaVo.putParam("OShowCustNoFg", "Y");
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}