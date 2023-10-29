package nahtan.toggleteammsg.client;

import nahtan.toggleteammsg.commands.ClientReplyCmd;
import nahtan.toggleteammsg.commands.ToggleTeamMsgCmd;
import nahtan.toggleteammsg.commands.AllChatCmd;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
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
            if(!ToggleTeamMsgCmd.isToggled()){
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
            if(cmd.startsWith("msg") || cmd.startsWith("tell") || cmd.startsWith("w")){
                String recipient = cmd.split(" ", 3)[1];
                ClientReplyCmd.setLastUser(recipient);
            }
            return true;
        });

    }
}
