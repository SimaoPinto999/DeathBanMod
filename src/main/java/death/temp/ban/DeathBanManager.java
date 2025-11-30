package death.temp.ban;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.google.gson.Gson; // Use esta biblioteca para JSON
import com.google.gson.reflect.TypeToken; // Importe este para ajudar o GSON

public class DeathBanManager {
    // Mapeamento: UUID do jogador -> Tempo de desbanimento (em milissegundos)
    private static Map<UUID, Long> bannedPlayers = new HashMap<>();
    private static final Gson GSON = new Gson();
    // Use o diretório de configurações do Fabric
    private static final Path DATA_FILE = Path.of("config", "death_bans.json");
    private static final long BAN_DURATION_MS = 1 * 60 * 60 * 1000; // duracao em milissegundos 

    public static void load() {
        if (!DATA_FILE.toFile().exists()) {
            return; // Se o arquivo não existir, não há nada para carregar
        }
        try (Reader reader = new FileReader(DATA_FILE.toFile())) {
            // Este Type Token é necessário para o GSON desserializar Mapas
            TypeToken<Map<UUID, Long>> token = new TypeToken<>() {};
            bannedPlayers = GSON.fromJson(reader, token.getType());
            // Se o arquivo estava vazio ou corrompido, inicialize um novo mapa
            if (bannedPlayers == null) {
                bannedPlayers = new HashMap<>();
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados de banimento: " + e.getMessage());
            bannedPlayers = new HashMap<>();
        }
    }

    public static void save() {
        // Garante que o diretório 'config' existe
        DATA_FILE.getParent().toFile().mkdirs();
        try (Writer writer = new FileWriter(DATA_FILE.toFile())) {
            GSON.toJson(bannedPlayers, writer);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados de banimento: " + e.getMessage());
        }
    }

    public static void banPlayer(UUID uuid) {
        long unbanTime = System.currentTimeMillis() + BAN_DURATION_MS;
        if(!bannedPlayers.containsKey(uuid)){
            bannedPlayers.put(uuid, unbanTime);
        }
    
        save();
    }

    public static boolean isBanned(UUID uuid) {
        if (!bannedPlayers.containsKey(uuid)) {
            return false;
        }
        long unbanTime = bannedPlayers.get(uuid);
        long currentTime = System.currentTimeMillis();

        if (currentTime >= unbanTime) {
            // Tempo expirou, remova o ban
            bannedPlayers.remove(uuid);
            save();
            return false;
        }
        return true;
    }

    public static long getRemainingTime(UUID uuid) {
        if (!bannedPlayers.containsKey(uuid)) {
            return 0;
        }
        return bannedPlayers.get(uuid) - System.currentTimeMillis();
    }

    public static void printBannedPlayers(){
        for (UUID uuid : bannedPlayers.keySet()) {
            long remainingTime = getRemainingTime(uuid);
            System.out.println("Player " + uuid + " is banned for " + remainingTime + " seconds");
        }
    }
}
