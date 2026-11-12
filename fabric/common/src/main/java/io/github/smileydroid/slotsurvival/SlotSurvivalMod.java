package io.github.smileydroid.slotsurvival;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Registers the /slotsurvival command. Each sub-command runs the matching function of the
 * embedded data (data/slot_lock/function/admin/*.mcfunction), with the same source as the caller,
 * so vanilla permission checks of /function still apply (operators only).
 */
public final class SlotSurvivalMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> register(dispatcher));
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("slotsurvival")
                .then(run("enable", "admin/enable"))
                .then(run("pause", "admin/pause"))
                .then(Commands.literal("offhand")
                        .then(run("on", "admin/offhand_on"))
                        .then(run("off", "admin/offhand_off")))
                .then(run("status", "admin/status"))
                .then(run("uninstall", "admin/uninstall")));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> run(String name, String function) {
        return Commands.literal(name).executes(context -> {
            CommandSourceStack source = context.getSource();
            source.getServer().getCommands().performPrefixedCommand(source, "function slot_lock:" + function);
            return 1;
        });
    }
}
