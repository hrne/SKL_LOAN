package com.st1.itx.trade.L7;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FinHoldRel;
import com.st1.itx.db.domain.FinHoldRelId;
import com.st1.itx.db.domain.LifeRelEmp;
import com.st1.itx.db.domain.LifeRelEmpId;
import com.st1.itx.db.domain.LifeRelHead;
import com.st1.itx.db.domain.LifeRelHeadId;
import com.st1.itx.db.service.FinHoldRelService;
import com.st1.itx.db.service.LifeRelEmpService;
import com.st1.itx.db.service.LifeRelHeadService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * 人壽利關人上傳作業-以 FTP 檔案更新資料庫
 * 
 * @author Wei
 * @version 1.0.0
 */
@Service("L7206p")
@Scope("prototype")
public class L7206p extends TradeBuffer {

	@Autowired
	private WebClient webClient;

	@Autowired
	private DateUtil dateUtil;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Autowired
	private FileCom fileCom;

	@Autowired
	private LifeRelHeadService lifeRelHeadService;

	@Autowired
	private LifeRelEmpService lifeRelEmpService;

	@Autowired
	private FinHoldRelService finHoldRelService;

	private boolean isError = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7206p 以 FTP 檔案更新資料庫 ");
		this.totaVo.init(titaVo);

		sendMsg("L7206 以 FTP 檔案更新資料庫 背景作業中", titaVo);

		readFtpFilesAndUpdateDatabase(titaVo);

