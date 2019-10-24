/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.socios.telas;

import java.sql.*;
import br.com.socios.dao.ModuloConexao;
import br.com.socios.outros.EnviarEmail;
import br.com.socios.outros.ListaBoletosTableModel;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.socios.telas.TelaDetalheSocio;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
//import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.EnumAceite;
import org.jrimum.utilix.Dates;

/**
 *
 * @author Samuel
 */
public class TelaDetalheSocio extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int matricSocio = 0;
    boolean taAtivo = true;
    boolean ehAlteracao = false;

    /**
     * Creates new form TelaPrincipal
     */
    public TelaDetalheSocio() {
        initComponents();
        conexao = ModuloConexao.conector();
        //System.out.println(conversorPontuacaoApresentacaoTela(10.2+""));

        //SetarSocio();
    }
    // METODO PARA ADICIONAR INFORMAÇÕES DO CADASTRO DE SOCIOS NA TABELA;

    public void setarSocio(int matricula) {
        matricSocio = matricula;
        String sql = "SELECT * FROM SOCIOS WHERE MATRICULA = ?";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, matricula + "");// matricula +"");
            rs = pst.executeQuery();
            if (rs.next()) {
                txtMatriculadt.setText(rs.getInt(1) + "");
                txtNomeSociodt.setText(rs.getString(2));
                txtCpfdt.setText(rs.getString(3));
                txtStatusdt.setText(rs.getString(4));
                txtDataNascimentodt.setText(rs.getString(5));
                txtEmaildt.setText(rs.getString(6));
                txtEnderecodt.setText(rs.getString(7));
                txtNumerodt.setText(rs.getString(8));
                txtBairrodt.setText(rs.getString(9));
                txtCidadedt.setText(rs.getString(10));
                txtEstadodt.setText(rs.getString(11));
                txtDataIniciodt.setText(transformaPadraoData(rs.getString(12)));
                if (rs.getString(13) != null) {
                    txtDataFimdt.setText(transformaPadraoData(rs.getString(13)));
                } else {
                    txtDataFimdt.setText(rs.getString(13));
                }
                txtTelefonedt.setText(rs.getString(14));
                txtSexodt.setText(rs.getString(15));
                String statusSocio = rs.getString(4);
                System.out.println(statusSocio);
                statusDifAtivo(statusSocio);
            }
            atualizaListaBoletos(matricula);

        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
        //atualizaTabela();
    }

    public void atualizaListaBoletos(int primaryKey) {
        String sql = "SELECT *FROM BOLETO";

        ArrayList dados = new ArrayList(); // LISTA DE CADA LINHA DA TABELA
        String[] colunas = {"ID", "BANCO", "VALOR", "VENCIMENTO", "PAGO EM", "STATUS"}; //DEFINE O NOME DAS COLUNAS E ORDEM

        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery(); // responsavel por obter dados.
            rs.first(); // CARREGA A PRIMEIRA LINHA --- VALIDA O RESULT SET.
            do {
                if (primaryKey == rs.getInt(8)) {
                    dados.add(new Object[]{ //rs.getInt(1), // CRIA UM VETOR DE OBJETOS ONDE CADA ÍNDICE CONTEM A INFORMAÇÃO QUE ESTÁ NO BANCO DAQUELE ÍNDICE 
                        completarComZeros(rs.getString(1)), //EXEMPLO - CONTEM A INFORMAÇÃO DO BANCO NO 2 ÍNDICE DO SQL --> NOME=> JOAQUIM
                        rs.getString(3),
                        rs.getString(4),
                        transformaPadraoData(rs.getString(5)),
                        transformaPadraoData(rs.getString(6)),
                        rs.getString(7)
                    });
                 
                }
            } while (rs.next()); // RETORNA TRUE ENQUANDO TIVER LINHA NA TABELA 
            ehAlteracao = false;
        } catch (SQLException errosql) {
            JOptionPane.showMessageDialog(rootPane, "Erro ao preencher ArrayList" + errosql);
        }
        ListaBoletosTableModel modelo = new ListaBoletosTableModel(dados, colunas);
        tblBoletos.setModel(modelo);
        tblBoletos.getColumnModel().getColumn(0).setPreferredWidth(55);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(0).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
        tblBoletos.getColumnModel().getColumn(1).setPreferredWidth(150);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(1).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
        tblBoletos.getColumnModel().getColumn(2).setPreferredWidth(150);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(2).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
        tblBoletos.getColumnModel().getColumn(3).setPreferredWidth(180);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(3).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
        tblBoletos.getColumnModel().getColumn(4).setPreferredWidth(150);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(4).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;
        tblBoletos.getColumnModel().getColumn(5).setPreferredWidth(200);// DEFINE O TAMANHO DA COLUNA
        tblBoletos.getColumnModel().getColumn(5).setResizable(false); // DEFINE SE É POSSIVEL EDITAR O TAMANHO DA COLUNA;

        tblBoletos.getTableHeader().setReorderingAllowed(false); // NAO PODERÁ REORGANIZAR O CABEÇARIO
        tblBoletos.setAutoResizeMode(tblBoletos.AUTO_RESIZE_OFF); // USUÁRIO NÃO PODERÁ REDIMENSIONAR A TABELA;
        tblBoletos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  //USUARIO ´SÓ PODERA SELECIONAR 1 LINHA.
        // CENTRALIZANDO COLUNA

        DefaultTableCellRenderer tabelaCenter = new DefaultTableCellRenderer();
        DefaultTableCellRenderer tabelaDireita = new DefaultTableCellRenderer();
        tabelaCenter.setHorizontalAlignment(SwingConstants.CENTER);
        tabelaDireita.setHorizontalAlignment(SwingConstants.RIGHT);
        tblBoletos.getColumnModel().getColumn(1).setCellRenderer(tabelaCenter);
        tblBoletos.getColumnModel().getColumn(3).setCellRenderer(tabelaCenter);
        tblBoletos.getColumnModel().getColumn(4).setCellRenderer(tabelaCenter);
        tblBoletos.getColumnModel().getColumn(5).setCellRenderer(tabelaCenter);
        tblBoletos.getColumnModel().getColumn(2).setCellRenderer(tabelaDireita);

    }

    public void adicionarBoleto() {
        String sql = "INSERT INTO BOLETO(IDNUMBOLETO, PARCELA, BANCO, VALOR, VENCIMENTO, DATA_PAG, STATUSBOLETO, FK_MATRICULA)VALUES(?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, null);
            pst.setString(2, 1 + "");

            String estados = String.valueOf(cbBancoBoleto.getSelectedItem());
            if (cbBancoBoleto.getSelectedIndex() != 0) {  // COMPARANDO SE O ITEM SELECIONADO NÃO É INDICE 0("SELECIONE"), SE NÃO FOR SALVA NO BANCO
                pst.setString(3, cbBancoBoleto.getSelectedItem().toString());
            }
            if (txtValorBoleto.getText() != null) {
                pst.setString(4, conversorPontuacao(txtValorBoleto.getText() + ""));
            }
            try {
                String data = txtVencimentoBoleto.getText() + "";
                if (comparaComDataAtual(data) != 1) {

                    pst.setString(5, transformaData(data) + "");
                } else {
                    JOptionPane.showMessageDialog(null, "Data anterior à data atual");
                }
            } catch (Exception erro) {
                JOptionPane.showMessageDialog(null, "Data Inválida.");
            }
            pst.setString(6, null);
            pst.setString(7, "PENDENTE");
            pst.setString(8, matricSocio + "");

            // Validação dos campos obrigatórios de acordo com o banco(not null).
            if ((txtValorBoleto.getText().isEmpty()) || (txtVencimentoBoleto.getText().isEmpty())
                    || (cbBancoBoleto.getSelectedIndex() == 0)) { // se os campos x, y , z estiverem vazios faça. 
                JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatórios.");

            } else {
                // A linha abaixo atualiza a tabela socio com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve de apoio ao entendimento da lógica

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Boleto Gerado com sucesso");

                    // Limpa os campos logo após ser salvo    
                    //txtQuantidadeGeraBoleto.setText(null);
                    txtValorBoleto.setText(null);
                    txtVencimentoBoleto.setText(null);
                    cbBancoBoleto.setSelectedIndex(0);
                }
            }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
        atualizaListaBoletos(matricSocio);
    }

    public void setarBoletoSelecionado(String idNumBoleto) {
        String sql = "SELECT * FROM BOLETO WHERE IDNUMBOLETO = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, idNumBoleto + "");
            rs = pst.executeQuery();

            if (rs.next()) {
                txtIdBoleto.setText(completarComZeros(rs.getString(1)));
                txtValorBoleto.setText(conversorPontuacaoApresentacaoTela(rs.getString(4)));
                cbBancoBoleto.setSelectedItem(rs.getString(3));
                txtVencimentoBoleto.setText(transformaPadraoData(rs.getString(5)));
                txtpagamentoBoleto.setText(transformaPadraoData(rs.getString(6)));
            }
            ehAlteracao = true;
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
    }

    public void alterarBoleto() {
        String sql = "UPDATE BOLETO SET BANCO = ?, VALOR = ?, VENCIMENTO = ?, DATA_PAG = ? WHERE IDNUMBOLETO = ?";
        try {
            pst = conexao.prepareStatement(sql);
            String estados = String.valueOf(cbBancoBoleto.getSelectedItem());
            if (cbBancoBoleto.getSelectedIndex() != 0) {  // COMPARANDO SE O ITEM SELECIONADO NÃO É INDICE 0("SELECIONE"), SE NÃO FOR SALVA NO BANCO
                pst.setString(1, cbBancoBoleto.getSelectedItem().toString());
            }
            if (txtValorBoleto.getText() != null) {
                pst.setString(2, conversorPontuacao(txtValorBoleto.getText() + ""));
            }
            try {
                String data = txtVencimentoBoleto.getText() + "";
                pst.setString(3, transformaData(data) + "");

                if ((txtpagamentoBoleto.getText()).equals("  /  /    ")) {
                    pst.setString(4, null);
                } else {
                    pst.setString(4, transformaData(txtpagamentoBoleto.getText()) + "");
                }

            } catch (Exception erro) {
                JOptionPane.showMessageDialog(null, "Data Inválida.");
            }

            pst.setString(5, txtIdBoleto.getText());

            // Validação dos campos obrigatórios de acordo com o banco(not null).
            if ((txtValorBoleto.getText().isEmpty()) || (txtVencimentoBoleto.getText().isEmpty())
                    || (cbBancoBoleto.getSelectedIndex() == 0)) { // se os campos x, y , z estiverem vazios faça. 
                JOptionPane.showMessageDialog(null, "Preencha todos os campos Obrigatórios.");
                setarBoletoSelecionado(txtIdBoleto.getText());
                tblBoletos.setEnabled(false);
                txtGerarNovoBoleto.setText("Alterar Boleto");
                lbPagoem.setEnabled(true);
            } else {
                // A linha abaixo atualiza a tabela socio com os dados do formulário
                // a estrutura abaixo é usada para confirmar a inserção dos dados na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve de apoio ao entendimento da lógica

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Boleto Alterado com sucesso");

                    // Limpa os campos logo após ser salvo    
                    //txtQuantidadeGeraBoleto.setText(null);
                    txtValorBoleto.setText(null);
                    txtVencimentoBoleto.setText(null);
                    cbBancoBoleto.setSelectedIndex(0);
                }
            }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }
        atualizaListaBoletos(matricSocio);
    }

    public void statusDifAtivo(String statusSocio) {
        if (statusSocio.equals("INATIVO")) {
            txtNomeSociodt.setForeground(Color.LIGHT_GRAY);
            txtCpfdt.setForeground(Color.LIGHT_GRAY);
            txtStatusdt.setForeground(Color.GRAY);
            txtEnderecodt.setForeground(Color.LIGHT_GRAY);
            txtBairrodt.setForeground(Color.LIGHT_GRAY);
            txtCidadedt.setForeground(Color.LIGHT_GRAY);
            txtDataNascimentodt.setForeground(Color.LIGHT_GRAY);
            txtEstadodt.setForeground(Color.LIGHT_GRAY);
            txtEmaildt.setForeground(Color.LIGHT_GRAY);
            txtDataIniciodt.setForeground(Color.LIGHT_GRAY);
            txtDataFimdt.setForeground(Color.LIGHT_GRAY);
            txtTelefonedt.setForeground(Color.LIGHT_GRAY);
            txtSexodt.setForeground(Color.LIGHT_GRAY);
            txtNumerodt.setForeground(Color.LIGHT_GRAY);
            txtMatriculadt.setForeground(Color.LIGHT_GRAY);
            tblBoletos.setEnabled(false);
            btnSalvarBoleto.setEnabled(false);
            btnEditBoleto.setEnabled(false);
            btnExcluirBoleto.setEnabled(false);
            btnImprimirBoleto.setEnabled(false);
        } else if (statusSocio.equals("ATIVO")) {
            txtNomeSociodt.setForeground(Color.black);
            txtCpfdt.setForeground(Color.black);
            txtStatusdt.setForeground(Color.blue);
            txtEnderecodt.setForeground(Color.DARK_GRAY);
            txtBairrodt.setForeground(Color.DARK_GRAY);
            txtCidadedt.setForeground(Color.DARK_GRAY);
            txtDataNascimentodt.setForeground(Color.DARK_GRAY);
            txtEstadodt.setForeground(Color.DARK_GRAY);
            txtEmaildt.setForeground(Color.BLUE);
            txtDataIniciodt.setForeground(Color.DARK_GRAY);
            txtDataFimdt.setForeground(Color.DARK_GRAY);
            txtTelefonedt.setForeground(Color.DARK_GRAY);
            txtSexodt.setForeground(Color.DARK_GRAY);
            txtNumerodt.setForeground(Color.DARK_GRAY);
            txtMatriculadt.setForeground(Color.DARK_GRAY);
            tblBoletos.setEnabled(true);
            btnSalvarBoleto.setEnabled(true);
        } else if (statusSocio.equals("BLOQUEADO")) {
            txtNomeSociodt.setForeground(Color.RED);
            txtCpfdt.setForeground(Color.RED);
            txtStatusdt.setForeground(Color.RED);
            txtEnderecodt.setForeground(Color.RED);
            txtBairrodt.setForeground(Color.RED);
            txtCidadedt.setForeground(Color.RED);
            txtDataNascimentodt.setForeground(Color.RED);
            txtEstadodt.setForeground(Color.RED);
            txtEmaildt.setForeground(Color.RED);
            txtDataIniciodt.setForeground(Color.RED);
            txtDataFimdt.setForeground(Color.RED);
            txtTelefonedt.setForeground(Color.RED);
            txtSexodt.setForeground(Color.RED);
            txtNumerodt.setForeground(Color.RED);
            txtMatriculadt.setForeground(Color.RED);
            // btnSalvarBoleto.setEnabled(false);
        }
    }
    public void setarStatusDeBoleto(String dataVencimento, String dataPagamento){
        
    }

    public LocalDate transformaData(String dataString) { // função que transforma uma String para um formato Date de banco de dados;
        if (dataString.equals("  /  /    ")) { // COMO A MASCARA DE DATA PASSA ESSAS BARRAS COMPARO COM A STRING DO PARAMETRO PARA VER SE A DATA ESTÁ VAZIA ENTÃO PASSO NULL
            // COMO QUANDO UM CAMPO É NULL MOSTRA A MENSAGEM DE PREENCHER TODOS OS CAMPOS ASSIM GARANTO QUE NÃO SEJA POSSÍVEL PASSAR UMA DATA INVALIDA
            return null;
        }
        String data = dataString;
        String[] dataSeparada = data.split("/");// separa a data em vertor de string a cada vez que encontra o simbolo
        LocalDate dateConvertida = LocalDate.of(Integer.parseInt(dataSeparada[2]), Integer.parseInt(dataSeparada[1]), Integer.parseInt(dataSeparada[0]));
        return dateConvertida;
    }

    public Date transformaDataParaTipoDate(String dataString) throws ParseException { // função que transforma uma String para um formato Date de banco de dados;

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date data = (Date) formato.parse(dataString);
        return data;

    }

    public String conversorPontuacao(String valor) {
        valor = valor.replace(".", "");
        valor = valor.replace(",", ".");
        return valor + "";
    }

    public String transformaPadraoData(String date) {
        if (date == null) {
            return null;
        }
        String data = date + "";
        String[] dataSeparada = data.split("-");
        String dataConvertida = dataSeparada[2] + "/" + dataSeparada[1] + "/" + dataSeparada[0];
        return dataConvertida;
    }

    public int comparaComDataAtual(String dataDigitada) { // SE DATA ANTERIOR A ATUAL RETORNA 1, SE IGUAL RETORNA 0, SE POSTERIOR A ATUAL -1;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date date = new java.util.Date();
        String dataAtual = dateFormat.format(date);

        return dataAtual.compareTo(dataDigitada);
    }

    public String completarComZeros(String valor) {
        String s = valor.trim();
        StringBuffer resp = new StringBuffer();
        int fim = 6 - s.length();
        for (int x = 0; x < fim; x++) {
            resp.append('0');
        }
        return resp + s;
    }

    public String retirarOsZeros(String valor) {
        String s = valor.trim();
        StringBuffer resp = new StringBuffer();

        for (int x = 0; x != 6; x++) {
            char c = valor.charAt(x);
            System.out.println(c);
            if (c != '0') {
                System.out.println("entro" + c);
                resp.append(c);
            }
        }
        return resp + "";
    }

    public String conversorPontuacaoApresentacaoTela(String valor) {
        valor = valor.replace(".", ",");
        // int count = 0;
        //for(int i=valor.length();i != 0;i--){
        //    char c = valor.charAt(i);
        //     count ++;
        //     if(c == ',' && count == 2 ){
        return valor;
        //   }
        //   }
        //     return valor+"0";
    }

    private void excluirBoleto() {
        int indiceLinha = tblBoletos.getSelectedRow();
        if (indiceLinha != -1) {
            System.out.println(tblBoletos.getValueAt(indiceLinha, 4));
            if (tblBoletos.getValueAt(indiceLinha, 4) != null || tblBoletos.getValueAt(indiceLinha, 4) == "  /  /    ") {
                JOptionPane.showMessageDialog(null, "O Boleto selecionado está 'PAGO' verifique se não há risco perder essa informação");
            }
            int confirma = JOptionPane.showConfirmDialog(null, "Certeza que deseja Excluir o boleto existente?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM BOLETO WHERE IDNUMBOLETO = ?";
                try {

                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, tblBoletos.getValueAt(indiceLinha, 0) + "");
                    pst.executeUpdate();
                    /// DEVO ANALISAR SE O BOLETO FOI PAGO..;..----------------------
                    txtValorBoleto.setText(null);
                    txtVencimentoBoleto.setText(null);
                    txtpagamentoBoleto.setText(null);
                    cbBancoBoleto.setSelectedIndex(0);
                    atualizaListaBoletos(matricSocio);
                    txtGerarNovoBoleto.setText("Gerar Novo Boleto");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione o Boleto que deseja excluir");
        }
    }

    // ================================ PARTE ONDE GERA BOLETO  =======================================================
    public File gerarBoleto(String idNumBoleto) throws ParseException {
        
        String sql = "SELECT * FROM BOLETO WHERE IDNUMBOLETO = ?";
        String valor = null;
        String vencimento = null;
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, idNumBoleto + "");
            rs = pst.executeQuery();
            if (rs.next()) {
                valor = rs.getString(4); // VALOR
                vencimento = transformaPadraoData(rs.getString(5)); // VENCIMENTO
            }
        } catch (HeadlessException | SQLException ErroSql) {
            JOptionPane.showMessageDialog(null, ErroSql);
        }

        /*
       * INFORMANDO DADOS SOBRE O CEDENTE. EMPRESA QUE RECEBERÁ O VALOR DO BOLETO
         */
        Cedente cedente = new Cedente("igreja catolica", "00.000.208/0001-00");

        /*
        * INFORMANDO DADOS SOBRE O SACADO. IDENTIFICAÇÃO DO SOCIOS
         */
        Sacado sacado = new Sacado(txtNomeSociodt.getText(), txtCpfdt.getText());

        Endereco enderecoSac = new Endereco();

        // Informando o endereço do sacado.
        enderecoSac.setUF(UnidadeFederativa.DESCONHECIDO);       // ESTADO
        enderecoSac.setLocalidade(txtCidadedt.getText());        //CIDADE
        enderecoSac.setCep(new CEP(null));                       //CEP
        enderecoSac.setBairro(txtBairrodt.getText());            //BAIRRO
        enderecoSac.setLogradouro(txtEnderecodt.getText());      //RUA
        enderecoSac.setNumero(txtNumerodt.getText());            //NUMERO
        sacado.addEndereco(enderecoSac);

        /*
       * INFORMANDO DADOS SOBRE O SACADOR AVALISTA.
         */
        // cria nome e o numero do cpf ou cnp
