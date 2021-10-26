package com.st1.itx.trade.LM;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM004ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component("lM004Report")
@Scope("prototype")

public class LM004Report extends MakeReport {

	@Autowired
	public LM004Report1 lm004report1;

	@Autowired
	public LM004Report2 lm004report2;

	@Autowired
	public LM004ServiceImpl lM004ServiceImpl;

	public boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM004List = null;
		try {
			LM004List = lM004ServiceImpl.findAll(titaVo, "pdf");
		} catch (Exception e) {
			this.info("lM004ServiceImpl.findAll error = " + e.toString());
		}

		List<Map<String, String>> LM004List_Excel = null;
		try {
			LM004List_Excel = lM004ServiceImpl.findAll(titaVo, "excel");
		} catch (Exception e) {
			this.info("lM004ServiceImpl.findAll_Excel error = " + e.toString());
		}
		this.getParentTranCode();
		lm004report1.setParentTranCode(this.getParentTranCode());
		lm004report2.setParentTranCode(this.getParentTranCode());
		this.info("LM004List--->" + LM004List.toString());
		this.info("LM004List_Excel--->" + LM004List_Excel.toString());
		lm004report1.exec(titaVo, LM004List);
		lm004report2.exec(titaVo, LM004List_Excel);

		return true;
	}

}
