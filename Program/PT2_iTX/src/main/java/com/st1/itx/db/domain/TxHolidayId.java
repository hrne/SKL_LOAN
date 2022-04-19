package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxHoliday 假日檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxHolidayId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2626677544458293458L;

// 地區別
	@Column(name = "`Country`", length = 2)
	private String country = " ";

	// 日期
	@Column(name = "`Holiday`")
	private int holiday = 0;

	public TxHolidayId() {
	}

	public TxHolidayId(String country, int holiday) {
		this.country = country;
		this.holiday = holiday;
	}

	/**
	 * 地區別<br>
	 * 
	 * @return String
	 */
	public String getCountry() {
		return this.country == null ? "" : this.country;
	}

	/**
	 * 地區別<br>
	 * 
	 *
	 * @param country 地區別
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 日期<br>
	 * 
	 * @return Integer
	 */
	public int getHoliday() {
		return StaticTool.bcToRoc(this.holiday);
	}

	/**
	 * 日期<br>
	 * 
	 *
	 * @param holiday 日期
	 * @throws LogicException when Date Is Warn
	 */
	public void setHoliday(int holiday) throws LogicException {
		this.holiday = StaticTool.rocToBc(holiday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(country, holiday);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		TxHolidayId txHolidayId = (TxHolidayId) obj;
		return country.equals(txHolidayId.country) && holiday == txHolidayId.holiday;
	}

	@Override
	public String toString() {
		return "TxHolidayId [country=" + country + ", holiday=" + holiday + "]";
	}
}
