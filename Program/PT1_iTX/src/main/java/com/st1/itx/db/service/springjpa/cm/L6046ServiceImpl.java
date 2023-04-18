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
import com.st1.itx.util.parse.Parse;

@Service("L6046ServiceImpl")
@Repository
public class L6046ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {

		this.info("L6046.findAll");
		String iCustNo = titaVo.getParam("CustNo");
		


		String sql = " Select ";
		sql += "    A.\"CustUKey\" ";
		sql += "  , A.\"CustNo\" AS \"CustNo\"  ";
		sql += "  , B.\"XXCustId\"  AS \"CustId\" ";
		sql += "  , B.\"CustId\" AS \"XXCustId\" ";
		sql += "  , A.\"CustName\" AS \"CustName\" ";
		sql += "  , B.\"CustName\" AS \"CustName1\" ";
		sql += " from \"CustMain\" A ";
        sql += " left join \"CustDataCtrl\" B on A.\"CustUKey\" = B.\"CustUKey\" ";
        sql += " where  ";
        sql += "  A.\"CustNo\" is not null and";
        sql += "  B.\"CustNo\" is not null  ";
//        sql += "  B.\"CustNo\" is not null and ";
//        sql += "  A.\"CustId\" is not null and ";
//        sql += "  B.\"CustId\" is not null ";
        
		if (iCustNo != null ) {
			sql += " AND  A.\"CustNo\" =  :iCustNo     ";
		}
		

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		if (iCustNo != null ) {
			query.setParameter("iCustNo", iCustNo);
		}

		return this.convertToMap(query);
	}

}