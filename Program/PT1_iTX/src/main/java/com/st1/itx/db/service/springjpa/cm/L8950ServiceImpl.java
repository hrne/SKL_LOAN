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
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.TbJcicMu01Service;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("L8950ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L8950ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(String iEmpId, int iDataDate, TitaVo titaVo) throws LogicException {

		this.info("L8950.findAll");
		this.info("titaVo date  = " + iDataDate);
		this.info("titaVo empid = " + iEmpId);

		String sql = " select  ";
		sql += "      a.\"HeadOfficeCode\" as \"HeadOfficeCode\" ,  ";
		sql += "      b.\"BankItem\" as \"HeadOfficeCodeX\" ,  ";
		sql += "      a.\"BranchCode\" as \"BranchCode\" ,  ";
		sql += "      a.\"DataDate\" as \"DataDate\" , ";
		sql += "      a.\"EmpId\" as \"EmpId\" , ";
		sql += "      c.\"Fullname\" as \"EmpName\" , ";
		sql += "      a.\"OutJcictxtDate\" as \"OutJcictxtDate\" , ";
		sql += "      a.\"LastUpdate\" as \"LastUpdate\" ,  ";
		sql += "      a.\"CreateDate\" as \"CreateDate\",  ";
		sql += "      a.\"LastUpdateEmpNo\" as \"LastUpdateEmpNo\",  ";
		sql += "      d.\"Fullname\" as \"LastUpdateEmpName\"   ";
		sql += "     from \"TbJcicMu01\" a";
		sql += "     left join \"CdBank\" b on a.\"HeadOfficeCode\" = b.\"BankCode\" and b.\"BranchCode\"='    ' ";
		sql += "     left join \"CdEmp\" c on a.\"EmpId\" = c.\"EmployeeNo\" ";
		sql += "     LEFT JOIN \"CdEmp\" d ON A.\"LastUpdateEmpNo\" = D.\"EmployeeNo\" ";
		if (!"".equals(iEmpId) || iDataDate > 19110000) {
			sql += "  where  ";
		}
		if (!"".equals(iEmpId)) {
			sql += "   a.\"EmpId\" =  :EmpId";
		}
		if (iDataDate > 19110000) {
			if(!"".equals(iEmpId)) {
				sql += " and";
			}
			sql += "  a.\"DataDate\" = :DataDate";
		}
		sql+="     order by a.\"LastUpdate\"";

	this.info("sql="+sql);

	Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
	EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);query=em.createNativeQuery(sql);

	if(!"".equals(iEmpId))
	{
		query.setParameter("EmpId", iEmpId);
	}if(iDataDate>19110000)
	{
		query.setParameter("DataDate", iDataDate);
	}
	return this.convertToMap(query);
}

}