		if (!isError) {
			sendMsg("L7206 以 FTP 檔案更新資料庫 已完成", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void readFtpFilesAndUpdateDatabase(TitaVo titaVo) throws LogicException {
		// T07_YYYYMMDD
		// T07_2_YYYYMMDD
		// T044_YYYYMMDD
		// TA07_YYYYMMDD
		// 每日金控下傳一次
		String ftpFolder = inFolder += "/FTP/L7206/";

		int acDate = titaVo.getEntDyI() + 19110000;

		this.info("acDate = " + acDate);

		String t07FileName = ftpFolder + "T07_" + acDate + ".csv"; // 寫入 LifeRelHead
		String t072FileName = ftpFolder + "T07_2" + acDate + ".csv"; // 寫入 LifeRelEmp
		String t044FileName = ftpFolder + "T44_" + acDate + ".csv"; // 寫入 FinHoldRel

		List<String> t07List = readFtpFiles(t07FileName, titaVo);
		if (!t07List.isEmpty()) {
			moveAndInsertLifeRelHead(acDate, t07List, titaVo);
		}

		List<String> t072List = readFtpFiles(t072FileName, titaVo);
		if (!t072List.isEmpty()) {
			moveAndInsertLifeRelEmp(acDate, t072List, titaVo);
		}

		List<String> t044List = readFtpFiles(t044FileName, titaVo);
		if (!t044List.isEmpty()) {
			moveAndInsertFinHoldRel(acDate, t044List, titaVo);
		}
	}

	private void moveAndInsertLifeRelHead(int acDate, List<String> t07List, TitaVo titaVo) throws LogicException {
		// 跳過第0筆標題,所以從1開始
		for (int i = 1; i < t07List.size(); i++) {
			String t07Data = t07List.get(i);
			this.info("t07Data = " + t07Data);
			// 列號,與本公司之關係,關係人代號,關係人名稱,關係人職稱,
			// 關係人親屬代號,關係人親屬姓名,關係人親屬親等,關係人親屬稱謂,
			// 事業代號,事業名稱,事業持股比率％,事業擔任職務,備註放款金額
			String[] columns = t07Data.split(",");
			if (columns != null && columns.length == 14) {
				LifeRelHead lifeRelHead = new LifeRelHead();
				LifeRelHeadId lifeRelHeadId = new LifeRelHeadId();
				lifeRelHeadId.setAcDate(acDate);
				lifeRelHeadId.setHeadId(columns[2]); // 關係人代號
				lifeRelHeadId.setRelId(columns[5]); // 關係人親屬代號
				lifeRelHeadId.setBusId(columns[9]); // 事業代號

				lifeRelHead.setLifeRelHeadId(lifeRelHeadId);
				lifeRelHead.setAcDate(acDate);
				lifeRelHead.setRelWithCompany(columns[1]); // 與本公司之關係
				lifeRelHead.setHeadId(columns[2]); // 關係人代號
				lifeRelHead.setHeadName(columns[3]); // 關係人名稱
				lifeRelHead.setHeadTitle(columns[4]); // 關係人職稱
				lifeRelHead.setRelId(columns[5]); // 關係人親屬代號
				lifeRelHead.setRelName(columns[6]); // 關係人親屬姓名
				lifeRelHead.setRelKinShip(columns[7]); // 關係人親屬親等
				lifeRelHead.setRelTitle(columns[8]); // 關係人親屬稱謂
				lifeRelHead.setBusId(columns[9]); // 事業代號
				lifeRelHead.setBusName(columns[10]); // 事業名稱
				lifeRelHead.setShareHoldingRatio(toInt(columns[11])); // 事業持股比率％
				lifeRelHead.setBusTitle(columns[12]); // 事業擔任職務
				lifeRelHead.setLoanBalance(toBigDecimal(columns[13])); // 備註放款金額

				try {
					lifeRelHeadService.insert(lifeRelHead, titaVo);
				} catch (Exception e) {
					String errorMsg = " L7206 T07 寫入資料錯誤,列號:" + columns[0];
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					sendErrorMsg("E0005" + errorMsg, titaVo);
				}
			}
		}
	}

	private void moveAndInsertLifeRelEmp(int acDate, List<String> t072List, TitaVo titaVo) throws LogicException {
		// 跳過第0筆標題,所以從1開始
		for (int i = 1; i < t072List.size(); i++) {
			String t072Data = t072List.get(i);
			this.info("t072Data = " + t072Data);
			// 列號,與本公司之關係,關係人代號,關係人名稱,關係人職稱,
			// 關係人親屬代號,關係人親屬姓名,關係人親屬親等,關係人親屬稱謂,
			// 事業代號,事業名稱,事業持股比率％,事業擔任職務,備註放款金額
			String[] columns = t072Data.split(",");
			if (columns != null && columns.length == 14) {
				LifeRelEmp lifeRelEmp = new LifeRelEmp();
				LifeRelEmpId lifeRelEmpID = new LifeRelEmpId();
				lifeRelEmpID.setAcDate(acDate);
				lifeRelEmpID.setEmpId(columns[2]); // 關係人代號

				lifeRelEmp.setLifeRelEmpId(lifeRelEmpID);
				lifeRelEmp.setAcDate(acDate);
				lifeRelEmp.setEmpId(columns[2]); // 關係人代號
				lifeRelEmp.setEmpName(columns[3]); // 關係人名稱
				lifeRelEmp.setLoanBalance(toBigDecimal(columns[13])); // 備註放款金額

				try {
					lifeRelEmpService.insert(lifeRelEmp, titaVo);
				} catch (Exception e) {
					String errorMsg = " L7206 T07_2 寫入資料錯誤,列號:" + columns[0];
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					sendErrorMsg("E0005" + errorMsg, titaVo);
				}
			}
		}
	}

	private void moveAndInsertFinHoldRel(int acDate, List<String> t044List, TitaVo titaVo) throws LogicException {
		// 跳過第0筆標題,所以從1開始
		for (int i = 1; i < t044List.size(); i++) {
			String t044Data = t044List.get(i);
			this.info("t044Data = " + t044Data);
			// 所在公司,身分證,姓名,職務,放款金額餘額
			String[] columns = t044Data.split(",");
			if (columns != null && columns.length == 14) {
				FinHoldRel finHoldRel = new FinHoldRel();
				FinHoldRelId finHoldRelId = new FinHoldRelId();
				finHoldRelId.setId(columns[1]); // 身分證
				finHoldRelId.setAcDate(acDate);
				finHoldRelId.setCompanyName(columns[0]); // 所在公司

				finHoldRel.setFinHoldRelId(finHoldRelId);
				finHoldRel.setAcDate(acDate);
				finHoldRel.setCompanyName(columns[0]); // 所在公司
				finHoldRel.setId(columns[1]); // 身分證
				finHoldRel.setName(columns[2]); // 姓名
				finHoldRel.setBusTitle(columns[3]); // 職務
				finHoldRel.setLoanBalance(toBigDecimal(columns[4])); // 放款金額餘額

				try {
					finHoldRelService.insert(finHoldRel, titaVo);
				} catch (Exception e) {
					String errorMsg = " L7206 T044 寫入資料錯誤,第" + i + "筆";
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					this.error(errors.toString());
					sendErrorMsg("E0005" + errorMsg, titaVo);
				}
			}
		}
	}

	private BigDecimal toBigDecimal(String s) {
		this.info("toBigDecimal s = " + s);
		BigDecimal i = BigDecimal.ZERO;
		try {
			i = new BigDecimal(s);
		} catch (Exception e) {
			// 轉換錯誤則給0
			i = BigDecimal.ZERO;
		}
		return i;
	}

	private int toInt(String s) {
		this.info("toInt s = " + s);
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			// 轉換錯誤則給0
			i = 0;
		}
		return i;
	}

	private List<String> readFtpFiles(String fileName, TitaVo titaVo) {
		this.info("readFtpFiles fileName = " + fileName);
		List<String> dataLineList = new ArrayList<>();
		try {
			dataLineList = fileCom.intputTxt(fileName, "big5");
		} catch (Exception e) {
			String errorMsg = " L7206 檔案不存在,請查驗路徑.\r\n" + fileName;
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			sendErrorMsg("E0014" + errorMsg, titaVo);
			return new ArrayList<>();
		}
		return dataLineList;
	}

	private void sendMsg(String msg, TitaVo titaVo) {
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(), msg,
				titaVo);
	}

	private void sendErrorMsg(String msg, TitaVo titaVo) {
		isError = true;
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "", titaVo.getTlrNo(), msg,
				titaVo);
	}
}
