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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("lB680ServiceImpl")
@Repository

/*
 * LB680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
 */

public class LB680ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LB680.findAll ---------------");
		this.info("-----LB680 TitaVo=" + titaVo);
		this.info("-----LB680 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

		// TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// LB680 「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔
		sql = "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\"" + "     , M.\"CustId\""
				+ "     , M.\"CustIdErr\"" + "     , M.\"Filler6\"" + "     , M.\"Amt\"" + " , M.\"JcicDataYM\""
				+ "     , M.\"Filler9\"" + " FROM  \"JcicB680\" M" + " WHERE M.\"DataYM\" = :dateMonth "
				+ " ORDER BY M.\"CustId\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LB680.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);
		query.setParameter("dateMonth", dateMonth); 

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query);
	}
}
