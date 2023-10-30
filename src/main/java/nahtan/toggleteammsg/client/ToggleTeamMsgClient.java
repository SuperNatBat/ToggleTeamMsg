package nahtan.toggleteammsg.client;

import com.mojang.datafixers.TypeRewriteRule;
import nahtan.toggleteammsg.commands.ClientReplyCmd;
import nahtan.toggleteammsg.commands.ToggleTeamMsgCmd;
import nahtan.toggleteammsg.commands.AllChatCmd;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class ToggleTeamMsgClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(ToggleTeamMsgCmd::register);
        ClientCommandRegistrationCallback.EVENT.register(AllChatCmd::register);
        ClientCommandRegistrationCallback.EVENT.register(ClientReplyCmd::register);
        // Listen to attempted chats
        ClientSendMessageEvents.ALLOW_CHAT.register((msg) -> {
            // Return if teamchat is not toggled or the message should be ignored
            if(!ToggleTeamMsgCmd.isToggled() || AllChatCmd.shouldIgnore()){
                return true;
            }
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null){
                return false;
            }
            String command = "/teammsg "+msg;
            command = command.substring(1);
            player.networkHandler.sendChatCommand(command);
            return false;
        });
        ClientSendMessageEvents.ALLOW_COMMAND.register((cmd) -> {
            if(ClientReplyCmd.shouldIgnore()){
                return true;
            }
            if(cmd.startsWith("msg") || cmd.startsWith("tell") || cmd.startsWith("w")){
                String recipient = cmd.split(" ", 3)[1];
                ClientReplyCmd.setLastUser(recipient);
            }
            return true;
        });

    }
}
