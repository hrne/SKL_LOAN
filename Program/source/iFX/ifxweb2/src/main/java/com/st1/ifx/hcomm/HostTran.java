package com.st1.ifx.hcomm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifx.fmt.FormCS001;
import com.ifx.fmt.FormCS002;
import com.ifx.fmt.FormCS004;
import com.st1.ifx.dataVo.TitaVo;
import com.st1.ifx.dataVo.TotaList;
import com.st1.ifx.dataVo.TotaVo;
import com.st1.ifx.domain.Journal;
import com.st1.ifx.domain.MsgBox;
import com.st1.ifx.domain.Rqsp;
import com.st1.ifx.domain.Ticker;
import com.st1.ifx.domain.TranDoc;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.hcomm.app.SessionMap;
import com.st1.ifx.hcomm.fmt.FormatUtil;
import com.st1.ifx.hcomm.sock.SockSender;
import com.st1.ifx.service.JournalService;
import com.st1.ifx.service.MsgService;
import com.st1.ifx.service.TickerService;
import com.st1.ifx.web.TTError;
import com.st1.msw.MswCenter;
import com.st1.msw.UserInfo;
import com.st1.servlet.GlobalValues;
import com.st1.util.DateUtil;
import com.st1.util.MySpring;
import com.st1.util.PoorManUtil;
import com.st1.util.cbl.CobolProcessor;
import com.st1.util.getContent.ContentName;

import io.netty.channel.ConnectTimeoutException;

@Component
@Scope("prototype")
public class HostTran {
	static final Logger logger = LoggerFactory.getLogger(HostTran.class);
	@Autowired
	private JournalService jnlService;

	@Autowired
	private SockSender sockSender;

	private TitaVo titaVo = null;

	private List<TotaVo> totaVoLi = null;

	private Journal jnl = null;

	private String updatedType;
	private String lastSeq;
	private String key;
	private String message = "";

	private int msgMode = 0;
	private int hostOvrMode = 0;
	private int origSeq;
	private int resvleng = 0;

	private long jnlId = -1;

	// 給xx006使用
	private boolean bRim = false;
	private boolean bTimeoutupjnl = false;
	private boolean status = false;

