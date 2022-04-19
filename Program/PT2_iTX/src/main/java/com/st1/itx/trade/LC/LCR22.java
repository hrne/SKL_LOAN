package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.CheckAuth;

@Service("LCR22")
@Scope("prototype")
/**
 * 
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public class LCR22 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LCR22.class);

	@Autowired
	public CheckAuth checkAuth;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LCR22 ");
		this.totaVo.init(titaVo);

//		S(XXR22,0,#KINBR,#TLRNO,#TXCD,#FKEY,#ACTFG,#END)R(1,XXR22)
		String iKINBR = titaVo.getParam("KINBR").trim();
		String iTLRNO = titaVo.getParam("TLRNO").trim();
		String iAGENT = titaVo.getParam("AGENT").trim();
		String iAUTHNO = titaVo.getParam("AUTHNO").trim();
		String iTXCD = titaVo.getParam("TXCD").trim();
		String iFKEY = titaVo.getParam("FKEY").trim();
		int iACTFG = Integer.parseInt(titaVo.getParam("ACTFG").trim());
		int iFUNCIND = Integer.parseInt(titaVo.getParam("FUNCIND").trim());

		if (!checkAuth.isCan(titaVo, iTLRNO, iAGENT, iAUTHNO, iTXCD, iACTFG, iFUNCIND)) {
			throw new LogicException("EC008", "經辦 [" + iTLRNO + "] 無交易 [" + iTXCD + "] 執行權限");
		}
		this.totaVo.putParam("TR_SECNO", "L");
		this.totaVo.putParam("TR_DBUCD", "1");
		this.totaVo.putParam("TR_DRELCD", "1");
		this.totaVo.putParam("TR_DABRNO", titaVo.getBrno());
		this.totaVo.putParam("TR_DFBRNO", titaVo.getBrno());
		this.totaVo.putParam("TR_PREFIX", "SKLC");
		this.totaVo.putParam("TR_OBUCD", "1");
		this.totaVo.putParam("TR_ORELCD", "1");
		this.totaVo.putParam("TR_OFBRNO", titaVo.getBrno());
		this.totaVo.putParam("TR_OPREFIX", "SKLC");

		this.addList(this.totaVo);
		return this.sendList();
	}
}