package br.mg.jcls.crud.services;

import br.mg.jcls.crud.data.vo.ProdutoVO;
import br.mg.jcls.crud.entity.Produto;
import br.mg.jcls.crud.message.ProdutoSendMessage;
import br.mg.jcls.crud.repository.ProdutoRepository;
import br.mg.jcls.crud.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    public static final String NO_RECORD_FOUND_FOR_THIS_ID = "No record found for this ID";
    private final ProdutoRepository produtoRepository;
    private final ProdutoSendMessage produtoSendMessage;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
        this.produtoRepository = produtoRepository;
        this.produtoSendMessage = produtoSendMessage;
    }

    public ProdutoVO create(ProdutoVO produtoVO) {
        var produto = ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
        produtoSendMessage.sendMessage(produto);
        return produto;
    }

    public Page<ProdutoVO> findAll(Pageable pageable) {
        var page = produtoRepository.findAll(pageable);
        return page.map(this::convertToProdutoVO);
    }

    public ProdutoVO findById(Long id) {
        var entity = produtoRepository.findById(id)
                .orElseThrow( () ->  new ResourceNotFoundException(NO_RECORD_FOUND_FOR_THIS_ID));
        return ProdutoVO.create(entity);
    }

    public ProdutoVO update(ProdutoVO produtoVO) {
        final var entityOptional = produtoRepository.findById(produtoVO.getId());

        if (!entityOptional.isPresent()) {
            throw new ResourceNotFoundException(NO_RECORD_FOUND_FOR_THIS_ID);
        }

        return ProdutoVO.create(produtoRepository.save(Produto.create(produtoVO)));
    }

    public void delete(Long id) {
        var entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NO_RECORD_FOUND_FOR_THIS_ID));
        produtoRepository.delete(entity);
    }

    private ProdutoVO convertToProdutoVO(Produto produto) {
        return ProdutoVO.create(produto);
    }
}