package com.st1.itx.trade.L8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.JcicServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.JcicCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L8R10")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L8R10 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8R10.class);

	/* DB服務注入 */
	@Autowired
	public JcicServiceImpl jcicServiceImpl;
	@Autowired
	public JcicCom jcicCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L8R10");
		this.info("active L8R10 ");

		this.totaVo.init(titaVo);
		String iCustId = titaVo.getParam("RimCustId").trim();// 身分證字號
		String iRcDate = titaVo.getParam("RimRcDate").trim();// 協商申請日
		String iSubmitKey = titaVo.getParam("RimSubmitKey").trim();// 報送單位代號

		int iDcRcDate = Integer.parseInt(jcicCom.RocTurnDc(iRcDate, 0));

		this.info("L8R10 RimCustId=[" + iCustId + "],iRcDate=[" + iRcDate + "],iSubmitKey=[" + iSubmitKey + "]");

		String lL8r10tom[] = { "L8r10TranKey", "L8r10CustId", "L8r10RcDate", "L8r10SubmitKey", "L8r10RbDate", "L8r10ApplyType", "L8r10RefBankId", "L8r10NotBankId1", "L8r10NotBankId2",
				"L8r10NotBankId3", "L8r10NotBankId4", "L8r10NotBankId5", "L8r10NotBankId6", "L8r10OutJcicTxtDate", "L8r10SubmitKeyX", "L8r10RefBankIdX", "L8r10NotBankIdX1", "L8r10NotBankIdX2",
				"L8r10NotBankIdX3", "L8r10NotBankIdX4", "L8r10NotBankIdX5", "L8r10NotBankIdX6" };
		Map<String, String> queryKey = new HashMap<String, String>();
		queryKey.put("CustId", iCustId);
		queryKey.put("RcDate", String.valueOf(iDcRcDate));
		queryKey.put("SubmitKey", iSubmitKey);

		String sql = jcicServiceImpl.sqlJcic(titaVo, "L8R10");
		List<String[]> data = jcicServiceImpl.FindJcic(0, 1, sql, queryKey, titaVo);
		String thisData[] = new String[lL8r10tom.length];
		if (data != null) {
			thisData = data.get(0);
		}
		for (int i = 0; i < lL8r10tom.length; i++) {
			if (lL8r10tom[i].equals("L8r10TranKey")) {
				thisData[i] = jcicCom.changeTranKey(thisData[i]);
			} else {
				if (lL8r10tom[i].equals("L8r10RcDate") || lL8r10tom[i].equals("L8r10RbDate") || lL8r10tom[i].equals("L8r10OutJcicTxtDate")) {
					if (thisData[i] != null && thisData[i].length() != 0) {
						thisData[i] = jcicCom.DcToRoc(thisData[i], 0);

					} else {
						thisData[i] = "0000000";
					}
				}
			}

			totaVo.putParam(lL8r10tom[i], thisData[i]);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}