package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.EmpDeductFileVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4511")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4511 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4511.class);

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public WebClient webClient;

	@Autowired
	public TxToDoCom txToDoCom;

	private String empNo = "";
	private String empName = "";
	private String entryDate = "";
	private String fileName = "";
	private String centerCodeAcc = "";

	private int cntY = 0;
	private int cntN = 0;
	private String sendMsg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4511 ");
		this.totaVo.init(titaVo);

		txToDoCom.setTxBuffer(this.getTxBuffer());

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		cntY = 0;
		cntN = 0;
		sendMsg = "L4511，";

//		1.產出媒體
//		------------is15------------
		int mediaDate = parse.stringToInteger(titaVo.getParam("MediaDate")) + 19110000;

//		String pathis15 = outFolder + "10H00015日薪.TXT";

		ArrayList<EmpDeductMedia> lis15EmpDeductMedia = new ArrayList<EmpDeductMedia>();

		Slice<EmpDeductMedia> slis15EmpDeductMedia = null;

		slis15EmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate, "4", this.index, this.limit);

		lis15EmpDeductMedia = slis15EmpDeductMedia == null ? null
				: new ArrayList<EmpDeductMedia>(slis15EmpDeductMedia.getContent());

		if (lis15EmpDeductMedia != null && lis15EmpDeductMedia.size() != 0) {
			setFile(lis15EmpDeductMedia, titaVo, 1);
		}
//		------------un15------------

		ArrayList<EmpDeductMedia> lun15EmpDeductMedia = new ArrayList<EmpDeductMedia>();

		Slice<EmpDeductMedia> sun15EmpDeductMedia = null;

		sun15EmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate, "5", this.index, this.limit);

		lun15EmpDeductMedia = sun15EmpDeductMedia == null ? null
				: new ArrayList<EmpDeductMedia>(sun15EmpDeductMedia.getContent());

//		String pathun15 = outFolder + "LNM617P非15日.txt";

		if (lun15EmpDeductMedia != null && lun15EmpDeductMedia.size() != 0) {
			setFile(lun15EmpDeductMedia, titaVo, 2);
		}

		if (cntY == 0 && cntN == 0) {
			throw new LogicException("E0001 ", "查無資料");
		}
		if (cntY > 0) {
			sendMsg += "15日薪媒體檔已完成，筆數：" + cntY + "。";
		}
		if (cntN > 0) {
			sendMsg += "非15日薪媒體檔已完成，筆數：" + cntN + "。";
		}

