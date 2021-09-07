package com.st1.itx.util.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxToDoMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.domain.TxToDoDetailId;
import com.st1.itx.db.service.TxToDoDetailService;
import com.st1.itx.db.service.TxToDoMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

//  -----------------------  應處理清單維護 ------------------ 
//*  功能：    
//*   
//*  一、新增應處理明細 
//*    1. add Detail  
//*      if Tita.isActfgRelease {              // 放行時執行
//*         TxToDoCom.addDetail(boolean dupSkip, int HCode, TxToDoDetail tDetail, TitaVo titaVo); // HCode: 0.正常  1.訂正
//*      }    
//*              
//*                                          
//*    2. add by Detail List 
//*      if Tita.isActfgRelease               // 放行時執行
//*         TxToDoCom.addByDetailList(boolean dupSkip, integer HCode, List<TxToDoDetail> detailList, TitaVo titaVo);
//* 
//*  二、更新應處理明細狀態 
//     1.update Detail Status          
//*      if Tita.isActfgRelease {              // 放行時執行
//*         TxToDoCom.updDetailStatus(integer status, TxToDoDetailId tDetailId, TitaVo titaVo) 
//*      } 
//*                                           處理 -> 2.已處理
//*                                           訂正 -> 0.未處理 
//*                                           保留 -> 1.已保留
//*                                           刪除 -> 3.已刪除
//*       
//*    2. update by Detail List 
//*      if Tita.isActfgRelease {              // 放行時執行
//*         TxToDoCom.updByDetailList(List<TxToDoDetail> detailList, TitaVo titaVo)  
//*        }
//*                                             
//*  三、每日維護清檔   daily House Keeping
//*
//*      TxToDoCom.dailyHouseKeeping(TitaVo titaVo)            
//*
//*新增應處理明細 
//  TxToDoDetail tTxToDoDetail = new TxToDoDetail();
//  tTxToDoDetail.setItemCode("ISTT00");
//  tTxToDoDetail.setCustNo(tInsuRenew.getCustNo());
//  tTxToDoDetail.setFacmNo(tInsuRenew.getFacmNo());
//  tTxToDoDetail.setDtlValue("<Text>");
//  tTxToDoDetail.setProcessNote(dataLines);			
//  txToDoCom.addDetail(false, 0, tTxToDoDetail, titaVo); //  addDetail	
//

/**
 * 應處理清單維護<BR>
 * 1.addDetail 新增應處理明細 call by LXXXX<BR>
 * 2.addByDetailList 整批新增應處理明細 call by LXXXX<BR>
 * 3.updDetailStatus 更新應處理明細狀態 call by LXXXX<BR>
 * 4.updByDetailList 整批更新應處理明細狀態 call by LXXXX<BR>
 * 5.run 自動更新應處理明細狀態 call by ApControl<BR>
 * 6.dailyHouseKeeping 每日維護清檔 call by 日始作業 BS001<BR>
 * 6.1 昨日留存 == Y 則刪除 資料狀態 = 2.已處理, 3.已刪 (不含 0.未處理 1.已保留)<BR>
 * 6.2 else 刪除全部<BR>
 * 
 * @author st1
 *
 */
@Component("txToDoCom")
@Scope("prototype")

public class TxToDoCom extends TradeBuffer {
	@Autowired
	public TxToDoDetailService txToDoDetailService;

	@Autowired
	public TxToDoMainService txToDoMainService;

	@Autowired
	Parse parse;

	private TxToDoDetail tDetail = new TxToDoDetail();
	private TxToDoDetailId tDetailId = new TxToDoDetailId();
	private TxToDoMain tMain = new TxToDoMain();

	/* maintain by Tx */
	/**
	 * 自動更新應處理明細狀態
	 */
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("TxToDoCom ... run");

