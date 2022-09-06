package com.st1.itx.trade.L5;

//import static java.util.Collections.sort;

//import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
//import java.io.FileWriter;
//import java.util.Vector;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.NegMainService;
/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicAtomDetail;
import com.st1.itx.db.domain.JcicAtomMain;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.domain.JcicZ048Id;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.domain.JcicZ062Id;
import com.st1.itx.db.domain.NegFinAcct;
import com.st1.itx.db.domain.NegFinShare;
import com.st1.itx.db.domain.NegFinShareId;
import com.st1.itx.db.domain.NegFinShareLog;
import com.st1.itx.db.domain.NegFinShareLogId;
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
/*DB服務*/
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicAtomDetailService;
import com.st1.itx.db.service.JcicAtomMainService;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareLogService;
import com.st1.itx.util.common.FileCom;
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.data.DataLog;

/**
 * Tita<br>
 * FilePath=X,20<br>
 */

@Service("L5706")
@Scope("prototype")
/**
 *
 *
 * @author Jacky
 * @version 1.0.0
 */
public class L5706 extends TradeBuffer {
	@Autowired
	public NegFinShareService sNegFinShareService;
	@Autowired
	public NegFinShareLogService sNegFinShareLogService;
	@Autowired
	public NegMainService sNegMainService;
	@Autowired
	public CustMainService sCustMainService;
	@Autowired
	public JcicAtomMainService sJcicAtomMainService;
	@Autowired
	public JcicAtomDetailService sJcicAtomDetailService;
	@Autowired
	public JcicZ048Service sJcicZ048Service;
	@Autowired
	public JcicZ062Service sJcicZ062Service;
	@Autowired
	public NegFinAcctService sNegFinAcctService;
	@Autowired
	public NegCom negCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public FileCom FileCom;

	@Autowired
	public DataLog dataLog;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	private boolean isNewMain = false;// 是否為新案件
	private boolean isZZM262 = false;// 是否為ZZM262
	int errorMsg = 1;// 回報錯誤 1:回報 0:不回報
//	Map<String, String> OccursDataValue = new HashMap<String, String>();
//	String OccursData[] = { "OORow", "OOCode", "OODataType", "OOMainRemark", "OOFiledName", "OOFiledType",
//			"OODetailRemark", "OOValue" };
	private ArrayList<NegFinShare> lNegFinShareM260 = new ArrayList<NegFinShare>(); // 正常
	private ArrayList<NegFinShare> lNegFinShareM262 = new ArrayList<NegFinShare>(); // 單獨受償
	private ArrayList<NegFinShare> lNegFinShareM264 = new ArrayList<NegFinShare>(); // 變更還款條件

	private ArrayList<NegFinShareLog> lNegFinShareLogM260 = new ArrayList<NegFinShareLog>();
	private ArrayList<NegFinShareLog> lNegFinShareLogM262 = new ArrayList<NegFinShareLog>();
	private ArrayList<NegFinShareLog> lNegFinShareLogM264 = new ArrayList<NegFinShareLog>();
	private int CustNo262 = 0;
	private int CaseSeq262 = 0;
	private BigDecimal totalContrAmt = BigDecimal.ZERO;
	private BigDecimal dueAmt = BigDecimal.ZERO;
	private String custid = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5706 ");
		this.totaVo.init(titaVo);
		this.info("L5706 TitaVo=[" + titaVo + "]");
		// 路徑
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();

		/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
		this.index = titaVo.getReturnIndex();
		this.info("L5706 First this.index=[" + this.index + "]");

		/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
		this.limit = 1;// Integer.MAX_VALUE查全部
		// 目前occurst長度
		// int OccursL=1030;

		ArrayList<String> lBr = new ArrayList<>();
		// 編碼參數，設定為UTF-8 || big5
		try {
			lBr = FileCom.intputTxt(FilePath, "big5");
		} catch (IOException e) {
			this.info("L5706(" + FilePath + ") : " + e.getMessage());
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + FilePath;
			// E5006 未預期的錯誤
			throw new LogicException(titaVo, "E5006", ErrorMsg);
		}

		try {

			int fileLength = lBr.size();
			this.info("fileLength==" + fileLength);

			for (int i = 0; i < fileLength; i++) {

				String ThisLine = lBr.get(i);
				this.info("ThisLine == [" + ThisLine + "]");

				// 資料檢核
				if (i == 0) {
					if (ThisLine.indexOf("JCIC-INQ-BARE-V01-458") != 0) {
						// E5009 資料檢核錯誤
						if (errorMsg == 1) {
							throw new LogicException(titaVo, "E5009", "首筆資料表頭格式不正確");
						}
					}
					continue;
				}

				// 結尾須把最後一個客戶分攤檔資料寫入

				this.info("i==" + i);
				if (i == fileLength - 1) {
					if (ThisLine.length() > 4 && ("TRLR").equals(ThisLine.substring(0, 4))) {
						doUpdate(titaVo);
					} else {
						if (errorMsg == 1) {
							throw new LogicException(titaVo, "E5009", "尾筆資料表頭格式不正確");
						}
					}
				}
				// TR 是結尾的記號
				if (ThisLine != null && ThisLine.length() != 0 && !("TR").equals(ThisLine.substring(0, 2))) {

					String Code = ThisLine.substring(33, 39);
					this.info("L5706 Code=[" + (Code) + "]");
					String ThisLineUse = ThisLine.substring(39, ThisLine.length());

					Map<String, String[]> mLineCut = cutSkill(Code, ThisLineUse, titaVo);
					if (mLineCut == null) {
						continue;
					}

//						ZZM260 各債權金融機構無擔保債權暨還款分配資料 (多筆 Z98)
//						ZZM261 債務人所有延期繳款資料 (多筆 Z98)
//						ZZM262 單獨受償後各機構無擔保債權暨還款分配資料 (多筆 Z98)
//						ZZM263 金融機構無擔保債務變更還款條件協議資料 (Z98 金融機構無擔保債務變更還款條件協議資料)
//						ZZM264 債權金融機構剩餘無擔保債權暨還款分配資料 (Z98 各債權金融機構剩餘無擔保債權暨還款分配資料(變更還款後)) 
//						ZZS240 債務人基本資料 (單筆 Z96 Z98)
//						ZZS260 金融機構無擔保債務協議資料 (單筆 Z98)
//						ZZS261 債務人繳款資料 (單筆 Z98 債務人繳款資料)
//						ZZS262 金融機構無擔保債務協議資料二階段還款方案 (單筆 Z98)
//						ZZS263 債務人繳款資料(9904起) (單筆 Z98 債務人繳款資料)

					// 第三字元 'S' 為單筆, 'M' 為多筆, 'I' 為背景資訊; * 尚未開放查詢
					switch (Code) {
					case "AAS003":
						doUpdate(titaVo);
//								doAAS003(Code, mLineCut, titaVo);
						break;
					case "ZZS240":
						doZZS240(Code, mLineCut, titaVo);// Z048
						break;
					case "ZZS260":
						doZZS260(Code, mLineCut, titaVo);// 更新NegMain
						break;
					case "ZZS261":
						break;
					case "ZZM261":
						break;
					case "ZZS262":
						break;
					case "ZZS263":
//								doZZS263(Code, mLineCut,titaVo);
						break;
					case "ZZM262": // 單獨受償後各機構無擔保債權暨還款分配資料
						doZZM262(Code, mLineCut, titaVo);
						break;
					case "ZZM263": // 變更還款條件 UPDATE 債權主檔
						doZZM263(Code, mLineCut, titaVo);// Z062
						break;
					case "ZZM264": // 變更還款條件 UPDATE TBJCICSHARE
						doZZM264(Code, mLineCut, titaVo);
						break;
					case "ZZM260": // 一般債權機構代號及名稱,債權比例
						doZZM260(Code, mLineCut, titaVo);
						break;
					default:
						break;
					}
				}

			}
		} catch (IOException e) {
			throw new LogicException(titaVo, "EC001", e.getMessage());
		}

