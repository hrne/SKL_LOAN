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
import com.st1.itx.db.domain.JcicZ440Log;
import com.st1.itx.db.domain.JcicZ440LogId;
import com.st1.itx.db.repository.online.JcicZ440LogRepository;
import com.st1.itx.db.repository.day.JcicZ440LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ440LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ440LogRepositoryHist;
import com.st1.itx.db.service.JcicZ440LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ440LogService")
@Repository
public class JcicZ440LogServiceImpl extends ASpringJpaParm implements JcicZ440LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ440LogRepository jcicZ440LogRepos;

	@Autowired
	private JcicZ440LogRepositoryDay jcicZ440LogReposDay;

	@Autowired
	private JcicZ440LogRepositoryMon jcicZ440LogReposMon;

	@Autowired
	private JcicZ440LogRepositoryHist jcicZ440LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ440LogRepos);
		org.junit.Assert.assertNotNull(jcicZ440LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ440LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ440LogReposHist);
	}

	@Override
	public JcicZ440Log findById(JcicZ440LogId jcicZ440LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ440LogId);
		Optional<JcicZ440Log> jcicZ440Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ440Log = jcicZ440LogReposDay.findById(jcicZ440LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ440Log = jcicZ440LogReposMon.findById(jcicZ440LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ440Log = jcicZ440LogReposHist.findById(jcicZ440LogId);
		else
			jcicZ440Log = jcicZ440LogRepos.findById(jcicZ440LogId);
		JcicZ440Log obj = jcicZ440Log.isPresent() ? jcicZ440Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ440Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ440Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ440LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ440LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ440LogReposHist.findAll(pageable);
		else
			slice = jcicZ440LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ440Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ440Log> jcicZ440LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ440LogT = jcicZ440LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ440LogT = jcicZ440LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ440LogT = jcicZ440LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ440LogT = jcicZ440LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ440LogT.isPresent() ? jcicZ440LogT.get() : null;
	}

	@Override
	public Slice<JcicZ440Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ440Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ440LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ440LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ440LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ440LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ440Log holdById(JcicZ440LogId jcicZ440LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ440LogId);
		Optional<JcicZ440Log> jcicZ440Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ440Log = jcicZ440LogReposDay.findByJcicZ440LogId(jcicZ440LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ440Log = jcicZ440LogReposMon.findByJcicZ440LogId(jcicZ440LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ440Log = jcicZ440LogReposHist.findByJcicZ440LogId(jcicZ440LogId);
		else
			jcicZ440Log = jcicZ440LogRepos.findByJcicZ440LogId(jcicZ440LogId);
		return jcicZ440Log.isPresent() ? jcicZ440Log.get() : null;
	}

	@Override
	public JcicZ440Log holdById(JcicZ440Log jcicZ440Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ440Log.getJcicZ440LogId());
		Optional<JcicZ440Log> jcicZ440LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ440LogT = jcicZ440LogReposDay.findByJcicZ440LogId(jcicZ440Log.getJcicZ440LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ440LogT = jcicZ440LogReposMon.findByJcicZ440LogId(jcicZ440Log.getJcicZ440LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ440LogT = jcicZ440LogReposHist.findByJcicZ440LogId(jcicZ440Log.getJcicZ440LogId());
		else
			jcicZ440LogT = jcicZ440LogRepos.findByJcicZ440LogId(jcicZ440Log.getJcicZ440LogId());
		return jcicZ440LogT.isPresent() ? jcicZ440LogT.get() : null;
	}

	@Override
	public JcicZ440Log insert(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ440Log.getJcicZ440LogId());
		if (this.findById(jcicZ440Log.getJcicZ440LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ440Log.setCreateEmpNo(empNot);

		if (jcicZ440Log.getLastUpdateEmpNo() == null || jcicZ440Log.getLastUpdateEmpNo().isEmpty())
			jcicZ440Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ440LogReposDay.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ440LogReposMon.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ440LogReposHist.saveAndFlush(jcicZ440Log);
		else
			return jcicZ440LogRepos.saveAndFlush(jcicZ440Log);
	}

	@Override
	public JcicZ440Log update(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ440Log.getJcicZ440LogId());
		if (!empNot.isEmpty())
			jcicZ440Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ440LogReposDay.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ440LogReposMon.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ440LogReposHist.saveAndFlush(jcicZ440Log);
		else
			return jcicZ440LogRepos.saveAndFlush(jcicZ440Log);
	}

	@Override
	public JcicZ440Log update2(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ440Log.getJcicZ440LogId());
		if (!empNot.isEmpty())
			jcicZ440Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ440LogReposDay.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ440LogReposMon.saveAndFlush(jcicZ440Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ440LogReposHist.saveAndFlush(jcicZ440Log);
		else
			jcicZ440LogRepos.saveAndFlush(jcicZ440Log);
		return this.findById(jcicZ440Log.getJcicZ440LogId());
	}

	@Override
	public void delete(JcicZ440Log jcicZ440Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ440Log.getJcicZ440LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ440LogReposDay.delete(jcicZ440Log);
			jcicZ440LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ440LogReposMon.delete(jcicZ440Log);
			jcicZ440LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ440LogReposHist.delete(jcicZ440Log);
			jcicZ440LogReposHist.flush();
		} else {
			jcicZ440LogRepos.delete(jcicZ440Log);
			jcicZ440LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException {
		if (jcicZ440Log == null || jcicZ440Log.size() == 0)
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
		for (JcicZ440Log t : jcicZ440Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ440Log = jcicZ440LogReposDay.saveAll(jcicZ440Log);
			jcicZ440LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ440Log = jcicZ440LogReposMon.saveAll(jcicZ440Log);
			jcicZ440LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ440Log = jcicZ440LogReposHist.saveAll(jcicZ440Log);
			jcicZ440LogReposHist.flush();
		} else {
			jcicZ440Log = jcicZ440LogRepos.saveAll(jcicZ440Log);
			jcicZ440LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ440Log == null || jcicZ440Log.size() == 0)
			throw new DBException(6);

		for (JcicZ440Log t : jcicZ440Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ440Log = jcicZ440LogReposDay.saveAll(jcicZ440Log);
			jcicZ440LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ440Log = jcicZ440LogReposMon.saveAll(jcicZ440Log);
			jcicZ440LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ440Log = jcicZ440LogReposHist.saveAll(jcicZ440Log);
			jcicZ440LogReposHist.flush();
		} else {
			jcicZ440Log = jcicZ440LogRepos.saveAll(jcicZ440Log);
			jcicZ440LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ440Log> jcicZ440Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ440Log == null || jcicZ440Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ440LogReposDay.deleteAll(jcicZ440Log);
			jcicZ440LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ440LogReposMon.deleteAll(jcicZ440Log);
			jcicZ440LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ440LogReposHist.deleteAll(jcicZ440Log);
			jcicZ440LogReposHist.flush();
		} else {
			jcicZ440LogRepos.deleteAll(jcicZ440Log);
			jcicZ440LogRepos.flush();
		}
	}

}
