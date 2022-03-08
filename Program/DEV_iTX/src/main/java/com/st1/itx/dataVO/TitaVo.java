package com.st1.itx.dataVO;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.format.FormatUtil;

@Component("titaVo")
@Scope("prototype")
public class TitaVo extends LinkedHashMap<String, String> {

	private static final long serialVersionUID = 3280668073933350149L;

	private static final Logger logger = LoggerFactory.getLogger(TitaVo.class);

	private final int headCount = 48;

	private int bodyCount = 0;

	private String orgTitaVO;

	private Date date = new Date();

	private final TradeTime tradeTime = new TradeTime(new SimpleDateFormat("yyyyMMdd").format(date), new SimpleDateFormat("HHmmssSSS").format(date));

	private List<Map<String, Object>> eBody;

	public void init() {
		this.put("KINBR", "");
		this.put("TLRNO", "");
		this.put("TXTNO", "");
		this.put("ENTDY", "");
		this.put("ORGKIN", "");
		this.put("ORGTLR", "");
		this.put("ORGTNO", "");
		this.put("ORGDD", "");
		this.put("OrgEntdy", "");
		this.put("TRMTYP", "");
		this.put("TXCD", "");
		this.put("MRKEY", "");
		this.put("CIFKEY", "");
		this.put("CIFERR", "");
		this.put("HCODE", "");
		this.put("CRDB", "");
		this.put("HSUPCD", "");
		this.put("CURCD", "");
		this.put("CURNM", "");
		this.put("TXAMT", "");
		this.put("EMPNOT", "");
		this.put("EMPNOS", "");
		this.put("CALDY", "");
		this.put("CALTM", "");
		this.put("MTTPSEQ", "");
		this.put("TOTAFG", "");
		this.put("OBUFG", "");
		this.put("ACBRNO", "");
		this.put("RBRNO", "");
		this.put("FBRNO", "");
		this.put("RELCD", "");
		this.put("ACTFG", "");
		this.put("SECNO", "");
		this.put("MCNT", "");
		this.put("TITFCD", "");
		this.put("RELOAD", "");
		this.put("BATCHNO", "");
		this.put("DELAY", "");
		this.put("FMTCHK", "");
		this.put("FROMMQ", "");
		this.put("FUNCIND", "");
		this.put("LockNo", "");
		this.put("LockCustNo", "");
		this.put("AUTHNO", "");
		this.put("AGENT", "");
		this.put("ORGEMPNM", "");
		this.put(ContentName.dataBase, ContentName.onLine);
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map<br>
	 * previously contained a mapping for the key, the old value is replaced.<br>
	 *
	 * @param key   key with which the specified value is to be associated
	 * @param param value to be associated with the specified key
	 */
	public void putParam(String key, Object param) {
		if (param instanceof Integer)
			this.put(key, new BigDecimal((Integer) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Double)
			this.put(key, new BigDecimal((Double) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Float)
			this.put(key, new BigDecimal((Float) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof Long)
			this.put(key, new BigDecimal((Long) (param == null ? 0 : param)).toPlainString());
		else if (param instanceof BigDecimal)
			this.put(key, ((BigDecimal) (param == null ? 0 : param)).toPlainString());
		else
			this.put(key, (String) (param == null ? "" : param));
	}

	/**
	 * Returns the value to which the specified key is mapped,or null if this map
	 * contains no mapping for the key.<br>
	 * 
	 * More formally, if this map contains a mapping from a key k to a value v such
	 * that (key==null ? k==null :key.equals(k)), then this method returns v;
	 * otherwiseit returns null. (There can be at<br>
	 * most one such mapping.)<br>
	 * 
	 * A return value of null does not necessarilyindicate that the map contains no
	 * mapping for the key; it's alsopossible that the map explicitly maps the key
	 * to null.The containsKey operation may be<br>
	 * used todistinguish these two cases.<br>
	 * 
	 * @param key 欄位名稱
	 * @return String Value
	 * @throws LogicException 無此名稱時扔出
	 */
	public String getParam(String key) throws LogicException {
		String value = this.get(key);
		if (value == null)
			throw new LogicException("CE000", "電文有誤,無此Tita欄位 [" + key + "]");

		return value.trim();
	}

	/**
	 * 
	 * @param msg String
	 * @return TitaVo Object
	 * @throws JsonParseException   JsonParseException
	 * @throws JsonMappingException JsonMappingException
	 * @throws IOException          IOException
	 */
	public TitaVo getVo(String msg) throws JsonParseException, JsonMappingException, IOException {
		this.setOrgTitaVO(msg);
		return new ObjectMapper().configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).readValue(msg, TitaVo.class);
	}

	/**
	 * get Json String
	 * 
	 * @return String this
	 * @throws LogicException if convert fail throw LogicException
	 */
	public String getJsonString() throws LogicException {
		try {
			if (this.isEmpty())
				return "";
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());

			throw new LogicException("CE000", "TempVo to String 轉換失敗 : " + this);
		}
	}

	/**
	 * 連動時可創建新的TitaVo<br>
	 * 預設同一Lable 除了TXCD
	 * 
	 * @param txcd 連動交易代號
	 * @return TitaVo
	 */
	public TitaVo newTitaVo(String txcd) {
		TitaVo titaVo = new TitaVo();

		titaVo.putParam("KINBR", this.getKinbr());
		titaVo.putParam("TLRNO", this.getTlrNo());
		titaVo.putParam("TXTNO", this.getTxtNo());
		titaVo.putParam("ENTDD", this.getEntdd());
		titaVo.putParam("ENTDY", this.getEntDy());
		titaVo.putParam("ORGTXSEQ", this.getOrgTxSeq());
		titaVo.putParam("ORGKIN", this.getOrgKin());
		titaVo.putParam("ORGTLR", this.getOrgTlr());
		titaVo.putParam("ORGTNO", this.getOrgTno());
		titaVo.putParam("ORGDD", this.getOrgdd());
		titaVo.putParam("TRMTYP", this.getTrmTyp());
		titaVo.putParam("TXCD", txcd);
		titaVo.putParam("TXCODE", txcd);
		titaVo.putParam("APTYPE", this.getApType());
		titaVo.putParam("TXNO", this.getTxtNo());
		titaVo.putParam("MRKEY", this.getMrKey());
		titaVo.putParam("CIFKEY", this.getCifKey());
		titaVo.putParam("CIFERR", this.getCifErr());
		titaVo.putParam("HCODE", this.getHCode());
		titaVo.putParam("CRDB", this.getCrdb());
		titaVo.putParam("SUPCD", this.getSupCode());
		titaVo.putParam("CURCD", this.getCurCodeI());
		titaVo.putParam("CURNM", this.getCurName());
		titaVo.putParam("TXAMT", this.getTxAmt());
		titaVo.putParam("EMPONT", this.getEmpNot());
		titaVo.putParam("EMPNOS", this.getEmpNos());
		titaVo.putParam("CLADY", this.getCalDy());
		titaVo.putParam("CALTM", this.getCalTm());
		titaVo.putParam("MTTPSEQ", this.getMttpseq());
		titaVo.putParam("TOTAFG", this.getTotaFg());
		titaVo.putParam("OBUFG", this.getObuFg());
		titaVo.putParam("ACBRNO", this.getAcbrNo());
		titaVo.putParam("RBRNO", this.getRbrNo());
		titaVo.putParam("FBRNO", this.getFbrNo());
		titaVo.putParam("RELCD", this.getRelCode());
		titaVo.putParam("ACTFG", this.getActFgS());
		titaVo.putParam("SECNO", this.getSecNo());
		titaVo.putParam("MCNT", this.getMCnt());
		titaVo.putParam("TITFCD", this.getTitfCode());
		titaVo.putParam("RELOAD", this.getReLoad());
		titaVo.putParam("BACTHNO", this.getBacthNo());

//		titaVo.putParam("DELAY", this.getDelay());
//		titaVo.putParam("FMTCHK", this.getFmtCheck());

		return titaVo;
	}

	/**
	 * gen bodyFld Asc
	 */
	public void getBodyLenAndAdd() {
		int i = 1;
		List<String> bodyNameList = new ArrayList<String>();
		for (Map.Entry<String, String> entry : this.entrySet()) {
			if (i > this.headCount && this.checkAnOther(entry.getKey())) {
				bodyNameList.add(entry.getKey());
				this.bodyCount++;
			}
			i++;
		}

		for (int l = 0; l < bodyNameList.size(); l++)
			this.putParam("BodyFld" + (l + 1), this.get(bodyNameList.get(l)));
		if (ThreadVariable.isLogger()) {
			logger.info("BodyCount : " + this.getBodyCount());
			logger.info("AllVo : " + this);
		}
		bodyNameList = null;
	}

	/**
	 * clear BodyFld
	 */
	public void clearBodyFld() {
		int i = 1;
		while (true) {
			if (Objects.isNull(this.remove("BodyFld" + i)))
				break;
			i++;
		}
	}

	private boolean checkAnOther(String name) {
		if ("rim".equals(name) || "TXCODE".equals(name) || "BRTLRNO".equals(name) || "RQSP".equals(name) || "SUPNO".equals(name) || "LEVEL".equals(name) || "PBRNO".equals(name))
			return false;
		else
			return true;
	}

	/**
	 * set orgTita
	 * 
	 * @param orgTitaVO String
	 */
	public void setOrgTitaVO(String orgTitaVO) {
		this.orgTitaVO = orgTitaVO;
	}

	/**
	 * getDBName
	 * 
	 * @return String DBName
	 */
	public String getDataBase() {
		return this.get(ContentName.dataBase) == null ? "onLine" : this.get(ContentName.dataBase);
	}

	/**
	 * getOrgDataBaseFg
	 * 
	 * @return String DBName
	 */
	public String getOrgDataBase() {
		return this.get(ContentName.orgDataBase) == null ? "onLine" : this.get(ContentName.orgDataBase);
	}

	/**
	 * keepOrgDataBaseFg
	 * 
	 */
	public void keepOrgDataBase() {
		this.putParam(ContentName.orgDataBase, this.getDataBase());
	}

	/**
	 * set DB Flag to OnLine
	 */
	public void setDataBaseOnLine() {
		this.putParam(ContentName.dataBase, ContentName.onLine);
	}

	/**
	 * set DB Flag to OnDay
	 */
	public void setDataBaseOnDay() {
		this.putParam(ContentName.dataBase, ContentName.onDay);
	}

	/**
	 * set DB Flag to OnMon
	 */
	public void setDataBaseOnMon() {
		this.putParam(ContentName.dataBase, ContentName.onMon);
	}

	/**
	 * set DB Flag to Hist
	 */
	public void setDataBaseOnHist() {
		this.putParam(ContentName.dataBase, ContentName.onHist);
	}

	/**
	 * set DB Flag to Org
	 */
	public void setDataBaseOnOrg() {
		this.putParam(ContentName.dataBase, this.getOrgDataBase());
	}

	/**
	 * 
	 * @return Integer reIndex
	 */
	public int getReturnIndex() {
		return Integer.parseInt(this.get(ContentName.reIndex) == null ? "0" : this.get(ContentName.reIndex).trim());
	}

	/**
	 * set return Index
	 * 
	 * @param reIndex Integer
	 */
	public void setReturnIndex(int reIndex) {
		this.putParam(ContentName.reIndex, reIndex);
	}

	/**
	 * is Orgtlr Auto if Orgtlr is auto return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isOrgtlrAuto() {
		return this.getOrgTlr().substring(0, 1).equals("C") || this.getOrgTlr().substring(0, 1).equals("B") || this.getOrgTlr().substring(0, 1).equals("X")
				|| this.getOrgTlr().substring(0, 1).equals("Y");
	}

	/**
	 * isRim?
	 * 
	 * @return boolean if rim return true else false
	 */
	public boolean isRim() {
		return this.get("rim") != null && this.get("rim").equals("1");
	}

	/**
	 * isNormal?
	 * 
	 * @return boolean isNormal true else false
	 */
	public boolean isHcodeNormal() {
		return this.get(ContentName.hCode) != null && this.get(ContentName.hCode).equals("0");
	}

	/**
	 * isModify?
	 * 
	 * @return boolean isModify true else false
	 */
	public boolean isHcodeModify() {
		return this.get(ContentName.hCode) != null && this.get(ContentName.hCode).equals("2");
	}

	/**
	 * isHcodeReject? 主管放行退回
	 * 
	 * @return boolean isHcodeReject true else false
	 */
	public boolean isHcodeReject() {
		return this.get(ContentName.hCode) != null && this.get(ContentName.hCode).equals("8");
	}

	/**
	 * isErase?
	 * 
	 * @return boolean isErase true else false
	 */
	public boolean isHcodeErase() {
		return this.get(ContentName.hCode) != null && this.get(ContentName.hCode).equals("1");
	}

	/**
	 * isHcodeSendOut? 櫃員送出放行
	 * 
	 * @return boolean isSendOut true else false
	 */
	public boolean isHcodeSendOut() {
		return this.get(ContentName.hCode) != null && this.get(ContentName.hCode).equals("9");
	}

	/**
	 * isEntry?
	 * 
	 * @return boolean isEntry true else false
	 */
	public boolean isActfgEntry() {
//		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("0") || this.get(ContentName.actfg).equals("1") || this.get(ContentName.actfg).equals("5"));
		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("0") || this.get(ContentName.actfg).equals("1"));
	}

	/**
	 * isRelease?
	 * 
	 * @return boolean isRelease true else false
	 */
	public boolean isActfgRelease() {
//		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("0") || this.get(ContentName.actfg).equals("4") || this.get(ContentName.actfg).equals("6"));
		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("0") || this.get(ContentName.actfg).equals("2"));
	}

