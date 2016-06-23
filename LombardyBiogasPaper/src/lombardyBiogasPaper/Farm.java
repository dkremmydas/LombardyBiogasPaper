package lombardyBiogasPaper;

import java.util.HashMap;

import crops.ArableCrop;



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
		r += "Current Surface: [" + curSurface.toString() + "]";
		
		return r + "]";
	}

	public HashMap<ArableCrop, Float> getCurSurface() {
		return curSurface;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	
	
	

}
