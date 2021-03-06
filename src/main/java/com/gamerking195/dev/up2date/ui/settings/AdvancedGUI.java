package com.gamerking195.dev.up2date.ui.settings;

import com.gamerking195.dev.up2date.Up2Date;
import com.gamerking195.dev.up2date.ui.SettingsGUI;
import com.gamerking195.dev.up2date.util.gui.GUI;
import com.gamerking195.dev.up2date.util.item.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Caden Kriese (GamerKing195) on 11/19/17.
 * <p>
 * License is specified by the distributor which this
 * file was written for. Otherwise it can be found in the LICENSE file.
 * If there is no license file the code is then completely copyrighted
 * and you must contact me before using it IN ANY WAY.
 */
public class AdvancedGUI extends GUI {
    public AdvancedGUI() {
        super ("&d&lU&5&l2&d&lD &8- &DSettings &8- &dAdvanced", 36);
    }

    @Override
    protected void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

        switch (event.getRawSlot()) {
            case 12:
                new AnvilGUI(Up2Date.getInstance(), player, "Enter amount of connections", (player1, response) -> {

                    if (NumberUtils.isNumber(response)) {
                        int number = Integer.valueOf(response);
                        if (number > 12)
                            number = 12;

                        Up2Date.getInstance().getMainConfig().setThreadPoolSize(number);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        new AdvancedGUI().open(player);
                        return "Success";
                    }

                    return "Invalid input";
                });
                break;
            case 14:
                new AnvilGUI(Up2Date.getInstance(), player, "Enter amount of connections", (player1, response) -> {

                    if (NumberUtils.isNumber(response)) {
                        int number = Integer.valueOf(response);
                        if (number > 15)
                            number = 15;

                        Up2Date.getInstance().getMainConfig().setConnectionPoolSize(number);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        new AdvancedGUI().open(player);
                        return "Success";
                    }

                    return "Invalid input";
                });
                break;
            case 31:
                new SettingsGUI(true).open(player);
                break;
        }
    }

    @Override
    protected void populate() {
        inventory.setItem(12, new ItemStackBuilder(Material.STAINED_GLASS_PANE)
                                      .setDurability((short) 5)
                                      .setName("&f&lTHREAD POOL SIZE")
                                      .setLore(
                                              "&7&lValue: &d&l"+ Up2Date.getInstance().getMainConfig().getThreadPoolSize(),
                                              "&7&lDescription: ",
                                              "     &d&lAmount of threads used while",
                                              "     &d&lparsing your plugins.",
                                              "     &d&lif you have 10-29 plugins leave it,",
                                              "     &d&l30-69 set it to around 10.",
                                              "&7&lMax: &d&l12",
                                              "&7&lType: &d&lInteger",
                                              "&7&lDefault: &d&l5")
                                      .build());

        inventory.setItem(14, new ItemStackBuilder(Material.STAINED_GLASS_PANE)
                                      .setDurability((short) 5)
                                      .setName("&f&lCONNECTION POOL SIZE")
                                      .setLore(
                                              "&7&lValue: &d&l"+ Up2Date.getInstance().getMainConfig().getConnectionPoolSize(),
                                              "&7&lDescription: ",
                                              "     &d&lAmount of connections ",
                                              "     &d&lused while transferring ",
                                              "     &d&ldata to your database.",
                                              "&7&lMax: &d&l15",
                                              "&7&lType: &d&lInteger",
                                              "&7&lDefault: &d&l5")
                                      .build());

        inventory.setItem(31, new ItemStackBuilder(Material.BARRIER).setName("&4&l&m«---&r &cBACK").build());
    }
}
