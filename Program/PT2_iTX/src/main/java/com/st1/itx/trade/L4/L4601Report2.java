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
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L4601Report2 <BR>
 * [L4600火險到期檔產生作業]產生LN5811P.CSV <BR>
 * 2021-11-09 智偉修改 <BR>
 * 原本在L4600產生 <BR>
 * 改為在L4602產生,並將報表程式改名
 * 
 * @author Chih Wei
 *
 */
@Component
@Scope("prototype")
public class L4601Report2 extends MakeReport {

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

	String fileName = "LN5811P.csv";

	private static final String fileHeader = "到期年月,戶號,額度,姓名,保單號碼,保險起日,保險迄日,火險保額,地震險保額,坪數(含建築物、公設等),建議火險保額 (e-loan),備註";

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4601Report2 exec start");

		List<String> file = getData(titaVo);

		
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno())
				.setRptCode(titaVo.getTxCode()).build();

		makeFile.open(titaVo, reportVo, fileName, 2);
		
//		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
//				titaVo.getTxCode() + "-LN5811P.CSV", fileName, 2);

		for (String line : file) {
			makeFile.put(line);
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);

		makeFile.toFile(sno);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo()+"L4602",
				titaVo.getTxCode() + " 已產生LN5811P.CSV", titaVo);
	}

	private List<String> getData(TitaVo titaVo) {

		this.info("L4601Report2 getData start");

		List<String> result = new ArrayList<>();

		// 加入表頭
		result.add(fileHeader);

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

			// 加入明細
			result.add(line);
		}

		return result;
	}
}
