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
import com.st1.itx.db.domain.GuildBuilders;
import com.st1.itx.db.repository.online.GuildBuildersRepository;
import com.st1.itx.db.repository.day.GuildBuildersRepositoryDay;
import com.st1.itx.db.repository.mon.GuildBuildersRepositoryMon;
import com.st1.itx.db.repository.hist.GuildBuildersRepositoryHist;
import com.st1.itx.db.service.GuildBuildersService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("guildBuildersService")
@Repository
public class GuildBuildersServiceImpl extends ASpringJpaParm implements GuildBuildersService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private GuildBuildersRepository guildBuildersRepos;

	@Autowired
	private GuildBuildersRepositoryDay guildBuildersReposDay;

	@Autowired
	private GuildBuildersRepositoryMon guildBuildersReposMon;

	@Autowired
	private GuildBuildersRepositoryHist guildBuildersReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(guildBuildersRepos);
		org.junit.Assert.assertNotNull(guildBuildersReposDay);
		org.junit.Assert.assertNotNull(guildBuildersReposMon);
		org.junit.Assert.assertNotNull(guildBuildersReposHist);
	}

	@Override
	public GuildBuilders findById(int custNo, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + custNo);
		Optional<GuildBuilders> guildBuilders = null;
		if (dbName.equals(ContentName.onDay))
			guildBuilders = guildBuildersReposDay.findById(custNo);
		else if (dbName.equals(ContentName.onMon))
			guildBuilders = guildBuildersReposMon.findById(custNo);
		else if (dbName.equals(ContentName.onHist))
			guildBuilders = guildBuildersReposHist.findById(custNo);
		else
			guildBuilders = guildBuildersRepos.findById(custNo);
		GuildBuilders obj = guildBuilders.isPresent() ? guildBuilders.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<GuildBuilders> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<GuildBuilders> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustNo"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustNo"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = guildBuildersReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = guildBuildersReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = guildBuildersReposHist.findAll(pageable);
		else
			slice = guildBuildersRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public GuildBuilders holdById(int custNo, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + custNo);
		Optional<GuildBuilders> guildBuilders = null;
		if (dbName.equals(ContentName.onDay))
			guildBuilders = guildBuildersReposDay.findByCustNo(custNo);
		else if (dbName.equals(ContentName.onMon))
			guildBuilders = guildBuildersReposMon.findByCustNo(custNo);
		else if (dbName.equals(ContentName.onHist))
			guildBuilders = guildBuildersReposHist.findByCustNo(custNo);
		else
			guildBuilders = guildBuildersRepos.findByCustNo(custNo);
		return guildBuilders.isPresent() ? guildBuilders.get() : null;
	}

	@Override
	public GuildBuilders holdById(GuildBuilders guildBuilders, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + guildBuilders.getCustNo());
		Optional<GuildBuilders> guildBuildersT = null;
		if (dbName.equals(ContentName.onDay))
			guildBuildersT = guildBuildersReposDay.findByCustNo(guildBuilders.getCustNo());
		else if (dbName.equals(ContentName.onMon))
			guildBuildersT = guildBuildersReposMon.findByCustNo(guildBuilders.getCustNo());
		else if (dbName.equals(ContentName.onHist))
			guildBuildersT = guildBuildersReposHist.findByCustNo(guildBuilders.getCustNo());
		else
			guildBuildersT = guildBuildersRepos.findByCustNo(guildBuilders.getCustNo());
		return guildBuildersT.isPresent() ? guildBuildersT.get() : null;
	}

	@Override
	public GuildBuilders insert(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + guildBuilders.getCustNo());
		if (this.findById(guildBuilders.getCustNo()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			guildBuilders.setCreateEmpNo(empNot);

		if (guildBuilders.getLastUpdateEmpNo() == null || guildBuilders.getLastUpdateEmpNo().isEmpty())
			guildBuilders.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return guildBuildersReposDay.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onMon))
			return guildBuildersReposMon.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onHist))
			return guildBuildersReposHist.saveAndFlush(guildBuilders);
		else
			return guildBuildersRepos.saveAndFlush(guildBuilders);
	}

	@Override
	public GuildBuilders update(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + guildBuilders.getCustNo());
		if (!empNot.isEmpty())
			guildBuilders.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return guildBuildersReposDay.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onMon))
			return guildBuildersReposMon.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onHist))
			return guildBuildersReposHist.saveAndFlush(guildBuilders);
		else
			return guildBuildersRepos.saveAndFlush(guildBuilders);
	}

	@Override
	public GuildBuilders update2(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + guildBuilders.getCustNo());
		if (!empNot.isEmpty())
			guildBuilders.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			guildBuildersReposDay.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onMon))
			guildBuildersReposMon.saveAndFlush(guildBuilders);
		else if (dbName.equals(ContentName.onHist))
			guildBuildersReposHist.saveAndFlush(guildBuilders);
		else
			guildBuildersRepos.saveAndFlush(guildBuilders);
		return this.findById(guildBuilders.getCustNo());
	}

	@Override
	public void delete(GuildBuilders guildBuilders, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + guildBuilders.getCustNo());
		if (dbName.equals(ContentName.onDay)) {
			guildBuildersReposDay.delete(guildBuilders);
			guildBuildersReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			guildBuildersReposMon.delete(guildBuilders);
			guildBuildersReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			guildBuildersReposHist.delete(guildBuilders);
			guildBuildersReposHist.flush();
		} else {
			guildBuildersRepos.delete(guildBuilders);
			guildBuildersRepos.flush();
		}
	}

	@Override
	public void insertAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException {
		if (guildBuilders == null || guildBuilders.size() == 0)
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
		for (GuildBuilders t : guildBuilders) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			guildBuilders = guildBuildersReposDay.saveAll(guildBuilders);
			guildBuildersReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			guildBuilders = guildBuildersReposMon.saveAll(guildBuilders);
			guildBuildersReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			guildBuilders = guildBuildersReposHist.saveAll(guildBuilders);
			guildBuildersReposHist.flush();
		} else {
			guildBuilders = guildBuildersRepos.saveAll(guildBuilders);
			guildBuildersRepos.flush();
		}
	}

	@Override
	public void updateAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (guildBuilders == null || guildBuilders.size() == 0)
			throw new DBException(6);

		for (GuildBuilders t : guildBuilders)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			guildBuilders = guildBuildersReposDay.saveAll(guildBuilders);
			guildBuildersReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			guildBuilders = guildBuildersReposMon.saveAll(guildBuilders);
			guildBuildersReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			guildBuilders = guildBuildersReposHist.saveAll(guildBuilders);
			guildBuildersReposHist.flush();
		} else {
			guildBuilders = guildBuildersRepos.saveAll(guildBuilders);
			guildBuildersRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<GuildBuilders> guildBuilders, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (guildBuilders == null || guildBuilders.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			guildBuildersReposDay.deleteAll(guildBuilders);
			guildBuildersReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			guildBuildersReposMon.deleteAll(guildBuilders);
			guildBuildersReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			guildBuildersReposHist.deleteAll(guildBuilders);
			guildBuildersReposHist.flush();
		} else {
			guildBuildersRepos.deleteAll(guildBuilders);
			guildBuildersRepos.flush();
		}
	}

}
