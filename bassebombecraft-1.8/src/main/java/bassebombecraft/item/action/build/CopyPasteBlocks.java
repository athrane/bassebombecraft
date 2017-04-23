package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.geom.GeometryUtils.calculateDegreesFromPlayerDirection;
import static bassebombecraft.geom.GeometryUtils.captureRectangle;
import static bassebombecraft.geom.GeometryUtils.rotateCoordinatesAroundYAxisAtOrigin;
import static bassebombecraft.geom.GeometryUtils.translate;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.player.PlayerUtils.sendChatMessageToPlayer;

import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.DefaultParticleRendering;
import bassebombecraft.event.particle.ParticleRendering;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import bassebombecraft.geom.WorldQueryImpl;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.player.PlayerUtils;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import bassebombecraft.world.TemplateUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which supports
 * replication of block structures.
 */
public class CopyPasteBlocks implements BlockClickedItemAction {

	static final EnumActionResult USED_ITEM = EnumActionResult.SUCCESS;
	static final EnumActionResult DIDNT_USED_ITEM = EnumActionResult.PASS;

	static final String MSG_COPIED = "Copied blocks.";
	static final String MSG_ILLEGAL_TRIGGER = "Illegal trigger. Click on a ground block to paste.";
	static final String MSG_RESET = "Reset captured blocks.";
	static final String MSG_REGISTERED_M1 = "Registered #1 marker. Click on another ground block to set the second marker.";
	static final String MSG_REGISTERED_M2 = "Registered #2 marker and captured blocks. Click on a ground block to paste. Click on a non-ground block to reset.";

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = CopyPasteBlocks.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	/**
	 * Null structure.
	 */
	static final Structure NULL_STRUCTURE = new CompositeStructure();

	/**
	 * First particle rendering array index.
	 */
	static final int FIRST_INDEX = 0;

	/**
	 * Define staff states.
	 */
	enum StaffState {
		NO_MARKERS_DEFINED, // no block defined
		FIRST_MARKER_DEFINED, // first block defined
		SECOND_MARKER_DEFINED, // second block defined and content is captured
	}

	/**
	 * Current staff state.
	 */
	StaffState state = StaffState.NO_MARKERS_DEFINED;

	/**
	 * First staff marker.
	 */
	BlockPos firstMarker;

	/**
	 * Second staff marker.
	 */
	BlockPos secondMarker;

	/**
	 * Ticks exists since first marker was set.
	 */
	int ticksExisted = 0;

	/**
	 * Defines whether action is active.
	 */
	boolean isActive = false;

	/**
	 * Player rotation when the first marker is set.
	 */
	int rotationDegrees;

	/**
	 * Captured list of block directives.
	 */
	List<BlockDirective> capturedBlocks;

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Particle rendering repository. Client side.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Particle for rendering of the first marker.
	 */
	ParticleRendering firstMarkerParticle;

	/**
	 * Particle for rendering of the second marker.
	 */
	ParticleRendering secondMarkerParticle;

	/**
	 * Defines if copied structure should be captured on disk.
	 */
	boolean captureOnCopy;

	/**
	 * CopyPasteBlocks constructor.
	 */
	public CopyPasteBlocks() {
		infos = createFromConfig(CONFIG_KEY);
		Config configuration = getBassebombeCraft().getConfiguration();
		captureOnCopy = configuration.getBoolean(CONFIG_KEY + ".CaptureOnCopy");
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();
		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return DIDNT_USED_ITEM;

		// create world query
		WorldQueryImpl worldQuery = new WorldQueryImpl(player, pos);

		// update state and create structure
		Structure structure = updateState(worldQuery);

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		// calculate list of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure);

		// add directives
		directivesRepository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		ticksExisted++;
	}

	/**
	 * Update book state depending on the blocks selected.
	 * 
	 * @param worldQuery
	 *            world query object.
	 * 
	 * @return structure to build.
	 */
	Structure updateState(WorldQuery worldQuery) {

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(worldQuery.getTargetBlockPosition().getY(),
				worldQuery.getPlayer());

		switch (state) {
		case NO_MARKERS_DEFINED:
			if (!isLegaLFirstMarker(isGroundBlock))
				return NULL_STRUCTURE;
			state = StaffState.FIRST_MARKER_DEFINED;
			registerFirstMarker(worldQuery.getTargetBlockPosition(), worldQuery.getPlayer());
			sendChatMessageToPlayer(worldQuery.getPlayer(), MSG_REGISTERED_M1);
			return NULL_STRUCTURE;
		case FIRST_MARKER_DEFINED:
			if (!isLegaLSecondMarker(isGroundBlock, worldQuery.getTargetBlockPosition()))
				return NULL_STRUCTURE;
			state = StaffState.SECOND_MARKER_DEFINED;
			registerSecondMarker(worldQuery.getTargetBlockPosition());
			captureWorldContent(worldQuery);
			sendChatMessageToPlayer(worldQuery.getPlayer(), MSG_REGISTERED_M2);
			return NULL_STRUCTURE;
		case SECOND_MARKER_DEFINED:
			if (!isLegalTrigger(isGroundBlock, worldQuery.getTargetBlockPosition(), worldQuery.getPlayer())) {

				// determine if capture should be reset
				if (shouldReset(isGroundBlock)) {
					state = StaffState.NO_MARKERS_DEFINED;
					sendChatMessageToPlayer(worldQuery.getPlayer(), MSG_RESET);
					clearRendering();
					return NULL_STRUCTURE;
				}

				sendChatMessageToPlayer(worldQuery.getPlayer(), MSG_ILLEGAL_TRIGGER);
				return NULL_STRUCTURE;
			}
			sendChatMessageToPlayer(worldQuery.getPlayer(), MSG_COPIED);
			return getCapturedContent(worldQuery);
		default:
			return NULL_STRUCTURE;
		}

	}

