package me.zachary.duel.gui;

import me.zachary.duel.Duel;
import me.zachary.zachcore.guis.ZMenu;
import me.zachary.zachcore.guis.buttons.ZButton;
import me.zachary.zachcore.guis.pagination.ZPaginationButtonBuilder;
import me.zachary.zachcore.utils.items.ItemBuilder;
import me.zachary.zachcore.utils.xseries.SkullUtils;
import me.zachary.zachcore.utils.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;
import java.util.stream.IntStream;

public class RequestGui {
    private Duel plugin;

    public RequestGui(Duel plugin) {
        this.plugin = plugin;
    }

    public Inventory getRequestGUI(){
        ZMenu requestGui = Duel.getGUI().create("&6&lRequest duel", 5);
        requestGui.setPaginationButtonBuilder(getPaginationButtonBuilder());
        setGlass(requestGui, 0);

        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        int slot = 10;
        int page = 0;
        for (Player player : players) {
            ItemBuilder skullItem = new ItemBuilder(SkullUtils.getSkull(player.getUniqueId()))
                    .name("&e" + player.getName())
                    .lore(
                            "&2Wins: 0",
                            "&cLoses: 0"
                    );
            ZButton skull = new ZButton(skullItem.build()).withListener(inventoryClickEvent -> {

            });

            requestGui.setButton(page, slot, skull);
            slot++;
            if(slot == 35){
                slot = 0;
                page++;
            }
            int finalPage = page;
            setGlass(requestGui, finalPage);
        }
        return requestGui.getInventory();
    }

    public ZPaginationButtonBuilder getPaginationButtonBuilder(){
        return (type, inventory) -> {
            switch (type) {
                case CLOSE_BUTTON:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf("REDSTONE").parseMaterial())
                            .name("&c&lClose menu")
                            .build()
                    ).withListener(event -> {
                        event.getWhoClicked().closeInventory();
                    });

                case PREV_BUTTON:
                    if (inventory.getCurrentPage() > 0) return new ZButton(new ItemBuilder(XMaterial.valueOf("ARROW").parseMaterial())
                            .name("&a&l\u2190 Previous Page")
                            .lore(
                                    "&aClick to move back to",
                                    "&apage " + inventory.getCurrentPage() + ".")
                            .build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.previousPage(event.getWhoClicked());
                    });
                    else return null;

                case CURRENT_BUTTON:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf("NAME_TAG").parseMaterial())
                            .name("&7&lPage " + (inventory.getCurrentPage() + 1) + " of " + inventory.getMaxPage())
                            .lore(
                                    "&7You are currently viewing",
                                    "&7page " + (inventory.getCurrentPage() + 1) + "."
                            ).build()
                    ).withListener(event -> event.setCancelled(true));

                case NEXT_BUTTON:
                    if (inventory.getCurrentPage() < inventory.getMaxPage() - 2) return new ZButton(new ItemBuilder(XMaterial.valueOf("ARROW").parseMaterial())
                            .name("&a&lNext Page \u2192")
                            .lore(
                                    "&aClick to move forward to",
                                    "&apage " + (inventory.getCurrentPage() + 2) + "."
                            ).build()
                    ).withListener(event -> {
                        event.setCancelled(true);
                        inventory.nextPage(event.getWhoClicked());
                    });
                    else return null;

                case CUSTOM_2:
                    return new ZButton(new ItemBuilder(XMaterial.valueOf("COMPASS").parseMaterial())
                            .name("&6Search a player name.")
                            .lore(
                                    "&aClick to search a",
                                    "&aspecific player name."
                            ).build()
                    );
                case CUSTOM_1:
                case CUSTOM_3:
                case CUSTOM_4:
                case UNASSIGNED:
                default:
                    return null;
            }
        };
    }

    public void setGlass(ZMenu menu, int page) {
        int[] TILES_TO_UPDATE = {
                0,  1,  2,  3,  4,  5,  6,  7,  8,
                9,                             17,
                18,                            26,
                27,                            35,
                36, 37, 38, 39, 40, 41, 42, 43, 44
        };
        IntStream.range(0, TILES_TO_UPDATE.length).map(i -> TILES_TO_UPDATE.length - i + -1).forEach(
                index -> menu.setButton(page, TILES_TO_UPDATE[index], new ZButton(new ItemBuilder(XMaterial.valueOf("GREEN_STAINED_GLASS_PANE").parseMaterial()).build()))
        );
    }
}
