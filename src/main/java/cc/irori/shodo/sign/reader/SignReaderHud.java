package cc.irori.shodo.sign.reader;

import cc.irori.shodo.TextBox;
import cc.irori.shodo.sign.ShodoSigns;
import com.buuz135.mhud.MultipleHUD;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.Anchor;
import com.hypixel.hytale.server.core.ui.Value;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.awt.*;

public class SignReaderHud extends CustomUIHud {

    private static final int HUD_TEXTBOX_WIDTH = 400;
    private static final int HUD_TEXTBOX_HEIGHT = 500;

    private final Player player;
    private final TextBox textBox;

    private boolean isVisible = false;
    private String signText;
    private Color signColor;

    public SignReaderHud(Player player, PlayerRef playerRef) {
        super(playerRef);
        this.player = player;
        this.textBox = TextBox.builder()
                .setWidth(HUD_TEXTBOX_WIDTH)
                .setHeight(HUD_TEXTBOX_HEIGHT)
                .setCleanupPeriodSeconds(0)
                .setFont(ShodoSigns.SIGN_FONT)
                .build();
    }

    @Override
    protected void build(@NonNullDecl UICommandBuilder uiCommandBuilder) {
        if (!isVisible) {
            return;
        }

        int height = ShodoSigns.SIGN_FONT.getLineHeight() * textBox.typesetter().getLineCount();

        Anchor containerAnchor = new Anchor();
        containerAnchor.setWidth(Value.of(HUD_TEXTBOX_WIDTH + 20));
        containerAnchor.setHeight(Value.of((int) (height * ShodoSigns.SIGN_FONT.getScale()) + 20));

        Anchor textAnchor = new Anchor();
        textAnchor.setWidth(Value.of(HUD_TEXTBOX_WIDTH));
        textAnchor.setHeight(Value.of(height));

        uiCommandBuilder.append("Shodo/Sign/Reader.ui");
        uiCommandBuilder.setObject("#SignTextContainer.Anchor", containerAnchor);
        uiCommandBuilder.setObject("#SignText.Anchor", textAnchor);

        textBox.render(uiCommandBuilder, "#SignText");
    }

    public boolean setIsVisible(boolean visible) {
        if (this.isVisible != visible) {
            this.isVisible = visible;
            return true;
        }
        return false;
    }

    public boolean setText(String text, Color color) {
        if (!text.equals(signText) || !color.equals(signColor)) {
            this.signText = text;
            this.signColor = color;

            textBox.typesetter().clear();
            textBox.typesetter().addMessage(text, color);
            return true;
        }
        return false;
    }

    public void update() {
        MultipleHUD.getInstance().setCustomHud(player, getPlayerRef(), "Shodo_Sign_Reader", this);
    }
}
