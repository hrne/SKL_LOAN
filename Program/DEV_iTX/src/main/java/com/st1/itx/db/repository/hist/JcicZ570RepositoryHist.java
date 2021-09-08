package com.st1.itx.db.repository.hist;


import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

import com.st1.itx.db.domain.JcicZ570;
import com.st1.itx.db.domain.JcicZ570Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ570RepositoryHist extends JpaRepository<JcicZ570, JcicZ570Id> {

  // CustId=
  public Slice<JcicZ570> findAllByCustIdIsOrderByCustIdAscApplyDateDesc(String custId_0, Pageable pageable);

  // ApplyDate=
  public Slice<JcicZ570> findAllByApplyDateIsOrderByCustIdAscApplyDateDesc(int applyDate_0, Pageable pageable);

  // CustId= , AND ApplyDate=
  public Slice<JcicZ570> findAllByCustIdIsAndApplyDateIsOrderByCustIdAscApplyDateDesc(String custId_0, int applyDate_1, Pageable pageable);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Slice<JcicZ570> findAllByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2, Pageable pageable);

  // Ukey=
  public Optional<JcicZ570> findTopByUkeyIs(String ukey_0);

  // CustId=, AND ApplyDate = ,AND SubmitKey = 
  public Optional<JcicZ570> findTopByCustIdIsAndApplyDateIsAndSubmitKeyIsOrderByCreateDateDesc(String custId_0, int applyDate_1, String submitKey_2);

  // CustId=, AND Bank1=
  public Slice<JcicZ570> findAllByCustIdIsAndBank1Is(String custId_0, String bank1_1, Pageable pageable);

  // CustId=, AND Bank2=
  public Slice<JcicZ570> findAllByCustIdIsAndBank2Is(String custId_0, String bank2_1, Pageable pageable);

  // CustId=, AND Bank3=
  public Slice<JcicZ570> findAllByCustIdIsAndBank3Is(String custId_0, String bank3_1, Pageable pageable);

  // CustId=, AND Bank4=
  public Slice<JcicZ570> findAllByCustIdIsAndBank4Is(String custId_0, String bank4_1, Pageable pageable);

  // CustId=, AND Bank5=
  public Slice<JcicZ570> findAllByCustIdIsAndBank5Is(String custId_0, String bank5_1, Pageable pageable);

  // CustId=, AND Bank6=
  public Slice<JcicZ570> findAllByCustIdIsAndBank6Is(String custId_0, String bank6_1, Pageable pageable);

  // CustId=, AND Bank7=
  public Slice<JcicZ570> findAllByCustIdIsAndBank7Is(String custId_0, String bank7_1, Pageable pageable);

  // CustId=, AND Bank8=
  public Slice<JcicZ570> findAllByCustIdIsAndBank8Is(String custId_0, String bank8_1, Pageable pageable);

  // CustId=, AND Bank9=
  public Slice<JcicZ570> findAllByCustIdIsAndBank9Is(String custId_0, String bank9_1, Pageable pageable);

  // CustId=, AND Bank10=
  public Slice<JcicZ570> findAllByCustIdIsAndBank10Is(String custId_0, String bank10_1, Pageable pageable);

  // CustId=, AND Bank11=
  public Slice<JcicZ570> findAllByCustIdIsAndBank11Is(String custId_0, String bank11_1, Pageable pageable);

  // CustId=, AND Bank12=
  public Slice<JcicZ570> findAllByCustIdIsAndBank12Is(String custId_0, String bank12_1, Pageable pageable);

  // CustId=, AND Bank13=
  public Slice<JcicZ570> findAllByCustIdIsAndBank13Is(String custId_0, String bank13_1, Pageable pageable);

  // CustId=, AND Bank14=
  public Slice<JcicZ570> findAllByCustIdIsAndBank14Is(String custId_0, String bank14_1, Pageable pageable);

  // CustId=, AND Bank15=
  public Slice<JcicZ570> findAllByCustIdIsAndBank15Is(String custId_0, String bank15_1, Pageable pageable);

  // CustId=, AND Bank16=
  public Slice<JcicZ570> findAllByCustIdIsAndBank16Is(String custId_0, String bank16_1, Pageable pageable);

  // CustId=, AND Bank17=
  public Slice<JcicZ570> findAllByCustIdIsAndBank17Is(String custId_0, String bank17_1, Pageable pageable);

  // CustId=, AND Bank18=
  public Slice<JcicZ570> findAllByCustIdIsAndBank18Is(String custId_0, String bank18_1, Pageable pageable);

  // CustId=, AND Bank19=
  public Slice<JcicZ570> findAllByCustIdIsAndBank19Is(String custId_0, String bank19_1, Pageable pageable);

  // CustId=, AND Bank20=
  public Slice<JcicZ570> findAllByCustIdIsAndBank20Is(String custId_0, String bank20_1, Pageable pageable);

  // CustId=, AND Bank21=
  public Slice<JcicZ570> findAllByCustIdIsAndBank21Is(String custId_0, String bank21_1, Pageable pageable);

  // CustId=, AND Bank22=
  public Slice<JcicZ570> findAllByCustIdIsAndBank22Is(String custId_0, String bank22_1, Pageable pageable);

  // CustId=, AND Bank23=
  public Slice<JcicZ570> findAllByCustIdIsAndBank23Is(String custId_0, String bank23_1, Pageable pageable);

  // CustId=, AND Bank24=
  public Slice<JcicZ570> findAllByCustIdIsAndBank24Is(String custId_0, String bank24_1, Pageable pageable);

  // CustId=, AND Bank25=
  public Slice<JcicZ570> findAllByCustIdIsAndBank25Is(String custId_0, String bank25_1, Pageable pageable);

  // CustId=, AND Bank26=
  public Slice<JcicZ570> findAllByCustIdIsAndBank26Is(String custId_0, String bank26_1, Pageable pageable);

  // CustId=, AND Bank27=
  public Slice<JcicZ570> findAllByCustIdIsAndBank27Is(String custId_0, String bank27_1, Pageable pageable);

  // CustId=, AND Bank28=
  public Slice<JcicZ570> findAllByCustIdIsAndBank28Is(String custId_0, String bank28_1, Pageable pageable);

  // CustId=, AND Bank29=
  public Slice<JcicZ570> findAllByCustIdIsAndBank29Is(String custId_0, String bank29_1, Pageable pageable);

  // CustId=, AND Bank30=
  public Slice<JcicZ570> findAllByCustIdIsAndBank30Is(String custId_0, String bank30_1, Pageable pageable);

  // Hold
  @Lock(value = LockModeType.PESSIMISTIC_READ)
  @Transactional(readOnly = false)
  public Optional<JcicZ570> findByJcicZ570Id(JcicZ570Id jcicZ570Id);

}

