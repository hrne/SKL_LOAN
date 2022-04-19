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
import com.st1.itx.db.domain.JcicZ062Log;
import com.st1.itx.db.domain.JcicZ062LogId;
import com.st1.itx.db.repository.online.JcicZ062LogRepository;
import com.st1.itx.db.repository.day.JcicZ062LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ062LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ062LogRepositoryHist;
import com.st1.itx.db.service.JcicZ062LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ062LogService")
@Repository
public class JcicZ062LogServiceImpl extends ASpringJpaParm implements JcicZ062LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ062LogRepository jcicZ062LogRepos;

	@Autowired
	private JcicZ062LogRepositoryDay jcicZ062LogReposDay;

	@Autowired
	private JcicZ062LogRepositoryMon jcicZ062LogReposMon;

	@Autowired
	private JcicZ062LogRepositoryHist jcicZ062LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ062LogRepos);
		org.junit.Assert.assertNotNull(jcicZ062LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ062LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ062LogReposHist);
	}

	@Override
	public JcicZ062Log findById(JcicZ062LogId jcicZ062LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ062LogId);
		Optional<JcicZ062Log> jcicZ062Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ062Log = jcicZ062LogReposDay.findById(jcicZ062LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ062Log = jcicZ062LogReposMon.findById(jcicZ062LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ062Log = jcicZ062LogReposHist.findById(jcicZ062LogId);
		else
			jcicZ062Log = jcicZ062LogRepos.findById(jcicZ062LogId);
		JcicZ062Log obj = jcicZ062Log.isPresent() ? jcicZ062Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ062Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ062Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ062LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ062LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ062LogReposHist.findAll(pageable);
		else
			slice = jcicZ062LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ062Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ062Log> jcicZ062LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ062LogT = jcicZ062LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ062LogT = jcicZ062LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ062LogT = jcicZ062LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ062LogT = jcicZ062LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ062LogT.isPresent() ? jcicZ062LogT.get() : null;
	}

	@Override
	public Slice<JcicZ062Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ062Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ062LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ062LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ062LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ062LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ062Log holdById(JcicZ062LogId jcicZ062LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ062LogId);
		Optional<JcicZ062Log> jcicZ062Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ062Log = jcicZ062LogReposDay.findByJcicZ062LogId(jcicZ062LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ062Log = jcicZ062LogReposMon.findByJcicZ062LogId(jcicZ062LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ062Log = jcicZ062LogReposHist.findByJcicZ062LogId(jcicZ062LogId);
		else
			jcicZ062Log = jcicZ062LogRepos.findByJcicZ062LogId(jcicZ062LogId);
		return jcicZ062Log.isPresent() ? jcicZ062Log.get() : null;
	}

	@Override
	public JcicZ062Log holdById(JcicZ062Log jcicZ062Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ062Log.getJcicZ062LogId());
		Optional<JcicZ062Log> jcicZ062LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ062LogT = jcicZ062LogReposDay.findByJcicZ062LogId(jcicZ062Log.getJcicZ062LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ062LogT = jcicZ062LogReposMon.findByJcicZ062LogId(jcicZ062Log.getJcicZ062LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ062LogT = jcicZ062LogReposHist.findByJcicZ062LogId(jcicZ062Log.getJcicZ062LogId());
		else
			jcicZ062LogT = jcicZ062LogRepos.findByJcicZ062LogId(jcicZ062Log.getJcicZ062LogId());
		return jcicZ062LogT.isPresent() ? jcicZ062LogT.get() : null;
	}

	@Override
	public JcicZ062Log insert(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ062Log.getJcicZ062LogId());
		if (this.findById(jcicZ062Log.getJcicZ062LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ062Log.setCreateEmpNo(empNot);

		if (jcicZ062Log.getLastUpdateEmpNo() == null || jcicZ062Log.getLastUpdateEmpNo().isEmpty())
			jcicZ062Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ062LogReposDay.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ062LogReposMon.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ062LogReposHist.saveAndFlush(jcicZ062Log);
		else
			return jcicZ062LogRepos.saveAndFlush(jcicZ062Log);
	}

	@Override
	public JcicZ062Log update(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ062Log.getJcicZ062LogId());
		if (!empNot.isEmpty())
			jcicZ062Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ062LogReposDay.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ062LogReposMon.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ062LogReposHist.saveAndFlush(jcicZ062Log);
		else
			return jcicZ062LogRepos.saveAndFlush(jcicZ062Log);
	}

	@Override
	public JcicZ062Log update2(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ062Log.getJcicZ062LogId());
		if (!empNot.isEmpty())
			jcicZ062Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ062LogReposDay.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ062LogReposMon.saveAndFlush(jcicZ062Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ062LogReposHist.saveAndFlush(jcicZ062Log);
		else
			jcicZ062LogRepos.saveAndFlush(jcicZ062Log);
		return this.findById(jcicZ062Log.getJcicZ062LogId());
	}

	@Override
	public void delete(JcicZ062Log jcicZ062Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ062Log.getJcicZ062LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ062LogReposDay.delete(jcicZ062Log);
			jcicZ062LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ062LogReposMon.delete(jcicZ062Log);
			jcicZ062LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ062LogReposHist.delete(jcicZ062Log);
			jcicZ062LogReposHist.flush();
		} else {
			jcicZ062LogRepos.delete(jcicZ062Log);
			jcicZ062LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException {
		if (jcicZ062Log == null || jcicZ062Log.size() == 0)
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
		for (JcicZ062Log t : jcicZ062Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ062Log = jcicZ062LogReposDay.saveAll(jcicZ062Log);
			jcicZ062LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ062Log = jcicZ062LogReposMon.saveAll(jcicZ062Log);
			jcicZ062LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ062Log = jcicZ062LogReposHist.saveAll(jcicZ062Log);
			jcicZ062LogReposHist.flush();
		} else {
			jcicZ062Log = jcicZ062LogRepos.saveAll(jcicZ062Log);
			jcicZ062LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ062Log == null || jcicZ062Log.size() == 0)
			throw new DBException(6);

		for (JcicZ062Log t : jcicZ062Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ062Log = jcicZ062LogReposDay.saveAll(jcicZ062Log);
			jcicZ062LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ062Log = jcicZ062LogReposMon.saveAll(jcicZ062Log);
			jcicZ062LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ062Log = jcicZ062LogReposHist.saveAll(jcicZ062Log);
			jcicZ062LogReposHist.flush();
		} else {
			jcicZ062Log = jcicZ062LogRepos.saveAll(jcicZ062Log);
			jcicZ062LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ062Log> jcicZ062Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ062Log == null || jcicZ062Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ062LogReposDay.deleteAll(jcicZ062Log);
			jcicZ062LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ062LogReposMon.deleteAll(jcicZ062Log);
			jcicZ062LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ062LogReposHist.deleteAll(jcicZ062Log);
			jcicZ062LogReposHist.flush();
		} else {
			jcicZ062LogRepos.deleteAll(jcicZ062Log);
			jcicZ062LogRepos.flush();
		}
	}

}
