package cc.irori.shodo.sign;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;
import java.util.UUID;

public class SignWithText implements Component<ChunkStore> {

    public static final BuilderCodec<SignWithText> CODEC = BuilderCodec.builder(SignWithText.class, SignWithText::new)
            .append(
                    new KeyedCodec<String>("SignText", Codec.STRING),
                    (sign, value) -> sign.signText = value,
                    sign -> sign.signText
            ).add()
            .append(
                    new KeyedCodec<Integer>("Color", Codec.INTEGER),
                    (sign, value) -> {
                        if (value != null) {
                            sign.color = new Color(value, false);
                        }
                    },
                    sign -> sign.color == null ? null : sign.color.getRGB()
            ).add()
            .append(
                    new KeyedCodec<UUID>("LastEditor", Codec.UUID_BINARY),
                    (sign, value) -> sign.lastEditor = value,
                    sign -> sign.lastEditor
            ).add()
            .build();

    private String signText = null;
    private Color color = Color.WHITE;
    private UUID lastEditor = null;

    public Sign getData() {
        return new Sign(signText, color, lastEditor);
    }

    public void setData(Sign sign) {
        this.signText = sign.text();
        this.color = sign.color();
        this.lastEditor = sign.lastEditor();
    }

    public boolean hasText() {
        return signText != null && !signText.isEmpty();
    }

    @NullableDecl
    @Override
    public Component<ChunkStore> clone() {
        SignWithText clone = new SignWithText();
        clone.signText = this.signText;
        clone.color = this.color;
        clone.lastEditor = this.lastEditor;
        return clone;
    }

    public static ComponentType<ChunkStore, SignWithText> getComponentType() {
        return ShodoSigns.get().getSignWithTextComponentType();
    }
}
