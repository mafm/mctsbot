package mctsbot.actions;

public class AllInAction implements Action {

	private final double amount;
	
	public AllInAction(double amount) {
		this.amount = amount;
	}
	
	public double getAmount() {
		return amount;
	}

	@Override
	public String getDescription() {
		return "All In Action " + amount;
	}

}