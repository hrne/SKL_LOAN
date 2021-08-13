package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/* log */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
import com.st1.itx.db.service.NegAppr01Service;
//import com.st1.itx.db.domain.NegTrans;
//import com.st1.itx.db.domain.NegTransId;
//import com.st1.itx.db.service.NegTransService;

/*DB服務*/

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.NegReportCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * BringUpDate=9,7<br>
 * FilePath=X,20<br>
 */

@Service("L5709")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5709 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5709.class);
	/* DB服務注入 */
	@Autowired
	public NegReportCom NegReportCom;
//	@Autowired
//	public NegTransService sNegTransService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Value("${iTXInFolder}")
	private String inFolder = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("Run L5709");
		this.info("active L5709 ");
		this.totaVo.init(titaVo);
		String BringUpDate = titaVo.getParam("BringUpDate").trim();// 提兌日
		int IntBringUpDate = 0;
		if (BringUpDate != null && BringUpDate.length() != 0) {
			if (BringUpDate.length() == 7) {
				IntBringUpDate = Integer.parseInt(BringUpDate) + 19110000;
			}
		}

		// 路徑
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo()
				+ File.separatorChar + titaVo.getParam("FILENA").trim();

		NegReportCom.CheckNegArrp(IntBringUpDate, 3, titaVo);
		List<String[]> lDetailData = null;
		try {
			lDetailData = NegReportCom.BatchTx04(titaVo, FilePath, BringUpDate);
		} catch (IOException e) {

		}
		for (String Detail[] : lDetailData) {
			OccursList occursList = new OccursList();
			occursList.putParam("OOCol1", Detail[0]);// 區別碼
			occursList.putParam("OOCol2", Detail[1]);// 發件單位
			occursList.putParam("OOCol3", Detail[2]);// 收件單位
			occursList.putParam("OOCol4", Detail[3]);// 指定入扣帳日
			occursList.putParam("OOCol5", Detail[4]);// 轉帳類別
			occursList.putParam("OOCol6", Detail[5]);// 交易序號
			occursList.putParam("OOCol8", Detail[7]);// 交易金額
			occursList.putParam("OOCol10", Detail[9]);// 委託單位
			occursList.putParam("OOCol11", Detail[10]);// 實際入扣帳日
			occursList.putParam("OOCol12", Detail[11]);// 回應代碼
			occursList.putParam("OOCol13", Detail[12]);// 轉帳行代碼
			occursList.putParam("OOCol14", Detail[13]);// 轉帳帳號
			occursList.putParam("OOCol15", Detail[14]);// 帳號ID
			occursList.putParam("OOCol16", Detail[15]);// 銷帳編號
			this.totaVo.addOccursList(occursList);

			if (("+").equals(Detail[6])) {
				// 檢核正確
				// 從交易序號 找到正確的NEGAPPR01的資料
				int ApprDate = Integer.parseInt(Detail[3]);// 入/扣帳日
				String FinCode = Detail[2];// 收件單位
				String BatchTxtNo = Detail[5];
				// List<NegAppr01> lNegAppr01 =
				// NegReportCom.InsUpdNegApprO1(IntBringUpdate,3,titaVo);
				Slice<NegAppr01> slNegAppr01 = sNegAppr01Service.FindBatch(BatchTxtNo, FinCode, ApprDate, 0,
						Integer.MAX_VALUE, titaVo);
				List<NegAppr01> lNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();
				this.info("L5709 lNegAppr01!=null titaVo.isHcodeNormal()=[" + titaVo.isHcodeNormal() + "]");
				if (lNegAppr01 != null) {
					this.info("L5709 lNegAppr01!=null ");
					for (NegAppr01 NegAppr01VO : lNegAppr01) {
						NegAppr01Id NegAppr01IdVO = NegAppr01VO.getNegAppr01Id();

						NegAppr01 NegAppr01UpdVO = sNegAppr01Service.holdById(NegAppr01IdVO);
						if (NegAppr01UpdVO != null) {
							BigDecimal ApprAmt = new BigDecimal(Detail[7]);// 撥付金額
							String ReplyCode = Detail[11];// 回應代碼
							int NegAppr01UpdVOApprAcDate = NegAppr01UpdVO.getApprAcDate();// 撥付傳票日期
							if (titaVo.isHcodeNormal()) {
								// 正向交易
								if (NegAppr01UpdVOApprAcDate != 0) {
									if (ApprAmt.compareTo(NegAppr01UpdVO.getApprAmt()) != 0) {
										//
										this.info("L5709 ApprAmt(檔案上傳)=[" + ApprAmt + "],(資料庫)=["
												+ NegAppr01UpdVO.getApprAmt() + "],NegAppr01Id=["
												+ NegAppr01UpdVO.getNegAppr01Id().toString() + "]");
										throw new LogicException(titaVo, "", "撥付金額不一致請查驗");
									}
									NegAppr01UpdVO.setBringUpDate(IntBringUpDate);// 提兌日
									NegAppr01UpdVO.setReplyCode(ReplyCode);// 回應代碼
								} else {
									// 撥付日期
									throw new LogicException(titaVo, "", "流程有誤,請查驗.[撥付傳票日]為空值.請確認是否已做過撥付出帳!");
								}

							} else {
								// 訂正
								NegAppr01UpdVO.setBringUpDate(0);// 提兌日
								NegAppr01UpdVO.setReplyCode("");// 回應代碼
							}

							try {
								sNegAppr01Service.update(NegAppr01UpdVO);
							} catch (DBException e) {
								throw new LogicException(titaVo, "E0007", "最大債權撥付資料檔");
							}

						} else {
							// E0006 鎖定資料時，發生錯誤
							throw new LogicException(titaVo, "E0006", "最大債權撥付資料檔");
						}
					}
				}

			} else {
				// 檢核失敗
				throw new LogicException(titaVo, "", "檢核欄位有誤,請查驗檔案是否有誤.");
			}
		}
//		} catch (Exception e) {
		// TODO Auto-generated catch block
//			throw new LogicException(titaVo, "","發生未預期的錯誤");
//		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}