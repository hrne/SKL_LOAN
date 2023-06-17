package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Lagdtp;
import com.st1.itx.db.domain.LagdtpId;
import com.st1.itx.db.service.LagdtpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2944")
@Scope("prototype")
/**
 * AS400不動產押品主檔查詢<BR>
 * 2023-06-17 Wei 增加 from SKL-佳怡:新系統要可以查舊系統AS400不動產押品主檔
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L2944 extends TradeBuffer {

	@Autowired
	private LagdtpService lagdtpService;

	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		info("active L2944 ");
		totaVo.init(titaVo);

		int gdrid1 = parse.stringToInteger(titaVo.getParam("Gdrid1"));
		int gdrid2 = parse.stringToInteger(titaVo.getParam("Gdrid2"));
		int gdrnum = parse.stringToInteger(titaVo.getParam("Gdrnum"));

		// 裝 Lahgtp PK
		LagdtpId lagdtpId = new LagdtpId();
		lagdtpId.setCusbrh(0);
		lagdtpId.setGdrid1(gdrid1);
		lagdtpId.setGdrid2(gdrid2);
		lagdtpId.setGdrnum(gdrnum);

		// 查DB
		Lagdtp lagdtp = lagdtpService.findById(lagdtpId, titaVo);

		// 該擔保品編號是否存在AS400建物明細檔
		// 若不存在 拋錯
		if (lagdtp == null) {
			throw new LogicException("E0001", "L2944該擔保品編號不存在AS400不動產押品主檔");
		}

		totaVo.putParam("Cusbrh", lagdtp.getCusbrh());
		totaVo.putParam("Gdrid1", lagdtp.getGdrid1());
		totaVo.putParam("Gdrid2", lagdtp.getGdrid2());
		totaVo.putParam("Gdrnum", lagdtp.getGdrnum());
		totaVo.putParam("Loclid", lagdtp.getLoclid());
		totaVo.putParam("Gdtidt", lagdtp.getGdtidt());
		totaVo.putParam("Gdtrdt", lagdtp.getGdtrdt());
		totaVo.putParam("Gdtpty", lagdtp.getGdtpty());
		totaVo.putParam("Gdtp1a", lagdtp.getGdtp1a());
		totaVo.putParam("Gdtp1m", lagdtp.getGdtp1m());
		totaVo.putParam("Gdtp2a", lagdtp.getGdtp2a());
		totaVo.putParam("Gdtp2m", lagdtp.getGdtp2m());
		totaVo.putParam("Gdtp3a", lagdtp.getGdtp3a());
		totaVo.putParam("Gdtp3m", lagdtp.getGdtp3m());
		totaVo.putParam("Gdttmr", lagdtp.getGdttmr());
		totaVo.putParam("Aplpam", lagdtp.getAplpam());
		totaVo.putParam("Lmsacn", lagdtp.getLmsacn());
		totaVo.putParam("Lmsapn", lagdtp.getLmsapn());
		totaVo.putParam("Gdtsdt", lagdtp.getGdtsdt());
		totaVo.putParam("Gdttyp", lagdtp.getGdttyp());
		totaVo.putParam("Gdtapn", lagdtp.getGdtapn());
		totaVo.putParam("Estval", lagdtp.getEstval());
		totaVo.putParam("Rstval", lagdtp.getRstval());
		totaVo.putParam("Ettval", lagdtp.getEttval());
		totaVo.putParam("Risval", lagdtp.getRisval());
		totaVo.putParam("Rntval", lagdtp.getRntval());
		totaVo.putParam("Mgttyp", lagdtp.getMgttyp());
		totaVo.putParam("Mtgagm", lagdtp.getMtgagm());
		totaVo.putParam("Gdrnum2", lagdtp.getGdrnum2());
		totaVo.putParam("Gdrmrk", lagdtp.getGdrmrk());
		totaVo.putParam("UpdateIdent", lagdtp.getUpdateIdent());

		addList(totaVo);

		return sendList();
	}
}