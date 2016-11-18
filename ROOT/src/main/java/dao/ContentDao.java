package dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import utils.Content;

public interface ContentDao {
	
	@Result(property = "abs", column = "abstract")
	@Select("select * from content")
	public List<Content> getContentList();
	
	@Result(property = "abs", column = "abstract")
	@Select("select * from content where id=#{id}")
	public Content getContentById(int id);
	
	@Select("select price from content where id=#{id}")
	public int getPriceById(int id);
	
	
	@Delete("delete from content where id=#{id}")
	public void deleteById(int id);
	
	@Result(property = "abs", column = "abstract")
	@Select("select * from content where title=#{title}")
	public Content getContentByTitle(@Param("title") String title);
	
	@Insert("insert into content(price,title,icon,abstract,text) values(#{price},#{title},#{icon},#{abstract},#{text})")
	public void insert(@Param("price") int price,@Param("title") String title,@Param("icon") String icon,@Param("abstract") String abs,@Param("text") String text);

	@Update("update content set price=#{price},title=#{title},icon=#{icon},abstract=#{abstract},text=#{text} where id=#{id}")
	public void update(@Param("id") int id,@Param("price") int price,@Param("title") String title,@Param("icon") String icon,@Param("abstract") String abs,@Param("text") String text);
}
