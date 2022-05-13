package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.parse.Parse;

@Service("L8401")
@Scope("prototype")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L8401 extends TradeBuffer {

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("=========== L8401 run titaVo: " + titaVo);

		this.info("active L8401 ");
		this.totaVo.init(titaVo);
		String job = "";

		String acdatestart = titaVo.getParam("AcDateStart");
		String acdateend = titaVo.getParam("AcDateEnd");

		// 需同年月,若起訖有不同月份踢錯誤訊息(同一檔案中之交易日期須為相同年月份)
		if (titaVo.get("DAILY2").equals("Y")) {
			if (acdatestart.substring(0, 5).compareTo(acdateend.substring(0, 5)) != 0) {
				throw new LogicException("E0010", "B211 同一檔案中之交易日期須為相同年月份");
			}
		}

		// B204 聯徵授信餘額日報檔
		if (titaVo.get("DAILY1").equals("Y")) {
			this.info("L8401 active LB204 ");
			MySpring.newTask("LB204p", this.txBuffer, titaVo);
//			job += ";jLB204";
		}

		// B211 聯徵每日授信餘額變動資料檔
		if (titaVo.get("DAILY2").equals("Y")) {
			this.info("L8401 active LB211 ");
			MySpring.newTask("LB211p", this.txBuffer, titaVo);
//			job += ";jLB211";
		}

		if (!job.equals("")) {
			this.info("=========== L8401 setBatchJobId : ");
			titaVo.setBatchJobId(job.substring(1));
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}