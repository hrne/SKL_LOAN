package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.Lahgtp;
import com.st1.itx.db.domain.LahgtpId;
import com.st1.itx.db.service.LahgtpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2943")
@Scope("prototype")
/**
 * AS400建物明細資料查詢<BR>
 * 2023-05-25 Wei 增加 from SKL-佳怡:新系統要可以查舊系統建物明細
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L2943 extends TradeBuffer {

	@Autowired
	private LahgtpService lahgtpService;

	@Autowired
	private Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		info("active L2943 ");
		totaVo.init(titaVo);

		int gdrid1 = parse.stringToInteger(titaVo.getParam("Gdrid1"));
		int gdrid2 = parse.stringToInteger(titaVo.getParam("Gdrid2"));
		int gdrnum = parse.stringToInteger(titaVo.getParam("Gdrnum"));
		int lgtseq = parse.stringToInteger(titaVo.getParam("Lgtseq"));

		// 裝 Lahgtp PK
		LahgtpId lahgtpId = new LahgtpId();
		lahgtpId.setCusbrh(0);
		lahgtpId.setGdrid1(gdrid1);
		lahgtpId.setGdrid2(gdrid2);
		lahgtpId.setGdrnum(gdrnum);
		lahgtpId.setLgtseq(lgtseq);

		// 查DB
		Lahgtp lahgtp = lahgtpService.findById(lahgtpId, titaVo);

		// 該擔保品編號是否存在AS400建物明細檔
		// 若不存在 拋錯
		if (lahgtp == null) {
			throw new LogicException("E0001", "L2943該擔保品編號不存在AS400建物明細檔");
		}

		totaVo.putParam("Cusbrh", lahgtp.getCusbrh());
		totaVo.putParam("Lgtcif", lahgtp.getLgtcif());
		totaVo.putParam("Lgtcif", lahgtp.getLgtcif());
		totaVo.putParam("Lgtadr", lahgtp.getLgtadr());
		totaVo.putParam("Hgtmhn", lahgtp.getHgtmhn());
		totaVo.putParam("Hgtmhs", lahgtp.getHgtmhs());
		totaVo.putParam("Hgtpsm", lahgtp.getHgtpsm());
		totaVo.putParam("Hgtcam", lahgtp.getHgtcam());
		totaVo.putParam("Lgtiid", lahgtp.getLgtiid());
		totaVo.putParam("Lgtunt", lahgtp.getLgtunt());
		totaVo.putParam("Lgtiam", lahgtp.getLgtiam());
		totaVo.putParam("Lgtsam", lahgtp.getLgtsam());
		totaVo.putParam("Lgtsat", lahgtp.getLgtsat());
		totaVo.putParam("Grtsts", lahgtp.getGrtsts());
		totaVo.putParam("Hgtstr", lahgtp.getHgtstr());
		totaVo.putParam("Hgtcdt", lahgtp.getHgtcdt());
		totaVo.putParam("Hgtflr", lahgtp.getHgtflr());
		totaVo.putParam("Hgtrof", lahgtp.getHgtrof());
		totaVo.putParam("Salnam", lahgtp.getSalnam());
		totaVo.putParam("Salid1", lahgtp.getSalid1());
		totaVo.putParam("Hgtcap", lahgtp.getHgtcap());
		totaVo.putParam("Hgtgus", lahgtp.getHgtgus());
		totaVo.putParam("Hgtaus", lahgtp.getHgtaus());
		totaVo.putParam("Hgtfor", lahgtp.getHgtfor());
		totaVo.putParam("Hgtcpe", lahgtp.getHgtcpe());
		totaVo.putParam("Hgtads", lahgtp.getHgtads());
		totaVo.putParam("Hgtad1", lahgtp.getHgtad1());
		totaVo.putParam("Hgtad2", lahgtp.getHgtad2());
		totaVo.putParam("Hgtad3", lahgtp.getHgtad3());
		totaVo.putParam("Hgtgtd", lahgtp.getHgtgtd());
		totaVo.putParam("Buyamt", lahgtp.getBuyamt());
		totaVo.putParam("Buydat", lahgtp.getBuydat());
		totaVo.putParam("Gdrnum2", lahgtp.getGdrnum2());
		totaVo.putParam("Gdrmrk", lahgtp.getGdrmrk());
		totaVo.putParam("Hgtmhn2", lahgtp.getHgtmhn2());
		totaVo.putParam("Hgtcip", lahgtp.getHgtcip());
		totaVo.putParam("UpdateIdent", lahgtp.getUpdateIdent());

		addList(totaVo);

		return sendList();
	}
}