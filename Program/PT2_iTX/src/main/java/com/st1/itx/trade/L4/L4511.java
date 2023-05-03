package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.EmpDeductScheduleService;
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
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public CdCodeService cdCodeService;

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
	private int mediaDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4511 ");
		this.totaVo.init(titaVo);

		txToDoCom.setTxBuffer(this.getTxBuffer());

		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem")); // 作業項目 1.15日薪 2.非15日薪

		cntY = 0;
		cntN = 0;
		sendMsg = "";
		int iEntryDate = parse.stringToInteger(titaVo.getParam("EntryDate")) + 19110000;
		// MediaDate 塞今天
		mediaDate = parse.stringToInteger(titaVo.getEntDy()) + 19110000;
		Slice<EmpDeductMedia> slEmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate,
				iOpItem == 1 ? "4" : "5", 0, Integer.MAX_VALUE, titaVo);
		if (titaVo.isHcodeNormal()) {
			if (slEmpDeductMedia != null) {
				throw new LogicException(titaVo, "E0015", "已執行L4511-產生媒體檔，須訂正L4511"); // 檢查錯誤
			}
		}
		if (titaVo.isHcodeErase()) {
			if (slEmpDeductMedia != null) {
				List<EmpDeductMedia> lEmpDeductMedia = new ArrayList<EmpDeductMedia>();
				lEmpDeductMedia = slEmpDeductMedia == null ? null : slEmpDeductMedia.getContent();
				try {
					empDeductMediaService.deleteAll(lEmpDeductMedia, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0008", "員工媒體檔刪除失敗 :" + e.getErrorMsg());
				}
			}
			Slice<EmpDeductDtl> slEmpDeductDtl = empDeductDtlService.mediaDateRng(mediaDate, iOpItem == 1 ? "4" : "5",
					0, Integer.MAX_VALUE, titaVo);
			List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();
			lEmpDeductDtl = slEmpDeductDtl == null ? null : slEmpDeductDtl.getContent();
			if (lEmpDeductDtl != null) {
				for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
					tEmpDeductDtl.setMediaSeq(0);
				}
				try {
					empDeductDtlService.updateAll(lEmpDeductDtl, titaVo);
				} catch (DBException e) {
					throw new LogicException("E0005", "員工扣薪檔更新失敗 :" + e.getErrorMsg());
				}
			}
		}
		if (titaVo.isHcodeNormal()) {
			if (slEmpDeductMedia != null) {
				throw new LogicException(titaVo, "E0015", "已執行L4511-產生媒體檔，須訂正L4511"); // 檢查錯誤
			}

			List<String> type = new ArrayList<String>();
//		------------is15------------
			if (iOpItem == 1) {

				type.add("4");
				type.add("5");

				Slice<EmpDeductDtl> sis15EmpDeductDtl = empDeductDtlService.entryDateRng(iEntryDate, iEntryDate, type,
						0, Integer.MAX_VALUE, titaVo);
				List<EmpDeductDtl> is15EmpDeductDtl = new ArrayList<EmpDeductDtl>();
				is15EmpDeductDtl = sis15EmpDeductDtl == null ? null : sis15EmpDeductDtl.getContent();
				this.info("Is15 Dtl Start...");

				ArrayList<EmpDeductMedia> lis15EmpDeductMedia = new ArrayList<EmpDeductMedia>();

//		    寫入EmpDeductMedia (彙總by戶號) 4:15日
				lis15EmpDeductMedia = setEmpDeductMedia(is15EmpDeductDtl, 4, titaVo);

				setFile(lis15EmpDeductMedia, titaVo, 1);
				sendMsg += "15日薪媒體檔已完成，筆數：" + cntY + "。";

				// 應處理清單(已處理)產出15日薪員工扣薪檔
				TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
				tTxToDoDetailId.setItemCode("L45101"); // 應處理清單(已處理)產出15日薪員工扣薪檔
				txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
			}

			// ------------un15------------
			if (iOpItem == 2) {

				type.add("1");
				type.add("2");
				type.add("3");
				type.add("6");
				type.add("7");
				type.add("8");
				type.add("9");

				Slice<EmpDeductDtl> sun15EmpDeductDtl = empDeductDtlService.entryDateRng(iEntryDate, iEntryDate, type,
						0, Integer.MAX_VALUE, titaVo);

				List<EmpDeductDtl> un15EmpDeductDtl = new ArrayList<EmpDeductDtl>();
				un15EmpDeductDtl = sun15EmpDeductDtl == null ? null : sun15EmpDeductDtl.getContent();
				this.info("Un15 Dtl Start...");

				ArrayList<EmpDeductMedia> lun15EmpDeductMedia = new ArrayList<EmpDeductMedia>();

//		  4.寫入EmpDeductMedia (彙總by戶號) 5:非15日
				lun15EmpDeductMedia = setEmpDeductMedia(un15EmpDeductDtl, 5, titaVo);

//		String pathun15 = outFolder + "LNM617P非15日.txt";
				setFile(lun15EmpDeductMedia, titaVo, 2);

				// 應處理清單(已處理)產出非15日薪員工扣薪檔
				TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
				tTxToDoDetailId.setItemCode("L45102"); // 產出非15日薪員工扣薪檔
				txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
				sendMsg += "非15日薪媒體檔已完成，筆數：" + cntN + "。";
			}

			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
					titaVo.getTlrNo() + "L4511", sendMsg, titaVo);

		}
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
				occursList.putParam("OccYearMonthA",
						setFormatMonth(parse.stringToInteger(titaVo.getCalDy()) + 19110000));
