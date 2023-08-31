package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LaLgtp;
import com.st1.itx.db.domain.LaLgtpId;
import com.st1.itx.db.service.LaLgtpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2945")
@Scope("prototype")
/**
 * AS400土地明細資料查詢<BR>
 * 2023-08-31 Wei 增加 from SKL-佳怡:新系統要可以查舊系統土地明細
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L2945 extends TradeBuffer {

	@Autowired
	private LaLgtpService laLgtpService;

	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		info("active L2945 ");
		totaVo.init(titaVo);

		int gdrid1 = parse.stringToInteger(titaVo.getParam("Gdrid1"));
		int gdrid2 = parse.stringToInteger(titaVo.getParam("Gdrid2"));
		int gdrnum = parse.stringToInteger(titaVo.getParam("Gdrnum"));
		int lgtseq = parse.stringToInteger(titaVo.getParam("Lgtseq"));

		// 裝 Lahgtp PK
		LaLgtpId laLgtpId = new LaLgtpId();
		laLgtpId.setCusbrh(0);
		laLgtpId.setGdrid1(gdrid1);
		laLgtpId.setGdrid2(gdrid2);
		laLgtpId.setGdrnum(gdrnum);
		laLgtpId.setLgtseq(lgtseq);

		// 查DB
		LaLgtp laLgtp = laLgtpService.findById(laLgtpId, titaVo);

		// 該擔保品編號是否存在AS400土地明細檔
		// 若不存在 拋錯
		if (laLgtp == null) {
			throw new LogicException("E0001", "L2945該擔保品編號不存在AS400土地明細檔");
		}

		totaVo.putParam("Cusbrh", laLgtp.getCusbrh());
		totaVo.putParam("Gdrid1", laLgtp.getGdrid1());
		totaVo.putParam("Gdrid2", laLgtp.getGdrid2());
		totaVo.putParam("Gdrnum", laLgtp.getGdrnum());
		totaVo.putParam("Lgtseq", laLgtp.getLgtseq());
		totaVo.putParam("Gdrmrk", laLgtp.getGdrmrk());
		totaVo.putParam("Gdrnum2", laLgtp.getGdrnum2());
		totaVo.putParam("Grtsts", laLgtp.getGrtsts());
		totaVo.putParam("Lgtcif", laLgtp.getLgtcif());
		totaVo.putParam("Lgtiam", laLgtp.getLgtiam());
		totaVo.putParam("Lgtiid", laLgtp.getLgtiid());
		totaVo.putParam("Lgtory", laLgtp.getLgtory());
		totaVo.putParam("Lgtpta", laLgtp.getLgtpta());
		totaVo.putParam("Lgtsam", laLgtp.getLgtsam());
		totaVo.putParam("Lgtsat", laLgtp.getLgtsat());
		totaVo.putParam("Lgtcty", laLgtp.getLgtcty());
		totaVo.putParam("Lgttwn", laLgtp.getLgttwn());
		totaVo.putParam("Lgtsgm", laLgtp.getLgtsgm());
		totaVo.putParam("Lgtssg", laLgtp.getLgtssg());
		totaVo.putParam("Lgtnm1", laLgtp.getLgtnm1());
		totaVo.putParam("Lgtnm2", laLgtp.getLgtnm2());
		totaVo.putParam("Lgtsqm", laLgtp.getLgtsqm());
		totaVo.putParam("Lgttax", laLgtp.getLgttax());
		totaVo.putParam("Lgttay", laLgtp.getLgttay());
		totaVo.putParam("Lgttyp", laLgtp.getLgttyp());
		totaVo.putParam("Lgttyr", laLgtp.getLgttyr());
		totaVo.putParam("Lgtunt", laLgtp.getLgtunt());
		totaVo.putParam("Lgtuse", laLgtp.getLgtuse());
		totaVo.putParam("Lgtval", laLgtp.getLgtval());
		totaVo.putParam("Lgtvym", laLgtp.getLgtvym());
		totaVo.putParam("Updateident", laLgtp.getUpdateident());

		addList(totaVo);

		return sendList();
	}
}