package com.st1.itx.trade.L8;

import java.util.ArrayList;
/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TbJcicMu01;
/*DB服務*/
import com.st1.itx.db.service.TbJcicMu01Service;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8950")
@Scope("prototype")
/**
 * 聯徵產品查詢
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L8950 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8950.class);
	/* DB服務注入 */
	@Autowired
	public TbJcicMu01Service sTbJcicMu01Service;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8349 ");
		this.totaVo.init(titaVo);

		String iEmpId = titaVo.getParam("EmpId");
		int iDataDate = Integer.valueOf(titaVo.getParam("DataDate"));
		/*
		 * 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		 */
		this.index = titaVo.getReturnIndex();

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 500;

		Slice<TbJcicMu01> iTbJcicMu01 = null;
		if (iEmpId.equals("") && iDataDate == 0) {
			iTbJcicMu01 = sTbJcicMu01Service.findAll(this.index, this.limit, titaVo);
		} else if (!iEmpId.equals("") && iDataDate == 0) {
			iTbJcicMu01 = sTbJcicMu01Service.EmpIdEq(iEmpId, this.index, this.limit, titaVo);
		} else if (iEmpId.equals("") && iDataDate != 0) {
			iTbJcicMu01 = sTbJcicMu01Service.DataDateEq(iDataDate, this.index, this.limit, titaVo);
		} else {
			iTbJcicMu01 = sTbJcicMu01Service.EmpIdRcEq(iEmpId, iDataDate, this.index, this.limit, titaVo);
		}
		if (iTbJcicMu01 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 主檔無資料錯誤訊息
		} else {
			for (TbJcicMu01 aTbJcicMu01 : iTbJcicMu01) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOEmpId", aTbJcicMu01.getEmpId());
				occursList.putParam("OOEmpName", aTbJcicMu01.getEmpName());
				if (aTbJcicMu01.getDataDate()==0){
					occursList.putParam("OODataDate", "0");
				}else{
					occursList.putParam("OODataDate", Integer.valueOf(aTbJcicMu01.getDataDate())-19110000);
				}
				this.totaVo.addOccursList(occursList);
			}
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}