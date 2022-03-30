package com.st1.ifx.web;

public class TranReq {
	String txcode;
	String tid;
	String key;
	String chain;
	String oveScrFile;
	String mode;
	String jsFileUrl;
	String jsBlock1 = "";

	TranReq(String txcode) {
		this.txcode = txcode;
	}

	public TranReq(String txcode2, String tid, String key2, String chain2, String ovrScrFile, String mode) {
		this(txcode2);
		this.tid = tid;
		this.key = key2;
		this.chain = chain2;
		this.mode = mode;

	}

	public String getTxcode() {
		return txcode;
	}

	public void setTxcode(String txcode) {
		this.txcode = txcode;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
	}

	public String getOveScrFile() {
		return oveScrFile;
	}

	public void setOveScrFile(String oveScrFile) {
		this.oveScrFile = oveScrFile;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getJsFileUrl() {
		return jsFileUrl;
	}

	public void setJsFileUrl(String jsFileUrl) {
		this.jsFileUrl = jsFileUrl;
	}

	public String getJsBlock1() {
		return jsBlock1;
	}

	public void setJsBlock1(String jsBlock1) {
		this.jsBlock1 = jsBlock1;
	}

	@Override
	public String toString() {
		return "TranReq [txcode=" + txcode + ", tid=" + tid + ", key=" + key + ", chain=" + chain + ", oveScrFile=" + oveScrFile + ", mode=" + mode + ", jsFileUrl=" + jsFileUrl + ", jsBlock1="
				+ jsBlock1 + "]";
	}

}
