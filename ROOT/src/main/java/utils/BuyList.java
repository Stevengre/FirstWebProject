package utils;

public class BuyList {
	int id;
	String title;
	String image;
	double buyPrice;
	long buyTime;
	
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
	public double getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(int buyPrice) {
		this.buyPrice = (double) buyPrice/100;
	}
	public long getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}
}
