package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4600ServiceImpl;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.InsuRenewFileVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

/**
 * L4601Report2 輸出檔案：險種不足 LNM01P
 *
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
public class L4602Report2 extends MakeReport {

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
	public CdCodeService cdCodeService;

	public void exec(TitaVo titaVo, InsuRenewFileVo insuRenewFileVo) throws LogicException {

		this.info("L4602Report2 exec start");

		// 轉換資料格式

		ArrayList<String> file = insuRenewFileVo.toFile();

		if (file.isEmpty()) {
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4602", "L4602火險到期檔查無資料", titaVo);
		} else {

			ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno())
					.setRptCode(titaVo.getTxCode()).setRptItem("LNM01P" + "(火險到期檔)").build();

			makeFile.open(titaVo, reportVo, "LNM01P.txt", 2);

			for (String line : file) {
				makeFile.put(line);
			}

			long sno = makeFile.close();

			this.info("sno : " + sno);
			makeFile.toFile(sno);

			// TXT製作完成，發MESSAGE
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4602", "L4602火險到期檔LNM01P.txt已完成", titaVo);
		}

	}

}
