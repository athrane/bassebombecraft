package bassebombecraft.entity.attribute;

import static bassebombecraft.ModConstants.MARKER_ATTRIBUTE_ISNT_SET;
import static bassebombecraft.ModConstants.MARKER_ATTRIBUTE_IS_SET;
import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

import java.util.function.Supplier;

import bassebombecraft.event.entity.attribute.EntityAttributeEventHandler;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registering custom entity attributes.
 * 
 * The custom attributes are registered to all entities in the
 * {@linkplain EntityAttributeEventHandler}.
 */
public class RegisteredAttributes {

	/**
	 * Deferred registry for registration of items.
	 */
	public static final DeferredRegister<Attribute> ATTRIBUTE_REGISTRY = create(ATTRIBUTES, MODID);

	/**
	 * Attributes
	 */
	public static final RegistryObject<Attribute> IS_DECOY_ATTRIBUTE = register("bassebombecraft.attribute.decoy");
	public static final RegistryObject<Attribute> RESPAWN_ATTRIBUTE = register("bassebombecraft.attribute.respawn");
	public static final RegistryObject<Attribute> IS_RESPAWNED_ATTRIBUTE = register("bassebombecraft.attribute.isrespawned");

	/**
	 * Register marker attribute.
	 * 
	 * A marker attribute is implemented as a {@linkplain RangedAttribute} with
	 * range 0..1 with value 0.
	 * 
	 * @param key registry object name.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<Attribute> register(String key) {
		Attribute attribute = new RangedAttribute(key, MARKER_ATTRIBUTE_ISNT_SET, MARKER_ATTRIBUTE_ISNT_SET,
				MARKER_ATTRIBUTE_IS_SET).setSyncable(true);
		Supplier<Attribute> splAttribute = () -> attribute;
		return ATTRIBUTE_REGISTRY.register(key, splAttribute);
	}

}
