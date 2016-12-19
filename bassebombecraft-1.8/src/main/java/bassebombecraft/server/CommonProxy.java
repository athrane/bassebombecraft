package bassebombecraft.server;

import bassebombecraft.projectile.GenericEggProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Minecraft uses a client and server setup, even on single player. 
 * The server side does all the work maintaining the world's state while the client renders the world. 
 * The thing is though, that all code runs on both the client and server side unless specified otherwise. 
 * There are two annotations for specifying code be ran on only one side.
 * The annotation @SidedProxy is used when you want the server to call the constructor of one class and the client another. 
 * Both classes need to be the same type or subtype of the field, and the names of the classes are passed as Strings. 
 */
public class CommonProxy {

    // Server stuff
    public void registerRenderers() {
        //System.out.println("CommonProxy:registerRenderers");
        
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        RenderChicken renderer = new RenderChicken(renderManager);
		RenderingRegistry.registerEntityRenderingHandler(GenericEggProjectile.class, renderer);
    }	
}
