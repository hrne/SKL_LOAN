package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * 搬運資料
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("L6972")
@Scope("prototype")
public class L6972 extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	TxArchiveTableService txArchiveTableService;

	WorkType workType;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6972 ");
		this.totaVo.init(titaVo);

		if (!ContentName.onLine.equals(titaVo.getDataBase())) {
			throw new LogicException("E0008", "L6972 只允許在連線環境執行!!");
		}

		workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));
		int inputRoute = parse.stringToInteger(titaVo.getParam("InputRoute"));
		int custNo = parse.stringToInteger(titaVo.getParam("InputCustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("InputFacmNo"));
		int bormNo = parse.stringToInteger(titaVo.getParam("InputBormNo"));

		// pass the exceptions of USPs to upper levels
		switch (workType) {
		case FiveYearsTX:
			if (inputRoute == 0) // online to history
			{
				this.info("L6972 execute ArchiveFiveYearTx");
				txArchiveTableService.Usp_L6_ArchiveFiveYearTx_Copy(titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), titaVo);
			} else {
				this.info("L6972 execute UnarchiveFiveYearTx: " + custNo + "-" + facmNo + "-" + bormNo);
				txArchiveTableService.Usp_L6_UnarchiveFiveYearTx_Copy(custNo, facmNo, bormNo, titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), titaVo);
			}
			break;
		default:
			break;
		}

		this.info("L6972 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}
}