//       SacadorAvalista sacadorAvalista = new SacadorAvalista(null , "000.000.000-00");
//       // Informando o endereço do sacador avalista.
//       Endereco enderecoSacAval = new Endereco();
//       enderecoSacAval.setUF(/*UnidadeFederativa.DF*/null);
//       enderecoSacAval.setLocalidade(null);
//       enderecoSacAval.setCep(new CEP(null));
//       enderecoSacAval.setBairro(null);
//       enderecoSacAval.setLogradouro(null);
//       enderecoSacAval.setNumero(null);
//                sacadorAvalista.addEndereco(/*enderecoSacAval*/null);
        /*
       * INFORMANDO OS DADOS SOBRE O TÍTULO.
         */
        // Informando dados sobre a conta bancária do título.
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.BANCO_BRADESCO.create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(123456, "0"));
        contaBancaria.setCarteira(new Carteira(30));
        contaBancaria.setAgencia(new Agencia(1234, "1"));

        Titulo titulo = new Titulo(contaBancaria, sacado, cedente);
        titulo.setNumeroDoDocumento(idNumBoleto);
        titulo.setNossoNumero("99345678912");
        titulo.setDigitoDoNossoNumero("5");
        titulo.setValor(BigDecimal.valueOf(Float.parseFloat(valor)));
        titulo.setDataDoDocumento(new Date());

        titulo.setDataDoVencimento(Dates.parse(vencimento, "dd/MM/yyyy"));

