package com.st1.itx.trade.L4;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
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
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.SystemParasService;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.MailVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.mail.MailService;

@Service("L4711")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4711 extends TradeBuffer {

	@Value("${iTXOutFolder}")
	private String outFolder = "";

	@Autowired
	private TxToDoCom txToDoCom;

	@Autowired
	private TxToDoDetailService txToDoDetailService;

	@Autowired
	private MakeFile makeFile;

	@Autowired
	private MailService mailService;

	@Autowired
	private MakeReport makeReport;

	@Autowired
	private SystemParasService systemParasService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4711 ");
		this.totaVo.init(titaVo);
		txToDoCom.setTxBuffer(this.txBuffer);

//		每天14:30批次執行此交易，將txtododetail之批號=MAIL00、狀態:0.未處理 產出file
//		L4454 L4603 L4703

//		temp path = D:\\tmp\\LNM56OP.txt
//		檔名暫定
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L4711";
		String fileItem = "LNM56OP_eMail(電子郵件媒體檔)";
		String fileName = "LNM56OP_eMail.txt";
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeFile.open(titaVo, reportVo, fileName, 1); // 1:UTF-8

		Slice<TxToDoDetail> sTxToDoDetail = txToDoDetailService.detailStatusRange("MAIL00", 0, 0, 0, Integer.MAX_VALUE,
				titaVo);

		if (sTxToDoDetail == null || sTxToDoDetail.isEmpty()) {
			this.info("電子郵件媒體檔,本日無資料");
			return new ArrayList<>();
		}

		SystemParas systemParas = systemParasService.findById("LN", titaVo);

		String emailFlag = "N";

		if (systemParas != null) {
			emailFlag = systemParas.getEmailFlag();
		}

		this.info("L4711 emailFlag = " + emailFlag);

		for (TxToDoDetail tTxToDoDetail : sTxToDoDetail) {

			int custNo = tTxToDoDetail.getCustNo();
			int facmNo = tTxToDoDetail.getFacmNo();
			int bormNo = tTxToDoDetail.getBormNo();
			String dtlValue = tTxToDoDetail.getDtlValue();
			String itemCode = tTxToDoDetail.getItemCode();
			String executeTxcd = tTxToDoDetail.getExcuteTxcd();
			String titaTlrNo = tTxToDoDetail.getTitaTlrNo();

			this.info("tTxToDoDetail foreach ...");
			this.info("custNo = " + custNo);
			this.info("facmNo = " + facmNo);
			this.info("bormNo = " + bormNo);
			this.info("dtlValue = " + dtlValue);
			this.info("itemCode = " + itemCode);
			this.info("executeTxcd = " + executeTxcd);
			this.info("titaTlrNo = " + titaTlrNo);

			String processNotes = tTxToDoDetail.getProcessNote();

			if (processNotes == null || processNotes.trim().isEmpty()) {
				this.info("processNotes 為空,跳過,讀下一筆");
				continue;
			}

			this.info("processNotes = " + processNotes);

			if (emailFlag.equals("Y") || emailFlag.equals("T")) {
				if (!processNotes.contains("<s>")) {
					this.info("processNotes 無項目區分標籤<s>,格式不合,跳過,讀下一筆");
					continue;
				}

				MailVo mailVo = new MailVo();

				if (!mailVo.splitProcessNotes(processNotes)) {
					this.info("processNotes 應有3~4個項目,格式不合,跳過,讀下一筆");
					continue;
				}
				sendMail(emailFlag, mailVo);
			}

			makeFile.put(processNotes);

			// 更新
			TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
			tTxToDoDetailId.setCustNo(custNo);
			tTxToDoDetailId.setFacmNo(facmNo);
			tTxToDoDetailId.setBormNo(bormNo);
			tTxToDoDetailId.setDtlValue(dtlValue);
			tTxToDoDetailId.setItemCode(itemCode);

			txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
		}

		// 輸出makeFile
		long sno = makeFile.close();
		this.info("sno : " + sno);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void sendMail(String emailFlag, MailVo mailVo) throws LogicException {
		this.info("sendMail ... ");

		// 收件信箱
		String email = mailVo.getEmail();
		// 信件標題
		String subject = mailVo.getSubject();
		// 信件內文
		String bodyText = mailVo.getBodyText();
		// 信件附件PDF
		long pdfno = mailVo.getPdfNo();

		if (emailFlag.equals("T")) {
			// T:測試用(測試套固定發送到skcu31780001@skl.com.tw,信件標題增加"測試用")
			email = "skcu31780001@skl.com.tw";
			subject = "[測試用]" + subject;
		}

		emailCheck(email);

		// 該有的欄位都有，傳去 mailService
		mailService.setParams(email, subject, bodyText);

		if (pdfno > 0) {
			String path = Paths.get(outFolder, pdfno + "-" + subject + ".pdf").toString();

			// 先設好參數,後面發送Email時才會讀取
			mailService.setParams("", path);

			// 製作暫存表
			makeReport.toPdf(pdfno, pdfno + "-" + subject);
		}

		// 發送Email
		mailService.exec();
	}

	private void emailCheck(String email) {
		String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(email);

		if (matcher.matches()) {
			this.info(email + " 是有效的電子郵件地址");
		} else {
			this.info(email + " 不是有效的電子郵件地址");
		}
	}
}