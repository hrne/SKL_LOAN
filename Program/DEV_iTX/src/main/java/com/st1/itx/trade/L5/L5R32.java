package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.springjpa.cm.L5R32ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L5R32")
@Scope("prototype")
/**
 * 資金運用概況計算已放款金額用
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L5R32 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5R32.class);

	@Autowired
	public Parse parse;

	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public L5R32ServiceImpl l5R32ServiceImpl;

	@Autowired
	public AcMainService iAcMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.info("active L5R32 ");
		this.totaVo.init(titaVo);

//		L5101調Rim
//		1.抓取上月月底營業日... 本月1號的上營業日
//		2.抓取已放款餘額.....  AcMain裡 Acccode = 310 320 330 340 990 且 acbookcode=000 且acdate = 畫面輸入日期

		int functionCode = 0;
		int today = titaVo.getOrgEntdyI();
		String monthFirst = "";
		int monthFirstI = 0;
		int lastMonthEnd = 0;
		BigDecimal aldyLoanBal = BigDecimal.ZERO;

		if (!"".equals(titaVo.getParam("RimFuncCode"))) {
			functionCode = parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		}

		switch (functionCode) {
		case 1:
			dateUtil.init();
			
			if ((""+today).length() >= 7) {
				this.info("today ..." + (""+today));
				monthFirst = (""+today).substring(0, 5) + "01";
			}

			monthFirstI = parse.stringToInteger(monthFirst);

			lastMonthEnd = dateUtil.getbussDate(monthFirstI, -1);

			break;
		case 2:
			List<Map<String, String>> fnAllList = new ArrayList<>();

			try {
				fnAllList = l5R32ServiceImpl.findAll(titaVo);
			} catch (Exception e) {
				this.info("error ..." + e.getMessage());
			}

			if (fnAllList != null && fnAllList.size() > 0) {
				for (Map<String, String> fnAllTable : fnAllList) {
					aldyLoanBal = aldyLoanBal.add(parse.stringToBigDecimal(fnAllTable.get("F0")));
				}
			}

			break;
		default:
			break;
		}

		totaVo.putParam("L5r32LastMonthEndDay", lastMonthEnd);
		totaVo.putParam("L5r32AldyLoanBal", aldyLoanBal);

		this.addList(this.totaVo);
		return this.sendList();
	}
}