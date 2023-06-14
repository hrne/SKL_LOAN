package com.st1.itx.trade.L6;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.db.service.springjpa.cm.L9729ServiceImpl.WorkType;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 搬運資料(背景作業)
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("L6972p")
@Scope("prototype")
public class L6972p extends TradeBuffer {

	@Autowired
	Parse parse;

	@Autowired
	TxArchiveTableService txArchiveTableService;

	@Autowired
	SystemParasService systemParasService;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dateUtil;

	WorkType workType;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6972 ");
		this.totaVo.init(titaVo);

		if (!ContentName.onLine.equals(titaVo.getDataBase())) {
			throw new LogicException("E0008", "L6972 只允許在連線環境執行!!");
		}

		// 2023-06-13 Wei增加檢核
		// SKL IT 琦欣測試本交易時會重複發動，看不到每一次搬運資料的結果
		// 用資料庫管控，L6972若已經在運作中，則顯示"已在搬運中"的訊息
		holdFlag(titaVo);

		workType = WorkType.getWorkTypeByHelp(titaVo.getParam("InputType"));
		int inputRoute = parse.stringToInteger(titaVo.getParam("InputRoute"));
		int custNo = parse.stringToInteger(titaVo.getParam("InputCustNo"));
		int facmNo = parse.stringToInteger(titaVo.getParam("InputFacmNo"));
		int bormNo = parse.stringToInteger(titaVo.getParam("InputBormNo"));

		// pass the exceptions of USPs to upper levels
		switch (workType) {
		case FiveYearsTX:
			if (inputRoute == 0) { // online to history
				this.info("L6972 execute ArchiveFiveYearTx");
				txArchiveTableService.Usp_L6_ArchiveFiveYearTx_Copy(titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(),
						titaVo);
			} else {
				this.info("L6972 execute UnarchiveFiveYearTx: " + custNo + "-" + facmNo + "-" + bormNo);
				txArchiveTableService.Usp_L6_UnarchiveFiveYearTx_Copy(custNo, facmNo, bormNo,
						titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), titaVo);
			}
			break;
		default:
			break;
		}

		this.info("L6972 exit.");

		// 2023-06-13 Wei增加檢核
		// SKL IT 琦欣測試本交易時會重複發動，看不到每一次搬運資料的結果
		// 用資料庫管控，L6972若已經在運作中，則顯示"已在搬運中"的訊息
		releaseFlag(titaVo);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(),
				"L6972搬運資料已完成", titaVo);

		titaVo.putParam("InputType", 1);
		titaVo.putParam("InputDate", titaVo.getEntDyI());

		MySpring.newTask("L9729p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void holdFlag(TitaVo titaVo) throws LogicException {
		SystemParas systemParas = systemParasService.findById("LN", titaVo);
		String flag = systemParas.getL6972Flag();
		if (flag.equals("Y")) {
			throwException(titaVo);
		} else {
			systemParas = systemParasService.holdById("LN", titaVo);
			flag = systemParas.getL6972Flag();
			if (flag.equals("Y")) {
				throwException(titaVo);
			} else {
				systemParas.setL6972Flag("Y");
				try {
					systemParasService.update(systemParas, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0007", "SystemParas"); // 更新失敗
				}
			}
		}
	}

	private void throwException(TitaVo titaVo) throws LogicException {
		throw new LogicException("E0015", "已在搬運中"); // 檢查錯誤
	}

	private void releaseFlag(TitaVo titaVo) throws LogicException {
		SystemParas systemParas = systemParasService.holdById("LN", titaVo);
		systemParas.setL6972Flag("N");
		try {
			systemParasService.update(systemParas, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0007", "SystemParas"); // 更新失敗
		}
	}
}
