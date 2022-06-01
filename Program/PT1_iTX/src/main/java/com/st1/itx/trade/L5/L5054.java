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
import com.st1.itx.db.service.springjpa.cm.L5054ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5054")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5054 extends TradeBuffer {

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L5054ServiceImpl l5054ServiceImpl;
	
	@Autowired
	TxDataLogService sTxDataLogService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5054 ");
		this.totaVo.init(titaVo);

		List<Map<String, String>> L5054VoList = null;

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部
		this.limit = 500;// 查全部

		try {
			L5054VoList = l5054ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		this.info("L5054 L5054VoList.size =" + L5054VoList.size());

		if (L5054VoList == null || L5054VoList.size() == 0) {
			throw new LogicException("E0001", "");
		} else {
			int cnt = 0;
			for (Map<String, String> MapL5054 : L5054VoList) {

//				@SuppressWarnings("unchecked")
//				Map<String, String> MapL5054 = (Map<String, String>) ThisObject;
				// L5053Vo L5053VO = (L5053Vo) l5053ServiceImpl.MapToVO(MapL5054, "L5053",
				// titaVo);
				OccursList occursList = new OccursList();

				int oBonusDate = Integer.valueOf(MapL5054.get("F0"));

				if (oBonusDate > 0) {
					oBonusDate -= 19110000;
				}
				occursList.putParam("OBonusDate", oBonusDate);// 獎金發放日期
				occursList.putParam("OBonusNo", MapL5054.get("F16"));// 序號
				occursList.putParam("OBonusType", MapL5054.get("F15"));// 獎金類別
				int oPerfDate = Integer.valueOf(MapL5054.get("F1"));
				if (oPerfDate > 0) {
					oPerfDate -= 19110000;
				}
				occursList.putParam("OPerfDate", oPerfDate);// 業績日期
				occursList.putParam("OCustNo", MapL5054.get("F2"));// 戶號
				occursList.putParam("OFacmNo", MapL5054.get("F3"));// 額度
				occursList.putParam("OBormNo", MapL5054.get("F4"));// 撥款
				occursList.putParam("OEmployeeNo", MapL5054.get("F5"));// 發放員工
				occursList.putParam("OFullname", MapL5054.get("F6"));// 發放員工

				occursList.putParam("OBonus", MapL5054.get("F7"));//
				occursList.putParam("OAdjustBonus", MapL5054.get("F8"));//
				int adjustBonusDate = Integer.valueOf(MapL5054.get("F9"));
				if (adjustBonusDate > 0) {
					adjustBonusDate -= 19110000;
				}
				occursList.putParam("OAdjustBonusDate", adjustBonusDate);//
				int workMonth = Integer.valueOf(MapL5054.get("F10"));
				if (workMonth > 0) {
					workMonth -= 191100;
				}
				occursList.putParam("OWorkMonth", workMonth);//
				int workSeason = Integer.valueOf(MapL5054.get("F11"));
				if (workSeason > 0) {
					workSeason -= 19110;
				}
				occursList.putParam("OWorkSeason", workSeason);//
				
				if (MapL5054.get("MediaDate") == null || "".equals(MapL5054.get("MediaDate"))) {
					occursList.putParam("OMediaFg", 0);//
					occursList.putParam("OMediaDate", 0);
				} else {
					occursList.putParam("OMediaFg", 1);//
					occursList.putParam("OMediaDate", parse.stringToStringDate(MapL5054.get("MediaDate")));
				}
				
				occursList.putParam("OManualFg", MapL5054.get("F14"));//

				this.info("L5054 CreateDate =" + MapL5054.get("CreateDate") + "/" + MapL5054.get("LastUpdate"));
				if (MapL5054.get("CreateDate").equals(MapL5054.get("LastUpdate"))) {
					occursList.putParam("OLogFg", 0);//
				} else {
					occursList.putParam("OLogFg", 1);//
				}

				occursList.putParam("OLastUpdate", parse.stringToStringDateTime(MapL5054.get("LastUpdate")));

				occursList.putParam("OLastEmp",
						MapL5054.get("LastUpdateEmpNo") + " " + MapL5054.get("LastUpdateEmpName"));
				
				// 歷程按鈕顯示與否
				// 邏輯同 L6933
				Slice<TxDataLog> slTxDataLog = sTxDataLogService.findByTranNo("L5504", FormatUtil.pad9(MapL5054.get("F2"), 7) + "-" + FormatUtil.pad9(MapL5054.get("F3"), 3) + "-" + FormatUtil.pad9(MapL5054.get("F4"), 3) + "-7", 0,
						1, titaVo);
				
				List<TxDataLog> lTxDataLog = slTxDataLog != null ? slTxDataLog.getContent() : null;
				
				occursList.putParam("OOHasHistory", lTxDataLog != null && !lTxDataLog.isEmpty() ? "Y" : "N");

				this.totaVo.addOccursList(occursList);
			}
		}

		if (L5054VoList != null && L5054VoList.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}