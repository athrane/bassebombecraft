package bassebombecraft.geom;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

@Deprecated
public class Coord3d  {
	public int x;
	public int y;
	public int z;
	Block block;
	boolean harvest;
	
	public Coord3d() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.block = Blocks.AIR;
		this.harvest = true;
	}

	public Coord3d(int x, int y, int z) {
		set(x, y, z);
		this.block = Blocks.AIR;
		this.harvest = true;		
	}

	public Coord3d(int x, int y, int z, Block block) {
		set(x, y, z);
		this.block = block;
		this.harvest = true;
	}

	public Coord3d(int x, int y, int z, Block block, boolean harvest) {
		set(x, y, z);
		this.block = block;
		this.harvest = harvest;
	}
	
	public Coord3d(Coord3d other) {
		set(other.x, other.y, other.z);
		this.block = other.block;		
		this.harvest = other.harvestBlock();
	}

	void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int hashCode() {
		return (this.x + (this.y << 8) + (this.z << 16));
	}

	public boolean equals(Object other) {
		if (other instanceof Coord3d) {
			return equals((Coord3d) other);
		}
		return false;
	}

	public boolean equals(Coord3d other) {
		return ((this.x == other.x) && (this.y == other.y) && (this.z == other.z));
	}
		
	public String toString() {
		return String.format("(%d,%d,%d)",
				new Object[] { Integer.valueOf(this.x),
						Integer.valueOf(this.y), Integer.valueOf(this.z) });
	}
	
	public Block getBlock() {
		return this.block;
	}
	
	/**
	 * returns true if block should harvested.
	 * 
	 * The block is harvested if the target block type is air 
	 * and the block directs harvesting.
	 * 
	 * @return true if block should harvested.
	 */
	public boolean harvestBlock() {
		if(getBlock() != Blocks.AIR) return false;
		return this.harvest;
	}
		
}