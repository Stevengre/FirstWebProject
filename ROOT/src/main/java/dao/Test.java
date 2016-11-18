package dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import utils.*;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		
		/*TrxDao dao = context.getBean("trxDao", TrxDao.class);
		int contentId = 1;
		int personId = 2;
		int price = 3;
		dao.insert(contentId, personId, price);
		List<Trx> persons = dao.geTrxList();
		for (Trx person : persons) {
			System.out.println(person.getPrice());
		}*/
		
		PersonDao dao = context.getBean("personDao",PersonDao.class);
		List<Person> persons = dao.getPersonList();
		for(Person person : persons){
			System.out.println(person.getUserName());
		}
		
		((ConfigurableApplicationContext) context).close();
	}

}