//              titulo.setDataDoVencimento(transformaDataParaTipoDate(txtVencimentoBoleto.getText()));
        titulo.setTipoDeDocumento(TipoDeTitulo.DM_DUPLICATA_MERCANTIL);
        titulo.setAceite(EnumAceite.N);
        titulo.setDesconto(new BigDecimal(0.00));
        titulo.setDeducao(BigDecimal.ZERO);
        titulo.setMora(BigDecimal.ZERO);
        titulo.setAcrecimo(BigDecimal.ZERO);
        titulo.setValorCobrado(BigDecimal.ZERO);
        /*
      * INFORMANDO OS DADOS SOBRE O BOLETO.
         */
        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento("Pagável em qualquer"
                + "qualquer agência Bancária até o Vencimento.");

        /*
         * GERANDO O BOLETO BANCÁRIO.
         */
        // Instanciando um objeto "BoletoViewer", classe responsável pela
        // geração do boleto bancário.
        BoletoViewer boletoViewer = new BoletoViewer(boleto);

        // Gerando o arquivo. No caso o arquivo mencionado será salvo na mesma
        // pasta do projeto. Outros exemplos:
        // WINDOWS: boletoViewer.getAsPDF("C:/Temp/MeuBoleto.pdf");
        // LINUX: boletoViewer.getAsPDF("/home/temp/MeuBoleto.pdf");
        boletoViewer.getPdfAsFile("C:/xampp/Boleto.pdf");
        
        File arquivoPdf = boletoViewer.getPdfAsFile("Boleto.pdf");
        
        return arquivoPdf;
