package bassebombecraft.client.screen;

import static bassebombecraft.ModConstants.NULL_I18N_ARGS;
import static bassebombecraft.client.rendering.RenderingUtils.createGuiTextureResourceLocation;
import static bassebombecraft.geom.GeometryUtils.oscillateFloat;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import bassebombecraft.inventory.container.CompositeMagicItemContainer;
import bassebombecraft.inventory.container.CompositeMagicItemItemStackHandler;
import bassebombecraft.inventory.container.CompositeMagicItemSequenceValidator;
import bassebombecraft.inventory.container.DefaultSequenceValidator;
import bassebombecraft.item.composite.CompositeMagicItem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * GUI for the composite magic item {@linkplain CompositeMagicItem}.
 */
public class CompositeMagicItemScreen extends ContainerScreen<CompositeMagicItemContainer> {

	/**
	 * Delta for sequence icons.
	 */
	static final int SEQUENCE_ICON_XDELTA = 18;

	/**
	 * Y position for sequence icons.
	 */
	static final int SEQUENCE_ICON_YPOS = 16;

	/**
	 * X position for sequence icons.
	 */
	static final int SEQUENCE_ICON_XPOS = 16;

	/**
	 * Texture size for sequence icon.
	 */
	static final int SEQUENCE_TEXTURE_SIZE = 32;

	/**
	 * Text color.
	 */
	static final int TEXT_COLOR = Color.BLACK.getRGB();

	/**
	 * X position for text.
	 */
	static final float HEADER_XPOS = 8;

	/**
	 * Y position for header text.
	 */
	static final float HEADER_YPOS = 10;

	/**
	 * X position for advice content text.
	 */
	static final float ADVICE_XPOS = 35;

	/**
	 * Y position for advice content text.
	 */
	static final float ADVICE_YPOS = 80;

	/**
	 * GUI background texture.
	 */
	static final ResourceLocation BACKGROUND_TEXTURE = createGuiTextureResourceLocation(CompositeMagicItemScreen.class);

	/**
	 * Sequence icon texture.
	 */
	static final ResourceLocation SEQUENCE_TEXTURE = createGuiTextureResourceLocation("compositesequence");

	/**
	 * Decoration texture.
	 */
	static final ResourceLocation DECORATION_TEXTURE = createGuiTextureResourceLocation("decoration");

	/**
	 * Decoration texture.
	 */
	static final ResourceLocation DECORATION2_TEXTURE = createGuiTextureResourceLocation("decoration2");

	/**
	 * Item advice generator
	 */
	ItemAdviceGenerator adviceGenerator;

	/**
	 * Item sequence validator.
	 */
	CompositeMagicItemSequenceValidator validator;

	/**
	 * GUI header text.
	 */
	String guiHeader;

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
		adviceGenerator = new DefaultAdviceGenerator(container);
		validator = new DefaultSequenceValidator();
		guiHeader = new TranslationTextComponent("compositemagicscreen.header", NULL_I18N_ARGS).getFormattedText();
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
		
		// get inventory
		CompositeMagicItemContainer compositeContainer = (CompositeMagicItemContainer) this.container;
		CompositeMagicItemItemStackHandler compositeInventory = compositeContainer.getCompositeItemInventory();

		renderGuiHeader();
		renderAdvice();
		renderSequenceHighlight(compositeInventory);
		renderDecoration();
	}
	
	/**
	 * Render advice header.
	 */
	void renderGuiHeader() {
		RenderSystem.pushMatrix();
		RenderSystem.scalef(1.0F, 1.0F, 1.0F);
		font.drawString(guiHeader, HEADER_XPOS, HEADER_YPOS, TEXT_COLOR);
		RenderSystem.popMatrix();
	}

	/**
	 * Render advice.
	 */
	void renderAdvice() {
		String[] advice = adviceGenerator.generate();
		RenderSystem.pushMatrix();
		RenderSystem.scalef(0.6F, 0.6F, 1.0F);

		int index = 0;
		for (String message : advice) {
			float yPos = ADVICE_YPOS + (index * 10);
			font.drawString(message, ADVICE_XPOS, yPos, TEXT_COLOR);
			index++;
		}

		RenderSystem.popMatrix();
	}

	/**
	 * Render sequence highlight.
	 * 
	 * @param compositeInventory composite item inventory.
	 */
	void renderSequenceHighlight(CompositeMagicItemItemStackHandler compositeInventory) {
		int length = validator.resolveLegalSequenceLength(compositeInventory);
		float oscRgb = oscillateFloat(0.5F, 1);
		RenderSystem.color4f(oscRgb, oscRgb, oscRgb, 1.0F);
		getMinecraft().getTextureManager().bindTexture(SEQUENCE_TEXTURE);
		for (int index = 0; index < length; index++) {
			int inventoryIndex = compositeInventory.getCompositeInventoryIndex() + index;
			int xPos = SEQUENCE_ICON_XPOS + (inventoryIndex * SEQUENCE_ICON_XDELTA);
			blit(xPos, SEQUENCE_ICON_YPOS, 0, 0, SEQUENCE_TEXTURE_SIZE, SEQUENCE_TEXTURE_SIZE, 32, 32);
		}
	}

	/**
	 * Render decoration.
	 */
	void renderDecoration() {
		float oscRgb = oscillateFloat(0.5F, 1);
		RenderSystem.color4f(1.0F, oscRgb, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(DECORATION_TEXTURE);
		blit(136, 55, 0, 0, SEQUENCE_TEXTURE_SIZE, SEQUENCE_TEXTURE_SIZE, 32, 32);

		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		getMinecraft().getTextureManager().bindTexture(DECORATION2_TEXTURE);
		blit(10, 65, 0, 0, SEQUENCE_TEXTURE_SIZE, SEQUENCE_TEXTURE_SIZE, 32, 32);

	}

	/**
	 * Render advice header.
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
		getMinecraft().getTextureManager().bindTexture(BACKGROUND_TEXTURE);
	      this.blit(matrixStack, getGuiLeft(), getGuiTop(), 0, 0, this.xSize, this.ySize);
	}
	
}
