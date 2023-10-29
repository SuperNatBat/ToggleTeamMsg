package nahtan.toggleteammsg.client;

import nahtan.toggleteammsg.commands.ToggleTeamMsgCmd;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class ToggleTeamMsgClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(ToggleTeamMsgCmd::register);
        ClientSendMessageEvents.ALLOW_CHAT.register((msg) -> {
            if(!ToggleTeamMsgCmd.isToggled()){
                return true;
            }
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if(player == null){
                return false;
            }
            try {
                String command = "/teammsg "+msg;
                command = command.substring(1);
                player.networkHandler.sendChatCommand(command);
            } catch (Exception e) {
                player.sendMessage(Text.of("shits fucked"));
            }
            return false;
        });
    }
}
