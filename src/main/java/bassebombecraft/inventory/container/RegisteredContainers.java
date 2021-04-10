package bassebombecraft.inventory.container;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.CONTAINERS;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered containers.
 */
public class RegisteredContainers {

	/**
	 * Deferred registry for registration of container.
	 */
	public static final DeferredRegister<ContainerType<?>> CONTAINER_REGISTRY = create(CONTAINERS, MODID);

	/**
	 * Registry object for {@linkplain CompositeMagicItemContainer}.
	 */
	public static final RegistryObject<ContainerType<CompositeMagicItemContainer>> COMPOSITE_ITEM_COMTAINER = CONTAINER_REGISTRY
			.register(CompositeMagicItemContainer.NAME.toLowerCase(),
					() -> IForgeContainerType.create(CompositeMagicItemContainer::new));

}
