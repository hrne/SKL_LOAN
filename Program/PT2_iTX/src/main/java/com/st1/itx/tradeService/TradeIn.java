package com.st1.itx.tradeService;

import java.util.ArrayList;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

/**
 * Trade Interface
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
public interface TradeIn {

	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException, DBException;

}
