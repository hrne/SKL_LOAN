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
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.domain.JcicZ047Id;
import com.st1.itx.db.repository.online.JcicZ047Repository;
import com.st1.itx.db.repository.day.JcicZ047RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ047RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ047RepositoryHist;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ047Service")
@Repository
public class JcicZ047ServiceImpl extends ASpringJpaParm implements JcicZ047Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ047Repository jcicZ047Repos;

	@Autowired
	private JcicZ047RepositoryDay jcicZ047ReposDay;

	@Autowired
	private JcicZ047RepositoryMon jcicZ047ReposMon;

	@Autowired
	private JcicZ047RepositoryHist jcicZ047ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ047Repos);
		org.junit.Assert.assertNotNull(jcicZ047ReposDay);
		org.junit.Assert.assertNotNull(jcicZ047ReposMon);
		org.junit.Assert.assertNotNull(jcicZ047ReposHist);
	}

	@Override
	public JcicZ047 findById(JcicZ047Id jcicZ047Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ047Id);
		Optional<JcicZ047> jcicZ047 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ047 = jcicZ047ReposDay.findById(jcicZ047Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ047 = jcicZ047ReposMon.findById(jcicZ047Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ047 = jcicZ047ReposHist.findById(jcicZ047Id);
		else
			jcicZ047 = jcicZ047Repos.findById(jcicZ047Id);
		JcicZ047 obj = jcicZ047.isPresent() ? jcicZ047.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ047> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ047> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "RcDate"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ047ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ047ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ047ReposHist.findAll(pageable);
		else
			slice = jcicZ047Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ047> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ047> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ047ReposDay.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ047ReposMon.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ047ReposHist.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);
		else
			slice = jcicZ047Repos.findAllByCustIdIsOrderByCustIdAscRcDateDesc(custId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ047> rcDateEq(int rcDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ047> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("rcDateEq " + dbName + " : " + "rcDate_0 : " + rcDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ047ReposDay.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ047ReposMon.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ047ReposHist.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);
		else
			slice = jcicZ047Repos.findAllByRcDateIsOrderByCustIdAscRcDateDesc(rcDate_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ047> custRcEq(String custId_0, int rcDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ047> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("custRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " rcDate_1 : " + rcDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ047ReposDay.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ047ReposMon.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ047ReposHist.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);
		else
			slice = jcicZ047Repos.findAllByCustIdIsAndRcDateIsOrderByCustIdAscRcDateDesc(custId_0, rcDate_1, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ047> otherEq(String submitKey_0, String custId_1, int rcDate_2, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ047> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("otherEq " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " rcDate_2 : " + rcDate_2);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ047ReposDay.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ047ReposMon.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ047ReposHist.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);
		else
			slice = jcicZ047Repos.findAllBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ047 ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ047> jcicZ047T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ047T = jcicZ047ReposDay.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ047T = jcicZ047ReposMon.findTopByUkeyIs(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ047T = jcicZ047ReposHist.findTopByUkeyIs(ukey_0);
		else
			jcicZ047T = jcicZ047Repos.findTopByUkeyIs(ukey_0);

		return jcicZ047T.isPresent() ? jcicZ047T.get() : null;
	}

	@Override
	public JcicZ047 otherFirst(String submitKey_0, String custId_1, int rcDate_2, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("otherFirst " + dbName + " : " + "submitKey_0 : " + submitKey_0 + " custId_1 : " + custId_1 + " rcDate_2 : " + rcDate_2);
		Optional<JcicZ047> jcicZ047T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ047T = jcicZ047ReposDay.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
		else if (dbName.equals(ContentName.onMon))
			jcicZ047T = jcicZ047ReposMon.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
		else if (dbName.equals(ContentName.onHist))
			jcicZ047T = jcicZ047ReposHist.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);
		else
			jcicZ047T = jcicZ047Repos.findTopBySubmitKeyIsAndCustIdIsAndRcDateIsOrderByCreateDateDesc(submitKey_0, custId_1, rcDate_2);

		return jcicZ047T.isPresent() ? jcicZ047T.get() : null;
	}

	@Override
	public JcicZ047 holdById(JcicZ047Id jcicZ047Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ047Id);
		Optional<JcicZ047> jcicZ047 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ047 = jcicZ047ReposDay.findByJcicZ047Id(jcicZ047Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ047 = jcicZ047ReposMon.findByJcicZ047Id(jcicZ047Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ047 = jcicZ047ReposHist.findByJcicZ047Id(jcicZ047Id);
		else
			jcicZ047 = jcicZ047Repos.findByJcicZ047Id(jcicZ047Id);
		return jcicZ047.isPresent() ? jcicZ047.get() : null;
	}

	@Override
	public JcicZ047 holdById(JcicZ047 jcicZ047, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ047.getJcicZ047Id());
		Optional<JcicZ047> jcicZ047T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ047T = jcicZ047ReposDay.findByJcicZ047Id(jcicZ047.getJcicZ047Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ047T = jcicZ047ReposMon.findByJcicZ047Id(jcicZ047.getJcicZ047Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ047T = jcicZ047ReposHist.findByJcicZ047Id(jcicZ047.getJcicZ047Id());
		else
			jcicZ047T = jcicZ047Repos.findByJcicZ047Id(jcicZ047.getJcicZ047Id());
		return jcicZ047T.isPresent() ? jcicZ047T.get() : null;
	}

	@Override
	public JcicZ047 insert(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ047.getJcicZ047Id());
		if (this.findById(jcicZ047.getJcicZ047Id(), titaVo) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ047.setCreateEmpNo(empNot);

		if (jcicZ047.getLastUpdateEmpNo() == null || jcicZ047.getLastUpdateEmpNo().isEmpty())
			jcicZ047.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ047ReposDay.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ047ReposMon.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ047ReposHist.saveAndFlush(jcicZ047);
		else
			return jcicZ047Repos.saveAndFlush(jcicZ047);
	}

	@Override
	public JcicZ047 update(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ047.getJcicZ047Id());
		if (!empNot.isEmpty())
			jcicZ047.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ047ReposDay.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ047ReposMon.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ047ReposHist.saveAndFlush(jcicZ047);
		else
			return jcicZ047Repos.saveAndFlush(jcicZ047);
	}

	@Override
	public JcicZ047 update2(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ047.getJcicZ047Id());
		if (!empNot.isEmpty())
			jcicZ047.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ047ReposDay.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onMon))
			jcicZ047ReposMon.saveAndFlush(jcicZ047);
		else if (dbName.equals(ContentName.onHist))
			jcicZ047ReposHist.saveAndFlush(jcicZ047);
		else
			jcicZ047Repos.saveAndFlush(jcicZ047);
		return this.findById(jcicZ047.getJcicZ047Id());
	}

	@Override
	public void delete(JcicZ047 jcicZ047, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ047.getJcicZ047Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ047ReposDay.delete(jcicZ047);
			jcicZ047ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ047ReposMon.delete(jcicZ047);
			jcicZ047ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ047ReposHist.delete(jcicZ047);
			jcicZ047ReposHist.flush();
		} else {
			jcicZ047Repos.delete(jcicZ047);
			jcicZ047Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException {
		if (jcicZ047 == null || jcicZ047.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("InsertAll...");
		for (JcicZ047 t : jcicZ047) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ047 = jcicZ047ReposDay.saveAll(jcicZ047);
			jcicZ047ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ047 = jcicZ047ReposMon.saveAll(jcicZ047);
			jcicZ047ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ047 = jcicZ047ReposHist.saveAll(jcicZ047);
			jcicZ047ReposHist.flush();
		} else {
			jcicZ047 = jcicZ047Repos.saveAll(jcicZ047);
			jcicZ047Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ047 == null || jcicZ047.size() == 0)
			throw new DBException(6);

		for (JcicZ047 t : jcicZ047)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ047 = jcicZ047ReposDay.saveAll(jcicZ047);
			jcicZ047ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ047 = jcicZ047ReposMon.saveAll(jcicZ047);
			jcicZ047ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ047 = jcicZ047ReposHist.saveAll(jcicZ047);
			jcicZ047ReposHist.flush();
		} else {
			jcicZ047 = jcicZ047Repos.saveAll(jcicZ047);
			jcicZ047Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ047> jcicZ047, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ047 == null || jcicZ047.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ047ReposDay.deleteAll(jcicZ047);
			jcicZ047ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ047ReposMon.deleteAll(jcicZ047);
			jcicZ047ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ047ReposHist.deleteAll(jcicZ047);
			jcicZ047ReposHist.flush();
		} else {
			jcicZ047Repos.deleteAll(jcicZ047);
			jcicZ047Repos.flush();
		}
	}

}
