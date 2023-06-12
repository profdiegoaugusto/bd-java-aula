import models.Departamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class Main {

    static final String DRIVER_BD = "com.mysql.cj.jdbc.Driver";
    static final String URL_BD = "jdbc:mysql://localhost:3306/rh";
    static final String USUARIO_BD = "root";
    static final String SENHA_BD = ""; // Coloque aqui a senha do banco de dados.

    /**
     * Registra o driver MySql para conexão com o SGBD.
     * Somente após o registro no método main, podemos
     * estabelecer a conexão com o banco de dados MySQL.
     * 
     * @see <a href="https://github.com/mysql/mysql-connector-j">Repositório MySQL
     *      Connector/J</a>
     */
    static void carregarDriverMySql() {

        try {

            Class.forName(DRIVER_BD);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * Estabelece uma conexão com o banco de dados.
     * 
     * @return um objeto {@link Connection} que representa uma conexão com o SGBD
     * @throws SQLException se houver um erro ou falha de conexão
     */
    static Connection criarConexao() throws SQLException {

        Connection conexao = null;
        Properties propriedadesDaConexao = new Properties();

        propriedadesDaConexao.put("user", USUARIO_BD);
        propriedadesDaConexao.put("password", SENHA_BD);

        try {

            conexao = DriverManager.getConnection(URL_BD, propriedadesDaConexao);

        } catch (SQLException e) {

            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("VendorError: " + e.getErrorCode());

        }
        return conexao;
    }

    /**
     * Adiciona um novo registro no banco de dados
     * 
     * @param departamento o novo produto que será adicionado
     * @return o id do cliente recém-criado ou -1 em caso de erro
     */
    static int inserirDepartamento(Departamento departamento) {

        int idDepartamento = -1;
        String query = "INSERT INTO Departamento(nome_departamento, descricao) VALUES (?, ?);";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            comando.setString(1, departamento.getNome());
            comando.setString(2, departamento.getDescricao());

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1) {

                ResultSet resultado = comando.getGeneratedKeys();

                while (resultado.next())
                    idDepartamento = resultado.getInt(1);

            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return idDepartamento;

    } // Fim do método inserirDepartamento()

    /**
     * Seleciona um departamento do banco de dados através do seu ID
     * 
     * @param id chave primária do departamento
     * @return o objeto do tipo Departamento encontrado, ou null se não for
     *         encontrado ou em caso de erro
     */
    static Departamento buscarDepartamento(int id) {

        Departamento departamento = null;

        String query = "SELECT * FROM Departamento WHERE id_departamento = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);
            comando.setInt(1, id);

            ResultSet resultado = comando.executeQuery();

            while (resultado.next()) {

                int idDepartamento = resultado.getInt("id_departamento");
                String nomeDepartamento = resultado.getString("nome_departamento");
                String descricao = resultado.getString("descricao");

                departamento = new Departamento(idDepartamento, nomeDepartamento, descricao);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return departamento;
    }

    /**
     * Obtém todos os produtos
     * 
     * @return um objeto do Produto encontrado, ou null se não encontrado ou em caso
     *         de erro
     */
    static ArrayList<Departamento> buscarTodosDepartamentos() {

        ArrayList<Departamento> departamentos = new ArrayList<Departamento>();
        String query = "SELECT * FROM Departamento;";

        try (Connection conexao = criarConexao()) {

            Statement comando = conexao.createStatement();
            ResultSet resultado = comando.executeQuery(query);

            while (resultado.next()) {

                int id = resultado.getInt("id_departamento");
                String nome = resultado.getString("nome_departamento");
                String descricao = resultado.getString("descricao");

                departamentos.add(new Departamento(id, nome, descricao));

            }

        } catch (SQLException e) {

            System.err.println("SQLException: " + e.getMessage());

        }

        return departamentos;
    }

    /**
     * Atualiza um departamento no banco de dados
     * 
     * @param departamento departamento com dados desatualizados
     * @return true em caso de sucesso ou false em caso de falha ou erro
     */
    static boolean atualizarDepartamento(Departamento departamento) {

        boolean departamentoAtualizado = false;

        String query = "UPDATE Departamento SET nome_departamento = ?, descricao = ? WHERE id_departamento = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);

            comando.setString(1, departamento.getNome());
            comando.setString(2, departamento.getDescricao());
            comando.setInt(3, departamento.getId());

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1)
                departamentoAtualizado = true;

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        }

        return departamentoAtualizado;
    }

    /**
     * Apaga um registro no banco de dados
     * 
     * @param id chave primária do Departamento
     * @return true em caso de sucesso ou false em caso de falha
     */
    static boolean excluirDepartamento(int id) {

        boolean excluido = false;
        String query = "DELETE FROM Departamento WHERE id_departamento = ?;";

        try (Connection conexao = criarConexao()) {

            PreparedStatement comando = conexao.prepareStatement(query);
            comando.setLong(1, id);

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas == 1)
                excluido = true;

        } catch (SQLException e) {

            System.err.println(e.getMessage());

        }

        return excluido;

    } // Fim do método excluirDepartamento()

    /**
     * Exibe na tela informações de um departamento
     * 
     * @param d
     */
    static void mostrarDepartamento(Departamento d) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("ID: %d\n", d.getId()));
        sb.append(String.format("Nome: %s\n", d.getNome()));
        sb.append(String.format("Descrição: %s\n", d.getDescricao()));

        System.out.println(sb.toString());

    } // Fim do método mostrarDepartamento

    /**
     * Exibe na tela todas as informações de departamentos
     * 
     * @param departamentos
     */
    static void mostrarTodosDepartamentos(ArrayList<Departamento> departamentos) {

        System.out.println();

        for (Departamento d : departamentos)
            mostrarDepartamento(d);

    }

    public static void main(String[] args) {

        carregarDriverMySql();

        Departamento vendas = new Departamento(1, "Vendas", "Responsável pela venda de produtos");
        Departamento rh = new Departamento(2, "RH", "Responsável pela gestão de recursos humanos");
        Departamento marketing = new Departamento(3, "Marketing", "Responsável pela promoção e divulgação da empresa");
        Departamento financeiro = new Departamento(4, "Financeiro", "Responsável pela gestão financeira da empresa");
        Departamento ti = new Departamento("TI", "Responsável pela infraestrutura tecnológica e suporte técnico");
        Departamento logistica = new Departamento("Logística", "Responsável pelo planejamento e execução das operações logísticas");
        Departamento ped = new Departamento("Pesquisa e Desenvolvimento", "Responsável pela inovação e criação de novos produtos");

        /**********************************************************************************
         * TESTANDO O CRUD *
         * CRUD são as quatro operações básicas utilizadas em bancos de dados
         * relacionais *
         * C — Create • R — READ • U — UPDATE • D — DELETE *
         **********************************************************************************/

        // CREATE: vamos cadastrar, adicionar, inserir ou salvar um registro no banco de
        // dados.
        // Após adicionar registros, comente as linhas para não criar linhas duplicadas
        // (repetidas)

        // READ: vamos Ler, recuperar ou selecionar os dados no banco de dados.

        // UPDATE: vamos atualizar, modificar ou alterar um registro no banco de dados.
        // Suponha que o departamento de Vendas vai se unificar com o Marketing se
        // tornando Marketing e Vendas

        // DELETE: vamos excluir, remover ou apagar um registro no banco de dados.
        // Após atualizar o departamento de marketing vamos apagar o departamento de
        // vendas porque ele se unificou com marketing

    } // Fim do método main

} // Fim da classe Main