/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.telas;

import java.sql.*;
import br.com.socios.dao.ModuloConexao;
import br.com.socios.outros.ListaBoletosTableModel;
import br.com.socios.outros.ListaSociosTableModel;
import java.awt.HeadlessException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author Samuel
 */
public class TelaDetalheSocioEdit extends javax.swing.JFrame {

    Connection conexao= null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int matricSc = 0;
    /**
     * Creates new form TelaPrincipal
     */
    public TelaDetalheSocioEdit() {
        initComponents();
        conexao = ModuloConexao.conector();
        //SetarSocio();

    }
    // METODO PARA ADICIONAR INFORMAÇÕES DO CADASTRO DE SOCIOS NA TABELA;
    
    public void setarSocio(int matricula){
        matricSc = matricula;
        String sql = "SELECT * FROM SOCIOS WHERE MATRICULA = ?";
        try {
             
            pst = conexao.prepareStatement(sql);
            pst.setString(1, matricula+"");// matricula +"");
            rs=pst.executeQuery();
            if(rs.next()){
                txtMatriculaed.setText(rs.getInt(1)+"");
                txtNomeSocioed.setText(rs.getString(2));
                txtCpfed.setText(rs.getString(3));
                cbStatusSc.setSelectedItem(rs.getString(4));    //(rs.getString(4));
                txtDtNasced.setText(rs.getString(5));
                txtEmailed.setText(rs.getString(6));
                txtEnderecoed.setText(rs.getString(7));
                txtNumeroed.setText(rs.getString(8));
                txtBairroed.setText(rs.getString(9));
                txtCidadeed.setText(rs.getString(10));
                cbEstados.setSelectedItem(rs.getString(11));  //(rs.getString(11)); parametro é um objeto;
                txtDataIniciodt.setText(transformaPadraoData(rs.getString(12)));
                txtDataFimdt.setText(transformaPadraoData(rs.getString(13)));
                txtTelefoneed.setText(rs.getString(14));
                
                if((rs.getString(15)).equals("M")){
                    rbtnSexoMasculino.setSelected(true);
                }else{
                    rbtnSexoFeminino.setSelected(true);
                } 
            }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
    }
    
    public void alterarSocio(int matricula){
        String sql = "UPDATE SOCIOS SET NOMESOCIO = ?, CPF = ?, STATUS_SC = ?, NASCIMENTO = ?, EMAIL = ?, END_RUA = ?, END_NUMERO = ? "
                + ", END_BAIRRO = ?, END_CIDADE = ?, END_ESTADO = ?, FIM_SC = ?, TEL_NUMERO = ?, SEXO = ? WHERE MATRICULA = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtNomeSocioed.getText());
            pst.setString(2, txtCpfed.getText());
            pst.setString(3, cbStatusSc.getSelectedItem().toString()); // -----------
            pst.setString(4, txtDtNasced.getText());
            pst.setString(5, txtEmailed.getText());
            pst.setString(6, txtEnderecoed.getText());
            pst.setString(7, txtNumeroed.getText());
            pst.setString(8, txtBairroed.getText());
            pst.setString(9, txtCidadeed.getText());
            pst.setString(10, cbEstados.getSelectedItem().toString());  
            if(cbStatusSc.getSelectedItem().toString().equals("INATIVO")){
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                txtDataFimdt.setText(dateFormat.format(date));
                pst.setString(11, transformaData(txtDataFimdt.getText()).toString());
            }else{
                pst.setString(11, null);
            }
            pst.setString(12, txtTelefoneed.getText());
            
            
//            String estados = String.valueOf(cbEstados.getSelectedItem());
//            if(cbEstados.getSelectedIndex() != 0){  // COMPARANDO SE O ITEM SELECIONADO NÃO É INDICE 0("SELECIONE"), SE NÃO FOR SALVA NO BANCO
//                pst.setString(11, cbEstados.getSelectedItem().toString());
//            }
            
            //pst.setString(12, transformaData(txtDTInicio.getText()).toString()); // Passando o parametro da data que foi gerada pelo sistema para uma função que transforma uma String para o formato aceito pelo banco de dados
            
            if(rbtnSexoMasculino.isSelected()){  // verifica qual foi selecionado para mandar para o banco
                pst.setString(13, "M");
            }else if(rbtnSexoFeminino.isSelected()){
                pst.setString(13, "F");
            }
            pst.setString(14, matricula+"");
            
          // Validação dos campos obrigatórios de acordo com o banco(not null).
          if ((txtNomeSocioed.getText().isEmpty()) || (txtCpfed.getText().isEmpty()) || (txtCidadeed.getText().isEmpty())){ // se os campos x, y , z estiverem vazios faça. 
              JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatórios.");
          }else{
              // A linha abaixo atualiza a tabela socio com os dados do formulário
              // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
              
              int adicionado = pst.executeUpdate();
              // a linha abaixo serve de apoio ao entendimento da lógica
              
              if(adicionado > 0){
                  JOptionPane.showMessageDialog(null, "Sócio Alterado com sucesso");
                    this.dispose();
                    
              }
          }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
    }
    
    
   public LocalDate transformaData(String dataString){ // função que transforma uma String para um formato Date de banco de dados;
      String data = dataString; 
      String[] dataSeparada = data.split("/");// separa a data em vertor de string a cada vez que encontra o simbolo
      LocalDate dateConvertida = LocalDate.of(Integer.parseInt(dataSeparada[2]), Integer.parseInt(dataSeparada[1]),Integer.parseInt(dataSeparada[0]));  
      return dateConvertida;
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngroupSexo = new javax.swing.ButtonGroup();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtNomeSocioed = new javax.swing.JTextField();
        txtEmailed = new javax.swing.JTextField();
        txtMatriculaed = new javax.swing.JLabel();
        txtCpfed = new javax.swing.JFormattedTextField();
        txtDtNasced = new javax.swing.JFormattedTextField();
        txtTelefoneed = new javax.swing.JFormattedTextField();
        txtEnderecoed = new javax.swing.JTextField();
        txtNumeroed = new javax.swing.JTextField();
        txtBairroed = new javax.swing.JTextField();
        txtCidadeed = new javax.swing.JTextField();
        cbEstados = new javax.swing.JComboBox<>();
        rbtnSexoMasculino = new javax.swing.JRadioButton();
        rbtnSexoFeminino = new javax.swing.JRadioButton();
        cbStatusSc = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtDataIniciodt = new javax.swing.JLabel();
        txtInicio = new javax.swing.JLabel();
        txtInicio1 = new javax.swing.JLabel();
        txtDataFimdt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Controle de Sócios - Home");
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(224, 222, 222));

        jPanel1.setBackground(new java.awt.Color(96, 0, 244));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Futura Md BT", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DETALHAMENTO DE SÓCIO (EDIÇÃO)");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/socio_branco.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(32, 32, 32))
        );

        jLabel19.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(46, 46, 46));
        jLabel19.setText("Nome:");

        jLabel20.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(46, 46, 46));
        jLabel20.setText("E-mail:");

        jLabel21.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(46, 46, 46));
        jLabel21.setText("Vigência:");

        jLabel23.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(46, 46, 46));
        jLabel23.setText("Endereço:");

        jLabel25.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(46, 46, 46));
        jLabel25.setText("Cidade:");

        btnAlterar.setBackground(new java.awt.Color(131, 150, 255));
        btnAlterar.setText("Alterar");
        btnAlterar.setBorder(null);
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(253, 158, 158));
        btnCancelar.setText("Sair");
        btnCancelar.setBorder(null);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(46, 46, 46));
        jLabel28.setText("Status:");

        jLabel30.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(46, 46, 46));
        jLabel30.setText("Sexo:");

        jLabel31.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(46, 46, 46));
        jLabel31.setText("Telefone:");

        jLabel32.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(46, 46, 46));
        jLabel32.setText("CPF:");

        jLabel33.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(46, 46, 46));
        jLabel33.setText("Data de Nasc.:");

        jLabel41.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(46, 46, 46));
        jLabel41.setText("Estado:");

        jLabel42.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(46, 46, 46));
        jLabel42.setText("Nº:");

        jLabel43.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(46, 46, 46));
        jLabel43.setText("Bairro:");

        jLabel22.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(46, 46, 46));
        jLabel22.setText("Matric.:");

        txtNomeSocioed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N
        txtNomeSocioed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeSocioedKeyReleased(evt);
            }
        });

        txtEmailed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N

        txtMatriculaed.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtMatriculaed.setText("Matricula");

        try {
            txtCpfed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDtNasced.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtTelefoneed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)# ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtEnderecoed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N
        txtEnderecoed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEnderecoedKeyReleased(evt);
            }
        });

        txtNumeroed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N

        txtBairroed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N
        txtBairroed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBairroedKeyReleased(evt);
            }
        });

        txtCidadeed.setFont(new java.awt.Font("Futura Md BT", 2, 12)); // NOI18N
        txtCidadeed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCidadeedKeyReleased(evt);
            }
        });

        cbEstados.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        cbEstados.setMaximumRowCount(5);
        cbEstados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" }));

        rbtnSexoMasculino.setBackground(new java.awt.Color(224, 222, 222));
        btngroupSexo.add(rbtnSexoMasculino);
        rbtnSexoMasculino.setText("M");
        rbtnSexoMasculino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSexoMasculinoActionPerformed(evt);
            }
        });

        rbtnSexoFeminino.setBackground(new java.awt.Color(224, 222, 222));
        btngroupSexo.add(rbtnSexoFeminino);
        rbtnSexoFeminino.setText("F");
        rbtnSexoFeminino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSexoFemininoActionPerformed(evt);
            }
        });

        cbStatusSc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ATIVO", "INATIVO", "BLOQUEADO" }));

        txtDataIniciodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtDataIniciodt.setForeground(new java.awt.Color(46, 46, 46));
        txtDataIniciodt.setText("DtInicio");

        txtInicio.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        txtInicio.setText("Início:");

        txtInicio1.setFont(new java.awt.Font("Futura Md BT", 0, 10)); // NOI18N
        txtInicio1.setText("Fim:");

        txtDataFimdt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtDataFimdt.setForeground(new java.awt.Color(46, 46, 46));
        txtDataFimdt.setText("DtFim");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDataIniciodt, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInicio)
                            .addComponent(txtInicio1))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtDataFimdt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDataIniciodt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtInicio1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDataFimdt)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel43)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel25)
                            .addComponent(jLabel22)
                            .addComponent(jLabel19)
                            .addComponent(jLabel31)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtEnderecoed, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumeroed, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                .addComponent(txtCidadeed, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbEstados, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbStatusSc, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addComponent(txtCpfed, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel8Layout.createSequentialGroup()
                                                    .addGap(100, 100, 100)
                                                    .addComponent(txtDtNasced, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jLabel33)))
                                        .addComponent(txtBairroed, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel8Layout.createSequentialGroup()
                                            .addComponent(txtTelefoneed, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel30)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(rbtnSexoMasculino)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(rbtnSexoFeminino))
                                        .addComponent(txtEmailed, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNomeSocioed, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addComponent(txtMatriculaed)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAlterar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel21)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(28, 28, 28))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtMatriculaed)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtNomeSocioed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtCpfed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(txtDtNasced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtTelefoneed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30)
                            .addComponent(rbtnSexoMasculino)
                            .addComponent(rbtnSexoFeminino))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtEmailed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtEnderecoed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(txtNumeroed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(cbStatusSc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(txtBairroed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel41)
                    .addComponent(txtCidadeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(289, 289, 289))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(652, 478));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        // Método para adiciona socio
        alterarSocio(matricSc);
        this.dispose();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
       
    
    }//GEN-LAST:event_formWindowActivated

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        // TODO add your handling code her
    }//GEN-LAST:event_formComponentHidden

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code 
    }//GEN-LAST:event_formWindowClosed

    private void formWindowIconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowIconified
        // TODO add your handling code
    }//GEN-LAST:event_formWindowIconified

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void rbtnSexoMasculinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSexoMasculinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSexoMasculinoActionPerformed

    private void rbtnSexoFemininoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSexoFemininoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSexoFemininoActionPerformed

    private void txtNomeSocioedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeSocioedKeyReleased
        // TODO add your handling code here:
        String temp = txtNomeSocioed.getText();
            txtNomeSocioed.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtNomeSocioedKeyReleased

    private void txtEnderecoedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEnderecoedKeyReleased
        // TODO add your handling code here:
        String temp1 = txtEnderecoed.getText();
            txtEnderecoed.setText(temp1.toUpperCase());
    }//GEN-LAST:event_txtEnderecoedKeyReleased

    private void txtBairroedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBairroedKeyReleased
        // TODO add your handling code here:
        String temp2 = txtBairroed.getText();
            txtBairroed.setText(temp2.toUpperCase());
    }//GEN-LAST:event_txtBairroedKeyReleased

    private void txtCidadeedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCidadeedKeyReleased
        // TODO add your handling code here:
        String temp3 = txtCidadeed.getText();
            txtCidadeed.setText(temp3.toUpperCase());
    }//GEN-LAST:event_txtCidadeedKeyReleased

    
   
    
    
    
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
            java.util.logging.Logger.getLogger(TelaDetalheSocioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocioEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaDetalheSocioEdit().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.ButtonGroup btngroupSexo;
    private javax.swing.JComboBox<String> cbEstados;
    private javax.swing.JComboBox<String> cbStatusSc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton rbtnSexoFeminino;
    private javax.swing.JRadioButton rbtnSexoMasculino;
    private javax.swing.JTextField txtBairroed;
    private javax.swing.JTextField txtCidadeed;
    private javax.swing.JFormattedTextField txtCpfed;
    private javax.swing.JLabel txtDataFimdt;
    private javax.swing.JLabel txtDataIniciodt;
    private javax.swing.JFormattedTextField txtDtNasced;
    private javax.swing.JTextField txtEmailed;
    private javax.swing.JTextField txtEnderecoed;
    private javax.swing.JLabel txtInicio;
    private javax.swing.JLabel txtInicio1;
    private javax.swing.JLabel txtMatriculaed;
    private javax.swing.JTextField txtNomeSocioed;
    private javax.swing.JTextField txtNumeroed;
    private javax.swing.JFormattedTextField txtTelefoneed;
    // End of variables declaration//GEN-END:variables

    
}
