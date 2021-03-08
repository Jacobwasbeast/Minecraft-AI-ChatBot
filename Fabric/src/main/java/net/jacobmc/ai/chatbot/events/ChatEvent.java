package net.jacobmc.ai.chatbot.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface ChatEvent {
    Event<ChatEvent> EVENT = EventFactory.createArrayBacked(ChatEvent.class,
            (listeners) -> (player, string) -> {
                for (ChatEvent listener : listeners) {

                    if (string == null) {
                        return false;
                    }
                }
                return true;

            });

    Boolean chat(PlayerEntity player, String chat);
}
