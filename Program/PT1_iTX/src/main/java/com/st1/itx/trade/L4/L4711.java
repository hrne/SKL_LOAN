package com.st1.itx.trade.L4;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeFile;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.TxToDoCom;
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

		if (sTxToDoDetail != null && !sTxToDoDetail.isEmpty()) {
			for (TxToDoDetail tTxToDoDetail : sTxToDoDetail) {

				String processNotes = tTxToDoDetail.getProcessNote();

				if (processNotes == null || processNotes.trim().isEmpty())
					continue;

				makeFile.put(processNotes);
				this.info("tTxToDoDetail : " + tTxToDoDetail.toString());

				String dtlValue = tTxToDoDetail.getDtlValue();

				String subject = "";
				String bodyText = "";
				String email = "";
				long pdfno;

				// 先做更新

				TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
				tTxToDoDetailId.setCustNo(tTxToDoDetail.getCustNo());
				tTxToDoDetailId.setFacmNo(tTxToDoDetail.getFacmNo());
				tTxToDoDetailId.setBormNo(tTxToDoDetail.getBormNo());
				tTxToDoDetailId.setDtlValue(tTxToDoDetail.getDtlValue());
				tTxToDoDetailId.setItemCode(tTxToDoDetail.getItemCode());

				txToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);

				boolean havePdf = false;

				switch (dtlValue) {
				case "<火險保費>":
					subject = "火險及地震險保費-繳款通知單";
					break;
				default:
					// 非預期的內容,跳過
					continue;
				}

				if (!processNotes.contains(",")) {
					continue;
				}

				String[] processNotesSplit = processNotes.split(",");

				if (processNotesSplit == null || processNotesSplit.length < 5 || processNotesSplit[4] == null
						|| processNotesSplit[4].trim().isEmpty()) {
					continue;
				}

				bodyText = processNotesSplit[4].replace("\"", "");
				email = processNotesSplit[2];
				pdfno = Long.parseLong(processNotesSplit[3]);

				// 該有的欄位都有，傳去 mailService
				mailService.setParams(email, subject, bodyText);

				if (havePdf) {
					String path = Paths.get(outFolder, pdfno + "-" + subject + ".pdf").toString();

					// 先設好參數,後面發送Email時才會讀取
					mailService.setParams("", path);
					
					// 製作暫存表
					makeReport.toPdf(pdfno, pdfno + "-" + subject);
				}

				// 發送Email
				mailService.exec();
			}

			// 輸出makeFile
			long sno = makeFile.close();
			this.info("sno : " + sno);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}