		// 放行時執行
		// 1.交易更新處理狀態
		if (titaVo.isActfgRelease()) {
			tMain = txToDoMainService.excuteTxcdFirst("C", titaVo.getTxcd(), titaVo); // C-連結或啟動原交易，執行後明細檔狀態改為已處理
			if (tMain != null)
				txUpdStatus(tMain, titaVo);
		}
		return null;
	}

	/* 1.交易更新處理狀態 */
	private void txUpdStatus(TxToDoMain tMain, TitaVo titaVo) throws LogicException {
		// 交易為應處理清單的執行交易，則找出第一筆吻合該項目的戶號 0123456-890-234，更新狀態為已處理
		this.info("txUpdStatus MRKEY=" + titaVo.getMrKey() + ", TxBormNo=" + titaVo.get("TxBormNo"));

		int custNo = 0;
		if (parse.isNumeric(titaVo.getMrKey().substring(0, 7)))
			custNo = parse.stringToInteger(titaVo.getMrKey().substring(0, 7));

		int facmNo = 0;
		if ("-".equals(titaVo.getMrKey().substring(7, 8)) && parse.isNumeric(titaVo.getMrKey().substring(8, 11)))
			facmNo = parse.stringToInteger(titaVo.getMrKey().substring(8, 11));

		int bormNo = 0;
		if (titaVo.get("TxBormNo") != null)
			bormNo = this.parse.stringToInteger(titaVo.getParam("TxBormNo"));
		else if ("-".equals(titaVo.getMrKey().substring(11, 12))
				&& parse.isNumeric(titaVo.getMrKey().substring(12, 15)))
			bormNo = parse.stringToInteger(titaVo.getMrKey().substring(12, 15));

		String iDtlValue = titaVo.get("TxDtlValue");
		if (iDtlValue == null || iDtlValue.trim().length() == 0)
			iDtlValue = " ";

		tDetailId.setItemCode(tMain.getItemCode());
		tDetailId.setCustNo(custNo);
		tDetailId.setFacmNo(facmNo);
		tDetailId.setBormNo(bormNo);
		tDetailId.setDtlValue(iDtlValue);
		tDetail = txToDoDetailService.findById(tDetailId, titaVo);
//		this.info("upd "+tDetail.toString());
		// 新增與處理交易序號需不相同
		if (tDetail != null && tDetail.getStatus() <= 2)
			if (tDetail.getTitaTlrNo() == null || titaVo.getTlrNo() == null
					|| !tDetail.getTitaTlrNo().equals(titaVo.getTlrNo())
					|| parse.stringToInteger(titaVo.getTxtNo()) != tDetail.getTitaTxtNo())
				updDetailStatus(2, tDetailId, titaVo); // 2.已處理

	}

	/**
	 * add by Detail
	 * 
	 * @param dupSkip true: duplicate skip
	 * @param HCode   0.新增 1.刪除
	 * @param tDetail 應處理明細檔
	 * @param titaVo  TitaVo
	 * @throws LogicException LogicException
	 */
	public void addDetail(boolean dupSkip, int HCode, TxToDoDetail tDetail, TitaVo titaVo) throws LogicException {
		this.info("TxToDoCom ... addByDetail");
		// 放行時執行
		if (titaVo.isActfgRelease()) {
			tDetailId.setItemCode(tDetail.getItemCode());
			tDetailId.setCustNo(tDetail.getCustNo());
			tDetailId.setFacmNo(tDetail.getFacmNo());
			tDetailId.setBormNo(tDetail.getBormNo());
			if (tDetail.getDtlValue() == null || tDetail.getDtlValue().trim().length() == 0)
				tDetailId.setDtlValue(" ");
			else
				tDetailId.setDtlValue(tDetail.getDtlValue());
			// 重複跳過時，正常且找得到或訂定時找不到，跳過處理
			boolean skip = false;
			if (dupSkip) {
				if (txToDoDetailService.holdById(tDetailId, titaVo) != null) {
					if (HCode == 0)
						skip = true;
				} else {
					if (HCode == 1)
						skip = true;
				}
			}
			if (!skip) {
				tDetail.setTxToDoDetailId(tDetailId);
				tDetail.setDataDate(this.txBuffer.getMgBizDate().getTbsDy());
				// 應處理主檔
				tMain = txToDoMainService.holdById(tDetail.getItemCode(), titaVo); // hold
				if (tMain == null) {
					this.info("***  ");
					tMain = new TxToDoMain();
					mntMainFixValue(tMain, tDetail.getItemCode(), titaVo);
					if (HCode == 0) {
						addMainCntValue(tMain, tDetail, titaVo);
					} else {
						subMainCntValue(tMain, tDetail, titaVo);
					}
					try {
						txToDoMainService.insert(tMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "TxToDoMain insert " + tMain + e.getErrorMsg());
					}

				} else {
					mntMainFixValue(tMain, tDetail.getItemCode(), titaVo);
					if (HCode == 0) {
						addMainCntValue(tMain, tDetail, titaVo);
					} else {
						subMainCntValue(tMain, tDetail, titaVo);
					}
					try {
						txToDoMainService.update(tMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "TxToDoMain update " + tMain + e.getErrorMsg());
					}
				}
				if (HCode == 0) { // 正常
					// 執行交易
					if (tDetail.getExcuteTxcd() == null || tDetail.getExcuteTxcd().trim().length() == 0)
						tDetail.setExcuteTxcd(tMain.getExcuteTxcd());
					// this.info("Add ="+tDetail.toString());
					try {
						txToDoDetailService.insert(tDetail, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "TxToDoDetail insert " + tDetail + e.getErrorMsg());
					}

				} else { // 訂正
					try {
						txToDoDetailService.delete(tDetail, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008",
								"TxToDoDetail deleteAll " + tDetail + e.getErrorMsg());
					}
				}
			}
		}
		return;
	}

	/* check Detail List */
	private void checkDetailList(List<TxToDoDetail> detailList, TitaVo titaVo) throws LogicException {
		this.info("TxToDoCom ... checkDetailList" + detailList.size());
		if (detailList.size() == 0)
			throw new LogicException(titaVo, "E0013", "toDoList empty ");
		for (int i = 0; i < detailList.size(); i++) {
			if (!detailList.get(i).getItemCode().equals(detailList.get(0).getItemCode())) {
				throw new LogicException(titaVo, "E0013",
						"每筆項目需相同 " + detailList.get(0).getItemCode() + " " + detailList.get(i).getItemCode());
			}
		}
	}

	/**
	 * add by Detail List
	 * 
	 * @param dupSkip    true: duplicate skip
	 * @param HCode      0.新增 1.刪除
	 * @param detailList 應處理明細檔List
	 * @param titaVo     TitaVo
	 * @throws LogicException LogicException
	 */
	public void addByDetailList(boolean dupSkip, int HCode, List<TxToDoDetail> detailList, TitaVo titaVo)
			throws LogicException {
		this.info("TxToDoCom ... addByDetailList" + detailList.size());
		// check Detail List
		checkDetailList(detailList, titaVo);

		// add by Detail
		for (TxToDoDetail tDetail : detailList) {
			addDetail(dupSkip, HCode, tDetail, titaVo);
		}
		return;
	}

	/**
	 * update Detail Status
	 * 
	 * @param status    資料狀態 0.未處理1.已保留2.已處理3.已刪除
	 * @param tDetailId TxToDoDetailId
	 * @param titaVo    TitaVo
	 * @throws LogicException LogicException
	 */
	public void updDetailStatus(int status, TxToDoDetailId tDetailId, TitaVo titaVo) throws LogicException {

		this.info("TxToDoCom ... updByDetailId status=" + status + "id=" + tDetailId);
		// 放行時執行
//   0.未處理
//   1.已保留
//   2.已處理
//   3.已刪除
		if (titaVo.isActfgRelease()) {
			// hold txToDoMain
			// hold txToDoDetail
			if (tDetailId.getDtlValue() == null || tDetailId.getDtlValue().trim().length() == 0)
				tDetailId.setDtlValue(" ");
			tDetail = txToDoDetailService.holdById(tDetailId, titaVo);
			if (tDetail == null)
				this.info("TxToDoCom ... updByDetailId notfound ");
			else {
				if (status == 2 && titaVo.isHcodeErase()) {
					status = 0;
				}
				if (tDetail.getStatus() == status)
					this.info("TxToDoCom ... updByDetailId same status");
				else {
					tMain = txToDoMainService.holdById(tDetailId.getItemCode(), titaVo);
					if (tMain == null)
						throw new LogicException(titaVo, "E0006", "TxToDoMain Notfound" + tDetailId);
					// 減原資料狀態筆數
					subMainCntValue(tMain, tDetail, titaVo);
					// 訂正交易 2.已處理 --> 0.未處理
					tDetail.setStatus(status);

					// 加新資料狀態筆數，正常交易執行
					addMainCntValue(tMain, tDetail, titaVo);

					try {
						txToDoMainService.update(tMain, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "TxToDoMain update " + tMain + e.getErrorMsg());
					}

					// 已處理交易序號
					if (status == 2 || status == 3) {
						tDetail.setTitaEntdy(this.txBuffer.getTxBizDate().getTbsDy());
						tDetail.setTitaKinbr(titaVo.getKinbr());
						tDetail.setTitaTlrNo(titaVo.getTlrNo());
						tDetail.setTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));
					}
					try {

						txToDoDetailService.update(tDetail, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "TxToDoDetail update " + tMain + e.getErrorMsg());
					}
				}
			}
		}
		return;
	}

	/**
	 * update by Detail List
	 * 
	 * @param detailList List of TxToDoDetail
	 * @param titaVo     TitaVo
	 * @throws LogicException LogicException
	 */
	public void updByDetailList(List<TxToDoDetail> detailList, TitaVo titaVo) throws LogicException {
		this.info("TxToDoCom ... updByDetailList ...");

		// 放行時執行
		if (titaVo.isActfgRelease()) {
			if (detailList != null) {
				for (TxToDoDetail tDetail : detailList) {
					tDetailId.setItemCode(tDetail.getItemCode());
					tDetailId.setCustNo(tDetail.getCustNo());
					tDetailId.setFacmNo(tDetail.getFacmNo());
					tDetailId.setBormNo(tDetail.getBormNo());
					tDetailId.setDtlValue(tDetail.getDtlValue());
					tDetail = txToDoDetailService.holdById(tDetailId, titaVo);
					updDetailStatus(tDetail.getStatus(), tDetailId, titaVo);
				}
			}
		}
		return;
	}

	/**
	 * delete by Detail List
	 * 
	 * @param detailList List of TxToDoDetail
	 * @param titaVo     TitaVo
	 * @throws LogicException LogicException
	 */
	public void delByDetailList(List<TxToDoDetail> detailList, TitaVo titaVo) throws LogicException {

		this.info("TxToDoCom ... delByDetailList ...");
		// check Detail List
		checkDetailList(detailList, titaVo);

		// update TxToDoMain
		tMain = txToDoMainService.holdById(detailList.get(0).getItemCode(), titaVo);

		subListCntValue(tMain, detailList, titaVo); // 減原資料狀態筆數

		try {
			txToDoMainService.update(tMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "TxToDoMain update " + tMain + e.getErrorMsg());
		}

		// delete TxToDoDetail
		try {
			txToDoDetailService.deleteAll(detailList, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "TxToDoDetail  deleteAll " + tMain + e.getErrorMsg());
		}

		return;
	}

	/**
	 * delete by Detail
	 * 
	 * @param tDetail TxToDoDetail
	 * @param titaVo  TitaVo
	 * @throws LogicException LogicException
	 */
	public void delByDetail(TxToDoDetail tDetail, TitaVo titaVo) throws LogicException {
		List<TxToDoDetail> detailList = new ArrayList<TxToDoDetail>();
		detailList.add(tDetail);
		delByDetailList(detailList, titaVo);

		return;
	}

	/**
	 * daily House Keeping
	 * 
	 * @param titaVo TitaVo
	 * @throws LogicException LogicException
	 */
	public void dailyHouseKeeping(TitaVo titaVo) throws LogicException {

		this.info("TxToDoCom ... dailyHouseKeeping");
		Slice<TxToDoMain> mainList = txToDoMainService.findAll(this.index, Integer.MAX_VALUE, titaVo);
		if (mainList != null) {
			try {
				txToDoMainService.deleteAll(mainList.getContent(), titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "TxToDoMain deleteAll " + e.getErrorMsg());
			}
		}

		Slice<TxToDoDetail> detailList;
		if (mainList != null) {
			for (TxToDoMain tMain : mainList.getContent()) {
				// delete txToDoDetail depending on YdReserveF
				// 昨日留存 == Y => 刪除 資料狀態 = 2.已處理, 3.已刪 (不含 0.未處理 1.已保留)，else 刪除全部
				if ("Y".equals(tMain.getYdReserveFg()))
					detailList = txToDoDetailService.detailStatusRange(tMain.getItemCode(), 2, 3, this.index,
							Integer.MAX_VALUE, titaVo);
				else
					detailList = txToDoDetailService.detailStatusRange(tMain.getItemCode(), 0, 9, this.index,
							Integer.MAX_VALUE, titaVo);
				if (detailList != null) {
					try {
						txToDoDetailService.deleteAll(detailList.getContent(), titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "TxToDoDetail deleteAll " + e.getErrorMsg());
					}
				}
				// if YdReserveFg = 'Y' find all remaining detail list
				if ("Y".equals(tMain.getYdReserveFg())) {
					detailList = txToDoDetailService.detailStatusRange(tMain.getItemCode(), 0, 1, this.index,
							Integer.MAX_VALUE, titaVo);
					if (detailList != null) {
						TxToDoMain tTxToDoMain = new TxToDoMain();
						mntMainFixValue(tTxToDoMain, tMain.getItemCode(), titaVo);
						addListCntValue(tTxToDoMain, detailList.getContent(), titaVo);
						try {
							txToDoMainService.insert(tTxToDoMain, titaVo);
						} catch (DBException e) {
							throw new LogicException(titaVo, "E0005", "TxToDoMain insert " + tMain + e.getErrorMsg());
						}
					}
				}
			}
		}
		return;
	}

	/* 累加應處理清單筆數 */
	private void addMainCntValue(TxToDoMain tMain, TxToDoDetail tdetail, TitaVo titaVo) throws LogicException {
		this.info("TxToDoCom ... addMainCntValue " + tdetail.getDataDate() + ", tx="
				+ this.txBuffer.getTxBizDate().getTbsDy());
		if (tdetail.getDataDate() < this.txBuffer.getTxBizDate().getTbsDy())
			tMain.setYdReserveCnt(tMain.getYdReserveCnt() + 1); // 昨日留存筆數
		else
			tMain.setTdNewCnt(tMain.getTdNewCnt() + 1); // 本日新增筆數
		switch (tdetail.getStatus()) { // 資料狀態
		case 0: // 0.未處理
			tMain.setUnProcessCnt(tMain.getUnProcessCnt() + 1); // 未處理筆數
			break;
		case 1: // 1.已保留
			tMain.setReserveCnt(tMain.getReserveCnt() + 1); // 保留筆數
			break;
		case 2: // 2.已處理
			tMain.setTdProcessCnt(tMain.getTdProcessCnt() + 1); // 本日處理筆數
			break;
		case 3: // 3.已刪除
			tMain.setTdDeleteCNT(tMain.getTdDeleteCNT() + 1); // 本日刪除筆數
			break;
		}

	}

	/* 累減應處理清單筆數 */
	private void subMainCntValue(TxToDoMain tMain, TxToDoDetail tdetail, TitaVo titaVo) throws LogicException {
		if (tdetail.getDataDate() < this.txBuffer.getTxBizDate().getTbsDy())
			tMain.setYdReserveCnt(tMain.getYdReserveCnt() - 1); // 昨日留存筆數
		else
			tMain.setTdNewCnt(tMain.getTdNewCnt() - 1); // 本日新增筆數
		switch (tdetail.getStatus()) { // 資料狀態
		case 0: // 0.未處理
			tMain.setUnProcessCnt(tMain.getUnProcessCnt() - 1); // 未處理筆數
			break;
		case 1: // 1.已保留
			tMain.setReserveCnt(tMain.getReserveCnt() - 1); // 保留筆數
			break;
		case 2: // 2.已處理
			tMain.setTdProcessCnt(tMain.getTdProcessCnt() - 1); // 本日處理筆數
			break;
		case 3: // 3.已刪除
			tMain.setTdDeleteCNT(tMain.getTdDeleteCNT() - 1); // 本日刪除筆數
			break;
		}

	}

	private void addListCntValue(TxToDoMain tMain, List<TxToDoDetail> detailList, TitaVo titaVo) throws LogicException {
		for (TxToDoDetail tDetail : detailList) {
			addMainCntValue(tMain, tDetail, titaVo);
		}

	}

	private void subListCntValue(TxToDoMain tMain, List<TxToDoDetail> detailList, TitaVo titaVo) throws LogicException {
		for (TxToDoDetail tDetail : detailList) {
			subMainCntValue(tMain, tDetail, titaVo);
		}

	}

	/* 應處理清單設定值 */
	private void mntMainFixValue(TxToDoMain tMain, String itemCode, TitaVo titaVo) throws LogicException {
		String settingValue = null;

//		項目  昨日留存  處理功能  刪除功能  保留功能  關帳檢核  連結查詢交易  連結處理交易  執行交易  訂正功能  項目中文
//      1    2        3       4        5        6       7            8           9        A       B
// 處理功能 Y-有自動處理功能，由該交易程式將明細檔狀態更改為已處理
//   		C-連結處理交易，交易處理後由TxToDoCom共用程式將明細檔狀態更改為已處理
//   		M-人工自行處理，明細檔狀態不會變動
		switch (itemCode) {
//                            1    2 3 4 5 6 7     8     9     A B
		case "TRLN00":
			settingValue = "TRLN00;-;C;-;-;-;L6981;L6981;L3420;Y;放款轉列催收";
			break;
		case "TRIS00":
			settingValue = "TRIS00;Y;Y;-;Y;Y;L6982;L6982;L618B;Y;火險費轉列催收";
			break;
		case "TRLW00":
			settingValue = "TRLW00;Y;Y;-;Y;Y;L6983;L6983;L618C;Y;法務費轉列催收";
			break;
		case "BDCL00":
			settingValue = "BDCL00;Y;C;-;-;-;L698A;L698A;L3731;-;呆帳還清待結案";
			break;
		case "EMRT00":
			settingValue = "EMRT00;Y;C;-;Y;-;L4030;L4030;L3721;-;員工利率調整";
			break;
		case "EMCU00":
			settingValue = "EMCU00;Y;C;-;Y;-;L698A;L698A;L1103;-;員工客戶別調整";
			break;
		case "ACHP00":
			settingValue = "ACHP00;Y;Y;-;-;-;L698A;L4040;L4040;-;產生ACH授權資料";
			break;
		case "POSP00":
			settingValue = "POSP00;Y;Y;-;-;-;L698A;L4041;L4041;-;產生郵局授權資料";
			break;
		case "CHCK00":
			settingValue = "CHCK00;-;M;-;-;-;L698A;L698A;L618A;-;支票兌現檢核";
			break;
		case "CHJC00":
			settingValue = "CHJC00;-;M;-;-;-;L698A;L698A;L8401;-;JCIC報送預先檢核";
			break;
		case "CHRE00":
			settingValue = "CHRE00;Y;C;-;-;-;L698A;L698A;L2307;-;關係人資料撿核";
			break;
		case "ACCL01":
			settingValue = "ACCL01;-;Y;-;-;-;L6985;L6985;L618D;Y;應收利息提存入帳";
			break;
		case "ACCL02":
			settingValue = "ACCL02;-;Y;-;-;-;L6985;L6985;L618D;Y;未付火險費提存入帳";
			break;
		case "ACCL03":
			settingValue = "ACCL03;-;Y;-;-;-;L6985;L6985;L618D;Y;放款承諾提存入帳";
			break;
		case "RVTX00":
			settingValue = "RVTX00;-;C;-;-;Y;L698A;L6984;L3100;Y;預約撥款到期";
			break;
		case "RVLN00":
			settingValue = "RVLN00;-;C;-;-;Y;L698A;L6986;L3101;Y;撥款收息作業";
			break;
		case "AMLX00":
			settingValue = "AMLX00;Y;Y;-;-;-;L698A;     ;L8200;-;AML交易檢核異常";
			break;
		case "TEXT00":
			settingValue = "TEXT00;Y;Y;Y;Y;-;L698A;     ;L4710;-;簡訊通知";
			break;
		case "MAIL00":
			settingValue = "MAIL00;Y;Y;Y;Y;-;L698A;     ;L4711;-;電子郵件";
			break;
		case "L4510":
			settingValue = "L4510 ;-;C;-;-;Y;L698A;L4511;L4511;-;員工扣薪媒體製作";
			break;
		case "EMEP00":
			settingValue = "EMEP00;-;C;-;-;-;L698A;L4200;L4200;-;員工扣薪入帳作業";
			break;
		case "L4600":
			settingValue = "L4603 ;Y;C;-;-;-;L698A;L4600;L4600;-;火險到期檔產生作業";
			break;
		case "L4604":
			settingValue = "L4604 ;Y;C;-;-;-;L698A;L4603;L4603;Y;火險保費未繳轉借支";
			break;
		case "L4702":
			settingValue = "L4702 ;Y;C;-;-;-;L698A;L4702;L4702;-;繳息通知單產生作業";
			break;
		case "L4703":
			settingValue = "L4703 ;Y;C;-;-;-;L698A;L4703;L4703;-;滯繳通知單產生作業";
			break;
		case "L4454":
			settingValue = "L4454 ;Y;C;-;-;-;L698A;L4454;L4454;-;銀扣失敗通知產生作業";
			break;
		case "L9710":
			settingValue = "L9710 ;-;C;-;-;-;L698A;L9710;L9710;-;產生寬限到期明細表";
			break;
		case "L9711":
			settingValue = "L9711 ;-;C;-;-;-;L698A;L9711;L9711;-;產生放款到期明細表及通知單";
			break;
		case "PFCL00":
			settingValue = "PFCL00;-;M;-;-;-;L698A;     ;     ;-;業績工作月結算啟動通知";
			break;
		default:
			throw new LogicException(titaVo, "E0013", "項目代號有誤" + itemCode);
		}
		String[] strAr = settingValue.split(";");
		tMain.setItemCode(strAr[0].trim());
		tMain.setYdReserveFg(strAr[1]);
		tMain.setAutoFg(strAr[2]);
		tMain.setDeleteFg(strAr[3]);
		tMain.setReserveFg(strAr[4]);
		tMain.setAcClsCheck(strAr[5]);
		tMain.setChainInqTxcd(strAr[6]);
		tMain.setChainUpdTxcd(strAr[7].trim());
		tMain.setExcuteTxcd(strAr[8].trim());
		tMain.setEraseFg(strAr[9]);
		tMain.setItemDesc(strAr[10]);
	}
}