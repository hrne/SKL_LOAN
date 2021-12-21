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
import com.st1.itx.db.domain.BankRelationSelf;
import com.st1.itx.db.domain.BankRelationSelfId;
import com.st1.itx.db.repository.online.BankRelationSelfRepository;
import com.st1.itx.db.repository.day.BankRelationSelfRepositoryDay;
import com.st1.itx.db.repository.mon.BankRelationSelfRepositoryMon;
import com.st1.itx.db.repository.hist.BankRelationSelfRepositoryHist;
import com.st1.itx.db.service.BankRelationSelfService;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Service("bankRelationSelfService")
@Repository
public class BankRelationSelfServiceImpl extends ASpringJpaParm implements BankRelationSelfService, InitializingBean {
	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private BankRelationSelfRepository bankRelationSelfRepos;

	@Autowired
	private BankRelationSelfRepositoryDay bankRelationSelfReposDay;

	@Autowired
	private BankRelationSelfRepositoryMon bankRelationSelfReposMon;

	@Autowired
	private BankRelationSelfRepositoryHist bankRelationSelfReposHist;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(bankRelationSelfRepos);
		org.junit.Assert.assertNotNull(bankRelationSelfReposDay);
		org.junit.Assert.assertNotNull(bankRelationSelfReposMon);
		org.junit.Assert.assertNotNull(bankRelationSelfReposHist);
	}

	@Override
	public BankRelationSelf findById(BankRelationSelfId bankRelationSelfId, TitaVo... titaVo) {
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("findById " + dbName + " " + bankRelationSelfId);
		Optional<BankRelationSelf> bankRelationSelf = null;
		if (dbName.equals(ContentName.onDay))
			bankRelationSelf = bankRelationSelfReposDay.findById(bankRelationSelfId);
		else if (dbName.equals(ContentName.onMon))
			bankRelationSelf = bankRelationSelfReposMon.findById(bankRelationSelfId);
		else if (dbName.equals(ContentName.onHist))
			bankRelationSelf = bankRelationSelfReposHist.findById(bankRelationSelfId);
		else
			bankRelationSelf = bankRelationSelfRepos.findById(bankRelationSelfId);
		BankRelationSelf obj = bankRelationSelf.isPresent() ? bankRelationSelf.get() : null;
		if (obj != null) {
			EntityManager em = this.baseEntityManager.getCurrentEntityManager(dbName);
			em.detach(obj);
			em = null;
		}
		return obj;
	}

	@Override
	public Slice<BankRelationSelf> findAll(int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<BankRelationSelf> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;
		if (limit == Integer.MAX_VALUE)
			pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.ASC, "CustName", "CustId"));
		else
			pageable = PageRequest.of(index, limit, Sort.by(Sort.Direction.ASC, "CustName", "CustId"));
		this.info("findAll " + dbName);
		if (dbName.equals(ContentName.onDay))
			slice = bankRelationSelfReposDay.findAll(pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = bankRelationSelfReposMon.findAll(pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = bankRelationSelfReposHist.findAll(pageable);
		else
			slice = bankRelationSelfRepos.findAll(pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public Slice<BankRelationSelf> findCustIdEq(String custId_0, int index, int limit, TitaVo... titaVo) {
		String dbName = "";
		Slice<BankRelationSelf> slice = null;
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		Pageable pageable = null;

		if (limit == Integer.MAX_VALUE)
			pageable = Pageable.unpaged();
		else
			pageable = PageRequest.of(index, limit);
		this.info("findCustIdEq " + dbName + " : " + "custId_0 : " + custId_0);
		if (dbName.equals(ContentName.onDay))
			slice = bankRelationSelfReposDay.findAllByCustIdIs(custId_0, pageable);
		else if (dbName.equals(ContentName.onMon))
			slice = bankRelationSelfReposMon.findAllByCustIdIs(custId_0, pageable);
		else if (dbName.equals(ContentName.onHist))
			slice = bankRelationSelfReposHist.findAllByCustIdIs(custId_0, pageable);
		else
			slice = bankRelationSelfRepos.findAllByCustIdIs(custId_0, pageable);

		if (slice != null)
			this.baseEntityManager.clearEntityManager(dbName);

		return slice != null && !slice.isEmpty() ? slice : null;
	}

	@Override
	public BankRelationSelf holdById(BankRelationSelfId bankRelationSelfId, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + bankRelationSelfId);
		Optional<BankRelationSelf> bankRelationSelf = null;
		if (dbName.equals(ContentName.onDay))
			bankRelationSelf = bankRelationSelfReposDay.findByBankRelationSelfId(bankRelationSelfId);
		else if (dbName.equals(ContentName.onMon))
			bankRelationSelf = bankRelationSelfReposMon.findByBankRelationSelfId(bankRelationSelfId);
		else if (dbName.equals(ContentName.onHist))
			bankRelationSelf = bankRelationSelfReposHist.findByBankRelationSelfId(bankRelationSelfId);
		else
			bankRelationSelf = bankRelationSelfRepos.findByBankRelationSelfId(bankRelationSelfId);
		return bankRelationSelf.isPresent() ? bankRelationSelf.get() : null;
	}

	@Override
	public BankRelationSelf holdById(BankRelationSelf bankRelationSelf, TitaVo... titaVo) {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Hold " + dbName + " " + bankRelationSelf.getBankRelationSelfId());
		Optional<BankRelationSelf> bankRelationSelfT = null;
		if (dbName.equals(ContentName.onDay))
			bankRelationSelfT = bankRelationSelfReposDay.findByBankRelationSelfId(bankRelationSelf.getBankRelationSelfId());
		else if (dbName.equals(ContentName.onMon))
			bankRelationSelfT = bankRelationSelfReposMon.findByBankRelationSelfId(bankRelationSelf.getBankRelationSelfId());
		else if (dbName.equals(ContentName.onHist))
			bankRelationSelfT = bankRelationSelfReposHist.findByBankRelationSelfId(bankRelationSelf.getBankRelationSelfId());
		else
			bankRelationSelfT = bankRelationSelfRepos.findByBankRelationSelfId(bankRelationSelf.getBankRelationSelfId());
		return bankRelationSelfT.isPresent() ? bankRelationSelfT.get() : null;
	}

	@Override
	public BankRelationSelf insert(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
			empNot = empNot.isEmpty() ? "System" : empNot;
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Insert..." + dbName + " " + bankRelationSelf.getBankRelationSelfId());
		if (this.findById(bankRelationSelf.getBankRelationSelfId()) != null)
			throw new DBException(2);

		if (!empNot.isEmpty())
			bankRelationSelf.setCreateEmpNo(empNot);

		if (bankRelationSelf.getLastUpdateEmpNo() == null || bankRelationSelf.getLastUpdateEmpNo().isEmpty())
			bankRelationSelf.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return bankRelationSelfReposDay.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onMon))
			return bankRelationSelfReposMon.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onHist))
			return bankRelationSelfReposHist.saveAndFlush(bankRelationSelf);
		else
			return bankRelationSelfRepos.saveAndFlush(bankRelationSelf);
	}

	@Override
	public BankRelationSelf update(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + bankRelationSelf.getBankRelationSelfId());
		if (!empNot.isEmpty())
			bankRelationSelf.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			return bankRelationSelfReposDay.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onMon))
			return bankRelationSelfReposMon.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onHist))
			return bankRelationSelfReposHist.saveAndFlush(bankRelationSelf);
		else
			return bankRelationSelfRepos.saveAndFlush(bankRelationSelf);
	}

	@Override
	public BankRelationSelf update2(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("Update..." + dbName + " " + bankRelationSelf.getBankRelationSelfId());
		if (!empNot.isEmpty())
			bankRelationSelf.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay))
			bankRelationSelfReposDay.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onMon))
			bankRelationSelfReposMon.saveAndFlush(bankRelationSelf);
		else if (dbName.equals(ContentName.onHist))
			bankRelationSelfReposHist.saveAndFlush(bankRelationSelf);
		else
			bankRelationSelfRepos.saveAndFlush(bankRelationSelf);
		return this.findById(bankRelationSelf.getBankRelationSelfId());
	}

	@Override
	public void delete(BankRelationSelf bankRelationSelf, TitaVo... titaVo) throws DBException {
		String dbName = "";
		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		this.info("Delete..." + dbName + " " + bankRelationSelf.getBankRelationSelfId());
		if (dbName.equals(ContentName.onDay)) {
			bankRelationSelfReposDay.delete(bankRelationSelf);
			bankRelationSelfReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			bankRelationSelfReposMon.delete(bankRelationSelf);
			bankRelationSelfReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			bankRelationSelfReposHist.delete(bankRelationSelf);
			bankRelationSelfReposHist.flush();
		} else {
			bankRelationSelfRepos.delete(bankRelationSelf);
			bankRelationSelfRepos.flush();
		}
	}

	@Override
	public void insertAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException {
		if (bankRelationSelf == null || bankRelationSelf.size() == 0)
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
		for (BankRelationSelf t : bankRelationSelf) {
			if (!empNot.isEmpty())
				t.setCreateEmpNo(empNot);
			if (t.getLastUpdateEmpNo() == null || t.getLastUpdateEmpNo().isEmpty())
				t.setLastUpdateEmpNo(empNot);
		}

		if (dbName.equals(ContentName.onDay)) {
			bankRelationSelf = bankRelationSelfReposDay.saveAll(bankRelationSelf);
			bankRelationSelfReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			bankRelationSelf = bankRelationSelfReposMon.saveAll(bankRelationSelf);
			bankRelationSelfReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			bankRelationSelf = bankRelationSelfReposHist.saveAll(bankRelationSelf);
			bankRelationSelfReposHist.flush();
		} else {
			bankRelationSelf = bankRelationSelfRepos.saveAll(bankRelationSelf);
			bankRelationSelfRepos.flush();
		}
	}

	@Override
	public void updateAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException {
		String dbName = "";
		String empNot = "";

		if (titaVo.length != 0) {
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
			empNot = titaVo[0].getEmpNot() != null ? titaVo[0].getEmpNot() : "";
		} else
			empNot = ThreadVariable.getEmpNot();

		this.info("UpdateAll...");
		if (bankRelationSelf == null || bankRelationSelf.size() == 0)
			throw new DBException(6);

		for (BankRelationSelf t : bankRelationSelf)
			if (!empNot.isEmpty())
				t.setLastUpdateEmpNo(empNot);

		if (dbName.equals(ContentName.onDay)) {
			bankRelationSelf = bankRelationSelfReposDay.saveAll(bankRelationSelf);
			bankRelationSelfReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			bankRelationSelf = bankRelationSelfReposMon.saveAll(bankRelationSelf);
			bankRelationSelfReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			bankRelationSelf = bankRelationSelfReposHist.saveAll(bankRelationSelf);
			bankRelationSelfReposHist.flush();
		} else {
			bankRelationSelf = bankRelationSelfRepos.saveAll(bankRelationSelf);
			bankRelationSelfRepos.flush();
		}
	}

	@Override
	public void deleteAll(List<BankRelationSelf> bankRelationSelf, TitaVo... titaVo) throws DBException {
		this.info("DeleteAll...");
		String dbName = "";

		if (titaVo.length != 0)
			dbName = titaVo[0].getDataBase() != null ? titaVo[0].getDataBase() : ContentName.onLine;
		if (bankRelationSelf == null || bankRelationSelf.size() == 0)
			throw new DBException(6);
		if (dbName.equals(ContentName.onDay)) {
			bankRelationSelfReposDay.deleteAll(bankRelationSelf);
			bankRelationSelfReposDay.flush();
		} else if (dbName.equals(ContentName.onMon)) {
			bankRelationSelfReposMon.deleteAll(bankRelationSelf);
			bankRelationSelfReposMon.flush();
		} else if (dbName.equals(ContentName.onHist)) {
			bankRelationSelfReposHist.deleteAll(bankRelationSelf);
			bankRelationSelfReposHist.flush();
		} else {
			bankRelationSelfRepos.deleteAll(bankRelationSelf);
			bankRelationSelfRepos.flush();
		}
	}

}