	/**
	 * Returns true if invoked block is a legal first marker. Block is a legal
	 * first marker if it is a ground block.
	 * 
	 * @param isGroundBlock
	 *            boolean to indicate if invoked block is a ground block.
	 * 
	 * @return true if invoked block is a legal first marker.
	 */
	boolean isLegaLFirstMarker(boolean isGroundBlock) {
		return (isGroundBlock);
	}

	/**
	 * Returns true if invoked block is legal second marker. Block is a legal
	 * second marker if it is a ground block and it isn't identical to the first
	 * marker.
	 * 
	 * @param isGroundBlock
	 *            boolean to indicate if invoked block is a ground block.
	 * @param pos
	 *            block position.
	 * 
	 * @return true is block is a legal second marker.
	 */
	boolean isLegaLSecondMarker(boolean isGroundBlock, BlockPos pos) {
		if (!isGroundBlock)
			return false;
		BlockPos candidateMarker = pos;
		return (!firstMarker.equals(candidateMarker));
	}

	/**
	 * Returns true if trigger block is legal. Block is a legal trigger if it is
	 * a ground block and it isn't identical to the first or second marker.
	 * 
	 * @param isGroundBlock
	 *            boolean to indicate if invoked block is a ground block.
	 * @param pos
	 *            trigger position.
	 * @param playerIn
	 *            player object.
	 * @return
	 */
	boolean isLegalTrigger(boolean isGroundBlock, BlockPos trigger, EntityPlayer playerIn) {
		if (!isGroundBlock)
			return false;

		// exit if first marker is selected
		if (firstMarker.equals(trigger)) {
			sendChatMessageToPlayer(playerIn, "First marker isn't a legal target position.");
			return false;
		}

		// exit if first marker is selected
		if (secondMarker.equals(trigger)) {
			sendChatMessageToPlayer(playerIn, "Second marker isn't a legal target position.");
			return false;
		}

		return true;
	}

	/**
	 * Returns true if captured blocks should be reset.
	 * 
	 * Block is a legal "resetter" if it is a non-ground block.
	 * 
	 * @param isGroundBlock
	 *            boolean to indicate if invoked block is a ground block.
	 * 
	 * @return true if captured blocks should bed reset
	 */
	boolean shouldReset(boolean isGroundBlock) {
		return (!isGroundBlock);
	}

	/**
	 * Register first marker.
	 * 
	 * @param pos
	 *            marker position.
	 * @param playerIn
	 *            player object.
	 */
	void registerFirstMarker(BlockPos pos, EntityPlayer playerIn) {
		firstMarker = pos;

		// get player rotation
		PlayerDirection playerDirection = PlayerUtils.getPlayerDirection(playerIn);

		// Hack: if player direction is east or west
		// then modify rotation 180 degrees
		if (playerDirection == PlayerDirection.East) {
			playerDirection = PlayerDirection.West;
		} else if (playerDirection == PlayerDirection.West) {
			playerDirection = PlayerDirection.East;
		}

		rotationDegrees = calculateDegreesFromPlayerDirection(playerDirection);

		// register with repository for rendering
		firstMarkerParticle = DefaultParticleRendering.getInstance(pos, infos[FIRST_INDEX]);
		particleRepository.add(firstMarkerParticle);
	}

	/**
	 * Register second marker.
	 * 
	 * @param pos
	 *            marker position.
	 */
	void registerSecondMarker(BlockPos pos) {
		secondMarker = pos;

		// register with repository for rendering
		secondMarkerParticle = DefaultParticleRendering.getInstance(pos, infos[FIRST_INDEX]);
		particleRepository.add(secondMarkerParticle);
	}

