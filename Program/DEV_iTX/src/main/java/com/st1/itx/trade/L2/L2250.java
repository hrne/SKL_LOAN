package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.Guarantor;
import com.st1.itx.db.domain.GuarantorId;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.GuarantorService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * FunCd=9,1<br>
 * ApplNo=9,7<br>
 * GuaId=X,10<br>
 */

@Service("L2250")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2250 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2250.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService sCustMainService;

	/* DB服務注入 */
	@Autowired
	public GuarantorService sGuarantorService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Autowired
	public SendRsp sendRsp;

	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2250 ");
		this.totaVo.init(titaVo);

		// tita FunCd
		int iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));

		// tita ApplNo
		int iApplNo = parse.stringToInteger(titaVo.getParam("ApplNo"));

		// tita GuaId
		String iGuaId = titaVo.getParam("GuaId").trim();

		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// 用保證人統編查客戶資料主檔鍵值
		CustMain tCustMain = sCustMainService.custIdFirst(iGuaId);

		if (tCustMain == null) {
			throw new LogicException("E0001", "客戶資料主檔" + iGuaId);
		}

		// 保證人鍵值
		String guaUKey = tCustMain.getCustUKey();

		// 申請號碼不得為0
		if (iApplNo == 0) {
			throw new LogicException("E2032", "");
		}

		// 用申請號碼查額度主檔檢查是否存在
		FacMain tFacMain = sFacMainService.facmApplNoFirst(iApplNo);

		if (tFacMain == null) {
			throw new LogicException("E2022", "額度主檔");
		}

		// new table PK
		GuarantorId tGuarantorId = new GuarantorId();

		// 塞值進PK
		tGuarantorId.setApproveNo(iApplNo);
		tGuarantorId.setGuaUKey(guaUKey);

		// new table 裝tita
		Guarantor tGuarantor = new Guarantor();

		tGuarantor = sGuarantorService.holdById(tGuarantorId);

		// Eloan check 是否重送 Y -> 修正
		if (this.isEloan && iFunCd == 1 && tGuarantor != null) {
			iFunCd = 2;
		}

		// FunCd=1新增 FunCd=3複製
		if (iFunCd == 1 || iFunCd == 3) {
			boolean isInsert = true;
			// 新增資料存在拋錯
			if (tGuarantor != null) {
				throw new LogicException("E0002", "保證人檔");
			} else {
				tGuarantor = new Guarantor();
			}

			tGuarantor.setGuarantorId(tGuarantorId);

			tGuarantor.setApproveNo(iApplNo);
			tGuarantor.setGuaUKey(guaUKey);

			tGuarantor.setGuaRelCode(titaVo.getParam("RelInd"));
			tGuarantor.setGuaAmt(parse.stringToBigDecimal(titaVo.getParam("GuaAmt")));
			tGuarantor.setGuaTypeCode(titaVo.getParam("GuaType"));
			tGuarantor.setGuaDate(parse.stringToInteger(titaVo.getParam("GuaDt")));
			tGuarantor.setGuaStatCode(titaVo.getParam("StatCd"));
			tGuarantor.setCancelDate(parse.stringToInteger(titaVo.getParam("CancelDt")));

			this.info("L2250" + tGuarantor);
			try {
				sGuarantorService.insert(tGuarantor, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "保證人檔");
			}

		} else if (iFunCd == 2) {
			// FunCd=2修改

			// PK hold table

			if (tGuarantor == null) {
				throw new LogicException("E0003", "保證人檔");
			}
			// 變更前
			Guarantor beforeGuarantor = (Guarantor) dataLog.clone(tGuarantor);

			this.info("befoamt = " + tGuarantor.getGuaAmt());
			this.info("newamt = " + parse.stringToBigDecimal(titaVo.getParam("GuaAmt")));
			this.info("empnos = " + titaVo.getEmpNos().trim());

			// 異動須刷主管卡
			if ( titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			// tita寫進table
			tGuarantor.setGuaRelCode(titaVo.getParam("RelInd"));
			tGuarantor.setGuaAmt(parse.stringToBigDecimal(titaVo.getParam("GuaAmt")));
			tGuarantor.setGuaTypeCode(titaVo.getParam("GuaType"));
			tGuarantor.setGuaDate(parse.stringToInteger(titaVo.getParam("GuaDt")));
			tGuarantor.setGuaStatCode(titaVo.getParam("StatCd"));
			tGuarantor.setCancelDate(parse.stringToInteger(titaVo.getParam("CancelDt")));

			try {
				// 修改
				tGuarantor = sGuarantorService.update2(tGuarantor, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "保證人檔");
			}

			// 紀錄變更前變更後
			dataLog.setEnv(titaVo, beforeGuarantor, tGuarantor);
			dataLog.exec();

		} else if (iFunCd == 4) {
			// FunCd=4刪除

			// PK hold table

			if (tGuarantor == null) {
				throw new LogicException("E0004", "保證人檔");
			}
			// 刪除保證人資料須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "");
			}

			try {
				// 保證人在非保證狀態才可刪除
				if (!"1".equals(tGuarantor.getGuaStatCode())) {
					sGuarantorService.delete(tGuarantor, titaVo);
				} else {
					throw new LogicException("E0008", "保証人尚在保証狀態，不可刪除(保證人主檔)");
				}

			} catch (DBException e) {
				throw new LogicException("E0008", "保證人檔");
			}

		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}