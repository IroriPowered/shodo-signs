package cc.irori.shodo.sign;

import cc.irori.shodo.BuiltInFontData;
import cc.irori.shodo.FontData;
import cc.irori.shodo.sign.editor.SignEditorPageSupplier;
import cc.irori.shodo.sign.editor.SignInteraction;
import cc.irori.shodo.sign.reader.SignReaderHud;
import cc.irori.shodo.sign.reader.SignReaderSystem;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.OpenCustomUIInteraction;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShodoSigns extends JavaPlugin {

    public static final FontData SIGN_FONT = BuiltInFontData.INSTANCE.ofScale(1.3);

    private static ShodoSigns instance;

    private final Map<UUID, SignReaderHud> signReaderHuds = new ConcurrentHashMap<>();

    private ComponentType<ChunkStore, SignWithText> signWithTextComponentType;

    public ShodoSigns(@NonNullDecl JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void start() {
        signWithTextComponentType = getChunkStoreRegistry().registerComponent(SignWithText.class, "SignWithText", SignWithText.CODEC);
        getEntityStoreRegistry().registerSystem(new SignReaderSystem());
        getEventRegistry().register(PlayerDisconnectEvent.class, event ->
            signReaderHuds.remove(event.getPlayerRef().getUuid()));
        getCodecRegistry(OpenCustomUIInteraction.PAGE_CODEC).register("Shodo_Sign_Editor", SignEditorPageSupplier.class, SignEditorPageSupplier.CODEC);
        getCodecRegistry(Interaction.CODEC).register("ShodoSignInteraction", SignInteraction.class, SignInteraction.CODEC);
    }

    public ComponentType<ChunkStore, SignWithText> getSignWithTextComponentType() {
        return signWithTextComponentType;
    }

    public SignReaderHud getOrCreateHud(Player player, PlayerRef ref) {
        return signReaderHuds.computeIfAbsent(ref.getUuid(), uuid -> new SignReaderHud(player, ref));
    }

    public static ShodoSigns get() {
        return instance;
    }
}
