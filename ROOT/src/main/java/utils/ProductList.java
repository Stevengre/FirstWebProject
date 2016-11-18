package utils;

public class ProductList {

	int id;
	String title;
	String image;
	double price;
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
