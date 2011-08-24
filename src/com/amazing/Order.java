package com.amazing;

public class Order {
    
    private String orderName;
    private String orderStatus;
    private int id;
    private float avg, arr, restock;
    
    public String getOrderName() {
        return orderName;
    }
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setArr(float arr) {
		this.arr = arr;
	}
	public float getArr() {
		return arr;
	}
	public void setAvg(float avg) {
		this.avg = avg;
	}
	public float getAvg() {
		return avg;
	}
	public void setRestock(float restock) {
		this.restock = restock;
	}
	public float getRestock() {
		return restock;
	}
}