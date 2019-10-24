/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.telas;

import java.sql.*;
import br.com.socios.dao.ModuloConexao;
import br.com.socios.outros.ListaSociosTableModel;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import net.proteanit.sql.DbUtils; // importa abaixo recursos da biblioteca rs2xml.jar


/**
 *
 * @author Samuel
 */
public class TelaSocios extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    boolean taAtivo = true;
    
            
           
 
//private static TelaSocios instancia;
//    
//  static TelaSocios getInstance() throws SQLException{ //FUNÇÃO QUE VERIFICA SE FOI INSTANCIADO A PÁGINA
//      if(instancia == null){
//          instancia = new TelaSocios();
//      }
//      return instancia;
//  }
    
    public TelaSocios() throws SQLException {
        this.setLocationRelativeTo(null);
//        String sql = "SELECT SOCIOS.MATRICULA, SOCIOS.NOMESOCIO, SOCIOS.CPF, BOLETO.VENCIMENTO, BOLETO.VALOR, BOLETO.STATUSBOLETO,"
//                  + "SOCIOS.END_CIDADE, SOCIOS.END_ESTADO, SOCIOS.STATUS_SC "
//                  + "FROM SOCIOS "
//                  + "INNER JOIN BOLETO "
//                  + "ON SOCIOS.MATRICULA = BOLETO.FK_MATRICULA";
        initComponents();
        conexao = ModuloConexao.conector();
       atualizaTabela();
        
       
    }

    // MÉTODO PARA A PESQUISAR SOCIOS PELO NOME COM FILTRO
    private void pesquisarSocio() throws SQLException {
        if(txtPesquisa.getText().length() == 0){
            atualizaTabela();
        }else{
            String sql = "SELECT * FROM SOCIOS WHERE NOMESOCIO LIKE ?";
            try {
                pst = conexao.prepareStatement(sql);
                // passando o conteúdo da caixa de pesquisa para o ?
                // atenção ao % que é a continuação do comando acima.
                pst.setString(1, txtPesquisa.getText() + "%");
                rs=pst.executeQuery();
                // a linha abaixo usa a biblioteca rs2xml.jar  para preencher a tabela
                tblSocios.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException ErroSql) {
                JOptionPane.showMessageDialog(rootPane, "Erro ao preencher a Lista"+ ErroSql);
                //----------------------------------------------------------------
            }
        }
        
    }
  
    void atualizaTabela() throws SQLException{
        if(taAtivo){
            int countLine = 0;
            float valorTotal = 0;
            String sql = "SELECT SOCIOS.MATRICULA, SOCIOS.NOMESOCIO, SOCIOS.CPF, BOLETO.VENCIMENTO, BOLETO.VALOR, BOLETO.STATUSBOLETO,"
                      + "SOCIOS.END_CIDADE, SOCIOS.END_ESTADO, SOCIOS.STATUS_SC "
                      + "FROM SOCIOS "
                      + "LEFT JOIN BOLETO "
                      + "ON SOCIOS.MATRICULA = BOLETO.FK_MATRICULA";
         //    as linhas abaixo preparam a consulta ao banco em função do que foi digitado nas caixas de texto

             ArrayList dados = new ArrayList(); // LISTA DE CADA LINHA DA TABELA
             String[] colunas = {"MATRICULA","NOME","CPF", "VENCIMENTO","VALOR","PAG. BOLETO","CIDADE","UF","STATUS"}; //DEFINE O NOME DAS COLUNAS E ORDEM

            try {
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery(); // responsavel por obter dados.
                rs.first(); // CARREGA A PRIMEIRA LINHA --- VALIDA O RESULT SET.
                do{
                    dados.add(new Object[]{ rs.getInt(1), // CRIA UM VETOR DE OBJETOS ONDE CADA ÍNDICE CONTEM A INFORMAÇÃO QUE ESTÁ NO BANCO DAQUELE ÍNDICE 
                                            rs.getString(2),   //EXEMPLO - CONTEM A INFORMAÇÃO DO BANCO NO 2 ÍNDICE DO SQL --> NOME=> JOAQUIM
                                            rs.getString(3),
                                            transformaPadraoData(rs.getString(4)),
                                            rs.getFloat(5),
                                            rs.getString(6),
                                            rs.getString(7),
                                            rs.getString(8),
                                            rs.getString(9)
                            
                    });
                    
                  countLine ++; // CONTAGEM DO VALOR DE LINHAS ;
                  valorTotal += rs.getFloat(5); // CONTAGEM DOS VALORES AS DOAÇÕES
                }while(rs.next()); // RETORNA TRUE ENQUANDO TIVER LINHA NA TABELA
                txtTotalSocios.setText(countLine+"");
                txtValorTotal.setText(valorTotal+"");
            } catch (SQLException errosql) {
                JOptionPane.showMessageDialog(rootPane, "Erro ao preencher ArrayList"+ errosql);
            }finally{
                //conexao.close();
                rs.close();

            }
            ListaSociosTableModel modelo = new ListaSociosTableModel(dados, colunas);
            tblSocios.setModel(modelo);
            tblSocios.getColumnModel().getColumn(0).setPreferredWidth(50);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(0).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(1).setPreferredWidth(230);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(1).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(2).setPreferredWidth(100);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(2).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(3).setPreferredWidth(95);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(3).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(4).setPreferredWidth(80);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(4).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(5).setPreferredWidth(100);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(5).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(6).setPreferredWidth(117);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(6).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(7).setPreferredWidth(30);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(7).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getColumnModel().getColumn(8).setPreferredWidth(100);// DEFINE O TAMANHO DA COLUNA
            tblSocios.getColumnModel().getColumn(8).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
            tblSocios.getTableHeader().setReorderingAllowed(false); // NAO PODERÁ REORGANIZAR O CABEÇARIO
            tblSocios.setAutoResizeMode(tblSocios.AUTO_RESIZE_OFF); // USUÁRIO NÃO PODERÁ REDIMENSIONAR A TABELA;
            tblSocios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //USUARIO ´SÓ PODERA SELECIONAR 1 LINHA.
            // CENTRALIZANDO COLUNA

            DefaultTableCellRenderer tabelaCenter = new DefaultTableCellRenderer();
            DefaultTableCellRenderer tabelaDireita = new DefaultTableCellRenderer();
            tabelaCenter.setHorizontalAlignment(SwingConstants.CENTER);
            tabelaDireita.setHorizontalAlignment(SwingConstants.RIGHT);
            tblSocios.getColumnModel().getColumn(3).setCellRenderer(tabelaCenter);
            tblSocios.getColumnModel().getColumn(5).setCellRenderer(tabelaCenter);
            tblSocios.getColumnModel().getColumn(8).setCellRenderer(tabelaCenter);
            tblSocios.getColumnModel().getColumn(4).setCellRenderer(tabelaDireita);
            
            //this.setEnabled(true);
        }
        
    }
    
     public String transformaPadraoData (String date){
         if(date == null){
             return null;
         }
       String data = date+"";
       String[] dataSeparada = data.split("-");
       String dataConvertida = dataSeparada[2]+"/"+dataSeparada[1]+"/"+dataSeparada[0];
       return dataConvertida;
   } 
