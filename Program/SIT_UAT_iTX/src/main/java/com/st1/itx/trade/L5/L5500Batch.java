package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.HlAreaData;
import com.st1.itx.db.domain.HlCusData;
import com.st1.itx.db.service.HlAreaDataService;
import com.st1.itx.db.service.HlCusDataService;
import com.st1.itx.db.service.springjpa.cm.L5500ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L5500Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5500Batch extends TradeBuffer {
	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public HlAreaDataService hlAreaDataService;

	@Autowired
	public HlCusDataService hlCusDataService;
	
	@Autowired
	public L5500ServiceImpl l5500ServiceImpl;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5500Batch ");
		this.totaVo.init(titaVo);

		ProcHlAreaData(titaVo);

		ProcHlCusData(titaVo);
		
		String msg = "L5500已處理完畢";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", "", msg, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 單位達成件數、金額統計檔
	public void procHlAreaLnYg6Pt(TitaVo titaVo) throws LogicException {

	}

	// 借款人資料
	public void ProcHlCusData(TitaVo titaVo) throws LogicException {
		//initialize 
		Slice<HlCusData> slHlCusData = hlCusDataService.findAll(0, Integer.MAX_VALUE);
		List<HlCusData> lHlCusData = slHlCusData == null ? null : slHlCusData.getContent();
		if (lHlCusData != null) {
			try {
				hlCusDataService.deleteAll(lHlCusData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlAreaData");
			}
		}
		
		String sql = "select \"CustNo\",\"CustName\",\"LastUpdate\" ";
		sql += "from \"CustMain\" ";
		sql += "where \"CustNo\" > 0 ";

		List<Map<String, String>> dataCusData = l5500ServiceImpl.findData(index, limit, sql, null, titaVo);
		lHlCusData = new ArrayList<HlCusData>();
		if (dataCusData != null && dataCusData.size() > 0) {
			for (Map<String, String> d : dataCusData) {
				HlCusData hlCusData = new HlCusData();

				hlCusData.setHlCusNo(Long.valueOf(d.get("CustNo").toString()));
				hlCusData.setHlCusName(d.get("CustName").substring(0, d.get("CustName").length()>50?50:d.get("CustName").length()));
				this.info(parse.stringToStringDate(d.get("LastUpdate"))+"-"+parse.stringToInteger(parse.stringToStringDate(d.get("LastUpdate"))));
				
				int processDate = parse.stringToInteger(parse.stringToStringDate(d.get("LastUpdate")));
				if (processDate > 0) {
					processDate += 19110000;
				}
				hlCusData.setProcessDate(processDate);
				lHlCusData.add(hlCusData);
			}

		}

		if (lHlCusData != null && lHlCusData.size() != 0) {
			try {
				hlCusDataService.insertAll(lHlCusData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlCusData");
			}
		}
		
		this.batchTransaction.commit();

	}

	// 區域資料主檔
	public void ProcHlAreaData(TitaVo titaVo) throws LogicException {
		//initialize 
		Slice<HlAreaData> slHlAreaData = hlAreaDataService.findAll(0, Integer.MAX_VALUE);
		List<HlAreaData> lHlAreaData = slHlAreaData == null ? null : slHlAreaData.getContent();
		if (lHlAreaData != null) {
			try {
				hlAreaDataService.deleteAll(lHlAreaData, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "HlAreaData");
			}
		}

		//insert
		this.info("L5500 DataInsertUpdateAreaData");
		String sql = "select A.\"UnitCode\",";
		sql += "A.\"UnitItem\",";
		sql += "A.\"UnitManager\",";
		sql += "B.\"Fullname\" ";
		sql += "from \"CdBcm\" A ";
		sql += "left join \"CdEmp\" B ON B.\"EmployeeNo\"=A.\"UnitManager\" ";
		sql += "where A.\"Enable\" = 'Y' ";

		List<Map<String, String>> dataAreaData = l5500ServiceImpl.findData(index, limit, sql, null, titaVo);
		lHlAreaData = new ArrayList<HlAreaData>();
		if (dataAreaData != null && dataAreaData.size() > 0) {
			for (Map<String, String> d : dataAreaData) {
//				this.info(d.get("UnitCode") + "=" + d.get("UnitItem") + "/" + d.get("UnitItem").toString().length());
				HlAreaData tHlAreaData = new HlAreaData();
				tHlAreaData.setAreaUnitNo(d.get("UnitCode"));// 區域代碼--VARCHAR2(6)
				tHlAreaData.setAreaName(d.get("UnitItem"));// 區域名稱--VARCHAR2(20)
				tHlAreaData.setAreaChiefEmpNo(d.get("UnitManager"));// 區域主管員編--VARCHAR2(6)
				tHlAreaData.setAreaChiefName(d.get("Fullname"));// 區域主管名稱--NVARCHAR2(15)

				lHlAreaData.add(tHlAreaData);
			}
		}

		if (lHlAreaData != null && lHlAreaData.size() != 0) {
//			this.info("lHlAreaData = " + lHlAreaData.size());
			try {
				hlAreaDataService.insertAll(lHlAreaData, titaVo);
			} catch (DBException e) {
//				this.info("L5500 HlAreaData[" + e.getErrorMsg() + "]");
				throw new LogicException(titaVo, "E0005", "HlAreaData");
			}
		}
		this.batchTransaction.commit();
	}

}