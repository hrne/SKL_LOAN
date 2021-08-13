package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.CdBonusService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.db.service.PfRewardService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.data.DataLog;
/**
 * Tita<br>
 */

@Service("L5512")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5512 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L5512.class);
	@Autowired
	public PfItDetailService sPfItDetailService;

	@Autowired
	public PfRewardService sPfRewardService;
	@Autowired
	public CdEmpService sCdEmpService;
	@Autowired
	public CdBonusService sCdBonusService;
	@Autowired
	public L5051ServiceImpl l5051ServiceImpl;

	@Autowired
	public MakeFile makeFile;
	@Autowired
	public DataLog dataLog;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5512 ");
		this.totaVo.init(titaVo);
		
//		MySpring.newTask("BS512", this.txBuffer, titaVo);
		MySpring.newTask("L5512Batch", this.txBuffer, titaVo);
		
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}