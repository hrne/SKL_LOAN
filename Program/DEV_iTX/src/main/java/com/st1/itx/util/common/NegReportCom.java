package com.st1.itx.util.common;

import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.NegAppr;
//import com.st1.itx.db.domain.NegApprId;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
import com.st1.itx.db.domain.NegAppr02;
//import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.domain.NegAppr02Id;
/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;
import com.st1.itx.db.domain.NegTrans;
//import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.db.service.NegApprService;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareService;
//import com.st1.itx.db.domain.NegApprId;
/* DB服務 */
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.springjpa.cm.L597AServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.data.DataLog;
/* 交易共用組件 */
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

//import com.st1.itx.util.common.MakeFile;
/**
 * 使用到的地方 L5074 債務協商作業－應處理事項清單 L597A 整批處理 L5075 債務協商滯繳/應繳明細查詢
 * 
 * @author Jacky Lu
 */
@Component("negReportCom")
@Scope("prototype")
public class NegReportCom extends CommBuffer {
	/* DB服務注入 */

	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public AcReceivableService sAcReceivableService;
	@Autowired
	public NegApprService sNegApprService;
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	@Autowired
	public NegAppr02Service sNegAppr02Service;
	@Autowired
	public NegFinShareService sNegFinShareService;
	@Autowired
	public NegFinAcctService sNegFinAcctService;

	@Autowired
	public GSeqCom gSeqCom;

	@Autowired
	public NegCom NegCom;

	@Autowired
	public L597AServiceImpl l597AServiceImpl;
	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;

	@Autowired
	public DataLog dataLog;

	/* 檔案上下傳工具 */
//	@Autowired
//	public FileCom fileCom;
	@Autowired
	public MakeFile makeFile;

//	String ConnectWord=",";//Key值區分字串
	String CombineWord = ";";// 連接字串
//	String UseDbNegTrans="NegTrans";
//	String UseDbAcReceivable="AcReceivable";
//	String UseDbNegAppr02="NegAppr02";

	BigDecimal BigDecimalZero = BigDecimal.ZERO;

	String ConnectWord = ",";
	String ChangeLine = "/n";

	public NegReportCom() {
	}

	/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
	private int index = 0;
	/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
	private Integer limit = Integer.MAX_VALUE;// 查全部

