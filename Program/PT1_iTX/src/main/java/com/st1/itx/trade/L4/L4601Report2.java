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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewMediaTemp;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.InsuRenewMediaTempService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4600ServiceImpl;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * 輸出檔案：險種不足LN5811P
 * 
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

	@Autowired
	Parse parse;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public InsuRenewMediaTempService insuRenewMediaTempService;

	@Autowired
	public CdCodeService cdCodeService;

	String fileName = "LN5811P.csv";

	private static final String fileHeader = "到期年月,戶號,額度,姓名,押品,序號,保單號碼,提供人,保險起日,保險迄日,火險保額,地震險保額,坪數(含建築物、公設等),建議火險保額 (e-loan),備註, 險種註記";

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("L4601Report2 exec start");

		List<String> file = getData(titaVo);

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno())
				.setRptCode(titaVo.getTxCode()).setRptItem("LN5811P(保單險種不足明細表)").build();

		makeFile.open(titaVo, reportVo, fileName, 2);

		for (String line : file) {
			makeFile.put(line);
		}

		makeFile.close();

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L4601", titaVo.getTxCode() + " 已產生LN5811P.CSV", titaVo);
	}

	private List<String> getData(TitaVo titaVo) throws LogicException {

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

			InsuRenewMediaTemp iInsuRenewMediaTemp = new InsuRenewMediaTemp();
			iInsuRenewMediaTemp = insuRenewMediaTempService.fireInsuFirst(m.get("InsuYearMonth"), m.get("ClCode1"),
					m.get("ClCode2"), m.get("ClNo"), m.get("PrevInsuNo"), titaVo);

			String line = "";
			line += m.get("InsuYearMonth");
			line += ",";
			line += m.get("CustNo");
			line += ",";
			line += m.get("FacmNo");
			line += ",";
			line += m.get("CustName");
			line += ",";
			line += m.get("ClCode1") + "-" + m.get("ClCode2") + "-" + m.get("ClNo");
			line += ",";
			line += iInsuRenewMediaTemp.getSeq().isEmpty()? " " : iInsuRenewMediaTemp.getSeq();
			line += ",";
			line += m.get("PrevInsuNo");
			line += ",";
			line +=  iInsuRenewMediaTemp.getInsuCustName().isEmpty()? " " :iInsuRenewMediaTemp.getInsuCustName();
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
			line += ",";

			// 險種註記
			InsuRenew tInsuRenew = insuRenewService.prevInsuNoFirst(parse.stringToInteger(m.get("CustNo").trim()),
					parse.stringToInteger(m.get("FacmNo").trim()), m.get("PrevInsuNo").trim(), titaVo);

			CdCode tCdCode = cdCodeService.findById(new CdCodeId("CommericalFlag", tInsuRenew.getCommericalFlag()),
					titaVo);
			String CommericalFlagX = "";
			if (tCdCode != null) {
				CommericalFlagX = tCdCode.getItem();
			}

			line += CommericalFlagX;

			this.info(line);

			// 加入明細
			result.add(line);
		}

		return result;
	}

}
