package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which supports
 * replication of block structures.
 */
public class CopyPasteBlocks implements BlockClickedItemAction {

	static final String MSG_COPIED = "Copied blocks.";
	static final String MSG_ILLEGAL_TRIGGER = "Illegal trigger. Click on a ground block to paste.";
	static final String MSG_RESET = "Reset captured blocks.";
	static final String MSG_REGISTERED_M1 = "Registered #1 marker. Click on another ground block to set the second marker.";
	static final String MSG_REGISTERED_M2 = "Registered #2 marker and captured blocks. Click on a ground block to paste. Click on a non-ground block to reset.";

	static final float R = 1.0F;
	static final float G = 1.0F;
	static final float B = 1.0F;
	static final int PARTICLE_NUMBER = 5;
	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_INSTANT;
	static final int PARTICLE_INFINITE_DURATION = -1;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo PARTICLE_INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER,
			PARTICLE_INFINITE_DURATION, R, G, B, PARTICLE_SPEED);

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks
	static final Structure NULL_STRUCTURE = new CompositeStructure();

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
	 * CopyPasteBlocks constructor.
	 */
	public CopyPasteBlocks() {
		super();
		particleRepository = getBassebombeCraft().getParticleRenderingRepository();
		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {

		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return false;

		// create world query
		WorldQueryImpl worldQuery = new WorldQueryImpl(playerIn, pos);

		// update state and create structure
		Structure structure = updateState(worldQuery);

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(playerIn);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(playerIn);

		// calculate list of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure);

		// add directives
		directivesRepository.addAll(directives);

		// TODO: determine correct response
		return false;
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
		firstMarkerParticle = DefaultParticleRendering.getInstance(pos, PARTICLE_INFO);
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
		secondMarkerParticle = DefaultParticleRendering.getInstance(pos, PARTICLE_INFO);
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

		// calculate lower and upper bounds
		BlockPos lower = calculateLowerBound();
		BlockPos upper = calculateUpperBound();
		BlockPos captureOffset = new BlockPos(lower);
		BlockPos captureSize = new BlockPos(upper.getX() - lower.getX(), upper.getY() - lower.getY(),
				upper.getZ() - lower.getZ());
				// System.out.println("lower: " + lower);
				// System.out.println("upper: " + upper);

		// System.out.println("Marker #1:" + firstMarker);
		// System.out.println("Marker #2:" + secondMarker);
		// System.out.println("target block:" +
		// worldQuery.getTargetBlockPosition());
		// System.out.println("captureOffset:" + captureOffset);
		// System.out.println("captureSize :" + captureSize);

		// Rule: if height == 0 set height to 1 to copy plane
		if (captureSize.getY() == 0) {
			captureSize = new BlockPos(captureSize.getX(), 1, captureSize.getZ());
		}

		// capture
		capturedBlocks = captureRectangle(captureOffset, captureSize, worldQuery);

		for (BlockDirective bd : capturedBlocks) {
			System.out.println("block: " + bd.toString());
		}

		// translate
		BlockPos translation = calculateTranslationVector(captureOffset);
		System.out.println("translation vector:" + translation);
		capturedBlocks = translate(translation, capturedBlocks);

		for (BlockDirective bd : capturedBlocks) {
			System.out.println("translated block: " + bd.toString());
		}
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
	 * 
	 * @return translation vector.
	 */
	BlockPos calculateTranslationVector(BlockPos captureOffset) {
		int translateX = captureOffset.getX();
		int translateY = captureOffset.getY();
		int translateZ = captureOffset.getZ();

		BlockPos translation = new BlockPos(translateX, translateY, translateZ);
		return translation;
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