//				occursList.putParam("OccUnit", FormatUtil.padX(centerCodeAcc, 6));
				occursList.putParam("OccUnit", "10H400");
				if (flag == 1) {
					occursList.putParam("OccUnknowA", "0000000001");
				} else {
//					occursList.putParam("OccUnknowA", "0000000002");
					String tEmpCudtId = tEmpDeductMedia.getCustId();
					CdEmp cDemp = cdEmpService.findAgentIdFirst(tEmpCudtId, titaVo);
					if(cDemp!=null) {
						String cDComm = cDemp.getCommLineType();
						if(cDComm == null) {
							cDComm = " ";
						}
						occursList.putParam("OccUnknowA", "000000000"+cDComm);
						String cCommL = cDemp.getCommLineCode();
						if(cCommL == null) {
							cCommL = "  ";
						}
						occursList.putParam("OccLineCode", cCommL);
					}else {
						occursList.putParam("OccUnknowA", "000000000"+" ");
						occursList.putParam("OccLineCode", "  ");
					}
					
				}
				occursList.putParam("OccCustId", FormatUtil.padX(tEmpDeductMedia.getCustId(), 10));

				if (flag == 1) {
					if (tEmpDeductMedia.getRepayCode() == 5) {
						occursList.putParam("OccUnknowB", FormatUtil.padX("92 火險", 11));
					} else {
						occursList.putParam("OccUnknowB", FormatUtil.padX("XH 房貸", 11));
					}
				} else {
					if (tEmpDeductMedia.getRepayCode() == 5) {
						occursList.putParam("OccUnknowB", FormatUtil.padX("92 火險", 11));
					} else {
						occursList.putParam("OccUnknowB", FormatUtil.padX("XH 房貸扣款", 11));
					}
				}
				occursList.putParam("OccRepayCode", tEmpDeductMedia.getPerfRepayCode());

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

		}

		long sno = makeFile.close();
		this.info("sno : " + sno);
		makeFile.toFile(sno);
	}

	private void deleEmpDeductMedia(String MediaKind, int iMediaDate, TitaVo titaVo) throws LogicException {

		Slice<EmpDeductMedia> slEmpDeductMedia = empDeductMediaService.mediaDateRng(iMediaDate, iMediaDate, MediaKind,
				0, Integer.MAX_VALUE, titaVo);

		if (slEmpDeductMedia != null) {
			for (EmpDeductMedia tEmpDeductMedia : slEmpDeductMedia.getContent()) {
				tEmpDeductMedia = empDeductMediaService.holdById(tEmpDeductMedia.getEmpDeductMediaId(), titaVo);
				try {
					empDeductMediaService.delete(tEmpDeductMedia, titaVo);
					this.info("deleEmpDeductMedia =" + tEmpDeductMedia);
				} catch (DBException e) {
					throw new LogicException("E0008", "員工媒體檔刪除失敗 :" + e.getErrorMsg());
				}
			}
		}
	}

