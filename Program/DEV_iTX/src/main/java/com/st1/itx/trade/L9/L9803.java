package com.st1.itx.trade.L9;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9803")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9803 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9803.class);

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9803 ");
		this.totaVo.init(titaVo);
		
		String tradeCode = "";
		String tradeName = "";
		String job = "";
		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));
		
		for (int i = 1; i <= totalItem; i++) {

			// if it's checked
			if (titaVo.getParam("BtnShell"+i).equals("V"))
			{
				tradeCode = titaVo.getParam("TradeCode"+i);
				tradeName = titaVo.getParam("TradeName"+i);
				// should it be
				// Y: Chained
				// N: run directly
				if (titaVo.getParam("TradeNTX"+i).equals("Y")) 
				{
					webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", tradeCode,
					titaVo.getParam("TLRNO"), tradeCode + tradeName +"須填寫查詢條件", titaVo);
				}
				else
				{
					this.info("L9803 adds job j"+ tradeCode +" peko");
					
					job += ";j" + tradeCode;

				}
			}
		}
			
		if (!job.equals("")) {
			titaVo.setBatchJobId(job.substring(1));
			
			// ;jL1111;jL2222...
			// hence 7
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), job.length() / 7 + " 支報表正在背景產製，完成後可於＂報表及製檔＂存取", titaVo);
		}
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}