package utility;

import player.Player;

public class FinalDpsHolder {

    // Fields
    private Player player;
    private String dps;
    private double damage;

    // Constructor
    public FinalDpsHolder(Player p, String dp, double dmg) {
        this.player = p;
        this.dps = dp;
        this.damage = dmg;
    }

    // Getters
    public Player getPlayer() { return player; }

    public String getDps() { return dps; }

    public double getDamage() { return damage; }
}
