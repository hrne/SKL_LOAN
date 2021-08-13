package com.st1.itx.trade.L3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.LockControl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeFile;
//import com.st1.itx.util.common.MakeDbf;
import com.st1.itx.util.common.SendRsp;

import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.db.domain.TxTranCode;
import com.st1.itx.db.service.TxTranCodeService;

import com.st1.itx.db.service.springjpa.cm.TableColumnServiceImpl;
import com.st1.itx.util.common.CheckAuth;
import com.st1.itx.util.common.data.CheckAuthVo;
import com.st1.itx.util.common.data.RemitFormVo;
import com.st1.itx.util.report.RemitForm;

import com.st1.itx.util.common.data.CheckAmlVo;

@Service("L3111")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L3111 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3111.class);

	/* DB服務注入 */
	@Autowired
	public TxTranCodeService txTranCodeService;

	@Autowired
	public TableColumnServiceImpl tableColumnServiceImpl;

	@Autowired
	public LockControl LockControl;

	@Autowired
	public CheckAuth checkAuth;

	@Autowired
	public L3111Report txReport;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public SendRsp sendRsp;

//	@Autowired
//	public MakeDbf makeDbf;

	@Autowired
	public MakeFile makeFile;

	@Autowired
	public DataLog dataLog;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	RemitForm remitForm;

	// pdf底稿路徑
	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3111 ");
		this.totaVo.init(titaVo);

		CheckAmlVo checkAmlVo;

		int iCustNo = Integer.valueOf(titaVo.getParam("CustNo").trim());

		String iTlrNo = titaVo.getTlrNo();
		int hcode = titaVo.getHCodeI();

		String itxcd = titaVo.getParam("itxcd").trim();

		if ("".equals(itxcd)) {
			itxcd = titaVo.getTxCode();
		}

		this.info("active L3111 iCustNo = " + iCustNo);
		this.info("active L3111 hcode = " + hcode);
		this.info("active L3111 RELCD = " + titaVo.getRelCode());
		this.info("active L3111 ACTFG = " + titaVo.getActFgI());

		// 主管放行
//		if (titaVo.isActfgRelease()) {
//			if (titaVo.isHcodeErase()) {
//		//更新時重新銷定
//				LockControl.ToLock(iCustNo, titaVo.getTxcd(), titaVo.getOrgTlr());
//			} else {
//		//解除銷定
//				LockControl.ToUnLock(iCustNo,titaVo.getTxcd(), titaVo.getOrgTlr(),true);
//			}			
//		}

//		if (iCustNo > 0 && hcode == 0) {
//			LockControl.ToUnLock(iCustNo,iTranNo,iTlrNo,true);
//		}
//		
//		if (iCustNo == 0 && hcode == 0) {
//			throw new LogicException(titaVo, "E0007", "客戶編號不可為0");
//		}

//		testExcel(titaVo, iCustNo);
//
//		testFile(titaVo, iCustNo);
//
//		testReport(titaVo);

		// 以下欄位,也可由VAR放
		// 幾段式交易
//		titaVo.put("RELCD", "2");
		// 限啟始改,後續由系統放
//		titaVo.put("ACTFG", "1");

//		testDataLog(titaVo);

//		testTableColumn(titaVo);

//		testDate(titaVo);

//		testReadExcel(titaVo);

//		testDbf(titaVo);

//		testCheckAuth(titaVo,itxcd);

//		testSendRsp(titaVo);

//		testSendMsg(titaVo);

//		testUpload(titaVo);

