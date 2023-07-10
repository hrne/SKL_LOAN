package com.st1.itx.util.common.data;

import java.util.LinkedHashMap;
import java.util.Objects;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// 項目區分標籤為<s> 格式:email<s>subject<s>bodyText<s>pdfno
// 格式中文:收件信箱<s>信件標題<s>信件內文<s>信件附件PDF
// 有附件之範例:xxx@gmail.com<s>測試信件<s>這是\"測試\"信件<s>12345
// 無附件之範例:xxx@gmail.com<s>測試信件<s>這是\"測試\"信件
@Component
@Scope("prototype")
public class MailVo extends LinkedHashMap<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7603118556852337383L;

	private String email;

	private String subject;

	private String bodyText;

	private long pdfNo;

	/**
	 * 產生格式化TxToDoDetail.ProcessNotes
	 * 
	 * @param email    收件人信箱
	 * @param subject  信件標題
	 * @param bodyText 信件內文
	 * @param pdfNo    附件PDFNO
	 * @return 格式化的 ProcessNotes
	 */
	public String generateProcessNotes(String email, String subject, String bodyText, long pdfNo) {
		this.email = email;
		this.subject = subject;
		this.bodyText = bodyText;
		this.pdfNo = pdfNo;
		return email + "<s>" + subject + "<s>" + bodyText + "<s>" + pdfNo;
	}

	public boolean splitProcessNotes(String processNotes) {
		String[] processNotesSplit = processNotes.split("<s>");

		if (!(processNotesSplit.length >= 3 && processNotesSplit.length <= 4)) {
			// processNotes 應有3~4個項目,格式不合,不拆解
			return false;
		}

		// 收件信箱
		email = processNotesSplit[0];
		// 信件標題
		subject = processNotesSplit[1];
		// 信件內文
		bodyText = processNotesSplit[2];
		// 信件附件PDF
		pdfNo = 0;

		if (processNotesSplit.length == 4) {
			pdfNo = Long.parseLong(processNotesSplit[3]);
		}
		return true;
	}

	public String getEmail() {
		return this.email;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getBodyText() {
		return this.bodyText;
	}

	public long getPdfNo() {
		return this.pdfNo;
	}

	@Override
	public String toString() {
		return "MailVo [email=" + email + ", subject=" + subject + ", bodyText=" + bodyText + ", pdfNo=" + pdfNo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(bodyText, email, pdfNo, subject);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof MailVo)) {
			return false;
		}
		MailVo other = (MailVo) obj;
		return Objects.equals(bodyText, other.bodyText) && Objects.equals(email, other.email) && pdfNo == other.pdfNo
				&& Objects.equals(subject, other.subject);
	}
}
