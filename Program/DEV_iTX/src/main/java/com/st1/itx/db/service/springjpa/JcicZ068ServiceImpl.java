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
import com.st1.itx.db.domain.JcicZ068;
import com.st1.itx.db.domain.JcicZ068Id;
import com.st1.itx.db.repository.online.JcicZ068Repository;
import com.st1.itx.db.repository.day.JcicZ068RepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ068RepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ068RepositoryHist;
import com.st1.itx.db.service.JcicZ068Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ068Service")
@Repository
public class JcicZ068ServiceImpl extends ASpringJpaParm implements JcicZ068Service, InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ068Repository jcicZ068Repos;

	@Autowired
	private JcicZ068RepositoryDay jcicZ068ReposDay;

	@Autowired
	private JcicZ068RepositoryMon jcicZ068ReposMon;

	@Autowired
	private JcicZ068RepositoryHist jcicZ068ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ068Repos);
		org.junit.Assert.assertNotNull(jcicZ068ReposDay);
		org.junit.Assert.assertNotNull(jcicZ068ReposMon);
		org.junit.Assert.assertNotNull(jcicZ068ReposHist);
	}

	@Override
	public JcicZ068 findById(JcicZ068Id jcicZ068Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ068Id);
		Optional<JcicZ068> jcicZ068 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ068 = jcicZ068ReposDay.findById(jcicZ068Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ068 = jcicZ068ReposMon.findById(jcicZ068Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ068 = jcicZ068ReposHist.findById(jcicZ068Id);
		else
			jcicZ068 = jcicZ068Repos.findById(jcicZ068Id);
		JcicZ068 obj = jcicZ068.isPresent() ? jcicZ068.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ068> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ068> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "SubmitKey", "CustId", "ApplyDate"));
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ068ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ068ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ068ReposHist.findAll(pageable);
		else
			slice = jcicZ068Repos.findAll(pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ068> CustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ068> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ068ReposDay.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ068ReposMon.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ068ReposHist.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);
		else
			slice = jcicZ068Repos.findAllByCustIdIsOrderByCustIdAscApplyDateDesc(custId_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ068> RcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ068> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("RcDateEq " + dbName + " : " + "applyDate_0 : " + applyDate_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ068ReposDay.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ068ReposMon.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ068ReposHist.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);
		else
			slice = jcicZ068Repos.findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(applyDate_0, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<JcicZ068> CustRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ068> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = PageRequest.of(index, limit);
		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		this.info("CustRcEq " + dbName + " : " + "custId_0 : " + custId_0 + " applyDate_1 : " + applyDate_1);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ068ReposDay.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ068ReposMon.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ068ReposHist.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);
		else
			slice = jcicZ068Repos.findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(custId_0, applyDate_1, pageable);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ068 holdById(JcicZ068Id jcicZ068Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ068Id);
		Optional<JcicZ068> jcicZ068 = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ068 = jcicZ068ReposDay.findByJcicZ068Id(jcicZ068Id);
		else if (dbName.equals(ContentName.onMon))
			jcicZ068 = jcicZ068ReposMon.findByJcicZ068Id(jcicZ068Id);
		else if (dbName.equals(ContentName.onHist))
			jcicZ068 = jcicZ068ReposHist.findByJcicZ068Id(jcicZ068Id);
		else
			jcicZ068 = jcicZ068Repos.findByJcicZ068Id(jcicZ068Id);
		return jcicZ068.isPresent() ? jcicZ068.get() : null;
	}

	@Override
	public JcicZ068 holdById(JcicZ068 jcicZ068, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ068.getJcicZ068Id());
		Optional<JcicZ068> jcicZ068T = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ068T = jcicZ068ReposDay.findByJcicZ068Id(jcicZ068.getJcicZ068Id());
		else if (dbName.equals(ContentName.onMon))
			jcicZ068T = jcicZ068ReposMon.findByJcicZ068Id(jcicZ068.getJcicZ068Id());
		else if (dbName.equals(ContentName.onHist))
			jcicZ068T = jcicZ068ReposHist.findByJcicZ068Id(jcicZ068.getJcicZ068Id());
		else
			jcicZ068T = jcicZ068Repos.findByJcicZ068Id(jcicZ068.getJcicZ068Id());
		return jcicZ068T.isPresent() ? jcicZ068T.get() : null;
	}

	@Override
	public JcicZ068 insert(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Insert..." + dbName + " " + jcicZ068.getJcicZ068Id());
		if (this.findById(jcicZ068.getJcicZ068Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ068.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ068ReposDay.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ068ReposMon.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ068ReposHist.saveAndFlush(jcicZ068);
		else
			return jcicZ068Repos.saveAndFlush(jcicZ068);
	}

	@Override
	public JcicZ068 update(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ068.getJcicZ068Id());
		if (!empNot.isEmpty())
			jcicZ068.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ068ReposDay.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ068ReposMon.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ068ReposHist.saveAndFlush(jcicZ068);
		else
			return jcicZ068Repos.saveAndFlush(jcicZ068);
	}

	@Override
	public JcicZ068 update2(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("Update..." + dbName + " " + jcicZ068.getJcicZ068Id());
		if (!empNot.isEmpty())
			jcicZ068.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ068ReposDay.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onMon))
			jcicZ068ReposMon.saveAndFlush(jcicZ068);
		else if (dbName.equals(ContentName.onHist))
			jcicZ068ReposHist.saveAndFlush(jcicZ068);
		else
			jcicZ068Repos.saveAndFlush(jcicZ068);
		return this.findById(jcicZ068.getJcicZ068Id());
	}

	@Override
	public void delete(JcicZ068 jcicZ068, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ068.getJcicZ068Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ068ReposDay.delete(jcicZ068);
			jcicZ068ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ068ReposMon.delete(jcicZ068);
			jcicZ068ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ068ReposHist.delete(jcicZ068);
			jcicZ068ReposHist.flush();
		} else {
			jcicZ068Repos.delete(jcicZ068);
			jcicZ068Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException {
		if (jcicZ068 == null || jcicZ068.size() == 0)
			throw new DBException(6);
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("InsertAll...");
		for (JcicZ068 t : jcicZ068)
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ068 = jcicZ068ReposDay.saveAll(jcicZ068);
			jcicZ068ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ068 = jcicZ068ReposMon.saveAll(jcicZ068);
			jcicZ068ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ068 = jcicZ068ReposHist.saveAll(jcicZ068);
			jcicZ068ReposHist.flush();
		} else {
			jcicZ068 = jcicZ068Repos.saveAll(jcicZ068);
			jcicZ068Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		}
		this.info("UpdateAll...");
		if (jcicZ068 == null || jcicZ068.size() == 0)
			throw new DBException(6);

		for (JcicZ068 t : jcicZ068)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ068 = jcicZ068ReposDay.saveAll(jcicZ068);
			jcicZ068ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ068 = jcicZ068ReposMon.saveAll(jcicZ068);
			jcicZ068ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ068 = jcicZ068ReposHist.saveAll(jcicZ068);
			jcicZ068ReposHist.flush();
		} else {
			jcicZ068 = jcicZ068Repos.saveAll(jcicZ068);
			jcicZ068Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ068> jcicZ068, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ068 == null || jcicZ068.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ068ReposDay.deleteAll(jcicZ068);
			jcicZ068ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ068ReposMon.deleteAll(jcicZ068);
			jcicZ068ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ068ReposHist.deleteAll(jcicZ068);
			jcicZ068ReposHist.flush();
		} else {
			jcicZ068Repos.deleteAll(jcicZ068);
			jcicZ068Repos.flush();
		}
	}

}