		totaVo.putParam("OSuccessFlag", 1);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doUpdate(TitaVo titaVo) throws LogicException {
		// lNegFinShareM260 // 正常
		// lNegFinShareM262 // 單獨受償
		// lNegFinShareM264 // 變更還款條件
		if (lNegFinShareM260.size() == 0 && lNegFinShareM262.size() == 0 && lNegFinShareM264.size() == 0) {
		} else {

			if (lNegFinShareM262.size() == 0 && lNegFinShareM264.size() == 0) { // 寫正常的分攤檔
				if (isNewMain == true) { // 新案件才新增
					CheckNegFinShare(lNegFinShareM260, custid, "ZZM260", titaVo);// 檢核期金+比例+簽約金額
					try {
						sNegFinShareService.insertAll(lNegFinShareM260, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔");
					}
					try {
						sNegFinShareLogService.insertAll(lNegFinShareLogM260, titaVo); // 分攤歷程檔
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔歷程檔");
					}
				}
			} else if (lNegFinShareM264.size() == 0) { // 寫單獨受償的分攤檔-需先刪掉原分攤資料再新增
				CheckNegFinShare(lNegFinShareM262, custid, "ZZM262", titaVo);// 檢核期金+比例
				// 刪除NegFinShare內最大案件序號
				DeleteZZM262(CustNo262, CaseSeq262, titaVo);

				try {
					sNegFinShareService.insertAll(lNegFinShareM262, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔");
				}
				try {
					sNegFinShareLogService.insertAll(lNegFinShareLogM262, titaVo); // 分攤歷程檔
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔歷程檔");
				}

			} else { // 寫變更還款條件的分攤檔
				CheckNegFinShare(lNegFinShareM264, custid, "ZZM264", titaVo);// 檢核期金+比例+簽約金額

				try {
					sNegFinShareService.insertAll(lNegFinShareM264, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔");
				}
				try {
					sNegFinShareLogService.insertAll(lNegFinShareLogM264, titaVo); // 分攤歷程檔
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔歷程檔");
				}

			}
		}

		lNegFinShareM260 = new ArrayList<NegFinShare>(); // 正常
		lNegFinShareM262 = new ArrayList<NegFinShare>(); // 單獨受償
		lNegFinShareM264 = new ArrayList<NegFinShare>(); // 變更還款條件
		lNegFinShareLogM260 = new ArrayList<NegFinShareLog>();
		lNegFinShareLogM262 = new ArrayList<NegFinShareLog>();
		lNegFinShareLogM264 = new ArrayList<NegFinShareLog>();
		totalContrAmt = BigDecimal.ZERO;
		dueAmt = BigDecimal.ZERO;
		custid = "";
		isZZM262 = false;

	}

	public String getCutSkill(String Code, Map<String, String[]> mData, String Key, TitaVo titaVo) throws LogicException {
		if (!mData.containsKey(Key)) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "資料格式代碼(" + Code + "),無此關鍵字(" + Key + ")");
		}
		String str = mData.get(Key)[0];
		return str;
	}

	/**
	 * 
	 * @param Code        功能代碼共6碼
	 * @param ThisLineUse 要被切的資料欄位
	 * @param titaVo
	 * @return 功能代碼的欄位名稱,StringArray 0:切割後的資料 1:欄位說明
	 * @throws LogicException               錯誤訊息
	 * @throws UnsupportedEncodingException
	 */
	public Map<String, String[]> cutSkill(String Code, String ThisLineUse, TitaVo titaVo) throws LogicException, UnsupportedEncodingException {
		Map<String, String[]> mData = new HashMap<>();

		JcicAtomMain tJcicAtomMain = sJcicAtomMainService.findById(Code, titaVo);
		if (tJcicAtomMain != null) {

			Slice<JcicAtomDetail> sJcicAtomDetail = sJcicAtomDetailService.findByFunctionCode(Code, 0, Integer.MAX_VALUE, titaVo);
			List<JcicAtomDetail> lJcicAtomDetail = sJcicAtomDetail == null ? null : sJcicAtomDetail.getContent();
			if (lJcicAtomDetail != null && lJcicAtomDetail.size() > 0) {

			} else {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "JcicAtomDetail無此資料格式(" + Code + ")");
			}
			// ThisLineUse="";

			int StartLength = 0;
			byte[] byt = ThisLineUse.getBytes("big5");
			int bytL = byt.length;
			for (JcicAtomDetail JcicAtomDetailVo : lJcicAtomDetail) {
				String FiledName = JcicAtomDetailVo.getFiledName();
				String FiledType = JcicAtomDetailVo.getFiledType();
				String Remark = JcicAtomDetailVo.getRemark();

				int Length = 0;
				if (FiledType == null) {
					throw new LogicException(titaVo, "E5009", "FiledType欄位不可為空值");// E5009 資料檢核錯誤
				}
				int FiledTypeL = FiledType.length();

				int FiledTypeL1 = FiledTypeL - 1;

				if (FiledType.indexOf("Char") != -1) {
					Length = Integer.parseInt(FiledType.substring(5, FiledTypeL1));
				} else if (FiledType.indexOf("Num") != -1) {
					Length = Integer.parseInt(FiledType.substring(4, FiledTypeL1));
				} else {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "出現未定義的資料型態[" + FiledType + "]");
				}

				if (StartLength > bytL) {
					this.info("L5706 Error ThisLineUse=[" + ThisLineUse + "]");
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "資料長度有問題,請查驗.");
				}

				String Value = ByteToString(byt, StartLength, Length, titaVo);
				StartLength = StartLength + Length;

				String data[] = { Value, Remark };
				mData.put(FiledName, data);

			}
		} else {
//			OccursDataValue.put(OccursData[2], "未知記號");// OODataType
//			OccursDataValue.put(OccursData[3], "無法判別的記號");// OOMainRemark
//			OccursDataValue.put(OccursData[4], "UNKNOW");// OOFiledName
//			OccursDataValue.put(OccursData[5], "CHAR(*)");// OOFiledType
//
//			OccursDataValue.put(OccursData[6], "未知");// OODetailRemark
//			OccursDataValue.put(OccursData[7], "");// OOValue

//			OccursList occursList = new OccursList();
//			for (String Key : OccursDataValue.keySet()) {
//				occursList.putParam(Key, OccursDataValue.get(Key));
//			}
//			this.totaVo.addOccursList(occursList);
		}

		return mData;
	}

	public BigDecimal sumAmt(String Amt1, String Amt2, String Amt3) {
		BigDecimal sum = BigDecimal.ZERO;
		String Data[] = { Amt1, Amt2, Amt3 };
		for (int i = 0; i < Data.length; i++) {
			String thisData = Data[i].replace(" ", "");
			if (thisData != null && thisData.trim().length() != 0) {
				sum = sum.add(new BigDecimal(thisData));
			}
		}
		return sum;
	}

	public String ByteToString(byte[] byt, int Start, int len, TitaVo titaVo) throws LogicException {
		// ByteToString

		byte[] NewByt = new byte[len];
		for (int i = 0; i < len; i++) {
			NewByt[i] = byt[(Start + i)];
		}
		String str = null;
		try {
			// str=new String(str.getBytes("utf-8"));
			str = new String(NewByt, "BIG5");
		} catch (UnsupportedEncodingException e) {
			this.info("L5706 ByteToString=" + e.toString());
			// E5010 資料格式轉換有誤
			throw new LogicException(titaVo, "E5010", "");
		}
		return str;
	}

	public String TrimStart(String str, char c) {
		String NewStr = str;
		if (str != null) {
			int strL = str.length();
			for (int i = 0; i < strL; i++) {
				if (str.charAt(i) == c) {
					continue;
				} else {
					NewStr = str.substring(i, strL);
					break;
				}
			}
		}
		return NewStr;
	}

	public boolean IsNullOrEmpty(String str) {
		if (str != null && str.trim().length() != 0) {
			return false;
		} else {
			return true;
		}
	}

	public int CheckCustId(String id, TitaVo titaVo) throws LogicException {
		this.info("L5706 CheckCustId Run");
		int CustNo = 0;
		// 查驗此客戶編號存在不存在,回傳CustNo
		if (id != null && id.trim().length() != 0) {
			CustMain tCustMain = sCustMainService.custIdFirst(id, titaVo);
			if (tCustMain != null) {
				CustNo = tCustMain.getCustNo();

				if (CustNo == 0) {
					// 取號
					CustNo = negCom.getNewCustNo(id, titaVo);

					// E5008 戶號為0
					// throw new LogicException(titaVo, "E5008","");
				}
			} else {

				if (errorMsg == 1) {
					throw new LogicException(titaVo, "E0001", "查無客戶主檔資料[" + id + "]");
				}

			}
		} else {
			if (errorMsg == 1) {
				throw new LogicException(titaVo, "E5007", "");// E5007 身分證字號為空值
			}
		}
		return CustNo;
	}

	public NegMain CheckNegMain(int custNo, TitaVo titaVo) throws LogicException {
		this.info("L5706 CheckNegMain Run");
		NegMain tNegMain = new NegMain();
		if (custNo != 0) {
			tNegMain = sNegMainService.custNoFirst(custNo, titaVo);
			if (tNegMain != null) {
				return tNegMain;
			} else {
				if (errorMsg == 1) {
					// E0001 查詢資料不存在
					// throw new LogicException(titaVo, "E0001","NegMain");
				}
			}
		}
		return tNegMain;
	}

	public void CheckNegFinAcct(String fincode, String id, String code, TitaVo titaVo) throws LogicException {
		this.info("L5706 CheckNegFinAcct Run");
		NegFinAcct tNegFinAcct = new NegFinAcct();
		if (fincode != null && fincode.trim().length() != 0) {
			tNegFinAcct = sNegFinAcctService.findById(fincode, titaVo);
			if (tNegFinAcct != null) {

			} else {
				throw new LogicException(titaVo, "E0001", "債權金融機構=" + fincode + ",身分證字號=" + id + ",識別碼=" + code + ",請先由入口交易L5974新增債權金融機構");
			}
		} else {
			throw new LogicException(titaVo, "E0015", "債權金融機構不可空白" + ":身分證字號=" + id + ",識別碼=" + code);
		}
	}

	public void CheckNegFinShare(ArrayList<NegFinShare> lNegFinShare, String id, String code, TitaVo titaVo) throws LogicException {
		this.info("L5706 CheckNegFinShare Run");
		BigDecimal sumrate = BigDecimal.ZERO;
		BigDecimal totalrate = new BigDecimal("100");
		BigDecimal sumdueamt = BigDecimal.ZERO;
		BigDecimal sumtotal = BigDecimal.ZERO;

		// 檢核期金+比例+簽約金額
		for (NegFinShare fin : lNegFinShare) {
			sumrate = sumrate.add(fin.getAmtRatio());
			sumdueamt = sumdueamt.add(fin.getDueAmt());
			sumtotal = sumtotal.add(fin.getContractAmt());
		}
		if (sumrate.compareTo(totalrate) != 0) { // 檢核比例
			throw new LogicException(titaVo, "E0015", "債權比例合計有誤" + ",身分證字號=" + id + ",識別碼=" + code);
		}

		if (sumdueamt.compareTo(dueAmt) != 0) { // 檢核期金
			throw new LogicException(titaVo, "E0015", "每期可分配金額合計有誤" + ",身分證字號=" + id + ",識別碼=" + code);
		}

		if (sumtotal.compareTo(totalContrAmt) != 0 && !"ZZM262".equals(code)) { // 非ZZM262要檢查簽約金額
			throw new LogicException(titaVo, "E0015", "債權簽約金額合計有誤" + ",身分證字號=" + id + ",識別碼=" + code);
		}

	}

	public int StrToInt(String value, String DataName, TitaVo titaVo) throws LogicException {
		int intvalue = 0;
		value = TrimStart(value, '0');
		if (IsNullOrEmpty(value)) {
			value = "0";
		}
		intvalue = Integer.parseInt(value);
		return intvalue;
	}

	public int FindSeq(int CustNo, int CaseSeq, TitaVo titaVo) throws LogicException {
		int iSeq = 0;
		int oSeq = 1;
		Slice<NegFinShareLog> sNegFinShareLog = sNegFinShareLogService.findFinCodeAll(CustNo, CaseSeq, this.index, Integer.MAX_VALUE, titaVo);
		List<NegFinShareLog> mNegFinShareLog = sNegFinShareLog == null ? null : sNegFinShareLog.getContent();
		// 更新歷程檔 找最大歷程序號
		if (mNegFinShareLog != null) {
			for (NegFinShareLog tNegFinShareLog : mNegFinShareLog) {
				iSeq = tNegFinShareLog.getSeq();
				if (iSeq > oSeq) {
					oSeq = iSeq;
				}
			}
		} else {
			oSeq = 0;
		}
		return oSeq + 1;
	}

	public void DeleteZZM262(int CustNo, int CaseSeq, TitaVo titaVo) throws LogicException {

		Slice<NegFinShare> sNegFinShare = sNegFinShareService.findFinCodeAll(CustNo, CaseSeq, this.index, Integer.MAX_VALUE, titaVo);

		List<NegFinShare> mNegFinShare = sNegFinShare == null ? null : sNegFinShare.getContent();
		this.info("Delete All==" + mNegFinShare);
		if (mNegFinShare != null) {
			try {
				sNegFinShareService.deleteAll(mNegFinShare);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

	}

	public BigDecimal StringToBigDecimal(String strBd, TitaVo titaVo) throws LogicException {
		BigDecimal BdV = BigDecimal.ZERO;
		strBd = TrimStart(strBd, '0');
		if (IsNullOrEmpty(strBd)) {
			strBd = "0";
		}
		BdV = parse.stringToBigDecimal(strBd);
		return BdV;
	}

	public int DateRocToDC(String date, String dateName, TitaVo titaVo) throws LogicException {

		date = date.trim();
		int dc = 0;
		if (date != null && date.length() != 0) {
			dc = (Integer.parseInt(date) + 19110000);
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", dateName + ":不可為空值");
		}

		return dc;
	}

	public void doZZS240(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZS240 債務協商 債務人基本資料 (單筆 Z96 Z98)

		// IDN_BAN Char(10) 身分證號
		// MAIN_CODE Char(3) 最大債權金融機構
		// MAIN_CODE_NAME Char(24) 最大債權金融機構名稱
		// RECEIVE_DATE Char(7) 協商申請日期
		// REG_ADDR Char(76) 戶籍地址
		// COM_ADDR Char(76) 通訊地址
		// REG_TELNO Char(16) 戶籍電話
		// COM_TELNO Char(16) 通訊電話
		// MOBIL_NO Char(16) 行動電話
		// FILLER Char(30) 保留欄位
		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo);// 最大債權金融機構
		// String
		// MAIN_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE_NAME");//最大債權金融機構名稱
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		String REG_ADDR = getCutSkill(Code, mLineCut, "REG_ADDR", titaVo);// 戶籍地址
		String COM_ADDR = getCutSkill(Code, mLineCut, "COM_ADDR", titaVo);// 通訊地址
		String REG_TELNO = getCutSkill(Code, mLineCut, "REG_TELNO", titaVo);// 戶籍電話
		String COM_TELNO = getCutSkill(Code, mLineCut, "COM_TELNO", titaVo);// 通訊電話
		String MOBIL_NO = getCutSkill(Code, mLineCut, "MOBIL_NO", titaVo);// 行動電話

		CheckCustId(IDN_BAN, titaVo);

		JcicZ048Id tJcicZ048Id = new JcicZ048Id();
		tJcicZ048Id.setCustId(IDN_BAN);
		tJcicZ048Id.setRcDate(DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo));
		tJcicZ048Id.setSubmitKey(MAIN_CODE);
		JcicZ048 tJcicZ048 = sJcicZ048Service.findById(tJcicZ048Id, titaVo);
		if (tJcicZ048 != null) {
			// 已有資料
		} else {
			// 未有資料
			tJcicZ048 = new JcicZ048();
			tJcicZ048.setJcicZ048Id(tJcicZ048Id);
			tJcicZ048.setTranKey("A");
			tJcicZ048.setCustRegAddr(REG_ADDR.trim());// 債務人戶籍之郵遞區號及地址
			tJcicZ048.setCustComAddr(COM_ADDR.trim());// 債務人通訊地之郵遞區號及地址
			tJcicZ048.setCustRegTelNo(REG_TELNO.trim());// 債務人戶籍電話
			tJcicZ048.setCustComTelNo(COM_TELNO.trim());// 債務人通訊電話
			tJcicZ048.setCustMobilNo(MOBIL_NO.trim());// 債務人行動電話
			tJcicZ048.setOutJcicTxtDate(0);
			String iKey = "";
			iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			tJcicZ048.setUkey(iKey);
			try {
				sJcicZ048Service.insert(tJcicZ048, titaVo);
			} catch (DBException e) {
				// E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", "債務人基本資料");
			}
		}

	}

	public void doAAS003(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// AAS003 自然人姓名,身分證補發,通報,補充註記 (單筆, 取代AAS001)
		// IDN Char(10) 身分證號
		// PNAME Char(40) 中文姓名
		// IS_LOST Char(1) 身分證註銷或一年內掛失紀錄 YN 自 95.07.15 改為空白
		// IS_WANTED Char(1) 通報案件紀錄 YN
		// IS_NOTE Char(1) 是否有補充註記1,2,A,N
		// NOTELIST Char(9) 補充註記 自 97.04.11 改為空白
		// WANTEDLIST Char(9) 通報案件紀錄 (對照表)
		// FOREIGNER_MARK Char(1) 在台無戶籍人士身分證號對照索引註記 'Y','N'

//		String IDN=getCutSkill(titaVo,Code,mLineCut,"IDN");//身分證號
//		String PNAME=getCutSkill(titaVo,Code,mLineCut,"PNAME");//中文姓名
//		String IS_LOST=getCutSkill(titaVo,Code,mLineCut,"IS_LOST");//身分證註銷或一年內掛失紀錄 YN  自 95.07.15 改為空白
//		String IS_WANTED=getCutSkill(titaVo,Code,mLineCut,"IS_WANTED");//通報案件紀錄 YN
//		String IS_NOTE=getCutSkill(titaVo,Code,mLineCut,"IS_NOTE");//是否有補充註記1,2,A,N
//		String NOTELIST=getCutSkill(titaVo,Code,mLineCut,"NOTELIST");//補充註記  自 97.04.11 改為空白
//		String WANTEDLIST=getCutSkill(titaVo,Code,mLineCut,"WANTEDLIST");//通報案件紀錄  (對照表)
//		String FOREIGNER_MARK=getCutSkill(titaVo,Code,mLineCut,"FOREIGNER_MARK");//在台無戶籍人士身分證號對照索引註記  'Y','N'

		// 檢查是否有客戶資料
		// 不用處理

		// 新壽原程式碼用來找CustNo戶號用
	}

	public void doZZS260(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZS260 債務協商 金融機構無擔保債務協議資料 (單筆 Z98)

		// IDN_BAN Char(10) 身分證號
		// MAIN_CODE Char(3) 最大債權金融機構
		// MAIN_CODE_NAME Char(24) 最大債權金融機構名稱
		// RECEIVE_DATE Char(7) 協商申請日期
		// PASS_DATE Char(7) 協議完成日期
		// INTERVIEW_DATE Char(7) 面談日期
		// SIGN_DATE Char(7) 簽約完成日期
		// PERIOD Num(3) 期數
		// RATE Num(5) 利率
		// FIRST_PAY_DATE Char(7) 首期應繳款日期
		// PAY_AMOUNT Num(9) 月付金
		// PAY_ACCOUNT Char(20) 繳款帳號
		// EXP_LOAN_AMT Num(9) 信用貸款債務總簽約金額
		// CASH_CARD_AMT Num(9) 現金卡債務總簽約金額
		// CREDIT_CARD_AMT Num(9) 信用卡債務簽約總金額
		// TOTAL_AMT Num(9) 總簽約金額合計
		// FILLER Char(30) 保留欄位

		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo);// 最大債權金融機構
		// String
		// MAIN_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE_NAME");//最大債權金融機構名稱
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		// String PASS_DATE=getCutSkill(titaVo,Code,mLineCut,"PASS_DATE");//協議完成日期
		// String
		// INTERVIEW_DATE=getCutSkill(titaVo,Code,mLineCut,"INTERVIEW_DATE");//面談日期
		// String SIGN_DATE=getCutSkill(titaVo,Code,mLineCut,"SIGN_DATE");//簽約完成日期
		String PERIOD = getCutSkill(Code, mLineCut, "PERIOD", titaVo);// 期數
		String RATE = getCutSkill(Code, mLineCut, "RATE", titaVo);// 利率
		String FIRST_PAY_DATE = getCutSkill(Code, mLineCut, "FIRST_PAY_DATE", titaVo).trim();// 首期應繳款日期
		String PAY_AMOUNT = getCutSkill(Code, mLineCut, "PAY_AMOUNT", titaVo);// 月付金
