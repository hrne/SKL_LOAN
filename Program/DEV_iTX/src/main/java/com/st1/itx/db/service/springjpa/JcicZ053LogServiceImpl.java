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
import com.st1.itx.db.domain.JcicZ053Log;
import com.st1.itx.db.domain.JcicZ053LogId;
import com.st1.itx.db.repository.online.JcicZ053LogRepository;
import com.st1.itx.db.repository.day.JcicZ053LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ053LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ053LogRepositoryHist;
import com.st1.itx.db.service.JcicZ053LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ053LogService")
@Repository
public class JcicZ053LogServiceImpl extends ASpringJpaParm implements JcicZ053LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ053LogRepository jcicZ053LogRepos;

	@Autowired
	private JcicZ053LogRepositoryDay jcicZ053LogReposDay;

	@Autowired
	private JcicZ053LogRepositoryMon jcicZ053LogReposMon;

	@Autowired
	private JcicZ053LogRepositoryHist jcicZ053LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ053LogRepos);
		org.junit.Assert.assertNotNull(jcicZ053LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ053LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ053LogReposHist);
	}

	@Override
	public JcicZ053Log findById(JcicZ053LogId jcicZ053LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ053LogId);
		Optional<JcicZ053Log> jcicZ053Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ053Log = jcicZ053LogReposDay.findById(jcicZ053LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ053Log = jcicZ053LogReposMon.findById(jcicZ053LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ053Log = jcicZ053LogReposHist.findById(jcicZ053LogId);
		else
			jcicZ053Log = jcicZ053LogRepos.findById(jcicZ053LogId);
		JcicZ053Log obj = jcicZ053Log.isPresent() ? jcicZ053Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ053Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ053Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ053LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ053LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ053LogReposHist.findAll(pageable);
		else
			slice = jcicZ053LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ053Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ053Log> jcicZ053LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ053LogT = jcicZ053LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ053LogT = jcicZ053LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ053LogT = jcicZ053LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ053LogT = jcicZ053LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ053LogT.isPresent() ? jcicZ053LogT.get() : null;
	}

	@Override
	public Slice<JcicZ053Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ053Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ053LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ053LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ053LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ053LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ053Log holdById(JcicZ053LogId jcicZ053LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ053LogId);
		Optional<JcicZ053Log> jcicZ053Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ053Log = jcicZ053LogReposDay.findByJcicZ053LogId(jcicZ053LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ053Log = jcicZ053LogReposMon.findByJcicZ053LogId(jcicZ053LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ053Log = jcicZ053LogReposHist.findByJcicZ053LogId(jcicZ053LogId);
		else
			jcicZ053Log = jcicZ053LogRepos.findByJcicZ053LogId(jcicZ053LogId);
		return jcicZ053Log.isPresent() ? jcicZ053Log.get() : null;
	}

	@Override
	public JcicZ053Log holdById(JcicZ053Log jcicZ053Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ053Log.getJcicZ053LogId());
		Optional<JcicZ053Log> jcicZ053LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ053LogT = jcicZ053LogReposDay.findByJcicZ053LogId(jcicZ053Log.getJcicZ053LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ053LogT = jcicZ053LogReposMon.findByJcicZ053LogId(jcicZ053Log.getJcicZ053LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ053LogT = jcicZ053LogReposHist.findByJcicZ053LogId(jcicZ053Log.getJcicZ053LogId());
		else
			jcicZ053LogT = jcicZ053LogRepos.findByJcicZ053LogId(jcicZ053Log.getJcicZ053LogId());
		return jcicZ053LogT.isPresent() ? jcicZ053LogT.get() : null;
	}

	@Override
	public JcicZ053Log insert(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ053Log.getJcicZ053LogId());
		if (this.findById(jcicZ053Log.getJcicZ053LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ053Log.setCreateEmpNo(empNot);

		if (jcicZ053Log.getLastUpdateEmpNo() == null || jcicZ053Log.getLastUpdateEmpNo().isEmpty())
			jcicZ053Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ053LogReposDay.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ053LogReposMon.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ053LogReposHist.saveAndFlush(jcicZ053Log);
		else
			return jcicZ053LogRepos.saveAndFlush(jcicZ053Log);
	}

	@Override
	public JcicZ053Log update(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ053Log.getJcicZ053LogId());
		if (!empNot.isEmpty())
			jcicZ053Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ053LogReposDay.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ053LogReposMon.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ053LogReposHist.saveAndFlush(jcicZ053Log);
		else
			return jcicZ053LogRepos.saveAndFlush(jcicZ053Log);
	}

	@Override
	public JcicZ053Log update2(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ053Log.getJcicZ053LogId());
		if (!empNot.isEmpty())
			jcicZ053Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ053LogReposDay.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ053LogReposMon.saveAndFlush(jcicZ053Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ053LogReposHist.saveAndFlush(jcicZ053Log);
		else
			jcicZ053LogRepos.saveAndFlush(jcicZ053Log);
		return this.findById(jcicZ053Log.getJcicZ053LogId());
	}

	@Override
	public void delete(JcicZ053Log jcicZ053Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ053Log.getJcicZ053LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ053LogReposDay.delete(jcicZ053Log);
			jcicZ053LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ053LogReposMon.delete(jcicZ053Log);
			jcicZ053LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ053LogReposHist.delete(jcicZ053Log);
			jcicZ053LogReposHist.flush();
		} else {
			jcicZ053LogRepos.delete(jcicZ053Log);
			jcicZ053LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException {
		if (jcicZ053Log == null || jcicZ053Log.size() == 0)
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
		for (JcicZ053Log t : jcicZ053Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ053Log = jcicZ053LogReposDay.saveAll(jcicZ053Log);
			jcicZ053LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ053Log = jcicZ053LogReposMon.saveAll(jcicZ053Log);
			jcicZ053LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ053Log = jcicZ053LogReposHist.saveAll(jcicZ053Log);
			jcicZ053LogReposHist.flush();
		} else {
			jcicZ053Log = jcicZ053LogRepos.saveAll(jcicZ053Log);
			jcicZ053LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ053Log == null || jcicZ053Log.size() == 0)
			throw new DBException(6);

		for (JcicZ053Log t : jcicZ053Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ053Log = jcicZ053LogReposDay.saveAll(jcicZ053Log);
			jcicZ053LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ053Log = jcicZ053LogReposMon.saveAll(jcicZ053Log);
			jcicZ053LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ053Log = jcicZ053LogReposHist.saveAll(jcicZ053Log);
			jcicZ053LogReposHist.flush();
		} else {
			jcicZ053Log = jcicZ053LogRepos.saveAll(jcicZ053Log);
			jcicZ053LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ053Log> jcicZ053Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ053Log == null || jcicZ053Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ053LogReposDay.deleteAll(jcicZ053Log);
			jcicZ053LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ053LogReposMon.deleteAll(jcicZ053Log);
			jcicZ053LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ053LogReposHist.deleteAll(jcicZ053Log);
			jcicZ053LogReposHist.flush();
		} else {
			jcicZ053LogRepos.deleteAll(jcicZ053Log);
			jcicZ053LogRepos.flush();
		}
	}

}
