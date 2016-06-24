package lombardyBiogasPaper.crops;


public class ArableCrop {
	
	/**
	 * A numerical id of the crop
	 */
	private int id;
	
	/**
	 * A short name (GAMS notation preferably) of the crop
	 */
	private String name;
	
	/**
	 * The long-text name of the crop
	 */
	private String originalName;
	
	
	public ArableCrop(int id, String name, String originalName) {
		super();
		this.id = id;
		this.name = name;
		this.originalName = originalName;
	}


	@Override
	public String toString() {
		//return "ArableCrop [id=" + id + ", name=" + name + ", originalName="
		//		+ originalName + "]";
		return name;
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getOriginalName() {
		return originalName;
	}
	
	

}