//		String PAY_ACCOUNT=getCutSkill(titaVo,Code,mLineCut,"PAY_ACCOUNT");//繳款帳號
//		String EXP_LOAN_AMT=getCutSkill(titaVo,Code,mLineCut,"EXP_LOAN_AMT");//信用貸款債務總簽約金額
//		String CASH_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CASH_CARD_AMT");//現金卡債務總簽約金額
//		String CREDIT_CARD_AMT=getCutSkill(titaVo,Code,mLineCut,"CREDIT_CARD_AMT");//信用卡債務簽約總金額
		String TOTAL_AMT = getCutSkill(Code, mLineCut, "TOTAL_AMT", titaVo);// 總簽約金額合計

		// 查驗是否已有客戶主檔
		int CustNo = CheckCustId(IDN_BAN, titaVo);
		custid = IDN_BAN;
		// 檢查債權機構
		CheckNegFinAcct(MAIN_CODE, IDN_BAN, "ZZS260", titaVo);

		// 檢查是否有NEGMAIN的主檔-無則新增
		NegMain tNegMain = sNegMainService.custNoAndApplDateFirst(CustNo, DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo), MAIN_CODE, titaVo);
		if (tNegMain != null) {
			isNewMain = false;
			totalContrAmt = tNegMain.getTotalContrAmt();// 分攤檔金額檢核使用
			dueAmt = tNegMain.getDueAmt();// 分攤檔金額檢核使用
			// 已有資料不做處理
		} else {
			isNewMain = true;
			// 未有資料-新增處理
			NegMainId tNegMainId = new NegMainId();
			// 找該戶號序號最大的
			NegMain t1NegMain = CheckNegMain(CustNo, titaVo);
			if (t1NegMain != null) {
				tNegMainId.setCaseSeq(t1NegMain.getCaseSeq() + 1);
			} else {
				tNegMainId.setCaseSeq(1);
			}
			tNegMainId.setCustNo(CustNo);

			NegMain t2NegMain = new NegMain();

			t2NegMain.setNegMainId(tNegMainId);
			t2NegMain.setCaseSeq(tNegMainId.getCaseSeq());
			t2NegMain.setCustNo(tNegMainId.getCustNo());
			t2NegMain.setMainFinCode(MAIN_CODE);

			t2NegMain.setApplDate(DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo));
			t2NegMain.setTotalPeriod(StrToInt(PERIOD, "期數", titaVo));
			t2NegMain.setIntRate(StringToBigDecimal(RATE, titaVo));
			t2NegMain.setFirstDueDate(DateRocToDC(FIRST_PAY_DATE, "首次應繳日", titaVo));
			t2NegMain.setDueAmt(StringToBigDecimal(PAY_AMOUNT, titaVo));
			t2NegMain.setTotalContrAmt(StringToBigDecimal(TOTAL_AMT, titaVo));// 簽約總金額
			totalContrAmt = t2NegMain.getTotalContrAmt();// 分攤檔金額檢核使用
			dueAmt = t2NegMain.getDueAmt();// 分攤檔金額檢核使用
			CustMain tCustMain = sCustMainService.custIdFirst(IDN_BAN, titaVo);
			String CustLoanKind = "";
			if (tCustMain != null) {
				List<String> CustTypeCode = new ArrayList<String>();
				CustTypeCode.add("05");
				// 戶別
				if (CustTypeCode.contains(tCustMain.getCustTypeCode())) {
					// 是保貸戶
					CustLoanKind = "2";
				} else {
					CustLoanKind = "1";
				}
			} else {
				if (errorMsg == 1) {
					// E0001 查詢資料不存在
					throw new LogicException(titaVo, "E0001", "查無客戶主檔資料[" + IDN_BAN + "]");
				}

			}
			t2NegMain.setCaseKindCode("1");// 案件種類 1:協商;2:調解;3:更生;4:清算
			t2NegMain.setStatus("0");// 債權戶況 0:正常 1:已變更 2:毀諾 3:結案 4:未生效
			t2NegMain.setCustLoanKind(CustLoanKind);// 債權戶別"1:放款戶 ;2:保貸戶;客戶主檔:保貸別
			t2NegMain.setTwoStepCode("N");// 二階段註記

			t2NegMain.setDeferYMStart(0);// 延期繳款年月(起)
			t2NegMain.setDeferYMEnd(0);// 延期繳款年月(訖)

			t2NegMain.setPrincipalBal(t2NegMain.getTotalContrAmt());// 總本金餘額

			t2NegMain.setNextPayDate(t2NegMain.getFirstDueDate());// 下次應繳日
			int LastDueDate = 0;
			if (t2NegMain.getTotalPeriod() > 0) {
				LastDueDate = negCom.getRepayDate(t2NegMain.getFirstDueDate(), t2NegMain.getTotalPeriod() - 1, titaVo); // 並非計算下一期故須減1
			} else {
				LastDueDate = t2NegMain.getFirstDueDate();
			}

			t2NegMain.setLastDueDate(LastDueDate);// 還款結束日
			t2NegMain.setPayIntDate(DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo));// 繳息迄日

			String IsMainFin = "";
			if (("458").equals(MAIN_CODE)) {
				IsMainFin = "Y";
			} else {
				IsMainFin = "N";
			}
			t2NegMain.setIsMainFin(IsMainFin);// 是否最大債權

			t2NegMain.setStatusDate(0);// 戶況日期

			try {
				sNegMainService.insert(t2NegMain, titaVo);
			} catch (DBException e) {
				// E0005 新增資料時，發生錯誤
				throw new LogicException(titaVo, "E0005", "債務協商案件主檔");
			}
		}

	}

	public void doZZM260(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZM260 債務協商 各債權金融機構無擔保債權暨還款分配資料 (多筆 Z98)
		// IDN_BAN Char(10) 身分證號
		// RECEIVE_DATE Char(7) 協商申請日期
		// MAIN_CODE Char(3) 最大債權金融機構
		// BANK_CODE Char(3) 債權金融機構
		// BANK_CODE_NAME Char(24) 債權金融機構名稱
		// EXP_LOAN_AMT Num(9) 信用貸款債權金額
		// CASH_CARD_AMT Num(9) 現金卡債權金額
		// CREDIT_CARD_AMT Num(9) 信用卡債權金額
		// DISP_AMT Num(9) 每期可分配金額
		// LOAN_RATE Num(6) 佔全部協商無擔保債權比例 小數點兩位, 例如023.45
		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo);// 最大債權金融機構
		String BANK_CODE = getCutSkill(Code, mLineCut, "BANK_CODE", titaVo);// 債權金融機構
		// String
		// BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT = getCutSkill(Code, mLineCut, "EXP_LOAN_AMT", titaVo);// 信用貸款債權金額
		String CASH_CARD_AMT = getCutSkill(Code, mLineCut, "CASH_CARD_AMT", titaVo);// 現金卡債權金額
		String CREDIT_CARD_AMT = getCutSkill(Code, mLineCut, "CREDIT_CARD_AMT", titaVo);// 信用卡債權金額
		String DISP_AMT = getCutSkill(Code, mLineCut, "DISP_AMT", titaVo);// 每期可分配金額
		String LOAN_RATE = getCutSkill(Code, mLineCut, "LOAN_RATE", titaVo);// 佔全部協商無擔保債權比例 小數點兩位, 例如023.45

		int CustNo = CheckCustId(IDN_BAN, titaVo);
		custid = IDN_BAN;

		// 檢查債權機構
		CheckNegFinAcct(BANK_CODE, IDN_BAN, "ZZM260", titaVo);

		if (("458").equals(MAIN_CODE)) { // 最大債權才做
			// CustNoAndApplDateFirst
			NegMain tNegMain = sNegMainService.custNoAndApplDateFirst(CustNo, DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo), MAIN_CODE, titaVo);
			if (tNegMain != null) {
				// 已有資料
				InsertNegFinShare(IDN_BAN, CustNo, tNegMain.getCaseSeq(), BANK_CODE, EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT, "ZZM260", titaVo);

			} else {
				// 無資料
				if (errorMsg == 1) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "身分證字號[" + IDN_BAN + "]債權資料不存在.");
				}

			}
		}
	}

	public void doZZS263(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZS263 債務協商 債務人繳款資料(9904起) (單筆 Z98 債務人繳款資料)
		// IDN_BAN Char(10) 身分證號
		// RECEIVE_DATE Char(7) 協商申請日期
		// MAIN_CODE Char(3) 最大債權金融機構
		// PAY_DATE Char(7) 最近一次繳還款日期
		// PAY_AMT Num(9) 最近一次繳款金額
		// PAYAMT_1 Num(9) 累計實際還款金額
		// PAYAMT_2 Num(9) 累計應還款金額
		// PAY_STATUS Char(1) 債權狀態 1:正常, 2:結案, 3:毀諾, 4:毀諾後清償, 5:協商終止
		// CASE_DATE Char(7) 清償結案或毀諾日期
		// LIMIT_DATE Char(7) 前置協商註記揭露期限
		// LAD_PAY_DATE Char(5) 進入第二階梯還款年月
		// FILLER Char(30) 保留欄位
//		String IDN_BAN=getCutSkill(titaVo,Code,mLineCut,"IDN_BAN");//身分證號
//		String RECEIVE_DATE=getCutSkill(titaVo,Code,mLineCut,"RECEIVE_DATE");//協商申請日期
//		String MAIN_CODE=getCutSkill(titaVo,Code,mLineCut,"MAIN_CODE");//最大債權金融機構
//		String PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"PAY_DATE");//最近一次繳還款日期
//		String PAY_AMT=getCutSkill(titaVo,Code,mLineCut,"PAY_AMT");//最近一次繳款金額
//		String PAYAMT_1=getCutSkill(titaVo,Code,mLineCut,"PAYAMT_1");//累計實際還款金額
//		String PAYAMT_2=getCutSkill(titaVo,Code,mLineCut,"PAYAMT_2");//累計應還款金額
//		String PAY_STATUS=getCutSkill(titaVo,Code,mLineCut,"PAY_STATUS");//債權狀態  1:正常, 2:結案, 3:毀諾, 4:毀諾後清償, 5:協商終止
//		String CASE_DATE=getCutSkill(titaVo,Code,mLineCut,"CASE_DATE");//清償結案或毀諾日期
//		String LIMIT_DATE=getCutSkill(titaVo,Code,mLineCut,"LIMIT_DATE");//前置協商註記揭露期限
//		String LAD_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"LAD_PAY_DATE");//進入第二階梯還款年月

		// 維護主檔戶況=>改為做入帳還款時再由交易維護主檔戶況
		// 不用處理

	}

	public void doZZM262(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZM262 債務協商 單獨受償後各機構無擔保債權暨還款分配資料 (多筆 Z98)
		// IDN_BAN Char(10) 身分證號
		// RECEIVE_DATE Char(7) 協商申請日期
		// MAIN_CODE Char(3) 最大債權金融機構
		// BANK_CODE Char(3) 債權金融機構
		// BANK_CODE_NAME Char(24) 債權金融機構名稱
		// EXP_LOAN_AMT Num(9) 信用貸款債權金額
		// CASH_CARD_AMT Num(9) 現金卡債權金額
		// CREDIT_CARD_AMT Num(9) 信用卡債權金額
		// DISP_AMT Num(9) 每期可分配金額
		// LOAN_RATE Num(6) 佔全部協商無擔保債權比例 小數點兩位, 例如023.45
		// PAY_BANK Char(3) 單獨受償金融機構
		// PAY_DATE Char(7) 單獨受償日期
		// PAY_SEQ Num(5) 單獨受償次序
		// PAY_REASON Char(1) 單獨受償原因代碼 (對照表)
		// FILLER Char(30) 保留欄位
		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo);// 最大債權金融機構
		String BANK_CODE = getCutSkill(Code, mLineCut, "BANK_CODE", titaVo);// 債權金融機構
