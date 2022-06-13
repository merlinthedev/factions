package me.merlin.profile;


import lombok.Getter;
import lombok.Setter;
import me.merlin.faction.Faction;


public class Profile {
    @Getter @Setter private Faction faction;
    @Getter @Setter private int power;
    @Getter @Setter private double balance;


}
