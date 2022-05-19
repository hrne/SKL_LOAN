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
public class L9728ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	
	public List<Map<String, String>> findAll(int custNoStart, int custNoEnd, int findDateStart, int findDateEnd, TitaVo titaVo) {
		this.info("L9728ServiceImpl queryCollection ");

		this.info("L9728ServiceImpl custNoStart = " + custNoStart);
		this.info("L9728ServiceImpl custNoEnd = " + custNoEnd);
		this.info("L9728ServiceImpl findDateStart = " + findDateStart);
		this.info("L9728ServiceImpl findDateEnd = " + findDateEnd);
		
		String sql = "";
		sql += " WITH OutputData AS ( ";
		sql += "     SELECT * ";
		sql += "     FROM \"CustNotice\" ";
		sql += "     WHERE 'N' IN (\"PaperNotice\", \"MsgNotice\", \"EmailNotice\") ";
		sql += "                    ) ";
		sql += " SELECT UNIQUE LPAD(CN.\"CustNo\", 7, '0')                   AS \"CustNo\" ";
		sql += "             , CN.\"FacmNo\"                                 AS \"FacmNo\" ";
		sql += "             , CM.\"CustName\"                               AS \"CustName\" ";
		sql += "             , CECreate.\"Fullname\"                         AS \"CreateName\" ";
		sql += "             , TO_CHAR(CN.\"CreateDate\", 'YYYY') - 1911 || ";
		sql += "               TO_CHAR(CN.\"CreateDate\", '/MM/DD hh:mm:ss') AS \"CreateDate\" ";
		sql += "             , CEUpdate.\"Fullname\"                         AS \"UpdateName\" ";
		sql += "             , TO_CHAR(CN.\"LastUpdate\", 'YYYY') - 1911 || ";
		sql += "               TO_CHAR(CN.\"LastUpdate\", '/MM/DD hh:mm:ss') AS \"UpdateDate\" ";
		sql += " FROM OutputData CN ";
		sql += " LEFT JOIN ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"FormNo\" ";
		sql += "          , ROW_NUMBER() over (PARTITION BY \"CustNo\" ORDER BY \"FormNo\" ASC, \"LastUpdate\" DESC ) \"Seq\" ";
		sql += "     FROM OutputData ";
		sql += "           ) CNPRT ON CNPRT.\"CustNo\" = CN.\"CustNo\" AND CNPRT.\"FacmNo\" = CN.\"FacmNo\" AND CNPRT.\"FormNo\" = CN.\"FormNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = CN.\"CustNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CECreate ON CECreate.\"EmployeeNo\" = CN.\"CreateEmpNo\" ";
		sql += " LEFT JOIN \"CdEmp\" CEUpdate ON CEUpdate.\"EmployeeNo\" = CN.\"LastUpdateEmpNo\" ";
		sql += " WHERE CNPRT.\"Seq\" = 1 ";
		sql += "   AND CNPRT.\"CustNo\" BETWEEN :custNoStart AND :custNoEnd ";
		sql += "   AND TO_CHAR(CN.\"LastUpdate\", 'YYYYMMDD') BETWEEN :findDateStart AND :findDateEnd ";
		sql += " ORDER BY \"CustNo\" ASC ";
		sql += "         ,\"FacmNo\" ASC ";


		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("custNoStart", custNoStart);
		query.setParameter("custNoEnd", custNoEnd);
		query.setParameter("findDateStart", findDateStart);
		query.setParameter("findDateEnd", findDateEnd);
		
		return this.convertToMap(query);
	}

}