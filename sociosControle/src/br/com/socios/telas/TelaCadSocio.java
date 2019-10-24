/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.telas;

import java.sql.*;
import br.com.socios.dao.ModuloConexao;
import java.awt.HeadlessException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.JOptionPane;
/**
 *
 * @author Samuel
 */
public class TelaCadSocio extends javax.swing.JFrame {

    Connection conexao= null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Creates new form TelaPrincipal
     */
    public TelaCadSocio() {
        initComponents();
        conexao = ModuloConexao.conector();
        
    }
    // METODO PARA ADICIONAR INFORMAÇÕES DO CADASTRO DE SOCIOS NA TABELA;
    public void adicionar(){
        String sql = "INSERT INTO SOCIOS(MATRICULA, NOMESOCIO, CPF, STATUS_SC, NASCIMENTO, EMAIL, END_RUA, END_NUMERO, END_BAIRRO, END_CIDADE, END_ESTADO,INICIO_SC, TEL_NUMERO, SEXO)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1,null);
           // String temp = txtNomeSocio.getText();
          //  txtNomeSocio.setText(temp.toUpperCase());
            pst.setString(2, txtNomeSocio.getText());
            pst.setString(3, txtCpf.getText());
            pst.setString(4, "ATIVO");
            try{
                pst.setString(5, transformaData(txtDTNascimento.getText())+"");
            }catch(Exception erro){
                JOptionPane.showMessageDialog(null, "Data Inválida. " + erro);
            }
            pst.setString(6, txtEmail.getText());
            pst.setString(7, txtRua.getText());
            pst.setString(8, txtNumero.getText());
            pst.setString(9, txtBairro.getText());
            pst.setString(10, txtCidade.getText());
            String estados = String.valueOf(cbEstados.getSelectedItem());
            if(cbEstados.getSelectedIndex() != 0){  // COMPARANDO SE O ITEM SELECIONADO NÃO É INDICE 0("SELECIONE"), SE NÃO FOR SALVA NO BANCO
                pst.setString(11, cbEstados.getSelectedItem().toString());
            }
            
            pst.setString(12, transformaData(txtDTInicio.getText()).toString()); // Passando o parametro da data que foi gerada pelo sistema para uma função que transforma uma String para o formato aceito pelo banco de dados
            pst.setString(13, txtTelefone.getText());
            if(rbtnSexoMasculino.isSelected()){  // verifica qual foi selecionado para mandar para o banco
                pst.setString(14, "M");
            }else if(rbtnSexoFeminino.isSelected()){
                pst.setString(14, "F");
            }
            
          // Validação dos campos obrigatórios de acordo com o banco(not null).
          if ((txtNomeSocio.getText().isEmpty()) || (txtCpf.getText().isEmpty()) || (txtCidade.getText().isEmpty()) || 
                  (cbEstados.getSelectedIndex() == 0) ){ // se os campos x, y , z estiverem vazios faça. 
              JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatórios.");
          }else{
              // A linha abaixo atualiza a tabela socio com os dados do formulário
              // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
              
              int adicionado = pst.executeUpdate();
              // a linha abaixo serve de apoio ao entendimento da lógica
              
              if(adicionado > 0){
                  JOptionPane.showMessageDialog(null, "Sócio adicionado com sucesso");
                  
              // Limpa os campos logo após ser salvo    
                  
                  txtNomeSocio.setText(null);
                  txtCpf.setText(null);
                  txtDTNascimento.setText(null);
                  txtEmail.setText(null);
                  txtRua.setText(null);
                  txtNumero.setText(null);
                  txtBairro.setText(null);
                  txtCidade.setText(null);
                  cbEstados.setSelectedIndex(0);
                  txtDTInicio.setText(null);
                  txtTelefone.setText(null);
                  btngroupSexo.clearSelection();  // RESETA A SELEÇÃO DE UM RADIO BOTTON
              }
          }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
        //atualizaTabela();
    }
    
