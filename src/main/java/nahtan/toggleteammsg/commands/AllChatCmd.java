package nahtan.toggleteammsg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;

public class AllChatCmd {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        LiteralCommandNode<FabricClientCommandSource> command = dispatcher.register(ClientCommandManager.literal("allchat")
                .then(ClientCommandManager.argument("msg", MessageArgumentType.message())
                        .executes(AllChatCmd::execute)));
        dispatcher.register(ClientCommandManager.literal("ac").redirect(command));
    }

    private static int execute(CommandContext<FabricClientCommandSource> ctx) {
        String msg = ctx.getArgument("msg", MessageArgumentType.MessageFormat.class).getContents();
        try {
            MinecraftClient.getInstance().player.networkHandler.sendChatMessage(msg);
        }catch (NullPointerException ignored){}
        return 0;
    }
}
