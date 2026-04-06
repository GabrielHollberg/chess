package handler;

import io.javalin.websocket.*;

import java.util.function.Consumer;

public class WSHandler implements Consumer<WsConfig> {

    @Override
    public void accept(WsConfig ws) {
        ws.onConnect(ctx -> {
            ctx.enableAutomaticPings();
            System.out.println("Websocket connected");
        });
        ws.onMessage(ctx -> ctx.send(ctx.message()));
        ws.onClose(ctx -> System.out.println("Websocket closed"));
    }
}