//		刪除應處理事項清單
//		0.新增 1.刪除
//		txToDo(titaVo, 1);

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				sendMsg, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void setFile(ArrayList<EmpDeductMedia> lEmpDeductMedia, TitaVo titaVo, int flag) throws LogicException {
		ArrayList<OccursList> tmp = new ArrayList<>();

//		flag 1.15日 2.非15日
		if (flag == 1) {
//			10H000_emp-id_emp-Name_entryDate

			empNo = titaVo.getTlrNo();
			CdEmp tCdEmp = cdEmpService.findById(empNo, titaVo);

			if (tCdEmp != null) {
				empName = tCdEmp.getFullname();
				centerCodeAcc = tCdEmp.getCenterCodeAcc();
			}

			entryDate = dateUtil.getNowStringRoc();

			fileName = centerCodeAcc + "_" + empNo + "_" + empName + "_" + entryDate + ".txt";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
					titaVo.getTxCode() + "-產出員工扣薪媒體檔-15日薪", fileName, 2);
		} else if (flag == 2) {
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
					titaVo.getTxCode() + "-產出員工扣薪媒體檔-非15日", "LNM617P.txt", 2);
		}

		if (lEmpDeductMedia != null && lEmpDeductMedia.size() != 0) {
//			產媒體順序 扣款類別ASC>戶號ASC
			lEmpDeductMedia.sort((c1, c2) -> {
				int result = 0;
				if (c1.getRepayCode() - c2.getRepayCode() != 0) {
					result = c1.getRepayCode() - c2.getRepayCode();
				} else if (c1.getCustNo() - c2.getCustNo() != 0) {
					result = c1.getCustNo() - c2.getCustNo();
				} else {
					result = 0;
				}
				return result;
			});

			for (EmpDeductMedia tEmpDeductMedia : lEmpDeductMedia) {
				if (flag == 1) {
					cntY = cntY + 1;
				} else {
					cntN = cntN + 1;
				}

				OccursList occursList = new OccursList();
				occursList.putParam("OccYearMonthA", setFormatMonth(tEmpDeductMedia.getPerfMonth()));
				occursList.putParam("OccUnit", FormatUtil.padX(centerCodeAcc, 6));
				if (flag == 1) {
					occursList.putParam("OccUnknowA", "0000000001");
				} else {
					occursList.putParam("OccUnknowA", "0000000002");
				}
				occursList.putParam("OccCustId", FormatUtil.padX(tEmpDeductMedia.getCustId(), 10));

				if (flag == 1) {
					if (tEmpDeductMedia.getRepayCode() == 5) {
						occursList.putParam("OccUnknowB", FormatUtil.padX("92 火險", 11));
						occursList.putParam("OccRepayCode", 9);
					} else {
						occursList.putParam("OccUnknowB", FormatUtil.padX("XH 房貸", 11));
						occursList.putParam("OccRepayCode", 1);
					}
				} else {
					if (tEmpDeductMedia.getRepayCode() == 5) {
						occursList.putParam("OccUnknowB", FormatUtil.padX("92 火險", 11));
						occursList.putParam("OccRepayCode", 9);
					} else {
						occursList.putParam("OccUnknowB", FormatUtil.padX("XH 房貸扣款", 11));
						occursList.putParam("OccRepayCode", 1);
					}
				}

				occursList.putParam("OccUnknowC", FormatUtil.padX("", 11));
				occursList.putParam("OccRepayAmt",
						FormatUtil.padX("-" + FormatUtil.pad9("" + tEmpDeductMedia.getRepayAmt(), 9), 10));
				occursList.putParam("OccUnknowD", FormatUtil.padX("", 40));
				occursList.putParam("OccUnknowE", "Y");
				occursList.putParam("OccEntryDate", tEmpDeductMedia.getEntryDate() + 19110000);
				occursList.putParam("OccYearMonthB", tEmpDeductMedia.getPerfMonth());
				occursList.putParam("OccProcessType", tEmpDeductMedia.getFlowCode());
				occursList.putParam("OccCustNo", FormatUtil.pad9("" + tEmpDeductMedia.getCustNo(), 7));
				occursList.putParam("OccAcctCode", FormatUtil.padX(tEmpDeductMedia.getAcctCode().trim(), 3));
				occursList.putParam("OccUnknowF", FormatUtil.padX("", 42));

				tmp.add(occursList);
			}
			EmpDeductFileVo empDeductFileVo = new EmpDeductFileVo();

			// 把明細資料容器裝到檔案資料容器內
			empDeductFileVo.setOccursList(tmp);
			// 轉換資料格式
			ArrayList<String> file = empDeductFileVo.toFile();

			for (String line : file) {
				makeFile.put(line);
			}

			long sno = makeFile.close();

			this.info("sno : " + sno);
			makeFile.toFile(sno);
		} else {
			throw new LogicException("E0001 ", "查無資料");
		}
	}

//	0.新增 1.刪除
	private void txToDo(TitaVo titaVo, int flag) throws LogicException {
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode("L4510"); // L4510 員工媒體 寫一筆提醒
		tTxToDoDetail.setCustNo(0);
		tTxToDoDetail.setFacmNo(0);
		tTxToDoDetail.setBormNo(0);
		tTxToDoDetail.setDtlValue("");
		txToDoCom.addDetail(true, flag, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
	}

	private String setFormatMonth(int month) {
		String result = "";

		result = ("" + month).substring(0, 4) + "/" + ("" + month).substring(4, 6);

		return result;
	}
}