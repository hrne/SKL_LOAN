
package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.domain.EmpDeductMedia;
import com.st1.itx.db.domain.EmpDeductMediaId;
import com.st1.itx.db.domain.EmpDeductSchedule;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.db.service.EmpDeductMediaService;
import com.st1.itx.db.service.EmpDeductScheduleService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.db.service.springjpa.cm.L4510ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L4510Batch")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4510Batch extends TradeBuffer {

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public L4510ServiceImpl l4510ServiceImpl;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public EmpDeductDtlService empDeductDtlService;

	@Autowired
	public BaTxCom baTxCom;

	@Autowired
	public L4510Report l4510Report;

	@Autowired
	public L4510Report2 l4510Report2;

	@Autowired
	public L4510Report3 l4510Report3;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public EmpDeductMediaService empDeductMediaService;

	@Autowired
	public EmpDeductScheduleService empDeductScheduleService;

	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	private int iEntryDate = 0; // 入帳日期
	private int iPayIntDate = 0; // 應繳日
	private int iRepayEndDate = 0; // 應繳截止日

//	繳息迄日 minNextPayIntDateList
	private HashMap<tmpFacm, Integer> minNextPayIntDateList = new HashMap<>();
//	計息起日 prevPayIntDateList
	private HashMap<tmpFacm, Integer> intStartDate = new HashMap<>();
//	計息止日 prevRepaidDateList
	private HashMap<tmpFacm, Integer> intEndDate = new HashMap<>();
//	皆取最小的

//	01.期款
	private HashMap<tmpFacm, BigDecimal> rpAmt01Map = new HashMap<>();
//	04.帳管費 
	private HashMap<tmpFacm, BigDecimal> rpAmt04Map = new HashMap<>();
//	06.契變手續費 
	private HashMap<tmpFacm, BigDecimal> rpAmt06Map = new HashMap<>();
//	05.火險
	private HashMap<tmpFacm, BigDecimal> rpAmt05Map = new HashMap<>();
//	10.本金
	private HashMap<tmpFacm, BigDecimal> rpAmt10Map = new HashMap<>();
//	11.利息
	private HashMap<tmpFacm, BigDecimal> rpAmt11Map = new HashMap<>();
//	1C.違約金
	private HashMap<tmpFacm, BigDecimal> rpAmt1CMap = new HashMap<>();
//	41.欠繳本金
	private HashMap<tmpFacm, BigDecimal> rpAmt41Map = new HashMap<>();
//	42.欠繳利息
	private HashMap<tmpFacm, BigDecimal> rpAmt42Map = new HashMap<>();
//	4D.短收
	private HashMap<tmpFacm, BigDecimal> rpAmt4DMap = new HashMap<>();
//	4C.溢收
	private HashMap<tmpFacm, BigDecimal> rpAmt4CMap = new HashMap<>();
//	30.暫收抵繳
	private HashMap<tmpFacm, BigDecimal> rpAmt30Map = new HashMap<>();

	private HashMap<tmpFacm, Integer> mapFlag = new HashMap<>();
	private HashMap<tmpFacm, Integer> mapFlag2 = new HashMap<>();
// 火險單
	private HashMap<tmpFacm, String> insuNoMap = new HashMap<>();
	private HashMap<tmpFacm, Integer> bormMap = new HashMap<>();

	private int mediaDate = 0;
//	private int entryDate = 0;
	private int cnt = 0;

	private HashMap<String, Integer> perfMonth = new HashMap<>();

	private HashMap<tmpFacm, String> errMsg = new HashMap<>();

	private HashMap<tmpFacm, String> facmAcctCode = new HashMap<>();
	private HashMap<Integer, String> custAcctCode = new HashMap<>();
	private TempVo tempVo = new TempVo();
	private HashMap<tmpFacm, String> jsonField = new HashMap<>();
	private HashMap<tmpFacm, Integer> mapSetFlag = new HashMap<>();

	Slice<EmpDeductSchedule> slEmpDeductSchedule = null;
	private long reportA = 0;
	private long reportB = 0;
	private long reportC = 0;
	private long reportD = 0;
	private long reportE = 0;
	private long reportF = 0;

	private String sendMsg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4510Batch ");
		this.totaVo.init(titaVo);

		String parentTranCode = titaVo.getTxcd();

		l4510Report.setParentTranCode(parentTranCode);
		l4510Report2.setParentTranCode(parentTranCode);
		l4510Report3.setParentTranCode(parentTranCode);

		baTxCom.setTxBuffer(this.getTxBuffer());
		txToDoCom.setTxBuffer(this.getTxBuffer());
		int iOpItem = parse.stringToInteger(titaVo.getParam("OpItem")); // 作業項目 1.15日薪 2.非15日薪

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		List<String> procCodeIs15 = new ArrayList<String>();
		List<String> procCodeUn15 = new ArrayList<String>();

//		List<String> dateIs15 = new ArrayList<String>();
//		List<String> dateUn15 = new ArrayList<String>();

//		1.清除原扣款檔
//		2.從主檔撈資料
//		3.BatxCom算金額
//		4.寫入扣款檔
//		5.產報表
//		6.報表下載功能(BS需增加程式完成，並指向產檔位置)

