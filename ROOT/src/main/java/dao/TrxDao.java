package dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import utils.Trx;

public interface TrxDao {
	@Select("select * from trx where personId=#{personId}")
	public List<Trx> getTrxByPerson(int personId);
	
	@Select("select * from trx")
	public List<Trx> geTrxList();
	
	@Select("select * from trx where contentId=#{contentId}")
	public Trx geTrxByContentId(@Param("contentId") int contentId);
	
	@Insert("insert into trx(contentId,personId,price,time) values(#{contentId},#{personId},#{price},#{time})")
	public void insert(@Param("contentId") int contentId,@Param("personId") int personId,@Param("price") int price, @Param("time") long time);
}
