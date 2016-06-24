package lombardyBiogasPaper.crops;

import java.util.Arrays;

import com.google.common.collect.HashBiMap;

public class AvailableArableCrops {

	private HashBiMap<Integer,ArableCrop> crops = HashBiMap.create();
	private HashBiMap<Integer,String> idToCrop= HashBiMap.create();
	

	
	public void add(ArableCrop c) {
		crops.put(c.getId(), c);
		idToCrop.put(c.getId(), c.getName());
	}
	
	public Iterable<ArableCrop> getAll() {
		return crops.values();
	}
	
	public ArableCrop getById(int id) {
		return crops.get(id);
	}
	
	public ArableCrop getByName(String name) {
		return crops.get(idToCrop.inverse().get(name));
	}

	@Override
	public String toString() {
		return "AvailableArableCrops [" + Arrays.toString(this.crops.values().toArray()) + "]";
	}
	
	
}
