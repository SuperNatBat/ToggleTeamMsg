package nahtan.toggleteammsg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.command.CommandRegistryAccess;

public class ToggleTeamMsgCmd {
    private static boolean isToggled = false;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        dispatcher.register(ClientCommandManager.literal("toggleteammsg").executes(ToggleTeamMsgCmd::execute));
    }

    private static int execute(CommandContext<FabricClientCommandSource> ctx) {
        Audience client = FabricClientAudiences.of().audience();
        if(isToggled){
            isToggled = false;
            client.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&#ff0000Toggled /teammsg (Disabled)"));
        }else{
            isToggled = true;
            client.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&#00ff00Toggled /teammsg (Enabled)"));
        }
        return 0;
    }
    public static boolean isToggled(){
        return isToggled;
    }
}