//		根據輸入之入帳日查詢BORM(ACDATE)->FACM(REPAYCODE:03)寫入empdtl
		mediaDate = parse.stringToInteger(titaVo.get("MediaDate")) + 19110000;

//		抓取媒體日為今日者
		slEmpDeductSchedule = empDeductScheduleService.mediaDateRange(mediaDate, mediaDate, this.index, this.limit,
				titaVo);
		if (slEmpDeductSchedule != null) {
			for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
				// 應繳截止日減1天，方便處理
				if (tEmpDeductSchedule.getRepayEndDate() > 0) {
					dDateUtil.init();
					dDateUtil.setDate_1(tEmpDeductSchedule.getRepayEndDate());
					dDateUtil.setDays(-1);
					tEmpDeductSchedule.setRepayEndDate(dDateUtil.getCalenderDay());
				}
				perfMonth.put(tEmpDeductSchedule.getAgType1(), tEmpDeductSchedule.getWorkMonth());
				CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(),
						titaVo);
//				1.15日薪 2.非15日薪
				if (iOpItem == 1 && "1".equals(tCdCode.getItem().substring(0, 1))) {
					procCodeIs15.add(tEmpDeductSchedule.getAgType1());
				}
				if (iOpItem == 2 && "2".equals(tCdCode.getItem().substring(0, 1))) {
					procCodeUn15.add(tEmpDeductSchedule.getAgType1());
				}
			}

		}

