package com.st1.itx.db.service;

import java.util.List;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JcicZ570;
import org.springframework.data.domain.Slice;
import com.st1.itx.db.domain.JcicZ570Id;

/**
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
public interface JcicZ570Service {

  /**
   * findByPrimaryKey
   *
   * @param jcicZ570Id PK
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 JcicZ570
   */
  public JcicZ570 findById(JcicZ570Id jcicZ570Id, TitaVo... titaVo);

  /**
   * findAll
   *
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findAll(int index, int limit, TitaVo... titaVo);

  /**
   * CustId=
   *
   * @param custId_0 custId_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> custIdEq(String custId_0, int index, int limit, TitaVo... titaVo);

  /**
   * ApplyDate=
   *
   * @param applyDate_0 applyDate_0
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> rcDateEq(int applyDate_0, int index, int limit, TitaVo... titaVo);

  /**
   * CustId= , AND ApplyDate=
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> custRcEq(String custId_0, int applyDate_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> otherEq(String custId_0, int applyDate_1, String submitKey_2, int index, int limit, TitaVo... titaVo);

  /**
   * Ukey=
   *
   * @param ukey_0 ukey_0
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public JcicZ570 ukeyFirst(String ukey_0, TitaVo... titaVo);

  /**
   * CustId=, AND ApplyDate = ,AND SubmitKey = 
   *
   * @param custId_0 custId_0
   * @param applyDate_1 applyDate_1
   * @param submitKey_2 submitKey_2
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public JcicZ570 otherFirst(String custId_0, int applyDate_1, String submitKey_2, TitaVo... titaVo);

  /**
   * CustId=, AND Bank1=
   *
   * @param custId_0 custId_0
   * @param bank1_1 bank1_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank1(String custId_0, String bank1_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank2=
   *
   * @param custId_0 custId_0
   * @param bank2_1 bank2_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank2(String custId_0, String bank2_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank3=
   *
   * @param custId_0 custId_0
   * @param bank3_1 bank3_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank3(String custId_0, String bank3_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank4=
   *
   * @param custId_0 custId_0
   * @param bank4_1 bank4_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank4(String custId_0, String bank4_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank5=
   *
   * @param custId_0 custId_0
   * @param bank5_1 bank5_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank5(String custId_0, String bank5_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank6=
   *
   * @param custId_0 custId_0
   * @param bank6_1 bank6_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank6(String custId_0, String bank6_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank7=
   *
   * @param custId_0 custId_0
   * @param bank7_1 bank7_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank7(String custId_0, String bank7_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank8=
   *
   * @param custId_0 custId_0
   * @param bank8_1 bank8_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank8(String custId_0, String bank8_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank9=
   *
   * @param custId_0 custId_0
   * @param bank9_1 bank9_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank9(String custId_0, String bank9_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank10=
   *
   * @param custId_0 custId_0
   * @param bank10_1 bank10_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank10(String custId_0, String bank10_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank11=
   *
   * @param custId_0 custId_0
   * @param bank11_1 bank11_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank11(String custId_0, String bank11_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank12=
   *
   * @param custId_0 custId_0
   * @param bank12_1 bank12_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank12(String custId_0, String bank12_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank13=
   *
   * @param custId_0 custId_0
   * @param bank13_1 bank13_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank13(String custId_0, String bank13_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank14=
   *
   * @param custId_0 custId_0
   * @param bank14_1 bank14_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank14(String custId_0, String bank14_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank15=
   *
   * @param custId_0 custId_0
   * @param bank15_1 bank15_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank15(String custId_0, String bank15_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank16=
   *
   * @param custId_0 custId_0
   * @param bank16_1 bank16_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank16(String custId_0, String bank16_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank17=
   *
   * @param custId_0 custId_0
   * @param bank17_1 bank17_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank17(String custId_0, String bank17_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank18=
   *
   * @param custId_0 custId_0
   * @param bank18_1 bank18_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank18(String custId_0, String bank18_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank19=
   *
   * @param custId_0 custId_0
   * @param bank19_1 bank19_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank19(String custId_0, String bank19_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank20=
   *
   * @param custId_0 custId_0
   * @param bank20_1 bank20_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank20(String custId_0, String bank20_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank21=
   *
   * @param custId_0 custId_0
   * @param bank21_1 bank21_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank21(String custId_0, String bank21_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank22=
   *
   * @param custId_0 custId_0
   * @param bank22_1 bank22_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank22(String custId_0, String bank22_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank23=
   *
   * @param custId_0 custId_0
   * @param bank23_1 bank23_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank23(String custId_0, String bank23_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank24=
   *
   * @param custId_0 custId_0
   * @param bank24_1 bank24_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank24(String custId_0, String bank24_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank25=
   *
   * @param custId_0 custId_0
   * @param bank25_1 bank25_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank25(String custId_0, String bank25_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank26=
   *
   * @param custId_0 custId_0
   * @param bank26_1 bank26_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank26(String custId_0, String bank26_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank27=
   *
   * @param custId_0 custId_0
   * @param bank27_1 bank27_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank27(String custId_0, String bank27_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank28=
   *
   * @param custId_0 custId_0
   * @param bank28_1 bank28_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank28(String custId_0, String bank28_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank29=
   *
   * @param custId_0 custId_0
   * @param bank29_1 bank29_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank29(String custId_0, String bank29_1, int index, int limit, TitaVo... titaVo);

  /**
   * CustId=, AND Bank30=
   *
   * @param custId_0 custId_0
   * @param bank30_1 bank30_1
   * @param index Page Index
   * @param limit Page Data Limit
   * @param titaVo Variable-Length Argument
   * @return Slice JcicZ570 JcicZ570 of List
   */
  public Slice<JcicZ570> findByBank30(String custId_0, String bank30_1, int index, int limit, TitaVo... titaVo);

  /**
   * hold By JcicZ570
   * 
   * @param jcicZ570Id key
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 JcicZ570
   */
  public JcicZ570 holdById(JcicZ570Id jcicZ570Id, TitaVo... titaVo);

  /**
   * hold By JcicZ570
   * 
   * @param jcicZ570 key
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 JcicZ570
   */
  public JcicZ570 holdById(JcicZ570 jcicZ570, TitaVo... titaVo);

  /**
   * Insert
   * 
   * @param jcicZ570 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 Entity
   * @throws DBException exception
   */
  public JcicZ570 insert(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Update
   * 
   * @param jcicZ570 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 Entity
   * @throws DBException exception
   */
  public JcicZ570 update(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Update2
   * 
   * @param jcicZ570 Entity
   * @param titaVo Variable-Length Argument
   * @return JcicZ570 Entity
   * @throws DBException exception
   */
  public JcicZ570 update2(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Delete
   * 
   * @param jcicZ570 Entity
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void delete(JcicZ570 jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Insert All For List
   * 
   * @param jcicZ570 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void insertAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Update All For List
   * 
   * @param jcicZ570 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void updateAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

  /**
   * Delete All For List
   * 
   * @param jcicZ570 Entity of List
   * @param titaVo Variable-Length Argument
   * @throws DBException exception
   */
  public void deleteAll(List<JcicZ570> jcicZ570, TitaVo... titaVo) throws DBException;

}
