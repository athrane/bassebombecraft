package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.player.PlayerUtils.getClientSidePlayerUId;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import javax.naming.OperationNotSupportedException;

import org.apache.logging.log4j.Logger;

import bassebombecraft.config.VersionUtils;
import bassebombecraft.event.rendering.CharmedInfoRenderer;
import bassebombecraft.event.rendering.DecoyRenderer;
import bassebombecraft.event.rendering.DecreaseSizeEffectRenderer;
import bassebombecraft.event.rendering.IncreaseSizeEffectRenderer;
import bassebombecraft.event.rendering.RespawnedRenderer;
import bassebombecraft.event.rendering.TargetInfoRenderer;
import bassebombecraft.event.rendering.TeamEnityRenderer;
import bassebombecraft.event.rendering.TeamInfoRenderer;

/**
 * Implementation of the {@linkplain Proxy} interface.
 * 
 * Forge client side proxy implementation.
 */
public class ClientProxy implements Proxy {

	/**
	 * Meta data for block.
	 */
	static final int META = 0;

	@Override
	public void startAnalyticsSession() {
		try {
			startSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());			
		}
	}

	@Override
	public void endAnalyticsSession() {
		try {
			endSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postItemUsage(String itemName, String user) {
		try {
			postItemUsageEvent(user, itemName);

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting usage failed with: " + ex.getMessage());
		}
	}
	
	@Override
	public void postException(Throwable e) {
		try {
			VersionUtils.postException(getUser(), e);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting exception:" + e.getMessage() + " failed with: " + ex.getMessage());
		}
	}
	
	@Override
	public void postAiObservation(String type, String observation) {
		try {
			VersionUtils.postAiObservation(getUser(), type, observation);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting AI observation: failed with: " + ex.getMessage());
		}		
	}

	@Override
	public String getUser() {
		return getClientSidePlayerUId();
	}

	@Override
	public void setupClientSideRendering() throws OperationNotSupportedException {
		// register debug renderer classes		
		// EVENT_BUS.addListener(DebugRenderer_MobLines::render);
		// EVENT_BUS.addListener(DebugRenderer_EntityText_v3::render);
		// MinecraftForge.EVENT_BUS.addListener(DebugRenderer_WorldLastEventText::render);
		// EVENT_BUS.addListener(DebugRenderer_Highlightblock::render);
		// EVENT_BUS.addListener(DebugRenderer_StrangeSize::render);
		// EVENT_BUS.addListener(DebugRenderer_2DEntities::renderPre);
		// EVENT_BUS.addListener(DebugRenderer_2DEntities::renderPost);

		// register renderer classes
		EVENT_BUS.addListener(TeamInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(TargetInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(CharmedInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(TeamEnityRenderer::handleRenderLivingEvent);
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPost);		
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPost);				
	}
	
}