//    public void listarTabela() throws SQLException{
//     // CÓDIGO QUE SELECIONA COLUNA DE 2 TABELAS SOCIOS E BOLETO;
//         atualizaTabela();
//}
//   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        MenuHome = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnEdit = new javax.swing.JLabel();
        btnAddSocios = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtPesquisa = new javax.swing.JTextField();
        cbCidades = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        cbAtivo = new javax.swing.JCheckBox();
        cbInativo = new javax.swing.JCheckBox();
        cbBloqueado = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        cbPago = new javax.swing.JCheckBox();
        cbPendente = new javax.swing.JCheckBox();
        cbVencido = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSocios = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtTotalSocios = new javax.swing.JLabel();
        txtValorTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Controle de Sócios - Home");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(49, 58, 101));

        jPanel2.setBackground(new java.awt.Color(11, 21, 60));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Controle");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("de");

        jLabel3.setFont(new java.awt.Font("Futura Md BT", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Sócios");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(19, 19, 19)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        MenuHome.setBackground(new java.awt.Color(55, 65, 101));
        MenuHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MenuHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuHomeMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("HOME");

        javax.swing.GroupLayout MenuHomeLayout = new javax.swing.GroupLayout(MenuHome);
        MenuHome.setLayout(MenuHomeLayout);
        MenuHomeLayout.setHorizontalGroup(
            MenuHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuHomeLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel4)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        MenuHomeLayout.setVerticalGroup(
            MenuHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jPanel5.setBackground(new java.awt.Color(118, 125, 153));
        jPanel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("SÓCIOS");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(84, 84, 84))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(MenuHome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(MenuHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(96, 0, 244));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Futura Md BT", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("LISTA DE SÓCIOS");

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/edit24.png"))); // NOI18N
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMouseClicked(evt);
            }
        });

        btnAddSocios.setBackground(new java.awt.Color(255, 51, 51));
        btnAddSocios.setForeground(new java.awt.Color(255, 102, 102));
        btnAddSocios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/add24.png"))); // NOI18N
        btnAddSocios.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddSocios.setDoubleBuffered(true);
        btnAddSocios.setName("Add"); // NOI18N
        btnAddSocios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddSociosMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAddSociosMouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddSocios)
                .addGap(43, 43, 43)
                .addComponent(btnEdit)
                .addGap(43, 43, 43))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEdit)
                    .addComponent(btnAddSocios)
                    .addComponent(jLabel6))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(234, 233, 233));

        txtPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaActionPerformed(evt);
            }
        });
        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
            }
        });

        cbCidades.setFont(new java.awt.Font("Futura Md BT", 0, 11)); // NOI18N
        cbCidades.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione a Cidade:" }));
        cbCidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCidadesActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/lupa.png"))); // NOI18N

        jPanel6.setBackground(new java.awt.Color(208, 208, 208));

        cbAtivo.setBackground(new java.awt.Color(208, 208, 208));
        cbAtivo.setText("Ativo");

        cbInativo.setBackground(new java.awt.Color(208, 208, 208));
        cbInativo.setText("Inativo");
        cbInativo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbInativoActionPerformed(evt);
            }
        });

        cbBloqueado.setBackground(new java.awt.Color(208, 208, 208));
        cbBloqueado.setText("Bloqueado");

        jLabel7.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(75, 75, 75));
        jLabel7.setText("Status do Sócio");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbInativo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbAtivo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbBloqueado)
                        .addGap(17, 17, 17))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(93, 93, 93))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbAtivo)
                    .addComponent(cbBloqueado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbInativo)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(208, 208, 208));

        cbPago.setBackground(new java.awt.Color(208, 208, 208));
        cbPago.setText("Pago");

        cbPendente.setBackground(new java.awt.Color(208, 208, 208));
        cbPendente.setText("Pendente");

        cbVencido.setBackground(new java.awt.Color(208, 208, 208));
        cbVencido.setText("Vencido");

        jLabel8.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(60, 60, 60));
        jLabel8.setText("Status do Boleto");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbPago)
                            .addComponent(jLabel8))
                        .addGap(2, 2, 2)
                        .addComponent(cbPendente))
                    .addComponent(cbVencido))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbPago)
                    .addComponent(cbPendente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbVencido)
                .addContainerGap())
        );

        jLabel9.setBackground(new java.awt.Color(84, 84, 84));
        jLabel9.setFont(new java.awt.Font("Futura Md BT", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(74, 74, 74));
        jLabel9.setText("Filtros:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o mês" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbCidades, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbCidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jScrollPane1.setBorder(null);

        tblSocios.setAutoCreateRowSorter(true);
        tblSocios.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        tblSocios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MATRÍCULA", "NOME", "CPF", "VENCIMENTO", "VALOR", "PAG. BOLETO", "CIDADE", "UF", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSocios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSocios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblSocios.setGridColor(new java.awt.Color(255, 255, 255));
        tblSocios.setRowHeight(17);
        tblSocios.setRowMargin(2);
        tblSocios.setSelectionBackground(new java.awt.Color(163, 81, 240));
        tblSocios.setShowHorizontalLines(false);
        tblSocios.setShowVerticalLines(false);
        tblSocios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSociosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSocios);
        if (tblSocios.getColumnModel().getColumnCount() > 0) {
            tblSocios.getColumnModel().getColumn(0).setResizable(false);
            tblSocios.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblSocios.getColumnModel().getColumn(1).setResizable(false);
            tblSocios.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblSocios.getColumnModel().getColumn(2).setResizable(false);
            tblSocios.getColumnModel().getColumn(2).setPreferredWidth(40);
            tblSocios.getColumnModel().getColumn(3).setResizable(false);
            tblSocios.getColumnModel().getColumn(3).setPreferredWidth(20);
            tblSocios.getColumnModel().getColumn(4).setResizable(false);
            tblSocios.getColumnModel().getColumn(4).setPreferredWidth(20);
            tblSocios.getColumnModel().getColumn(5).setResizable(false);
            tblSocios.getColumnModel().getColumn(5).setPreferredWidth(40);
            tblSocios.getColumnModel().getColumn(6).setResizable(false);
            tblSocios.getColumnModel().getColumn(6).setPreferredWidth(50);
            tblSocios.getColumnModel().getColumn(7).setResizable(false);
            tblSocios.getColumnModel().getColumn(7).setPreferredWidth(5);
            tblSocios.getColumnModel().getColumn(8).setResizable(false);
        }

        jLabel11.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(70, 68, 68));
        jLabel11.setText("Total de Socios:");

        jLabel12.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(70, 68, 68));
        jLabel12.setText("Valor Total:");

        txtTotalSocios.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        txtTotalSocios.setForeground(new java.awt.Color(70, 68, 68));
        txtTotalSocios.setText("Total de Socios:");

        txtValorTotal.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        txtValorTotal.setForeground(new java.awt.Color(70, 68, 68));
        txtValorTotal.setText("Valor Total:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotalSocios)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorTotal)
                        .addGap(289, 289, 289)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(txtTotalSocios)
                    .addComponent(txtValorTotal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );

        setSize(new java.awt.Dimension(1182, 714));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbCidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCidadesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbCidadesActionPerformed

    private void txtPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaActionPerformed

    private void MenuHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuHomeMouseClicked
        // Quando clicar vai para
        TelaPrincipal home = new TelaPrincipal();
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_MenuHomeMouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel5MouseClicked
    // O EVENTO QUE ENQUANTO VAI DIGITANDO FAZ ALGO
    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        
        try {
            pesquisarSocio();
        } catch (SQLException ex) {
            Logger.getLogger(TelaSocios.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // QUANDO A JANELA FOR ABERTA IRÁ FAZER..
         
       //   listarTabela();
     
    }//GEN-LAST:event_formWindowActivated

    private void btnAddSociosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSociosMouseClicked
        // TODO add your handling code here:
        if(taAtivo){
            taAtivo = false;
            btnAddSocios.setEnabled(false);
           // this.setEnabled(false);
            TelaCadSocio cadSocio = new TelaCadSocio();
            cadSocio.setVisible(true);
            cadSocio.addWindowListener(new WindowListener(){
                @Override
                public void windowOpened(WindowEvent we) {

                }

                @Override
                public void windowClosing(WindowEvent we) {

                }

                @Override
                public void windowClosed(WindowEvent we) {
                    try {
                        taAtivo = true;
                        btnAddSocios.setEnabled(true);
                        atualizaTabela();
                    } catch (SQLException ex) {
                        Logger.getLogger(TelaSocios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void windowIconified(WindowEvent we) {

                }

                @Override
                public void windowDeiconified(WindowEvent we) {

                }

                @Override
                public void windowActivated(WindowEvent we) {

                }

                @Override
                public void windowDeactivated(WindowEvent we) {

                }
            });
        }
    }//GEN-LAST:event_btnAddSociosMouseClicked

    private void btnAddSociosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddSociosMouseExited
        // TODO add your handling code here:
       // btnAddSocios.PressedSet();
    }//GEN-LAST:event_btnAddSociosMouseExited

    private void cbInativoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbInativoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbInativoActionPerformed

    private void tblSociosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSociosMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            if(taAtivo){
                taAtivo= false;
                btnEdit.setEnabled(false);
                int indiceLinha = tblSocios.getSelectedRow();
                TelaDetalheSocio detalheSocio = new TelaDetalheSocio();
               // detalheSocio.getValueAt(tblSocios.getSelectedRow()),1);

                detalheSocio.setarSocio(Integer.parseInt(tblSocios.getValueAt(indiceLinha, 0).toString()));
                detalheSocio.setVisible(true);
                detalheSocio.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent we) {
                    }

                    @Override
                    public void windowClosing(WindowEvent we) {
                    }

                    @Override
                    public void windowClosed(WindowEvent we) {
                        try {
                            taAtivo = true;
                            btnEdit.setEnabled(true);
                            
                            atualizaTabela();
                        } catch (SQLException ex) {
                            Logger.getLogger(TelaSocios.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void windowIconified(WindowEvent we) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent we) {
                    }

                    @Override
                    public void windowActivated(WindowEvent we) {
                    }

                    @Override
                    public void windowDeactivated(WindowEvent we) {
                    }
                });
                
            }
         }
            
        
    }//GEN-LAST:event_tblSociosMouseClicked

    private void btnEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMouseClicked
        // TODO add your handling code here:
        if(taAtivo){
            int indiceLinha = tblSocios.getSelectedRow();
            
            if(indiceLinha != -1){
                taAtivo = false;
                btnEdit.setEnabled(false);
                

                    TelaDetalheSocio detalheSocio = new TelaDetalheSocio();
                   // detalheSocio.getValueAt(tblSocios.getSelectedRow()),1);

                    detalheSocio.setarSocio(Integer.parseInt(tblSocios.getValueAt(indiceLinha, 0).toString()));
                    detalheSocio.setVisible(true);
                    detalheSocio.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent we) {
                        
                    }

                    @Override
                    public void windowClosing(WindowEvent we) {
                    }

                    @Override
                    public void windowClosed(WindowEvent we) {
                       
                        try {
                            taAtivo = true;
                            btnEdit.setEnabled(true);
                            
                            atualizaTabela();
                        } catch (SQLException ex) {
                            Logger.getLogger(TelaSocios.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void windowIconified(WindowEvent we) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent we) {
                    }

                    @Override
                    public void windowActivated(WindowEvent we) {
                    }

                    @Override
                    public void windowDeactivated(WindowEvent we) {
                    }
                });
                    
            }else{
                JOptionPane.showMessageDialog(null, "Selecione o Sócio que deseja editar!");
            }
        }
  
    }//GEN-LAST:event_btnEditMouseClicked

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaSocios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaSocios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaSocios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaSocios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TelaSocios().setVisible(true); // essa é a linha válida se eu quiser retirar o close conexao.
                } catch (SQLException ex) {
                    Logger.getLogger(TelaSocios.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MenuHome;
    private javax.swing.JLabel btnAddSocios;
    private javax.swing.JLabel btnEdit;
    private javax.swing.JCheckBox cbAtivo;
    private javax.swing.JCheckBox cbBloqueado;
    private javax.swing.JComboBox<String> cbCidades;
    private javax.swing.JCheckBox cbInativo;
    private javax.swing.JCheckBox cbPago;
    private javax.swing.JCheckBox cbPendente;
    private javax.swing.JCheckBox cbVencido;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSocios;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JLabel txtTotalSocios;
    private javax.swing.JLabel txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
