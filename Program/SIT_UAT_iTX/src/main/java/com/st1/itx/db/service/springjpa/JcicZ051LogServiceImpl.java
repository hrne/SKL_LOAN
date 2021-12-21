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
import com.st1.itx.db.domain.JcicZ051Log;
import com.st1.itx.db.domain.JcicZ051LogId;
import com.st1.itx.db.repository.online.JcicZ051LogRepository;
import com.st1.itx.db.repository.day.JcicZ051LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ051LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ051LogRepositoryHist;
import com.st1.itx.db.service.JcicZ051LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ051LogService")
@Repository
public class JcicZ051LogServiceImpl extends ASpringJpaParm implements JcicZ051LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ051LogRepository jcicZ051LogRepos;

	@Autowired
	private JcicZ051LogRepositoryDay jcicZ051LogReposDay;

	@Autowired
	private JcicZ051LogRepositoryMon jcicZ051LogReposMon;

	@Autowired
	private JcicZ051LogRepositoryHist jcicZ051LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ051LogRepos);
		org.junit.Assert.assertNotNull(jcicZ051LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ051LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ051LogReposHist);
	}

	@Override
	public JcicZ051Log findById(JcicZ051LogId jcicZ051LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ051LogId);
		Optional<JcicZ051Log> jcicZ051Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ051Log = jcicZ051LogReposDay.findById(jcicZ051LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ051Log = jcicZ051LogReposMon.findById(jcicZ051LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ051Log = jcicZ051LogReposHist.findById(jcicZ051LogId);
		else
			jcicZ051Log = jcicZ051LogRepos.findById(jcicZ051LogId);
		JcicZ051Log obj = jcicZ051Log.isPresent() ? jcicZ051Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ051Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ051Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ051LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ051LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ051LogReposHist.findAll(pageable);
		else
			slice = jcicZ051LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ051Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ051Log> jcicZ051LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ051LogT = jcicZ051LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ051LogT = jcicZ051LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ051LogT = jcicZ051LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ051LogT = jcicZ051LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ051LogT.isPresent() ? jcicZ051LogT.get() : null;
	}

	@Override
	public Slice<JcicZ051Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ051Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ051LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ051LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ051LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ051LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ051Log holdById(JcicZ051LogId jcicZ051LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ051LogId);
		Optional<JcicZ051Log> jcicZ051Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ051Log = jcicZ051LogReposDay.findByJcicZ051LogId(jcicZ051LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ051Log = jcicZ051LogReposMon.findByJcicZ051LogId(jcicZ051LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ051Log = jcicZ051LogReposHist.findByJcicZ051LogId(jcicZ051LogId);
		else
			jcicZ051Log = jcicZ051LogRepos.findByJcicZ051LogId(jcicZ051LogId);
		return jcicZ051Log.isPresent() ? jcicZ051Log.get() : null;
	}

	@Override
	public JcicZ051Log holdById(JcicZ051Log jcicZ051Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ051Log.getJcicZ051LogId());
		Optional<JcicZ051Log> jcicZ051LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ051LogT = jcicZ051LogReposDay.findByJcicZ051LogId(jcicZ051Log.getJcicZ051LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ051LogT = jcicZ051LogReposMon.findByJcicZ051LogId(jcicZ051Log.getJcicZ051LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ051LogT = jcicZ051LogReposHist.findByJcicZ051LogId(jcicZ051Log.getJcicZ051LogId());
		else
			jcicZ051LogT = jcicZ051LogRepos.findByJcicZ051LogId(jcicZ051Log.getJcicZ051LogId());
		return jcicZ051LogT.isPresent() ? jcicZ051LogT.get() : null;
	}

	@Override
	public JcicZ051Log insert(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ051Log.getJcicZ051LogId());
		if (this.findById(jcicZ051Log.getJcicZ051LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ051Log.setCreateEmpNo(empNot);

		if (jcicZ051Log.getLastUpdateEmpNo() == null || jcicZ051Log.getLastUpdateEmpNo().isEmpty())
			jcicZ051Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ051LogReposDay.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ051LogReposMon.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ051LogReposHist.saveAndFlush(jcicZ051Log);
		else
			return jcicZ051LogRepos.saveAndFlush(jcicZ051Log);
	}

	@Override
	public JcicZ051Log update(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ051Log.getJcicZ051LogId());
		if (!empNot.isEmpty())
			jcicZ051Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ051LogReposDay.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ051LogReposMon.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ051LogReposHist.saveAndFlush(jcicZ051Log);
		else
			return jcicZ051LogRepos.saveAndFlush(jcicZ051Log);
	}

	@Override
	public JcicZ051Log update2(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ051Log.getJcicZ051LogId());
		if (!empNot.isEmpty())
			jcicZ051Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ051LogReposDay.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ051LogReposMon.saveAndFlush(jcicZ051Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ051LogReposHist.saveAndFlush(jcicZ051Log);
		else
			jcicZ051LogRepos.saveAndFlush(jcicZ051Log);
		return this.findById(jcicZ051Log.getJcicZ051LogId());
	}

	@Override
	public void delete(JcicZ051Log jcicZ051Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ051Log.getJcicZ051LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ051LogReposDay.delete(jcicZ051Log);
			jcicZ051LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ051LogReposMon.delete(jcicZ051Log);
			jcicZ051LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ051LogReposHist.delete(jcicZ051Log);
			jcicZ051LogReposHist.flush();
		} else {
			jcicZ051LogRepos.delete(jcicZ051Log);
			jcicZ051LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException {
		if (jcicZ051Log == null || jcicZ051Log.size() == 0)
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
		for (JcicZ051Log t : jcicZ051Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ051Log = jcicZ051LogReposDay.saveAll(jcicZ051Log);
			jcicZ051LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ051Log = jcicZ051LogReposMon.saveAll(jcicZ051Log);
			jcicZ051LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ051Log = jcicZ051LogReposHist.saveAll(jcicZ051Log);
			jcicZ051LogReposHist.flush();
		} else {
			jcicZ051Log = jcicZ051LogRepos.saveAll(jcicZ051Log);
			jcicZ051LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ051Log == null || jcicZ051Log.size() == 0)
			throw new DBException(6);

		for (JcicZ051Log t : jcicZ051Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ051Log = jcicZ051LogReposDay.saveAll(jcicZ051Log);
			jcicZ051LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ051Log = jcicZ051LogReposMon.saveAll(jcicZ051Log);
			jcicZ051LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ051Log = jcicZ051LogReposHist.saveAll(jcicZ051Log);
			jcicZ051LogReposHist.flush();
		} else {
			jcicZ051Log = jcicZ051LogRepos.saveAll(jcicZ051Log);
			jcicZ051LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ051Log> jcicZ051Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ051Log == null || jcicZ051Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ051LogReposDay.deleteAll(jcicZ051Log);
			jcicZ051LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ051LogReposMon.deleteAll(jcicZ051Log);
			jcicZ051LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ051LogReposHist.deleteAll(jcicZ051Log);
			jcicZ051LogReposHist.flush();
		} else {
			jcicZ051LogRepos.deleteAll(jcicZ051Log);
			jcicZ051LogRepos.flush();
		}
	}

}
