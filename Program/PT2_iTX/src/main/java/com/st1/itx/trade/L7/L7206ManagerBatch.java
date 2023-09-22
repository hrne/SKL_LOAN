package com.st1.itx.trade.L7;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.L7206Manager;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.service.L7206ManagerService;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.SftpClient;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;

/**
 * 撈取、產製並上傳檔案 利害關係人_負責人 LNM63H2P.txt
 * 
 * @author Wei
 * @version 1.0.0
 */
@Service("L7206ManagerBatch")
@Scope("prototype")
public class L7206ManagerBatch extends TradeBuffer {

	@Autowired
	private DateUtil dateUtil;

	@Autowired
	private L7206ManagerService dbService;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private SftpClient sftpClient;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	private SystemParasService systemParasService;

	private String comma = ",";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7206ManagerBatch");
		this.totaVo.init(titaVo);

		String empNo = titaVo.getTlrNo();

		// 執行預存程序 Usp_L7_L7206Manager_Ins
		dbService.Usp_L7_L7206Manager_Ins(empNo, titaVo);

		// 產製檔案 LNM63H2P.txt
		String fileName = createFile(titaVo);

		// 上傳檔案 LNM63H2P.txt
		sendToFTP(fileName, titaVo);

		// 刪除本地檔案 LNM63H2P.txt
		deleteLocalFile(fileName);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String createFile(TitaVo titaVo) throws LogicException {

		int reportDate = dateUtil.getNowIntegerForBC();
		String brno = titaVo.getBrno();
		String txcd = "L7206";
		String fileItem = "L7206Manager";
		String fileName = "LNM63H2P.txt";
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 撈取資料表 L7206Manager
		Slice<L7206Manager> slice = dbService.findAll(0, Integer.MAX_VALUE, titaVo);

		if (slice != null) {
			// 開啟新檔 LNM63H2P.txt
			makeFile.open(titaVo, reportVo, fileName, 1); // 1: UTF8

			List<L7206Manager> list = slice.toList();

			for (L7206Manager data : list) {
				StringBuilder sb = new StringBuilder();

				// CustId 補空白補足10碼
				String custId = String.format("%-10s", data.getCustId());

				// CustName 補空白補足42碼 (特殊計算,半形字算1碼,全形字算2碼,不是直接用bytes算,UTF8的bytes長度不固定)
				String custName = padStringWithFullAndHalfWidth(data.getCustName(), 42);
				
				// ManagerId 補空白補足10碼
				String managerId = String.format("%-10s", data.getManagerId());

				// CustNo 前補零補足7碼
				String custNo = String.format("%07d", data.getCustNo());

				// FacmNo 前補零補足3碼
				String facmNo = String.format("%03d", data.getFacmNo());

				// DataMonth 前補零補足6碼
				String dataMonth = String.format("%06d", data.getDataMonth());

				// AvgLineAmt 前補零補足15碼
				String avgLineAmt = String.format("%015d",
						data.getAvgLineAmt().setScale(0, RoundingMode.DOWN).longValue());

				// SumLoanBal 前補零補足15碼
				String sumLoanBal = String.format("%015d",
						data.getSumLoanBal().setScale(0, RoundingMode.DOWN).longValue());

				// 使用StringBuilder組合以上格式化的字串
				sb.append(custId).append(comma);
				sb.append(custName).append(comma);
				sb.append(managerId).append(comma);
				sb.append(custNo).append(comma);
				sb.append(facmNo).append(comma);
				sb.append(dataMonth).append(comma);
				sb.append(avgLineAmt).append(comma);
				sb.append(sumLoanBal);

				String output = sb.toString();

				this.info("output = " + output);

				// 寫入檔案 LNM63H2P.txt
				makeFile.put(output);
			}

			long fileNo = makeFile.close();

			makeFile.toFile(fileNo, fileName);
		}
		return fileName;
	}

	public String padStringWithFullAndHalfWidth(String input, int targetLength) {
		int length = 0;
		for (char c : input.toCharArray()) {
			if (String.valueOf(c).getBytes(StandardCharsets.UTF_8).length == 1) {
				length++; // 半形字符
			} else {
				length += 2; // 全形字符
			}
		}

		int paddingNeeded = targetLength - length;
		for (int i = 0; i < paddingNeeded; i++) {
			input += " "; // 補充半形空白
		}
		return input;
	}

	private boolean sendToFTP(String fileName, TitaVo titaVo) throws LogicException {
		SystemParas systemParas = systemParasService.findById("LN", titaVo);

		if (systemParas == null) {
			this.error("sendToFTP: SystemParas doesn't exist !?");
			return false;
		}

		this.info("sendToFTP sending to FTP...");

		String url = systemParas.getL7206SftpUrl();
		String port = "22"; // 預設22
		if (url.contains(":")) {
			String[] s = url.split(":");
			url = s[0];
			port = s[1];
		}
		String[] auth = systemParas.getSmsFtpAuth().split(":");
		Path pathToFile = Paths.get(outFolder, fileName);

		// 呼叫SFTPCient
		return sftpClient.upload(url, port, auth, pathToFile, "L7206", titaVo);
	}

	private void deleteLocalFile(String fileName) {
		Path pathToFile = Paths.get(outFolder, fileName);
		if (Files.exists(pathToFile)) {
			try {
				Files.delete(pathToFile);
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.mustInfo("ERROR " + errors.toString());
			}
		}
	}
}
