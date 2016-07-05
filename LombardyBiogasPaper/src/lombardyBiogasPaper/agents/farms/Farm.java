package lombardyBiogasPaper.agents.farms;

import java.util.HashMap;

import lombardyBiogasPaper.agents.farms.priceExpectations.PriceExpectation;
import lombardyBiogasPaper.crops.ArableCrop;



public class Farm  {
	
	/**
	 * A numerical id
	 */
	private int id;
	
	/**
	 * A short name representing the GAMS set code
	 */
	private String name;
	
	/**
	 * PriceExpectationRule
	 */
	private PriceExpectation priceExp; //you have to set in constructor
	
	/**
	 * The current allocated production area for each arableCrop
	 */
	private HashMap<ArableCrop,Float> cropPlan = new HashMap<>();
	
	/**
	 * The expected yields for each arableCrop
	 */
	private HashMap<ArableCrop,Float> yield = new HashMap<>();
	
	/**
	 * The expected varcosts for each arableCrop
	 */
	private HashMap<ArableCrop,Float> varcost = new HashMap<>();
	
	/**
	 * The Accounting books
	 */
	private FarmerAccounting account = new FarmerAccounting();
	
	
	/**
	 * Total Available Land
	 */
	private Float totalLand = 0f;
	
	
	public Farm(int code, String name) {
		super();
		this.id = code;
		this.name = name;
	}
	
	public Farm(int code) {
		this(code,"");
	}

	@Override
	public String toString() {
		String r = "Farm [code=" + id + ", name=" + name ;
		r += "\nTotal Land: [" + totalLand + "]";
		r += "\nCurrent Crop Allocation: [" + cropPlan.toString() + "]";
		r += "\nCurrent Yields Expectations: [" + yield.toString() + "]";
		r += "\nCurrent VarCosts Expectations: [" + varcost.toString() + "]";
		r += "\n" + this.account;
		return r + "\n]";
	}

	public HashMap<ArableCrop, Float> getCropPlan() {
		return cropPlan;
	}
	
	public HashMap<ArableCrop, Float> getYield() {
		return yield;
	}
	
	public HashMap<ArableCrop, Float> getVarCost() {
		return varcost;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Float getTotalLand() {
		return totalLand;
	}

	public void setTotalLand(Float totalLand) {
		this.totalLand = totalLand;
	}

	public PriceExpectation getPriceExpectationObject() {
		return priceExp;
	}
	
	public HashMap<ArableCrop, Long> getPriceExpectationSnapshot() {
		return priceExp.getExpectations();
	}

	public FarmerAccounting getAccount() {
		return account;
	}

	public void setPriceExp(PriceExpectation pe) {
		this.priceExp = pe;
	}

	
}
