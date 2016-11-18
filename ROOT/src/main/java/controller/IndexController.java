package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dao.ContentDao;
import dao.PersonDao;
import dao.TrxDao;
import utils.BuyList;
import utils.Content;
import utils.Person;
import utils.Product;
import utils.ProductList;
import utils.Trx;
import utils.User;

@Controller
public class IndexController {
	
	//测试用的页面
	@RequestMapping(value="/spring")
	public void spring(HttpServletResponse response) throws IOException {
		response.getWriter().write("Hello!");
	}
	
	//测试用的页面
	@RequestMapping(value="/test")
	public String test(HttpServletRequest hsr, HttpServletResponse hsr1, ModelMap map) throws IOException {
		map.addAttribute("title", "Spring MVC");
		map.addAttribute("content", "hello");
		return "hello";
	}
	
	//返回login页面
	@RequestMapping(value="/login")
	public String Login(){
		return "login";
	}
	
	//login的逻辑跳转页面实现
	@RequestMapping(value="/api/login" , method=RequestMethod.POST)
	public ModelMap LoginCheck(HttpServletRequest request,@RequestParam("userName") String userName,@RequestParam("password") String password){
		ModelMap map = new ModelMap();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		PersonDao dao = context.getBean("personDao",PersonDao.class);
		List<Person> persons = dao.getPersonList();
		for(Person person : persons){
			if ((userName.equals(person.getUserName()) && (password.equals(person.getPassword())))) {
				
				map.addAttribute("code", 200);
				map.addAttribute("message", "OK");
				map.addAttribute("result", true);
				
				HttpSession session = request.getSession();
				session.setAttribute("userName", person.getUserName());
				session.setAttribute("userType", person.getUserType());
				((ConfigurableApplicationContext) context).close();
				return map;
			}
		}

		map.addAttribute("code", 202);
		map.addAttribute("message", "账户或密码错误");
		map.addAttribute("result", true);
		((ConfigurableApplicationContext) context).close();
		return map;
	}
	
	//logout动作
	@RequestMapping(value="/logout")
	public String Logout(HttpServletRequest request){
		request.getSession().invalidate();
		return "login";
	}
	
