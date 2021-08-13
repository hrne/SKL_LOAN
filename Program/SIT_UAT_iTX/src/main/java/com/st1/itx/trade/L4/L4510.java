package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
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
import com.st1.itx.db.domain.TxToDoDetail;
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

@Service("L4510")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4510 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L4510.class);

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
	public WebClient webClient;

	private int iY15EntryDate = 0;
	private int iN15EntryDate = 0;
	private int delayDate = 0;

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

	private HashMap<tmpFacm, String> rpAcCodeMap = new HashMap<>();
	private HashMap<tmpFacm, String> insuNoMap = new HashMap<>();
	private HashMap<tmpFacm, Integer> bormMap = new HashMap<>();

	int mediaDate = 0;

	private HashMap<String, Integer> perfMonth = new HashMap<>();

	private HashMap<tmpFacm, String> errMsg = new HashMap<>();
	private HashMap<tmpFacm, Integer> flagMap = new HashMap<>();
	private TempVo tempVo = new TempVo();
	private HashMap<tmpFacm, String> jsonField = new HashMap<>();
	private HashMap<tmpFacm, Integer> mapSetFlag = new HashMap<>();
//	重複註記
	private HashMap<tmpFacm, Integer> flagMap2 = new HashMap<>();

	private long reportA = 0;
	private long reportB = 0;
	private long reportC = 0;
	private long reportD = 0;
	private long reportE = 0;
	private long reportF = 0;

	private String sendMsg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4510 ");
		this.totaVo.init(titaVo);

		baTxCom.setTxBuffer(this.getTxBuffer());
		txToDoCom.setTxBuffer(this.getTxBuffer());

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		List<String> procCodeIs15 = new ArrayList<String>();
		List<String> procCodeUn15 = new ArrayList<String>();

//		1.清除原扣款檔
//		2.從主檔撈資料
//		3.BatxCom算金額
//		4.寫入扣款檔
//		5.產報表
//		6.報表下載功能(BS需增加程式完成，並指向產檔位置)

//		根據輸入之入帳日查詢BORM(ACDATE)->FACM(REPAYCODE:03)寫入empdtl
		mediaDate = parse.stringToInteger(titaVo.get("MediaDate")) + 19110000;

		List<EmpDeductSchedule> lEmpDeductSchedule = new ArrayList<EmpDeductSchedule>();

		Slice<EmpDeductSchedule> sEmpDeductSchedule = null;

//		抓取媒體日為今日者
		sEmpDeductSchedule = empDeductScheduleService.mediaDateRange(mediaDate, mediaDate, this.index, this.limit,
				titaVo);

		lEmpDeductSchedule = sEmpDeductSchedule == null ? null : sEmpDeductSchedule.getContent();

		if (lEmpDeductSchedule != null && lEmpDeductSchedule.size() != 0) {
			for (EmpDeductSchedule tEmpDeductSchedule : lEmpDeductSchedule) {
				perfMonth.put(tEmpDeductSchedule.getAgType1(), tEmpDeductSchedule.getWorkMonth());

				CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tEmpDeductSchedule.getAgType1(),
						titaVo);
//				1.15日薪 2.非15日薪
				if ("2".equals(tCdCode.getItem().substring(0, 1))) {
					procCodeUn15.add(tEmpDeductSchedule.getAgType1());
					iN15EntryDate = tEmpDeductSchedule.getEntryDate() + 19110000;
//					iN15EntryDate = 20200720;
				} else if ("1".equals(tCdCode.getItem().substring(0, 1))) {
					procCodeIs15.add(tEmpDeductSchedule.getAgType1());
					iY15EntryDate = tEmpDeductSchedule.getEntryDate() + 19110000;
				}
			}

			this.info("iN15EntryDate ... " + iN15EntryDate);
			this.info("iY15EntryDate ... " + iY15EntryDate);

//			刪除應處理事項清單
//			0.新增 1.刪除
			txToDo(titaVo, 1);
		} else {
			throw new LogicException("E0001 ", "查無資料");
		}

//		先刪除舊資料
		deleEmpDeductDtl(procCodeUn15, iN15EntryDate);
		deleEmpDeductDtl(procCodeIs15, iY15EntryDate);

		if (iY15EntryDate > 19110000) {
			calculateY15BaTxCom(titaVo);
		}
