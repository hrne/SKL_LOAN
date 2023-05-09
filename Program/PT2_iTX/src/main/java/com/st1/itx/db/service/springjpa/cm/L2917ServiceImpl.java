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
import com.st1.itx.util.parse.Parse;

@Service("L2917ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L2917ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));

		String sql = " ";
		sql += " select ";
		sql += " S1.\"ClCode1\"				AS F0, "; // 擔保品代號1
		sql += " S1.\"ClCode2\"				AS F1, "; // 擔保品代號2
		sql += " S1.\"ClNo\"				AS F2, "; // 擔保品編號
		sql += " S1.\"BdLocation\"			AS F3, "; // 建物門牌
		sql += " S1.\"LandSeq\"				AS F4, "; // 土地序號
		sql += " S1.\"LandLocation\"		AS F5 "; // 土地坐落
		sql += " FROM ";
		sql += " (select ";
		sql += " cll.\"ClCode1\"				AS \"ClCode1\", "; // 擔保品代號1
		sql += " cll.\"ClCode2\"				AS \"ClCode2\", "; // 擔保品代號2
		sql += " cll.\"ClNo\"					AS \"ClNo\", "; // 擔保品編號
		sql += " MIN(clb.\"BdLocation\")		AS \"BdLocation\", "; // 建物門牌
		sql += " cll.\"LandSeq\"				AS \"LandSeq\", "; // 土地序號
		sql += " MIN(cll.\"LandLocation\")		AS \"LandLocation\" "; // 土地坐落
		sql += " from \"ClFac\"  cf ";
		sql += " LEFT JOIN \"ClLand\" cll ON cll.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                         AND cll.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                          AND cll.\"ClNo\" = cf.\"ClNo\" ";
		sql += " LEFT JOIN \"ClBuilding\" clb ON clb.\"ClCode1\" = cf.\"ClCode1\" ";
		sql += "                         AND clb.\"ClCode2\" = cf.\"ClCode2\" ";
		sql += "                          AND clb.\"ClNo\" = cf.\"ClNo\" ";
		sql += " where ";
		sql += " cf.\"CustNo\" = :custNo ";
		sql += "             AND cll.\"ClCode1\" is not null ";
		sql += "             AND clb.\"ClCode1\" is not null ";
		sql += " GROUP BY cll.\"ClCode1\",cll.\"ClCode2\",cll.\"ClNo\",cll.\"LandSeq\" ";
		sql += " ) S1 ";
		sql += " ORDER BY S1.\"ClCode1\" ,S1.\"ClCode2\" ,S1.\"ClNo\" ,S1.\"LandSeq\" ";

		sql += " " + sqlRow;

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNo", iCustNo);
		this.info("iCustNo = " + iCustNo);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		this.info("index = " + index);
		this.info("limit = " + limit);

		cnt = query.getResultList().size();
		this.info("Total cnt ..." + cnt);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
//		query.setFirstResult(this.index * this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		List<Object> result = query.getResultList();

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}

}