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
import com.st1.itx.db.domain.JcicZ065;
import com.st1.itx.db.domain.JcicZ065Id;
import com.st1.itx.db.repository.online.JcicZ065Repository;
import com.st1.itx.db.repository.day.JcicZ065RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ065RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ065RepositoryHist;
import com.st1.itx.db.service.JcicZ065Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ065Service")
@Repository
public class JcicZ065ServiceImpl extends ASpringJpaParm implements JcicZ065Service, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ065Repository jcicZ065Repos;

	@Autowired
	private JcicZ065RepositoryDay jcicZ065ReposDay;

	@Autowired
	private JcicZ065RepositoryMon jcicZ065ReposMon;

	@Autowired
	private JcicZ065RepositoryHist jcicZ065ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ065Repos);
		org.junit.Assert.assertNotNull(jcicZ065ReposDay);
		org.junit.Assert.assertNotNull(jcicZ065ReposMon);
		org.junit.Assert.assertNotNull(jcicZ065ReposHist);
	}

	@Override
	public JcicZ065 findById(JcicZ065Id jcicZ065Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ065Id);
		Optional<JcicZ065> jcicZ065 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ065 = jcicZ065ReposDay.findById(jcicZ065Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ065 = jcicZ065ReposMon.findById(jcicZ065Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ065 = jcicZ065ReposHist.findById(jcicZ065Id);
		else
			jcicZ065 = jcicZ065Repos.findById(jcicZ065Id);
		JcicZ065 obj = jcicZ065.isPresent() ? jcicZ065.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ065> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ065> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "BankId"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ065ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ065ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ065ReposHist.findAll(pageable);
		else
			slice = jcicZ065Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ065> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ065> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ065ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ065ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ065ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ065Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ065> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ065> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ065ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ065ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ065ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ065Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ065> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ065> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ065ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ065ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ065ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ065Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ065 holdById(JcicZ065Id jcicZ065Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ065Id);
		Optional<JcicZ065> jcicZ065 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ065 = jcicZ065ReposDay.findByJcicZ065Id(jcicZ065Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ065 = jcicZ065ReposMon.findByJcicZ065Id(jcicZ065Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ065 = jcicZ065ReposHist.findByJcicZ065Id(jcicZ065Id);
		else
			jcicZ065 = jcicZ065Repos.findByJcicZ065Id(jcicZ065Id);
		return jcicZ065.isPresent() ? jcicZ065.get() : null;
	}

	@Override
	public JcicZ065 holdById(JcicZ065 jcicZ065, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ065.getJcicZ065Id());
		Optional<JcicZ065> jcicZ065T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ065T = jcicZ065ReposDay.findByJcicZ065Id(jcicZ065.getJcicZ065Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ065T = jcicZ065ReposMon.findByJcicZ065Id(jcicZ065.getJcicZ065Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ065T = jcicZ065ReposHist.findByJcicZ065Id(jcicZ065.getJcicZ065Id());
		else
			jcicZ065T = jcicZ065Repos.findByJcicZ065Id(jcicZ065.getJcicZ065Id());
		return jcicZ065T.isPresent() ? jcicZ065T.get() : null;
	}

	@Override
	public JcicZ065 insert(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + jcicZ065.getJcicZ065Id());
		if (this.findById(jcicZ065.getJcicZ065Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ065.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ065ReposDay.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ065ReposMon.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ065ReposHist.saveAndFlush(jcicZ065);
		else
			return jcicZ065Repos.saveAndFlush(jcicZ065);
	}

	@Override
	public JcicZ065 update(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ065.getJcicZ065Id());
		if (!empNot.isEmpty())
			jcicZ065.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ065ReposDay.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ065ReposMon.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ065ReposHist.saveAndFlush(jcicZ065);
		else
			return jcicZ065Repos.saveAndFlush(jcicZ065);
	}

	@Override
	public JcicZ065 update2(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ065.getJcicZ065Id());
		if (!empNot.isEmpty())
			jcicZ065.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ065ReposDay.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onMon))
			jcicZ065ReposMon.saveAndFlush(jcicZ065);
		else if (dbName.equals(ContentName.onHist))
			jcicZ065ReposHist.saveAndFlush(jcicZ065);
		else
			jcicZ065Repos.saveAndFlush(jcicZ065);
		return this.findById(jcicZ065.getJcicZ065Id());
	}

	@Override
	public void delete(JcicZ065 jcicZ065, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ065.getJcicZ065Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ065ReposDay.delete(jcicZ065);
			jcicZ065ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ065ReposMon.delete(jcicZ065);
			jcicZ065ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ065ReposHist.delete(jcicZ065);
			jcicZ065ReposHist.flush();
		} else {
			jcicZ065Repos.delete(jcicZ065);
			jcicZ065Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException {
		if (jcicZ065 == null || jcicZ065.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (JcicZ065 t : jcicZ065)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ065 = jcicZ065ReposDay.saveAll(jcicZ065);
			jcicZ065ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ065 = jcicZ065ReposMon.saveAll(jcicZ065);
			jcicZ065ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ065 = jcicZ065ReposHist.saveAll(jcicZ065);
			jcicZ065ReposHist.flush();
		} else {
			jcicZ065 = jcicZ065Repos.saveAll(jcicZ065);
			jcicZ065Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (jcicZ065 == null || jcicZ065.size() == 0)
			throw new DBException(6);

		for (JcicZ065 t : jcicZ065)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ065 = jcicZ065ReposDay.saveAll(jcicZ065);
			jcicZ065ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ065 = jcicZ065ReposMon.saveAll(jcicZ065);
			jcicZ065ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ065 = jcicZ065ReposHist.saveAll(jcicZ065);
			jcicZ065ReposHist.flush();
		} else {
			jcicZ065 = jcicZ065Repos.saveAll(jcicZ065);
			jcicZ065Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ065> jcicZ065, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ065 == null || jcicZ065.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ065ReposDay.deleteAll(jcicZ065);
			jcicZ065ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ065ReposMon.deleteAll(jcicZ065);
			jcicZ065ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ065ReposHist.deleteAll(jcicZ065);
			jcicZ065ReposHist.flush();
		} else {
			jcicZ065Repos.deleteAll(jcicZ065);
			jcicZ065Repos.flush();
		}
	}

}
