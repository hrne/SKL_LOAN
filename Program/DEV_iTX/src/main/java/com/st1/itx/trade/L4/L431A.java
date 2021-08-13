package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BatxRateChange;
import com.st1.itx.db.domain.BatxRateChangeId;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L431A")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L431A extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L431A.class);

	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	public CdBaseRateService cdBaseRateService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	int adjDate = 0;

	/**
	 * 調整代碼 </br>
	 * 1:按合約自動調整 </br>
	 * 2:按地區別自動調整 </br>
	 * 3:人工調整-未調整 </br>
	 * 4:人工調整-待輸入 </br>
	 * 5:人工調整-已調整 </br>
	 * 9:上次繳息日大於利率生效日
	 */
	private int adjCode = 0;
	private int custNo = 0;
	private int facmNo = 0;
	private int bormNo = 0;
	private int curtEffDate = 0;
	private int txKind = 0;

	/**
	 * 2.按合約加碼利率調整 </br>
	 * 3.按目前利率調整 </br>
	 * 4.按個別加碼利率調整 </br>
	 * 5.按輸入利率調整 </br>
	 */
	int adjFlag = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L431A ");
		this.totaVo.init(titaVo);

//		T(6A,#AdjMonth+#OOAdjCode+#OOCustNo+#OOFacmNo+#OOBormNo+#OOCurtEffDate)

		adjDate = parse.stringToInteger(titaVo.getParam("AdjDate")) + 19110000;
		txKind = parse.stringToInteger(titaVo.getParam("TxKind"));
		adjCode = parse.stringToInteger(titaVo.getParam("OOAdjCode"));
		custNo = parse.stringToInteger(titaVo.getParam("OOCustNo"));
		facmNo = parse.stringToInteger(titaVo.getParam("OOFacmNo"));
		bormNo = parse.stringToInteger(titaVo.getParam("OOBormNo"));
		curtEffDate = parse.stringToInteger(titaVo.getParam("OOCurtEffDate"));

		this.info("titaVo.getBtnIndex() ..." + titaVo.getBtnIndex());
		
		adjFlag = getAdjFlag(titaVo.getBtnIndex());

		this.info("adjDate ..." + adjDate);
		this.info("txKind ..." + txKind);
		this.info("adjCode ..." + adjCode);
		this.info("custNo ..." + custNo);
		this.info("facmNo ..." + facmNo);
		this.info("bormNo ..." + bormNo);
		this.info("curtEffDate ..." + curtEffDate);
		this.info("adjFlag ..." + adjFlag);

		BatxRateChange tBatxRateChange = new BatxRateChange();
		BatxRateChangeId tBatxRateChangeId = new BatxRateChangeId();
		tBatxRateChangeId.setAdjDate(adjDate);
		tBatxRateChangeId.setCustNo(custNo);
		tBatxRateChangeId.setFacmNo(facmNo);
		tBatxRateChangeId.setBormNo(bormNo);

		tBatxRateChange = batxRateChangeService.findById(tBatxRateChangeId);

//		1:按合約自動調整            delete  (4)
//	    2:按地區別自動調整        delete  (4)
//	    3:人工調整-未調整         flag on (1)
//	    4:人工調整-待輸入         delete  (4)
//	    5:人工調整-已調整         delete  (4)

//		if 固定機動&下次調整日為0者不寫LoanRateChange

		if (adjCode == 3) {
			if (tBatxRateChange != null) {
				setBatxRateChange(tBatxRateChange, 1, titaVo);
			} else {
				throw new LogicException("E0001", "L431A Couldn't Find CustNo : " + parse.IntegerToString(custNo, 7)
						+ "-" + parse.IntegerToString(facmNo, 3) + "-" + parse.IntegerToString(bormNo, 3));
			}

		} else {
			if (tBatxRateChange != null) {
				setBatxRateChange(tBatxRateChange, 4, titaVo);
			} else {
				throw new LogicException("E0001", "L431A Couldn't Find CustNo : " + parse.IntegerToString(custNo, 7)
						+ "-" + parse.IntegerToString(facmNo, 3) + "-" + parse.IntegerToString(bormNo, 3));
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setBatxRateChange(BatxRateChange iBatxRateChange, int flag, TitaVo titaVo) throws LogicException {
		BatxRateChange oBatxRateChange = batxRateChangeService.holdById(iBatxRateChange.getBatxRateChangeId());

		if (oBatxRateChange.getConfirmFlag() == 1) {
			throw new LogicException("E0007", " 此筆資料已被確定，請先訂正L4321。");
		}

		if (flag == 1) {
			switch (adjFlag) {
			case 2:
				oBatxRateChange.setAdjustedRate(iBatxRateChange.getContractRate());
//				5.人工調整(已調整)
				oBatxRateChange.setAdjCode(5);
				oBatxRateChange.setRateKeyInCode(1); // 1.已輸入
				break;
			case 3:
				oBatxRateChange.setAdjustedRate(iBatxRateChange.getPresentRate());
//				5.人工調整(已調整)
				oBatxRateChange.setAdjCode(5);
				oBatxRateChange.setRateKeyInCode(1); // 1.已輸入
				break;
			case 4:
				oBatxRateChange
						.setAdjustedRate(iBatxRateChange.getPresentRate().add(iBatxRateChange.getIndividualIncr()));
//				5.人工調整(已調整)
				oBatxRateChange.setAdjCode(5);
				oBatxRateChange.setRateKeyInCode(1); // 1.已輸入
				break;
			case 5:
//				4.人工調整(待輸入)
				oBatxRateChange.setAdjCode(4);
				break;
			default:
				break;
			}

			try {
				batxRateChangeService.update(oBatxRateChange);
			} catch (DBException e) {
				throw new LogicException("E0007", "L431A tBatxRateChange update " + e.getErrorMsg());
			}

//		取消註記，把利率&記號歸零
		} else if (flag == 4) {

			oBatxRateChange.setAdjCode(3); // 3.人工調整(未調整)
			oBatxRateChange.setRateKeyInCode(0); // 0.未輸入
			oBatxRateChange.setAdjustedRate(BigDecimal.ZERO); // 調整後利率

			try {
				batxRateChangeService.update(oBatxRateChange);
			} catch (DBException e) {
				throw new LogicException("E0007", "L431A tBatxRateChange update " + e.getErrorMsg());
			}
		}
	}

	private int getAdjFlag(String flag) {
		int result = 0;

//		3.機動利率調整 按合約加碼利率按鈕移除
		if (txKind == 3) {
			switch (flag) {
			case "0":
				result = 3;
				break;
			case "1":
				result = 4;
				break;
			case "2":
				result = 5;
				break;
			default:
				break;
			}
		} else {
			switch (flag) {
			case "0":
				result = 2;
				break;
			case "1":
				result = 3;
				break;
			case "2":
				result = 4;
				break;
			case "3":
				result = 5;
				break;
			default:
				break;
			}
		}
		return result;
	}
}