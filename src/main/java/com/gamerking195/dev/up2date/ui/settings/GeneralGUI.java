package com.gamerking195.dev.up2date.ui.settings;

import com.gamerking195.dev.up2date.Up2Date;
import com.gamerking195.dev.up2date.config.MainConfig;
import com.gamerking195.dev.up2date.ui.SettingsGUI;
import com.gamerking195.dev.up2date.util.gui.GUI;
import com.gamerking195.dev.up2date.util.item.ItemStackBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Caden Kriese (flogic)
 * <p>
 * Created on 11/9/17
 */
public class GeneralGUI extends GUI {
    public GeneralGUI() {
        super("&d&lU&5&l2&d&lD &8- &DSettings &8- &dGeneral", 36);
    }

    @Override
    protected void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);

        switch (event.getRawSlot()) {
            case 12:
                new AnvilGUI(Up2Date.getInstance(), player, "Enter amount of minutes", (player1, response) -> {
                    response = response.replace(" ", "").replace("m", "").replace("minute", "").replace("minutes", "");

                    if (NumberUtils.isNumber(response)) {
                        int number = Integer.valueOf(response);
                        if (number < 5)
                            number = 5;

                        MainConfig.getConf().setCacheRefreshDelay(number);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        new GeneralGUI().open(player);
                        return "Success";
                    }

                    return "Invalid input";
                });
                break;
            case 14:
                new AnvilGUI(Up2Date.getInstance(), player, "Enter amount of minutes", (player1, response) -> {
                    response = response.replace(" ", "").replace("m", "").replace("minute", "").replace("minutes", "");

                    if (NumberUtils.isNumber(response)) {
                        int number = Integer.valueOf(response);
                        if (number < 5)
                            number = 5;

                        MainConfig.getConf().setDatabaseRefreshDelay(number);
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        new GeneralGUI().open(player);
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
        inventory.setItem(12, new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE)
                                      .setName("&f&lCACHE REFRESH DELAY")
                                      .setLore(
                                              "&7&lValue: &d&l" + MainConfig.getConf().getCacheRefreshDelay(),
                                              "&7&lDescription: ",
                                              "     &d&lInterval for how long before we refresh the cache ",
                                              "     &d&l(check for updates) for all of your plugins.",
                                              "&7&lNote:",
                                              "     &d&lThe refresh process takes an estimated &5" + Bukkit.getPluginManager().getPlugins().length * 2 + " &d&lseconds.",
                                              "&7&lType: &d&lInteger (Minutes)",
                                              "&7&lMinimum: &d&l5",
                                              "&7&lDefault: &d&l120")
                                      .build());

        inventory.setItem(14, new ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE)
                                      .setName("&f&lDATABASE REFRESH DELAY")
                                      .setLore(
                                              "&7&lValue: &d&l" + MainConfig.getConf().getDatabaseRefreshDelay(),
                                              "&7&lDescription: ",
                                              "     &d&lHow often the server will ping the",
                                              "     &d&ldatabase and update plugin info.",
                                              "&7&lNote:",
                                              "     &d&lOnly applies to people with SQL enabled.",
                                              "&7&lType: &d&lInteger (Minutes)",
                                              "&7&lMinimum: &d&l5",
                                              "&7&lDefault: &d&l30")
                                      .build());

        inventory.setItem(31, new ItemStackBuilder(Material.BARRIER).setName("&4&l&m«---&r &cBACK").build());
    }
}
