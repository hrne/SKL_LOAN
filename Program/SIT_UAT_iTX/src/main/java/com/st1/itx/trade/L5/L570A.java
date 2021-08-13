package com.st1.itx.trade.L5;

import java.util.ArrayList;
/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
/* DB容器 */
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
/*DB服務*/
import com.st1.itx.util.common.NegCom;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegMainService;

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CustId=X,10<br>
 * CaseSeq=X,3<br>
 * CustNo=X,7<br>
 * TransAcDate=9,7<br>
 * TransTitaTlrNo=X,6<br>
 * TransTitaTxtNo=X,8<br>
 * TransCustNo=X,7<br>
 * TransCaseSeq=X,3<br>
 * NewTransTxKind=9,1<br>
 * NewTransReturnAmt=9,14.2<br>
 */

@Service("L570A")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L570A extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L570A.class);
	/* DB服務注入 */
	@Autowired
	public NegTransService sNegTransService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public NegCom sNegCom;
	@Autowired
	public AcNegCom acNegCom;
	@Autowired
	public AcDetailCom acDetailCom;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	private NegTrans tNegTrans = new NegTrans();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L570A ");
		this.totaVo.init(titaVo);
		acDetailCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		this.info("L570A titaVo=[" + titaVo + "]");
//		#CustId
//		#CaseSeq
//		#CustNo
//		#TransAcDate
//		#TransTitaTlrNo
//		#TransTitaTxtNo
//		#TransCustNo
//		#TransCaseSeq
//		#TransTxKind
//		#TransReturnAmt

		// #OOAcctDate+#OOTitaTlrNo+#OOTitaTxtNo
		String OOAcctDate = "";
		String OOTitaTlrNo = "";
		String OOTitaTxtNo = "";

		String TransAcDate = "";
		String TransTitaTlrNo = "";
		String TransTitaTxtNo = "";

		this.info("***L570A****ST");
		OOAcctDate = titaVo.getParam("OOAcctDate").trim();
		OOTitaTlrNo = titaVo.getParam("OOTitaTlrNo").trim();
		OOTitaTxtNo = titaVo.getParam("OOTitaTxtNo").trim();

		TransAcDate = OOAcctDate;
		TransTitaTlrNo = OOTitaTlrNo;
		TransTitaTxtNo = OOTitaTxtNo;
		//入帳明細轉待處理	
		NegTransId tNegTransId = new NegTransId();
		int AcDate = parse.stringToInteger(TransAcDate);
		tNegTransId.setAcDate(AcDate);
		tNegTransId.setTitaTlrNo(TransTitaTlrNo);
		tNegTransId.setTitaTxtNo(parse.stringToInteger(TransTitaTxtNo));
		tNegTrans = sNegTransService.holdById(tNegTransId);
		if (tNegTrans == null) {
			throw new LogicException(titaVo, "E0001", "債務協商交易檔");
		}
		if (tNegTrans.getTxStatus() == 0) {
			tNegTrans.setTxStatus(1); // 交易狀態0:未入帳;1:待處理;2:已入帳
				
		} else if(tNegTrans.getTxStatus() == 1){
			tNegTrans.setTxStatus(0); // 交易狀態0:未入帳;1:待處理;2:已入帳
		
		}else {
			throw new LogicException(titaVo, "E0010", "已入帳，如需變更交易狀態請做訂正");
		}
		try {
			sNegTransService.update(tNegTrans, titaVo);//
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());// E0007 更新資料時，發生錯誤
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}