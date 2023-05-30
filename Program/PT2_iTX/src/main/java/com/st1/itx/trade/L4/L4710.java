package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxFile;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxFileService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.FtpClient;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.SmsCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

@Service("L4710")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4710 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	TxToDoDetailService txToDoDetailService;

	@Autowired
	MakeFile makeFile;

	@Autowired
	MakeReport makeReport;

	@Autowired
	TxToDoCom txToDoCom;

	@Autowired
	TxFileService txFileService;

	@Autowired
	SystemParasService systemParasService;

	@Autowired
	FtpClient ftpClient;

	@Autowired
	SmsCom smsCom;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	MailService mailService;

	private TitaVo titaVo = new TitaVo();

	private static final Pattern patternPrefix = Pattern.compile("^<.+>");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4710 ");
		this.totaVo.init(titaVo);

//		每天14:30批次執行此交易，將txtododetail之批號=TEXT00、狀態:0.未處理 組簡訊產出file
//		L4453 L4454 L4603 L4703

		makeMsgMedia(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void makeMsgMedia(TitaVo titaVo) throws LogicException {

		Slice<TxToDoDetail> sTxToDoDetail = null;

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

		sTxToDoDetail = txToDoDetailService.detailStatusRange("TEXT00", 0, 0, 0, Integer.MAX_VALUE);

		lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

		txBuffer.init(titaVo);

		this.info("txBuffer.getTxCom().getTbsdy() : " + txBuffer.getTxCom().getTbsdy());
		this.info("txBuffer.getTxCom().getNbsdy() : " + txBuffer.getTxCom().getNbsdy());

//			temp path = D:\\tmp\\LNM56OP.txt

		if (lTxToDoDetail == null || lTxToDoDetail.isEmpty()) {
			this.info("簡訊媒體檔,本日無資料");
			return;
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4710";
		String fileItem = "LNM56OP(簡訊媒體檔)";
		String fileName = "LNM56OP.txt";
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeFile.open(titaVo, reportVo, fileName);

//		makeFile.open(titaVo, txBuffer.getTxCom().getTbsdy(), "0000", titaVo.getTxCode(), titaVo.getTxCode() + "-簡訊媒體檔",
//				"LNM56OP.txt", 2);

		for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
//			1.產出
			makeFile.put(tTxToDoDetail.getProcessNote());
			this.info("tTxToDoDetail : " + tTxToDoDetail.toString());
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);
//		boolean result = sendToFTP(sno, titaVo);

//		if (!result)
//			return;

		for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
			// 2023-05-29 Wei 改用API傳送 from SKL 琦欣
			String proccessNote = tTxToDoDetail.getProcessNote();
			String[] data = proccessNote.split(",");

			if (data.length >= 4) {
				String mobile = data[2];
				String msg = data[3];
				this.info("L4710 sendSms test mobile = " + mobile);
				this.info("L4710 sendSms test msg = " + msg);
				if (titaVo.getTlrNo().equals("001702")) {
					smsCom.sendSms(titaVo, mobile, msg);
				}
			}

			// 2.回寫狀態
			TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
			tTxToDoDetailId.setCustNo(tTxToDoDetail.getCustNo());
			tTxToDoDetailId.setFacmNo(tTxToDoDetail.getFacmNo());
			tTxToDoDetailId.setBormNo(tTxToDoDetail.getBormNo());
			tTxToDoDetailId.setDtlValue(tTxToDoDetail.getDtlValue());
			tTxToDoDetailId.setItemCode(tTxToDoDetail.getItemCode());

			txToDoCom.setTxBuffer(txBuffer);
			txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
		}

	}

	private boolean sendToFTP(long fileSno, TitaVo titaVo) throws LogicException {
		SystemParas systemParas = systemParasService.findById("LN", titaVo);

		if (systemParas == null) {
			this.error("sendToFTP: SystemParas doesn't exist !?");
			return false;
		}

		if (!"Y".equals(systemParas.getSmsFtpFlag())) {
			this.info("sendToFTP: SmsFtpFlag != Y, skip uploading");
			return true;
		}

		this.info("ScheduledL4701 sending to FTP...");

		TxFile txFile = txFileService.findById(fileSno, titaVo);

		try {
			makeFile.toFile(fileSno);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("sendToFTP: makeFile.toFile failed: ");
			this.error(errors.toString());
		}

		if (txFile == null) {
			this.error("Tried to sendToFTP() but fileNo is not found in TxFile!");
			this.error("fileNo: " + fileSno);
			return false;
		}

		String ftpPath = systemParas.getLoanMediaFtpUrl();
		String[] auth = systemParas.getSmsFtpAuth().split(":");
		String fileName = txFile.getFileOutput();
		Path pathToFile = Paths.get(outFolder, fileName);

		try {
			ftpClient.sendFile(ftpPath, auth[0], auth[1], pathToFile.toString(), "inbound");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("ScheduledL4710 error = " + errors.toString());
			return false;
		}

		return true;
	}

}