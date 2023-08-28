package com.st1.itx.trade.L5;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.NegAppr;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
import com.st1.itx.db.service.NegAppr01Service;
//import com.st1.itx.db.domain.NegTrans;
//import com.st1.itx.db.domain.NegTransId;
//import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.NegApprService;

/*DB服務*/

/* 交易共用組件 */
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.MakeExcel;
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
	public MakeExcel makeExcel;
	@Autowired
	public NegAppr01Service sNegAppr01Service;
	@Autowired
	public NegApprService sNegApprService;
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

		int successtimes = 0;
		int falsetimes = 0;
		int successamt = 0;
		int falseamt = 0;

		// 路徑
		if ("".equals(titaVo.getParam("FILENA").trim())) {
			throw new LogicException(titaVo, "E0015", "檔案不存在,請查驗路徑");
		}
		String FilePath = inFolder + dateUtil.getNowStringBc() + File.separatorChar + titaVo.getTlrNo() + File.separatorChar + titaVo.getParam("FILENA").trim();

		NegReportCom.CheckNegArrp(IntBringUpDate, 3, titaVo);

		Slice<NegAppr> slNegAppr = sNegApprService.bringUpDateEq(IntBringUpDate, 0, 500, titaVo);
		List<NegAppr> lNegAppr = slNegAppr == null ? null : slNegAppr.getContent();
		int tApprAcDate = 0;
		NegAppr tNegAppr = new NegAppr();
		if (lNegAppr != null) {
			for (NegAppr cNegAppr : lNegAppr) {
				tNegAppr = sNegApprService.holdById(cNegAppr.getNegApprId(), titaVo);
				tApprAcDate = tNegAppr.getApprAcDate();
				this.info("tApprAcDate==" + tApprAcDate);
			}
		}
		tApprAcDate = tApprAcDate + 19110000;
		List<String[]> lDetailData = null;
		try {
			lDetailData = NegReportCom.BatchTx04(titaVo, FilePath, BringUpDate);
		} catch (IOException e) {

		}

		int i = 1;
		String Col11 = "";// 回應代碼
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "L5709", "最大債權撥付回覆檔檢核", "最大債權撥付回覆檔檢核");
		makeExcel.setValue(1, 1, "區別碼");
		makeExcel.setValue(1, 2, "發件單位");
		makeExcel.setValue(1, 3, "收件單位");
		makeExcel.setValue(1, 4, "指定入扣帳日");
		makeExcel.setValue(1, 5, "轉帳類別");
		makeExcel.setValue(1, 6, "交易序號");
		makeExcel.setValue(1, 7, "交易金額");
		makeExcel.setValue(1, 8, "委託單位");
		makeExcel.setValue(1, 9, "實際入扣帳日");
		makeExcel.setValue(1, 10, "回應代碼");
		makeExcel.setValue(1, 11, "轉帳行代碼");
		makeExcel.setValue(1, 12, "轉帳帳號");
		makeExcel.setValue(1, 13, "帳號ID");
		makeExcel.setValue(1, 14, "銷帳編號");

		for (String Detail[] : lDetailData) {
			i++;
			makeExcel.setValue(i, 1, Detail[0]);// 區別碼
			makeExcel.setValue(i, 2, Detail[1]);// 發件單位
			makeExcel.setValue(i, 3, Detail[2]);// 收件單位
			makeExcel.setValue(i, 4, Detail[3]);// 指定入扣帳日
			makeExcel.setValue(i, 5, Detail[4]);// 轉帳類別
			makeExcel.setValue(i, 6, Detail[5]);// 交易序號
			makeExcel.setValue(i, 7, Integer.parseInt(Detail[7]) / 100, "#.##00");// 交易金額
			makeExcel.setValue(i, 8, Detail[9]);// 委託單位
			makeExcel.setValue(i, 9, Detail[10]);// 實際入扣帳日
			if (("4001").equals(Detail[11])) {
				Col11 = "4001:入/扣帳成功";
				successtimes++;
				successamt = successamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4505").equals(Detail[11])) {
				Col11 = "4505:存款不足";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4508").equals(Detail[11])) {
				Col11 = "4508:非委託或已終止帳戶";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4806").equals(Detail[11])) {
				Col11 = "4806:存戶查核資料錯誤";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4808").equals(Detail[11])) {
				Col11 = "4808:無此帳戶或問題帳戶";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4405").equals(Detail[11])) {
				Col11 = "4405:未開卡或額度不足";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("4705").equals(Detail[11])) {
				Col11 = "4705:剔除不轉帳";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else if (("2999").equals(Detail[11])) {
				Col11 = "2999:其他錯誤";
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			} else {
				Col11 = Detail[11];
				falsetimes++;
				falseamt = falseamt + Integer.parseInt(Detail[7]) / 100;
			}
			makeExcel.setValue(i, 10, Col11);// 回應代碼
			makeExcel.setValue(i, 11, Detail[12]);// 轉帳行代碼
			makeExcel.setValue(i, 12, Detail[13]);// 轉帳帳號
			makeExcel.setValue(i, 13, Detail[14]);// 帳號ID
			makeExcel.setValue(i, 14, Detail[15]);// 銷帳編號
			if (("+").equals(Detail[6])) {
				// 檢核正確
				// 從交易序號 找到正確的NEGAPPR01的資料
				String FinCode = Detail[2].trim();// 收件單位
				String BatchTxtNo = Detail[5];
				Slice<NegAppr01> slNegAppr01 = sNegAppr01Service.findBatch(BatchTxtNo, FinCode, tApprAcDate, 0, Integer.MAX_VALUE, titaVo);
				List<NegAppr01> lNegAppr01 = slNegAppr01 == null ? null : slNegAppr01.getContent();
				this.info("L5709 lNegAppr01!=null titaVo.isHcodeNormal()=[" + titaVo.isHcodeNormal() + "]");
				if (lNegAppr01 != null) {
					this.info("L5709 lNegAppr01!=null ");
					for (NegAppr01 NegAppr01VO : lNegAppr01) {
						NegAppr01Id NegAppr01IdVO = NegAppr01VO.getNegAppr01Id();

						NegAppr01 NegAppr01UpdVO = sNegAppr01Service.holdById(NegAppr01IdVO, titaVo);
						if (NegAppr01UpdVO != null) {
							BigDecimal ApprAmt = new BigDecimal(Detail[7]);// 撥付金額
							BigDecimal Hund = new BigDecimal(100);// 撥付金額
//							BigDecimal AccuApprAmt = NegAppr01UpdVO.getAccuApprAmt();// 累計撥付金額
							ApprAmt = ApprAmt.divide(Hund);
							this.info("ApprAmt==" + ApprAmt);
							String ReplyCode = Detail[11];// 回應代碼
							int NegAppr01UpdVOApprAcDate = NegAppr01UpdVO.getApprAcDate();// 撥付傳票日期
							if (titaVo.isHcodeNormal()) {
								// 正向交易
								if (NegAppr01UpdVOApprAcDate != 0) {
									if (ApprAmt.compareTo(NegAppr01UpdVO.getApprAmt()) != 0) {
										//
										this.info("L5709 ApprAmt(檔案上傳)=[" + ApprAmt + "],(資料庫)=[" + NegAppr01UpdVO.getApprAmt() + "],NegAppr01Id=[" + NegAppr01UpdVO.getNegAppr01Id().toString() + "]");
										throw new LogicException(titaVo, "", "撥付金額不一致請查驗");
									}
									NegAppr01UpdVO.setBringUpDate(IntBringUpDate);// 提兌日
									NegAppr01UpdVO.setReplyCode(ReplyCode);// 回應代碼
									//2023/7/5:撥付失敗時倒扣累計撥付金額 , 2023/8/26調整為失敗不到扣,重播不累加
									//if(!"4001".equals(ReplyCode)) {
									//	NegAppr01UpdVO.setAccuApprAmt(AccuApprAmt.subtract(ApprAmt));
									//}
									
								} else {
									// 撥付日期
									throw new LogicException(titaVo, "", "流程有誤,請查驗.[撥付傳票日]為空值.請確認是否已做過撥付出帳!");
								}

							} else {
								// 訂正
								NegAppr01UpdVO.setBringUpDate(0);// 提兌日
								NegAppr01UpdVO.setReplyCode("");// 回應代碼
								//2023/7/5:訂正須加回累計撥付金額 ,  2023/8/26調整正向不倒扣故點掉
								//if(!"4001".equals(ReplyCode)) {
								//	NegAppr01UpdVO.setAccuApprAmt(AccuApprAmt.add(ApprAmt));
								//}
							}

							try {
								sNegAppr01Service.update(NegAppr01UpdVO, titaVo);
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
		if (lDetailData != null) {
			makeExcel.setWidth(2, 15);
			makeExcel.setWidth(3, 15);
			makeExcel.setWidth(4, 15);
			makeExcel.setWidth(5, 15);
			makeExcel.setWidth(6, 15);
			makeExcel.setWidth(7, 30);
			makeExcel.setWidth(8, 15);
			makeExcel.setWidth(9, 15);
			makeExcel.setWidth(10, 40);
			makeExcel.setWidth(11, 15);
			makeExcel.setWidth(12, 30);
			makeExcel.setWidth(13, 15);
			makeExcel.setWidth(14, 15);
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
		totaVo.put("ExcelSnoM", "" + sno);
		if (titaVo.isHcodeNormal()) {
			totaVo.put("OSuccessFlag", "成功筆數 = " + successtimes + "筆      總金額 = " + successamt + "\n" + "失敗筆數 = " + falsetimes + "筆      總金額 = " + falseamt);
		} else {
			totaVo.put("OSuccessFlag", "");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}