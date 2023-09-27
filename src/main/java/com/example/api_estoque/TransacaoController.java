package com.example.api_estoque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transacoes")
public class TransacaoController {
    private final TransacaoRepository transacaoRepository;

    @Autowired
    public TransacaoController(TransacaoRepository produtoRepository) {
        this.transacaoRepository = produtoRepository;
    }

    @GetMapping("/selecionar")
    public List<Transacao> listarTransacoes(){
        return transacaoRepository.findAll();
    }

    @GetMapping("/selecionarPorTipo/{tipo}")
    public List<Transacao> listarTransacoesPorTipo(@PathVariable String tipo){
        List<Transacao> listaOriginal = transacaoRepository.findAll();
        List<Transacao> listaRetorno = new ArrayList<>();;

        for (Transacao x: listaOriginal) {
            if(x.getTipo_transacao().equals(tipo)){
                listaRetorno.add(x);
            }
        }

//        Transacao transacaoTeste = new Transacao("TESTE", 45.78, new Date(), "PIX");
//        listaOriginal.add(transacaoTeste);
        return listaRetorno;
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id){
        transacaoRepository.deleteById(id);
        return ResponseEntity.ok("Transação excluída com sucesso!");
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id, @RequestBody Transacao transacaoAtualizada){
        Optional<Transacao> transacaoExistente = transacaoRepository.findById(id);

        if(transacaoExistente.isPresent()){
            Transacao transacao = transacaoExistente.get();
            transacao.setDescricao(transacaoAtualizada.getDescricao());
            transacao.setValor(transacaoAtualizada.getValor());
            transacao.setTipo_transacao(transacaoAtualizada.getTipo_transacao());

            transacaoRepository.save(transacao);

            return ResponseEntity.ok("Produto atualizado com sucesso");
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto(@RequestBody Transacao objeto){
        Transacao novaTransacao = new Transacao(objeto.getDescricao(), objeto.getValor(), new Date(), objeto.getTipo_transacao());
        try{
            transacaoRepository.save(novaTransacao);
            return ResponseEntity.ok("Produto inserido com sucesso");
        } catch (DataAccessException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir o produto");
        }
    }
}
