package cc.irori.shodo.sign.editor;

import cc.irori.shodo.TextBox;
import cc.irori.shodo.sign.ShodoSigns;
import cc.irori.shodo.sign.Sign;
import cc.irori.shodo.sign.util.SignUtil;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;

public class SignEditorPage extends InteractiveCustomUIPage<SignEditorPage.PageData> {

    private static final String KEY_BUTTON = "Button";
    private static final String KEY_SIGN_TEXT = "@SignText";

    private final Vector3i signPos;
    private final TextBox textBox;

    private String signText = "";

    public SignEditorPage(@NonNullDecl PlayerRef playerRef, @NonNullDecl CustomPageLifetime lifetime, Vector3i signPos) {
        super(playerRef, lifetime, PageData.CODEC);
        this.signPos = signPos;
        this.textBox = TextBox.builder()
                .setWidth(500 - 17 * 2)
                .setHeight(150)
                .setCleanupPeriodSeconds(0)
                .setFont(ShodoSigns.SIGN_FONT)
                .build();

        Sign sign = SignUtil.getSign(playerRef.getReference().getStore().getExternalData().getWorld(),
                signPos.getX(), signPos.getY(), signPos.getZ());
        if (sign != null && sign.hasText()) {
            this.signText = sign.text();
        }
    }

    public SignEditorPage(@NonNullDecl PlayerRef playerRef, @NonNullDecl CustomPageLifetime lifetime, Vector3i signPos, String text) {
        super(playerRef, lifetime, PageData.CODEC);
        this.signPos = signPos;
        this.textBox = TextBox.builder()
                .setWidth(500 - 17 * 2)
                .setHeight(150)
                .setCleanupPeriodSeconds(0)
                .setFont(ShodoSigns.SIGN_FONT)
                .build();
        this.signText = text;
    }

    @Override
    public void build(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl UICommandBuilder uiCommandBuilder, @NonNullDecl UIEventBuilder uiEventBuilder, @NonNullDecl Store<EntityStore> store) {
        uiCommandBuilder.append("Shodo/Sign/Editor.ui");
        uiCommandBuilder.set("#SignInput.Value", signText);
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#SaveButton",
                new EventData()
                        .append(KEY_BUTTON, "SaveButton")
                        .append(KEY_SIGN_TEXT, "#SignInput.Value"),
                false
        );
        uiEventBuilder.addEventBinding(
                CustomUIEventBindingType.ValueChanged,
                "#SignInput",
                EventData.of(KEY_SIGN_TEXT, "#SignInput.Value"),
                false
        );

        textBox.typesetter().clear();
        if (signText != null && !signText.isEmpty()) {
            textBox.typesetter().addMessage(signText);
        } else {
            textBox.typesetter().addMessage("下の入力欄にテキストを記入して保存ボタンを押すと、看板を編集できます。", Color.GRAY);
        }
        textBox.render(uiCommandBuilder, "#SignPreview");
    }

    @Override
    public void handleDataEvent(@NonNullDecl Ref<EntityStore> ref, @NonNullDecl Store<EntityStore> store, @NonNullDecl PageData data) {
        super.handleDataEvent(ref, store, data);

        signText = data.signText;

        Player player = store.getComponent(ref, Player.getComponentType());
        PlayerRef playerRef = store.getComponent(ref, PlayerRef.getComponentType());

        if (data.button != null && data.button.equals("SaveButton")) {
            SignUtil.updateSign(store.getExternalData().getWorld(), signPos.getX(), signPos.getY(), signPos.getZ(),
                    new Sign(data.signText, Color.WHITE, playerRef.getUuid(), false));

            textBox.typesetter().clear();
            textBox.typesetter().addMessage(signText);
            player.getPageManager().openCustomPage(ref, store, new SignEditorPage(playerRef, getLifetime(), signPos, data.signText));
        }
    }

    public static class PageData {

        static final BuilderCodec<PageData> CODEC = BuilderCodec.builder(PageData.class, PageData::new)
                .addField(
                        new KeyedCodec<>(KEY_BUTTON, Codec.STRING),
                        (data, value) -> data.button = value,
                        data -> data.button
                )
                .addField(
                        new KeyedCodec<>(KEY_SIGN_TEXT, Codec.STRING),
                        (data, value) -> data.signText = value,
                        data -> data.signText
                )
                .build();

        private String button;
        private String signText;
    }
}
