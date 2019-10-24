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
public class ListaSociosTableModel extends AbstractTableModel{

    private ArrayList linhas = null;
    private String[] colunas = null;
    
    //CONSTRUTOR 
    public ListaSociosTableModel (ArrayList lin, String[] col){
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
    }
     
   
    
//    public void addRow(ListaSocios socio){
//        this..add(socio);
//    }
}
