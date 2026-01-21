package cc.irori.shodo.sign.command;

import cc.irori.shodo.sign.Sign;
import cc.irori.shodo.sign.util.SignUtil;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.TargetUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class SignLockCommand extends AbstractPlayerCommand {

    public SignLockCommand() {
        super("signlock", "Toggles the locked state of a sign");
    }

    @NullableDecl
    @Override
    protected String generatePermissionNode() {
        return "shodo.sign.lock";
    }

    @Override
    protected void execute(@NonNullDecl CommandContext context, @NonNullDecl Store<EntityStore> store, @NonNullDecl Ref<EntityStore> ref, @NonNullDecl PlayerRef playerRef, @NonNullDecl World world) {
        Vector3i targetBlock = TargetUtil.getTargetBlock(ref, 5, ref.getStore());
        if (targetBlock == null) {
            context.sendMessage(Message.raw("You must be looking at a sign to lock."));
            return;
        }

        Sign sign = SignUtil.getSign(world, targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
        SignUtil.updateSign(world, targetBlock.getX(), targetBlock.getY(), targetBlock.getZ(), new Sign(
                sign.text(),
                sign.color(),
                sign.lastEditor(),
                !sign.locked()
        ));

        context.sendMessage(Message.raw("The sign is now " + (sign.locked() ? "unlocked" : "locked") + "."));
    }
}
