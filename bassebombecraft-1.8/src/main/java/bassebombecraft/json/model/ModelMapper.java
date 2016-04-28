package bassebombecraft.json.model;

import java.util.ArrayList;
import java.util.Collection;

import bassebombecraft.geom.Coord3d;

public class ModelMapper {

	private static final int BLOCK_PREFIX = 5;

	public static Voxel[] mapToJsonModel (Collection<Coord3d> coords) {
		ArrayList voxels = new ArrayList<Voxel>();		
		for(Coord3d coord : coords) {	
			
			// remove block prefix "tile."
			String unlocalizedName = coord.getBlock().getUnlocalizedName();			
			String name = unlocalizedName.substring(BLOCK_PREFIX);
			
			// create voxel
			Voxel voxel = new Voxel(
					coord.x, 
					coord.y, 
					coord.z,
					name);
			voxels.add(voxel);
		}		
		Voxel[] array = new Voxel[voxels.size()];
		return (Voxel[]) voxels.toArray(array);		
	}
}
