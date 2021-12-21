package com.st1.itx.trade.L7;

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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.TxAmlRating;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.TxAmlRatingService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * ClCode1=9,1<br>
 * ClCode2=9,2<br>
 * ClNo=9,7<br>
 * END=X,1<br>
 */

@Service("L7913")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L7913 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7913.class);

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* DB服務注入 */
	@Autowired
	public TxAmlRatingService sTxAmlRatingService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7913 ");
		this.totaVo.init(titaVo);

		// 取tita戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 取tita案件編號
		int iCaseNo = parse.stringToInteger(titaVo.getParam("CaseNo"));

		Slice<FacMain> slFacMain = null;
		List<FacMain> lFacMain = new ArrayList<FacMain>();
		if (iCustNo != 0) {
			slFacMain = sFacMainService.facmCustNoRange(iCustNo, iCustNo, 1, 999, this.index, this.limit, titaVo);

			lFacMain = slFacMain == null ? null : slFacMain.getContent();

			if (lFacMain == null) {
				throw new LogicException("E0001", "L7913額度主檔 戶號 = " + iCustNo);
			}

		} else {
			slFacMain = sFacMainService.facmCreditSysNoRange(iCaseNo, iCaseNo, 1, 999, this.index, this.limit, titaVo);

			lFacMain = slFacMain == null ? null : slFacMain.getContent();

			if (lFacMain == null) {
				throw new LogicException("E0001", "L7913額度主檔 案件編號 = " + iCaseNo);
			}

		}

		String sCustNo = "";
		String sCaseNo = "";

		for (FacMain tFacMain : lFacMain) {
			sCustNo = String.valueOf(tFacMain.getCustNo());
			sCaseNo = String.valueOf(tFacMain.getCreditSysNo());
			break;
		}

		Slice<TxAmlRating> slTxAmlRating = sTxAmlRatingService.findByCaseNo(sCaseNo, this.index, this.limit);

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

		this.addList(this.totaVo);
		return this.sendList();
	}
}