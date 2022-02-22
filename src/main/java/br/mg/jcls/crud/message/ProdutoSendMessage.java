package br.mg.jcls.crud.message;

import br.mg.jcls.crud.data.vo.ProdutoVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProdutoSendMessage {

    @Value("${crud.rabbitmq.exchange}")
    String exchange;

    @Value("${crud.rabbitmq.routingkey}")
    String routingkey;

    public final RabbitTemplate template;

    @Autowired
    public ProdutoSendMessage(RabbitTemplate template) {
        this.template = template;
    }

    public void sendMessage(ProdutoVO produtoVO) {
        template.convertAndSend(exchange, routingkey, produtoVO);
    }
}
