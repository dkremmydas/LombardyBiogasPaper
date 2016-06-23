package lombardyBiogasPaper.dataLoaders;

import java.util.ArrayList;

import com.google.common.collect.ArrayListMultimap;

import lombardyBiogasPaper.Farm;
import lombardyBiogasPaper.Municipality;
import crops.ArableCrop;
import crops.AvailableArableCrops;

/**
 * Interface for Data Loaders <br />
 * The order of calling them is : getMunicipalities, getAvailableCrops, getFarms
 * 
 * @author Dimitris Kremmydas
 *
 */
public interface DataLoader {
	
	/**
	 * Return the list of Municipalities of the simulation
	 * @return ArrayList<Municipality>
	 */
	ArrayList<Municipality> getMunicipalities();
	
	/**
	 * Returns the available crops of the simulation
	 * @return
	 */
	ArrayList<ArableCrop> getAvailableCrops();
	
	/**
	 * Returns a MultiMap (municipality_id->Farm) of the available farms of the simulation
	 * @return
	 */
	ArrayListMultimap<Integer,Farm> getFarms();

}
