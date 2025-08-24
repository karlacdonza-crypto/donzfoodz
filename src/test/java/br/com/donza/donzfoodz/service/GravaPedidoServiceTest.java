package br.com.donza.donzfoodz.service;

import br.com.donza.donzfoodz.entity.GravaPedido;
import br.com.donza.donzfoodz.repository.GravaPedidoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GravaPedidoServiceTest {

    private final GravaPedidoRepository gravaPedidoRepository = mock(GravaPedidoRepository.class);
    private final GravaPedidoService gravaPedidoService = new GravaPedidoService(gravaPedidoRepository);

    @Test
    void deveSalvarPedidoComSucesso() {
        String numeroPedido = "12345";

        gravaPedidoService.salvarPedido(numeroPedido);

        ArgumentCaptor<GravaPedido> captor = ArgumentCaptor.forClass(GravaPedido.class);
        verify(gravaPedidoRepository, times(1)).save(captor.capture());
        assertEquals(numeroPedido, captor.getValue().getMessage());
    }
}