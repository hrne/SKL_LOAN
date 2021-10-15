package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L4600ServiceImpl;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L4600Report <BR>
 * 
 * @author Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L4600Report extends MakeReport {

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	DateUtil dateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	MakeFile makeFile;

	@Autowired
	L4600ServiceImpl l4600ServiceImpl;

	// [L4600火險到期檔產生作業]產生LN5811P.CSV
	String fileName = "LN5811P.csv";

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4600Report exec start");

		List<String> file = getData(titaVo);

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + "-LN5811P.CSV", fileName, 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				"L4600 已產生LN5811P.CSV", titaVo);
	}

	private List<String> getData(TitaVo titaVo) {

		this.info("L4600Report getData start");

		List<String> result = new ArrayList<>();

		List<Map<String, String>> list = null;

		try {
			list = l4600ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error(e.getMessage());
		}

		if (list == null) {
			return result;
		}

		for (Map<String, String> m : list) {

			String line = "";
			line += m.get("InsuYearMonth");
			line += ",";
			line += m.get("CustNo");
			line += ",";
			line += m.get("FacmNo");
			line += ",";
			line += m.get("CustName");
			line += ",";
			line += m.get("PrevInsuNo");
			line += ",";
			line += m.get("InsuStartDate");
			line += ",";
			line += m.get("InsuEndDate");
			line += ",";
			line += m.get("FireInsuCovrg");
			line += ",";
			line += m.get("EthqInsuCovrg");
			line += ",";
			line += m.get("TotalArea");
			line += ",";
			line += m.get("AdviseFireInsuCovrg");
			line += ",";
			line += m.get("Remark");

			this.info(line);

			result.add(line);
		}

		return result;
	}
}
