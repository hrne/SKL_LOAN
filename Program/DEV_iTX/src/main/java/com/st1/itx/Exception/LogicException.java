package com.st1.itx.Exception;

import java.util.Objects;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxErrCode;
import com.st1.itx.db.service.TxErrCodeService;
import com.st1.itx.util.MySpring;

/**
 * LogicException
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
public class LogicException extends Exception {

	private static final long serialVersionUID = 1713417913516730890L;

	private String errorMsgId = "", errorMsg = "";

	private TotaVo totaVo = new TotaVo();

	public LogicException(String errorMsgId, String errorMsg) {
		super(errorMsgId + errorMsg);
		this.errorMsgId = errorMsgId;
		this.errorMsg = errorMsg;
		this.setErrMsg();
	}

	public LogicException(TitaVo titaVo, String errorMsgId, String errorMsg) {
		super(errorMsgId + errorMsg);
		this.errorMsgId = errorMsgId;
		this.errorMsg = errorMsg;
		this.setErrMsg();
	}

	public TotaVo getErrorMsg(TitaVo titaVo) {
		totaVo.init(titaVo);
		if (!errorMsgId.equals("CE901"))
			this.totaVo.setTxrsutE();
		this.totaVo.setErrorMsgId(this.errorMsgId);
		this.totaVo.setErrorMsg(this.errorMsg.length() > 101 ? this.errorMsg.substring(0, 100) : this.errorMsg);
		return this.totaVo;
	}

	private void setErrMsg() {
		TxErrCodeService txErrCodeService = (TxErrCodeService) MySpring.getBean("txErrCodeService");
		TxErrCode txErrCode = txErrCodeService.findById(this.errorMsgId);
		if (txErrCode != null)
			this.errorMsg = !Objects.isNull(this.errorMsg) && !this.errorMsg.trim().isEmpty() ? txErrCode.getErrContent() + " (" + this.errorMsg + " )" : txErrCode.getErrContent();
		if (Objects.isNull(this.errorMsg))
			this.errorMsg = "";
	}

	public String getErrorMsgId() {
		return errorMsgId;
	}

	public void setErrorMsgId(String errorMsgId) {
		this.errorMsgId = errorMsgId;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