	/**
	 * isVerify?
	 * 
	 * @return boolean isVerify true else false
	 */
	public boolean isActfgVerify() {
		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("3"));
	}

	/**
	 * isConfirm?
	 * 
	 * @return boolean isConfirm true else false
	 */
	public boolean isActfgConfirm() {
		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("4"));
	}

	/**
	 * isSuprele?
	 * 
	 * @return boolean isSuprele true else false
	 */
	public boolean isActfgSuprele() {
		return this.get(ContentName.actfg) != null && (this.get(ContentName.actfg).equals("2") || this.get(ContentName.actfg).equals("4"));
	}

	/**
	 * NBSDY
	 * 
	 * @return NBSDY String
	 */
	public String getNbsDy() {
		return Objects.isNull(this.get("NBSDY")) ? "0" : this.get("NBSDY");
	}

	/**
	 * NNBSDY String
	 * 
	 * @return NNBSDY String
	 */
	public String getNnbsDy() {
		return Objects.isNull(this.get("NNBSDY")) ? "0" : this.get("NNBSDY");
	}

	/**
	 * chk nbsdy and nnbsdy
	 * 
	 * @param nbsDy  Integer
	 * @param nnbsDy Integer
	 * @return if not same return true else false
	 */
	public boolean isHolidayChange(int nbsDy, int nnbsDy) {
		if (Integer.parseInt(this.getNbsDy()) != nbsDy || Integer.parseInt(this.getNnbsDy()) != nnbsDy)
			return true;
		return false;
	}

	/**
	 * is TXCD Special?<br>
	 * if TXCD is Special return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTxcdSpecial() {
		return this.getTxcd().startsWith("BS") || this.getTxcd().startsWith("LC") || this.getTxcd().equals("L6103");
//		return this.getTxcd().equals("LC100") || this.getTxcd().equals("LC101") || this.getTxcd().equals("LCR07") || this.getTxcd().equals("L6103") || this.getTxcd().equals("LC013");
	}

	/**
	 * is TXCD INQ?<br>
	 * if TXCD is INQ return true else false
	 * 
	 * @return isTxcdInq true
	 */
	public boolean isTxcdInq() {
		return this.getTxcd().trim().substring(2, 3).equals("9") || this.getTxcd().trim().substring(2, 3).equals("R") || this.getTxcd().trim().substring(2, 3).equals("0")
				|| this.getTxCode().trim().substring(2, 3).equals("0") || this.getTxCode().trim().substring(2, 3).equals("9") || this.getTxCode().trim().substring(2, 3).equals("R")
				|| this.isFuncindInquire();
	}

	/**
	 * 
	 * @return isTxcdRim true
	 */
	public boolean isTxcdRim() {
		return this.getTxcd().trim().substring(2, 3).equals("R") || this.getTxCode().trim().substring(2, 3).equals("R");
	}

	/**
	 * 系統操作交易，不編序號 LC100,LC101:櫃員登入/登出
	 * 
	 * @return boolen true or false
	 */
	public boolean isTxcdOperation() {
		return this.getTxcd().trim().equals("LC100") || this.getTxcd().trim().equals("LC101");
	}

	/**
	 * is Terminal?<br>
	 * if TermType is Terminal return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypTerminal() {
		return "00".equals(this.getTrmTyp());
	}

	/**
	 * is Terminal?<br>
	 * if TermType is NTSYS return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypNtSys() {
		return this.getTrmTyp().equals("15");
	}

	/**
	 * is Terminal?<br>
	 * if TermType is Batch return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypBatch() {
		return this.getTrmTyp().equals("BA");
	}

	/**
	 * is Terminal?<br>
	 * if TermType is CCUT return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypNtCcut() {
		return this.getTrmTyp().equals("32");
	}

	public boolean isTrmtypNwBank() {
		String s = this.getTrmTyp();
		int value = Integer.parseInt(s.isEmpty() ? "0" : s);
		return value >= 41 && value <= 69;
	}

	/**
	 * is Trmtyp TermAuto<br>
	 * if TermType is TermAuto return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypTermAuto() {
		return this.getTrmTyp().equals("AU");
	}

	/**
	 * is Trmtyp TermTeller<br>
	 * if TermType is TermTeller return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypTermTeller() {
		return this.getTrmTyp().isEmpty();
	}

	/**
	 * is UPDF?<br>
	 * if TermType is UPDF return true else false
	 * 
	 * @return boolean true or false
	 */
	public boolean isTrmtypTermUpdf() {
		return this.getTrmTyp().equals("14");
	}

	/**
	 * is ReLoad?<br>
	 * if ReLoad return true
	 * 
	 * @return boolean true or false
	 */
	public boolean isReLoad() {
		return this.getReLoad().equals("1");
	}

	public boolean isAlreadyReLoad() {
		return this.getReLoad().equals("2");
	}

	/**
	 * @return String 輸入行
	 */
	public String getKinbr() {
		return this.get(ContentName.kinbr) == null ? "" : this.get(ContentName.kinbr);
	}

	/**
	 * 
	 * @return an String 交易員
	 */
	public String getTlrNo() {
		return this.get(ContentName.tlrno) == null ? "" : this.get(ContentName.tlrno);
	}

	/**
	 * 櫃員姓名
	 * 
	 * @return String
	 */
	public String getEmpNm() {
		return this.get(ContentName.empnm) == null ? "" : this.get(ContentName.empnm);
	}

	public boolean isEloan() {
		return "E-LOAN".equals(this.getTlrNo()) || "E-TEST".equals(this.getTlrNo());
	}

	/**
	 * 
	 * @return an String 交易序號
	 */
	public String getTxtNo() {
		return this.get(ContentName.txtno) == null ? "" : this.get(ContentName.txtno);
	}

	/**
	 * 
	 * @return String 完整交易序號
	 */
	public String getTxSeq() {
		return this.getKinbr() + this.getTlrNo() + this.getTxtNo();
	}

	/**
	 * @return String 帳務日
	 */
	public String getEntdd() {
		return this.get(ContentName.entdd) == null ? "" : this.get(ContentName.entdd);
	}

	/**
	 * 帳務日
	 * 
	 * @return String 帳務日
	 */
	public String getEntDy() {
		return this.get(ContentName.entdy) == null ? "" : this.get(ContentName.entdy);
	}

	public int getEntDyI() {
		try {
			return Integer.parseInt(this.getEntDy());
		} catch (Throwable e) {
			return 0;
		}

	}

	/**
	 * @return String 原輸入行
	 */
	public String getOrgKin() {
		return this.get(ContentName.orgkin) == null ? "" : this.get(ContentName.orgkin);
	}

	/**
	 * @return String 原交易員
	 */
	public String getOrgTlr() {
		return this.get(ContentName.orgtlr) == null ? "" : this.get(ContentName.orgtlr);
	}

	/**
	 * @return String 原交易序號
	 */
	public String getOrgTno() {
		return this.get(ContentName.orgtno) == null ? "" : this.get(ContentName.orgtno);
	}

	/**
	 * 
	 * @return String 原輸入行 + 原交易員 + 原交易序號
	 */
	public String getOrgTxSeq() {
		return this.getOrgKin() + this.getOrgTlr() + this.getOrgTno();
	}

	/**
	 * @return String 原帳務日
	 */
	public String getOrgdd() {
		return this.get(ContentName.orgdd) == null ? "" : this.get(ContentName.orgdd);
	}

	public String getOrgEntdy() {
		return this.get(ContentName.orgentdy) == null ? "" : this.get(ContentName.orgentdy);

	}

	public int getOrgEntdyI() {
		if (this.get(ContentName.orgentdy) == null)
			return 0;

		try {
			return Integer.parseInt(this.get(ContentName.orgentdy));
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			return 0;
		}
	}

	/**
	 * @return String 櫃台機種類
	 */
	public String getTrmTyp() {
		return this.get(ContentName.trmtyp) == null ? "" : this.get(ContentName.trmtyp).trim();
	}

	/**
	 * 
	 * @return String 交易代號
	 */
	public String getTxcd() {
		return this.get(ContentName.txcd) == null ? FormatUtil.padX("", 5) : this.get(ContentName.txcd);
	}

	/**
	 * APTYPE
	 * 
	 * @return String APTYPE
	 */
	public String getApType() {
		return this.getTxcd().substring(1, 2);
	}

	/**
	 * TXNO
	 * 
	 * @return String TXNO
	 */
	public String getTxNo() {
		return this.getTxcd().substring(2, 4);
	}

	/**
	 * 
	 * @return String 交易編號
	 */
	public String getMrKey() {
		return this.get(ContentName.mrkey) == null ? "" : this.get(ContentName.mrkey);
	}

	/**
	 * 
	 * @return String 統編
	 */
	public String getCifKey() {
		return this.get(ContentName.cifkey) == null ? "" : this.get(ContentName.cifkey);
	}

	/**
	 * 
	 * @return String 註記
	 */
	public String getCifErr() {
		return this.get(ContentName.ciferr) == null ? "" : this.get(ContentName.ciferr);
	}

	/**
	 * HCODE
	 * 
	 * @return String HCODE
	 */
	public String getHCode() {
		return this.get(ContentName.hCode) == null ? "0" : this.get(ContentName.hCode);
	}

	public int getHCodeI() {
		return Integer.parseInt(this.getHCode());
	}

	/**
	 * @return HCODE = 0 false, HCODE = 1 true
	 */
	public boolean isHcode() {
		return this.get(ContentName.hCode).equals("0") ? false : true;
	}

	/**
	 * 
	 * @return String 借貸別
	 */
	public String getCrdb() {
		return this.get(ContentName.crdb) == null ? "" : this.get(ContentName.crdb);
	}

	/**
	 * 
	 * @return String 主管授權記號
	 */
	public String getSupCode() {
		return this.get(ContentName.supcd) == null ? "" : this.get(ContentName.supcd);
	}

	/**
	 * 
	 * @return String 授權主管編號
	 */
	public String getSupNo() {
		return this.get(ContentName.supno) == null ? "" : this.get(ContentName.supno);
	}

	/**
	 * 
	 * @return String 主管授權記號
	 */
	public String getHsupCode() {
		return this.get(ContentName.hsupcd) == null ? "" : this.get(ContentName.hsupcd);
	}
	
	/**
	 * 
	 * @return boolean 授權記號 1 return true
	 */
	public boolean isHsupCodeOn() {
		return "1".equals(this.getHsupCode().trim());
	}

	/**
	 * 
	 * @return String 授權理由
	 */
	public String getRqsp() {
		return this.get(ContentName.rqsp) == null ? "" : this.get(ContentName.rqsp);
	}

	/**
	 * 
	 * @return String 交易金額
	 */
	public String getTxAmt() {
		return this.get(ContentName.txamt) == null ? "" : this.get(ContentName.txamt);
	}

	/**
	 * 
	 * @param txAmt TXAMT
	 */
	public void setTxAmt(Object txAmt) {
		this.putParam(ContentName.txamt, txAmt);
	}

	/**
	 * 
	 * @return String EMPNOT
	 */
	public String getEmpNot() {
		return this.get(ContentName.empnot) == null ? "" : this.get(ContentName.empnot);
	}

	/**
	 * 
	 * @return String EMPNOS
	 */
	public String getEmpNos() {
		return this.get(ContentName.empnos) == null ? "" : this.get(ContentName.empnos);
	}

	/**
	 * 
	 * @return String 交易日期(國曆)
	 */
	public String getCalDy() {
		return this.get(ContentName.caldy) == null ? "" : this.get(ContentName.caldy);
	}

	/**
	 * 
	 * @return String 交易日時間
	 */
	public String getCalTm() {
		return this.get(ContentName.caltm) == null ? "" : this.get(ContentName.caltm);
	}

	/**
	 * 
	 * @return String 連棟次數
	 */
	public String getMttpseq() {
		return this.get(ContentName.mttpseq) == null ? "" : this.get(ContentName.mttpseq);
	}

	/**
	 * 
	 * @return String 連棟次數
	 */
	public String getTotaFg() {
		return this.get(ContentName.totafg) == null ? "" : this.get(ContentName.totafg);
	}

	/**
	 * 
	 * @return String obu記號
	 */
	public String getObuFg() {
		return this.get(ContentName.obufg) == null ? "" : this.get(ContentName.obufg);
	}

	/**
	 * 
	 * @return String 掛帳行
	 */
	public String getAcbrNo() {
		return this.get(ContentName.acbrno) == null ? "" : this.get(ContentName.acbrno);
	}

	/**
	 * 
	 * @return String 受理行
	 */
	public String getRbrNo() {
		return this.get(ContentName.rbrno) == null ? "" : this.get(ContentName.rbrno);
	}

	/**
	 * 
	 * @return String 記帳行
	 */
	public String getFbrNo() {
		return this.get(ContentName.fbrno) == null ? "" : this.get(ContentName.fbrno);
	}

	/**
	 * 
	 * @return String (一／二／三段式交易記號)
	 */
	public String getRelCode() {
		return this.get(ContentName.relcd) == null ? "" : this.get(ContentName.relcd);
	}

	/**
	 * 
	 * @return Integer (一／二／三段式交易記號)
	 */
	public int getRelCodeI() {
		String temp = this.get(ContentName.relcd);
		int value = temp != null && temp.trim().isEmpty() ? -1 : Integer.parseInt(temp.trim());
		return value;
	}

	/**
	 * 
	 * @return an Integer 交易進行記號
	 */
	public String getActFgS() {
		return this.get("ACTFG") == null ? "" : this.get("ACTFG");
	}

	/**
	 * 
	 * @return an Integer 交易進行記號
	 */
	public int getActFgI() {
		return Integer.parseInt(this.get("ACTFG") == null ? "0" : this.get("ACTFG"));
	}

	/**
	 * 
	 * @return String 結帳業務別
	 */
	public String getSecNo() {
		return this.get(ContentName.secno) == null ? "" : this.get(ContentName.secno);
	}

	/**
	 * 
	 * @return String 連棟次數
	 */
	public String getMCnt() {
		return this.get(ContentName.mcnt) == null ? "" : this.get(ContentName.mcnt);
	}

	/**
	 * 
	 * @return String 收付欄記號 有無 TITF 記號，0-無,1-帳務,2-央媒
	 */
	public String getTitfCode() {
		return this.get(ContentName.titfcd) == null ? "" : this.get(ContentName.titfcd);
	}

	/**
	 * 收付欄記號
	 * 
	 * @return boolean is TITF 記號1-帳務
	 */
	public boolean isTitfCodeYes() {
		return this.getTitfCode() != null && this.getTitfCode().trim().equals("1");
	}

	/**
	 * 收付欄記號
	 * 
	 * @return boolean is TITF 記號2-央媒
	 */
	public boolean isTitfCodeSlip() {
		return this.getTitfCode() != null && this.getTitfCode().trim().equals("2");
	}

	/**
	 * 收付欄記號
	 * 
	 * @return boolean is TITF 記號1-帳務,2-央媒
	 */
	public boolean isTitfCodeUse() {
		return this.isTitfCodeYes() || this.isTitfCodeUse();
	}

	/**
	 * 收付欄記號
	 * 
	 * @return boolean is TITF 記號0-無
	 */
	public boolean isTitfCodeNo() {
		return this.getTitfCode() != null && this.getTitfCode().trim().equals("0");
	}

	/**
	 * 
	 * @return String 更正重登記號
	 */
	public String getReLoad() {
		return this.get(ContentName.reload) == null ? "" : this.get(ContentName.reload);
	}

	/**
	 * 
	 * @return String 整批批號
	 */
	public String getBacthNo() {
		return this.get(ContentName.batchno) == null ? "" : this.get(ContentName.batchno);
	}

	public void setBatchNo(String batchNo) {
		this.put(ContentName.batchno, batchNo);
	}

	/**
	 * 整批明細序號
	 * 
	 * @return String 整批明細序號
	 */
	public String getBatchSeq() {
		return this.get(ContentName.batchSeq) == null ? "" : this.get(ContentName.batchSeq);
	}

	public void setBatchSeq(String batchSeq) {
		this.put(ContentName.batchSeq, batchSeq);
	}

	/**
	 * 
	 * @return String 功能 1:新增 2:修改 3:拷貝 4:刪除 5:查詢
	 */
	public String getFuncind() {
		return this.get(ContentName.funcind) == null ? "" : this.get(ContentName.funcind);
	}

	public boolean isFuncindNew() {
		return this.get(ContentName.funcind) != null && this.get(ContentName.funcind).trim().equals("1");
	}

	public boolean isFuncindModify() {
		return this.get(ContentName.funcind) != null && this.get(ContentName.funcind).trim().equals("2");
	}

	public boolean isFuncindCopy() {
		return this.get(ContentName.funcind) != null && this.get(ContentName.funcind).trim().equals("3");
	}

	public boolean isFuncindDel() {
		return this.get(ContentName.funcind) != null && this.get(ContentName.funcind).trim().equals("4");
	}

	public boolean isFuncindInquire() {
		return this.get(ContentName.funcind) != null && this.get(ContentName.funcind).trim().equals("5");
	}

	/**
	 * 
	 * @return curcd String
	 */
	public String getCurCodeS() {
		return this.get(ContentName.curcd) == null ? "0" : this.get(ContentName.curcd);
	}

	/**
	 * 
	 * @return curcd Integer
	 */
	public int getCurCodeI() {
		return Integer.parseInt(this.get(ContentName.curcd) == null ? "0" : this.get(ContentName.curcd));
	}

	/**
	 * 
	 * @return String 彆別swift code
	 */
	public String getCurName() {
		return this.get(ContentName.curnm) == null ? "" : this.get(ContentName.curnm);
	}

	/**
	 * 
	 * @return an String TXCode
	 */
	public String getTxCode() {
		return this.get(ContentName.txCode) == null ? "" : this.get(ContentName.txCode);
	}

	/**
	 * 
	 * @return String BRNO+TLRNO
	 */
	public String getBrTlrNo() {
		return this.get(ContentName.brTlrno) == null ? "" : this.get(ContentName.brTlrno);
	}

	/**
	 * 
	 * @return an String BRNO
	 */
	public String getBrno() {
		return this.get(ContentName.kinbr) == null ? "" : this.get(ContentName.kinbr);
	}

	/**
	 * 
	 * @return TradeTime
	 */
	public TradeTime getTradeTime() {
		return this.tradeTime;
	}

	/**
	 * 
	 * @return an String orgTita
	 */
	public String getOrgTitaVO() {
		return orgTitaVO;
	}

	/**
	 * logger flag
	 * 
	 * @return String
	 */
	public String getLoggerFg() {
		return this.get(ContentName.printFg);
	}

	/**
	 * set logger flag
	 * 
	 * @param value String
	 */
	public void setLoggerFg(String value) {
		this.putParam(ContentName.printFg, value);
	}

	/**
	 * JobId
	 * 
	 * @param name 如需多個請用分號隔開
	 */
	public void setBatchJobId(String name) {
		this.putParam("jobId", name);
	}

	/**
	 * JobId
	 * 
	 * @return String jobId
	 */
	public String getBatchJobId() {
		return this.get("jobId") == null ? "" : this.get("jobId");
	}

	/**
	 * 設定BatchJob參數
	 * 
	 * @param jobParm String fot JobParm
	 */
	public void setBatchJobParam(String jobParm) {
		this.putParam("jobParm", jobParm);
	}

	/**
	 * get For JobParm
	 * 
	 * @return String
	 */
	public String getBatchJobParm() {
		return this.get("jobParm") == null ? "" : this.get("jobParm");
	}

	/**
	 * get jobTxSeq
	 * 
	 * @return String
	 */
	public String getJobTxSeq() {
		return this.get(ContentName.jobTxSeq) == null ? "" : this.get(ContentName.jobTxSeq);
	}

	/**
	 * set JobTxSeq
	 * 
	 * @param jobTxSeq String
	 */
	public void setJobTxSeq(String jobTxSeq) {
		this.putParam(ContentName.jobTxSeq, jobTxSeq);
	}

	/**
	 * is off true
	 * 
	 * @return boolean
	 */
	public boolean isJobSendMsgChainOff() {
		return !Objects.isNull(this.get("sendMsgChainOff")) && this.get("sendMsgChainOff").equals("1") ? true : false;
	}

	/**
	 * set Msg Off For batchJob
	 */
	public void setJobSendMsgChainOff() {
		this.put("sendMsgChainOff", "1");
	}

	/**
	 * setMonOrdailyFg<br>
	 * M = Monthly<br>
	 * D = Daily
	 * 
	 * @param fg M or Ds
	 */
	public void setMonOrdailyFg(String fg) {
		this.putParam(ContentName.monOrDay, fg);
	}

	/**
	 * getMonOrdailyFg
	 * 
	 * @return String M or D
	 */
	public String getMonOrdailyFg() {
		return this.get(ContentName.monOrDay);
	}

	public List<Map<String, Object>> geteBody() {
		return eBody;
	}

	public void seteBody(List<Map<String, Object>> eBody) {
		this.eBody = eBody;
	}

	/**
	 * 權限群組
	 * 
	 * @return String
	 */
	public String getAuthNo() {
		return this.get(ContentName.authNo) == null ? "" : this.get(ContentName.authNo);
	}

	/**
	 * 被代理人員
	 * 
	 * @return String
	 */
	public String getAgent() {
		return this.get(ContentName.agent) == null ? "" : this.get(ContentName.agent);
	}

	/**
	 * 取得使用者IP
	 * 
	 * @return String IP
	 */
	public String getIp() {
		return this.get("IP") == null ? "" : this.get("IP");
	}

	public int getBodyCount() {
		return bodyCount;
	}

	public void setBodyCount(int bodyCount) {
		this.bodyCount = bodyCount;
	}

	/**
	 * getBtnIndex
	 * 
	 * @return Index
	 * @throws LogicException When This Field Is Null
	 */
	public String getBtnIndex() throws LogicException {
		return this.getParam("btnIndex");
	}
	
	/**
	 * when trade is not gridBatch or value is null return 0
	 * 
	 * @return selectTotal
	 */
	public int getSelectTotal() {
		return Objects.isNull(this.get("selectTotal")) ? 0 : Integer.parseInt(this.get("selectTotal"));
	}

	/**
	 * when trade is not gridBatch or value is null return 0
	 * 
	 * @return selectIndex
	 */
	public int getSelectIndex() {
		return Objects.isNull(this.get("selectIndex")) ? 0 : Integer.parseInt(this.get("selectIndex"));
	}
	
	/**
	 * when trade is not gridBatch or value is null or previous gridBatch not
	 * Success return 0
	 * 
	 * @return SelectReturnOK Count
	 */
	public int getSelectReturnOK() {
		return Objects.isNull(this.get("selectReturnOK")) ? 0 : Integer.parseInt(this.get("selectReturnOK"));
	}

	// flowtype = 0,1 / flowstep = 0,1
	// flowtype = 2 / flowstep = 1,2
	// flowtype = 3 / flowstep = 1,3,4
	// flowtype = 4 / flowstep = 1,2,3,4

	public void checkFlow() throws LogicException {
		int flowtype = this.getRelCodeI();
		int flowstep = this.getActFgI();

		if ((flowstep == 0 && flowtype > 1) || (flowstep == 2 && !(flowtype == 2 || flowtype == 4)) || (flowstep == 3 && flowtype < 3) || (flowstep == 4 && flowtype < 3) || (flowstep > 4)
				|| (flowtype > 4)) {
			throw new LogicException("EC005", "FlowType:" + flowtype + " / FlowStep:" + flowstep);
		}

//		if (this.isHcodeModify() && (this.isActfgRelease())) {
//
//		}
	}

	public boolean isReason() {
		return this.isActfgEntry() && (this.isHcodeNormal() || this.isHcodeModify()) && this.getReason().isEmpty();
	}

	public String getReason() {
		return this.get(ContentName.reason) == null ? "" : this.get(ContentName.reason).trim();
	}

}
