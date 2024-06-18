package uk.rayware.nitrogen.punishment

enum class PunishmentType(val context: String, val undoContext: String?) {
    IP_BAN("ip-banned", "un ip-banned"),
    BAN("banned", "unbanned"),
    WARN("warned", null),
    KICK("kicked", null),
    MUTE("muted", "un-muted");
}