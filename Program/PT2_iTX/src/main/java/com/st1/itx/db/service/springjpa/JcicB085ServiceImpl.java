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
import com.st1.itx.db.domain.JcicB085;
import com.st1.itx.db.domain.JcicB085Id;
import com.st1.itx.db.repository.online.JcicB085Repository;
import com.st1.itx.db.repository.day.JcicB085RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB085RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB085RepositoryHist;
import com.st1.itx.db.service.JcicB085Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB085Service")
@Repository
public class JcicB085ServiceImpl extends ASpringJpaParm implements JcicB085Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicB085Repository jcicB085Repos;

	@Autowired
	private JcicB085RepositoryDay jcicB085ReposDay;

	@Autowired
	private JcicB085RepositoryMon jcicB085ReposMon;

	@Autowired
	private JcicB085RepositoryHist jcicB085ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicB085Repos);
		org.junit.Assert.assertNotNull(jcicB085ReposDay);
		org.junit.Assert.assertNotNull(jcicB085ReposMon);
		org.junit.Assert.assertNotNull(jcicB085ReposHist);
	}

	@Override
	public JcicB085 findById(JcicB085Id jcicB085Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicB085Id);
		Optional<JcicB085> jcicB085 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB085 = jcicB085ReposDay.findById(jcicB085Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB085 = jcicB085ReposMon.findById(jcicB085Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB085 = jcicB085ReposHist.findById(jcicB085Id);
		else
			jcicB085 = jcicB085Repos.findById(jcicB085Id);
		JcicB085 obj = jcicB085.isPresent() ? jcicB085.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicB085> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicB085> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "DataYM", "BefAcctNo", "AftAcctNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "DataYM", "BefAcctNo", "AftAcctNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicB085ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicB085ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicB085ReposHist.findAll(pageable);
		else
			slice = jcicB085Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicB085 holdById(JcicB085Id jcicB085Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB085Id);
		Optional<JcicB085> jcicB085 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB085 = jcicB085ReposDay.findByJcicB085Id(jcicB085Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB085 = jcicB085ReposMon.findByJcicB085Id(jcicB085Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB085 = jcicB085ReposHist.findByJcicB085Id(jcicB085Id);
		else
			jcicB085 = jcicB085Repos.findByJcicB085Id(jcicB085Id);
		return jcicB085.isPresent() ? jcicB085.get() : null;
	}

	@Override
	public JcicB085 holdById(JcicB085 jcicB085, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB085.getJcicB085Id());
		Optional<JcicB085> jcicB085T = null;
		if (dbName.equals(ContentName.onDay))
			jcicB085T = jcicB085ReposDay.findByJcicB085Id(jcicB085.getJcicB085Id());
		else if (dbName.equals(ContentName.onMon))
			jcicB085T = jcicB085ReposMon.findByJcicB085Id(jcicB085.getJcicB085Id());
		else if (dbName.equals(ContentName.onHist))
			jcicB085T = jcicB085ReposHist.findByJcicB085Id(jcicB085.getJcicB085Id());
		else
			jcicB085T = jcicB085Repos.findByJcicB085Id(jcicB085.getJcicB085Id());
		return jcicB085T.isPresent() ? jcicB085T.get() : null;
	}

	@Override
	public JcicB085 insert(JcicB085 jcicB085, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicB085.getJcicB085Id());
		if (this.findById(jcicB085.getJcicB085Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicB085.setCreateEmpNo(empNot);

		if (jcicB085.getLastUpdateEmpNo() == null || jcicB085.getLastUpdateEmpNo().isEmpty())
			jcicB085.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB085ReposDay.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onMon))
			return jcicB085ReposMon.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onHist))
			return jcicB085ReposHist.saveAndFlush(jcicB085);
		else
			return jcicB085Repos.saveAndFlush(jcicB085);
	}

	@Override
	public JcicB085 update(JcicB085 jcicB085, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB085.getJcicB085Id());
		if (!empNot.isEmpty())
			jcicB085.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB085ReposDay.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onMon))
			return jcicB085ReposMon.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onHist))
			return jcicB085ReposHist.saveAndFlush(jcicB085);
		else
			return jcicB085Repos.saveAndFlush(jcicB085);
	}

	@Override
	public JcicB085 update2(JcicB085 jcicB085, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB085.getJcicB085Id());
		if (!empNot.isEmpty())
			jcicB085.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicB085ReposDay.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onMon))
			jcicB085ReposMon.saveAndFlush(jcicB085);
		else if (dbName.equals(ContentName.onHist))
			jcicB085ReposHist.saveAndFlush(jcicB085);
		else
			jcicB085Repos.saveAndFlush(jcicB085);
		return this.findById(jcicB085.getJcicB085Id());
	}

	@Override
	public void delete(JcicB085 jcicB085, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicB085.getJcicB085Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicB085ReposDay.delete(jcicB085);
			jcicB085ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB085ReposMon.delete(jcicB085);
			jcicB085ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB085ReposHist.delete(jcicB085);
			jcicB085ReposHist.flush();
		} else {
			jcicB085Repos.delete(jcicB085);
			jcicB085Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException {
		if (jcicB085 == null || jcicB085.size() == 0)
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
		for (JcicB085 t : jcicB085) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicB085 = jcicB085ReposDay.saveAll(jcicB085);
			jcicB085ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB085 = jcicB085ReposMon.saveAll(jcicB085);
			jcicB085ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB085 = jcicB085ReposHist.saveAll(jcicB085);
			jcicB085ReposHist.flush();
		} else {
			jcicB085 = jcicB085Repos.saveAll(jcicB085);
			jcicB085Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicB085 == null || jcicB085.size() == 0)
			throw new DBException(6);

		for (JcicB085 t : jcicB085)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicB085 = jcicB085ReposDay.saveAll(jcicB085);
			jcicB085ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB085 = jcicB085ReposMon.saveAll(jcicB085);
			jcicB085ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB085 = jcicB085ReposHist.saveAll(jcicB085);
			jcicB085ReposHist.flush();
		} else {
			jcicB085 = jcicB085Repos.saveAll(jcicB085);
			jcicB085Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicB085> jcicB085, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicB085 == null || jcicB085.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicB085ReposDay.deleteAll(jcicB085);
			jcicB085ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB085ReposMon.deleteAll(jcicB085);
			jcicB085ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB085ReposHist.deleteAll(jcicB085);
			jcicB085ReposHist.flush();
		} else {
			jcicB085Repos.deleteAll(jcicB085);
			jcicB085Repos.flush();
		}
	}

	@Override
	public void Usp_L8_JcicB085_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			jcicB085ReposDay.uspL8Jcicb085Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			jcicB085ReposMon.uspL8Jcicb085Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			jcicB085ReposHist.uspL8Jcicb085Upd(TBSDYF, EmpNo);
		else
			jcicB085Repos.uspL8Jcicb085Upd(TBSDYF, EmpNo);
	}

}
