package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.entity.GravaPedido;
import br.com.donza.donzfoodz.repository.GravaPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GravaPedidoService {

    private final GravaPedidoRepository gravaPedidoRepository;
    private static final Logger logger = LoggerFactory.getLogger(GravaPedidoService.class);
    public GravaPedidoService(GravaPedidoRepository gravaPedidoRepository) {
        this.gravaPedidoRepository = gravaPedidoRepository;
    }

    public void salvarPedido(String numeroPedido) {
        GravaPedido gravaPedido = new GravaPedido();
        gravaPedido.setMessage(numeroPedido);
        gravaPedidoRepository.save(gravaPedido);
        logger.info("Pedido salvo no banco: {}", numeroPedido);
    }
}