//		String BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT = getCutSkill(Code, mLineCut, "EXP_LOAN_AMT", titaVo);// 信用貸款債權金額
		String CASH_CARD_AMT = getCutSkill(Code, mLineCut, "CASH_CARD_AMT", titaVo);// 現金卡債權金額
		String CREDIT_CARD_AMT = getCutSkill(Code, mLineCut, "CREDIT_CARD_AMT", titaVo);// 信用卡債權金額
		String DISP_AMT = getCutSkill(Code, mLineCut, "DISP_AMT", titaVo);// 每期可分配金額
		String LOAN_RATE = getCutSkill(Code, mLineCut, "LOAN_RATE", titaVo);// 佔全部協商無擔保債權比例 小數點兩位, 例如023.45
		String PAY_BANK = getCutSkill(Code, mLineCut, "PAY_BANK", titaVo);// 單獨受償金融機構
		String PAY_DATE = getCutSkill(Code, mLineCut, "PAY_DATE", titaVo);// 單獨受償日期
//		String PAY_SEQ=getCutSkill(titaVo,Code,mLineCut,"PAY_SEQ");//單獨受償次序
//		String PAY_REASON=getCutSkill(titaVo,Code,mLineCut,"PAY_REASON");//單獨受償原因代碼  (對照表)

		// 找債協主檔序號最大的那筆
		int CustNo = CheckCustId(IDN_BAN, titaVo);
		custid = IDN_BAN;

		// 檢查債權機構
		CheckNegFinAcct(BANK_CODE, IDN_BAN, "ZZM262", titaVo);

		if (("458").equals(MAIN_CODE)) { // 最大債權才做
			NegMain tNegMain = sNegMainService.custNoAndApplDateFirst(CustNo, DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo), MAIN_CODE, titaVo);

			if (tNegMain == null) {
				if (errorMsg == 1) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "身分證字號[" + IDN_BAN + "]債權資料不存在.");
				}
			}

			// 已有資料
			InsertNegFinShare(IDN_BAN, CustNo, tNegMain.getCaseSeq(), BANK_CODE, EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT, "ZZM262", titaVo);
			if (isZZM262 == false) {// 紀錄被註銷債權資料及更新總本金餘額,同ID只做一次
				isZZM262 = true;
				CheckNegFinAcct(PAY_BANK, IDN_BAN, "ZZM262", titaVo);// 檢查註銷債權機構
				InsertNegFinShareLog(IDN_BAN, tNegMain, PAY_BANK, PAY_DATE, titaVo);
			}
		}
	}

	public void InsertNegFinShare(String CustId, int CustNo, int CaseSeq, String FinCode, String ExpLoanAmt, String CashCardAmt, String CreditCardAmt, String LoanRate, String DispAmt, String Code,
			TitaVo titaVo) throws LogicException {
		String Code1 = Code;

		// 歷程檔找最大歷程序號
		int oSeq = FindSeq(CustNo, CaseSeq, titaVo);

		NegFinShareLogId tNegFinShareLogId = new NegFinShareLogId();
		tNegFinShareLogId.setCustNo(CustNo);
		tNegFinShareLogId.setCaseSeq(CaseSeq);
		tNegFinShareLogId.setFinCode(FinCode);
		tNegFinShareLogId.setSeq(oSeq);

		NegFinShareId tNegFinShareId = new NegFinShareId();
		tNegFinShareId.setCaseSeq(CaseSeq);
		tNegFinShareId.setCustNo(CustNo);
		tNegFinShareId.setFinCode(FinCode);

		this.info("L5706 InsertNegFinShare tNegFinShareId=[" + tNegFinShareId + "]");
		NegFinShare tNegFinShare = sNegFinShareService.findById(tNegFinShareId, titaVo);

		if (tNegFinShare != null && !("ZZM262").equals(Code1)) {
			if (errorMsg == 1) {
				// E5009 資料檢核錯誤
//				throw new LogicException(titaVo, "E5009","身分證字號["+CustId+"] 戶號:["+CustNo+"] 案件序號:["+CaseSeq+"] 債權銀行:["+FinCode+"] 已有債務協商債權分攤檔資料.");
			}
		} else {

			tNegFinShare = new NegFinShare();
			tNegFinShare.setNegFinShareId(tNegFinShareId);
			BigDecimal amtall = sumAmt(ExpLoanAmt, CashCardAmt, CreditCardAmt);
			tNegFinShare.setContractAmt(amtall);// 簽約金額
			tNegFinShare.setAmtRatio(StringToBigDecimal(LoanRate, titaVo));// 債權比例% 
			tNegFinShare.setDueAmt(StringToBigDecimal(DispAmt, titaVo));// 期款
			tNegFinShare.setCancelDate(0);// 註銷日期
			tNegFinShare.setCancelAmt(BigDecimal.ZERO);// 註銷本金

			NegFinShareLog tNegFinShareLog = new NegFinShareLog();
			tNegFinShareLog.setNegFinShareLogId(tNegFinShareLogId);
			tNegFinShareLog.setContractAmt(amtall);
			tNegFinShareLog.setAmtRatio(StringToBigDecimal(LoanRate, titaVo));
			tNegFinShareLog.setDueAmt(StringToBigDecimal(DispAmt, titaVo));
			tNegFinShareLog.setCancelDate(0);
			tNegFinShareLog.setCancelAmt(BigDecimal.ZERO);

			// 依傳入的識別碼分別存到不同暫存檔

			if (("ZZM260").equals(Code1)) {
				lNegFinShareM260.add(tNegFinShare);
				lNegFinShareLogM260.add(tNegFinShareLog);
			}
			if (("ZZM262").equals(Code1)) {
				CustNo262 = CustNo;
				CaseSeq262 = CaseSeq;
				lNegFinShareM262.add(tNegFinShare);
				lNegFinShareLogM262.add(tNegFinShareLog);
			}
			if (("ZZM264").equals(Code1)) {
				lNegFinShareM264.add(tNegFinShare);
				lNegFinShareLogM264.add(tNegFinShareLog);
			}

			// try {
//				sNegFinShareService.insert(tNegFinShare, titaVo);
//			} catch (DBException e) {
//				//E0005 新增資料時，發生錯誤
//				throw new LogicException(titaVo, "E0005", "債務協商債權分攤檔");
//			}
		}

	}

	public void InsertNegFinShareLog(String CustId, NegMain tNegMain, String paybank, String paydate, TitaVo titaVo) throws LogicException {
		// ZZM262寫本次單獨受償的債權機構資料紀錄在NegFinShareLog

		// 計算目前債務協商債權分攤檔簽約金額加總
		Slice<NegFinShare> slNegFinShare = sNegFinShareService.findFinCodeAll(tNegMain.getCustNo(), tNegMain.getCaseSeq(), this.index, 30);
		List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();
		BigDecimal sumContractAmt = new BigDecimal("0");
		if (lNegFinShare == null) {
			throw new LogicException(titaVo, "E0001", "身分證字號[" + CustId + "] 戶號:[" + tNegMain.getCustNo() + "] 案件序號:[" + tNegMain.getCaseSeq() + "] 債務協商債權分攤檔");
		}
		for (NegFinShare t1NegFinShare : lNegFinShare) {
			sumContractAmt = sumContractAmt.add(t1NegFinShare.getContractAmt());// 累加簽約金額
		}
		if (sumContractAmt.compareTo(BigDecimal.ZERO) == 0) {// 分攤檔簽約金額加總不應為0
			throw new LogicException(titaVo, "E5009", "身分證字號[" + CustId + "] 戶號:[" + tNegMain.getCustNo() + "] 案件序號:[" + tNegMain.getCaseSeq() + "] 簽約金額加總為0");
		}

		// 歷程檔找最大歷程序號
		int oSeq = FindSeq(tNegMain.getCustNo(), tNegMain.getCaseSeq(), titaVo);

		NegFinShareLogId tNegFinShareLogId = new NegFinShareLogId();
		tNegFinShareLogId.setCustNo(tNegMain.getCustNo());
		tNegFinShareLogId.setCaseSeq(tNegMain.getCaseSeq());
		tNegFinShareLogId.setFinCode(paybank);
		tNegFinShareLogId.setSeq(oSeq);

		NegFinShareId tNegFinShareId = new NegFinShareId();
		tNegFinShareId.setCaseSeq(tNegMain.getCaseSeq());
		tNegFinShareId.setCustNo(tNegMain.getCustNo());
		tNegFinShareId.setFinCode(paybank);
		NegFinShare tNegFinShare = sNegFinShareService.findById(tNegFinShareId, titaVo);

		if (tNegFinShare == null) {
			throw new LogicException(titaVo, "E5009", "身分證字號[" + CustId + "] 戶號:[" + tNegMain.getCustNo() + "] 案件序號:[" + tNegMain.getCaseSeq() + "] 債權銀行:[" + paybank + "] 單獨受償之債權銀行不存在");
		}

		NegFinShareLog tNegFinShareLog = new NegFinShareLog();
		tNegFinShareLog.setNegFinShareLogId(tNegFinShareLogId);
		tNegFinShareLog.setContractAmt(tNegFinShare.getContractAmt());
		tNegFinShareLog.setAmtRatio(tNegFinShare.getAmtRatio());
		tNegFinShareLog.setDueAmt(tNegFinShare.getDueAmt());
		tNegFinShareLog.setCancelDate(DateRocToDC(paydate, "單獨受償日期", titaVo));// 註銷日期
		// 註銷金額=簽約金額/簽約金額加總*總本金餘額 ; 先乘再除,四捨五入到整數
		BigDecimal CancelAmt = tNegFinShare.getContractAmt().multiply(tNegMain.getPrincipalBal()).divide(sumContractAmt, 0, BigDecimal.ROUND_HALF_UP);
		tNegFinShareLog.setCancelAmt(CancelAmt);

		// 存到暫存檔
		CustNo262 = tNegMain.getCustNo();
		CaseSeq262 = tNegMain.getCaseSeq();
		lNegFinShareLogM262.add(tNegFinShareLog);

		// 更新主檔的總本金餘額減掉註銷金額
		NegMain OrgNegMain = (NegMain) dataLog.clone(tNegMain);
		tNegMain = sNegMainService.holdById(tNegMain.getNegMainId(), titaVo);
		if (tNegMain == null) {
			throw new LogicException(titaVo, "E0006", "債務協商案件主檔");
		}
		tNegMain.setPrincipalBal(tNegMain.getPrincipalBal().subtract(CancelAmt));
		try {
			sNegMainService.update(tNegMain, titaVo);// 資料異動後-1
			titaVo.putParam("CustNo", tNegMain.getCustNo());
			dataLog.setEnv(titaVo, OrgNegMain, tNegMain);// 資料異動後-2
			dataLog.exec();// 資料異動後-3
		} catch (DBException e) {
			// E0007 更新資料時，發生錯誤
			throw new LogicException(titaVo, "E0007", "債務協商案件主檔");
		}

	}

	public void UpdNegMain(NegMain tNegMain, int chgCondDate, TitaVo titaVo) throws LogicException {
		// 本筆資料改為已變更
		NegMain OrgNegMain = (NegMain) dataLog.clone(tNegMain);
		tNegMain = sNegMainService.holdById(tNegMain.getNegMainId(), titaVo);
		if (tNegMain != null) {
			tNegMain.setStatus("1");
			tNegMain.setStatusDate(dateUtil.getNowIntegerForBC());// 戶況日期
			tNegMain.setChgCondDate(chgCondDate);// 申請變更還款條件日
			try {
				sNegMainService.update(tNegMain, titaVo);// 資料異動後-1
				titaVo.putParam("CustNo", tNegMain.getCustNo());
				dataLog.setEnv(titaVo, OrgNegMain, tNegMain);// 資料異動後-2
				dataLog.exec();// 資料異動後-3
			} catch (DBException e) {
				// E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", "債務協商案件主檔");
			}

		} else {
			// E0006 鎖定資料時，發生錯誤
			throw new LogicException(titaVo, "E0006", "債務協商案件主檔");
		}
	}

	public void doZZM263(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZM263 債務協商 金融機構無擔保債務變更還款條件協議資料 (Z98 金融機構無擔保債務變更還款條件協議資料)
		// IDN_BAN Char(10) 身分證號
		// RECEIVE_DATE Char(7) 協商申請日期
		// MAIN_CODE Char(3) 最大債權金融機構
		// CHANGE_PAY_DATE Char(7) 申請變更還款條件日
		// COMMIT_PAY_PERIOD Num(3) 變更還款條件已履約期數
		// CHANGE_PASS_DATE Char(7) 變更還款條件協議完成日
		// CHANGE_INTERVI_DATE Char(7) 變更還款條件面談日期
		// CHANGE_SIGN_DATE Char(7) 變更還款條件簽約日期
		// FIRST_PERIOD Num(3) （第一階梯）期數
		// FIRST_RATE Num(5) （第一階梯）利率
		// FIRST_PAY_DATE Char(7) 變更還款條件首期應繳款日期
		// PAY_AMOUNT Num(9) 月付金
		// PAY_ACCOUNT Char(20) 繳款帳號
		// CLOSE_DATE Char(7) 變更還款條件結案日期
		// REM_EXP_AMT Num(9) 信用貸款協商剩餘債務總金額
		// REM_CASH_AMT Num(9) 現金卡協商剩餘債務總金額
		// REM_CREDIT_AMT Num(9) 信用卡協商剩餘債務總金額
		// CHANGE_TOTAL_AMT Num(10) 變更還款條件簽約總債務金額
		// LAD_PAY_NOTE Char(1) 屬階梯式還款註記
		// LAD_PERIOD Num(3) 第二階梯期數
		// LAD_RATE Num(5) 第二階梯利率
		// LAD_PAY_AMT Num(9) 第二階梯月付金
		// FILLER Char(30) 保留欄位

		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo).trim();// 最大債權金融機構
		String CHANGE_PAY_DATE = getCutSkill(Code, mLineCut, "CHANGE_PAY_DATE", titaVo);// 申請變更還款條件日
		String COMMIT_PAY_PERIOD = getCutSkill(Code, mLineCut, "COMMIT_PAY_PERIOD", titaVo);// 變更還款條件已履約期數
		String CHANGE_PASS_DATE = getCutSkill(Code, mLineCut, "CHANGE_PASS_DATE", titaVo);// 變更還款條件協議完成日
		String CHANGE_INTERVI_DATE = getCutSkill(Code, mLineCut, "CHANGE_INTERVI_DATE", titaVo);// 變更還款條件面談日期
		String CHANGE_SIGN_DATE = getCutSkill(Code, mLineCut, "CHANGE_SIGN_DATE", titaVo);// 變更還款條件簽約日期
		String FIRST_PERIOD = getCutSkill(Code, mLineCut, "FIRST_PERIOD", titaVo);// （第一階梯）期數
		String FIRST_RATE = getCutSkill(Code, mLineCut, "FIRST_RATE", titaVo);// （第一階梯）利率
		String FIRST_PAY_DATE = getCutSkill(Code, mLineCut, "FIRST_PAY_DATE", titaVo);// 變更還款條件首期應繳款日期
		String PAY_AMOUNT = getCutSkill(Code, mLineCut, "PAY_AMOUNT", titaVo);// 月付金
		String PAY_ACCOUNT = getCutSkill(Code, mLineCut, "PAY_ACCOUNT", titaVo);// 繳款帳號
