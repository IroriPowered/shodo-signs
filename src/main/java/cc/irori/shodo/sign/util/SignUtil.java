package cc.irori.shodo.sign.util;

import cc.irori.shodo.sign.Sign;
import cc.irori.shodo.sign.SignWithText;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.block.BlockModule;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;

import javax.annotation.Nullable;

public class SignUtil {

    // Private constructor to prevent instantiation
    private SignUtil() {
    }

    public static void updateSign(World world, int x, int y, int z, Sign sign) {
        Ref<ChunkStore> blockRef = BlockModule.getBlockEntity(world, x, y, z);
        if (blockRef == null) {
            Logs.logger().atWarning().log("Block (%d, %d, %d) is not a block entity", x, y, z);
            return;
        }

        BlockType blockType = world.getBlockType(x, y, z);
        if (blockType == null) {
            Logs.logger().atWarning().log("Null block type for block (%d, %d, %d)", x, y, z);
            return;
        }

        WorldChunk worldChunk = world.getChunk(ChunkUtil.indexChunkFromBlock(x, z));
        if (worldChunk == null) {
            Logs.logger().atWarning().log("Null world chunk for block (%d, %d, %d)", x, y, z);
            return;
        }

        BlockChunk blockChunk = worldChunk.getBlockChunk();
        if (blockChunk == null) {
            Logs.logger().atWarning().log("Null block chunk for block (%d, %d, %d)", x, y, z);
            return;
        }

        BlockSection blockSection = blockChunk.getSectionAtBlockY(y);
        int rotation = blockSection.getRotationIndex(x, y, z);

        BlockType stateToSet = blockType;
        if (sign.hasText()) {
            stateToSet = blockType.getBlockForState("WithText");
            if (stateToSet == null) {
                Logs.logger().atWarning().log("Block (%d, %d, %d) has does not have a WithText state", x, y, z);
                return;
            }
        } else {
            stateToSet = blockType.getBlockForState("default");
        }
        worldChunk.setBlock(x, y, z, BlockType.getAssetMap().getIndex(stateToSet.getId()), stateToSet, rotation, 0, 0);

        ChunkStore chunkStore = world.getChunkStore();
        blockRef = BlockModule.getBlockEntity(world, x, y, z);
        SignWithText signWithText = chunkStore.getStore().getComponent(blockRef, SignWithText.getComponentType());
        if (signWithText == null) {
            Logs.logger().atWarning().log("Block (%d, %d, %d) is not a sign", x, y, z);
            return;
        }
        signWithText.setData(sign);
    }

    public static @Nullable Sign getSign(World world, int x, int y, int z) {
        Ref<ChunkStore> blockRef = BlockModule.getBlockEntity(world, x, y, z);
        if (blockRef == null) {
            return null;
        }

        ChunkStore chunkStore = world.getChunkStore();
        SignWithText signWithText = chunkStore.getStore().getComponent(blockRef, SignWithText.getComponentType());
        if (signWithText == null) {
            return null;
        }

        return signWithText.getData();
    }
}