	public long CreateTxt(TitaVo titaVo, StringBuffer sb, String FileName) throws LogicException {
		long sno = 0L;
		ArrayList<String> TxtData = new ArrayList<String>();
		String Data[] = sb.toString().split(ChangeLine);
		if (Data != null && Data.length != 0) {
			String fileitem = "";
			String filename = "";
			switch (FileName) {
			case "BACHTX01":
				// For L5707 BatchTx01
				fileitem = "最大債權撥付產檔";
				filename = "BACHTX01.txt";
				break;
			case "BACHTX03":
				// For L5710 BACHTX03
				fileitem = "一般債權撥付資料檢核";
				filename = "BACHTX03.txt";
				break;
			}
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), fileitem, filename, 2);
			for (String ThisLine : Data) {
				TxtData.add(ThisLine);
				makeFile.put(ThisLine);
			}
			sno = makeFile.close();
			makeFile.toFile(sno);// 及時輸出的才要加這行
		}
		return sno;
		/*
		 * DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd"); Date date = new
		 * Date(); dateFormat.format(date); //String FilePath = "C:\\SKL\\out\\"; String
		 * Path="C:\\SKL\\out\\";//St1Share的路徑 String FilenameExtension=".txt"; int
		 * TempSeries=1; File DirFile=new File(Path); if(DirFile.exists()) {
		 * DirFile.mkdirs(); }
		 * 
		 * try { // 用共用工具寫入檔案
		 * 
		 * //String YYYYmmdd = dateFormat.format(date); //String FilePath =
		 * "C:\\SKL\\out\\"; //String FileName = YYYYmmdd + "L5511" + "_" +
		 * String.valueOf(FlowNum) + ".txt"; String OutFilePath
		 * =Path+FileName+"_"+String.valueOf(TempSeries)+FilenameExtension; File fw =
		 * new File(OutFilePath); // 相對路徑，如果沒有則要建立一個新的檔案。txt檔案 while (fw.exists()) {
		 * TempSeries++; OutFilePath
		 * =Path+FileName+"_"+String.valueOf(TempSeries)+FilenameExtension; fw = new
		 * File(OutFilePath); } ArrayList<String> TxtData=new ArrayList<String>();
		 * String Data[]=sb.toString().split(ChangeLine); if(Data!=null &&
		 * Data.length!=0) { for(String ThisLine:Data) { TxtData.add(ThisLine); } }
		 * //TxtData.add(sb.toString()); fileCom.outputTxt(TxtData, OutFilePath, false);
		 * } catch (IOException e) { e.printStackTrace(); throw new
		 * LogicException("XXXXX", "L5511 output error"); }
		 */

	}

	@SuppressWarnings("resource")
	public List<String[]> BatchTx04(TitaVo titaVo, String FilePath, String BringUpDate) throws LogicException, IOException {
		int DataLength[] = { 8, 19, 15 };
		File file = new File(FilePath);
		this.info("BatchTx04 FilePath = [" + FilePath + "]");
		List<String[]> lDetailData = new ArrayList<String[]>();
		titaVo.getTlrNo();
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (br.ready()) {
				String ThisLine = br.readLine();
				this.info("BatchTx04 = [" + ThisLine + "]");
				// 區別碼
				String Identifier = ThisLine.substring(0, 1);
				// 發件單位
				String FromFinCode = ThisLine.substring(1, 9);
				// 收件單位
				String ToFinCode = ThisLine.substring(9, 17);
				// 入/扣帳日-民國年
				String AssigeDate = ThisLine.substring(17, 24);
				if (!AssigeDate.equals(BringUpDate)) {
					// E0015 檢查錯誤
					throw new LogicException(titaVo, "E0015", "提兌日與檔案日期不符");
				}
				// 轉帳類別
				String TransAccCode = ThisLine.substring(24, 29);
				switch (Identifier) {
				case "1":
					// 首錄
					String Header[] = new String[DataLength[0]];
					// 區別碼
					Header[0] = Identifier;
					// 發件單位
					Header[1] = FromFinCode;
					// 收件單位
					Header[2] = ToFinCode;
					// 入/扣帳日-民國年
					Header[3] = AssigeDate;
					// 轉帳類別
					Header[4] = TransAccCode;
					// 性質別
					Header[5] = ThisLine.substring(29, 30);
					// 批號
					Header[6] = ThisLine.substring(30, 32);
					// 保留欄
					Header[7] = ThisLine.substring(32, ThisLine.length());
					break;
				case "2":
					// 明細錄
					String Detail[] = new String[DataLength[1]];
					// 區別碼
					Detail[0] = Identifier;
					// 發件單位
					Detail[1] = FromFinCode;
					// 收件單位
					Detail[2] = ToFinCode;
					// 入/扣帳日-民國年
					Detail[3] = AssigeDate;
					// 轉帳類別
					Detail[4] = TransAccCode;
					// 交易序號-
					// 1.由發件單位自訂.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.
					// 2.編碼原則:第一碼固定為[0],第二碼及第三碼為批號,應與首錄之批號欄位相同,後七碼由發件單位自訂.
					Detail[5] = ThisLine.substring(29, 39);
					// 檢核欄位-固定為[+]
					Detail[6] = ThisLine.substring(39, 40);
					// 交易金額
					// 入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位
					Detail[7] = ThisLine.substring(40, 53);
					// 委託單位區別
					Detail[8] = ThisLine.substring(53, 54);
					// 委託單位-委託轉帳之事業統編
					// 1. 轉帳類別為 0290 02970時,該欄位置入最大債權銀行/發卡機構代號,左靠右補0,代號首位為[A]時請置入[10]
					Detail[9] = ThisLine.substring(54, 62);
					// 實際入/扣帳日期
					Detail[10] = ThisLine.substring(62, 69);
					// 回應代碼
					Detail[11] = ThisLine.substring(69, 73);
					// 轉帳行代碼
					Detail[12] = ThisLine.substring(73, 80);
					// 轉帳帳號
					Detail[13] = ThisLine.substring(80, 96);
					// 帳戶ID
					Detail[14] = ThisLine.substring(96, 106);
					// 銷帳編號
					Detail[15] = ThisLine.substring(106, 122);
					// 費用代號
					Detail[16] = ThisLine.substring(122, 126);
					// 銀行專用區
					Detail[17] = ThisLine.substring(126, 146);
					// 事業單位專用資料區
					Detail[18] = ThisLine.substring(146, ThisLine.length());
					lDetailData.add(Detail);
					break;
				case "3":
					// 尾錄
					String End[] = new String[DataLength[2]];
					// 區別碼
					End[0] = Identifier;
					// 發件單位
					End[1] = FromFinCode;
					// 收件單位
					End[2] = ToFinCode;
					// 入/扣帳日-民國年
					End[3] = AssigeDate;
					// 轉帳類別
					End[4] = TransAccCode;
					// 檢核欄位
					End[5] = ThisLine.substring(29, 30);
					// 交易總金額
					// 入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位
					End[6] = ThisLine.substring(30, 45);
					// 交易總筆數
					End[7] = ThisLine.substring(45, 55);
					// 檢核欄位-固定[+]
					End[8] = ThisLine.substring(55, 56);
					// 成交總金額-右靠左補0 9(13)V99
					End[9] = ThisLine.substring(56, 71);
					// 成交總筆數
					End[10] = ThisLine.substring(71, 81);
					// 檢核欄位-固定[+]
					End[11] = ThisLine.substring(81, 82);
					// 未成交總金額-右靠左補0 9(13)V99
					End[12] = ThisLine.substring(82, 97);
					// 未成交總筆數
					End[13] = ThisLine.substring(97, 107);
					// 保留欄
					End[14] = ThisLine.substring(107, ThisLine.length());
					break;
				default:
					// E5006 未預期的錯誤
					throw new LogicException(titaVo, "E5006", "檔案區別碼錯誤");
				}
			}
			fr.close();
			fr = null;
		} else {
			String ErrorMsg = "檔案不存在,請查驗路徑.\r\n" + FilePath;
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", ErrorMsg);
		}
		return lDetailData;
	}

	@SuppressWarnings("resource")
	public StringBuffer BatchTx02(TitaVo titaVo, String FilePath, String BringUpDate) throws LogicException, IOException {
		StringBuffer sbBatchTx03 = new StringBuffer();
		int DataLength[] = { 8, 14, 8 };
		File file = new File(FilePath);
//		List<String[]> lDetailData = new ArrayList<String[]>();// 明細表資料
		List<String[]> lTotalData = new ArrayList<String[]>();//
//		String TlrNo = titaVo.getTlrNo();// 經辦

		// 交易序號-CdGseqCom-getSeqNo-抓兩馬-+自己流水序號(2碼)
		// param1: Date 編號日期
		// param2: Code 編號方式 0:不分 1:年度編號 2:月份編號 3:日編號
		// param3: Type 業務類別 L1, L2, L3, ...
		// param4: Kind 交易種類 0001:戶號 0002:案件申請編號
		// param5: Offset 有效值 例如:有效值=999 , 當流水號=999時 , 下一個流水號從001開始
		// param6: titaVo
//		int TxtNo = gSeqCom.getSeqNo(Today, 0, "XX", "BATX", 99, titaVo);// 交易序號(2)
		if (file.exists()) {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

//			int FlowNo = 0;
			while (br.ready()) {
				String ThisLine = br.readLine();
				// 區別碼
				String Identifier = ThisLine.substring(0, 1);
				// 發件單位
				String FromFinCode = ThisLine.substring(1, 9);
				// 收件單位
				String ToFinCode = ThisLine.substring(9, 17);
				// 入/扣帳日-民國年
				String AssigeDate = ThisLine.substring(17, 24);
				// 轉帳類別
				String TransAccCode = ThisLine.substring(24, 29);
				switch (Identifier) {
				case "1":
					// 首錄
					String Header[] = new String[DataLength[0]];
					// 區別碼
					Header[0] = Identifier;
					// 發件單位
					Header[1] = FromFinCode;
					// 收件單位
					Header[2] = ToFinCode;
					// 入/扣帳日-民國年
					Header[3] = AssigeDate;
					// 轉帳類別
					Header[4] = TransAccCode;
					// 性質別
					Header[5] = ThisLine.substring(29, 30);
					// 批號
					Header[6] = ThisLine.substring(30, 32);
					// 保留欄
					Header[7] = ThisLine.substring(32, ThisLine.length());
					lTotalData.add(Header);
					break;
				case "2":
					// 明細錄
					String Detail[] = new String[DataLength[1]];
					// 區別碼
					Detail[0] = Identifier;
					// 發件單位
					Detail[1] = FromFinCode;
					// 收件單位
					Detail[2] = ToFinCode;
					// 入/扣帳日-民國年
					Detail[3] = AssigeDate;
					// 轉帳類別
					Detail[4] = TransAccCode;
					// 交易序號-
					// 1.由發件單位自訂.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.
					// 2.編碼原則:第一碼固定為[0],第二碼及第三碼為批號,應與首錄之批號欄位相同,後七碼由發件單位自訂.
					Detail[5] = ThisLine.substring(29, 39);
					// 交易金額
					// 入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位
					Detail[6] = ThisLine.substring(39, 52);
					// 委託單位-委託轉帳之事業統編
					// 1. 轉帳類別為 0290 02970時,該欄位置入最大債權銀行/發卡機構代號,左靠右補0,代號首位為[A]時請置入[10]
					Detail[7] = ThisLine.substring(52, 60);
					// 轉帳行代碼
					Detail[8] = ThisLine.substring(60, 67);
					// 轉帳帳號
					Detail[9] = ThisLine.substring(67, 83);
					// 帳戶ID
					Detail[10] = ThisLine.substring(83, 93);
					// 銷帳編號
					Detail[11] = ThisLine.substring(93, 109);
					// 銀行專用區
					Detail[12] = ThisLine.substring(109, 129);
					// 事業單位專用資料區
					Detail[13] = ThisLine.substring(129, ThisLine.length());
//					lDetailData.add(Detail);
					lTotalData.add(Detail);
					break;
				case "3":
					// 尾錄
					String End[] = new String[DataLength[2]];
					// 區別碼
					End[0] = Identifier;
					// 發件單位
					End[1] = FromFinCode;
					// 收件單位
					End[2] = ToFinCode;
					// 入/扣帳日-民國年
					End[3] = AssigeDate;
					// 轉帳類別
					End[4] = TransAccCode;
					// 交易總金額
					// 入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位
					End[5] = ThisLine.substring(29, 44);
					// 交易總筆數
					End[6] = ThisLine.substring(44, 54);
					// 保留欄
					End[6] = ThisLine.substring(54, ThisLine.length());
					lTotalData.add(End);
					break;
				default:
					// E5006 未預期的錯誤
					throw new LogicException(titaVo, "E5006", "");
				}
			}
			fr.close();
			fr = null;

			if (lTotalData != null && lTotalData.size() != 0) {

				BigDecimal SumTranAmt = new BigDecimal("0");// 交易總金額
				int SumTranCount = 0;// 交易總筆數
				BigDecimal SumSuccessTranAmt = new BigDecimal("0");// 成交總金額
				int SumSuccessTranCount = 0;// 成交總筆數
				BigDecimal SumUnSuccessTranAmt = new BigDecimal("0");// 未成交總金額
				int SumUnSuccessTranCount = 0;// 未成交總筆數

				int llTotalDataS = lTotalData.size();
				for (int i = 0; i < llTotalDataS; i++) {
					String Detail[] = lTotalData.get(i);
					StringBuffer sbDetailBatchTx03 = new StringBuffer();

					StringBuffer sbHeadBatchTx03 = new StringBuffer();

					StringBuffer sbEndBatchTx03 = new StringBuffer();

					String Identifier = Detail[0];//

					StringBuffer ThisHeader = new StringBuffer();
					String Header1 = "1";// 區別碼
					String Header2 = LRFormat(Detail[1], 8, "L", " ");// 發件單位
					String Header3 = LRFormat(Detail[2], 8, "L", " ");// 收件單位
					String Header4 = Detail[3];// 指定入/扣帳日
					if (!Header4.equals(BringUpDate)) {
						// E0015 檢查錯誤
						throw new LogicException(titaVo, "E0015", "提兌日與檔案日期不符");
					}

					String Header5 = Detail[4];// 轉帳類別
					String Header2_5 = Header2 + Header3 + Header4 + Header5;
					String Header6 = "2";// 性質別
					String Header7 = LRFormat(Detail[6], 2, "L", "0 ");// 批號
					String Header8 = "                                                                              ";// 保留欄位
					if ("1".equals(Identifier)) {

						ThisHeader.append(Header1);
						ThisHeader.append(Header2);
						ThisHeader.append(Header3);
						ThisHeader.append(Header4);
						ThisHeader.append(Header5);
						ThisHeader.append(Header6);
						ThisHeader.append(Header7);
						ThisHeader.append(Header8);
						if (sbHeadBatchTx03.equals(ThisHeader)) {

						} else {
							sbHeadBatchTx03 = ThisHeader;
							sbBatchTx03.append(sbHeadBatchTx03 + ChangeLine);
						}
					} // if

					if ("2".equals(Identifier)) {

						String FromFinCode = Detail[1];// 發件單位
						String ToFinCode = Detail[2];// 收件單位
						String EntryDate = Detail[3];// 入/扣帳日(民國年)
						if (!EntryDate.equals(BringUpDate)) {
							// E0015 檢查錯誤
							throw new LogicException(titaVo, "E0015", "提兌日與檔案日期不符");
						}
						String TransAccCode = Detail[4];// 轉帳類別
						String CustId = Detail[10].trim();
						String TxSeq = Detail[5];// (10)
						String FinIns = Detail[8];// 轉帳行代碼
						String RemitAcct = Detail[9];// 轉帳帳號
						BigDecimal TxAmt = new BigDecimal(Detail[6]).divide(new BigDecimal(100));

						NegAppr02Id tNegAppr02Id = new NegAppr02Id();
						NegAppr02 tNegAppr02 = new NegAppr02();
						// 提兌日
						tNegAppr02Id.setBringUpDate(parse.stringToInteger(BringUpDate));
						// 債權機構代號(8)
						tNegAppr02Id.setFinCode(FromFinCode);
						// 資料交易序號
						tNegAppr02Id.setTxSeq(TxSeq);

						tNegAppr02.setNegAppr02Id(tNegAppr02Id);

						// 會計日期
						tNegAppr02.setAcDate(0);

						// 發件單位 Detail[1]
						tNegAppr02.setSendUnit(FromFinCode);
						// 收件單位 Detail[2]
						tNegAppr02.setRecvUnit(ToFinCode);

						// 入/扣帳日-民國年 Detail[3]
						tNegAppr02.setEntryDate(Integer.parseInt(EntryDate) + 19110000);

						// 轉帳類別 Detail[4]

						tNegAppr02.setTransCode(TransAccCode);

//					  //交易序號-
//					  //1.由發件單位自訂.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.
//					  //2.編碼原則:第一碼固定為[0],第二碼及第三碼為批號,應與首錄之批號欄位相同,後七碼由發件單位自訂.
						// tNegAppr02.setTxSeq(TxSeq); //資料檔交易序號(10)

						// 交易金額 Detail[6]
						// 入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位

						tNegAppr02.setTxAmt(TxAmt);

						// 委託單位-委託轉帳之事業統編 Detail[7]
						// 1. 轉帳類別為 0290 02970時,該欄位置入最大債權銀行/發卡機構代號,左靠右補0,代號首位為[A]時請置入[10]
						tNegAppr02.setConsign(Detail[7]);
						// 轉帳行代碼 Detail[8]
						tNegAppr02.setFinIns(FinIns);
						// 轉帳帳號 Detail[9]
						tNegAppr02.setRemitAcct(RemitAcct);
						// 帳戶ID Detail[10]
						tNegAppr02.setCustId(CustId);
						// 銷帳編號 Detail[11]
						String BatchTx02Detail12 = Detail[11];
						BatchTx02Detail12.substring(0, 1);
						switch (TransAccCode) {
						case "01010":
							// 省水水號(11位)
							break;
						case "01020":
							// 台電電號(11位)+再送次數(1位,首次為空白,再送時從1開始編起)
							break;
						case "010610":
							// 保單編號(06位)
							break;
						case "01070":
							// 編號(10位)
							break;
						case "01110":
							// 牌照號碼(10位)
							break;
						case "01120":
							// 市水水號(10位)
							break;
						case "01960":
							// 還款所屬年月(4位)
							// Detail16=String.valueOf(tNegAppr01.getApprDate());

							break;
						case "01970":
							// 還款所屬年月(4位)
							break;
						case "02020":
							// 股票代號(6位)
							break;
						case "02070":
							// 編號(10位)
							break;
						case "02100":
							// 津貼受理號碼(11位)
							break;
						case "02120":
							// 市水水號(10位)
							break;
						case "02960":
							// 還款狀況-0:正常,1:溢繳,2:短繳,3:大額還本,4:結清

							break;
						case "02970":
							// 還款狀況-0:正常,1:溢繳,2:短繳,3:大額還本,4:結清

							break;
						default:
							// E5006 未預期的錯誤
							throw new LogicException(titaVo, "E5006", "檔案轉帳類別錯誤");
						} // switch
							// 銀行專用區 Detail[12]

						// 事業單位專用資料區 Detail[13]

						// 還款狀況
						String StatusCode = "";

						// 先查詢後新增

						// 檢核
						// 戶號要存在
						int CustNo = 0;
						if (CustId != null && CustId.length() != 0) {
							CustMain tCustMain = sCustMainService.custIdFirst(CustId, titaVo);
							if (tCustMain != null) {
								CustNo = tCustMain.getCustNo();
							} // if
						} // if
						tNegAppr02.setCustNo(CustNo);

						if (CustNo != 0) {
							// 戶號存在
							// 檢查NegMain在不在
							NegMain tNegMain = sNegMainService.custNoFirst(CustNo, titaVo);
							if (tNegMain != null) {
								// 已存在
								StatusCode = "4001"; // 4001:入/扣帳成功
							} else {
								// 無債權資料
								StatusCode = "4808"; // 無此帳戶或問題帳戶
							}
						} else {
							// 戶號不存在
							StatusCode = "4808"; // 無此帳戶或問題帳戶
						}
						// 4001:入/扣帳成功
						// 4505:存款不足
						// 4508:非委託或已終止帳戶
						// 4806:存戶查核資料錯誤
						// 4808:無此帳戶或問題帳戶
						// 4405:未開卡或額度不足
						// 4705:剃除不轉帳
						// 2999:其他錯誤
						tNegAppr02.setStatusCode(StatusCode);
						NegAppr02 NegAppr02Trial = sNegAppr02Service.holdById(tNegAppr02Id, titaVo);
						// tNegAppr02Id
						if (NegAppr02Trial != null) {
							try {
								sNegAppr02Service.update(tNegAppr02, titaVo);
							} catch (DBException e) {
								// E0007 更新資料時，發生錯誤
								throw new LogicException(titaVo, "E0007", "一般債權撥付資料檔");
							}
						} else {
							try {
								sNegAppr02Service.insert(tNegAppr02, titaVo);
							} catch (DBException e) {
								throw new LogicException(titaVo, "E0005", "一般債權撥付資料檔");
							}
						}

						String Detail1 = "2";// 區別碼
						String Detail2_5 = Header2_5;
						String Detail6 = TxSeq;// 交易序號
						String Detail7 = String.format("%2.2f", TxAmt);// 交易金額
						Detail7 = LRFormat(Detail7.replace(".", ""), 13, "R", "0");

						String Detail8 = FinIns;// 轉帳行代碼
						String Detail9 = LRFormat(RemitAcct, 16, "R", "0");// 轉帳帳號
						String Detail10 = StatusCode;// 回應代碼
						String Detail11 = "";// 銀行專用區
						if ("4001".equals(StatusCode)) {
							Detail11 = parse.IntegerToString(CustNo, 7) + "             ";
						} else {
							Detail11 = "0601776             ";
						}

						String Detail12 = "           ";// 事業單位專用資料區

						sbDetailBatchTx03.append(Detail1);
						sbDetailBatchTx03.append(Detail2_5);
						sbDetailBatchTx03.append(Detail6);
						sbDetailBatchTx03.append(Detail7);
						sbDetailBatchTx03.append(Detail8);
						sbDetailBatchTx03.append(Detail9);
						sbDetailBatchTx03.append(Detail10);
						sbDetailBatchTx03.append(Detail11);
						sbDetailBatchTx03.append(Detail12);

						sbBatchTx03.append(sbDetailBatchTx03 + ChangeLine);

						SumTranAmt = SumTranAmt.add(TxAmt);
						SumTranCount++;
						if (("4001").equals(StatusCode)) {
							// 成功
							SumSuccessTranAmt = SumSuccessTranAmt.add(TxAmt);
							SumSuccessTranCount++;
						} else {
							// 失敗
							SumUnSuccessTranAmt = SumUnSuccessTranAmt.add(TxAmt);
							SumUnSuccessTranCount++;
						}
					} // if
//					;//交易金額
//					Detail7=;
					if ("3".equals(Identifier)) {
						String End1 = "3";// 區別碼
						String End2_5 = Header2_5;

						String End6 = LRFormat(String.format("%2.2f", SumTranAmt).replace(".", ""), 15, "R", "0");// 交易總金額
						String End7 = LRFormat(String.valueOf(SumTranCount), 10, "R", "0");// 交易總筆數
						String End8 = LRFormat(String.format("%2.2f", SumSuccessTranAmt).replace(".", ""), 15, "R", "0");// 成交總金額
						String End9 = LRFormat(String.valueOf(SumSuccessTranCount), 10, "R", "0");// 成交總筆數
						String End10 = LRFormat(String.format("%2.2f", SumUnSuccessTranAmt).replace(".", ""), 15, "R", "0");// 未成交總金額
						String End11 = LRFormat(String.valueOf(SumUnSuccessTranCount), 10, "R", "0");// 未成交總筆數
						String End12 = "      ";// 保留欄位
						sbEndBatchTx03.append(End1);
						sbEndBatchTx03.append(End2_5);
						sbEndBatchTx03.append(End6);
						sbEndBatchTx03.append(End7);
						sbEndBatchTx03.append(End8);
						sbEndBatchTx03.append(End9);
						sbEndBatchTx03.append(End10);
						sbEndBatchTx03.append(End11);
						sbEndBatchTx03.append(End12);
//				      if (i == llTotalDataS1) {
						sbBatchTx03.append(sbEndBatchTx03 + ChangeLine);
//					  }
						SumTranAmt = new BigDecimal("0");
						SumTranCount = 0;
						SumSuccessTranAmt = new BigDecimal("0");
						SumSuccessTranCount = 0;
						SumUnSuccessTranAmt = new BigDecimal("0");
						SumUnSuccessTranCount = 0;
					}
				}
			}
			// 產出BACHTX03
		} else {
			// E0015 檢查錯誤
			throw new LogicException(titaVo, "E0015", "檔案不存在,請查驗路徑");
		}
		return sbBatchTx03;
	}

	public String CheckNegArrp(int IntDate, int Status, TitaVo titaVo) throws LogicException {
		Slice<NegAppr> slNegAppr = sNegApprService.bringUpDateEq(IntDate, 0, 500, titaVo);
		List<NegAppr> lNegAppr = slNegAppr == null ? null : slNegAppr.getContent();

		String pKindCode = "";

		// IntDate為畫面輸入之提兌日-西元年
		int bringupdate = IntDate - 19110000;
		int today = titaVo.getOrgEntdyI();// 會計日期
		int IntDateRoc = 0;
		if (today != 0 && String.valueOf(today).length() == 8) {
			IntDateRoc = today - 19110000;
		} else {
			IntDateRoc = today;
		}

		NegAppr tNegAppr = new NegAppr();
		NegAppr tNegApprOrg = new NegAppr();
		if (lNegAppr != null) {
			for (NegAppr cNegAppr : lNegAppr) {
				tNegAppr = sNegApprService.holdById(cNegAppr.getNegApprId(), titaVo);

				pKindCode += String.valueOf(tNegAppr.getKindCode());
				this.info("pKindCode 1==" + pKindCode);

				tNegApprOrg = tNegAppr;
				if (tNegAppr != null) {
					//this.info("tNegAppr.getExportDate()=[" + tNegAppr.getExportDate() + "] , String.valueOf(tNegAppr.getExportDate()).length()=[" + String.valueOf(tNegAppr.getExportDate()).length()
					//		+ "]");
					this.info("CheckNegArrp IntDate=[" + IntDateRoc + "]" + ",[製檔 " + tNegAppr.getExportMark() + "," + tNegAppr.getExportDate() + "]" + ",[傳票 " + tNegAppr.getApprAcMark() + ","
							+ tNegAppr.getApprAcDate() + "]" + ",[提兌 " + tNegAppr.getBringUpMark() + "," + tNegAppr.getBringUpDate() + "]");
					if (Status == 1) {

						// 撥付製檔
						if (titaVo.isHcodeNormal()) {
							// 正向
							if (tNegAppr.getExportMark() != 0) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行過[L5707撥付製檔]");
							}
							if (tNegAppr.getExportDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5704[製檔日期] 不等於 [會計日]");
							} else {
								tNegAppr.setExportMark(1);
							}
						} else {
							// 訂正
							if (tNegAppr.getExportDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5707[製檔日期]不等於 [會計日]");
							}
							if (tNegAppr.getBringUpMark() == 1 ) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行過[L5709最大債權撥付回覆檔檢核]");
							}
							if (tNegAppr.getApprAcMark() == 1) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行過[L5708最大債權撥付出帳]");
							}
							if (tNegAppr.getExportMark() == 0) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5707撥付製檔]");
							}

							tNegAppr.setExportMark(0);
							
						}
					} else if (Status == 2) {
						// 撥付傳票
						if (titaVo.isHcodeNormal()) {
							// 正向
							if (tNegAppr.getExportMark() != 1) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5707撥付製檔]");
							}
							if (tNegAppr.getApprAcMark() != 0) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行過[L5708最大債權撥付出帳]");
							}
							if (tNegAppr.getApprAcDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5704[傳票日期] 不等於 [會計日]");
							} else {
								tNegAppr.setApprAcMark(1);
							}
						} else {
							// 訂正
							if (tNegAppr.getApprAcDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5704[傳票日期] 不等於 [會計日]");
							}
							if (tNegAppr.getBringUpMark() == 1 ) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行過[L5709最大債權撥付回覆檔檢核]");
							}
							if (tNegAppr.getApprAcMark() == 0) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5708最大債權撥付出帳]");
							}
							
							tNegAppr.setApprAcMark(0);
						}
					} else if (Status == 3) {
						// 撥付提兌
						if (titaVo.isHcodeNormal()) {
							// 正向
							if (tNegAppr.getExportMark() != 1) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5707撥付製檔]");
							}
							if (tNegAppr.getApprAcMark() != 1) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5708最大債權撥付出帳]");

							}
							if (tNegAppr.getBringUpMark() != 0) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "已執行[L5709最大債權撥付回覆檔檢核]");
							}

							if (tNegAppr.getBringUpDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5704[提兌日] 不等於 [會計日]");
							} else {
								tNegAppr.setBringUpMark(1);
							}
						} else {
							// 訂正
							if (tNegAppr.getBringUpDate() != IntDateRoc) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "L5704[傳票日期] 不等於 [會計日]");
							}
							if (tNegAppr.getBringUpMark() == 0 ) {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "未執行[L5709最大債權撥付回覆檔檢核]");
							}
							tNegAppr.setBringUpMark(0);
						}
					}
					try {
						sNegApprService.update2(tNegAppr, titaVo);
						dataLog.setEnv(titaVo, tNegApprOrg, tNegAppr);// 資料異動後-2
						dataLog.exec();// 資料異動後-3
					} catch (DBException e) {

					}

				}
			}
		} else {
			// E0001 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "L5704撥付日期設定查無資料[提兌日:" + bringupdate + "]");
		}

		return pKindCode;

	}

	// L5707,L5708 記得有訂正
	public List<NegAppr01> InsUpdNegApprO1(int intbringupdate, int Status, TitaVo titaVo) throws LogicException {
		// IntDate =交易畫面輸入的提兌日-西元年
		String pKindCode = CheckNegArrp(intbringupdate, Status, titaVo);// 資料檢核
		this.info("pKindCode  2==" + pKindCode);

		List<NegAppr01> lNegAppr01 = new ArrayList<NegAppr01>();
		// 找出最大債權,已入帳,未製檔
		// 然後寫入NegAppr01
//		String ConnectWord=",";//Key值區分字串
//		String CombineWord=";";//連接字串
//		String UseDbNegTrans="NegTrans";
//		String UseDbAcReceivable="AcReceivable";
//		String UseDbNegAppr02="NegAppr02";

		int today = titaVo.getOrgEntdyI(); // 會計日期
		int intdate = today + 19110000; // intdate為會計日期-西元年

		int IsMainFin = 1;// 最大債權
		int State = 4;// 已入帳
		int Detail = 0;
		int ExportDateYN = -1;// 撥付製檔
		int IsBtn = 0;// 不是按鈕
		if (Status == 1) {
			// 撥付製檔
			// L5707
			ExportDateYN = 3;// 撥付製檔
		} else if (Status == 2) {
			// 撥付傳票日
			// L5708
			ExportDateYN = 4;// 撥付傳票
		} else if (Status == 3) {
			// 撥付提兌日
			// L5709去處理
			ExportDateYN = 5;// 撥付提兌
		}

		String sql = "";
		List<String[]> Data = null;
		try {
			sql = l597AServiceImpl.FindL597A(titaVo, intdate, IsMainFin, State, Detail, ExportDateYN, IsBtn, pKindCode);
		} catch (Exception e) {
			// E5003 組建SQL語法發生問題
			this.info("NegReportCom ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5003", "");
		}
		try {
			Data = l597AServiceImpl.FindL597A(l597AServiceImpl.FindData(this.index, this.limit, sql, titaVo, intdate, IsMainFin, State, Detail, ExportDateYN, IsBtn), "L597A");
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}

		if (Data != null && Data.size() != 0) {
			for (String[] lData : Data) {
				String UseDb = lData[0];// 使用資料庫
				String AcDate = lData[8];// 會計日
				String TitaTlrNo = lData[21];// 經辦
				String TitaTxtNo = lData[22];// 交易序號

				// ---L5707 不計算只壓日期
				if ("NegTrans".equals(UseDb)) {
					// AcDate,TitaTlrNo,TitaTxtNo
					NegTransId tNegTransId = new NegTransId();
					tNegTransId.setAcDate(Integer.parseInt(AcDate));
					tNegTransId.setTitaTlrNo(TitaTlrNo);
					tNegTransId.setTitaTxtNo(Integer.parseInt(TitaTxtNo));
					NegTrans tNegTrans = sNegTransService.holdById(tNegTransId, titaVo);
					if (tNegTrans != null) {
						// 直接找NEGAPPR01即可 ,原使用SumCustNo改用FindTrans
						this.info("no1  = " + tNegTrans.getCustNo());
						this.info("seq1  = " + tNegTrans.getCaseSeq());
						Slice<NegAppr01> sNegAppr01 = sNegAppr01Service.findTrans(Integer.parseInt(AcDate), TitaTlrNo, Integer.parseInt(TitaTxtNo), 0, Integer.MAX_VALUE, titaVo);
						List<NegAppr01> lTempNegAppr01 = sNegAppr01 == null ? null : sNegAppr01.getContent();

						if (lTempNegAppr01 != null && lTempNegAppr01.size() != 0) {
							lNegAppr01.addAll(lTempNegAppr01);
							for (NegAppr01 tNegAppr01 : lTempNegAppr01) {
								if (Status == 1) {
									// 撥付製檔
									int ExportDate = 0;
									if (titaVo.isHcodeNormal()) {
										// 正向
										this.info("table  = " + tNegAppr01);
										this.info("acdate  = " + tNegAppr01.getAcDate());
										this.info("經辦  = " + tNegAppr01.getTitaTlrNo());
										this.info("戶號  = " + tNegAppr01.getCustNo());
										this.info("製檔日期  = " + tNegAppr01.getExportDate());
										if (tNegAppr01.getExportDate() != 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔已製檔");
										}
										ExportDate = intdate; // 會計日期-西元年
									} else {
										// 逆向
										ExportDate = 0;
										tNegAppr01.setBatchTxtNo(" "); // 更正需清掉Batch交易序號
									}
									tNegAppr01.setExportDate(ExportDate);// 製檔日期
								} else if (Status == 2) {
									// 撥付傳票日
									// 撥付傳票
									int ApprDate = 0;// 撥付出帳日期
									if (titaVo.isHcodeNormal()) {
										// 正向
										if (tNegAppr01.getExportDate() == 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔未製檔");
										}
										if (tNegAppr01.getApprDate() != 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔已出帳");
										}
										ApprDate = intdate; // 會計日期-西元年
									} else {
										// 逆向
										ApprDate = 0;
									}
									tNegAppr01.setApprDate(ApprDate);// 撥付出帳日期
									tNegAppr01.setApprAcDate(ApprDate);// 撥付傳票日

								} else if (Status == 3) {
									// 撥付提兌日
									// L5709去處理
									int BringUpDate = 0;// 撥付提兌日期
									if (titaVo.isHcodeNormal()) {
										// 正向
										if (tNegAppr01.getExportDate() == 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔未製檔");
										}
										if (tNegAppr01.getApprDate() == 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔未出帳");
										}
										if (tNegAppr01.getBringUpDate() != 0) {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "最大債權撥付資料檔已提兌");
										}
										BringUpDate = intdate; // 會計日期-西元年
									} else {
										BringUpDate = 0;
									}
									tNegAppr01.setBringUpDate(BringUpDate);
								}

								try {
									NegAppr01 NegAppr01Org = sNegAppr01Service.holdById(tNegAppr01, titaVo);
									sNegAppr01Service.update2(tNegAppr01, titaVo);
									dataLog.setEnv(titaVo, NegAppr01Org, tNegAppr01);// 資料異動後-2
									dataLog.exec();
								} catch (DBException e) {
									// E0007 更新資料時，發生錯誤
									throw new LogicException(titaVo, "E0007", "最大債權撥付資料檔");
								}
							}

						} else {
							// E0001 查詢資料不存在
							throw new LogicException(titaVo, "E0001", "最大債權撥付資料檔");
						}

						// 異動NegTrans
						if (titaVo.isHcodeNormal()) {
							// 正向交易
							if (Status == 1) {
								// 撥付製檔
								tNegTrans.setExportDate(intdate); // 會計日期-西元年
							} else if (Status == 2) {
								// 撥付傳票
								tNegTrans.setExportAcDate(intdate); // 會計日期-西元年
							} else if (Status == 3) {
								// 撥付提兌
							}
						} else {
							// 訂正
							if (Status == 1) {
								// 撥付製檔
								tNegTrans.setExportDate(0);
							} else if (Status == 2) {
								// 撥付傳票
								tNegTrans.setExportAcDate(0);
							} else if (Status == 3) {
								// 撥付提兌
							}
						}
						// 異動NegTrnas
						if (Status == 1 || Status == 2) {
							// 撥付製檔 //撥付傳票
							this.info("NegReportCom InsUpdNegApprO1 sNegTransService");
							try {
								sNegTransService.update(tNegTrans, titaVo);
							} catch (DBException e) {
								// E0007 更新資料時，發生錯誤
								throw new LogicException(titaVo, "E0007", "債務協商交易檔");
							}
						}
					} else {
						// E0006 鎖定資料時，發生錯誤
						throw new LogicException(titaVo, "E0006", "債務協商交易檔");
					}
				} else {
					// E0010 功能選擇錯誤
					throw new LogicException(titaVo, "E0010", "");
				}
			}

		} else {
			// 查無有效資料
			throw new LogicException(titaVo, "E0001", "查無有效資料");
		}
		return lNegAppr01;
	}

	public StringBuffer BatchTx01(String BringUpDate, TitaVo titaVo) throws LogicException {
		StringBuffer sbBatchTx01 = new StringBuffer();
		// findFiled
		int IntBringUpdate = parse.stringToInteger(BringUpDate); // 畫面輸入之提兌日-轉西元年
		if (String.valueOf(IntBringUpdate).length() == 7 || String.valueOf(IntBringUpdate).length() == 6) {
			IntBringUpdate = IntBringUpdate + 19110000;
		}

		//// 找出最大債權,已入帳,未製檔
		// 然後寫入NegAppr01
		this.info("2  =");
		List<NegAppr01> lNegAppr01 = InsUpdNegApprO1(IntBringUpdate, 1, titaVo);
		if (!titaVo.isHcodeNormal()) {
			// 訂正
			// 在上方就會處理掉
			return sbBatchTx01;
		}
		// 入/扣帳日(同檔案一致)-Order4
		int AssigeDate = 0;//

		Slice<NegAppr> sNegAppr = sNegApprService.bringUpDateEq(IntBringUpdate, 0, 500, titaVo);
		List<NegAppr> lNegAppr = sNegAppr == null ? null : sNegAppr.getContent();

		if (lNegAppr != null && lNegAppr.size() != 0) {
			AssigeDate = lNegAppr.get(0).getBringUpDate();
		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[指定入/扣帳日]不存在");
		}
		if (AssigeDate != 0 && String.valueOf(AssigeDate).length() == 8) {
			// 轉成民國年
			AssigeDate = AssigeDate - 19110000;
		} else {

		}
		if (lNegAppr01 != null && lNegAppr01.size() != 0) {
			List<String[]> lTempData = new ArrayList<String[]>();
			List<String> lKey = new ArrayList<String>();

			for (NegAppr01 tNegAppr01 : lNegAppr01) {
				NegTransId tNegTransId = new NegTransId();

				tNegTransId.setAcDate(tNegAppr01.getNegAppr01Id().getAcDate());
				tNegTransId.setTitaTlrNo(tNegAppr01.getNegAppr01Id().getTitaTlrNo());
				tNegTransId.setTitaTxtNo(tNegAppr01.getNegAppr01Id().getTitaTxtNo());
				this.info("NegReportCom BatchTx01 tNegAppr01=[" + tNegAppr01.getNegAppr01Id().toString() + "]");
				this.info("NegReportCom BatchTx01 tNegTransId=[" + tNegTransId.toString() + "]");
				NegTrans tNegTrans = sNegTransService.findById(tNegTransId, titaVo);

				if (tNegTrans != null) {
					NegMainId tNegMainId = new NegMainId();
					tNegMainId.setCaseSeq(tNegTrans.getCaseSeq());
					tNegMainId.setCustNo(tNegTrans.getCustNo());
					NegMain tNegMain = new NegMain();
					tNegMain = sNegMainService.findById(tNegMainId, titaVo);
					if (tNegMain != null) {
						String IsMainFin = tNegMain.getIsMainFin();
						if (("Y").equals(IsMainFin)) {
							// 最大債權

							// 發件單位-Order1
							String FromFinCode = tNegMain.getMainFinCode();// 最大債權銀行代碼
							// 收件單位-Order2
							String ToFinCode = tNegAppr01.getFinCode();// 債權機構代號

							// 轉帳類別(同檔案一致)-Order3
							String TransAccCode = "02960";// 代發前置協商金融款項
//							// 入/扣帳日(同檔案一致)-Order4
//							int AssigeDate = tNegAppr01.getExportDate();//
//							if(AssigeDate!=0) {
//								//轉成民國年
//								AssigeDate=AssigeDate-19110000;
//							}
							// ExportDate 製檔日期
							// ApprDate 撥付日期
							// BringUpDate 提兌日
							// 或是由L5704 NegAppr 找出

							// 批號-同一發件單位、轉帳類別、入/扣帳日,批號不得重複
							String Key = FromFinCode + ConnectWord + ToFinCode + ConnectWord + TransAccCode + ConnectWord + String.valueOf(AssigeDate);
							this.info("NegReportCom Key FromFinCode=[" + FromFinCode + "] ToFinCode=[" + ToFinCode + "] TransAccCode=[" + TransAccCode + "] AssigeDate=[" + AssigeDate + "]");
							if (!lKey.contains(Key)) {
								lKey.add(Key);
								this.info("NegReportCom FromFinCode=[" + FromFinCode + "] ToFinCode=[" + ToFinCode + "] TransAccCode=[" + TransAccCode + "] AssigeDate=[" + AssigeDate + "]");
							}

							CustMain tCustMain = sCustMainService.custNoFirst(tNegMainId.getCustNo(), tNegMainId.getCustNo(), titaVo);

							String Detail16 = "";// 銷帳編號

							switch (TransAccCode) {
							case "01010":
								// 省水水號(11位)
								break;
							case "01020":
								// 台電電號(11位)+再送次數(1位,首次為空白,再送時從1開始編起)
								break;
							case "010610":
								// 保單編號(06位)
								break;
							case "01070":
								// 編號(10位)
								break;
							case "01110":
								// 牌照號碼(10位)
								break;
							case "01120":
								// 市水水號(10位)
								break;
							case "01960":
								// 還款所屬年月(4位)
								// Detail16=String.valueOf(tNegAppr01.getApprDate());

								break;
							case "01970":
								// 還款所屬年月(4位)
								break;
							case "02020":
								// 股票代號(6位)
								break;
							case "02070":
								// 編號(10位)
								break;
							case "02100":
								// 津貼受理號碼(11位)
								break;
							case "02120":
								// 市水水號(10位)
								break;
							case "02960":
								// 還款狀況-0:正常,1:溢繳,2:短繳,3:大額還本,4:結清
								Detail16 = tNegTrans.getTxKind();// tNegTrans-交易別
								if (("5").equals(Detail16)) {//tNegTrans-交易別=5:提前清償
									Detail16 = "4";
								}
								break;
							case "02970":
								// 還款狀況-0:正常,1:溢繳,2:短繳,3:大額還本,4:結清
								Detail16 = tNegTrans.getTxKind();// tNegTrans-交易別
								break;
							default:
								Detail16 = tNegTrans.getTxKind();// tNegTrans-交易別
								break;

							}
							String TempData[] = new String[14];
							TempData[0] = FromFinCode;// 發件單位
							TempData[1] = ToFinCode;// 收件單位
							TempData[2] = TransAccCode;// 轉帳類別
							TempData[3] = String.valueOf(AssigeDate);// 入/扣帳日
							TempData[4] = String.valueOf(tNegAppr01.getApprAmt());// 交易金額 //tNegAppr01-撥付金額
							TempData[5] = tNegAppr01.getDataSendUnit();// 委託單位 //tNegAppr01-資料傳送單位
							TempData[6] = "9999999";// 實際入/扣帳日
							TempData[7] = "9999";// 回應代碼
							TempData[8] = tNegAppr01.getRemitBank();// 轉帳行代碼 //tNegAppr01-匯款銀行
							TempData[9] = tNegAppr01.getRemitAcct();// 轉帳帳號 //tNegAppr01-匯款帳號
							TempData[10] = tCustMain.getCustId();// 委託轉帳之事業單位統編
							TempData[11] = Detail16;// 銷帳編號
							TempData[12] = tCustMain.getCustId();// 統編
							TempData[13] = NegAppr01KeyToString(tNegAppr01);
							lTempData.add(TempData);
						} else {
							// E0013 程式邏輯有誤
							throw new LogicException(titaVo, "E0013", "不應為一般債權 戶號[" + tNegMainId.getCustNo() + "]");
						}
					}
				} else {
					this.info("BatchTx01 tNegTrans IS NULL");
					// E0006 鎖定資料時，發生錯誤
					throw new LogicException(titaVo, "E0006", "債務協商交易檔");
				}
			}
			// Head4-指定入/扣帳日-特殊條件
			List<String> lTrialTransAccCode = new ArrayList<String>();
			lTrialTransAccCode.add("01120");
			lTrialTransAccCode.add("02120");

			// Detail10-委託單位-特殊條件
			// Detail15-帳戶ID-特殊條件
			List<String> lTrialTrans1AccCode = new ArrayList<String>();
			lTrialTrans1AccCode.add("02960");
			lTrialTrans1AccCode.add("02970");

			// Detail14-轉帳帳號-特殊條件
			List<String> lTrialTrans2AccCode = new ArrayList<String>();
			lTrialTrans2AccCode.add("01010");
			lTrialTrans2AccCode.add("01020");
			lTrialTrans2AccCode.add("01120");
			// 編寫Txt資料
			if (lKey != null && lKey.size() != 0) {
				int lKeyS = lKey.size();

				int DetailListSeries = 0;// 交易序號
				Collections.sort(lKey);// 升序排列

				// 批號-同一發件單位、轉帳類別、入/扣帳日,批號不得重複
				int OrderStart = 0;// 批號-從0開始編碼

				String strOrderStart = String.valueOf(OrderStart);// 批號
				strOrderStart = LRFormat(strOrderStart, 2, "R", "0");

				String Same2To5 = "";
				for (int i = 0; i < lKeyS; i++) {
					String[] ValueKey = lKey.get(i).split(ConnectWord);
					String FromFinCode = ValueKey[0];// 發件單位
					String ToFinCode = ValueKey[1];// 收件單位
					String TransAccCode = ValueKey[2];// 轉帳類別
					// String AssigeDate = ValueKey[3];// 入/扣帳日

					this.info("BatchTx01 FromFinCode=[" + FromFinCode + "],ToFinCode=[" + ToFinCode + "],TransAccCode=[" + TransAccCode + "],AssigeDate=[" + AssigeDate + "]");
					String CompanyId = "";
					if (("458").equals(FromFinCode)) {
						CompanyId = "03458902";// 公司統編
					} else {
						CompanyId = FromFinCode;
					}

					// Head
					StringBuffer sbHead = new StringBuffer();
					// Head-區別碼
					String Head1 = "1";
					// 發件單位
					String Head2 = LRFormat(CompanyId, 8, "L", " ");

					// 收件單位
					String Head3 = LRFormat(ToFinCode, 8, "L", " ");

					// 指定入/扣帳日
					String Head4 = "";
					if (lTrialTransAccCode.contains(TransAccCode)) {
						// 通知檔之檔案傳送日期
						String FileSendDate = "";
						Head4 = FileSendDate;
					} else {
						Head4 = String.valueOf(AssigeDate);
					}
					Head4 = LRFormat(Head4, 7, "R", "0");

					// 轉帳類別
					String Head5 = TransAccCode;
					Head5 = LRFormat(Head5, 5, "R", "0");

					//this.info("BatchTx01 Head1=[" + Head1 + "],Head2=[" + Head2 + "],Head3=[" + Head3 + "],Head4=[" + Head4 + "],Head5=[" + Head5 + "]");

					// 性質別
					String Head6 = "1";

					// 批號-同一發件單位、轉帳類別、入/扣帳日,批號不得重複
					String Head7 = strOrderStart;// 從0開始編

					// 保留欄
					String Head8 = LRFormat("", 168, "R", " ");
					Same2To5 = Head2 + Head3 + Head4 + Head5;
					//this.info("BatchTx01 Same2To5=[" + Same2To5 + "]");

					sbHead.append(Head1);
					sbHead.append(Same2To5);
					sbHead.append(Head6);
					sbHead.append(Head7);
					sbHead.append(Head8);

					this.info("sbBatchTx01 Head=[" + sbHead.toString() + "]");
					sbBatchTx01.append(sbHead + ChangeLine);

					// Detail

					BigDecimal sumTransAmt = new BigDecimal(0);
					int CountList = 0;
					for (String TempData[] : lTempData) {
						if (TempData[0].equals(FromFinCode) // 發件單位
								&& TempData[1].equals(ToFinCode)// 收件單位
								&& TempData[2].equals(TransAccCode)// 轉帳類別
								&& TempData[3].equals(String.valueOf(AssigeDate))// 入/扣帳日
						) {
							StringBuffer sbDetail = new StringBuffer();
							// Key is equale
							// 區別碼
							String Detail1 = "2";
							String Detail2_5 = Same2To5;

							// 交易序號-
							// 1.由發件單位自訂.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.
							// 2.編碼原則:第一碼固定為[0],第二碼及第三碼為批號,應與首錄之批號欄位相同,後七碼由發件單位自訂.
							DetailListSeries++;
							String Detail6 = "0" + strOrderStart + LRFormat(String.valueOf(DetailListSeries), 7, "R", "0");
							// 檢核欄位-固定為[+]
							String Detail7 = "+";
							// 交易金額
							BigDecimal TransAmt = new BigDecimal(TempData[4]);
							sumTransAmt = sumTransAmt.add(TransAmt);
							CountList++;
							String Detail8 = String.format("%2.2f", TransAmt);

							Detail8 = LRFormat(Detail8.replace(".", ""), 13, "R", "0");
							// 委託單位區別-固定為[0]
							String Detail9 = "0";
							// 委託單位-委託轉帳之事業單位統編
							// 1. 轉帳類別為 02960 02970時,該欄位置入最大債權銀行/發卡機構代號,左靠右補0,代號首位為[A]時請置入[10]
							String Detail10 = "";
							if (lTrialTrans1AccCode.contains(TransAccCode)) {
								// 最大債權銀行/發卡機構代號
								Detail10 = CompanyId;
							} else {
								Detail10 = TempData[12];
							}
							if (Detail10 != null && Detail10.length() != 0) {
								if (("A").equals(Detail10.substring(0, 1))) {
									Detail10 = "10" + Detail10.substring(1, Detail10.length());
								}
							}
							Detail10 = LRFormat(Detail10, 8, "L", "0");
							// 實際入/扣帳日
							String Detail11 = "9999999";
							// 回應代碼
							String Detail12 = "9999";
							// 轉帳行代碼
							String Detail13 = TempData[8];
							Detail13 = LRFormat(Detail13, 7, "L", " ");
							// 轉帳帳號
							String Detail14 = TempData[9];
							if (lTrialTrans2AccCode.contains(TransAccCode)) {
								Detail14 = "00" + Detail14;
							}
							Detail14 = LRFormat(Detail14, 16, "R", "0");
							// 帳戶ID
							String Detail15 = TempData[10];
							if (lTrialTrans1AccCode.contains(TransAccCode)) {
								//
							} else {
								// 還款人身分證號
							}
							Detail15 = LRFormat(Detail15, 10, "L", " ");
							// 銷帳編號
							String Detail16 = TempData[11];
							Detail16 = LRFormat(Detail16, 16, "L", " ");
							// 費用代號
							String Detail17 = "    ";
							// 銀行專用區
							String Detail18 = "                    ";
							// 事業單位專用資料區
							String Detail19 = "                                                      ";
							sbDetail.append(Detail1);
							sbDetail.append(Detail2_5);
							sbDetail.append(Detail6);
							sbDetail.append(Detail7);
							sbDetail.append(Detail8);
							sbDetail.append(Detail9);
							sbDetail.append(Detail10);
							sbDetail.append(Detail11);
							sbDetail.append(Detail12);
							sbDetail.append(Detail13);
							sbDetail.append(Detail14);
							sbDetail.append(Detail15);
							sbDetail.append(Detail16);
							sbDetail.append(Detail17);
							sbDetail.append(Detail18);
							sbDetail.append(Detail19);
							sbBatchTx01.append(sbDetail + ChangeLine);
							this.info("sbBatchTx01 Detail=[" + sbDetail.toString() + "]");
							// LRFormat(String str,int TotalLength,String type,String AddStr) {
							// TempData[13];//NegAppr01 Id
							NegAppr01Id tNegAppr01Id = StringToNegAppr01Id(TempData[13]);
							NegAppr01 tNegAppr01 = sNegAppr01Service.holdById(tNegAppr01Id, titaVo);
							if (tNegAppr01 != null) {
								// 把BatchTx01的交易序號寫入 以利BatchTx04使用
								tNegAppr01.setBatchTxtNo(Detail6);// 交易序號
								try {
									sNegAppr01Service.update(tNegAppr01, titaVo);
								} catch (DBException e) {
									// E0007 更新資料時，發生錯誤
									throw new LogicException(titaVo, "E0007", "最大債權撥付資料檔");
								}
							} else {
								// E0006 鎖定資料時，發生錯誤
								throw new LogicException(titaVo, "E0006", "最大債權撥付資料檔");
							}
						} else {
							// Key is not equale
							continue;
						}
//							TempData[0]=FromFinCode;
//							TempData[1]=ToFinCode;
//							TempData[2]=TransAccCode;
//							TempData[3]=String.valueOf(AssigeDate);
//							TempData[4]=String.valueOf(tNegAppr01.getApprAmt());//交易金額 //tNegAppr01-撥付金額
//							TempData[5]=tNegAppr01.getDataSendUnit();//委託單位 //tNegAppr01-資料傳送單位
//							TempData[6]="9999999";//實際入/扣帳日
//							TempData[7]="9999";//回應代碼
//							TempData[8]=tNegAppr01.getRemitBank();//轉帳行代碼 //tNegAppr01-匯款銀行
//							TempData[9]=tNegAppr01.getRemitAcct();//轉帳帳號 //tNegAppr01-匯款帳號
//							TempData[10]=String.valueOf(tNegMainId.getCustNo());//帳戶ID //NegMain-戶號
//							TempData[11]=tNegTrans.getTxKind();//銷帳編號 //tNegTrans-交易別
//							TempData[12]; //統編
					}
					// End
					StringBuffer sbEnd = new StringBuffer();
					String End1 = "3";// 區別碼
					String End2_5 = Same2To5;
					// 檢核欄位
					String End6 = "+";
					// 交易總金額
					String End7 = String.format("%2.2f", sumTransAmt);
					End7 = LRFormat(End7.replace(".", ""), 15, "R", "0");
					// 交易總筆數
					String End8 = LRFormat(String.valueOf(CountList), 10, "R", "0");

					// 檢核欄位
					String End9 = "+";
					// 成交總金額
					String End10 = "000000000000000";
					// 成交總筆數
					String End11 = "0000000000";
					// 檢核欄位
					String End12 = "+";
					// 未成交總金額
					String End13 = "000000000000000";
					// 未成交總筆數
					String End14 = "0000000000";
					// 保留欄
					String End15 = "                                                                                             ";
					sbEnd.append(End1);
					sbEnd.append(End2_5);
					sbEnd.append(End6);
					sbEnd.append(End7);
					sbEnd.append(End8);
					sbEnd.append(End9);
					sbEnd.append(End10);
					sbEnd.append(End11);
					sbEnd.append(End12);
					sbEnd.append(End13);
					sbEnd.append(End14);
					sbEnd.append(End15);
					sbBatchTx01.append(sbEnd + ChangeLine);
					this.info("sbBatchTx01 End=[" + sbEnd.toString() + "]");
				}

				// int TotaRow=DataRow+lKeyS*2;
				// int TotalCol=19;
			} else {
				// 查無有效資料
				throw new LogicException(titaVo, "E0001", "查無有效資料");
			}
		} else {
			// 查詢資料不存在
			throw new LogicException(titaVo, "E0001", "");
		}

//		//SORT 發件單位,收件單位,轉帳類別(同檔案一致),入/扣帳日(同檔案一致)
//		String Header[][]= {
//			//{"序號","項目名稱","位置","長度","說明"},
//			{"1","區別碼","1:1","N1","首筆為[1]"},
//			{"2","發件單位","2:9","AN8","委託單位或代理銀行總行代號,左靠,右補空白."},
//			{"3","收件單位","10:17","AN8","轉帳銀行總行代號,左靠,右補空白"},
//			{"4","指定入/扣帳日","18:24","N7","1.民國年月日YYYMMDD,同檔案一致.發件單位指定之入/扣帳日期 \r\n2.轉帳類別為01120或02120時,該欄位置入通知檔之檔案傳送日期"},
//			{"5","轉帳類別","25:29","AN5","轉帳類別代碼,同檔案一致"},
//			{"6","性質別","30:30","N1","通知為[1],同檔案一致"},
//			{"7","批號","31:32","N2","由發件單位編碼,右靠,左補零.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.同檔案一致."},
//			{"8","保留欄位","33:200","AN168",""}
//		};
//		String Detail[][]= {
//				//{"序號","項目名稱","位置","長度","說明"},
//				{"1","區別碼","1:1","N1","明細為[2]"},
//				{"2","發件單位","2:9","AN8","委託單位或代理銀行總行代號,左靠,右補空白."},
//				{"3","收件單位","10:17","AN8","轉帳銀行總行代號,左靠,右補空白"},
//				{"4","指定入/扣帳日","18:24","N7","1.民國年月日YYYMMDD,同檔案一致.發件單位指定之入/扣帳日期 \r\n2.轉帳類別為01120或02120時,該欄位置入通知檔之檔案傳送日期"},
//				{"5","轉帳類別","25:29","AN5","轉帳類別代碼,同檔案一致"},
//				{"6","交易序號","30:39","AN10","1.由發件單位自訂.同一[發件單位、轉帳類別、入/扣帳日、批號]不得重複.\r\n2.編碼原則:第一碼固定為[0],第二碼及第三碼為批號,應與首錄之批號欄位相同,後七碼由發件單位自訂."},
//				{"7","檢核欄位","40:40","N1","固定為[+]"},
//				{"8","交易金額","41:53","N13","入扣帳金額,右靠,左補零,9(11)V99 末兩位為小數點後兩位"},
//				{"9","委託單位區別","54:54","AN1","固定為[0]"},
//				{"10","委託單位","55:62","AN8","委託轉帳之事業單位統編. [轉帳類別]為 02960、02970 時,該欄位置入最大債權銀行/發卡機構代號,左靠,右補零,代號首位為[A]時,請置入[10]"},
//				{"11","實際入/扣帳日","63:69","N7","固定為[9999999]"},
//				{"12","回應代碼","70:73","N4","固定為[9999]"},
//				{"13","轉帳行代碼","74:80","AN7","轉帳帳號所屬銀行代碼,三碼總行代碼及四碼分行代碼,分行代碼可空白"},
//				{"14","轉帳帳號","81:85","AN16","入扣帳帳號,右靠,左補零.[轉帳類別]為01010或01020或01120時,該欄位置入[00]+14位帳號,詳附錄四"},
//				{"15","帳戶ID","97:106","AN10","1.帳戶客戶身分證號或統編,文數字,左靠,右補空白(一般為10位身分證字號或8位統編,若無則放空白) \r\n2.轉帳類別為02960或02970時,該欄位置入環款人身分證號."},
//				{"16","銷帳編號","107:122","AN16","銷帳資料,左靠,右補空白"},
//				{"17","費用代號","123:126","AN20","空白"},
//				{"18","銀行專用區","127:146","AN20","銀行內部自由運用,財金不予處理,無責空白"},
//				{"19","事業單位專用資料區","147:200","AN54","各營利事業單位依需求需要報請財金公司統一定義"}
//			};
//		String End[][]= {
//				//{"序號","項目名稱","位置","長度","說明"},
//				{"1","區別碼","1:1","N1","尾筆為[3]"},
//				{"2","發件單位","2:9","AN8","委託單位或代理銀行總行代號,左靠,右補空白"},
//				{"3","收件單位","10:17","AN8","轉帳銀行總行代號,左靠,右補空白"},
//				{"4","指定入/扣帳日","18:24","N7","1.民國年月日YYYMMDD,同檔案一致.發件單位指定之入/扣帳日期 \r\n2.轉帳類別為01120或02120時,該欄位置入通知檔之檔案傳送日期"},
//				{"5","轉帳類別","25:29","AN5","轉帳類別代碼,同檔案一致"},
//				{"6","檢核欄位","30:30","AN1","固定為[+]"},
//				{"7","交易總金額","31:45","N15","交易總金額,右靠,左補零,9(13)V99末兩位為小數點後兩位"},
//				{"8","交易總筆數","46:55","N10","交易總筆數,右靠,左補零"},
//				{"9","檢核欄位","56:56","AN1","固定為[+]"},
//				{"10","成交總金額","57:71","N15","通知放零,右靠左補零,9(13)V99"},
//				{"11","成交總筆數","72:81","N10","通知放零,右靠左補零"},
//				{"12","檢核欄位","82:82","AN1","固定為[+]"},
//				{"12","未成交總金額","83:97","N15","通知放零,右靠左補零,9(13)V99"},
//				{"14","未成交總筆數","98:107","N10","通知放零,右靠左補零"},
//				{"15","保留欄位","108:200","AN93",""}
//		};
//		
//		//轉帳類別-銷帳編號專用
//		String TransferType[][]= {
//				//{"轉帳類別","資料說明"},
//				{"01010","省水水號(11位)"},
//				{"01020","台電電號(11位)+在送次數(1位,首次空白,再送時從1開始編起)"},
//				{"01060","保單編號(16位)"},
//				{"01070","編號(10位)"},
//				{"01110","牌照號碼(10位)"},
//				{"01120","市水水號(10位)"},
//				{"01960","還款所屬年月(4位)"},
//				{"01970","還款所屬年月(4位)"},
//				{"02020","股票代號(6位)"},
//				{"02070","編號(10位)"},
//				{"02100","津貼受理號碼(11位)"},
//				{"02120","市水水號(10位)"},
//				{"02960","還款狀況(1位)(0:正常,1:溢繳,2:短繳,3:大額還本,4:結清)"},
//				{"02970","還款狀況(1位)(0:正常,1:溢繳,2:短繳,3:大額還本,4:結清)"}
//		};
//		
//		String InstitutionData[][]= {
//				//{"代號","序號","項目名稱","位置","長度","說明"},
//				//[01010]事業單位專用資料區說明(長度54位元)
//				{"01010","1","工作區","147:150","N4",""},
//				{"01010","2","保留欄","151:158","AN8","空白"},
//				{"01010","3","開票年月","159:163","N5","YYYMM原(YYMM)"},
//				{"01010","4","省水代號","164:171","AN8","省水自訂之銀行代碼"},
//				{"01010","5","保留欄","172:200","AN29",""},
//				//[01020]代繳台電電費專用資料區說明(長度54位元)
//				{"01020","1","計算日","147:148","AN2",""},
//				{"01020","2","收費戶區","149:152","AN4","收費帳戶(1)+收費區(3)"},
//				{"01020","3","收據收費日","153:158","N6","年(2)+月(2)+日(2)"},
//				{"01020","4","開票年月","159:162","N4","年(2)+月(2)"},
//				{"01020","5","收據號碼","163:169","AN7",""},
//				{"01020","6","台電使用資料","170:174","AN5","契約別(1)+用電別(1)+發行類別(1)+日序(2)"},
//				{"01020","7","更正前用戶電號","175:183","AN9","無則空白"},
//				{"01020","8","營利編號別","184:184","AN1",""},
//				{"01020","9","保留欄","185:200","AN16","空白"},
//				//[01960,01970] 代繳債務協商清償服務費用款項專用資料區說明
//				{"01960,01970","1","費用類別","147:147","AN1","0:提醒繳款,1:跟催繳款"},
//				{"01960,01970","2","計價筆數","148:157","N10","服務費用計價筆數,右靠,左補零"},
//				{"01960,01970","3","發卡機構代號","158:160","AN3","發卡機構總機構代號"},
//				{"01960,01970","4","保留欄位","161:200","AN40",""},
//				//[02100] 代發津貼專用資料區說明
//				{"02100","1","發放年月","147:151","N5","民國年月YYYMM"},
//				{"02100","2","保險别","152:152","AN1",""},
//				{"02100","3","舊空白欄位","153:156","AN4","舊規格代繳代發欄位"},
//				{"02100","4","舊空白欄位","157:162","AN6","舊規格代繳代發欄位"},
//				{"02100","5","保留欄位","163:200","AN38",""},
//				//[02120] 待退北市水專用資料區說明
//				{"02120","1","沖退原因","147:147","AN1","1:水費異常,6:更正案"},
//				{"02120","2","收費年月","148:152","N5","YYYMM"},
//				{"02120","3","扣帳日期","153:159","AN7","異常水費之扣帳日期YYYMMDD"},
//				{"02120","4","保留欄位","160:200","AN41",""}
//				//{"02120","","",":","",""},
//		};
//		List<String> Data=new ArrayList<String>();
		return sbBatchTx01;
	}

	public String NegAppr01KeyToString(NegAppr01 tNegAppr01) {
		String str = "";
		if (tNegAppr01 != null) {
			str = tNegAppr01.getNegAppr01Id().getAcDate() + CombineWord + tNegAppr01.getNegAppr01Id().getFinCode() + CombineWord + tNegAppr01.getNegAppr01Id().getTitaTlrNo() + CombineWord
					+ tNegAppr01.getNegAppr01Id().getTitaTxtNo();
		}
		return str;
	}

	public NegAppr01Id StringToNegAppr01Id(String str) throws LogicException {
		// str=tNegAppr01Id.toString();
		NegAppr01Id tNegAppr01Id = new NegAppr01Id();
		if (str != null && str.length() != 0) {
			String NegAppr01Id[] = str.split(CombineWord);
			if (NegAppr01Id != null && NegAppr01Id.length == 4) {
				int AcDate = Integer.parseInt(NegAppr01Id[0]);
				String FinCode = NegAppr01Id[1];
				String TitaTlrNo = NegAppr01Id[2];
				int TitaTxtNo = Integer.parseInt(NegAppr01Id[3]);
				tNegAppr01Id.setAcDate(AcDate);
				tNegAppr01Id.setFinCode(FinCode);
				tNegAppr01Id.setTitaTlrNo(TitaTlrNo);
				tNegAppr01Id.setTitaTxtNo(TitaTxtNo);
			} else {
				this.info("NegReportCom StringToNegAppr01Id Error NegAppr01Id IS NULL");
			}
		}

		return tNegAppr01Id;
	}

	/**
	 * LRFormat 字串對齊
	 * 
	 * @param str         原始的字串
	 * @param TotalLength 字串總長度
	 * @param type        L:左靠 R:右靠
	 * @param AddStr      補充的字串,長度為1
	 * @return str 格式化後的字串
	 */
	public String LRFormat(String str, int TotalLength, String type, String AddStr) {
		int strL = str.length();
		// int AddStrL=AddStr.length();
		if (strL < TotalLength) {
			if ("L".equals(type)) {
				// 左靠
				for (int i = strL; i < TotalLength; i++) {
					str = str + AddStr;
				}
			} else if ("R".equals(type)) {
				// 右靠
				for (int i = strL; i < TotalLength; i++) {
					str = AddStr + str;
				}
			}
		} else {
			// 超過長度直接切掉
			this.info("NegReportCom Original str=" + str + "");
			str = str.substring(0, TotalLength);
			this.info("NegReportCom Changed str=" + str + "");
		}

		return str;
	}

	@Override
	public void exec() throws LogicException {

	}
}
