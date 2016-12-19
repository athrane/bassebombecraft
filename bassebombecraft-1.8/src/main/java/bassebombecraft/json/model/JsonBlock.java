package bassebombecraft.json.model;

import com.google.common.collect.ImmutableMap;

import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.math.BlockPos;

/**
 * Block representation for saving block to JSON.
 */
public class JsonBlock {

	public String name;
	public BlockPos position;
	public ImmutableMap<IProperty, Object> stateProperties;
	// public UniqueIdentifier id;
	public int meta;

	public JsonBlock(BlockDirective directive, String name) {
		/**
		 * this.position = directive.getBlockPosition(); this.name = name;
		 * 
		 * IBlockState state = directive.getState(); this.id =
		 * GameRegistry.findUniqueIdentifierFor(state.getBlock());
		 * stateProperties = state.getProperties();
		 * 
		 * Block block = directive.getBlock(); meta =
		 * block.getMetaFromState(state);
		 * 
		 * ImmutableSet<IProperty> keys = stateProperties.keySet();
		 * for(IProperty key : keys) {
		 * 
		 * // inspect key Class keyValue = key.getValueClass();
		 * //System.out.println("key: " + key);
		 * 
		 * if(keyValue == PropertyEnum.class) {
		 * 
		 * //PropertyEnum typecastEnum = (PropertyEnum) keyValue;
		 * //System.out.println(typecastEnum.getName());
		 * //System.out.println(typecastEnum.getAllowedValues());
		 * //System.out.println(typecastEnum.getValueClass()); }
		 * 
		 * // inspect value Object value = stateProperties.get(key);
		 * System.out.println("value: " + value); System.out.println("value
		 * class: " + value.getClass()); }
		 * 
		 * Collection<IProperty> propertyNames = state.getPropertyNames();
		 * for(IProperty property : propertyNames) { System.out.println("name:"
		 * +property.getName()); System.out.println("value
		 * class:"+property.getValueClass()); System.out.println("state
		 * value:"+state.getValue(property)); }
		 **/
	}

}
