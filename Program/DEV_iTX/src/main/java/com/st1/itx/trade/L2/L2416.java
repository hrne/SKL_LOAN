package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClLand;
import com.st1.itx.db.domain.ClLandId;
import com.st1.itx.db.domain.ClLandOwner;
import com.st1.itx.db.domain.ClLandOwnerId;
import com.st1.itx.db.domain.ClLandReason;
import com.st1.itx.db.domain.ClLandReasonId;
import com.st1.itx.db.domain.ClMain;
import com.st1.itx.db.domain.ClMainId;
import com.st1.itx.db.service.ClLandOwnerService;
import com.st1.itx.db.service.ClLandReasonService;
import com.st1.itx.db.service.ClLandService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * COL_IND1=9,1<br>
 * COL_IND2=9,2<br>
 * COL_NO=9,7<br>
 * END=X,1<br>
 */

@Service("L2416")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2416 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2416.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClLandService sClLandService;

	/* DB服務注入 */
	@Autowired
	public ClLandOwnerService sClLandOwnerService;

	/* DB服務注入 */
	@Autowired
	public ClLandReasonService sClLandReasonService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	//
	private int iFunCd;
	private int iClCode1;
	private int iClCode2;
	private int iClNo;
	private int iLandSeq;
	private ClMainId clMainId = new ClMainId();
	private ClLandId clLandId = new ClLandId();
	private ClLandOwnerId clLandOwnerId = new ClLandOwnerId();
	private ClLandReasonId clLandReasonId = new ClLandReasonId();
	private ClMain tClMain = new ClMain();
	private ClLand tClLand = new ClLand();
	private ClLandOwner tClLandOwner = new ClLandOwner();
	private ClLandReason tClLandReason = new ClLandReason();
	private List<ClLandOwner> lClLandOwner = new ArrayList<ClLandOwner>();
	private List<ClLandReason> lClLandReason = new ArrayList<ClLandReason>();
	private boolean isEloan = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2416 ");
		this.totaVo.init(titaVo);

		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
		}

		// tita
		iFunCd = parse.stringToInteger(titaVo.getParam("FunCd"));
		iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		iLandSeq = parse.stringToInteger(titaVo.getParam("LandSeq"));
		if (iClCode1 == 2 && iLandSeq > 0) {
			throw new LogicException("E0019", "土地擔保品土地序號應為0"); // 輸入資料錯誤
		}

		if (iClCode1 == 1 && iLandSeq == 0 && iFunCd != 1) {
			throw new LogicException("E0019", "房地擔保品土地序號應大於0"); // 輸入資料錯誤
		}

		// ClMain
		clMainId.setClCode1(iClCode1);
		clMainId.setClCode2(iClCode2);
		clMainId.setClNo(iClNo);
