package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.springjpa.cm.L5053ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
//import com.st1.itx.db.domain.vo.L5053Vo;
//import com.st1.itx.db.domain.PfRewardMedia;
//import com.st1.itx.db.domain.PfRewardMediaId;
//import com.st1.itx.db.service.PfRewardMediaService;

/**
 * Tita<br>
 * FunctionCd=9,1<br>
 * PerfDateFm=9,7<br>
 * PerfDateTo=9,7<br>
 * CustNo=9,7<br>
 * FacmNo=9,3<br>
 */

@Service("L5053")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5053 extends TradeBuffer {

//	@Autowired
//	public PfRewardMediaService pfRewardMediaService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	
	@Autowired
	TxDataLogService sTxDataLogService;

	@Autowired
	public L5053ServiceImpl l5053ServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5053 ");
		this.totaVo.init(titaVo);

		List<Map<String, String>> L5053VoList = null;

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部
		this.limit = 500;// 查全部

		try {
			L5053VoList = l5053ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		this.info("L5051 L5053VoList.size =" + L5053VoList.size());

		if (L5053VoList == null || L5053VoList.size() == 0) {
			throw new LogicException("E0001", "");
		} else {
			int cnt = 0;
			for (Map<String, String> MapL5053 : L5053VoList) {
				this.info("L5053 L5053VoList count =" + ++cnt);
//				@SuppressWarnings("unchecked")
//				Map<String, String> MapL5053 = (Map<String, String>) ThisObject;
				// L5053Vo L5053VO = (L5053Vo) l5053ServiceImpl.MapToVO(MapL5053, "L5053",
				// titaVo);
				OccursList occursList = new OccursList();
				/**傳入時會帶文字0而非空白 20226/3/15 Mata*/
				if (MapL5053.get("BonusDate") == null || "0".equals(MapL5053.get("BonusDate").toString().trim())) {
					occursList.putParam("OBonusDate", 0);// 獎金發放日期
				} else {
					int oBonusDate = Integer.valueOf(MapL5053.get("BonusDate").toString()) - 19110000;
					occursList.putParam("OBonusDate", oBonusDate);// 獎金發放日期
				}
								
				occursList.putParam("OBonusNo", MapL5053.get("F16"));// 序號
				occursList.putParam("OBonusType", MapL5053.get("F15"));// 獎金類別
				int oPerfDate = Integer.valueOf(MapL5053.get("F1"));
				if (oPerfDate > 0) {
					oPerfDate -= 19110000;
				}
				occursList.putParam("OPerfDate", oPerfDate);// 業績日期
				occursList.putParam("OCustNo", MapL5053.get("F2"));// 戶號
				occursList.putParam("OFacmNo", MapL5053.get("F3"));// 額度
				occursList.putParam("OBormNo", MapL5053.get("F4"));// 撥款
				occursList.putParam("OEmployeeNo", MapL5053.get("F5"));// 發放員工
				occursList.putParam("OFullname", MapL5053.get("F6"));// 發放員工

				occursList.putParam("OBonus", MapL5053.get("F7"));//
				occursList.putParam("OAdjustBonus", MapL5053.get("F8"));//
				int adjustBonusDate = Integer.valueOf(MapL5053.get("F9"));
				if (adjustBonusDate > 0) {
					adjustBonusDate -= 19110000;
				}
				occursList.putParam("OAdjustBonusDate", adjustBonusDate);//
				int workMonth = Integer.valueOf(MapL5053.get("F10"));
				if (workMonth > 0) {
					workMonth -= 191100;
				}
				occursList.putParam("OWorkMonth", workMonth);//
				int workSeason = Integer.valueOf(MapL5053.get("F11"));
				if (workSeason > 0) {
					workSeason -= 19110;
				}
				occursList.putParam("OWorkSeason", workSeason);//
				if (MapL5053.get("MediaDate") == null || "".equals(MapL5053.get("MediaDate"))) {
					occursList.putParam("OMediaFg", 0);//
					occursList.putParam("OMediaDate", 0);
				} else {
					occursList.putParam("OMediaFg", 1);//
					occursList.putParam("OMediaDate", parse.stringToStringDate(MapL5053.get("MediaDate")));
				}
				
//				int mediaDate = Integer.valueOf(MapL5053.get("F13"));
//				if (mediaDate > 0) {
//					mediaDate -= 19110000;
//				}
				occursList.putParam("OManualFg", MapL5053.get("F14"));//

				if (MapL5053.get("CreateDate").equals(MapL5053.get("LastUpdate"))) {
					occursList.putParam("OLogFg", 0);//
				} else {
					occursList.putParam("OLogFg", 1);//
				}
				
				this.info("LastUpdate = " + MapL5053.get("LastUpdate") + "=" + parse.stringToStringDate(MapL5053.get("LastUpdate")));
				occursList.putParam("OLastUpdate", parse.stringToStringDateTime(MapL5053.get("LastUpdate")));
				
				occursList.putParam("OLastEmp", MapL5053.get("LastUpdateEmpNo") + " " + MapL5053.get("LastUpdateEmpName"));
				
				// 歷程按鈕顯示與否
				// 邏輯同 L6933
				Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByTranNo("L5503", FormatUtil.pad9(MapL5053.get("F2"), 7) + "-" + FormatUtil.pad9(MapL5053.get("F3"), 3) + "-" + FormatUtil.pad9(MapL5053.get("F4"), 3), 0,
						1, titaVo);
				
				List<TxDataLog> lTxDataLog = slTxDataLog != null ? slTxDataLog.getContent() : null;
				
				occursList.putParam("OOHasHistory", lTxDataLog != null && !lTxDataLog.isEmpty() ? "Y" : "N");
				
				this.totaVo.addOccursList(occursList);
			}
		}

		if (L5053VoList != null && L5053VoList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}