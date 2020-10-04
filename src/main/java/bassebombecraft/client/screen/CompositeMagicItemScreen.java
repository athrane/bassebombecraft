package bassebombecraft.client.screen;

import static bassebombecraft.client.rendering.RenderingUtils.createGuiTextureResourceLocation;

import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.inventory.container.CompositeMagicItemContainer;
import bassebombecraft.item.composite.CompositeMagicItem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * GUI for the composite magic item {@linkplain CompositeMagicItem}.
 */
public class CompositeMagicItemScreen extends ContainerScreen<CompositeMagicItemContainer> {

	/**
	 * GUI Background texture.
	 */
	static final ResourceLocation BACKGROUND_TEXTURE = createGuiTextureResourceLocation(CompositeMagicItemScreen.class);

	/**
	 * Constructor
	 * 
	 * @param container container object
	 * @param inventory player inventory
	 * @param title     screen title
	 */
	public CompositeMagicItemScreen(CompositeMagicItemContainer container, PlayerInventory inventory,
			ITextComponent title) {
		super(container, inventory, title);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		// bind texture		
        RenderHelper.setupGuiFlatDiffuseLighting();
        RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );        
        getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);		

		// centre the texture within the window
		int edgeSpacingX = (this.width - this.xSize) / 2;
		int edgeSpacingY = (this.height - this.ySize) / 2;

		// draw texture
		this.blit(edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
	}

}
