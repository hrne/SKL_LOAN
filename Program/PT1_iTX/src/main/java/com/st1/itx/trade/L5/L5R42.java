package com.st1.itx.trade.L5;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
/*DB服務*/
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegFinShareService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/* DB容器 */
import com.st1.itx.db.domain.CustMain;

/*DB服務*/
import com.st1.itx.db.service.CustMainService;

/**
 * Tita<br>
 * YYY=9,3<br>
 * MM=9,2<br>
 * DD=9,2<br>
 * CustId=X,10<br>
 */

@Service("L5R42")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5R42 extends TradeBuffer {
	/* DB服務注入 */

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegFinShareService sNegFinShareService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Autowired
	public NegCom sNegCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5R42");
		this.info("active L5R42 ");
		this.totaVo.init(titaVo);

		int iCustNo = Integer.parseInt(titaVo.getParam("RimCustNo"));
		int iCaseSeq = Integer.parseInt(titaVo.getParam("RimCaseSeq"));

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 200;// 查全部 Integer.MAX_VALUE

		for (int i = 1; i <= 30; i++) {
			totaVo.putParam("L5R42FinCode" + i, "");
			totaVo.putParam("L5R42FinCodeName" + i, "");
			totaVo.putParam("L5R42ApprAmt" + i, 0);
			totaVo.putParam("L5R42AccuApprAmt" + i, 0);
		}
		totaVo.putParam("L5R42TxAmt", "");

		Slice<NegAppr01> slNegAppr01 = sNegAppr01Service.findByCustNoCaseSeq(iCustNo, iCaseSeq, 0, this.index,
				this.limit, titaVo);
		List<NegAppr01> lNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();

		if (lNegAppr01 == null) {
			throw new LogicException(titaVo, "E0001", "最大債權撥付資料檔");
		}

		CustMain CustMainVO = new CustMain();
		NegMain negMainVO = new NegMain();
		NegMainId negMainIdVO = new NegMainId();

		String tName = "";
		if (iCustNo > 9990000) {
			negMainIdVO.setCustNo(iCustNo);
			negMainIdVO.setCaseSeq(iCaseSeq);
			negMainVO = sNegMainService.findById(negMainIdVO, titaVo);
			if (negMainVO != null) {
				tName = negMainVO.getNegCustName();
			}
		} else {
			CustMainVO = sCustMainService.custNoFirst(iCustNo, iCustNo, titaVo);
			if (CustMainVO != null) {
				tName = CustMainVO.getCustName();
			}
		}
		totaVo.putParam("L5R42CustName", tName);

		int row = 1;
		int iTitaTxno = 0;
		int tAcdate = 0;
		String iTitaTlrNo = "";
		if (lNegAppr01 != null) {
			for (NegAppr01 NegAppr01VO : lNegAppr01) {

				if (row != 1 && iTitaTxno != NegAppr01VO.getTitaTxtNo()) {
					continue;
				}

				if (("458").equals(NegAppr01VO.getFinCode())) {
					continue;
				}
				tAcdate = NegAppr01VO.getAcDate() + 19110000;
				iTitaTlrNo = NegAppr01VO.getTitaTlrNo();
				totaVo.putParam("L5R42FinCode" + row, NegAppr01VO.getFinCode());
				totaVo.putParam("L5R42FinCodeName" + row, sNegCom.FindNegFinAcc(NegAppr01VO.getFinCode(), titaVo)[0]);// 債權機構名稱
				totaVo.putParam("L5R42ApprAmt" + row, NegAppr01VO.getApprAmt());
				totaVo.putParam("L5R42AccuApprAmt" + row, NegAppr01VO.getAccuApprAmt());

				totaVo.putParam("L5R42AcDate", NegAppr01VO.getAcDate());
				totaVo.putParam("L5R42TlrNo", NegAppr01VO.getTitaTlrNo());
				totaVo.putParam("L5R42TxtNo", NegAppr01VO.getTitaTxtNo());

				iTitaTxno = NegAppr01VO.getTitaTxtNo();
				row++;

			}

		}

		NegMain tNegMain = sNegMainService.findById(new NegMainId(iCustNo, iCaseSeq), titaVo);

		NegTrans tNegTrans = sNegTransService.findById(new NegTransId(tAcdate, iTitaTlrNo, iTitaTxno, iCustNo), titaVo);

		if (tNegTrans != null) {

			totaVo.putParam("L5R42TxAmt", tNegTrans.getTxAmt());

			if (tNegTrans.getSklShareAmt().compareTo(BigDecimal.ZERO) == 1) {
				totaVo.putParam("L5R42FinCode" + row, "458");
				totaVo.putParam("L5R42FinCodeName" + row, sNegCom.FindNegFinAcc("458", titaVo)[0]);// 債權機構名稱
				totaVo.putParam("L5R42ApprAmt" + row, tNegTrans.getSklShareAmt());

				if (tNegMain != null) {
					totaVo.putParam("L5R42AccuApprAmt" + row, tNegMain.getAccuSklShareAmt());
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}