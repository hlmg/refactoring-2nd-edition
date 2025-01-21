package ch1.data;

import java.util.List;

public class StatementData {

	private String customer;
	private List<Performance> performances;
	private int totalAmount;
	private int totalVolumeCredits;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public List<Performance> getPerformances() {
		return performances;
	}

	public void setPerformances(List<Performance> performances) {
		this.performances = performances;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getTotalVolumeCredits() {
		return totalVolumeCredits;
	}

	public void setTotalVolumeCredits(int totalVolumeCredits) {
		this.totalVolumeCredits = totalVolumeCredits;
	}

}
