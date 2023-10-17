package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L9996ServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.MailVo;
import com.st1.itx.util.mail.MailService;
import com.st1.itx.util.parse.Parse;

@Service("L9996")
@Scope("prototype")
/**
 * 
 * 
 * @author
 * @version 1.0.0
 */
public class L9996 extends TradeBuffer {

	@Value("${iTXOutFolder}")
	private String outFolder = "";
	@Autowired
	L9996ServiceImpl l9996ServiceImpl;
	@Autowired
	private MailService mailService;
	@Autowired
	Parse parse;
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		this.totaVo.init(titaVo);
	
		if (titaVo.getParam("ItemCode") == null) {
			this.error("ItemCode null");
		}
		if (titaVo.getParam("CustNo") == null) {
			this.error("CustNo null");
		}
		if (titaVo.getParam("ExcuteTxcd") == null) {
			this.error("ExcuteTxcd null");
		}
		if (titaVo.getParam("Email") == null) {
			this.error("Email null");
		}

		String itemCode = titaVo.getParam("ItemCode");
		int custNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		String executeTxcd = titaVo.getParam("ExcuteTxcd");
		String email = titaVo.getParam("Email");
		this.info("itemCode = " + itemCode);
		this.info("custNo = " + custNo);
		this.info("executeTxcd = " + executeTxcd);
		this.info("email = " + email);

		List<Map<String, String>> result = null;
		try {
			result = l9996ServiceImpl.findData(itemCode, custNo, executeTxcd, titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("l9996ServiceImpl.findData error = " + errors.toString());
		}

		emailCheck(email);

		if (result.size() > 0) {
			String processNotes = "";

			for (Map<String, String> r : result) {

				processNotes = r.get("ProcessNote");

				MailVo mailVo = new MailVo();

				mailVo.splitProcessNotes(processNotes);

				sendMail(mailVo, email);

			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void sendMail(MailVo mailVo, String email) throws LogicException {

		// 信件標題
		String subject = mailVo.getSubject();
		// 信件內文
		String bodyText = mailVo.getBodyText();
		// 信件附件PDF
		long pdfno = mailVo.getPdfNo();

		this.info("email = " + email);
		this.info("subject = " + subject);
		this.info("bodyText = " + bodyText);

		// 該有的欄位都有，傳去 mailService
		mailService.setParams(email, subject, bodyText);

		if (pdfno > 0) {
			this.info("pdfno = " + pdfno);
			String path = Paths.get(outFolder, pdfno + "-" + subject + ".pdf").toString();
			// 先設好參數,後面發送Email時才會讀取
			mailService.setParams("", path);
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