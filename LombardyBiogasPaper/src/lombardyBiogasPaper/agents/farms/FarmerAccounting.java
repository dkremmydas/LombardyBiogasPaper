package lombardyBiogasPaper.agents.farms;

/**
 * A base class for representing the financial status of a Farmer
 *  
 * @author Dimitris Kremmydas
 * @version $Revision$
 * @since 2.0
 */
public class FarmerAccounting {

	//assets
	private Long cash = 0l;
	
	private Long cropInventory = 0l;
	
	private Long machinery = 0l;
	
	
	// liabilities
	private Long debts = 0l;
	
	private Long equity = 0l;
	

	public void addCash(Long amount) {
		this.cash = this.cash + (long)amount;
	}
	
	public void removeCash(Long amount) {
		this.cash = this.cash - (long) amount;
	}
	
	public void addCropInventory(Long amount) {
		this.cropInventory =+ (long)amount;
	}
	
	public void removeCropInventory(Long amount) {
		this.cropInventory =- (long)amount;
	}
	
	public void addMachinery(Long amount) {
		this.machinery =+ (long)amount;
	}
	
	public void removeMachinery(Long amount) {
		this.machinery =- (long)amount;
	}
	
	public void addDebt(Long amount) {
		this.debts =+ (long)amount;
	}
	
	public void removeDebt(Long amount) {
		this.debts =- (long)amount;
	}
	
	public void addEquity(Long amount) {
		this.equity =+ (long)amount;
	}
	
	public void removeEquity(Long amount) {
		this.equity =- (long)amount;
	}

	public Long getCash() {
		return cash;
	}

	public Long getCropInventory() {
		return cropInventory;
	}

	public Long getMachinery() {
		return machinery;
	}

	public Long getDebts() {
		return debts;
	}

	public Long getEquity() {
		return equity;
	}

	@Override
	public String toString() {
		return "FarmerAccounting [cash=" + cash + ", cropInventory="
				+ cropInventory + ", machinery=" + machinery + ", debts="
				+ debts + ", equity=" + equity + "]";
	}
	
	
	
}
