package br.com.donza.donzfoodz.repository;

import br.com.donza.donzfoodz.entity.GravaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GravaPedidoRepository extends JpaRepository<GravaPedido, Long> {
}