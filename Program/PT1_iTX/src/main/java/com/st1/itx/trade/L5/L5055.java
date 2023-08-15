package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxControl;
import com.st1.itx.db.service.TxControlService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.springjpa.cm.L5055ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5055")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5055 extends TradeBuffer {

//	@Autowired
//	public PfRewardMediaService pfRewardMediaService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L5055ServiceImpl l5055ServiceImpl;
	@Autowired
	public TxControlService txControlService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5055");
		this.totaVo.init(titaVo);

		int WorkMonth = Integer.valueOf(titaVo.getParam("WorkYM").trim());

		String controlCode = "L5510." + WorkMonth + ".1";
		TxControl txControl = txControlService.findById(controlCode, titaVo);
		if (txControl == null) {
			throw new LogicException(titaVo, "E0010", "未執行 L5510 保費檢核");
		}

		List<Map<String, String>> L5055VoList = null;

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		// this.limit=Integer.MAX_VALUE;//查全部
		this.limit = 500;// 查全部

		try {
			L5055VoList = l5055ServiceImpl.FindData(titaVo, this.index, this.limit);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			// E5004 讀取DB時發生問題
			throw new LogicException(titaVo, "E5004", "");
		}

		this.info("L5051 L5053VoList.size =" + L5055VoList.size());

		if (L5055VoList == null || L5055VoList.size() == 0) {
			throw new LogicException("E0001", "");
		} else {
			for (Map<String, String> MapL5055 : L5055VoList) {
				OccursList occursList = new OccursList();
				int drawdownDate = parse.stringToInteger(MapL5055.get("DrawdownDate"));
				if (drawdownDate > 19110000) {
					drawdownDate = drawdownDate - 19110000;
				}
				int mediaDate = parse.stringToInteger(MapL5055.get("MediaDate"));
				if (mediaDate > 19110000) {
					mediaDate = mediaDate - 19110000;
				}
				occursList.putParam("OOCustNo", MapL5055.get("CustNo"));
				occursList.putParam("OOFacmNo", MapL5055.get("FacmNo"));
				occursList.putParam("OOBormNo", MapL5055.get("BormNo"));
				occursList.putParam("OOCustName", MapL5055.get("CustName"));
				occursList.putParam("OODrawdownDate", drawdownDate);
				occursList.putParam("OOProdCode", MapL5055.get("ProdCode"));
				occursList.putParam("OOPieceCode", MapL5055.get("PieceCode"));
				occursList.putParam("OOCntingCode", MapL5055.get("CntingCode"));
				occursList.putParam("OODrawdownAmt", MapL5055.get("DrawdownAmt"));
				occursList.putParam("OODeptCode", MapL5055.get("DeptCode"));
				occursList.putParam("OODistCode", MapL5055.get("DistCode"));
				occursList.putParam("OOUnitCode", MapL5055.get("UnitCode"));
				occursList.putParam("OOItDeptName", MapL5055.get("ItDeptName"));
				occursList.putParam("OOItDistName", MapL5055.get("ItDistName"));
				occursList.putParam("OOItUnitName", MapL5055.get("ItUnitName"));
				occursList.putParam("OOIntroducer", MapL5055.get("Introducer"));
				occursList.putParam("OOIntroducerName", MapL5055.get("IntroducerName"));
				occursList.putParam("OOPerfEqAmt", MapL5055.get("PerfEqAmt"));
				occursList.putParam("OOPerfReward", MapL5055.get("PerfReward"));
				occursList.putParam("OOMediaDate", mediaDate);
				occursList.putParam("OOAdjFg", "0".equals(MapL5055.get("AdjRange")) ? "" : MapL5055.get("AdjRange"));

				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}