	/**
	 * Capture world content
	 * 
	 * @param sourceBlock
	 * @param worldQuery
	 * @return
	 */
	void captureWorldContent(WorldQuery worldQuery) {

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(worldQuery.getPlayer());

		// calculate lower and upper bounds
		BlockPos lower = calculateLowerBound();
		BlockPos upper = calculateUpperBound();

		BlockPos captureOffset = new BlockPos(lower);
		BlockPos captureSize = new BlockPos(upper.getX() - lower.getX(), upper.getY() - lower.getY(),
				upper.getZ() - lower.getZ());

		// Rule: if height == 0 set height to 1 to copy plane
		if (captureSize.getY() <= 0) {
			captureSize = new BlockPos(captureSize.getX(), 1, captureSize.getZ());
		}

		// capture
		capturedBlocks = captureRectangle(captureOffset, captureSize, worldQuery);

		// translate
		BlockPos translation = calculateTranslationVector(captureOffset, captureSize, playerDirection);
		capturedBlocks = translate(translation, capturedBlocks);

		// modify offset for Minecraft structure
		captureOffset = captureOffset.add(0, 1, 0);

		// save captured content as a Minecraft structure file
		if (captureOnCopy)
			TemplateUtils.save(worldQuery.getWorld(), captureOffset, captureSize);
	}

	/**
	 * Calculate lower bound for captured area.
	 * 
	 * @return lower bound.
	 */
	BlockPos calculateLowerBound() {
		int x = 0;
		int y = 0;
		int z = 0;

		if (firstMarker.getX() < secondMarker.getX()) {
			x = firstMarker.getX();
		} else {
			x = secondMarker.getX();
		}

		if (firstMarker.getY() < secondMarker.getY()) {
			y = firstMarker.getY();
		} else {
			y = secondMarker.getY();
		}

		if (firstMarker.getZ() < secondMarker.getZ()) {
			z = firstMarker.getZ();
		} else {
			z = secondMarker.getZ();
		}

		return new BlockPos(x + 1, y + 1, z + 1);
	}

	/**
	 * Calculate upper bound for captured area.
	 * 
	 * @return upper bound.
	 */
	BlockPos calculateUpperBound() {
		int x = 0;
		int y = 0;
		int z = 0;

		if (firstMarker.getX() < secondMarker.getX()) {
			x = secondMarker.getX();
		} else {
			x = firstMarker.getX();
		}

		if (firstMarker.getY() < secondMarker.getY()) {
			y = secondMarker.getY();
		} else {
			y = firstMarker.getY();
		}

		if (firstMarker.getZ() < secondMarker.getZ()) {
			z = secondMarker.getZ();
		} else {
			z = firstMarker.getZ();
		}

		return new BlockPos(x, y, z);
	}

	/**
	 * Calculate translation vector.
	 * 
	 * @param captureOffset
	 *            capture offset.
	 * @param captureSize
	 *            capture size.
	 * @param playerDirection
	 *            player direction.
	 * 
	 * @return translation vector.
	 */
	BlockPos calculateTranslationVector(BlockPos captureOffset, BlockPos captureSize, PlayerDirection playerDirection) {
		int translateX = captureOffset.getX();
		int translateY = captureOffset.getY();
		int translateZ = captureOffset.getZ();

		switch (playerDirection) {

		case South:
			return new BlockPos(translateX + captureSize.getX() - 1, translateY, translateZ);
		case West:
			return new BlockPos(translateX + captureSize.getX() - 1, translateY, translateZ + captureSize.getZ() - 1);
		case North:
			return new BlockPos(translateX, translateY, translateZ + captureSize.getZ() - 1);
		case East:
			return new BlockPos(translateX, translateY, translateZ);
		default:
			return new BlockPos(translateX, translateY, translateZ);
		}
	}

	/**
	 * Generate structure for captured content.
	 * 
	 * @param worldQuery
	 *            world query object.
	 * 
	 * @return structure containing the captured content.
	 */
	Structure getCapturedContent(WorldQuery worldQuery) {

		// rotate around origin
		List<BlockDirective> rotatedBlocks = rotateCoordinatesAroundYAxisAtOrigin(rotationDegrees, capturedBlocks);

		// generate captured structure
		return generateStructureForCapturedContent(rotatedBlocks, worldQuery);
	}

	/**
	 * Generate structure for captured content.
	 * 
	 * @param capturedBlocks
	 *            list of captured block directives.
	 * @param worldQuery
	 *            world query object.
	 * 
	 * @return structure containing the captured content.
	 */
	Structure generateStructureForCapturedContent(List<BlockDirective> capturedBlocks, WorldQuery worldQuery) {
		final BlockPos unitSize = new BlockPos(1, 1, 1);

		CompositeStructure composite = new CompositeStructure();
		for (BlockDirective directive : capturedBlocks) {
			Structure child = new ChildStructure(directive.getBlockPosition(), unitSize, directive.getBlock(),
					directive.getState());
			composite.add(child);
		}
		return composite;
	}

	/**
	 * Clear rendering of particles.
	 */
	void clearRendering() {
		particleRepository.remove(firstMarkerParticle);
		particleRepository.remove(secondMarkerParticle);
	}

}
