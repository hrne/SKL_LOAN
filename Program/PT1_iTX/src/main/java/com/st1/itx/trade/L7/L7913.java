package com.st1.itx.trade.L7;

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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.TxAmlRating;
import com.st1.itx.db.domain.TxAmlRatingAppl;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.TxAmlRatingApplService;
import com.st1.itx.db.service.TxAmlRatingService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7913")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L7913 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public TxAmlRatingService txAmlRatingService;
	@Autowired
	public TxAmlRatingApplService txAmlRatingApplService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	private String sCustNo = "";
	private String sCaseNo = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7913 ");
		this.totaVo.init(titaVo);

		// 取tita
		// 項目1[預設]:ELOAN評級 2:案件申請留存[修改刪除查詢]
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode"));
		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 案件編號
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));

		Slice<FacMain> slFacMain = null;
		if (iCustNo != 0) {
			slFacMain = sFacMainService.facmCustNoRange(iCustNo, iCustNo, 1, 999, 1, 1, titaVo);
			if (slFacMain == null) {
				throw new LogicException("E0001", "L7913額度主檔 戶號 = " + iCustNo);
			}
		} else {
			slFacMain = sFacMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 1, 999, 1, 1, titaVo);
			if (slFacMain == null) {
				throw new LogicException("E0001", "L7913額度主檔 案件編號 = " + iCaseNo);
			}
		}

		sCustNo = "";
		sCaseNo = "";

		for (FacMain tFacMain : slFacMain.getContent()) {
			sCustNo = String.valueOf(tFacMain.getCustNo());
			sCaseNo = String.valueOf(tFacMain.getCreditSysNo());
			break;
		}
		if (iItemCode == 1) {
			MoveTota(titaVo);
		} else {
			MoveApplTota(titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveTota(TitaVo titaVo) throws LogicException {
		Slice<TxAmlRating> slTxAmlRating = txAmlRatingService.findByCaseNo(sCaseNo, this.index, this.limit, titaVo);

		List<TxAmlRating> lTxAmlRating = new ArrayList<TxAmlRating>();

		lTxAmlRating = slTxAmlRating == null ? null : slTxAmlRating.getContent();

		if (lTxAmlRating == null) {
			throw new LogicException("E0001", "L7913案件編號不存在");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAmlRating != null && slTxAmlRating.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (TxAmlRating tmpTxAmlRating : lTxAmlRating) {
			// new occurs
			OccursList occurslist = new OccursList();

			// 借戶戶號
			occurslist.putParam("OOCustNo", sCustNo);
			// 案件編號
			occurslist.putParam("OORspCaseNo", tmpTxAmlRating.getRspCaseNo());

			String YY = String.valueOf(parse.stringToInteger(tmpTxAmlRating.getModifyDate().substring(0, 4)));
			String MM = tmpTxAmlRating.getModifyDate().substring(4, 6);
			String DD = tmpTxAmlRating.getModifyDate().substring(6, 8);
			String HH = tmpTxAmlRating.getModifyDate().substring(8, 10);
			String mm = tmpTxAmlRating.getModifyDate().substring(10, 12);

			// 異動時間
			occurslist.putParam("OOModifyDate", YY + "/" + MM + "/" + DD + " " + HH + ":" + mm);
			// 分數
			occurslist.putParam("OORspTotalRatingsScore", tmpTxAmlRating.getRspTotalRatingsScore());
			// 總評級

			String TotalRatings = " ";
			switch (tmpTxAmlRating.getRspTotalRatings()) {
			case "L":
				TotalRatings = "L.低";
				break;
			case "M":
				TotalRatings = "M.中";
				break;
			case "H":
				TotalRatings = "H.高";
				break;
			default:
				break;
			}
			occurslist.putParam("OORspTotalRatings", TotalRatings);
			// 序號
			occurslist.putParam("OOLogNo", tmpTxAmlRating.getLogNo());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
	}

	private void MoveApplTota(TitaVo titaVo) throws LogicException {
		Slice<TxAmlRatingAppl> slTxAmlRatingAppl = txAmlRatingApplService.findByCaseNo(sCaseNo, this.index, this.limit,
				titaVo);

		if (slTxAmlRatingAppl == null) {
			throw new LogicException("E0001", "L7913案件編號不存在");
		}

		/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
		if (slTxAmlRatingAppl != null && slTxAmlRatingAppl.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		for (TxAmlRatingAppl t : slTxAmlRatingAppl.getContent()) {
			// new occurs
			OccursList occurslist = new OccursList();

			// 借戶戶號
			occurslist.putParam("OOCustNo", sCustNo);
			// 案件編號
			occurslist.putParam("OORspCaseNo", t.getRspCaseNo());

			String YY = String.valueOf(parse.stringToInteger(t.getModifyDate().substring(0, 4)));
			String MM = t.getModifyDate().substring(4, 6);
			String DD = t.getModifyDate().substring(6, 8);
			String HH = t.getModifyDate().substring(8, 10);
			String mm = t.getModifyDate().substring(10, 12);

			// 異動時間
			occurslist.putParam("OOModifyDate", YY + "/" + MM + "/" + DD + " " + HH + ":" + mm);
			// 分數
			occurslist.putParam("OORspTotalRatingsScore", t.getRspTotalRatingsScore());
			// 總評級

			String TotalRatings = " ";
			switch (t.getRspTotalRatings()) {
			case "L":
				TotalRatings = "L.低";
				break;
			case "M":
				TotalRatings = "M.中";
				break;
			case "H":
				TotalRatings = "H.高";
				break;
			default:
				break;
			}
			occurslist.putParam("OORspTotalRatings", TotalRatings);
			// 序號
			occurslist.putParam("OOLogNo", t.getLogNo());

			/* 將每筆資料放入Tota的OcList */
			this.totaVo.addOccursList(occurslist);
		}
	}
}