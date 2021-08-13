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

@Service("L7902")
@Scope("prototype")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L7902 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L7902.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("=========== L7902 run titaVo: " + titaVo);

		this.info("active L7902 ");
		this.totaVo.init(titaVo);
		String job = "";

		// 清單1 ：表內放款與應收帳款-資產基本資料與計算原始有效利率用
		if (titaVo.get("MONTHLY1").equals("Y")) {
			this.info("L7902 active LNM39AP ");
//			MySpring.newTask("LNM39AP", this.txBuffer, titaVo);
			job += ";jLNM39AP";
		}

		// 清單2 ：台幣放款-計算原始有效利率用
		if (titaVo.get("MONTHLY2").equals("Y")) {
			this.info("L7902 active LNM39BP ");
//			MySpring.newTask("LNM39BP", this.txBuffer, titaVo);
			job += ";jLNM39BP";
		}

		// 清單3 ：台幣放款-計算原始有效利率用
		if (titaVo.get("MONTHLY3").equals("Y")) {
			this.info("L7902 active LNM39CP ");
//			MySpring.newTask("LNM39CP", this.txBuffer, titaVo);
			job += ";jLNM39CP";
		}

		// 清單4 ：放款與AR-估計回收率用
		if (titaVo.get("MONTHLY4").equals("Y")) {
			this.info("L7902 active LNM39DP ");
//			MySpring.newTask("LNM39DP", this.txBuffer, titaVo);
			job += ";jLNM39DP";
		}

//		// 清單5 ：會計帳 
//		if (titaVo.get("MONTHLY5").equals("Y")) {
//			this.info("L7902 active LNM39EP ");
////    MySpring.newTask("LNM39EP", this.txBuffer, titaVo);
//			job+=";jLNM39EP";
//		}

		// 清單6 ：放款與應收帳款-協商戶用
		if (titaVo.get("MONTHLY5").equals("Y")) {
			this.info("L7902 active LNM39FP ");
//			MySpring.newTask("LNM39FP", this.txBuffer, titaVo);
			job += ";jLNM39FP";
		}

		// 清單7 ：放款與應收帳款-stage轉換用
		if (titaVo.get("MONTHLY6").equals("Y")) {
			this.info("L7902 active LNM39GP ");
//			MySpring.newTask("LNM39GP", this.txBuffer, titaVo);
			job += ";jLNM39GP";
		}

		// 清單8 ：放款與應收帳款-風險參數用
		if (titaVo.get("MONTHLY7").equals("Y")) {
			this.info("L7902 active LNM39HP ");
//			MySpring.newTask("LNM39HP", this.txBuffer, titaVo);
			job += ";jLNM39HP";
		}

		// 清單9 ：表外放款與應收帳款-資產基本資料與計算原始有效利率用
		if (titaVo.get("MONTHLY8").equals("Y")) {
			this.info("L7902 active LNM39IP ");
//			MySpring.newTask("LNM39IP", this.txBuffer, titaVo);
			job += ";jLNM39IP";
		}

		// 清單10：借新還舊
		if (titaVo.get("MONTHLY9").equals("Y")) {
			this.info("L7902 active LNM39JP ");
//			MySpring.newTask("LNM39JP", this.txBuffer, titaVo);
			job += ";jLNM39JP";
		}

		if (!job.equals("")) {
			this.info("=========== L7902 setBatchJobId : ");
			titaVo.setBatchJobId(job.substring(1));
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}