package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InnDocRecord;
import com.st1.itx.db.domain.InnDocRecordId;
import com.st1.itx.db.service.InnDocRecordService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L5R48")
@Scope("prototype")
/**
 * 抓jsonField
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
public class L5R48 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse iParse;

	@Autowired
	public InnDocRecordService innDocRecordService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R48 ");
		this.totaVo.init(titaVo);

		int iCustNo = Integer.valueOf(titaVo.getParam("Rim5R48CustNo"));
		int iFacmNo = Integer.valueOf(titaVo.getParam("Rim5R48FacmNo"));
		String iApplSeq = titaVo.getParam("Rim5R48ApplSeq");

		for (int n = 1; n <= 25; n++) {
			this.totaVo.putParam("L5r48OPTA" + n, 0);
			this.totaVo.putParam("L5r48AMTA" + n, 0);
			this.totaVo.putParam("L5r48OPTB" + n, 0);
			this.totaVo.putParam("L5r48AMTB" + n, 0);
			this.totaVo.putParam("L5r48OPTC" + n, 0);
			this.totaVo.putParam("L5r48AMTC" + n, 0);
		}

		TempVo tTempVo = new TempVo();

		InnDocRecord tInnDocRecord = innDocRecordService.findById(new InnDocRecordId(iCustNo, iFacmNo, iApplSeq), titaVo);

		String OPT = "";
		if (tInnDocRecord != null) {
			tTempVo = tTempVo.getVo(tInnDocRecord.getJsonFields());

			this.info("tTempVo==" + tTempVo);
			// OPTA
			for (int i = 1; i <= 25; i++) {
				OPT = tTempVo.getParam("OPTA" + i);

				if (!OPT.isEmpty() && OPT != null) {
					this.info("OPTA==" + OPT);
					this.totaVo.putParam("L5r48OPTA" + i, Integer.parseInt(tTempVo.getParam("OPTA" + i)));
					this.totaVo.putParam("L5r48AMTA" + i, Integer.parseInt(tTempVo.getParam("AMTA" + i)));

				}

			}
			// OPTB
			for (int i = 1; i <= 25; i++) {
				OPT = tTempVo.getParam("OPTB" + i);

				if (!OPT.isEmpty() && OPT != null) {
					this.info("OPTB==" + OPT);
					this.totaVo.putParam("L5r48OPTB" + i, Integer.parseInt(tTempVo.getParam("OPTB" + i)));
					this.totaVo.putParam("L5r48AMTB" + i, Integer.parseInt(tTempVo.getParam("AMTB" + i)));

				}

			}
			// OPTC
			for (int i = 1; i <= 25; i++) {
				OPT = tTempVo.getParam("OPTC" + i);

				if (!OPT.isEmpty() && OPT != null) {
					this.info("OPTC==" + OPT);
					this.totaVo.putParam("L5r48OPTC" + i, Integer.parseInt(tTempVo.getParam("OPTC" + i)));
					this.totaVo.putParam("L5r48AMTC" + i, Integer.parseInt(tTempVo.getParam("AMTC" + i)));

				}

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}