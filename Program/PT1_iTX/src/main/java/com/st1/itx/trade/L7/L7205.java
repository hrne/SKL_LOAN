package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;


@Service("L7205")
@Scope("prototype")
/**
 * 五類資產分類上傳轉檔作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L7205 extends TradeBuffer {


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7205 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L7205p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
		
	}

}