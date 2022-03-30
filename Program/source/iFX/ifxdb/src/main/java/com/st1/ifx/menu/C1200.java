package com.st1.ifx.menu;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

// no more parse
@FieldList({ "level", "apkind", "name", "empno", "mode", "txgrp", "dapknd", "oapknd" })
public class C1200 {
	@Cobol("9,1")
	int level;

	@Cobol("9,26")
	String apkind;

	@Cobol("X,12")
	String name;

	@Cobol("X,6")
	String empno;

	@Cobol("9,1")
	int mode;

	@Cobol("X,2")
	String txgrp;

	@Cobol("X,26")
	String dapknd;

	@Cobol("X,26")
	String oapknd;

	int fxlvl; // move from Attach

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getApkind() {
		return apkind;
	}

	public void setApkind(String apkind) {
		this.apkind = apkind;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmpno() {
		return empno;
	}

	public void setEmpno(String empno) {
		this.empno = empno;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getTxgrp() {
		return txgrp;
	}

	public void setTxgrp(String txgrp) {
		this.txgrp = txgrp;
	}

	public String getDapknd() {
		return dapknd;
	}

	public void setDapknd(String dapknd) {
		this.dapknd = dapknd;
	}

	public String getOapknd() {
		return oapknd;
	}

	public void setOapkndp(String oapknd) {
		this.oapknd = oapknd;
	}

	public int getFxlvl() {
		return fxlvl;
	}

	public void setFxlvl(int fxlvl) {
		this.fxlvl = fxlvl;
	}

	@Override
	public String toString() {
		return "C1200 [level=" + level + ", apkind=" + apkind + ", name=" + name + ", empno=" + empno + ", mode=" + mode
				+ ", txgrp=" + txgrp + ", fxlvl=" + fxlvl + ", dapknd=" + dapknd + ", oapknd=" + oapknd + "]";
	}

}
