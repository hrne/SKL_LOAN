package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxTeller;
import com.st1.itx.db.service.TxTellerService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.http.WebClient;

@Service("L5568")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class L5568 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5568.class);

	@Autowired
	public WebClient webClient;

	@Autowired
	public TxTellerService txTellerService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5568 ");
		this.totaVo.init(titaVo);

//		webClient.sendPost("20200707", "2300", "001746", "Y", "L1001", "123456", "55688123456TestTest", titaVo);
//		String jobId = "job1";
//		JobParameters params = new JobParametersBuilder().addDate("batch.date", new Date()).toJobParameters();
//		MySpring.jobLaunch(jobId, params);

//		titaVo.setBatchJobId("job01");

		TxTeller txTeller = txTellerService.findById("001746");
		if (txTeller.getLevelFg() == 1)
			txTeller.setLevelFg(3);
		else
			txTeller.setLevelFg(1);

		txTellerService.holdById(txTeller);
		try {
			txTellerService.update(txTeller);
		} catch (DBException e) {
			;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}