package lombardyBiogasPaper.agents;

import java.util.HashMap;

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
	private PriceExpectations priceExp;
	
	/**
	 * The current allocated production area for each arableCrop
	 */
	private HashMap<ArableCrop,Float> curSurface = new HashMap<>();
	
	/**
	 * The expected yields for each arableCrop
	 */
	private HashMap<ArableCrop,Float> yield = new HashMap<>();
	
	/**
	 * The expected varcosts for each arableCrop
	 */
	private HashMap<ArableCrop,Float> varcost = new HashMap<>();
	
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
		r += "\nCurrent Crop Allocation: [" + curSurface.toString() + "]";
		r += "\nCurrent Yields Expectations: [" + yield.toString() + "]";
		r += "\nCurrent VarCosts Expectations: [" + varcost.toString() + "]";
		
		return r + "\n]";
	}

	public HashMap<ArableCrop, Float> getCurSurface() {
		return curSurface;
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

	
	
	

}