   public LocalDate transformaData(String dataString){ // função que transforma uma String para um formato Date de banco de dados;
      String data = dataString; 
      String[] dataSeparada = data.split("/");// separa a data em vertor de string a cada vez que encontra o simbolo
      LocalDate dateConvertida = LocalDate.of(Integer.parseInt(dataSeparada[2]), Integer.parseInt(dataSeparada[1]),Integer.parseInt(dataSeparada[0]));  
      return dateConvertida;
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
        txtEmail = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtNomeSocio = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        txtRua = new javax.swing.JTextField();
        txtCidade = new javax.swing.JTextField();
        txtBairro = new javax.swing.JTextField();
        txtNumero = new javax.swing.JTextField();
        txtDTInicio = new javax.swing.JLabel();
        btnConcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        rbtnSexoFeminino = new javax.swing.JRadioButton();
        rbtnSexoMasculino = new javax.swing.JRadioButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JFormattedTextField();
        txtDTNascimento = new javax.swing.JFormattedTextField();
        txtTelefone = new javax.swing.JFormattedTextField();
        cbEstados = new javax.swing.JComboBox<>();

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
        jLabel6.setText("CADASTRO DE SÓCIOS");

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

        jLabel19.setForeground(new java.awt.Color(46, 46, 46));
        jLabel19.setText("*Nome:");

        txtEmail.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtEmail.setMinimumSize(new java.awt.Dimension(2, 20));
        txtEmail.setPreferredSize(new java.awt.Dimension(2, 20));
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jLabel20.setForeground(new java.awt.Color(46, 46, 46));
        jLabel20.setText("E-mail:");

        txtNomeSocio.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtNomeSocio.setMinimumSize(new java.awt.Dimension(2, 20));
        txtNomeSocio.setPreferredSize(new java.awt.Dimension(2, 20));
        txtNomeSocio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeSocioActionPerformed(evt);
            }
        });
        txtNomeSocio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomeSocioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeSocioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNomeSocioKeyTyped(evt);
            }
        });

        jLabel21.setForeground(new java.awt.Color(46, 46, 46));
        jLabel21.setText("Data de Início:");

        jLabel23.setForeground(new java.awt.Color(46, 46, 46));
        jLabel23.setText("Endereço:");

        jLabel24.setForeground(new java.awt.Color(46, 46, 46));
        jLabel24.setText("Bairro:");

        jLabel25.setForeground(new java.awt.Color(46, 46, 46));
        jLabel25.setText("*Cidade:");

        jLabel26.setForeground(new java.awt.Color(46, 46, 46));
        jLabel26.setText("Nº:");

        jLabel27.setForeground(new java.awt.Color(46, 46, 46));
        jLabel27.setText("*Estado:");

        txtRua.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtRua.setPreferredSize(new java.awt.Dimension(2, 20));
        txtRua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRuaActionPerformed(evt);
            }
        });
        txtRua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRuaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRuaKeyTyped(evt);
            }
        });

        txtCidade.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtCidade.setPreferredSize(new java.awt.Dimension(2, 20));
        txtCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCidadeActionPerformed(evt);
            }
        });
        txtCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCidadeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCidadeKeyTyped(evt);
            }
        });

        txtBairro.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBairroActionPerformed(evt);
            }
        });
        txtBairro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBairroKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBairroKeyTyped(evt);
            }
        });

        txtNumero.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtNumero.setPreferredSize(new java.awt.Dimension(2, 20));
        txtNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroActionPerformed(evt);
            }
        });

        txtDTInicio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtDTInicio.setForeground(new java.awt.Color(46, 46, 46));
        txtDTInicio.setText("Início");

        btnConcluir.setBackground(new java.awt.Color(131, 150, 255));
        btnConcluir.setText("Concluir");
        btnConcluir.setBorder(null);
        btnConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConcluirActionPerformed(evt);
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

        jLabel28.setForeground(new java.awt.Color(46, 46, 46));
        jLabel28.setText("Status:");

        txtStatus.setFont(new java.awt.Font("Futura Md BT", 0, 15)); // NOI18N
        txtStatus.setForeground(new java.awt.Color(5, 8, 124));
        txtStatus.setText("Ativo");

        jLabel30.setForeground(new java.awt.Color(46, 46, 46));
        jLabel30.setText("*Sexo:");

        rbtnSexoFeminino.setBackground(new java.awt.Color(224, 222, 222));
        btngroupSexo.add(rbtnSexoFeminino);
        rbtnSexoFeminino.setText("F");
        rbtnSexoFeminino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSexoFemininoActionPerformed(evt);
            }
        });

        rbtnSexoMasculino.setBackground(new java.awt.Color(224, 222, 222));
        btngroupSexo.add(rbtnSexoMasculino);
        rbtnSexoMasculino.setText("M");
        rbtnSexoMasculino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSexoMasculinoActionPerformed(evt);
            }
        });

        jLabel31.setForeground(new java.awt.Color(46, 46, 46));
        jLabel31.setText("Telefone:");

        jLabel32.setForeground(new java.awt.Color(46, 46, 46));
        jLabel32.setText("*CPF:");

        jLabel33.setForeground(new java.awt.Color(46, 46, 46));
        jLabel33.setText("Data de Nascimento:");

        jLabel29.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(86, 86, 86));
        jLabel29.setText("(*) Campos Obrigatórios");

        txtCpf.setBorder(null);
        try {
            txtCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCpf.setPreferredSize(new java.awt.Dimension(45, 20));
        txtCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfActionPerformed(evt);
            }
        });

        txtDTNascimento.setBorder(null);
        try {
            txtDTNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDTNascimento.setPreferredSize(new java.awt.Dimension(32, 20));
        txtDTNascimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDTNascimentoActionPerformed(evt);
            }
        });

        txtTelefone.setBorder(null);
        try {
            txtTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)# ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefone.setPreferredSize(new java.awt.Dimension(48, 20));
        txtTelefone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefoneActionPerformed(evt);
            }
        });

        cbEstados.setMaximumRowCount(5);
        cbEstados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(117, 117, 117)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDTNascimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel29)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(22, 22, 22)
                            .addComponent(btnConcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNomeSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(375, 375, 375)
                                    .addComponent(rbtnSexoMasculino)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(rbtnSexoFeminino))
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addGap(322, 322, 322)
                                        .addComponent(jLabel30))
                                    .addComponent(txtCpf, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel28)
                                .addComponent(txtStatus))
                            .addGap(45, 45, 45)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtDTInicio)
                                .addComponent(jLabel21)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel25)
                                        .addComponent(jLabel23)
                                        .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(24, 24, 24)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel27)))
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                                            .addComponent(txtRua, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel26)
                                                .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel24)
                                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel30)
                    .addComponent(jLabel28)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNomeSocio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtnSexoMasculino)
                    .addComponent(rbtnSexoFeminino)
                    .addComponent(txtStatus)
                    .addComponent(txtDTInicio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDTNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbEstados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(737, 505));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtNomeSocioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeSocioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeSocioActionPerformed

    private void txtRuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRuaActionPerformed

    private void txtCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCidadeActionPerformed

    private void txtBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBairroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBairroActionPerformed

    private void txtNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroActionPerformed

    private void rbtnSexoFemininoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSexoFemininoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSexoFemininoActionPerformed

    private void rbtnSexoMasculinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSexoMasculinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtnSexoMasculinoActionPerformed

    private void btnConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConcluirActionPerformed
        // Método para adiciona socio
        adicionar();
    }//GEN-LAST:event_btnConcluirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfActionPerformed

    private void txtDTNascimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDTNascimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDTNascimentoActionPerformed

    private void txtTelefoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefoneActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       Date date = new Date();
       txtDTInicio.setText(dateFormat.format(date));
    
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

    private void txtNomeSocioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeSocioKeyTyped
        // TODO add your handling code here
    }//GEN-LAST:event_txtNomeSocioKeyTyped

    private void txtNomeSocioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeSocioKeyPressed
        // TODO add your handling code         
    }//GEN-LAST:event_txtNomeSocioKeyPressed

    private void txtRuaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRuaKeyTyped
        // TODO add your handling code he
    }//GEN-LAST:event_txtRuaKeyTyped

    private void txtBairroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBairroKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBairroKeyTyped

    private void txtCidadeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCidadeKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCidadeKeyTyped

    private void txtNomeSocioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeSocioKeyReleased
        // TODO add your handling code here:
             String temp = txtNomeSocio.getText();
            txtNomeSocio.setText(temp.toUpperCase());
    }//GEN-LAST:event_txtNomeSocioKeyReleased

    private void txtRuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRuaKeyReleased
        // TODO add your handling code here:
        String temp2 = txtRua.getText();
            txtRua.setText(temp2.toUpperCase());
    }//GEN-LAST:event_txtRuaKeyReleased

    private void txtBairroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBairroKeyReleased
        // TODO add your handling code here:
        String temp3 = txtBairro.getText();
            txtBairro.setText(temp3.toUpperCase());
    }//GEN-LAST:event_txtBairroKeyReleased

    private void txtCidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCidadeKeyReleased
        // TODO add your handling code here:
        String temp4 = txtCidade.getText();
            txtCidade.setText(temp4.toUpperCase()); 
    }//GEN-LAST:event_txtCidadeKeyReleased

    
   
    
    
    
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
            java.util.logging.Logger.getLogger(TelaCadSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaCadSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaCadSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaCadSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaCadSocio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConcluir;
    private javax.swing.ButtonGroup btngroupSexo;
    private javax.swing.JComboBox<String> cbEstados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton rbtnSexoFeminino;
    private javax.swing.JRadioButton rbtnSexoMasculino;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JFormattedTextField txtCpf;
    private javax.swing.JLabel txtDTInicio;
    private javax.swing.JFormattedTextField txtDTNascimento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNomeSocio;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtRua;
    private javax.swing.JLabel txtStatus;
    private javax.swing.JFormattedTextField txtTelefone;
    // End of variables declaration//GEN-END:variables

    private void atualizaTabela() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
