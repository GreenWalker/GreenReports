package br.com.empirelands.listeners;

import br.com.empirelands.DreamReports;
import br.com.empirelands.commands.player.ReportCmd;
import br.com.empirelands.config.ConfigHandler;
import br.com.empirelands.listeners.events.CommandPreProcess;
import br.com.empirelands.menu.Menus;

import br.com.empirelands.player.normal_user.GenericUser;
import br.com.empirelands.player.report_user.ReportPlayer;
import br.com.empirelands.report.Report;
import br.com.empirelands.util.ConfigUtils;
import br.com.empirelands.util.Util;
import br.com.empirelands.util.UtilMethods;
import br.com.empirelands.manager.DatabaseDaoManager;
import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static br.com.empirelands.menu.MenuType.*;

public class InventoryClickHandler implements Listener {

    private UtilMethods methods;
    private DatabaseDaoManager daoManager;
    private Menus menus;
    private ConfigHandler language;
    private FileConfiguration config;

    public InventoryClickHandler(UtilMethods methods, DatabaseDaoManager daoManager, Menus menus) {
        this.methods = methods;
        this.daoManager = daoManager;
        this.menus = menus;
        this.language = DreamReports.getInstance().getLangFile();
        this.config = DreamReports.getInstance().getConfig();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        GenericUser user = methods.getUser(p);
        if (user == null) return;
        if (event.getInventory().getType() == InventoryType.CRAFTING) return;
        if (user.getHelper().getAtualMenu() == null || user.getHelper().getAtualMenu() == NONE) return;
        if (event.getClickedInventory() != null) {
            if (event.getCurrentItem().getItemMeta() == null) return;
            if (event.getCurrentItem().getType() == Material.AIR) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            Material itemType = item.getType();
            switch (user.getHelper().getAtualMenu()) {
                case REPORTS: {
                    if (itemType == Material.SKULL_ITEM) {
                        if (event.getClick() == ClickType.LEFT) {
                            ReportPlayer reportPlayer = methods.getReportPlayer(Util.getInstancie().getSkullOwner(item));
                            if (reportPlayer == null) {
                                p.closeInventory();
                                p.sendMessage("§cOcorreu um erro!, tente novamente ou abra o report manualmente.");
                                return;
                            }
                            menus.loadReportPlayerPerfil(user, reportPlayer);
                        }
                    }
                    if (itemType == Material.PAPER) {
                        int u = user.getHelper().getAtualPage();
                        if (event.getSlot() == 48) {
                            menus.nextPageInventory(u - 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        } else if (event.getSlot() == 50) {
                            menus.nextPageInventory(u + 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        }
                    }
                    break;
                }
                case USER_PERFIL: {
                    if (itemType == Material.SKULL_ITEM) {
                        if (event.getClick() == ClickType.LEFT) {
                            Player player = Bukkit.getPlayerExact(Util.getInstancie().getSkullOwner(item));
                            if (player == null) {
                                p.closeInventory();
                                p.sendMessage("§cPlayer está offline!");
                                return;
                            }
                            if (p.hasPermission("dcreports.staff.teleport")) {
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
                                p.teleport(player.getLocation());
                                return;
                            } else {
                                p.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                                return;
                            }
                        }
                        break;
                    }
                }
                case USER_REPORT: {
                    if (itemType == Material.PAPER) {
                        int u = user.getHelper().getAtualPage();
                        if (event.getSlot() == 48) {
                            menus.nextPageInventory(u - 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        } else if (event.getSlot() == 50) {
                            menus.nextPageInventory(u + 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        }
                        String s = item.getItemMeta().getDisplayName().split(" ")[1].trim().replaceAll("§", "").replaceAll("a", "");
                        if (!Util.getInstancie().isNumber(s)) {
                            p.closeInventory();
                            p.sendMessage("§Ocorreu um erro! abra o report manualmente.");
                            return;
                        }
                        Report r = methods.getReport(Integer.valueOf(s));
                        if (event.getClick() == ClickType.LEFT) {
                            if (r == null) {
                                p.closeInventory();
                                p.sendMessage("§cOcorreu um erro! veja o console e contate o desenvolvedor. ");
                                return;
                            }
                            menus.loadReportOptions(user, r);
                            break;
                        }
                        if (event.getClick() == ClickType.RIGHT) {
                            if (r == null) {
                                p.closeInventory();
                                p.sendMessage("§cOcorreu um erro! veja o console e contate o desenvolvedor. ");
                                return;
                            }
                            Inventory inv = event.getInventory();
                            if (r.getProof() != null && !r.getProof().equalsIgnoreCase("")) {
                                p.sendMessage("§9§m" + Strings.repeat("-", 53));
                                p.sendMessage("§2Você tem 10 segundos para copiar a prova.");
                                p.sendMessage("§3" + r.getProof());
                                p.sendMessage("§9§m" + Strings.repeat("-", 53));
                                CommandPreProcess.getInCommandListening().add(p);
                                p.closeInventory();
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (p.isOnline()) {
                                            p.openInventory(inv);
                                        }
                                        CommandPreProcess.getInCommandListening().remove(p);
                                        cancel();
                                    }
                                }.runTaskLater(DreamReports.getInstance(), 12 * 20);
                            } else {
                                p.sendMessage("§cEste Reporte não possui provas.");
                            }
                        }
                    }
                }
                case STAFF_PERFIL: {
                    if (itemType == Material.SKULL_ITEM) {
                        Player player = Bukkit.getPlayerExact(Util.getInstancie().getSkullOwner(item));
                        if (event.getClick() == ClickType.RIGHT) {
                            if (player == null) {
                                p.closeInventory();
                                p.sendMessage("§cStaff está offline!");
                                return;
                            }
                            if (p.hasPermission("dcreports.staff.teleport")) {
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
                                p.teleport(player.getLocation());
                                return;
                            } else {
                                p.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                                return;
                            }
                        }
                    }
                    if (itemType == Material.BOOK) {
                        menus.loadViewReports(user, daoManager.getReportViewDatabaseMG().getUserViewReports(Menus.getStaffPerfil().get(user).getId()));
                    }
                    break;
                }
                case REPORT_OPTIONS: {
                    if (itemType == Material.SKULL_ITEM) {
                        String s = Util.getInstancie().getSkullOwner(item);
                        if (event.getClick() == ClickType.LEFT) {
                            GenericUser u = methods.getUser(s);

                            if (u.isStaff() || daoManager.getStaffDatabaseMG().getStaff(u.getId()) != null) {
                                menus.loadStaffPerfil(user, u);
                                return;
                            }
                            menus.loadUserPerfil(user, u);
                            return;
                        }
                        if (event.getClick() == ClickType.RIGHT) {
                            Player player = Bukkit.getPlayerExact(s);
                            if (player == null) {
                                p.closeInventory();
                                p.sendMessage("§cPlayer está offline!");
                                return;
                            }
                            if (p.hasPermission("dcreports.staff.teleport")) {
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1F, 1F);
                                p.teleport(player.getLocation());
                                return;
                            } else {
                                p.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                                return;
                            }
                        }
                    }
                    if (itemType == Material.STAINED_CLAY) {
                        if (item.getDurability() == 14) {
                            Report r = Menus.getInMenu().get(user);
                            if (event.getClick() == ClickType.RIGHT) {
                                if (event.getSlot() == 30) {
                                    methods.banPlayer(r.getReporter(), "abuso de reporte");
                                } else if (event.getSlot() == 32) {
                                    methods.banPlayer(r.getReported(), "Uso de " + r.getMotive());
                                }
                            } else if (event.getClick() == ClickType.LEFT) {
                                if (event.getSlot() == 30) {
                                    p.sendMessage(language.getStringReplacedWithPrefix("Input-Ban-Command").replaceAll("%player%", r.getReporter().getNick()));
                                    ChatHandler.getBanCommand().put(p, r.getReporter());
                                } else if (event.getSlot() == 32) {
                                    p.sendMessage(language.getStringReplacedWithPrefix("Input-Ban-Command").replaceAll("%player%", r.getReported().getNick()));
                                    ChatHandler.getBanCommand().put(p, r.getReported());
                                }
                            }
                        }
                    }
                    if (itemType == Material.FLINT_AND_STEEL) {
                        Report r = Menus.getInMenu().get(user);
                        Menus.getInMenu().remove(user);
                        if (r != null) {
                            if (user.isStaff()) {
                                p.closeInventory();
                                ReportPlayer pp = methods.getReportPlayer(r.getReported().getNick());
                                GenericUser staff = methods.getStaff(user);
                                methods.deleteReport(staff, pp, r.getReportid());
                                int i = staff.getFinishedReports() + 1;
                                daoManager.getStaffDatabaseMG().updateCompletedReports(staff.getId(), i);
                                staff.setFinishedReports(i);
                                p.sendMessage(language.getStringReplacedWithPrefix("Report-Closed").replaceAll("%id%", String.valueOf(r.getReportid())));
                            } else {
                                p.closeInventory();
                                p.sendMessage(language.getStringReplacedWithPrefix("Need-Permission"));
                                return;
                            }
                        }
                    }
                    break;
                }
                case REPORT_SELECTOR: {
                    if (itemType == Material.BARRIER) {
                        p.closeInventory();
                        return;
                    }
                    for (String s : ConfigUtils.getConfigReasons()) {
                        if (itemType == ConfigUtils.getReasonMaterial(s)) {
                            GenericUser reported = methods.getUser(ReportCmd.getWhoReported().get(user.getNick()));
                            p.closeInventory();
                            Bukkit.dispatchCommand(p, "reportar " + reported.getNick() + " " + s);
                            // methods.createReport(user, reported, s, "");
                        }
                    }
                    break;
                }
                case VIEWS: {
                    if (itemType == Material.BOOK_AND_QUILL) {
                        if (event.getClick() == ClickType.LEFT) {
                            String s = item.getItemMeta().getDisplayName().split(" ")[1].trim().replaceAll("§", "").replaceAll("a", "");
                            if (!Util.getInstancie().isNumber(s)) {
                                p.sendMessage("§Ocorreu um erro! abra o report manualmente.");
                                return;
                            }
                            Report r = methods.getReport(Integer.valueOf(s));
                            if (r == null) {
                                p.closeInventory();
                                p.sendMessage("§cOcorreu um erro! veja o console e contate o desenvolvedor. ");
                                return;
                            }
                            menus.loadReportOptions(user, r);
                        }
                    }
                    if (itemType == Material.PAPER) {
                        int u = user.getHelper().getAtualPage();
                        if (event.getSlot() == 48) {
                            menus.nextPageInventory(u - 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        } else if (event.getSlot() == 50) {
                            menus.nextPageInventory(u + 1, event.getInventory(), user, user.getHelper().getAtualItemStackView());
                            p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player p = (Player) event.getPlayer();
            GenericUser u = methods.getUser(p);
            if (u != null && u.getHelper().getAtualMenu() != null) {
                u.getHelper().setAtualMenu(NONE);
                u.getHelper().setAtualPage(1);
                u.getHelper().setAtualItemStackView(null);
            }
        }
    }
}