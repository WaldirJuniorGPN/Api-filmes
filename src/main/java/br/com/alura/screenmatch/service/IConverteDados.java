package br.com.alura.screenmatch.service;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
    <T> List<T> obterDadosEmLista(String json, Class<T> clazz);
}
