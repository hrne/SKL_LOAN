package com.st1.itx.buffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.MgCurr;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TxCom;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.TxBizDate;
import com.st1.itx.db.service.TxBizDateService;
import com.st1.itx.db.domain.SystemParas;
import com.st1.itx.db.domain.TxAmlLog;
import com.st1.itx.db.service.SystemParasService;

/**
 * 共用參數區塊 TxBizDate 當營業日區塊 nTxBizDate 下一營業日區塊 Mgcurr 幣別
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
@Component("txBuffer")
@Scope("prototype")
public class TxBuffer {
	@Autowired
	MGBuffer mgBuffer;

	@Autowired
	SystemParasService systemParasService;

	@Autowired
	private TxBizDateService txBizDateService;

	private SystemParas systemParas;

	private TxBizDate txBizDate, nTxBizDate, mgBizDate;

	private MgCurr mgCurr;

	private List<AcDetail> acDetailList;

	private TxCom txCom;

	// 主管授權明細
	private List<HashMap<String, String>> rspList = new ArrayList<HashMap<String, String>>();

	private List<TxAmlLog> amlList = new ArrayList<TxAmlLog>();

//	@PostConstruct
//	public void init() throws LogicException {
	public void init(TitaVo titaVo) throws LogicException {

//		this.txBizDate = mgBuffer.getBizDate();
		this.mgBizDate = txBizDateService.findById("ONLINE", titaVo);
		if (this.mgBizDate == null) {
			throw new LogicException("EC001", "系統日期檔(ONLINE)");
		}

		// 次日交易
		if (titaVo.getEntDyI() == this.mgBizDate.getNbsDy()) {
			this.txBizDate = txBizDateService.findById("NONLINE", titaVo);
			if (this.txBizDate == null) {
				throw new LogicException("EC001", "系統日期檔(ONLINE)");
			}
			this.nTxBizDate = txBizDateService.findById("N2ONLINE", titaVo);
			if (this.nTxBizDate == null) {
				throw new LogicException("EC001", "系統日期檔(N2ONLINE)");
			}
		} else {
			this.txBizDate = this.mgBizDate;
			this.nTxBizDate = txBizDateService.findById("NONLINE", titaVo);
			if (this.nTxBizDate == null) {
				throw new LogicException("EC001", "系統日期檔(NONLINE)");
			}
		}

//		this.nTxBizDate = mgBuffer.getnBizDate();

		this.mgCurr = mgBuffer.getMgCurr();
		this.txCom = new TxCom(this.txBizDate);
		this.acDetailList = new ArrayList<AcDetail>();

//		systemParasService.getEntityManager(titaVo);
		systemParas = systemParasService.findById("LN");
		if (systemParas == null)
			systemParas = new SystemParas();
	}

	/**
	 * @return TxBizDate Business date
	 */
	public TxBizDate getTxBizDate() {
		return this.txBizDate;
	}

	/**
	 * 
	 * @return TxBizDate next Business date
	 */
	public TxBizDate getNtxBizDate() {
		return this.nTxBizDate;
	}

	/**
	 * 
	 * @return mgBizDate
	 */
	public TxBizDate getMgBizDate() {
		return mgBizDate;
	}

	/**
	 * 
	 * @return Mgcurr Currency
	 */
	public MgCurr getMgCurr() {
		return mgCurr;
	}

	/**
	 * set MgCUrr
	 * 
	 * @param mgCurr currMap
	 */
	public void setMgCurr(MgCurr mgCurr) {
		this.mgCurr = mgCurr;
	}

	/**
	 * get TxCom
	 * 
	 * @return TxCom
	 */
	public TxCom getTxCom() {
		return txCom;
	}

	/**
	 * set TxCOm
	 * 
	 * @param txCom txCom Object
	 */
	public void setTxCom(TxCom txCom) {
		this.txCom = txCom;
	}

	/**
	 * get List for AcDetail
	 * 
	 * @return List AcDetail
	 */
	public List<AcDetail> getAcDetailList() {
		return acDetailList;
	}

	/**
	 * add for one
	 * 
	 * @param acDetail Acdetail
	 */
	public void addAcDetailList(AcDetail acDetail) {
		this.acDetailList.add(acDetail);
	}

	/**
	 * add For All
	 * 
	 * @param acDetailList List
	 */
	public void addAllAcDetailList(List<AcDetail> acDetailList) {
		this.acDetailList.addAll(acDetailList);
	}

	/**
	 * set AcDetail
	 * 
	 * @param acDetailList List
	 */
	public void setAcDetailList(List<AcDetail> acDetailList) {
		this.acDetailList = acDetailList;
	}

	/**
	 * get SystemParas
	 * 
	 * @return SystemParas
	 */
	public SystemParas getSystemParas() {
		return systemParas;
	}

	/**
	 * set SystemParas
	 * 
	 * @param systemParas SystemParas
	 */
	public void setSystemParas(SystemParas systemParas) {
		this.systemParas = systemParas;
	}

	/**
	 * @return the rspList
	 */
	public List<HashMap<String, String>> getRspList() {
		return rspList;
	}

	/**
	 * @param rspList the rspList to set
	 */
	public void setRspList(List<HashMap<String, String>> rspList) {
		this.rspList = rspList;
	}

	public List<TxAmlLog> getAmlList() {
		return amlList;
	}

	public void setAmlList(List<TxAmlLog> amlList) {
		this.amlList = amlList;
	}

	public void addAmlList(TxAmlLog txAmlLog) {
		this.amlList.add(txAmlLog);
	}

}