//	[flag = 4:15日 ; 5:非15日]
	private ArrayList<EmpDeductMedia> setEmpDeductMedia(List<EmpDeductDtl> lEmpDeductDtl, int flag, TitaVo titaVo)
			throws LogicException {

		if (lEmpDeductDtl == null || lEmpDeductDtl.size() == 0) {
			return null;
		}

		this.info("setEmpDeductMedia Start... ");

		HashMap<tmpCustRpCd, BigDecimal> repayAmt = new HashMap<>();
		lEmpDeductDtl = new ArrayList<EmpDeductDtl>(lEmpDeductDtl);
		lEmpDeductDtl.sort((c1, c2) -> {
			int result = 0;
//		入帳順序: 入帳日ASC > 戶號ASC > 扣款類別ASC (火險往前，因其他費用包含在期款內)
			if (c1.getEntryDate() - c2.getEntryDate() != 0) {
				result = c1.getEntryDate() - c2.getEntryDate();
			} else if (c1.getCustNo() - c2.getCustNo() != 0) {
				result = c1.getCustNo() - c2.getCustNo();
			} else if (c1.getAchRepayCode() != c2.getAchRepayCode()) {
				if (c1.getAchRepayCode() == 5) {
					result = -1;
				} else if (c2.getAchRepayCode() == 5) {
					result = 1;
				} else {
					result = c1.getAchRepayCode() - c2.getAchRepayCode();
				}
			} else {
				result = 0;
			}
			return result;
		});

//	合計金額 火險、期款(其他費用)
		for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
			this.info("tEmpDeductDtl.getEntryDate() : " + tEmpDeductDtl.getEntryDate());
			this.info("tEmpDeductDtl.getCustNo() : " + tEmpDeductDtl.getCustNo());
			this.info("tEmpDeductDtl.getAchRepayCode() : " + tEmpDeductDtl.getAchRepayCode());

			String rpcd = "";

			if (tEmpDeductDtl.getAchRepayCode() == 5) {
				rpcd = "92";
			} else {
				rpcd = "XH";
			}

			tmpCustRpCd tmp = new tmpCustRpCd(tEmpDeductDtl.getMediaDate(), tEmpDeductDtl.getCustNo(), rpcd);
			this.info("tmp : " + tmp.toString());

			if (repayAmt.containsKey(tmp)) {
				repayAmt.put(tmp, repayAmt.get(tmp).add(tEmpDeductDtl.getRepayAmt()));
			} else {
				repayAmt.put(tmp, tEmpDeductDtl.getRepayAmt());
			}
			this.info("repayAmt : " + repayAmt.get(tmp));
		} // for

		List<EmpDeductMedia> deleEmpDeductMedia = new ArrayList<EmpDeductMedia>();

		Slice<EmpDeductMedia> delesEmpDeductMedia = null;

		delesEmpDeductMedia = empDeductMediaService.mediaDateRng(mediaDate, mediaDate, "" + flag, 0, Integer.MAX_VALUE,
				titaVo);

		deleEmpDeductMedia = delesEmpDeductMedia == null ? null : delesEmpDeductMedia.getContent();

		if (deleEmpDeductMedia != null && deleEmpDeductMedia.size() != 0) {
			if (deleEmpDeductMedia.get(0).getAcDate() > 0) {
				throw new LogicException("E0008", "已入帳");
			}
			try {
				empDeductMediaService.deleteAll(deleEmpDeductMedia, titaVo);
			} catch (DBException e1) {
				throw new LogicException("E0008", e1.getErrorMsg());
			}
		}

		HashMap<tmpCustRpCd, Integer> empCnt = new HashMap<>();
		int seq = 0;

		ArrayList<EmpDeductMedia> lEmpDeductMedia = new ArrayList<EmpDeductMedia>();
		for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
			EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();
			EmpDeductMediaId tEmpDeductMediaId = new EmpDeductMediaId();

