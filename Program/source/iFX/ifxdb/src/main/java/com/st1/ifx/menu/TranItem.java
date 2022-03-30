package com.st1.ifx.menu;

import java.io.Serializable;
import java.util.Map;

import com.st1.ifx.domain.Txcd;

public class TranItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1759969693599099846L;

	public TranItem() {
	}

	public TranItem(Txcd txcd) {
		this.txcd = txcd.getTxcd();
		this.txnm = txcd.getTxdnm();
		this.type = txcd.getType();
		this.sbtyp = txcd.getSbtyp().trim();
		this.txdfg = txcd.getTxdfg();
	}

	public TranItem(String txcode) {
		this.txcd = txcode;
	}

	int type;
	String sbtyp;

	boolean enabled = true;

	// 業務/交易代號
	String txcd;
	// 業務/交易名稱
	String txnm;

	// 自行交易權限(0:不可交易,1:主管授權,2:不限制(不需主管授權))
	int dbucd;

	// 交易更正權限(0:不可更正,1:主管授權,2:不限制(不需主管授權))
	int hodecd;

	// 聯行交易權限(0:不可聯行,1:主管授權,2:不限制(不需主管授權))
	int chopcd;

	// 代OBU權限 (0:不可代OBU,1:主管授權,2:不限制(不需主管授權))
	int obucd;

	// 放行交易 (0:非放行交易,1:放行交易)
	int passcd;

	// $SECNO 9(02) 結帳類別
	int secno;

	// $TLRFG 9(01) 櫃員執行權限
	int tlrfg;

	// DBU放行記號
	int drelcd;

	// DBU掛帳行
	String dabrno;

	// DBU放行行
	String drbrno;

	// OBU放行記號
	int orelcd;

	// OBU放行行
	String orbrno;

	// 顯示記號
	int txdfg;

	// 暫時與舊版Menu之Auth 相容, 目前主機似乎未設記Auth
	// String auth = "B";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getTxcd() {
		return txcd;
	}

	public void setTxcd(String txcd) {
		this.txcd = txcd;
	}

	public String getTxnm() {
		return txnm;
	}

	public void setTxnm(String txnm) {
		this.txnm = txnm;
	}

	public int getDbucd() {
		return dbucd;
	}

	public void setDbucd(int dbucd) {
		this.dbucd = dbucd;
	}

	public int getHodecd() {
		return hodecd;
	}

	public void setHodecd(int hodecd) {
		this.hodecd = hodecd;
	}

	public int getChopcd() {
		return chopcd;
	}

	public void setChopcd(int chopcd) {
		this.chopcd = chopcd;
	}

	public int getObucd() {
		return obucd;
	}

	public void setObucd(int obucd) {
		this.obucd = obucd;
	}

	public int getPasscd() {
		return passcd;
	}

	public void setPasscd(int passcd) {
		this.passcd = passcd;
	}

	public int getDrelcd() {
		return drelcd;
	}

	public void setDrelcd(int drelcd) {
		this.drelcd = drelcd;
	}

	public String getDabrno() {
		return dabrno;
	}

	public void setDabrno(String dabrno) {
		this.dabrno = dabrno;
	}

	public String getDrbrno() {
		return drbrno;
	}

	public void setDrbrno(String drbrno) {
		this.drbrno = drbrno;
	}

	public int getOrelcd() {
		return orelcd;
	}

	public void setOrelcd(int orelcd) {
		this.orelcd = orelcd;
	}

	public String getOrbrno() {
		return orbrno;
	}

	public void setOrbrno(String orbrno) {
		this.orbrno = orbrno;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSbtyp() {
		return sbtyp;
	}

	public void setSbtyp(String sbtyp) {
		this.sbtyp = sbtyp;
	}

	public int getSecno() {
		return secno;
	}

	public void setSecno(int secno) {
		this.secno = secno;
	}

	public int getTlrfg() {
		return tlrfg;
	}

	public void setTlrfg(int tlrfg) {
		this.tlrfg = tlrfg;
	}

	public int getTxdfg() {
		return txdfg;
	}

	public void setTxdfg(int txdfg) {
		this.txdfg = txdfg;
	}

	@Override
	public String toString() {
		return "TranItem [type=" + type + ", sbtyp=" + sbtyp + ", enabled=" + enabled + ", txcd=" + txcd + ", txnm="
				+ txnm + ", dbucd=" + dbucd + ", hodecd=" + hodecd + ", chopcd=" + chopcd + ", obucd=" + obucd
				+ ", passcd=" + passcd + ", secno=" + secno + ", tlrfg=" + tlrfg + ", drelcd=" + drelcd + ", dabrno="
				+ dabrno + ", drbrno=" + drbrno + ", orelcd=" + orelcd + ", orbrno=" + orbrno + ", txdfg=" + txdfg
				+ "]";
	}

	private void put(Map<String, String> m, String n, String v) {
		m.put(n.toUpperCase() + "$", v);
	}

	private void put(Map<String, String> m, String n, Integer v) {
		put(m, n.toUpperCase(), v.toString());
	}

	public void merge(Map<String, String> m) {
		put(m, "dbucd", dbucd);
		put(m, "hodecd", hodecd);
		put(m, "chopcd", chopcd);
		put(m, "obucd", obucd);
		put(m, "passcd", passcd);

		put(m, "secno", secno);
		put(m, "tlrfg", tlrfg);

		put(m, "drelcd", drelcd);
		put(m, "dabrno", dabrno);
		put(m, "drbrno", drbrno);
		put(m, "orelcd", orelcd);

		put(m, "orbrno", orbrno);
		put(m, "txdfg", txdfg);

	}

}
