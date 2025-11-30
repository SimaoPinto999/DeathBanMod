package death.temp.ban;

import net.fabricmc.api.ModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateMod implements ModInitializer {
	public static final String MOD_ID = "template-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// T
		// his code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		DeathBanManager.load();

		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			if (entity instanceof ServerPlayer player) {
				LOGGER.info("Jogador morreu: " + player.getName().getString()); 
				DeathBanManager.banPlayer(player.getUUID());

				DeathBanManager.printBannedPlayers();

				long remaining = DeathBanManager.getRemainingTime(player.getUUID());
				long hours = TimeUnit.MILLISECONDS.toHours(remaining);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;
				long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;

				player.connection.disconnect(
            			net.minecraft.network.chat.Component.literal("Morreste e estas temporariamente banido por " + hours + " horas, " + minutes + " minutos e " + seconds + " segundos.")
        			);
			}
		}); 

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            
            if (DeathBanManager.isBanned(player.getUUID())) {
                
				long remaining = DeathBanManager.getRemainingTime(player.getUUID());
				long hours = TimeUnit.MILLISECONDS.toHours(remaining);
				long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;
				long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;

				player.connection.disconnect(Component.literal(
					String.format("§cAINDA BANIDO!\n§fVolta em: §e%02dh %02dm %02ds", hours, minutes, seconds)
				));
            }
        });

	}
}