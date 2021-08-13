package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8402")
@Scope("prototype")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L8402 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8402.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("=========== L8402 run titaVo: " + titaVo);

		this.info("active L8402 ");
		this.totaVo.init(titaVo);
		String job = "";

		// B201 聯徵授信餘額月報檔
		if (titaVo.get("MONTHLY1").equals("Y")) {
			this.info("L8402 active LB201 ");
//			MySpring.newTask("LB201", this.txBuffer, titaVo);
			job += ";jLB201";
		}

		// B207 授信戶基本資料檔
		if (titaVo.get("MONTHLY2").equals("Y")) {
			this.info("L8402 active LB207 ");
//			MySpring.newTask("LB207", this.txBuffer, titaVo);
			job += ";jLB207";
		}

		// B080 授信額度資料檔
		if (titaVo.get("MONTHLY3").equals("Y")) {
			this.info("L8402 active LB080 ");
//			MySpring.newTask("LB080", this.txBuffer, titaVo);
			job += ";jLB080";
		}

		// B085 帳號轉換資料檔
		if (titaVo.get("MONTHLY4").equals("Y")) {
			this.info("L8402 active LB085 ");
//			MySpring.newTask("LB085", this.txBuffer, titaVo);
			job += ";jLB085";
		}

		// B087 聯貸案首次動撥後６個月內發生違約之實際主導金融機構註記檔
		if (titaVo.get("MONTHLY5").equals("Y")) {
			this.info("L8402 active LB087 ");
//			MySpring.newTask("LB087", this.txBuffer, titaVo);
			job+=";jLB087";
		}

		// B090 擔保品關聯檔資料檔
		if (titaVo.get("MONTHLY6").equals("Y")) {
			this.info("L8402 active LB090 ");
//			MySpring.newTask("LB090", this.txBuffer, titaVo);
			job += ";jLB090";
		}

		// B092 不動產擔保品明細檔
		if (titaVo.get("MONTHLY7").equals("Y")) {
			this.info("L8402 active LB092 ");
//			MySpring.newTask("LB092", this.txBuffer, titaVo);
			job += ";jLB092";
		}

		// B093 動產及貴重物品擔保品明細檔
		if (titaVo.get("MONTHLY8").equals("Y")) {
			this.info("L8402 active LB093 ");
//			MySpring.newTask("LB093", this.txBuffer, titaVo);
			job += ";jLB093";
		}

		// B094 股票擔保品明細檔
		if (titaVo.get("MONTHLY9").equals("Y")) {
			this.info("L8402 active LB094 ");
//			MySpring.newTask("LB094", this.txBuffer, titaVo);
			job += ";jLB094";
		}

		// B095 不動產擔保品明細-建號附加檔
		if (titaVo.get("MONTHLY10").equals("Y")) {
			this.info("L8402 active LB095 ");
//			MySpring.newTask("LB095", this.txBuffer, titaVo);
			job += ";jLB095";
		}

		// B096 不動產擔保品明細-地號附加檔
		if (titaVo.get("MONTHLY11").equals("Y")) {
			this.info("L8402 active LB096 ");
//			MySpring.newTask("LB096", this.txBuffer, titaVo);
			job += ";jLB096";
		}

		// B680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
		if (titaVo.get("MONTHLY12").equals("Y")) {
			this.info("L8402 active LB680 ");
//			MySpring.newTask("LB680", this.txBuffer, titaVo);
			job += ";jLB680";
		}

		if (!job.equals("")) {
			this.info("=========== L8402 setBatchJobId : ");
			titaVo.setBatchJobId(job.substring(1));
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}