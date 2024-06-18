package uk.rayware.nitroproxy.pyrite.packet;

import lombok.Data;

/**
 * Packet
 */
@Data
public class Packet {

    private PacketMetadata metadata = new PacketMetadata(this);

}