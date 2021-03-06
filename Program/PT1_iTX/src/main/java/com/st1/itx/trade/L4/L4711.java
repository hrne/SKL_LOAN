package com.st1.itx.trade.L4;

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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

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

	/* 轉型共用工具 */
	@Autowired
	private Parse parse;

	/* 日期工具 */
	@Autowired
	private DateUtil dateUtil;

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

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<TxToDoDetail> sTxToDoDetail = null;

//		每天14:30批次執行此交易，將txtododetail之批號=MAIL00、狀態:0.未處理 產出file
//		L4454 L4603 L4703

//		temp path = D:\\tmp\\LNM56OP.txt
//		檔名暫定
		makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), titaVo.getTxCode(),
				titaVo.getTxCode() + "-電子郵件媒體檔", "LNM56OP_eMail.txt", 2);

		List<TxToDoDetail> lTxToDoDetail = new ArrayList<TxToDoDetail>();

		sTxToDoDetail = txToDoDetailService.detailStatusRange("MAIL00", 0, 0, this.index, this.limit);

		lTxToDoDetail = sTxToDoDetail == null ? null : sTxToDoDetail.getContent();

		if (lTxToDoDetail != null && lTxToDoDetail.size() != 0) {
			for (TxToDoDetail tTxToDoDetail : lTxToDoDetail) {
				TxToDoCom tTxToDoCom = new TxToDoCom();

//				tTxToDoDetail.setDtlValue("<火險保費>" + tInsuRenew.getPrevInsuNo());

				String dtlValue = tTxToDoDetail.getDtlValue();

				if (dtlValue.startsWith("<火險保費>")) {
					String subject = "火險及地震險保費-繳款通知單 ";
					String bodyText = "親愛的客戶，繳款通知" + "\n" + "新光人壽關心您。";

					// 附件是PDF時
					String[] processNotes = tTxToDoDetail.getProcessNote().split(",");
					String email = processNotes[2];
					long pdfno = Long.parseLong(processNotes[3]);

					mailService.setParams(email, subject, bodyText);

					// 先設好參數,後面發送Email時才會讀取
					mailService.setParams("", outFolder + pdfno + "-火險及地震險保費-繳款通知單.pdf");

					// 製作暫存表
					makeReport.toPdf(pdfno, "" + pdfno + "-火險及地震險保費-繳款通知單");

					// 發送Email
					mailService.exec();
				} else {
					// 1.產出
					makeFile.put(tTxToDoDetail.getProcessNote());
				}

//				2.回寫狀態
				TxToDoDetailId tTxToDoDetailId = new TxToDoDetailId();
				tTxToDoDetailId.setCustNo(tTxToDoDetail.getCustNo());
				tTxToDoDetailId.setFacmNo(tTxToDoDetail.getFacmNo());
				tTxToDoDetailId.setBormNo(tTxToDoDetail.getBormNo());
				tTxToDoDetailId.setDtlValue(tTxToDoDetail.getDtlValue());
				tTxToDoDetailId.setItemCode(tTxToDoDetail.getItemCode());

				tTxToDoCom.updDetailStatus(2, tTxToDoDetailId, titaVo);
			}

			long sno = makeFile.close();

			this.info("sno : " + sno);

			makeFile.toFile(sno);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}