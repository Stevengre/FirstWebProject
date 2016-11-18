package utils;

import java.io.UnsupportedEncodingException;

public class Product {
	
	int id;
	String title;
	String summary;
	String detail;
	String image;
	double price;
	double buyPrice;
	boolean isBuy = false;
	boolean isSell = false;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) throws UnsupportedEncodingException {
		this.detail = new String(detail.getBytes("iso-8859-1"),"UTF-8");
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = (double) price/100;
	}
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = (double) buyPrice/100;
	}
	public boolean getIsBuy() {
		return isBuy;
	}
	public void setIsBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}
	public boolean getIsSell() {
		return isSell;
	}
	public void setIsSell(boolean isSell) {
		this.isSell = isSell;
	}
	
	
}
