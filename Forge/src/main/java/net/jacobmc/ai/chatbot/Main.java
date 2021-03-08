package net.jacobmc.ai.chatbot;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("aichatbot")
public class Main {
    private static final Logger LOGGER = LogManager.getLogger();
    final Timer[] timer = new Timer[]{new Timer()};
    final boolean[] dog = new boolean[]{false};

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(FMLCommonSetupEvent event) {
    }

    private void doClientStuff(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        try {
            ChatterBotFactory factory = new ChatterBotFactory();
            ChatterBot bot1 = factory.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            final ChatterBotSession bot1session = bot1.createSession(new Locale[0]);
            final PlayerEntity player = Minecraft.getInstance().player;
            final String chat = event.getMessage();
            if (chat.startsWith("!chat")) {
                this.dog[0] = !this.dog[0];
                if (this.dog[0]) {
                    player.sendMessage(ForgeHooks.newChatWithLinks("You are now chatting with an AI"), (UUID)null);
                } else {
                    player.sendMessage(ForgeHooks.newChatWithLinks("You can now chat normally"), (UUID)null);
                }

                event.setCanceled(true);
            } else if (this.dog[0]) {
                player.sendMessage(ForgeHooks.newChatWithLinks(player.getName().getString() + ": " + chat), (UUID)null);
                this.timer[0].schedule(new TimerTask() {
                    public void run() {
                        player.sendMessage(ForgeHooks.newChatWithLinks("..."), (UUID)null);
                        Main.this.timer[0] = new Timer();
                        this.cancel();
                    }
                }, 300L);
                this.timer[0].schedule(new TimerTask() {
                    public void run() {
                        try {
                            player.sendMessage(ForgeHooks.newChatWithLinks("AI: " + bot1session.think(chat)), (UUID)null);
                        } catch (Exception var2) {
                        }

                        Main.this.timer[0] = new Timer();
                        this.cancel();
                    }
                }, 330L);
                event.setCanceled(true);
            }
        } catch (Exception var7) {
        }

    }

    private void enqueueIMC(InterModEnqueueEvent event) {
    }

    private void processIMC(InterModProcessEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static class RegistryEvents {
        public RegistryEvents() {
        }

        @SubscribeEvent
        public static void onBlocksRegistry(RegistryEvent.Register<Block> blockRegistryEvent) {
        }
    }
}
