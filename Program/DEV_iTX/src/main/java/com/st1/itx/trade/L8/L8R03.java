package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;

@Service("L8R03")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R03 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R03.class);
	@Autowired
	public JcicCom jcicCom;
	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8R03");
		this.totaVo.init(titaVo);
		String JcicCourtZipCode = titaVo.getParam("RimJcicCourtZipCode");//Jcic法院或郵遞區號代碼
		this.info("L8R03 JcicCourtZipCode=["+JcicCourtZipCode+"]");
		String JcicCourtZipName=jcicCom.JcicCourtCodeAndZipCode(JcicCourtZipCode,titaVo);//80碼長度
		
		this.totaVo.putParam("L8r03JcicCourtZipName", JcicCourtZipName);
		this.addList(this.totaVo);
		return this.sendList();
	}
}