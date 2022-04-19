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

@Service("l4321ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4321ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("l4321.findAll AdjDate=" + iAdjDate);

		String sql = "SELECT CC.\"CityItem\"   " // 鄉鎮區
				+ "      , CA.\"AreaItem\"      " // 地區別
				+ "      , BR.\"CustNo\"        " // 戶號
				+ "      , BR.\"FacmNo\"        " // 額度
				+ "      , BR.\"BormNo\"        " // 撥款
				+ "      , CM.\"CustName\"      " // 戶名
				+ "      , BR.\"DrawdownAmt\"   " // 撥款金額
				+ "      , BR.\"LoanBalance\"   " // 放款餘額
				+ "      , BR.\"PresEffDate\"   " // 目前生效日
				+ "      , BR.\"CurtEffDate\"   " // 本次生效日
				+ "      , BR.\"PrevIntDate\"   " // 繳息迄日
				+ "      , BR.\"ProdNo\"        " // 利率代碼
				+ "      , FP.\"ProdName\"      " // 利率名稱
				+ "      , BR.\"PresentRate\"   " // 目前利率
				+ "      , BR.\"ProposalRate\"  " // 擬調
				+ "      , CC.\"IntRateFloor\"  " // 下限
				+ "      , CC.\"IntRateCeiling\"" // 上限
				+ "      , BR.\"AdjustedRate\"  " // 調後
				+ " FROM \"BatxRateChange\" BR " + " LEFT JOIN \"CdCity\"   CC ON CC.\"CityCode\" = BR.\"CityCode\" "
				+ " LEFT JOIN \"CdArea\"   CA ON CA.\"CityCode\" = BR.\"CityCode\" "
				+ "                        AND CA.\"AreaCode\" = BR.\"AreaCode\" "
				+ " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\"   = BR.\"CustNo\" "
				+ " LEFT JOIN \"FacProd\"  FP ON FP.\"ProdNo\"   = BR.\"ProdNo\" " + " WHERE BR.\"AdjDate\" = "
				+ iAdjDate + "   AND BR.\"TxKind\" = " + txKind + "   AND BR.\"CustCode\" >= " + custType1
				+ "   AND BR.\"CustCode\" <= " + custType2 + "   AND BR.\"AdjCode\" = " + adjCode;
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}