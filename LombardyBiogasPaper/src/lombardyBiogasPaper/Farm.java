package lombardyBiogasPaper;



public class Farm  {
	
	private int code;
	private String name;
	private PriceExpectations priceExp;
	
	
	public Farm(int code, String name) {
		super();
		this.code = code;
		this.name = name;
	}
	
	public Farm(int code) {
		this(code,"");
	}
	
	

}
