package com.st1.itx.trade.L7;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.StakeholdersStaff;
import com.st1.itx.db.service.StakeholdersStaffService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L7206")
@Scope("prototype")
/**
 * 人壽利關人上傳作業
 *
 * @author Ted
 * @version 1.0.0
 */
public class L7206 extends TradeBuffer {

	@Autowired
	public Parse parse;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public FileCom fileCom;
	@Autowired
	public StakeholdersStaffService tStakeholdersStaffService;

	@Autowired
	MakeExcel makeExcel;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	// 明細資料容器
	private ArrayList<OccursList> occursList = new ArrayList<>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7206 ");
		this.totaVo.init(titaVo);

		// 吃檔
		String filename = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		this.info("filename=" + filename);

		ArrayList<String> dataLineList = new ArrayList<>();

//       編碼參數，設定為UTF-8 || big5
		try {
			dataLineList = fileCom.intputTxt(filename, "UTF-8");
		} catch (IOException e) {
			this.info("L7206(" + filename + ") : " + e.getMessage());
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + filename;

			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		titaVo.setDataBaseOnMon();// 指定月報環境

		String extension[] = filename.split("\\.");

		this.info("file extension=" + extension[extension.length - 1]);

		if ("xlsx".equals(extension[extension.length - 1]) || "xls".equals(extension[extension.length - 1])) {
			// 打開上傳的excel檔案，預設讀取第1個工作表
			makeExcel.openExcel(filename, 1);
			// 切資料
			setValueFromFileExcel(titaVo);
		} else if ("csv".equals(extension[extension.length - 1])) {
			setValueFromFile(dataLineList);
		} else {
			String ErrorMsg = "請上傳正確附檔名之檔案-csv,xls,xlsx";
			throw new LogicException(titaVo, "E0014", ErrorMsg);
		}

		int CountAll = occursList.size();
		int CountS = 0;

		List<StakeholdersStaff> instakeholdersStaff = new ArrayList<StakeholdersStaff>();

		List<StakeholdersStaff> delStakeholdersStaff = new ArrayList<StakeholdersStaff>();

		Slice<StakeholdersStaff> tStakeholdersStaff = tStakeholdersStaffService.findAll(0, Integer.MAX_VALUE, titaVo);

		delStakeholdersStaff = tStakeholdersStaff == null ? null : tStakeholdersStaff.getContent();
		this.info("delStakeholdersStaff = " + delStakeholdersStaff);

		for (OccursList tempOccursList : occursList) {

			String id = tempOccursList.get("StaffId").toString();
			String name = tempOccursList.get("StaffName").toString();
			BigDecimal loanAmount = new BigDecimal(tempOccursList.get("LoanAmount"));

			StakeholdersStaff sStakeholdersStaff = new StakeholdersStaff();

			sStakeholdersStaff.setStaffId(id);
			sStakeholdersStaff.setStaffName(name);
			sStakeholdersStaff.setLoanAmount(loanAmount);
			instakeholdersStaff.add(sStakeholdersStaff);

			CountS++; // 成功筆數+1

		} // for

		try {

			if (delStakeholdersStaff != null) {
				this.info("1.delete old data");
				tStakeholdersStaffService.deleteAll(delStakeholdersStaff, titaVo);
			}
			this.info("2.insert new data");
			tStakeholdersStaffService.insertAll(instakeholdersStaff, titaVo);

		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg());
		}

		this.totaVo.putParam("CountAll", CountAll);
		this.totaVo.putParam("Count", CountS);

		this.addList(this.totaVo);
		return this.sendList();

	}

	public void setValueFromFile(ArrayList<String> lineList) {

		// 依照行數擷取明細資料
		for (String thisLine : lineList) {

			// 明細
			OccursList occursList = new OccursList();

			String[] thisColumn = thisLine.split(",");
			if (thisColumn.length >= 1 && thisColumn != null) {
				occursList.putParam("StaffId", thisColumn[0]);
				occursList.putParam("StaffName", thisColumn[1]);
				occursList.putParam("LoanAmount", thisColumn[2]);

			}
			this.occursList.add(occursList);
		}

	}

	public void setValueFromFileExcel(TitaVo titaVo) throws LogicException {

		// 取得工作表資料的最後一列
		int lastRowNum = makeExcel.getSheetLastRowNum() + 1;

		this.info("lastRowNum=" + lastRowNum);

		String id = "";
		String name = "";
		BigDecimal loanAmt = BigDecimal.ZERO;
		for (int i = 2; i <= lastRowNum; i++) {

			OccursList occursList = new OccursList();

			// 連續資料串，遇到空值強行結束
			if (makeExcel.getValue(i, 1).toString().trim().length() == 0 || makeExcel.getValue(i, 2).toString().trim().length() == 0
					|| makeExcel.getValue(i, 3).toString().trim().length() == 0) {
				break;
			}

			try {
				id = makeExcel.getValue(i, 1).toString().trim();
				Object xid = makeExcel.getValue(i, 1).toString().trim().length() > 10
						? new BigDecimal(makeExcel.getValue(i, 1).toString().trim()).toPlainString()
						: id;

				id = xid.toString();
				name = makeExcel.getValue(i, 2).toString().trim();
				loanAmt = new BigDecimal(makeExcel.getValue(i, 3).toString());
				this.info("id =" + id.length() + ":" + id);
			} catch (Exception e) {

				String ErrorMsg = "L7206(Excel欄位應為關係人代號在A欄、關係人名稱在B欄、放款金額為C欄)，請確認";

				throw new LogicException(titaVo, "E0015", ErrorMsg);
			}

			occursList.putParam("StaffId", id);
			occursList.putParam("StaffName", name);
			occursList.putParam("LoanAmount", loanAmt.intValue());

			this.occursList.add(occursList);
		}

	}

}