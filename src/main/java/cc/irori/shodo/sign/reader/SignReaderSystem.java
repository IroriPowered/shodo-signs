package cc.irori.shodo.sign.reader;

import cc.irori.shodo.sign.ShodoSigns;
import cc.irori.shodo.sign.Sign;
import cc.irori.shodo.sign.util.Logs;
import cc.irori.shodo.sign.util.SignUtil;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.entity.EntityUtils;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;

public class SignReaderSystem extends EntityTickingSystem<EntityStore> {

    private final Query<EntityStore> query;

    public SignReaderSystem() {
        this.query = Query.and(Player.getComponentType());
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }

    @Override
    public void tick(float dt, int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer) {
        Holder<EntityStore> holder = EntityUtils.toHolder(index, archetypeChunk);
        Player player = holder.getComponent(Player.getComponentType());

        PlayerRef playerRef = holder.getComponent(PlayerRef.getComponentType());
        Ref<EntityStore> playerEntityRef = player.getReference();

        if (playerRef == null || playerEntityRef == null) {
            return;
        }

        SignReaderHud hud = ShodoSigns.get().getOrCreateHud(player, playerRef);
        Vector3i targetBlock = TargetUtil.getTargetBlock(playerEntityRef, 5, playerEntityRef.getStore());
        if (targetBlock == null) {
            hideHud(hud);
            return;
        }

        World world = playerEntityRef.getStore().getExternalData().getWorld();
        world.execute(() -> {
            Sign sign = SignUtil.getSign(world, targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            if (sign == null || !sign.hasText()) {
                hideHud(hud);
                return;
            }

            hud.setIsVisible(true);
            if (hud.setText(sign.text(), sign.color())) {
                hud.update();
            }
        });
    }

    private static void hideHud(SignReaderHud hud) {
        if (hud.setIsVisible(false)) {
            hud.update();
        }
    }
}
