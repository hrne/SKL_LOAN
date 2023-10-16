package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("L8102ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L8102ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(String iReviewType, TitaVo titaVo) throws LogicException {

		this.info("L8102");

		String sql = " select  ";
		sql += "      A.\"Code\"  as \"Code\",    ";
		sql += "      A.\"Item\"  as \"Item\",   ";
		sql += "      B.\"Email\"  as \"Email\"  ";
		sql += "     from \"CdCode\" a " ;
		sql += "     left join \"CdEmp\" b on a.\"Item\" = b.\"EmployeeNo\" ";
		sql += "     where  A.\"DefType\" = '08'  ";
		sql += "   	   and A.\"DefCode\" = 'HMLCode'  ";
		sql += "       and A.\"Code\" like  :ReviewType ";
		sql += "    group by A.\"Code\", A.\"Item\" , B.\"Email\"    "; 

	this.info("L8102sql="+sql);

	Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
	EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);query=em.createNativeQuery(sql);

	if(!"".equals(iReviewType))
	{
		query.setParameter("ReviewType","%" + iReviewType + "%");
	}

	return this.convertToMap(query);
}

}