// eric 20210422 findbyid > holdbyid
//		tClMain = sClMainService.findById(clMainId, titaVo);
		tClMain = sClMainService.holdById(clMainId, titaVo);
		if (tClMain == null) {
			throw new LogicException("E0019", "應先輸入L2411"); // 輸入資料錯誤
		}

		boolean clLandFlag = true;

		// clLand
		// 房地 && 新增
		if (iClCode1 == 1 && iFunCd == 1) {
			if (this.isEloan) {
				// 土地序號有值時,改為修改
				if (iLandSeq > 0) {
					iFunCd = 2;
				} else {
					iLandSeq = eLoanUnique(titaVo);
					if (iLandSeq > 0) {
						iFunCd = 2;
					}
				}
			}

			// 自動編號
			if (iLandSeq == 0) {
				clLandFlag = false;
				ClLand tLastClLand = sClLandService.findClNoFirst(iClCode1, iClCode2, iClNo);
				if (tLastClLand == null) {
					iLandSeq = 1;
				} else {
					iLandSeq = tLastClLand.getLandSeq() + 1;
				}
			}
		}

		clLandId.setClCode1(iClCode1);
		clLandId.setClCode2(iClCode2);
		clLandId.setClNo(iClNo);
		clLandId.setLandSeq(iLandSeq);
		if (clLandFlag) {
			tClLand = sClLandService.holdById(clLandId, titaVo);
		}

		// 房地
		if (iClCode1 == 1) {
			if (iFunCd == 1) {
				// 新增
				// 擔保品不動產土地檔
				this.tClLand = new ClLand();
				tClLand.setClLandId(clLandId);
				setClLand(titaVo);
				try {
					sClLandService.insert(this.tClLand);
				} catch (DBException e) {
					throw new LogicException("E2009", "擔保品不動產土地檔");
				}
				// insert 土地所有權人檔
				InsertClLandOwner(titaVo);

			} else if (iFunCd == 2) {
				// 擔保品不動產土地檔
				// 變更前
				ClLand beforeClLand = (ClLand) dataLog.clone(tClLand);
				setClLand(titaVo);
				try {
					tClLand = sClLandService.update2(tClLand);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產土地檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClLand, tClLand);
				dataLog.exec();

				// delete 土地所有權人
				deleteClLandOwner(titaVo);
				// insert 土地所有權人檔
				InsertClLandOwner(titaVo);
				// delete 土地修改原因檔
				deleteClLandReason(titaVo);
				// insert 土地修改原因檔
				InsertClLandReason(titaVo);
				// FunCD=4 刪除
			} else if (iFunCd == 4) {
//				tClLand = sClLandService.holdById(clLandId, titaVo);

				if (tClLand != null) {
					try {
						sClLandService.delete(tClLand, titaVo);
						;
					} catch (DBException e) {
						throw new LogicException("E0008", "擔保品土地所有權人檔");
					}
				}
				// delete 土地所有權人
				deleteClLandOwner(titaVo);
				// delete 土地修改原因檔
				deleteClLandReason(titaVo);
			}
		}
		// 土地
		else {
			if (tClLand == null) {
				throw new LogicException("E0014", "擔保品土地檔應在L2411產生"); // 檔案錯誤
			}
			// 新增
			if (iFunCd == 1) {
				// 擔保品不動產土地檔
				setClLand(titaVo);
				try {
					sClLandService.update(tClLand);
				} catch (DBException e) {
					throw new LogicException("E2009", "擔保品不動產土地檔");
				}
				// insert 土地修改原因檔
				InsertClLandReason(titaVo);
				// FunCd=2 修改
			} else if (iFunCd == 2) {
				// 擔保品不動產土地檔
				// 變更前
				ClLand beforeClLand = (ClLand) dataLog.clone(tClLand);
				setClLand(titaVo);
				try {
					tClLand = sClLandService.update2(tClLand);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產土地檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClLand, tClLand);
				dataLog.exec();
				// delete 土地修改原因檔
				deleteClLandReason(titaVo);
				// insert 土地修改原因檔
				InsertClLandReason(titaVo);
				// FunCD=4 刪除
			} else if (iFunCd == 4) {

				// 變更前
				ClLand beforeClLand = (ClLand) dataLog.clone(tClLand);
				setClLand(titaVo);
				try {
					tClLand = sClLandService.update2(tClLand);
				} catch (DBException e) {
					throw new LogicException("E0007", "擔保品不動產土地檔");
				}
				// 紀錄變更前變更後
				dataLog.setEnv(titaVo, beforeClLand, tClLand);
				dataLog.exec();
				// delete 土地修改原因檔
				deleteClLandReason(titaVo);
			}
		}

