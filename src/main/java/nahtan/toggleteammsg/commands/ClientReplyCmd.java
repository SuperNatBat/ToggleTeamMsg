package nahtan.toggleteammsg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.MessageArgumentType;

public class ClientReplyCmd {
    private static String lastUser = "";
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        LiteralCommandNode<FabricClientCommandSource> command = dispatcher.register(ClientCommandManager.literal("reply")
                .then(ClientCommandManager.argument("msg", MessageArgumentType.message())
                        .executes(ClientReplyCmd::execute)));
        dispatcher.register(ClientCommandManager.literal("r").redirect(command));
    }

    private static int execute(CommandContext<FabricClientCommandSource> ctx) {
        if(lastUser.equals("")){
            FabricClientAudiences.of().audience().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&#ff0000You have not sent a message to anyone yet!"));
            return 1;
        }
        String msg = ctx.getArgument("msg", MessageArgumentType.MessageFormat.class).getContents();
        try{
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand("msg "+lastUser+" "+msg);
        }catch (NullPointerException ignored){}
        return 0;
    }
    public static void setLastUser(String user){
        lastUser = user;
    }
}
