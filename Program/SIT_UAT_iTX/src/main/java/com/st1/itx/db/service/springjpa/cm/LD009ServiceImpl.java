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

@Service
@Repository
public class LD009ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("ld009.findAll ");

		String sql = " ";
		sql += " SELECT am.\"AcctCode\" AS F0 ";
		sql += "       ,CdC.\"Item\" AS F1 ";
		sql += "       ,am.\"AcSubBookCode\" AS F2 ";
		sql += "       ,CdCb.\"Item\" AS F3 ";
		sql += "       ,  aac.\"TdCnt\" ";
		sql += "        - aac.\"TdNewCnt\" ";
		sql += "        + aac.\"TdClsCnt\" ";
		sql += "        - aac.\"TdExtCnt\" AS F4 "; // 展出展入未完全
		sql += "       ,aac.\"TdNewCnt\" AS F5 ";
		sql += "       ,aac.\"TdClsCnt\" AS F6 ";
		sql += "       ,aac.\"TdExtCnt\" AS F7 "; // 展入件數
		sql += "       ,0 AS F8 "; // 展出件數; 展出的定義是什麼? LA$LDGP有兩個展期件數, LDGETC and LDGEIC, 哪一個是展出?
		sql += "       ,aac.\"TdNewCnt\" + aac.\"TdExtCnt\" - aac.\"TdClsCnt\" AS F9 "; // 展出展入未完全
		sql += "       ,aac.\"TdCnt\" AS F10 ";
		sql += "       ,am.\"YdBal\" AS F11 ";
		sql += "       ,am.\"DbAmt\" AS F12 ";
		sql += "       ,am.\"CrAmt\" AS F13 ";
		sql += "       ,am.\"DbAmt\" - am.\"CrAmt\" AS F14 ";
		sql += "       ,am.\"TdBal\" AS F15 ";
		sql += "       ,aac.\"TdExtAmt\" AS F16 ";
		sql += " FROM \"AcMain\" am ";
		sql += " LEFT JOIN \"AcAcctCheck\" aac ON aac.\"AcDate\" = am.\"AcDate\" ";
		sql += "                            AND NVL(aac.\"AcctCode\", ' ') = am.\"AcctCode\" ";
		sql += "                            AND NVL(aac.\"AcSubBookCode\", ' ') = am.\"AcSubBookCode\" ";
		sql += "                            AND aac.\"BranchNo\" = am.\"BranchNo\" ";
		sql += "                            AND aac.\"CurrencyCode\" = am.\"CurrencyCode\" ";
		sql += " LEFT JOIN \"CdCode\" CdC ON CdC.\"DefCode\" = 'AcctCode' ";
		sql += "                       AND CdC.\"Code\" = am.\"AcctCode\" ";
		sql += " LEFT JOIN \"CdCode\" CdCb ON CdCb.\"DefCode\" = 'AcSubBookCode' ";
		sql += "                        AND CdCb.\"Code\" = am.\"AcSubBookCode\" ";
		sql += " WHERE am.\"AcctCode\" BETWEEN '310' AND '390' ";
		sql += "   AND am.\"AcDate\" = :today ";
		sql += " ORDER BY am.\"AcctCode\" ";
		sql += "         ,am.\"AcSubBookCode\" ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("today", titaVo.getEntDyI() + 19110000);

		return this.convertToMap(query);
	}

}