//        // Mostrando o boleto gerado na tela.
//        mostreBoletoNaTela(arquivoPdf);
//        /**
//         * Exibe o arquivo na tela.
//         *
//         * @param arquivoBoleto
//         */

    }

    private static void mostreBoletoNaTela(File arquivoBoleto) {

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        try {
            desktop.open(arquivoBoleto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void enviaBoletoEmail(String idNumBoleto) throws EmailException, ParseException{
                gerarBoleto(idNumBoleto);
                // cria o anexo 1.
                System.out.println("entrou no enviar...");
		EmailAttachment anexo1 = new EmailAttachment();
		anexo1.setPath("C:/xampp/Boleto.pdf"); //caminho do arquivo (RAIZ_PROJETO/teste/teste.txt)
		anexo1.setDisposition(EmailAttachment.ATTACHMENT);
		anexo1.setDescription("Boleto de Doação Igreja Católica");
		anexo1.setName("Boletotttt.pdf");		
			
		// configura o email
		MultiPartEmail email = new MultiPartEmail();
		email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
		email.addTo(txtEmaildt.getText(), txtNomeSociodt.getText()); //destinatário
		email.setFrom("ssankys3@gmail.com", "sam"); // remetente (o nome aparece no remetende do email.)
		email.setSubject("Boleto de Doação"); // assunto do e-mail
		email.setMsg("Segue Anexo Boleto de Doação.."); //conteudo do e-mail
		email.setAuthentication("ssankys3@gmail.com", "samuelsk8");
		email.setSmtpPort(465);
		email.setSSL(true);
		email.setTLS(true);
		// adiciona arquivo(s) anexo(s)
		email.attach(anexo1);
		
		// envia o email
		email.send();
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnEditSocio = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btnConcluir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtNomeSociodt = new javax.swing.JLabel();
        txtSexodt = new javax.swing.JLabel();
        txtCpfdt = new javax.swing.JLabel();
        txtDataNascimentodt = new javax.swing.JLabel();
        txtTelefonedt = new javax.swing.JLabel();
        txtEmaildt = new javax.swing.JLabel();
        txtDataIniciodt = new javax.swing.JLabel();
        txtDataFimdt = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtEnderecodt = new javax.swing.JLabel();
        txtNumerodt = new javax.swing.JLabel();
        txtBairrodt = new javax.swing.JLabel();
        txtEstadodt = new javax.swing.JLabel();
        txtCidadedt = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBoletos = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtMatriculadt = new javax.swing.JLabel();
        txtStatusdt = new javax.swing.JLabel();
        pGerarBoleto = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtGerarNovoBoleto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtVencimentoBoleto = new javax.swing.JFormattedTextField();
        txtValorBoleto = new javax.swing.JFormattedTextField();
        cbBancoBoleto = new javax.swing.JComboBox<>();
        btnSalvarBoleto = new javax.swing.JButton();
        lbPagoem = new javax.swing.JLabel();
        txtpagamentoBoleto = new javax.swing.JFormattedTextField();
        lbid = new javax.swing.JLabel();
        txtIdBoleto = new javax.swing.JLabel();
        btnImprimirBoleto = new javax.swing.JLabel();
        btnEditBoleto = new javax.swing.JLabel();
        btnExcluirBoleto = new javax.swing.JLabel();
        pbCarregando = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        txtCarregando = new javax.swing.JLabel();
        btnEnviarBoleto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Controle de Sócios - Home");
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
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
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
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
        jLabel6.setText("DETALHAMENTO DE SÓCIO");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/socio_branco.png"))); // NOI18N

        btnEditSocio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/edit24.png"))); // NOI18N
        btnEditSocio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditSocio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditSocioMouseClicked(evt);
            }
        });

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/lixo24br.png"))); // NOI18N

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/imprimir24br2.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(326, 326, 326)
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(59, 59, 59)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(49, 49, 49)
                .addComponent(btnEditSocio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(80, 80, 80))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditSocio)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel6))
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
        jLabel33.setText("Data de Nascimento:");

        txtNomeSociodt.setFont(new java.awt.Font("Futura Md BT", 0, 14)); // NOI18N
        txtNomeSociodt.setForeground(new java.awt.Color(46, 46, 46));
        txtNomeSociodt.setText("Nome ");
        txtNomeSociodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtSexodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtSexodt.setForeground(new java.awt.Color(46, 46, 46));
        txtSexodt.setText("Sexo");

        txtCpfdt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtCpfdt.setForeground(new java.awt.Color(46, 46, 46));
        txtCpfdt.setText("CPF");
        txtCpfdt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtDataNascimentodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtDataNascimentodt.setForeground(new java.awt.Color(46, 46, 46));
        txtDataNascimentodt.setText("Data de Nasc.");
        txtDataNascimentodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtTelefonedt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtTelefonedt.setForeground(new java.awt.Color(46, 46, 46));
        txtTelefonedt.setText("Telefone:");
        txtTelefonedt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtEmaildt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtEmaildt.setForeground(new java.awt.Color(82, 130, 151));
        txtEmaildt.setText("E-mail");
        txtEmaildt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtDataIniciodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtDataIniciodt.setForeground(new java.awt.Color(46, 46, 46));
        txtDataIniciodt.setText("DtInicio");
        txtDataIniciodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtDataFimdt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtDataFimdt.setForeground(new java.awt.Color(46, 46, 46));
        txtDataFimdt.setText("DtFim");
        txtDataFimdt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        jLabel41.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(46, 46, 46));
        jLabel41.setText("Estado:");

        jLabel42.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(46, 46, 46));
        jLabel42.setText("Nº:");

        jLabel43.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(46, 46, 46));
        jLabel43.setText("Bairro:");

        txtEnderecodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtEnderecodt.setForeground(new java.awt.Color(46, 46, 46));
        txtEnderecodt.setText("Endereço");
        txtEnderecodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtNumerodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtNumerodt.setForeground(new java.awt.Color(46, 46, 46));
        txtNumerodt.setText("Nº");
        txtNumerodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtBairrodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtBairrodt.setForeground(new java.awt.Color(46, 46, 46));
        txtBairrodt.setText("Bairro");
        txtBairrodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtEstadodt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtEstadodt.setForeground(new java.awt.Color(46, 46, 46));
        txtEstadodt.setText("Estado");
        txtEstadodt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        txtCidadedt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtCidadedt.setForeground(new java.awt.Color(46, 46, 46));
        txtCidadedt.setText("Cidade");
        txtCidadedt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        tblBoletos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblBoletos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBoletosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBoletos);

        jLabel2.setFont(new java.awt.Font("Futura Md BT", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(46, 46, 46));
        jLabel2.setText("Boletos Gerados:");

        jLabel22.setFont(new java.awt.Font("Futura Md BT", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(46, 46, 46));
        jLabel22.setText("Matricula:");

        txtMatriculadt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtMatriculadt.setForeground(new java.awt.Color(46, 46, 46));
        txtMatriculadt.setText("Matricula:");

        txtStatusdt.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtStatusdt.setForeground(new java.awt.Color(5, 8, 124));
        txtStatusdt.setText("Ativo");
        txtStatusdt.setAlignmentY(0.0F);

        jLabel5.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        jLabel5.setText("Valor:");

        txtGerarNovoBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 18)); // NOI18N
        txtGerarNovoBoleto.setForeground(new java.awt.Color(46, 46, 46));
        txtGerarNovoBoleto.setText("Gerar Novo Boleto");

        jLabel8.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        jLabel8.setText("Vencimento:");

        try {
            txtVencimentoBoleto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtValorBoleto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        txtValorBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorBoletoActionPerformed(evt);
            }
        });

        cbBancoBoleto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione o Banco", "CEF", "BRADESCO", "B. do BRASIL", "SANTANDER" }));
        cbBancoBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBancoBoletoActionPerformed(evt);
            }
        });

        btnSalvarBoleto.setText("Salvar");
        btnSalvarBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarBoletoActionPerformed(evt);
            }
        });

        lbPagoem.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        lbPagoem.setText("Pago Em:");

        try {
            txtpagamentoBoleto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        lbid.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        lbid.setText("Id:");

        txtIdBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 12)); // NOI18N
        txtIdBoleto.setText("Id:");

        javax.swing.GroupLayout pGerarBoletoLayout = new javax.swing.GroupLayout(pGerarBoleto);
        pGerarBoleto.setLayout(pGerarBoletoLayout);
        pGerarBoletoLayout.setHorizontalGroup(
            pGerarBoletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pGerarBoletoLayout.createSequentialGroup()
                .addGroup(pGerarBoletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pGerarBoletoLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(cbBancoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValorBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtVencimentoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pGerarBoletoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtGerarNovoBoleto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbid)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtIdBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbPagoem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtpagamentoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(btnSalvarBoleto)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        pGerarBoletoLayout.setVerticalGroup(
            pGerarBoletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pGerarBoletoLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(pGerarBoletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGerarNovoBoleto)
                    .addComponent(lbid)
                    .addComponent(txtIdBoleto)
                    .addComponent(lbPagoem, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpagamentoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pGerarBoletoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVencimentoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtValorBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbBancoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvarBoleto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnImprimirBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 11)); // NOI18N
        btnImprimirBoleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/imprimirpt.png"))); // NOI18N
        btnImprimirBoleto.setText("Imprimir Boleto");
        btnImprimirBoleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnImprimirBoleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImprimirBoletoMouseClicked(evt);
            }
        });
        btnImprimirBoleto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnImprimirBoletoKeyPressed(evt);
            }
        });

        btnEditBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 11)); // NOI18N
        btnEditBoleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/editpt.png"))); // NOI18N
        btnEditBoleto.setText("Editar Boleto");
        btnEditBoleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditBoleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditBoletoMouseClicked(evt);
            }
        });
        btnEditBoleto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEditBoletoKeyPressed(evt);
            }
        });

        btnExcluirBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 11)); // NOI18N
        btnExcluirBoleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/lixo16pt.png"))); // NOI18N
        btnExcluirBoleto.setText("Excluir Boleto");
        btnExcluirBoleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluirBoleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnExcluirBoletoMouseClicked(evt);
            }
        });

        pbCarregando.setStringPainted(true);

        jLabel3.setFont(new java.awt.Font("Futura Md BT", 1, 10)); // NOI18N

        txtCarregando.setFont(new java.awt.Font("Futura Md BT", 1, 10)); // NOI18N
        txtCarregando.setText("xxx");

        btnEnviarBoleto.setFont(new java.awt.Font("Futura Md BT", 0, 11)); // NOI18N
        btnEnviarBoleto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/socios/icones/Email16pt.png"))); // NOI18N
        btnEnviarBoleto.setText("Enviar Boleto");
        btnEnviarBoleto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEnviarBoleto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEnviarBoletoMouseClicked(evt);
            }
        });
        btnEnviarBoleto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEnviarBoletoKeyPressed(evt);
            }
        });

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
                            .addComponent(jLabel22)
                            .addComponent(jLabel19)
                            .addComponent(jLabel32)
                            .addComponent(jLabel20)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCpfdt)
                                    .addComponent(txtEmaildt, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTelefonedt)
                                    .addComponent(txtDataNascimentodt)
                                    .addComponent(txtSexodt))
                                .addGap(64, 64, 64)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addComponent(jLabel28)))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txtDataIniciodt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(65, 65, 65)
                                        .addComponent(txtDataFimdt, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(106, 106, 106))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(txtStatusdt)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtEnderecodt, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCidadedt, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel41)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtEstadodt, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jLabel42)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtNumerodt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel43)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtBairrodt, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtMatriculadt)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(pGerarBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(jLabel3))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCarregando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnConcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(pbCarregando, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(txtNomeSociodt, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnEditBoleto)
                                .addGap(61, 61, 61)
                                .addComponent(btnEnviarBoleto)
                                .addGap(48, 48, 48)
                                .addComponent(btnImprimirBoleto)
                                .addGap(57, 57, 57)
                                .addComponent(btnExcluirBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(117, 117, 117))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 892, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtMatriculadt))
                .addGap(8, 8, 8)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel30)
                            .addComponent(txtNomeSociodt)
                            .addComponent(txtSexodt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(txtCpfdt)
                            .addComponent(jLabel33)
                            .addComponent(txtDataNascimentodt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtEmaildt)
                            .addComponent(jLabel31)
                            .addComponent(txtTelefonedt)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtDataIniciodt)
                            .addComponent(txtDataFimdt))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(txtStatusdt))))
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtEnderecodt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(txtCidadedt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(btnImprimirBoleto)
                            .addComponent(btnEditBoleto)
                            .addComponent(btnExcluirBoleto)
                            .addComponent(btnEnviarBoleto)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel42)
                            .addComponent(jLabel43)
                            .addComponent(txtNumerodt)
                            .addComponent(txtBairrodt))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(txtEstadodt))
                        .addGap(51, 51, 51)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pGerarBoleto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(pbCarregando, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCarregando, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnConcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(981, 653));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        // DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Date date = new Date();
        //txtDTInicio.setText(dateFormat.format(date));
        txtIdBoleto.setVisible(false);
        lbid.setVisible(false);
        lbPagoem.setEnabled(false);
        txtpagamentoBoleto.setEnabled(false);
        txtCarregando.setVisible(false);
        pbCarregando.setVisible(false);
        
