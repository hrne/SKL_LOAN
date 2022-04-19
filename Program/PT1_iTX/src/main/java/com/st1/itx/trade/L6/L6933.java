package com.st1.itx.trade.L6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxDataLog;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.TxDataLogService;
import com.st1.itx.db.service.TxTranCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Service("L6933")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L6933 extends TradeBuffer {
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
		this.info("active L6933 ");
		this.totaVo.init(titaVo);

		String TranNo = titaVo.getParam("TranNo").trim();
		Slice<TxDataLog> slTxDataLog = txDataLogService.findByTranNo(TranNo, titaVo.getParam("MrKey"), 0,
				Integer.MAX_VALUE, titaVo);
		List<TxDataLog> lTxDataLog = slTxDataLog == null ? null : slTxDataLog.getContent();

		boolean first = true;
		if (lTxDataLog != null && lTxDataLog.size() > 0) {
			for (TxDataLog txDataLog : lTxDataLog) {
				if (("L5701").equals(TranNo)) {
					List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
					try {
						this.info("txDataLog.getContent()=" + txDataLog.getContent());
						listMap = new ObjectMapper().readValue(txDataLog.getContent(), ArrayList.class);
					} catch (IOException e) {
						throw new LogicException("EC009", "資料格式");
					}
					boolean skip = true;
					for (HashMap<String, Object> map : listMap) {
						String fld = "";
						if(map.get("f") !=null) {
							fld = map.get("f").toString();
						}
						if ("延期繳款年月(起)".equals(fld) || "延期繳款年月(訖)".equals(fld)) {
							skip = false;
						}
					}
					if (skip) {
						continue;
					}
				}
				if (first) {
					first = false;
					this.totaVo.putParam("OCustNo", txDataLog.getCustNo());
					this.totaVo.putParam("OFacmNo", txDataLog.getFacmNo());
					this.totaVo.putParam("OBormNo", txDataLog.getBormNo());
					this.totaVo.putParam("OMrKey", txDataLog.getMrKey());

					String tranName = txDataLog.getTranNo();

					TxTranCode txTranCode = txTranCodeService.findById(txDataLog.getTranNo(), titaVo);
					if (txTranCode != null) {
						tranName += " " + txTranCode.getTranItem();
					}
					totaVo.putParam("OTranName", tranName);

				}
				OccursList occursList = new OccursList();

				occursList.putParam("OReason", txDataLog.getReason());

				String lastUpdate = parse.timeStampToString(txDataLog.getLastUpdate());

//				occursList.putParam("OLastUpdate", txDataLog.getLastUpdate().toString().substring(0, 16));
				occursList.putParam("OLastUpdate", lastUpdate);

				String lastEmp = txDataLog.getLastUpdateEmpNo();
				if (txDataLog.getLastUpdateEmpNo() != null && txDataLog.getLastUpdateEmpNo().toString().length() > 0) {
					CdEmp cdEmp = cdEmpService.findById(txDataLog.getLastUpdateEmpNo(), titaVo);
					if (cdEmp != null) {
						lastEmp += " " + StringCut.stringCut(cdEmp.getFullname(), 0, 10);
					}
				}

				occursList.putParam("OLastEmp", lastEmp);
				occursList.putParam("OTxDate", txDataLog.getTxDate());
				occursList.putParam("OTxSeq", txDataLog.getTxSeq());
				occursList.putParam("OTxSno", txDataLog.getTxSno());

				this.totaVo.addOccursList(occursList);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}