package com.st1.itx.trade.L6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.springjpa.cm.L6041ServiceImpl;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.menu.MenuBuilder;
import com.st1.itx.util.parse.Parse;

@Service("L6080")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L6080 extends TradeBuffer {

	
	/* DB服務注入 */
	@Autowired
	L6041ServiceImpl iL6041ServiceImpl;

	@Autowired
	CdEmpService cdEmpService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;

	@Autowired
	MenuBuilder menuBuilder;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6080 ");

		this.totaVo.init(titaVo);
		String iTlrNo = titaVo.getParam("TlrNo");

		List<Map<String, String>> sL6041ServiceImpl = null;
		sL6041ServiceImpl = iL6041ServiceImpl.findByTrolAll(iTlrNo, 0, Integer.MAX_VALUE, titaVo);

		if (sL6041ServiceImpl == null) {
			throw new LogicException("E0001", "");
		} else {
			for (Map<String, String> t : sL6041ServiceImpl) {
				OccursList occursList = new OccursList();
				occursList.putParam("OTlrNo", t.get("F0"));
				occursList.putParam("OAuthNo", t.get("F1"));

				String DateTime = this.parse.stringToStringDateTime(t.get("F2"));
				occursList.putParam("OLastUpdate", DateTime);

				String iEmpNo = t.get("F3");
				if (!iEmpNo.isEmpty() || iEmpNo.length() > 0) {
					CdEmp tCdEmp = cdEmpService.findById(iEmpNo, titaVo);
					if (tCdEmp != null) {
						iEmpNo = iEmpNo + " " + tCdEmp.getFullname();
					}
				}
				occursList.putParam("OLastUpdateEmpNo", iEmpNo);
				this.totaVo.addOccursList(occursList);
			}

		}
		// 清除選單快取 Adam
		menuBuilder.evict();

		this.addList(this.totaVo);
		return this.sendList();
	}
}
