package com.st1.itx.trade.L7;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L7901")
@Scope("prototype")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L7901 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7901.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("=========== L7901 run titaVo: " + titaVo);

		this.info("active L7901 ");
		this.totaVo.init(titaVo);
		String job = "";

		if (titaVo.get("LNM341").equals("Y")) {
			this.info("L7901 active LNM34AP ");
//			MySpring.newTask("LNM34AP", this.txBuffer, titaVo);
			job += ";jLNM34AP";
		}

		if (titaVo.get("LNM342").equals("Y")) {
			this.info("L7901 active LNM34BP ");
//			MySpring.newTask("LNM34BP", this.txBuffer, titaVo);
			job += ";jLNM34BP";
		}

		if (titaVo.get("LNM343").equals("Y")) {
			this.info("L7901 active LNM34CP ");
//			MySpring.newTask("LNM34CP", this.txBuffer, titaVo);
			job += ";jLNM34CP";
		}

		if (titaVo.get("LNM344").equals("Y")) {
			this.info("L7901 active LNM34DP ");
//			MySpring.newTask("LNM34DP", this.txBuffer, titaVo);
			job += ";jLNM34DP";
		}

		if (titaVo.get("LNM345").equals("Y")) {
			this.info("L7901 active LNM34EP ");
//			MySpring.newTask("LNM34EP", this.txBuffer, titaVo);
			job += ";jLNM34EP";
		}

		if (titaVo.get("LNM346").equals("Y")) {
			this.info("L7901 active LNM34GP ");
//			MySpring.newTask("LNM34GP", this.txBuffer, titaVo);
			job += ";jLNM34GP";
		}

		if (!job.equals("")) {
			this.info("=========== L7901 setBatchJobId : ");
			titaVo.setBatchJobId(job.substring(1));
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}