//		this.totaVo.putParam("OResult", "Y");
		this.totaVo.putParam("LandSeq", tClLand.getLandSeq());

		this.addList(this.totaVo);
		return this.sendList();

	}

	private int eLoanUnique(TitaVo titaVo) throws LogicException {
		int landSeq = 0;

		sClLandService.findClNoFirst(iClCode1, iClCode2, iClNo);

		Slice<ClLand> slClLand = sClLandService.findClNo(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		List<ClLand> lClLand = slClLand == null ? null : slClLand.getContent();
		if (lClLand != null) {
			for (ClLand clLand : lClLand) {
				if (clLand.getCityCode().equals(titaVo.getParam("CityCode")) && clLand.getAreaCode().equals(titaVo.getParam("AreaCode")) && clLand.getIrCode().equals(titaVo.getParam("IrCode"))
						&& clLand.getLandNo1().equals(titaVo.getParam("LandNo1")) && clLand.getLandNo2().equals(titaVo.getParam("LandNo2"))) {
					landSeq = clLand.getLandSeq();
					break;
				}

			}
		}
		return landSeq;
	}

	// 土地資料
	private void setClLand(TitaVo titaVo) throws LogicException {
		this.info("L2416.setClLand = " + iFunCd);
		if (iFunCd == 4) {
//			tClLand.setClLandId(clLandId);
//			tClLand.setClCode1(iClCode1);
//			tClLand.setClCode2(iClCode2);
//			tClLand.setClNo(iClNo);
//			tClLand.setLandSeq(iLandSeq);
			tClLand.setCityCode(titaVo.getParam("CityCode"));
			tClLand.setAreaCode(titaVo.getParam("AreaCode"));
			tClLand.setIrCode(titaVo.getParam("IrCode"));
			tClLand.setLandNo1(titaVo.getParam("LandNo1"));
			tClLand.setLandNo2(titaVo.getParam("LandNo2"));
			tClLand.setLandLocation(titaVo.getParam("LandLocation"));
			//
			tClLand.setLandCode("");
			tClLand.setArea(BigDecimal.ZERO);
			tClLand.setLandZoningCode("");
			tClLand.setLandUsageType("");
			tClLand.setPostedLandValue(BigDecimal.ZERO);
			tClLand.setPostedLandValueYearMonth(0);
			tClLand.setTransferedYear(0);
			tClLand.setLastTransferedAmt(BigDecimal.ZERO);
			tClLand.setEvaUnitPrice(BigDecimal.ZERO);
			tClLand.setLandUsageCode("");
			tClLand.setLandRentStartDate(0);
			tClLand.setLandRentEndDate(0);
		} else {
//			tClLand.setClLandId(clLandId);
//			tClLand.setClCode1(iClCode1);
//			tClLand.setClCode2(iClCode2);
//			tClLand.setClNo(iClNo);
//			tClLand.setLandSeq(iLandSeq);
			tClLand.setCityCode(titaVo.getParam("CityCode"));
			tClLand.setAreaCode(titaVo.getParam("AreaCode"));
			tClLand.setIrCode(titaVo.getParam("IrCode"));
			tClLand.setLandNo1(titaVo.getParam("LandNo1"));
			tClLand.setLandNo2(titaVo.getParam("LandNo2"));
			tClLand.setLandLocation(titaVo.getParam("LandLocation"));
			//
			tClLand.setLandCode(titaVo.getParam("LandCode"));
			tClLand.setArea(parse.stringToBigDecimal(titaVo.getParam("Area")));
			tClLand.setLandZoningCode(titaVo.getParam("LandZoningCode"));
			tClLand.setLandUsageType(titaVo.getParam("LandUsageType"));
			tClLand.setPostedLandValue(parse.stringToBigDecimal(titaVo.getParam("PostedLandValue")));
			tClLand.setPostedLandValueYearMonth(
					parse.stringToInteger(parse.IntegerToString(parse.stringToInteger(titaVo.getParam("PostedLandValueYear")) + 1911, 4) + (titaVo.getParam("PostedLandValueMonth"))));
			tClLand.setTransferedYear(parse.stringToInteger(titaVo.getParam("TransferedYear")) + 1911);
			tClLand.setLastTransferedAmt(parse.stringToBigDecimal(titaVo.getParam("LastTransferedAmt")));
			tClLand.setEvaUnitPrice(parse.stringToBigDecimal(titaVo.getParam("EvaUnitPrice")));
			tClLand.setLandUsageCode(titaVo.getParam("LandUsageCode"));
			tClLand.setLandRentStartDate(parse.stringToInteger(titaVo.getParam("LandRentStartDate")));
			tClLand.setLandRentEndDate(parse.stringToInteger(titaVo.getParam("LandRentEndDate")));
		}

	}

	// insert 土地所有權人檔
	private void InsertClLandOwner(TitaVo titaVo) throws LogicException {
		for (int i = 1; i <= 10; i++) {
			// 若該筆無資料就離開迴圈
			if (titaVo.getParam("OwnerId" + i) == null || titaVo.getParam("OwnerId" + i).trim().isEmpty()) {
				break;
			}
			String iOwnerId = titaVo.getParam("OwnerId" + i);
			tClLandOwner = new ClLandOwner();
			clLandOwnerId = new ClLandOwnerId();
			clLandOwnerId.setClCode1(iClCode1);
			clLandOwnerId.setClCode2(iClCode2);
			clLandOwnerId.setClNo(iClNo);
			clLandOwnerId.setLandSeq(iLandSeq);
			clLandOwnerId.setOwnerId(iOwnerId);

			tClLandOwner.setClLandOwnerId(clLandOwnerId);
			tClLandOwner.setClCode1(iClCode1);
			tClLandOwner.setClCode2(iClCode2);
			tClLandOwner.setClNo(iClNo);
			tClLandOwner.setLandSeq(iLandSeq);
			tClLandOwner.setOwnerId(iOwnerId);
			tClLandOwner.setOwnerName(titaVo.getParam("OwnerName" + i));
			tClLandOwner.setOwnerRelCode(titaVo.getParam("OwnerRelCode" + i));
			tClLandOwner.setOwnerPart(parse.stringToBigDecimal(titaVo.getParam("OwnerPart" + i)));
			tClLandOwner.setOwnerTotal(parse.stringToBigDecimal(titaVo.getParam("OwnerTotal" + i)));
			try {
				sClLandOwnerService.insert(tClLandOwner);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品土地所有權人檔");
			}
		}
	}

	// delete 土地所有權人檔
	private void deleteClLandOwner(TitaVo titaVo) throws LogicException {
		Slice<ClLandOwner> slClLandOwner = sClLandOwnerService.LandSeqEq(iClCode1, iClCode2, iClNo, iLandSeq, 0, Integer.MAX_VALUE);
		lClLandOwner = slClLandOwner == null ? null : slClLandOwner.getContent();

		if (lClLandOwner != null && lClLandOwner.size() > 0) {
			try {
				sClLandOwnerService.deleteAll(lClLandOwner);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品土地所有權人檔");
			}
		}
	}

	// insert 土地修改原因檔
	private void InsertClLandReason(TitaVo titaVo) throws LogicException {
		lClLandReason = new ArrayList<ClLandReason>();
		for (int i = 1; i <= 10; i++) {
			// 若該筆無資料就離開迴圈
			if (titaVo.getParam("Reason" + i) == null || titaVo.getParam("Reason" + i).trim().isEmpty()) {
				break;
			}
			tClLandReason = new ClLandReason();
			clLandReasonId = new ClLandReasonId();

			clLandReasonId.setClCode1(iClCode1);
			clLandReasonId.setClCode2(iClCode2);
			clLandReasonId.setClNo(iClNo);
			clLandReasonId.setReasonSeq(i);

			tClLandReason.setClLandReasonId(clLandReasonId);
			tClLandReason.setClCode1(iClCode1);
			tClLandReason.setClCode2(iClCode2);
			tClLandReason.setClNo(iClNo);
			tClLandReason.setReasonSeq(i);
			tClLandReason.setReason(parse.stringToInteger(titaVo.getParam("Reason" + i)));
			tClLandReason.setOtherReason(titaVo.getParam("OtherReason" + i));
			
			tClLandReason.setCreateEmpNo(titaVo.getParam("CreateEmpNo" + i));
			tClLandReason.setLastUpdateEmpNo(titaVo.getParam("CreateEmpNo" + i));
			
			try {
				sClLandReasonService.insert(tClLandReason, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "擔保品土地修改原因檔");
			}
		}
	}

	// insert 土地修改原因檔
	private void deleteClLandReason(TitaVo titaVo) throws LogicException {

		Slice<ClLandReason> slClLandReason = sClLandReasonService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE);
		lClLandReason = slClLandReason == null ? null : slClLandReason.getContent();
		if (lClLandReason != null && lClLandReason.size() > 0) {
			try {
				sClLandReasonService.deleteAll(lClLandReason);
			} catch (DBException e) {
				throw new LogicException("E0008", "擔保品土地修改原因檔");
			}

		}
	}

}