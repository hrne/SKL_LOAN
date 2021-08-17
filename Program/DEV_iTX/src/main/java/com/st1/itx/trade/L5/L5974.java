package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.service.CdBankService;
/*DB服務*/
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.springjpa.cm.L5974ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FinCode=9,3<br>
 */

@Service("L5974")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5974 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	@Autowired
	public CdBankService sCdBankService;
	@Autowired
	public L5974ServiceImpl l5974ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5974");
		this.info("active L5974 ");
		this.totaVo.init(titaVo);

		String FinCode = titaVo.getParam("FinCode").trim(); // 債權機構
		String MaxDataRow = titaVo.getParam("MaxDataRow").trim(); // 最大資料筆數

		this.info("L5974 FinCode=[" + FinCode + "]");

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = parse.stringToInteger(MaxDataRow);			
		

		String sql = l5974ServiceImpl.sqlL5974(FinCode);
		this.info("L5974 sql=[" + sql + "]");

		List<String[]> tL5974 = l5974ServiceImpl.FindL5974(index, limit, sql, FinCode, titaVo);

		if (tL5974 != null && tL5974.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			this.totaVo.setMsgEndToAuto();// 自動折返
//			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		if (tL5974 != null && tL5974.size() != 0) {
			for (String rowData[] : tL5974) {
				String OORemitBank = rowData[2];

				String OORemitBankX = "";
				String OORemitBankX1 = rowData[3].trim();
				if (OORemitBankX1 != null && OORemitBankX1.length() != 0) {
					OORemitBankX = OORemitBankX1;
				} else {
					String OORemitBankX2 = rowData[4].trim();
					if (OORemitBankX2 != null && OORemitBankX2.length() != 0) {
						OORemitBankX = OORemitBankX2;
					}
				}
				OORemitBankX = "（" + OORemitBank + "） " + OORemitBankX;

				String OODataSendSection = rowData[6];
				String OODataSendSectionX = "";
				String OODataSendSectionX1 = rowData[7].trim();
				if (OODataSendSectionX1 != null && OODataSendSectionX1.length() != 0) {
					OODataSendSectionX = OODataSendSectionX1;
				} else {
					String OODataSendSectionX2 = rowData[8].trim();
					if (OODataSendSectionX2 != null && OODataSendSectionX2.length() != 0) {
						OODataSendSectionX = OODataSendSectionX2;
					}
				}
				OODataSendSectionX = "（" + OODataSendSection + "） " + OODataSendSectionX;

				OccursList occursList = new OccursList();
				occursList.putParam("OOFinCode", rowData[0]);//
				occursList.putParam("OOFinCodeX", rowData[1]);//
				occursList.putParam("OORemitBank", OORemitBank);//
				occursList.putParam("OORemitBankX", OORemitBankX);//
				occursList.putParam("OORemitAcct", rowData[5]);//
				occursList.putParam("OODataSendSection", OODataSendSection);//
				occursList.putParam("OODataSendSectionX", OODataSendSectionX);//
				this.totaVo.addOccursList(occursList);
			}
		} else {
			if (this.index != 0) {
				// 代表有多筆查詢,然後筆數剛好可以被整除

			} else {
				// E2003 查無資料
				throw new LogicException(titaVo, "E2003", "債務協商債權機構帳戶檔");
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}