package lol.aabss.skhttp.elements.server.expressions.exchange;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import lol.aabss.skhttp.objects.server.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("HTTP Protocol")
@Description("Gets the protocol of the http exchange.")
@Examples({
        "send http protocol of {_exchange}"
})
@Since("1.3")
public class ExprExchangeProtocol extends SimplePropertyExpression<HttpExchange, String> {

    static {
        register(ExprExchangeProtocol.class, String.class, "[http[s]] protocol", "httpexchanges");
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "protocol";
    }

    @Override
    public @Nullable String convert(HttpExchange httpExchange) {
        return httpExchange.protocol();
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }
}
