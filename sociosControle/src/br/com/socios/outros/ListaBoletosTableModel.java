/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.outros;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Samuel
 */
public class ListaBoletosTableModel extends AbstractTableModel{

    private ArrayList linhas = null;
    private String[] colunas = null;
    
    //CONSTRUTOR 
    public ListaBoletosTableModel (ArrayList lin, String[] col){
        setLinhas(lin);
        setColunas(col);
    }
    // METODOS SET GET -
    public ArrayList getLinhas() {
        return linhas;
    }

    public void setLinhas(ArrayList linhas) {
        this.linhas = linhas;
    }

    public String[] getColunas() {
        return colunas;
    }

    public void setColunas(String[] colunas) {
        this.colunas = colunas;
    }
    
    // METODOS ESPECIAIS
    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }
    
    @Override
    public int getRowCount() {
        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    

    @Override
    public Object getValueAt(int numLin, int numCol) {
        Object[] linha = (Object[])getLinhas().get(numLin);
        return linha[numCol];
//        switch(numCol){
//            case 0:
//                return dados.get(linha).getNomeSocio();
//            case 1:
//                return dados.get(linha).getCpf();
//            case 2:
//                return dados.get(linha).getVencimento();
//            case 3:
//                return dados.get(linha).getValor();
//            case 4:
//                return dados.get(linha).getStatus(); // referente ao campo de pagBoleto da tabela, irá dizer se o pagamento foi feito ou não.
//            case 5:
//                return dados.get(linha).getCidade();
//            case 6:
//                return dados.get(linha).getEstado();
//            case 7:
//                return dados.get(linha).getStatusSocio();
        }
        //return null;
    
    
//    public void addRow(ListaSocios socio){
//        this..add(socio);
//    }
}
