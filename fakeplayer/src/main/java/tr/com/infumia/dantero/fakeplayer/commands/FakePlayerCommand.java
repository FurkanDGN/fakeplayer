package tr.com.infumia.dantero.fakeplayer.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import tr.com.infumia.dantero.fakeplayer.Core;
import tr.com.infumia.dantero.fakeplayer.api.Fake;
import tr.com.infumia.dantero.fakeplayer.handle.FakeBasic;

import java.io.IOException;
import java.util.HashMap;

@CommandAlias("fakeplayer|fp")
public class FakePlayerCommand extends BaseCommand {

    private final HashMap<String, Integer> tasks = new HashMap<>();

    @Default
    @CommandPermission("fakeplayer.command.main")
    public void defaultCommand(final CommandSender sender) {
        sender.sendMessage("/fakeplayer reload : Reload the config file.");
        sender.sendMessage("/fakeplayer spawn <name>: Spawns a NPC.");
        sender.sendMessage("/fakeplayer remove : Spawns a NPC.");
    }

    @Subcommand("animate")
    @CommandPermission("fakeplayer.command.animate")
    @CommandCompletion("@fakeplayers")
    public void animate(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                if (!Core.getFakeManager().fakeplayers.containsKey(args[0])){
                    sender.sendMessage("There is not a fake player named " + args[0] + "!");
                    return;
                }

                if (tasks.containsKey(sender.getName())){
                    Bukkit.getScheduler().cancelTask(tasks.get(sender.getName()));
                }

                Fake fakeBasic = Core.getFakeManager().fakeplayers.get(args[0]);

                tasks.put(sender.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {

                    private int step = 0;

                    @Override
                    public void run() {
                        if (step == 0) {
                            fakeBasic.jump();
                        }
                        if (step == 1) {
                            fakeBasic.jump();
                        }
                        if (step == 2) {
                            fakeBasic.toggleCrouch();
                        }
                        if (step == 3) {
                            fakeBasic.toggleCrouch();
                        }
                        if (step == 4) {
                            fakeBasic.toggleCrouch();
                        }
                        if (step == 5) {
                            fakeBasic.hit();
                        }
                        if (step == 6) {
                            fakeBasic.toggleCrouch();
                            Bukkit.getScheduler().cancelTask(tasks.get(sender.getName()));
                        }
                        step++;
                    }
                }, 10, 20));
                sender.sendMessage("Fake player " + args[0] + " is animating!");
            } else {
                sender.sendMessage("Wrong args.");
            }
        } else {
            sender.sendMessage("Please type name of a fake player.");
        }
    }

    @Subcommand("spawn")
    @CommandPermission("fakeplayer.command.spawn")
    public void spawnFake(final CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                if (args.length == 1) {
                    final Player player = (Player) sender;
                    Core.getInstance().getFakemanager().addFake(args[0], player.getLocation());
                    sender.sendMessage("Fake player " + args[0] + " is created.");
                } else {
                    sender.sendMessage("Wrong args.");
                }
            } else {
                sender.sendMessage("Please type name of fake player.");
            }
        } else {
            sender.sendMessage("Please use this command in game.");
        }
    }


    @Subcommand("remove")
    @CommandPermission("fakeplayer.command.remove")
    @CommandCompletion("@fakeplayers")
    public void removeFake(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                Core.getFakeManager().removeFake(args[0]);
                sender.sendMessage("Fake player " + args[0] + " is removed.");
            } else {
                sender.sendMessage("Wrong args.");
            }
        } else {
            sender.sendMessage("Please type name of a fake player.");
        }
    }

    @Subcommand("reload")
    @CommandPermission("fakeplayer.command.reload")
    public void reloadCommand(final CommandSender sender) throws IOException, InvalidConfigurationException {
        Core.getFakeManager().disablePlugin();
        Core.getInstance().getConfig().load(Core.getInstance().getConfigfile());
        Core.getFakeManager().onLoad();
    }
}
