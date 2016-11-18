package dao;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import utils.*;

public interface PersonDao {
	@Select("select * from person where userName=#{userName}")
	public Person getPerson(String userName);
	
	@Select("select id from person where userName=#{userName}")
	public int getIdByUserName(String userName);
	
	@Select("select * from person")
	public List<Person> getPersonList();
}
