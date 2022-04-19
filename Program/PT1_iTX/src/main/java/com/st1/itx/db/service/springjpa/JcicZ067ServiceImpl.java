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
import com.st1.itx.db.domain.JcicZ067;
import com.st1.itx.db.domain.JcicZ067Id;
import com.st1.itx.db.repository.online.JcicZ067Repository;
import com.st1.itx.db.repository.day.JcicZ067RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ067RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ067RepositoryHist;
import com.st1.itx.db.service.JcicZ067Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ067Service")
@Repository
public class JcicZ067ServiceImpl extends ASpringJpaParm implements JcicZ067Service, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ067Repository jcicZ067Repos;

	@Autowired
	private JcicZ067RepositoryDay jcicZ067ReposDay;

	@Autowired
	private JcicZ067RepositoryMon jcicZ067ReposMon;

	@Autowired
	private JcicZ067RepositoryHist jcicZ067ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ067Repos);
		org.junit.Assert.assertNotNull(jcicZ067ReposDay);
		org.junit.Assert.assertNotNull(jcicZ067ReposMon);
		org.junit.Assert.assertNotNull(jcicZ067ReposHist);
	}

	@Override
	public JcicZ067 findById(JcicZ067Id jcicZ067Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ067Id);
		Optional<JcicZ067> jcicZ067 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ067 = jcicZ067ReposDay.findById(jcicZ067Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ067 = jcicZ067ReposMon.findById(jcicZ067Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ067 = jcicZ067ReposHist.findById(jcicZ067Id);
		else
			jcicZ067 = jcicZ067Repos.findById(jcicZ067Id);
		JcicZ067 obj = jcicZ067.isPresent() ? jcicZ067.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ067> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ067> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate", "PayDate"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ067ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ067ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ067ReposHist.findAll(pageable);
		else
			slice = jcicZ067Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ067> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ067> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ067ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ067ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ067ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ067Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ067> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ067> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ067ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ067ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ067ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ067Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ067> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ067> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ067ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ067ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ067ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ067Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ067 holdById(JcicZ067Id jcicZ067Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ067Id);
		Optional<JcicZ067> jcicZ067 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ067 = jcicZ067ReposDay.findByJcicZ067Id(jcicZ067Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ067 = jcicZ067ReposMon.findByJcicZ067Id(jcicZ067Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ067 = jcicZ067ReposHist.findByJcicZ067Id(jcicZ067Id);
		else
			jcicZ067 = jcicZ067Repos.findByJcicZ067Id(jcicZ067Id);
		return jcicZ067.isPresent() ? jcicZ067.get() : null;
	}

	@Override
	public JcicZ067 holdById(JcicZ067 jcicZ067, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ067.getJcicZ067Id());
		Optional<JcicZ067> jcicZ067T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ067T = jcicZ067ReposDay.findByJcicZ067Id(jcicZ067.getJcicZ067Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ067T = jcicZ067ReposMon.findByJcicZ067Id(jcicZ067.getJcicZ067Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ067T = jcicZ067ReposHist.findByJcicZ067Id(jcicZ067.getJcicZ067Id());
		else
			jcicZ067T = jcicZ067Repos.findByJcicZ067Id(jcicZ067.getJcicZ067Id());
		return jcicZ067T.isPresent() ? jcicZ067T.get() : null;
	}

	@Override
	public JcicZ067 insert(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + jcicZ067.getJcicZ067Id());
		if (this.findById(jcicZ067.getJcicZ067Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ067.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ067ReposDay.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ067ReposMon.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ067ReposHist.saveAndFlush(jcicZ067);
		else
			return jcicZ067Repos.saveAndFlush(jcicZ067);
	}

	@Override
	public JcicZ067 update(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ067.getJcicZ067Id());
		if (!empNot.isEmpty())
			jcicZ067.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ067ReposDay.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ067ReposMon.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ067ReposHist.saveAndFlush(jcicZ067);
		else
			return jcicZ067Repos.saveAndFlush(jcicZ067);
	}

	@Override
	public JcicZ067 update2(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ067.getJcicZ067Id());
		if (!empNot.isEmpty())
			jcicZ067.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ067ReposDay.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onMon))
			jcicZ067ReposMon.saveAndFlush(jcicZ067);
		else if (dbName.equals(ContentName.onHist))
			jcicZ067ReposHist.saveAndFlush(jcicZ067);
		else
			jcicZ067Repos.saveAndFlush(jcicZ067);
		return this.findById(jcicZ067.getJcicZ067Id());
	}

	@Override
	public void delete(JcicZ067 jcicZ067, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ067.getJcicZ067Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ067ReposDay.delete(jcicZ067);
			jcicZ067ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ067ReposMon.delete(jcicZ067);
			jcicZ067ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ067ReposHist.delete(jcicZ067);
			jcicZ067ReposHist.flush();
		} else {
			jcicZ067Repos.delete(jcicZ067);
			jcicZ067Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException {
		if (jcicZ067 == null || jcicZ067.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (JcicZ067 t : jcicZ067)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ067 = jcicZ067ReposDay.saveAll(jcicZ067);
			jcicZ067ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ067 = jcicZ067ReposMon.saveAll(jcicZ067);
			jcicZ067ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ067 = jcicZ067ReposHist.saveAll(jcicZ067);
			jcicZ067ReposHist.flush();
		} else {
			jcicZ067 = jcicZ067Repos.saveAll(jcicZ067);
			jcicZ067Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (jcicZ067 == null || jcicZ067.size() == 0)
			throw new DBException(6);

		for (JcicZ067 t : jcicZ067)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ067 = jcicZ067ReposDay.saveAll(jcicZ067);
			jcicZ067ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ067 = jcicZ067ReposMon.saveAll(jcicZ067);
			jcicZ067ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ067 = jcicZ067ReposHist.saveAll(jcicZ067);
			jcicZ067ReposHist.flush();
		} else {
			jcicZ067 = jcicZ067Repos.saveAll(jcicZ067);
			jcicZ067Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ067> jcicZ067, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ067 == null || jcicZ067.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ067ReposDay.deleteAll(jcicZ067);
			jcicZ067ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ067ReposMon.deleteAll(jcicZ067);
			jcicZ067ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ067ReposHist.deleteAll(jcicZ067);
			jcicZ067ReposHist.flush();
		} else {
			jcicZ067Repos.deleteAll(jcicZ067);
			jcicZ067Repos.flush();
		}
	}

}