//		若15 非15同天，則會有後做將前做過在setEmpDtl一次
		mapFlag = new HashMap<>();

		if (iN15EntryDate > 19110000) {
			calculateN15BaTxCom(titaVo);
		}

//		3.產出火險(05)、帳管(04)、明細表
		List<EmpDeductDtl> is15EmpDeductDtl = new ArrayList<EmpDeductDtl>();
		List<EmpDeductDtl> un15EmpDeductDtl = new ArrayList<EmpDeductDtl>();

		Slice<EmpDeductDtl> sis15EmpDeductDtl = null;
		Slice<EmpDeductDtl> sun15EmpDeductDtl = null;

		sun15EmpDeductDtl = empDeductDtlService.entryDateRng(iN15EntryDate, iN15EntryDate, procCodeUn15, this.index,
				this.limit);
		sis15EmpDeductDtl = empDeductDtlService.entryDateRng(iY15EntryDate, iY15EntryDate, procCodeIs15, this.index,
				this.limit);

		is15EmpDeductDtl = sis15EmpDeductDtl == null ? null : sis15EmpDeductDtl.getContent();
		un15EmpDeductDtl = sun15EmpDeductDtl == null ? null : sun15EmpDeductDtl.getContent();

		if (un15EmpDeductDtl != null && un15EmpDeductDtl.size() != 0) {
			this.info("Un15 Dtl Start...");
//			4.寫入EmpDeductMedia (彙總by戶號) 5:非15日
			setEmpDeductMedia(un15EmpDeductDtl, 5);
//			A B C
//			火險費
			try {
				reportA = setReportInsuFee(iN15EntryDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
//			帳管費
			try {
				reportB = setReportAcctFee(iN15EntryDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

			try {
				reportC = setReportMedia(iN15EntryDate, procCodeUn15, 2, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

		} else {
			this.info("un15EmpDeductDtl is null");
		}

		if (is15EmpDeductDtl != null && is15EmpDeductDtl.size() != 0) {
			this.info("Is15 Dtl Start...");
//			4.寫入EmpDeductMedia (彙總by戶號) 4:15日
			setEmpDeductMedia(is15EmpDeductDtl, 4);
//			D E F
//			火險費
			try {
				reportD = setReportInsuFee(iY15EntryDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}
//			帳管費
			try {
				reportE = setReportAcctFee(iY15EntryDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

			try {
				reportF = setReportMedia(iY15EntryDate, procCodeIs15, 1, titaVo);
			} catch (LogicException e1) {
				throw new LogicException("E0008 ", e1.getErrorMsg());
			}

		} else {
			this.info("is15EmpDeductDtl is null");
		}

//		由var判斷下載按鈕是否呈現
		totaVo.put("OReportA", "" + reportA);
		totaVo.put("OReportB", "" + reportB);
		totaVo.put("OReportC", "" + reportC);
		totaVo.put("OReportD", "" + reportD);
		totaVo.put("OReportE", "" + reportE);
		totaVo.put("OReportF", "" + reportF);

//		0.新增 1.刪除
		txToDo(titaVo, 0);

		sendMsg = "L4510-報表已完成";

		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
				sendMsg, titaVo);

		this.addList(totaVo);
		return this.sendList();
	}

//	用既有之List for each 找出RepayCode 1.期款  4.帳管 5.火險 6.契變手續費 
	private void doBaTxCom(List<Map<String, String>> resultList, TitaVo titaVo) throws LogicException {
		List<BaTxVo> listBaTxVo = new ArrayList<>();

		baTxCom.setTxBuffer(this.getTxBuffer());

		for (Map<String, String> result : resultList) {
//			F0 CustNo
//			F1 FacmNo
//			F2 AcctCode
//			F3 1.15日薪 2.非15日薪
//			F4 BormNo (逾兩期需指到撥款，無需扣費用)

			tmpFacm tmp2 = new tmpFacm(parse.stringToInteger(result.get("F0")), parse.stringToInteger(result.get("F1")),
					0, 0);

//			非15日僅抓取逾兩期之撥款
//			Ex.若當月n-1-1逾一期，n-1-2撥款，下個月僅收n-1-1
//			20210409修改為抓取全部撥款且費用，by淑薇電話確認
			if (flagMap2.containsKey(tmp2)) {
				this.info("custNo = " + parse.stringToInteger(result.get("F0")) + " facmNo = "
						+ parse.stringToInteger(result.get("F1")) + " 同戶號額度僅進入計算一次 continue...");
				continue;
			} else {
				flagMap2.put(tmp2, 1);
			}

			if ("2".equals(result.get("F3"))) {
				listBaTxVo = baTxCom.settingUnPaid(iN15EntryDate - 19110000, parse.stringToInteger(result.get("F0")),
						parse.stringToInteger(result.get("F1")), 0, 1, BigDecimal.ZERO, titaVo);
			} else {
				listBaTxVo = baTxCom.settingUnPaid(iY15EntryDate - 19110000, parse.stringToInteger(result.get("F0")),
						parse.stringToInteger(result.get("F1")), 0, 1, BigDecimal.ZERO, titaVo);
			}
			setBatxValue(listBaTxVo, result.get("F2"), result.get("F3"));
		}

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

		public tmpFacm(int custNo, int facmNo, int bormNo, int achRepayCode) {
			this.setCustNo(custNo);
			this.setFacmNo(facmNo);
			this.setBormNo(bormNo);
			this.setAchRepayCode(achRepayCode);
		}

		private int custNo = 0;
		private int facmNo = 0;
		private int BormNo = 0;
		private int achRepayCode = 0;

		@Override
		public String toString() {
			return "tmpFacm [custNo=" + custNo + ", facmNo=" + facmNo + ", BormNo=" + BormNo + ", achRepayCode="
					+ achRepayCode + "]";
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

		private L4510 getEnclosingInstance() {
			return L4510.this;
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

			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(tmp.getCustNo(), tmp.getCustNo());

			CdEmp tCdEmp = new CdEmp();
			tCdEmp = cdEmpService.findById(tCustMain.getEmpNo());
			this.info("tCdEmp.getAgType1() : " + tCdEmp.getAgType1());

			CdCode tCdCode = cdCodeService.getItemFirst(4, "EmpDeductType", tCdEmp.getAgType1(), titaVo);

			int month = 0;

			if (perfMonth.get(tCdEmp.getAgType1()) != null) {
				month = perfMonth.get(tCdEmp.getAgType1());
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

			tmpFacm tmp2 = new tmpFacm(tmp.getCustNo(), tmp.getFacmNo(), tmp.getBormNo(), 0);
			tmpFacm tmp3 = new tmpFacm(tmp.getCustNo(), tmp.getFacmNo(), 0, 0);

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
				tempVo.putParam("LeglFee", rpAmt1CMap.get(tmp2));
			}
			if (rpAmt41Map.get(tmp2) != null) {
				tempVo.putParam("OvduAmt", rpAmt41Map.get(tmp2));
			}
			if (rpAmt42Map.get(tmp2) != null) {
				tempVo.putParam("OvduBal", rpAmt42Map.get(tmp2));
			}

			this.info("tempVo ... " + tempVo.toString());

			if (tempVo.getJsonString() != null && !"".equals(tempVo.getJsonString().trim())) {
//				if (jsonField.get(tmp2) != null && !"".equals(jsonField.toString().trim())) {
//					jsonField.put(tmp2, jsonField.get(tmp2) + tempVo.getJsonString());
//				} else {
//					this.info("tempVo ... " + tempVo.getJsonString());
					jsonField.put(tmp2, tempVo.getJsonString());
//				}
			}

//			1.15日薪 2.非15日薪
			if ("2".equals(tCdCode.getItem().substring(0, 1))) {
				tEmpDeductDtlId.setEntryDate(iN15EntryDate);
			} else {
				tEmpDeductDtlId.setEntryDate(iY15EntryDate);
			}
			tEmpDeductDtlId.setCustNo(tmp.getCustNo());
			tEmpDeductDtlId.setAchRepayCode(tmp.getAchRepayCode());

			tEmpDeductDtlId.setPerfMonth(month);
			tEmpDeductDtlId.setProcCode(tCdEmp.getAgType1());

//			QC.623 非15薪扣款代碼應為滯繳件
			if ("2".equals(tCdCode.getItem().substring(0, 1))) {
				tEmpDeductDtlId.setRepayCode("3");
			} else {
				tEmpDeductDtlId.setRepayCode("1"); // ???
			}
			tEmpDeductDtlId.setAcctCode(rpAcCodeMap.get(tmp));
			tEmpDeductDtlId.setFacmNo(tmp.getFacmNo());
			tEmpDeductDtlId.setBormNo(tmp.getBormNo());

			tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);
			tEmpDeductDtl.setEmpNo(tCdEmp.getEmployeeNo());
			tEmpDeductDtl.setTitaTlrNo(this.getTxBuffer().getTxCom().getRelTlr());
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

			tEmpDeductDtl.setTxAmt(txAmt);
			tEmpDeductDtl.setRepayAmt(txAmt);

			tEmpDeductDtl.setErrMsg(errMsg.get(tmp));
			tEmpDeductDtl.setAcdate(0);
			tEmpDeductDtl.setTitaTxtNo("" + this.getTxBuffer().getTxCom().getRelTno());
			tEmpDeductDtl.setBatchNo("");
			tEmpDeductDtl.setResignCode(tCdEmp.getAgStatusCode());
			tEmpDeductDtl.setDeptCode(tCdEmp.getCenterCodeAcc2());
			tEmpDeductDtl.setUnitCode(tCdEmp.getCenterCodeAcc());
			if (intStartDate.get(tmp) != null)
				tEmpDeductDtl.setIntStartDate(intStartDate.get(tmp));
			if (intEndDate.get(tmp) != null)
				tEmpDeductDtl.setIntEndDate(intEndDate.get(tmp));
			tEmpDeductDtl.setPositCode(tCdEmp.getAgPost());
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
				empDeductDtlService.insert(tEmpDeductDtl);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
			}
		}
	}

//	flag = 1.15日 2.非15
	private long setReportInsuFee(int iEntryDate, List<String> iProcCode, int flag, TitaVo titaVo)
			throws LogicException {

		return l4510Report.exec(iEntryDate, iProcCode, flag, titaVo);
	}

	private long setReportAcctFee(int iEntryDate, List<String> iProcCode, int flag, TitaVo titaVo)
			throws LogicException {

		return l4510Report2.exec(iEntryDate, iProcCode, flag, titaVo);
	}

	private long setReportMedia(int iEntryDate, List<String> iProcCode, int flag, TitaVo titaVo) throws LogicException {
		this.info("setReportMedia Start... ");
		this.info("iEntryDate ... " + iEntryDate);
		this.info("iProcCode ... " + iProcCode);
		this.info("flag ... " + flag);

		return l4510Report3.exec(iEntryDate, iProcCode, flag, titaVo);
	}

//		[flag = 4:15日 ; 5:非15日]
	private void setEmpDeductMedia(List<EmpDeductDtl> lEmpDeductDtl, int flag) throws LogicException {

		this.info("setEmpDeductMedia Start... ");

		HashMap<tmpCustRpCd, BigDecimal> repayAmt = new HashMap<>();
		lEmpDeductDtl = new ArrayList<EmpDeductDtl>(lEmpDeductDtl);
		lEmpDeductDtl.sort((c1, c2) -> {
			int result = 0;
//			入帳順序: 入帳日ASC > 戶號ASC > 扣款類別ASC (火險往前，因其他費用包含在期款內)
			if (c1.getEmpDeductDtlId().getEntryDate() - c2.getEmpDeductDtlId().getEntryDate() != 0) {
				result = c1.getEmpDeductDtlId().getEntryDate() - c2.getEmpDeductDtlId().getEntryDate();
			} else if (c1.getEmpDeductDtlId().getCustNo() - c2.getEmpDeductDtlId().getCustNo() != 0) {
				result = c1.getEmpDeductDtlId().getCustNo() - c2.getEmpDeductDtlId().getCustNo();
			} else if (c1.getEmpDeductDtlId().getAchRepayCode() != c2.getEmpDeductDtlId().getAchRepayCode()) {
				if (c1.getEmpDeductDtlId().getAchRepayCode() == 5) {
					result = -1;
				} else if (c2.getEmpDeductDtlId().getAchRepayCode() == 5) {
					result = 1;
				} else {
					result = c1.getEmpDeductDtlId().getAchRepayCode() - c2.getEmpDeductDtlId().getAchRepayCode();
				}
			} else {
				result = 0;
			}
			return result;
		});

//		合計金額 火險、期款(其他費用)
		for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
			this.info("tEmpDeductDtl.getEntryDate() : " + tEmpDeductDtl.getEmpDeductDtlId().getEntryDate());
			this.info("tEmpDeductDtl.getCustNo() : " + tEmpDeductDtl.getEmpDeductDtlId().getCustNo());
			this.info("tEmpDeductDtl.getAchRepayCode() : " + tEmpDeductDtl.getEmpDeductDtlId().getAchRepayCode());

			String rpcd = "";

			if (tEmpDeductDtl.getEmpDeductDtlId().getAchRepayCode() == 5) {
				rpcd = "92";
			} else {
				rpcd = "XH";
			}

			tmpCustRpCd tmp = new tmpCustRpCd(tEmpDeductDtl.getEmpDeductDtlId().getEntryDate(),
					tEmpDeductDtl.getEmpDeductDtlId().getCustNo(), rpcd);
			this.info("tmp : " + tmp.toString());

			if (repayAmt.containsKey(tmp)) {
				repayAmt.put(tmp, repayAmt.get(tmp).add(tEmpDeductDtl.getRepayAmt()));
			} else {
				repayAmt.put(tmp, tEmpDeductDtl.getRepayAmt());
			}
			this.info("repayAmt : " + repayAmt.get(tmp));
		}
		List<EmpDeductMedia> deleEmpDeductMedia = new ArrayList<EmpDeductMedia>();

		Slice<EmpDeductMedia> delesEmpDeductMedia = null;

		delesEmpDeductMedia = empDeductMediaService.mediaDateRng(this.getTxBuffer().getTxCom().getTbsdyf(),
				this.getTxBuffer().getTxCom().getTbsdyf(), "" + flag, this.index, this.limit);

		deleEmpDeductMedia = delesEmpDeductMedia == null ? null : delesEmpDeductMedia.getContent();

		if (deleEmpDeductMedia != null && deleEmpDeductMedia.size() != 0) {
			if (deleEmpDeductMedia.get(0).getAcDate() > 0) {
				throw new LogicException("E0008", "已入帳");
			}
			try {
				empDeductMediaService.deleteAll(deleEmpDeductMedia);
			} catch (DBException e1) {
				throw new LogicException("E0008", e1.getErrorMsg());
			}
		}

		int todayF = this.getTxBuffer().getTxCom().getTbsdyf();
		HashMap<tmpCustRpCd, Integer> empCnt = new HashMap<>();
		int seq = 0;
		for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
			EmpDeductMedia tEmpDeductMedia = new EmpDeductMedia();
			EmpDeductMediaId tEmpDeductMediaId = new EmpDeductMediaId();

//			group by CustNo, RepayCode
			String rpcd = "";

			if (tEmpDeductDtl.getEmpDeductDtlId().getAchRepayCode() == 5) {
				rpcd = "92";
			} else {
				rpcd = "XH";
			}

			tmpCustRpCd tmp = new tmpCustRpCd(tEmpDeductDtl.getEmpDeductDtlId().getEntryDate(),
					tEmpDeductDtl.getEmpDeductDtlId().getCustNo(), rpcd);

			if (empCnt.containsKey(tmp)) {
				updateEmpDeductDtl(tEmpDeductDtl, todayF, flag, seq);
				this.info("tmp 已進入 continue ..." + tmp);
				continue;
			} else {
				empCnt.put(tmp, 1);
			}

			seq = seq + 1;

			tEmpDeductMediaId.setMediaDate(todayF);
			tEmpDeductMediaId.setMediaKind("" + flag);
			tEmpDeductMediaId.setMediaSeq(seq);

			tEmpDeductMedia.setEmpDeductMediaId(tEmpDeductMediaId);
			tEmpDeductMedia.setCustNo(tEmpDeductDtl.getEmpDeductDtlId().getCustNo());
			tEmpDeductMedia.setRepayCode(tEmpDeductDtl.getEmpDeductDtlId().getAchRepayCode());
			tEmpDeductMedia.setPerfRepayCode(parse.stringToInteger(tEmpDeductDtl.getEmpDeductDtlId().getRepayCode()));
			tEmpDeductMedia.setRepayAmt(repayAmt.get(tmp));
			tEmpDeductMedia.setPerfMonth(tEmpDeductDtl.getEmpDeductDtlId().getPerfMonth());
			tEmpDeductMedia.setFlowCode(tEmpDeductDtl.getEmpDeductDtlId().getProcCode());
			tEmpDeductMedia.setUnitCode(tEmpDeductDtl.getUnitCode());
			tEmpDeductMedia.setCustId(tEmpDeductDtl.getCustId());
			tEmpDeductMedia.setEntryDate(tEmpDeductDtl.getEmpDeductDtlId().getEntryDate());
			tEmpDeductMedia.setTxAmt(BigDecimal.ZERO);
			tEmpDeductMedia.setErrorCode("");
			tEmpDeductMedia.setAcctCode(tEmpDeductDtl.getEmpDeductDtlId().getAcctCode());
			tEmpDeductMedia.setAcDate(0);
			tEmpDeductMedia.setBatchNo("");
			tEmpDeductMedia.setDetailSeq(0);

			try {
				empDeductMediaService.insert(tEmpDeductMedia);
			} catch (DBException e) {
				throw new LogicException("E0005", "員工扣薪檔新增失敗 :" + e.getErrorMsg());
			}

			updateEmpDeductDtl(tEmpDeductDtl, todayF, flag, seq);
		}
	}

	private void updateEmpDeductDtl(EmpDeductDtl tEmpDeductDtl, int todayF, int flag, int seq) throws LogicException {
		EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();
		tEmpDeductDtlId.setAcctCode(tEmpDeductDtl.getEmpDeductDtlId().getAcctCode());
		tEmpDeductDtlId.setAchRepayCode(tEmpDeductDtl.getEmpDeductDtlId().getAchRepayCode());
		tEmpDeductDtlId.setCustNo(tEmpDeductDtl.getEmpDeductDtlId().getCustNo());
		tEmpDeductDtlId.setEntryDate(tEmpDeductDtl.getEmpDeductDtlId().getEntryDate());
		tEmpDeductDtlId.setPerfMonth(tEmpDeductDtl.getEmpDeductDtlId().getPerfMonth());
		tEmpDeductDtlId.setProcCode(tEmpDeductDtl.getEmpDeductDtlId().getProcCode());
		tEmpDeductDtlId.setRepayCode(tEmpDeductDtl.getEmpDeductDtlId().getRepayCode());
		tEmpDeductDtlId.setFacmNo(tEmpDeductDtl.getEmpDeductDtlId().getFacmNo());
		tEmpDeductDtlId.setBormNo(tEmpDeductDtl.getEmpDeductDtlId().getBormNo());

		EmpDeductDtl t2EmpDeductDtl = new EmpDeductDtl();
		t2EmpDeductDtl = empDeductDtlService.findById(tEmpDeductDtlId);

		empDeductDtlService.holdById(t2EmpDeductDtl.getEmpDeductDtlId());
		t2EmpDeductDtl.setMediaDate(todayF);
		t2EmpDeductDtl.setMediaKind("" + flag);
		t2EmpDeductDtl.setMediaSeq(seq);
		try {
			empDeductDtlService.update(t2EmpDeductDtl);
		} catch (DBException e) {
			throw new LogicException("E0005", "員工扣薪檔更新失敗 :" + e.getErrorMsg());
		}
	}

//	暫時紀錄 入帳日期 戶號-額度-撥款 扣款代碼
	private class tmpCustRpCd {

		private int entryDate = 0;
		private int custNo = 0;
		private String repayFlag = "";

		public tmpCustRpCd(int entryDate, int custNo, String repayFlag) {
			this.setEntryDate(entryDate);
			this.setCustNo(custNo);
			this.setRepayFlag(repayFlag);
		}

		private int getEntryDate() {
			return entryDate;
		}

		private void setEntryDate(int entryDate) {
			this.entryDate = entryDate;
		}

		private int getCustNo() {
			return custNo;
		}

		private void setCustNo(int custNo) {
			this.custNo = custNo;
		}

		private String getRepayFlag() {
			return repayFlag;
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
			result = prime * result + entryDate;
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
			if (entryDate != other.entryDate)
				return false;
			if (repayFlag == null) {
				if (other.repayFlag != null)
					return false;
			} else if (!repayFlag.equals(other.repayFlag))
				return false;
			return true;
		}

		private L4510 getEnclosingInstance() {
			return L4510.this;
		}

		@Override
		public String toString() {
			return "tmpCustRpCd [entryDate=" + entryDate + ", custNo=" + custNo + ", repayFlag=" + repayFlag + "]";
		}
	}

	private void deleEmpDeductDtl(List<String> procCode, int iEntryDate) throws LogicException {
		List<EmpDeductDtl> lEmpDeductDtl = new ArrayList<EmpDeductDtl>();

		Slice<EmpDeductDtl> sEmpDeductDtl = null;

		sEmpDeductDtl = empDeductDtlService.entryDateRng(iEntryDate, iEntryDate, procCode, this.index, this.limit);

		lEmpDeductDtl = sEmpDeductDtl == null ? null : sEmpDeductDtl.getContent();

		if (lEmpDeductDtl != null && lEmpDeductDtl.size() != 0) {
			for (EmpDeductDtl tEmpDeductDtl : lEmpDeductDtl) {
				tEmpDeductDtl = empDeductDtlService.holdById(tEmpDeductDtl.getEmpDeductDtlId());
				try {
					empDeductDtlService.delete(tEmpDeductDtl);
				} catch (DBException e) {
					throw new LogicException("E0008", "員工扣薪檔刪除失敗 :" + e.getErrorMsg());
				}
			}
		}
	}

//	flag 1:15日薪 2:非15日薪
	private void setBatxValue(List<BaTxVo> listBaTxVo, String acctCode, String flag) throws LogicException {
		this.info("setBatxValue Start ...");

		if (listBaTxVo != null && listBaTxVo.size() != 0) {
			for (BaTxVo tBaTxVo : listBaTxVo) {

				tmpFacm tmp = new tmpFacm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(),
						tBaTxVo.getRepayType());

				tmpFacm tmp2 = new tmpFacm(tBaTxVo.getCustNo(), tBaTxVo.getFacmNo(), tBaTxVo.getBormNo(), 0);

//				20210409修改為抓取全部撥款且費用，by淑薇電話確認
//				非15日僅製作期款媒體
//				if ("2".equals(flag) && tBaTxVo.getRepayType() != 1) {
//					this.info(tBaTxVo.getCustNo() + "-" + tBaTxVo.getFacmNo() + " 非15日僅製作期款媒體...");
//					continue;
//				}

				if (!mapFlag.containsKey(tmp)) {
					mapFlag.put(tmp, 1);
				}

//				QC.462 期款科目->抓取額度檔
				if (tBaTxVo.getRepayType() == 1) {
					rpAcCodeMap.put(tmp, acctCode);
				} else {
					rpAcCodeMap.put(tmp, tBaTxVo.getAcctCode());
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

	private int delayTerms(int terms, int date) throws LogicException {
		terms = 0 - terms;

		dateUtil.init();
		dateUtil.setDate_1(date);
		dateUtil.setDate_2(date);
		dateUtil.setMons(terms);
		dateUtil.setDays(-1);

		date = dateUtil.getCalenderDay();

		return date;
	}

	private void calculateY15BaTxCom(TitaVo titaVo) throws LogicException {
		if (iY15EntryDate > 19110000) {
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				resultList = l4510ServiceImpl.findAll(0, iY15EntryDate, 1, titaVo);
			} catch (Exception e) {
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() != 0) {
				doBaTxCom(resultList, titaVo);
			} else {
				throw new LogicException("E0001", "15日薪於撥款主檔無符合資料");
			}
		}
	}

	private void calculateN15BaTxCom(TitaVo titaVo) throws LogicException {
		if (iN15EntryDate > 19110000) {
//			QC.616 非15扣薪 滯繳兩期者，而非全部
			delayDate = delayTerms(2, iN15EntryDate);
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			try {
				resultList = l4510ServiceImpl.findAll(0, delayDate, 2, titaVo);
			} catch (Exception e) {
				throw new LogicException("E0013", e.getMessage());
			}

			if (resultList != null && resultList.size() != 0) {
				doBaTxCom(resultList, titaVo);
			} else {
				throw new LogicException("E0001", "非15日薪於撥款主檔無符合資料");
			}
		}
	}
}