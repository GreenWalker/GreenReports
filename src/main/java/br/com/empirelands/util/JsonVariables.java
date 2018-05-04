package br.com.empirelands.util;

import lombok.Getter;

public enum JsonVariables {

    IN_COOLDOWN("{\"text\":\"§cPor Favor\"}"),
    IN_COOLDOWN_SUB("{\"text\":\"§cAguarde %segundos% Segundos Para Criar um Novo Report\"}"),
    REPORT_CREATED_SUCCEFULL("{\"text\":\"§aSeu Report Foi Enviado\"}"),
    REPORT_CREATED_THANKS("{\"text\":\"§bO Servidor Agradece!\"}"),
    REPORT_CLOSED("{\"text\":\"§aSeu reporte contra §a%contra%§a foi §cencerrado§a!\"}"),
    PLAYER_BANNED("{\"text\":\"§cPlayer banido!\"}"),
    PLAYER_NON_BANNED("{\"text\":\"§cPlayer não foi banido!\"}"),
    LOADING_REPORTS("{\"text\":\"§eCarregando os Reports!\"}"),
    LOADING_REPORTS_SUB("{\"text\":\"§c%percentage% carregado.\"}"),
    CLEAR_ALL_REPORTS("{\"text\":\"§bVocê limpou §a%q%§b reporte(s)!\"}"),
    CLEAR_PLAYER_REPORTS("{\"text\":\"§bVocê limpou §a%q%§b reporte(s) do player §a%p%!\"}"),
    CLEAR_PLAYER_REPORT("{\"text\":\"§bVocê limpou o report §a%q%§b do player §a%p%!\"}");

    @Getter
    private String msg;

    JsonVariables(String msg) {
        this.msg = msg;
    }

}