//		刪除舊資料
		if (iOpItem == 1) {

			deleEmpDeductDtl("4", mediaDate, titaVo);
			deleEmpDeductMedia("4", mediaDate, titaVo);
			// 還款試算
			calculateY15BaTxCom(titaVo);
			List<EmpDeductDtl> is15EmpDeductDtl = new ArrayList<EmpDeductDtl>();
			Slice<EmpDeductDtl> sis15EmpDeductDtl = empDeductDtlService.mediaDateRng(mediaDate, "4", this.index,
					this.limit, titaVo);
			is15EmpDeductDtl = sis15EmpDeductDtl == null ? null : sis15EmpDeductDtl.getContent();
			this.info("Is15 Dtl Start...");
//		4.寫入EmpDeductMedia (彙總by戶號) 4:15日
			setEmpDeductMedia(is15EmpDeductDtl, 4, titaVo);
//		D E F
//		火險費
			try {
				reportD = setReportInsuFee(mediaDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
//		帳管費
			try {
				reportE = setReportAcctFee(mediaDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

			try {
				reportF = setReportMedia(mediaDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
		}
		if (iOpItem == 2) {
			deleEmpDeductDtl("5", mediaDate, titaVo);
			deleEmpDeductMedia("5", mediaDate, titaVo);
//			3.產出火險(05)、帳管(04)、明細表
			calculateN15BaTxCom(titaVo);
			List<EmpDeductDtl> un15EmpDeductDtl = new ArrayList<EmpDeductDtl>();

			Slice<EmpDeductDtl> sun15EmpDeductDtl = empDeductDtlService.mediaDateRng(mediaDate, "5", this.index,
					this.limit, titaVo);
			un15EmpDeductDtl = sun15EmpDeductDtl == null ? null : sun15EmpDeductDtl.getContent();
			this.info("Un15 Dtl Start...");
//			4.寫入EmpDeductMedia (彙總by戶號) 5:非15日
			setEmpDeductMedia(un15EmpDeductDtl, 5, titaVo);

//			A B C
//			火險費
			try {
				reportA = setReportInsuFee(mediaDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
//			帳管費
			try {
				reportB = setReportAcctFee(mediaDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

			try {
				reportC = setReportMedia(mediaDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
		}

//
		totaVo.put("OReportA", "" + reportA);
		totaVo.put("OReportB", "" + reportB);
		totaVo.put("OReportC", "" + reportC);
		totaVo.put("OReportD", "" + reportD);
		totaVo.put("OReportE", "" + reportE);
		totaVo.put("OReportF", "" + reportF);
		if (cnt == 0) {
			sendMsg = (iOpItem == 1 ? "15日薪" : "非15日薪") + " 產生扣薪明細筆數 = 0";
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "L4511",
					titaVo.get("MediaDate") + iOpItem, sendMsg, titaVo);
		} else {
			sendMsg = (iOpItem == 1 ? "15日薪" : "非15日薪") + " 扣薪報表已完成，扣薪明細筆數 = " + cnt;
			webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					sendMsg, titaVo);
		}

		this.addList(totaVo);
		return this.sendList();
	}

	private void getList(int flag, Slice<EmpDeductSchedule> slEmpDeductSchedule, String AgType1) throws LogicException {
//	iEntryDate  入帳日
//  iPayIntDate 應繳日
//  iRepayEndDate 應繳截止日
		this.info("slEmpDeductSchedule = " + slEmpDeductSchedule);
		for (EmpDeductSchedule tEmpDeductSchedule : slEmpDeductSchedule.getContent()) {
			if (AgType1.equals("" + tEmpDeductSchedule.getAgType1())) {
				this.iEntryDate = tEmpDeductSchedule.getEntryDate();
				// 15薪 應繳日=15
				if (flag == 1) {
					this.iPayIntDate = (iEntryDate / 100) * 100 + 15;
				} else {
					this.iPayIntDate = iEntryDate;
				}
				if (tEmpDeductSchedule.getRepayEndDate() > 0) {
					this.iRepayEndDate = tEmpDeductSchedule.getRepayEndDate();
				} else {
					this.iRepayEndDate = iEntryDate;
				}
			}
		}
		this.info("iEntryDate=" + iEntryDate + ", iPayIntDate=" + iPayIntDate + ", iRepayEndDate=" + iRepayEndDate);
	}

//	用既有之List for each 找出RepayCode 1.期款  4.帳管 5.火險 6.契變手續費 
	private void doBaTxCom(int flag, List<Map<String, String>> resultList, TitaVo titaVo) throws LogicException {
		List<BaTxVo> listBaTxVo = new ArrayList<>();

		baTxCom.setTxBuffer(this.getTxBuffer());

//                       15薪	                                                   員工身份別=非15薪
// 還款方式=員工扣薪	應繳日=15	
// 還款方式=非員工扣薪	依據[應繳截止日] 逾1期(含)以上                 依據[應繳截止日]逾1期(含)以上
// 應繳截止日   上繳日          逾期數 (條件:逾1期(含)以上)
// 4/30         2/20             2
// 3/31         2/28(應繳日31)   0
// 3/31         2/20             1

		for (Map<String, String> result : resultList) {
//			F0 CustNo
//			F1 FacmNo
//			F2 AcctCode
//			F3 1.15日薪 2.非15日薪
//			F4 BormNo (逾兩期需指到撥款，無需扣費用)
//			F5 ProcCode (流程別)
			getList(flag, slEmpDeductSchedule, result.get("AgType1"));
			int nextPayIntDate = parse.stringToInteger(result.get("NextPayIntDate")) - 19110000;
			if ("3".equals(result.get("RepayCode"))) {
				if (nextPayIntDate > iPayIntDate) {
					this.info("skip NextPayIntDate > iPayIntDate " + result);
					continue;
				}
			} else {
				if (nextPayIntDate > iRepayEndDate) {
					this.info("skip NextPayIntDate > iRepayEndDate " + result);
					continue;
				} else {
					iPayIntDate = iRepayEndDate;
				}
			}

			// 應繳試算
			listBaTxVo = baTxCom.settingPayintDate(iEntryDate, iPayIntDate, parse.stringToInteger(result.get("CustNo")),
					parse.stringToInteger(result.get("FacmNo")), 0, 1, BigDecimal.ZERO, titaVo);

			tmpFacm tmp2 = new tmpFacm(parse.stringToInteger(result.get("CustNo")),
					parse.stringToInteger(result.get("FacmNo")), 0, 0, flag,
					parse.stringToInteger(result.get("AgType1")));
			// 業務科目(額度)
			facmAcctCode.put(tmp2, result.get("AcctCode"));

			// 業務科目(戶號第一筆)
			if (!custAcctCode.containsKey(parse.stringToInteger(result.get("CustNo")))) {
				custAcctCode.put(parse.stringToInteger(result.get("CustNo")), result.get("AcctCode"));
			}

			this.info("listBaTxVo =" + listBaTxVo);
			setBatxValue(listBaTxVo, flag, parse.stringToInteger(result.get("AgType1")));
		} // for

//		各個repaycode寫入BankDeductDtl
		if (mapFlag != null) {
			setEmpDeductDtl(mapFlag, titaVo);
		}

	}

//	暫時紀錄戶號額度
	/**
	 * 
	 * @author custNo <br>
	 *         facmNo <br>
	 *         bormNo <br>
	 *         achRepayCode <br>
	 *
	 */
	private class tmpFacm {

		public tmpFacm(int custNo, int facmNo, int bormNo, int achRepayCode, int flag, int procCode) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setBormNo(bormNo);
			this.setAchRepayCode(achRepayCode);
			this.setFlag(flag);
			this.setProcCode(procCode);
		}

		private int custNo = 0;
		private int facmNo = 0;
		private int BormNo = 0;
		private int achRepayCode = 0;
		private int flag = 0;
		private int procCode = 0;

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", BormNo=" + BormNo + ", achRepayCode="
					+ achRepayCode + ", flag=" + flag + ", procCode=" + procCode + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + BormNo;
			result = prime * result + achRepayCode;
			result = prime * result + custNo;
			result = prime * result + facmNo;
			result = prime * result + flag;
			result = prime * result + procCode;
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
			tmpFacm other = (tmpFacm) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (BormNo != other.BormNo)
				return false;
			if (achRepayCode != other.achRepayCode)
				return false;
			if (custNo != other.custNo)
				return false;
			if (facmNo != other.facmNo)
				return false;
			if (flag != other.flag)
				return false;
			if (procCode != other.procCode)
				return false;
			return true;
		}

		private int getCustNo() {
			return custNo;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private int getFacmNo() {
			return facmNo;
		}

		private void setFacmNo(int facmNo) {
			this.facmNo = facmNo;
		}

		private int getBormNo() {
			return BormNo;
		}

		private void setBormNo(int bormNo) {
			BormNo = bormNo;
		}

		private int getAchRepayCode() {
			return achRepayCode;
		}

		private void setAchRepayCode(int achRepayCode) {
			this.achRepayCode = achRepayCode;
		}

		private int getFlag() {
			return flag;
		}

		private void setFlag(int flag) {
			this.flag = flag;
		}

		private int getProcCode() {
			return procCode;
		}

		private void setProcCode(int procCode) {
			this.procCode = procCode;
		}

		private L4510Batch getEnclosingInstance() {
			return L4510Batch.this;
		}
	}

//	寫入EmpDeductDtl
	private void setEmpDeductDtl(HashMap<tmpFacm, Integer> map, TitaVo titaVo) throws LogicException {
		this.info("setEmpDeductDtl Start...");

		this.info("map ..." + map.toString());

		Set<tmpFacm> tempSet = map.keySet();

		List<tmpFacm> tempList = new ArrayList<>();

		for (Iterator<tmpFacm> it = tempSet.iterator(); it.hasNext();) {
			tmpFacm tmpFacmVo = it.next();
			tempList.add(tmpFacmVo);
		}

//		CustNo ASC > FacmNo ASC > RepayType DESC > BormNo ASC
		tempList.sort((c1, c2) -> {
			int result = 0;
			if (c1.getCustNo() - c2.getCustNo() != 0) {
				result = c1.getCustNo() - c2.getCustNo();
			} else if (c1.getFacmNo() - c2.getFacmNo() != 0) {
				result = c1.getFacmNo() - c2.getFacmNo();
			} else if (c1.getAchRepayCode() - c2.getAchRepayCode() != 0) {
				result = c2.getAchRepayCode() - c1.getAchRepayCode();
			} else if (c1.getBormNo() - c2.getBormNo() != 0) {
				result = c1.getBormNo() - c2.getBormNo();
			} else {
				result = 0;
			}
			return result;
		});

		this.info("tempList ..." + tempList.toString());
		mapFlag2 = new HashMap<>();
		jsonField = new HashMap<>();
//		RepayType = 0 先寫入jsonFild

		for (tmpFacm tmp : tempList) {
			if (tmp.getAchRepayCode() == 0) {
				this.info(tmp.toString() + " repayType = 0，不寫入DB... continue");
//				repayType = 0 不寫入DB
				continue;
			}

//			欠繳金額根據AcReceivable.RvNo放入撥款
//			暫收抵繳放入欠款之第一個撥款
//
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tmp.getCustNo(), tmp.getCustNo(), titaVo);

			CdEmp tCdEmp = new CdEmp();
			if (tCustMain.getEmpNo() != null) {
				tCdEmp = cdEmpService.findById(tCustMain.getEmpNo(), titaVo);
			}
//			this.info("tCdEmp.getAgType1() : " + tCdEmp.getAgType1());
//
//			CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tCdEmp.getAgType1(), titaVo);

			int month = 0;

			if (perfMonth.get("" + tmp.getProcCode()) != null) {
				month = perfMonth.get("" + tmp.getProcCode());
			}

			if (month == 0) {
				this.info(tmp.toString() + " month = 0，同流程不同日期，不寫入DB... continue");
//				month = 0 不寫入DB
				continue;
			}

//			跳過重複
			if (mapSetFlag.containsKey(tmp)) {
				this.info(tmp.toString() + " 已產出... continue");
				continue;
			} else {
				mapSetFlag.put(tmp, 1);
			}

//			FormatUtil formatUtil = new FormatUtil();
			EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
			EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();

			tmpFacm tmp2 = new tmpFacm(tmp.getCustNo(), tmp.getFacmNo(), tmp.getBormNo(), 0, tmp.getFlag(),
					tmp.getProcCode());
			tmpFacm tmp3 = new tmpFacm(tmp.getCustNo(), tmp.getFacmNo(), 0, 0, tmp.getFlag(), tmp.getProcCode());

			this.info("tmp ... " + tmp);
			this.info("tmp2 ... " + tmp2);
			this.info("rpAmt30Map ... " + rpAmt30Map.get(tmp2));
			this.info("mapFlag2 ... " + mapFlag2.containsKey(tmp3));

//			暫收抵繳僅寫入第一筆為期款撥款(報表用)
			tempVo = new TempVo();
			if (!mapFlag2.containsKey(tmp3) && tmp.getAchRepayCode() == 1 && rpAmt30Map.get(tmp3) != null) {
				tempVo.putParam("TempAmt", rpAmt30Map.get(tmp3));
				mapFlag2.put(tmp3, 1);

				this.info("tempVo ... " + tempVo.toString());

			} else {
				if (mapFlag2.containsKey(tmp3)) {
					this.info(tmp3.toString() + "已寫入暫收抵繳");
				}
				if (tmp.getAchRepayCode() != 1) {
					this.info(tmp3.toString() + "不為1");
				}
				if (rpAmt30Map.get(tmp3) == null) {
					this.info(tmp3.toString() + "rpAmt30Map = null");
				}
			}

			this.info("mapFlag2 ... " + mapFlag2.containsKey(tmp3));

			if (rpAmt1CMap.get(tmp2) != null) {
				tempVo.putParam("Breach", rpAmt1CMap.get(tmp2));
			}
			if (rpAmt41Map.get(tmp2) != null) {
				tempVo.putParam("ShortPri", rpAmt41Map.get(tmp2));
			}
			if (rpAmt42Map.get(tmp2) != null) {
				tempVo.putParam("ShortInt", rpAmt42Map.get(tmp2));
			}
			if (insuNoMap.get(tmp2) != null) {
				tempVo.putParam("InsuNo", insuNoMap.get(tmp2));
			}

			this.info("tempVo ... " + tempVo.toString());

			if (tempVo.getJsonString() != null && !"".equals(tempVo.getJsonString().trim())) {
				jsonField.put(tmp2, tempVo.getJsonString());
			}

			tEmpDeductDtlId.setEntryDate(iEntryDate + 19110000);
			tEmpDeductDtlId.setCustNo(tmp.getCustNo());
			tEmpDeductDtlId.setAchRepayCode(tmp.getAchRepayCode());

			tEmpDeductDtlId.setPerfMonth(month);
			tEmpDeductDtlId.setProcCode("" + tmp.getProcCode());

//			QC.623 非15薪扣款代碼應為滯繳件
			// MediaDate, MediaKind由最後update移至
			if (tmp.getFlag() == 2) {
				tEmpDeductDtlId.setRepayCode("3");
				tEmpDeductDtl.setMediaKind("5");
			} else {
				tEmpDeductDtlId.setRepayCode("1"); // ???
				tEmpDeductDtl.setMediaKind("4");
			}
			tEmpDeductDtlId.setAcctCode(facmAcctCode.get(tmp3));
			tEmpDeductDtlId.setFacmNo(tmp.getFacmNo());
			tEmpDeductDtlId.setBormNo(tmp.getBormNo());

			tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);

			tEmpDeductDtl.setMediaDate(mediaDate);
			tEmpDeductDtl.setEntryDate(tEmpDeductDtlId.getEntryDate());
			tEmpDeductDtl.setCustNo(tEmpDeductDtlId.getCustNo());
			tEmpDeductDtl.setAchRepayCode(tEmpDeductDtlId.getAchRepayCode());
			tEmpDeductDtl.setPerfMonth(tEmpDeductDtlId.getPerfMonth());
			tEmpDeductDtl.setProcCode(tEmpDeductDtlId.getProcCode());
			tEmpDeductDtl.setRepayCode(tEmpDeductDtlId.getRepayCode());
			tEmpDeductDtl.setAcctCode(tEmpDeductDtlId.getAcctCode());
			tEmpDeductDtl.setFacmNo(tEmpDeductDtlId.getFacmNo());
			tEmpDeductDtl.setBormNo(tEmpDeductDtlId.getBormNo());

			if (tCdEmp != null) {
				tEmpDeductDtl.setEmpNo(tCdEmp.getEmployeeNo());
			}
			tEmpDeductDtl.setCustId(tCustMain.getCustId());

			this.info("tmp.getAchRepayCode() : " + tmp.getAchRepayCode());
			this.info("rpAmt01Map : " + rpAmt01Map.get(tmp));
			this.info("rpAmt04Map : " + rpAmt04Map.get(tmp));
			this.info("rpAmt06Map : " + rpAmt06Map.get(tmp));
			this.info("rpAmt05Map : " + rpAmt05Map.get(tmp));

			BigDecimal txAmt = BigDecimal.ZERO;

			if (tmp.getAchRepayCode() == 1 && rpAmt01Map.get(tmp) != null) {
				txAmt = rpAmt01Map.get(tmp);
				txAmt = rpAmt01Map.get(tmp);
			} else if (tmp.getAchRepayCode() == 4 && rpAmt04Map.get(tmp) != null) {
				txAmt = rpAmt04Map.get(tmp);
				txAmt = rpAmt04Map.get(tmp);
			} else if (tmp.getAchRepayCode() == 6 && rpAmt06Map.get(tmp) != null) {
				txAmt = rpAmt06Map.get(tmp);
				txAmt = rpAmt06Map.get(tmp);
			} else if (tmp.getAchRepayCode() == 5 && rpAmt05Map.get(tmp) != null) {
				txAmt = rpAmt05Map.get(tmp);
				txAmt = rpAmt05Map.get(tmp);
			}

			tEmpDeductDtl.setTxAmt(BigDecimal.ZERO);
			tEmpDeductDtl.setRepayAmt(txAmt);

			tEmpDeductDtl.setErrMsg(errMsg.get(tmp));
			tEmpDeductDtl.setAcdate(0);
			tEmpDeductDtl.setTitaTxtNo("");
			tEmpDeductDtl.setTitaTlrNo("");
			tEmpDeductDtl.setBatchNo("");
			if (tCdEmp != null) {
				tEmpDeductDtl.setResignCode(tCdEmp.getAgStatusCode());
				tEmpDeductDtl.setDeptCode(tCdEmp.getCenterCodeAcc2());
				tEmpDeductDtl.setUnitCode(tCdEmp.getCenterCodeAcc());
				tEmpDeductDtl.setPositCode(tCdEmp.getAgPost());
			}
			if (intStartDate.get(tmp) != null)
				tEmpDeductDtl.setIntStartDate(intStartDate.get(tmp));
			if (intEndDate.get(tmp) != null)
				tEmpDeductDtl.setIntEndDate(intEndDate.get(tmp));

			if (rpAmt10Map.get(tmp) != null) {
				tEmpDeductDtl.setPrincipal(rpAmt10Map.get(tmp));
				tEmpDeductDtl.setCurrPrinAmt(rpAmt10Map.get(tmp));
			} else {
				tEmpDeductDtl.setPrincipal(BigDecimal.ZERO);
				tEmpDeductDtl.setCurrPrinAmt(BigDecimal.ZERO);
			}
			if (rpAmt11Map.get(tmp) != null) {
				tEmpDeductDtl.setInterest(rpAmt11Map.get(tmp));
				tEmpDeductDtl.setCurrIntAmt(rpAmt11Map.get(tmp));
			} else {
				tEmpDeductDtl.setInterest(BigDecimal.ZERO);
				tEmpDeductDtl.setCurrIntAmt(BigDecimal.ZERO);
			}
			if (rpAmt4DMap.get(tmp) != null) {
				tEmpDeductDtl.setSumOvpayAmt(rpAmt4DMap.get(tmp));
			} else {
				tEmpDeductDtl.setSumOvpayAmt(BigDecimal.ZERO);
			}

			if (jsonField.get(tmp2) != null) {
				this.info("jsonField ... " + jsonField.toString());
				tEmpDeductDtl.setJsonFields(jsonField.get(tmp2));
			}

			try {
				empDeductDtlService.insert(tEmpDeductDtl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
			}
			if (tmp.getAchRepayCode() == 1 || tmp.getAchRepayCode() == 5) {
				cnt++;
			}
		}
	}

//	flag = 1.15日 2.非15
	private long setReportInsuFee(int imediaDate, List<String> iProcCode, int flag, TitaVo titaVo)
			throws LogicException {

		return l4510Report.exec(imediaDate, iProcCode, flag, titaVo);
	}

	private long setReportAcctFee(int imediaDate, List<String> iProcCode, int flag, TitaVo titaVo)
			throws LogicException {

		return l4510Report2.exec(imediaDate, iProcCode, flag, titaVo);
	}

	private long setReportMedia(int imediaDate, List<String> iProcCode, int flag, TitaVo titaVo) throws LogicException {
		this.info("setReportMedia Start... ");
		this.info("imediaDate ... " + imediaDate);
		this.info("iProcCode ... " + iProcCode);
		this.info("flag ... " + flag);

		return l4510Report3.exec(imediaDate, iProcCode, flag, titaVo);
	}

//		[flag = 4:15日 ; 5:非15日]
	private void setEmpDeductMedia(List<EmpDeductDtl> lEmpDeductDtl, int flag, TitaVo titaVo) throws LogicException {

		if (lEmpDeductDtl == null || lEmpDeductDtl.size() == 0) {
			return;
		}

		this.info("setEmpDeductMedia Start... ");

		HashMap<tmpCustRpCd, BigDecimal> repayAmt = new HashMap<>();
		lEmpDeductDtl = new ArrayList<EmpDeductDtl>(lEmpDeductDtl);
		lEmpDeductDtl.sort((c1, c2) -> {
			int result = 0;
//			入帳順序: 入帳日ASC > 戶號ASC > 扣款類別ASC (火險往前，因其他費用包含在期款內)
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

//		合計金額 火險、期款(其他費用)
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

		delesEmpDeductMedia = empDeductMediaService.mediaDateRng(this.getTxBuffer().getTxCom().getTbsdyf(),
				this.getTxBuffer().getTxCom().getTbsdyf(), "" + flag, this.index, this.limit, titaVo);

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
		for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
			EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();
			EmpDeductMediaId tEmpDeductMediaId = new EmpDeductMediaId();

//			group by CustNo, RepayCode
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
			tEmpDeductMedia.setTxAmt(BigDecimal.ZERO);
			tEmpDeductMedia.setErrorCode("");
			if (tEmpDeductDtl.getAchRepayCode() == 5) {
				tEmpDeductMedia.setAcctCode("000");
			} else {
				tEmpDeductMedia.setAcctCode(custAcctCode.get(tEmpDeductDtl.getCustNo()));
			}
			tEmpDeductMedia.setAcDate(0);
			tEmpDeductMedia.setBatchNo("");
			tEmpDeductMedia.setDetailSeq(0);

			this.info("tEmpDeductMedia.insert = " + tEmpDeductMedia);
			try {
				empDeductMediaService.insert(tEmpDeductMedia, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
			}

			updateEmpDeductDtl(tEmpDeductDtl.getEmpDeductDtlId(), seq, titaVo);
		}
	}

	private void updateEmpDeductDtl(EmpDeductDtlId tEmpDeductDtlId, int seq, TitaVo titaVo) throws LogicException {
		this.info("updateEmpDeductDtl.tEmpDeductDtlId = " + tEmpDeductDtlId);
		EmpDeductDtl t2EmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtlId, titaVo);
		t2EmpDeductDtl.setMediaSeq(seq);
		try {
			empDeductDtlService.update(t2EmpDeductDtl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "員工扣薪檔更新失敗 :" + e.getErrorMsg());
		}
	}

//	暫時紀錄 媒體日期 戶號-額度-撥款 扣款代碼
	private class tmpCustRpCd {

//		private int entryDate = 0;
		private int mediaDate = 0;
		private int custNo = 0;
		private String repayFlag = "";

		public tmpCustRpCd(int mediaDate, int custNo, String repayFlag) {
//			this.setEntryDate(entryDate);
			this.setMediaDate(mediaDate);
			this.setCustNo(custNo);
			this.setRepayFlag(repayFlag);
		}

//		private void setEntryDate(int entryDate) {
//			this.entryDate = entryDate;
//		}

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

		private L4510Batch getEnclosingInstance() {
			return L4510Batch.this;
		}

		@Override
		public String toString() {
			return "tmpCustRpCd [mediaDate=" + mediaDate + ", custNo=" + custNo + ", repayFlag=" + repayFlag + "]";
		}
	}

	private void deleEmpDeductDtl(String MediaKind, int iMediaDate, TitaVo titaVo) throws LogicException {
		Slice<EmpDeductDtl> slEmpDeductDtl = empDeductDtlService.mediaDateRng(iMediaDate, MediaKind, this.index,
				this.limit, titaVo);

		if (slEmpDeductDtl != null) {
			for (EmpDeductDtl tEmpDeductDtl : slEmpDeductDtl.getContent()) {
				tEmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtl.getEmpDeductDtlId(), titaVo);
				try {
					empDeductDtlService.delete(tEmpDeductDtl, titaVo);
					this.info("deleEmpDeductDtl =" + tEmpDeductDtl);
				} catch (DBException e) {
					throw new LogicException("E0008", "員工扣薪檔刪除失敗 :" + e.getErrorMsg());
				}
			}
		}
	}

	private void deleEmpDeductMedia(String MediaKind, int iMediaDate, TitaVo titaVo) throws LogicException {

		Slice<EmpDeductMedia> slEmpDeductMedia = empDeductMediaService.mediaDateRng(iMediaDate, iMediaDate, MediaKind,
				this.index, this.limit, titaVo);

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

//	flag 1:15日薪 2:非15日薪
	private void setBatxValue(List<BaTxVo> listBaTxVo, int flag, int procCode) throws LogicException {
		this.info("setBatxValue Start ...");

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			for (BaTxVo tBaTxVo : listBaTxVo) {

				tmpFacm tmp = new tmpFacm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(),
						tBaTxVo.getRepayType(), flag, procCode);

				tmpFacm tmp2 = new tmpFacm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(), 0, flag,
						procCode);

//				20210409修改為抓取全部撥款且費用，by淑薇電話確認
//				非15日僅製作期款媒體
//				if ("2".equals(flag) && tBaTxVo.getRepayType() != 1) {
//					this.info(tBaTxVo.getCustNo() + "-" + tBaTxVo.getFacmNo() + " 非15日僅製作期款媒體...");
//					continue;
//				}

				if (!mapFlag.containsKey(tmp)) {
					mapFlag.put(tmp, 1);
				}

				this.info("DataKind : " + tBaTxVo.getDataKind());
				this.info("RepayType : " + tBaTxVo.getRepayType());

				int minNextIntDate = tBaTxVo.getIntStartDate();
				if (minNextPayIntDateList.containsKey(tmp)) {
					if (minNextIntDate < minNextPayIntDateList.get(tmp)) {
						minNextPayIntDateList.put(tmp, minNextIntDate);
					}
				} else {
					minNextPayIntDateList.put(tmp, minNextIntDate);
				}

				if (tBaTxVo.getDataKind() == 2 && tBaTxVo.getRepayType() == 1) {

					if (!intStartDate.containsKey(tmp)) {
						intStartDate.put(tmp, tBaTxVo.getIntStartDate());
					} else {
						if (tBaTxVo.getIntStartDate() < intStartDate.get(tmp))
							intStartDate.put(tmp, tBaTxVo.getIntStartDate());
					}

					if (!intEndDate.containsKey(tmp)) {
						intEndDate.put(tmp, tBaTxVo.getIntEndDate());
					} else {
						if (tBaTxVo.getIntEndDate() > intEndDate.get(tmp))
							intEndDate.put(tmp, tBaTxVo.getIntEndDate());
					}

//					本金
					if (!rpAmt10Map.containsKey(tmp)) {
						rpAmt10Map.put(tmp, tBaTxVo.getPrincipal());
					} else {
						rpAmt10Map.put(tmp, rpAmt10Map.get(tmp).add(tBaTxVo.getPrincipal()));
					}

//					利息
					if (!rpAmt11Map.containsKey(tmp)) {
						rpAmt11Map.put(tmp, tBaTxVo.getInterest());
					} else {
						rpAmt11Map.put(tmp, rpAmt11Map.get(tmp).add(tBaTxVo.getInterest()));
					}
//					期款
					if (!rpAmt01Map.containsKey(tmp)) {
						rpAmt01Map.put(tmp, tBaTxVo.getUnPaidAmt());
					} else {
						rpAmt01Map.put(tmp, rpAmt01Map.get(tmp).add(tBaTxVo.getUnPaidAmt()));
					}

//					違約金  --結算至撥款，用tmp2
					if (!rpAmt1CMap.containsKey(tmp2)) {
						rpAmt1CMap.put(tmp2, tBaTxVo.getBreachAmt().add(tBaTxVo.getDelayInt()));
					} else {
						rpAmt1CMap.put(tmp2,
								rpAmt1CMap.get(tmp2).add(tBaTxVo.getBreachAmt().add(tBaTxVo.getDelayInt())));
					}

//					帳管費
				} else if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 4) {
					if (!rpAmt04Map.containsKey(tmp)) {
						rpAmt04Map.put(tmp, tBaTxVo.getUnPaidAmt());
						bormMap.put(tmp, parse.stringToInteger(tBaTxVo.getRvNo().substring(0, 3)));
					} else {
						rpAmt04Map.put(tmp, rpAmt04Map.get(tmp).add(tBaTxVo.getUnPaidAmt()));
					}
//					契變手續費
				} else if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 6) {
					if (!rpAmt06Map.containsKey(tmp)) {
						rpAmt06Map.put(tmp, tBaTxVo.getUnPaidAmt());
					} else {
						rpAmt06Map.put(tmp, rpAmt06Map.get(tmp).add(tBaTxVo.getUnPaidAmt()));
					}
//					火險費
				} else if (tBaTxVo.getDataKind() == 1 && tBaTxVo.getRepayType() == 5) {
					if (!rpAmt05Map.containsKey(tmp)) {
						rpAmt05Map.put(tmp, tBaTxVo.getUnPaidAmt());
						insuNoMap.put(tmp, tBaTxVo.getRvNo());
					} else {
						rpAmt05Map.put(tmp, rpAmt05Map.get(tmp).add(tBaTxVo.getUnPaidAmt()));
						insuNoMap.put(tmp, insuNoMap.get(tmp) + "," + tBaTxVo.getRvNo());
					}
//					短收 --結算至撥款，用tmp2
				} else if (tBaTxVo.getDataKind() == 4 && "D".equals(tBaTxVo.getDbCr())) {
					if (!rpAmt4DMap.containsKey(tmp)) {
						rpAmt4DMap.put(tmp, tBaTxVo.getUnPaidAmt());
					} else {
						rpAmt4DMap.put(tmp, rpAmt4DMap.get(tmp).add(tBaTxVo.getUnPaidAmt()));
					}
//					溢收
				} else if (tBaTxVo.getDataKind() == 4 && "C".equals(tBaTxVo.getDbCr())) {
					if (!rpAmt4CMap.containsKey(tmp)) {
						rpAmt4CMap.put(tmp, tBaTxVo.getUnPaidAmt());
					} else {
						rpAmt4CMap.put(tmp, rpAmt4CMap.get(tmp).add(tBaTxVo.getUnPaidAmt()));
					}
				} else if (tBaTxVo.getReceivableFlag() == 4) {
//					欠繳本金 --結算至撥款，用tmp2
					if (!rpAmt41Map.containsKey(tmp2)) {
						rpAmt41Map.put(tmp2, tBaTxVo.getPrincipal());
					} else {
						rpAmt41Map.put(tmp2, rpAmt41Map.get(tmp2).add(tBaTxVo.getPrincipal()));
					}
//					欠繳利息 --結算至撥款，用tmp2
					if (!rpAmt42Map.containsKey(tmp2)) {
						rpAmt42Map.put(tmp2, tBaTxVo.getInterest());
					} else {
						rpAmt42Map.put(tmp2, rpAmt42Map.get(tmp2).add(tBaTxVo.getInterest()));
					}
//					暫收抵繳 --結算至第一筆撥款，用tmp2(到撥款) tmp3(到額度)
				} else if (tBaTxVo.getDataKind() == 3) {
					if (!rpAmt30Map.containsKey(tmp2)) {
						rpAmt30Map.put(tmp2, tBaTxVo.getUnPaidAmt());
					} else {
						rpAmt30Map.put(tmp2, rpAmt30Map.get(tmp2).add(tBaTxVo.getUnPaidAmt()));
					}
				}
			}
		} else {
			this.info("listBaTxVo is null");
		}
	}

	private void calculateY15BaTxCom(TitaVo titaVo) throws LogicException {
		if (mediaDate > 19110000) {
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				resultList = l4510ServiceImpl.findAll(1, titaVo);
			} catch (Exception e) {
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() != 0) {
				doBaTxCom(1, resultList, titaVo);
			}
		}
	}

	private void calculateN15BaTxCom(TitaVo titaVo) throws LogicException {
		if (mediaDate > 19110000) {
//			QC.616 非15扣薪 滯繳兩期者，而非全部
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				resultList = l4510ServiceImpl.findAll(2, titaVo);
			} catch (Exception e) {
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() != 0) {
				doBaTxCom(2, resultList, titaVo);
			}
		}
	}
}