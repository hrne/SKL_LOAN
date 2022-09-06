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
import com.st1.itx.util.parse.Parse;

@Service
@Repository

public class L2614ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	// *** 折返控制相關 ***
	private int limit;

	// *** 折返控制相關 ***
	private int cnt;

	// *** 折返控制相關 ***
	private int size;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {

		int iOverdueDate = parse.stringToInteger(titaVo.getParam("OverdueDate")) + 19110000;

		this.info("L2614ServiceImpl.findAll ");
		String sql = "     SELECT ";
		sql += "    ad.\"TitaTxtNo\"   AS \"TitaTxtNo\",  ";
		sql += "    ad.\"SlipNo\"      AS \"SlipNo\",     ";
		sql += "    ad.\"AcNoCode\"    AS \"AcNoCode\",   ";
		sql += "    ad.\"AcSubCode\"   AS \"AcSubCode\",  ";
		sql += "    ad.\"AcDtlCode\"   AS \"AcDtlCode\",  ";
		sql += "    cc.\"AcNoItem\"    AS \"AcNoItem\",   ";
		sql += "    CASE  WHEN ad.\"DbCr\" = 'D' THEN  ad.\"TxAmt\"    ";
		sql += "          ELSE  0  END AS \"DTxAmt\",     ";
		sql += "    CASE  WHEN ad.\"DbCr\" = 'D' THEN 0   ";
		sql += "          ELSE ad.\"TxAmt\" END AS \"CTxAmt\",";
		sql += "    ad.\"CustNo\"      AS \"CustNo\",     ";
		sql += "    cm.\"CustName\"    AS \"CustName\"    ";
		sql += "    FROM ";
		sql += "      \"ForeclosureFee\"   ff";
		sql += "    LEFT JOIN \"AcDetail\"         ad ON ad.\"AcctCode\" = 'F24'";
		sql += "                               AND ad.\"CustNo\" = ff.\"CustNo\"";
		sql += "                               AND ad.\"RvNo\" = lpad(ff.\"RecordNo\", 7, '0')";
		sql += "    LEFT JOIN \"CdAcCode\"         cc ON cc.\"AcNoCode\" = ad.\"AcNoCode\"";
		sql += "                               AND cc.\"AcSubCode\" = ad.\"AcSubCode\"";
		sql += "                               AND cc.\"AcDtlCode\" = ad.\"AcDtlCode\"";
		sql += "    LEFT JOIN \"CustMain\"         cm ON cm.\"CustNo\" = ad.\"CustNo\"";
		sql += "    WHERE";
		sql += "    ff.\"OverdueDate\" >= :ioverduedate";
		sql += "    AND ff.\"OverdueDate\" <= :ioverduedate";
		sql += "    AND ad.\"AcctCode\" is not null";
		sql += "    ORDER BY";
		sql += "    ad.\"TitaTxtNo\",";
		sql += "    ad.\"SlipNo\"";

		sql += " " + sqlRow;
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ioverduedate", iOverdueDate);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

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

		size = result.size();
		this.info("Total size ..." + size);

		return this.convertToMap(query);
	}

	public int getSize() {
		return cnt;
	}

}