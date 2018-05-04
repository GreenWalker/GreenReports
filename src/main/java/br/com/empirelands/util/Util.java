package br.com.empirelands.util;

import br.com.empirelands.report.Report;
import fancyfull.main.java.mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static Util instancie;

    private Util(){

    }

    public static Util getInstancie() {
        if(null == instancie){
            instancie = new Util();
        }
        return instancie;
    }

    public boolean checkIsStaff(Player p) {
        if (p != null) {
            return p.hasPermission("dcreports.staff");
        }
        return false;
    }

    public String getDate(long time){
        SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy 'as' HH:mm:ss");
        return sp.format(new Date(time));
    }

    public boolean isNumber(String s){
        try{
            int i = Integer.valueOf(s);
            return true;
        } catch (NumberFormatException err){
            return false;
        }
    }

    public String getPrefix() {
        return Prefix.getPrefix();
    }

    public String colorize(String message){
       return ChatColor.translateAlternateColorCodes('&',message);
    }

    public String getSkullOwner(ItemStack it) {
        if (it.getType() == Material.SKULL_ITEM) {
            SkullMeta k = (SkullMeta) it.getItemMeta();
            return k.getOwner();
        }
        return null;
    }

    public FancyMessage sendReportId(Player p, Report r) {
        return new FancyMessage("§cID do reporte gerado: ").then("§f" + r.getReportid()).tooltip("§6Clique §7para abrir o reporte").command("/reporte info " + r.getReportid());
    }

    public void removePlayerEffects(Player p){
        if(p.hasPotionEffect(PotionEffectType.BLINDNESS) && p.hasPotionEffect(PotionEffectType.JUMP)) {
            p.removePotionEffect(PotionEffectType.SLOW);
            p.removePotionEffect(PotionEffectType.JUMP);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.removePotionEffect(PotionEffectType.CONFUSION);
        }
    }

    public boolean checkUrl(String s){
        return Patterns.WEB_URL.matcher(s).matches();
    }

    public void sendHelpMessage(CommandSender commandSender) {
        commandSender.sendMessage("§3=--------------- §2§lLista de Comandos §3---------------=");
        commandSender.sendMessage(" §e§lDica §7tente passar o §f§n§omouse§r §7sobre as mensagens.");
        commandSender.sendMessage(" ");
        new FancyMessage(" §7- ")
                        .then("§6/reportar ")
                .tooltip("§6pseudônimos:", "§f/report", "§f/rep", "§f/denunciar")
                        .then("§c<jogador> ")
                .tooltip("§6Especificação §cObrigatória."," ","§7Player que será reportado.", "§cLembre-se, qualquer denúncia tola poderá gerar", "§cConsequências.")
                        .then("§7[motivo] ")
                .tooltip("§6Especificação §eOpcional."," ","§7Motivo, razão ou circunstancia do report.", "§cPrecione tab para ver os motivos disponíveis.", "")
                        .then("§7[prova]")
                .tooltip("§6Especificação §eOpcional.", " ", "§7Prova clara do motivo do report.", "§7(link de um vídeo / print do chat)", "§cApresentar provas é opcional (exceto em denúncias de chat)", "§cCaso seja apresentada uma prova tola", "§cA denúncia será absorvida.", "§cNao utilize encurtadores de link!", "§7Caso o link da prova seja muito grande, ou haja mais de 1 (uma) prova", "§7Utilize o site §6imgur.com§7 agrupe as provas é mande o link.")
                .send(commandSender);
        new FancyMessage(" §7- ")
                        .then("§6/reporte ")
                        .then("§6info ")
                .tooltip("§7Carrega seu perfil.")
                        .then("§7<jogador> ")
                .tooltip("§6Especificação §eOpcional."," ","§7Carrega perfil do player especificado", "§cNecessita de permissão adicional.")
                .send(commandSender);
        if(commandSender.isOp() || (commandSender instanceof Player && commandSender.hasPermission("reportes.staff"))){
            new FancyMessage(" §7- ")
                    .then("§6/reportes ")
                    .tooltip("§6pseudônimos:", "§f/reports", "§f/denuncias", "§f/reps")
                    .then("§7[jogador] ")
                    .tooltip("§6Especificação §eOpcional."," ","§7Listar todos os reports do player diretamente")
                    .send(commandSender);
            new FancyMessage(" §7- ")
                    .then("§6/reportes ")
                    .tooltip("§6pseudônimos:", "§f/reports", "§f/reps", "§f/rps")
                    .then("§6limpar ")
                    .then("§7[player] ")
                    .tooltip("§6Especificação §eOpcional."," ","§7Caso não seja especificado §cAPAGARA§7 todos os reports")
                    .then("§7[report-id]")
                    .tooltip("§6Especificação §eOpcional."," ","§7Caso não seja especificado §cAPAGARA§7 todos os reports do player")
                    .send(commandSender);
        }
        commandSender.sendMessage("§3-----------------------------------------------------");
    }

    public String suchStatus(Report.ReportProcess t) {
        switch (t) {
            case IN_WAITING:
                return "§4Em Espera";
            case ACCEPTED:
                return "§aAceito";
            case IN_PROGRESS:
                return "§eEm Análise";
        }
        return t.toString();
    }
}
