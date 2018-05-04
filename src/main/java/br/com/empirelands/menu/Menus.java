package br.com.empirelands.menu;

import br.com.empirelands.DreamReports;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.events.PlayerViewReportEvent;
import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.report.Report;
import br.com.empirelands.report.ReportView;
import br.com.empirelands.util.ConfigUtils;
import br.com.empirelands.util.UnicodeSymbols;
import br.com.empirelands.util.Util;
import br.com.empirelands.manager.DatabaseDaoManager;
import br.com.empirelands.util.objects.Item;
import br.com.empirelands.util.objects.SkullItem;
import br.com.empirelands.util.objects.UserBanned;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Menus {

    private ConfigHandler langauge;
    private DatabaseDaoManager dbm;
    private HashMap<String, Object> var = new HashMap<>();
    @Getter
    private static Map<GenericUser, Report> inMenu = new HashMap<>();
    @Getter
    private static Map<GenericUser, GenericUser> staffPerfil = new HashMap<>();

    private static final Item REPRESETANT_ITEM = new Item(Material.DIAMOND_AXE)
            .setName("&6&lEmpire&2&lLands")
            .setLore("&c&lSistema de Reportes")
            .setEnchant(Enchantment.DURABILITY, 1).hideAttributes();

    public Menus(DatabaseDaoManager dbm) {
        langauge = DreamReports.getInstance().getLangFile();
        this.dbm = dbm;
    }

    public void loadReportOptions(GenericUser player, Report r) {
        Item skullReporter = new SkullItem().skull(r.getReporter().getNick())
                .setName("&6Reportador:&e " + r.getReporter().getNick())
                .setLore(" ", "&6Botão Esquerdo &7para abrir o perfil do player", r.getReporter().isOnline() ? "&6Botão Direito &7para ir até a localização do player" : "&6&mBotão Direito&7 para ir até a localização do player");

        Item skullReported = new SkullItem().skull(r.getReported().getNick())
                .setName("&6Reportado:&e " + r.getReported().getNick())
                .setLore(" ", "&6Botão Esquerdo &7para abrir o perfil do player", r.getReported().isOnline() ? "&6Botão Direito &7para ir até a localização do player" : "&6&mBotão Direito&7 para ir até a localização do player");

        Item clayred = new Item(Material.STAINED_CLAY).setName("&c&lPunir " + r.getReporter().getNick()).setLore(" ", "&6Botão Direito &7para punir por abuso de report", "&6Botão Esquerdo para punir com motivo personalizado");
        Item clayyell = new Item(Material.STAINED_CLAY).setName("&e&lPunir " + r.getReported().getNick()).setLore(" ", "&6Botão Direito &7para punir por &c" + r.getMotive(), "&6Botão Esquerdo para punir com motivo personalizado");

        Item flint = new Item(Material.FLINT_AND_STEEL).setName("&cRemover Report").setLore(" ", "&6Botão Esquerdo &7para remover este report");

        Item book = new Item(Material.ENCHANTED_BOOK).setName("&bStaffs que já visualizaram este report: ");
//        List<String> aff = new ArrayList<>();
//        for (GenericUser u : dbm.getReportViewDatabaseMG().getUserThatViewSameReport(r.getReportid())) {
//            aff.add("§7 " + u.getNick());
//        }
//        book.setLore(aff);
        var.clear();
        var.put("%reportid%", r.getReportid());
        String title = langauge.getStringReplaced("Menus.Report-Options", var);
        Inventory inv = new MenuMG(54, title.length() > 32 ? title.substring(0, 29) + ".." : title).getInventory();
        int[] bg = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        int i = new Random().nextInt(15);
        for (int aBg : bg) {
            if (aBg <= inv.getSize()) {
                inv.setItem(aBg, new Item(Material.STAINED_GLASS_PANE).setName(" ").setDurability(i).build());
            }
        }
        inv.setItem(43, flint.build());
        inv.setItem(21, skullReporter.build());
        inv.setItem(22, REPRESETANT_ITEM.build());
        inv.setItem(23, skullReported.build());
        inv.setItem(37, book.build());
        if (player.isStaff()) {
            inv.setItem(30, clayred.setDurability(14).build());
            inv.setItem(32, clayyell.setDurability(14).build());
        } else {
            inv.setItem(30, clayred.setDurability(5).build());
            inv.setItem(32, clayyell.setDurability(5).build());
        }
        player.getHelper().setAtualMenu(MenuType.REPORT_OPTIONS);
        inMenu.put(player, r);
        player.toPlayer().openInventory(inv);
        Bukkit.getPluginManager().callEvent(new PlayerViewReportEvent(player, r));
    }

    public void loadReportMenu(GenericUser player, List<ReportPlayer> reports) {
        var.clear();
        List<ItemStack> skulls = new ArrayList<>();
        for (ReportPlayer r : reports) {
            if (r != null) {
                skulls.add(buildReportSkull(r).build());
            }
        }
        var.put("%quant%", skulls.size());
        Inventory inv = new MenuMG(54, langauge.getStringReplaced("Menus.Reports-Menu", var)).getInventory();
        nextPageInventory(1, inv, player, skulls);
        player.getHelper().setAtualMenu(MenuType.REPORTS);
        player.toPlayer().openInventory(inv);
    }

    public void loadUserPerfil(GenericUser player, GenericUser user) {
        var.clear();
        var.put("%player%", user.getNick());
        String title = langauge.getStringReplaced("Menus.Player-Perfil", var);
        Inventory inv = new MenuMG(45, title.length() > 32 ? title.substring(0, 29) + ".." : title).getInventory();
        int[] bg = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        inv.setItem(20, buildInfo(user).build());
        inv.setItem(22, buildPerfil(user).build());
        inv.setItem(24, new Item(Material.BOOK).setName("&cBanido?").setLore("&5&l" + (dbm.getBanDatabaseMG().isBanned(user) ? "Sim" : "Não")).build());
        for (int aBg : bg) {
            if (aBg <= inv.getSize()) {
                inv.setItem(aBg, new Item(Material.STAINED_GLASS_PANE).setName(" ").setDurability(15).build());
            }
        }
        player.getHelper().setAtualMenu(MenuType.USER_PERFIL);
        player.toPlayer().openInventory(inv);
    }

    public void loadReportPlayerPerfil(GenericUser player, ReportPlayer reportedPlayer) {
        var.clear();
        List<ItemStack> itemStacks = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        for (Report r : reportedPlayer.getReports()) {
            Item it = new Item(Material.PAPER).setName("&cReport &a" + r.getReportid()).setEnchant(Enchantment.DURABILITY, 1).hideAttributes();
            lore.add("&7Status: &a" + Util.getInstancie().suchStatus(r.getProcess()));
            lore.add("&7Criado em: &e" + Util.getInstancie().getDate(r.getReport_date().getTime()));
            lore.add(" ");
            lore.add("&7Reportador: &c" + r.getReporter().getNick() + " &7(" + (r.getReporter().isOnline() ? "&aonline" : "&coffline") + "&7)");
            lore.add("&7Razão do Report: &c" + r.getMotive());
            boolean t = r.getProof() != null && !r.getProof().equals("");
            lore.add("&7Prova do Report: " + (t ? "&b" + r.getProof() : "&4Nenhuma prova apresentada"));
            lore.add(" ");
            if (player.toPlayer().hasPermission("reportes.staff")) {
                lore.add("&6Botão Esquerdo &7para mais opções");
                String rightBotton = "Botão Direito§r &7para copiar o link da prova.";
                if (t) {
                    lore.add("&6" + rightBotton);
                } else lore.add("&6&m" + rightBotton);
            }
            it.setLore(lore);
            lore.clear();
            itemStacks.add(it.build());
        }
        Inventory inv = new MenuMG(54, langauge.getStringReplaced("Menus.Report-User")).getInventory();
        nextPageInventory(1, inv, player, itemStacks);
        player.getHelper().setAtualMenu(MenuType.USER_REPORT);
        player.toPlayer().openInventory(inv);
    }

    public void loadViewReports(GenericUser user, List<ReportView> views) {
        List<ItemStack> items = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        for (ReportView view : views) {
            Item it = new Item(Material.BOOK_AND_QUILL)
                    .setName("&cReport &a" + view.getView().getReportid()).setEnchant(Enchantment.DURABILITY, 1).hideAttributes();
            lore.add("&7Status: &a" + Util.getInstancie().suchStatus(view.getView().getProcess()));
            lore.add("&7Data da visualização: &e" + Util.getInstancie().getDate(view.getViewDate().getTime()));
            lore.add("&6Botão Esquerdo &7para abrir report");
            it.setLore(lore);
            lore.clear();
            items.add(it.build());
        }
        Inventory inv = new MenuMG(54, "§6Views").getInventory();
        nextPageInventory(1, inv, user, items);
        user.getHelper().setAtualMenu(MenuType.VIEWS);
        user.toPlayer().openInventory(inv);
    }

    public void loadBanneds(GenericUser user, List<UserBanned> bans) {
        List<ItemStack> items = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        for (UserBanned ban : bans) {
            Item it = new SkullItem().skull(ban.getUserbanned().getNick()).getItem();
            it.setName("&6Nick: &7" + ban.getUserbanned().getNick());
            lore.add("&6ID: &7" + ban.getUserbanned().getId());
            lore.add("&7Data da aplicação: &e" + Util.getInstancie().getDate(ban.getBandate().getTime()));
            it.setLore(lore);
            lore.clear();
            items.add(it.build());
        }
        Inventory inv = new MenuMG(54, "§b&mBans").getInventory();
        nextPageInventory(1, inv, user, items);
        user.getHelper().setAtualMenu(MenuType.BANS);
        user.toPlayer().openInventory(inv);
    }

    public void loadReportOptionsMenu(GenericUser player, GenericUser reported) {
        List<Item> reportItems = new ArrayList<>();
        var.clear();
        var.put("%player%", reported.getNick());
        for (String s : ConfigUtils.getConfigReasons()) {
            var.put("%reason%", s);
            reportItems.add(new Item(ConfigUtils.getReasonMaterial(s)).setName(langauge.getStringReplaced("Item-report", var)).setLore(langauge.getStringList("Item-report-lore", var)));
        }
        Inventory inv = new MenuMG(54, langauge.getStringReplaced("Menus.Report-Menu", var)).getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            if (i > 8 && i <= 17 || i >= 45) {
                inv.setItem(i, new Item(Material.STAINED_GLASS_PANE).setName(" ").setDurability(7).build());
            }
            if (i > 17 && i < 45) {
                int b = 18;
                for (Item reportItem : reportItems) {
                    inv.setItem(b, reportItem.build());
                    b++;
                }
            }
        }
        inv.setItem(49, new Item(Material.BARRIER).setName("§4§lCancelar").build());
        player.getHelper().setAtualMenu(MenuType.REPORT_SELECTOR);
        player.toPlayer().openInventory(inv);
    }

    public void loadStaffPerfil(GenericUser player, GenericUser staff) {
        var.clear();
        var.put("%player%", staff.getNick());
        String title = langauge.getStringReplaced("Menus.Staff-Perfil", var);
        Inventory inv = new MenuMG(45, title.length() > 32 ? title.substring(0, 29) + ".." : title).getInventory();

        Item bookOfCompletedReports = new Item(Material.ENCHANTED_BOOK)
                .setName("§aReports Concluídos")
                .setLore("&cEste staff já concluiu &e" + staff.getFinishedReports() + " &cReports");

        Item sk = new SkullItem()
                .skull(staff.getNick())
                .setName("&c" + staff.getNick() + " &7(" + (Bukkit.getPlayerExact(staff.getNick()) != null ? "&aonline" : "&coffline") + "&7)");

        ArrayList<String> sklore = new ArrayList<>();
        sklore.add(" ");
        sklore.add("&2" + UnicodeSymbols.TRIANGLE_RIGHT.getSymbol() + "&aRegistrado em: ");
        sklore.add("  &b" + Util.getInstancie().getDate(staff.getRegister_date().getTime()));
        sklore.add("&2" + UnicodeSymbols.TRIANGLE_RIGHT.getSymbol() + "&aReports Visualizados não resolvidos: ");
        int i = 1;
        for (ReportView rvw : dbm.getReportViewDatabaseMG().getUserViewReports(staff.getId())) {
            sklore.add("§aIDR:§b" + rvw.getView().getReportid() + "§7 - §b" + Util.getInstancie().getDate(rvw.getViewDate().getTime()));
            if (i == 5) {
                sklore.add("&7...");
                break;
            }
            i++;
        }
        String atualLocation = "Botão Direito &7para ir até a localização do staff";
        if (staff.isOnline()) {
            sklore.add("&6" + atualLocation);
        } else sklore.add("&6&m" + atualLocation);
        sk.setLore(sklore);
        int[] bg = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        inv.setItem(20, bookOfCompletedReports.build());
        inv.setItem(22, sk.build());
        inv.setItem(24, new Item(Material.BOOK).setName("&b&lReports Visualizados").build());
        for (int aBg : bg) {
            if (aBg <= inv.getSize()) {
                inv.setItem(aBg, new Item(Material.STAINED_GLASS_PANE).setName(" ").setDurability(15).build());
            }
        }
        player.getHelper().setAtualMenu(MenuType.STAFF_PERFIL);
        staffPerfil.put(player, staff);

        player.toPlayer().openInventory(inv);
    }

    public void nextPageInventory(int page, Inventory inventory, GenericUser player, List<ItemStack> list) {
        if (page < 1) {
            final int size = list.size();
            page = size / 45;
            if (size % 45 > 0) {
                ++page;
            }
        }
        int size2 = list.size();
        int n2 = (page - 1) * 45;
        int n3 = (n2 + 44 >= size2) ? (size2 - 1) : (page * 45 - 1);
        if (n3 - n2 + 1 < 1 && page != 1) {
            nextPageInventory(1, inventory, player, list);
            return;
        }
        int n4 = 0;
        for (int i = 0; i < 45; ++i) {
            inventory.setItem(i, (ItemStack) null);
        }
        for (int j = n2; j <= n3; ++j) {
            inventory.setItem(n4, list.get(j));
            ++n4;
        }
        player.getHelper().setAtualPage(page);
        player.getHelper().setAtualItemStackView(list);
        int pagination = (int) Math.round((list.size() / 45) + 0.5D);
        buildIndex(inventory, page, pagination);
        if (player.toPlayer() != null) {
            player.toPlayer().updateInventory();
        }
    }

    private void buildIndex(Inventory inv, int atualIndex, int finalIndex) {
        var.clear();
        var.put("%atual%", atualIndex);
        var.put("%total%", finalIndex);
        var.put("%indice%", langauge.getStringReplaced("indice", var));
        Item next = new Item(Material.PAPER).setName(langauge.getStringReplaced("Proxima-Pagina", var)).setEnchant(Enchantment.DURABILITY, 1).hideAttributes();
        Item anterior = new Item(Material.PAPER).setName(langauge.getStringReplaced("Pagina-Anterior", var)).setEnchant(Enchantment.DURABILITY, 1).hideAttributes();
        inv.setItem(48, anterior.build());
        inv.setItem(50, next.build());
    }

    private Item buildReportSkull(ReportPlayer r) {
        if (r != null) {
            var.put("%player%", r.getNick());
            var.put("%reports%", r.getAllReportsString());
            if (r.getLastReport() != null) {
                var.put("%lastreason%", r.getLastReport().getMotive());
                var.put("%player-reporter%", r.getLastReport().getReporter().getNick());
                var.put("%reporter-online%", (r.getLastReport().getReporter().isOnline() ? "&aOnline" : "&cOffline"));
            } else {
                var.put("%lastreason%", "§7Nenhum report recente.");
                var.put("%player-reporter%", "§7Nenhum report recente.");
                var.put("%reporter-online%", "§7");
            }
            var.put("%reported-online%", r.isOnline() ? "&aOnline" : "&cOffline");
            var.put("%already-logout%", r.isAlreadyLogout() ? "&aSim" : r.isOnline() ? "&cNão" : "&aSim");
            return new SkullItem().skull(r.getNick()).setName(langauge.getStringReplaced("Skull.skull-name", var))
                    .setLore(langauge.getStringList("Skull.skull-lore", var));
        }
        return null;
    }

    private Item buildInfo(GenericUser user) {
        Item info = new Item(Material.ENCHANTED_BOOK).setName("§aInfo do player");
        List<String> lore = new ArrayList<>();
        lore.add("&3" + UnicodeSymbols.DIAMOND.getSymbol() + "&3Reportes enviados: &f" + user.getReports_sends());
        lore.add(" ");
        lore.add("&3" + UnicodeSymbols.DIAMOND.getSymbol() + "&3Reportes recebidos: ");
        lore.add("    &3" + UnicodeSymbols.DIAMOND.getSymbol() + "&3Reportes já recebidos: &f" + user.getReportedAllTimes());
        lore.add(" ");
        lore.add("    &3" + UnicodeSymbols.DIAMOND.getSymbol() + "&3Reportes recebidos hoje: &f" + user.getReportedTimesAtLastRestart());
        info.setLore(lore);
        return info;
    }

    private Item buildPerfil(GenericUser user) {
        Item sk = new SkullItem().skull(user.getNick()).setName("&c" + user.getNick() + " &7(" + (Bukkit.getPlayerExact(user.getNick()) != null ? "&aonline" : "&coffline") + "&7)");
        ArrayList<String> sklore = new ArrayList<>();
        sklore.add(" ");
        sklore.add("&2" + UnicodeSymbols.TRIANGLE_RIGHT.getSymbol() + "&aRegistrado em: ");
        sklore.add("  &b" + Util.getInstancie().getDate(user.getRegister_date().getTime()));
        sklore.add(" ");
        String atualLocation = "Botão Direito§r &7para ir até a localização do player";
        if (user.isOnline()) {
            sklore.add("&6" + atualLocation);
        } else sklore.add("&6&m" + atualLocation);
        sk.setLore(sklore);
        return sk;
    }
}
