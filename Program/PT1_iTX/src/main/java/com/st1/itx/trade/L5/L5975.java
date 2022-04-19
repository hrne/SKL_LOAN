package com.st1.itx.trade.L5;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.springjpa.cm.L5975ServiceImpl;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FinCode=9,3<br>
 */

@Service("L5975")
@Scope("prototype")
/**
 * 
 * 
 * @author Heng
 * @version 1.0.0
 */
public class L5975 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5975.class);
	/* DB服務注入 */

	@Autowired
	L5975ServiceImpl l5975ServiceImpl;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public NegCom sNegCom;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5975");
		this.info("active L5975 ");
		this.totaVo.init(titaVo);

		int Select = parse.stringToInteger(titaVo.getParam("Select"));

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 40;// 查全部

		List<Map<String, String>> listL5975 = null;

		if (Select == 1) {
			try {
				listL5975 = l5975ServiceImpl.findFindCode(titaVo, this.index, this.limit);

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L5975ServiceImpl.findAll error = " + errors.toString());
			}
		} else {
			try {
				listL5975 = l5975ServiceImpl.findDataSendUnit(titaVo, this.index, this.limit);

			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("L5975ServiceImpl.findAll error = " + errors.toString());
			}
		}

		if (listL5975 != null && listL5975.size() >= this.limit) {
			/* 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可 */
			titaVo.setReturnIndex(this.setIndexNext());
			// this.totaVo.setMsgEndToAuto();// 自動折返
			this.totaVo.setMsgEndToEnter();// 手動折返
		}

		if (listL5975 == null || listL5975.size() == 0) {
			throw new LogicException(titaVo, "E0001", "最大債權撥付統計查詢");

		} else {

			for (Map<String, String> t5975 : listL5975) {

				OccursList occursList = new OccursList();
				occursList.putParam("OOFinCode", t5975.get("F0"));
				String MainFinCodeName = sNegCom.FindNegFinAcc(t5975.get("F0"), titaVo)[0];
				occursList.putParam("OOFinCodeName", MainFinCodeName);
				occursList.putParam("OOAmt", t5975.get("F1"));
				occursList.putParam("OOCnt", t5975.get("F2"));

				this.totaVo.addOccursList(occursList);

			} // for

		} // else

		this.addList(this.totaVo);
		return this.sendList();
	}

}