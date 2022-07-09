package me.merlin.kits;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class Kit {

    @Getter private final String name;

    @Getter @Setter private String displayName;

    @Getter @Setter private ItemStack[] inventory;
    @Getter @Setter private ItemStack[] armor;

    @Getter @Setter private ItemStack displayItem;

}