	private HttpSession session = null;

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public boolean send(String tita, String text, String resv, String mq, TitaVo titaVoT) {
		this.titaVo = titaVoT;

		if (!checkSession())
			return false;

		String logFile = getLogFileName();
		String titaTxcd = this.titaVo.getTxcd();
		String txCode = this.titaVo.getTxCode();

		this.lastSeq = (String) this.session.getAttribute(GlobalValues.SESSION_LAST_HOSTSEQ);
		this.lastSeq = this.lastSeq.trim();

		// 檢查是否有逾時交易
		String sessionasjson = "";
		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		boolean checkErtxtno = ((sessionMap.get("ERTXTNO") != null && !sessionMap.get("ERTXTNO").isEmpty()) ? true : false);

		logger.info("checkErtxtno4 :" + checkErtxtno);
		logger.info("gettitaMsgId TITA :" + FilterUtils.escape(titaTxcd));
		logger.info("checkErtxtno :" + FilterUtils.escape(checkErtxtno));

		try {
			int seq = 0;
			logger.info("msgMode = " + msgMode + ", use original seq:" + this.origSeq);
			if (msgMode == 9) {
				logger.info(FilterUtils.escape("msgMode  = 9,txcode:" + titaVo.getTxCode()));
				if (txCode.equals("XX006")) {
					logger.info("Do Not change seq!");
					this.putoriSeqToTita(tita);
				} else {
					logger.info("Do Not change seq, set seq 0!");
					tita = putSeqToTita(tita, 0);
					this.titaVo.putSequenceToTita(0); // also change this.key
				}
			} else {
				if (msgMode == 1) {
					seq = this.origSeq;
				} else {
					// 柯 交易傳輸序號+1 並存入變數中 TXTNO
					seq = getNextSeqAndSave(lastSeq);
					this.origSeq = seq; // save to origSeq for browser side
				}
				logger.info("lastSeq:" + FilterUtils.escape(lastSeq) + "sequence:" + FilterUtils.escape(seq));
				tita = putSeqToTita(tita, seq); // also change this.key
				this.titaVo.putSequenceToTita(seq);
			}
//			seq = getNextSeqAndSave(lastSeq);
//			this.origSeq = seq;
			logger.info("lastSeq:" + FilterUtils.escape(lastSeq) + "sequence:" + FilterUtils.escape(seq));
			tita = putSeqToTita(tita, seq); // also change this.key
			this.titaVo.putSequenceToTita(seq);

			logger.info("tita update with sequence:" + FilterUtils.escape(tita));
			logger.info("titar6 update with sequence:" + FilterUtils.escape(this.titaVo.voToString()));
			writeLog(logFile, this.titaVo.voToString(), "TITA");

			try {
				resvleng = resv.getBytes("UTF-8").length; // resv.length()
				logger.info("resv len:" + resv.length() + ", btye len:" + resv.getBytes("UTF-8").length);
				this.prelogJournal(resv);
			} catch (Exception ex) {
				StringWriter errors = new StringWriter();
				ex.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());

				writeLog(logFile, ex.getMessage(), "TITA-RESV");
				message = TTError.TT999 + "儲存失敗-" + ex.getMessage() + "(TITA-RESV)";
				logger.error(message);
				this.updateJournal(false, "TT997", message, this.titaVo.getMrKey());
				return false;
			}

			String sendKey = this.key;
			logger.info("* * * send key:" + FilterUtils.escape(sendKey));
			try {
				totaVoLi = sockSender.send(this.titaVo, titaTxcd);
			} catch (InterruptedException | ConnectTimeoutException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				sockSender.closeChannel();

				if (titaVo.isUpdate()) {
					logger.error("Time Out...");
					logger.info("Time Out JnlId : [" + FilterUtils.escape(this.jnlId) + "]");
					logger.error("ERTXTNO : [" + String.format("%08d", this.origSeq) + "]");
					sessionMap.put("ERJNLID", this.jnlId + "");
					sessionasjson = getSessionAsJSON(sessionMap);
					session.setAttribute(GlobalValues.SESSION_SYSVAR_MAP, sessionMap);
					session.setAttribute(GlobalValues.SESSION_SYSVAR, sessionasjson);
				}
				message = TTError.TT997 + "傳送失敗-" + e.getMessage() + "(socket)";
				writeLog(logFile, message, "TOTA");
				TotaVo totaVo = new TotaVo(this.titaVo);
				totaVo.setErrorMsg(message);
				totaVoLi = new ArrayList<TotaVo>();
				totaVoLi.add(totaVo);
				this.updateJournal(false, "TT997", message, this.titaVo.getMrKey());
				return false;
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
				sockSender.closeChannel();

				message = TTError.TT999 + " 交易序號 : [" + seq + "] " + e.getMessage();
				writeLog(logFile, message, "TOTA");
				TotaVo totaVo = new TotaVo(this.titaVo);
				totaVo.setErrorMsg(message);
				totaVoLi = new ArrayList<TotaVo>();
				totaVoLi.add(totaVo);
				this.updateJournal(false, "TT998", message, titaVo.getMrKey());
				return false;
			}

			if (Objects.isNull(totaVoLi)) {
				message = TTError.TT999 + " 交易序號 : [" + seq + "] TotaVoLi Is Null";
				writeLog(logFile, message, "TOTA");
				this.updateJournal(false, "TT999", message, this.titaVo.getMrKey());
				return false;
			}

			for (TotaVo t : totaVoLi)
				writeLog(logFile, t.toJsonString(), "TOTA");

			logger.info("totaVoLi size:" + totaVoLi.size());

			if (this.handleCE999()) {
				logger.info("break receive loop");
				this.jnlId = -1;
				this.updateJournal(false, "CE999", "CE999", this.totaVoLi.get(0).getMrkey());
			} else {
				this.updateJournal(false, this.totaVoLi.get(0).getMsgId(), "", this.totaVoLi.get(0).getMrkey());
			}
			status = true;
		} catch (Exception ex) {
			message = TTError.TT999 + "系統內部錯誤," + ex.getMessage();

			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		} finally {
			;
		}
		return status;
	}

	private boolean checkSession() {
		UserInfo userInfo = (UserInfo) session.getAttribute(GlobalValues.SESSION_USER_INFO);
		if (userInfo == null) {
			message = TTError.TT101;
			logger.error(message);
			return false;
		}
		return true;
	}

	private void saveLastSeq(int seq) {
		String s = new Integer(seq).toString();
		logger.info("ready save to session seq = " + s);
		logger.info("try get session seq  = " + this.session.getAttribute(GlobalValues.SESSION_LAST_HOSTSEQ));
		this.session.setAttribute(GlobalValues.SESSION_LAST_HOSTSEQ, s);
		logger.info(FilterUtils.escape("user:" + this.session.getAttribute(GlobalValues.SESSION_UID) + " seq is changed to " + s));
	}

	private int getNextSeqAndSave(String lastSeq) {
		logger.info("getNextSeqAndSave:" + FilterUtils.escape(lastSeq) + ".");
		int seq = Integer.parseInt(lastSeq);
		seq++;
		if (seq > ContentName.maxHostSeq)
			seq = ContentName.minHostSeq;
		saveLastSeq(seq);
		return seq;
	}

	private String putSeqToTita(String t, int seq) {
		String s = FormatUtil.pad9(Integer.toString(seq), ContentName.hostSeqLen);
		this.key += s;
		return t.substring(0, ContentName.hostSeqStart) + s + t.substring(ContentName.hostSeqEnd);
	}

	private void putoriSeqToTita(String t) {
		String s = FormatUtil.pad9(t.substring(ContentName.hostSeqStart, ContentName.hostSeqEnd), ContentName.hostSeqLen);
		this.key += s;
	}

	public static String getTitaMsgTxcd(String s) {
		return s.substring(ContentName.titamsgIdPos, ContentName.titamsgIdPos + 5);
	}

	private void updateJournal(boolean success, String msgid, String errmsg, String mrkey) {
		logger.info("up HostTran journalWanted:" + GlobalValues.journalWanted);
		logger.info("up HostTran bRim:" + this.bRim);
		logger.info("up HostTran bTimeoutupjnl:" + bTimeoutupjnl);
		if (resvleng >= ContentName.maxresvlen) {
			logger.info("沒有儲存journal!!  return" + resvleng);
			return;
		}
		// bTimeoutupjnl FOR XX006是假RIM,但是它需要更新JOURNAL,單據之類的問題
		if ((this.bRim && !bTimeoutupjnl) || !GlobalValues.journalWanted)
			return;

		if (this.jnl == null)
			jnl = jnlService.get(this.jnlId);

		logger.info("after jnlService get jnlId:" + this.jnlId);
		if (success)
			jnl.setTranStatus(100);
		else
			jnl.setTranStatus(1);

		logger.info("after jnl.setTranStatus");
		// MSGID是否比照辦理??? 目前取第一個
		jnl.setMsgid(msgid);
		logger.info(FilterUtils.escape("jnl.getTxcode():" + jnl.getTxcode() + "<->" + msgid));

		String setmrkeytmp = (this.totaVoLi == null ? "" : this.totaVoLi.get(0).getMrkey());
		if (!setmrkeytmp.isEmpty())
			jnl.setMrkey(setmrkeytmp);

		jnl.setErrmsg(errmsg);
		jnl.touch();
		jnlService.save(jnl);
		saveTmpTranDoc();
	}

	// 柯 新加入 暫存journal資料到global
	private void saveTmpTranDoc() {
		TranDoc trandoc = new TranDoc();
		trandoc.setJnlId(jnl.getId());
		trandoc.setSrhKinbr(jnl.getBrn());
		trandoc.setSrhTxcode(jnl.getTxcode());
		trandoc.setSrhRbrno(jnl.getRbrno());
		trandoc.setSrhFbrno(jnl.getFbrno());
		trandoc.setSrhAcbrno(jnl.getAcbrno());
		trandoc.setSrhPbrno(jnl.getPbrno());
		trandoc.setSrhCurrency(jnl.getCurrency());
		trandoc.setSrhTxamt(jnl.getTxamt());
		trandoc.setSrhBatno(jnl.getBatno());
		trandoc.setSrhBusdate(jnl.getBusdate());

		logger.info(FilterUtils.escape("TRAN_DOC_TMP:" + GlobalValues.TRAN_DOC_TMP));
		this.session.setAttribute(GlobalValues.TRAN_DOC_TMP, trandoc);
		logger.info(FilterUtils.escape("session:" + this.session.toString()));
		logger.info(FilterUtils.escape("TRAN_DOC_TMP:" + this.session.getAttribute(GlobalValues.TRAN_DOC_TMP)));
	}

	// prelog錯誤控制
	private void prelogJournal(String resv) {
		logger.info("HostTran journalWanted:" + GlobalValues.journalWanted);
		logger.info("HostTran this.bRim:" + this.bRim);
		logger.info("HostTran jnlId:" + jnlId);
		logger.info(FilterUtils.escape("HostTran this.updatedType:" + this.updatedType));
		logger.info(FilterUtils.escape("this.hostOvrMode:" + this.hostOvrMode));
		logger.info("this.getJnlId():" + this.getJnlId());
		logger.info("resv.length():" + resv.length());
		logger.info("resv.byte.length():" + resv.getBytes().length);
		logger.info("txcode:" + FilterUtils.escape(this.titaVo.getTxCode()));

		// 新增 && this.hostOvrMode != 1) 為了更正 授權時
		// 非常時期 非常手段
		if (this.bRim || !this.updatedType.equals("1") || (jnlId != -1 && this.hostOvrMode != 1) || !GlobalValues.journalWanted) {
			logger.info("no save return!!");
			return;
		}

		SessionMap sessionMap = (SessionMap) session.getAttribute(GlobalValues.SESSION_SYSVAR_MAP);
		String entDay = sessionMap.get("FDATE");

		Rqsp oRqsp = null;
		if (this.titaVo.getRqsp().length() > 4) {
			oRqsp = new Rqsp();
			oRqsp.setBrno(this.titaVo.getBrno());
			oRqsp.setTlrno(this.titaVo.getTlrNo());
			oRqsp.setSupno(this.titaVo.getSupno());
			oRqsp.setSendTimes(1);
			oRqsp.setOvrType(1);
			logger.info("rqsp.length():" + this.titaVo.getRqsp().length() + ";");
			oRqsp.createCodes(this.titaVo.getRqsp());
			oRqsp.touch();
			logger.info(oRqsp.toString());
		}

		if (this.hostOvrMode == 1 && this.getJnlId() != -1) {
			logger.info("host ovr mode, update jnl:" + this.getJnlId());
			this.jnl = jnlService.get(this.jnlId);
//			jnl.setTita(this.titaVo.getOrgTita());
			jnl.setTita(this.titaVo.voToString());
			jnl.setTxno(this.titaVo.getTxCode().equals("LC101") ? null : this.titaVo.getTxtNo());
			jnl.setSupno(this.titaVo.getSupno());
			jnl.setLvel(this.titaVo.getLevel());
			jnl.setOvred(2);
			jnl.setSendTimes(jnl.getSendTimes() + 1);

			logger.info("tita:" + FilterUtils.escape(this.titaVo.voToString()));
			logger.info("txno:" + FilterUtils.escape(this.titaVo.getTxtNo()));
			logger.info("supno:" + FilterUtils.escape(this.titaVo.getSupno()));
			jnl.touch();

			if (oRqsp != null) {
				logger.info("" + jnl.getId().toString().length());
				logger.info("" + jnl.getOvred());
				logger.info("" + jnl.getSendTimes());
				oRqsp.setJnlId(jnl.getId());
				oRqsp.setOvrType(jnl.getOvred());
				oRqsp.setSendTimes(jnl.getSendTimes());
			}
			jnlService.saveJnlAndRqsp(jnl, oRqsp);
			return;
		}

		jnl = new Journal();

		jnl.setBusdate(entDay);
		jnl.setBrn(this.titaVo.getBrno());
		jnl.setTlrno(this.titaVo.getTlrNo());

		// TODO ACBRNO RBRNO FBRNO CIFKEY BATNO
		jnl.setRbrno(this.titaVo.getRbrno());
		jnl.setFbrno(this.titaVo.getFbrno());
		jnl.setAcbrno(this.titaVo.getAcbrno());
		jnl.setCifkey(this.titaVo.getCifkey());
		jnl.setCurrency(this.titaVo.getCurcd());
		jnl.setTxamt(this.titaVo.getTxAmt());
		jnl.setBatno(this.titaVo.getBatchNo());
		jnl.setPbrno(this.titaVo.getPbrno()); // 柯 新增:send.jsp 提供

		jnl.setTxno(this.titaVo.getTxtNo());
		jnl.setTxcode(this.titaVo.getTxCode());
		jnl.setTranStatus(-1);
//		jnl.setTita(this.titaVo.getOrgTita());
		jnl.setTita(this.titaVo.voToString());
		jnl.setTitaResv(resv);
		jnl.setSupno(this.titaVo.getSupno());
		jnl.setLvel(this.titaVo.getLevel());
		logger.info("AFTER setLevel!");
		jnl.setSendTimes(1);
		if (oRqsp != null)
			jnl.setOvred(this.titaVo.getRqsp().length() == 0 ? 0 : 1);

		jnl.touch();
		jnl = this.jnlService.saveJnlAndRqsp(jnl, oRqsp);
		this.jnlId = jnl.getId();
		logger.info("jnl id:" + this.jnlId);
	}

	private boolean isGoodMsgId() {
		String txRsut = this.totaVoLi.get(0).getTxrsut();
		logger.info("isGoodMsgId:" + txRsut);
		return txRsut.equals("S") || txRsut.equals("W");
	}

	// return 0: do nothing
	// 1: break receive loop
	private boolean handleCE999() {
		if (this.totaVoLi.get(0).getMsgId().equals("CE999")) {
			String uid = (String) this.session.getAttribute(GlobalValues.SESSION_UID);
			logger.info("user:" + uid + " got a CE999");
			try {
				String correctSeq = this.totaVoLi.get(0).getTxtNo();
				logger.info("user:" + uid + " got a CE999 with correct seq:" + correctSeq);
				int iCorrectSeq = Integer.parseInt(correctSeq);
				this.saveLastSeq(iCorrectSeq);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}
			return true;
		}
		return false;
	}

	private String getLogFileName() {
		return GlobalValues.hostLogFolder + File.separator + DateUtil.getToday() + "_" + this.titaVo.getTlrNo() + ".log";
	}

	private void writeLog(String filename, String tmpstr, String tag) {
		StringBuffer t = new StringBuffer();

		t.append(PoorManUtil.getNowwithFormat("yyyy/MM/dd HH:mm:ss.SSS"));
		t.append(" [");
		t.append(tag);
		t.append("] TXCD:");
		t.append(this.titaVo.getTxCode());
		t.append(",key:");
		t.append(key);

		try {
			File log = new File(FilterUtils.filter(filename));
			if (!log.exists()) {
				log.createNewFile();
			}

			PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(FilterUtils.filter(filename), true)));
			pr.println(t.toString());
			pr.println(tmpstr);
			pr.println();
			pr.close();

		} catch (IOException e) {
			;
		}
	}

	public String getTotaJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("txcd", this.titaVo.getTxCode());
		map.put("key", key);
		map.put("status", status);
		map.put("seqno", this.titaVo.getTxtNo());
		map.put("message", message);
		map.put("errorhead", "");
		map.put("origSeq", this.origSeq);
		map.put("totaLi", this.totaVoLi == null ? "" : this.totaVoLi);
		map.put("jnlId", this.jnlId);
		try {
			return new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).writeValueAsString(map);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return null;
		}
	}

	// 柯: for timeout
	public String getSessionAsJSON(SessionMap sessionMap) throws Exception {
		StringWriter writer = new StringWriter();
		ObjectMapper m = new ObjectMapper();
		m.writeValue(writer, sessionMap);
		return writer.toString();
	}

	public long getJnlId() {
		return jnlId;
	}

	public void setJnlId(long jnlId) {
		this.jnlId = jnlId;
	}

	public String getUpdatedType() {
		return updatedType;
	}

	public void setUpdatedType(String updatedType) {
		this.updatedType = updatedType;
	}

	public void setbRim(boolean bRim) {
		this.bRim = bRim;
	}

	public void setMsgMode(int msgMode) {
		this.msgMode = msgMode;
	}

	public void setOrigSeq(int origSeq) {
		this.origSeq = origSeq;
	}

	public int getHostOvrMode() {
		return hostOvrMode;
	}

	public void setHostOvrMode(int hostOvrMode) {
		this.hostOvrMode = hostOvrMode;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
