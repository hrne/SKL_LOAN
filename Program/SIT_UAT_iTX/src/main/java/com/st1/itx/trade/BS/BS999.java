package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcMain;
import com.st1.itx.db.domain.CdAcCode;
import com.st1.itx.db.domain.CdAcCodeId;
import com.st1.itx.db.service.AcMainService;
import com.st1.itx.db.service.CdAcCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcMainCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 日始作業 <br>
 * 執行時機：patch AcMain
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS999")
@Scope("prototype")
public class BS999 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS999.class);

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcMainService acMainService;

	@Autowired
	public CdAcCodeService cdAcCodeService;

	@Autowired
	public AcMainCom acMainCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS999 ......");

		// commitEnd
		this.batchTransaction.commit();

		/*---------- Step 2. 系統換日過帳(含年初損益類結轉) ----------*/

		acMainCom.setTxBuffer(this.txBuffer);
		Slice<AcMain> slAcMain = acMainService.findAll(this.index, Integer.MAX_VALUE, titaVo);
		List<AcMain> lAcMain = slAcMain == null ? null : new ArrayList<AcMain>(slAcMain.getContent());
		;
		for (AcMain ac : lAcMain) {
			CdAcCode tCdAcCode = cdAcCodeService
					.findById(new CdAcCodeId(ac.getAcNoCode(), ac.getAcSubCode(), ac.getAcDtlCode()), titaVo);
			if (tCdAcCode != null) {
				ac.setAcctCode(tCdAcCode.getAcctCode());
			}
		}
		try {
			acMainService.updateAll(lAcMain, titaVo); // update AcMain
		} catch (DBException e) {
			throw new LogicException(titaVo, "E6003", "tAcMain updateAll " + e.getErrorMsg());
		}

		Collections.sort(lAcMain, new Comparator<AcMain>() {
			public int compare(AcMain c1, AcMain c2) {
				if (c1.getAcDate() != c2.getAcDate()) {
					return c1.getAcDate() - c2.getAcDate();
				}
				return 0;
			}
		});

		int bhDate = lAcMain.get(0).getAcDate();
		List<AcMain> lAc = new ArrayList<AcMain>();
		for (AcMain ac : lAcMain) {
			if (ac.getAcDate() != bhDate) {
				acMainCom.changeDate(bhDate, ac.getAcDate(), lAc, titaVo);
				bhDate = ac.getAcDate();
				lAc = new ArrayList<AcMain>();
			}
			lAc.add(ac);
		}
		// end
		return null;
	}

}