//		group by CustNo, RepayCode
			String rpcd = "";

			if (tEmpDeductDtl.getAchRepayCode() == 5) {
				rpcd = "92";
			} else {
				rpcd = "XH";
			}

			tmpCustRpCd tmp = new tmpCustRpCd(tEmpDeductDtl.getMediaDate(), tEmpDeductDtl.getCustNo(), rpcd);

			if (empCnt.containsKey(tmp)) {
				updateEmpDeductDtl(tEmpDeductDtl.getEmpDeductDtlId(), seq, titaVo);
				this.info("tmp 已進入 continue ..." + tmp);
				continue;
			} else {
				empCnt.put(tmp, 1);
			}

			seq = seq + 1;

			tEmpDeductMediaId.setMediaDate(mediaDate);
			tEmpDeductMediaId.setMediaKind("" + flag);
			tEmpDeductMediaId.setMediaSeq(seq);
			tEmpDeductMedia.setEmpDeductMediaId(tEmpDeductMediaId);
			tEmpDeductMedia.setCustNo(tEmpDeductDtl.getCustNo());
			tEmpDeductMedia.setRepayCode(tEmpDeductDtl.getAchRepayCode());
			tEmpDeductMedia.setPerfRepayCode(parse.stringToInteger(tEmpDeductDtl.getRepayCode()));
			tEmpDeductMedia.setRepayAmt(repayAmt.get(tmp));
			tEmpDeductMedia.setPerfMonth(tEmpDeductDtl.getPerfMonth());
			tEmpDeductMedia.setFlowCode(tEmpDeductDtl.getProcCode());
			tEmpDeductMedia.setUnitCode(tEmpDeductDtl.getUnitCode());
			tEmpDeductMedia.setCustId(tEmpDeductDtl.getCustId());
			tEmpDeductMedia.setEntryDate(tEmpDeductDtl.getEntryDate());
			tEmpDeductMedia.setTxAmt(tEmpDeductDtl.getTxAmt());
			tEmpDeductMedia.setErrorCode("");
			tEmpDeductMedia.setAcctCode(tEmpDeductDtl.getAcctCode());

			tEmpDeductMedia.setAcDate(tEmpDeductDtl.getAcdate());
			tEmpDeductMedia.setBatchNo(tEmpDeductDtl.getBatchNo());
			tEmpDeductMedia.setDetailSeq(0);

			this.info("tEmpDeductMedia.insert = " + tEmpDeductMedia);
			try {
				empDeductMediaService.insert(tEmpDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
			}

			updateEmpDeductDtl(tEmpDeductDtl.getEmpDeductDtlId(), seq, titaVo);
			lEmpDeductMedia.add(tEmpDeductMedia);
		}

		return lEmpDeductMedia;
	}

	private void updateEmpDeductDtl(EmpDeductDtlId tEmpDeductDtlId, int seq, TitaVo titaVo) throws LogicException {
		this.info("updateEmpDeductDtl.tEmpDeductDtlId = " + tEmpDeductDtlId);
		EmpDeductDtl t2EmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtlId, titaVo);
		t2EmpDeductDtl.setMediaSeq(seq);
		t2EmpDeductDtl.setMediaDate(mediaDate);
		try {
			empDeductDtlService.update(t2EmpDeductDtl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "員工扣薪檔更新失敗 :" + e.getErrorMsg());
		}
	}

//暫時紀錄 媒體日期 戶號-額度-撥款 扣款代碼
	private class tmpCustRpCd {

		private int mediaDate = 0;
		private int custNo = 0;
		private String repayFlag = "";

		public tmpCustRpCd(int mediaDate, int custNo, String repayFlag) {
			this.setMediaDate(mediaDate);
			this.setCustNo(custNo);
			this.setRepayFlag(repayFlag);
		}

		private void setMediaDate(int mediaDate) {
			this.mediaDate = mediaDate;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private void setRepayFlag(String repayFlag) {
			this.repayFlag = repayFlag;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + custNo;
			result = prime * result + mediaDate;
			result = prime * result + ((repayFlag == null) ? 0 : repayFlag.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			tmpCustRpCd other = (tmpCustRpCd) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (custNo != other.custNo)
				return false;
			if (mediaDate != other.mediaDate)
				return false;
			if (repayFlag == null) {
				if (other.repayFlag != null)
					return false;
			} else if (!repayFlag.equals(other.repayFlag))
				return false;
			return true;
		}

		private L4511 getEnclosingInstance() {
			return L4511.this;
		}

		@Override
		public String toString() {
			return "tmpCustRpCd [mediaDate=" + mediaDate + ", custNo=" + custNo + ", repayFlag=" + repayFlag + "]";
		}
	}

	private String setFormatMonth(int month) {
		String result = "";

		result = ("" + month).substring(0, 4) + "/" + ("" + month).substring(4, 6);

		return result;
	}
}