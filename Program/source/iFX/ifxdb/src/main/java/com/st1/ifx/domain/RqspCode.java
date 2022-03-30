package com.st1.ifx.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RqspCode {

	public RqspCode() {
	}

	public RqspCode(String code, String text, int no) {
		this.code = code;
		this.text = text;
		this.no = no;
	}

	@Column(name = "NO")
	private int no;

	@Column(name = "CODE")
	private String code;

	@Column(name = "TEXT")
	private String text;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "RqspCode [no=" + no + ", code=" + code + ", text=" + text + "]";
	}

}
