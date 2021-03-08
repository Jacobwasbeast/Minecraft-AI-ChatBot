package net.jacobmc.ai.chatbot;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import net.fabricmc.api.ModInitializer;
import net.jacobmc.ai.chatbot.events.ChatEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Timer;
import java.util.TimerTask;

public class Main implements ModInitializer {

    @Override
    public void onInitialize() {
        final Timer[] timer = {new Timer()};
        System.out.println("");
        final boolean[] dog = {false};
        try {
            ChatterBotFactory factory = new ChatterBotFactory();


            ChatterBot bot1 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            ChatterBotSession bot1session = bot1.createSession();
            ChatEvent.EVENT.register(new ChatEvent() {
                @Override
                public Boolean chat(PlayerEntity player, String chat) {
                    if (chat.startsWith("!chat")) {
                        dog[0] = !dog[0];
                        if (dog[0]) {
                            player.sendMessage(new LiteralText("You are now chatting with an AI"), false);

                        } else {
                            player.sendMessage(new LiteralText("You can now chat normally"), false);

                        }
                        return false;

                    } else {
                        if (dog[0]) {
                            player.sendMessage(new LiteralText(player.getName().getString() + ": " + chat), false);
                            timer[0].schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    player.sendMessage(new LiteralText("..."), false);
                                    timer[0] = new Timer();
                                    this.cancel();
                                }
                            }, 300);
                            timer[0].schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        player.sendMessage(new LiteralText("AI" + ": " + bot1session.think(chat)), false);
                                    } catch (Exception e) {

                                    }
                                    timer[0] = new Timer();
                                    this.cancel();
                                }
                            }, 330);
                            return false;
                        } else {
                            return true;
                        }

                    }
                }
            });
        } catch (Exception e) {

        }
    }
}