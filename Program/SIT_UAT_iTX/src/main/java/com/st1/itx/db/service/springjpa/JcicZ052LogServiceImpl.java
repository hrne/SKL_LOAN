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
import com.st1.itx.db.domain.JcicZ052Log;
import com.st1.itx.db.domain.JcicZ052LogId;
import com.st1.itx.db.repository.online.JcicZ052LogRepository;
import com.st1.itx.db.repository.day.JcicZ052LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ052LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ052LogRepositoryHist;
import com.st1.itx.db.service.JcicZ052LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ052LogService")
@Repository
public class JcicZ052LogServiceImpl extends ASpringJpaParm implements JcicZ052LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ052LogRepository jcicZ052LogRepos;

	@Autowired
	private JcicZ052LogRepositoryDay jcicZ052LogReposDay;

	@Autowired
	private JcicZ052LogRepositoryMon jcicZ052LogReposMon;

	@Autowired
	private JcicZ052LogRepositoryHist jcicZ052LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ052LogRepos);
		org.junit.Assert.assertNotNull(jcicZ052LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ052LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ052LogReposHist);
	}

	@Override
	public JcicZ052Log findById(JcicZ052LogId jcicZ052LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ052LogId);
		Optional<JcicZ052Log> jcicZ052Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ052Log = jcicZ052LogReposDay.findById(jcicZ052LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ052Log = jcicZ052LogReposMon.findById(jcicZ052LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ052Log = jcicZ052LogReposHist.findById(jcicZ052LogId);
		else
			jcicZ052Log = jcicZ052LogRepos.findById(jcicZ052LogId);
		JcicZ052Log obj = jcicZ052Log.isPresent() ? jcicZ052Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ052Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ052Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ052LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ052LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ052LogReposHist.findAll(pageable);
		else
			slice = jcicZ052LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ052Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ052Log> jcicZ052LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ052LogT = jcicZ052LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ052LogT = jcicZ052LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ052LogT = jcicZ052LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ052LogT = jcicZ052LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ052LogT.isPresent() ? jcicZ052LogT.get() : null;
	}

	@Override
	public Slice<JcicZ052Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ052Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ052LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ052LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ052LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ052LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ052Log holdById(JcicZ052LogId jcicZ052LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ052LogId);
		Optional<JcicZ052Log> jcicZ052Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ052Log = jcicZ052LogReposDay.findByJcicZ052LogId(jcicZ052LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ052Log = jcicZ052LogReposMon.findByJcicZ052LogId(jcicZ052LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ052Log = jcicZ052LogReposHist.findByJcicZ052LogId(jcicZ052LogId);
		else
			jcicZ052Log = jcicZ052LogRepos.findByJcicZ052LogId(jcicZ052LogId);
		return jcicZ052Log.isPresent() ? jcicZ052Log.get() : null;
	}

	@Override
	public JcicZ052Log holdById(JcicZ052Log jcicZ052Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ052Log.getJcicZ052LogId());
		Optional<JcicZ052Log> jcicZ052LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ052LogT = jcicZ052LogReposDay.findByJcicZ052LogId(jcicZ052Log.getJcicZ052LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ052LogT = jcicZ052LogReposMon.findByJcicZ052LogId(jcicZ052Log.getJcicZ052LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ052LogT = jcicZ052LogReposHist.findByJcicZ052LogId(jcicZ052Log.getJcicZ052LogId());
		else
			jcicZ052LogT = jcicZ052LogRepos.findByJcicZ052LogId(jcicZ052Log.getJcicZ052LogId());
		return jcicZ052LogT.isPresent() ? jcicZ052LogT.get() : null;
	}

	@Override
	public JcicZ052Log insert(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ052Log.getJcicZ052LogId());
		if (this.findById(jcicZ052Log.getJcicZ052LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ052Log.setCreateEmpNo(empNot);

		if (jcicZ052Log.getLastUpdateEmpNo() == null || jcicZ052Log.getLastUpdateEmpNo().isEmpty())
			jcicZ052Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ052LogReposDay.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ052LogReposMon.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ052LogReposHist.saveAndFlush(jcicZ052Log);
		else
			return jcicZ052LogRepos.saveAndFlush(jcicZ052Log);
	}

	@Override
	public JcicZ052Log update(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ052Log.getJcicZ052LogId());
		if (!empNot.isEmpty())
			jcicZ052Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ052LogReposDay.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ052LogReposMon.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ052LogReposHist.saveAndFlush(jcicZ052Log);
		else
			return jcicZ052LogRepos.saveAndFlush(jcicZ052Log);
	}

	@Override
	public JcicZ052Log update2(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ052Log.getJcicZ052LogId());
		if (!empNot.isEmpty())
			jcicZ052Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ052LogReposDay.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ052LogReposMon.saveAndFlush(jcicZ052Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ052LogReposHist.saveAndFlush(jcicZ052Log);
		else
			jcicZ052LogRepos.saveAndFlush(jcicZ052Log);
		return this.findById(jcicZ052Log.getJcicZ052LogId());
	}

	@Override
	public void delete(JcicZ052Log jcicZ052Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ052Log.getJcicZ052LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ052LogReposDay.delete(jcicZ052Log);
			jcicZ052LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ052LogReposMon.delete(jcicZ052Log);
			jcicZ052LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ052LogReposHist.delete(jcicZ052Log);
			jcicZ052LogReposHist.flush();
		} else {
			jcicZ052LogRepos.delete(jcicZ052Log);
			jcicZ052LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException {
		if (jcicZ052Log == null || jcicZ052Log.size() == 0)
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
		for (JcicZ052Log t : jcicZ052Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ052Log = jcicZ052LogReposDay.saveAll(jcicZ052Log);
			jcicZ052LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ052Log = jcicZ052LogReposMon.saveAll(jcicZ052Log);
			jcicZ052LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ052Log = jcicZ052LogReposHist.saveAll(jcicZ052Log);
			jcicZ052LogReposHist.flush();
		} else {
			jcicZ052Log = jcicZ052LogRepos.saveAll(jcicZ052Log);
			jcicZ052LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ052Log == null || jcicZ052Log.size() == 0)
			throw new DBException(6);

		for (JcicZ052Log t : jcicZ052Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ052Log = jcicZ052LogReposDay.saveAll(jcicZ052Log);
			jcicZ052LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ052Log = jcicZ052LogReposMon.saveAll(jcicZ052Log);
			jcicZ052LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ052Log = jcicZ052LogReposHist.saveAll(jcicZ052Log);
			jcicZ052LogReposHist.flush();
		} else {
			jcicZ052Log = jcicZ052LogRepos.saveAll(jcicZ052Log);
			jcicZ052LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ052Log> jcicZ052Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ052Log == null || jcicZ052Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ052LogReposDay.deleteAll(jcicZ052Log);
			jcicZ052LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ052LogReposMon.deleteAll(jcicZ052Log);
			jcicZ052LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ052LogReposHist.deleteAll(jcicZ052Log);
			jcicZ052LogReposHist.flush();
		} else {
			jcicZ052LogRepos.deleteAll(jcicZ052Log);
			jcicZ052LogRepos.flush();
		}
	}

}
