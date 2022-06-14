package me.merlin.menu;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.merlin.Factions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class Menu {

    public static Map<String, Menu> currentlyOpen = new HashMap<>();

    private Map<Integer, Button> buttons = new HashMap<>();
    private boolean autoUpdate = false;
    private boolean updateAfterClick = true;
    private boolean closedByMenu = false;
    private boolean placeholder = false;
    private Button placeholderButton = Button.placeholder(Material.IRON_FENCE, (byte) 0, "Placeholder");
    public static Map<String, BukkitRunnable> checkTasks = Maps.newHashMap();
    private boolean async = false;
    private Inventory inventory = null;

    private ItemStack createItemStack(Player player, Button button) {
        ItemStack item = button.getButtonItem(player);

        if(item.getType() != Material.SKULL_ITEM) {
            ItemMeta meta = item.getItemMeta();
            if(meta != null && meta.hasDisplayName()) {
                meta.setDisplayName(meta.getDisplayName() + "§b§c§d§e");
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private Inventory createInventory(final Player player) {
        final Map<Integer, Button> inventoryButtons = this.getButtons(player);
        inventory = Bukkit.createInventory((InventoryHolder) player, this.size(inventoryButtons), this.getTitle(player));

        for(final Map.Entry<Integer, Button> buttonEntry : inventoryButtons.entrySet()) {
            this.buttons.put(buttonEntry.getKey(), buttonEntry.getValue());
            inventory.setItem((int) buttonEntry.getKey(), buttonEntry.getValue().getButtonItem(player));

        }
        if(this.isPlaceholder()) {
            final Button placeholder = Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 15, new String[0]);
            for(int i = 0; i < this.size(inventoryButtons); ++i) {
                if(inventoryButtons.get(i) == null) {
                    this.buttons.put(i, placeholder);
                    inventory.setItem(i, placeholder.getButtonItem(player));
                }
            }
        }
        return inventory;

    }

    public void openMenu(final Player player) {
        Inventory inv = createInventory(player);
        player.openInventory(inv);
        update(player);
    }

    private void update(final Player player) {
        cancelCheck(player);
        Menu.currentlyOpen.put(player.getName(), this);
        this.onOpen(player);
        final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    Menu.cancelCheck(player);
                    Menu.currentlyOpen.remove(player.getName());

                }
                if(Menu.this.isAutoUpdate()) {
                    player.getOpenInventory().getTopInventory().setContents(Menu.this.createInventory(player).getContents());
                }
            }
        };
        bukkitRunnable.runTaskTimerAsynchronously(Factions.getInstance(), 0L, 0L);
        Menu.checkTasks.put(player.getName(), bukkitRunnable);
    }

    public static void cancelCheck(final Player player) {
        if(Menu.checkTasks.containsKey(player.getName())) {
            Menu.checkTasks.get(player.getName()).cancel();
            Menu.checkTasks.remove(player.getName());
        }
    }

    public int size(Map<Integer, Button> buttons) {
        int highest = 0;

        for(int buttonValue : buttons.keySet()) {
            if(buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public int getSize() {
        return -1;
    }

    public int getSlot(int x, int y) {
        return ((9 * y) + x);
    }

    public abstract String getTitle(Player player);
    public abstract Map<Integer, Button> getButtons(Player player);
    public void onOpen(Player player) {}
    public void onClose(Player player) {}

    private int getSlot(Button button) {
        for(int entry : buttons.keySet()) {
            if(buttons.get(entry) == button) {
                return entry;
            }
        }
        return 100;
    }

}
