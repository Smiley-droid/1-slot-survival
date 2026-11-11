package io.github.smileydroid.slotsurvival;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * 1 SLOT SURVIVAL : seul le slot 1 de la barre rapide est utilisable.
 * Les 35 autres slots contiennent une barrière "SLOT BLOQUÉ". Un objet étranger trouvé dans
 * un slot bloqué est éjecté au sol (jamais supprimé) puis la barrière est remise.
 */
public final class SlotSurvival extends JavaPlugin implements Listener, TabExecutor {

    private static final Component PREFIX = Component.text("[1 SLOT SURVIVAL] ", NamedTextColor.GOLD, TextDecoration.BOLD);

    private NamespacedKey lockKey;
    private NamespacedKey initKey;
    private boolean enabled;
    private boolean blockOffhand;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        lockKey = new NamespacedKey(this, "lock");
        initKey = new NamespacedKey(this, "init");
        enabled = getConfig().getBoolean("enabled", true);
        blockOffhand = getConfig().getBoolean("block-offhand", false);

        getServer().getPluginManager().registerEvents(this, this);
        PluginCommand command = getCommand("slotsurvival");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
        // Balayage de sécurité toutes les 10 ticks (0,5 s), comme la version datapack
        getServer().getScheduler().runTaskTimer(this, this::sweep, 10L, 10L);
    }

    // ------------------------------------------------------------------ objets

    private ItemStack createLock() {
        ItemStack stack = new ItemStack(Material.BARRIER);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.text("SLOT BLOQUÉ", NamedTextColor.RED, TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Cet emplacement est interdit !", NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false)));
        meta.getPersistentDataContainer().set(lockKey, PersistentDataType.BYTE, (byte) 1);
        stack.setItemMeta(meta);
        return stack;
    }

    private boolean isLock(ItemStack stack) {
        if (stack == null || stack.getType() != Material.BARRIER || !stack.hasItemMeta()) {
            return false;
        }
        return stack.getItemMeta().getPersistentDataContainer().has(lockKey, PersistentDataType.BYTE);
    }

    private static boolean isEmpty(ItemStack stack) {
        return stack == null || stack.getType().isAir() || stack.getAmount() <= 0;
    }

    // --------------------------------------------------------------- logique

    private boolean isSpectator(Player player) {
        return player.getGameMode() == GameMode.SPECTATOR;
    }

    private boolean isActive(Player player) {
        return enabled && !isSpectator(player) && !player.hasPermission("slotsurvival.exempt");
    }

    private void sweep() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            process(player);
            if (isActive(player) && isLock(player.getInventory().getItemInMainHand())) {
                player.sendActionBar(Component.text("Slot bloqué ! Utilise le slot 1 de ta barre rapide.", NamedTextColor.RED));
            }
        }
    }

    private void scheduleCheck(Player player) {
        Bukkit.getScheduler().runTask(this, () -> {
            if (player.isOnline()) {
                process(player);
            }
        });
    }

    private void process(Player player) {
        if (isSpectator(player)) {
            return;
        }
        if (!isActive(player)) {
            strip(player);
            return;
        }
        if (!player.getPersistentDataContainer().has(initKey, PersistentDataType.BYTE)) {
            player.getPersistentDataContainer().set(initKey, PersistentDataType.BYTE, (byte) 1);
            player.sendMessage(PREFIX.append(Component.text(
                    "Un seul slot est utilisable : le premier de ta barre rapide. "
                            + "Les objets mis dans un slot bloqué sont éjectés au sol, jamais supprimés.",
                    NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false)));
        }
        enforce(player);
    }

    private void enforce(Player player) {
        PlayerInventory inv = player.getInventory();

        // Slot 1 de la hotbar (index 0) : doit rester libre
        if (isLock(inv.getItem(0))) {
            inv.setItem(0, null);
        }
        // Hotbar 2 à 9 (1..8) + inventaire principal (9..35)
        for (int slot = 1; slot <= 35; slot++) {
            lockSlot(player, inv, slot);
        }
        // Main secondaire (optionnel)
        ItemStack off = inv.getItemInOffHand();
        if (blockOffhand) {
            if (!isLock(off)) {
                if (isEmpty(off) || eject(player, off)) {
                    inv.setItemInOffHand(createLock());
                }
            }
        } else if (isLock(off)) {
            inv.setItemInOffHand(new ItemStack(Material.AIR));
        }
    }

    private void lockSlot(Player player, PlayerInventory inv, int slot) {
        ItemStack current = inv.getItem(slot);
        if (isLock(current)) {
            return;
        }
        if (!isEmpty(current) && !eject(player, current)) {
            return; // l'éjection a échoué : l'objet reste chez le joueur (jamais de perte)
        }
        inv.setItem(slot, createLock());
    }

    /** Dépose une copie de l'objet au sol. Renvoie true si l'objet a bien été éjecté. */
    private boolean eject(Player player, ItemStack stack) {
        Item dropped = player.getWorld().dropItem(player.getLocation().add(0, 0.6, 0), stack.clone());
        if (!dropped.isValid()) {
            dropped.remove();
            return false;
        }
        dropped.setPickupDelay(50);
        dropped.setVelocity(new Vector(0, 0.2, 0));
        return true;
    }

    /** Retire toutes les barrières de blocage de l'inventaire. */
    private void strip(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int slot = 0; slot <= 35; slot++) {
            if (isLock(inv.getItem(slot))) {
                inv.setItem(slot, null);
            }
        }
        if (isLock(inv.getItemInOffHand())) {
            inv.setItemInOffHand(new ItemStack(Material.AIR));
        }
    }

    private int countLocks(Player player) {
        int count = 0;
        PlayerInventory inv = player.getInventory();
        for (int slot = 0; slot <= 35; slot++) {
            if (isLock(inv.getItem(slot))) {
                count++;
            }
        }
        if (isLock(inv.getItemInOffHand())) {
            count++;
        }
        return count;
    }

    // ---------------------------------------------------------------- events

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        scheduleCheck(event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player) || !isActive(player)) {
            return;
        }
        boolean cancel = isLock(event.getCurrentItem());
        if (!cancel && event.getClick() == ClickType.NUMBER_KEY) {
            int button = event.getHotbarButton();
            if (button >= 0 && button <= 8) {
                cancel = isLock(player.getInventory().getItem(button));
            }
        }
        if (!cancel && event.getClick() == ClickType.SWAP_OFFHAND) {
            cancel = isLock(player.getInventory().getItemInOffHand());
        }
        if (cancel) {
            event.setCancelled(true);
        }
        scheduleCheck(player);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getWhoClicked() instanceof Player player && isActive(player)) {
            scheduleCheck(player);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player player && isActive(player)) {
            scheduleCheck(player);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && isActive(player)) {
            scheduleCheck(player);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (isLock(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        if (isLock(event.getMainHandItem()) || isLock(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (isLock(event.getItemInHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (isLock(event.getEntity().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().removeIf(this::isLock);
    }

    // ------------------------------------------------------------- commandes

    private void reply(CommandSender sender, String text, NamedTextColor color) {
        sender.sendMessage(PREFIX.append(Component.text(text, color).decoration(TextDecoration.BOLD, false)));
    }

    /** Informe tous les joueurs connectés, et l'émetteur s'il n'est pas un joueur (console). */
    private void announce(CommandSender sender, String text, NamedTextColor color) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            reply(player, text, color);
        }
        if (!(sender instanceof Player)) {
            reply(sender, text, color);
        }
    }

    private void changeEnabled(boolean value) {
        enabled = value;
        getConfig().set("enabled", value);
        saveConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            process(player);
        }
    }

    private void setBlockOffhand(boolean value) {
        blockOffhand = value;
        getConfig().set("block-offhand", value);
        saveConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            process(player);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            reply(sender, "Utilisation : /" + label + " <enable|pause|offhand on|off|status|uninstall|selftest>", NamedTextColor.YELLOW);
            reply(sender, "Système : " + (enabled ? "activé" : "en pause")
                    + " | main secondaire : " + (blockOffhand ? "bloquée" : "libre"), NamedTextColor.GRAY);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "enable" -> {
                changeEnabled(true);
                announce(sender, "Système activé.", NamedTextColor.GREEN);
            }
            case "pause" -> {
                changeEnabled(false);
                announce(sender, "Système en pause.", NamedTextColor.RED);
            }
            case "offhand" -> {
                if (args.length < 2 || !(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("off"))) {
                    reply(sender, "Utilisation : /" + label + " offhand <on|off>", NamedTextColor.YELLOW);
                    return true;
                }
                boolean on = args[1].equalsIgnoreCase("on");
                setBlockOffhand(on);
                announce(sender, on ? "Main secondaire bloquée." : "Main secondaire libre.", NamedTextColor.YELLOW);
            }
            case "status" -> {
                reply(sender, "Système : " + (enabled ? "activé" : "en pause")
                        + " | main secondaire : " + (blockOffhand ? "bloquée" : "libre"), NamedTextColor.YELLOW);
                if (sender instanceof Player player) {
                    reply(sender, "Barrières dans ton inventaire : " + countLocks(player)
                            + " (35 attendues, 36 si la main secondaire est bloquée)", NamedTextColor.YELLOW);
                }
            }
            case "uninstall" -> {
                changeEnabled(false);
                announce(sender, "Barrières retirées. Tu peux maintenant supprimer le plugin SlotSurvival.", NamedTextColor.YELLOW);
            }
            case "selftest" -> selfTest(sender);
            default -> {
                reply(sender, "Commande inconnue. Utilisation : /" + label
                        + " <enable|pause|offhand on|off|status|uninstall>", NamedTextColor.RED);
            }
        }
        return true;
    }

    /** Diagnostic : exerce les API utilisées (objets, messages, action bar, éjection) sans joueur. */
    private void selfTest(CommandSender sender) {
        selfCheck(sender, "creation de la barriere", () -> {
            ItemStack lock = createLock();
            if (!isLock(lock)) {
                throw new IllegalStateException("la barriere n'est pas reconnue");
            }
        });
        selfCheck(sender, "message (Component)", () -> reply(sender, "test message", NamedTextColor.GRAY));
        selfCheck(sender, "action bar", () -> sender.sendActionBar(Component.text("test", NamedTextColor.RED)));
        selfCheck(sender, "ejection au sol", () -> {
            org.bukkit.World world = Bukkit.getWorlds().get(0);
            org.bukkit.Location where = world.getSpawnLocation().add(0, 2, 0);
            where.getChunk().load(true); // sans joueur, le chunk de spawn peut être déchargé
            Item dropped = world.dropItem(where, new ItemStack(Material.DIAMOND, 3));
            dropped.setPickupDelay(50);
            dropped.setVelocity(new Vector(0, 0.2, 0));
            boolean valid = dropped.isValid();
            dropped.remove();
            if (!valid) {
                throw new IllegalStateException("objet invalide");
            }
        });
        reply(sender, "Version serveur : " + Bukkit.getVersion(), NamedTextColor.GRAY);
    }

    private void selfCheck(CommandSender sender, String name, Runnable test) {
        try {
            test.run();
            sender.sendMessage(Component.text("[selftest] PASS " + name, NamedTextColor.GREEN));
        } catch (Throwable error) {
            sender.sendMessage(Component.text("[selftest] FAIL " + name + " : " + error, NamedTextColor.RED));
            getLogger().severe("selftest FAIL " + name + " : " + error);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> out = new ArrayList<>();
        if (args.length == 1) {
            for (String s : List.of("enable", "pause", "offhand", "status", "uninstall", "selftest")) {
                if (s.startsWith(args[0].toLowerCase())) {
                    out.add(s);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("offhand")) {
            for (String s : List.of("on", "off")) {
                if (s.startsWith(args[1].toLowerCase())) {
                    out.add(s);
                }
            }
        }
        return out;
    }
}
