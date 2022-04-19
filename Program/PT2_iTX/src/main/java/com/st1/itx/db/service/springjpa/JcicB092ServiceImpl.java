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
import com.st1.itx.db.domain.JcicB092;
import com.st1.itx.db.domain.JcicB092Id;
import com.st1.itx.db.repository.online.JcicB092Repository;
import com.st1.itx.db.repository.day.JcicB092RepositoryDay;
import com.st1.itx.db.repository.mon.JcicB092RepositoryMon;
import com.st1.itx.db.repository.hist.JcicB092RepositoryHist;
import com.st1.itx.db.service.JcicB092Service;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicB092Service")
@Repository
public class JcicB092ServiceImpl extends ASpringJpaParm implements JcicB092Service, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicB092Repository jcicB092Repos;

	@Autowired
	private JcicB092RepositoryDay jcicB092ReposDay;

	@Autowired
	private JcicB092RepositoryMon jcicB092ReposMon;

	@Autowired
	private JcicB092RepositoryHist jcicB092ReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicB092Repos);
		org.junit.Assert.assertNotNull(jcicB092ReposDay);
		org.junit.Assert.assertNotNull(jcicB092ReposMon);
		org.junit.Assert.assertNotNull(jcicB092ReposHist);
	}

	@Override
	public JcicB092 findById(JcicB092Id jcicB092Id, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicB092Id);
		Optional<JcicB092> jcicB092 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB092 = jcicB092ReposDay.findById(jcicB092Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB092 = jcicB092ReposMon.findById(jcicB092Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB092 = jcicB092ReposHist.findById(jcicB092Id);
		else
			jcicB092 = jcicB092Repos.findById(jcicB092Id);
		JcicB092 obj = jcicB092.isPresent() ? jcicB092.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicB092> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicB092> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE,
					Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "ClTypeJCIC", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "LandNo1", "LandNo2", "BdNo1", "BdNo2"));
		else
			pageable = PageRequest.of(index, limit,
					Sort.by(Sort.Direction.ASC, "DataYM", "ClActNo", "ClTypeJCIC", "OwnerId", "CityJCICCode", "AreaJCICCode", "IrCode", "LandNo1", "LandNo2", "BdNo1", "BdNo2"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicB092ReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicB092ReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicB092ReposHist.findAll(pageable);
		else
			slice = jcicB092Repos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicB092 holdById(JcicB092Id jcicB092Id, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB092Id);
		Optional<JcicB092> jcicB092 = null;
		if (dbName.equals(ContentName.onDay))
			jcicB092 = jcicB092ReposDay.findByJcicB092Id(jcicB092Id);
		else if (dbName.equals(ContentName.onMon))
			jcicB092 = jcicB092ReposMon.findByJcicB092Id(jcicB092Id);
		else if (dbName.equals(ContentName.onHist))
			jcicB092 = jcicB092ReposHist.findByJcicB092Id(jcicB092Id);
		else
			jcicB092 = jcicB092Repos.findByJcicB092Id(jcicB092Id);
		return jcicB092.isPresent() ? jcicB092.get() : null;
	}

	@Override
	public JcicB092 holdById(JcicB092 jcicB092, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicB092.getJcicB092Id());
		Optional<JcicB092> jcicB092T = null;
		if (dbName.equals(ContentName.onDay))
			jcicB092T = jcicB092ReposDay.findByJcicB092Id(jcicB092.getJcicB092Id());
		else if (dbName.equals(ContentName.onMon))
			jcicB092T = jcicB092ReposMon.findByJcicB092Id(jcicB092.getJcicB092Id());
		else if (dbName.equals(ContentName.onHist))
			jcicB092T = jcicB092ReposHist.findByJcicB092Id(jcicB092.getJcicB092Id());
		else
			jcicB092T = jcicB092Repos.findByJcicB092Id(jcicB092.getJcicB092Id());
		return jcicB092T.isPresent() ? jcicB092T.get() : null;
	}

	@Override
	public JcicB092 insert(JcicB092 jcicB092, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicB092.getJcicB092Id());
		if (this.findById(jcicB092.getJcicB092Id()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicB092.setCreateEmpNo(empNot);

		if (jcicB092.getLastUpdateEmpNo() == null || jcicB092.getLastUpdateEmpNo().isEmpty())
			jcicB092.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB092ReposDay.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onMon))
			return jcicB092ReposMon.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onHist))
			return jcicB092ReposHist.saveAndFlush(jcicB092);
		else
			return jcicB092Repos.saveAndFlush(jcicB092);
	}

	@Override
	public JcicB092 update(JcicB092 jcicB092, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB092.getJcicB092Id());
		if (!empNot.isEmpty())
			jcicB092.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicB092ReposDay.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onMon))
			return jcicB092ReposMon.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onHist))
			return jcicB092ReposHist.saveAndFlush(jcicB092);
		else
			return jcicB092Repos.saveAndFlush(jcicB092);
	}

	@Override
	public JcicB092 update2(JcicB092 jcicB092, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicB092.getJcicB092Id());
		if (!empNot.isEmpty())
			jcicB092.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicB092ReposDay.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onMon))
			jcicB092ReposMon.saveAndFlush(jcicB092);
		else if (dbName.equals(ContentName.onHist))
			jcicB092ReposHist.saveAndFlush(jcicB092);
		else
			jcicB092Repos.saveAndFlush(jcicB092);
		return this.findById(jcicB092.getJcicB092Id());
	}

	@Override
	public void delete(JcicB092 jcicB092, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicB092.getJcicB092Id());
		if (dbName.equals(ContentName.onDay)) {
			jcicB092ReposDay.delete(jcicB092);
			jcicB092ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB092ReposMon.delete(jcicB092);
			jcicB092ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB092ReposHist.delete(jcicB092);
			jcicB092ReposHist.flush();
		} else {
			jcicB092Repos.delete(jcicB092);
			jcicB092Repos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException {
		if (jcicB092 == null || jcicB092.size() == 0)
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
		for (JcicB092 t : jcicB092) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicB092 = jcicB092ReposDay.saveAll(jcicB092);
			jcicB092ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB092 = jcicB092ReposMon.saveAll(jcicB092);
			jcicB092ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB092 = jcicB092ReposHist.saveAll(jcicB092);
			jcicB092ReposHist.flush();
		} else {
			jcicB092 = jcicB092Repos.saveAll(jcicB092);
			jcicB092Repos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicB092 == null || jcicB092.size() == 0)
			throw new DBException(6);

		for (JcicB092 t : jcicB092)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicB092 = jcicB092ReposDay.saveAll(jcicB092);
			jcicB092ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB092 = jcicB092ReposMon.saveAll(jcicB092);
			jcicB092ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB092 = jcicB092ReposHist.saveAll(jcicB092);
			jcicB092ReposHist.flush();
		} else {
			jcicB092 = jcicB092Repos.saveAll(jcicB092);
			jcicB092Repos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicB092> jcicB092, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicB092 == null || jcicB092.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicB092ReposDay.deleteAll(jcicB092);
			jcicB092ReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicB092ReposMon.deleteAll(jcicB092);
			jcicB092ReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicB092ReposHist.deleteAll(jcicB092);
			jcicB092ReposHist.flush();
		} else {
			jcicB092Repos.deleteAll(jcicB092);
			jcicB092Repos.flush();
		}
	}

	@Override
	public void Usp_L8_JcicB092_Upd(int TBSDYF, String EmpNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (dbName.equals(ContentName.onDay))
			jcicB092ReposDay.uspL8Jcicb092Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onMon))
			jcicB092ReposMon.uspL8Jcicb092Upd(TBSDYF, EmpNo);
		else if (dbName.equals(ContentName.onHist))
			jcicB092ReposHist.uspL8Jcicb092Upd(TBSDYF, EmpNo);
		else
			jcicB092Repos.uspL8Jcicb092Upd(TBSDYF, EmpNo);
	}

}