//       EnviarEmail enviar = new EnviarEmail();
//        try {
//            enviar.enviandoEmail();
//        } catch (EmailException ex) {
//            Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    
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

    private void txtValorBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorBoletoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorBoletoActionPerformed

    private void cbBancoBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBancoBoletoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbBancoBoletoActionPerformed

    private void btnEditSocioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditSocioMouseClicked
        // TODO add your handling code here:
        if (taAtivo) {
            taAtivo = false;
            btnEditSocio.setEnabled(false);
            TelaDetalheSocioEdit socioEdit = new TelaDetalheSocioEdit();
            socioEdit.setVisible(true);
            socioEdit.setarSocio(matricSocio);
            socioEdit.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent we) {

                }

                @Override
                public void windowClosing(WindowEvent we) {

                }

                @Override
                public void windowClosed(WindowEvent we) {
                    taAtivo = true;
                    btnEditSocio.setEnabled(true);
                    //socioEdit.alterarSocio(matricSocio);
                    setarSocio(matricSocio);
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
    }//GEN-LAST:event_btnEditSocioMouseClicked

    private void btnSalvarBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarBoletoActionPerformed
        // TODO add your handling code here:
        if (ehAlteracao == false) {
            adicionarBoleto();
        } else {
            alterarBoleto();
            ehAlteracao = false;
            tblBoletos.setEnabled(true);
            txtGerarNovoBoleto.setText("Gerar Novo Boleto");
            lbPagoem.setEnabled(false);
            txtIdBoleto.setVisible(false);
            lbid.setVisible(false);
            txtpagamentoBoleto.setText(null);

        }

    }//GEN-LAST:event_btnSalvarBoletoActionPerformed

    private void btnEditBoletoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEditBoletoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditBoletoKeyPressed

    private void btnEditBoletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditBoletoMouseClicked
        // TODO add your handling code here:
        int indiceLinha = tblBoletos.getSelectedRow();

        if (indiceLinha != -1) {
            //  System.out.println(completarComZeros(tblBoletos.getValueAt(indiceLinha, 0)+"")));
            setarBoletoSelecionado(completarComZeros(tblBoletos.getValueAt(indiceLinha, 0) + ""));
            //Integer.parseInt(tblBoletos.getValueAt(indiceLinha, 0).toString())

            tblBoletos.setEnabled(false);
            txtGerarNovoBoleto.setText("Alterar Boleto");
            lbPagoem.setEnabled(true);
            txtIdBoleto.setVisible(true);
            lbid.setVisible(true);
            lbPagoem.setEnabled(true);
            txtpagamentoBoleto.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Selecione o Boleto que deseja alterar");
        }
    }//GEN-LAST:event_btnEditBoletoMouseClicked

    private void tblBoletosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBoletosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            btnEditBoletoMouseClicked(evt);
        }
    }//GEN-LAST:event_tblBoletosMouseClicked

    private void btnExcluirBoletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirBoletoMouseClicked
        // TODO add your handling code here:
        excluirBoleto();
    }//GEN-LAST:event_btnExcluirBoletoMouseClicked

    private void btnImprimirBoletoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnImprimirBoletoKeyPressed

    }//GEN-LAST:event_btnImprimirBoletoKeyPressed

    private void btnImprimirBoletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImprimirBoletoMouseClicked
        // TODO add your handling code here:
        int indiceLinha = tblBoletos.getSelectedRow();

        if (indiceLinha != -1) {
             txtCarregando.setVisible(true);
             pbCarregando.setVisible(true);
            new Thread() {
                public void run() {
                    for (int i = 0; i < 101; i++) {
                        try {
                            sleep(8);
                            pbCarregando.setValue(i);
                            if (pbCarregando.getValue() <= 25) {
                                txtCarregando.setText("Aguarde...");
                            } else if (pbCarregando.getValue() <= 50) {
                                txtCarregando.setText("Carregando os dados...");
                            } else if (pbCarregando.getValue() <= 75) {
                                txtCarregando.setText("Criando PDF...");
                            } else if (pbCarregando.getValue() <= 100) {
                                txtCarregando.setText("Aguarde...Abrindo PDF");
                            }
                        } catch (InterruptedException e) {
                            Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    try {
                        mostreBoletoNaTela(gerarBoleto(tblBoletos.getValueAt(indiceLinha, 0) + ""));
                    } catch (ParseException ex) {
                        Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        } else {
            JOptionPane.showMessageDialog(null, "Selecione o Boleto que deseja imprimir");
        }
    }//GEN-LAST:event_btnImprimirBoletoMouseClicked

    private void btnConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConcluirActionPerformed
        // Método para adiciona socio
        // adicionar();
    }//GEN-LAST:event_btnConcluirActionPerformed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        // TODO add your handling code here:
         txtCarregando.setVisible(false);
        pbCarregando.setVisible(false);
    }//GEN-LAST:event_formWindowDeactivated

    private void btnEnviarBoletoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEnviarBoletoMouseClicked
        // TODO add your handling code here:
        int indiceLinha = tblBoletos.getSelectedRow();

        if (indiceLinha != -1) {
            
             txtCarregando.setVisible(true);
             pbCarregando.setVisible(true);
            new Thread() {
                public void run() {
                    for (int i = 0; i < 101; i++) {
                        try {
                            sleep(50);
                            pbCarregando.setValue(i);
                            if (pbCarregando.getValue() <= 25) {
                                txtCarregando.setText("Aguarde...Carregando os dados...");
                            } else if (pbCarregando.getValue() <= 50) {
                                txtCarregando.setText("Anexando PDF...");
                            } else if (pbCarregando.getValue() <= 75) {
                                txtCarregando.setText("Aguarde...Enviando Email...");
                            } else if (pbCarregando.getValue() <= 100) {
                                txtCarregando.setText("Email esta sendo encaminhado...");
                            }
                        } catch (InterruptedException e) {
                            Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                }
            }.start();
            new Thread() {
                public void run() {
                    try {
                        enviaBoletoEmail(tblBoletos.getValueAt(indiceLinha, 0) + "");
                    } catch (EmailException ex) {
                        Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(TelaDetalheSocio.class.getName()).log(Level.SEVERE, null, ex);
                    }    
                }
            }.start();
        } else {
            JOptionPane.showMessageDialog(null, "Selecione o Boleto que deseja enviar");
        }
            
    }//GEN-LAST:event_btnEnviarBoletoMouseClicked

    private void btnEnviarBoletoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEnviarBoletoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEnviarBoletoKeyPressed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        if(pbCarregando.getValue() == 100){
            txtCarregando.setVisible(false);
            pbCarregando.setVisible(false);
        }
    }//GEN-LAST:event_formMouseClicked

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
            java.util.logging.Logger.getLogger(TelaDetalheSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaDetalheSocio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new TelaDetalheSocio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnConcluir;
    private javax.swing.JLabel btnEditBoleto;
    private javax.swing.JLabel btnEditSocio;
    private javax.swing.JLabel btnEnviarBoleto;
    private javax.swing.JLabel btnExcluirBoleto;
    private javax.swing.JLabel btnImprimirBoleto;
    private javax.swing.JButton btnSalvarBoleto;
    private javax.swing.ButtonGroup btngroupSexo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbBancoBoleto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbPagoem;
    private javax.swing.JLabel lbid;
    private javax.swing.JPanel pGerarBoleto;
    private javax.swing.JProgressBar pbCarregando;
    private javax.swing.JTable tblBoletos;
    private javax.swing.JLabel txtBairrodt;
    private javax.swing.JLabel txtCarregando;
    private javax.swing.JLabel txtCidadedt;
    private javax.swing.JLabel txtCpfdt;
    private javax.swing.JLabel txtDataFimdt;
    private javax.swing.JLabel txtDataIniciodt;
    private javax.swing.JLabel txtDataNascimentodt;
    private javax.swing.JLabel txtEmaildt;
    private javax.swing.JLabel txtEnderecodt;
    private javax.swing.JLabel txtEstadodt;
    private javax.swing.JLabel txtGerarNovoBoleto;
    private javax.swing.JLabel txtIdBoleto;
    private javax.swing.JLabel txtMatriculadt;
    private javax.swing.JLabel txtNomeSociodt;
    private javax.swing.JLabel txtNumerodt;
    private javax.swing.JLabel txtSexodt;
    private javax.swing.JLabel txtStatusdt;
    private javax.swing.JLabel txtTelefonedt;
    private javax.swing.JFormattedTextField txtValorBoleto;
    private javax.swing.JFormattedTextField txtVencimentoBoleto;
    private javax.swing.JFormattedTextField txtpagamentoBoleto;
    // End of variables declaration//GEN-END:variables

}
