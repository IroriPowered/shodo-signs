package cc.irori.shodo.sign.editor;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.entities.player.pages.CustomUIPage;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;

public class SignEditorPageSupplier implements OpenCustomUIInteraction.CustomPageSupplier {

    public static final BuilderCodec<SignEditorPageSupplier> CODEC = BuilderCodec.builder(SignEditorPageSupplier.class, SignEditorPageSupplier::new).build();

    @NullableDecl
    @Override
    public CustomUIPage tryCreate(Ref<EntityStore> ref, ComponentAccessor<EntityStore> componentAccessor, PlayerRef playerRef, InteractionContext interactionContext) {
        return new SignEditorPage(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, new Vector3i());
    }

    public CustomUIPage tryCreateWithPos(Ref<EntityStore> ref, ComponentAccessor<EntityStore> componentAccessor, @Nonnull PlayerRef playerRef, InteractionContext context, Vector3i signPos) {
        return new SignEditorPage(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, signPos);
    }
}
