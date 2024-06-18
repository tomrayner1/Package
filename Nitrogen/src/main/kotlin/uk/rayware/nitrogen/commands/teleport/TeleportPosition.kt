package uk.rayware.nitrogen.commands.teleport

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import uk.rayware.nitrolib.NitroLib

class TeleportPosition : Command("teleportposition") {

    init {
        permission = "nitrogen.command.teleportposition"
        aliases = listOf("tppos", "teleportpos", "tpposition")
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            return true
        }

        if (!sender.hasPermission("nitrogen.command.teleportposition")) {
            sender.sendMessage(NitroLib.permissionMessage)
            return true
        }

        var x = sender.location.x
        var y = sender.location.y
        var z = sender.location.z
        var world = sender.world
        var yaw = sender.location.yaw
        var pitch = sender.location.pitch

        try {
            when (args.size) {
                3 -> {
                    if (args[0] != "~") {
                        x = args[0].toDouble()
                    }

                    if (args[1] != "~") {
                        y = args[1].toDouble()
                    }

                    if (args[2] != "~") {
                        z = args[2].toDouble()
                    }
                }
                4 -> {
                    if (args[0] != "~") {
                        x = args[0].toDouble()
                    }

                    if (args[1] != "~") {
                        y = args[1].toDouble()
                    }

                    if (args[2] != "~") {
                        z = args[2].toDouble()
                    }

                    if (args[3] != "~") {
                        world = Bukkit.getWorld(args[3])

                        if (world == null) {
                            sender.sendMessage("${ChatColor.RED}Could not find specified world.")
                            return true
                        }
                    }
                }
                6 -> {
                    if (args[0] != "~") {
                        x = args[0].toDouble()
                    }

                    if (args[1] != "~") {
                        y = args[1].toDouble()
                    }

                    if (args[2] != "~") {
                        z = args[2].toDouble()
                    }

                    if (args[3] != "~") {
                        world = Bukkit.getWorld(args[3])

                        if (world == null) {
                            sender.sendMessage("${ChatColor.RED}Could not find specified world.")
                            return true
                        }
                    }

                    if (args[4] != "~") {
                        yaw = args[4].toFloat()
                    }

                    if (args[5] != "~") {
                        pitch = args[5].toFloat()
                    }
                }
                else -> {
                    sender.sendMessage("${ChatColor.YELLOW}Usage:${ChatColor.GREEN} /teleportposition${ChatColor.WHITE} <x> <y> <z> [world] [yaw] [pitch]")
                    return true
                }
            }
        } catch (e: NumberFormatException) {
            sender.sendMessage("${ChatColor.RED}There was an error whilst parsing the position (~ symbol is allowed).")
            return true
        }

        sender.teleport(Location(world, x, y, z, yaw, pitch))
        sender.sendMessage("${ChatColor.GREEN}You have been teleported to ${x}, ${y}, $z in ${world.name} (Yaw: ${yaw}, Pitch: ${pitch}).")
        return true
    }

    override fun tabComplete(sender: CommandSender?, alias: String?, args: Array<out String>?): MutableList<String>? {
        if (args!!.size == 4) {
            val completed = mutableListOf<String>()

            Bukkit.getWorlds().forEach { world -> completed.add(world.name) }

            return completed
        }

        return null
    }

}