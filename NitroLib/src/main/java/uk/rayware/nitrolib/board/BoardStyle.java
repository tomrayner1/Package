package uk.rayware.nitrolib.board;

import lombok.Getter;

@Getter
public enum BoardStyle {

    DEFAULT(false, 1),
    MODERN(true, 15);

    private boolean descending;
    private int startNumber;

    /**
     *
     *
     * @param descending  whether the positions are going down or up.
     * @param startNumber from where to loop from.
     */
    BoardStyle(boolean descending, int startNumber) {
        this.descending = descending;
        this.startNumber = startNumber;
    }

    public BoardStyle reverse() {
        return descending(!this.descending);
    }

    public BoardStyle descending(boolean descending) {
        this.descending = descending;
        return this;
    }

    public BoardStyle startNumber(int startNumber) {
        this.startNumber = startNumber;
        return this;
    }

}
