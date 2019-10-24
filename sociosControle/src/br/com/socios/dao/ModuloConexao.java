/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.dao;

import java.sql.*;

/**
 *
 * @author Samuel
 */
public class ModuloConexao { // faz a conecção do banco de dados com a aplicação.
    // MÉTODO RESPONSÁVEL POR ESTABELECER A CONEXÃO COM O BANCO

    public static Connection conector() {
        java.sql.Connection conexao = null; // pega no pacote connection e cria uma variável atribuindo a null
        // a linha abaixo "chama" o driver
        String driver = "com.mysql.jdbc.Driver"; // Cria uma variável String que recebe o diretório do driver
        // Armazenando informações referente ao banco
        String url = "jdbc:mysql://localhost:3306/dbsocios";
        String user = "root";
        String password = "";
        
        // Estabelecendo a conexão com o banco
        try {
            Class.forName(driver); // executar o arquivo do driver
            conexao = DriverManager.getConnection(url, user, password); // variável acima, obter as conexões utilizando esses parametros.
            return conexao;
        } catch (Exception e) {
            // a linha abaixo serve de apoio para ver o erro de conexao
            System.out.println(e);
            return null; // caso aconteça algum erro  na conexao
       }
        
        
    }
}
