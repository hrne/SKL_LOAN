package com.st1.itx.Exception;

import com.st1.itx.dataVO.TotaVo;

/**
 * DBException
 * 
 * @author AdamPan
 * @version 1.0.0
 *
 */
public class DBException extends Exception {

	private static final long serialVersionUID = 6377133918776963404L;

	private int errId = 0;

	private TotaVo totaVo = new TotaVo();

	/**
	 * 1. Entity Is Null 2. Key Duplicate 3. Update Not Found else UnKnow Error
	 * 
	 * @param errorId Integer
	 */
	public DBException(int errorId) {
		super("errorId = " + errorId);
		this.errId = errorId;
	}

	/**
	 * get error Id <br>
	 * 0 = normal,<br>
	 * 1 = entity is null,<br>
	 * 2 = key Duplicate,<br>
	 * 3 = notFound ,<br>
	 * 4 = updateOrDeleteNotSameKey,<br>
	 * 5 = Update Or Delete Most Be Lock,<br>
	 * 6 = List Of Entities Is Null<br>
	 * 
	 * @return Integer
	 */
	public int getErrorId() {
		return errId;
	}

	/**
	 * 
	 * @return String errMsg
	 */
	public String getErrorMsg() {
		String errMsg = "";
		switch (this.errId) {
		case 1:
			errMsg = "entity is null !!";
			break;
		case 2:
			errMsg = "key Duplicate";
			break;
		case 3:
			errMsg = "NotFound";
			break;
		case 4:
			errMsg = "updateOrDeleteNotSameKey";
			break;
		case 5:
			errMsg = "Update Or Delete Most Be Lock";
			break;
		case 6:
			errMsg = "List Of Entities Is Null";
			break;
		case 7:
			errMsg = "Not Set Which One DBEntityManager";
			break;
		default:
			errMsg = "unknow Error";
			break;
		}
		this.totaVo.setTxrsutE();
		this.totaVo.setErrorMsgId("DB000");
		this.totaVo.setErrorMsg(errMsg);
		return errMsg;
	}
}