	//根据求返回index页面
	@RequestMapping(value="/")
	public String index(HttpServletRequest request,ModelMap map){
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ContentDao dao = context.getBean("contentDao",ContentDao.class);
		List<Content> contents = dao.getContentList();
		List<ProductList> productList = new ArrayList<ProductList>();
		for (Content content : contents) {
			ProductList productList2 = new ProductList();
			productList2.setId(content.getId());
			productList2.setImage(content.getIcon());
			productList2.setPrice(content.getPrice());
			productList2.setTitle(content.getTitle());
			productList.add(productList2);
		}
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
			TrxDao dao2 = context.getBean("trxDao", TrxDao.class);
			
			List<Trx> trxs = dao2.geTrxList();
			for(Trx trx : trxs){
				int index = 0;
				for(ProductList productList2 : productList){
				if (productList2.getId()==trx.getContentId()) {
						productList2.setIsBuy(true);
						productList2.setIsSell(true);
						productList.set(index, productList2);
					}
					index++;
				}
			}
		}
		
		
		map.addAttribute("productList", productList);
		((ConfigurableApplicationContext) context).close();
		return "index";
	}
	
	//删除商品
	@RequestMapping(value="/api/delete" , method=RequestMethod.POST)
	public ModelMap Delete(@RequestParam("id") int id){
		ModelMap map = new ModelMap();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		TrxDao trxDao = context.getBean("trxDao", TrxDao.class);
		if (trxDao.geTrxByContentId(id) == null) {
			ContentDao contentDao = context.getBean("contentDao", ContentDao.class);
			contentDao.deleteById(id);
			map.addAttribute("code", 200);
			map.addAttribute("message", "OK");
			map.addAttribute("result", true);
			((ConfigurableApplicationContext) context).close();
			return map;
		}else {
			map.addAttribute("code", 202);
			map.addAttribute("message", "无法删除");
			map.addAttribute("result", true);
			((ConfigurableApplicationContext) context).close();
			return map;
		}
	}
	
	//根据求返回show页面
	@RequestMapping(value="/show")
	public String show(HttpServletRequest request, ModelMap map) throws UnsupportedEncodingException{
		Product product = new Product();
		product.setId(Integer.valueOf(request.getParameter("id")));
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ContentDao dao = context.getBean("contentDao",ContentDao.class);
		Content content = dao.getContentById(product.getId());
		product.setImage(content.getIcon());
		product.setTitle(content.getTitle());
		product.setSummary(content.getAbs());
		if(content.getText() != null)
			product.setDetail(content.getText());
		product.setPrice(content.getPrice());
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
			TrxDao dao2 = context.getBean("trxDao", TrxDao.class);
			
			Trx trx = dao2.geTrxByContentId(product.getId());
			if (trx != null) {
				product.setBuyPrice(trx.getPrice());
				product.setIsBuy(true);
				product.setIsSell(true);
			}
		}
		
		map.addAttribute("product", product);
		((ConfigurableApplicationContext) context).close();
		return "show";
	}
	
	//购买商品
	@RequestMapping(value="/api/buy" , method=RequestMethod.POST)
	public ModelMap Buy(@RequestParam("id") int id, HttpServletRequest request){
		ModelMap map = new ModelMap();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		TrxDao trxDao = context.getBean("trxDao", TrxDao.class);
		if (trxDao.geTrxByContentId(id) == null) {
			ContentDao contentDao = context.getBean("contentDao", ContentDao.class);
			int price = contentDao.getPriceById(id);
			PersonDao personDao = context.getBean("personDao", PersonDao.class);
			int personId = personDao.getIdByUserName((String)request.getSession().getAttribute("userName"));
			java.util.Date date = new java.util.Date();
			trxDao.insert(id, personId, price,date.getTime());
			map.addAttribute("code", 200);
			map.addAttribute("message", "OK");
			map.addAttribute("result", true);
			((ConfigurableApplicationContext) context).close();
			return map;
		}else {
			map.addAttribute("code", 202);
			map.addAttribute("message", "无法购买");
			map.addAttribute("result", true);
			((ConfigurableApplicationContext) context).close();
			return map;
		}
		
	}
	
	//根据求返回account页面
	@RequestMapping(value="/account")
	public String account(HttpServletRequest request, ModelMap map){
		List<BuyList> buyList = new ArrayList<BuyList>();		
		
		HttpSession session = request.getSession();
		String userName = (String) session.getAttribute("userName");

		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
		}
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		
		PersonDao personDao = context.getBean("personDao",PersonDao.class);
		int personId = personDao.getIdByUserName(userName);
		
		TrxDao trxDao = context.getBean("trxDao", TrxDao.class);
		List<Trx> trxs = trxDao.geTrxList();
		
		ContentDao contentDao = context.getBean("contentDao", ContentDao.class);
		List<Content> contents = contentDao.getContentList();
		for(Trx trx : trxs){
			if (trx.getPersonId() == personId) {
				BuyList buyList2 = new BuyList();
				buyList2.setBuyPrice(trx.getPrice());
				buyList2.setBuyTime(trx.getTime());
				buyList2.setId(trx.getContentId());
				for(Content content : contents){
					if (content.getId() == buyList2.getId()) {
						buyList2.setImage(content.getIcon());
						buyList2.setTitle(content.getTitle());
					}
				}
				buyList.add(buyList2);
			}
		}
		
		map.addAttribute("buyList", buyList);
		((ConfigurableApplicationContext) context).close();
		return "account";
	}
	
	//根据求返回public页面
	@RequestMapping(value="/public")
	public String returnPublic(HttpServletRequest request, ModelMap map){
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
		}
		return "public";
	}
	
	//根据求返回publicSubmit页面
	@RequestMapping(value="/publicSubmit", method=RequestMethod.POST)
	public String publicSubmit(@RequestParam("title") String title, 
			@RequestParam("image") String image, 
			@RequestParam("detail") String detail, 
			@RequestParam("price") double price, 
			@RequestParam("summary") String summary, 
			ModelMap map,
			HttpServletRequest request) throws UnsupportedEncodingException{
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ContentDao contentDao = context.getBean("contentDao", ContentDao.class);

		contentDao.insert((int) (price*100), title, image, summary, detail);

		Product product = new Product();
		Content content = contentDao.getContentByTitle(title);
		product.setId(content.getId());
		product.setImage(content.getIcon());
		product.setTitle(content.getTitle());
		product.setSummary(content.getAbs());
		if(content.getText() != null)
			product.setDetail(content.getText());
		product.setPrice(content.getPrice());
		map.addAttribute("product", product);
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
		}
		
		((ConfigurableApplicationContext) context).close();
		return "publicSubmit";
	}
	
	//根据求返回edit页面
	@RequestMapping(value="/edit")
	public String edit(HttpServletRequest request,@RequestParam("id") int id, ModelMap map) throws UnsupportedEncodingException{
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ContentDao contentDao = context.getBean("contentDao", ContentDao.class);
		
		Product product = new Product();
		Content content = contentDao.getContentById(id);
		product.setId(content.getId());
		product.setImage(content.getIcon());
		product.setTitle(content.getTitle());
		product.setSummary(content.getAbs());
		product.setDetail(content.getText());
		product.setPrice(content.getPrice());
		map.addAttribute("product", product);
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
		}
		((ConfigurableApplicationContext) context).close();
		return "edit";
	}
	
	//根据求返回editSubmit页面
	@RequestMapping(value="/editSubmit")
	public String editSubmit(@RequestParam("title") String title, 
			@RequestParam("image") String image, 
			@RequestParam("detail") String detail, 
			@RequestParam("price") double price, 
			@RequestParam("summary") String summary,
			@RequestParam("id") int id,
			HttpServletRequest request, ModelMap map) throws UnsupportedEncodingException{
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context-dao.xml");
		ContentDao contentDao = context.getBean("contentDao", ContentDao.class);
		
		contentDao.update(id,(int) (price*100), title, image, summary, detail);
		
		Product product = new Product();
		Content content = contentDao.getContentById(id);
		product.setId(content.getId());
		product.setImage(content.getIcon());
		product.setTitle(content.getTitle());
		product.setSummary(content.getAbs());
		if(content.getText() != null)
			product.setDetail(content.getText());
		product.setPrice(content.getPrice());
		map.addAttribute("product", product);
		
		
		HttpSession session = request.getSession();
		if (session.getAttribute("userName")!=null) {
			User user = new User();
			user.setUsername("user");
			user.setUsertype((Integer) session.getAttribute("userType"));
			map.addAttribute("user", user);
		}
		
		((ConfigurableApplicationContext) context).close();
		return "editSubmit";
	}
}
