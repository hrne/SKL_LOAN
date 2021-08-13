package com.st1.itx.dataVO;

public class TradeTime {
	private String date;
	private String Time;

	public TradeTime(String date, String time) {
		this.setDate(date);
		this.setTime(time);
	}

	/**
	 * SYSDATE
	 * 
	 * @return String sysDate
	 */
	public String getDate() {
		return date;
	}

	/**
	 * SYSDATE
	 * 
	 * @return Integer SYSDATE
	 */
	public int getDateInteger() {
		return Integer.parseInt(date.trim());
	}

	private void setDate(String date) {
		this.date = date;
	}

	/**
	 * SYSTIME
	 * 
	 * @return String SYSTIME
	 */
	public String getTime() {
		return Time;
	}

	/**
	 * SYSTIME
	 * 
	 * @return Integer SYSTIME
	 */
	public int getTimeInteger() {
		return Integer.parseInt(Time.trim());
	}

	private void setTime(String time) {
		Time = time;
	}
}
