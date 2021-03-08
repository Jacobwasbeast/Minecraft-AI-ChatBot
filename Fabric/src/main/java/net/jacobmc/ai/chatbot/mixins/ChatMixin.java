package net.jacobmc.ai.chatbot.mixins;

import net.jacobmc.ai.chatbot.events.ChatEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ChatMixin {

    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;)V", cancellable = true)
    public void onChatMessage(String message, CallbackInfo ci) {
        Boolean result = ChatEvent.EVENT.invoker().chat(MinecraftClient.getInstance().player,message);
        if (result == false) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(message);
            ci.cancel();
        }
    }

}
