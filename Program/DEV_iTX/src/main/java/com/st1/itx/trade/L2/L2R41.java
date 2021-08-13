package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.ClMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R41")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R41 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R41.class);

	/* DB服務注入 */
	@Autowired
	public ClMainService sClMainService;

	/* DB服務注入 */
	@Autowired
	public ClFacService sClFacService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R41 ");
		this.totaVo.init(titaVo);

//		tita輸入參數
		int iClCode1 = parse.stringToInteger(titaVo.getParam("ClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("ClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("ClNo"));
		BigDecimal evaAmt = parse.stringToBigDecimal(titaVo.getParam("RimEvaAmt"));// 鑑估總價
		BigDecimal loanToValue = parse.stringToBigDecimal(titaVo.getParam("RimLoanToValue"));// 貸放成數
		BigDecimal settingAmt = parse.stringToBigDecimal(titaVo.getParam("RimSettingAmt"));// 設定金額
		BigDecimal shareTotal = BigDecimal.ZERO;// tita可分配金額
		BigDecimal shareAmtSum = new BigDecimal(0); // 計算同擔保品分配金額加總

		// 查此同一擔保品在"擔保品與額度關聯檔"的分配金額加總
		Slice<ClFac> slClFac = sClFacService.clNoEq(iClCode1, iClCode2, iClNo, 0, Integer.MAX_VALUE, titaVo);
		List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
		// 加總
		if (lClFac != null && lClFac.size() > 0) {
			for (ClFac tmpClFac : lClFac) {
				shareAmtSum = shareAmtSum.add(tmpClFac.getShareAmt());
			}
		}

		// 輸入鑑價總值,貸放成數
		if (evaAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.info("鑑估總價 = " + evaAmt.toString());
			this.info("貸放成數 = " + loanToValue.toString());
			// 可分配金額=鑑估總值*貸放成數(四捨五入至個位數)
			// 同一擔保品在ClFac擔保品關聯檔的分配金額加總需小於ClMain擔保品主檔的可分配金額

			shareTotal = evaAmt.multiply(loanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);

			if (loanToValue.compareTo(BigDecimal.ZERO) > 0) {
				if (shareTotal.subtract(shareAmtSum).compareTo(BigDecimal.ZERO) < 0) {
					throw new LogicException("E2034", "鑑估總價*貸放成數 = " + evaAmt + "*" + loanToValue + "=" + shareTotal
							+ "," + "同一擔保品額度關聯檔之分配金額加總 = " + shareAmtSum);
				}
			} else if (loanToValue.compareTo(BigDecimal.ZERO) == 0) {
				if (shareTotal.subtract(shareAmtSum).compareTo(BigDecimal.ZERO) < 0) {
					throw new LogicException("E2033", "擔保註記為副擔保,先到額度與擔保品關聯檔修改分配金額");
				}
			}
		}

		// 設定金額輸入
		if (settingAmt.compareTo(BigDecimal.ZERO) > 0) {
			this.info("設定金額 = " + settingAmt.toString());
			this.info("擔保品關聯分配金額加總 = " + shareAmtSum.toString());
			// 設定金額不可小於同一擔保品在ClFac的分配金額加總
			if (settingAmt.subtract(shareAmtSum).compareTo(BigDecimal.ZERO) < 0) {
				throw new LogicException("E2034", "設定金額 = " + settingAmt + "," + "同一擔保品額度關聯檔之分配金額加總 = " + shareAmtSum);

			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}