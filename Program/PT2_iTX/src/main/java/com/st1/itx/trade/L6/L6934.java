package com.st1.itx.trade.L6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxDataLogId;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Service("L6934")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6934 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public TxDataLogService txDataLogService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6934 ");
		this.totaVo.init(titaVo);
		TxDataLogId txDataLogId = new TxDataLogId();
		int txDate = Integer.valueOf(titaVo.getParam("TxDate"));
		String txSeq = titaVo.getParam("TxSeq");
		int txSno = Integer.valueOf(titaVo.getParam("TxSno"));
		txDataLogId.setTxDate(txDate);
		txDataLogId.setTxSeq(txSeq);
		txDataLogId.setTxSno(txSno);
		TxDataLog txDataLog = txDataLogService.findById(txDataLogId, titaVo);
		if (txDataLog == null) {
			throw new LogicException(titaVo, "E0001", "");
		}
		String tranName = txDataLog.getTranNo();
		TxTranCode txTranCode = txTranCodeService.findById(txDataLog.getTranNo(), titaVo);
		if (txTranCode != null) {
			tranName += " " + txTranCode.getTranItem();
		}
		totaVo.putParam("OTranName", tranName);
		this.totaVo.putParam("OCustNo", txDataLog.getCustNo());
		this.totaVo.putParam("OFacmNo", txDataLog.getFacmNo());
		this.totaVo.putParam("OBormNo", txDataLog.getBormNo());
		this.totaVo.putParam("OMrKey", txDataLog.getMrKey());
		totaVo.putParam("OReason", txDataLog.getReason());
		String lastUpdate = parse.timeStampToString(txDataLog.getLastUpdate());
		totaVo.putParam("OLastUpdate", lastUpdate);
		String lastEmp = txDataLog.getLastUpdateEmpNo();
		if (txDataLog.getLastUpdateEmpNo() != null && txDataLog.getLastUpdateEmpNo().toString().length() > 0) {
			CdEmp cdEmp = cdEmpService.findById(txDataLog.getLastUpdateEmpNo(), titaVo);
			if (cdEmp != null) {
				lastEmp += " " + StringCut.stringCut(cdEmp.getFullname(), 0, 10);
			}
		}
		totaVo.putParam("OLastEmp", lastEmp);
		List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
		try {
			this.info("txDataLog.getContent()=" + txDataLog.getContent());
			listMap = new ObjectMapper().readValue(txDataLog.getContent(), ArrayList.class);
		} catch (IOException e) {
			throw new LogicException("EC009", "資料格式");
		}
		for (HashMap<String, Object> map : listMap) {
			String fld = "";
			if (map.get("f") != null) {
				fld = map.get("f").toString();
			} else {
				continue;
			}
			String oval = map.get("o").toString();

			String nval = map.get("n").toString();
			// 去除金額.00處理
			if (oval.length() > 3) {
				if (".00".equals(oval.substring(oval.length() - 3, oval.length()))) {
					oval = oval.substring(0, oval.length() - 3);
				}
			}
			if (nval.length() > 3) {
				if (".00".equals(nval.substring(nval.length() - 3, nval.length()))) {
					nval = nval.substring(0, nval.length() - 3);
				}
			}
			// 排除變更項目時需同時修正L9136Report
			// 系統自動更新
			if ("最後更新人員".equals(fld) || "交易進行記號".equals(fld) || "上次櫃員編號".equals(fld) || "上次交易序號".equals(fld)
					|| "已編BorTx流水號".equals(fld) || "最後更新日期時間".equals(fld) || "上次會計日".equals(fld) || "會計日期".equals(fld)
					|| "上次交易行別".equals(fld) || "上次交易日".equals(fld)) {
				continue;
			}
			// 系統控制用
			if ("L8203".equals(txDataLog.getTranNo())) {
				if ("流程控制序號".equals(fld) || "流程控制帳務日".equals(fld)) {
					continue;
				}
			}
			// 額度維護時個別加碼是系統自動計算,非人工修改
			if ("L2154".equals(txDataLog.getTranNo())) {
				if ("個別加碼".equals(fld)) {
					continue;
				}
			}
			// L2606帳管費維護
			if ("L2606".equals(txDataLog.getTranNo())) {
				if ("業務科目代號加碼".equals(fld) || "科目代號".equals(fld) || "子目代號".equals(fld) || "細目代號".equals(fld)
						|| "單位別".equals(fld) || "銷帳記號".equals(fld) || "業務科目記號".equals(fld) || "銷帳科目記號".equals(fld)
						|| "起帳金額".equals(fld) || "會計日餘額".equals(fld) || "帳冊別".equals(fld) || "區隔帳冊".equals(fld)
						|| "起帳交易代號".equals(fld) || "起帳單位別".equals(fld) || "起帳經辦".equals(fld) || "起帳交易序號".equals(fld)
						|| "上次作帳日".equals(fld) || "最後交易日".equals(fld) || "交易代號".equals(fld) || "單位別".equals(fld)
						|| "經辦".equals(fld) || "交易序號".equals(fld) || "jason格式紀錄欄".equals(fld)) {
					continue;
				}

			}

			this.info(fld + " = " + oval + " to " + nval);
//			if (iChainFlag == 1 && ("L5701").equals(TranNo)) { // L5701 喘息期歷程特殊需求
//				if ("延期繳款年月(起)".equals(fld) || "延期繳款年月(訖)".equals(fld)) {
//
//				} else {
//					continue;
//				}
//			}

			/* 將每筆資料放入Tota的OcList */

			OccursList occursList = new OccursList();

			occursList.putParam("OColumnName", fld);
			occursList.putParam("ODataBefore", oval);
			occursList.putParam("ODataAfter", nval);

			this.totaVo.addOccursList(occursList);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

}