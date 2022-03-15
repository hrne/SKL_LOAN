package com.st1.itx.db.service.springjpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ066;
import com.st1.itx.db.domain.JcicZ066Id;
import com.st1.itx.db.repository.online.JcicZ066Repository;
import com.st1.itx.db.repository.day.JcicZ066RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ066RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ066RepositoryHist;
import com.st1.itx.db.service.JcicZ066Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ066Service")
@Repository
public class JcicZ066ServiceImpl extends ASpringJpaParm implements JcicZ066Service, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ066Repository jcicZ066Repos;

	@Autowired
	private JcicZ066RepositoryDay jcicZ066ReposDay;

	@Autowired
	private JcicZ066RepositoryMon jcicZ066ReposMon;

	@Autowired
	private JcicZ066RepositoryHist jcicZ066ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ066Repos);
		org.junit.Assert.assertNotNull(jcicZ066ReposDay);
		org.junit.Assert.assertNotNull(jcicZ066ReposMon);
		org.junit.Assert.assertNotNull(jcicZ066ReposHist);
	}

	@Override
	public JcicZ066 findById(JcicZ066Id jcicZ066Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ066Id);
		Optional<JcicZ066> jcicZ066 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ066 = jcicZ066ReposDay.findById(jcicZ066Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ066 = jcicZ066ReposMon.findById(jcicZ066Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ066 = jcicZ066ReposHist.findById(jcicZ066Id);
		else
			jcicZ066 = jcicZ066Repos.findById(jcicZ066Id);
		JcicZ066 obj = jcicZ066.isPresent() ? jcicZ066.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ066> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ066> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate", "BankId"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ066ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ066ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ066ReposHist.findAll(pageable);
		else
			slice = jcicZ066Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ066> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ066> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ066ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ066ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ066ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ066Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ066> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ066> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ066ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ066ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ066ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ066Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ066> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ066> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ066ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ066ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ066ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ066Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ066 holdById(JcicZ066Id jcicZ066Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ066Id);
		Optional<JcicZ066> jcicZ066 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ066 = jcicZ066ReposDay.findByJcicZ066Id(jcicZ066Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ066 = jcicZ066ReposMon.findByJcicZ066Id(jcicZ066Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ066 = jcicZ066ReposHist.findByJcicZ066Id(jcicZ066Id);
		else
			jcicZ066 = jcicZ066Repos.findByJcicZ066Id(jcicZ066Id);
		return jcicZ066.isPresent() ? jcicZ066.get() : null;
	}

	@Override
	public JcicZ066 holdById(JcicZ066 jcicZ066, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ066.getJcicZ066Id());
		Optional<JcicZ066> jcicZ066T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ066T = jcicZ066ReposDay.findByJcicZ066Id(jcicZ066.getJcicZ066Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ066T = jcicZ066ReposMon.findByJcicZ066Id(jcicZ066.getJcicZ066Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ066T = jcicZ066ReposHist.findByJcicZ066Id(jcicZ066.getJcicZ066Id());
		else
			jcicZ066T = jcicZ066Repos.findByJcicZ066Id(jcicZ066.getJcicZ066Id());
		return jcicZ066T.isPresent() ? jcicZ066T.get() : null;
	}

	@Override
	public JcicZ066 insert(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + jcicZ066.getJcicZ066Id());
		if (this.findById(jcicZ066.getJcicZ066Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ066.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ066ReposDay.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ066ReposMon.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ066ReposHist.saveAndFlush(jcicZ066);
		else
			return jcicZ066Repos.saveAndFlush(jcicZ066);
	}

	@Override
	public JcicZ066 update(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ066.getJcicZ066Id());
		if (!empNot.isEmpty())
			jcicZ066.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ066ReposDay.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ066ReposMon.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ066ReposHist.saveAndFlush(jcicZ066);
		else
			return jcicZ066Repos.saveAndFlush(jcicZ066);
	}

	@Override
	public JcicZ066 update2(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ066.getJcicZ066Id());
		if (!empNot.isEmpty())
			jcicZ066.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ066ReposDay.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onMon))
			jcicZ066ReposMon.saveAndFlush(jcicZ066);
		else if (dbName.equals(ContentName.onHist))
			jcicZ066ReposHist.saveAndFlush(jcicZ066);
		else
			jcicZ066Repos.saveAndFlush(jcicZ066);
		return this.findById(jcicZ066.getJcicZ066Id());
	}

	@Override
	public void delete(JcicZ066 jcicZ066, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ066.getJcicZ066Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ066ReposDay.delete(jcicZ066);
			jcicZ066ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ066ReposMon.delete(jcicZ066);
			jcicZ066ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ066ReposHist.delete(jcicZ066);
			jcicZ066ReposHist.flush();
		} else {
			jcicZ066Repos.delete(jcicZ066);
			jcicZ066Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException {
		if (jcicZ066 == null || jcicZ066.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (JcicZ066 t : jcicZ066)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ066 = jcicZ066ReposDay.saveAll(jcicZ066);
			jcicZ066ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ066 = jcicZ066ReposMon.saveAll(jcicZ066);
			jcicZ066ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ066 = jcicZ066ReposHist.saveAll(jcicZ066);
			jcicZ066ReposHist.flush();
		} else {
			jcicZ066 = jcicZ066Repos.saveAll(jcicZ066);
			jcicZ066Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (jcicZ066 == null || jcicZ066.size() == 0)
			throw new DBException(6);

		for (JcicZ066 t : jcicZ066)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ066 = jcicZ066ReposDay.saveAll(jcicZ066);
			jcicZ066ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ066 = jcicZ066ReposMon.saveAll(jcicZ066);
			jcicZ066ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ066 = jcicZ066ReposHist.saveAll(jcicZ066);
			jcicZ066ReposHist.flush();
		} else {
			jcicZ066 = jcicZ066Repos.saveAll(jcicZ066);
			jcicZ066Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ066> jcicZ066, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ066 == null || jcicZ066.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ066ReposDay.deleteAll(jcicZ066);
			jcicZ066ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ066ReposMon.deleteAll(jcicZ066);
			jcicZ066ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ066ReposHist.deleteAll(jcicZ066);
			jcicZ066ReposHist.flush();
		} else {
			jcicZ066Repos.deleteAll(jcicZ066);
			jcicZ066Repos.flush();
		}
	}

}
