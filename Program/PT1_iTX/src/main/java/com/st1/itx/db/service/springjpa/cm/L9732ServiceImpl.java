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

@Service("L9732ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9732ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L9732.findAll");

		String iDAY = String.valueOf(Integer.valueOf(titaVo.get("ACCTDATE")) + 19110000);
		this.info("會計日期     = " + iDAY);

		// 新增保護機制 當除數為0 該欄位輸出為0
		String sql = " SELECT CF.\"CustNo\" "; // 戶號 0
		sql += "      , CF.\"FacmNo\" ";//1
		sql += "      , CF.\"ClCode1\" ";//2
		sql += "      , CF.\"ClCode2\" ";//3
		sql += "      , CF.\"ClNo\" ";//4
		sql += "      , CS.\"LoanToValue\" / 100  as \"LoanToValue\" ";// 核貸成數5
		sql += "      , CS.\"SettingBalance\"  as \"SettingBalance\" ";// 設 定 股 數 ( 股 )6
		sql += "      , NVL(LBM.\"LoanBal\",0)  AS \"LoanBal\" ";// 目前餘額(萬)7
		sql += "      , CS.\"YdClosingPrice\" ";//
		sql += "        * CS.\"SettingBalance\" AS \"TotalMarketValue\" ";// 擔保品總市價8
		sql += "      , Case ";
		sql += "          when ";
		sql += "             ( CS.\"YdClosingPrice\" * CS.\"SettingBalance\" ) = 0 ";
		sql += "          then 0 ";
		sql += "        else  NVL(LBM.\"LoanBal\",0)  ";
		sql += "              / ( CS.\"YdClosingPrice\"  ";
		sql += "                   * CS.\"SettingBalance\" )  ";
		sql += "                       end AS \"ActualLoanToValue\" ";// 實貸成數9
		sql += "      , Case ";
		sql += "          when CS.\"SettingBalance\" = 0 ";
		sql += "          then 0 ";
		sql += "        else NVL(LBM.\"LoanBal\",0) ";
		sql += "             / CS.\"SettingBalance\" end AS \"LoanPerShare\" ";// 每股貸放10
		sql += "      ,Case ";
		sql += "         when ( CS.\"LoanToValue\" * CS.\"SettingBalance\" / 100) =0 ";
		sql += "         then 0 ";
		sql += "       else NVL(LBM.\"LoanBal\",0)  ";
		sql += "            / ( CS.\"LoanToValue\"  ";
		sql += "                 * CS.\"SettingBalance\"  ";
		sql += "                    / 100)      end AS \"ModifiedEvaPrice\" ";// 調整後鑑定單價11
		sql += "      , CS.\"ClMtr\"     /100           AS \"ClMtr\" ";// 擔保品維持率12
		sql += "      , Case ";
		sql += "          when CS.\"SettingBalance\" * CS.\"ClMtr\" / 100  = 0 ";
		sql += "          then 0 ";
		sql += "        else NVL(LBM.\"LoanBal\",0) ";
		sql += "             / CS.\"SettingBalance\" ";
		sql += "                * CS.\"ClMtr\"  ";
		sql += "                  / 100    end  AS \"RecoveryPrice\" "; // 追繳價格13
		sql += "      , CS.\"YdClosingPrice\" as \"YdClosingPrice\" "; // 收盤價14
		sql += "      , Case ";
		sql += "          when  NVL(LBM.\"LoanBal\",0)* 100 =0 ";
		sql += "          then 0 ";
		sql += "        else ";
		sql += "        CS.\"YdClosingPrice\" ";
		sql += "        * CS.\"SettingBalance\" ";
		sql += "        / NVL(LBM.\"LoanBal\",0) ";
		sql += "                       end  AS \"TotalMtr\" ";// 全戶維持率15
		sql += "      , CASE ";
		sql += "          WHEN CS.\"YdClosingPrice\" < Case  ";
		sql += "                                       when CS.\"SettingBalance\"  * CS.\"ClMtr\" / 100 =0 ";
		sql += "                                       then 0 ";
		sql += "                                     else ";
		sql += "                                     NVL(LBM.\"LoanBal\",0) ";
		sql += "                                     / CS.\"SettingBalance\" ";
		sql += "                                        * CS.\"ClMtr\" ";
		sql += "                                           / 100 end  ";
		sql += "          THEN '是' ";
		sql += "        ELSE '否' END         AS \"IsClosingPriceLessThanRecoveryPrice\" ";//16
		sql += "    , CM.\"CustName\" AS \"CustName\" "; // 戶名17
		sql += "    , CS.\"StockCode\" AS \"StockCode\" "; // 股票代號18
		sql += "    , NVL(CDS.\"StockItem\",' ') AS \"StockItem\""; // 股票名稱19
		sql += "    , OwnerCM.\"CustName\" AS \"OwnerName\" "; // 擔保品提供人姓名20
		sql += " FROM \"ClFac\" CF ";
		sql += " LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                  , \"FacmNo\" ";
		sql += "                  , SUM(\"LoanBal\") AS \"LoanBal\" ";
		sql += "             FROM \"LoanBorMain\" ";
		sql += "             WHERE \"LoanBal\" > 0 ";
		sql += "             GROUP BY \"CustNo\" ";
		sql += "                    , \"FacmNo\" ";
		sql += "           ) LBM ON LBM.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                AND LBM.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += " LEFT JOIN \"ClStock\" CS ON CS.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CS.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CS.\"ClNo\" = CF.\"ClNo\" ";
		sql += " LEFT JOIN \"CdStock\" CDS ON CDS.\"StockCode\" = CS.\"StockCode\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = CF.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" OwnerCM ON OwnerCM.\"CustUKey\" = CS.\"OwnerCustUKey\" ";
		sql += " WHERE CF.\"ClCode1\" = 3 ";
		sql += "   AND CF.\"CustNo\" != 0 ";
	    sql += "   AND NVL(LBM.\"LoanBal\",0) > 0 ";

		this.info("L9732sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
//		return null;
		return this.convertToMap(query.getResultList());
	}

}