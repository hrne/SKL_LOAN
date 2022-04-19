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
import com.st1.itx.db.domain.JcicZ041Log;
import com.st1.itx.db.domain.JcicZ041LogId;
import com.st1.itx.db.repository.online.JcicZ041LogRepository;
import com.st1.itx.db.repository.day.JcicZ041LogRepositoryDay;
import com.st1.itx.db.repository.mon.JcicZ041LogRepositoryMon;
import com.st1.itx.db.repository.hist.JcicZ041LogRepositoryHist;
import com.st1.itx.db.service.JcicZ041LogService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("jcicZ041LogService")
@Repository
public class JcicZ041LogServiceImpl extends ASpringJpaParm implements JcicZ041LogService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private JcicZ041LogRepository jcicZ041LogRepos;

	@Autowired
	private JcicZ041LogRepositoryDay jcicZ041LogReposDay;

	@Autowired
	private JcicZ041LogRepositoryMon jcicZ041LogReposMon;

	@Autowired
	private JcicZ041LogRepositoryHist jcicZ041LogReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(jcicZ041LogRepos);
		org.junit.Assert.assertNotNull(jcicZ041LogReposDay);
		org.junit.Assert.assertNotNull(jcicZ041LogReposMon);
		org.junit.Assert.assertNotNull(jcicZ041LogReposHist);
	}

	@Override
	public JcicZ041Log findById(JcicZ041LogId jcicZ041LogId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + jcicZ041LogId);
		Optional<JcicZ041Log> jcicZ041Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ041Log = jcicZ041LogReposDay.findById(jcicZ041LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ041Log = jcicZ041LogReposMon.findById(jcicZ041LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ041Log = jcicZ041LogReposHist.findById(jcicZ041LogId);
		else
			jcicZ041Log = jcicZ041LogRepos.findById(jcicZ041LogId);
		JcicZ041Log obj = jcicZ041Log.isPresent() ? jcicZ041Log.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<JcicZ041Log> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ041Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "Ukey", "TxSeq"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ041LogReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ041LogReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ041LogReposHist.findAll(pageable);
		else
			slice = jcicZ041LogRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ041Log ukeyFirst(String ukey_0, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("ukeyFirst " + dbName + " : " + "ukey_0 : " + ukey_0);
		Optional<JcicZ041Log> jcicZ041LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ041LogT = jcicZ041LogReposDay.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onMon))
			jcicZ041LogT = jcicZ041LogReposMon.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else if (dbName.equals(ContentName.onHist))
			jcicZ041LogT = jcicZ041LogReposHist.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);
		else
			jcicZ041LogT = jcicZ041LogRepos.findTopByUkeyIsOrderByCreateDateDesc(ukey_0);

		return jcicZ041LogT.isPresent() ? jcicZ041LogT.get() : null;
	}

	@Override
	public Slice<JcicZ041Log> ukeyEq(String ukey_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<JcicZ041Log> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("ukeyEq " + dbName + " : " + "ukey_0 : " + ukey_0);
		if (dbName.equals(ContentName.onDay))
			slice = jcicZ041LogReposDay.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = jcicZ041LogReposMon.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = jcicZ041LogReposHist.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);
		else
			slice = jcicZ041LogRepos.findAllByUkeyIsOrderByCreateDateDesc(ukey_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public JcicZ041Log holdById(JcicZ041LogId jcicZ041LogId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ041LogId);
		Optional<JcicZ041Log> jcicZ041Log = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ041Log = jcicZ041LogReposDay.findByJcicZ041LogId(jcicZ041LogId);
		else if (dbName.equals(ContentName.onMon))
			jcicZ041Log = jcicZ041LogReposMon.findByJcicZ041LogId(jcicZ041LogId);
		else if (dbName.equals(ContentName.onHist))
			jcicZ041Log = jcicZ041LogReposHist.findByJcicZ041LogId(jcicZ041LogId);
		else
			jcicZ041Log = jcicZ041LogRepos.findByJcicZ041LogId(jcicZ041LogId);
		return jcicZ041Log.isPresent() ? jcicZ041Log.get() : null;
	}

	@Override
	public JcicZ041Log holdById(JcicZ041Log jcicZ041Log, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + jcicZ041Log.getJcicZ041LogId());
		Optional<JcicZ041Log> jcicZ041LogT = null;
		if (dbName.equals(ContentName.onDay))
			jcicZ041LogT = jcicZ041LogReposDay.findByJcicZ041LogId(jcicZ041Log.getJcicZ041LogId());
		else if (dbName.equals(ContentName.onMon))
			jcicZ041LogT = jcicZ041LogReposMon.findByJcicZ041LogId(jcicZ041Log.getJcicZ041LogId());
		else if (dbName.equals(ContentName.onHist))
			jcicZ041LogT = jcicZ041LogReposHist.findByJcicZ041LogId(jcicZ041Log.getJcicZ041LogId());
		else
			jcicZ041LogT = jcicZ041LogRepos.findByJcicZ041LogId(jcicZ041Log.getJcicZ041LogId());
		return jcicZ041LogT.isPresent() ? jcicZ041LogT.get() : null;
	}

	@Override
	public JcicZ041Log insert(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + jcicZ041Log.getJcicZ041LogId());
		if (this.findById(jcicZ041Log.getJcicZ041LogId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			jcicZ041Log.setCreateEmpNo(empNot);

		if (jcicZ041Log.getLastUpdateEmpNo() == null || jcicZ041Log.getLastUpdateEmpNo().isEmpty())
			jcicZ041Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ041LogReposDay.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ041LogReposMon.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ041LogReposHist.saveAndFlush(jcicZ041Log);
		else
			return jcicZ041LogRepos.saveAndFlush(jcicZ041Log);
	}

	@Override
	public JcicZ041Log update(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ041Log.getJcicZ041LogId());
		if (!empNot.isEmpty())
			jcicZ041Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return jcicZ041LogReposDay.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onMon))
			return jcicZ041LogReposMon.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onHist))
			return jcicZ041LogReposHist.saveAndFlush(jcicZ041Log);
		else
			return jcicZ041LogRepos.saveAndFlush(jcicZ041Log);
	}

	@Override
	public JcicZ041Log update2(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + jcicZ041Log.getJcicZ041LogId());
		if (!empNot.isEmpty())
			jcicZ041Log.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			jcicZ041LogReposDay.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onMon))
			jcicZ041LogReposMon.saveAndFlush(jcicZ041Log);
		else if (dbName.equals(ContentName.onHist))
			jcicZ041LogReposHist.saveAndFlush(jcicZ041Log);
		else
			jcicZ041LogRepos.saveAndFlush(jcicZ041Log);
		return this.findById(jcicZ041Log.getJcicZ041LogId());
	}

	@Override
	public void delete(JcicZ041Log jcicZ041Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + jcicZ041Log.getJcicZ041LogId());
		if (dbName.equals(ContentName.onDay)) {
			jcicZ041LogReposDay.delete(jcicZ041Log);
			jcicZ041LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ041LogReposMon.delete(jcicZ041Log);
			jcicZ041LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ041LogReposHist.delete(jcicZ041Log);
			jcicZ041LogReposHist.flush();
		} else {
			jcicZ041LogRepos.delete(jcicZ041Log);
			jcicZ041LogRepos.flush();
		}
	}

	@Override
	public void insertAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException {
		if (jcicZ041Log == null || jcicZ041Log.size() == 0)
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
		for (JcicZ041Log t : jcicZ041Log) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			jcicZ041Log = jcicZ041LogReposDay.saveAll(jcicZ041Log);
			jcicZ041LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ041Log = jcicZ041LogReposMon.saveAll(jcicZ041Log);
			jcicZ041LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ041Log = jcicZ041LogReposHist.saveAll(jcicZ041Log);
			jcicZ041LogReposHist.flush();
		} else {
			jcicZ041Log = jcicZ041LogRepos.saveAll(jcicZ041Log);
			jcicZ041LogRepos.flush();
		}
	}

	@Override
	public void updateAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (jcicZ041Log == null || jcicZ041Log.size() == 0)
			throw new DBException(6);

		for (JcicZ041Log t : jcicZ041Log)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			jcicZ041Log = jcicZ041LogReposDay.saveAll(jcicZ041Log);
			jcicZ041LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ041Log = jcicZ041LogReposMon.saveAll(jcicZ041Log);
			jcicZ041LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ041Log = jcicZ041LogReposHist.saveAll(jcicZ041Log);
			jcicZ041LogReposHist.flush();
		} else {
			jcicZ041Log = jcicZ041LogRepos.saveAll(jcicZ041Log);
			jcicZ041LogRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<JcicZ041Log> jcicZ041Log, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (jcicZ041Log == null || jcicZ041Log.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			jcicZ041LogReposDay.deleteAll(jcicZ041Log);
			jcicZ041LogReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			jcicZ041LogReposMon.deleteAll(jcicZ041Log);
			jcicZ041LogReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			jcicZ041LogReposHist.deleteAll(jcicZ041Log);
			jcicZ041LogReposHist.flush();
		} else {
			jcicZ041LogRepos.deleteAll(jcicZ041Log);
			jcicZ041LogRepos.flush();
		}
	}

}
