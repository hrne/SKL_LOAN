package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l4320RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4320RServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		String iAdjDate = String.valueOf(Integer.valueOf(titaVo.get("AdjDate")) + 19110000);
		int custType1 = 0;
		int custType2 = 0;
		String txKind = titaVo.getParam("TxKind");

//		輸入畫面 戶別 CustType 1:個金;2:企金（含企金自然人）
//		客戶檔 0:個金1:企金2:企金自然人
		if (Integer.parseInt(titaVo.getParam("CustType")) == 2) {
			custType1 = 1;
			custType2 = 2;
		}
		int adjCode = Integer.valueOf(titaVo.getParam("AdjCode"));
		int rateKeyInCode = Integer.valueOf(titaVo.getParam("RateKeyInCode"));
		String titaTlrNo = titaVo.getTlrNo();
		String titaTxtNo = titaVo.getTxtNo();
		this.info("l4320.findAll AdjDate=" + iAdjDate);

		String sql = "SELECT CC.\"CityItem\"    " // F0 鄉鎮區
				+ "      , CA.\"AreaItem\"      " // F1 地區別
				+ "      , BR.\"CustNo\"        " // F2 戶號
				+ "      , BR.\"FacmNo\"        " // F3 額度
				+ "      , BR.\"BormNo\"        " // F4 撥款
				+ "      , CM.\"CustName\"      " // F5 戶名
				+ "      , BR.\"DrawdownAmt\"   " // F6 撥款金額
				+ "      , BR.\"LoanBalance\"   " // F7 放款餘額
				+ "      , BR.\"PresEffDate\"   " // F8 目前生效日
				+ "      , BR.\"CurtEffDate\"   " // F9 本次生效日
				+ "      , BR.\"PrevIntDate\"   " // F10 繳息迄日
				+ "      , BR.\"ProdNo\"        " // F11 商品代碼
				+ "      , FP.\"ProdName\"      " // F12 商品名稱
				+ "      , CD.\"Item\"          " // F13 利率種類
				+ "      , BR.\"PresentRate\"   " // F14 目前利率
				+ "      , BR.\"ContrRateIncr\" " // F15 合約加碼值
				+ "      , BR.\"ProposalRate\"  " // F16 擬調利率
				+ "      , LN.\"DrawdownDate\"  " // F17 撥款日期
				+ "      , LN.\"MaturityDate\"  " // F18 到期日
				+ "      , LN.\"FirstAdjRateDate\"  " // F19 首次調整日期
				+ "      , BR.\"PreNextAdjFreq\"  " // F20 利率調整週期
				+ "      , BR.\"PreNextAdjDate\"  " // F21預定下次利率調整日
				+ "      , BR.\"JsonFields\"  " // F22 jason格式紀錄欄
				+ " FROM \"BatxRateChange\" BR " + " LEFT JOIN \"CdCity\"   CC ON CC.\"CityCode\" = BR.\"CityCode\" "
				+ " LEFT JOIN \"LoanBorMain\" LN ON LN.\"CustNo\"   = BR.\"CustNo\" "
				+ "                             AND LN.\"FacmNo\" = BR.\"FacmNo\" "
				+ "                             AND LN.\"BormNo\" = BR.\"BormNo\" "
				+ " LEFT JOIN \"CdArea\"   CA ON CA.\"CityCode\" = BR.\"CityCode\" "
				+ "                        AND CA.\"AreaCode\" = BR.\"AreaCode\" "
				+ " LEFT JOIN \"CdCode\"   CD ON CD.\"DefCode\" = 'BaseRate0' "
				+ "                          AND CD.\"Code\" = BR.\"BaseRateCode\" "
				+ " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\"   = BR.\"CustNo\" "
				+ " LEFT JOIN \"FacProd\"  FP ON FP.\"ProdNo\"   = BR.\"ProdNo\" " + " WHERE BR.\"AdjDate\" = "
				+ iAdjDate + "   AND BR.\"TxKind\" = " + txKind + "   AND BR.\"CustCode\" >= " + custType1
				+ "   AND BR.\"CustCode\" <= " + custType2 + "   AND BR.\"AdjCode\" = " + adjCode
				+ "   AND BR.\"RateKeyInCode\" = " + rateKeyInCode + "   AND BR.\"TitaTlrNo\" = " + titaTlrNo
				+ "   AND BR.\"TitaTxtNo\" = " + titaTxtNo;
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}