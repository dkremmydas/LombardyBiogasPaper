package lombardyBiogasPaper.crops;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.HashBiMap;

public class AvailableArableCrops {

	private HashBiMap<Integer,ArableCrop> crops = HashBiMap.create();
	private HashBiMap<Integer,String> idToCrop= HashBiMap.create();
	
	/**
	 * Empty constructor
	 */
	public AvailableArableCrops() {}
	
	/**
	 * Constructor passing a List of ArableCrops
	 * @param cs
	 */
	public AvailableArableCrops(List<ArableCrop> cs) {
		this();
		for(ArableCrop c: cs) {
			this.add(c);
		}
	}
	
	/**
	 * Add one ArableCrop
	 * @param c
	 */
	public void add(ArableCrop c) {
		crops.put(c.getId(), c);
		idToCrop.put(c.getId(), c.getName());
	}
	
	/**
	 * Get the crops as Iterable<ArableCrop>
	 * @return
	 */
	public Iterable<ArableCrop> getAll() {
		return crops.values();
	}
	
	/**
	 * Get an ArableCrop by id
	 * @param id
	 * @return
	 */
	public ArableCrop getById(int id) {
		return crops.get(id);
	}
	
	/**
	 * Get an ArableCrop by name
	 * @param name
	 * @return
	 */
	public ArableCrop getByName(String name) {
		return crops.get(idToCrop.inverse().get(name));
	}

	@Override
	public String toString() {
		return "AvailableArableCrops [" + Arrays.toString(this.crops.values().toArray()) + "]";
	}

	
	
	
	
}
