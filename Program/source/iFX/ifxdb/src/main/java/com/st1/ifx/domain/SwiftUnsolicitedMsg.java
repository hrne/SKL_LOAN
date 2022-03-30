package com.st1.ifx.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ifx.fmt.FormH5200;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManUtil;

@Entity
@Table(name = "FX_SWIFT_UNSO_MSG")
public class SwiftUnsolicitedMsg implements Serializable {
	private static final long serialVersionUID = 7290706764553849216L;

	@Column(name = "ID")
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FX_SWIFT_UNSO_MSG_SEQ")
	@SequenceGenerator(name = "FX_SWIFT_UNSO_MSG_SEQ", sequenceName = "FX_SWIFT_UNSO_MSG_SEQ", allocationSize = 1)
	@Id
	private Long id;

	// 接收電文日
	@Column(name = "RCV_DATE")
	private Date rcvDate;

	@Column(name = "RCV_TIME")
	private Time rcvTime;

	@Column(name = "BRNODEPT")
	private String brndept;

	// 供前端搜尋日
	@Column(name = "SRHDAY")
	private String srhday;

	// 營業日
	@Column(name = "FTBSDY")
	private String ftbsdy;

	@Column(name = "MSGTYP")
	private String msgType;

	// S、R
	@Column(name = "MSGSTATUS")
	private String msgStatus;

	// osnEntseq
	@Column(name = "OSN")
	private String osn;

	@Column(name = "ENTSEQ")
	private String entSeq;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_PATH")
	private String filePath;

	@Column(name = "FILE_SIZE")
	private Long fileSize;

	@Column(name = "LAST_PRINT_DATE")
	private Date lastPrintDate;

	@Column(name = "LAST_PRINT_TIME")
	private Time lastPrintTime;

	@Column(name = "PRINT_TIMES")
	private int printTimes;

	public static SwiftUnsolicitedMsg from(FormH5200 h52, String rootFolder) {
		SwiftUnsolicitedMsg m = new SwiftUnsolicitedMsg();
		m.brndept = h52.getBrno();
		m.ftbsdy = h52.getFtbsdy();
		m.msgType = h52.getMsgtyp().trim();
		m.osn = h52.getOsn();
		m.buildFilePath(rootFolder);
		m.touch();
		m.printTimes = 0;
		return m;
	}

	/**
	 * TotaToPrn.java存成實體檔案時使用.
	 * 
	 * @param brndept  分行.if=1058 add DEPT)
	 * @param srhday   搜索日
	 * @param ftbsdy   營業日
	 * @param msgtyp   MT電文
	 * @param osn      序號
	 * @param entSeq   序號
	 * @param fileName 電文稿檔案名稱
	 * @param filePath 電文稿檔案路徑
	 * @param size     電文稿檔案大小
	 * @return SwiftUnsolicitedMsg
	 */
	public static SwiftUnsolicitedMsg fromfile(String brndept, String srhday, String ftbsdy, String msgtyp, String osn,
			String entSeq, String fileName, String filePath, long size, String msgStatus) {
		SwiftUnsolicitedMsg m = new SwiftUnsolicitedMsg();
		m.setBrndept(brndept);
		m.setSrhday(srhday);
		m.setFtbsdy(ftbsdy);
		m.setMsgType(msgtyp);
		m.setOsn(osn);
		m.setEntSeq(entSeq);
		m.setFileName(fileName);
		m.setFilePath(filePath);
		m.setFileSize(size);
		m.setMsgStatus(msgStatus);
		m.touch();
		m.setPrintTimes(0);
		return m;
	}

	private void buildFilePath(String rootFolder) {
		//
		String fileName = String.format("%s-%s-%s-%s.txt", brndept, ftbsdy, msgType, osn);
		this.filePath = PoorManFile.combine(new String[] { rootFolder, PoorManUtil.getToday(), fileName });

	}

	// 更改srhday值
	public void movetouch() {
		java.util.Date today = new java.util.Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		String stoday = sdf2.format(today);
		this.setSrhday(stoday);
		// this.audits = new ArrayList<JournalAudit>();
	}

	// 第一次
	public void touch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.setRcvDate(new java.sql.Date(t));
		this.setRcvTime(new Time(t));
		// this.audits = new ArrayList<JournalAudit>();
	}

	// 列印更新
	public void printTouch() {
		java.util.Date today = new java.util.Date();
		long t = today.getTime();
		this.setLastPrintDate(new java.sql.Date(t));
		this.setLastPrintTime(new Time(t));
		;
		this.setPrintTimes(this.getPrintTimes() + 1);
		// this.audits = new ArrayList<JournalAudit>();
	}

	public static String saveMsg(String folder, String brn, String content) {
		return null;
	}

	public Date getRcvDate() {
		return rcvDate;
	}

	public void setRcvDate(Date rcvDate) {
		this.rcvDate = rcvDate;
	}

	public Time getRcvTime() {
		return rcvTime;
	}

	public void setRcvTime(Time rcvTime) {
		this.rcvTime = rcvTime;
	}

	public String getBrndept() {
		return brndept;
	}

	public void setBrndept(String brndept) {
		this.brndept = brndept;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getLastPrintDate() {
		return lastPrintDate;
	}

	public void setLastPrintDate(Date lastPrintDate) {
		this.lastPrintDate = lastPrintDate;
	}

	public Time getLastPrintTime() {
		return lastPrintTime;
	}

	public void setLastPrintTime(Time lastPrintTime) {
		this.lastPrintTime = lastPrintTime;
	}

	public int getPrintTimes() {
		return printTimes;
	}

	public void setPrintTimes(int printTimes) {
		this.printTimes = printTimes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public String getSrhday() {
		return srhday;
	}

	public void setSrhday(String srhday) {
		this.srhday = srhday;
	}

	public String getFtbsdy() {
		return ftbsdy;
	}

	public void setFtbsdy(String ftbsdy) {
		this.ftbsdy = ftbsdy;
	}

	public String getOsn() {
		return osn;
	}

	public void setOsn(String osn) {
		this.osn = osn;
	}

	public String getEntSeq() {
		return entSeq;
	}

	public void setEntSeq(String entSeq) {
		this.entSeq = entSeq;
	}

	public String getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(String msgStatus) {
		this.msgStatus = msgStatus;
	}

	@Override
	public String toString() {
		return "SwiftUnsolicitedMsg [id=" + id + ", rcvDate=" + rcvDate + ", rcvTime=" + rcvTime + ", brndept="
				+ brndept + ", srhday=" + srhday + ", ftbsdy=" + ftbsdy + ", msgType=" + msgType + ", osn=" + osn
				+ ", entSeq=" + entSeq + ", msgStatus=" + msgStatus + ", filePath=" + filePath + ", fileName="
				+ fileName + ", fileSize=" + fileSize + ", lastPrintDate=" + lastPrintDate + ", lastPrintTime="
				+ lastPrintTime + ", printTimes=" + printTimes + "]";
	}

}
