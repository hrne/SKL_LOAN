package com.st1.ifx.menu;

import com.st1.util.cbl.Cobol;
import com.st1.util.cbl.FieldList;

@FieldList({ "tcLogin", "brnName", "restart", "bkno", "brno", "ebrnbk", "burcd", "fxlvl", "abrno", "brlvl", "fxbrno",
		"finbrno", "obubrno" })
public class Attach {

	@Cobol("9,1")
	int tcLogin;

	@Cobol("X,30")
	String brnName;

	@Cobol("9,1")
	int restart;

	@Cobol("9,3")
	String bkno;

	@Cobol("9,4")
	String brno;

	@Cobol("X,4")
	String ebrnbk;

	@Cobol("9,2")
	int burcd;

	@Cobol("9,1")
	int fxlvl;

	@Cobol("9,4")
	String abrno;

	@Cobol("9,2")
	int brlvl;

	@Cobol("9,4")
	String fxbrno;

	@Cobol("9,4")
	String finbrno;

	@Cobol("9,4")
	String obubrno;

	public int getTcLogin() {
		return tcLogin;
	}

	public void setTcLogin(int tcLogin) {
		this.tcLogin = tcLogin;
	}

	public String getBrnName() {
		return brnName;
	}

	public void setBrnName(String brnName) {
		this.brnName = brnName;
	}

	public int getRestart() {
		return restart;
	}

	public void setRestart(int restart) {
		this.restart = restart;
	}

	public String getBkno() {
		return bkno;
	}

	public void setBkno(String bkno) {
		this.bkno = bkno;
	}

	public String getBrno() {
		return brno;
	}

	public void setBrno(String brno) {
		this.brno = brno;
	}

	public String getEbrnbk() {
		return ebrnbk;
	}

	public void setEbrnbk(String ebrnbk) {
		this.ebrnbk = ebrnbk;
	}

	public int getBurcd() {
		return burcd;
	}

	public void setBurcd(int burcd) {
		this.burcd = burcd;
	}

	public int getFxlvl() {
		return fxlvl;
	}

	public void setFxlvl(int fxlvl) {
		this.fxlvl = fxlvl;
	}

	public String getAbrno() {
		return abrno;
	}

	public void setAbrno(String abrno) {
		this.abrno = abrno;
	}

	public int getBrlvl() {
		return brlvl;
	}

	public void setBrlvl(int brlvl) {
		this.brlvl = brlvl;
	}

	public String getFxbrno() {
		return fxbrno;
	}

	public void setFxbrno(String fxbrno) {
		this.fxbrno = fxbrno;
	}

	public String getFinbrno() {
		return finbrno;
	}

	public void setFinbrno(String finbrno) {
		this.finbrno = finbrno;
	}

	public String getObubrno() {
		return obubrno;
	}

	public void setObubrno(String obubrno) {
		this.obubrno = obubrno;
	}

	@Override
	public String toString() {
		return "Attach [tcLogin=" + tcLogin + ", brnName=" + brnName + ", restart=" + restart + ", bkno=" + bkno
				+ ", brno=" + brno + ", ebrnbk=" + ebrnbk + ", burcd=" + burcd + ", fxlvl=" + fxlvl + ", abrno=" + abrno
				+ ", brlvl=" + brlvl + ", fxbrno=" + fxbrno + ", finbrno=" + finbrno + ", obubrno=" + obubrno + "]";
	}

}
