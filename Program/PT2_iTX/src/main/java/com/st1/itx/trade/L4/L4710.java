package com.st1.itx.trade.L4;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

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
import com.st1.itx.util.MySpring;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.SftpClient;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.ReportVo;

@Service("L4710")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4710 extends TradeBuffer {

	@Autowired
	private TxToDoDetailService txToDoDetailService;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private TxFileService txFileService;

	@Autowired
	private SystemParasService systemParasService;

	@Autowired
	private SftpClient sftpClient;

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	private String smsFtpFlag = "Y";

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

		sTxToDoDetail = txToDoDetailService.detailStatusRange("TEXT00", 0, 0, 0, Integer.MAX_VALUE);

		txBuffer.init(titaVo);

		this.info("txBuffer.getTxCom().getTbsdy() : " + txBuffer.getTxCom().getTbsdy());
		this.info("txBuffer.getTxCom().getNbsdy() : " + txBuffer.getTxCom().getNbsdy());

//			temp path = D:\\tmp\\LNM56OP.txt

		SystemParas systemParas = systemParasService.findById("LN", titaVo);

		if (systemParas != null) {
			smsFtpFlag = systemParas.getSmsFtpFlag();
		}
		this.info("L4710 smsFtpFlag = " + smsFtpFlag);

		if (smsFtpFlag.equals("T") || smsFtpFlag.equals("A")) {
			// 2023-07-06 Wei 用背景作業打API
			MySpring.newTask("L4710p", this.txBuffer, titaVo);
		}

		if (sTxToDoDetail == null || sTxToDoDetail.isEmpty()) {
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
		makeFile.open(titaVo, reportVo, fileName, 2); // 2:BIG 5

		for (TxToDoDetail tTxToDoDetail : sTxToDoDetail) {
//			1.產出
			makeFile.put(tTxToDoDetail.getProcessNote());
		}

		long sno = makeFile.close();

		this.info("sno : " + sno);
		boolean result = sendToFTP(sno, titaVo);

		if (!result) {
			this.info("sendToFTP 失敗 不回寫TxToDoDetail狀態");
			return;
		}

		for (TxToDoDetail tTxToDoDetail : sTxToDoDetail) {
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

		smsFtpFlag = systemParas.getSmsFtpFlag();

		if (!"Y".equals(smsFtpFlag)) {
			this.info("sendToFTP: SmsFtpFlag != Y, skip uploading");
			return true;
		}

		this.info("sendToFTP sending to FTP...");

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

		String smsUrl = systemParas.getSmsFtpUrl();
		String smsPort = "22"; // 預設22
		if (smsUrl.contains(":")) {
			String[] s = smsUrl.split(":");
			smsUrl = s[0];
			smsPort = s[1];
		}
		String[] auth = systemParas.getSmsFtpAuth().split(":");
		String fileName = txFile.getFileOutput();
		Path pathToFile = Paths.get(outFolder, fileName);

		// 呼叫SFTPCient
		return sftpClient.upload(smsUrl, smsPort, auth, pathToFile, "inbound", titaVo);
	}

}