package uk.rayware.nitrogen.network

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import uk.rayware.nitrogen.pyrite.packet.PacketContainer
import uk.rayware.nitrogen.pyrite.packet.PacketListener
import uk.rayware.nitrogen.util.StaffUtil

class NitrogenPacketHandler : PacketContainer {

    @PacketListener
    fun onRequest(packet: RequestPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S] ${ChatColor.DARK_AQUA}[${packet.serverName}]${ChatColor.RESET} ${packet.target}${ChatColor.RESET}${ChatColor.DARK_AQUA} requested assistance:${ChatColor.AQUA} ${packet.request}")
    }

    @PacketListener
    fun onReport(packet: ReportPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S]${ChatColor.DARK_AQUA} [${packet.serverName}]${ChatColor.RESET} ${packet.reportedBy}${ChatColor.RESET}${ChatColor.DARK_AQUA} reported ${ChatColor.RESET}${packet.target}${ChatColor.RESET}${ChatColor.DARK_AQUA} (${packet.targetCount})${if (packet.reason == null) "." else " for: ${ChatColor.AQUA}${packet.reason}"}")
    }

    @PacketListener
    fun onStaffJoin(packet: StaffJoinPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S]${ChatColor.RESET} ${packet.target}${ChatColor.DARK_AQUA} has connected to ${ChatColor.AQUA}${packet.serverName}${ChatColor.DARK_AQUA}.")
    }

    @PacketListener
    fun onStaffQuit(packet: StaffQuitPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S]${ChatColor.RESET} ${packet.target}${ChatColor.DARK_AQUA} has disconnected from ${ChatColor.AQUA}${packet.serverName}${ChatColor.DARK_AQUA}.")
    }

    @PacketListener
    fun onStaffSwap(packet: StaffSwapPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S]${ChatColor.RESET} ${packet.target}${ChatColor.DARK_AQUA} joined ${ChatColor.AQUA}${packet.toServer}${ChatColor.DARK_AQUA} from ${ChatColor.AQUA}${packet.fromServer}${ChatColor.DARK_AQUA}.")
    }

    @PacketListener
    fun onStaffChat(packet: StaffChatPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S] ${ChatColor.DARK_AQUA}[${packet.serverName}]${ChatColor.RESET} ${packet.target}${ChatColor.GRAY}:${ChatColor.AQUA} ${packet.message}")
    }

    @PacketListener
    fun onChatMute(packet: ChatMutePacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S] ${ChatColor.DARK_AQUA}[${packet.serverName}]${ChatColor.RESET} ${packet.sender ?: ("${ChatColor.RED}${ChatColor.BOLD}Console")} ${ChatColor.DARK_AQUA}has ${if (packet.muted) "muted" else "unmuted"} the chat.")
    }

    @PacketListener
    fun onChatSlow(packet: ChatSlowPacket) {
        StaffUtil.messageStaff("${ChatColor.AQUA}[S] ${ChatColor.DARK_AQUA}[${packet.serverName}]${ChatColor.RESET} ${packet.sender ?: ("${ChatColor.RED}${ChatColor.BOLD}Console")} ${ChatColor.DARK_AQUA}has slowed the chat by ${ChatColor.AQUA}${packet.amount} second${if (packet.amount != 1) "s" else ""}${ChatColor.DARK_AQUA}.")
    }

    @PacketListener
    fun onAlert(packet: AlertPacket) {
        Bukkit.broadcastMessage(packet.message)
    }

}