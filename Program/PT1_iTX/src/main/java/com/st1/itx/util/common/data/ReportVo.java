package com.st1.itx.util.common.data;

import java.util.Objects;

import javax.persistence.Entity;

@Entity
public class ReportVo {

	public static class ReportVoBuilder {

		private int rptDate;
		private String brno;
		private String rptCode;
		private String rptItem;
		private String security;
		private String rptSize;
		private String pageOrientation;
		private boolean useDefault;

		ReportVoBuilder() {
		}

		public ReportVo build() {
			return new ReportVo(rptDate, brno, rptCode, rptItem, security, rptSize, pageOrientation, useDefault);
		}

		public ReportVoBuilder setBrno(String brno) {
			this.brno = brno;
			return this;
		}

		public ReportVoBuilder setPageOrientation(String pageOrientation) {
			this.pageOrientation = pageOrientation;
			return this;
		}

		public ReportVoBuilder setRptCode(String rptCode) {
			this.rptCode = rptCode;
			return this;
		}

		public ReportVoBuilder setRptDate(int rptDate) {
			this.rptDate = rptDate;
			return this;
		}

		public ReportVoBuilder setRptItem(String rptItem) {
			this.rptItem = rptItem;
			return this;
		}

		public ReportVoBuilder setRptSize(String rptSize) {
			this.rptSize = rptSize;
			return this;
		}

		public ReportVoBuilder setSecurity(String security) {
			this.security = security;
			return this;
		}

		public ReportVoBuilder setUseDefault(boolean useDefault) {
			this.useDefault = useDefault;
			return this;
		}
	}

	public static ReportVoBuilder builder() {
		return new ReportVoBuilder();
	}

	private int rptDate;

	private String brno;

	private String rptCode;

	private String rptItem;

	private String security;

	private String rptSize;

	private String pageOrientation;
	private boolean useDefault;

	ReportVo(int rptDate, String brno, String rptCode, String rptItem, String security, String rptSize,
			String pageOrientation, boolean useDefault) {
		this.rptDate = rptDate;
		this.brno = brno;
		this.rptCode = rptCode;
		this.rptItem = rptItem;
		this.security = security;
		this.rptSize = rptSize;
		this.pageOrientation = pageOrientation;
		this.useDefault = useDefault;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ReportVo)) {
			return false;
		}
		ReportVo other = (ReportVo) obj;
		return Objects.equals(brno, other.brno) && Objects.equals(pageOrientation, other.pageOrientation)
				&& Objects.equals(rptCode, other.rptCode) && rptDate == other.rptDate
				&& Objects.equals(rptItem, other.rptItem) && Objects.equals(rptSize, other.rptSize)
				&& Objects.equals(security, other.security) && useDefault == other.useDefault;
	}

	/**
	 * @return the brno
	 */
	public String getBrno() {
		return brno;
	}

	/**
	 * @return the pageOrientation
	 */
	public String getPageOrientation() {
		return pageOrientation;
	}

	/**
	 * @return the rptCode
	 */
	public String getRptCode() {
		return rptCode;
	}

	/**
	 * @return the rptDate
	 */
	public int getRptDate() {
		return rptDate;
	}

	/**
	 * @return the rptItem
	 */
	public String getRptItem() {
		return rptItem;
	}

	/**
	 * @return the rptSize
	 */
	public String getRptSize() {
		return rptSize;
	}

	/**
	 * @return the security
	 */
	public String getSecurity() {
		return security;
	}

	@Override
	public int hashCode() {
		return Objects.hash(brno, pageOrientation, rptCode, rptDate, rptItem, rptSize, security, useDefault);
	}

	/**
	 * @return the useDefault
	 */
	public boolean isUseDefault() {
		return useDefault;
	}

	/**
	 * @param brno 單位別
	 */
	public void setBrno(String brno) {
		this.brno = brno;
	}

	/**
	 * @param pageOrientation 紙張方向 P:Portrait Orientation (直印) , L:Landscape
	 *                        Orientation (橫印)
	 */
	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}

	/**
	 * @param rptCode 報表代號
	 */
	public void setRptCode(String rptCode) {
		this.rptCode = rptCode;
	}

	/**
	 * @param rptDate 報表會計日期
	 */
	public void setRptDate(int rptDate) {
		this.rptDate = rptDate;
	}

	/**
	 * @param rptItem 報表名稱
	 */
	public void setRptItem(String rptItem) {
		this.rptItem = rptItem;
	}

	/**
	 * @param rptSize 紙張大小
	 */
	public void setRptSize(String rptSize) {
		this.rptSize = rptSize;
	}

	/**
	 * @param security 報表機密等級(中文敍述)
	 */
	public void setSecurity(String security) {
		this.security = security;
	}

	/**
	 * @param useDefault 是否使用預設底稿
	 */
	public void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}

	@Override
	public String toString() {
		return "ReportVo [rptDate=" + rptDate + ", brno=" + brno + ", rptCode=" + rptCode + ", rptItem=" + rptItem
				+ ", security=" + security + ", rptSize=" + rptSize + ", pageOrientation=" + pageOrientation
				+ ", useDefault=" + useDefault + "]";
	}
}
