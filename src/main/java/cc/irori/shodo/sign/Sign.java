package cc.irori.shodo.sign;

import java.awt.*;
import java.util.UUID;

public record Sign(
        String text,
        Color color,
        UUID lastEditor
) {

    public boolean hasText() {
        return text != null && !text.isEmpty();
    }
}