//		String CLOSE_DATE=getCutSkill(titaVo,Code,mLineCut,"CLOSE_DATE");//變更還款條件結案日期
		String REM_EXP_AMT = getCutSkill(Code, mLineCut, "REM_EXP_AMT", titaVo);// 信用貸款協商剩餘債務總金額
		String REM_CASH_AMT = getCutSkill(Code, mLineCut, "REM_CASH_AMT", titaVo);// 現金卡協商剩餘債務總金額
		String REM_CREDIT_AMT = getCutSkill(Code, mLineCut, "REM_CREDIT_AMT", titaVo);// 信用卡協商剩餘債務總金額
		String CHANGE_TOTAL_AMT = getCutSkill(Code, mLineCut, "CHANGE_TOTAL_AMT", titaVo);// 變更還款條件簽約總債務金額
		String LAD_PAY_NOTE = getCutSkill(Code, mLineCut, "LAD_PAY_NOTE", titaVo);// 屬階梯式還款註記
		String LAD_PERIOD = getCutSkill(Code, mLineCut, "LAD_PERIOD", titaVo);// 第二階梯期數
		String LAD_RATE = getCutSkill(Code, mLineCut, "LAD_RATE", titaVo);// 第二階梯利率
		String LAD_PAY_AMT = getCutSkill(Code, mLineCut, "LAD_PAY_AMT", titaVo);// 第二階梯月付金

		int chgCondDate = DateRocToDC(CHANGE_PAY_DATE, "申請變更還款條件日", titaVo);
		int ApplyDate = DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo);
		int FirstDueDate = DateRocToDC(FIRST_PAY_DATE, "首次應繳日", titaVo);
		// 本筆資料改為已變更,新增一筆NegMain
		int CustNo = CheckCustId(IDN_BAN, titaVo);
		custid = IDN_BAN;
		// 檢查債權機構
		CheckNegFinAcct(MAIN_CODE, IDN_BAN, "ZZM263", titaVo);

		NegMain tNegMain = sNegMainService.custNoAndApplDateFirst(CustNo, ApplyDate, MAIN_CODE, titaVo);
		if (tNegMain == null) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "身分證字號[" + IDN_BAN + "] 戶號:[" + CustNo + "] 最大債權銀行:[" + MAIN_CODE + "] 未有債權資料");
		}
		// 本筆資料改為已變更,申請變更還款條件日也需寫入
		UpdNegMain(tNegMain, chgCondDate, titaVo);
		// 新增一筆NegMain
		NegMain InserttNegMain = new NegMain();
		NegMainId InserttNegMainId = new NegMainId();
		InserttNegMain = (NegMain) dataLog.clone(tNegMain);

		// 找該戶號序號最大的
		NegMain t1NegMain = CheckNegMain(CustNo, titaVo);
		if (t1NegMain != null) {
			InserttNegMainId.setCaseSeq(t1NegMain.getCaseSeq() + 1);
		} else {
			InserttNegMainId.setCaseSeq(1);
		}
		InserttNegMainId.setCustNo(CustNo);
		InserttNegMain.setNegMainId(InserttNegMainId);
		InserttNegMain.setStatus("0");
		InserttNegMain.setChgCondDate(0); // 新一筆申請變更還款條件日=0
		InserttNegMain.setTotalContrAmt(new BigDecimal(CHANGE_TOTAL_AMT));// 簽約總金額
		InserttNegMain.setTotalPeriod(parse.stringToInteger(FIRST_PERIOD));// 期數
		InserttNegMain.setIntRate(parse.stringToBigDecimal(FIRST_RATE)); // 計息條件%
		InserttNegMain.setDueAmt(parse.stringToBigDecimal(PAY_AMOUNT));// 月付金
		InserttNegMain.setFirstDueDate(FirstDueDate);// 首次應繳日
		totalContrAmt = InserttNegMain.getTotalContrAmt();// 分攤檔金額檢核使用
		dueAmt = InserttNegMain.getDueAmt();// 分攤檔金額檢核使用

		int LastDueDate = 0;
		if (InserttNegMain.getTotalPeriod() > 0) {
			LastDueDate = negCom.getRepayDate(InserttNegMain.getFirstDueDate(), InserttNegMain.getTotalPeriod() - 1, titaVo); // 並非計算下一期故須減1
		} else {
			LastDueDate = InserttNegMain.getFirstDueDate();
		}
		InserttNegMain.setLastDueDate(LastDueDate);// 還款結束日

		InserttNegMain.setNextPayDate(FirstDueDate);// 下次應繳日
		InserttNegMain.setRepaidPeriod(0);// 已繳期數
		InserttNegMain.setPayIntDate(chgCondDate);// 繳息迄日=申請變更還款條件日
		InserttNegMain.setPrincipalBal(new BigDecimal(CHANGE_TOTAL_AMT));// 總本金餘額
		InserttNegMain.setDeferYMStart(0);// 延期繳款年月(起)
		InserttNegMain.setDeferYMEnd(0);// 延期繳款年月(訖)
		InserttNegMain.setAccuTempAmt(BigDecimal.ZERO);// 累繳金額
		InserttNegMain.setAccuOverAmt(BigDecimal.ZERO);// 累溢繳金額
		InserttNegMain.setAccuDueAmt(BigDecimal.ZERO);// 累應還金額
		InserttNegMain.setAccuSklShareAmt(BigDecimal.ZERO);// 累新壽分攤金額
		InserttNegMain.setRepayPrincipal(BigDecimal.ZERO);// 累償還本金
		InserttNegMain.setRepayInterest(BigDecimal.ZERO);// 累償還利息
		InserttNegMain.setStatusDate(0);// 戶況日期
		String isMainFin = "";
		if (("458").equals(MAIN_CODE)) {
			isMainFin = "Y";
		} else {
			isMainFin = "N";
		}
		InserttNegMain.setIsMainFin(isMainFin);// 是否最大債權

		String TwoStepCode = "N";
		if (("Y").equals(LAD_PAY_NOTE)) {
			if (LAD_PERIOD != null && LAD_PERIOD.length() != 0 && parse.stringToInteger(LAD_PERIOD) > 0) {
				TwoStepCode = "2";
			} else {
				TwoStepCode = "1";
			}
		}
		InserttNegMain.setTwoStepCode(TwoStepCode);

		try {
			sNegMainService.insert(InserttNegMain, titaVo);
		} catch (DBException e1) {
			// E0005 新增資料時，發生錯誤
			throw new LogicException(titaVo, "E0005", "債務協商案件主檔");
		}

		// JcicZ062
		if (("458").contentEquals(MAIN_CODE)) {
			JcicZ062Id tJcicZ062Id = new JcicZ062Id();
			tJcicZ062Id.setChangePayDate(chgCondDate);
			tJcicZ062Id.setCustId(IDN_BAN);
			tJcicZ062Id.setRcDate(ApplyDate);
			tJcicZ062Id.setSubmitKey("458");

			JcicZ062 tJcicZ062 = sJcicZ062Service.findById(tJcicZ062Id, titaVo);
			if (tJcicZ062 != null) {

			} else {
				tJcicZ062 = new JcicZ062();
				tJcicZ062.setJcicZ062Id(tJcicZ062Id);
				tJcicZ062.setTranKey("A");// 交易代號
				tJcicZ062.setCompletePeriod(parse.stringToInteger(COMMIT_PAY_PERIOD));// 變更還款條件已履約期數
				tJcicZ062.setPeriod(parse.stringToInteger(FIRST_PERIOD));// (第一階梯)期數
				tJcicZ062.setRate(parse.stringToBigDecimal(FIRST_RATE));// (第一階梯)利率
				tJcicZ062.setExpBalanceAmt(parse.stringToInteger(REM_EXP_AMT));// 信用貸款協商剩餘債務簽約餘額
				tJcicZ062.setCashBalanceAmt(parse.stringToInteger(REM_CASH_AMT));// 現金卡協商剩餘債務簽約餘額
				tJcicZ062.setCreditBalanceAmt(parse.stringToInteger(REM_CREDIT_AMT));// 信用卡協商剩餘債務簽約餘額
				tJcicZ062.setChaRepayAmt(parse.stringToBigDecimal(CHANGE_TOTAL_AMT));// 變更還款條件簽約總債務金額
				tJcicZ062.setChaRepayAgreeDate(DateRocToDC(CHANGE_PASS_DATE, "變更還款條件協議完成日", titaVo));// 變更還款條件協議完成日
				tJcicZ062.setChaRepayViewDate(DateRocToDC(CHANGE_INTERVI_DATE, "變更還款條件面談日期", titaVo));// 變更還款條件面談日期
				tJcicZ062.setChaRepayEndDate(DateRocToDC(CHANGE_SIGN_DATE, "變更還款條件簽約日期", titaVo));// 變更還款條件簽約完成日期
				tJcicZ062.setChaRepayFirstDate(FirstDueDate);// 變更還款條件首期應繳款日
				tJcicZ062.setPayAccount(PAY_ACCOUNT);// 繳款帳號
				tJcicZ062.setPostAddr("台北市中正區忠孝西路一段66號18樓");// 最大債權金融機構聲請狀送達地址
				tJcicZ062.setMonthPayAmt(parse.stringToInteger(PAY_AMOUNT));// 月付金
				tJcicZ062.setGradeType(TwoStepCode);// 屬階梯式還款註記
				tJcicZ062.setPeriod2(parse.stringToInteger(LAD_PERIOD));// 第二階梯期數
				tJcicZ062.setRate2(parse.stringToBigDecimal(LAD_RATE));// 第二階梯利率
				tJcicZ062.setMonthPayAmt2(parse.stringToInteger(LAD_PAY_AMT));// 第二階段月付金
				tJcicZ062.setOutJcicTxtDate(0);
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ062.setUkey(iKey);

				try {
					sJcicZ062Service.insert(tJcicZ062, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "金融機構無擔保債務變更還款條件協議資料");
				}
			}
		}
	}

	public void doZZM264(String Code, Map<String, String[]> mLineCut, TitaVo titaVo) throws LogicException {
		// ZZM264 債務協商 債權金融機構剩餘無擔保債權暨還款分配資料 (Z98 各債權金融機構剩餘無擔保債權暨還款分配資料(變更還款後))
		// IDN_BAN Char(10) 身分證號
		// RECEIVE_DATE Char(7) 協商申請日期
		// MAIN_CODE Char(3) 最大債權金融機構
		// CHANGE_PAY_DATE Char(7) 申請變更還款條件日
		// BANK_CODE Char(3) 債權金融機構
		// BANK_CODE_NAME Char(24) 債權金融機構名稱
		// EXP_LOAN_AMT Num(9) 信用貸款債權金額
		// CASH_CARD_AMT Num(9) 現金卡債權金額
		// CREDIT_CARD_AMT Num(9) 信用卡債權金額
		// DISP_AMT1 Num(9) 第一階梯分配金額
		// DISP_AMT2 Num(9) 第二階梯分配金額
		// LOAN_RATE Num(6) 佔全部協商無擔保債權比例 小數點兩位, 例如023.45
		// MAIN_SEND_NOTE Char(1) 最大債權金融機構報送註記
		// FILLER Char(30) 保留欄位
		String IDN_BAN = getCutSkill(Code, mLineCut, "IDN_BAN", titaVo);// 身分證號
		String RECEIVE_DATE = getCutSkill(Code, mLineCut, "RECEIVE_DATE", titaVo);// 協商申請日期
		String MAIN_CODE = getCutSkill(Code, mLineCut, "MAIN_CODE", titaVo);// 最大債權金融機構
//		String CHANGE_PAY_DATE=getCutSkill(titaVo,Code,mLineCut,"CHANGE_PAY_DATE");//申請變更還款條件日
		String BANK_CODE = getCutSkill(Code, mLineCut, "BANK_CODE", titaVo);// 債權金融機構
//		String BANK_CODE_NAME=getCutSkill(titaVo,Code,mLineCut,"BANK_CODE_NAME");//債權金融機構名稱
		String EXP_LOAN_AMT = getCutSkill(Code, mLineCut, "EXP_LOAN_AMT", titaVo);// 信用貸款債權金額
		String CASH_CARD_AMT = getCutSkill(Code, mLineCut, "CASH_CARD_AMT", titaVo);// 現金卡債權金額
		String CREDIT_CARD_AMT = getCutSkill(Code, mLineCut, "CREDIT_CARD_AMT", titaVo);// 信用卡債權金額
		String DISP_AMT1 = getCutSkill(Code, mLineCut, "DISP_AMT1", titaVo);// 第一階梯分配金額
//		String DISP_AMT2=getCutSkill(titaVo,Code,mLineCut,"DISP_AMT2");//第二階梯分配金額
		String LOAN_RATE = getCutSkill(Code, mLineCut, "LOAN_RATE", titaVo);// 佔全部協商無擔保債權比例 小數點兩位, 例如023.45
//		String MAIN_SEND_NOTE=getCutSkill(titaVo,Code,mLineCut,"MAIN_SEND_NOTE");//最大債權金融機構報送註記

		int CustNo = CheckCustId(IDN_BAN, titaVo);
		custid = IDN_BAN;

		// 檢查債權機構
		CheckNegFinAcct(BANK_CODE, IDN_BAN, "ZZM264", titaVo);

		if (("458").equals(MAIN_CODE)) { // 最大債權才做
			NegMain tNegMain = sNegMainService.custNoAndApplDateFirst(CustNo, DateRocToDC(RECEIVE_DATE, "協商申請日", titaVo), MAIN_CODE, titaVo);

			if (tNegMain != null) {
				// 已有資料
				InsertNegFinShare(IDN_BAN, CustNo, tNegMain.getNegMainId().getCaseSeq(), BANK_CODE, EXP_LOAN_AMT, CASH_CARD_AMT, CREDIT_CARD_AMT, LOAN_RATE, DISP_AMT1, "ZZM264", titaVo);
			} else {
				// 無資料
				if (errorMsg == 1) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "身分證字號[" + IDN_BAN + "]債權資料不存在.");
				}

			}
		}
	}
}