//		this.txBuffer.getTxCom().setConfirmBrNo(titaVo.getKinbr());
//		this.txBuffer.getTxCom().setConfirmGroupNo("1");
//		long pdfsno = testRemitForm(titaVo);
//		this.totaVo.putParam("PdfSno", pdfsno);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private long testRemitForm(TitaVo titaVo) throws LogicException {
		RemitFormVo remitformVo = new RemitFormVo();

		// 報表代號(交易代號)
		remitformVo.setReportCode(titaVo.getTxCode());

		// 報表說明(預設為"國內匯款申請書(兼取款憑條)")
		remitformVo.setReportItem("國內匯款申請書(兼取款憑條) to 黃*晋");

		// 申請日期(民國年)
		remitformVo.setApplyDay(titaVo.getEntDyI());

		// 取款金額記號:1.同匯款金額 2.同匯款金額及手續費
		remitformVo.setAmtFg(2);

		// 取款帳號
		remitformVo.setWithdrawAccount("1234567890");

		// 銀行記號:1.跨行 2.聯行 3.國庫 4.同業 5.證券 6.票券
		remitformVo.setBankFg(1);

		// 收款行-銀行
		remitformVo.setReceiveBank("郵局");

		// 收款行-分行
		remitformVo.setReceiveBranch("中山支局");

		// 財金費
		remitformVo.setFiscFeeAmt(10);

		// 手續費
		remitformVo.setNormalFeeAmt(20);

		// 收款人-帳號
		remitformVo.setReceiveAccount("00017730******");

		// 收款人-戶名
		remitformVo.setReceiveName("黃*晋");

		// 匯款代理人
		remitformVo.setAgentName("黃*薇");

		// 匯款代理人身份證號碼
		remitformVo.setAgentId("B2217*****");

		// 匯款人代理人電話
		remitformVo.setAgentTel("戶號1354867");

		// 匯款金額
		remitformVo.setRemitAmt(136);

		// 匯款人名稱
		remitformVo.setRemitName("新光人壽保險股份有限公司");

		// 匯款人統一編號
		remitformVo.setRemitId("03458902");

		// 匯款人電話
		remitformVo.setRemitTel("23895858#7086");

		// 附言
		remitformVo.setNote("結清溢款");

		long sno = 0l;
		remitForm.toPdf(sno);

		return sno;

	}

	private void testUpload(TitaVo titaVo) throws LogicException {
		String filena = titaVo.getParam("FILENA").trim();
		String[] filelist = filena.split(";");
		for (String filename : filelist) {
			String fname = inFolder + File.separatorChar + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + filename;
			this.info("active L3111 UpLoad : " + filename);
		}
	}

	private void testSendMsg(TitaVo titaVo) throws LogicException {
		this.info("active L3111 testSendMsg");

		MySpring.newTask("L3111b", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("報表已背景啟動，請注意系統完成通知訊息");
	}

	private void testSendRsp(TitaVo titaVo) throws LogicException {
		sendRsp.addvReason(this.txBuffer, titaVo, "0001", "L3111測試1");
		sendRsp.addvReason(this.txBuffer, titaVo, "0002", "L3111測試2");
	}

	private void testCheckAuth(TitaVo titaVo, String txcd) throws LogicException {

		boolean can = checkAuth.isCan(titaVo, titaVo.getTlrNo(), txcd, 0, 0);

		this.info("L3111.testCheckAuth can =" + can);

		CheckAuthVo checkAuthVo = checkAuth.getAuth(titaVo, titaVo.getTlrNo(), txcd, 0);

		this.info("L3111.testCheckAuth can =" + can);

	}

	private void testCallBin(TitaVo titaVo) throws LogicException {

		MySpring.newTask("L9132", this.txBuffer, titaVo);
	}

	private void testCE901(TitaVo titaVo) throws LogicException {

		String msg = String.format("%-50s", "測試主管刷卡");

		this.info("L3111.testCE901=" + msg);

		throw new LogicException(titaVo, "CE901", msg);
	}

//	private void testDbf(TitaVo titaVo) throws LogicException {
//		this.info("L3111.testDbf");
//
//		// 開始作業
//		makeDbf.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + " test dbf", titaVo.getTxCode());
//
//		// 設定欄位
//		makeDbf.addNumericField("NO", 5, 0);
//		makeDbf.addStringField("NAME", 20);
//		makeDbf.addNumericField("TXAMT", 11, 2);
//
//		// 置入資料1
//		makeDbf.addData(new Object[] { 1, "趙一", 12345.67 });
//		// 置入資料2
//		makeDbf.addData(new Object[] { 1, "錢二", 88888 });
//		// 置入資料3
//		makeDbf.addData(new Object[] { 1, "孫三", 99999.123 });
//		// 置入資料4
//		makeDbf.addData(new Object[] { 1, "李四", 99999.666 });
//
//		// 結束作業
//		long sno = makeDbf.close();
//
//		// 測試驗證
//		makeDbf.toDbf(sno);
//	}

	private void testReadCsv(TitaVo titaVo) throws LogicException {
		this.info("L3111.testReadCsv");
		// 讀取Csv檔名
		String filename = inFolder + "L9713.csv";

		makeExcel.openCsv(filename, ",");

		int i = 0;
		for (HashMap<String, Object> map : makeExcel.getListMap()) {
			this.info("L3111.testReadCsv record:" + (++i) + map.get("f1") + "/" + map.get("f2"));
		}
	}

	private void testReadExcel(TitaVo titaVo) throws LogicException {
		this.info("L3111.testReadExcel");

		// 讀取Excel檔名
		String filename = inFolder + "test.xlsx";

		// 開檔及指定Sheet
		makeExcel.openExcel(filename, 1);

		// 讀取值
		double v1 = (double) makeExcel.getValue(1, 1);
		double v2 = (double) makeExcel.getValue(1, 2);
		double v3 = (double) makeExcel.getValue(1, 3);

		this.info("L3111.testReadExcel=" + v1 + "/" + v2 + "/" + v3);
	}

	private void testDate(TitaVo titaVo) throws LogicException {
		this.info("L3111.testDate");
		int d = 0;
		d = dateUtil.getbussDate(titaVo.getEntDyI(), 1);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " + 1 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), 2);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " + 2 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), 3);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " + 3 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), 4);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " + 4 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), 5);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " + 5 = " + d);

		d = dateUtil.getbussDate(titaVo.getEntDyI(), -1);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " - 1 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), -2);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " - 2 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), -3);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " - 3 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), -4);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " - 4 = " + d);
		d = dateUtil.getbussDate(titaVo.getEntDyI(), -5);
		this.info("L3111.testDate=" + titaVo.getEntDyI() + " - 5 = " + d);
	}

	private void testTableColumn(TitaVo titaVo) throws LogicException {
		this.info("L3111.testTableColumn");
//		try {
////			List<TableColumnVo> lTableColumnVo = tableColumnServiceImpl.findAll2("TxTranCode");
//			for (TableColumnVo tTableColumnVo : lTableColumnVo) {
////				this.info("table/column/comments=" + tTableColumnVo.getTableName() + "/" + tTableColumnVo.getColumnName() + "/" + tTableColumnVo.getColumnComments());
//			}
//		} catch (Throwable e) {
//			throw new LogicException(titaVo, "EC004", e.getMessage());
//		}

	}

	private void testDataLog(TitaVo titaVo) throws LogicException {
		TxTranCode txTranCode = (TxTranCode) txTranCodeService.holdById("L3111");

		TxTranCode txTranCode2 = (TxTranCode) dataLog.clone(txTranCode);

		txTranCode.setMenuFg(2);

		try {
			txTranCode = txTranCodeService.update2(txTranCode);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC003", "1:" + e.getMessage());
		}

		dataLog.setEnv(titaVo, txTranCode2, txTranCode);
		dataLog.exec();

		////

		txTranCode = (TxTranCode) txTranCodeService.holdById("L3111");

		txTranCode2 = (TxTranCode) dataLog.clone(txTranCode);

		txTranCode.setMenuFg(1);

		try {
			txTranCode = txTranCodeService.update2(txTranCode);
		} catch (DBException e) {
			throw new LogicException(titaVo, "EC003", "2:" + e.getMessage());
		}

		dataLog.setEnv(titaVo, txTranCode2, txTranCode);
		dataLog.exec();
	}

	private void testFile(TitaVo titaVo, int fg) throws LogicException {

		this.info("L3111 testFile");

		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(), titaVo.getTxCode() + " test file", titaVo.getTxCode() + ".csv", 2);
		makeFile.put("NO:123,50000");
		makeFile.put("abc,1000");
		makeFile.put("NO1234567,123");
		makeFile.put("abcdef,888");
		makeFile.put(makeFile.cutString("台北市內湖區", 8) + ",5555");
		makeFile.put(makeFile.fillStringR("台北市12345678", 8) + ",6666");
		makeFile.put(makeFile.fillStringL("台北市", 8) + ",7777");
		makeFile.put(makeFile.fillStringR("台北市內湖區", 7) + ",8888");
		makeFile.put(makeFile.fillStringR("12345", 7, '0') + ",8888");

		long sno = makeFile.close();

		makeFile.toFile(sno);

	}

	private void testExcel(TitaVo titaVo, int fg) throws LogicException {
		this.info("L3111 testExcel");

		if (fg == 1) {

			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L3111", "test1", "test1");

			makeExcel.setValue(1, 1, "張三");
			makeExcel.setValue(1, 2, 123);
		} else if (fg == 2) {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L3111", "test2", "test2", "test1.xlsx", "L3111");
			makeExcel.setSheet("L3111", "測試一");
			makeExcel.newSheet("test2");
			makeExcel.setValue(2, 1, "王五");
			makeExcel.setValue(2, 2, 456);
			this.info("row 1 value = " + makeExcel.getValue(1, 1) + "/" + makeExcel.getValue(1, 2));
		} else if (fg == 3) {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L3111", "test2", "test2", "test1.xlsx", 1);
			makeExcel.setSheet("L3111", "測試一");
			makeExcel.newSheet("test3");
			makeExcel.setValue(2, 1, "王老五");
			makeExcel.setValue(2, 2, 123456);
			this.info("row 1 value = " + makeExcel.getValue(1, 1) + "/" + makeExcel.getValue(1, 2));

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void testReport(TitaVo titaVo) throws LogicException {

		txReport.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L3111", "L3111", "Normal");

		txReport.print(1, 2, "┌─┬─┐");
		txReport.print(1, 2, "│細│框│");
		txReport.print(1, 2, "├─┼─┤");
		txReport.print(1, 2, "│ab│AB│");
		txReport.print(1, 2, "└─┴─┘");
//		txReport.print(1, 2, "╔═╦═╗");
//		txReport.print(1, 2, "║粗║框║");
//		txReport.print(1, 2, "╠═╬═╣");
//		txReport.print(1, 2, "║ab║AB║");
//		txReport.print(1, 2, "╚═╩═╝");
//		txReport.print(1, 2, "╔═╤═╗");
//		txReport.print(1, 2, "║中│框║");
//		txReport.print(1, 2, "╟─┼─╢");
//		txReport.print(1, 2, "║ab│AB║");
//		txReport.print(1, 2, "╚═╧═╝");
//		for (int i = 1;i<100;i++) {
////			this.info("active L3111  列印測試，行"+i);
//			txReport.prinst(1, 2, String.format("%02d", i));
//			txReport.print(0, 11, "列印測試，行"+i);
//		}

//		LD003ServiceImpl.getEntityManager(titaVo);
////		List<LD003Vo> LD003List = null;
//		int    cnt = 0;
//		int    amt = 0;
//		int    tcnt = 0;
//		int    tamt = 0;
//		boolean printfg = false;
//		try {
////			LD003List = LD003ServiceImpl.findAll();
//			List<HashMap<String, String>> LD003List = LD003ServiceImpl.findAll();
////			for (LD003Vo tLD003Vo : LD003List) {
//			
//			String o1 = "";
//			for (HashMap<String, String> tLD003Vo : LD003List) {
////				this.info("LD003Vo = " + tLD003Vo.getF1()+"/"+tLD003Vo.getF2()+"/"+tLD003Vo.getF3()+"/"+tLD003Vo.getF4()+"/"+tLD003Vo.getSUMF5()+"/"+tLD003Vo.getSUMF6());
////				txReport.print(1, 1, "├────────────────────┼──────┼─────────┤");
////				txReport.print(1, 1, "│                                        │            │                  │");
////				txReport.print(0, 5, tLD003Vo.getF2());
////				txReport.print(0, 23, tLD003Vo.getF4());
////				txReport.print(0, 53, String.format("%,d", tLD003Vo.getSUMF5()),"R");
////				txReport.print(0, 72, String.format("%,d", tLD003Vo.getSUMF6()),"R");
//				this.info("LD003Vo = " + tLD003Vo.get("0")+"/"+tLD003Vo.get("1")+"/"+tLD003Vo.get("2")+"/"+tLD003Vo.get("3")+"/"+tLD003Vo.get("4")+"/"+tLD003Vo.get("5"));
//				
//				if (printfg && !o1.equals(tLD003Vo.get("f1"))) {
////					txReport.print(1, 1, "├────────────────────┼──────┼─────────┤");
////					txReport.print(1, 1, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　　　│");
////					txReport.print(0, 23, "小　　計");
////					txReport.print(0, 51, String.format("%,d", cnt),"R");
////					txReport.print(0, 71, String.format("%,d", amt),"R");
//					printSum("　　　　　　　　　　小　　計",cnt,amt);
//					o1 = tLD003Vo.get("f1");
//					cnt = 0;
//					amt = 0;
//				}	else {
//					o1 = tLD003Vo.get("f1");
//				}
//				txReport.print(1, 1, "├────────────────────┼──────┼─────────┤");
//				txReport.print(1, 1, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//				txReport.print(0, 5, tLD003Vo.get("f2"));
//				txReport.print(0, 23, tLD003Vo.get("f4"));
//				txReport.print(0, 51, String.format("%,d", Integer.valueOf(tLD003Vo.get("f5").toString())),"R");
//				txReport.print(0, 71, String.format("%,d", Integer.valueOf(tLD003Vo.get("f6").toString())),"R");
//				
//				printfg = true;
//				cnt += Integer.valueOf(tLD003Vo.get("f6").toString());
//				amt += Integer.valueOf(tLD003Vo.get("f6").toString());
//				tcnt += Integer.valueOf(tLD003Vo.get("f5").toString());
//				tamt += Integer.valueOf(tLD003Vo.get("f6").toString());
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			StringWriter errors = new StringWriter();
//			e.printStackTrace(new PrintWriter(errors));
//			this.info("LD003ServiceImpl.findAll error = " + errors.toString());
//		}	
//		txReport.print(1, 1, "├────────────────────┼──────┼─────────┤");
//		txReport.print(1, 1, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　　　│");
//		txReport.print(0, 13, "合　　　　　　計");
//		txReport.print(0, 51, String.format("%,d", tcnt),"R");
//		txReport.print(0, 71, String.format("%,d", tamt),"R");
//		if (printfg) {
//			printSum("　　　　　　　　　　小　　計",cnt,amt);
//			printSum("　　　　合　　　　　　計",tcnt,tamt);
//		}	else {
//			txReport.print(1,1,"無資料");
//		}
//		txReport.print(1, 1, "└────────────────────┴──────┴─────────┘");

		long sno = txReport.close();

		// 測試用
		txReport.toPdf(sno);
	}

	private void printSum(String title, int cnt, int amt) {
		txReport.print(1, 1, "├────────────────────┼──────┼─────────┤");
		txReport.print(1, 1, "│　　　　　　　　　　　　　　　　　　　　│　　　　　　│　　　　　　　　　│");
		txReport.print(0, 5, title);
		txReport.print(0, 51, String.format("%,d", cnt), "R");
		txReport.print(0, 71, String.format("%,d", amt), "R");

	}
}