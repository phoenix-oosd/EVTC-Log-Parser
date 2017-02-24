package utility;


import player.Player;

public class PhaseDpsHolder {
    // Fields
    private Player player;
    private String[] all_phase_dps;
    private double average_dps;

    // Constructor
    public PhaseDpsHolder(Player p, String[] all_dps, double avg) {
        this.player = p;
        this.all_phase_dps = all_dps;
        this.average_dps = avg;
    }

    // Getters
    public Player getPlayer() { return player; }

    public String[] getAll_phase_dps() { return all_phase_dps; }

    public double getAverage_dps() { return average_dps; }
}
